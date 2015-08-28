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

import java.util.ArrayList;

public class StaticBillPrintJob extends StaticReceiptPrintJob implements Parcelable {
  private static final String BUNDLE_KEY_ITEM_IDS = "i";
  private static final String BUNDLE_KEY_MARK_PRINTED = "m";
  private static final String BUNDLE_KEY_BIN_NAME = "b";

  public static class Builder extends StaticReceiptPrintJob.Builder {
    protected ArrayList<String> itemIds;
    private boolean markPrinted = false;
    private String binName;

    public Builder staticBillPrintJob(StaticBillPrintJob pj) {
      staticReceiptPrintJob(pj);
      this.order = pj.order;
      this.itemIds = new ArrayList<String>(pj.itemIds);
      this.markPrinted = pj.markPrinted;

      return this;
    }

    public Builder binName(String binName) {
      this.binName = binName;
      return this;
    }

    public Builder itemIds(ArrayList<String> itemIds) {
      this.itemIds = itemIds;
      return this;
    }

    public Builder markPrinted(boolean markPrinted) {
      this.markPrinted = markPrinted;
      return this;
    }

    public StaticBillPrintJob build() {
      flags |= FLAG_BILL;
      return new StaticBillPrintJob(this);
    }
  }

  public final ArrayList<String> itemIds;
  public final boolean markPrinted;
  public final String binName;

  @Deprecated
  public StaticBillPrintJob(Order order, ArrayList<String> itemIds, int flags, boolean markPrinted, String binName) {
    super(order, flags);
    this.itemIds = itemIds;
    this.markPrinted = markPrinted;
    this.binName = binName;
  }

  protected StaticBillPrintJob(Builder builder) {
    super(builder);
    this.itemIds = builder.itemIds;
    this.markPrinted = builder.markPrinted;
    this.binName = builder.binName;
  }

  public static final Creator<StaticBillPrintJob> CREATOR = new Creator<StaticBillPrintJob>() {
    public StaticBillPrintJob createFromParcel(Parcel in) {
      return new StaticBillPrintJob(in);
    }

    public StaticBillPrintJob[] newArray(int size) {
      return new StaticBillPrintJob[size];
    }
  };

  protected StaticBillPrintJob(Parcel in) {
    super(in);
    Bundle bundle = in.readBundle(((Object)this).getClass().getClassLoader()); // needed otherwise BadParcelableException: ClassNotFoundException when unmarshalling
    itemIds = bundle.getStringArrayList(BUNDLE_KEY_ITEM_IDS);
    markPrinted = bundle.getBoolean(BUNDLE_KEY_MARK_PRINTED);
    binName = bundle.getString(BUNDLE_KEY_BIN_NAME);
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
    bundle.putStringArrayList(BUNDLE_KEY_ITEM_IDS, itemIds);
    bundle.putBoolean(BUNDLE_KEY_MARK_PRINTED, markPrinted);
    bundle.putString(BUNDLE_KEY_BIN_NAME, binName);

    dest.writeBundle(bundle);
  }
}
