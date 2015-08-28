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

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import com.clover.sdk.internal.util.OutputUriFactory;
import com.clover.sdk.internal.util.Views;
import com.clover.sdk.v1.printer.Category;
import com.clover.sdk.v1.printer.ReceiptFileContract;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Create a PrintJob from a given {@link android.view.View}. This may take time, must not be built on the main thread.
 */
public class ViewPrintJob extends PrintJob implements Parcelable {

  private static final String TAG = "ViewPrintJob";

  public static class Builder extends PrintJob.Builder {
    protected View view;

    public Builder view(View view) {
      this.view = view;
      return this;
    }

    @Override
    public ViewPrintJob build() {
      return new ViewPrintJob(this);
    }
  }

  public final ArrayList<String> imageFiles;
  private static final String BUNDLE_KEY_IMAGE_FILES = "i";

  @Deprecated
  protected ViewPrintJob(View view, int flags) {
    this((ViewPrintJob.Builder) new Builder().view(view).flags(flags));

  }

  protected ViewPrintJob(Builder builder) {
    super(builder);
    ArrayList<String> files = null;
    try {
      files = Views.writeBitmapChucks(builder.view, new ReceiptFileOutputFactory(builder.view.getContext()));
    } catch (Exception e) {
      Log.e(TAG, "Unable to produce image files for print", e);
    }
    imageFiles = files;
  }

  @Override
  public Category getPrinterCategory() {
    return Category.RECEIPT;
  }

  public static final Parcelable.Creator<ViewPrintJob> CREATOR
      = new Parcelable.Creator<ViewPrintJob>() {
    public ViewPrintJob createFromParcel(Parcel in) {
      return new ViewPrintJob(in);
    }

    public ViewPrintJob[] newArray(int size) {
      return new ViewPrintJob[size];
    }
  };

  protected ViewPrintJob(Parcel in) {
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
      return getContext().getContentResolver().insert(ReceiptFileContract.ReceiptFileFactory.CONTENT_URI, values);
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

}
