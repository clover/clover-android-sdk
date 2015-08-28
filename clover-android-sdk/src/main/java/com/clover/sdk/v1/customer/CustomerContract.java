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
package com.clover.sdk.v1.customer;

import android.accounts.Account;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * The contract between the customers provider and applications. Contains
 * definitions for the supported URIs and columns.
 */
public final class CustomerContract {
  /**
   * The authority for the modifier provider
   */
  public static final String AUTHORITY = "com.clover.customers";

  /**
   * A content:// style uri to the authority for the customer provider
   */
  public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

  public static final String AUTH_TOKEN_PARAM = "token";
  public static final String ACCOUNT_NAME_PARAM = "account_name";
  public static final String ACCOUNT_TYPE_PARAM = "account_type";

  public interface SummaryColumns {
    /**
     * UUID for this customer
     * <p/>
     * Type: TEXT
     */

    public static final String ID = "id";

    /**
     * <p/>
     * Type: TEXT
     */
    public static final String LAST_NAME = "last_name";

    /**
     * <p/>
     * Type: TEXT
     */
    public static final String FIRST_NAME = "first_name";
  }

  public static final class Summary implements BaseColumns, SummaryColumns {
    /**
     * This utility class cannot be instantiated
     */
    private Summary() {
    }

    /**
     * base content directory for a customer
     */
    public static final String CONTENT_DIRECTORY = "summary";

    /**
     * The content:// style URI for this table
     */
    public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_DIRECTORY);

    /**
     * The MIME type of {@link #CONTENT_URI} providing a directory of customers
     * public static final String TYPE = "type";.
     */
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/summary";

    /**
     * The MIME type of a {@link #CONTENT_URI} subdirectory of a single customer
     */
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/summary";

    public static Uri contentUriWithToken(String token) {
      return CONTENT_URI.buildUpon().appendQueryParameter(AUTH_TOKEN_PARAM, token).build();
    }

    public static Uri contentUriWithAccount(Account account) {
      Uri.Builder builder = CONTENT_URI.buildUpon();
      builder.appendQueryParameter(ACCOUNT_NAME_PARAM, account.name);
      builder.appendQueryParameter(ACCOUNT_TYPE_PARAM, account.type);
      return builder.build();
    }
  }

  public interface DataColumns {
    /**
     * UUID for this customer
     * <p/>
     * Type: TEXT
     */

    public static final String ID = "id";

    /**
     * <p/>
     * Type: TEXT
     */
    public static final String KEY = "key";

    /**
     * <p/>
     * Type: TEXT
     */
    public static final String VALUE = "value";
  }

  public static final class Data implements BaseColumns, DataColumns {
    /**
     * This utility class cannot be instantiated
     */
    private Data() {
    }

    /**
     * base content directory for a customer
     */
    public static final String CONTENT_DIRECTORY = "data";

    /**
     * The content:// style URI for this table
     */
    public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_DIRECTORY);

    /**
     * The MIME type of {@link #CONTENT_URI} providing a directory of key value data for a customer
     * public static final String TYPE = "type";.
     */
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/data";

    /**
     * The MIME type of a {@link #CONTENT_URI} subdirectory of a single key value
     */
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/data";

    public static Uri contentUriWithToken(String token) {
      return CONTENT_URI.buildUpon().appendQueryParameter(AUTH_TOKEN_PARAM, token).build();
    }

    public static Uri contentUriWithAccount(Account account) {
      Uri.Builder builder = CONTENT_URI.buildUpon();
      builder.appendQueryParameter(ACCOUNT_NAME_PARAM, account.name);
      builder.appendQueryParameter(ACCOUNT_TYPE_PARAM, account.type);
      return builder.build();
    }
  }
}
