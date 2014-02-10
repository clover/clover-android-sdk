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

import com.clover.sdk.v1.printer.Category;

import java.io.Serializable;

public class PaymentPrintJob extends ReceiptPrintJob implements Serializable {
  public static class Builder extends ReceiptPrintJob.Builder {
    private String paymentId;

    public Builder paymentId(String paymentId) {
      this.paymentId = paymentId;
      return this;
    }

    public PaymentPrintJob build() {
      return new PaymentPrintJob(orderId, paymentId, flags | FLAG_SALE);
    }
  }

  public final String paymentId;

  protected PaymentPrintJob(String orderId, String paymentId, int flags) {
    super(orderId, flags);
    this.paymentId = paymentId;
  }

  @Override
  public Category getPrinterCategory() {
    return Category.RECEIPT;
  }
}
