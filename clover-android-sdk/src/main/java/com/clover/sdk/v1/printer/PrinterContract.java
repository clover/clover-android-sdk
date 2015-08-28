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
package com.clover.sdk.v1.printer;

import android.accounts.Account;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Contract defining printers for use in Clover applications.
 * <p/>
 * The printers contract is composed of two tables and one view. The <code>Devices</code> table
 * lists all known printer devices. The <code>Categories</code> table maps a printer device
 * to a printing category, either receipt or order.
 * <p/>
 * Conceptually, the
 * {@link com.clover.sdk.v1.printer.PrinterContract.Devices}
 * class represents the printers that are configured
 * on any device belong to the merchant. The
 * {@link com.clover.sdk.v1.printer.PrinterContract.Categories}
 * class maps a printer
 * device to a printer category, and represents the printer devices that are configured
 * on this device.
 * <p/>
 * The
 * {@link com.clover.sdk.v1.printer.PrinterContract.DeviceCategories}
 * class is a join of the
 * {@link com.clover.sdk.v1.printer.PrinterContract.Devices}
 * and
 * {@link com.clover.sdk.v1.printer.PrinterContract.Categories}
 * class where the category is not null. In other words,
 * {@link com.clover.sdk.v1.printer.PrinterContract.DeviceCategories}
 * is the configured devices and their category.
 */
public final class PrinterContract {
  public static final String PARAM_ACCOUNT_NAME = "account_name";
  public static final String PARAM_ACCOUNT_TYPE = "account_type";
  public static final String PARAM_SYNC = "sync";

  public static final String AUTHORITY = "com.clover.printers";
  public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

  public interface DevicesColumns {
    /**
     * The unique identifier for the printer.
     */
    public static final String UUID = "uuid";
    /**
     * The type of the printer, as defined by the enum <code>Type</code>.
     */
    public static final String TYPE = "type";
    /**
     * The name of the printer.
     */
    public static final String NAME = "name";
    /**
     * The MAC address of the printer, or null if this is a USB printer.
     */
    public static final String MAC = "mac";
    /**
     * The IP address of the printer, or null if this is a USB printer.
     */
    public static final String IP = "ip";
  }

  public static final class Devices implements BaseColumns, DevicesColumns {
    public static final String CONTENT_DIRECTORY = "devices";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_DIRECTORY);
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/devices";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/devices";

    /**
     * URI builder for printer devices.
     */
    public static class UriBuilder {
      private Boolean sync = null;
      private Account account = null;

      /**
       * Synchronize changes with server?
       */
      public UriBuilder sync(boolean sync) {
        this.sync = sync;
        return this;
      }

      public UriBuilder account(Account account) {
        this.account = account;
        return this;
      }

      public Uri build() {
        Uri.Builder builder = Devices.CONTENT_URI.buildUpon();
        if (sync != null) {
          builder.appendQueryParameter(PARAM_SYNC, sync.toString());
        }
        if (account != null) {
          builder.appendQueryParameter(PARAM_ACCOUNT_NAME, account.name);
          builder.appendQueryParameter(PARAM_ACCOUNT_TYPE, account.type);
        }
        return builder.build();
      }
    }
  }

  public interface CategoriesColumns {
    /**
     * The printer device UUID.
     */
    public static final String DEVICE_UUID = "device_uuid";
    /**
     * The category assigned to the printer device, as defined by the enum <code>Category</code>.
     */
    public static final String CATEGORY = "category";
  }

  public static final class Categories implements BaseColumns, CategoriesColumns {
    public static final String CONTENT_DIRECTORY = "categories";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_DIRECTORY);
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/categories";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/categories";

    /**
     * URI builder for printer categories.
     */
    public static class UriBuilder {
      private Account account = null;

      public UriBuilder account(Account account) {
        this.account = account;
        return this;
      }

      public Uri build() {
        Uri.Builder builder = Categories.CONTENT_URI.buildUpon();
        if (account != null) {
          builder.appendQueryParameter(PARAM_ACCOUNT_NAME, account.name);
          builder.appendQueryParameter(PARAM_ACCOUNT_TYPE, account.type);
        }
        return builder.build();
      }
    }
  }

  public interface DeviceCategoriesColumns extends BaseColumns, DevicesColumns, CategoriesColumns {
  }

  public static final class DeviceCategories implements BaseColumns, DeviceCategoriesColumns {
    public static final String CONTENT_DIRECTORY = "category_devices";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_DIRECTORY);
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/category_devices";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/category_devices";

    /**
     * URI builder for printer device categories.
     */
    public static class UriBuilder {
      private Account account = null;

      public UriBuilder account(Account account) {
        this.account = account;
        return this;
      }

      public Uri build() {
        Uri.Builder builder = DeviceCategories.CONTENT_URI.buildUpon();
        if (account != null) {
          builder.appendQueryParameter(PARAM_ACCOUNT_NAME, account.name);
          builder.appendQueryParameter(PARAM_ACCOUNT_TYPE, account.type);
        }
        return builder.build();
      }
    }
  }

  public static boolean isSync(Uri uri) {
    String s = uri.getQueryParameter(PrinterContract.PARAM_SYNC);
    if (s == null) {
      return true;
    }
    return Boolean.parseBoolean(s);
  }
}
