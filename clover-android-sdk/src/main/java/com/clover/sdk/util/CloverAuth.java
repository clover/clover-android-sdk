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
package com.clover.sdk.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

public class CloverAuth {

  private static String TAG = CloverAuth.class.getSimpleName();

  /**
   * Authenticates with the Clover service.  This method makes a network call to the
   * Clover service.  It should be run on a background thread.
   *
   * @param activity the activity that initiated the authentication
   * @param account  the account used for authentication
   */
  public static AuthResult authenticate(Activity activity, Account account) throws OperationCanceledException, AuthenticatorException, IOException {
      Log.d(TAG, "Authenticating " + account);
      AccountManager accountManager = AccountManager.get(activity);
      AccountManagerFuture<Bundle> future = accountManager.getAuthToken(account, "com.clover.account.token.app", null, activity, null, null);
      Bundle result = future.getResult();
      Log.v(TAG, "Bundle result returned from account manager: ");
      for (String key : result.keySet()) {
        Log.v(TAG, key + " => " + result.get(key));
      }
      return new AuthResult(result);
  }

  public static AuthResult authenticate(Context context, Account account) throws OperationCanceledException, AuthenticatorException, IOException {
    Log.d(TAG, "Authenticating " + account);
    AccountManager accountManager = AccountManager.get(context);
    AccountManagerFuture<Bundle> future = accountManager.getAuthToken(account, "com.clover.account.token.app", false, null, null);
    Bundle result = future.getResult();
    Log.v(TAG, "Bundle result returned from account manager: ");
    for (String key : result.keySet()) {
      Log.v(TAG, key + " => " + result.get(key));
    }
    return new AuthResult(result);
  }

  /**
   * Represents the data returned when an account authenticates with the Clover service.
   */
  public static class AuthResult {
    /**
     * The auth token, used for sending subsequent requests to the service.
     */
    public final String authToken;

    /**
     * The base URL of the Clover service.  REST API endpoints are relative to this URL.
     */
    public final String baseUrl;

    /**
     * Error message that was generated during authentication, or {@code null}.
     */
    public final String errorMessage;

    /**
     * The id of the merchant associated with the authenticated account.
     */
    public final String merchantId;

    /**
     * The id of the app that performed authentication.
     */
    public final String appId;

    /**
     * The complete set of data returned by {@link android.accounts.AccountManager}.
     */
    public final Bundle authData;

    public AuthResult(Bundle authData) {
      this.authData = new Bundle(authData);
      authToken = authData.getString(AccountManager.KEY_AUTHTOKEN);
      baseUrl = authData.getString(CloverAccount.KEY_BASE_URL);
      merchantId = authData.getString(CloverAccount.KEY_MERCHANT_ID);
      appId = authData.getString(CloverAccount.KEY_APP_ID);
      errorMessage = authData.getString(AccountManager.KEY_ERROR_MESSAGE);
    }

    public String toString() {
      return AuthResult.class.getSimpleName() + ": {authToken=" + authToken + ", baseUrl=" + baseUrl + "}";
    }
  }
}
