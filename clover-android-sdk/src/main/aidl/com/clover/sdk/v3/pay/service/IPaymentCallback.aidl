// IPaymentCallback.aidl
package com.clover.sdk.v3.pay.service;

import com.clover.sdk.v3.payments.PaymentResponse;
import com.clover.sdk.v3.pay.service.PaymentError;

/**
 * Callback interface for receiving the result of an asynchronous payment transaction.
 */
oneway interface IPaymentCallback {

    /**
     * Called when a payment transaction with payment gateway sever and service has got response back.
     * This should be called for declined transactions as well, as long as the transaction was processed by the gateway.
     *
     * @param response The PaymentResponse parcelable containing transaction results.
     */
    void onTransactionSuccess(in PaymentResponse response);

    /**
     * Called when a transaction failed because the service had an error or the transaction could not be processed by the payment gateway server.
     * Void request will be send on network failures, timeouts errors and unknow error to clean up any partial transactions.
     *
     * @param errorCode    A standard error code from {@link PaymentError}.
     * @param errorMessage A human-readable message describing the error.
     */
    void onTransactionFailure(int errorCode, String errorMessage);
}