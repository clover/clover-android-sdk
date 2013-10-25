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

import java.io.Serializable;
import java.util.List;

public class LineItemOrderPrintJob extends OrderPrintJob implements Serializable {
  public static class Builder extends OrderPrintJob.Builder {
    protected List<String> itemIds;

    public Builder itemIds(List<String> itemIds) {
      this.itemIds = itemIds;
      return this;
    }

    public LineItemOrderPrintJob build() {
      return new LineItemOrderPrintJob(orderId, itemIds, flags);
    }
  }

  public final List<String> itemIds;

  protected LineItemOrderPrintJob(String orderId, List<String> itemIds, int flags) {
    super(orderId, flags);
    this.itemIds = itemIds;
  }
}
