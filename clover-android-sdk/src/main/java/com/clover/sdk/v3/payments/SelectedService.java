package com.clover.sdk.v3.payments;

import android.os.Parcel;
import android.os.Parcelable;

public enum SelectedService implements Parcelable {
  NONE((byte) 0x00),
  PAYMENT((byte) 0x01),
  REFUND((byte) 0x02),
  CANCELLATION((byte) 0x03),
  PRE_AUTH((byte) 0x04),
  UPDATE_PRE_AUTH((byte) 0x05),
  PAYMENT_COMPLETION((byte) 0x06),
  CASH_ADVANCE((byte) 0x07),
  DEFERRED_PAYMENT((byte) 0x08),
  DEFERRED_PAYMENT_COMPLETION((byte) 0x09),
  VOICE_AUTHORISATION((byte) 0x10),
  CARDHOLDER_DETECTION((byte) 0x11),
  // No NEXO-Services (default mode)
  TOKEN_REQUEST((byte) 0x50),
  VERIFICATION((byte) 0x51);

  final byte value;

  SelectedService(byte value) {
    this.value = value;
  }

  SelectedService(Parcel in) {
    value = in.readByte();
  }

  public static SelectedService valueOf(byte value) {
    for (SelectedService tmp : values()) {
      if (tmp.value == value) {
        return tmp;
      }
    }
    return null;
  }

  public byte getValue() {
    return value;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeByte(value);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<SelectedService> CREATOR = new Creator<SelectedService>() {
    @Override
    public SelectedService createFromParcel(Parcel in) {
      return SelectedService.valueOf(in.readByte());
    }

    @Override
    public SelectedService[] newArray(int size) {
      return new SelectedService[size];
    }
  };
}
