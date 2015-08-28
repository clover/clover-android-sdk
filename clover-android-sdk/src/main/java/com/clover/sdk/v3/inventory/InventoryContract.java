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
package com.clover.sdk.v3.inventory;

import android.accounts.Account;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * The contract between the inventory provider and applications. Contains definitions for the supported URIs and columns.
 * <p/>
 * The Content Provider for inventory may be used directly by applications wishing to use a {@link android.database.Cursor}
 * to navigate a large inventory database.  Basic item and category information is accessible through the Content Provider, and
 * more detailed information about items, categories and related information may then be retrieved using the
 * {@link com.clover.sdk.v3.inventory.IInventoryService} via binding to the AIDL service, or through the wrapper class
 * {@link com.clover.sdk.v3.inventory.InventoryConnector}.
 * <p/>
 * The inventory database is typically kept up to date using push notifications from the Clover server to the client,
 * so in most cases any changes made through the web or through another device will be immediately reflected
 * on all devices belonging to a particular merchant.  When network connections are unreliable, the inventory service will attempt
 * to synchronize its local database (i.e. the backing store for this content provider) with the server on a regular basis,
 * usually on an interval no greater than 3 hours.
 */
public final class InventoryContract {
  /**
   * The authority for the inventory provider
   */
  public static final String AUTHORITY = "com.clover.inventory";

  /**
   * A content:// style uri to the authority for the modifiers provider
   */
  public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

  public static final String AUTH_TOKEN_PARAM = "token";
  public static final String ACCOUNT_NAME_PARAM = "account_name";
  public static final String ACCOUNT_TYPE_PARAM = "account_type";

  public interface ItemColumns {
    /**
     * Unique identifier for an item
     * <p/>
     * Type: TEXT
     */
    public static final String ID = "uuid";

    /**
     * Name of the item
     * <p/>
     * Type: TEXT
     */
    public static final String NAME = "name";

    /**
     * Alternate name of the item
     * <p/>
     * Type: TEXT
     */
    public static final String ALTERNATE_NAME = "alternate_name";

    /**
     * Item price, typically in cents.
     * <p/>
     * Type: INTEGER
     */
    public static final String PRICE = "price";

    /**
     * Item product code, e.g. UPC, EAN, etc.
     * <p/>
     * Type: TEXT
     */
    public static final String CODE = "code";

    /**
     * Item price type; use the {@link com.clover.sdk.v3.inventory.PriceType} enum to determine the correct type
     * <p/>
     * Type: TEXT
     */
    public static final String PRICE_TYPE = "price_type";

    /**
     * Item unit name, e.g. "oz", "lb", etc.
     * <p/>
     * Type: TEXT
     */
    public static final String UNIT_NAME = "unit_name";

    /**
     * Modified time
     *
     * Type: LONG
     */
    public static final String MODIFIED_TIME = "modified_time";

    /**
     * Flag to indicate whether or not to use default tax rates; a call to
     * {@link com.clover.sdk.v3.inventory.IInventoryService#getItem(String, com.clover.sdk.v1.ResultStatus)}
     * will always return the appropriate tax rates needed to calculate total item cost to the customer.
     * <p/>
     * Type: INTEGER
     */
    public static final String DEFAULT_TAX_RATES = "default_tax_rates";
  }

  public static final class Item implements BaseColumns, ItemColumns {
    /**
     * This utility class cannot be instantiated
     */
    private Item() {
    }

    /**
     * base content directory for modifier
     */
    public static final String CONTENT_DIRECTORY = "item";

    /**
     * The content:// style URI for this table
     */
    public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_DIRECTORY);

    /**
     * The MIME type of {@link #CONTENT_URI} providing a directory of modifiers.
     */
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/item";

    /**
     * The MIME type of a {@link #CONTENT_URI} subdirectory of a single modifier.
     */
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/item";

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
