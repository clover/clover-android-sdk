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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v3.cash.CashEvents;

public class CashEventTestActivity extends Activity {
  private static final String TAG = "CashEventTestActivity";

  private TextView statusText;

  private Account account;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cashevent_test);

    statusText = (TextView) findViewById(R.id.status);

    account = CloverAccount.getAccount(this);

    Button b = (Button) findViewById(R.id.button);
    b.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        statusText.setText("");
        doExample();
      }
    });
  }

  private void doExample() {
    new AsyncTask<Void, Void, Boolean>() {
      @Override
      protected Boolean doInBackground(Void... params) {
        CashEvents cashEvents = new CashEvents(CashEventTestActivity.this, account);
        return cashEvents.addCash(50l, "This is an add test");
      }

      @Override
      protected void onPostExecute(Boolean result) {
        if (result != null && result) {
          statusText.setText("Add Success");
        } else {
          statusText.setText("Add Failure");
        }
      }
    }.execute();

    new AsyncTask<Void, Void, Boolean>() {
      @Override
      protected Boolean doInBackground(Void... params) {
        try {
          Thread.sleep(1500);
        } catch (InterruptedException ex) {};
        CashEvents cashEvents = new CashEvents(CashEventTestActivity.this, account);
        return cashEvents.removeCash(-50l, "This is a removal test");
      }

      @Override
      protected void onPostExecute(Boolean result) {
        if (result != null && result) {
          statusText.setText(statusText.getText() + ", Remove Success");
        } else {
          statusText.setText(statusText.getText() + ", Remove Failure");
        }
      }
    }.execute();
  }
}
