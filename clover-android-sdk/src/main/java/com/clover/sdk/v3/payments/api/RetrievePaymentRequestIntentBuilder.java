package com.clover.sdk.v3.payments.api;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.clover.sdk.v1.Intents;

public class RetrievePaymentRequestIntentBuilder extends BaseIntentBuilder {
    private String externalPaymentId;
    private String paymentId;

    public RetrievePaymentRequestIntentBuilder() {}

    public RetrievePaymentRequestIntentBuilder externalPaymentId(String externalPaymentId) {
        this.externalPaymentId = externalPaymentId;
        return this;
    }

    public RetrievePaymentRequestIntentBuilder paymentId(String paymentId) {
        this.paymentId = paymentId;
        return this;
    }

    public Intent build(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context must be populated with a non null value");
        }
        if (externalPaymentId == null && paymentId == null) {
            throw new IllegalArgumentException("Either externalPaymentId or paymentId must be provided");
        }

        Intent i = super.build(context);
        i.setComponent(new ComponentName("com.clover.payment.builder.pay", "com.clover.payment.builder.pay.handler.RetrievePaymentRequestHandler"));
        if (externalPaymentId != null) {
            i.putExtra(Intents.EXTRA_EXTERNAL_PAYMENT_ID, externalPaymentId);
        }

        if (paymentId != null) {
            i.putExtra(Intents.EXTRA_PAYMENT_ID, paymentId);
        }

        return i;
    }
}
