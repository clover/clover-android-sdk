package com.clover.sdk.v3.payments.api;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.clover.sdk.v1.Intents;

/**
 * Use the RetrieveCreditRequestIntentBuilder class to retrieve a previously processed credit on a Clover device.
 */
public class RetrieveCreditRequestIntentBuilder extends BaseIntentBuilder {
    private String creditId;

    /**
     * Creates an instance of the RetrieveCreditRequestIntentBuilder class
     * @param creditId The ID of the credit to retrieve
     */
    public RetrieveCreditRequestIntentBuilder(String creditId) {
        this.creditId = creditId;
    }

    public Intent build(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context must be populated with a non null value");
        }
        if (creditId == null) {
            throw new IllegalArgumentException("Credit ID cannot be null");
        }

        Intent i = super.build(context);
        i.setComponent(new ComponentName("com.clover.payment.builder.pay", "com.clover.payment.builder.pay.handler.RetrieveCreditRequestHandler"));
        i.putExtra(Intents.EXTRA_CREDIT_ID, creditId);

        return i;
    }

    public static class Response {
        /**
         * The retrieved Credit object.
         */
        public static final String CREDIT = Intents.EXTRA_CREDIT;
        /**
         * If retrieving the credit fails for any reason, there will be a failure message sent.
         */
        public static final String FAILURE_MESSAGE = Intents.EXTRA_FAILURE_MESSAGE;
    }
}
