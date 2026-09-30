package com.clover.sdk.v1.printer.job;

import com.clover.sdk.v1.printer.Category;
import com.clover.sdk.v1.printer.DeclineReason;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class StaticPaymentDeclinePrintJob extends StaticPaymentPrintJob implements Parcelable {
  private static final String BUNDLE_KEY_REASON = "r";

  public static class Builder extends StaticPaymentPrintJob.Builder {
    public Builder declineReason(Context context, DeclineReason declineReason) {
      reason = declineReason == null ? null : declineReason.getMessage(context);

      return this;
    }

    public StaticPaymentDeclinePrintJob build() {
      flag(PrintJob.FLAG_NO_SIGNATURE);
      return new StaticPaymentDeclinePrintJob(this);
    }
  }

  protected StaticPaymentDeclinePrintJob(Builder builder) {
    super(builder);
  }

  public static final Creator<StaticPaymentDeclinePrintJob> CREATOR = new Creator<StaticPaymentDeclinePrintJob>() {
    public StaticPaymentDeclinePrintJob createFromParcel(Parcel in) {
      return new StaticPaymentDeclinePrintJob(in);
    }

    public StaticPaymentDeclinePrintJob[] newArray(int size) {
      return new StaticPaymentDeclinePrintJob[size];
    }
  };

  protected StaticPaymentDeclinePrintJob(Parcel in) {
    super(in);
    // need to keep this as a placeholder to add new fields in the future
    Bundle bundle = in.readBundle(((Object) this).getClass().getClassLoader()); // needed otherwise BadParcelableException: ClassNotFoundException when unmarshalling
    if (bundle.getString(BUNDLE_KEY_REASON) != null) {
      reason = bundle.getString(BUNDLE_KEY_REASON);
    }
  }

  @Override
  public Category getPrinterCategory() {
    return Category.RECEIPT;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    Bundle bundle = new Bundle();
    // THIS IS FOR BACKWARDS COMPAT: local field reason was removed and we are now using the one in parent
    bundle.putString(BUNDLE_KEY_REASON, reason);
    dest.writeBundle(bundle);
  }
}
