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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.printer.CashDrawer;
import com.clover.sdk.v1.printer.Category;
import com.clover.sdk.v1.printer.Printer;
import com.clover.sdk.v1.printer.PrinterConnector;
import com.clover.sdk.v1.printer.job.ImagePrintJob;
import com.clover.sdk.v1.printer.job.PrintJob;
import com.clover.sdk.v1.printer.job.PrintJobsConnector;
import com.clover.sdk.v1.printer.job.ReceiptPrintJob;
import com.clover.sdk.v1.printer.job.TextPrintJob;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class PrintTestActivity extends Activity {
  private static final String TAG = "PrintTestActivity";
  private static final int REQUEST_ACCOUNT = 0;

  private Account account;
  private PrinterConnector printerConnector;

  private EditText editPrinterId;

  private EditText editPrintOrderId;
  private Button buttonPrintOrderReceipt;

  private Button buttonOpenCashdrawer;

  private EditText editPrintImageUrl;
  private Button buttonPrintImage;
  private Button buttonPrintImageSync;

  private EditText editPrintText;
  private Button buttonPrintText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_print_test);

    account = CloverAccount.getAccount(this);
    printerConnector = new PrinterConnector(this, account, null);
    fillPrinterId();

    editPrinterId = (EditText) findViewById(R.id.edit_printer_id);

    editPrintOrderId = (EditText) findViewById(R.id.edit_print_order_id);
    buttonPrintOrderReceipt = (Button) findViewById(R.id.button_print_order_receipt);
    buttonPrintOrderReceipt.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        final String id = editPrintOrderId.getText().toString().toUpperCase();
        new AsyncTask<Void,Void,Printer>() {
          @Override
          protected Printer doInBackground(Void... params) {
            return getPrinter();
          }

          @Override
          protected void onPostExecute(Printer printer) {
            if (printer == null) {
              return;
            }
            new ReceiptPrintJob.Builder().orderId(id).build().print(PrintTestActivity.this, account, printer);
          }
        }.execute();

      }
    });

    buttonOpenCashdrawer = (Button) findViewById(R.id.button_open_cashdrawer);
    buttonOpenCashdrawer.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        new AsyncTask<Void,Void,Printer>() {
          @Override
          protected Printer doInBackground(Void... params) {
            return getPrinter();
          }

          @Override
          protected void onPostExecute(Printer printer) {
            if (printer == null) {
              return;
            }
            CashDrawer.open(PrintTestActivity.this, account, printer);
          }
        }.execute();
      }
    });

    editPrintImageUrl = (EditText) findViewById(R.id.edit_print_image_url);
    buttonPrintImage = (Button) findViewById(R.id.button_print_image);
    buttonPrintImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        final String url = editPrintImageUrl.getText().toString();

        if (TextUtils.isEmpty(url)) {
          return;
        }

        new AsyncTask<Void, Void, Pair<Printer,Bitmap>>() {
          @Override
          protected Pair<Printer,Bitmap> doInBackground(Void... voids) {
            try {
              InputStream is = (InputStream) new URL(url).getContent();
              Bitmap b = BitmapFactory.decodeStream(is);

              Printer p = getPrinter();

              return Pair.create(p, b);
            } catch (IOException e) {
              e.printStackTrace();
            }
            return null;
          }

          @Override
          protected void onPostExecute(Pair<Printer,Bitmap> result) {
            if (result == null || result.first == null || result.second == null) {
              return;
            }
            new ImagePrintJob.Builder().bitmap(result.second).maxWidth().build().print(PrintTestActivity.this, account, result.first);
          }
        }.execute();
      }
    });

    buttonPrintImageSync = (Button) findViewById(R.id.button_print_image_sync);
    buttonPrintImageSync.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        final String url = editPrintImageUrl.getText().toString();

        if (TextUtils.isEmpty(url)) {
          return;
        }

        new AsyncTask<Void, Void, String>() {
          @Override
          protected String doInBackground(Void... voids) {
            try {
              InputStream is = (InputStream) new URL(url).getContent();
              Bitmap b = BitmapFactory.decodeStream(is);

              Printer p = getPrinter();
              PrintJob printJob = new ImagePrintJob.Builder().bitmap(b).maxWidth().build();
              return new PrintJobsConnector(PrintTestActivity.this).print(p, printJob);
            } catch (Exception e) {
              e.printStackTrace();
            }
            return null;
          }

          @Override
          protected void onPostExecute(String id) {
            if (id != null) {
              Toast.makeText(PrintTestActivity.this, "Print success, print job ID: " + id, Toast.LENGTH_SHORT).show();
            } else {
              Toast.makeText(PrintTestActivity.this, "Print failure", Toast.LENGTH_SHORT).show();
            }
          }
        }.execute();
      }
    });

    editPrintText = (EditText) findViewById(R.id.edit_print_text);
    buttonPrintText = (Button) findViewById(R.id.button_print_text);
    buttonPrintText.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        final String text = editPrintText.getText().toString();
        new TextPrintJob.Builder().text(text).build().print(PrintTestActivity.this, account);
      }
    });
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
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
