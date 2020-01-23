/**
 * Copyright (C) 2016 Clover Network, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
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
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Obtain authentication information. The primary use of this class is to get the Clover
 * cloud base URL (see {@link AuthResult#baseUrl}) and access token
 * (see {@link AuthResult#authToken) used for REST API calls to the Clover cloud.
 * <p/>
 * The information returned from {@link #authenticate(Context, boolean, Long, TimeUnit)} is
 * cached for 24 hours on the device. After 24 hours the information is validated
 * with the Clover cloud. This means that periodically, a call to
 * {@link #authenticate(Context, boolean, Long, TimeUnit)} will result in a blocking
 * network request. Your app should be prepared for this. Do not call this method
 * on the main UI thread, and be prepared to communicate any delays to the user.
 * Access tokens are guaranteed to be valid for at least 24 hours. There
 * is no situation where you can obtain an invalid access token from this method,
 * as long as your app is a valid Clover app in the Clover cloud and is installed
 * to this merchant.
 * <p/>
 * The information returned from {@link #authenticate(Context, boolean, Long, TimeUnit)}
 * should not be persisted or cached. Call this method prior to all request to the Clover cloud.
 * <p/>
 * The forceValidate argument to {@link #authenticate(Context, boolean, Long, TimeUnit)}
 * causes the token to be validated always, as opposed to every 24 hours as noted above.
 * Use of this is atypical.
 * <p/>
 * An app must be a Clover developer app and be installed to this merchant
 * in the Clover cloud to obtain this information.
 * It is not sufficient to only sideload an app.
 */
public class CloverAuth {
  private static final String TAG = CloverAuth.class.getSimpleName();

  /**
   * Authenticates with the Clover service.  This method makes a network call to the
   * Clover service.  It should be run on a background thread.
   * <p/>
   * This method will block indefinitely for a result.
   *
   * @deprecated Use {@link #authenticate(Context, boolean, Long, TimeUnit)}
   * on SDK levels 26 or higher.
   *
   */
  public static AuthResult authenticate(Activity activity, Account account, boolean forceValidateToken) throws OperationCanceledException, AuthenticatorException, IOException {
    return authenticate(activity, account, forceValidateToken, null, null);
  }

  /**
   * Authenticates with the Clover service.  This method makes a network call to the
   * Clover service.  It should be run on a background thread.
   *
   * @deprecated Use {@link #authenticate(Context, boolean, Long, TimeUnit)}
   * on SDK levels 26 or higher.
   *
   * @param activity           the activity that initiated the authentication
   * @param account            the account used for authentication
   * @param forceValidateToken flag for if token should be validated against API.
   *                           Increases response latency, use only when necessary.
   * @param timeout the maximum time to wait
   * @param unit the time unit of the timeout argument. This must not be null.
   *
   * @see #authenticate(Context, boolean, Long, TimeUnit)
   */
  public static AuthResult authenticate(Activity activity, Account account, boolean forceValidateToken, Long timeout, TimeUnit unit) throws OperationCanceledException, AuthenticatorException, IOException {
    ensureTargetSdkLessThan25(activity);

    Log.d(TAG, "Authenticating " + account);
    final Bundle options = new Bundle();
    options.putBoolean(CloverAccount.KEY_FORCE_VALIDATE, forceValidateToken);
    AccountManager accountManager = AccountManager.get(activity);
    AccountManagerFuture<Bundle> future = accountManager.getAuthToken(account, CloverAccount.CLOVER_AUTHTOKEN_TYPE, options, activity, null, null);
    Bundle result;
    if (timeout != null && unit != null) {
      Log.d(TAG, "Getting result with timeout " + timeout + " (" + unit + ")");
      result = future.getResult(timeout, unit);
    } else {
      Log.d(TAG, "Getting result (no timeout)");
      result = future.getResult();
    }
    Log.v(TAG, "Bundle result returned from account manager: ");
    for (String key : result.keySet()) {
      Log.v(TAG, key + " => " + result.get(key));
    }
    return new AuthResult(result);
  }

  /**
   * @deprecated Use {@link #authenticate(Context, boolean, Long, TimeUnit)}
   * on SDK levels 26 or higher.
   */
  public static AuthResult authenticate(Context context, Account account, boolean forceValidateToken) throws OperationCanceledException, AuthenticatorException, IOException {
    return authenticate(context, account, forceValidateToken, null, null);
  }

  /**
   * @deprecated Use {@link #authenticate(Context, boolean, Long, TimeUnit)}
   * on SDK levels 26 or higher.
   *
   * @see #authenticate(Context, boolean, Long, TimeUnit)
   */
  public static AuthResult authenticate(Context context, Account account, boolean forceValidateToken, Long timeout, TimeUnit unit) throws OperationCanceledException, AuthenticatorException, IOException {
    ensureTargetSdkLessThan25(context);

    Log.d(TAG, "Authenticating " + account);
    final Bundle options = new Bundle();
    options.putBoolean(CloverAccount.KEY_FORCE_VALIDATE, forceValidateToken);
    AccountManager accountManager = AccountManager.get(context);
    AccountManagerFuture<Bundle> future = accountManager.getAuthToken(account, CloverAccount.CLOVER_AUTHTOKEN_TYPE, options, false, null, null);
    Bundle result;
    if (timeout != null && unit != null) {
      Log.d(TAG, "Getting result with timeout " + timeout + " (" + unit + ")");
      result = future.getResult(timeout, unit);
    } else {
      Log.d(TAG, "Getting result (no timeout)");
      result = future.getResult();
    }
    Log.v(TAG, "Bundle result returned from account manager: ");
    for (String key : result.keySet()) {
      Log.v(TAG, key + " => " + result.get(key));
    }
    return new AuthResult(result);
  }

  /**
   * @deprecated Use {@link #authenticate(Context, boolean, Long, TimeUnit)}
   * on SDK levels 26 or higher.
   *
   * @see #authenticate(Context, boolean, Long, TimeUnit)
   */
  public static AuthResult authenticate(Activity activity, Account account) throws OperationCanceledException, AuthenticatorException, IOException {
    return authenticate(activity, account, false);
  }

  /**
   * @deprecated Use {@link #authenticate(Context, boolean, Long, TimeUnit)}
   * on SDK levels 26 or higher.
   *
   * @see #authenticate(Context, boolean, Long, TimeUnit)
   */
  public static AuthResult authenticate(Context context, Account account) throws OperationCanceledException, AuthenticatorException, IOException {
    return authenticate(context, account, false);
  }

  public static final String AUTH_AUTHORITY = "com.clover.app.auth";
  public static final Uri AUTH_URI = Uri.parse("content://" + AUTH_AUTHORITY);

  public static final String METHOD_AUTH = "auth";

  private static final ExecutorService exec = Executors.newSingleThreadExecutor();

  /**
   * Equivalent to calling {@link #authenticate(Context, boolean, Long, TimeUnit)} with
   * force validate false, and a timeout of 20 seconds.
   *
   * @see #authenticate(Context, boolean, Long, TimeUnit)
   */
  public static AuthResult authenticate(Context context) throws InterruptedException, ExecutionException, TimeoutException {
    return authenticate(context, false, 20L, TimeUnit.SECONDS);
  }

  /**
   * Authenticates with the Clover service.  This method may result in a network call to the
   * Clover cloud.  It should be run on a background thread.
   * <p/>
   * It is recommended that you prefer {@link #authenticate(Context)} over this method
   * as it provides reasonable defaults for arguments.
   * <p/>
   * This method differs from {@link #authenticate(Context, Account, boolean, Long, TimeUnit)} in
   * that it does not require an {@link Account} argument. If your app targets API 26 or higher
   * you MUST use this method over the variants that accept an account as your app will
   * be restricted from obtaining the Clover account from {@link CloverAccount} (via
   * {@link AccountManager}).
   *
   * @param context            the context that initiated the authentication.
   * @param forceValidateToken should the token should be validated against the Clover cloud?
   *                           Tokens are validated every 24 hours regardless of this flag.
   *                           Passing true here is atypical and is discouraged.
   * @param timeout the maximum time to wait. If null, wait forever. Typically this call will
   *                finish in the order of tens of milliseconds, however, every 24 hours
   *                the access token is validated against the Clover cloud, causing this
   *                call to result in a blocking network request that can take longer.
   * @param unit the time unit of the timeout argument. If null, wait forever.
   */
  public static AuthResult authenticate(Context context, boolean forceValidateToken, Long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
    Log.d(TAG, "Authenticating with provider (no account)");

    final Bundle options = new Bundle();
    options.putBoolean(CloverAccount.KEY_FORCE_VALIDATE, forceValidateToken);

    Log.d(TAG, "Getting result from provider with timeout " + timeout + " (" + unit + ")");

    Future<AuthResult> future = exec.submit(() -> {
      final Bundle result = context.getContentResolver().call(AUTH_URI, METHOD_AUTH, null, options);
      if (result == null) {
        Log.w(TAG, "Bundle result null from provider");
        return null;
      }
      Log.v(TAG, "Bundle result returned from provider:");
      for (String key : result.keySet()) {
        Log.v(TAG, key + " => " + result.get(key));
      }

      return new AuthResult(result);
    });

    if (timeout != null && unit != null) {
      return future.get(timeout, unit);
    } else {
      return future.get();
    }
  }

  private static void ensureTargetSdkLessThan25(Context context) {
    int targetSdk = getTargetSdk(context);
    if (targetSdk > Build.VERSION_CODES.N_MR1) {
      throw new IllegalStateException(String.format("This method cannot be called if your app targets SDK levels > %d. Please use the variant that does not require an account parameter.", Build.VERSION_CODES.N_MR1));
    }
  }

  private static int getTargetSdk(Context context) {
    try {
      return context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).targetSdkVersion;
    } catch (PackageManager.NameNotFoundException e) {
      return -1;
    }
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

    @Override
    public String toString() {
      return "AuthResult{" +
          "authToken='" + authToken + '\'' +
          ", baseUrl='" + baseUrl + '\'' +
          ", errorMessage='" + errorMessage + '\'' +
          ", merchantId='" + merchantId + '\'' +
          ", appId='" + appId + '\'' +
          ", authData=" + authData +
          '}';
    }
  }
}
