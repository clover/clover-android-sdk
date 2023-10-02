/**
 * Copyright (C) 2016 Clover Network, Inc.
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
package com.clover.android.sdk.examples

import android.accounts.Account
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.clover.android.sdk.examples.databinding.ActivityPrintjobsTestBinding
import com.clover.sdk.util.CloverAccount
import com.clover.sdk.v1.printer.Category
import com.clover.sdk.v1.printer.Printer
import com.clover.sdk.v1.printer.PrinterConnector
import com.clover.sdk.v1.printer.job.PrintJobsConnector
import com.clover.sdk.v1.printer.job.PrintJobsContract.AUTHORITY_URI
import com.clover.sdk.v1.printer.job.PrintJobsContract.STATE_DONE
import com.clover.sdk.v1.printer.job.PrintJobsContract.STATE_ERROR
import com.clover.sdk.v1.printer.job.PrintJobsContract.STATE_IN_QUEUE
import com.clover.sdk.v1.printer.job.PrintJobsContract.STATE_PRINTING
import com.clover.sdk.v1.printer.job.TestReceiptPrintJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * This class is an example usage of the [PrintJobsConnector] class. It shows two buttons:
 * - Print: This looks up a configured receipt printers and calls [PrintJobsConnector.print] (with a printer argument).
 * - Print to any: Calls [PrintJobsConnector.print] (without a printer argument).
 *
 * It then observes the print jobs and displays their state. For example, "print" is
 * clicked expect the print job to move from "in queue" to "printing" and finally "done".
 */
open class PrintJobsTestActivity : AppCompatActivity() {

  private lateinit var account: Account
  private lateinit var printerConnector: PrinterConnector
  private lateinit var printJobsConnector: PrintJobsConnector
  private lateinit var binding: ActivityPrintjobsTestBinding

  private val printJobsObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
    override fun onChange(selfChange: Boolean) {
      lifecycleScope.launch { updateState() }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityPrintjobsTestBinding.inflate(layoutInflater)
    setContentView(binding.root)

    account = CloverAccount.getAccount(this)
    printerConnector = PrinterConnector(this, account, null)
    printJobsConnector = PrintJobsConnector(this)

    binding.buttonPrint.setOnClickListener {
      lifecycleScope.launch {
        withContext(Dispatchers.IO) {
          receiptPrinter?.let { p ->
            printJobsConnector.print(p, TestReceiptPrintJob.Builder().build())
          }
        }
      }
    }
    binding.buttonPrintToAny.setOnClickListener {
      lifecycleScope.launch {
        withContext(Dispatchers.IO) {
          printJobsConnector.print(TestReceiptPrintJob.Builder().build())
        }
      }
    }
  }

  override fun onStart() {
    super.onStart()
    // Observe print job changes
    contentResolver.registerContentObserver(AUTHORITY_URI, true, printJobsObserver)

    // Populate initial values
    lifecycleScope.launch { updateState() }

    // Do we have a receipt printer configured?
    lifecycleScope.launch {
      withContext(Dispatchers.IO) { receiptPrinter }?.let {
        binding.layoutButtons.visibility = View.VISIBLE
        binding.textError.visibility = View.GONE
      } ?: run {
        binding.layoutButtons.visibility = View.GONE
        binding.textError.visibility = View.VISIBLE
      }
    }

  }

  override fun onStop() {
    contentResolver.unregisterContentObserver(printJobsObserver)
    super.onStop()
  }

  override fun onDestroy() {
    super.onDestroy()
    printerConnector.disconnect()
  }

  private suspend fun updateState() {
    val states = withContext(Dispatchers.IO) {
      mapOf(
          ::STATE_IN_QUEUE.name to printJobsConnector.getPrintJobIds(STATE_IN_QUEUE),
          ::STATE_PRINTING.name to printJobsConnector.getPrintJobIds(STATE_PRINTING),
          ::STATE_DONE.name to printJobsConnector.getPrintJobIds(STATE_DONE),
          ::STATE_ERROR.name to printJobsConnector.getPrintJobIds(STATE_ERROR),
      )
    }

    binding.printjobIds.text = StringBuilder().apply {
      for (entry in states) {
        append("${entry.key.padEnd(16)}: ")
        for (id in entry.value) {
          append(id)
          if (id != entry.value.last()) {
            append(", ")
          }
        }
        append("\n\n")
      }
    }
  }

  /**
   * Return a [Printer] for the category [Category.RECEIPT], or `null` if no such
   * printer is configured. If there are multiple category [Category.RECEIPT]
   * printers configured one is selected at random.
   */
  private val receiptPrinter: Printer?
    get() = printerConnector.getPrinters(Category.RECEIPT)?.let { printers ->
      if (printers.isEmpty()) {
        return null
      }
      return printers[0]
    } ?: run { return null }
}