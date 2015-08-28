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
import com.clover.sdk.v3.payments.Credit;

public class StaticCreditPrintJob extends StaticReceiptPrintJob implements Parcelable {
  private static final String BUNDLE_KEY_CREDIT_ID = "c";
  private static final String BUNDLE_KEY_CREDIT = "cc";

  public static class Builder extends StaticReceiptPrintJob.Builder {
    private String creditId;
    private Credit credit;

    public Builder staticCreditPrintJob(StaticCreditPrintJob pj) {
      staticReceiptPrintJob(pj);
      this.creditId = pj.creditId;
      this.credit = pj.credit;

      return this;
    }

    public Builder creditId(String creditId) {
      this.creditId = creditId;
      return this;
    }

    public Builder credit(Credit credit) {
      this.credit = credit;
      return this;
    }

    public StaticCreditPrintJob build() {
      return new StaticCreditPrintJob(this);
    }
  }

  public final String creditId;
  public final Credit credit;

  @Deprecated
  public StaticCreditPrintJob(Order order, String creditId, int flags) {
    super(order, flags);
    this.creditId = creditId;
    this.credit = null;
  }

  protected StaticCreditPrintJob(Builder builder) {
    super(builder);
    this.creditId = builder.creditId;
    this.credit = builder.credit;
  }

  public static final Creator<StaticCreditPrintJob> CREATOR = new Creator<StaticCreditPrintJob>() {
    public StaticCreditPrintJob createFromParcel(Parcel in) {
      return new StaticCreditPrintJob(in);
    }

    public StaticCreditPrintJob[] newArray(int size) {
      return new StaticCreditPrintJob[size];
    }
  };

  protected StaticCreditPrintJob(Parcel in) {
    super(in);
    Bundle bundle = in.readBundle(((Object)this).getClass().getClassLoader()); // needed otherwise BadParcelableException: ClassNotFoundException when unmarshalling
    creditId = bundle.getString(BUNDLE_KEY_CREDIT_ID);
    credit = bundle.getParcelable(BUNDLE_KEY_CREDIT);
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
    bundle.putString(BUNDLE_KEY_CREDIT_ID, creditId);
    bundle.putParcelable(BUNDLE_KEY_CREDIT, credit);

    dest.writeBundle(bundle);
  }
}
