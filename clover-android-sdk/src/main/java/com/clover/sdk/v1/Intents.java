/*
 * Copyright (C) 2016 Clover Network, Inc.
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

import com.clover.sdk.v3.scanner.BarcodeScanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * This class contains most of the Clover-specific intents available to developer apps. These
 * intents allow apps to listen for events and start Clover activities. See Android documentation
 * for information about how to use Intents:
 * <a href="https://developer.android.com/guide/components/intents-filters.html" target="_blank>
 *   Intents and Intent Filters</a>
 */
public class Intents {

  /**
   * Launch Register Point of Sale activity
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_ORDER_ID} - The UUID of the order opened in Register</li>
   * <li>{@link #EXTRA_CUSTOMER_V3} - a customer object to be associated with the order</li>
   * </ul>
   * <p>
   * Result data includes:
   * <ul>
   * <li>NONE</li>
   * </ul>
   */
  public static final String ACTION_START_REGISTER = "com.clover.intent.action.START_REGISTER";

  /**
   * Launch Register Select Item activity, allowing an employee to add or remove items from an order
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_ORDER_ID} - The UUID of the order to be modified (Required)</li>
   * </ul>
   * <p>
   * Result data includes:
   * <ul>
   * <li>NONE</li>
   * </ul>
   */
  public static final String ACTION_ITEM_SELECT = "com.clover.intent.action.ITEM_SELECT";

  /**
   * Launch the Print Receipts activity, to show receipt printing and sending options for an order
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_ORDER_ID} - The UUID of the target order (Required)</li>
   * </ul>
   * <p>
   * Result data includes:
   * <ul>
   * <li>NONE</li>
   * </ul>
   */
  public static final String ACTION_START_PRINT_RECEIPTS = "com.clover.intent.action.START_PRINT_RECEIPTS";

  /**
   * Launch the Customer Profile activity
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_CUSTOMER_ID} - The UUID of the target customer (Required)</li>
   * </ul>
   * <p>
   * Result data includes:
   * <ul>
   * <li>NONE</li>
   * </ul>
   */
  public static final String ACTION_START_CUSTOMER_PROFILE = "com.clover.intent.action.START_CUSTOMER_PROFILE";

  /**
   * Launch Pay activity with the provided order. This may be used to take a payment or perform a
   * refund. When performing a refund the line items must have negative amounts.
   * <p>
   * See the SaleRefundTestActivity in the clover-android-sdk-example project for a full example.
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_CLOVER_ORDER_ID} - The UUID of the order being paid for (REQUIRED)</li>
   * <li>{@link #EXTRA_TRANSACTION_TYPE} - may be {@link #TRANSACTION_TYPE_CREDIT} for refund, the default is {@link #TRANSACTION_TYPE_PAYMENT}</li>
   * <li>{@link #EXTRA_OBEY_AUTO_LOGOUT} - If true and merchant uses auto-logout, device logs out after payment, default is false</li>
   * <li>{@link #EXTRA_ASK_FOR_TIP} - If true, customer will be prompted for tip after payment, default is true</li>
   * <li>{@link #EXTRA_ALLOW_FIRE} - If true and merchant fires orders after payment, order is fired after payment, default is true</li>
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
   * Received by your app's broadcast receiver to launch your modify order activity,  (See
   * <a href="https://github.com/clover/android-examples/tree/master/modifyorderbutton">Example Usage</a>)
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
   * Received by your app's broadcast receiver to launch your modify amount activity (See
   * <a href="https://github.com/clover/android-examples/tree/master/modifyamountbutton">Example Usage</a>)
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
   * @deprecated Please use {@link #ACTION_CLOVER_PAY} instead
   */
  @Deprecated
  public static final String ACTION_MANUAL_PAY = "clover.intent.action.MANUAL_PAY";

  /**
   * @deprecated Please use {@link #ACTION_CLOVER_PAY} instead
   */
  @Deprecated
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
   * <li>{@link #EXTRA_ORDER_ID} - the UUID of the order to be shown (REQUIRED)</li>
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
   * Launch activity to have a customer add a tip to a payment. A third party developer firing this intent should invoke
   * {@link com.clover.sdk.util.CustomerMode#disable(Context, boolean) CustomerMode#disable} in
   * {@link android.app.Activity#onActivityResult(int, int, Intent) onActivityResult} to restore the
   * bottom navigation bar and top status bar.
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_ORDER_ID} - Order UUID, the order associated with the payment and tip (REQUIRED)</li>
   * <li>{@link #EXTRA_AMOUNT} - Long, the amount of the payment the tip will be added to (REQUIRED)</li>
   * <li>{@link #EXTRA_TIP_CONFIRM_MODE} - Boolean, whether to confirm the tip and total amount (OPTIONAL) </li>
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
   * Launch activity to closeout sales and refunds batch
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_CLOSEOUT_ALLOW_OPEN_TABS} - a boolean flag indicating if a closeout can proceed if open tabs are found (defaults to false)</li>
   * <li>{@link #EXTRA_CLOSEOUT_BATCHID} - the id of the batch to close (Optional - defaults to the current open batch )</li>
   * </ul>
   * <p>
   * Result data includes:
   * <ul>
   * <li>NONE</li>
   * </ul>
   */
  public static final String ACTION_CLOSEOUT_BATCH = "clover.intent.action.CLOSEOUT_BATCH";

  /**
   * Broadcast from Clover, indicating a closeout has been scheduled
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_CLOSEOUT_BATCH} the scheduled batch, only set when scheduled </li>
   * <li>{@link #EXTRA_FAILURE_MESSAGE} a message indicating status when scheduling fails </li>
   * </ul>
   */
  public static final String ACTION_CLOSEOUT_BATCH_SCHEDULED = "clover.intent.action.CLOSEOUT_BATCH_SCHEDULED";

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
   *
   * @see #ACTION_REQUEST_PAYMENT_REMOTE_VIEWS
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
   */
  public static final String ACTION_UPDATE_PAYMENT_REMOTE_VIEWS = "clover.intent.action.ACTION_UPDATE_PAYMENT_REMOTE_VIEWS";

  /**
   * Launch a dialog-style activity to authenticate an employee. All supported modes of
   * authentication that are possible on this device will be enabled, such as: passcode, card swipe,
   * fingerprint.
   * <p/>
   * Pick one of the following ways to authentication an employee:
   * <ol>
   *   <li>Authenticate an explicit employee by passing {@link #EXTRA_EMPLOYEE_ID} with the employee id</li>
   *   <li>Allow any manager or admin to authenticate by passing {@link #EXTRA_VALIDATE_ROLE} with boolean value true</li>
   *   <li>Authenticate an employee that has the permission specified by {@link #EXTRA_PERMISSIONS}
   *   <li>Authenticate an employee that has one of the provided
   *   {@link com.clover.sdk.v3.employees.AccountRole}s specified in {@link #EXTRA_ACCOUNT_ROLES}.</li>
   * </ol>
   * <p/>
   * Optionally you may add the following extras:
   * <ul>
   *   <li>{@link #EXTRA_REASON} - written explanation for why authentication is needed (please localize to current locale)</li>
   *   <li>{@link #EXTRA_SHOW_CANCEL_BUTTON} - if true, show cancel button on the dialog, default false</li>
   *   <li>{@link #EXTRA_PACKAGE} - package name associated with permission identified by {@link #EXTRA_PERMISSIONS} if validating some other package</li>
   * </ul>
   * <p>
   * Result data includes:
   * <ul>
   *   <li>{@link #EXTRA_EMPLOYEE_ID} String of the authenticated employee id if successful</li>
   * </ul>
   * <p>
   * Result codes:
   * <ul>
   *   <li>{@link android.app.Activity#RESULT_OK} - authenticated successfully</li>
   *   <li>{@link android.app.Activity#RESULT_CANCELED} - did not authenticate successfully</li>
   * </ul>
   */
  public static final String ACTION_AUTHENTICATE_EMPLOYEE = "clover.intent.action.AUTHENTICATE_EMPLOYEE";

  /**
   * @deprecated Use {@link #ACTION_AUTHENTICATE_EMPLOYEE} with {@link #EXTRA_ACCOUNT_ROLES}.
   */
  public static final String ACTION_REQUEST_ROLE = "com.clover.intent.action.REQUEST_ROLE";

  /**
   * Launch the secure payment activity. First check if the device is able to do this using
   * {@link com.clover.sdk.util.Platform2.Feature#SECURE_PAYMENTS}.
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_AMOUNT} - amount to be paid (Required)</li>
   * <li>{@link #EXTRA_ORDER_ID} - order associated with payment, if excluded, a new order record will be created</li>
   * <li>{@link #EXTRA_CARD_ENTRY_METHODS} - (deprecated - replaced by {@link #EXTRA_TRANSACTION_SETTINGS}) allowed payment types, default all allowed</li>
   * <li>{@link #EXTRA_EMPLOYEE_ID} - employee conducting transaction</li>
   * <li>{@link #EXTRA_TIP_AMOUNT} - tip amount</li>
   * <li>{@link #EXTRA_TAX_AMOUNT} - tax amount</li>
   * <li>{@link #EXTRA_TAXABLE_AMOUNTS} - tax rates, with eligible amounts</li>
   * <li>{@link #EXTRA_SERVICE_CHARGE_AMOUNT} - service charge amount</li>
   * <li>{@link #EXTRA_DISABLE_CASHBACK} - (deprecated - replaced by {@link #EXTRA_TRANSACTION_SETTINGS}) disable option for cashback during payment, default false</li>
   * <li>{@link #EXTRA_IS_TESTING} - whether payment is testing mode</li>
   * <li>{@link #EXTRA_VOICE_AUTH_CODE} - voice auth code</li>
   * <li>{@link #EXTRA_AVS_POSTAL_CODE} - postal code associated with payment</li>
   * <li>{@link #EXTRA_CARD_NOT_PRESENT} - whether payment card is not present, default false</li>
   * <li>{@link #EXTRA_REMOTE_PRINT} - if printing will be delegated to remote device</li>
   * <li>{@link #EXTRA_TRANSACTION_NO} - transaction number for payment</li>
   * <li>{@link #EXTRA_FORCE_SWIPE_PIN_ENTRY} - (deprecated - replaced by {@link #EXTRA_TRANSACTION_SETTINGS}) if only payment option will be swipe debit, default false</li>
   * <li>{@link #EXTRA_DISABLE_RESTART_TRANSACTION_WHEN_FAILED} - (deprecated - replaced by {@link #EXTRA_TRANSACTION_SETTINGS}) if activity will end after failed transaction, default false</li>
   * <li>{@link #EXTRA_EXTERNAL_PAYMENT_ID} - external payment id, used for integration with other POS platforms</li>
   * <li>{@link #EXTRA_CUSTOMER_TENDER} - Pre-selected customer tender. If present, pay with this tender.
   * The customer will not have the option to select a different tender or pay with a card. If the tender is not valid for this merchant this extra is ignored.
   * See {@link #ACTION_CUSTOMER_TENDER}.</li>
   * <li>{@link #EXTRA_TRANSACTION_SETTINGS} - pass transaction settings as a single object</li>
   * </ul>
   * <p>
   * Result data includes:
   * <ul>
   * <li>{@link #EXTRA_PAYMENT} - created payment</li>
   * <li>{@link #EXTRA_PAYMENT_ID} - created payment's UUID</li>
   * <li>{@link #EXTRA_VAS_PAYLOAD} - any vas payload read during session</li>
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
   * Launches station pay activity (Only used on Station)
   */
  public static final String ACTION_STATION_PAY = "clover.intent.action.START_STATION_PAYMENT";

  /**
   * Launches station secure pay activity (Only used on Station 2018)
   */
  public static final String ACTION_STATION_SECURE_PAY = "clover.intent.action.START_STATION_SECURE_PAYMENT";

  /**
   * Services that implement the IVasProvider interface
   */
  public static final String SERVICE_VAS_PROVIDER = "clover.intent.action.VAS_PROVIDER";

  /**
   * Launch activity to securely capture card data. First check if the device is able to do this using
   * {@link com.clover.sdk.util.Platform2.Feature#SECURE_PAYMENTS}.
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
   * Launch activity to check credit/debit card balance. First check if the device is
   * able to do this using {@link com.clover.sdk.util.Platform2.Feature#SECURE_PAYMENTS}.
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_TRANSACTION_TYPE} - must be set to {@link #TRANSACTION_TYPE_BALANCE_INQUIRY} (Required)</li>
   * <li>{@link #EXTRA_CARD_ENTRY_METHODS} - allowed payment types, default all allowed</li>
   * </ul>
   * <p>
   * Result data includes:
   * <ul>
   * <li>{@link #EXTRA_PAYMENT} - payment object with balance info populated if supported by card/returned by gateway</li>
   * <li>{@link #EXTRA_CARD_DATA} - captured card data</li>
   * </ul>
   * <p>
   * Result codes:
   * <ul>
   *     <li>{@link android.app.Activity#RESULT_OK} - balance request successful</li>
   *     <li>{@link android.app.Activity#RESULT_CANCELED} - balance request not successful</li>
   * </ul>
   */
  public static final String ACTION_SECURE_BALANCE_CHECK = "clover.intent.action.START_SECURE_BALANCE_CHECK";

  /**
   * Intent passed to start your app's customer-facing extensible tender activity (See
   * <a href="https://github.com/clover/android-examples/tree/master/extensibletenderexample">Example Usage</a>)
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_AMOUNT} - the transaction amount</li>
   * <li>{@link #EXTRA_CURRENCY} - the transaction currency</li>
   * <li>{@link #EXTRA_TAX_AMOUNT} - the transaction's tax amount</li>
   * <li>{@link #EXTRA_SERVICE_CHARGE_AMOUNT} - the transaction's service charge amount</li>
   * <li>{@link #EXTRA_TIP_AMOUNT} - the transaction's tip amount</li>
   * <li>{@link #EXTRA_ORDER_ID} - the Clover order ID</li>
   * <li>{@link #EXTRA_EMPLOYEE_ID} - the ID of the employee who initiated the payment</li>
   * <li>{@link #EXTRA_TENDER} - the tender for the transaction</li>
   * </ul>
   * </p>
   * <p>
   * Result data must include:
   * <ul>
   * <li>(Required) {@link #EXTRA_AMOUNT} - the approved transaction amount</li>
   * <li>(Optional) {@link #EXTRA_TIP_AMOUNT} - the approved transaction's tip amount</li>
   * <li>(Optional) {@link #EXTRA_CLIENT_ID} - the client ID / external payment ID</li>
   * <li>(Optional) {@link #EXTRA_NOTE} - the payment note</li>
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
   * @see #ACTION_REFUND
   */
  public static final String ACTION_CUSTOMER_TENDER = "clover.intent.action.CUSTOMER_TENDER";

  /**
   * Intent passed to start your app's merchant-facing extensible tender activity (See
   * <a href="https://github.com/clover/android-examples/tree/master/extensibletenderexample">Example Usage</a>)
   *
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_AMOUNT} - the transaction amount</li>
   * <li>{@link #EXTRA_CURRENCY} - the transaction currency</li>
   * <li>{@link #EXTRA_TAX_AMOUNT} - the transaction's tax amount</li>
   * <li>{@link #EXTRA_SERVICE_CHARGE_AMOUNT} - the transaction's service charge amount</li>
   * <li>{@link #EXTRA_ORDER_ID} - the Clover order ID</li>
   * <li>{@link #EXTRA_EMPLOYEE_ID} - the ID of the employee who initiated the payment</li>
   * <li>{@link #EXTRA_TENDER} - the tender for the transaction</li>
   * <li>{@link #EXTRA_ORDER} - the order for the transaction</li>
   * <li>{@link #EXTRA_NOTE} - the order note for the transaction</li>
   * </ul>
   * </p>
   * <p>
   * Result data includes:
   * <ul>
   * <li>(Required) {@link #EXTRA_AMOUNT} - the approved transaction amount </li>
   * <li>(Optional) {@link #EXTRA_CLIENT_ID} - the client ID / external payment ID</li>
   * <li>(Optional) {@link #EXTRA_NOTE} - the payment note</li>
   * <li>(Optional) {@link #EXTRA_TIP_AMOUNT} - the transaction's tip amount</li>
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
   * @see #ACTION_REFUND
   */
  public static final String ACTION_MERCHANT_TENDER = "clover.intent.action.MERCHANT_TENDER";

  /**
   * Intent passed to start your app's extensible tender refund activity
   * <p>
   * Extras passed:
   * <ul>
   * 		<li>{@link #EXTRA_MERCHANT_ID}</li>
   * 		<li>{@link #EXTRA_CURRENCY}</li>
   * 		<li>{@link #EXTRA_ORDER_ID}</li>
   * 		<li>{@link #EXTRA_PAYMENT_ID}</li>
   * 		<li>{@link #EXTRA_CLIENT_ID}</li>
   * 		<li>{@link #EXTRA_LINE_ITEM_IDS}</li>
   * 		<li>{@link #EXTRA_AMOUNT}</li>
   * </ul>
   * <p>
   * Result data includes:
   * <ul>
   * 		<li>(Required) {@link #EXTRA_AMOUNT}</li>
   * 		<li>(Required) {@link #EXTRA_ORDER_ID}</li>
   * 		<li>(Required) {@link #EXTRA_PAYMENT_ID}</li>
   * 		<li>(Optional) {@link #EXTRA_LINE_ITEM_IDS}</li>
   * </ul>
   * Result codes:
   * <ul>
   *    <li>{@link android.app.Activity#RESULT_OK} - refund processed</li>
   *    <li>{@link android.app.Activity#RESULT_CANCELED} - refund canceled</li>
   * </ul>
   */
  public static final String ACTION_REFUND = "clover.intent.action.REFUND";

  /**
   * @deprecated This does not work on recent devices. Use {@link BarcodeScanner} instead.
   * <p/>
   * Broadcast to start barcode scanner service on Station or Mini (Mobile scanner must be activated via physical trigger)
   * <p/>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_START_SCAN} - true will start scanner, false will close scanner (Required)</li>
   * <li>{@link #EXTRA_SHOW_PREVIEW} - whether scanner preview video will be shown, default is true</li>
   * <li>{@link #EXTRA_SHOW_MERCHANT_PREVIEW} - whether scanner preview will be shown in merchant facing mode, default is true</li>
   * <li>{@link #EXTRA_SHOW_CUSTOMER_PREVIEW} - whether scanner preview will be shown in customer facing mode, default is true</li>
   * <li>{@link #EXTRA_LED_ON} - whether LED will be on (selected devices only), default is false</li>
   * <li>{@link #EXTRA_SCAN_QR_CODE} - whether QR codes will be scanned, default is true</li>
   * <li>{@link #EXTRA_SCAN_1D_CODE} - whether 1D codes will be scanned, default is true</li>
   * <li>{@link #EXTRA_SHOW_CLOSE_BUTTON} - whether scanner preview will have a 'close' button, default is true</li>
   * <li>{@link #EXTRA_SHOW_LED_BUTTON} - whether scanner preview will have an LED toggle, default is true</li>
   * </ul>
   */
  @Deprecated
  public static final String ACTION_SCAN = "clover.intent.action.BARCODE_SCAN";

  /**
   * A broadcast intent sent to your app when it is installed or updated to a
   * new version.
   * <p/>
   * When an Android app is first installed it is placed in a stopped state. While in
   * the stopped state an app may not receive implicit broadcasts. An app exits the
   * stopped state when it has had an activity, service, or explicit broadcast started by
   * the user or another app. On typical Android devices this usually means the app won't
   * ever have any components invoked until the user taps the launcher icon once.
   * <p/>
   * By implementing a receiver for this broadcast and registering it in your app manifest
   * your app will immediately move out of the stopped state upon installation on a Clover
   * device. Even if you don't actually need to do anything specifically related to
   * installation you may want to implement an empty receiver just to ensure that your app
   * is moved out of the stopped state so it can immediately begin receiving other
   * broadcasts.
   * <p/>
   * Ensure that any operation performed in this receiver is idempotent.
   * <p/>
   * <b>
   * The service version of this intent is deprecated. Versions of Android 26 and greater
   * place restrictions of background execution that prevent non-foreground services from
   * being started. Receive this intent as a broadcast, as described below:
   * </b>
   * <pre>
   * {@code
   * <receiver
   *     android:name=".MyAppInstalledReceiver"
   *     android:exported="true">
   *   <intent-filter>
   *     <action android:name="com.clover.intent.action.APP_INSTALL_DONE"/>
   *   </intent-filter>
   * </receiver>
   * }
   * </pre>
   * Apps are restricted from performing blocking or otherwise long running operations in
   * a receiver's {@link android.content.BroadcastReceiver#onReceive(Context, Intent)}.
   * If you need to perform such work as a result on this intent use
   * <code>JobIntentService</code> from the support or androidx library.
   * You can find an example of this in <code>AppInstallDoneService</code> in
   * the SDK Examples.
   * <p/>
   * If you are developing an app and installing it via ADB (Android Studio or command
   * line) then you must manually invoke this broadcast receiver with the following
   * command:
   * <pre>
   *   adb shell am broadcast -a com.clover.intent.action.APP_INSTALL_DONE -n your.packagename/.MyAppInstalledReceiver
   * </pre>
   * This broadcast intent is only received by the app that was installed. You will
   * not receive this intent when other apps are installed or updated.
   *
   * @see <a href="https://developer.android.com/reference/androidx/core/app/JobIntentService">JobIntentService</a>
   */
  public static final String ACTION_APP_INSTALL_DONE = "com.clover.intent.action.APP_INSTALL_DONE";

  /**
   * An intent service intent that indicates the target app will be uninstalled in the
   * next 5 seconds. After 5 seconds any processes associated with the app are killed and the app is
   * uninstalled. The service started by this intent must be an intent service.
   * <p/>
   * Apps can use this time to perform any required cleanup actions. For example, remove any
   * inventory items your app created that do not make sense without your app
   * or order types created that are specific to your app. This intent should not trigger
   * anything that is visible to or require any interaction with the user. Preferably
   * all work is completed synchronously within the bounds of the
   * {@link android.app.IntentService}'s
   * {@link android.app.IntentService#onHandleIntent(Intent)} method.
   * <p/>
   * Note that this is a service intent, not a broadcast intent. It must be
   * received by an (intent) service, not a broadcast receiver.
   * <pre>
   * {@code
   * <service
   *     android:name=".MyCleanupService"
   *     android:exported="true">
   *   <intent-filter>
   *     <action android:name="com.clover.intent.action._APP_PRE_UNINSTALL"/>
   *   </intent-filter>
   * </service>
   * }
   * </pre>
   * This is only invoked for apps installed via the Clover app store, it is not invoked
   * on development devices when adb uninstall is used. To test during development you
   * may invoke this manually with the following command:
   * <pre>
   *   adb shell am startservice -a com.clover.intent.action.APP_PRE_UNINSTALL -n your.packagename/.MyCleanupService
   * </pre>
   */
  public static final String ACTION_APP_PRE_UNINSTALL = "com.clover.intent.action.APP_PRE_UNINSTALL";

  /** @deprecated Replaced by {@link #ACTION_MERCHANT_TENDER} and {@link #ACTION_CUSTOMER_TENDER} */
  public static final String ACTION_PAY = "clover.intent.action.PAY";

  /** @deprecated  */
  public static final String ACTION_BILL_SPLIT = "com.clover.intent.action.BILL_SPLIT";

  /** @deprecated  */
  public static final String ACTION_GIFT_CARD_TX = "clover.intent.action.GIFT_CARD_TX";

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

  /** {@link boolean}, true indicates that a closeout can proceed if open tabs are found */
  public static final String EXTRA_CLOSEOUT_ALLOW_OPEN_TABS = "clover.intent.extra.CLOSEOUT_ALLOW_OPEN_TABS";

  /** {@link String}, the UUID of a Batch object */
  public static final String EXTRA_CLOSEOUT_BATCHID = "clover.intent.extra.CLOSEOUT_BATCHID";

  /** {@link String}, the UUID of a LineItem object */
  public static final String EXTRA_CLOVER_LINE_ITEM_ID = "com.clover.intent.extra.LINE_ITEM_ID";

  /** {@link String}, the UUID of an Item object */
  public static final String EXTRA_CLOVER_ITEM_ID = "com.clover.intent.extra.ITEM_ID";

  /** {@link String}, the UUID of a Payment object */
  public static final String EXTRA_CLOVER_PAYMENT_ID = "com.clover.intent.extra.PAYMENT_ID";

  /** {@link String}, the labelKey of a Tender type */
  public static final String EXTRA_CLOVER_TENDER_LABEL_KEY = "com.clover.intent.extra.TENDER";

  /** {@link Long}, a monetary amount */
  public static final String EXTRA_AMOUNT = "clover.intent.extra.AMOUNT";

  /** {@link String}, the invoice number */
  public static final String EXTRA_INVOICE_NUMBER = "clover.intent.extra.INVOICE_NUMBER";

  /** {@link Long}, the tippable amount associated with a payment */
  public static final String EXTRA_TIPPABLE_AMOUNT = "clover.intent.extra.TIPPABLE_AMOUNT";

  /** {@link String}, the json for the tip suggestions associated with a payment */
  public static final String EXTRA_TIP_SUGGESTIONS = "clover.intent.extra.TIP_SUGGESTIONS";

  /** {@link java.util.Currency}, the currency of a transaction */
  public static final String EXTRA_CURRENCY = "clover.intent.extra.CURRENCY";

  /** {@link String}, the UUID of a merchant */
  public static final String EXTRA_MERCHANT_ID = "clover.intent.extra.MERCHANT_ID";

  /** {@link String}, the UUID of a Payment object */
  public static final String EXTRA_PAYMENT_ID = "clover.intent.extra.PAYMENT_ID";

  /** {@link String}, the UUID of the quick pay transaction if collected **/
  public static final String EXTRA_QUICK_PAYMENT_TRANSACTION_ID = "clover.intent.extra.QUICK_PAY_ID";

  /** {@link String}, the UUID of a Credit object */
  public static final String EXTRA_CREDIT_ID = "clover.intent.extra.CREDIT_ID";

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

  /** {@link com.clover.sdk.v3.customers.Customer}, a customer object */
  public static final String EXTRA_CUSTOMER_V3 = "com.clover.intent.extra.CUSTOMERV3";

  /** {@link com.clover.sdk.v3.customers.CustomerInfo}, a customer object */
  public static final String EXTRA_CUSTOMERINFO = "com.clover.intent.extra.CUSTOMERINFO";

  /** {@link String}, the UUID of an Employee object */
  public static final String EXTRA_EMPLOYEE_ID = "clover.intent.extra.EMPLOYEE_ID";

  /** An {@link java.util.ArrayList} of {@link com.clover.sdk.v3.employees.AccountRole}. */
  public static final String EXTRA_ACCOUNT_ROLES = "clover.intent.extra.ACCOUNT_ROLES";

  /** {@link String}, the ID of a Client or external payment */
  public static final String EXTRA_CLIENT_ID = "clover.intent.extra.CLIENT_ID";

  /** {@link Long}, tip amount */
  public static final String EXTRA_TIP_AMOUNT = "clover.intent.extra.TIP_AMOUNT";

  /** {@link Boolean}, whether to display confirmation of tip and total amount */
  public static final String EXTRA_TIP_CONFIRM_MODE = "clover.intent.extra.TIP_CONFIRM_MODE";

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

  /** {@link int}, does this intent need remote payment confirmation i.e. RemotePay */
  public static final String EXTRA_REQUIRES_REMOTE_CONFIRMATION = "clover.intent.extra.REQUIRES_REMOTE_CONFIRMATION";

  /** {@link int}, does this intent need remote payment confirmation i.e. RemotePay */
  public static final String EXTRA_REQUIRES_FINAL_REMOTE_APPROVAL = "clover.intent.extra.REQUIRES_FINAL_REMOTE_APPROVAL";

  /** {@link Boolean}, does this intent skip the ELV override screen i.e. RemotePay */
  public static final String EXTRA_SKIP_ELV_LIMIT_OVERRIDE = "clover.intent.extra.SKIP_ELV_LIMIT_OVERRIDE";

  /** {@link int}, does this intent need remote payment confirmation i.e. RemotePay */
  public static final String EXTRA_APP_TRACKING_ID = "clover.intent.extra.APP_TRACKING_ID";

  /** {@link int}, is partial authorization allowed (if it occurs)? */
  public static final String EXTRA_ALLOW_PARTIAL_AUTH = "clover.intent.extra.ALLOW_PARTIAL_AUTH";

  /** {@link int}, does this intent need german info */
  public static final String GERMAN_INFO = "clover.intent.extra.GERMAN_INFO";

  /** {@link com.clover.sdk.v3.payments.CashAdvanceCustomerIdentification} object used  for CashAdvance payments */
  public static final String CASHADVANCE_CUSTOMER_IDENTIFICATION = "clover.intent.extra.CASHADVANCE_CUSTOMER_IDENTIFICATION";

  /** {@link int}, does this payment need remote offline payment confirmation */
  public static final String EXTRA_OFFLINE_PAYMENT_CONFIRMATION = "clover.intent.extra.OFFLINE_PAYMENT_CONFIRMATION";

  /** {@link int}, does this payment need remote duplicate payment confirmation */
  public static final String EXTRA_DUPLICATE_PAYMENT_CONFIRMATION = "clover.intent.extra.DUPLICATE_PAYMENT_CONFIRMATION";

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

  /** {@link Boolean}, whether the launched activity should validate roles instead of permissions (Required) */
  public static final String EXTRA_VALIDATE_ROLE = "clover.intent.extra.CHECK_ROLE";

  /** {@link Boolean}, whether to start or stop barcode scanner */
  public static final String EXTRA_START_SCAN = "clover.intent.extra.SCAN_START";

  /** {@link Boolean}, whether to show scanner preview video (merchant and customer facing modes) */
  public static final String EXTRA_SHOW_PREVIEW = "clover.intent.extra.SHOW_PREVIEW";

  /** {@link Boolean}, whether to show scanner preview video (merchant facing mode only) */
  public static final String EXTRA_SHOW_MERCHANT_PREVIEW = "clover.intent.extra.SHOW_MERCHANT_PREVIEW";

  /** {@link Boolean}, whether to show scanner preview video (customer facing mode only) */
  public static final String EXTRA_SHOW_CUSTOMER_PREVIEW = "clover.intent.extra.SHOW_CUSTOMER_PREVIEW";

  /** {@link Boolean}, whether LED will be on during barcode scanning if this device has a barcode LED,
   * generally this option is best left out and the preferred device default with be used. */
  public static final String EXTRA_LED_ON = "clover.intent.extra.LED_ON";

  /** {@link Boolean}, whether QR codes will be scanned */
  public static final String EXTRA_SCAN_QR_CODE = "clover.intent.extra.SCAN_QR_CODE";

  /** {@link Boolean}, whether 1D codes will be scanned */
  public static final String EXTRA_SCAN_1D_CODE = "clover.intent.extra.SCAN_1D_CODE";

  /** {@link Boolean}, whether scanner preview video will have a 'close' button. If the scan is
   * occurring in merchant-facing mode the merchant will always have a mechanism to close the
   * scanner app. */
  public static final String EXTRA_SHOW_CLOSE_BUTTON = "clover.intent.extra.SHOW_CLOSE_BUTTON";

  /** {@link Boolean}, whether scanner preview video will have a LED light toggle if this device
   * has a barcode LED, many Clover devices do not have an LED, some devices may not honor this
   * option. */
  public static final String EXTRA_SHOW_LED_BUTTON = "clover.intent.extra.SHOW_LED_BUTTON";

  public static final String EXTRA_SCAN_X = "clover.intent.extra.SCAN_X";

  public static final String EXTRA_SCAN_Y = "clover.intent.extra.SCAN_Y";

  /**
   * On devices where multiple barcode scanners are available, select a barcode scanner to
   * start. Set this extra into the {@link Bundle} passed to
   * {@link BarcodeScanner#startScan(Bundle)}.
   * The value passed must be one of the results from
   * {@link BarcodeScanner#getAvailable()}. The result of passing a value that is not in
   * the result of {@link BarcodeScanner#getAvailable()} is undefined.
   * <p/>
   * If not present, this defaults to either {@link BarcodeScanner#BARCODE_SCANNER_FACING_DUAL} or
   * {@link BarcodeScanner#BARCODE_SCANNER_FACING_MERCHANT},
   * depending on what is available on the device.
   *
   * @see BarcodeScanner#getAvailable()
   * @see BarcodeScanner#startScan(Bundle)
   * @see BarcodeScanner#BARCODE_SCANNER_FACING_DUAL
   * @see BarcodeScanner#BARCODE_SCANNER_FACING_MERCHANT
   * @see BarcodeScanner#BARCODE_SCANNER_FACING_CUSTOMER
   */
  public static final String EXTRA_SCANNER_FACING = "clover.intent.extra.SCANNER_FACING";

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

  /**  @deprecated */
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
  /** A value for {@link #EXTRA_TRANSACTION_TYPE} */
  public static final String TRANSACTION_TYPE_CASH_ADVANCE = "cashAdvance";
  /** A value for {@link #EXTRA_TRANSACTION_TYPE} */
  public static final java.lang.String TRANSACTION_TYPE_MANUAL_REVERSAL_PAYMENT = "manualReversalPayment";
  /** A value for {@link #EXTRA_TRANSACTION_TYPE} */
  public static final java.lang.String TRANSACTION_TYPE_ADJUSTMENT_PAYMENT = "adjustmentPayment";
  /** A value for {@link #EXTRA_TRANSACTION_TYPE} */
  public static final java.lang.String TRANSACTION_TYPE_MANUAL_REVERSAL_REFUND  = "manualReversalRefund";
  /** A value for {@link #EXTRA_TRANSACTION_TYPE} */
  public static final java.lang.String TRANSACTION_TYPE_ADJUSTMENT_REFUND = "adjustmentRefund";
  /** A value for {@link #EXTRA_TRANSACTION_TYPE} */
  public static final java.lang.String TRANSACTION_TYPE_CAPTURE_PREAUTH  = "capturePreAuth";
  /** A value for {@link #EXTRA_TRANSACTION_TYPE} */
  public static final java.lang.String TRANSACTION_TYPE_VAS_DATA  = "vasData";

  /**
   * A value for {@link #EXTRA_TRANSACTION_TYPE}, a transaction that allows an app to verify whether a card is valid.
   * An external app sends an intent of type Intents.ACTION_SECURE_PAY to secure payment app to launch the card verification workflow,
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_TRANSACTION_TYPE} - type of transaction to be performed by Secure Payment, set value to TRANSACTION_TYPE_VERIFY_CARD </li>
   * <li>{@link #EXTRA_EXTERNAL_REFERENCE_ID} -  used for passing in an external reference id which gets stored as part of the transaction at the server </li>
   * Result data includes:
   * <ul>
   * <li>{@link #EXTRA_TOKEN_REQUEST} - created token request, see {@link com.clover.sdk.v3.payments.TokenRequest}  </li>
   * </ul>
   */
  public static final String TRANSACTION_TYPE_VERIFY_CARD  = "verifyCard";

  /**
   * A value for {@link #EXTRA_TRANSACTION_TYPE}, a transaction that allows an app to create a token from a card for later use in lieu of the card.
   * An external app sends an intent of type Intents.ACTION_SECURE_PAY with TRANSACTION_TYPE_TOKENIZE_CARD as the EXTRA_TRANSACTION_TYPE
   * to the secure payment app to launch the card tokenization workflow,
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_TRANSACTION_TYPE} - type of transaction to be performed by Secure Payment, set value to TRANSACTION_TYPE_TOKENIZE_CARD </li>
   * <li>{@link #EXTRA_EXTERNAL_REFERENCE_ID} -  used for passing in an external reference id which gets stored as part of the transaction at the server </li>
   * Result data includes:
   * <ul>
   * <li>{@link #EXTRA_TOKEN_REQUEST} - token request object created, see {@link com.clover.sdk.v3.payments.TokenRequest}  </li>
   * </ul>
   */
  public static final String TRANSACTION_TYPE_TOKENIZE_CARD  = "tokenizeCard";

  /** {@link String}, external reference id */
  public static final String  EXTRA_EXTERNAL_REFERENCE_ID = "clover.intent.extra.EXTERNAL_REFERENCE_ID";

  /** {@link String}, external token id */
  public static final String  EXTRA_EXTERNAL_TOKEN_ID = "clover.intent.extra.EXTERNAL_TOKEN_ID";

  /** {@link com.clover.sdk.v3.payments.TokenRequest}, token request objects   */
  public static final String  EXTRA_TOKEN_REQUEST = "clover.intent.extra.TOKEN_REQUEST";

  /** {@link Boolean}, card not present, used during manual card entry */
  public static final String EXTRA_CARD_NOT_PRESENT = "clover.intent.extra.CARD_NOT_PRESENT";

  /** {@link Boolean}, allow payments to be accepted offline */
  public static final String EXTRA_ALLOW_OFFLINE_ACCEPTANCE = "clover.intent.extra.ALLOW_OFFLINE_ACCEPTANCE";

  /** {@link Boolean}, allow offline payments to be accepted without an approval prompt */
  public static final String EXTRA_OFFLINE_NO_PROMPT = "clover.intent.extra.OFFLINE_NO_PROMPT";

  /** {@link Boolean}, force payments to be accepted offline */
  public static final String EXTRA_FORCE_OFFLINE = "clover.intent.extra.FORCE_OFFLINE";

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

  /**
   * {@link String}, payment transaction number
   * @deprecated , internally in the SPS/SPA
   */
  public static final String EXTRA_TRANSACTION_NO = "transactionNo";

  /** {@link Boolean}, if only payment option will be swipe debit */
  public static final String EXTRA_FORCE_SWIPE_PIN_ENTRY = "forceSwipePinEntry";

  /** {@link Boolean}, if true, the secure payment activity will end after a failed transaction */
  public static final String EXTRA_DISABLE_RESTART_TRANSACTION_WHEN_FAILED = "disableRestartTransactionWhenFailed";

  /** {@link String}, external payment id, used for integration with other POS platforms*/
  public static final String EXTRA_EXTERNAL_PAYMENT_ID = "externalPaymentId";
  /** A com.clover.sdk.v3.payments.VaultedCard object that contains a multi-pay token to support paying with card-on-file */
  public static final String EXTRA_VAULTED_CARD = "vaultedCard";

  /** {@link String}, an error description */
  public static final String EXTRA_DECLINE_REASON = "clover.intent.extra.DECLINE_REASON";

  /** {@link com.clover.sdk.v3.base.Tender}, a Tender object */
  public static final String EXTRA_TENDER = "clover.intent.extra.TENDER";

  /** {@link com.clover.sdk.v3.base.Tender}, Pre-selected customer tender */
  public static final String EXTRA_CUSTOMER_TENDER = "clover.intent.extra.CUSTOMER_TENDER";

  /** {@link String}, a card number */
  public static final String EXTRA_CARD_NUMBER = "clover.intent.extra.CARD_NUMBER";

  /** {@link Boolean} */
  public static final String EXTRA_IGNORE_PAYMENT = "clover.intent.extra.IGNORE_PAYMENT";

  /** {@link com.clover.sdk.v3.payments.GiftCardResponse} */
  public static final String EXTRA_GIFT_CARD_RESPONSE = "clover.intent.extra.GIFT_CARD_RESPONSE";

  /** {@link com.clover.sdk.v3.payments.Payment} */
  public static final String EXTRA_VAULTED_CARD_PAYMENT = "clover.intent.extra.VAULTED_CARD_PAYMENT";

  /** {@link String}, message shown upon failure */
  public static final String EXTRA_FAILURE_MESSAGE ="clover.intent.extra.FAILURE_MESSAGE";

  /** {@link String}, indicator of a network failure */
  public static final String EXTRA_NETWORK_FAILURE ="clover.intent.extra.NETWORK_FAILURE";

  /** {@link com.clover.sdk.v3.payments.CardTransaction}, originating transaction */
  public static final String EXTRA_ORIGINATING_TRANSACTION = "originatingTransaction";

  /** {@link com.clover.sdk.v3.payments.Payment}, originating payment (applies to refunds, required for Interac refunds) */
  public static final String EXTRA_ORIGINATING_PAYMENT = "originatingPayment";

  /** {@link com.clover.sdk.v3.payments.Credit}, originating credit (applies to reversal, required for Nexo credit reversal) */
  public static final String EXTRA_ORIGINATING_CREDIT = "originatingCredit";

  /** A bit value for {@link #EXTRA_CARD_ENTRY_METHODS}, can be bitwise-ored with other CARD_ENTRY_METHOD values */
  public static final int CARD_ENTRY_METHOD_MAG_STRIPE = 0b00000001;
  /** A bit value for {@link #EXTRA_CARD_ENTRY_METHODS}, can be bitwise-ored with other CARD_ENTRY_METHOD values */
  public static final int CARD_ENTRY_METHOD_ICC_CONTACT = 0b00000010;
  /** A bit value for {@link #EXTRA_CARD_ENTRY_METHODS}, can be bitwise-ored with other CARD_ENTRY_METHOD values */
  public static final int CARD_ENTRY_METHOD_NFC_CONTACTLESS = 0b00000100;
  /** A bit value for {@link #EXTRA_CARD_ENTRY_METHODS}, can be bitwise-ored with other CARD_ENTRY_METHOD values */
  public static final int CARD_ENTRY_METHOD_MANUAL = 0b00001000;
  /** A bit value for {@link #EXTRA_CARD_ENTRY_METHODS}, this value should be used exclusively if Vaulted Card payment is intended for headless mode  */
  public static final int CARD_ENTRY_METHOD_VAULTED_CARD = 0b00010000;
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

  /** {@link com.clover.sdk.v3.payments.VasPayload}, v3 VasPayload object */
  public static final String EXTRA_VAS_PAYLOAD = "clover.intent.extra.VAS_PAYLOAD";

  /** {@link com.clover.sdk.v3.payments.Batch}, v3 Batch object */
  public static final String EXTRA_CLOSEOUT_BATCH = "clover.intent.extra.BATCH";

  /** {@link java.util.ArrayList} of {@link com.clover.sdk.v3.payments.Payment} objects */
  public static final String EXTRA_PAYMENTS = "clover.intent.extra.PAYMENTS";

  /** {@link java.util.ArrayList} of {@link com.clover.sdk.v3.payments.Payment} objects */
  public static final String EXTRA_VOIDED_PAYMENTS = "clover.intent.extra.VOIDED_PAYMENTS";

  /** {@link com.clover.sdk.v3.order.VoidReason}, v3 VoidReason object */
  public static final String EXTRA_VOID_REASON = "clover.intent.extra.VOID_REASON";

  /** {@link com.clover.sdk.v3.payments.Credit}, v3 Credit object (Manual Refund) */
  public static final String EXTRA_CREDIT = "clover.intent.extra.CREDIT";

  /** {@link com.clover.sdk.v3.payments.Refund}, v3 Refund object */
  public static final String EXTRA_REFUND = "clover.intent.extra.REFUND";

  /** {@link com.clover.sdk.v3.payments.CreditRefund}, v3 CreditRefund object (Reversal of Manual Refund) */
  public static final String EXTRA_CREDIT_REFUND = "clover.intent.extra.CREDIT_REFUND";

  /** {@link com.clover.sdk.v3.payments.Authorization}, v3 Authorization object */
  public static final String EXTRA_AUTHORIZATION = "clover.intent.extra.AUTHORIZATION";

  /** {@link Boolean}, whether an authorization is being incremented or decremented */
  public static final String EXTRA_AUTHORIZATION_INCREMENT = "clover.intent.extra.AUTHORIZATION_INCREMENT";

  /** {@link Boolean}, whether to show amount remaining after payment */
  public static final String EXTRA_SHOW_REMAINING = "clover.intent.extra.SHOW_REMAINING";

  /** {@link Boolean}, whether to show the Void button */
  public static final String EXTRA_SHOW_VOID_BUTTON = "clover.intent.extra.SHOW_VOID_BUTTON";

  /** {@link Boolean}, whether signature is already verified */
  public static final String EXTRA_SIGNATURE_VERIFIED = "clover.intent.extra.SIGNATURE_VERIFIED";

  /** {@link com.clover.common2.Signature2}, payment Signature2 signature */
  public static final String EXTRA_SIGNATURE = "clover.intent.extra.SIGNATURE";

  /** {@link Boolean}, print receipt extras */
  public static final String EXTRA_PRINT_RECEIPT_ONLY = "clover.intent.extra_PRINT_RECEIPT_ONLY";

  /** {@link int}, representation of bit flags from {@link com.clover.sdk.v1.printer.job.PrintJob} */
  public static final String EXTRA_RECEIPT_FLAG = "clover.intent.extra.RECEIPT_FLAG";

  /** Vas Settings */
  public static final String EXTRA_VAS_SETTINGS = "clover.intent.extra.VAS_SETTINGS";

  /** VAS URL customization tokens */
  public static final String EXTRA_VAS_URL_TOKENS = "clover.intent.extra.VAS_URL_TOKENS";

  /* Transaction Settings Section Start */

  public static final String EXTRA_TRANSACTION_SETTINGS = "clover.intent.extra.TRANSACTION_SETTINGS";

  /** {@link Boolean}, are tips enabled for this transaction */
  public static final String EXTRA_TIPS_ENABLED = "clover.intent.extra.TIPS_ENABLED";

  /** {@link Boolean}, is the receipt options screen disabled for this transaction */
  public static final String EXTRA_DISABLE_RECEIPT_OPTIONS = "clover.intent.extra.DISABLE_RECEIPT_OPTIONS";

  /** {@link java.util.Map<String, String>} set of key/value pairs that are passed through to the server */
  public static final String EXTRA_PASS_THROUGH_VALUES = "clover.intent.extra.PASS_THROUGH_VALUES";

  /** {@link java.util.Map<String, String>} set of key/value pairs used for application specific implementations */
  public static final String EXTRA_APPLICATION_SPECIFIC_VALUES = "clover.intent.extra.APPLICATION_SPECIFIC_VALUES";

  /** {@link com.clover.sdk.v3.payments.TipMode}, where tips are entered/provided (e.g. on screen
   * after/before, on paper, provided or no tip) for this transaction */
  public static final String EXTRA_TIP_MODE = "clover.intent.extra.TIP_MODE";
  /** A value for {@link #EXTRA_TIP_MODE} */
  public static final String TIP_MODE_ON_SCREEN_BEFORE_PAYMENT = "ON_SCREEN_BEFORE_PAYMENT";
  /** A value for {@link #EXTRA_TIP_MODE} */
  public static final String TIP_MODE_ON_SCREEN_AFTER_PAYMENT = "ON_SCREEN_AFTER_PAYMENT";
  /** A value for {@link #EXTRA_TIP_MODE} */
  public static final String TIP_MODE_ON_PAPER = "ON_PAPER";
  /** A value for {@link #EXTRA_TIP_MODE} */
  public static final String TIP_MODE_NO_TIP = "NO_TIP";

  /** {@link com.clover.sdk.v3.payments.DataEntryLocation}, where signatures are entered (e.g. on screen, on paper, none) for this transaction */
  public static final String EXTRA_SIGNATURE_ENTRY_LOCATION = "clover.intent.extra.SIGNATURE_ENTRY_LOCATION";
  /** A value for {@link #EXTRA_SIGNATURE_ENTRY_LOCATION} */
  public static final String DATA_ENTRY_LOCATION_ON_SCREEN = "ON_SCREEN";
  /** A value for {@link #EXTRA_SIGNATURE_ENTRY_LOCATION} */
  public static final String DATA_ENTRY_LOCATION_ON_PAPER = "ON_PAPER";
  /** A value for {@link #EXTRA_SIGNATURE_ENTRY_LOCATION} */
  public static final String DATA_ENTRY_LOCATION_NONE = "NONE";

  /** {@link Long}, what is the signature threshold for this transaction */
  public static final String EXTRA_SIGNATURE_THRESHOLD = "clover.intent.extra.SIGNATURE_THRESHOLD";

  /** {@link String}, elv transaction type for Germany */
  public static final String EXTRA_GERMAN_ELV = "clover.intent.extra.GERMAN_ELV";
  /** A value for {@link #EXTRA_GERMAN_ELV} */
  public static final String GERMAN_ELV = "germanElv";
  /** A value for {@link #EXTRA_GERMAN_ELV} */
  public static final String GERMAN_ELV_ONLINE  = "germanElvOnline";
  /** A value for {@link #EXTRA_GERMAN_ELV} */
  public static final String GERMAN_ELV_OFFLINE  = "germanElvOffline";
  /** A value for {@link #EXTRA_GERMAN_ELV} */
  public static final String GERMAN_GIROCARD = "germanGirocard";
  /** A value for {@link #EXTRA_ELV_APPLICATION_LABEL} */
  public static final String EXTRA_ELV_APPLICATION_LABEL = "elvApplicationLabel";

  /* Transaction Settings Section End */

  /**
   * Broadcast from Clover, indicating an order was created
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_CLOVER_ORDER_ID} - the UUID of created order (note that this order may or may not be saved) </li>
   * </ul>
   */
  public static final String ACTION_ORDER_CREATED = "com.clover.intent.action.ORDER_CREATED";

  /**
   * Broadcast from Clover, indicating a lineItem has been added to an order
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_CLOVER_LINE_ITEM_ID} - the UUID of the created LineItem</li>
   * <li>{@link #EXTRA_CLOVER_ITEM_ID} - the UUID of the Item associated with the LineItem</li>
   * <li>{@link #EXTRA_CLOVER_ORDER_ID} - the UUID of the order associated with the LineItem</li>
   * </ul>
   */
  public static final String ACTION_LINE_ITEM_ADDED = "com.clover.intent.action.LINE_ITEM_ADDED";

  /**
   * Broadcast from Clover, indicating a payment has been successfully processed
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_CLOVER_PAYMENT_ID} - the UUID of the processed Payment</li>
   * <li>{@link #EXTRA_CLOVER_TENDER_LABEL_KEY} - the LabelKey of the Tender used</li>
   * <li>{@link #EXTRA_CLOVER_ORDER_ID} - the UUID of the order associated with the Payment</li>
   * </ul>
   */
  public static final String ACTION_PAYMENT_PROCESSED = "com.clover.intent.action.PAYMENT_PROCESSED";

  /**
   * Broadcast from Clover, indicating an Order has been saved for later processing
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_CLOVER_ORDER_ID} - the UUID of saved Order</li>
   * </ul>
   */
  public static final String ACTION_ORDER_SAVED = "com.clover.intent.action.ORDER_SAVED";

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
   * Broadcast from Clover, indicating the active order in the Register Pay activity
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

  /**
   * Broadcast indicating that a customer has been identified as a 'current' customer of interest.
   * This may indicate that the customer was identified by a third party app on the device, or by a
   * Clover application.  The customer may or may not exist as a clover customer.  The customer set
   * on the intent is a wrapper of the Clover Customer.
   *
   * <p>
   * Extras passed:
   * <ul>
   * <li>{@link #EXTRA_CUSTOMERINFO} - the RemoteCustomer object that was identified. (Required)</li>
   * </ul>
   */
  public static final String ACTION_V1_CUSTOMER_IDENTIFIED = "clover.intent.action.V1_CUSTOMER_IDENTIFIED";

  /** @deprecated */
  public static final String EXTRA_AVAILABLE = "clover.intent.extra_AVAILABLE";

  /** {@link Boolean} */
  public static final String EXTRA_SHOW_SEARCH = "clover.intent.extra_SHOW_SEARCH";

  /** {@link int}, A drawable resource ID, the image to be displayed on the customer-facing tender button */
  public static final String META_CUSTOMER_TENDER_IMAGE = "clover.intent.meta.CUSTOMER_TENDER_IMAGE";

  /** {@link int}, A drawable resource ID, the image to be displayed on the merchant-facing tender button*/
  public static final String META_MERCHANT_TENDER_IMAGE = "clover.intent.meta.MERCHANT_TENDER_IMAGE";

  /** {@link Boolean} flag */
  public static final String EXTRA_USE_LAST_SWIPE = "clover.intent.extra_USE_LAST_SWIPE";

  /** {@link Boolean} flag Indicates if device supports ECR mode or not*/
  public static final String EXTRA_ECR_MODE = "clover.intent.extra_ECR_MODE";

  /** {@link String} Indicates name of theme to be used in station-pay/secure-pay*/
  public static final String EXTRA_THEME_NAME = "clover.intent.extra_THEME_NAME";

  /** {@link Boolean} flag Indicates if the secure pay app should send the transaction result when the transaction is complete.
   * Usually the result is sent when spa finishes, but this flag indicates, that the result shall be sent as soon as the
   * transaction result is available
   */
  public static final String EXTRA_SEND_RESULT_ON_TRANSACTION_COMPLETE = "clover.intent.extra_ECR_SEND_RESULT_ON_TRANSACTION_COMPLETE";

  /** {@link String} A regular expression to check the validity of invoice number*/
  public static final String EXTRA_INVOICE_ID_REGEX = "clover.intent.extra.INVOICE_ID_REGEX";

  /** {@link Boolean} Flag to check if invoice feature is available for merchant*/
  public static final String EXTRA_INVOICE_ID_AVAILABLE = "clover.intent.extra.INVOICE_ID_AVAILABLE";

  ///// Keypad ////////////////////////////////////////////////////////////////////////////////////
  /**
   * An activity action that will start an activity that accepts keypad input from the
   * user. Start for result, and obtain the text entered by the user from the result data
   * extra {@link #RESULT_KEYPAD_TEXT}
   * <p/>
   * The type of keypad is selected by setting the extra {@link #EXTRA_KEYPAD_TYPE} to either
   * {@link #KEYPAD_TYPE_NUMERIC}, {@link #KEYPAD_TYPE_EMAIL}, or
   * {@link #KEYPAD_TYPE_PHONESMS}. The default is {@link #KEYPAD_TYPE_NUMERIC} if not
   * specified.
   * <p/>
   * To pre-populate the text set the extra {@link #EXTRA_KEYPAD_TYPE}. This is optional.
   * <p/>
   * To show the user a list of text completions to the user set the extra
   * {@link #EXTRA_KEYPAD_COMPLETIONS}. This is optional.
   */
  public static final String ACTION_KEYPAD = "clover.intent.action.KEYPAD";

  /**
   * Keypad optimized for numeric input. Set in the extra {@link #EXTRA_KEYPAD_TYPE}.
   */
  public static final int KEYPAD_TYPE_NUMERIC = 1;
  /**
   * Keypad optimized for email address input. Set in the extra {@link #EXTRA_KEYPAD_TYPE}.
   */
  public static final int KEYPAD_TYPE_EMAIL = 2;
  /**
   * Keypad optimized for phone and SMS number input. Set in the extra {@link #EXTRA_KEYPAD_TYPE}.
   */
  public static final int KEYPAD_TYPE_PHONESMS = 3;
  /**
   * An {@link ArrayList} of {@link String}, word completions to display above the keyboard.
   * For example this can be used to
   * provide a list of email suffixes ("@gmail.com", "@hotmail.com", etc). If absent, the
   * completion bar is hidden. Set this into the start activity intent using
   * {@link Bundle#putStringArrayList(String, ArrayList)}.
   */
  public static final String EXTRA_KEYPAD_COMPLETIONS = "completions";
  /**
   * An integer, the desired keypad type as an integer. Either {@link #KEYPAD_TYPE_NUMERIC}, or
   * {@link #KEYPAD_TYPE_EMAIL}, or {@link #KEYPAD_TYPE_PHONESMS}.
   * This is optional and defaults to {@link #KEYPAD_TYPE_NUMERIC} if not specified.
   */
  public static final String EXTRA_KEYPAD_TYPE = "type";
  /**
   * A {@link String}, the initial text to populate into the edit area.
   * This is optional and defaults to the empty string if not specified.
   */
  public static final String EXTRA_KEYPAD_TEXT = "text";
  /**
   * A {@link String}, the text entered by the user.
   * This is returned as an extra in the activity result data. This
   * is only valid if the activity result code is {@link android.app.Activity#RESULT_OK}.
   * <p/>
   * The entire content of the edit text is returned here, as a string. This includes
   * any initial text provided via {@link #EXTRA_KEYPAD_TEXT} (that wasn't edited away by the
   * user).
   */
  public static final String RESULT_KEYPAD_TEXT = "text";
  ///// Keypad ////////////////////////////////////////////////////////////////////////////////////

  /**
   * Intent to track package of the Payment.
   */
  public static final String EXTRA_ORIGINATING_PAYMENT_PACKAGE = "originating_payment_package";

  /**
   * Intent Extra to toggle credit surcharge.
   */
  public static final String EXTRA_DISABLE_CREDIT_SURCHARGE = "clover.intent.extra.DISABLE_SURCHARGE";

  /**
   * {@link Boolean}, a private extra indicating the user selected Scan QR Code for this Payment.
   * The intent consumer will then display the QR code as the only payment method.
   */
  public static final String EXTRA_PRESENT_QRC_ONLY = "clover.intent.extra.PRESENT_QRC_ONLY";

  /**
   * Intent Extra to Bypass Manual Card Entry Data.
   */
  public static final String EXTRA_MANUAL_CARD_ENTRY_BYPASS_MODE = "clover.intent.extra.EXTRA_MANUAL_CARD_ENTRY_BYPASS_MODE";

  /**
   * Intent Extra to Allow Manual Card Entry on MFD.
   */
  public static final String EXTRA_ALLOW_MANUAL_CARD_ENTRY_ON_MFD = "clover.intent.extra.EXTRA_ALLOW_MANUAL_CARD_ENTRY_ON_MFD";

  /**
   * Intent Extra to Manual Card PAN Data.
   */
  public static final String EXTRA_MANUAL_CARD_PAN = "clover.intent.extra.EXTRA_MANUAL_CARD_PAN";

  /**
   * Intent Extra to Manual Card CVV Data.
   */
  public static final String EXTRA_MANUAL_CARD_CVV = "clover.intent.extra.EXTRA_MANUAL_CARD_CVV";

  /**
   * Intent Extra to Manual Card Expiry Data.
   */
  public static final String EXTRA_MANUAL_CARD_EXPIRY = "clover.intent.extra.EXTRA_MANUAL_CARD_EXPIRY";


  //  The following extras are used by the PaymentRequestHandler
  /** {@link Boolean} automatically accept payment confirmations */
  public static final String EXTRA_AUTO_ACCEPT_PAYMENT_CONFIRMATIONS = "clover.intent.extra.AUTO_ACCEPT_PAYMENT_CONFIRMATIONS";
  /** {@link Boolean} automatically accept payment confirmations */
  public static final String EXTRA_AUTO_ACCEPT_SIGNATURE = "clover.intent.extra.AUTO_ACCEPT_SIGNATURE";
  /** {@link Boolean} skip the display of the receipt screen */
  public static final String EXTRA_SKIP_RECEIPT_SCREEN = "clover.intent.extra.SKIP_RECEIPT_SCREEN";
  /** {@link Boolean} force the payment to be taken offline even if the device is online */
  public static final String EXTRA_FORCE_OFFLINE_PAYMENT = "clover.intent.extra.FORCE_OFFLINE_PAYMENT";
  /** {@link Boolean} don't prompt for confirmation of an offline payment */
  public static final String EXTRA_APPROVE_OFFLINE_PAYMENT_WITHOUT_PROMPT = "clover.intent.extra.APPROVE_OFFLINE_PAYMENT_WITHOUT_PROMPT";
  /** {@link Boolean} allow offline payments even if the merchant isn't configured to accept them */
  public static final String EXTRA_ALLOW_OFFLINE_PAYMENT = "clover.intent.extra.ALLOW_OFFLINE_PAYMENT";
  /** the location for the customer signature */
  public static final String EXTRA_SIGNATURE_LOCATION = "clover.intent.extra.SIGNATURE_LOCATION";

  /** Result of C-Token requested part of Transaction Type link #TRANSACTION_TYPE_PAYMENT} or {@link #TRANSACTION_TYPE_AUTH}
   * {@link #TRANSACTION_TYPE_TOKENIZE_CARD} see {@link com.clover.sdk.v3.payments.TokenizeCardResponse} */
  public static final String EXTRA_C_TOKEN_RESULT = "clover.intent.extra.C_TOKEN_RESULT";
  /** Request an C-Token part of Transaction Type link #TRANSACTION_TYPE_PAYMENT} or {@link #TRANSACTION_TYPE_AUTH}
   * or {@link #TRANSACTION_TYPE_TOKENIZE_CARD}. A Valid Api access token must be set on {@link com.clover.sdk.v3.payments.TokenizeCardRequest} which can be
   * obtained from the merchant dashboard */
  public static final String EXTRA_C_TOKEN_REQUEST = "clover.intent.extra.C_TOKEN_REQUEST";

  public static final String EXTRA_NO_TIP = "clover.intent.extra.EXTRA_NO_TIP";
}
