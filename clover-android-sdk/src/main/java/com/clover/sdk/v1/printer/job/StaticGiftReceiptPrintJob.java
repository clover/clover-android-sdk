package com.clover.sdk.v1.printer.job;

import android.os.Parcel;
import android.os.Parcelable;
import com.clover.sdk.v1.printer.Category;
import com.clover.sdk.v3.order.Order;

public class StaticGiftReceiptPrintJob extends StaticReceiptPrintJob implements Parcelable {

  public static class Builder extends StaticReceiptPrintJob.Builder {

    public Builder staticGiftReceiptPrintJob(StaticGiftReceiptPrintJob pj) {
      staticReceiptPrintJob(pj);
      return this;
    }

    public StaticGiftReceiptPrintJob build() {
      return new StaticGiftReceiptPrintJob(this);
    }
  }

  @Deprecated
  public StaticGiftReceiptPrintJob(Order order, int flags) {
    super(order, flags);
  }

  protected StaticGiftReceiptPrintJob(Builder builder) {
    super(builder);
  }

  public static final Creator<StaticGiftReceiptPrintJob> CREATOR = new Creator<StaticGiftReceiptPrintJob>() {
    public StaticGiftReceiptPrintJob createFromParcel(Parcel in) {
      return new StaticGiftReceiptPrintJob(in);
    }

    public StaticGiftReceiptPrintJob[] newArray(int size) {
      return new StaticGiftReceiptPrintJob[size];
    }
  };

  protected StaticGiftReceiptPrintJob(Parcel in) {
    super(in);
    // This class cannot contain any data because we forgot to put a Bundle here
  }

  @Override
  public Category getPrinterCategory() {
    return Category.RECEIPT;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    // This class cannot contain any data because we forgot to put a Bundle here
  }
}
