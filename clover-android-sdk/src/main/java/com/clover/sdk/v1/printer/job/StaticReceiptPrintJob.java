package com.clover.sdk.v1.printer.job;

import android.os.Parcel;
import android.os.Parcelable;
import com.clover.sdk.v1.printer.Category;
import com.clover.sdk.v3.order.Order;

public class StaticReceiptPrintJob extends StaticOrderBasedPrintJob implements Parcelable {

  public static class Builder extends StaticOrderBasedPrintJob.Builder {
    public Builder staticReceiptPrintJob(StaticReceiptPrintJob pj) {
      staticOrderBasedPrintJob(pj);

      return this;
    }

    // This method isn't needed but kept for backwards compatibility
    public Builder order(Order order) {
      super.order(order);
      return this;
    }

    public StaticReceiptPrintJob build() {
      flags |= FLAG_SALE;
      return new StaticReceiptPrintJob(this);
    }
  }

  @Deprecated
  public StaticReceiptPrintJob(Order order, int flags) {
    super(order, flags);
  }

  protected StaticReceiptPrintJob(Builder builder) {
    super(builder);
  }

  public static final Creator<StaticReceiptPrintJob> CREATOR = new Creator<StaticReceiptPrintJob>() {
    public StaticReceiptPrintJob createFromParcel(Parcel in) {
      return new StaticReceiptPrintJob(in);
    }

    public StaticReceiptPrintJob[] newArray(int size) {
      return new StaticReceiptPrintJob[size];
    }
  };

  protected StaticReceiptPrintJob(Parcel in) {
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
