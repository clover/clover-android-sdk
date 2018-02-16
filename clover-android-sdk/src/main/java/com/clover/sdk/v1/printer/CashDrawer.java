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
  private static final String SERVICE_HOST = "com.clover.engine";

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
   * Open specified cash drawer
   *
   * @param context A context.
   * @param account A Clover account.
   * @param cashDrawerNumber A cash drawer number, usually applicable to devices which support multiple
   *                         cash drawers. This value can be '1' for those printers which support only one cash drawer port
   */
  public static void open(Context context, Account account, int cashDrawerNumber) {
    open(context, account, null, null, cashDrawerNumber);
  }

  /**
   * Open a cash drawer connected to a printer.
   *
   * @param context A context.
   * @param account A Clover account.
   * @param openAny If multiple receipt printers are configured and false, the system will prompt
   *                the user to select the printer (or cash drawer if applicable). If true, the system will open the cash drawer
   *                on the first configured receipt printer (when multiple cash drawers are supported
   *                opens the cash drawer numbered '1').
   */
  public static void open(Context context, Account account, boolean openAny) {
    open(context, account, null, openAny, null);
  }

  /**
   * Open a cash drawer connected to the designated printer.
   *
   * @param context A context.
   * @param account A Clover account.
   * @param printer The printer to which the cash drawer is connected.
   */
  public static void open(Context context, Account account, Printer printer) {
    open(context, account, printer, false, null);
  }


  /**
   * Opens specified cash drawer. In case device does not support multiple cash drawers, supplied cash drawer number
   * is ignored and opens the only supported cash drawer connected to the printer
   *
   * @param context A context.
   * @param account A Clover account.
   * @param printer The printer to which the cash drawer is connected.
   * @param cashDrawerNumber A cash drawer number, usually applicable to devices which support multiple
   *                         cash drawers. This value can be '1' for those printers which support only one cash drawer port
   */
  public static void open(Context context, Account account, Printer printer, int cashDrawerNumber) {
    open(context, account, printer, null, cashDrawerNumber);
  }

  private static void open(Context context, Account account, Printer printer, Boolean openAny, Integer cashDrawerNumber) {
    Intent intent = new Intent(PrinterIntent.ACTION_OPEN_CASH_DRAWER_SERVICE);
    intent.setPackage(SERVICE_HOST);
    intent.putExtra(Intents.EXTRA_ACCOUNT, account);
    intent.putExtra(PrinterIntent.EXTRA_PRINTER, printer);
    intent.putExtra(PrinterIntent.EXTRA_OPEN_ANY, openAny);
    intent.putExtra(PrinterIntent.CASH_DRAWER_NUMBER, cashDrawerNumber);

    context.startService(intent);
  }
}
