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

public class StaticGiftReceiptPrintJob extends StaticReceiptPrintJob implements Parcelable {

  public static class Builder extends StaticReceiptPrintJob.Builder {

    public Builder staticGiftReceiptPrintJob(StaticGiftReceiptPrintJob pj) {
      staticReceiptPrintJob(pj);
      return this;
    }

    public StaticGiftReceiptPrintJob build() {
      return new StaticGiftReceiptPrintJob(this);
    }
  }

  @Deprecated
  public StaticGiftReceiptPrintJob(Order order, int flags) {
    super(order, flags);
  }

  protected StaticGiftReceiptPrintJob(Builder builder) {
    super(builder);
  }

  public static final Creator<StaticGiftReceiptPrintJob> CREATOR = new Creator<StaticGiftReceiptPrintJob>() {
    public StaticGiftReceiptPrintJob createFromParcel(Parcel in) {
      return new StaticGiftReceiptPrintJob(in);
    }

    public StaticGiftReceiptPrintJob[] newArray(int size) {
      return new StaticGiftReceiptPrintJob[size];
    }
  };

  protected StaticGiftReceiptPrintJob(Parcel in) {
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
