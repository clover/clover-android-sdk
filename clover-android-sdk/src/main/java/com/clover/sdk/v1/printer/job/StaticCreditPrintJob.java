package com.clover.sdk.v1.printer.job;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.clover.sdk.v1.printer.Category;
import com.clover.sdk.v3.order.Order;
import com.clover.sdk.v3.payments.Credit;

public class StaticCreditPrintJob extends StaticReceiptPrintJob implements Parcelable {
  private static final String BUNDLE_KEY_CREDIT_ID = "c";
  private static final String BUNDLE_KEY_CREDIT = "cc";

  public static class Builder extends StaticReceiptPrintJob.Builder {
    private String creditId;
    private Credit credit;

    public Builder staticCreditPrintJob(StaticCreditPrintJob pj) {
      staticReceiptPrintJob(pj);
      this.creditId = pj.creditId;
      this.credit = pj.credit;

      return this;
    }

    public Builder creditId(String creditId) {
      this.creditId = creditId;
      return this;
    }

    public Builder credit(Credit credit) {
      this.credit = credit;
      return this;
    }

    public StaticCreditPrintJob build() {
      return new StaticCreditPrintJob(this);
    }
  }

  public final String creditId;
  public final Credit credit;

  @Deprecated
  public StaticCreditPrintJob(Order order, String creditId, int flags) {
    super(order, flags);
    this.creditId = creditId;
    this.credit = null;
  }

  protected StaticCreditPrintJob(Builder builder) {
    super(builder);
    this.creditId = builder.creditId;
    this.credit = builder.credit;
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
    Bundle bundle = in.readBundle(((Object)this).getClass().getClassLoader()); // needed otherwise BadParcelableException: ClassNotFoundException when unmarshalling
    creditId = bundle.getString(BUNDLE_KEY_CREDIT_ID);
    credit = bundle.getParcelable(BUNDLE_KEY_CREDIT);
    // Add more data here, but remember old apps might not provide it!
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
    bundle.putParcelable(BUNDLE_KEY_CREDIT, credit);

    dest.writeBundle(bundle);
  }
}
