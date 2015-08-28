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

@Deprecated
/**
 * @deprecated Please use (@link com.clover.sdk.v1.printer.job.StaticBillPrintJob}
 */
public class BillPrintJob extends ReceiptPrintJob implements Parcelable {

  @Deprecated
  public static class Builder extends ReceiptPrintJob.Builder {

    private String binName;

    public Builder binName(String binName) {
      this.binName = binName;
      return this;
    }

    public BillPrintJob build() {
      flags |= FLAG_BILL;
      return new BillPrintJob(this);
    }
  }

  public final String binName;
  public static final String BUNDLE_KEY_BIN_NAME = "b";

  @Deprecated
  protected BillPrintJob(String orderId, String binName, int flags) {
    super(orderId, flags);
    this.binName = binName;
  }

  protected BillPrintJob(Builder builder) {
    super(builder);
    this.binName = builder.binName;
  }

  @Override
  public Category getPrinterCategory() {
    return Category.RECEIPT;
  }

  public static final Parcelable.Creator<BillPrintJob> CREATOR
      = new Parcelable.Creator<BillPrintJob>() {
    public BillPrintJob createFromParcel(Parcel in) {
      return new BillPrintJob(in);
    }

    public BillPrintJob[] newArray(int size) {
      return new BillPrintJob[size];
    }
  };

  protected BillPrintJob(Parcel in) {
    super(in);
    Bundle bundle = in.readBundle(((Object)this).getClass().getClassLoader());
    binName = bundle.getString(BUNDLE_KEY_BIN_NAME);
    // Add more data here, but remember old apps might not provide it!
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    Bundle bundle = new Bundle();
    bundle.putString(BUNDLE_KEY_BIN_NAME, binName);
    // Add more stuff here

    dest.writeBundle(bundle);
  }
}
