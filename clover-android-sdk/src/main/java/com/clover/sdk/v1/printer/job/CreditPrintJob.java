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
 * @deprecated Please use {@link com.clover.sdk.v1.printer.job.StaticCreditPrintJob}
 */
public class CreditPrintJob extends ReceiptPrintJob implements Parcelable {
  public static class Builder extends ReceiptPrintJob.Builder {
    private String creditId;

    public Builder creditId(String paymentId) {
      this.creditId = paymentId;
      return this;
    }

    public CreditPrintJob build() {
      return new CreditPrintJob(this);
    }
  }

  public final String creditId;
  private static final String BUNDLE_KEY_CREDIT_ID = "c";

  @Deprecated
  protected CreditPrintJob(String orderId, String creditId, int flags) {
    super(orderId, flags);
    this.creditId = creditId;
  }

  protected CreditPrintJob(Builder builder) {
    super(builder);
    this.creditId = builder.creditId;
  }

  @Override
  public Category getPrinterCategory() {
    return Category.RECEIPT;
  }

  public static final Parcelable.Creator<CreditPrintJob> CREATOR
      = new Parcelable.Creator<CreditPrintJob>() {
    public CreditPrintJob createFromParcel(Parcel in) {
      return new CreditPrintJob(in);
    }

    public CreditPrintJob[] newArray(int size) {
      return new CreditPrintJob[size];
    }
  };

  protected CreditPrintJob(Parcel in) {
    super(in);
    Bundle bundle = in.readBundle(((Object)this).getClass().getClassLoader());
    creditId = bundle.getString(BUNDLE_KEY_CREDIT_ID);
    // Add more data here, but remember old apps might not provide it!
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    Bundle bundle = new Bundle();
    bundle.putString(BUNDLE_KEY_CREDIT_ID, creditId);
    // Add more stuff here

    dest.writeBundle(bundle);
  }
}
