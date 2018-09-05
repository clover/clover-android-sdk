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

public class TestFiscalPrintJob extends PrintJob implements Parcelable {
  public static class Builder extends PrintJob.Builder {

    public TestFiscalPrintJob build() {
      return new TestFiscalPrintJob();
    }
  }

  @Deprecated
  protected TestFiscalPrintJob() {
    super(FLAG_NONE);
  }

  @Override
  public Category getPrinterCategory() {
    return Category.FISCAL;
  }

  public static final Creator<TestFiscalPrintJob> CREATOR
      = new Creator<TestFiscalPrintJob>() {
    public TestFiscalPrintJob createFromParcel(Parcel in) {
      return new TestFiscalPrintJob(in);
    }

    public TestFiscalPrintJob[] newArray(int size) {
      return new TestFiscalPrintJob[size];
    }
  };

  protected TestFiscalPrintJob(Parcel in) {
    super(in);
    Bundle bundle = in.readBundle(((Object)this).getClass().getClassLoader());
    // Add more data here, but remember old apps might not provide it!
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    Bundle bundle = new Bundle();
    // Add more stuff here

    dest.writeBundle(bundle);
  }
}
