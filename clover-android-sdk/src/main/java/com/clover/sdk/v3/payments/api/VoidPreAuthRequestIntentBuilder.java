package com.clover.sdk.v3.payments.api;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.clover.sdk.v1.Intents;

public class VoidPreAuthRequestIntentBuilder extends BaseIntentBuilder {
    private String paymentId;

    private VoidPreAuthRequestIntentBuilder() {}

    public VoidPreAuthRequestIntentBuilder(String paymentId) {
        this.paymentId = paymentId;
    }

    public VoidPreAuthRequestIntentBuilder paymentId(String paymentId) {
        this.paymentId = paymentId;
        return this;
    }

    public Intent build(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Activity context must be populated with a non null value");
        }
        if (paymentId == null) {
            throw new IllegalArgumentException("paymentId must be populated with a non null value");
        }
        Intent i = super.build(context);
        i.setComponent(new ComponentName("com.clover.payment.builder.pay", "com.clover.payment.builder.pay.handler.VoidPreAuthRequestHandler"));
        i.putExtra(Intents.EXTRA_PAYMENT_ID, paymentId);
        return i;
    }
}
