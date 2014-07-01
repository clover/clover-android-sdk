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
 * @deprecated Please use (@link com.clover.sdk.v1.printer.job.StaticRefundPrintJob}
 */
public class RefundPrintJob extends OrderBasedPrintJob implements Parcelable {
  public static class Builder extends OrderBasedPrintJob.Builder {
    private String refundId;

    public Builder redfundId(String refundId) {
      this.refundId = refundId;
      return this;
    }

    public RefundPrintJob build() {
      return new RefundPrintJob(orderId, refundId, flags | FLAG_REFUND);
    }
  }

  public final String refundId;
  private static final String BUNDLE_KEY_REFUND_ID = "r";

  protected RefundPrintJob(String orderId, String refundId, int flags) {
    super(orderId, flags);
    this.refundId = refundId;
  }

  @Override
  public Category getPrinterCategory() {
    return Category.RECEIPT;
  }

  public static final Parcelable.Creator<RefundPrintJob> CREATOR
      = new Parcelable.Creator<RefundPrintJob>() {
    public RefundPrintJob createFromParcel(Parcel in) {
      return new RefundPrintJob(in);
    }

    public RefundPrintJob[] newArray(int size) {
      return new RefundPrintJob[size];
    }
  };

  protected RefundPrintJob(Parcel in) {
    super(in);
    Bundle bundle = in.readBundle();
    refundId = bundle.getString(BUNDLE_KEY_REFUND_ID);
    // Add more data here, but remember old apps might not provide it!
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    Bundle bundle = new Bundle();
    bundle.putString(BUNDLE_KEY_REFUND_ID, refundId);
    // Add more stuff here

    dest.writeBundle(bundle);
  }
}
