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

package com.clover.sdk.v3.order;

import android.os.Parcelable;
import android.os.Parcel;

/**
 * This is an auto-generated Clover data enum.
 * Detailed void enum from device.
 */
@SuppressWarnings("all")
public enum VoidReasonCode implements Parcelable {
  CANCEL_PAYMENT_ADJUST, BREAK_RESET, CANCEL, AUTH_FAILED, NETWORK_FAILED, NON_OK_RESULT_CODE_RETURNED_FROM_VERIFY_CVM, PARTIAL_AUTH_NOT_ALLOWED, PARTIAL_AUTH_CANCELLED, QUICK_CHIP_DECLINE, KERNEL_DECLINE, NON_OK_RESULT_CODE_FROM_FORCE_ACCEPT, MERCHANT_CANCELLED_THE_FORCE_ACCEPTANCE, CARD_READER_ERROR, UNKNOWN_EXCEPTION, ERROR_EVENT, KERNEL_EXCEPTION, FATAL_EXCEPTION, CARD_CHECK_FAILED_FOR_REVERSAL, MANUAL_ENTRY_NOT_ALLOWED, MANUAL_ENTRY_NOT_ALLOWED_FOR_CARD, SWIPE_CARD_CHECK_FAILED_FOR_REVERSAL, OFFLINE_AUTH_FAILED, TXN_STATUS_DECLINED, COMPLETION_DEVICE, NON_OK_RESULT_CODE_FROM_DUPLICATE_CHECK, MERCHANT_CANCELLED_THE_DUPLICATE_CHECK, NON_OK_RESULT_CODE_RETURNED_FROM_OFFLINE_SCREEN, MERCHANT_CANCELLED_OFFLINE_PAYMENT, OFFLINE_AUTH_FAILED_ON_CARD_PRESENT_REVERSAL, QUICK_CHIP_TIME_OUT, HANDLE_PAY_STATE, HANDLE_CONTACT_REQUIRED, FINALIZE, HANDLE_CARD_DECLINED_TXN_STATUS;

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(final Parcel dest, final int flags) {
    dest.writeString(name());
  }

  public static final Creator<VoidReasonCode> CREATOR = new Creator<VoidReasonCode>() {
    @Override
    public VoidReasonCode createFromParcel(final Parcel source) {
      return VoidReasonCode.valueOf(source.readString());
    }

    @Override
    public VoidReasonCode[] newArray(final int size) {
      return new VoidReasonCode[size];
    }
  };
}
