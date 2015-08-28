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

import android.os.Parcel;
import android.os.Parcelable;
import com.clover.sdk.v1.printer.Category;
import com.clover.sdk.v3.order.Order;

public class StaticReceiptPrintJob extends StaticOrderBasedPrintJob implements Parcelable {

  public static class Builder extends StaticOrderBasedPrintJob.Builder {
    public Builder staticReceiptPrintJob(StaticReceiptPrintJob pj) {
      staticOrderBasedPrintJob(pj);

      return this;
    }

    // This method isn't needed but kept for backwards compatibility
    public Builder order(Order order) {
      super.order(order);
      return this;
    }

    public StaticReceiptPrintJob build() {
      flags |= FLAG_SALE;
      return new StaticReceiptPrintJob(this);
    }
  }

  @Deprecated
  public StaticReceiptPrintJob(Order order, int flags) {
    super(order, flags);
  }

  protected StaticReceiptPrintJob(Builder builder) {
    super(builder);
  }

  public static final Creator<StaticReceiptPrintJob> CREATOR = new Creator<StaticReceiptPrintJob>() {
    public StaticReceiptPrintJob createFromParcel(Parcel in) {
      return new StaticReceiptPrintJob(in);
    }

    public StaticReceiptPrintJob[] newArray(int size) {
      return new StaticReceiptPrintJob[size];
    }
  };

  protected StaticReceiptPrintJob(Parcel in) {
    super(in);
    // This class cannot contain any data because we forgot to put a Bundle here
  }

  @Override
  public Category getPrinterCategory() {
    return Category.RECEIPT;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    // This class cannot contain any data because we forgot to put a Bundle here
  }
}
