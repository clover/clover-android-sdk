/*
 * Copyright (C) 2013 Clover Network, Inc.
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
 * @deprecated Please use (@link com.clover.sdk.v1.printer.job.StaticPaymentPrintJob}
 */
public class PaymentPrintJob extends ReceiptPrintJob implements Parcelable {
  public static class Builder extends ReceiptPrintJob.Builder {
    private String paymentId;

    public Builder paymentId(String paymentId) {
      this.paymentId = paymentId;
      return this;
    }

    public PaymentPrintJob build() {
      return new PaymentPrintJob(orderId, paymentId, flags | FLAG_SALE);
    }
  }

  public final String paymentId;
  public static final String BUNDLE_KEY_PAYMENT_ID = "p";

  protected PaymentPrintJob(String orderId, String paymentId, int flags) {
    super(orderId, flags);
    this.paymentId = paymentId;
  }

  @Override
  public Category getPrinterCategory() {
    return Category.RECEIPT;
  }

  public static final Parcelable.Creator<PaymentPrintJob> CREATOR
      = new Parcelable.Creator<PaymentPrintJob>() {
    public PaymentPrintJob createFromParcel(Parcel in) {
      return new PaymentPrintJob(in);
    }

    public PaymentPrintJob[] newArray(int size) {
      return new PaymentPrintJob[size];
    }
  };

  protected PaymentPrintJob(Parcel in) {
    super(in);
    Bundle bundle = in.readBundle();
    paymentId = bundle.getString(BUNDLE_KEY_PAYMENT_ID);
    // Add more data here, but remember old apps might not provide it!
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    Bundle bundle = new Bundle();
    bundle.putString(BUNDLE_KEY_PAYMENT_ID, paymentId);
    // Add more stuff here

    dest.writeBundle(bundle);
  }
}
