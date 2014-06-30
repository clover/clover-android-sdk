package com.clover.sdk.v1.printer.job;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.clover.sdk.v1.printer.Category;
import com.clover.sdk.v3.order.Order;

public class StaticPaymentPrintJob extends StaticReceiptPrintJob implements Parcelable {
  private static final String BUNDLE_KEY_PAYMENT_ID = "p";

  public static class Builder extends StaticReceiptPrintJob.Builder {
    private String paymentId;

    public Builder staticPaymentPrintJob(StaticPaymentPrintJob pj) {
      this.order = pj.order;
      this.flags = pj.flags;
      this.paymentId = pj.paymentId;

      return this;
    }

    public Builder paymentId(String paymentId) {
      this.paymentId = paymentId;
      return this;
    }

    public StaticPaymentPrintJob build() {
      return new StaticPaymentPrintJob(order, paymentId, flags | FLAG_SALE);
    }
  }

  public final String paymentId;

  public StaticPaymentPrintJob(Order order, String paymentId, int flags) {
    super(order, flags);
    this.paymentId = paymentId;
  }

  public static final Creator<StaticPaymentPrintJob> CREATOR = new Creator<StaticPaymentPrintJob>() {
    public StaticPaymentPrintJob createFromParcel(Parcel in) {
      return new StaticPaymentPrintJob(in);
    }

    public StaticPaymentPrintJob[] newArray(int size) {
      return new StaticPaymentPrintJob[size];
    }
  };

  protected StaticPaymentPrintJob(Parcel in) {
    super(in);
    Bundle bundle = in.readBundle(getClass().getClassLoader()); // needed otherwise BadParcelableException: ClassNotFoundException when unmarshalling
    paymentId = bundle.getString(BUNDLE_KEY_PAYMENT_ID);
  }

  @Override
  public Category getPrinterCategory() {
    return Category.RECEIPT;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    Bundle bundle = new Bundle();
    bundle.putString(BUNDLE_KEY_PAYMENT_ID, paymentId);

    dest.writeBundle(bundle);
  }
}
