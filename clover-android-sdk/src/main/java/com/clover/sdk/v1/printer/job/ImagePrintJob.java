/**
 * Copyright (C) 2015 Clover Network, Inc.
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

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.clover.sdk.v1.printer.Category;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ImagePrintJob extends PrintJob implements Parcelable {
  private static final int WIDTH_MAX = 600;

  private static final String TAG = "ImagePrintJob";

  public static class Builder extends PrintJob.Builder {
    protected Bitmap bitmap;

    public Builder bitmap(Bitmap bitmap) {
      this.bitmap = bitmap;
      return this;
    }

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

    @Override
    public PrintJob build() {
      return new ImagePrintJob(this);
    }
  }

  // FIXME: This is not a safe way to transfer file data, we should be using ContentProvider file methods instead
  public final File imageFile;
  private static final String BUNDLE_KEY_IMAGE_FILE = "i";

  @Deprecated
  protected ImagePrintJob(Bitmap bitmap, int flags) {
    this((ImagePrintJob.Builder)new Builder().bitmap(bitmap).flags(flags));
  }

  protected ImagePrintJob(Builder builder) {
    super(builder);
    File f = null;
    try {
      f = writeImageFile(builder.bitmap);
      builder.bitmap.recycle();
    } catch (IOException e) {
      Log.e(TAG, "unable to write image file", e);
    }
    imageFile = f;
  }

  @Override
  public Category getPrinterCategory() {
    return Category.RECEIPT;
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

  private static File writeImageFile(Bitmap bitmap) throws IOException {
    OutputStream os = null;
    try {
      //File dir = new File(Environment.getExternalStorageDirectory(), "clover" + File.separator + "image-print");
      File dir = new File("/sdcard", "clover" + File.separator + "image-print");
      if (!dir.exists()) {
        dir.mkdirs();
      }
      File imageFile = File.createTempFile("image-", ".png", dir);
      os = new FileOutputStream(imageFile);
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
      return imageFile;
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
