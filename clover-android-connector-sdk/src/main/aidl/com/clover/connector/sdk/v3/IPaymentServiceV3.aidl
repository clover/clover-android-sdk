package com.clover.connector.sdk.v3;

import com.clover.connector.sdk.v3.IPaymentServiceListener;
import com.clover.sdk.v3.apps.AppTracking;
import com.clover.sdk.v3.base.Challenge;
import com.clover.sdk.v3.payments.Payment;
import com.clover.sdk.v3.remotepay.AuthRequest;

import com.clover.sdk.v3.remotepay.CapturePreAuthRequest;
import com.clover.sdk.v3.remotepay.CloseoutRequest;


import com.clover.sdk.v3.remotepay.ManualRefundRequest;
import com.clover.sdk.v3.remotepay.PreAuthRequest;
import com.clover.sdk.v3.remotepay.ReadCardDataRequest;
import com.clover.sdk.v3.remotepay.RefundPaymentRequest;
import com.clover.sdk.v3.remotepay.RetrievePaymentRequest;
import com.clover.sdk.v3.remotepay.SaleRequest;
import com.clover.sdk.v3.remotepay.TipAdjustAuthRequest;
import com.clover.sdk.v3.remotepay.VerifySignatureRequest;
import com.clover.sdk.v3.remotepay.VoidPaymentRequest;
import com.clover.sdk.v1.configuration.UIConfiguration;
import com.clover.sdk.v3.remotepay.VoidPaymentRefundRequest;

/**
 * This service mirrors the functionality of {@link IOrderService} but uses a different
 * mechanism for trasferring Clover SDK objects. Specifically, as can be seen from
 * the interface definition below, CloverSDK objects are transferred wrapped by a
 * {@link FdParcelable}. This includes SDK objects passed to the service and objects
 * returned from the server.
 * <p/>
 * For example, to create an FD parcelablable input for an {@link Order} argument,
 * <pre>
 *   {@code
 *   OrderFdParcelable fdo = new OrderFdParcelable(order);
 *   service.updateOrder(fdo, ...);
 *   }
 * </pre>
 * <p/>
 * To obtain an Order object return value from an FD parcelable,
 * <pre>
 *   {@code
 *   OrderFdParcelable fdo = service.getOrder(...);
 *   Order order = fdo.getValue();
 *   }
 * </pre>
 */
interface IPaymentServiceV3 {
  /**
   * add an ICloverConnectorListener to receive callbacks
   *
   * @param listener
   */
  void addPaymentServiceListener(in IPaymentServiceListener listener);

  /**
   * remove an ICloverConnectorListener from receiving callbacks
   *
   * @param listener
   */
  void removePaymentServiceListener(in IPaymentServiceListener listener);

  /**
   * Sale method, aka "purchase"
   *
   * @param saleRequest    - A SaleRequest object containing basic information needed for the transaction
   */
  void sale(in SaleRequest saleRequest);

  /**
   * If signature is captured during a Sale, this method accepts the signature as entered
   *
   * @param request -
   **/
  void acceptSignature(in VerifySignatureRequest request);

  /**
   * If signature is captured during a Sale, this method rejects the signature as entered
   *
   * @param request -
   **/
  void rejectSignature(in VerifySignatureRequest request);

  /**
   * If payment confirmation is required during a Sale, this method accepts the payment
   *
   * @param payment -
   **/
  void acceptPayment(in Payment payment);

  /**
   * If payment confirmation is required during a Sale, this method rejects the payment
   *
   * @param payment   -
   * @param challenge -
   **/
  void rejectPayment(in Payment payment, in Challenge challenge);

  /**
   * Auth method to obtain an Auth payment that can be used as the payment
   * to call tipAdjust
   *
   * @param request -
   **/
  void auth(in AuthRequest request);

  /**
   * PreAuth method to obtain a Pre-Auth for a card
   *
   * @param request -
   **/
  void preAuth(in PreAuthRequest request);

  /**
   * Capture a previous Auth. Note: Should only be called if request's PaymentID is from an AuthResponse
   *
   * @param request -
   **/
  void capturePreAuth(in CapturePreAuthRequest request);

  /**
   * Adjust the tip for a previous Auth. Note: Should only be called if request's PaymentID is from an AuthResponse
   *
   * @param request -
   **/
  void tipAdjustAuth(in TipAdjustAuthRequest request);

  /**
   * Void a transaction, given a previously used order ID and/or payment ID
   *
   * @param request - A VoidRequest object containing basic information needed to void the transaction
   **/
  void voidPayment(in VoidPaymentRequest request);

  /**
   * Refund a specific payment
   *
   * @param request -
   **/
  void refundPayment(in RefundPaymentRequest request);

  /**
   * Manual refund method, aka "naked credit"
   *
   * @param request - A ManualRefundRequest object
   **/
  void manualRefund(in ManualRefundRequest request); // NakedRefund is a Transaction, with just negative amount

  /**
   * Vault card information. Requests the mini capture card information and request a payment token from the payment gateway.
   * The value returned in the response is a card, with all the information necessary to use for payment in a SaleRequest or AuthRequest
   *
   * @param cardEntryMethods - The card entry methods allowed to capture the payment token.
   **/
  void vaultCard(in int cardEntryMethods);

  /**
   * Used to request a list of pending payments that have been taken offline, but
   * haven't processed yet. will trigger an onRetrievePendingPaymentsResponse callback
   */
  void retrievePendingPayments();

  /**
   * Used to request card information. Specifically track1 and track2 information
   *
   * @param request - A ReadCardDataRequest object
   */
  void readCardData(in ReadCardDataRequest request);

  /**
   * Sends a request to get a payment.
   * Only valid for payments made in the past 24 hours on the device queried.
   *
   * @param request The request details
   */
  void retrievePayment(in RetrievePaymentRequest request);

  /**
   * Request a closeout of all orders.
   *
   * @param request The request details
   */
  void closeout(in CloseoutRequest request);

  /**
   * PRIVATE send calling application information to use for logging and analytics
   *
   * @param appTracking - An object containing calling application information needed for
   *                      analytics capture
   */
  void sendAppTracking(in AppTracking appTracking);

  /**
   * Set configuration object (e.g. theme)
   * @param configuration - An object containing custom configurations
   */
  void setUIConfiguration(in UIConfiguration configuration);

  /**
   * Send device logs
   * @param message - A string to distinguish log messages
   */
  void sendDebugLog(in String message);

  /**
   * Sends a request to void a payment refund
   *
   * @param request The request details
   */
  void voidPaymentRefund(in VoidPaymentRefundRequest request);

  /**
   * Used to reset the device if it gets in an invalid state from POS perspective.
   * This could cause a missed transaction or other missed information, so it
   * needs to be used cautiously as a last resort
   */
  void resetDevice();
}
