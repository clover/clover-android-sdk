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
import java.util.ArrayList;

public class LineItemOrderPrintJob extends OrderPrintJob implements Parcelable {
  public static class Builder extends OrderPrintJob.Builder {
    protected ArrayList<String> itemIds;

    public Builder itemIds(ArrayList<String> itemIds) {
      this.itemIds = itemIds;
      return this;
    }

    public LineItemOrderPrintJob build() {
      return new LineItemOrderPrintJob(orderId, itemIds, flags);
    }
  }

  public final ArrayList<String> itemIds;
  private static final String BUNDLE_KEY_ITEM_IDS = "i";

  protected LineItemOrderPrintJob(String orderId, ArrayList<String> itemIds, int flags) {
    super(orderId, flags);
    this.itemIds = itemIds;
  }

  public static final Parcelable.Creator<LineItemOrderPrintJob> CREATOR
      = new Parcelable.Creator<LineItemOrderPrintJob>() {
    public LineItemOrderPrintJob createFromParcel(Parcel in) {
      return new LineItemOrderPrintJob(in);
    }

    public LineItemOrderPrintJob[] newArray(int size) {
      return new LineItemOrderPrintJob[size];
    }
  };

  protected LineItemOrderPrintJob(Parcel in) {
    super(in);
    Bundle bundle = in.readBundle();
    itemIds = bundle.getStringArrayList(BUNDLE_KEY_ITEM_IDS);
    // Add more data here, but remember old apps might not provide it!
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    Bundle bundle = new Bundle();
    bundle.putStringArrayList(BUNDLE_KEY_ITEM_IDS, itemIds);
    // Add more stuff here

    dest.writeBundle(bundle);
  }
}
