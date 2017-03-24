package com.clover.sdk.v3.connector;

import com.clover.sdk.v3.base.Challenge;
import com.clover.sdk.v3.payments.Payment;
import com.clover.sdk.v3.remotepay.AuthRequest;
import com.clover.sdk.v3.remotepay.CapturePreAuthRequest;
import com.clover.sdk.v3.remotepay.ManualRefundRequest;
import com.clover.sdk.v3.remotepay.PreAuthRequest;
import com.clover.sdk.v3.remotepay.ReadCardDataRequest;
import com.clover.sdk.v3.remotepay.RefundPaymentRequest;
import com.clover.sdk.v3.remotepay.SaleRequest;
import com.clover.sdk.v3.remotepay.TipAdjustAuthRequest;
import com.clover.sdk.v3.remotepay.VerifySignatureRequest;
import com.clover.sdk.v3.remotepay.VoidPaymentRequest;

import android.app.Activity;

/**
 * Created by glennbedwell on 9/6/16.
 */
public interface IPaymentConnector {
  /**
   * add an ICloverConnectorListener to receive callbacks
   *
   * @param listener
   */
  void addPaymentConnectorListener(IPaymentConnectorListener listener);

  /**
   * remove an ICloverConnectorListener from receiving callbacks
   *
   * @param listener
   */
  void removePaymentConnectorListener(IPaymentConnectorListener listener);

  /**
   * Sale method, aka "purchase"
   *
   * @param parentActivity - The activity from which the connector action is being called
   * @param saleRequest    - A SaleRequest object containing basic information needed for the transaction
   */
  void sale(Activity parentActivity, SaleRequest saleRequest);

  /**
   * If signature is captured during a Sale, this method accepts the signature as entered
   *
   * @param request -
   **/
  void acceptSignature(Activity parentActivity, VerifySignatureRequest request);

  /**
   * If signature is captured during a Sale, this method rejects the signature as entered
   *
   * @param request -
   **/
  void rejectSignature(Activity parentActivity, VerifySignatureRequest request);

  /**
   * If payment confirmation is required during a Sale, this method accepts the payment
   *
   * @param payment -
   **/
  void acceptPayment(Activity parentActivity, Payment payment);

  /**
   * If payment confirmation is required during a Sale, this method rejects the payment
   *
   * @param payment   -
   * @param challenge -
   **/
  void rejectPayment(Activity parentActivity, Payment payment, Challenge challenge);

  /**
   * Auth method to obtain an Auth payment that can be used as the payment
   * to call tipAdjust
   *
   * @param request -
   **/
  void auth(Activity parentActivity, AuthRequest request);

  /**
   * PreAuth method to obtain a Pre-Auth for a card
   *
   * @param request -
   **/
  void preAuth(Activity parentActivity, PreAuthRequest request);

  /**
   * Capture a previous Auth. Note: Should only be called if request's PaymentID is from an AuthResponse
   *
   * @param request -
   **/
  void capturePreAuth(CapturePreAuthRequest request);

  /**
   * Adjust the tip for a previous Auth. Note: Should only be called if request's PaymentID is from an AuthResponse
   *
   * @param request -
   **/
  void tipAdjustAuth(TipAdjustAuthRequest request);

  /**
   * Void a transaction, given a previously used order ID and/or payment ID
   *
   * @param request - A VoidRequest object containing basic information needed to void the transaction
   **/
  void voidPayment(VoidPaymentRequest request);

  /**
   * Refund a specific payment
   *
   * @param request -
   **/
  void refundPayment(RefundPaymentRequest request);

  /**
   * Manual refund method, aka "naked credit"
   *
   * @param request - A ManualRefundRequest object
   **/
  void manualRefund(Activity parentActivity, ManualRefundRequest request); // NakedRefund is a Transaction, with just negative amount

  /**
   * Vault card information. Requests the mini capture card information and request a payment token from the payment gateway.
   * The value returned in the response is a card, with all the information necessary to use for payment in a SaleRequest or AuthRequest
   *
   * @param cardEntryMethods - The card entry methods allowed to capture the payment token. null will provide default values
   **/
  void vaultCard(Activity parentActivity, Integer cardEntryMethods);

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
  void readCardData(Activity parentActivity, ReadCardDataRequest request);

}