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
package com.clover.sdk.v3.order;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.clover.core.internal.calc.Decimal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.math.RoundingMode;

public class LineItemPercent implements Parcelable {
  private static final int SCALE = 7;
  private static final Decimal BIG_WHOLE = new Decimal(10000000L);
  private static final Decimal BIG_ONE_HUNDRED = new Decimal(100);

  public static final LineItemPercent ONE_HUNDRED = new LineItemPercent(1);

  final Decimal percent;

  public LineItemPercent(int split) {
    this(1, split);
  }

  public LineItemPercent(long divisor, long dividend) {
    percent = new Decimal(divisor).divide(new Decimal(dividend), SCALE, RoundingMode.HALF_UP);
  }

  public LineItemPercent(long longValue) {
    percent = new Decimal(longValue).divide(BIG_WHOLE, SCALE, RoundingMode.HALF_UP);
  }

  public long longValue() {
    return percent.multiply(BIG_WHOLE).setScale(0, RoundingMode.HALF_UP).longValue();
  }

  public Decimal getValue() {
    return percent;
  }

  public boolean isFractional() {
    return percent.compareTo(Decimal.ONE) < 0;
  }

  public String toDisplayString() {
    return percent.multiply(BIG_ONE_HUNDRED).setScale(0, RoundingMode.HALF_UP).longValue() + "%";
  }

  @Override
  public String toString() {
    return percent.multiply(BIG_ONE_HUNDRED).setScale(SCALE - 2, RoundingMode.HALF_UP).toString();
  }

  public long multiply(long value) {
    Decimal v = new Decimal(value);
    v = v.multiply(percent);
    return v.setScale(0, RoundingMode.HALF_UP).longValue();
  }

  public int describeContents() {
    return 0;
  }

  public void writeToParcel(Parcel out, int flags) {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutput oo = new ObjectOutputStream(baos);
      percent.writeExternal(oo);
      oo.close();
      byte[] bytes = baos.toByteArray();
      out.writeInt(bytes.length);
      out.writeByteArray(bytes);
    } catch (IOException ioe) {
      Log.e("LineItemPercent", "Error in parceling percent object", ioe);
    }
  }

  public static final Parcelable.Creator<LineItemPercent> CREATOR
      = new Parcelable.Creator<LineItemPercent>() {
    public LineItemPercent createFromParcel(Parcel in) {
      return new LineItemPercent(in);
    }

    public LineItemPercent[] newArray(int size) {
      return new LineItemPercent[size];
    }
  };

  private LineItemPercent(Parcel in) {
    try {
      byte[] bytes = new byte[in.readInt()];
      in.readByteArray(bytes);
      ObjectInput oi = new ObjectInputStream(new ByteArrayInputStream(bytes));
      percent = new Decimal(oi);
      oi.close();
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }
}
