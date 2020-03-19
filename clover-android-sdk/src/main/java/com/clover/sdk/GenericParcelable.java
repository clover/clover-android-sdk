/*
 * Copyright (C) 2019 Clover Network, Inc.
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
package com.clover.sdk;

import android.os.Parcelable;

/**
 * For Clover internal use only.
 * <p>
 * There are two copies of this file, one in clover-android-sdk and one in
 * schema-tool, please keep them in sync.
 */
public abstract class GenericParcelable implements Parcelable {

  /**
   * Gets a Bundle which can be used to get and set data attached to this instance. The attached Bundle will be
   * parcelled but not jsonified.
   */
  public final android.os.Bundle getBundle() {
    return getGenericClient().getBundle();
  }

  @Override
  public final String toString() {
    return getGenericClient().toString();
  }

  @Override
  public final int describeContents() {
    return 0;
  }

  @Override
  public final void writeToParcel(android.os.Parcel dest, int flags) {
    getGenericClient().writeToParcel(dest, flags);
  }

  protected abstract GenericClient getGenericClient();

}
