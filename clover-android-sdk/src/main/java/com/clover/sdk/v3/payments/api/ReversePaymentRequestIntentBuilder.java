package com.clover.sdk.v3.payments.api;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.clover.sdk.v1.Intents;

/**
 * Use the ReversePaymentIntentBuilder class to initiate a payment reversal (void or refund)
 */
public class ReversePaymentRequestIntentBuilder extends BaseIntentBuilder {
    private String paymentId;
    private Long amount;

    private ReversePaymentRequestIntentBuilder(){}

    /**
     * Creates an instance of the ReversePaymentIntentBuilder class
     * @param paymentId
     */
    public ReversePaymentRequestIntentBuilder(String paymentId) {
        this.paymentId = paymentId;
    }

    /**
     * Sets the amount to be refunded
     * @param amount
     * @return ReversePaymentIntentBuilder object with new amount
     */
    public ReversePaymentRequestIntentBuilder amount(Long amount) {
        this.amount = amount;
        return this;
    }

    /**
     * Builder method to create an Intent to be use by Integrator POS to initiate a payment reversal.
     * @param context
     * @return
     */
    public Intent build(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Activity context must be populated with a non null value");
        }
        if (paymentId == null) {
            throw new IllegalArgumentException("paymentId must be populated with a non null value");
        }
        Intent i = super.build(context);
        i.setComponent(new ComponentName("com.clover.payment.builder.pay", "com.clover.payment.builder.pay.handler.ReversePaymentRequestHandler"));
        i.putExtra(Intents.EXTRA_PAYMENT_ID, paymentId);

        if (amount != null) {
            i.putExtra(Intents.EXTRA_AMOUNT, amount);
        }
        return i;
    }
}
