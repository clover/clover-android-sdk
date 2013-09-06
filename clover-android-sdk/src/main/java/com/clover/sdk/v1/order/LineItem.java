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

  public String getId() {
    return mLineItem.optString("id");
  }

  public String getItemId() {
    return mLineItem.optString("itemId");
  }

  public String getName() {
    return mLineItem.optString("name");
  }

  public long getPrice() {
    return mLineItem.optLong("price", 0);
  }

  public int getUnitQuantity() {
    return mLineItem.optInt("unitQty", 0);
  }

  public String getUnitName() {
    return mLineItem.optString("unitName");
  }

  public long getTaxRate() {
    return mLineItem.optLong("taxRate", 0);
  }

  public String getProductCode() {
    return mLineItem.optString("productCode");
  }

  public String getAlternateName() {
    return mLineItem.optString("alternateName");
  }

  public String getBinName() {
    return mLineItem.optString("binName");
  }

  public String getUserData() {
    return mLineItem.optString("userData");
  }

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
