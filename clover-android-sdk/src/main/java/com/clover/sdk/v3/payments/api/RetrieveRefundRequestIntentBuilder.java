package com.clover.sdk.v3.payments.api;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.clover.sdk.v1.Intents;

/**
 * Use the RetrieveRefundRequestIntentBuilder class to retrieve a previously processed refund on a Clover device.
 */
public class RetrieveRefundRequestIntentBuilder extends BaseIntentBuilder {
    private String refundId;

    /**
     * Creates an instance of the RetrieveRefundRequestIntentBuilder class
     * @param refundId The ID of the refund to retrieve
     */
    public RetrieveRefundRequestIntentBuilder(String refundId) {
        this.refundId = refundId;
    }

    public Intent build(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context must be populated with a non null value");
        }
        if (refundId == null) {
            throw new IllegalArgumentException("Refund ID cannot be null");
        }

        Intent i = super.build(context);
        i.setComponent(new ComponentName("com.clover.payment.builder.pay", "com.clover.payment.builder.pay.handler.RetrieveRefundRequestHandler"));
        i.putExtra(Intents.EXTRA_REFUND_ID, refundId);

        return i;
    }

    public static class Response {
        /**
         * The retrieved refund.
         */
        public static final String REFUND = Intents.EXTRA_REFUND;
        /**
         * If retrieving the refund fails, there will be a failure message sent.
         */
        public static final String FAILURE_MESSAGE = Intents.EXTRA_FAILURE_MESSAGE;
    }
}
