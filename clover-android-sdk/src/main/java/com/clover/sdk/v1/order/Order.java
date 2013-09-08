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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Order implements Parcelable {
  final public JSONObject mOrder;

  private List<LineItem> mLineItems;

  public Order(JSONObject order) throws JSONException {
    mOrder = order;
  }

  public Order(String json) throws JSONException {
    mOrder = new JSONObject(json);
  }

  public Order(Parcel parcel) {
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
    return mOrder.optString("type");
  }

  public String getState() {
    return mOrder.optString("state");
  }

  public String getPaymentState() {
    return mOrder.optString("paymentState");
  }

  public String getEmployeeId() {
    return mOrder.optString("employeeId");
  }

  public long getTotal() {
    return mOrder.optLong("total", 0);
  }

  public boolean getTaxRemoved() {
    return mOrder.optBoolean("taxRemoved", false);
  }

  public boolean getGroupLineItems() {
    return mOrder.optBoolean("groupLineItems", false);
  }

  public List<LineItem> getLineItems() {
    if (mLineItems == null) {
      try {
        JSONArray li = mOrder.optJSONArray("lineItems");

        if (li != null) {
          List<LineItem> lineItems = new ArrayList<LineItem>(li.length());
          for (int i=0; i < li.length(); i++) {
            lineItems.add(new LineItem(li.getJSONObject(i)));
          }
          mLineItems = lineItems;
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }

    return mLineItems;
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

  public static final android.os.Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
    @Override
    public Order createFromParcel(Parcel parcel) {
      return new Order(parcel);
    }

    @Override
    public Order[] newArray(int size) {
      return new Order[size];
    }
  };
}
