/*
 * Copyright (C) 2016 Clover Network, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
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
import android.hardware.usb.UsbDevice;
import android.net.Uri;
import android.os.Bundle;

import com.clover.sdk.internal.util.UnstableCallClient;
import com.clover.sdk.internal.util.UnstableContentResolverClient;
import com.clover.sdk.v1.Intents;

/**
 * Class for interacting with connected cash drawers.
 *
 * @deprecated This class interacts with cash drawers that are connected via configured Clover
 *     printers. For a more generalized API for working with cash drawers, please use
 *     {@link com.clover.sdk.cashdrawer.CashDrawers}.
 */
@Deprecated
public class CashDrawer {
  public static final String POP_CASH_DRAWER_METHOD = "POP_CASH_DRAWER_METHOD";
  public static final String CASH_DRAWER_AUTHORITY = "com.clover.engine.printer.cashdrawerprovider";
  public static final Uri CASH_DRAWER_AUTHORITY_URI = Uri.parse("content://" + CASH_DRAWER_AUTHORITY);
  public static final String USB_DEVICE = "USB_DEVICE";

  private CashDrawer() {
  }

  /**
   * @deprecated Prefer interacting with cash drawers via {@link com.clover.sdk.cashdrawer.CashDrawers}
   */
  @Deprecated
  public static void open(Context context, Account account) {
    open(context, account, null, null, null, null);
  }

  /**
   * @deprecated Prefer interacting with cash drawers via {@link com.clover.sdk.cashdrawer.CashDrawers}
   */
  @Deprecated
  public static void open(Context context, Account account, int cashDrawerNumber) {
    open(context, account, null, null, cashDrawerNumber, null);
  }

  /**
   * @deprecated Prefer interacting with cash drawers via {@link com.clover.sdk.cashdrawer.CashDrawers}
   */
  @Deprecated
  public static void open(Context context, Account account, boolean openAny) {
    open(context, account, null, openAny, null, null);
  }

  /**
   * @deprecated Prefer interacting with cash drawers via {@link com.clover.sdk.cashdrawer.CashDrawers}
   */
  @Deprecated
  public static void open(Context context, Account account, Printer printer) {
    open(context, account, printer, false, null, null);
  }

  /**
   * @deprecated Prefer interacting with cash drawers via {@link com.clover.sdk.cashdrawer.CashDrawers}
   */
  @Deprecated
  public static void open(Context context, Account account, Printer printer, int cashDrawerNumber) {
    open(context, account, printer, null, cashDrawerNumber, null);
  }

  /**
   * @deprecated Prefer interacting with cash drawers via {@link com.clover.sdk.cashdrawer.CashDrawers}
   */
  @Deprecated
  public static void open(Context context, Account account, UsbDevice usbDevice) {
    open(context, account, null, null, null, usbDevice);
  }

  private static void open(Context context, Account account, Printer printer, Boolean openAny, Integer cashDrawerNumber, UsbDevice usbDevice) {
    Bundle extras = new Bundle();
    extras.putParcelable(Intents.EXTRA_ACCOUNT, account);
    extras.putParcelable(PrinterIntent.EXTRA_PRINTER, printer);
    extras.putBoolean(PrinterIntent.EXTRA_OPEN_ANY, (openAny == null) ? Boolean.FALSE : openAny);
    extras.putString(PrinterIntent.CASH_DRAWER_NUMBER,
        (cashDrawerNumber == null) ? null : cashDrawerNumber.toString());
    extras.putParcelable(USB_DEVICE, usbDevice);
    context.getContentResolver().call(CASH_DRAWER_AUTHORITY_URI, POP_CASH_DRAWER_METHOD,
        null, extras);
  }

  ///////////////////////////////////////////////

  /**
   * Open a cash drawer connected to a printer. If more than one printer exists that supports
   * cash drawers, the system will prompt the user to select the printer.
   *
   * @param context A context.
   * @param account A Clover account.
   *
   * @return true if the cash drawer service could be contacted and asked to open the cash
   * drawer. A true return value DOES NOT mean the cash drawer physically
   * opened, only that it was asked to open. There are a variety of factors that can cause
   * the cash drawer to not open, including but not limited to it being locked
   * and it not being connected. If false is returned, the cash drawer did not open.
   * This is not typical but can occur if the service used by this class is being re-installed
   * at call time or is otherwise not available on the device. In this case it is appropriate
   * to inform the user of the error and allow them to re-attempt the operation.
   */
  @Deprecated
  public static boolean open2(Context context, Account account) {
    return open2(context, account, null, null, null, null);
  }

  /**
   * Open specified cash drawer
   *
   * @deprecated Prefer interacting with cash drawers via {@link com.clover.sdk.cashdrawer.CashDrawers}
   *
   * @param context A context.
   * @param account A Clover account.
   * @param cashDrawerNumber A cash drawer number, usually applicable to devices which support multiple
   *                         cash drawers. This value can be '1' for those printers which support only one cash drawer port
   *
   * @return true if the cash drawer service could be contacted and asked to open the cash
   * drawer. A true return value DOES NOT mean the cash drawer physically
   * opened, only that it was asked to open. There are a variety of factors that can cause
   * the cash drawer to not open, including but not limited to it being locked
   * and it not being connected. If false is returned, the cash drawer did not open.
   * This is not typical but can occur if the service used by this class is being re-installed
   * at call time or is otherwise not available on the device. In this case it is appropriate
   * to inform the user of the error and allow them to re-attempt the operation.
   */
  @Deprecated
  public static boolean open2(Context context, Account account, int cashDrawerNumber) {
    return open2(context, account, null, null, cashDrawerNumber, null);
  }

  /**
   * Open a cash drawer connected to a printer.
   *
   * @deprecated Prefer interacting with cash drawers via {@link com.clover.sdk.cashdrawer.CashDrawers}
   *
   * @param context A context.
   * @param account A Clover account.
   * @param openAny If multiple receipt printers are configured and false, the system will prompt
   *                the user to select the printer (or cash drawer if applicable). If true, the system will open the cash drawer
   *                on the first configured receipt printer (when multiple cash drawers are supported
   *                opens the cash drawer numbered '1').
   *
   * @return true if the cash drawer service could be contacted and asked to open the cash
   * drawer. A true return value DOES NOT mean the cash drawer physically
   * opened, only that it was asked to open. There are a variety of factors that can cause
   * the cash drawer to not open, including but not limited to it being locked
   * and it not being connected. If false is returned, the cash drawer did not open.
   * This is not typical but can occur if the service used by this class is being re-installed
   * at call time or is otherwise not available on the device. In this case it is appropriate
   * to inform the user of the error and allow them to re-attempt the operation.
   */
  @Deprecated
  public static boolean open2(Context context, Account account, boolean openAny) {
    return open2(context, account, null, openAny, null, null);
  }

  /**
   * Open a cash drawer connected to the designated printer.
   *
   * @deprecated Prefer interacting with cash drawers via {@link com.clover.sdk.cashdrawer.CashDrawers}
   *
   * @param context A context.
   * @param account A Clover account.
   * @param printer The printer to which the cash drawer is connected.
   *
   * @return true if the cash drawer service could be contacted and asked to open the cash
   * drawer. A true return value DOES NOT mean the cash drawer physically
   * opened, only that it was asked to open. There are a variety of factors that can cause
   * the cash drawer to not open, including but not limited to it being locked
   * and it not being connected. If false is returned, the cash drawer did not open.
   * This is not typical but can occur if the service used by this class is being re-installed
   * at call time or is otherwise not available on the device. In this case it is appropriate
   * to inform the user of the error and allow them to re-attempt the operation.
   */
  @Deprecated
  public static boolean open2(Context context, Account account, Printer printer) {
    return open2(context, account, printer, false, null, null);
  }

  /**
   * Opens specified cash drawer. In case device does not support multiple cash drawers, supplied cash drawer number
   * is ignored and opens the only supported cash drawer connected to the printer
   *
   * @deprecated Prefer interacting with cash drawers via {@link com.clover.sdk.cashdrawer.CashDrawers}
   *
   * @param context A context.
   * @param account A Clover account.
   * @param printer The printer to which the cash drawer is connected.
   * @param cashDrawerNumber A cash drawer number, usually applicable to devices which support multiple
   *                         cash drawers. This value can be '1' for those printers which support only one cash drawer port
   *
   * @return true if the cash drawer service could be contacted and asked to open the cash
   * drawer. A true return value DOES NOT mean the cash drawer physically
   * opened, only that it was asked to open. There are a variety of factors that can cause
   * the cash drawer to not open, including but not limited to it being locked
   * and it not being connected. If false is returned, the cash drawer did not open.
   * This is not typical but can occur if the service used by this class is being re-installed
   * at call time or is otherwise not available on the device. In this case it is appropriate
   * to inform the user of the error and allow them to re-attempt the operation.
   */
  @Deprecated
  public static boolean open2(Context context, Account account, Printer printer, int cashDrawerNumber) {
    return open2(context, account, printer, null, cashDrawerNumber, null);
  }

  /**
   * @deprecated Prefer interacting with cash drawers via {@link com.clover.sdk.cashdrawer.CashDrawers}
   */
  @Deprecated
  public static boolean open2(Context context, Account account, UsbDevice usbDevice) {
    return open2(context, account, null, null, null, usbDevice);
  }

  private static boolean open2(Context context, Account account, Printer printer, Boolean openAny, Integer cashDrawerNumber, UsbDevice usbDevice) {
    Bundle extras = new Bundle();
    extras.putParcelable(Intents.EXTRA_ACCOUNT, account);
    extras.putParcelable(PrinterIntent.EXTRA_PRINTER, printer);
    extras.putBoolean(PrinterIntent.EXTRA_OPEN_ANY, (openAny == null) ? Boolean.FALSE : openAny);
    extras.putString(PrinterIntent.CASH_DRAWER_NUMBER,
        (cashDrawerNumber == null) ? null : cashDrawerNumber.toString());
    extras.putParcelable(USB_DEVICE, usbDevice);

    UnstableContentResolverClient client = new UnstableContentResolverClient(context.getContentResolver(), CASH_DRAWER_AUTHORITY_URI);
    // Engine call method returns an empty bundle on successful IPC
    return client.call(POP_CASH_DRAWER_METHOD,null, extras, null) != null;
  }

}
