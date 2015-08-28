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

public class TestReceiptPrintJob extends PrintJob implements Parcelable {
  public static class Builder extends PrintJob.Builder {

    public TestReceiptPrintJob build() {
      return new TestReceiptPrintJob();
    }
  }

  @Deprecated
  protected TestReceiptPrintJob() {
    super(FLAG_NONE);
  }

  @Override
  public Category getPrinterCategory() {
    return Category.RECEIPT;
  }

  public static final Parcelable.Creator<TestReceiptPrintJob> CREATOR
      = new Parcelable.Creator<TestReceiptPrintJob>() {
    public TestReceiptPrintJob createFromParcel(Parcel in) {
      return new TestReceiptPrintJob(in);
    }

    public TestReceiptPrintJob[] newArray(int size) {
      return new TestReceiptPrintJob[size];
    }
  };

  protected TestReceiptPrintJob(Parcel in) {
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
