// IPaymentProcessingService.aidl
package com.clover.sdk.v3.pay.service;

import com.clover.sdk.v3.pay.PaymentRequest;
import com.clover.sdk.v3.pay.InitTransactionRequest;
import com.clover.sdk.v3.payments.PaymentResponse;
import com.clover.sdk.v3.payments.RefundRequest;
import com.clover.sdk.v3.payments.RefundResponse;
import com.clover.sdk.v3.payments.VoidRequest;
import com.clover.sdk.v3.pay.service.IPaymentCallback;
import com.clover.sdk.v3.pay.service.IInitTransactionCallback;
import com.clover.sdk.v3.pay.service.IRefundCallback;

/**
 * IPaymentProcessingService defines the contract for payment processing operations between Clover payment and external payment processing service.
 * This interface is designed to be implemented by payment processing services that provide support to process the payment
 * with there own gateway.
 * Implementations of this interface handle payment-related requests and responses,
 * enabling communication across process boundaries in the Clover payment application and external payment processing service.
 *
 * <h3>Related Classes</h3>
 * <ul>
 *   <li>{@link com.clover.sdk.v3.pay.PaymentRequest} - Used to request for a payment..</li>
 *   <li>{@link com.clover.sdk.v3.pay.PaymentRequestCardDetails} - Card details for the payment/refund request.</li>
 *   <li>{@link com.clover.sdk.v3.payments.VoidRequest} - Used to request a void for a payment.</li>
 *   <li>{@link com.clover.sdk.v3.payments.RefundRequest} - Used to request a refund for a payment.</li>
 *   <li>{@link com.clover.sdk.v3.payments.RefundResponse} - Response for a refund request.</li>
 *   <li>{@link com.clover.sdk.v3.payments.Refund} - Refund data object.</li>
 *   <li>{@link com.clover.sdk.v3.payments.Payment} - Payment data object.</li>
 * </ul>
 *  Version: 1.0
 */
interface IPaymentProcessingService {
    /**
     * The current version of this AIDL interface.
     * This constant is used for version checking to ensure compatibility.
     */
    const String VERSION = "1.1";
    /**
     * Initiates a final sale for a payment transaction.
     * This will be an asynchronous, time-bound operation.
     * If the operation does not complete within the specified time (e.g., 30 seconds), it will be considered failed.
     * After a successful payment authorization, the payment is considered complete and settled.
     * In case of failure or timeout, the payment will be voided to clean up any partial transactions.
     *
     * @param paymentRequest The details of the payment to be processed, including amount, payment method, and other metadata.
     * @param callback       The callback interface to receive the result of the sale operation.
     */
    void sale(in PaymentRequest paymentRequest, in IPaymentCallback callback);

    /**
     * Initiates an authorization for a payment transaction.
     * This will be an asynchronous, time-bound operation.
     * If the operation does not complete within the specified time (e.g., 30 seconds), it will be considered failed.
     * After a successful payment authorization, the payment is considered authorized but not yet settled.
     * Payment must be captured to complete the transaction and settle the payment.
     * In case of failure or timeout, the payment will be voided to clean up any partial transactions.
     *
     * @param paymentRequest The details of the payment to be processed, including amount, payment method, and other metadata.
     * @param callback       The callback interface to receive the result of the authorization operation.
     */
    void auth(in PaymentRequest paymentRequest, in IPaymentCallback callback);

    /**
     * Initiates a capture request to process a previously authorized transaction.
     * This will be an asynchronous, time-bound operation.
     * If the operation does not complete within the specified time (e.g., 30 seconds), it will be considered failed.
     * After this operation is successful, the payment is considered complete and settled.
     * In case of failure or timeout, user have to retry capture operation.
     *
     * @param paymentRequest The details for the capture, including the authorized payment ID.
     * @param callback       The callback interface to receive the result of the capture operation.
     */
    void capture(in PaymentRequest paymentRequest, in IPaymentCallback callback);

    /**
     * Cancels an ongoing transaction.
     * This can be triggered by user action or a timeout event.
     *
     * @param paymentId The unique ID of the transaction to be canceled.
     */
    oneway void cancel(in String paymentId);

    /**
     * Voids a previously completed payment.
     * This is used to reverse a payment, typically due to systemn errors, timeout faliure, customer requests, or other business logic.
     * Preferaly payment processing service should maintain queue for the void requests and process them when the payment gateway is reachable.
     * Service should be designed to handle void requests with potential processing delays due to network issues or payment gateway unavailability
     * Service should respond with a success status upon successfully adding the void request to a queue.
     * In case of failure, clover payment application will retry for twice before giving up. Transaction status will be in disputed state if all retries are exhausted.
     *
     * @param voidRequest The detail pf the void payment to be processed includeing, paymentId, orderId void reason and other metadata.
     * @return true if the payment service queued the void request, false otherwise.
     */
    boolean voidPayment(in VoidRequest voidRequest, in android.os.Bundle extras);

    /**
     * Processes a refund for a completed payment.
     * This will be an asynchronous, time-bound operation.
     * If the operation does not complete within the specified time (e.g., 30 seconds), it will be considered failed.
     * In case of failure or timeout, the payment will be voided to clean up any partial transactions..
     *
     * @param refundRequest The details of the refund, including amount, payment reference, and reason.
     * @param callback      The callback interface to receive the result of the refund operation.
     */
    void refund(in RefundRequest refundRequest, in IRefundCallback callback);


    /**
     * Gets the current version of the implemented payment service.
     * @return The string version code of the service.
     */
    String getServiceVersion();

    /**
     * Initializes a transaction with card data (SRED, KSN) and returns transaction information from the payment gateway.
     * This is the first step after card data is read, before the sale is processed.
     *
     * @param initTransactionRequest    The detail of init transaction with SRED, KSN from the card.
     * @param callback                  The callback interface to receive the result, including transactionId, payment method, and extra data.
     *
     * <p>Related Classes:</p>
     * <ul>
     *   <li>{@link com.clover.sdk.v3.pay.InitTransactionRequest} - Contains transaction initlize data.</li>
     *   <li>{@link com.clover.sdk.v3.pay.service.IInitTransactionCallback} - Callback for initTransaction result.</li>
     * </ul>
     */
    void initTransaction(in InitTransactionRequest initTransactionRequest, in IInitTransactionCallback callback);

    /**
     * Completes the transaction after the second card action (e.g., after TC and DOL are generated).
     * This is called after the payment is authorized and the issuer data is processed.
     *
     * @param paymentRequest The details for completing the transaction, including TC, DOL, and any issuer data.
     * @param callback       The callback interface to receive the result of the completion operation.
     *
     * <p>Related Classes:</p>
     * <ul>
     *   <li>{@link com.clover.sdk.v3.pay.PaymentRequest} - Contains transaction completion data.</li>
     *   <li>{@link com.clover.sdk.v3.payments.PaymentResponse} - Response for transaction completion.</li>
     *   <li>{@link com.clover.sdk.v3.pay.service.IPaymentCallback} - Callback for completion result.</li>
     * </ul>
     */
    void completeTx(in PaymentRequest paymentRequest, in IPaymentCallback callback);
}