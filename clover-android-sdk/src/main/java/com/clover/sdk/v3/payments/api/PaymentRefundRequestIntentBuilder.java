package com.clover.sdk.v3.payments.api;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.clover.sdk.v1.Intents;

/**
 * Use the PaymentRefundRequestIntentBuilder class to initiate a payment refund on an Android device.
 */
public class PaymentRefundRequestIntentBuilder extends BaseIntentBuilder {
    private String paymentId;
    private Long amount;

    private PaymentRefundRequestIntentBuilder() {}

    /**
     * Creates an instance of the PaymentRefundRequestIntentBuilder class
     * @param paymentId
     */
    public PaymentRefundRequestIntentBuilder(String paymentId) {
        this.paymentId = paymentId;
    }

    /**
     * Sets the amount to be refunded
     * @param amount
     * @return PaymentRefundRequestIntentBuilder object with new amount
     */
    public PaymentRefundRequestIntentBuilder amount(Long amount) {
        this.amount = amount;
        return this;
    }

    /**
     * Builder method to create an Intent to be used by Integrator POS to initiate a payment refund.
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
        i.setComponent(new ComponentName("com.clover.payment.builder.pay", "com.clover.payment.builder.pay.handler.PaymentRefundRequestHandler"));
        i.putExtra(Intents.EXTRA_PAYMENT_ID, paymentId);
        i.putExtra(Intents.EXTRA_AMOUNT, amount);

        return i;
    }
}
