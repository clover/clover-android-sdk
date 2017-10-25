/**
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

import com.clover.sdk.v1.printer.Category;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Allows direct printing of http(s)://, file://, or content:// images.
 *
 * Supports bmp, gif, jpg, png formats.  Note that the support of formats is limited
 * based on the printer capability. Color models, modes, as well as the size of the
 * images will affect the ability to print and the quality of the image printed.
 */
public class MultipleImagePrintJob extends PrintJob implements Parcelable {

  public static final Parcelable.Creator<MultipleImagePrintJob> CREATOR
      = new Parcelable.Creator<MultipleImagePrintJob>() {
    public MultipleImagePrintJob createFromParcel(Parcel in) {
      return new MultipleImagePrintJob(in);
    }

    public MultipleImagePrintJob[] newArray(int size) {
      return new MultipleImagePrintJob[size];
    }
  };

  private static final String TAG = "MultipleImagePrintJob";
  private static final String BUNDLE_KEY_CONTENT_STRS = "i";
  public final ArrayList<String> uriStrings;

  protected MultipleImagePrintJob(Builder builder) {
    super(builder);
    this.uriStrings = builder.uriStrings;
  }

  protected MultipleImagePrintJob(Parcel in) {
    super(in);
    Bundle bundle = in.readBundle(((Object) this).getClass().getClassLoader());
    uriStrings = bundle.getStringArrayList(BUNDLE_KEY_CONTENT_STRS);
  }

  @Override
  public Category getPrinterCategory() {
    return Category.RECEIPT;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    Bundle bundle = new Bundle();
    bundle.putStringArrayList(BUNDLE_KEY_CONTENT_STRS, uriStrings);
    dest.writeBundle(bundle);
  }

  public static class Builder extends PrintJob.Builder {
    protected ArrayList<String> uriStrings;

    public Builder uriStrings(String... uriStrings) {
      this.uriStrings = new ArrayList<>(Arrays.asList(uriStrings));
      return this;
    }

    @Override
    public PrintJob build() {
      return new MultipleImagePrintJob(this);
    }
  }
}
