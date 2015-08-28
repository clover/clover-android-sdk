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
package com.clover.sdk.v3.cash;

import android.accounts.Account;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * The contract between the cash management provider and applications. Contains
 * definitions for the supported URIs and columns.
 */
public final class CashContract {
  /** The authority for the modifier provider */
  public static final String AUTHORITY = "com.clover.cash";

  /** A content:// style uri to the authority for the cash management provider */
  public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

  public static final String AUTH_TOKEN_PARAM = "token";
  public static final String ACCOUNT_NAME_PARAM = "account_name";
  public static final String ACCOUNT_TYPE_PARAM = "account_type";

  public interface CashEventColumns {
    /**
     * Transaction type, 'TRANSACTION', 'ADJUSTMENT', 'COUNT'.
     * <p>
     * Type: TEXT
     */

    public static final String TYPE = "type";

    /**
     * Time stamp for the request
     * <p>
     * Type: TIMESTAMP
     */
    public static final String TIMESTAMP = "timestamp";

    /**
     * The amount this transaction changed the running total
     * <p>
     * Type: BIGINT
     */
    public static final String AMOUNT_CHANGE = "amount_change";

    /**
     * Running total of the cash draw
     * <p>
     * Type: BIGINT
     */
    @Deprecated
    public static final String TOTAL = "total";

    /**
     * Note Associated with the transaction
     * Type: TEXT
     */
    public static final String NOTE = "note";


    /**
     * Employee id
     * <p>
     * Type: VCHAR
     */
    public static final String EMPLOYEEID = "employee_id";
  }

  public static final class CashEvent implements BaseColumns, CashEventColumns {
    /**
     * This utility class cannot be instantiated
     */
    private CashEvent() {
    }

    /**
     * base content directory for cash events
     */
    public static final String CONTENT_DIRECTORY = "cashevent";

    /**
     * The content:// style URI for this table
     */
    public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_DIRECTORY);

    /**
     * The MIME type of {@link #CONTENT_URI} providing a directory of cash events
     public static final String TYPE = "type";.
     */
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/cashevent";

    /**
     * The MIME type of a {@link #CONTENT_URI} subdirectory of a single cash events.
     */
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/cashevent";

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

