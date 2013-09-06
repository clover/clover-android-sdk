/*
 * Copyright (C) 2013 Clover Network, Inc.
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

package com.clover.sdk.v1;

public class Intents {
  public static final String ACTION_PAY = "clover.intent.action.PAY";
  public static final String ACTION_REFUND = "clover.intent.action.REFUND";

  public static final String EXTRA_ORDER_ID = "clover.intent.extra.ORDER_ID";
  public static final String EXTRA_AMOUNT = "clover.intent.extra.AMOUNT";
  public static final String EXTRA_CURRENCY = "clover.intent.extra.CURRENCY";
  public static final String EXTRA_MERCHANT_ID = "clover.intent.extra.MERCHANT_ID";
  public static final String EXTRA_PAYMENT_ID = "clover.intent.extra.PAYMENT_ID";
  public static final String EXTRA_NOTE = "clover.intent.extra.NOTE";

  public static final String EXTRA_CUSTOMER_ID = "clover.intent.extra.CUSTOMER_ID";
  public static final String EXTRA_EMPLOYEE_ID = "clover.intent.extra.EMPLOYEE_ID";


  public static final String EXTRA_CLIENT_ID = "clover.intent.extra.CLIENT_ID";
  public static final String EXTRA_TIP_AMOUNT = "clover.intent.extra.TIP_AMOUNT";

  /**
   * An {@link android.accounts.Account}, the Clover account associated with
   * the action or broadcast.
   */
  public static final String EXTRA_ACCOUNT = "clover.intent.extra.ACCOUNT";

  /**
   * An <code>int</code>, the version of the service.
   */
  public static final String EXTRA_VERSION = "clover.intent.extra.VERSION";
}
