// IDisplayServiceV3.aidl
package com.clover.connector.sdk.v3;

import com.clover.connector.sdk.v3.IDisplayServiceListener;
import com.clover.sdk.v3.order.DisplayOrder;

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
 *  This is the service interface definition
 *
 */
interface IDisplayServiceV3 {
  /**
   * add an ICloverConnectorListener to receive callbacks
   *
   * @param listener
   */
  void addDisplayServiceListener(in IDisplayServiceListener listener);

  /**
   * remove an ICloverConnectorListener from receiving callbacks
   *
   * @param listener
   */
  void removeDisplayServiceListener(in IDisplayServiceListener listener);

  /**
   * Show the welcome display on the device.
   */
  void showWelcomeScreen();

  /**
   * Show a text message on the device.
   *
   * @param  message
   */
  void showMessage(in String message);

  /**
   * Show the thank you display on the device.
   */
  void showThankYouScreen();

  /**
   * Display order information on the screen. Calls to this method will cause the DisplayOrder
   * to show on the clover device. If a DisplayOrder is already showing on the Clover device,
   * it will replace the existing DisplayOrder on the device.
   *
   * @param order
   */
  void showDisplayOrder(in DisplayOrder order);

  /**
   * Destroy the connector.  After this is called, the connection to the displays is severed, and this object is
   * no longer usable
   */
  void dispose();
}
