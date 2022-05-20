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

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.clover.common2.payments.PayIntent;
import com.clover.sdk.v1.printer.Category;
import com.clover.sdk.v3.payments.Payment;
import com.clover.sdk.v3.payments.Refund;

public class StaticRefundPrintJob extends StaticReceiptPrintJob implements Parcelable {
  private static final String BUNDLE_KEY_REFUND = "rr";
  private static final String BUNDLE_KEY_REFUND_ID = "r";
  private static final String BUNDLE_KEY_ORDER_ID = "o";

  public static class Builder extends StaticReceiptPrintJob.Builder {
    protected String refundId;
    protected Refund refund;
    protected String orderId;

    public Builder staticRefundPrintJob(StaticRefundPrintJob pj) {
      staticReceiptPrintJob(pj);
      this.refundId = pj.refundId;
      this.refund = pj.refund;
      this.orderId = pj.orderId;
      return this;
    }

    public Builder refundId(String refundId) {
      this.refundId = refundId;
      return this;
    }

    public Builder orderId(String orderId) {
      this.orderId = orderId;
      return this;
    }

    public Builder refund(Refund refund) {
      this.refund = refund;
      return this;
    }

    public StaticRefundPrintJob build() {
      flags |= FLAG_REFUND;
      return new StaticRefundPrintJob(this);
    }
  }

  public final String refundId;
  public final Refund refund;
  public final String orderId;

  protected StaticRefundPrintJob(Builder builder) {
    super(builder);
    this.refundId = builder.refundId;
    this.refund = builder.refund;
    this.orderId = builder.orderId;
  }

  public static final Creator<StaticRefundPrintJob> CREATOR = new Creator<StaticRefundPrintJob>() {
    public StaticRefundPrintJob createFromParcel(Parcel in) {
      return new StaticRefundPrintJob(in);
    }

    public StaticRefundPrintJob[] newArray(int size) {
      return new StaticRefundPrintJob[size];
    }
  };

  protected StaticRefundPrintJob(Parcel in) {
    super(in);
    Bundle bundle = in.readBundle(((Object)this).getClass().getClassLoader()); // needed otherwise BadParcelableException: ClassNotFoundException when unmarshalling
    refundId = bundle.getString(BUNDLE_KEY_REFUND_ID);
    refund = bundle.getParcelable(BUNDLE_KEY_REFUND);
    orderId = bundle.getString(BUNDLE_KEY_ORDER_ID);
  }

  @Override
  public Category getPrinterCategory() {
    return Category.RECEIPT;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    Bundle bundle = new Bundle();
    bundle.putString(BUNDLE_KEY_REFUND_ID, refundId);
    bundle.putParcelable(BUNDLE_KEY_REFUND, refund);
    bundle.putString(BUNDLE_KEY_ORDER_ID, orderId);
    dest.writeBundle(bundle);
  }
}
