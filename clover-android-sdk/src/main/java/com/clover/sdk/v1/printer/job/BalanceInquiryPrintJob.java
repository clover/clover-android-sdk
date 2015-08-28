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
import com.clover.sdk.v3.payments.Payment;

public class BalanceInquiryPrintJob extends PrintJob implements Parcelable {
  private static final String BUNDLE_PAYMENT_RESPONSE = "p";

  public final Payment payment;

  public static class Builder extends PrintJob.Builder {
    protected Payment payment;

    public Builder payment(Payment payment) {
      this.payment = payment;
      return this;
    }

    @Override
    public PrintJob build() {
      return new BalanceInquiryPrintJob(this);
    }
  }

  @Deprecated
  public BalanceInquiryPrintJob(int flags, Payment payment) {
    super(flags);
    this.payment = payment;
  }

  protected BalanceInquiryPrintJob(Builder builder) {
    super(builder);
    this.payment = builder.payment;
  }

  public BalanceInquiryPrintJob(Parcel in) {
    super(in);
    Bundle bundle = in.readBundle(((Object)this).getClass().getClassLoader());
    payment = bundle.getParcelable(BUNDLE_PAYMENT_RESPONSE);
  }

  public static final Creator<BalanceInquiryPrintJob> CREATOR
      = new Creator<BalanceInquiryPrintJob>() {
    public BalanceInquiryPrintJob createFromParcel(Parcel in) {
      return new BalanceInquiryPrintJob(in);
    }

    public BalanceInquiryPrintJob[] newArray(int size) {
      return new BalanceInquiryPrintJob[size];
    }
  };

  @Override
  public Category getPrinterCategory() {
    return Category.RECEIPT;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    Bundle bundle = new Bundle();
    bundle.putParcelable(BUNDLE_PAYMENT_RESPONSE, payment);
    dest.writeBundle(bundle);
  }
}
