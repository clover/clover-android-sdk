/*
 * Copyright (C) 2013 Clover Network, Inc.
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
import android.util.Log;

import com.clover.sdk.v1.printer.Category;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

public class ImagePrintJob extends PrintJob implements Serializable {
  private static final int WIDTH_MAX = 600;

  private static final String TAG = "ImagePrintJob";

  protected static final String KEY_IMAGE_FILE = "imageFile";

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

      Bitmap resized = Bitmap.createScaledBitmap(bitmap, WIDTH_MAX, scaledHeight, false);
      bitmap.recycle();
      bitmap = resized;

      return this;
    }

    @Override
    public PrintJob build() {
      return new ImagePrintJob(bitmap, flags);
    }
  }

  public final File imageFile;

  protected ImagePrintJob(Bitmap bitmap, int flags) {
    super(flags);
    File f = null;
    try {
      f = writeImageFile(bitmap);
      bitmap.recycle();
    } catch (IOException e) {
      Log.e(TAG, "unable to write image file", e);
    }
    imageFile = f;
  }

  @Override
  public Category getPrinterCategory() {
    return Category.RECEIPT;
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
