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
 * A class representing an email address associated with a customer. Instances of this object are returned
 * by the {@link com.clover.sdk.v1.customer.ICustomerService#addEmailAddress(String customerId,
 * String emailAddress, ResultStatus resultStatus)} method or as part of a
 * {@link com.clover.sdk.v1.customer.Customer} object.
 */
public class EmailAddress implements Parcelable {
  public static class Builder {
    private String id = null;
    private String emailAddress = null;

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder emailAddress(String emailAddress) {
      this.emailAddress = emailAddress;
      return this;
    }

    public EmailAddress build() {
      return new EmailAddress(id, emailAddress);
    }
  }

  private final JSONObject data;

  private EmailAddress(String id, String emailAddress) {
    data = new JSONObject();
    try {
      data.put("id", id);
      data.put("emailAddress", emailAddress);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  EmailAddress(String json) throws JSONException {
    this.data = new JSONObject(json);
  }

  EmailAddress(Parcel in) throws JSONException {
    String json = in.readString();
    this.data = new JSONObject(json);
  }

  public EmailAddress(JSONObject data) {
    this.data = data;
  }

  public String getId() {
    return data.optString("id", null);
  }

  public String getEmailAddress() {
    return data.optString("emailAddress", null);
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

  public static final Creator<EmailAddress> CREATOR = new Creator<EmailAddress>() {
    public EmailAddress createFromParcel(Parcel in) {
      try {
        return new EmailAddress(in);
      } catch (JSONException e) {
        throw new RuntimeException(e);
      }
    }

    public EmailAddress[] newArray(int size) {
      return new EmailAddress[size];
    }
  };
}