package com.clover.sdk.v3.payments.api;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.clover.sdk.v1.Intents;

/**
 * Use the RetrievePendingPaymentsRequestIntentBuilder class to retrieve a list of pending (offline) payments
 */
public class RetrievePendingPaymentsRequestIntentBuilder extends BaseIntentBuilder {

    /**
     * Creates an instance of RetrievePendingPaymentsRequestIntentBuilder class
     */
    public RetrievePendingPaymentsRequestIntentBuilder() {}

    /**
     * Builder method to create an Intent to be used by an Integrator POS to retrieve a list of pending (offline) payments
     * @param context
     * @return Android Intent to be used to retrieve a list of pending (offline) payments
     */
    public Intent build(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context must be populated with a non null value");
        }
        Intent i = super.build(context);
        i.setComponent(new ComponentName("com.clover.payment.builder.pay", "com.clover.payment.builder.pay.handler.RetrievePendingPaymentsRequestHandler"));

        return i;
    }


    public static class Response {
        /**
         * A list of retrieved pending payments.
         */
        public static final String PAYMENTS = Intents.EXTRA_PAYMENTS;
        /**
         * If retrieving pending payments fails for any reason, there will be a failure message sent.
         */
        public static final String FAILURE_MESSAGE = Intents.EXTRA_FAILURE_MESSAGE;
    }
}
