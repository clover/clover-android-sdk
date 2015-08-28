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
package com.clover.sdk.v1.tender;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class Tender implements Parcelable {
  private static final String KEY_ID = "id";
  private static final String KEY_LABEL = "label";
  private static final String KEY_LABEL_KEY = "labelKey";
  private static final String KEY_ENABLED = "enabled";
  private static final String KEY_OPENS_CASH_DRAWER = "opensCashDrawer";
  private static final String KEY_SUPPORTS_TIPPING = "supportsTipping";

  private final Bundle data;

  Tender() {
    this.data = new Bundle();
  }

  Tender(Parcel in) {
    this.data = in.readBundle();
  }

  public Tender(Bundle in) {
    this.data = in;
  }

  public String getId() {
    return data.getString(KEY_ID, null);
  }

  public String getLabel() {
    return data.getString(KEY_LABEL, null);
  }

  public String getLabelKey() {
    return data.getString(KEY_LABEL_KEY, null);
  }

  public boolean getEnabled() {
    return data.getBoolean(KEY_ENABLED, false);
  }

  public boolean getOpensCashDrawer() {
    return data.getBoolean(KEY_OPENS_CASH_DRAWER, false);
  }

  public boolean getSupportsTipping() {
    return data.getBoolean(KEY_SUPPORTS_TIPPING, false);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeBundle(data);
  }

  public static final Creator<Tender> CREATOR = new Creator<Tender>() {
    public Tender createFromParcel(Parcel in) {
      return new Tender(in);
    }

    public Tender[] newArray(int size) {
      return new Tender[size];
    }
  };
}
