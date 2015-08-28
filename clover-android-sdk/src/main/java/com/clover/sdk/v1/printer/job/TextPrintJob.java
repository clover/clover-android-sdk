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

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.clover.sdk.v1.printer.Category;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TextPrintJob extends PrintJob implements Parcelable {
  public static class Builder extends PrintJob.Builder {
    private List<String> lines = Collections.emptyList();

    public Builder lines(List<String> lines) {
      this.lines = lines;
      return this;
    }

    public Builder text(String text) {
      String[] l = text.split("\\n");
      lines = Arrays.asList(l);
      return this;
    }

    public TextPrintJob build() {
      return new TextPrintJob(this);
    }
  }

  public final ArrayList<String> lines;
  private static final String BUNDLE_KEY_LINES = "l";

  protected TextPrintJob(Builder builder) {
    super(builder);
    this.lines = new ArrayList<String>(builder.lines);
  }

  @Override
  public Category getPrinterCategory() {
    return Category.RECEIPT;
  }

  public static final Parcelable.Creator<TextPrintJob> CREATOR
      = new Parcelable.Creator<TextPrintJob>() {
    public TextPrintJob createFromParcel(Parcel in) {
      return new TextPrintJob(in);
    }

    public TextPrintJob[] newArray(int size) {
      return new TextPrintJob[size];
    }
  };

  protected TextPrintJob(Parcel in) {
    super(in);
    Bundle bundle = in.readBundle(((Object)this).getClass().getClassLoader());
    lines = bundle.getStringArrayList(BUNDLE_KEY_LINES);
    // Add more data here, but remember old apps might not provide it!
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    Bundle bundle = new Bundle();
    bundle.putStringArrayList(BUNDLE_KEY_LINES, lines);
    // Add more stuff here

    dest.writeBundle(bundle);
  }
}
