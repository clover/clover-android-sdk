/*
 * Copyright (C) 2020 Clover Network, Inc.
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
import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v1.printer.ReceiptRegistration;
import com.clover.sdk.v1.printer.ReceiptRegistrationConnector;
import com.clover.sdk.v1.printer.job.StaticPaymentPrintJob;
import com.clover.sdk.v3.order.Order;
import com.clover.sdk.v3.order.OrderConnector;
import com.clover.sdk.v3.order.OrderContract;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

@SuppressLint("StaticFieldLeak")
public class ReceiptRegistrationTestActivity extends Activity {
  private Account account;
  private ReceiptRegistrationConnector receiptRegistrationConnector;
  private OrderConnector orderConnector;

  private TextView textAddStatus;
  private EditText editRegister;
  private Button buttonRegister;

  private TextView textGetStatus;
  private TextView textGetResult;
  private Button buttonGet;

  private EditText editPrint;
  private Button buttonPrint;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_receipt_registration_test);

    account = CloverAccount.getAccount(getApplicationContext());

    connect();

    textAddStatus = findViewById(R.id.text_add_status);
    editRegister = findViewById(R.id.edit_register);
    editRegister.setText(ReceiptRegistrationTestProvider.CONTENT_URI_IMAGE.toString());
    buttonRegister = findViewById(R.id.button_register);
    buttonRegister.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        receiptRegistrationConnector.register(Uri.parse(editRegister.getText().toString()),
            new ReceiptRegistrationConnector.ReceiptRegistrationCallback<Void>() {
          @Override
          public void onServiceSuccess(Void result, ResultStatus status) {
            super.onServiceSuccess(result, status);
            updateAdd(status);
          }

          @Override
          public void onServiceFailure(ResultStatus status) {
            super.onServiceFailure(status);
            updateAdd(status);
          }

          @Override
          public void onServiceConnectionFailure() {
            super.onServiceConnectionFailure();
            updateAdd(null);
          }
        });
      }
    });

    textGetStatus = findViewById(R.id.text_get_status);
    textGetResult = findViewById(R.id.text_get_result);
    buttonGet = findViewById(R.id.button_get);
    buttonGet.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        receiptRegistrationConnector.getRegistrations(new ReceiptRegistrationConnector.ReceiptRegistrationCallback<List<ReceiptRegistration>>() {
          @Override
          public void onServiceSuccess(List<ReceiptRegistration> result, ResultStatus status) {
            super.onServiceSuccess(result, status);
            updateGet(status, result);
          }

          @Override
          public void onServiceFailure(ResultStatus status) {
            super.onServiceFailure(status);
            updateGet(status, null);
          }

          @Override
          public void onServiceConnectionFailure() {
            super.onServiceConnectionFailure();
            updateGet(null, null);
          }
        });
      }
    });

    editPrint = findViewById(R.id.edit_print);
    buttonPrint = findViewById(R.id.button_print);
    buttonPrint.setOnClickListener(view -> {
      final String orderId = editPrint.getText().toString().toUpperCase();

      new AsyncTask<Void,Void, Order>() {
        @Override
        protected Order doInBackground(Void... voids) {
          try {
            return orderConnector.getOrder(orderId);
          } catch (Exception e) {
            e.printStackTrace();
          }
          return null;
        }

        @Override
        protected void onPostExecute(Order order) {
          if (order != null) {
            new StaticPaymentPrintJob.Builder()
                .order(order)
                .build().print(ReceiptRegistrationTestActivity.this, account);
          }
        }
      }.execute();
    });
  }

  private void updateAdd(ResultStatus resultStatus) {
    textAddStatus.setText("<add registration " + (resultStatus != null ? resultStatus : "connect failure") + ": " + DateFormat.getDateTimeInstance().format(new Date()) + ">");
  }

  private void updateGet(ResultStatus resultStatus, List<ReceiptRegistration> result) {
    textGetStatus.setText("<add registration " + (resultStatus != null ? resultStatus : "connect failure") + ": " + DateFormat.getDateTimeInstance().format(new Date()) + ">");
    if (result != null) {
      textGetResult.setText(result.toString());
    } else {
      textGetResult.setText("");
    }
  }


  @Override
  protected void onResume() {
    super.onResume();

    new AsyncTask<Void,Void,String>() {
      @Override
      protected String doInBackground(Void... voids) {
        return getLastPaidOrderId();
      }

      @Override
      protected void onPostExecute(String orderId) {
        editPrint.setText(orderId);
      }
    }.execute();
  }

  @Override
  protected void onDestroy() {
    disconnect();
    super.onDestroy();
  }

  private void connect() {
    disconnect();

    receiptRegistrationConnector = new ReceiptRegistrationConnector(this, account, null);
    receiptRegistrationConnector.connect();

    orderConnector = new OrderConnector(getApplicationContext(),
        CloverAccount.getAccount(getApplicationContext()), null);
    orderConnector.connect();

  }

  private void disconnect() {
    if (receiptRegistrationConnector != null) {
      receiptRegistrationConnector.disconnect();
      receiptRegistrationConnector = null;
    }
    if (orderConnector != null) {
      orderConnector.disconnect();
      orderConnector = null;
    }
  }

  private String getLastPaidOrderId() {
    try (Cursor c = getContentResolver().query(OrderContract.OrderSummary.contentUriWithAccount(account),
        null, OrderContract.OrderSummary.AMOUNT_PAID + " IS NOT NULL",
        null, null)) {
      if (c == null || !c.moveToLast())  {
        return null;
      }
      return c.getString(c.getColumnIndex(OrderContract.OrderSummary.ID));
    }
  }
}
