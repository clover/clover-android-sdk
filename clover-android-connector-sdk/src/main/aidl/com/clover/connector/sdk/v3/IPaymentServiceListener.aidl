package com.clover.connector.sdk.v3;

import com.clover.sdk.v3.remotepay.AuthResponse;
import com.clover.sdk.v3.remotepay.CapturePreAuthResponse;
import com.clover.sdk.v3.remotepay.CloseoutResponse;
import com.clover.sdk.v3.remotepay.ConfirmPaymentRequest;
import com.clover.sdk.v3.remotepay.ManualRefundResponse;
import com.clover.sdk.v3.remotepay.PreAuthResponse;
import com.clover.sdk.v3.remotepay.ReadCardDataResponse;
import com.clover.sdk.v3.remotepay.RefundPaymentResponse;
import com.clover.sdk.v3.remotepay.RetrievePaymentResponse;
import com.clover.sdk.v3.remotepay.RetrievePendingPaymentsResponse;
import com.clover.sdk.v3.remotepay.SaleResponse;
import com.clover.sdk.v3.remotepay.TipAdded;
import com.clover.sdk.v3.remotepay.TipAdjustAuthResponse;
import com.clover.sdk.v3.remotepay.VaultCardResponse;
import com.clover.sdk.v3.remotepay.VerifySignatureRequest;
import com.clover.sdk.v3.remotepay.VoidPaymentResponse;
import com.clover.sdk.v3.remotepay.VoidPaymentRefundResponse;

interface IPaymentServiceListener {

  /**
   * Called in response to a pre auth request
   *
   * @param response
   */
  void onPreAuthResponse(in PreAuthResponse response);

  /**
   * Called in response to an auth request
   *
   * @param response
   */
  void onAuthResponse(in AuthResponse response);

  /**
   * Called in response to a tip adjust of an auth payment
   *
   * @param response
   */
  void onTipAdjustAuthResponse(in TipAdjustAuthResponse response);

  /**
   * Called in response to a capture of a pre auth payment
   *
   * @param response
   */
  void onCapturePreAuthResponse(in CapturePreAuthResponse response);

  /**
   * Called when the Clover device requires a signature to be verified
   *
   * @param request
   */
  void onVerifySignatureRequest(in VerifySignatureRequest request);

  /**
   * Called when the Clover device requires confirmation for a payment
   * e.g. Duplicates or Offline
   *
   * @param request
   */
  void onConfirmPaymentRequest(in ConfirmPaymentRequest request);

  /**
   * Called in response to a sale request
   *
   * @param response
   */
  void onSaleResponse(in SaleResponse response);

  /**
   * Called in response to a manual refund request
   *
   * @param response
   */
  void onManualRefundResponse(in ManualRefundResponse response);

  /**
   * Called in response to a refund payment request
   *
   * @param response
   */
  void onRefundPaymentResponse(in RefundPaymentResponse response);

  /**
   * Called when a customer selects a tip amount on the Clover device screen
   *
   * @param tipAdded
   */
  void onTipAdded(in TipAdded tipAdded);

  /**
   * Called in response to a void payment request
   *
   * @param response
   */
  void onVoidPaymentResponse(in VoidPaymentResponse response);

  /**
   * Called in response to a vault card request
   *
   * @param response
   */
  void onVaultCardResponse(in VaultCardResponse response);

  /**
   * Called in response to a retrievePendingPayment(...) request.
   * @param retrievePendingPaymentResponse
   */
  void onRetrievePendingPaymentsResponse(in RetrievePendingPaymentsResponse retrievePendingPaymentResponse);

  /**
   * Called in response to a readCardData(...) request.
   * @param response
   */
  void onReadCardDataResponse(in ReadCardDataResponse response);

 /**
   * Called in response to a RetrievePaymentRequest
   *
   * @param response The response
   */
  void onRetrievePaymentResponse(in RetrievePaymentResponse response);

  /**
   * Called in response to a closeout being processed
   *
   * @param response The response
   */
  void onCloseoutResponse(in CloseoutResponse response);

  /**
     * Called in response to a VoidPaymentRefundRequest
     *
     * @param response The response
     */
  void onVoidPaymentRefundResponse(in VoidPaymentRefundResponse response);
}

