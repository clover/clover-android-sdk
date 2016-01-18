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
package com.clover.sdk.v1;

public class Intents {

  /**
   * Launch Pay app with active order (Register payments)
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_CLOVER_ORDER_ID} - The UUID of the order being paid for (REQUIRED)</li>
   * <li>{@link #EXTRA_OBEY_AUTO_LOGOUT} - If true and merchant uses auto-logout, device logs out after payment, default is false</li>
   * <li>{@link #EXTRA_ASK_FOR_TIP} - If true, customer will be prompted for tip after payment, default is true</li>
   * <li>{@link #EXTRA_ALLOW_FIRE} - If true and merchant fires orders after payment, order is fired after payment, default is true </li>
   * </ul>
   * <p>
   * Result data for this activity will include the same extras passed to it
   * <p>
   * Result codes:
   * <ul>
   *     <li>{@link android.app.Activity#RESULT_OK} - payment completed successfully</li>
   *     <li>{@link android.app.Activity#RESULT_CANCELED} - payment not completed successfully</li>
   * </ul>
   */
  public static final String ACTION_CLOVER_PAY = "com.clover.intent.action.PAY";

  /**
   * Received by your app's broadcast receiver to launch your modify order activity,  (See <a href="https://github.com/clover/android-examples/tree/master/modifyorderbutton">Example Usage</a>)
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_ORDER_ID} - the UUID of the order active in Register</li>
   * </ul>
   * <p>
   * Result data must include:
   * <ul>
   * None (Modifications can be made to the Order object directly)
   * </ul>
   */
  public static final String ACTION_MODIFY_ORDER = "clover.intent.action.MODIFY_ORDER";

  /**
   * Received by your app's broadcast receiver to launch your modify amount activity (See <a href="https://github.com/clover/android-examples/tree/master/modifyamountbutton">Example Usage</a>)
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_AMOUNT} - the initial amount</li>
   * <li>{@link #EXTRA_TRANSACTION_TYPE} - the transaction type</li>
   * </ul>
   * <p>
   * Result data must include:
   * <ul>
   * <li>{@link #EXTRA_AMOUNT} - the amount the amount resulting from your activity</li>
   * </ul>
   */
  public static final String ACTION_MODIFY_AMOUNT = "clover.intent.action.MODIFY_AMOUNT";

  /**
   * Launch Sale activity
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_AMOUNT} - Amount displayed on activity keypad (can be modified during activity)</li>
   * </ul>
   * <p>
   * Result data includes:
   * <ul>
   * <li>{@link #EXTRA_PAYMENT} - The resulting payment</li>
   * </ul>
   * <p>
   * Result codes:
   * <ul>
   *     <li>{@link android.app.Activity#RESULT_OK} - payment completed successfully</li>
   *     <li>{@link android.app.Activity#RESULT_CANCELED} - payment not completed successfully</li>
   * </ul>
   */
  public static final String ACTION_MANUAL_PAY = "clover.intent.action.MANUAL_PAY";

  /**
   * Launch Refund activity
   * <p>
   * Extras passed:
   * <ul>
   * <li>NONE</li>
   * </ul>
   * <p>
   * Result data includes:
   * <ul>
   * <li>{@link #EXTRA_CREDIT}</li>
   * </ul>
   */
  public static final String ACTION_MANUAL_REFUND = "clover.intent.action.MANUAL_REFUND";

  /**
   * Launch App Market's App Detail activity
   * <p>
   * Extras passed (Must include either {@link #EXTRA_APP_PACKAGE_NAME} or {@link #EXTRA_APP_ID}):
   * <ul>
   * <li>{@link #EXTRA_APP_PACKAGE_NAME} - the package name of the app that will be shown</li>
   * <li>{@link #EXTRA_APP_ID} - the UUID of the app that will be shown</li>
   * <li>{@link #EXTRA_TARGET_SUBSCRIPTION_ID} - the UUID of the subscription tier that will be selected by default</li>
   * </ul>
   * <p>
   * If user changes to a new subscription tier, result data includes:
   * <ul>
   * <li>{@link #EXTRA_RESULT_SUBSCRIPTION_ID} - the UUID of the subscription tier installed by user</li>
   * </ul>
   *
   */
  public static final String ACTION_START_APP_DETAIL = "clover.intent.action.START_APP_DETAIL";

  /**
   * Launch Order Manage activity
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_ORDER_ID} - the UUID of the order to be shown</li>
   * </ul>
   * <p>
   * Result data includes:
   * <ul>
   * <li>NONE</li>
   * </ul>
   *
   */
  public static final String ACTION_START_ORDER_MANAGE = "clover.intent.action.START_ORDER_MANAGE";

  /**
   * Launch activity to add tip to requested payment
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_PAYMENT_ID} - UUID of payment associated with tip</li>
   * <li>{@link #EXTRA_TITLE} - label on the tip activity's back button</li>
   * </ul>
   * <p>
   * Result data includes:
   * <ul>
   * <li>NONE</li>
   * </ul>
   */
  public static final String ACTION_ADD_TIP = "clover.intent.action.ADD_TIP";

  /**
   * Launch activity have a customer add a tip to a payment
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_ORDER_ID} - Order UUID, the order associated with the payment and tip (REQUIRED)</li>
   * <li>{@link #EXTRA_AMOUNT} - Long, the amount of the payment the tip will be added to (REQUIRED)</li>
   * </ul>
   * <p>
   * Result data includes:
   * <ul>
   * <li>{@link #EXTRA_TIP_AMOUNT} Long, the amount of the added tip </li>
   * </ul>
   * <p>
   * Result codes:
   * <ul>
   *     <li>{@link android.app.Activity#RESULT_OK} - tip added successfully</li>
   *     <li>{@link android.app.Activity#RESULT_CANCELED} - tip not added successfully</li>
   * </ul>
   */
  public static final String ACTION_CUSTOMER_ADD_TIP = "clover.intent.action.CUSTOMER_ADD_TIP";

  /**
   * Launch activity to view batch details for open batch
   * <p>
   * Extras passed:
   * <ul>
   * <li>NONE</li>
   * </ul>
   * <p>
   * Result data includes:
   * <ul>
   * <li>NONE</li>
   * </ul>
   */
  public static final String ACTION_CLOSEOUT = "clover.intent.action.CLOSEOUT";

  /**
   * Launch Transactions detail activity
   * <p>
   * Extras passed (Must include one of the following: {@link #EXTRA_PAYMENT} or {@link #EXTRA_CREDIT} or {@link #EXTRA_REFUND}):
   * <ul>
   * <li>{@link #EXTRA_PAYMENT} - the Payment object that will be shown</li>
   * <li>{@link #EXTRA_CREDIT} - the Credit (manual refund) object that will be shown</li>
   * <li>{@link #EXTRA_REFUND} - the Refund object that will be shown</li>
   * </ul>
   * <p>
   * Result data includes:
   * <ul>
   * <li>NONE</li>
   * </ul>
   */
  public static final String ACTION_START_TRANSACTION_DETAIL = "clover.intent.action.START_TRANSACTION_DETAIL";

  /**
   * Broadcast sent by Payment screen to request a RemoteViews object from apps to be displayed before an order is complete, Received by your app's broadcast receiver
   *<p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_VIEWID} - unique id to be used with {@link #ACTION_UPDATE_PAYMENT_REMOTE_VIEWS}</li>
   * <li>{@link #EXTRA_ORDER_ID} - order being processed</li>
   * <li>{@link #EXTRA_REMOTE_VIEW_SIZE} - for the size desired</li>
   * </ul>
   * <p>
   * Result data must include:
   * <ul>
   * <li>NONE (Remote views should be passed back via {@link #ACTION_UPDATE_PAYMENT_REMOTE_VIEWS}</li>
   * </ul>
   * @see #ACTION_REQUEST_PAYMENT_REMOTE_VIEWS
   *
   */
  public static final String ACTION_REQUEST_PAYMENT_REMOTE_VIEWS = "clover.intent.action.ACTION_REQUEST_PAYMENT_REMOTE_VIEWS";

  /**
   * Payment screen listens for this broadcast as a response to {@link #ACTION_REQUEST_PAYMENT_REMOTE_VIEWS} to receive views to be
   * displayed on the tender screen
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_REMOTE_VIEWS}</li>
   * <li>{@link #EXTRA_VIEWID} unique id passed by {@link #ACTION_REQUEST_PAYMENT_REMOTE_VIEWS}</li>
   * </ul>
   * <p>
   * Result data includes:
   * <ul>
   * <li>NONE</li>
   * </ul>
   *
   */
  public static final String ACTION_UPDATE_PAYMENT_REMOTE_VIEWS = "clover.intent.action.ACTION_UPDATE_PAYMENT_REMOTE_VIEWS";

  /**
   * Launch activity to authenticate an employee (Enter PIN)
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_ACCOUNT} - if set then get employee by this account, otherwise use currently logged in account</li>
   * <li>{@link #EXTRA_EMPLOYEE_ID} - if set then authenticate requires this particular employee, if not set then authenticate any employee and return id in result</li>
   * <li>{@link #EXTRA_REASON} - written explanation for authentication, visible on pin entry layout</li>
   * <li>{@link #EXTRA_SHOW_CANCEL_BUTTON} - If true, show cancel button on pin entry layout, default false</li>
   * <li>{@link #EXTRA_PERMISSIONS} - Permission, if set then you must authenticate an employee granted this particular permission</li>
   * <li>{@link #EXTRA_PACKAGE} - package name associated with permission identified by {@link #EXTRA_PERMISSIONS}</li>
   * </ul>
   * <p>
   * Result data includes:
   * <ul>
   * <li>{@link #EXTRA_EMPLOYEE_ID} Integer representing the authenticated employee id (not returned if {@link #EXTRA_EMPLOYEE_ID} was passed </li>
   * <li>Nothing if {@link #EXTRA_EMPLOYEE_ID} is set in input params</li>
   * </ul>
   * <p>
   * Result codes:
   * <ul>
   *     <li>{@link android.app.Activity#RESULT_OK} - authenticated successfully</li>
   *     <li>{@link android.app.Activity#RESULT_CANCELED} - did not authenticate successfully (Cancel button)</li>
   * </ul>
   */
  public static final String ACTION_AUTHENTICATE_EMPLOYEE = "clover.intent.action.AUTHENTICATE_EMPLOYEE";

  /**
   * Launch activity to authenticate for requested role (Enter PIN)
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_ROLE} - role required to authenticate employee PIN (Required)</li>
   * <li>{@link #EXTRA_TITLE} - custom title shown on PIN entry layout</li>
   *</ul>
   * <p>
   * <p>
   * Result codes:
   * <ul>
   *     <li>{@link android.app.Activity#RESULT_OK} - employee with required role, successfully authenticated</li>
   *     <li>{@link android.app.Activity#RESULT_CANCELED} - did not authenticate successfully (Cancel button)</li>
   * </ul>
   */
  public static final String ACTION_REQUEST_ROLE = "com.clover.intent.action.REQUEST_ROLE";

  /**
   * Launch the secure payment activity (Requires that your app has "clover.permission.ACTION_PAY" in it's AndroidManifest.xml file)
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_AMOUNT} - amount to be paid (Required)</li>
   * <li>{@link #EXTRA_ORDER_ID} - order associated with payment, if excluded, a new order record will be created</li>
   * <li>{@link #EXTRA_CARD_ENTRY_METHODS} - allowed payment types, default all allowed</li>
   * <li>{@link #EXTRA_EMPLOYEE_ID} - employee conducting transaction</li>
   * <li>{@link #EXTRA_TIP_AMOUNT} - tip amount</li>
   * <li>{@link #EXTRA_TAX_AMOUNT} - tax amount</li>
   * <li>{@link #EXTRA_TAXABLE_AMOUNTS} - tax rates, with eligible amounts</li>
   * <li>{@link #EXTRA_SERVICE_CHARGE_AMOUNT} - service charge amount</li>
   * <li>{@link #EXTRA_DISABLE_CASHBACK} - disable option for cashback during payment, default false</li>
   * <li>{@link #EXTRA_IS_TESTING} - whether payment is testing mode</li>
   * <li>{@link #EXTRA_VOICE_AUTH_CODE} - voice auth code</li>
   * <li>{@link #EXTRA_AVS_POSTAL_CODE} - postal code associated with payment</li>
   * <li>{@link #EXTRA_CARD_NOT_PRESENT} - whether payment card is not present, default false</li>
   * <li>{@link #EXTRA_REMOTE_PRINT} - if printing will be delegated to remote device</li>
   * <li>{@link #EXTRA_TRANSACTION_NO} - transaction number for payment</li>
   * <li>{@link #EXTRA_FORCE_SWIPE_PIN_ENTRY} - if only payment option will be swipe debit, default false</li>
   * <li>{@link #EXTRA_DISABLE_RESTART_TRANSACTION_WHEN_FAILED} - if activity will end after failed transaction, default false</li>
   * <li>{@link #EXTRA_EXTERNAL_PAYMENT_ID} - external payment id, used for integration with other POS platforms</li>
   * </ul>
   * <p>
   * Result data includes:
   * <ul>
   * <li>{@link #EXTRA_PAYMENT} - created payment</li>
   * <li>{@link #EXTRA_PAYMENT_ID} - created payment's UUID</li>
   * </ul>
   * <p>
   * Result codes:
   * <ul>
   *     <li>{@link android.app.Activity#RESULT_OK} - payment successful</li>
   *     <li>{@link android.app.Activity#RESULT_CANCELED} - payment not successful (Cancel button)</li>
   * </ul>
   */
  public static final String ACTION_SECURE_PAY = "clover.intent.action.START_SECURE_PAYMENT";

  /**
   * Launch activity to securely capture card data on Mobile or Mini (Requires that your app has "clover.permission.ACTION_PAY" in it's AndroidManifest.xml file)
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_TRANSACTION_TYPE} - must be set to {@link #TRANSACTION_TYPE_CARD_DATA} (Required)</li>
   * <li>{@link #EXTRA_CARD_DATA_MESSAGE} - written explanation shown on card entry screen</li>
   * <li>{@link #EXTRA_CARD_ENTRY_METHODS} - allowed payment types, default all allowed</li>
   * </ul>
   * <p>
   * Result data includes:
   * <ul>
   * <li>{@link #EXTRA_CARD_DATA} - captured card data</li>
   * </ul>
   * <p>
   * Result codes:
   * <ul>
   *     <li>{@link android.app.Activity#RESULT_OK} - card data captured successfully</li>
   *     <li>{@link android.app.Activity#RESULT_CANCELED} - card data not captured successfully (Cancel button)</li>
   * </ul>
   */
  public static final String ACTION_SECURE_CARD_DATA = "clover.intent.action.START_SECURE_CARD_DATA";

  /**
   * Intent passed to start your app's customer-facing extensible tender activity (See <a href="https://github.com/clover/android-examples/tree/master/extensibletenderexample">Example Usage</a>)
   *
   *<p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_AMOUNT} - the transaction amount</li>
   * <li>{@link #EXTRA_CURRENCY} - the transaction currency</li>
   * <li>{@link #EXTRA_TAX_AMOUNT} - the transaction's tax amount</li>
   * <li>{@link #EXTRA_SERVICE_CHARGE_AMOUNT} - the transactions' service charge amount</li>
   * <li>{@link #EXTRA_TIP_AMOUNT} - the transactions' tip amount</li>
   * <li>{@link #EXTRA_ORDER_ID} - the Clover order ID</li>
   * <li>{@link #EXTRA_EMPLOYEE_ID} - the ID of the employee who initiated the payment</li>
   * <li>{@link #EXTRA_TENDER} - the tender for the transaction</li>
   * </ul>
   *</p>
   * <p>
   * Result data must include:
   * <ul>
   * <li>{@link #EXTRA_AMOUNT} - the approved transaction amount (Required)</li>
   * <li>{@link #EXTRA_CLIENT_ID} - the client ID / external payment ID</li>
   * <li>{@link #EXTRA_NOTE} - the payment note</li>
   * </ul>
   * <p>
   * Result codes:
   * <ul>
   *     <li>{@link android.app.Activity#RESULT_OK} - payment successful</li>
   *     <li>{@link android.app.Activity#RESULT_CANCELED} - payment not successful</li>
   * </ul>
   *
   * @see #ACTION_MERCHANT_TENDER
   * @see #META_CUSTOMER_TENDER_IMAGE
   */
  public static final String ACTION_CUSTOMER_TENDER = "clover.intent.action.CUSTOMER_TENDER";

  /**
   * Intent passed to start your app's merchant-facing extensible tender activity (See <a href="https://github.com/clover/android-examples/tree/master/extensibletenderexample">Example Usage</a>)
   *
   *<p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_AMOUNT} - the transaction amount</li>
   * <li>{@link #EXTRA_CURRENCY} - the transaction currency</li>
   * <li>{@link #EXTRA_TAX_AMOUNT} - the transaction's tax amount</li>
   * <li>{@link #EXTRA_SERVICE_CHARGE_AMOUNT} - the transactions' service charge amount</li>
   * <li>{@link #EXTRA_ORDER_ID} - the Clover order ID</li>
   * <li>{@link #EXTRA_EMPLOYEE_ID} - the ID of the employee who initiated the payment</li>
   * <li>{@link #EXTRA_TENDER} - the tender for the transaction</li>
   * <li>{@link #EXTRA_ORDER} - the order for the transaction</li>
   * <li>{@link #EXTRA_NOTE} - the order note for the transaction</li>
   * </ul>
   *</p>
   * <p>
   * Result data must include:
   * <ul>
   * <li>a (Required) {@link #EXTRA_AMOUNT} - the approved transaction amount </li>
   * <li>a (Optional) {@link #EXTRA_CLIENT_ID} - the client ID / external payment ID</li>
   * <li>a (Optional) {@link #EXTRA_NOTE} - the payment note</li>
   * </ul>
   * <p>
   * Result codes:
   * <ul>
   *     <li>{@link android.app.Activity#RESULT_OK} - payment successful</li>
   *     <li>{@link android.app.Activity#RESULT_CANCELED} - payment not successful</li>
   * </ul>
   *
   * @see #ACTION_CUSTOMER_TENDER
   * @see #META_CUSTOMER_TENDER_IMAGE
   */
  public static final String ACTION_MERCHANT_TENDER = "clover.intent.action.MERCHANT_TENDER";
  
  /**
   * Broadcast to start barcode scanner service on Station or Mini (Mobile scanner must be activated via physical trigger)
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_START_SCAN} - true will start scanner, false will close scanner (Required)</li>
   * <li>{@link #EXTRA_SHOW_PREVIEW} - whether scanner preview video will be shown, default is true</li>
   * <li>{@link #EXTRA_SHOW_MERCHANT_PREVIEW} - whether scanner preview will be shown in merchant facing mode, default is true</li>
   * <li>{@link #EXTRA_SHOW_CUSTOMER_PREVIEW} - whether scanner preview will be shown in customer facing mode, default is true</li>
   * <li>{@link #EXTRA_LED_ON} - whether LED will be on (Station only), default is false</li>
   * <li>{@link #EXTRA_SCAN_QR_CODE} - whether QR codes will be scanned, default is true</li>
   * <li>{@link #EXTRA_SCAN_1D_CODE} - whether 1D codes will be scanned, default is true</li>
   * <li>{@link #EXTRA_SHOW_CLOSE_BUTTON} - whether scanner preview will have a 'close' button, default is true</li>
   * <li>{@link #EXTRA_SHOW_LED_BUTTON} - whether scanner preview will have an LED toggle, default is true</li>
   * </ul>
   */
  public static final String ACTION_SCAN = "clover.intent.action.BARCODE_SCAN";

  /** @deprecated Replaced by {@link #ACTION_MERCHANT_TENDER} and {@link #ACTION_CUSTOMER_TENDER} */
  public static final String ACTION_PAY = "clover.intent.action.PAY";

  /** @deprecated  */
  public static final String ACTION_BILL_SPLIT = "com.clover.intent.action.BILL_SPLIT";

  /** @deprecated  */
  public static final String ACTION_GIFT_CARD_TX = "clover.intent.action.GIFT_CARD_TX";

  /** @deprecated  */
  public static final String ACTION_REFUND = "clover.intent.action.REFUND";

  /** @deprecated  */
  public static final String ACTION_STORE_CREDIT = "clover.intent.action.STORE_CREDIT";

  /** {@link com.clover.sdk.v3.payments.Transaction}, a Transaction object */
  public static final String EXTRA_TRANSACTION = "clover.intent.extra.TRANSACTION";

  /** {@link String}, the UUID of an Order object */
  public static final String EXTRA_ORDER_ID = "clover.intent.extra.ORDER_ID";

  /** {@link com.clover.sdk.v3.order.Order}, an Order object */
  public static final String EXTRA_ORDER = "clover.intent.extra.ORDER";

  /** {@link String}, the UUID of an Order object */
  public static final String EXTRA_CLOVER_ORDER_ID = "com.clover.intent.extra.ORDER_ID";

  /** {@link Long}, a monetary amount */
  public static final String EXTRA_AMOUNT = "clover.intent.extra.AMOUNT";

  /** {@link Long}, the tippable amount associated with a payment */
  public static final String EXTRA_TIPPABLE_AMOUNT = "clover.intent.extra.TIPPABLE_AMOUNT";

  /** {@link java.util.Currency}, the currency of a transaction */
  public static final String EXTRA_CURRENCY = "clover.intent.extra.CURRENCY";

  /** {@link String}, the UUID of a merchant */
  public static final String EXTRA_MERCHANT_ID = "clover.intent.extra.MERCHANT_ID";

  /** {@link String}, the UUID of a Payment object */
  public static final String EXTRA_PAYMENT_ID = "clover.intent.extra.PAYMENT_ID";

  /** {@link java.util.List} of {@link String} objects - UUIDs of payments */
  public static final String EXTRA_PAYMENT_IDS = "clover.intent.extra.PAYMENT_IDS";

  /** {@link String}, note content */
  public static final String EXTRA_NOTE = "clover.intent.extra.NOTE";

  /** {@link java.util.ArrayList} of {@link String} objects - LineItem UUIDs */
  public static final String EXTRA_LINE_ITEM_IDS = "clover.intent.extra.LINE_ITEM_IDS";

  /** {@link java.util.ArrayList} of {@link com.clover.sdk.v3.payments.LineItemPayment} objects */
  public static final String EXTRA_LINE_ITEM_PAYMENTS = "clover.intent.extra.LINE_ITEM_PAYMENTS";

  /** {@link  java.util.ArrayList} of {@link String} objects - UUIDs of disabled tenders */
  public static final String EXTRA_DISABLED_TENDER_IDS = "clover.intent.extra.DISABLED_TENDER_IDS";

  /** {@link Boolean}, whether to prompt customers for tip */
  public static final String EXTRA_ASK_FOR_TIP = "clover.intent.extra.ASK_FOR_TIP";

  /** {@link String}, UUID of a customer */
  public static final String EXTRA_CUSTOMER_ID = "clover.intent.extra.CUSTOMER_ID";

  /** {@link String}, the UUID of an Employee object */
  public static final String EXTRA_EMPLOYEE_ID = "clover.intent.extra.EMPLOYEE_ID";

  /** {@link String}, the ID of a Client or external payment */
  public static final String EXTRA_CLIENT_ID = "clover.intent.extra.CLIENT_ID";

  /** {@link Long}, tip amount */
  public static final String EXTRA_TIP_AMOUNT = "clover.intent.extra.TIP_AMOUNT";

  /** @deprecated */
  public static final String EXTRA_APP = "clover.intent.extra.APP";

  /** {@link String}, App package name */
  public static final String EXTRA_APP_PACKAGE_NAME = "clover.intent.extra.APP_PACKAGE_NAME";

  /** {@link String}, App UUID */
  public static final String EXTRA_APP_ID = "clover.intent.extra.APP_ID";

  /** {@link String}, UUID for target subscription (during an upgrade flow)*/
  public static final String EXTRA_TARGET_SUBSCRIPTION_ID = "clover.intent.extra.TARGET_SUBSCRIPTION_ID";

  /** {@link Boolean}, whether transaction will respect merchant's auto logout settings */
  public static final String EXTRA_OBEY_AUTO_LOGOUT = "com.clover.intent.extra.OBEY_AUTO_LOGOUT";

  /** {@link String}, UUID for result subscription (during an upgrade flow)*/
  public static final String EXTRA_RESULT_SUBSCRIPTION_ID = "clover.intent.extra.RESULT_SUBSCRIPTION_ID";

  /** {@link android.accounts.Account}, the Clover account associated with the action or broadcast */
  public static final String EXTRA_ACCOUNT = "clover.intent.extra.ACCOUNT";

  /** {@link int}, the version of the service */
  public static final String EXTRA_VERSION = "clover.intent.extra.VERSION";

  /** {@link android.widget.RemoteViews}, RemoteViews sent to Register Payment activity */
  public static final String EXTRA_REMOTE_VIEWS = "clover.intent.extra.REMOTE_VIEWS";

  /** See {@link #ACTION_REQUEST_PAYMENT_REMOTE_VIEWS} and {@link #ACTION_UPDATE_PAYMENT_REMOTE_VIEWS} */
  public static final String EXTRA_VIEWID = "clover.intent.extra.VIEW_ID";

  /** Values passed with {@link #EXTRA_REMOTE_VIEW_SIZE} */
  public enum RemoteViewSize {
    /** 500dp x 160dp  */
    MEDIUM,
    /** 688dp x 160dp */
    LARGE,
  }

  /** {@link String}, version of one of the values from the enum {@link com.clover.sdk.v1.Intents.RemoteViewSize} */
  public static final String EXTRA_REMOTE_VIEW_SIZE = "clover.intent.extra.REMOTE_VIEW_SIZE";

  /** {@link String}, written explanation */
  public static final String EXTRA_REASON = "clover.intent.extra.REASON";

  /** @deprecated */
  public static final String EXTRA_DIALOG = "clover.intent.extra.DIALOG";

  /** {@link String}, the string constant of a permission */
  public static final String EXTRA_PERMISSIONS = "clover.intent.extra.PERMISSIONS";

  /** {@link String}, package name */
  public static final String EXTRA_PACKAGE = "clover.intent.extra.PACKAGE";

  /** {@link com.clover.sdk.v3.employees.AccountRole}, employee role (admin, manager or employee)*/
  public static final String EXTRA_ROLE = "com.clover.intent.extra.ROLE";

  /** {@link String}, On-screen text */
  public static final String EXTRA_TITLE = "com.clover.intent.extra.TITLE";

  /** {@link Boolean}, whether the launched activity should display a cancel button */
  public static final String EXTRA_SHOW_CANCEL_BUTTON = "clover.intent.extra.SHOW_CANCEL_BUTTON";

  /** {@link Boolean}, whether to start or stop barcode scanner */
  public static final String EXTRA_START_SCAN = "clover.intent.extra.SCAN_START";

  /** {@link Boolean}, whether to show scanner preview video (merchant and customer facing modes) */
  public static final String EXTRA_SHOW_PREVIEW = "clover.intent.extra.SHOW_PREVIEW";

  /** {@link Boolean}, whether to show scanner preview video (merchant facing mode only) */
  public static final String EXTRA_SHOW_MERCHANT_PREVIEW = "clover.intent.extra.SHOW_MERCHANT_PREVIEW";

  /** {@link Boolean}, whether to show scanner preview video (customer facing mode only) */
  public static final String EXTRA_SHOW_CUSTOMER_PREVIEW = "clover.intent.extra.SHOW_CUSTOMER_PREVIEW";

  /** {@link Boolean}, whether LED will be on */
  public static final String EXTRA_LED_ON = "clover.intent.extra.LED_ON";

  /** {@link Boolean}, whether QR codes will be scanned */
  public static final String EXTRA_SCAN_QR_CODE = "clover.intent.extra.SCAN_QR_CODE";

  /** {@link Boolean}, whether 1D codes will be scanned */
  public static final String EXTRA_SCAN_1D_CODE = "clover.intent.extra.SCAN_1D_CODE";

  /** {@link Boolean}, whether scanner preview video will have a 'close' button */
  public static final String EXTRA_SHOW_CLOSE_BUTTON = "clover.intent.extra.SHOW_CLOSE_BUTTON";

  /** {@link Boolean}, whether scanner preview video will have a LED light toggle */
  public static final String EXTRA_SHOW_LED_BUTTON = "clover.intent.extra.SHOW_LED_BUTTON";

  /** @deprecated  */
  public static final String EXTRA_SCAN_X = "clover.intent.extra.SCAN_X";

  /** @deprecated  */
  public static final String EXTRA_SCAN_Y = "clover.intent.extra.SCAN_Y";

  /** {@link Boolean}, whether order should respect merchant's setting for firing after payment */
  public static final String EXTRA_ALLOW_FIRE = "clover.intent.extra.ALLOW_FIRE";

  /** {@link com.clover.sdk.v3.employees.Shift}, the current employee shift */
  public static final String EXTRA_CURRENT_SHIFT = "clover.intent.extra.CURRENT_SHIFT";

  /** {@link Long}, a tax amount */
  public static final String EXTRA_TAX_AMOUNT = "clover.intent.extra.TAX_AMOUNT";

  /** {@link com.clover.sdk.v3.payments.ServiceChargeAmount} */
  public static final String EXTRA_SERVICE_CHARGE_AMOUNT = "clover.intent.extra.SERVICE_CHARGE_AMOUNT";

  /** {@link java.util.ArrayList} of {@link com.clover.sdk.v3.payments.TaxableAmountRate} objects - tax rates, and amounts to which they apply */
  public static final String EXTRA_TAXABLE_AMOUNTS = "clover.intent.extra.TAXABLE_AMOUNTS";

  /** {@link int}, representation of bit flags for Intents.CARD_ENTRY_METHOD_* values , when not included in the intent all methods will be allowed */
  public static final String EXTRA_CARD_ENTRY_METHODS = "clover.intent.extra.CARD_ENTRY_METHODS";

  /** {@link Boolean}, true will disable cashback option during secure payment */
  public static final String EXTRA_DISABLE_CASHBACK = "clover.intent.extra.DISABLE_CASHBACK";

  /** {@link String}, the voice auth code for payment by voice authorization */
  public static final String EXTRA_VOICE_AUTH_CODE = "clover.intent.extra.VOICE_AUTH_CODE";

  /** {@link Boolean}, true if this is just a test transaction */
  public static final String EXTRA_IS_TESTING = "clover.intent.extra.IS_TESTING";

  /** {@link String}, a TRANSACTION_TYPE value */
  public static final String EXTRA_TRANSACTION_TYPE = "clover.intent.extra.TRANSACTION_TYPE";
  /** A value for {@link #EXTRA_TRANSACTION_TYPE} */
  public static final String TRANSACTION_TYPE_PAYMENT = "payment";
  /** A value for {@link #EXTRA_TRANSACTION_TYPE} */
  public static final String TRANSACTION_TYPE_CREDIT = "credit";
  /** A value for {@link #EXTRA_TRANSACTION_TYPE} */
  public static final String TRANSACTION_TYPE_AUTH = "auth";
  /** A value for {@link #EXTRA_TRANSACTION_TYPE} */
  public static final String TRANSACTION_TYPE_CARD_DATA = "cardData";
  /** A value for {@link #EXTRA_TRANSACTION_TYPE} */
  public static final String TRANSACTION_TYPE_BALANCE_INQUIRY = "balanceInquiry";

  /** {@link Boolean}, card not present, used during manual card entry */
  public static final String EXTRA_CARD_NOT_PRESENT = "clover.intent.extra.CARD_NOT_PRESENT";

  /** {@link String}, street address for use with AVS */
  public static final String EXTRA_AVS_STREET_ADDRESS = "clover.intent.extra.AVS_STREET_ADDRESS";

  /** {@link String}, postal code for use with AVS */
  public static final String EXTRA_AVS_POSTAL_CODE = "clover.intent.extra.AVS_POSTAL_CODE";

  /** {@link com.clover.sdk.v3.pay.PaymentRequestCardDetails}, payment card data */
  public static final String EXTRA_CARD_DATA = "cardData";

  /** {@link String}, the message to prompt the user */
  public static final String EXTRA_CARD_DATA_MESSAGE = "cardDataMessage";

  /** {@link Long}, the pre-selected cash back amount */
  public static final String EXTRA_CASHBACK_AMOUNT = "cashbackAmount";

  /** {@link Boolean}, if true printing will be delegated to remote device */
  public static final String EXTRA_REMOTE_PRINT = "remotePrint";

  /** {@link String}, payment transaction number */
  public static final String EXTRA_TRANSACTION_NO = "transactionNo";

  /** {@link Boolean}, if only payment option will be swipe debit */
  public static final String EXTRA_FORCE_SWIPE_PIN_ENTRY = "forceSwipePinEntry";

  /** {@link Boolean}, if true, the secure payment activity will end after a failed transaction */
  public static final String EXTRA_DISABLE_RESTART_TRANSACTION_WHEN_FAILED = "disableRestartTransactionWhenFailed";

  /** {@link String}, external payment id, used for integration with other POS platforms*/
  public static final String EXTRA_EXTERNAL_PAYMENT_ID = "externalPaymentId";

  /** {@link String}, an error description */
  public static final String EXTRA_DECLINE_REASON = "clover.intent.extra.DECLINE_REASON";

  /** {@link com.clover.sdk.v3.base.Tender}, a Tender object */
  public static final String EXTRA_TENDER = "clover.intent.extra.TENDER";

  /** {@link String}, a card number */
  public static final String EXTRA_CARD_NUMBER = "clover.intent.extra.CARD_NUMBER";

  /** {@link Boolean} */
  public static final String EXTRA_IGNORE_PAYMENT = "clover.intent.extra.IGNORE_PAYMENT";

  /** {@link com.clover.sdk.v3.payments.GiftCardResponse} */
  public static final String EXTRA_GIFT_CARD_RESPONSE = "clover.intent.extra.GIFT_CARD_RESPONSE";

  /** {@link String}, message shown upon failure */
  public static final String EXTRA_FAILURE_MESSAGE ="clover.intent.extra.FAILURE_MESSAGE";

  /** A bit value for {@link #EXTRA_CARD_ENTRY_METHODS}, can be bitwise-ored with other CARD_ENTRY_METHOD values */
  public static final int CARD_ENTRY_METHOD_MAG_STRIPE = 0b00000001;
  /** A bit value for {@link #EXTRA_CARD_ENTRY_METHODS}, can be bitwise-ored with other CARD_ENTRY_METHOD values */
  public static final int CARD_ENTRY_METHOD_ICC_CONTACT = 0b00000010;
  /** A bit value for {@link #EXTRA_CARD_ENTRY_METHODS}, can be bitwise-ored with other CARD_ENTRY_METHOD values */
  public static final int CARD_ENTRY_METHOD_NFC_CONTACTLESS = 0b00000100;
  /** A bit value for {@link #EXTRA_CARD_ENTRY_METHODS}, can be bitwise-ored with other CARD_ENTRY_METHOD values */
  public static final int CARD_ENTRY_METHOD_MANUAL = 0b00001000;
  /** A bit value for {@link #EXTRA_CARD_ENTRY_METHODS}, all card entry methods. */
  public static final int CARD_ENTRY_METHOD_ALL = CARD_ENTRY_METHOD_MAG_STRIPE | CARD_ENTRY_METHOD_ICC_CONTACT | CARD_ENTRY_METHOD_NFC_CONTACTLESS | CARD_ENTRY_METHOD_MANUAL;

  /** A bit value used to indicate that a kiosk card entry method mode mask is supplied - implicitly set by other mask values **/
  public static final int KIOSK_MODE_CARD_ENTRY_MASK_SUPPLIED = 0b10000000_00000000;
  /** A bit value for {@link #EXTRA_CARD_ENTRY_METHODS}, can be bitwise-ored with other CARD_ENTRY_METHOD values to provide entry mode mask for device in kiosk mode */
  public static final int KIOSK_MODE_CARD_ENTRY_MASK_MAG_STRIPE = 0b00000001_00000000 | KIOSK_MODE_CARD_ENTRY_MASK_SUPPLIED;
  /** A bit value for {@link #EXTRA_CARD_ENTRY_METHODS}, can be bitwise-ored with other CARD_ENTRY_METHOD values to provide entry mode mask for device in kiosk mode */
  public static final int KIOSK_MODE_CARD_ENTRY_MASK_ICC_CONTACT = 0b00000010_00000000 | KIOSK_MODE_CARD_ENTRY_MASK_SUPPLIED;
  /** A bit value for {@link #EXTRA_CARD_ENTRY_METHODS}, can be bitwise-ored with other CARD_ENTRY_METHOD values to provide entry mode mask for device in kiosk mode */
  public static final int KIOSK_MODE_CARD_ENTRY_MASK_NFC_CONTACTLESS = 0b000000100_00000000 | KIOSK_MODE_CARD_ENTRY_MASK_SUPPLIED;
  /** A bit value for {@link #EXTRA_CARD_ENTRY_METHODS}, can be bitwise-ored with other CARD_ENTRY_METHOD values to provide entry mode mask for device in kiosk mode */
  public static final int KIOSK_MODE_CARD_ENTRY_MASK_MANUAL = 0b00001000_00000000 | KIOSK_MODE_CARD_ENTRY_MASK_SUPPLIED;
  /** A bit value for {@link #EXTRA_CARD_ENTRY_METHODS}, sets kiosk mode mask to ALL entry mode flags permitted */
  public static final int KIOSK_MODE_CARD_ENTRY_MASK_ALL = KIOSK_MODE_CARD_ENTRY_MASK_MAG_STRIPE | KIOSK_MODE_CARD_ENTRY_MASK_ICC_CONTACT | KIOSK_MODE_CARD_ENTRY_MASK_NFC_CONTACTLESS | KIOSK_MODE_CARD_ENTRY_MASK_MANUAL;

  /** {@link com.clover.sdk.v3.payments.Payment}, v3 Payment object */
  public static final String EXTRA_PAYMENT = "clover.intent.extra.PAYMENT";

  /** {@link java.util.ArrayList} of {@link com.clover.sdk.v3.payments.Payment} objects */
  public static final String EXTRA_PAYMENTS = "clover.intent.extra.PAYMENTS";

  /** {@link java.util.ArrayList} of {@link com.clover.sdk.v3.payments.Payment} objects */
  public static final String EXTRA_VOIDED_PAYMENTS = "clover.intent.extra.VOIDED_PAYMENTS";

  /** {@link com.clover.sdk.v3.payments.Credit}, v3 Credit object (Manual Refund) */
  public static final String EXTRA_CREDIT = "clover.intent.extra.CREDIT";

  /** {@link com.clover.sdk.v3.payments.Refund}, v3 Refund object */
  public static final String EXTRA_REFUND = "clover.intent.extra.REFUND";

  /** {@link com.clover.sdk.v3.payments.Authorization}, v3 Authorization object */
  public static final String EXTRA_AUTHORIZATION = "clover.intent.extra.AUTHORIZATION";

  /** {@link Boolean}, whether to show amount remaining after payment */
  public static final String EXTRA_SHOW_REMAINING = "clover.intent.extra.SHOW_REMAINING";

  /** {@link Boolean}, whether to show the Void button */
  public static final String EXTRA_SHOW_VOID_BUTTON = "clover.intent.extra.SHOW_VOID_BUTTON";

  /** {@link Boolean}, whether signature is already verified */
  public static final String EXTRA_SIGNATURE_VERIFIED = "clover.intent.extra.SIGNATURE_VERIFIED";

  /** {@link Boolean}, print receipt extras */
  public static final String EXTRA_PRINT_RECEIPT_ONLY = "clover.intent.extra_PRINT_RECEIPT_ONLY";

  /** {@link int}, representation of bit flags from {@link com.clover.sdk.v1.printer.job.PrintJob} */
  public static final String EXTRA_RECEIPT_FLAG = "clover.intent.extra.RECEIPT_FLAG";
  
  /**
   * Broadcast from Clover, indicating the active order in Register
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_ORDER_ID} - the UUID of the active order in Register, or null if there is no longer an active order </li>
   * </ul>
   */
  public static final String ACTION_ACTIVE_REGISTER_ORDER = "clover.intent.action.ACTIVE_REGISTER_ORDER";

  /**
   * Broadcast from Clover, indicating the active order in the Register Pay Activity
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_ORDER_ID} - the UUID of the active order in Pay, or null if there is no longer an active order </li>
   * </ul>
   */
  public static final String ACTION_V1_ACTIVE_PAY_ORDER = "clover.intent.action.V1_ACTIVE_PAY_ORDER";

  /**
   * Broadcast from Clover, indicating that we are starting to build an order
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_ORDER_ID} - the UUID of the order being built</li>
   * </ul>
   */
  public static final String ACTION_V1_ORDER_BUILD_START = "clover.intent.action.V1_ORDER_BUILD_START";

  /**
   * Broadcast from Clover, indicating that we have stopped building an order
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_ORDER_ID} - the UUID of the order that is no longer being built</li>
   * </ul>
   */
  public static final String ACTION_V1_ORDER_BUILD_STOP = "clover.intent.action.V1_ORDER_BUILD_STOP";

  /**
   * Broadcast from Clover, indicating that we are showing PayActivity
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_ORDER_ID} - the UUID of the order active in the PayActivity</li>
   * </ul>
   */
  public static final String ACTION_V1_PAY_BUILD_SHOW = "clover.intent.action.V1_PAY_BUILD_SHOW";

  /**
   * Broadcast from Clover, indicating that PayActivity was hidden
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_ORDER_ID} - the UUID of the order that is no longer active in the PayActivity</li>
   * </ul>
   */
  public static final String ACTION_V1_PAY_BUILD_HIDE = "clover.intent.action.V1_PAY_BUILD_HIDE";

  /**
   * Broadcast from Clover, indicating that we are starting to build a payment
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_ORDER_ID} - the UUID of the order associated with the payment now being built</li>
   * </ul>
   */
  public static final String ACTION_V1_PAY_BUILD_START = "clover.intent.action.V1_PAY_BUILD_START";

  /**
   * Broadcast from Clover, indicating that we have stopped building a payment
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_ORDER_ID} - the UUID of the order associated with the payment that is no longer being built</li>
   * </ul>
   */
  public static final String ACTION_V1_PAY_BUILD_STOP = "clover.intent.action.V1_PAY_BUILD_STOP";

  /**
   * Broadcast from Clover, indicating that we have started executing a payment
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_ORDER_ID} - the UUID of the order associated with the payment that is now being executed</li>
   * </ul>
   */
  public static final String ACTION_V1_PAY_EXECUTE_START = "clover.intent.action.V1_PAY_EXECUTE_START";

  /**
   * Broadcast from Clover, indicating that we have stopped executing a payment
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_ORDER_ID} - the UUID of the order associated with the payment that is no longer being executed</li>
   * </ul>
   */
  public static final String ACTION_V1_PAY_EXECUTE_STOP = "clover.intent.action.V1_PAY_EXECUTE_STOP";

  /** @deprecated */
  public static final String EXTRA_AVAILABLE = "clover.intent.extra_AVAILABLE";

  /** {@link Boolean} */
  public static final String EXTRA_SHOW_SEARCH = "clover.intent.extra_SHOW_SEARCH";

  /** {@link int}, A drawable resource ID, the image to be displayed on the customer-facing tender button */
  public static final String META_CUSTOMER_TENDER_IMAGE = "clover.intent.meta.CUSTOMER_TENDER_IMAGE";

  /** {@link int}, A drawable resource ID, the image to be displayed on the merchant-facing tender button*/
  public static final String META_MERCHANT_TENDER_IMAGE = "clover.intent.meta.MERCHANT_TENDER_IMAGE";
}
