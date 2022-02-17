/*
 * Copyright (C) 2016 Clover Network, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clover.sdk.util;

import com.clover.sdk.internal.util.UnstableContentResolverClient;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

/**
 * This class provides access to the Clover {@link android.accounts.Account} object on the device.
 * The Clover account is genrally needed when interacting with ServiceConnectors and Contracts.
 *
 * @see android.accounts.Account
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
   * Get the Clover Account on the device. The Clover Account is needed by most Clover service
   * connectors.
   *
   * @param context the context from your Android application or component
   * @return the Clover account on the device, should never return null
   */
  @SuppressWarnings("deprecation")
  public static Account getAccount(Context context) {
    Account[] accounts = getAccounts(context);
    if (accounts == null || accounts.length == 0) {
      return null;
    }
    return accounts[0];
  }

  private static final Uri MERCHANTS_URI = Uri.parse("content://" + "com.clover.merchants");

  private static final Uri MERCHANTS_ACCOUNTS_CONTENT_URI = Uri.withAppendedPath(MERCHANTS_URI, "accounts");

  /**
   * @deprecated Use {@link #getAccount(Context)} instead.
   *
   * Very early versions of Clover software supported multiple Clover Accounts on a single device,
   * but this is no longer the case, there is now always a single Clover Account on each device.
   */
  @Deprecated
  public static Account[] getAccounts(Context context) {
    try {
      UnstableContentResolverClient client = new UnstableContentResolverClient(context.getContentResolver(),
          MERCHANTS_ACCOUNTS_CONTENT_URI);

      try (Cursor c = client.query(null, null, null, null)) {
        if (c == null || c.getCount() == 0) {
          throw new RemoteException("No Clover accounts found");
        }

        Account[] accounts = new Account[c.getCount()];
        int accountNameColIndex = c.getColumnIndex("account_name");
        int i = 0;
        while (c.moveToNext()) {
          String accountName = c.getString(accountNameColIndex);
          accounts[i++] = new Account(accountName, CLOVER_ACCOUNT_TYPE);
        }
        return accounts;
      }
    } catch (Exception e) {
      Log.e(TAG, "failed to get clover accounts", e);
    }

    return null;
  }

}
