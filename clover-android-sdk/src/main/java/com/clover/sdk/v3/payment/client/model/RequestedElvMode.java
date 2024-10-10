/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */


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

package com.clover.sdk.v3.payment.client.model;

import android.os.Parcelable;
import android.os.Parcel;

/**
 * This is an auto-generated Clover data enum.
 * The ELV transaction type. Automatic means the terminal will choose between ELV offline, ELV online, or EMV using a GiroCard.
 */
@SuppressWarnings("all")
public enum RequestedElvMode implements Parcelable {
  AUTOMATIC, ELV_OFFLINE, ELV_ONLINE, GIROCARD;

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(final Parcel dest, final int flags) {
    dest.writeString(name());
  }

  public static final Creator<RequestedElvMode> CREATOR = new Creator<RequestedElvMode>() {
    @Override
    public RequestedElvMode createFromParcel(final Parcel source) {
      return RequestedElvMode.valueOf(source.readString());
    }

    @Override
    public RequestedElvMode[] newArray(final int size) {
      return new RequestedElvMode[size];
    }
  };
}
