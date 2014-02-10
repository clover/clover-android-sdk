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

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import com.clover.sdk.v1.Intents;
import com.clover.sdk.v1.printer.Category;
import com.clover.sdk.v1.printer.Printer;
import com.clover.sdk.v1.printer.PrinterIntent;

import java.io.Serializable;

public abstract class PrintJob implements Serializable {
  public static final int FLAG_NONE = 0;
  public static final int FLAG_REPRINT = 1 << 0;
  public static final int FLAG_BILL = 1 << 1;
  public static final int FLAG_SALE = 1 << 2;
  public static final int FLAG_REFUND = 1 << 3;
  public static final int FLAG_NO_SIGNATURE = 1 << 4;

  public abstract static class Builder {
    protected int flags = FLAG_NONE;

    @Deprecated
    public Builder flags(int flags) {
      this.flags = flags;
      return this;
    }

    public Builder flag(int flag) {
      this.flags |= flag;
      return this;
    }

    public abstract PrintJob build();
  }

  public final int flags;

  protected PrintJob(int flags) {
    this.flags = flags;
  }

  public abstract Category getPrinterCategory();

  public void print(Context context, Account account) {
    print(context, account, null);
  }

  public void print(Context context, Account account, Printer printer) {
    Intent intent = new Intent(PrinterIntent.ACTION_PRINT_SERVICE);
    intent.putExtra(PrinterIntent.EXTRA_PRINTJOB, this);
    intent.putExtra(Intents.EXTRA_ACCOUNT, account);
    intent.putExtra(PrinterIntent.EXTRA_PRINTER, printer);

    context.startService(intent);
  }

  public void cancel() {
  }
}
