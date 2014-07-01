package com.clover.sdk.v1.printer.job;

import android.os.Parcel;
import android.os.Parcelable;
import com.clover.sdk.v1.printer.Category;
import com.clover.sdk.v3.order.Order;

public class StaticReceiptPrintJob extends StaticOrderBasedPrintJob implements Parcelable {

  public static class Builder extends StaticOrderBasedPrintJob.Builder {
    protected Order order;

    public Builder staticReceiptPrintJob(StaticReceiptPrintJob pj) {
      this.order = pj.order;
      this.flags = pj.flags;

      return this;
    }

    public Builder order(Order order) {
      this.order = order;
      return this;
    }

    public StaticReceiptPrintJob build() {
      return new StaticReceiptPrintJob(order, flags | FLAG_SALE);
    }
  }

  public StaticReceiptPrintJob(Order order, int flags) {
    super(order, flags);
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
