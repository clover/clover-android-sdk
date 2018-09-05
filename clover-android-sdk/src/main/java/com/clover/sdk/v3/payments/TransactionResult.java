package com.clover.sdk.v3.payments;

import android.os.Parcel;
import android.os.Parcelable;

public enum TransactionResult implements Parcelable {
  APPROVED((byte)0x00),
  DECLINED((byte)0x01),
  ABORTED((byte)0x02),
  VOICE_AUTHORISATION((byte)0x03),
  PAYMENT_PART_ONLY((byte)0x04),
  PARTIALLY_APPROVED((byte)0x05),
  NONE((byte)0x99);

  final byte value;

  TransactionResult(byte value) {
    this.value = value;
  }

  TransactionResult(Parcel in) {
    value = in.readByte();
  }

  public static TransactionResult valueOf(byte value) {
    for (TransactionResult tmp : values()) {
      if (tmp.value == value) {
        return tmp;
      }
    }
    return null;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeByte(value);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<TransactionResult> CREATOR = new Creator<TransactionResult>() {
    @Override
    public TransactionResult createFromParcel(Parcel in) {
      return TransactionResult.valueOf(in.readByte());
    }

    @Override
    public TransactionResult[] newArray(int size) {
      return new TransactionResult[size];
    }
  };
}
