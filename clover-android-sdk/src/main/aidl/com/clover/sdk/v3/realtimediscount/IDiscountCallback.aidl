package com.clover.sdk.v3.realtimediscount;

import com.clover.sdk.v3.realtimediscount.DiscountResponse;

/**
 * IDiscountCallback serves as the asynchronous communication channel between a requested
 * third-party Real-Time Discount provider and the Clover Payment system.
 *
 * <p><strong>Significance:</strong>
 * Because discount calculation might require network calls to external banking architectures,
 * it must be handled asynchronously to prevent blocking the Clover payment engine's main thread.
 * This callback interface ensures the standard flow gracefully pauses, awaits the provider's
 * discount decision via this interface, and subsequently resumes the processing logic.
 *
 * <p><strong>Usage:</strong>
 * The Clover system passes an instance of this callback to the
 * {@link IRealtimeDiscountProvider#getDiscount} and
 * {@link IRealtimeDiscountProvider#onPartialApproval} methods. The provider must call either
 * {@link #onResult} on success (even if the discount is 0) or {@link #onError} on failure.
 * Failure to invoke either method within the timeout threshold typically results in implicit
 * voiding of the transaction to protect merchant intent.
 */
interface IDiscountCallback {
    /**
     * Called when the discount request is processed successfully.
     *
     * @param response The response containing the discount details (amount, description, etc.).
     */
    void onResult(in DiscountResponse response);

    /**
     * Called when the discount request fails.
     *
     * @param errorCode An error code indicating the reason for failure. See 
     *                  {@link com.clover.sdk.v3.realtimediscount.DiscountErrorCode} for standard codes:
     *                  <br/> 1000: UNKNOWN_ERROR
     *                  <br/> 1001: INTERNAL_ERROR
     *                  <br/> 1002: NETWORK_FAILURE
     *                  <br/> 1003: TIMEOUT
     *                  <br/> 1004: INVALID_REQUEST
     * @param errorMessage A readable description of the error.
     */
    void onError(int errorCode, String errorMessage);
}
