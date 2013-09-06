/*
 * Copyright (C) 2013 Clover Network, Inc.
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.clover.sdk.util.AuthTask;
import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.util.CloverAuth;
import com.clover.sdk.v1.app.AppNotification;
import com.clover.sdk.v1.app.AppNotificationReceiver;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppNotificationTestActivity extends Activity {

  private static final String TAG = AppNotificationTestActivity.class.getSimpleName();

  private TextView logText;
  private EditText appEventText;
  private EditText payloadText;
  private Button sendButton;
  private AuthTask authTask;

  private SimpleDateFormat dateFormat = new SimpleDateFormat("H:mm:ss");

  private View.OnClickListener sendListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      String appEvent = appEventText.getText().toString();
      String payload = payloadText.getText().toString();
      AppNotification notification = new AppNotification(appEvent, payload);
      log("Attempted to send " + notification + ", but this feature is not implemented yet.");
    }
  };

  private AppNotificationReceiver receiver = new AppNotificationReceiver() {
    @Override
    public void onReceive(AppNotification notification) {
      log("Received " + notification);
    }
  };

  private class SendNotificationTask extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... voids) {
      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_app_notification_test);
    getActionBar().setTitle(R.string.app_notification_test);

    logText = (TextView) findViewById(R.id.logText);
    appEventText = (EditText) findViewById(R.id.appEventText);
    payloadText = (EditText) findViewById(R.id.payloadText);
    sendButton = (Button) findViewById(R.id.sendButton);
    sendButton.setOnClickListener(sendListener);

    receiver.register(this);

    log("Starting app notification test.");

    final Account account = CloverAccount.getAccount(AppNotificationTestActivity.this);
    if (account == null) {
      log("Account not found.");
      return;
    }

    authTask = new AuthTask(this) {
      @Override
      protected void onAuthComplete(boolean success, CloverAuth.AuthResult result) {
        if (success) {
          log("Auth successful.");
        } else {
          log("Auth error: " + getErrorMessage());
        }
      }
    };
    authTask.execute(account);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    authTask.cancel(true);
  }

  /**
   * Write the given message to the device log and to the on-screen log.
   */
  private void log(String text) {
    Log.i(TAG, text);

    StringBuilder sb = new StringBuilder(logText.getText().toString());
    if (sb.length() > 0) {
      sb.append('\n');
    }
    sb.append(dateFormat.format(new Date())).append(": ").append(text);
    logText.setText(sb.toString());
  }
}
