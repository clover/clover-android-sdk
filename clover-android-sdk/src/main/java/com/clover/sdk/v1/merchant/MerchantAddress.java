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

package com.clover.sdk.v1.merchant;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * A class representing a merchant address. Instances of this class are immutable.
 * To construct a new instance of this class, use
 * {@link com.clover.sdk.v1.merchant.MerchantAddress.Builder}.
 */
public class MerchantAddress implements Parcelable {
  /**
   * Builder class for constructing {@link com.clover.sdk.v1.merchant.MerchantAddress}s.
   */
  public static class Builder {
    private String address1 = null;
    private String address2 = null;
    private String city = null;
    private String state = null;
    private String zip = null;
    private String country = null;

    public Builder address1(String address1) {
      this.address1 = address1;
      return this;
    }

    public Builder address2(String address2) {
      this.address2 = address2;
      return this;
    }

    public Builder city(String city) {
      this.city = city;
      return this;
    }

    public Builder state(String state) {
      this.state = state;
      return this;
    }

    public Builder zip(String zip) {
      this.zip = zip;
      return this;
    }

    public Builder country(String country) {
      this.country = country;
      return this;
    }

    public MerchantAddress build() {
      return new MerchantAddress(address1, address2, city, state, zip, country);
    }
  }

  private static final String KEY_ADDRESS1 = "address1";
  private static final String KEY_ADDRESS2 = "address2";
  private static final String KEY_CITY = "city";
  private static final String KEY_STATE = "state";
  private static final String KEY_ZIP = "zip";
  private static final String KEY_COUNTRY = "country";

  private final Bundle data;

  MerchantAddress(String address1, String address2, String city, String state, String zip, String country) {
    this();

    setAddress1(address1);
    setAddress2(address2);
    setCity(city);
    setState(state);
    setZip(zip);
    setCountry(country);
  }

  MerchantAddress(Bundle data) {
    this.data = data;
  }

  /**
   * Constructs a new, empty address.
   */
  public MerchantAddress() {
    this.data = new Bundle();
  }

  MerchantAddress(Parcel in) {
    this.data = in.readBundle();
  }

  /**
   * Returns the first address line of the address.
   */
  public String getAddress1() {
    return data.getString(KEY_ADDRESS1);
  }

  /**
   * Sets the first address line of the address.
   */
  void setAddress1(String address1) {
    data.putString(KEY_ADDRESS1, address1);
  }

  /**
   * Returns the second address line of the address.
   */
  public String getAddress2() {
    return data.getString(KEY_ADDRESS2);
  }

  /**
   * Sets the second address line of the address.
   */
  void setAddress2(String address2) {
    data.putString(KEY_ADDRESS2, address2);
  }

  /**
   * Gets the city of the address.
   */
  public String getCity() {
    return data.getString(KEY_CITY);
  }

  /**
   * Sets the city of the address.
   */
  void setCity(String city) {
    data.putString(KEY_CITY, city);
  }

  /**
   * Gets the state of the address.
   */
  public String getState() {
    return data.getString(KEY_STATE);
  }

  /**
   * Sets the state of the address.
   */
  void setState(String state) {
    data.putString(KEY_STATE, state);
  }

  /**
   * Gets the zip code of the address;
   */
  public String getZip() {
    return data.getString(KEY_ZIP);
  }

  /**
   * Sets the zip code of the address.
   */
  void setZip(String zip) {
    data.putString(KEY_ZIP, zip);
  }

  /**
   * Gets the country of the address.
   */
  public String getCountry() {
    return data.getString(KEY_COUNTRY);
  }

  /**
   * Sets the country of the address.
   */
  void setCountry(String country) {
    data.putString(KEY_COUNTRY, country);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeBundle(data);
  }

  public static final Parcelable.Creator<MerchantAddress> CREATOR = new Parcelable.Creator<MerchantAddress>() {
    public MerchantAddress createFromParcel(Parcel in) {
      return new MerchantAddress(in);
    }

    public MerchantAddress[] newArray(int size) {
      return new MerchantAddress[size];
    }
  };

  @Override
  public String toString() {
    return String.format("%s{address1=%s, address2=%s, city=%s, state=%s, zip=%s, country=%s", getClass().getSimpleName(), getAddress1(), getAddress2(), getCity(), getState(), getZip(), getCountry());
  }
}
