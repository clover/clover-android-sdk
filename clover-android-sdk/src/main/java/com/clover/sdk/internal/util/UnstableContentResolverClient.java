/*
 * Copyright (C) 2020 Clover Network, Inc.
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
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Locale;

/**
 * For internal use only.
 * <p>
 * Using this class to communicate with providers in other processes ensures that if the provider
 * process dies your process will not die with it. This class also automatically retries for several
 * seconds any operations that fail due to the provider process not being available, such as when
 * the provider process has crashed or is being reinstalled.
 * <p>
 * Unlike the standard ContentProviderClient this class does not require you to release/close
 * instances you obtain, instead this class acquires and releases the internal ContentProviderClient
 * for each method invocation.
 * <p>
 * Note that methods in this class eat all usage exceptions and return null (or the default value as
 * with the call method) instead.
 */
public class UnstableContentResolverClient {

  private static final String TAG = "UnstableResolverClient";

  private static final int MAX_RETRY_ATTEMPTS = 8;
  private static final long RETRY_DELAY_MS = 500;

  private final ContentResolver contentResolver;
  private final Uri uri;

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
      if (Thread.currentThread().isInterrupted()) {
        savedException = new InterruptedException("Retry interrupted for URI: " + uri).fillInStackTrace();
        break;
      }

      if (retryCount > 1) {
        try {
          Thread.sleep(RETRY_DELAY_MS);
        } catch (InterruptedException e) {
          // Reset interrupted flag
          Thread.currentThread().interrupt();

          savedException = new InterruptedException("Sleep interrupted").fillInStackTrace();
          break;
        }
      }

      // Always use a fresh client when retrying, per javadocs:
      // In particular, catching a {@link android.os.DeadObjectException} from the calls there
      // will let you know that the content provider has gone away; at that point the current
      // ContentProviderClient object is invalid, and you should release it.
      ContentProviderClient client = null;
      try {
        client = contentResolver.acquireUnstableContentProviderClient(uri);
        if (client == null) {
          savedException = new Exception("Client is null for URI: " + uri).fillInStackTrace();
          continue;
        }
        return callable.call(client);
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
        // No retry, fail now
        break;
      } finally {
        if (client != null) {
          client.release();
        }
      }
    }

    // Operation failed
    Log.e(TAG, String.format(Locale.ROOT, "Usage of provider %s failed", uri ), savedException);
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
  public Uri insert(ContentValues values) {
    Uri resultUri = makeUnstableCall((client) -> client.insert(uri, values));
    return resultUri;
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
  public int update(ContentValues values, String selection, String[] selectionArgs) {
    Integer numRowsChanged = makeUnstableCall((client) -> client.update(uri, values, selection, selectionArgs));
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
  public int delete(String selection, String[] selectionArgs) {
    Integer numRowsDeleted = makeUnstableCall((client) -> client.delete(uri, selection, selectionArgs));
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
  public Cursor query(String[] projection,
                      String selection, String[] selectionArgs,
                      String sortOrder) {
    return makeUnstableCall((client) -> client.query(uri, projection, selection, selectionArgs, sortOrder));
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
  public Bundle call(String method, String arg, Bundle extras, Bundle defaultResult) {
    return makeUnstableCall((client) -> client.call(method, arg, extras), defaultResult);
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
  public AssetFileDescriptor openAssetFile(String mode) {
    return makeUnstableCall((client) -> client.openAssetFile(uri, mode));
  }

  private interface ContentProviderClientCallable<T> {
    T call(ContentProviderClient client) throws RemoteException, FileNotFoundException;
  }

}
