package com.clover.sdk.v3.payments;

import android.os.Parcel;
import android.os.Parcelable;

public enum TxFormat implements Parcelable {
  DEFAULT,
  NEXO;

  TxFormat() {}

  TxFormat(Parcel in) {
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(name());
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<TxFormat> CREATOR = new Creator<TxFormat>() {
    @Override
    public TxFormat createFromParcel(Parcel in) {
      return TxFormat.valueOf(in.readString());
    }

    @Override
    public TxFormat[] newArray(int size) {
      return new TxFormat[size];
    }
  };
}
