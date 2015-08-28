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

public final class ReceiptRegistrationContract {

  public static final String PARAM_ACCOUNT_NAME = "account_name";
  public static final String PARAM_ACCOUNT_TYPE = "account_type";

  public static final String AUTHORITY = "com.clover.receipt_registration";
  public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

  public interface RegistrationColumns {
    public static final String PACKAGE = "package";
    public static final String URI = "uri";
  }

  public static final class Registration implements BaseColumns, RegistrationColumns {
    public static final String CONTENT_DIRECTORY = "registration";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_DIRECTORY);
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/registration";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/registration";

    /**
     * URI builder for registration.
     */
    public static class UriBuilder {
      private Account account = null;

      public UriBuilder account(Account account) {
        this.account = account;
        return this;
      }

      public Uri build() {
        Uri.Builder builder = Registration.CONTENT_URI.buildUpon();
        if (account != null) {
          builder.appendQueryParameter(PARAM_ACCOUNT_NAME, account.name);
          builder.appendQueryParameter(PARAM_ACCOUNT_TYPE, account.type);
        }
        return builder.build();
      }
    }
  }
}
