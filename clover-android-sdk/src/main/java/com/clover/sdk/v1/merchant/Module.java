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
package com.clover.sdk.v1.merchant;

import android.os.Parcel;
import android.os.Parcelable;

public enum Module implements Parcelable {
  MERCHANT, PAYMENTS, EMPLOYEES, PRINTERS, CUSTOMERS, ORDERS, ITEMS, CASH, DISCOUNTS, TAX_RATES;

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(final Parcel dest, final int flags) {
    dest.writeString(name());
  }

  public static final Creator<Module> CREATOR = new Creator<Module>() {
    @Override
    public Module createFromParcel(final Parcel source) {
      return Module.valueOf(source.readString());
    }

    @Override
    public Module[] newArray(final int size) {
      return new Module[size];
    }
  };
}
