// IInitTransactionCallback.aidl
package com.clover.sdk.v3.pay.service;


import com.clover.sdk.v3.payments.InitTransactionResponse;
import com.clover.sdk.v3.pay.service.PaymentError;

/**
 * Callback interface for receiving the result of an asynchronous payment transaction.
 */

interface IInitTransactionCallback {
     /**
     * Called when service has successfully initiated the transaction with payment gateway.
     *
     * @param response The InitTransactionResponse parcelable containing transaction Id and other data.
     */
    void onTransactionSuccess(in InitTransactionResponse response);

    /**
     * Called when service has failed to initiated the transaction with payment gateway.
     * Becuase of network error or some other scenrio.
     *
     * @param errorCode    A standard error code from {@link PaymentError}.
     * @param errorMessage A human-readable message describing the error.
     */
    void onTransactionFailure(int errorCode, String errorMessage);
}