/**
 * Copyright (C) 2024 Clover Network, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clover.sdk.v1.printer

import android.os.ParcelFileDescriptor
import com.clover.sdk.v1.printer.job.BalanceInquiryPrintJob
import com.clover.sdk.v1.printer.job.GiftCardPrintJob
import com.clover.sdk.v1.printer.job.PrintJob
import com.clover.sdk.v1.printer.job.StaticBillPrintJob
import com.clover.sdk.v1.printer.job.StaticCreditPrintJob
import com.clover.sdk.v1.printer.job.StaticGiftReceiptPrintJob
import com.clover.sdk.v1.printer.job.StaticLabelPrintJob
import com.clover.sdk.v1.printer.job.StaticOrderPrintJob
import com.clover.sdk.v1.printer.job.StaticPaymentPrintJob
import com.clover.sdk.v1.printer.job.StaticRefundPrintJob
import com.clover.sdk.v1.printer.job.TextPrintJob
import com.clover.sdk.v1.printer.job.TokenRequestBasedPrintJob

/**
 *
 * A class defining the contract to be implemented by content providers that wish
 * to provide custom content for receipts.
 *
 * To integrate a custom receipt provider with Clover, define a content provider
 * with below meta-data in manifest:
 *
 * - exported = true
 *
 * - name = [CUSTOM_RECEIPT_CONTENT_PROVIDER_RECEIPT_TYPES], value = comma separated list of [ReceiptType]
 * List of Print receipt use cases for which the content provider will provide custom formatted receipts
 *
 * Ex:
 * ```
 * ...
 *
 * <provider
 *  ...
 *  android:name="..."
 *  android:exported="true">
 *    <meta-data
 *        android:name="com.clover.printer.receipt.custom.content.receipt.types"
 *        android:value="Payment,Bill"/>
 * </provider>
 * ```
 *
 * Provider must override the "call" method. It should handle the method [METHOD_GET_RECEIPT_CONTENT_URIS]
 * Input data is passed in as [PrintJob] in extras.
 * The bundle extras will have the following:
 * 1. An instance of [PrintJob], which can be of one of the print job types defined in [ReceiptType],
 * as value of [EXTRA_PRINT_JOB] key
 * 2. [Printer] on which the receipt will be printed, as value of [EXTRA_PRINTER] key
 *
 * Provider must return a list of receipt segment bitmap uris as extra [EXTRA_RECEIPT_CONTENT_URIS]
 *
 * Ex:
 * ```
 * override fun call(method: String, arg: String?, extras: Bundle?): Bundle {
 *     if (method == [METHOD_GET_RECEIPT_CONTENT_URIS]) {
 *        val printJob = it.getParcelable<PrintJob>([EXTRA_PRINT_JOB])
 *        //create receipt bitmaps from printJob
 *        //store it as bitmap blob and get its receipt segment uris
 *        //bundle the receipt segment uris into an ArrayList
 *        //return the bundle
 *     }
 *     ....
 * }
 * ```
 *
 * Also, provider must override the [android.content.ContentProvider.openFile] method. This method
 * should take in receipt segment uri, fetch the bitmap using that uri and return
 * the [ParcelFileDescriptor] using [android.content.ContentProvider.openPipeHelper] of
 * type [android.graphics.Bitmap]. Clover will use the returned [ParcelFileDescriptor] to open
 * input stream and fetch the bitmap data.
 *
 * Ex.
 * ```
 * override fun openFile(contentUri: Uri, mode: String): ParcelFileDescriptor {
 *  val bitmap = getReceiptSegmentBitmap(contentUri)
 *  return openPipeHelper<Bitmap>(contentUri, "mimeType", null, bitmap)
 *   { output: ParcelFileDescriptor, uri: Uri, mimeType: String?, opts: Bundle?, args: Bitmap? ->
 *     try {
 *        AutoCloseOutputStream(output).use { bitmap?.compress(Bitmap.CompressFormat.PNG, 100, it) }
 *     } catch (e: IOException) {
 *        e.printStackTrace()
 *     }
 *   }
 * }
 * ```
 *
 * ### Bitmap specifications:
 * - If the generated receipt bitmap height is greater than 2048 pixels, it must be divided into
 * smaller chunks. Clover will reject the print request if the generated receipt bitmap height is greater than
 * 2048 pixels.
 * - The generated receipt bitmap width must be obtained from the passed in [Printer] object as shown
 * in example below. Clover will reject the print request if the generated receipt bitmap width is
 * greater than the width obtained from the passed in [Printer] object.
 *
 * Ex.
 * ```
 *  val td: TypeDetails = printerConnector.getPrinterTypeDetails(printer)
 *  val supportedWidth = td.numDotsWidth
 * ```
 * - When multiple bitmap chunks are present, for Clover to print the receipt in a
 * proper sequence, provider must return the uris corresponding to the bitmap chunks in the
 * following order: first chunk: receipt header -> next chunk(s): receipt body -> final chunk: receipt footer
 * - It is recommended for the generated receipt bitmap to have
 * [android.graphics.Bitmap.Config.RGB_565] color space.
 * - It is recommended to send the bitmap in PNG format.
 *
 * @see android.content.ContentProvider
 * @see android.graphics.Bitmap
 * @see ParcelFileDescriptor
 * @see PrinterConnector
 * @see TypeDetails
 */
object ReceiptContentContract {
  /**
   * Meta-data name for supported receipt types. Values are comma separated list of
   * one or more case insensitive [ReceiptType] names
   */
  const val CUSTOM_RECEIPT_CONTENT_PROVIDER_RECEIPT_TYPES = "com.clover.printer.receipt.custom.content.receipt.types"

  /**
   * Call method name to invoke "get receipt content uris". When invoked, the extras bundle will include
   * a key[EXTRA_PRINT_JOB] and value will be one of the [PrintJob] derived classes i.e. [StaticPaymentPrintJob]
   *
   * The result bundle must contain a key = [EXTRA_RECEIPT_CONTENT_URIS] and its value being an [ArrayList<Uri>],
   * which will have list of receipt segment bitmap uris
   * Ex.
   * ```
   * override fun call(method: String, arg: String?, extras: Bundle?): Bundle {
   *     val result = Bundle()
   *     if (method == "get_receipt_content_uris") {
   *        //bundle the uris into an ArrayList
   *     }
   *     ....
   * }
   * ```
   */
  const val METHOD_GET_RECEIPT_CONTENT_URIS = "get_receipt_content_uris"

  /**
   * Extra for the list of receipt segment bitmap uris
   */
  const val EXTRA_RECEIPT_CONTENT_URIS = "receipt_content_uris"

  /**
   * Extra required for call method [METHOD_GET_RECEIPT_CONTENT_URIS]. A [PrintJob] corresponding
   * to print receipt request.
   */
  const val EXTRA_PRINT_JOB = "print_job"

  /**
   * Extra required for call method [METHOD_GET_RECEIPT_CONTENT_URIS]. The [Printer] on which the
   * receipt will be printed.
   *
   * Clover POS can be connected to one or more printers. Merchant can select a printer on which
   * the current receipt should be printed. The selected printer is passed in the bundle extras as
   * the value of this key
   */
  const val EXTRA_PRINTER = "printer"

  /**
   * Supported ReceiptTypes to be listed in provider's meta-data.
   * Value defines the mapping of ReceiptType to corresponding PrintJob
   *
   * Ex.
   * ```
   * <provider..>
   *   ...
   *    <meta-data
   *      android:name="com.clover.printer.receipt.custom.content.receipt.types"
   *      android:value="Payment,Bill"/>
   * <provider>
   * ```
   * @see [StaticBillPrintJob]
   * @see [StaticCreditPrintJob]
   * @see [StaticPaymentPrintJob]
   * @see [StaticRefundPrintJob]
   * @see [StaticGiftReceiptPrintJob]
   * @see [TextPrintJob]
   * @see [StaticOrderPrintJob]
   * @see [GiftCardPrintJob]
   * @see [BalanceInquiryPrintJob]
   * @see [TokenRequestBasedPrintJob]
   * @see [StaticLabelPrintJob]
   */
  enum class ReceiptType(val printJobClass: Class<out PrintJob>) {
    BILL(StaticBillPrintJob::class.java),
    CREDIT(StaticCreditPrintJob::class.java),
    PAYMENT(StaticPaymentPrintJob::class.java),
    REFUND(StaticRefundPrintJob::class.java),
    GIFT(StaticGiftReceiptPrintJob::class.java),
    TEXT(TextPrintJob::class.java),
    ORDER(StaticOrderPrintJob::class.java),
    GIFT_CARD(GiftCardPrintJob::class.java),
    BALANCE_INQUIRY(BalanceInquiryPrintJob::class.java),
    TOKEN_REQUEST_BASED(TokenRequestBasedPrintJob::class.java),
    LABEL(StaticLabelPrintJob::class.java);
  }
}