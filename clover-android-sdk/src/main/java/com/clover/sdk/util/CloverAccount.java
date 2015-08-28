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

  private CloverAccount() {
  }

  /**
   * Get the Clover account on the device. If there are multiple Clover accounts, only the
   * first is returned.
   *
   * @param context the context
   * @return the first Clover account on the device.
   */
  public static Account getAccount(Context context) {
    Account[] accounts = getAccounts(context);
    if (accounts == null || accounts.length == 0) {
      return null;
    }
    return accounts[0];
  }

  /**
   * Get all Clover accounts on the device.
   *
   * @param context the context
   */
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
