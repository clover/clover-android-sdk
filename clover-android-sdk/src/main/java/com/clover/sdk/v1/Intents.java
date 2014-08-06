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
  public static final String ACTION_CLOVER_PAY = "com.clover.intent.action.PAY";
  public static final String ACTION_REFUND = "clover.intent.action.REFUND";
  public static final String ACTION_STORE_CREDIT = "clover.intent.action.STORE_CREDIT";
  public static final String ACTION_MODIFY_ORDER = "clover.intent.action.MODIFY_ORDER";

  // Use this intent action to start App Market's App Detail screen
  public static final String ACTION_START_APP_DETAIL = "clover.intent.action.START_APP_DETAIL";

  public static final String EXTRA_ORDER_ID = "clover.intent.extra.ORDER_ID";
  public static final String EXTRA_CLOVER_ORDER_ID = "com.clover.intent.extra.ORDER_ID";
  public static final String EXTRA_AMOUNT = "clover.intent.extra.AMOUNT";
  public static final String EXTRA_CURRENCY = "clover.intent.extra.CURRENCY";
  public static final String EXTRA_MERCHANT_ID = "clover.intent.extra.MERCHANT_ID";
  public static final String EXTRA_PAYMENT_ID = "clover.intent.extra.PAYMENT_ID";
  public static final String EXTRA_NOTE = "clover.intent.extra.NOTE";
  public static final String EXTRA_LINE_ITEM_IDS = "clover.intent.extra.LINE_ITEM_IDS";

  public static final String EXTRA_CUSTOMER_ID = "clover.intent.extra.CUSTOMER_ID";
  public static final String EXTRA_EMPLOYEE_ID = "clover.intent.extra.EMPLOYEE_ID";


  public static final String EXTRA_CLIENT_ID = "clover.intent.extra.CLIENT_ID";
  public static final String EXTRA_TIP_AMOUNT = "clover.intent.extra.TIP_AMOUNT";

  // Extra for parcelable App for the START_APP_DETAIL action
  public static final String EXTRA_APP = "clover.intent.extra.APP";

  // Extra for App package name for the START_APP_DETAIL action
  public static final String EXTRA_APP_PACKAGE_NAME = "clover.intent.extra.APP_PACKAGE_NAME";

  // Extra for App UUID for the START_APP_DETAIL action
  public static final String EXTRA_APP_ID = "clover.intent.extra.APP_ID";

  // Extra for target subscription (during an upgrade flow) for the START_APP_DETAIL action
  public static final String EXTRA_TARGET_SUBSCRIPTION_ID = "clover.intent.extra.TARGET_SUBSCRIPTION_ID";

  public static final String EXTRA_OBEY_AUTO_LOGOUT = "com.clover.intent.extra.OBEY_AUTO_LOGOUT";

  // Extra for result subscription (during an upgrade flow) for the START_APP_DETAIL action
  public static final String EXTRA_RESULT_SUBSCRIPTION_ID = "clover.intent.extra.RESULT_SUBSCRIPTION_ID";

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

  public static final String EXTRA_REASON = "clover.intent.extra.REASON";

  public static final String EXTRA_DIALOG = "clover.intent.extra.DIALOG";

  public static final String EXTRA_PERMISSIONS = "clover.intent.extra.PERMISSIONS";

  public static final String EXTRA_PACKAGE = "clover.intent.extra.PACKAGE";

  /**
   * start Activity to authenticate an employee (i.e. enter pin number)
   * <p>
   * Input:
   * <li>{@link #EXTRA_ACCOUNT} if not set then use current account
   * <li>{@link #EXTRA_EMPLOYEE_ID} String. if set then authenticate this particular employee, if not set then authenticate any employee and return id in result
   * <li>{@link #EXTRA_REASON} String custom title (optional)
   * <li>{@link #EXTRA_DIALOG} Boolean extra value, false by default. if True then use Dialog theme.
   *
   * <li>{@link #EXTRA_PERMISSIONS} String value, if set then you must authenticate an employee with this particular permission
   * <li>{@link #EXTRA_PACKAGE} Optional String packageName. Works in conjunction with {@link #EXTRA_PERMISSIONS}
   * <p>
   * Output:
   * <li>{@link #EXTRA_EMPLOYEE_ID} Integer representing the authenticated employee id
   * <li>Nothing if {@link #EXTRA_EMPLOYEE_ID} is set in input params
   *
   */
  public static final String ACTION_AUTHENTICATE_EMPLOYEE = "clover.intent.action.AUTHENTICATE_EMPLOYEE";

  public static final String EXTRA_SHOW_CANCEL_BUTTON = "clover.intent.extra.SHOW_CANCEL_BUTTON";


  /**
   * Intents for barcode scanning preview.
   */
  public static final String ACTION_SCAN = "clover.intent.action.BARCODE_SCAN";
  public static final String EXTRA_START_SCAN = "clover.intent.extra.SCAN_START";
  public static final String EXTRA_SHOW_PREVIEW = "clover.intent.extra.SHOW_PREVIEW";
  public static final String EXTRA_SHOW_MERCHANT_PREVIEW = "clover.intent.extra.SHOW_MERCHANT_PREVIEW";
  public static final String EXTRA_SHOW_CUSTOMER_PREVIEW = "clover.intent.extra.SHOW_CUSTOMER_PREVIEW";
  public static final String EXTRA_LED_ON = "clover.intent.extra.LED_ON";
  /**
   * Default value is true.
   * Set it to false if you don't want to scan QR_CODE
   */
  public static final String EXTRA_SCAN_QR_CODE = "clover.intent.extra.SCAN_QR_CODE";
  /**
   * Default value is true.
   * Set it to false if you don't want to scan ONE_D codes
   */
  public static final String EXTRA_SCAN_1D_CODE = "clover.intent.extra.SCAN_1D_CODE";
  public static final String EXTRA_SHOW_CLOSE_BUTTON = "clover.intent.extra.SHOW_CLOSE_BUTTON";
  public static final String EXTRA_SHOW_LED_BUTTON = "clover.intent.extra.SHOW_LED_BUTTON";
  public static final String EXTRA_SCAN_X = "clover.intent.extra.SCAN_X";
  public static final String EXTRA_SCAN_Y = "clover.intent.extra.SCAN_Y";

  public static final String EXTRA_ALLOW_FIRE = "clover.intent.extra.ALLOW_FIRE";
}
