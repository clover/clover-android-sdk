/**
 * Copyright (C) 2016 Clover Network, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *    http://www.apache.org/licenses/LICENSE-2.0
 * <p>
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
  private static final String BUNDLE_KEY_BANNER = "banner";

  public static class Builder extends StaticOrderBasedPrintJob.Builder {
    protected ArrayList<String> itemIds;
    private boolean reprintAllowed = false;
    private boolean markPrinted = false;
    private String banner = null;

    public Builder staticOrderPrintJob(StaticOrderPrintJob pj) {
      staticOrderBasedPrintJob(pj);
      this.itemIds = pj.itemIds;
      this.reprintAllowed = pj.reprintAllowed;
      this.markPrinted = pj.markPrinted;
      this.banner = pj.banner;

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

    /**
     * Sets the custom banner text to be printed on the receipt.
     *
     * @param banner the banner text to display on the printed output, or null for no banner
     * @return this Builder instance for method chaining
     */
    public Builder banner(String banner) {
      this.banner = banner;
      return this;
    }

    public StaticOrderPrintJob build() {
      return new StaticOrderPrintJob(this);
    }
  }

  public final ArrayList<String> itemIds;
  public final boolean reprintAllowed;
  public final boolean markPrinted;
  /**
   * Custom banner text to be printed on the receipt or label.
   * This text will be displayed as a banner on the printed output.
   */
  public final String banner;

  /**
   * Creates a new StaticOrderPrintJob instance.
   *
   * @param order the order to print
   * @param itemIds optional list of specific item IDs to print, or null to print all items
   * @param reprintAllowed whether reprinting is allowed for this job
   * @param flags additional flags for the print job
   * @param markPrinted whether to mark items as printed after successful print
   * @param banner custom banner text to display on the printed output, or null for no banner
   * @deprecated Use {@link Builder} instead to construct instances
   */
  @Deprecated
  public StaticOrderPrintJob(Order order, ArrayList<String> itemIds, boolean reprintAllowed, int flags, boolean markPrinted, String banner) {
    super(order, flags);
    this.itemIds = itemIds;
    this.reprintAllowed = reprintAllowed;
    this.markPrinted = markPrinted;
    this.banner = banner;
  }

  protected StaticOrderPrintJob(Builder builder) {
    super(builder);
    this.itemIds = builder.itemIds;
    this.reprintAllowed = builder.reprintAllowed;
    this.markPrinted = builder.markPrinted;
    this.banner = builder.banner;
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
    banner = bundle.getString(BUNDLE_KEY_BANNER);
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
    bundle.putString(BUNDLE_KEY_BANNER, banner);

    dest.writeBundle(bundle);
  }
}
