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

/**
 * Definition of actions and extras for communicating with
 * {@link com.clover.sdk.v1.printer.IPrinterService}.
 *
 * @see com.clover.sdk.v1.printer.IPrinterService
 */
public class PrinterIntent {

  private PrinterIntent() {
  }

  /**
   * Service action: bind to the printer service.
   */
  public static final String ACTION_PRINTER_SERVICE = "com.clover.sdk.printer.intent.action.PRINTER_SERVICE";
  /**
   * Service action: start the print service.
   */
  public static final String ACTION_PRINT_SERVICE = "com.clover.sdk.printer.intent.action.PRINT_SERVICE";
  /**
   * Service action: start the print notify service.
   */
  public static final String ACTION_PRINT_NOTIFY_SERVICE = "com.clover.sdk.printer.intent.action.PRINT_NOTIFY_SERVICE";
  /**
   * Service action: start the cash drawer service.
   */
  public static final String ACTION_OPEN_CASH_DRAWER_SERVICE = "com.clover.sdk.printer.intent.action.OPEN_CASH_DRAWER_SERVICE";
  /**
   * Service action: bind to the receipt registration service.
   */
  public static final String ACTION_RECEIPT_REGISTRATION_SERVICE = "com.clover.sdk.printer.intent.action.RECEIPT_REGISTRATION_SERVICE";

  /**
   * A {@link com.clover.sdk.v1.printer.job.PrintJob}.
   */
  public static final String EXTRA_PRINTJOB = "clover.intent.extra.PRINT_JOB";
  /**
   * A {@link com.clover.sdk.v1.printer.Printer}.
   */
  public static final String EXTRA_PRINTER = "clover.intent.extra.PRINTER";
}
