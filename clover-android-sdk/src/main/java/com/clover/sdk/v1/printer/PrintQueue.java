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
package com.clover.sdk.v1.printer;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import com.clover.sdk.v1.Intents;

/**
 * Class for interacting with the Clover print queue.
 */
public class PrintQueue {
  private PrintQueue() {
  }

  /**
   * Start the print queue. The system will attempt to print all queued print jobs for the
   * specified printer.
   *
   * @param context The context to use when communicating with the print queue.
   * @param account A Clover account.
   * @param printer Print jobs to this printer will be started.
   */
  public static void start(Context context, Account account, Printer printer) {
    Intent intent = new Intent(PrinterIntent.ACTION_PRINT_NOTIFY_SERVICE);
    intent.putExtra(Intents.EXTRA_ACCOUNT, account);
    intent.putExtra(PrinterIntent.EXTRA_PRINTER, printer);

    context.startService(intent);
  }

  /**
   * Start the print queue. The system will attempt to print all queued print jobs for
   * all printers.
   *
   * @param context The context to use when communicating with the print queue.
   * @param account A Clover account.
   */
  public static void start(Context context, Account account) {
    start(context, account, null);
  }
}
