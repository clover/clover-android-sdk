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
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.clover.sdk.util.CloverAccount;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class CreateCustomTenderTestActivity extends Activity {
  private static final String TAG = "CreateCustomTenderTestActivity";

  private static final int REQUEST_ACCOUNT = 1;

  private Account account;
  private String appAuthToken;
  private String merchantId;

  private TextView resultText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tenders_test);

    resultText = (TextView) findViewById(R.id.result);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_ACCOUNT && resultCode == RESULT_OK) {
      String name = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
      String type = data.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);

      account = new Account(name, type);
    }
  }

  private void startAccountChooser() {
    Intent intent = AccountManager.newChooseAccountIntent(null, null, new String[]{CloverAccount.CLOVER_ACCOUNT_TYPE}, false, null, null, null, null);
    startActivityForResult(intent, REQUEST_ACCOUNT);
  }

  @Override
  protected void onResume() {
    super.onResume();

    if (account != null) {
      if (TextUtils.isEmpty(appAuthToken)) {
        AccountManager.get(this).getAuthToken(account, CloverAccount.CLOVER_AUTHTOKEN_TYPE, null, this,
            new AccountManagerCallback<Bundle>() {
              public void run(AccountManagerFuture<Bundle> future) {
                try {
                  Bundle result = future.getResult();
                  appAuthToken = result.getString(AccountManager.KEY_AUTHTOKEN);
                  merchantId = result.getString("merchant_id");

                  registerTender();
                } catch (OperationCanceledException e) {
                  Log.e(TAG, "Authentication cancelled", e);
                } catch (Exception e) {
                  Log.e(TAG, "Error retrieving authentication", e);
                }
              }
            }, null);
      }
    } else {
      startAccountChooser();
    }
  }

  private void registerTender() {
    final String tenderName = "Clover Example Tender";
    final String packageName = getPackageName();

    resultText.setText("registering " + tenderName);

    AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
      @Override
      protected String doInBackground(String... voids) {
        if (!tenderExists(tenderName, packageName)) {
          return addTender(tenderName, packageName);
        } else {
          return tenderName + " already registered";
        }
      }

      protected void onPostExecute(String result) {
        resultText.setText(result);
      }
    };
    task.execute();
  }

  private boolean tenderExists(String tenderName, String packageName) {
    CustomHttpClient httpClient = CustomHttpClient.getHttpClient();
    String tendersUrl = "https://api.clover.com/v2/merchant/" + merchantId + "/tenders" + "?access_token=" + appAuthToken;
    String result = null;
    try {
      result = httpClient.get(tendersUrl);
      JSONObject resultObj = new JSONObject(result);
      JSONArray tenders = resultObj.getJSONArray("tenders");
      for (int i = 0; i < tenders.length(); i++) {
        JSONObject tender = tenders.getJSONObject(i);
        String labelKey = tender.getString("labelKey");
        if (packageName.equals(labelKey)) {
          return true;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return false;
  }

  private String addTender(String tenderName, String packageName) {
    JSONObject req = new JSONObject();

    try {
      JSONObject tenderObj = new JSONObject();

      tenderObj.put("label", tenderName);
      tenderObj.put("labelKey", packageName);
      tenderObj.put("enabled", true);
      tenderObj.put("opensCashDrawer", false);

      req.put("tender", tenderObj);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    CustomHttpClient httpClient = CustomHttpClient.getHttpClient();
    String tendersUrl = "https://api.clover.com/v2/merchant/" + merchantId + "/tenders" + "?access_token=" + appAuthToken;
    String result = null;
    try {
      result = httpClient.post(tendersUrl, req.toString());
      JSONObject resultObj = new JSONObject(result);

      return tenderName + " registered";
    } catch (Exception e) {
      e.printStackTrace();
      return tenderName + " register failed";
    }
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
