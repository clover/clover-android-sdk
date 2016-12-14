package com.clover.sdk;

import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
 *
 * @param <V> Value object, per contract of {@link Parcel#writeValue(Object)}.
 */
public class FdParcelable<V> implements Parcelable {
  // Use up to 8 threads to write data.
  private static final ExecutorService exec = Executors.newFixedThreadPool(8);

  // Value and bytes are lazily created. That is, we delay parceling the value to bytes
  // until writeToParcel() is called, and we delay unparceling bytes until getValue() is called.
  //
  // If value is !null and bytes are null, then we have not parceled data yet
  // If value is null and bytes are !null, then we have not unparceled yet (no call to getValue())
  // If both are null, either we were passed a null value, or unparceling failed.

  private V value = null;
  private byte[] bytes = null;

  public FdParcelable(V value) {
    this.value = value;
    this.bytes = null;
  }

  FdParcelable(byte[] bytes) {
    this.value = null;
    this.bytes = bytes;
  }

  public FdParcelable(Parcel in) {
    ParcelFileDescriptor readFd = in.readParcelable(getClass().getClassLoader());

    FileInputStream fis = new ParcelFileDescriptor.AutoCloseInputStream(readFd);
    ByteArrayOutputStream out = new ByteArrayOutputStream();

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
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    this.value = null;
    this.bytes = out.toByteArray();
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

  protected static Future<?> write(final byte[] data, final ParcelFileDescriptor writeFd) {
    return exec.submit(new Runnable() {
      @Override
      public void run() {
        FileOutputStream fos = new ParcelFileDescriptor.AutoCloseOutputStream(writeFd);
        try {
          fos.write(data);
        } catch (IOException e) {
          e.printStackTrace();
        } finally {
          try {
            fos.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    });
  }

  public void writeToParcel(Parcel out, int flags) {
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
    write(bytes, fds[1]);
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
    parcel.setDataPosition(0); // this is extremely important!

    V value = (V) parcel.readValue(FdParcelable.class.getClassLoader());
    parcel.recycle();
    return value;
  }
}
