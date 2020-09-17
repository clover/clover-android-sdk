/*
 * Copyright (C) 2020 Clover Network, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clover.sdk.cashdrawer;

import android.os.Bundle;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * A cash drawer managed by Clover Services. Instances are created by Clover and should not
 * be created by third party apps.
 */
public class CloverServiceCashDrawer extends CashDrawer implements Parcelable {

  private static final String KEY_CASH_DRAWER_IDENTIFIER = "id";
  private static final String KEY_CASH_DRAWER_NUMBER = "num";
  private static final String KEY_CASH_DRAWER_DISPLAY_NAME = "displayName";

  private final String identifier;
  private final String displayName;

  /**
   * For internal use only.
   */
  public CloverServiceCashDrawer(String identifier, int drawerNumber, String displayName) {
    super(drawerNumber);
    this.identifier = identifier;
    this.displayName = displayName;
  }

  @Override
  public String getIdentifier() {
    return identifier;
  }

  @Override
  public String getDisplayName() {
    return displayName;
  }

  @Override
  public boolean pop() {
    if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
      Log.w(TAG, "pop method must not be invoked on the main thread",
          new Exception().fillInStackTrace());
    }

    Bundle extras = new Bundle();
    extras.putParcelable(CashDrawers.Contract.EXTRA_CLOVER_SERVICE_CASH_DRAWER, this);
    Bundle result = new CashDrawers(context).call(CashDrawers.Contract.METHOD_POP, null, extras);
    return result != null && result.getBoolean(CashDrawers.Contract.EXTRA_SUCCESS, false);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    Bundle bundle = new Bundle();
    bundle.putString(KEY_CASH_DRAWER_IDENTIFIER, identifier);
    bundle.putString(KEY_CASH_DRAWER_DISPLAY_NAME, displayName);
    bundle.putInt(KEY_CASH_DRAWER_NUMBER, drawerNumber);
    dest.writeBundle(bundle);
  }

  public static final Parcelable.Creator<CloverServiceCashDrawer> CREATOR = new Parcelable.Creator<CloverServiceCashDrawer>() {
    @Override
    public CloverServiceCashDrawer createFromParcel(Parcel in) {
      Bundle bundle = in.readBundle(getClass().getClassLoader());
      String identifier = bundle.getString(KEY_CASH_DRAWER_IDENTIFIER, "");
      String displayName = bundle.getString(KEY_CASH_DRAWER_DISPLAY_NAME, identifier);
      int drawerNum = bundle.getInt(KEY_CASH_DRAWER_NUMBER, 1);
      return new CloverServiceCashDrawer(identifier, drawerNum, displayName);
    }

    @Override
    public CloverServiceCashDrawer[] newArray(int size) {
      return new CloverServiceCashDrawer[size];
    }
  };

  @Override
  public String toString() {
    return "CloverServiceCashDrawer{" +
           "identifier='" + identifier + '\'' +
           ", displayName='" + displayName + '\'' +
           ", drawerNumber=" + drawerNumber +
           '}';
  }

}
