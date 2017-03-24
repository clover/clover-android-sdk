/**
 * Copyright (C) 2016 Clover Network, Inc.
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
import android.content.Context;
import android.util.Log;

/**
 * This class provides access to the Clover {@link android.accounts.Account} object on the device.
 *
 * @see android.accounts.Account
 * @see android.accounts.AccountManager
 */
public class CloverAccount {

  private static final String TAG = CloverAccount.class.getSimpleName();
  /**
   * The account type string for Clover accounts.
   */
  public static final String CLOVER_ACCOUNT_TYPE = "com.clover.account";
  public static final String CLOVER_AUTHTOKEN_TYPE = "com.clover.account.token.app";
  public static final String KEY_BASE_URL = "base_url";
  public static final String KEY_MERCHANT_ID = "merchant_id";
  public static final String KEY_APP_ID = "app_id";
  public static final String KEY_FORCE_VALIDATE = "force_validate";

  /**
   * {@link AccountManager#KEY_ERROR_CODE} indicating that an unknown error occurred retrieving
   * a Clover app auth token.
   */
  public static final int GET_AUTH_TOKEN_ERROR_CODE_UNKNOWN = -1;
  /**
   * {@link AccountManager#KEY_ERROR_CODE} indicating that a network-related error occurred
   * retrieving a Clover app auth token.
   */
  public static final int GET_AUTH_TOKEN_ERROR_CODE_NETWORK = -2;
  /**
   * {@link AccountManager#KEY_ERROR_CODE} indicating that no app token existed for the calling
   * application.
   */
  public static final int GET_AUTH_TOKEN_ERROR_CODE_NO_TOKEN = -3;
  /**
   * {@link AccountManager#KEY_ERROR_CODE} indicating that an undefined account type was passed.
   */
  public static final int GET_AUTH_TOKEN_ERROR_CODE_INVALID_TYPE = -4;

  private CloverAccount() {
  }

  /**
   * Get the Clover Account on the device. If there are multiple Clover accounts, only the
   * first is returned. Requires your Android Manifest include
   * <code>android.permission.GET_ACCOUNTS</code>. The Clover Account is needed by most Clover
   * service connectors.
   *
   * @param context the context from your Android application or component
   * @return the Clover account on the device, should never return null
   */
  public static Account getAccount(Context context) {
    Account[] accounts = getAccounts(context);
    if (accounts == null || accounts.length == 0) {
      return null;
    }
    return accounts[0];
  }

  /**
   * @deprecated Use {@link #getAccount(Context)} instead.
   *
   * Very early versions of Clover software supported multiple Clover Accounts on a single device,
   * but this is no longer the case, there is now always a single Clover Account on each device.
   */
  @Deprecated
  public static Account[] getAccounts(Context context) {
    try {
      AccountManager mgr = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
      Account[] accounts = mgr.getAccountsByType(CLOVER_ACCOUNT_TYPE);

      return accounts;
    } catch (RuntimeException e) {
      Log.e(TAG, "failed to get accounts", e);
    }

    return null;
  }

}
