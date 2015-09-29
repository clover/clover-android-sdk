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
  public static final String ACTION_PAY = "clover.intent.action.PAY";
  public static final String ACTION_CLOVER_PAY = "com.clover.intent.action.PAY";
  public static final String ACTION_REFUND = "clover.intent.action.REFUND";
  public static final String ACTION_STORE_CREDIT = "clover.intent.action.STORE_CREDIT";
  public static final String ACTION_MODIFY_ORDER = "clover.intent.action.MODIFY_ORDER";
  public static final String ACTION_MODIFY_AMOUNT = "clover.intent.action.MODIFY_AMOUNT";
  public static final String ACTION_MANUAL_PAY = "clover.intent.action.MANUAL_PAY";
  public static final String ACTION_MANUAL_REFUND = "clover.intent.action.MANUAL_REFUND";
  public static final String ACTION_BILL_SPLIT = "com.clover.intent.action.BILL_SPLIT";

  // Use this intent action to start App Market's App Detail screen
  public static final String ACTION_START_APP_DETAIL = "clover.intent.action.START_APP_DETAIL";

  // add tip - merchant facing
  public static final String ACTION_ADD_TIP = "clover.intent.action.ADD_TIP";

  // add tip - customer facing
  public static final String ACTION_CUSTOMER_ADD_TIP = "clover.intent.action.CUSTOMER_ADD_TIP";

  /** use this to start Closeout app */
  public static final String ACTION_CLOSEOUT = "clover.intent.action.CLOSEOUT";

  // launch transaction detail
  public static final String ACTION_START_TRANSACTION_DETAIL = "clover.intent.action.START_TRANSACTION_DETAIL";

  public static final String EXTRA_TRANSACTION = "clover.intent.extra.TRANSACTION";

  public static final String EXTRA_ORDER_ID = "clover.intent.extra.ORDER_ID";
  public static final String EXTRA_ORDER = "clover.intent.extra.ORDER";
  public static final String EXTRA_CLOVER_ORDER_ID = "com.clover.intent.extra.ORDER_ID";
  public static final String EXTRA_AMOUNT = "clover.intent.extra.AMOUNT";
  public static final String EXTRA_CURRENCY = "clover.intent.extra.CURRENCY";
  public static final String EXTRA_MERCHANT_ID = "clover.intent.extra.MERCHANT_ID";
  public static final String EXTRA_PAYMENT_ID = "clover.intent.extra.PAYMENT_ID";
  public static final String EXTRA_PAYMENT_IDS = "clover.intent.extra.PAYMENT_IDS";
  public static final String EXTRA_NOTE = "clover.intent.extra.NOTE";
  public static final String EXTRA_LINE_ITEM_IDS = "clover.intent.extra.LINE_ITEM_IDS";
  public static final String EXTRA_LINE_ITEM_PAYMENTS = "clover.intent.extra.LINE_ITEM_PAYMENTS";
  public static final String EXTRA_DISABLED_TENDER_IDS = "clover.intent.extra.DISABLED_TENDER_IDS";

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

  /**
   * start Activity to enter pin number for requested role
   * <p>Input:
   * <li>{@link #EXTRA_ROLE} requested {@link com.clover.sdk.v3.employees.AccountRole}. Could be admin, manager or employee.
   * <li>{@link #EXTRA_TITLE} String custom title (optional)
   *
   * <p>Output: The returned result code will be {@link android.app.Activity#RESULT_OK} on success
   */
  public static final String ACTION_REQUEST_ROLE = "com.clover.intent.action.REQUEST_ROLE";

  /**
   * Used as a {@link com.clover.sdk.v3.employees.AccountRole} extra field with {@link #ACTION_REQUEST_ROLE}
   */
  public static final java.lang.String EXTRA_ROLE = "com.clover.intent.extra.ROLE";

  /**
   * Used as a String extra field with {@link #ACTION_REQUEST_ROLE}
   */
  public static final java.lang.String EXTRA_TITLE = "com.clover.intent.extra.TITLE";


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

  public static final String EXTRA_CURRENT_SHIFT = "clover.intent.extra.CURRENT_SHIFT";

  /**
   * Action to start a secure payment flow on devices that support it.
   */
  public static final String ACTION_SECURE_PAY = "clover.intent.action.START_SECURE_PAYMENT";
  /**
   * Action to securely capture card data on devices that support it.
   */
  public static final String ACTION_SECURE_CARD_DATA = "clover.intent.action.START_SECURE_CARD_DATA";

  /**
   * Action to start a customer-facing tender.
   *
   * The following extras are passed,
   * <ul>
   * <li>{@link #EXTRA_AMOUNT}, the transaction amount, a {@link java.lang.Long}</li>
   * <li>{@link #EXTRA_CURRENCY}, the transaction currency, a {@link java.util.Currency}</li>
   * <li>{@link #EXTRA_TAX_AMOUNT}, the transaction's tax amount, a {@link java.lang.Long}</li>
   * <li>{@link #EXTRA_TAXABLE_AMOUNTS}, transaction's taxable amounts, a {@link java.util.List} of {@link com.clover.sdk.v3.payments.TaxableAmountRate}</li>
   * <li>{@link #EXTRA_SERVICE_CHARGE_AMOUNT}, the transactions' service charge amount, a {@link java.lang.Long}</li>
   * <li>{@link #EXTRA_ORDER_ID}, the Clover order ID, a {@link java.lang.String}</li>
   * <li>{@link #EXTRA_EMPLOYEE_ID}, the ID of the employee which initiated the payment, a {@link java.lang.String}</li>
   * <li>{@link #EXTRA_TENDER}, the tender for the transaction, a {@link com.clover.sdk.v3.base.Tender}
   * </ul>
   *
   * Result data must include the following,
   * <ul>
   * <li>a (Required) {@link #EXTRA_AMOUNT}</li>, the approved transaction amount, a {@link java.lang.Long}
   * <li>a (Optional) {@link #EXTRA_CLIENT_ID}, the client ID / external payment ID, a {@link java.lang.String}</li>
   * <li>a (Optional) {@link #EXTRA_NOTE}, the payment note, a {@link java.lang.String}</li>
   * </ul>
   *
   * @see #ACTION_MERCHANT_TENDER
   * @see #META_CUSTOMER_TENDER_IMAGE
   */
  public static final String ACTION_CUSTOMER_TENDER = "clover.intent.action.CUSTOMER_TENDER";

  /**
   * Action to start a merchant-facing tender.
   *
   * The following extras are passed,
   * <ul>
   * <li>{@link #EXTRA_AMOUNT}, the transaction amount, a {@link java.lang.Long}</li>
   * <li>{@link #EXTRA_CURRENCY}, the transaction currency, a {@link java.util.Currency}</li>
   * <li>{@link #EXTRA_TAX_AMOUNT}, the transaction's tax amount, a {@link java.lang.Long}</li>
   * <li>{@link #EXTRA_TAXABLE_AMOUNTS}, transaction's taxable amounts, a {@link java.util.List} of {@link com.clover.sdk.v3.payments.TaxableAmountRate}</li>
   * <li>{@link #EXTRA_SERVICE_CHARGE_AMOUNT}, the transactions' service charge amount, a {@link java.lang.Long}</li>
   * <li>{@link #EXTRA_ORDER_ID}, the Clover order ID, a {@link java.lang.String}</li>
   * <li>{@link #EXTRA_EMPLOYEE_ID}, the ID of the employee which initiated the payment, a {@link java.lang.String}</li>
   * <li>{@link #EXTRA_TENDER}, the tender for the transaction, a {@link com.clover.sdk.v3.base.Tender}
   * <li>{@link #EXTRA_ORDER}, the order for the transaction, a {@link com.clover.sdk.v3.order.Order}
   * <li>{@link #EXTRA_NOTE}, the order note for the transaction, a {@link java.lang.String}
   * </ul>
   *
   * Result data must include the following,
   * <ul>
   * <li>a (Required) {@link #EXTRA_AMOUNT}</li>, the approved transaction amount, a {@link java.lang.Long}
   * <li>a (Optional) {@link #EXTRA_CLIENT_ID}, the client ID / external payment ID, a {@link java.lang.String}</li>
   * <li>a (Optional) {@link #EXTRA_NOTE}, the payment note, a {@link java.lang.String}</li>
   * </ul>
   *
   * @see #ACTION_CUSTOMER_TENDER
   * @see #META_CUSTOMER_TENDER_IMAGE
   */
  public static final String ACTION_MERCHANT_TENDER = "clover.intent.action.MERCHANT_TENDER";

  /** A long */
  public static final String EXTRA_TAX_AMOUNT = "clover.intent.extra.TAX_AMOUNT";
  /** A {@link com.clover.sdk.v3.payments.ServiceChargeAmount} */
  public static final String EXTRA_SERVICE_CHARGE_AMOUNT = "clover.intent.extra.SERVICE_CHARGE_AMOUNT";
  /** An ArrayList of {@link com.clover.sdk.v3.payments.TaxableAmountRate} for this payment */
  public static final String EXTRA_TAXABLE_AMOUNTS = "clover.intent.extra.TAXABLE_AMOUNTS";
  /** A int of bitwise-ored CARD_ENTRY_METHOD values, when not included in the intent all methods will be allowed */
  public static final String EXTRA_CARD_ENTRY_METHODS = "clover.intent.extra.CARD_ENTRY_METHODS";
  /** A boolean, set to disable cashback option during payment */
  public static final String EXTRA_DISABLE_CASHBACK = "clover.intent.extra.DISABLE_CASHBACK";
  /** A String */
  public static final String EXTRA_VOICE_AUTH_CODE = "clover.intent.extra.VOICE_AUTH_CODE";
  /** A boolean, true if this is just a test transaction */
  public static final String EXTRA_IS_TESTING = "clover.intent.extra.IS_TESTING";
  /** A required String, one of TRANSACTION_TYPE values */
  public static final String EXTRA_TRANSACTION_TYPE = "clover.intent.extra.TRANSACTION_TYPE";
  /** A value for EXTRA_TRANSACTION_TYPE */
  public static final String TRANSACTION_TYPE_PAYMENT = "payment";
  /** A value for EXTRA_TRANSACTION_TYPE */
  public static final String TRANSACTION_TYPE_CREDIT = "credit";
  /** A value for EXTRA_TRANSACTION_TYPE */
  public static final String TRANSACTION_TYPE_AUTH = "auth";
  /** A value for EXTRA_TRANSACTION_TYPE */
  public static final String TRANSACTION_TYPE_CARD_DATA = "cardData";
  /** A value for EXTRA_TRANSACTION_TYPE */
  public static final String TRANSACTION_TYPE_BALANCE_INQUIRY = "balanceInquiry";
  /** A boolean, card not present, used during manual card entry */
  public static final String EXTRA_CARD_NOT_PRESENT = "clover.intent.extra.CARD_NOT_PRESENT";
  /** A String */
  public static final String EXTRA_AVS_STREET_ADDRESS = "clover.intent.extra.AVS_STREET_ADDRESS";
  /** A String */
  public static final String EXTRA_AVS_POSTAL_CODE = "clover.intent.extra.AVS_POSTAL_CODE";
  /** A PaymentRequestCardDetails */
  public static final String EXTRA_CARD_DATA = "cardData";
  /** A String, the message to prompt the user */
  public static final String EXTRA_CARD_DATA_MESSAGE = "cardDataMessage";
  /** A long, the pre-selected cash back amount */
  public static final String EXTRA_CASHBACK_AMOUNT = "cashbackAmount";
  /** A boolean, whether printing will be performed remotely */
  public static final String EXTRA_REMOTE_PRINT = "remotePrint";
  /** A String, the transaction number */
  public static final String EXTRA_TRANSACTION_NO = "transactionNo";
  /** A boolean, force pin entry on swipe */
  public static final String EXTRA_FORCE_SWIPE_PIN_ENTRY = "forceSwipePinEntry";
  /** A String, an error string. */
  public static final String EXTRA_DECLINE_REASON = "clover.intent.extra.DECLINE_REASON";
  /** A {@link com.clover.sdk.v3.base.Tender} */
  public static final String EXTRA_TENDER = "clover.intent.extra.TENDER";

  public static final String EXTRA_CARD_NUMBER = "clover.intent.extra.CARD_NUMBER";
  public static final String EXTRA_IGNORE_PAYMENT = "clover.intent.extra.IGNORE_PAYMENT";
  public static final String EXTRA_GIFT_CARD_RESPONSE = "clover.intent.extra.GIFT_CARD_RESPONSE";
  public static final String EXTRA_FAILURE_MESSAGE ="clover.intent.extra.FAILURE_MESSAGE";
  public static final String ACTION_GIFT_CARD_TX = "clover.intent.action.GIFT_CARD_TX";

  /** A bit value for EXTRA_CARD_ENTRY_METHODS, can be bitwise-ored with other CARD_ENTRY_METHOD values */
  public static final int CARD_ENTRY_METHOD_MAG_STRIPE = 0x01;
  /** A bit value for EXTRA_CARD_ENTRY_METHODS, can be bitwise-ored with other CARD_ENTRY_METHOD values */
  public static final int CARD_ENTRY_METHOD_ICC_CONTACT = 0x02;
  /** A bit value for EXTRA_CARD_ENTRY_METHODS, can be bitwise-ored with other CARD_ENTRY_METHOD values */
  public static final int CARD_ENTRY_METHOD_NFC_CONTACTLESS = 0x04;
  /** A bit value for EXTRA_CARD_ENTRY_METHODS, can be bitwise-ored with other CARD_ENTRY_METHOD values */
  public static final int CARD_ENTRY_METHOD_MANUAL = 0x08;
  /** A bit value for EXTRA_CARD_ENTRY_METHODS, all card entry methods. */
  public static final int CARD_ENTRY_METHOD_ALL = CARD_ENTRY_METHOD_MAG_STRIPE | CARD_ENTRY_METHOD_ICC_CONTACT | CARD_ENTRY_METHOD_NFC_CONTACTLESS | CARD_ENTRY_METHOD_MANUAL;

  /**
   * A {@link com.clover.sdk.v3.payments.Payment} returned by secure payment
   */
  public static final String EXTRA_PAYMENT = "clover.intent.extra.PAYMENT";
  public static final String EXTRA_PAYMENTS = "clover.intent.extra.PAYMENTS";
  public static final String EXTRA_VOIDED_PAYMENTS = "clover.intent.extra.VOIDED_PAYMENTS";

  /**
   * A {@link com.clover.sdk.v3.payments.Credit} returned by secure payment
   */
  public static final String EXTRA_CREDIT = "clover.intent.extra.CREDIT";
  /**
   * A {@link com.clover.sdk.v3.payments.Refund}
   */
  public static final String EXTRA_REFUND = "clover.intent.extra.REFUND";
  /**
   * A {@link com.clover.sdk.v3.payments.Authorization}
   */
  public static final String EXTRA_AUTHORIZATION = "clover.intent.extra.AUTHORIZATION";
  public static final String EXTRA_SHOW_REMAINING = "clover.intent.extra.SHOW_REMAINING";
  public static final String EXTRA_SHOW_VOID_BUTTON = "clover.intent.extra.SHOW_VOID_BUTTON";
  public static final String EXTRA_SIGNATURE_VERIFIED = "clover.intent.extra.SIGNATURE_VERIFIED";

  /**
   * print receipt extras
   */
  public static final String EXTRA_PRINT_RECEIPT_ONLY = "clover.intent.extra_PRINT_RECEIPT_ONLY";
  public static final String EXTRA_RECEIPT_FLAG = "clover.intent.extra.RECEIPT_FLAG";

  /**
   * Broadcast action indicating the active order in Register
   * Use with extra EXTRA_ORDER_ID
   */
  public static final String ACTION_ACTIVE_REGISTER_ORDER = "clover.intent.action.ACTIVE_REGISTER_ORDER";

  /**
   * Broadcast action indicating the active order in Pay Activity
   * Use with extra EXTRA_ORDER_ID
   */
  public static final String ACTION_V1_ACTIVE_PAY_ORDER = "clover.intent.action.V1_ACTIVE_PAY_ORDER";

  /**
   * Broadcast action indicating that we are starting to build an order.
   */
  public static final String ACTION_V1_ORDER_BUILD_START = "clover.intent.action.V1_ORDER_BUILD_START";
  /**
   * Broadcast action indicating that we have stopped building an order.
   */
  public static final String ACTION_V1_ORDER_BUILD_STOP = "clover.intent.action.V1_ORDER_BUILD_STOP";
  /**
   * Broadcast action indicating that we are showing PayActivity.
   */
  public static final String ACTION_V1_PAY_BUILD_SHOW = "clover.intent.action.V1_PAY_BUILD_SHOW";
  /**
   * Broadcast action indicating that PayActivity was hidden.
   */
  public static final String ACTION_V1_PAY_BUILD_HIDE = "clover.intent.action.V1_PAY_BUILD_HIDE";
  /**
   * Broadcast action indicating that we are starting to build a payment.
   */
  public static final String ACTION_V1_PAY_BUILD_START = "clover.intent.action.V1_PAY_BUILD_START";
  /**
   * Broadcast action indicating that we have stopped building a payment.
   */
  public static final String ACTION_V1_PAY_BUILD_STOP = "clover.intent.action.V1_PAY_BUILD_STOP";
  /**
   * Broadcast action indicating that we have started executing a payment.
   */
  public static final String ACTION_V1_PAY_EXECUTE_START = "clover.intent.action.V1_PAY_EXECUTE_START";
  /**
   * Broadcast action indicating that we have stopped executing a payment.
   */
  public static final String ACTION_V1_PAY_EXECUTE_STOP = "clover.intent.action.V1_PAY_EXECUTE_STOP";

  public static final String EXTRA_AVAILABLE = "clover.intent.extra_AVAILABLE";

  public static final String EXTRA_SHOW_SEARCH = "clover.intent.extra_SHOW_SEARCH";

  /**
   * A drawable resource ID, the image to be displayed on the customer-facing tender button.
   */
  public static final String META_CUSTOMER_TENDER_IMAGE = "clover.intent.meta.CUSTOMER_TENDER_IMAGE";

  /**
   * A drawable resource ID, the image to be displayed on the merchant-facing tender button.
   */
  public static final String META_MERCHANT_TENDER_IMAGE = "clover.intent.meta.MERCHANT_TENDER_IMAGE";
}
