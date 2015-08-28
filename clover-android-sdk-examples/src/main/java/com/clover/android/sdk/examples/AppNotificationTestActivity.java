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
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.clover.sdk.util.AuthTask;
import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.util.CloverAuth;
import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v1.ServiceConnector;
import com.clover.sdk.v1.app.AppConnector;
import com.clover.sdk.v1.app.AppNotification;
import com.clover.sdk.v1.app.AppNotificationReceiver;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Demonstrates sending an app notification by either binding to the app service
 * or by sending an HTTP POST to the Clover Service REST endpoint.
 */
public class AppNotificationTestActivity extends Activity {

  private static final String TAG = AppNotificationTestActivity.class.getSimpleName();
  private static final String KEY_LOG = "log";

  enum ConnectionType {HTTP, BoundService}

  ;

  private TextView logText;
  private EditText appEventText;
  private EditText payloadText;
  private Button sendButton;
  private AuthTask authTask;
  private Spinner connectionTypeSpinner;
  private CloverAuth.AuthResult authResult;
  private AppConnector connector;

  private SimpleDateFormat dateFormat = new SimpleDateFormat("H:mm:ss");

  /**
   * Receives an app notification and logs it.
   */
  private AppNotificationReceiver receiver = new AppNotificationReceiver() {
    @Override
    public void onReceive(Context context, AppNotification notification) {
      log("Received " + notification);
    }
  };

  private ConnectionType getConnectionType() {
    if (connectionTypeSpinner.getSelectedItemPosition() == 0) {
      return ConnectionType.BoundService;
    }
    return ConnectionType.HTTP;
  }

  /**
   * Sends a notification by POSTing to the REST endpoint.
   */
  private class HttpNotificationTask extends AsyncTask<AppNotification, Void, Exception> {

    @Override
    protected Exception doInBackground(AppNotification... notifications) {
      AppNotification notification = notifications[0];

      try {
        // Post the app notification to the REST API.
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
        log("Successfully sent notification with HTTP.");
      } else {
        log("Unable to send notification with HTTP: " + e.toString());
      }
    }
  }

  private ServiceConnector.Callback<Void> serviceNotifyCallback = new ServiceConnector.Callback<Void>() {
    @Override
    public void onServiceSuccess(Void result, ResultStatus status) {
      log("Successfully sent notification with the app service: " + status);
    }

    @Override
    public void onServiceFailure(ResultStatus status) {
      log("Unable to send notification with the app service: " + status);
    }

    @Override
    public void onServiceConnectionFailure() {
      log("Service connection failure.");
    }
  };

  private View.OnClickListener sendListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      String appEvent = appEventText.getText().toString();
      String payload = payloadText.getText().toString();
      AppNotification notification = new AppNotification(appEvent, payload);

      switch (getConnectionType()) {
        case BoundService:
          try {
            connector.notify(notification, serviceNotifyCallback);
          } catch (RemoteException e) {
            log("Unable to call service: " + e);
          }
          break;

        case HTTP:
          if (authResult == null) {
            Toast.makeText(AppNotificationTestActivity.this, R.string.cannot_send_auth_failed, Toast.LENGTH_LONG).show();
            return;
          }
          log("Sending notification with HTTP");
          HttpNotificationTask httpTask = new HttpNotificationTask();
          httpTask.execute(notification);
      }
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_app_notification_test);
    getActionBar().setTitle(R.string.app_notification_test);

    logText = (TextView) findViewById(R.id.log_text);
    appEventText = (EditText) findViewById(R.id.app_event_text);
    payloadText = (EditText) findViewById(R.id.payload_text);
    sendButton = (Button) findViewById(R.id.send_button);
    connectionTypeSpinner = (Spinner) findViewById(R.id.connection_type_spinner);

    sendButton.setOnClickListener(sendListener);
    if (savedInstanceState != null) {
      logText.setText(savedInstanceState.getString(KEY_LOG));
    }

    receiver.register(this);

    log("Starting app notification test.");

    // Start authentication.
    final Account account = CloverAccount.getAccount(AppNotificationTestActivity.this);
    if (account == null) {
      log("Account not found.");
      return;
    }

    log("Authenticating.");
    authTask = new AuthTask(this) {
      @Override
      protected void onAuthComplete(boolean success, CloverAuth.AuthResult result) {
        if (success) {
          authResult = result;
          log("Auth successful: " + result + ", " + result.authData);
        } else {
          log("Auth error: " + getErrorMessage() + ".  Cannot use HTTP.");
        }
      }
    };
    authTask.execute(account);

    // Connect to the app service.
    connector = new AppConnector(this, account);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    authTask.cancel(true);
    receiver.unregister();
    connector.disconnect();
  }

  /**
   * Write the given message to the device log and to the on-screen log.
   */
  private void log(String text) {
    Log.w(TAG, text);

    StringBuilder sb = new StringBuilder(logText.getText().toString());
    if (sb.length() > 0) {
      sb.append('\n');
    }
    sb.append(dateFormat.format(new Date())).append(": ").append(text);
    logText.setText(sb.toString());
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putString(KEY_LOG, logText.getText().toString());
  }
}
