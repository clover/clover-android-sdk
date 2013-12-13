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

import android.accounts.Account;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Currency;
import java.util.TimeZone;

/**
 * A class representing a merchant. Instances of this class are immutable.
 */
public class Merchant implements Parcelable {
  private static final String KEY_ID = "id";
  private static final String KEY_NAME = "name";
  private static final String KEY_CURRENCY = "defaultCurrency";
  private static final String KEY_TIMEZONE = "timeZone";
  private static final String KEY_ACCOUNT = "account";
  private static final String KEY_DEVICE_ID = "deviceId";
  private static final String KEY_PHONE_NUMBER = "phoneNumber";
  private static final String KEY_IS_VAT = "isVat";

  private final Bundle data;

  public Merchant(Bundle data, Bundle localData) {
    this.data = data;
    this.data.putAll(localData);
  }

  public Merchant(Parcel in) {
    this.data = in.readBundle();
  }

  /**
   * Get the merchant ID.
   */
  public String getId() {
    return data.getString(KEY_ID, null);
  }

  /**
   * Get the merchant name.
   */
  public String getName() {
    return data.getString(KEY_NAME, null);
  }

  /**
   * Get the merchant currency.
   */
  public Currency getCurrency() {
    String code = data.getString(KEY_CURRENCY, "USD");
    return Currency.getInstance(code);
  }

  /**
   * Get the merchant time zone.
   */
  public TimeZone getTimeZone() {
    String id = data.getString(KEY_TIMEZONE, "USD");
    return TimeZone.getTimeZone(id);
  }

  /**
   * Get the merchant account.
   */
  public Account getAccount() {
    return data.getParcelable(KEY_ACCOUNT);
  }

  /**
   * Get the merchant address.
   */
  public MerchantAddress getAddress() {
    return new MerchantAddress(data);
  }

  /**
   * Get the device ID.
   */
  public String getDeviceId() {
    return data.getString(KEY_DEVICE_ID, null);
  }

  /**
   * Get the merchant phone number.
   */
  public String getPhoneNumber() {
    return data.getString(KEY_PHONE_NUMBER, null);
  }

  /**
   * Returns whether this merchant is in a region using VAT
   */
  public boolean isVat() {
    return data.getBoolean(KEY_IS_VAT, false);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeBundle(data);
  }

  public static final Parcelable.Creator<Merchant> CREATOR = new Parcelable.Creator<Merchant>() {
    public Merchant createFromParcel(Parcel in) {
      return new Merchant(in);
    }

    public Merchant[] newArray(int size) {
      return new Merchant[size];
    }
  };

  @Override
  public String toString() {
    return String.format("%s{id=%s, name=%s, currency=%s, timeZone=%s (%s), account=%s, address=%s, deviceId=%s, phoneNumber=%s}", getClass().getSimpleName(), getId(), getName(), getCurrency(), getTimeZone().getDisplayName(), getTimeZone().getID(), getAccount().toString(), getAddress().toString(), getDeviceId(), getPhoneNumber());
  }

}
