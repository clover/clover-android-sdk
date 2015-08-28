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

public class StaticOrderPrintJob extends StaticOrderBasedPrintJob implements Parcelable {
  private static final String BUNDLE_KEY_ITEM_IDS = "i";
  private static final String BUNDLE_REPRINT_ALLOWED = "b";
  private static final String BUNDLE_KEY_MARK_PRINTED = "m";

  public static class Builder extends StaticOrderBasedPrintJob.Builder {
    protected ArrayList<String> itemIds;
    private boolean reprintAllowed = false;
    private boolean markPrinted = false;

    public Builder staticOrderPrintJob(StaticOrderPrintJob pj) {
      staticOrderBasedPrintJob(pj);
      this.itemIds = pj.itemIds;
      this.reprintAllowed = pj.reprintAllowed;
      this.markPrinted = pj.markPrinted;

      return this;
    }

    public Builder itemIds(ArrayList<String> itemIds) {
      this.itemIds = itemIds;
      return this;
    }

    public Builder reprintAllowed(boolean allowed) {
      this.reprintAllowed = allowed;
      return this;
    }

    public Builder markPrinted(boolean markPrinted) {
      this.markPrinted = markPrinted;
      return this;
    }

    public StaticOrderPrintJob build() {
      return new StaticOrderPrintJob(this);
    }
  }

  public final ArrayList<String> itemIds;
  public final boolean reprintAllowed;
  public final boolean markPrinted;

  @Deprecated
  public StaticOrderPrintJob(Order order, ArrayList<String> itemIds, boolean reprintAllowed, int flags, boolean markPrinted) {
    super(order, flags);
    this.itemIds = itemIds;
    this.reprintAllowed = reprintAllowed;
    this.markPrinted = markPrinted;
  }

  protected StaticOrderPrintJob(Builder builder) {
    super(builder);
    this.itemIds = builder.itemIds;
    this.reprintAllowed = builder.reprintAllowed;
    this.markPrinted = builder.markPrinted;
  }

  public static final Creator<StaticOrderPrintJob> CREATOR = new Creator<StaticOrderPrintJob>() {
    public StaticOrderPrintJob createFromParcel(Parcel in) {
      return new StaticOrderPrintJob(in);
    }

    public StaticOrderPrintJob[] newArray(int size) {
      return new StaticOrderPrintJob[size];
    }
  };

  protected StaticOrderPrintJob(Parcel in) {
    super(in);
    Bundle bundle = in.readBundle(((Object)this).getClass().getClassLoader()); // needed otherwise BadParcelableException: ClassNotFoundException when unmarshalling
    itemIds = bundle.getStringArrayList(BUNDLE_KEY_ITEM_IDS);
    reprintAllowed = bundle.getBoolean(BUNDLE_REPRINT_ALLOWED);
    markPrinted = bundle.getBoolean(BUNDLE_KEY_MARK_PRINTED);
  }

  @Override
  public Category getPrinterCategory() {
    return Category.ORDER;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    Bundle bundle = new Bundle();
    bundle.putStringArrayList(BUNDLE_KEY_ITEM_IDS, itemIds);
    bundle.putBoolean(BUNDLE_REPRINT_ALLOWED, reprintAllowed);
    bundle.putBoolean(BUNDLE_KEY_MARK_PRINTED, markPrinted);

    dest.writeBundle(bundle);
  }
}
