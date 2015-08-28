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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.util.CloverAuth;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WebServiceActivity extends Activity {

  private static final String TAG = WebServiceActivity.class.getSimpleName();

  private Account mAccount;
  private Button mButton;
  private TextView mLogText;

  private SimpleDateFormat dateFormat = new SimpleDateFormat("H:mm:ss");

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.web_service);

    mLogText = (TextView) findViewById(R.id.logText);
    mButton = (Button) findViewById(R.id.button);
    mButton.setOnClickListener(mRequestOnClickListener);

    mAccount = CloverAccount.getAccount(this);
    if (mAccount == null) {
      log("Account not found.");
      return;
    }
    log("Retrieved Clover Account: " + mAccount.name);
  }

  private View.OnClickListener mRequestOnClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      queryWebService();
      v.setEnabled(false);
    }
  };

  private void log(String text) {
    Log.i(TAG, text);

    StringBuilder sb = new StringBuilder(mLogText.getText().toString());
    if (sb.length() > 0) {
      sb.append('\n');
    }
    sb.append(dateFormat.format(new Date())).append(": ").append(text);
    mLogText.setText(sb.toString());
  }

  private void queryWebService() {
    new AsyncTask<Void, String, Void>() {

      @Override
      protected void onProgressUpdate(String... values) {
        String logString = values[0];
        log(logString);
      }

      @Override
      protected Void doInBackground(Void... params) {
        try {
          publishProgress("Requesting auth token");
          CloverAuth.AuthResult authResult = CloverAuth.authenticate(WebServiceActivity.this, mAccount);
          publishProgress("Successfully authenticated as " + mAccount + ".  authToken=" + authResult.authToken + ", authData=" + authResult.authData);

          if (authResult.authToken != null && authResult.baseUrl != null) {
            CustomHttpClient httpClient = CustomHttpClient.getHttpClient();
            String getNameUri = "/v2/merchant/name";
            String url = authResult.baseUrl + getNameUri + "?access_token=" + authResult.authToken;
            publishProgress("requesting merchant id using: " + url);
            String result = httpClient.get(url);
            JSONTokener jsonTokener = new JSONTokener(result);
            JSONObject root = (JSONObject) jsonTokener.nextValue();
            String merchantId = root.getString("merchantId");
            publishProgress("received merchant id: " + merchantId);

            // now do another get using the merchant id
            String inventoryUri = "/v2/merchant/" + merchantId + "/inventory/items";
            url = authResult.baseUrl + inventoryUri + "?access_token=" + authResult.authToken;

            publishProgress("requesting inventory items using: " + url);
            result = httpClient.get(url);
            publishProgress("received inventory items response: " + result);
          }
        } catch (Exception e) {
          publishProgress("Error retrieving merchant info from server" + e);
        }
        return null;
      }

      @Override
      protected void onPostExecute(Void aVoid) {
        mButton.setEnabled(true);
      }
    }.execute();
  }

  static class CustomHttpClient extends DefaultHttpClient {
    private static final int CONNECT_TIMEOUT = 60000;
    private static final int READ_TIMEOUT = 60000;
    private static final int MAX_TOTAL_CONNECTIONS = 5;
    private static final int MAX_CONNECTIONS_PER_ROUTE = 3;
    private static final int SOCKET_BUFFER_SIZE = 8192;
    private static final boolean FOLLOW_REDIRECT = false;
    private static final boolean STALE_CHECKING_ENABLED = true;
    private static final String CHARSET = HTTP.UTF_8;
    private static final HttpVersion HTTP_VERSION = HttpVersion.HTTP_1_1;
    private static final String USER_AGENT = "CustomHttpClient"; // + version

    public static CustomHttpClient getHttpClient() {
      CustomHttpClient httpClient = new CustomHttpClient();
      final HttpParams params = httpClient.getParams();
      HttpProtocolParams.setUserAgent(params, USER_AGENT);
      HttpProtocolParams.setContentCharset(params, CHARSET);
      HttpProtocolParams.setVersion(params, HTTP_VERSION);
      HttpClientParams.setRedirecting(params, FOLLOW_REDIRECT);
      HttpConnectionParams.setConnectionTimeout(params, CONNECT_TIMEOUT);
      HttpConnectionParams.setSoTimeout(params, READ_TIMEOUT);
      HttpConnectionParams.setSocketBufferSize(params, SOCKET_BUFFER_SIZE);
      HttpConnectionParams.setStaleCheckingEnabled(params, STALE_CHECKING_ENABLED);
      ConnManagerParams.setTimeout(params, CONNECT_TIMEOUT);
      ConnManagerParams.setMaxTotalConnections(params, MAX_TOTAL_CONNECTIONS);
      ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRouteBean(MAX_CONNECTIONS_PER_ROUTE));

      return httpClient;
    }

    public String get(String url) throws IOException, HttpException {
      String result;
      HttpGet request = new HttpGet(url);
      HttpResponse response = execute(request);
      int statusCode = response.getStatusLine().getStatusCode();
      if (statusCode == HttpStatus.SC_OK) {
        HttpEntity entity = response.getEntity();
        if (entity != null) {
          result = EntityUtils.toString(entity);
        } else {
          throw new HttpException("Received empty body from HTTP response");
        }
      } else {
        throw new HttpException("Received non-OK status from server: " + response.getStatusLine());
      }
      return result;
    }

    @SuppressWarnings("unused")
    public String post(String url, String body) throws IOException, HttpException {
      String result;
      HttpPost request = new HttpPost(url);
      HttpEntity bodyEntity = new StringEntity(body);
      request.setEntity(bodyEntity);
      HttpResponse response = execute(request);
      int statusCode = response.getStatusLine().getStatusCode();
      if (statusCode == HttpStatus.SC_OK) {
        HttpEntity entity = response.getEntity();
        if (entity != null) {
          result = EntityUtils.toString(entity);
        } else {
          throw new HttpException("Received empty body from HTTP response");
        }
      } else {
        throw new HttpException("Received non-OK status from server: " + response.getStatusLine());
      }
      return result;
    }
  }
}
