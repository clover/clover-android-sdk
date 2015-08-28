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
package com.clover.sdk.v1.printer.job;

import com.clover.sdk.v1.printer.Category;
import com.clover.sdk.v3.payments.GiftCardResponse;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class GiftCardPrintJob extends PrintJob implements Parcelable {
  private static final String BUNDLE_GIFT_CARD_RESPONSE = "g";

  public final GiftCardResponse giftCardResponse;

  public static class Builder extends PrintJob.Builder {
    protected GiftCardResponse giftCardResponse;

    public Builder giftCardResponse(GiftCardResponse response) {
      this.giftCardResponse = response;
      return this;
    }

    @Override
    public PrintJob build() {
      return new GiftCardPrintJob(this);
    }
  }

  @Deprecated
  public GiftCardPrintJob(int flags, GiftCardResponse giftCardResponse) {
    super(flags);
    this.giftCardResponse = giftCardResponse;
  }

  protected  GiftCardPrintJob(Builder builder) {
    super(builder);
    this.giftCardResponse = builder.giftCardResponse;
  }

  public GiftCardPrintJob(Parcel in) {
    super(in);
    Bundle bundle = in.readBundle(((Object)this).getClass().getClassLoader());
    giftCardResponse = bundle.getParcelable(BUNDLE_GIFT_CARD_RESPONSE);
  }

  public static final Parcelable.Creator<GiftCardPrintJob> CREATOR
      = new Parcelable.Creator<GiftCardPrintJob>() {
    public GiftCardPrintJob createFromParcel(Parcel in) {
      return new GiftCardPrintJob(in);
    }

    public GiftCardPrintJob[] newArray(int size) {
      return new GiftCardPrintJob[size];
    }
  };

  @Override
  public Category getPrinterCategory() {
    return Category.RECEIPT;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    Bundle bundle = new Bundle();
    bundle.putParcelable(BUNDLE_GIFT_CARD_RESPONSE, giftCardResponse);
    dest.writeBundle(bundle);
  }
}
