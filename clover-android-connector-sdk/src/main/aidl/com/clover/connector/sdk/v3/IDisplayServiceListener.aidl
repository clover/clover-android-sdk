package com.clover.connector.sdk.v3;

/**
 *  Functional implementation of Display Control.
 *
 *  Part of the DisplayConnector architecture.
 *  In the clover-android-sdk repo, the com.clover.connector.sdk.v3.DisplayConnector
 *    uses a com.clover.connector.sdk.v3.DisplayV3Connector to connect to an aidl service
 *    located in the payments repo using the com.clover.connector.sdk.v3.DisplayIntent#ACTION_DISPLAY_SERVICE_V3
 *    ("com.clover.intent.action.DISPLAY_SERVICE_V3") intent action.
 *
 *  In the payments repo, the com.clover.payment.display.DisplayService is started by the
 *    ACTION_DISPLAY_SERVICE_V3 intent.  The DisplayService creates a com.clover.payment.v3.DisplayBinderV3
 *    during binding, which creates the com.clover.payment.v3.DisplayBinderImpl instance.  The
 *    DisplayBinderImpl contains the functionality to control the display.  It does this via
 *    broadcasts that are received by the com.clover.payment.display.PaymentDisplayService.OrderPaymentEventsReceiver
 *    which sends the broadcast intent to com.clover.payment.display.PaymentDisplayService#onStartCommand(android.content.Intent, int, int).
 *    The PaymentDisplayService interprets the intent, and displays a message on the display(s) it controls,
 *    or the intent may (en/dis)able display of orders created on the device.
 *
 *  This is a placeholder.  We expect to enhance the interface to have callbacks for what was displayed.
 */
interface IDisplayServiceListener {
}

