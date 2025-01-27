/*
 * Copyright (C) 2024 Clover Network, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clover.sdk.internal.util;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * For internal use only.
 * <p>
 * <b>
 * Because of the context where this class is used, it must not have any dependencies on
 * other libraries outside of the Android SDK. It must compile to language level 7. This means,
 * for example, it cannot use lambdas, method references, or try-with-resources.
 * </b>
 * <p>
 * Using this class to communicate with providers in other processes ensures that, if the provider
 * process dies, your process will not die with it. This class also automatically retries for
 * several seconds any operations that fail due to the provider process not being available, such
 * as when the provider process has crashed or is being reinstalled.
 * <p>
 * Unlike the standard {@link ContentProviderClient}, this class does not require you to
 * release/close instances you obtain, instead this class acquires and releases the internal
 * instance of {@link ContentProviderClient} for each method invocation.
 * <p>
 * Methods in this class eat all usage exceptions and return null (or the default value as
 * with the {@link #call(String, String, Bundle, Bundle)} method) if the provider is not available.
 * This behavior can be changed by setting the {@link #noDefault} flag to true. When true,
 * the exception that occurred on the last retry attmpt is thrown, wrapped in a
 * {@link RuntimeException}.
 */
public class UnstableContentResolverClient {

  private static final String TAG = "UnstableResolverClient";

  // VisibleForTesting
  static final int MAX_RETRY_ATTEMPTS = 8;
  // VisibleForTesting
  static long RETRY_DELAY_MS = 500;

  /**
   * List of exception classes that, if they are thrown during execution, are logged at INFO level.
   * Otherwise, they are logged at ERROR level.
   */
  private static final List<Class<? extends Throwable>> INFO_EXCEPTIONS =
      new ArrayList<Class<? extends Throwable>>() {{
        add(InterruptedException.class);
      }};

  private final ContentResolver contentResolver;
  private final Uri uri;

  /**
   * Do not log UnsupportedOperationExceptions when true.
   */
  public boolean quiet = false;

  /**
   * Never return the default value, instead throw a RuntimeException with the last exception cause.
   */
  public boolean noDefault = false;

  public UnstableContentResolverClient(ContentResolver contentResolver, Uri uri) {
    this.contentResolver = contentResolver;
    this.uri = uri;
  }

  private <T> T makeUnstableCall(ContentProviderClientCallable<T> callable) {
    return makeUnstableCall(callable, null);
  }

  private <T> T makeUnstableCall(ContentProviderClientCallable<T> callable, T defaultValue) {
    Throwable savedException = null;

    for (int retryCount = 0; retryCount < MAX_RETRY_ATTEMPTS; retryCount++) {

      // Always use a fresh client when retrying, per javadocs:
      // In particular, catching a {@link android.os.DeadObjectException} from the calls there
      // will let you know that the content provider has gone away; at that point the current
      // ContentProviderClient object is invalid, and you should release it.
      ContentProviderClient client = null;
      try {
        if (Thread.currentThread().isInterrupted()) {
          throw new InterruptedException("Interrupted for URI: " + uri);
        }

        if (retryCount > 1) {
          Thread.sleep(RETRY_DELAY_MS);
        }

        client = contentResolver.acquireUnstableContentProviderClient(uri);
        if (client == null) {
          savedException = new Exception("Client is null for URI: " + uri).fillInStackTrace();
          continue;
        }
        return callable.call(client);
      } catch (InterruptedException e) {
        savedException = e;

        // Reset interrupted flag
        Thread.currentThread().interrupt();

        // Break out of loop; we don't want to keep retrying if we were interrupted
        break;
      } catch (RemoteException e) {
        savedException = e;
      } catch (IllegalStateException e) {
        savedException = e;

        // on JellyBean MR1 and earlier this occurs due to an Android bug
        //        Two REMOVE_PROVIDER messages caused by race condition.
        //
        //        Fix a bug in unstable ContentProvider.
        //        IllegalStateException: ref counts can't go to zero here: stable=0 unstable=0
        //        IllegalStateException: unstable count < 0: -1
        // commit 9e3e5266506cb6817ea676e02d3099a7c44855f3 to frameworks/base
        // https://android.googlesource.com/platform/frameworks/base/+/9e3e5266506cb6817ea676e02d3099a7c44855f3
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR1) {
          continue;
        }

        // No retry, fail now
        break;
      } catch (Exception e) {
        savedException = e;

        if (!noDefault &&
            quiet &&
            UnsupportedOperationException.class.isAssignableFrom(e.getClass())) {
          // Don't log, don't retry, we expected this
          return defaultValue;
        }
        break;
      } finally {
        if (client != null) {
          client.release();
        }
      }
    }

    // Operation failed

    if (noDefault) {
      throw new RuntimeException(savedException);
    }

    if (isExpectedException(savedException)) {
      Log.i(TAG, String.format(Locale.ROOT, "Communication with URI: %s failed: %s", uri, savedException));
    } else {
      Log.e(TAG, String.format(Locale.ROOT, "Communication with URI: %s failed", uri), savedException);
    }

    return defaultValue;
  }

  /**
   * Perform {@link android.content.ContentProvider#insert(Uri, ContentValues)} while
   * allowing for a provider that may not be available momentarily.
   * <p>
   * Note this implementation eats all exceptions, including ones resulting from improper usage
   * of the provider such as SecurityException, IllegalArgumentException, etc. See full list at
   * {@link android.os.Parcel#writeException(Exception)}. Watch logs accordingly for errors.
   *
   * @return The result (may be null) upon a successful invocation of insert, or null
   *          if the content provider wasn't available even after waiting a few seconds or the
   *          insert generated any other type of exception
   */
  public Uri insert(final ContentValues values) {
    return makeUnstableCall(new ContentProviderClientCallable<Uri>() {
      @Override
      public Uri call(ContentProviderClient client) throws RemoteException {
        return client.insert(uri, values);
      }
    });
  }

  /**
   * Perform {@link android.content.ContentProvider#update(Uri, ContentValues, String, String[])}
   * while allowing for a provider that may not be available momentarily.
   * <p>
   * Note this implementation eats all exceptions, including ones resulting from improper usage
   * of the provider such as SecurityException, IllegalArgumentException, etc. See full list at
   * {@link android.os.Parcel#writeException(Exception)}. Watch logs accordingly for errors.
   *
   * @return The number of rows updated (may be 0) upon a successful invocation of update, or 0
   *          if the content provider wasn't available even after waiting a few seconds or the
   *          update generated any other type of exception
   */
  public int update(final ContentValues values, final String selection, final String[] selectionArgs) {
    Integer numRowsChanged = makeUnstableCall(new ContentProviderClientCallable<Integer>() {
      @Override
      public Integer call(ContentProviderClient client) throws RemoteException {
        return client.update(uri, values, selection, selectionArgs);
      }
    });
    return numRowsChanged != null ? numRowsChanged : 0;
  }

  /**
   * Perform {@link android.content.ContentProvider#delete(Uri, String, String[])} while
   * allowing for a provider that may not be available momentarily.
   * <p>
   * Note this implementation eats all exceptions, including ones resulting from improper usage
   * of the provider such as SecurityException, IllegalArgumentException, etc. See full list at
   * {@link android.os.Parcel#writeException(Exception)}. Watch logs accordingly for errors.
   *
   * @return The number of rows deleted (may be 0) upon a successful invocation of delete, or 0
   *          if the content provider wasn't available even after waiting a few seconds or the
   *          delete generated any other type of exception
   */
  public int delete(final String selection, final String[] selectionArgs) {
    Integer numRowsDeleted = makeUnstableCall(new ContentProviderClientCallable<Integer>() {
      @Override
      public Integer call(ContentProviderClient client) throws RemoteException {
        return client.delete(uri, selection, selectionArgs);
      }
    });
    return numRowsDeleted != null ? numRowsDeleted : 0;
  }

  /**
   * Perform {@link android.content.ContentProvider#query(Uri, String[], String, String[], String)}
   * while allowing for a provider that may not be available momentarily.
   * <p>
   * Note this implementation eats all exceptions, including ones resulting from improper usage
   * of the provider such as SecurityException, IllegalArgumentException, etc. See full list at
   * {@link android.os.Parcel#writeException(Exception)}. Watch logs accordingly for errors.
   * <p>
   * The caller must close the cursor when it is no longer needed.
   *
   * @return The Cursor (may be null) upon a successful invocation of query, or null
   *          if the content provider wasn't available even after retrying a few seconds or the
   *          query generated any other type of exception
   */
  public Cursor query(
      final String[] projection,
      final String selection,
      final String[] selectionArgs,
      final String sortOrder
  ) {
    return makeUnstableCall(new ContentProviderClientCallable<Cursor>() {
      @Override
      public Cursor call(ContentProviderClient client) throws RemoteException {
        return client.query(uri, projection, selection, selectionArgs, sortOrder);
      }
    });
  }

  /**
   * Perform {@link android.content.ContentProvider#call(String, String, String, Bundle)} while
   * allowing for a provider that may not be available momentarily.
   * <p>
   * Note this implementation eats all exceptions, including ones resulting from improper usage
   * of the provider such as SecurityException, IllegalArgumentException, etc. See full list at
   * {@link android.os.Parcel#writeException(Exception)}. Watch logs accordingly for errors.
   *
   * @return The call result (may be null) upon a successful invocation of call, or defaultResult
   *          if the content provider wasn't available even after waiting a few seconds or the call
   *          generated any other type of exception
   */
  public Bundle call(
      final String method,
      final String arg,
      final Bundle extras,
      final Bundle defaultResult
  ) {
    return makeUnstableCall(new ContentProviderClientCallable<Bundle>() {
      @Override
      public Bundle call(ContentProviderClient client) throws RemoteException {
        return client.call(method, arg, extras);
      }
    }, defaultResult);
  }

  /**
   * Perform {@link ContentResolver#openAssetFileDescriptor(Uri, String)} while
   * allowing for a provider that may not be available momentarily.
   * <p>
   * Note this implementation eats all exceptions, including ones resulting from improper usage
   * of the provider such as SecurityException, IllegalArgumentException, etc. See full list at
   * {@link android.os.Parcel#writeException(Exception)}. Watch logs accordingly for errors.
   *
   * @return The result (may be null) upon a successful invocation, or null
   *          if the content provider wasn't available even after waiting
   *          a few seconds or the invocation generated any other type of exception.
   *
   * @see ContentResolver#openAssetFileDescriptor(Uri, String)
   */
  public AssetFileDescriptor openAssetFile(final String mode) {
    return makeUnstableCall(new ContentProviderClientCallable<AssetFileDescriptor>() {
      @Override
      public AssetFileDescriptor call(ContentProviderClient client) throws RemoteException, FileNotFoundException {
        return client.openAssetFile(uri, mode);
      }
    });
  }

  /**
   * Perform {@link android.content.ContentProvider#openFile(Uri, String)} while
   * allowing for a provider that may not be available momentarily.
   *
   * @return The input stream (may be null) upon a successful invocation of openFile, or defaultResult
   *          if the content provider wasn't available even after waiting a few seconds or the call
   *          generated any other type of exception
   */
  public InputStream openInputStream(final String mode) {
    return makeUnstableCall(new ContentProviderClientCallable<InputStream>() {
      @Override
      public InputStream call(ContentProviderClient client) throws RemoteException, FileNotFoundException {
        ParcelFileDescriptor pfd = client.openFile(uri, mode);
        return pfd != null ? new ParcelFileDescriptor.AutoCloseInputStream(pfd) : null;
      }
    });
  }

  public boolean isExpectedException(Throwable e) {
    for (Class<? extends Throwable> exception : INFO_EXCEPTIONS) {
      if (isAnyInstance(e, exception)) {
        return true;
      }
    }
    return false;
  }

  private static boolean isAnyInstance(Throwable e, Class<? extends Throwable> expected) {
    if (e == null) {
      return false;
    }
    if (expected.isInstance(e)) {
      return true;
    }
    return isAnyInstance(e.getCause(), expected);
  }

  private interface ContentProviderClientCallable<T> {
    T call(ContentProviderClient client) throws RemoteException, FileNotFoundException;
  }
}
