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
import com.clover.sdk.v3.order.Order;
import com.clover.sdk.v3.payments.Payment;

public class StaticPaymentPrintJob extends StaticReceiptPrintJob implements Parcelable {
  private static final String BUNDLE_KEY_PAYMENT = "pp";
  private static final String BUNDLE_KEY_PAYMENT_ID = "p";

  public static class Builder extends StaticReceiptPrintJob.Builder {
    protected String paymentId;
    protected Payment payment;

    public Builder staticPaymentPrintJob(StaticPaymentPrintJob pj) {
      staticReceiptPrintJob(pj);
      this.paymentId = pj.paymentId;
      this.payment = pj.payment;

      return this;
    }

    public Builder paymentId(String paymentId) {
      this.paymentId = paymentId;
      return this;
    }

    public Builder payment(Payment payment) {
      this.payment = payment;
      return this;
    }

    public StaticPaymentPrintJob build() {
      flags |= FLAG_SALE;
      return new StaticPaymentPrintJob(this);
    }
  }

  public final String paymentId;
  public final Payment payment;


  @Deprecated
  public StaticPaymentPrintJob(Order order, String paymentId, int flags) {
    super(order, flags);
    this.paymentId = paymentId;
    this.payment = null;
  }

  protected StaticPaymentPrintJob(Builder builder) {
    super(builder);
    this.paymentId = builder.paymentId;
    this.payment = builder.payment;
  }

  public static final Creator<StaticPaymentPrintJob> CREATOR = new Creator<StaticPaymentPrintJob>() {
    public StaticPaymentPrintJob createFromParcel(Parcel in) {
      return new StaticPaymentPrintJob(in);
    }

    public StaticPaymentPrintJob[] newArray(int size) {
      return new StaticPaymentPrintJob[size];
    }
  };

  protected StaticPaymentPrintJob(Parcel in) {
    super(in);
    Bundle bundle = in.readBundle(((Object)this).getClass().getClassLoader()); // needed otherwise BadParcelableException: ClassNotFoundException when unmarshalling
    paymentId = bundle.getString(BUNDLE_KEY_PAYMENT_ID);
    payment = bundle.getParcelable(BUNDLE_KEY_PAYMENT);
    // Add more data here, but remember old apps might not provide it!
  }

  @Override
  public Category getPrinterCategory() {
    return Category.RECEIPT;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    Bundle bundle = new Bundle();
    bundle.putString(BUNDLE_KEY_PAYMENT_ID, paymentId);
    bundle.putParcelable(BUNDLE_KEY_PAYMENT, payment);

    dest.writeBundle(bundle);
  }
}
