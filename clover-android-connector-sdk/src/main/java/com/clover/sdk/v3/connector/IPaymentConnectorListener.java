package com.clover.sdk.v3.connector;


import com.clover.sdk.v3.remotepay.AuthResponse;
import com.clover.sdk.v3.remotepay.CapturePreAuthResponse;
import com.clover.sdk.v3.remotepay.CloseoutResponse;
import com.clover.sdk.v3.remotepay.ConfirmPaymentRequest;
import com.clover.sdk.v3.remotepay.ManualRefundResponse;
import com.clover.sdk.v3.remotepay.MerchantInfo;
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
import com.clover.sdk.v3.remotepay.VoidPaymentRefundResponse;
import com.clover.sdk.v3.remotepay.VoidPaymentResponse;

public interface IPaymentConnectorListener extends IDeviceConnectorListener{

  /**
   * Called in response to a pre auth request
   *
   * @param response
   */
  void onPreAuthResponse(PreAuthResponse response);

  /**
   * Called in response to an auth request
   *
   * @param response
   */
  void onAuthResponse(AuthResponse response);

  /**
   * Called in response to a tip adjust of an auth payment
   *
   * @param response
   */
  void onTipAdjustAuthResponse(TipAdjustAuthResponse response);

  /**
   * Called in response to a capture of a pre auth payment
   *
   * @param response
   */
  void onCapturePreAuthResponse(CapturePreAuthResponse response);

  /**
   * Called when the Clover device requires a signature to be verified
   *
   * @param request
   */
  void onVerifySignatureRequest(VerifySignatureRequest request);

  /**
   * Called when the Clover device requires confirmation for a payment
   * e.g. Duplicates or Offline
   *
   * @param request
   */
  void onConfirmPaymentRequest(ConfirmPaymentRequest request);

  /**
   * Called in response to a sale request
   *
   * @param response
   */
  void onSaleResponse(SaleResponse response);

  /**
   * Called in response to a manual refund request
   *
   * @param response
   */
  void onManualRefundResponse(ManualRefundResponse response);

  /**
   * Called in response to a refund payment request
   *
   * @param response
   */
  void onRefundPaymentResponse(RefundPaymentResponse response);

  /**
   * Called when a customer selects a tip amount on the Clover device screen
   *
   * @param tipAdded
   */
  void onTipAdded(TipAdded tipAdded);

  /**
   * Called in response to a void payment request
   *
   * @param response
   */
  void onVoidPaymentResponse(VoidPaymentResponse response);

  /**
   * Called in response to a vault card request
   *
   * @param response
   */
  void onVaultCardResponse(VaultCardResponse response);

  /**
   * Called in response to a retrievePendingPayment(...) request.
   * @param retrievePendingPaymentResponse
   */
  void onRetrievePendingPaymentsResponse(RetrievePendingPaymentsResponse retrievePendingPaymentResponse);

  /**
   * Called in response to a readCardData(...) request.
   * @param response
   */
  void onReadCardDataResponse(ReadCardDataResponse response);

  /**
   * Called in response to a closeout being processed
   *
   * @param response
   */
  void onCloseoutResponse(CloseoutResponse response);

  /**
   * Called in response to a doRetrievePayment(...) request
   *
   * @param response
   */
  void onRetrievePaymentResponse(RetrievePaymentResponse response);

  /**
   * Called in response to a voidPaymentRefund(...) request
   *
   * @param response
   */
  void onVoidPaymentRefundResponse(VoidPaymentRefundResponse response);
}

