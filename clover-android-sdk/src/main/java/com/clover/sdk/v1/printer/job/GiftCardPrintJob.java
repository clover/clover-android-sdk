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
