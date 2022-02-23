/*
 * Copyright (C) 2021 Clover Network, Inc.
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
import android.os.Bundle;
import android.os.CancellationSignal;
import android.provider.BaseColumns;


/**
 * The contract between the orders provider and apps. For more detailed
 * information about orders please see {@link IOrderServiceV3_1}.
 * <p/>
 * The content provider for orders may be used directly by apps wishing to
 * use a {@link android.database.Cursor} to navigate a large orders
 * database. Specifically using the contract allows an app to discover all
 * the recent orders (via query). Read
 * <a href="https://developer.android.com/guide/topics/providers/content-provider-basics">Content provider basics</a>
 * for an overview of how to interact with a content provider. In this case
 * only methods which read data such as
 * {@link android.content.ContentResolver#query(Uri, String[], Bundle, CancellationSignal)}
 * will work. Methods which write data will be rejected.
 * <p/>
 * Using this contract requires the client app have
 * {@link com.clover.sdk.v3.employees.Permission#ORDERS_R} permission.
 * <p/>
 * To mutate orders use through the wrapper class
 * {@link com.clover.sdk.v3.order.OrderConnector}.
 * <p/>
 * The orders provider which fulfills this contract supplies only recent
 * orders (see {@link OrderSummary}). Older orders can be fetched via
 * the OrderConnector.
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

  public static final String AUTH_TOKEN_PARAM = "token";
  public static final String ACCOUNT_NAME_PARAM = "account_name";
  public static final String ACCOUNT_TYPE_PARAM = "account_type";

  public interface SummaryColumns {
    /**
     * Order Id.
     * <p/>
     * Type: TEXT
     */
    String ID = "id";

    /**
     * Timestamp when order was created, in a {@link System#currentTimeMillis()} time base.
     * <p/>
     * Type: INTEGER
     */
    String CREATED = "created_time";

    /**
     * Timestamp when order was last modified, in a {@link System#currentTimeMillis()} time base.
     * <p/>
     * Type: INTEGER
     */
    String LAST_MODIFIED = "last_modified";

    /**
     * Order total. Values are stored in cents ($1.99 is stored as 199).
     * <p/>
     * Type: INTEGER
     */
    String TOTAL = "total";

    /**
     * Order amount paid. Values are stored in cents ($1.99 is stored as 199).
     * <p/>
     * Type: INTEGER
     */
    String AMOUNT_PAID = "amount_paid";

    /**
     * Order amount refunded. Values are stored in cents ($1.99 is stored as 199).
     * <p/>
     * Type: INTEGER
     */
    String AMOUNT_REFUNDED = "amount_refunded";

    /**
     * Order amount credited. Values are stored in cents ($1.99 is stored as 199).
     * <p/>
     * Type: INTEGER
     */
    String AMOUNT_CREDITED = "amount_credited";

    /**
     * Order currency.
     * <p/>
     * Type: TEXT
     */
    String CURRENCY = "currency";

    /**
     * Order customer id.
     * <p/>
     * Type: TEXT
     */
    String CUSTOMER_ID = "customer_id";

    /**
     * Order customer name.
     * <p/>
     * Type: TEXT
     */
    String CUSTOMER_NAME = "customer_name";

    /**
     * Order employee name.
     * <p/>
     * Type: TEXT
     */
    String EMPLOYEE_NAME = "employee_name";

    /**
     * Order title (order number, order name etc).
     * <p/>
     * Type: TEXT
     */
    String TITLE = "title";

    /**
     * Order note.
     * <p/>
     * Type: TEXT
     */
    String NOTE = "note";

    /**
     * Order deleted flag.
     * <p/>
     * Type: INTEGER
     */
    String DELETED = "deleted";

    /**
     * Order state.
     * <p/>
     * Type: TEXT
     */
    String STATE = "state";

    /**
     * Order payment state.
     * <p/>
     * Type: TEXT
     */
    String PAYMENT_STATE = "payment_state";

    /**
     * Order payment types.
     * <p/>
     * Type: TEXT
     */
    String TENDERS = "tenders";

    /**
     * Order type (UUID).
     * <p/>
     * Type: TEXT
     */
    String ORDER_TYPE = "order_type";

    /**
     * Order payment types.
     * <p/>
     * Type: TEXT
     */
    String PAYMENT_IDS = "payment_ids";

    /**
     * Is this order an authorization
     * <p/>
     * Type: INTEGER
     */
    String IS_AUTH = "is_auth";

    /**
     * The UUID of the device that created the order
     */
    String DEVICE_ID = "device_id";
  }

  /**
   * @deprecated {@link OrderSummary} replaces this. This contract is much slower and contains only
   * data that the user of the device has actually interacted with in the last 7 days.
   */
  @Deprecated
  public static final class Summaries implements BaseColumns, SummaryColumns {
    /**
     * This utility class cannot be instantiated
     */
    private Summaries() {
    }

    /**
     * base content directory for summaries
     */
    @Deprecated
    public static final String CONTENT_DIRECTORY = "summaries";

    /**
     * The content:// style URI for this table
     */
    @Deprecated
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

    @Deprecated
    public static Uri contentUriWithToken(String token) {
      return CONTENT_URI.buildUpon().appendQueryParameter(AUTH_TOKEN_PARAM, token).build();
    }

    @Deprecated
    public static Uri contentUriWithAccount(Account account) {
      Uri.Builder builder = CONTENT_URI.buildUpon();
      builder.appendQueryParameter(ACCOUNT_NAME_PARAM, account.name);
      builder.appendQueryParameter(ACCOUNT_TYPE_PARAM, account.type);
      return builder.build();
    }
  }

  /**
   * The values in these columns correspond to the data found in an {@link Order}.
   */
  public interface OrderSummaryColumns {
    /**
     * Order uuid
     */
    String ID = "id";

    /**
     * Timestamp when order was created.
     * <p/>
     * Type: INTEGER
     */
    String CREATED_TIME = "created_time";

    /**
     * Timestamp when order was last modified, in a {@link System#currentTimeMillis()} time base.
     * <p/>
     * Type: INTEGER
     */
    String MODIFIED_TIME = "modified_time";

    /**
     * Timestamp when the row was inserted, used for data retention
     */
    String CREATED = "created";

    /**
     * Order total. Values are stored in cents ($1.99 is stored as 199).
     * <p/>
     * Type: INTEGER
     */
    String TOTAL = "total";

    /**
     * Order amount paid. Values are stored in cents ($1.99 is stored as 199).
     * <p/>
     * Type: INTEGER
     */
    String AMOUNT_PAID = "amount_paid";

    /**
     * Order amount refunded. Values are stored in cents ($1.99 is stored as 199).
     * <p/>
     * Type: INTEGER
     */
    String AMOUNT_REFUNDED = "amount_refunded";

    /**
     * Order amount credited. Values are stored in cents ($1.99 is stored as 199).
     * <p/>
     * Type: INTEGER
     */
    String AMOUNT_CREDITED = "amount_credited";

    /**
     * Order payment state.
     * <p/>
     * Type: TEXT
     */
    String PAYMENT_STATE = "payment_state";

    /**
     * Order state. See {@link Order#getState()}.
     * <p/>
     * Type: TEXT
     */
    String STATE = "state";

    /**
     * The UUID of the employee that created the order
     */
    String EMPLOYEE_ID = "employee_id";

    /**
     * Order customer id. Requires CUSTOMERS_R permission.
     * <p/>
     * Type: TEXT
     */
    String CUSTOMER_ID = "customer_id";

    /**
     * Order type (UUID).
     * <p/>
     * Type: TEXT
     */
    String ORDER_TYPE = "order_type";

    /**
     * The UUID of the device that created the order
     */
    String DEVICE_ID = "device_id";

    /**
     * Is this order an authorization
     * <p/>
     * Type: INTEGER
     */
    String IS_AUTH = "is_auth";

    /**
     * Order currency.
     * <p/>
     * Type: TEXT
     */
    String CURRENCY = "currency";

    /**
     * Order customer name. Requires CUSTOMERS_R permission.
     * <p/>
     * Type: TEXT
     */
    String CUSTOMER_NAME = "customer_name";

    /**
     * Order employee name.
     * <p/>
     * Type: TEXT
     */
    String EMPLOYEE_NAME = "employee_name";

    /**
     * Order title (order number, order name etc).
     * <p/>
     * Type: TEXT
     */
    String TITLE = "title";

    /**
     * Comma separated tender uuids for payments associated with this order
     * <p/>
     * Type: TEXT
     */
    String TENDER_IDS = "tender_ids";

    /**
     * Comma separated payment uuids for payments associated with this order
     * <p/>
     * Type: TEXT
     */
    String PAYMENT_IDS = "payment_ids";

    /**
     * Comma separated last 4 digits of card numbers for payments associated with this order
     * <p/>
     * Type: TEXT
     */
    String LAST_FOURS = "last_fours";

    /**
     * Comma separated tender labels for payments associated with this order
     * <p/>
     * Type: TEXT
     */
    String TENDER_LABELS = "tender_labels";

    /**
     * Order note.
     * <p/>
     * Type: TEXT
     */
    String NOTE = "note";

    /**
     * Order external reference number.
     * <p/>
     * Type: TEXT
     */
    String EXTERNAL_REFERENCE_ID = "external_reference_id";

    /**
     * Time stamp when order to be fulfilled
     * <p/>
     * Type: INTEGER
     */
    String FULFILLMENT_TIME = "fulfillment_time";

    /**
     * Service specific note by customer used for order fulfillment
     * <p/>
     * Type: Text
     */
    String CUSTOMER_NOTE = "customer_note";

    /**
     * For online order's online order customer name
     * <p/>
     * Type: TEXT
     */
    String OLO_CUSTOMER_NAME = "olo_customer_name";

    /**
     * For online order's service type
     * <p/>
     * Type: TEXT
     */
    String OLO_SERVICE_TYPE = "olo_service_type";

  }

  /**
   * Provides order summaries for up to 100 days of recent orders. To retrieve full order data use
   * the {@link OrderConnector}.
   */
  public static final class OrderSummary implements BaseColumns, OrderSummaryColumns {
    private OrderSummary() {
    }

    /**
     * base content directory for orders
     */
    public static final String CONTENT_DIRECTORY = "summary";

    /**
     * The content:// style URI for this table
     */
    public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_DIRECTORY);

    /**
     * The MIME type of {@link #CONTENT_URI} providing a directory of orders.
     */
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/summary";

    /**
     * The MIME type of a {@link #CONTENT_URI} subdirectory of a single order.
     */
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/summary";

    public static Uri contentUriWithToken(String token) {
      return CONTENT_URI.buildUpon().appendQueryParameter(AUTH_TOKEN_PARAM, token).build();
    }

    /**
     * Return order summaries
     */
    public static Uri contentUriWithAccount(Account account) {
      Uri.Builder builder = CONTENT_URI.buildUpon();
      builder.appendQueryParameter(ACCOUNT_NAME_PARAM, account.name);
      builder.appendQueryParameter(ACCOUNT_TYPE_PARAM, account.type);
      return builder.build();
    }

    public static final String CONTENT_DIRECTORY_2 = "summary2";

    public static final Uri CONTENT_URI_2 = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_DIRECTORY_2);

    /**
     * Same as {@link #contentUriWithAccount(Account)}, but queries will not automatically add a
     * state IS NOT NULL condition to the WHERE clause. See {@link Order#getState()} for details
     * about the meaning of order state.
     */
    public static Uri contentUriWithAccount2(Account account) {
      Uri.Builder builder = CONTENT_URI_2.buildUpon();
      builder.appendQueryParameter(ACCOUNT_NAME_PARAM, account.name);
      builder.appendQueryParameter(ACCOUNT_TYPE_PARAM, account.type);
      return builder.build();
    }
  }
}
