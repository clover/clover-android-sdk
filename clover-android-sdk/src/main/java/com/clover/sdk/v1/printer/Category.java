/**
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
package com.clover.sdk.v1.printer;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public enum Category implements Parcelable {

  ORDER, RECEIPT, FISCAL, LABEL;

  private static final String TAG = Category.class.getSimpleName();

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(final Parcel dest, final int flags) {
    dest.writeString(name());
  }

  public static final Creator<Category> CREATOR = new Creator<Category>() {
    @Override
    public Category createFromParcel(final Parcel source) {
      String catStr = source.readString();

      Category category = null;
      try {
        category = Category.valueOf(catStr);
      } catch (IllegalArgumentException e) {
        Log.w(TAG, "Unknown category " + catStr + ", returning null");
      }

      return category;
    }

    @Override
    public Category[] newArray(final int size) {
      return new Category[size];
    }
  };

}
