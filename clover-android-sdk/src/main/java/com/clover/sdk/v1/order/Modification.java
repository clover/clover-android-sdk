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

/**
 * A class representing an modification associated with an order or a line item. Instances of this object are returned
 * by the {@link com.clover.sdk.v1.order.OrderConnector#addLineItemModification(String, String, com.clover.sdk.v1.inventory.Modifier, com.clover.sdk.v1.ServiceConnector.Callback)}
 * or as part of a {@link com.clover.sdk.v1.order.Order} object.
 */
public class Modification implements Parcelable {
  public static final String ID = "id";
  public static final String MODIFIER_ID = "modifierId";
  public static final String AMOUNT = "amount";
  public static final String NAME = "name";
  public static final String ALTERNATE_NAME = "alternateName";

  public String id;
  public String modifierId;
  public Long amount;
  public String name;
  public String alternateName;

  final protected JSONObject mModification;

  public Modification() {
    this(new JSONObject());
  }

  public Modification(JSONObject modification) {
    mModification = modification;

    id = mModification.optString(ID);
    modifierId = mModification.optString(MODIFIER_ID);
    if (mModification.has(AMOUNT)) {
      amount = mModification.optLong(AMOUNT, 0);
    }
    name = mModification.optString(NAME);
    alternateName = mModification.optString(ALTERNATE_NAME);
  }

  public Modification(String json) throws JSONException {
    this(new JSONObject(json));
  }

  public Modification(Parcel parcel) throws JSONException {
    this(new JSONObject(parcel.readString()));
  }

  /**
   * Unique identifier
   */
  public String getId() {
    return id;
  }

  /**
   * discount id
   */
  public String getModifierId() {
    return modifierId;
  }

  /**
   * Name of the adjustment
   */
  public String getName() {
    return name;
  }

  /**
   * Name of the adjustment
   */
  public String getAlternateName() {
    return alternateName;
  }

  /**
   * Amount of the modification, typically in cents; use priceType and merchant currency to determine actual item price
   */
  public Long getAmount() {
    return amount;
  }

  public JSONObject getJSONObject() {
    try {
      mModification.put(ID, id);
      mModification.put(MODIFIER_ID, modifierId);
      if (amount != null) {
        mModification.put(AMOUNT, amount);
      } else {
        mModification.remove(AMOUNT);
      }
      mModification.put(NAME, alternateName);
      mModification.put(ALTERNATE_NAME, name);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return mModification;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    String json = getJSONObject().toString();
    parcel.writeString(json);
  }

  public static final android.os.Parcelable.Creator<Modification> CREATOR = new Parcelable.Creator<Modification>() {
    @Override
    public Modification createFromParcel(Parcel parcel) {
      try {
        return new Modification(parcel);
      } catch (JSONException e) {
        e.printStackTrace();
      }

      return null;
    }

    @Override
    public Modification[] newArray(int size) {
      return new Modification[size];
    }
  };
}
