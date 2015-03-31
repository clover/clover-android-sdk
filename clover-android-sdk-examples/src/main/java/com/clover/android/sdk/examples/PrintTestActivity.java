/*
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
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.printer.CashDrawer;
import com.clover.sdk.v1.printer.job.ImagePrintJob;
import com.clover.sdk.v1.printer.job.ReceiptPrintJob;
import com.clover.sdk.v1.printer.job.TextPrintJob;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class PrintTestActivity extends Activity {
  private static final String TAG = "PrintTestActivity";
  private static final int REQUEST_ACCOUNT = 0;

  private Account account;

  private EditText editPrintOrderId;
  private EditText editPrintOrderPid;
  private Button buttonPrintOrderReceipt;

  private EditText editOpenCashdrawer;
  private Button buttonOpenCashdrawer;

  private EditText editPrintImageId;
  private EditText editPrintImageUrl;
  private Button buttonPrintImage;

  private EditText editPrintText;
  private Button buttonPrintText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_print_test);

    editPrintOrderId = (EditText) findViewById(R.id.edit_print_order_id);
    editPrintOrderPid = (EditText) findViewById(R.id.edit_print_order_pid);
    buttonPrintOrderReceipt = (Button) findViewById(R.id.button_print_order_receipt);
    buttonPrintOrderReceipt.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        final String id = editPrintOrderId.getText().toString().toUpperCase();
        final String pid = editPrintOrderPid.getText().toString().toUpperCase();

        if (TextUtils.isEmpty(id)) {
          return;
        }

        new ReceiptPrintJob.Builder().orderId(id).build().print(PrintTestActivity.this, account);
      }
    });

    editOpenCashdrawer = (EditText) findViewById(R.id.edit_open_cashdrawer);
    buttonOpenCashdrawer = (Button) findViewById(R.id.button_open_cashdrawer);
    buttonOpenCashdrawer.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String printerId = editOpenCashdrawer.getText().toString();
        if (TextUtils.isEmpty(printerId)) {
          CashDrawer.open(PrintTestActivity.this, account);
        } else {
          // TODO
        }
      }
    });

    editPrintImageId = (EditText) findViewById(R.id.edit_print_image_id);
    editPrintImageUrl = (EditText) findViewById(R.id.edit_print_image_url);
    buttonPrintImage = (Button) findViewById(R.id.button_print_image);
    buttonPrintImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        final String id = editPrintImageId.getText().toString();
        final String url = editPrintImageUrl.getText().toString();

        if (TextUtils.isEmpty(url)) {
          return;
        }

        new AsyncTask<Void, Void, Bitmap>() {
          @Override
          protected Bitmap doInBackground(Void... voids) {
            try {
              InputStream is = (InputStream) new URL(url).getContent();
              Bitmap b = BitmapFactory.decodeStream(is);
              return b;
            } catch (IOException e) {
              e.printStackTrace();
            }
            return null;
          }

          @Override
          protected void onPostExecute(Bitmap bitmap) {
            if (bitmap == null) {
              return;
            }
            new ImagePrintJob.Builder().bitmap(bitmap).maxWidth().build().print(PrintTestActivity.this, account);
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
  protected void onResume() {
    super.onResume();

    if (account != null) {
    } else {
      startAccountChooser();
    }
  }

  private void startAccountChooser() {
    Intent intent = AccountManager.newChooseAccountIntent(null, null, new String[]{CloverAccount.CLOVER_ACCOUNT_TYPE}, false, null, null, null, null);
    startActivityForResult(intent, REQUEST_ACCOUNT);
  }

  @Override
  protected void onPause() {
    super.onPause();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_ACCOUNT) {
      if (resultCode == RESULT_OK) {
        String name = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String type = data.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);

        account = new Account(name, type);
      } else {
        if (account == null) {
          finish();
        }
      }
    }
  }
}
