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
package com.clover.sdk.v1.app;

import android.accounts.Account;
import android.content.Intent;
import com.clover.sdk.v1.Intents;

/**
 * Definition of actions and extras for communicating with the app service.
 *
 * @see com.clover.sdk.v1.app.IAppService
 */
public class AppIntent {

  /**
   * Service action: bind to the app service.
   */
  public static final String ACTION_APP_SERVICE = "com.clover.sdk.app.intent.action.APP_SERVICE";

  /**
   * Extract the {@link android.accounts.Account}
   * from the given intent's extras {@link android.os.Bundle}.
   *
   * @param intent An intent.
   * @return An {@link android.accounts.Account}, or <code>null</code> if the account extra is
   * not present.
   */
  public static Account getAccount(Intent intent) {
    return intent.getParcelableExtra(Intents.EXTRA_ACCOUNT);
  }

  /**
   * Extract the version
   * from the given intent's extras {@link android.os.Bundle}.
   *
   * @param intent An intent.
   * @return An {@code int}, or {@code 1} if the version extra is
   * not present.
   */
  public static int getVersion(Intent intent) {
    return intent.getIntExtra(Intents.EXTRA_VERSION, 1);
  }
}
