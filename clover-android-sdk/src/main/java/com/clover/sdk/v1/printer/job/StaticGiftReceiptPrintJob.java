package com.clover.sdk.v1.printer.job;

import android.os.Parcel;
import android.os.Parcelable;
import com.clover.sdk.v1.printer.Category;
import com.clover.sdk.v3.order.Order;

public class StaticGiftReceiptPrintJob extends StaticReceiptPrintJob implements Parcelable {

  public static class Builder extends StaticReceiptPrintJob.Builder {

    public Builder staticGiftReceiptPrintJob(StaticGiftReceiptPrintJob pj) {
      this.order = pj.order;
      this.flags = pj.flags;

      return this;
    }

    public StaticGiftReceiptPrintJob build() {
      return new StaticGiftReceiptPrintJob(order, flags);
    }
  }

  public StaticGiftReceiptPrintJob(Order order, int flags) {
    super(order, flags);
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
  }

  @Override
  public Category getPrinterCategory() {
    return Category.RECEIPT;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
  }
}
