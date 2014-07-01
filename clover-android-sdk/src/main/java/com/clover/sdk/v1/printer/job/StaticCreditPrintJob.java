package com.clover.sdk.v1.printer.job;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.clover.sdk.v1.printer.Category;
import com.clover.sdk.v3.order.Order;

import java.util.ArrayList;

public class StaticCreditPrintJob extends StaticReceiptPrintJob implements Parcelable {
  private static final String BUNDLE_KEY_CREDIT_ID = "c";

  public static class Builder extends StaticReceiptPrintJob.Builder {
    private String creditId;

    public Builder staticCreditPrintJob(StaticCreditPrintJob pj) {
      this.order = pj.order;
      this.flags = pj.flags;
      this.creditId = pj.creditId;

      return this;
    }

    public Builder creditId(String creditId) {
      this.creditId = creditId;
      return this;
    }

    public StaticCreditPrintJob build() {
      return new StaticCreditPrintJob(order, creditId, flags);
    }
  }

  public final String creditId;

  public StaticCreditPrintJob(Order order, String creditId, int flags) {
    super(order, flags);
    this.creditId = creditId;
  }

  public static final Creator<StaticCreditPrintJob> CREATOR = new Creator<StaticCreditPrintJob>() {
    public StaticCreditPrintJob createFromParcel(Parcel in) {
      return new StaticCreditPrintJob(in);
    }

    public StaticCreditPrintJob[] newArray(int size) {
      return new StaticCreditPrintJob[size];
    }
  };

  protected StaticCreditPrintJob(Parcel in) {
    super(in);
    Bundle bundle = in.readBundle(getClass().getClassLoader()); // needed otherwise BadParcelableException: ClassNotFoundException when unmarshalling
    creditId = bundle.getString(BUNDLE_KEY_CREDIT_ID);
  }

  @Override
  public Category getPrinterCategory() {
    return Category.RECEIPT;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    Bundle bundle = new Bundle();
    bundle.putString(BUNDLE_KEY_CREDIT_ID, creditId);

    dest.writeBundle(bundle);
  }
}
