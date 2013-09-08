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
import android.widget.Toast;

import com.clover.sdk.util.AuthTask;
import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.util.CloverAuth;
import com.clover.sdk.v1.app.AppNotification;
import com.clover.sdk.v1.app.AppNotificationReceiver;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppNotificationTestActivity extends Activity {

  private static final String TAG = AppNotificationTestActivity.class.getSimpleName();

  private TextView logText;
  private EditText appEventText;
  private EditText payloadText;
  private Button sendButton;
  private AuthTask authTask;
  private CloverAuth.AuthResult authResult;

  private SimpleDateFormat dateFormat = new SimpleDateFormat("H:mm:ss");

  private AppNotificationReceiver receiver = new AppNotificationReceiver() {
    @Override
    public void onReceive(AppNotification notification) {
      log("Received " + notification);
    }
  };

  private class SendNotificationTask extends AsyncTask<AppNotification, Void, Exception> {

    @Override
    protected Exception doInBackground(AppNotification... notifications) {
      try {
        // Post the app notification to the REST API.
        AppNotification notification = notifications[0];
        Log.i(TAG, "Sending " + notification);

        InventoryTestActivity.CustomHttpClient client = InventoryTestActivity.CustomHttpClient.getHttpClient();
        JSONObject request = new JSONObject();
        request.put("notification", notification.toJson());

        String uri = authResult.baseUrl + "/v2/merchant/" + authResult.merchantId + "/apps/" +
                authResult.appId + "/notifications?access_token=" + authResult.authToken;
        Log.i(TAG, "Posting app notification to " + uri);
        client.post(uri, request.toString());
      } catch (Exception e) {
        String msg = getResources().getString(R.string.error_while_sending, e.getMessage());
        Log.w(TAG, msg);
        return e;
      }
      return null;
    }

    @Override
    protected void onPostExecute(Exception e) {
      super.onPostExecute(e);
      if (e == null) {
        log("Successfully sent notification.");
      } else {
        log("Unable to send notification: " + e.toString());
      }
    }
  }

  private View.OnClickListener sendListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      if (authResult == null) {
        Toast.makeText(AppNotificationTestActivity.this, R.string.cannot_send_auth_failed, Toast.LENGTH_LONG);
        return;
      }

      String appEvent = appEventText.getText().toString();
      String payload = payloadText.getText().toString();
      AppNotification notification = new AppNotification(appEvent, payload);
      log("Sending " + notification);

      SendNotificationTask task = new SendNotificationTask();
      task.execute(notification);
    }
  };

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
          authResult = result;
          log("Auth successful: " + result + ", " + result.authData);
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
    receiver.unregister();
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
