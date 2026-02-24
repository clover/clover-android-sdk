// IRefundCallback.aidl
package com.clover.sdk.v3.pay.service;

import com.clover.sdk.v3.payments.RefundResponse;
import com.clover.sdk.v3.pay.service.PaymentError;

/**
 * Callback interface for receiving the result of an asynchronous refund transaction.
 */
oneway interface IRefundCallback {

    /**
     * Called when a refund transaction with payment gateway sever and service has got response back.
     * This should be called for declined transactions as well, as long as the transaction was processed by the gateway.
     *
     * @param response The RefundResponse parcelable containing refund results.
     */
    void onRefundSuccess(in RefundResponse response);

    /**
     * Called when a refund transaction fails.
     * Void request will be sent on network failures, timeouts errors and unknown errors to clean up any partial transactions.
     *
     * @param errorCode    A standard error code from {@link PaymentError}.
     * @param errorMessage A human-readable message describing the error.
     */
    void onRefundFailure(int errorCode, String errorMessage);
}