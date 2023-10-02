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

package com.clover.sdk.v3.payments;

@SuppressWarnings("all")
public class AuthorizationFdParcelable extends com.clover.sdk.FdParcelable<com.clover.sdk.v3.payments.Authorization> {
    public AuthorizationFdParcelable(com.clover.sdk.v3.payments.Authorization value) {
        super(value);
    }

    public AuthorizationFdParcelable(android.os.Parcel in) {
        super(in);
    }

    public static final android.os.Parcelable.Creator<AuthorizationFdParcelable> CREATOR = new android.os.Parcelable.Creator<AuthorizationFdParcelable>() {
        @Override
        public AuthorizationFdParcelable createFromParcel(android.os.Parcel in) {
            return new AuthorizationFdParcelable(in);
        }

        @Override
        public AuthorizationFdParcelable[] newArray(int size) {
            return new AuthorizationFdParcelable[size];
        }
    };
}
