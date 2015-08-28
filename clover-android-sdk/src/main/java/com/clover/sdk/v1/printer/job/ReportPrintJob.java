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
import android.view.View;

public class ReportPrintJob extends ViewPrintJob implements Parcelable {
  public enum ReportType {
    // Do not remove or rename these, add only to avoid breaking 3rd party apps
    PAYMENTS, ITEMS, DISCOUNTS, TAXES, SHIFTS;
  }

  public static class Builder extends ViewPrintJob.Builder {
    private ReportType type = ReportType.PAYMENTS;

    public Builder type(ReportType type) {
      this.type = type;
      return this;
    }

    public ReportPrintJob build() {
      return new ReportPrintJob(this);
    }
  }

  public final ReportType type;
  private static final String BUNDLE_KEY_REPORT_TYPE = "t";

  @Deprecated
  protected ReportPrintJob(View view, ReportType type, int flags) {
    super(view, flags);
    this.type = type;
  }

  protected ReportPrintJob(Builder builder) {
    super(builder);
    this.type = builder.type;
  }

  public static final Parcelable.Creator<ReportPrintJob> CREATOR
      = new Parcelable.Creator<ReportPrintJob>() {
    public ReportPrintJob createFromParcel(Parcel in) {
      return new ReportPrintJob(in);
    }

    public ReportPrintJob[] newArray(int size) {
      return new ReportPrintJob[size];
    }
  };

  protected ReportPrintJob(Parcel in) {
    super(in);
    Bundle bundle = in.readBundle(((Object)this).getClass().getClassLoader());
    type = ReportType.valueOf(bundle.getString(BUNDLE_KEY_REPORT_TYPE));
    // Add more data here, but remember old apps might not provide it!
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    Bundle bundle = new Bundle();
    bundle.putString(BUNDLE_KEY_REPORT_TYPE, type.name());
    // Add more stuff here

    dest.writeBundle(bundle);
  }
}
