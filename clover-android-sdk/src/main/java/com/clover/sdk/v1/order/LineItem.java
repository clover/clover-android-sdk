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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing an line item associated with an order. Instances of this object are returned
 * by the {@link com.clover.sdk.v1.order.IOrderService#addLineItem(String orderId, com.clover.sdk.v1.inventory.Item item,
 * int unitQuantity, String binName, String userData, ResultStatus resultStatus)} method or as part of a
 * {@link com.clover.sdk.v1.order.Order} object.
 */
public class LineItem implements Parcelable {
  final public JSONObject mLineItem;

  List<TaxRate> mTaxRates;

  public LineItem(JSONObject lineItem) throws JSONException {
    mLineItem = lineItem;
  }

  public LineItem(String json) throws JSONException {
    mLineItem = new JSONObject(json);
  }

  public LineItem(Parcel parcel) {
    String json = parcel.readString();
    try {
      mLineItem = new JSONObject(json);
    } catch (JSONException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Unique identifier
   */
  public String getId() {
    return mLineItem.optString("id");
  }

  /**
   * item id
   */
  public String getItemId() {
    return mLineItem.optString("itemId");
  }

  /**
   * Name of the line item
   */
  public String getName() {
    return mLineItem.optString("name");
  }

  /**
   * Price of the line item, typically in cents; use priceType and merchant currency to determine actual item price
   */
  public long getPrice() {
    return mLineItem.optLong("price", 0);
  }

  /**
   * Quantity of item for items of type PriceType.PER_UNIT
   */
  public int getUnitQuantity() {
    return mLineItem.optInt("unitQty", 0);
  }

  /**
   * Unit Name for items of type PriceType.PER_UNIT
   */
  public String getUnitName() {
    return mLineItem.optString("unitName");
  }

  /**
   * Line item tax rate
   */
  public long getTaxRate() {
    return mLineItem.optLong("taxRate", 0);
  }

  /**
   * Product code, e.g. UPC or EAN
   */
  public String getProductCode() {
    return mLineItem.optString("productCode");
  }

  /**
   * Alternate name of the line item
   */
  public String getAlternateName() {
    return mLineItem.optString("alternateName");
  }

  public String getBinName() {
    return mLineItem.optString("binName");
  }

  public String getUserData() {
    return mLineItem.optString("userData");
  }

  /**
   * Line item tax rates
   * @return
   */
  public List<TaxRate> getTaxRates() {
    if (mTaxRates == null) {
      try {
        JSONArray li = mLineItem.optJSONArray("taxRates");

        if (li != null) {
          List<TaxRate> taxRates = new ArrayList<TaxRate>(li.length());
          for (int i=0; i < li.length(); i++) {
            taxRates.add(new TaxRate(li.getJSONObject(i)));
          }
          mTaxRates = taxRates;
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }

    return mTaxRates;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    String json = mLineItem.toString();
    parcel.writeString(json);
  }

  public static final android.os.Parcelable.Creator<LineItem> CREATOR = new Parcelable.Creator<LineItem>() {
    @Override
    public LineItem createFromParcel(Parcel parcel) {
      return new LineItem(parcel);
    }

    @Override
    public LineItem[] newArray(int size) {
      return new LineItem[size];
    }
  };
}
