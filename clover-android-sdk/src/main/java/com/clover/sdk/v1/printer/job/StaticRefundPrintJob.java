package com.clover.sdk.v1.printer.job;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.clover.sdk.v1.printer.Category;
import com.clover.sdk.v3.order.Order;

public class StaticRefundPrintJob extends StaticReceiptPrintJob implements Parcelable {
  private static final String BUNDLE_KEY_REFUND_ID = "r";

  public static class Builder extends StaticReceiptPrintJob.Builder {
    private String refundId;

    public Builder staticRefundPrintJob(StaticRefundPrintJob pj) {
      this.order = pj.order;
      this.flags = pj.flags;
      this.refundId = pj.refundId;

      return this;
    }

    public Builder refundId(String refundId) {
      this.refundId = refundId;
      return this;
    }

    public StaticRefundPrintJob build() {
      return new StaticRefundPrintJob(order, refundId, flags | FLAG_REFUND);
    }
  }

  public final String refundId;

  public StaticRefundPrintJob(Order order, String refundId, int flags) {
    super(order, flags);
    this.refundId = refundId;
  }

  public static final Creator<StaticRefundPrintJob> CREATOR = new Creator<StaticRefundPrintJob>() {
    public StaticRefundPrintJob createFromParcel(Parcel in) {
      return new StaticRefundPrintJob(in);
    }

    public StaticRefundPrintJob[] newArray(int size) {
      return new StaticRefundPrintJob[size];
    }
  };

  protected StaticRefundPrintJob(Parcel in) {
    super(in);
    Bundle bundle = in.readBundle(getClass().getClassLoader()); // needed otherwise BadParcelableException: ClassNotFoundException when unmarshalling
    refundId = bundle.getString(BUNDLE_KEY_REFUND_ID);
  }

  @Override
  public Category getPrinterCategory() {
    return Category.RECEIPT;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    Bundle bundle = new Bundle();
    bundle.putString(BUNDLE_KEY_REFUND_ID, refundId);

    dest.writeBundle(bundle);
  }
}
