/*
 * Copyright (C) 2019 Clover Network, Inc.
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
package com.clover.sdk;

import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * A {@link Parcelable} that transfers data over a pipe
 * created using {@link ParcelFileDescriptor#createPipe()}.
 * This circumvents the standard Android binder size limit allowing for passing objects
 * of any size, limited only by the heap space of the sender and receiver process.
 * <p/>
 * This class is not thread safe.
 * <p/>
 * To enable verbose logging:
 * <code>
 * adb shell setprop log.tag.FdParcelable VERBOSE
 * </code>
 * <p/>
 * There are two copies of this file, one in clover-android-sdk and one in
 * schema-tool, please keep them in sync.
 *
 * @param <V> Value object, per contract of {@link Parcel#writeValue(Object)}.
 */
public class FdParcelable<V> implements Parcelable {

  private static final String TAG = FdParcelable.class.getSimpleName();

  // Use up to 8 threads to write data.
  private static final ExecutorService exec = Executors.newFixedThreadPool(8);

  // Value and bytes are lazily created. That is, we delay parceling the value to bytes
  // until writeToParcel() is called, and we delay unparceling bytes until getValue() is called.
  //
  // If value is !null and bytes are null, then we have not parceled data yet
  // If value is null and bytes are !null, then we have not unparceled yet (no call to getValue())
  // If both are null, either we were passed a null value, or unparceling failed.

  private V value;
  private byte[] bytes;

  public FdParcelable(V value) {
    this.value = value;
    this.bytes = null;
  }

  FdParcelable(byte[] bytes) {
    this.value = null;
    this.bytes = bytes;
  }

  public FdParcelable(Parcel in) {
    if (Log.isLoggable(TAG, Log.VERBOSE)) {
      Log.v(TAG, this + ": unparceling...");
    }

    ParcelFileDescriptor readFd = in.readParcelable(getClass().getClassLoader());
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    if (readFd != null) {
      FileInputStream fis = new ParcelFileDescriptor.AutoCloseInputStream(readFd);

      byte[] b = new byte[16 * 1024];
      int n;

      try {
        while ((n = fis.read(b)) != -1) {
          out.write(b, 0, n);
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      } finally {
        try {
          fis.close();
          if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, String.format(Locale.US, "%s: closed read", this));
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    this.value = null;
    this.bytes = out.toByteArray();

    if (Log.isLoggable(TAG, Log.VERBOSE)) {
      Log.v(TAG, String.format(Locale.US, "%s: unparceled %d bytes", this, this.bytes.length));
    }
  }

  public static final Creator<FdParcelable> CREATOR = new Creator<FdParcelable>() {
    @Override
    public FdParcelable createFromParcel(Parcel in) {
      return new FdParcelable(in);
    }

    @Override
    public FdParcelable[] newArray(int size) {
      return new FdParcelable[size];
    }
  };

  public V getValue() {
    if (value == null) {
      value = unmarshall(bytes);
    }
    return value;
  }

  protected Future<?> write(final byte[] data, final ParcelFileDescriptor readFd, final ParcelFileDescriptor writeFd) {
    return exec.submit(new Runnable() {
      @Override
      public void run() {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
          Log.v(TAG, String.format(Locale.US, "%s: writing %d bytes...", FdParcelable.this, data.length));
        }
        FileOutputStream fos = new ParcelFileDescriptor.AutoCloseOutputStream(writeFd);
        try {
          fos.write(data);
          if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, String.format(Locale.US, "%s: done writing %d bytes...", FdParcelable.this, data.length));
          }
        } catch (IOException e) {
          e.printStackTrace();
        } finally {
          try {
            fos.close();
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
              Log.v(TAG, String.format(Locale.US, "%s: closed write", FdParcelable.this));
            }
          } catch (IOException e) {
            e.printStackTrace();
          }
          try {
            readFd.close();
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
              Log.v(TAG, String.format(Locale.US, "%s: closed read", FdParcelable.this));
            }
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    });
  }

  public void writeToParcel(Parcel out, int flags) {
    if (Log.isLoggable(TAG, Log.VERBOSE)) {
      Log.v(TAG, String.format(Locale.US, "%s: parceling...", this));
    }

    ParcelFileDescriptor[] fds;
    try {
      fds = ParcelFileDescriptor.createPipe();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    out.writeParcelable(fds[0], 0);
    if (bytes == null) {
      bytes = marshall(value);
    }
    if (Log.isLoggable(TAG, Log.VERBOSE)) {
      Log.v(TAG, String.format(Locale.US, "%s: parceled %d bytes...", this, bytes.length));
    }
    write(bytes, fds[0], fds[1]);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  private static byte[] marshall(Object value) {
    if (value == null) {
      return new byte[0];
    }
    Parcel parcel = Parcel.obtain();
    parcel.writeValue(value);
    byte[] bytes = parcel.marshall();
    parcel.recycle(); // not sure if needed or a good idea
    return bytes;
  }

  private static <V> V unmarshall(byte[] bytes) {
    if (bytes == null) {
      return null;
    }
    Parcel parcel = Parcel.obtain();
    parcel.unmarshall(bytes, 0, bytes.length);
    parcel.setDataPosition(0); // This is extremely important!

    V value = (V) parcel.readValue(FdParcelable.class.getClassLoader());
    parcel.recycle();
    return value;
  }
}
