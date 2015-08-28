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


@Deprecated
/**
 * @deprecated See (@link com.clover.sdk.v1.printer.job.StaticOrderBasedPrintJob}
 */
public abstract class OrderBasedPrintJob extends PrintJob implements Parcelable {

  @Deprecated
  public abstract static class Builder extends PrintJob.Builder {
    protected String orderId;

    public Builder orderId(String orderId) {
      this.orderId = orderId;
      return this;
    }
  }

  public final String orderId;
  private static final String BUNDLE_KEY_ORDER_ID = "o";

  @Deprecated
  protected OrderBasedPrintJob(String orderId, int flags) {
    super(flags);
    this.orderId = orderId;
  }

  protected OrderBasedPrintJob(Builder builder) {
    super(builder);
    this.orderId = builder.orderId;
  }

  protected OrderBasedPrintJob(Parcel in) {
    super(in);
    Bundle bundle = in.readBundle(((Object)this).getClass().getClassLoader());
    orderId = bundle.getString(BUNDLE_KEY_ORDER_ID);
    // Add more data here, but remember old apps might not provide it!
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    Bundle bundle = new Bundle();
    bundle.putString(BUNDLE_KEY_ORDER_ID, orderId);
    // Add more stuff here

    dest.writeBundle(bundle);
  }
}
