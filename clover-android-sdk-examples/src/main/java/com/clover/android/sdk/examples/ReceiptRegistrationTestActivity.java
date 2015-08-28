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
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v1.printer.ReceiptRegistration;
import com.clover.sdk.v1.printer.ReceiptRegistrationConnector;
import com.clover.sdk.v1.printer.job.PrintJob;
import com.clover.sdk.v1.printer.job.ReceiptPrintJob;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class ReceiptRegistrationTestActivity extends Activity {
  private static final String TAG = "PrintTestActivity";
  private static final int REQUEST_ACCOUNT = 0;

  private Account account;
  private ReceiptRegistrationConnector connector;

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

    textAddStatus = (TextView) findViewById(R.id.text_add_status);
    editRegister = (EditText) findViewById(R.id.edit_register);
    editRegister.setText(ReceiptRegistrationTestProvider.CONTENT_URI_IMAGE.toString());
    buttonRegister = (Button) findViewById(R.id.button_register);
    buttonRegister.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        connector.register(Uri.parse(editRegister.getText().toString()), new ReceiptRegistrationConnector.ReceiptRegistrationCallback<Void>() {
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

    textGetStatus = (TextView) findViewById(R.id.text_get_status);
    textGetResult = (TextView) findViewById(R.id.text_get_result);
    buttonGet = (Button) findViewById(R.id.button_get);
    buttonGet.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        connector.getRegistrations(new ReceiptRegistrationConnector.ReceiptRegistrationCallback<List<ReceiptRegistration>>() {
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

    editPrint = (EditText) findViewById(R.id.edit_print);
    buttonPrint = (Button) findViewById(R.id.button_print);
    buttonPrint.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        PrintJob pj = new ReceiptPrintJob.Builder().orderId(editPrint.getText().toString().toUpperCase()).build();
        pj.print(ReceiptRegistrationTestActivity.this, account);
      }
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

    if (account != null) {
      connect();
    } else {
      startAccountChooser();
    }
  }

  @Override
  protected void onPause() {
    disconnect();
    super.onPause();
  }

  private void connect() {
    disconnect();
    if (account != null) {
      connector = new ReceiptRegistrationConnector(this, account, null);
      connector.connect();
    }
  }

  private void disconnect() {
    if (connector != null) {
      connector.disconnect();
      connector = null;
    }
  }

  private void startAccountChooser() {
    Intent intent = AccountManager.newChooseAccountIntent(null, null, new String[]{CloverAccount.CLOVER_ACCOUNT_TYPE}, false, null, null, null, null);
    startActivityForResult(intent, REQUEST_ACCOUNT);
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
