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

import com.clover.sdk.v1.inventory.TaxRate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * A class representing an adjustment associated with an order or a line item. Instances of this object are returned
 * by the {@link com.clover.sdk.v1.order.IOrderService#addAdjustment(String orderId, com.clover.sdk.v1.inventory.Discount discount,
 * String note, ResultStatus resultStatus)} method or as part of a
 * {@link com.clover.sdk.v1.order.Order} object.
 */
public class Adjustment implements Parcelable {
  final public JSONObject mAdjustment;

  List<TaxRate> mTaxRates;

  public Adjustment(JSONObject lineItem) throws JSONException {
    mAdjustment = lineItem;
  }

  public Adjustment(String json) throws JSONException {
    mAdjustment = new JSONObject(json);
  }

  public Adjustment(Parcel parcel) {
    String json = parcel.readString();
    try {
      mAdjustment = new JSONObject(json);
    } catch (JSONException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Unique identifier
   */
  public String getId() {
    return mAdjustment.optString("id");
  }

  /**
   * discount id
   */
  public String getDiscountId() {
    return mAdjustment.optString("discountId");
  }

  /**
   * Name of the adjustment
   */
  public String getName() {
    return mAdjustment.optString("name");
  }

  /**
   * Amount of the adjust, typically in cents; use priceType and merchant currency to determine actual item price
   */
  public long getAmount() {
    return mAdjustment.optLong("amount", 0);
  }

  public long getPercentage() {
    return mAdjustment.optLong("percentage", 0);
  }

  public String getType() {
    return mAdjustment.optString("type");
  }

  public String getNote() {
    return mAdjustment.optString("note");
  }

  public boolean hasAmount() {
    return mAdjustment.has("amount");
  }

  public boolean hasPercentage() {
    return mAdjustment.has("percentage");
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    String json = mAdjustment.toString();
    parcel.writeString(json);
  }

  public static final android.os.Parcelable.Creator<Adjustment> CREATOR = new Parcelable.Creator<Adjustment>() {
    @Override
    public Adjustment createFromParcel(Parcel parcel) {
      return new Adjustment(parcel);
    }

    @Override
    public Adjustment[] newArray(int size) {
      return new Adjustment[size];
    }
  };
}
