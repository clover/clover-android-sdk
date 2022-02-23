/*
 * Copyright (C) 2016 Clover Network, Inc.
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
package com.clover.sdk.v1.printer.job;

import com.clover.sdk.internal.util.OutputUriFactory;
import com.clover.sdk.internal.util.UnstableContentResolverClient;
import com.clover.sdk.internal.util.Views;
import com.clover.sdk.v1.printer.Category;
import com.clover.sdk.v1.printer.Printer;
import com.clover.sdk.v1.printer.ReceiptFileContract;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Create a PrintJob from a given {@link android.view.View}. The {@link Builder#build()} performs
 * blocking IO so it must be invoked on a background thread.
 * <p/>
 * The View used need not be displayed on screen but it must be laid out and measured first
 * beforehand. For that reason {@link Builder#view(View, int)} is now the preferred method to
 * build an instance of this job since that method ensures layout and measurement happens.
 * <p/>
 * The width of the view should almost always be equal to the printer head width which can be
 * obtained like so:
 * <pre><code>
 * PrinterConnector pc = new PrinterConnector(context, CloverAccount.getAccount(context), null);
 * List<Printer> printerList = pc.getPrinters(Category.RECEIPT);
 * if (printerList.size() > 0) {
 *      Printer preferredPrinter = printerList.get(0);
 *      TypeDetails typeDetails = pc.getPrinterTypeDetails(preferredPrinter);
 *      int width = typeDetails.getNumDotsWidth();
 * }
 * </code></pre>
 */
public class ViewPrintJob extends PrintJob implements Parcelable {

  private static final String TAG = "ViewPrintJob";

  public static class Builder extends PrintJob.Builder {
    protected View view;

    /**
     * Set the view to print in this PrintJob. You must manually layout and measure your View
     * before building an instance.
     *
     * @deprecated Prefer {@link #view(View, int)} since it ensures the View is laid out and
     *   measured.
     */
    @Deprecated
    public Builder view(View view) {
      this.view = view;
      return this;
    }

    /**
     * This method differs from {@link #view(View)} as it prepares the View for printing by
     * laying it out and measuring it for you.
     */
    public Builder view(View view, int viewWidth) {
      this.view = view;
      layoutAndMeasureView(view, viewWidth);
      return this;
    }

    /**
     * Builds an instance. Performs some blocking IO so it must be invoked on a background thread.
     */
    @Override
    public ViewPrintJob build() {
      return new ViewPrintJob(this);
    }

    /**
     * Measure and layout the view that was passed
     */
    public void layoutAndMeasureView(View view, int viewWidth) {
      int measuredWidth = View.MeasureSpec.makeMeasureSpec(viewWidth, View.MeasureSpec.EXACTLY);
      int measuredHeight = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
      view.measure(measuredWidth, measuredHeight);
      view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
      view.requestLayout();
    }
  }

  /**
   * For internal use only.
   */
  public final ArrayList<String> imageFiles;

  private static final String BUNDLE_KEY_IMAGE_FILES = "i";

  @Deprecated
  protected ViewPrintJob(View view,
                         int flags) {
    this((ViewPrintJob.Builder) new Builder().view(view).flags(flags));
  }

  protected ViewPrintJob(Builder builder) {
    super(builder);
    ArrayList<String> files = new ArrayList<>();
    try {
      files = Views.writeBitmapChucks(builder.view, new ReceiptFileOutputFactory(builder.view.getContext()));
    } catch (Exception e) {
      Log.e(TAG, "Unable to produce image files for print", e);
    }
    imageFiles = files;
  }

  @Override
  public void print(Context context, Account account, Printer printer) {
    if (imageFiles.size() == 0) {
      Log.w(TAG, "Cannot print, no image files are available, check usage and/or logs");
      return;
    }

    super.print(context, account, printer);
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
    Bundle bundle = in.readBundle(((Object) this).getClass().getClassLoader());
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

}
