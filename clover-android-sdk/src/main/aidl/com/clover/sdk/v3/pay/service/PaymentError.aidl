// PaymentError.aidl
package com.clover.sdk.v3.pay.service;

/**
 * Defines a set of standard error codes for payment transactions.
 */
interface PaymentError {
    /**
     * An unknown error occurred.
     */
    const int UNKNOWN_ERROR = 1000;

    /**
     * An internal error occurred in the payment processing service.
     */
    const int INTERNAL_ERROR = 1001;

    /**
     * The payment gateway or network is unreachable.
     */
    const int NETWORK_FAILURE = 1002;

    /**
     * The transaction timed out.
     */
    const int TIMEOUT = 1003;

    /**
     * The provided request data is invalid.
     */
    const int INVALID_REQUEST = 1004;
}