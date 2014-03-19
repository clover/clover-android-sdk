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

  /**
   * Broadcast by Payment screen to request a RemoteViews object from apps to be displayed before an order is complete.
   * It is sent with EXTRA_VIEWID which is a unique id to be used with the ACTION_UPDATE_PAYMENT_REMOTE_VIEWS intent,
   * EXTRA_ORDER_ID for the order being processed, and EXTRA_REMOTE_VIEW_SIZE for the size desired.
   */
  public static final String ACTION_REQUEST_PAYMENT_REMOTE_VIEWS = "clover.intent.action.ACTION_REQUEST_PAYMENT_REMOTE_VIEWS";

  /**
   * Payment screen listens for this broadcast as a response to ACTION_REQUEST_PAYMENT_REMOTE_VIEWS to receive views to be
   * displayed on the tender screen. It must be sent with EXTRA_REMOTE_VIEWS and the EXTRA_VIEWID.
   */
  public static final String ACTION_UPDATE_PAYMENT_REMOTE_VIEWS = "clover.intent.action.ACTION_UPDATE_PAYMENT_REMOTE_VIEWS";

  /** A RemoteViews objects sent to Register app */
  public static final String EXTRA_REMOTE_VIEWS = "clover.intent.extra.REMOTE_VIEWS";

  /** See ACTION_REQUEST_PAYMENT_REMOTE_VIEWS and ACTION_UPDATE_PAYMENT_REMOTE_VIEWS */
  public static final String EXTRA_VIEWID = "clover.intent.extra.VIEW_ID";

  public enum RemoteViewSize {
    /** 500dp x 160dp  */
    MEDIUM,
    /** 688dp x 160dp */
    LARGE,
  }

  /** A String version of one of the values from the enum RemoteViewSize. */
  public static final String EXTRA_REMOTE_VIEW_SIZE = "clover.intent.extra.REMOTE_VIEW_SIZE";

}
