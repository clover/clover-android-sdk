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
import android.text.TextUtils;

/**
 * In older versions of the clover-android-sdk this was an enum, but has now been refactored to a
 * class to future-proof it.
 * <p/>
 * Instances of this class contain a single non-human readable String which can be used to uniquely
 * identify the printer type.
 * <p/>
 * Use {@link TypeDetails} to get detailed information about a printer which formerly was available
 * here.
 */
public class Type implements Parcelable {

  /**
   * A non-human readable String identifying the type of printer. An example value is "SEIKO_USB".
   * The value shall not be null or empty.
   */
  public final String name;

  /**
   * This class used to be an enum and to remain semi-compatible retains a compatible method.
   */
  public String name() {
    return name;
  }

  public Type(String name) {
    if (TextUtils.isEmpty(name)) {
      throw new IllegalArgumentException("Type name must be non-empty");
    }
    this.name = name;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(final Parcel dest, final int flags) {
    dest.writeString(name);
  }

  public static final Creator<Type> CREATOR = new Creator<Type>() {
    @Override
    public Type createFromParcel(final Parcel source) {
      return new Type(source.readString());
    }

    @Override
    public Type[] newArray(final int size) {
      return new Type[size];
    }
  };

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Type type = (Type) o;

    return name.equals(type.name);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public String toString() {
    return name;
  }

}
