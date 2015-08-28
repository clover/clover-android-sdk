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
/**
 * This package contains classes for printing to Clover connected printers. The name of the class
 * describes the type of print job. For example, {@link com.clover.sdk.v1.printer.job.BillPrintJob}
 * prints bills, {@link com.clover.sdk.v1.printer.job.OrderPrintJob} prints orders, etc.
 * <br/>
 * <br/>
 * Each class contains a builder subclass that is used to construct the print job. Use the
 * {@link com.clover.sdk.v1.printer.job.PrintJob#print(android.content.Context, android.accounts.Account)}
 * method to print immediately, if there is only one printer for the job type, or to have the
 * system query the user for the target printer.
 * Use the
 * {@link com.clover.sdk.v1.printer.job.PrintJob#print(android.content.Context, android.accounts.Account, com.clover.sdk.v1.printer.Printer)}
 * to target a specific printer. For example, to re-print a receipt,
 * <pre>
 * <code>
 * PrintJob receiptPrintJob = new ReceiptPrintJob()
 *     .orderId("FH0VXM0WJX6JG")
 *     .flags(PrintJob.FLAG_REPRINT)
 *     .build();
 * receiptPrintJob.print(context, CloverAccount.getAccount(context));
 * </code>
 * </pre>
 * To print simple text,
 * <pre>
 * <code>
 * PrintJob textPrintJob = new TextPrintJob()
 *     .text("Hello, Clover printer!")
 *     .build();
 * textPrintJob.print(context, CloverAccount.getAccount(context));
 * </code>
 * </pre>
 * To print a View,
 * <pre>
 * <code>
 * PrintJob viewPrintJob = new ViewPrintJob()
 *     .view(view)
 *     .build();
 * viewPrintJob.print(context, CloverAccount.getAccount(context));
 * </code>
 * </pre>
 *
 */
package com.clover.sdk.v1.printer.job;