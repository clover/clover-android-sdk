package com.clover.sdk.v1.printer.job;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.clover.sdk.v1.printer.Category;
import com.clover.sdk.v3.order.Order;

import java.util.ArrayList;

public abstract class StaticOrderBasedPrintJob extends PrintJob implements Parcelable {
  private static final String BUNDLE_KEY_ORDER = "o";

  public abstract static class Builder extends PrintJob.Builder {
    protected Order order;

    public Builder staticOrderBasedPrintJob(StaticOrderBasedPrintJob pj) {
      this.order = pj.order;
      this.flags = pj.flags;

      return this;
    }

    public Builder order(Order order) {
      this.order = new Order(order);
      return this;
    }
  }

  public final Order order;

  public StaticOrderBasedPrintJob(Order order, int flags) {
    super(flags);
    this.order = order;
  }

  protected StaticOrderBasedPrintJob(Parcel in) {
    super(in);
    Bundle bundle = in.readBundle(getClass().getClassLoader()); // needed otherwise BadParcelableException: ClassNotFoundException when unmarshalling
    order = bundle.getParcelable(BUNDLE_KEY_ORDER);
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    Bundle bundle = new Bundle();
    bundle.putParcelable(BUNDLE_KEY_ORDER, order);

    dest.writeBundle(bundle);
  }
}
