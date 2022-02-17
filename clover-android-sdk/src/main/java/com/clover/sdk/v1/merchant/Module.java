/*
 * Copyright (C) 2016 Clover Network, Inc.
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

/**
 * Modules control which software features a merchant has access to and which apps a merchant may
 * install and use. The modules enabled for a particular merchant are defined by the plan that a
 * merchant is currently enrolled in. See
 * <a href="https://docs.clover.com/clover-platform/docs/understanding-modules">Clover Docs:
 * Understanding Modules</a> for detailed information about plans and modules.
 * <p/>
 * App developers can choose which modules they consider required for their app on the developer app
 * dashboard. Merchants will not be able to purchase or install an app unless their plan has all the
 * required modules for that particular app.
 * <p/>
 * An app developer may choose to not require a module but could instead detect the enabled modules
 * at runtime to show or hide software features.
 * <p/>
 * Modules <b>do not</b> affect API functionality. For example, assuming an app has proper
 * permissions, an app may still use the inventory APIs for a merchant that does not have the
 * "ITEMS" module enabled.
 *
 * @see Merchant#getModules()
 * @see MerchantConnector
 */
public enum Module implements Parcelable {
  MERCHANT,
  PAYMENTS,
  EMPLOYEES,
  PRINTERS,
  CUSTOMERS,
  ORDERS,
  ITEMS,
  CASH,
  DISCOUNTS,
  TAX_RATES,
  ORDER_TYPES,
  ORDER_PRINTERS,
  ITEM_MODIFIERS,
  SERVICE_CHARGES,
  COMBINE_ASSIGN_ORDERS,
  AUTOMATIC_DISCOUNTS,
  WEIGH_PER_UNIT_ITEMS,
  ITEM_VARIANTS,
  TABLE_MANAGEMENT,
  ITEM_EXCHANGES,
  CUSTOMER_PREFERENCES_PROMOTIONS,
  BAR_TAB_MANAGEMENT,
  RESTAURANT_SHIFT_MANAGEMENT,
  FISCAL_PRINTERS,
  PHONE_SALES,
  REGISTER_PREAUTH,
  FAST_PAY,
  TABLE_SERVICE,
  COST_OF_GOODS,
  ADVANCED_REPORTING,
  ;

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
