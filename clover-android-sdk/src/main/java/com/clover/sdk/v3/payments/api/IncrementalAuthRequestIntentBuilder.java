package com.clover.sdk.v3.payments.api;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.clover.sdk.v1.Intents;

/**
 * Use the IncrementalAuthRequestIntentBuilder class to increment an existing Authorization/PreAuth.
 */
public class IncrementalAuthRequestIntentBuilder extends BaseIntentBuilder {
    private Long amount;
    private String paymentId;

    public IncrementalAuthRequestIntentBuilder(String paymentId, Long amount) {
        this.amount = amount;
        this.paymentId = paymentId;
    }

    public Intent build(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context must be populated with a non null value");
        }
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("amount cannot be null or less than 0");
        }
        if (paymentId == null) {
            throw new IllegalArgumentException("paymentId cannot be null");
        }

        Intent i = super.build(context);
        i.setComponent(new ComponentName("com.clover.payment.builder.pay", "com.clover.payment.builder.pay.handler.IncrementalAuthRequestHandler"));
        i.putExtra(Intents.EXTRA_AMOUNT, amount);
        i.putExtra(Intents.EXTRA_PAYMENT_ID, paymentId);

        return i;
    }
}
