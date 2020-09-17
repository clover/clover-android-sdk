/*
 * Copyright (C) 2016 Clover Network, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clover.sdk.v1.printer.job;

import android.accounts.Account;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.Pair;
import com.clover.sdk.internal.util.BitmapUtils;
import com.clover.sdk.v1.printer.Category;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This class requires WRITE_EXTERNAL_STORAGE permission. Declare the permission in your AndroidManifest.xml
 * <code>
 *     <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 * </code>
 *
 * @deprecated Use {@link ImagePrintJob2}. This class writes files into external storage that
 *     are not cleaned up and can eventually exhaust the device's storage.
 */
@Deprecated
public class ImagePrintJob extends PrintJob implements Parcelable {

  private static final String TAG = "ImagePrintJob";

  private static final int WIDTH_MAX = 600;

  public static final int MAX_RECEIPT_WIDTH = 576;

  private static BitmapUtils mBitmapUtils = new BitmapUtils();

  public static class Builder extends PrintJob.Builder {
    protected Bitmap bitmap;
    protected String urlString;

    public Builder bitmap(Bitmap bitmap) {
      this.bitmap = bitmap;
      return this;
    }

    public Builder urlString(String urlString) {
      this.urlString = urlString;
      return this;
    }

    /**
     * Forces the provided bitmap to be scaled down to the maximum allowed width.
     * <p/>
     * If used, this method must be invoked after the {@link #bitmap(Bitmap)} method. This method
     * performs possibly time-consuming calulations so it must be invoked on a background thread.
     */
    public Builder maxWidth() {
      if (bitmap == null) {
        return this;
      }

      int width = bitmap.getWidth();
      int height = bitmap.getHeight();

      float scale = (float) WIDTH_MAX / width;
      int scaledHeight = (int) (height * scale);

      bitmap = Bitmap.createScaledBitmap(bitmap, WIDTH_MAX, scaledHeight, false);

      return this;
    }

    /**
     * Builds an instance. Performs some blocking IO so it must be invoked on a background thread.
     */
    @Override
    public PrintJob build() {
      return new ImagePrintJob(this);
    }
  }

  public final File imageFile;
  protected String urlString;
  private static final String BUNDLE_KEY_IMAGE_FILE = "i";

  @Deprecated
  protected ImagePrintJob(Bitmap bitmap, int flags) {
    this((ImagePrintJob.Builder) new Builder().bitmap(bitmap).flags(flags));
  }

  protected ImagePrintJob(Builder builder) {
    super(builder);
    this.urlString = builder.urlString;

    File f = null;
    if (builder.bitmap != null) {
      try {
        f = writeImageFile(builder.bitmap);
        builder.bitmap.recycle();
      } catch (IOException e) {
        Log.e(TAG, "unable to write image file", e);
      }
    } else {
      try {
        f = generateImageFileObject();
      } catch (IOException e) {
        Log.e(TAG, "unable to create image file", e);
      }
    }
    imageFile = f;
  }

  @Override
  public Category getPrinterCategory() {
    return Category.RECEIPT;
  }

  public void print(Context context, Account account) {
    Log.w(TAG, "Using deprecated ImagePrintJob class, please switch to ImagePrintJob2");

    Pair<Context, Account> params = new Pair<>(context,account);
    if (this.urlString != null) {
      new AsyncTask<Pair<Context, Account>, Void, Pair<Context, Account>>() {
        @Override
        protected Pair<Context, Account> doInBackground(Pair<Context, Account>... params) {
          try {
            Bitmap bitmap = mBitmapUtils.decodeSampledBitmapFromURL(urlString, MAX_RECEIPT_WIDTH);
            writeImageFile(imageFile, bitmap);
            return params[0];
          } catch(Exception excep) {
            Log.e(TAG, "Error writing image to disk from url - " + urlString, excep);
          }
          return null;
        }
        @Override
        protected void onPostExecute(Pair<Context, Account> pair) {
          if(pair != null) {
            ImagePrintJob.super.print(pair.first, pair.second, null);
          } else {
            Log.e(TAG, "Print job aborted, error writing image to disk.");
          }
        }
      }.execute(params);

    } else {
      super.print(context, account, null);
    }
  }

  public static final Parcelable.Creator<ImagePrintJob> CREATOR
      = new Parcelable.Creator<ImagePrintJob>() {
    public ImagePrintJob createFromParcel(Parcel in) {
      return new ImagePrintJob(in);
    }

    public ImagePrintJob[] newArray(int size) {
      return new ImagePrintJob[size];
    }
  };

  protected ImagePrintJob(Parcel in) {
    super(in);
    Bundle bundle = in.readBundle(((Object)this).getClass().getClassLoader());
    imageFile = new File(bundle.getString(BUNDLE_KEY_IMAGE_FILE));
    // Add more data here, but remember old apps might not provide it!
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    Bundle bundle = new Bundle();
    bundle.putString(BUNDLE_KEY_IMAGE_FILE, imageFile.getAbsolutePath());
    // Add more stuff here

    dest.writeBundle(bundle);
  }

  private static File generateImageFileObject() throws IOException {
    File dir = new File("/sdcard", "clover" + File.separator + "image-print");
    if (!dir.exists()) {
        if (!dir.mkdirs()) {
            Log.e(TAG, "Unable to create dir for ImagePrintJob. Did you forget to declare WRITE_EXTERNAL_STORAGE permission in your AndroidManifest.xml?");
        }
    }
    File imageFile = File.createTempFile("image-", ".png", dir);
    return imageFile;
  }

  private static File writeImageFile(Bitmap bitmap) throws IOException {
    File imageFile = generateImageFileObject();
    writeImageFile(imageFile, bitmap);
    return imageFile;
  }

  private static void writeImageFile(File imageFile, Bitmap bitmap) throws IOException {
    OutputStream os = null;
    try {
      os = new FileOutputStream(imageFile);
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
    } finally {
      if (os != null) {
        try {
          os.close();
        } catch (IOException e) {
        }
      }
    }
  }

  @Override
  public void cancel() {
    if (imageFile != null) {
      imageFile.delete();
    }
  }
}
