/**
 * Copyright (C) 2015 Clover Network, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clover.sdk.v1.printer.job;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.clover.sdk.v3.order.Order;

public abstract class StaticOrderBasedPrintJob extends PrintJob implements Parcelable {
  private static final String BUNDLE_KEY_ORDER = "o";
  private static final String BUNDLE_KEY_REASON = "r";

  public abstract static class Builder extends PrintJob.Builder {
    protected Order order;
    protected String reason;

    public Builder staticOrderBasedPrintJob(StaticOrderBasedPrintJob pj) {
      printJob(pj);
      this.order = pj.order;

      return this;
    }

    public Builder reason(String reason) {
      this.reason = reason;
      return this;
    }

    public Builder order(Order order) {
      if (order != null) {
        this.order = new Order(order);
      }
      return this;
    }
  }

  public final Order order;
  // yes, this is not final
  // reason is that for backwards compat we needed subclass to possible write this value in the unparceling ctor
  public String reason;

  @Deprecated
  public StaticOrderBasedPrintJob(Order order, int flags) {
    super(flags);
    this.order = order;
    this.reason = null;
  }


  protected StaticOrderBasedPrintJob(Builder builder) {
    super(builder);
    this.order = builder.order;
    this.reason = builder.reason;
  }

  protected StaticOrderBasedPrintJob(Parcel in) {
    super(in);
    Bundle bundle = in.readBundle(((Object)this).getClass().getClassLoader()); // needed otherwise BadParcelableException: ClassNotFoundException when unmarshalling
    order = bundle.getParcelable(BUNDLE_KEY_ORDER);
    reason = bundle.getString(BUNDLE_KEY_REASON);
    // Add more data here, but remember old apps might not provide it!
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    Bundle bundle = new Bundle();
    bundle.putParcelable(BUNDLE_KEY_ORDER, order);
    bundle.putString(BUNDLE_KEY_REASON, reason);
    dest.writeBundle(bundle);
  }
}
