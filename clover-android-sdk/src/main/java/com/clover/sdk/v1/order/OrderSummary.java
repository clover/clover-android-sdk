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

package com.clover.sdk.v1.order;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 *
 */
public class OrderSummary implements Parcelable {
  final public JSONObject mOrder;

  private List<LineItem> mLineItems;
  private List<Adjustment> mAdjustments;

  public OrderSummary(JSONObject summary) throws JSONException {
    mOrder = summary;
  }

  public OrderSummary(String json) throws JSONException {
    mOrder = new JSONObject(json);
  }

  public OrderSummary(Parcel parcel) {
    String json = parcel.readString();
    try {
      mOrder = new JSONObject(json);
    } catch (JSONException e) {
      throw new RuntimeException(e);
    }
  }

  public String getId() {
    return mOrder.optString("id");
  }

  public String getTitle() {
    return mOrder.optString("title");
  }

  public String getNote() {
    return mOrder.optString("note");
  }

  public String getType() {
    return mOrder.optString("orderType");
  }

  public String getState() {
    return mOrder.optString("state");
  }

  public String getPaymentState() {
    return mOrder.optString("paymentState");
  }

  public String getEmployeeName() {
    return mOrder.optString("employeeName");
  }

  public String getCurrency() {
    return mOrder.optString("currency");
  }

  public long getTimestamp() {
    return mOrder.optLong("timestamp", 0);
  }

  public long getModified() {
    return mOrder.optLong("modified", 0);
  }

  public long getTotal() {
    return mOrder.optLong("total", 0);
  }

  public long getPaid() {
    return mOrder.optLong("paid", 0);
  }

  public long getRefunded() {
    return mOrder.optLong("refunded", 0);
  }

  public long getCredited() {
    return mOrder.optLong("credited", 0);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    String json = mOrder.toString();
    parcel.writeString(json);
  }

  public static final android.os.Parcelable.Creator<OrderSummary> CREATOR = new Parcelable.Creator<OrderSummary>() {
    @Override
    public OrderSummary createFromParcel(Parcel parcel) {
      return new OrderSummary(parcel);
    }

    @Override
    public OrderSummary[] newArray(int size) {
      return new OrderSummary[size];
    }
  };
}
