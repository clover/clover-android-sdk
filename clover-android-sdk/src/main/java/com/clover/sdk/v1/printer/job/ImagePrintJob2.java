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
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.clover.sdk.internal.util.OutputUriFactory;
import com.clover.sdk.internal.util.UnstableContentResolverClient;
import com.clover.sdk.v1.printer.Category;
import com.clover.sdk.v1.printer.Printer;
import com.clover.sdk.v1.printer.ReceiptFileContract;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Create a PrintJob to print one or more bitmaps. If multiple bitmaps are provided they printed
 * one after another vertically on the receipt. Bitmaps will be scaled down proportionally to
 * the paper width as needed.
 * <p/>
 * The {@link Builder#build()} performs blocking IO so it must be invoked on a background thread.
 */
public class ImagePrintJob2 extends PrintJob implements Parcelable {

  public static final int MAX_HEIGHT = 2048;

  private static final String TAG = ImagePrintJob2.class.getSimpleName();

  public static class Builder extends PrintJob.Builder {
    protected final List<Bitmap> bitmaps = new ArrayList<>();
    private final Context context;

    public Builder(Context context) {
      super();
      this.context = context;
    }

    /**
     * Add a bitmap to print in this PrintJob. Call repeatedly to add additional bitmaps.
     * Bitmaps are printed in the order they are added here.
     *
     * @throws IllegalArgumentException if the bitmap's height is greater than
     * {@link #MAX_HEIGHT}. If you need to print an image that is greater than
     * {@link #MAX_HEIGHT} pixels in height you must break it into multiple pieces.
     */
    public Builder bitmap(Bitmap bitmap) {
      if (bitmap.getHeight() > MAX_HEIGHT) {
        throw new IllegalArgumentException("Bitmap height cannot be greater than 2048 pixels");
      }
      this.bitmaps.add(bitmap);
      return this;
    }

    /**
     * Builds an instance. This method performs some blocking IO so it must be invoked on a
     * background thread.
     */
    @Override
    public ImagePrintJob2 build() {
      return new ImagePrintJob2(this);
    }
  }

  /**
   * For internal use only.
   */
  public final ArrayList<String> imageFiles;

  private static final String BUNDLE_KEY_IMAGE_FILES = "i";

  protected ImagePrintJob2(Builder builder) {
    super(builder);
    ArrayList<String> files = new ArrayList<>();
    try {
      files = writeBitmapChucks(builder.bitmaps, new ReceiptFileOutputFactory(builder.context));
    } catch (Exception e) {
      Log.e(TAG, "Unable to produce image files for print", e);
    }
    imageFiles = files;
  }

  @Override
  public Category getPrinterCategory() {
    return Category.RECEIPT;
  }

  @Override
  public void print(Context context, Account account, Printer printer) {
    if (imageFiles.size() == 0) {
      Log.w(TAG, "Cannot print, no image files are available, check usage and/or logs");
      return;
    }

    super.print(context, account, printer);
  }

  public static final Creator<ImagePrintJob2> CREATOR = new Creator<ImagePrintJob2>() {
    public ImagePrintJob2 createFromParcel(Parcel in) {
      return new ImagePrintJob2(in);
    }

    public ImagePrintJob2[] newArray(int size) {
      return new ImagePrintJob2[size];
    }
  };

  protected ImagePrintJob2(Parcel in) {
    super(in);
    Bundle bundle = in.readBundle(((Object)this).getClass().getClassLoader());
    imageFiles = bundle.getStringArrayList(BUNDLE_KEY_IMAGE_FILES);
    // Add more data here, but remember old apps might not provide it!
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    Bundle bundle = new Bundle();
    bundle.putStringArrayList(BUNDLE_KEY_IMAGE_FILES, imageFiles);
    // Add more stuff here

    dest.writeBundle(bundle);
  }

  private static class ReceiptFileOutputFactory extends OutputUriFactory {
    public ReceiptFileOutputFactory(Context context) {
      super(context);
    }

    @Override
    public Uri createNewOutputUri() {
      ContentValues values = new ContentValues();
      values.put(ReceiptFileContract.ReceiptFiles.FILE_EXTENSION, "png");
      UnstableContentResolverClient client = new UnstableContentResolverClient(getContext().getContentResolver(),
          ReceiptFileContract.ReceiptFileFactory.CONTENT_URI);
      return client.insert(values);
    }

    @Override
    public OutputStream getOutputStreamForUri(Uri uri) {
      try {
        return getContext().getContentResolver().openOutputStream(uri);
      } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private static ArrayList<String> writeBitmapChucks(List<Bitmap> bitmaps, OutputUriFactory outputUriFactory) throws IOException {
    if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
      throw new RuntimeException("Long running operation should not be executed on the main thread");
    }

    ArrayList<String> outUris = new ArrayList<String>();

    for (Bitmap bitmap: bitmaps) {
      Uri outUri = outputUriFactory.createNewOutputUri();
      BufferedOutputStream bos = new BufferedOutputStream(outputUriFactory.getOutputStreamForUri(outUri));
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
      bos.flush();
      bos.close();

      outUris.add(outUri.toString());
    }

    return outUris;
  }
}
