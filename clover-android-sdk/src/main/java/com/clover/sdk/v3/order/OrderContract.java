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
package com.clover.sdk.v3.order;

import android.accounts.Account;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * The contract between the orders provider and applications. Contains
 * definitions for the supported URIs and columns.
 */
public final class OrderContract {
  /**
   * The authority for the orders provider
   */
  public static final String AUTHORITY = "com.clover.orders";

  /**
   * A content:// style uri to the authority for the contacts provider
   */
  public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

  public static final String ACTION_UPDATE_STATUS = "com.clover.intent.action.ORDER_ACTION";

  public static final String ACCOUNT_NAME_PARAM = "account_name";
  public static final String ACCOUNT_TYPE_PARAM = "account_type";

  public interface SummaryColumns {
    /**
     * Order Id.
     * <p/>
     * Type: TEXT
     */
    public static final String ID = "id";

    /**
     * Timestamp when order was created, in a {@link System#currentTimeMillis()} time base.
     * <p/>
     * Type: INTEGER
     */
    public static final String CREATED = "created_time";

    /**
     * Timestamp when order was last modified, in a {@link System#currentTimeMillis()} time base.
     * <p/>
     * Type: INTEGER
     */
    public static final String LAST_MODIFIED = "last_modified";

    /**
     * Order total. Values are stored in cents ($1.99 is stored as 199).
     * <p/>
     * Type: INTEGER
     */
    public static final String TOTAL = "total";

    /**
     * Order amount paid. Values are stored in cents ($1.99 is stored as 199).
     * <p/>
     * Type: INTEGER
     */
    public static final String AMOUNT_PAID = "amount_paid";

    /**
     * Order amount refunded. Values are stored in cents ($1.99 is stored as 199).
     * <p/>
     * Type: INTEGER
     */
    public static final String AMOUNT_REFUNDED = "amount_refunded";

    /**
     * Order amount credited. Values are stored in cents ($1.99 is stored as 199).
     * <p/>
     * Type: INTEGER
     */
    public static final String AMOUNT_CREDITED = "amount_credited";

    /**
     * Order currency.
     * <p/>
     * Type: TEXT
     */
    public static final String CURRENCY = "currency";

    /**
     * Order customer id.
     * <p/>
     * Type: TEXT
     */
    public static final String CUSTOMER_ID = "customer_id";

    /**
     * Order customer name.
     * <p/>
     * Type: TEXT
     */
    public static final String CUSTOMER_NAME = "customer_name";

    /**
     * Order employee name.
     * <p/>
     * Type: TEXT
     */
    public static final String EMPLOYEE_NAME = "employee_name";

    /**
     * Order title (order number, order name etc).
     * <p/>
     * Type: TEXT
     */
    public static final String TITLE = "title";

    /**
     * Order note.
     * <p/>
     * Type: TEXT
     */
    public static final String NOTE = "note";

    /**
     * Order deleted flag.
     * <p/>
     * Type: INTEGER
     */
    public static final String DELETED = "deleted";

    /**
     * Order state.
     * <p/>
     * Type: TEXT
     */
    public static final String STATE = "state";

    /**
     * Order payment state.
     * <p/>
     * Type: TEXT
     */
    public static final String PAYMENT_STATE = "payment_state";

    /**
     * Order payment types.
     * <p/>
     * Type: TEXT
     */
    public static final String TENDERS = "tenders";

    /**
     * Order type (UUID).
     * <p/>
     * Type: TEXT
     */
    public static final String ORDER_TYPE = "order_type";
  }

  public static final class Summaries implements BaseColumns, SummaryColumns {
    /**
     * This utility class cannot be instantiated
     */
    private Summaries() {
    }

    /**
     * base content directory for summaries
     */
    public static final String CONTENT_DIRECTORY = "summaries";

    /**
     * The content:// style URI for this table
     */
    public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_DIRECTORY);

    /**
     * /**
     * The MIME type of {@link #CONTENT_URI} providing a directory of order
     * summaries.
     */
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/summary";

    /**
     * The MIME type of a {@link #CONTENT_URI} subdirectory of a single
     * order summary.
     */
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/summary";

    /**
     * Creates an order summary content {@link Uri} with the {@link android.accounts.Account} added as query parameters
     *
     * @param account Clover {@link android.accounts.Account} to associate with order summary content {@link Uri}
     * @return a new {@link Uri}
     */
    public static Uri contentUriWithAccount(Account account) {
      Uri.Builder builder = CONTENT_URI.buildUpon();
      builder.appendQueryParameter(ACCOUNT_NAME_PARAM, account.name);
      builder.appendQueryParameter(ACCOUNT_TYPE_PARAM, account.type);
      return builder.build();
    }
  }
}
