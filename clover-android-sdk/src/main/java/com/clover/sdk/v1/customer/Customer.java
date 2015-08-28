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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class representing a customer. Instances of this object are returned
 * by the {@link com.clover.sdk.v1.customer.ICustomerService#createCustomer(String firstName,
 * String lastName, boolean marketingAllowed, ResultStatus resultStatus)},
 * {@link com.clover.sdk.v1.customer.ICustomerService#getCustomer(String customerId, ResultStatus resultStatus)} or
 * {@link com.clover.sdk.v1.customer.ICustomerService#getCustomers(String query,
 * ResultStatus resultStatus)} methods.
 */
public class Customer implements Parcelable {
  private final JSONObject data;


  Customer(String json) throws JSONException {
    this.data = new JSONObject(json);
  }

  Customer(Parcel in) throws JSONException {
    String json = in.readString();
    this.data = new JSONObject(json);
  }

  public Customer(JSONObject data) {
    this.data = data;
  }

  public String getId() {
    return data.optString("id", null);
  }

  public String getFirstName() {
    return data.optString("firstName", null);
  }

  public String getLastName() {
    return data.optString("lastName", null);
  }

  public boolean getMarketingAllowed() {
    return data.optBoolean("marketingAllowed", false);
  }

  public List<PhoneNumber> getPhoneNumbers() {
    try {
      JSONArray array = data.optJSONArray("phoneNumbers");
      if (array != null) {
        List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();
        for (int i = 0; i < array.length(); i++) {
          JSONObject phoneNumberObject = array.getJSONObject(i);
          phoneNumbers.add(new PhoneNumber(phoneNumberObject));
        }
        return phoneNumbers;
      }
    } catch (JSONException ex) {
    }
    return Collections.emptyList();
  }

  public List<EmailAddress> getEmailAddresses() {
    try {
      JSONArray array = data.optJSONArray("emailAddresses");
      if (array != null) {
        List<EmailAddress> emailAddresses = new ArrayList<EmailAddress>();
        for (int i = 0; i < array.length(); i++) {
          JSONObject emailAddressObject = array.getJSONObject(i);
          emailAddresses.add(new EmailAddress(emailAddressObject));
        }
        return emailAddresses;
      }
    } catch (JSONException ex) {
    }
    return Collections.emptyList();
  }

  public List<Address> getAddresses() {
    try {
      JSONArray array = data.optJSONArray("addresses");
      if (array != null) {
        List<Address> addresses = new ArrayList<Address>();
        for (int i = 0; i < array.length(); i++) {
          JSONObject addressObject = array.getJSONObject(i);
          addresses.add(new Address(addressObject));
        }
        return addresses;
      }
    } catch (JSONException ex) {
    }
    return Collections.emptyList();
  }

  public List<Order> getOrders() {
    try {
      JSONArray array = data.optJSONArray("orders");
      if (array != null) {
        List<Order> orders = new ArrayList<Order>();
        for (int i = 0; i < array.length(); i++) {
          JSONObject ordersObject = array.getJSONObject(i);
          orders.add(new Order(ordersObject));
        }
        return orders;
      }
    } catch (JSONException ex) {
    }
    return Collections.emptyList();
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

  public static final Parcelable.Creator<Customer> CREATOR = new Parcelable.Creator<Customer>() {
    public Customer createFromParcel(Parcel in) {
      try {
        return new Customer(in);
      } catch (JSONException e) {
        throw new RuntimeException(e);
      }
    }

    public Customer[] newArray(int size) {
      return new Customer[size];
    }
  };
}