package com.clover.sdk.v3.payments.api;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.clover.sdk.v1.Intents;

/**
 * Use the RetrieveOpenPaymentsRequestIntentBuilder class to retrieve a list of payment IDs
 * of tip-adjustable payments.
 */
public class RetrieveOpenPaymentsRequestIntentBuilder extends BaseIntentBuilder {


    /**
     * Builder method to create an Intent to be by an Integrator POS to retrieve a list of payment IDs of tip-adjustable payments.
     * @param context
     * @return Android Intent to be used to retrieve a list of payment IDs of tip-adjustable payments.
     */
    public Intent build(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context must be populated with a non null value");
        }
        Intent i = super.build(context);
        i.setComponent(new ComponentName("com.clover.payment.builder.pay", "com.clover.payment.builder.pay.handler.RetrieveOpenPaymentsRequestHandler"));

        return i;
    }

    public static class Response {
        /**
         * If there are open payments, the retrieved payment IDs will be returned.
         */
        public static final String PAYMENT_IDS = Intents.EXTRA_PAYMENT_IDS;
        /**
         * If there are no open payments returned, the reason will be provided.
         */
        public static final String REASON = Intents.EXTRA_REASON;
        /**
         * If retrieving open payments fails for any reason, there will be a failure message sent.
         */
        public static final String FAILURE_MESSAGE = Intents.EXTRA_FAILURE_MESSAGE;
    }
}