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
package com.clover.android.sdk.examples;

import android.accounts.Account;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.printer.Category;
import com.clover.sdk.v1.printer.Printer;
import com.clover.sdk.v1.printer.PrinterConnector;
import com.clover.sdk.v1.printer.job.PrintJob;
import com.clover.sdk.v1.printer.job.PrintJobsConnector;
import com.clover.sdk.v1.printer.job.PrintJobsContract;
import com.clover.sdk.v1.printer.job.TestReceiptPrintJob;

import java.util.Arrays;
import java.util.List;

public class PrintJobsTestActivity extends Activity {
  private static final String TAG = PrintJobsTestActivity.class.getSimpleName();

  private static final Handler uiHandler = new Handler(Looper.getMainLooper());

  private Account account;
  private PrinterConnector printerConnector;

  private EditText editPrinterId;
  private Button buttonPrint;
  private TextView textId;

  private Button buttonGetPrintJobIds;
  private TextView textPrintJobIds;

  private HandlerThread handlerThread = new HandlerThread(this.getClass().getName());
  private Handler handler;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_printjobs_test);

    handlerThread.start();
    handler = new Handler(handlerThread.getLooper());

    account = CloverAccount.getAccount(this);
    printerConnector = new PrinterConnector(this, account, null);
    fillPrinterId();

    textId = (TextView) findViewById(R.id.text_id);

    editPrinterId = (EditText) findViewById(R.id.edit_printer_id);
    buttonPrint = (Button) findViewById(R.id.button_print);
    buttonPrint.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        new AsyncTask<Void, Void, String>() {
          @Override
          protected String doInBackground(Void... voids) {
            try {
              Printer p = getPrinter();
              PrintJob printJob = new TestReceiptPrintJob.Builder().build();
              return new PrintJobsConnector(PrintJobsTestActivity.this).print(p, printJob);
            } catch (Exception e) {
              e.printStackTrace();
            }
            return null;
          }

          @Override
          protected void onPostExecute(String id) {
            if (id == null) {
              return;
            }
            textId.setText(id);
            monitorState();
          }
        }.execute();
      }
    });

    textPrintJobIds = (TextView) findViewById(R.id.text_printjob_ids);
  }

  private void monitorState() {
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        PrintJobsConnector connector = new PrintJobsConnector(PrintJobsTestActivity.this);

        final List<String> inQueueIds = connector.getPrintJobIds(PrintJobsContract.STATE_IN_QUEUE);
        final List<String> printingIds = connector.getPrintJobIds(PrintJobsContract.STATE_PRINTING);
        final List<String> doneIds = connector.getPrintJobIds(PrintJobsContract.STATE_DONE);
        final List<String> errorIds = connector.getPrintJobIds(PrintJobsContract.STATE_ERROR);

        final StringBuilder printJobIds = new StringBuilder();

        List<Pair<String,List<String>>> pairs = Arrays.asList(
            Pair.create("In queue: ", inQueueIds),
            Pair.create("Printing: ", printingIds),
            Pair.create("Done: ", doneIds),
            Pair.create("Error: ", errorIds)
        );
        for (Pair<String,List<String>> pair: pairs) {
          printJobIds.append(pair.first);
          for (String id: pair.second) {
            printJobIds.append(id).append(", ");
          }
          if (!pair.second.isEmpty()) {
            printJobIds.delete(printJobIds.length() - 2, printJobIds.length());
          }
          printJobIds.append("\n\n");
        }
        uiHandler.post(new Runnable() {
          @Override
          public void run() {
            textPrintJobIds.setText(printJobIds.toString());
          }
        });

        monitorState();
      }
    }, 100);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    uiHandler.removeCallbacksAndMessages(null);
    handler.removeCallbacksAndMessages(null);

    if (printerConnector != null) {
      printerConnector.disconnect();
      printerConnector = null;
    }
  }

  private void fillPrinterId() {
    new AsyncTask<Void,Void,List<Printer>>() {
      @Override
      protected List<Printer> doInBackground(Void... params) {
        try {
          return printerConnector.getPrinters(Category.RECEIPT);
        } catch (Exception e) {
          e.printStackTrace();
        }
        return null;
      }

      @Override
      protected void onPostExecute(List<Printer> printers) {
        if (printers != null && !printers.isEmpty()) {
          editPrinterId.setText(printers.get(0).getUuid());
        }
      }
    }.execute();
  }

  private Printer getPrinter() {
    String id = editPrinterId.getText().toString();
    if (TextUtils.isEmpty(id)) {
      try {
        List<Printer> printers = printerConnector.getPrinters(Category.RECEIPT);
        if (printers != null && !printers.isEmpty()) {
          return printers.get(0);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;
    }

    try {
      return printerConnector.getPrinter(id);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;

  }
}
