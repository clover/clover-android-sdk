/*
 * Copyright (C) 2013 Clover Network, Inc.
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

import android.view.View;
import java.io.Serializable;

public class ReportPrintJob extends ViewPrintJob implements Serializable {
  public enum ReportType {
    PAYMENTS, ITEMS, DISCOUNTS, TAXES, SHIFTS;
  }

  public static class Builder extends ViewPrintJob.Builder {
    private ReportType type = ReportType.PAYMENTS;

    public Builder type(ReportType type) {
      this.type = type;
      return this;
    }

    public ReportPrintJob build() {
      return new ReportPrintJob(view, type, flags);
    }
  }

  public final ReportType type;

  protected ReportPrintJob(View view, ReportType type, int flags) {
    super(view, flags);
    this.type = type;
  }
}
