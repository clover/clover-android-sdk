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
package com.clover.sdk.v1.customer;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A class representing a phone number associated with a customer. Instances of this object are returned
 * by the {@link com.clover.sdk.v1.customer.ICustomerService#addPhoneNumber(String customerId,
 * String emailAddress, ResultStatus resultStatus)} method or as part of a
 * {@link com.clover.sdk.v1.customer.Customer} object.
 */
public class PhoneNumber implements Parcelable {
  public static class Builder {
    private String id = null;
    private String phoneNumber = null;

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder phoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
      return this;
    }

    public PhoneNumber build() {
      return new PhoneNumber(id, phoneNumber);
    }
  }

  private final JSONObject data;

  private PhoneNumber(String id, String phoneNumber) {
    data = new JSONObject();
    try {
      data.put("id", id);
      data.put("phoneNumber", phoneNumber);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  PhoneNumber(String json) throws JSONException {
    this.data = new JSONObject(json);
  }

  PhoneNumber(Parcel in) throws JSONException {
    String json = in.readString();
    this.data = new JSONObject(json);
  }

  public PhoneNumber(JSONObject data) {
    this.data = data;
  }

  public String getId() {
    return data.optString("id", null);
  }

  public String getPhoneNumber() {
    return data.optString("phoneNumber", null);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    String json = data.toString();
    dest.writeString(json);
  }

  public static final Creator<PhoneNumber> CREATOR = new Creator<PhoneNumber>() {
    public PhoneNumber createFromParcel(Parcel in) {
      try {
        return new PhoneNumber(in);
      } catch (JSONException e) {
        throw new RuntimeException(e);
      }
    }

    public PhoneNumber[] newArray(int size) {
      return new PhoneNumber[size];
    }
  };
}