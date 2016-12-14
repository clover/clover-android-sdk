/**
 * Copyright (C) 2016 Clover Network, Inc.
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
 * Class for interacting with connected cash drawers.
 *
 * This class interacts with cash drawers that are connected via configured Clover printers. For a more generalized
 * API for working with cash drawers, consider using {@link com.clover.sdk.cashdrawer}.
 */
public class CashDrawer {
  private CashDrawer() {
  }

  /**
   * Open a cash drawer connected to a printer. If more than one printer exists that supports
   * cash drawers, the system will prompt the user to select the printer.
   *
   * @param context A context.
   * @param account A Clover account.
   */
  public static void open(Context context, Account account) {
    open(context, account, null);
  }

  /**
   * Open a cash drawer connected to a printer.
   *
   * @param context A context.
   * @param account A Clover account.
   * @param openAny If multiple receipt printers are configured and false, the system will prompt
   *                the user to select the printer. If true, the system will open the cash drawer
   *                on the first configured receipt printer.
   */
  public static void open(Context context, Account account, boolean openAny) {
    open(context, account, null, openAny);
  }

  /**
   * Open a cash drawer connected to the designated printer.
   *
   * @param context A context.
   * @param account A Clover account.
   * @param printer The printer to which the cash drawer is connected.
   */
  public static void open(Context context, Account account, Printer printer) {
    open(context, account, printer, false);
  }

  private static void open(Context context, Account account, Printer printer, boolean openAny) {
    Intent intent = new Intent(PrinterIntent.ACTION_OPEN_CASH_DRAWER_SERVICE);
    intent.putExtra(Intents.EXTRA_ACCOUNT, account);
    intent.putExtra(PrinterIntent.EXTRA_PRINTER, printer);
    intent.putExtra(PrinterIntent.EXTRA_OPEN_ANY, openAny);

    context.startService(intent);
  }
}
