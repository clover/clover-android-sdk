package com.clover.sdk.v3.payments.api;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.clover.sdk.v1.Intents;

/**
 * Use the CloseoutRequestIntentBuilder to build an intent for a merchant to closeout transactions.
 * Before a closeout can be successful, the merchant must be approved for manual closeout before using, as well as
 * open payments must have a tip amount set.  Optionally, through TipOptions, you can choose to set a zero amount
 * for all remaining open payments, to leave preauths open, and for Clover to handle tips.
 *
 */
public class CloseoutRequestIntentBuilder extends BaseIntentBuilder {
    private TipOptions tipOptions;
    private Boolean leavePreauthsOpen;

    /**
     * The ability to customize how tips are handled for the Closeout request.
     * TipOptions include: ZeroOutOpenTips, PromptForOpenPayments
     * @param tipOptions
     * @return the CloseoutRequestIntentBuilder object with tipOptions set.
     */
    public CloseoutRequestIntentBuilder tipOptions(TipOptions tipOptions) {
        this.tipOptions = tipOptions;
        return this;
    }

    /**
     * The ability to leave preauths open.  By default, closeout will fail if there are preauths open.
     * @param leavePreauthsOpen
     * @return the CloseoutRequestIntentBuilder object with leavePreauthsOpen set.
     */
    public CloseoutRequestIntentBuilder leavePreauthsOpen(Boolean leavePreauthsOpen) {
        this.leavePreauthsOpen = leavePreauthsOpen;
        return this;
    }

    public Intent build(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context must be populated with a non null value");
        }

        Intent i = super.build(context);
        i.setComponent(new ComponentName("com.clover.payment.builder.pay", "com.clover.payment.builder.pay.handler.CloseoutRequestHandler"));

        if (leavePreauthsOpen != null) {
            i.putExtra(Intents.EXTRA_LEAVE_PREAUTHS_OPEN, leavePreauthsOpen);
        }
        if (tipOptions != null) {
            if (tipOptions.tipOption != null) {
                i.putExtra(Intents.EXTRA_OPEN_PAYMENT_OPTIONS, tipOptions.tipOption);
            }
        }
        return i;
    }

    /**
     * The TipOptions class gives the integrator the ability to customize how tips will be handled
     * with the CloseoutRequestIntentBuilder
     *
     */
    public static class TipOptions {
        private final String tipOption;

        private TipOptions(String tipOption) {
            this.tipOption = tipOption;
        }

        /**
         * TipOptions that will set all open tips to zero before queuing closeout
         * @return
         */
        public static TipOptions ZeroOutOpenTips() {
            return new TipOptions(OpenPaymentOption.ZERO_OUT_OPEN_TIPS);
        }

        /**
         * TipOptions that will open the default Clover Tips app, if there are any open tips, before
         * queuing closeout
         * @return
         */
        public static TipOptions PromptForOpenPayments() {
            return new TipOptions(OpenPaymentOption.PROMPT_FOR_OPEN_PAYMENTS);
        }

        /**
         * TipOptions that will return a list of payment IDs of payments that have open tips, if any.
         * When open tip payments are present, a closeout will not be queued.
         * @return
         */
        public static TipOptions ReturnOpenPayments() {
            return new TipOptions(OpenPaymentOption.RETURN_OPEN_TIP_PAYMENTS);
        }

        /**
         * TipOption that will return immediately with the count of open payments.
         * When open tips are present, a closeout will not be queued.
         * @return
         */
        public static TipOptions ReturnOpenPaymentsCount() {
            return new TipOptions(OpenPaymentOption.RETURN_OPEN_PAYMENTS_COUNT);
        }
    }

    public static class Response {
        /**
         * The result of the closeout request.
         * @see CloseoutResult
         */
        public static final String CLOSEOUT_RESULT = Intents.EXTRA_CLOSEOUT_RESULT;
        /**
         * If the closeout was successful, the resulting Batch.
         */
        public static final String BATCH = Intents.EXTRA_CLOSEOUT_BATCH;
        /**
         * If the closeout fails for any reason, there will be a failure message sent.
         */
        public static final String FAILURE_MESSAGE = Intents.EXTRA_FAILURE_MESSAGE;
        /**
         * If there are open payments in the current Batch, there will be a list of payment IDs.
         */
        public static final String PAYMENT_IDS = Intents.EXTRA_PAYMENT_IDS;

        /**
         * The count of open payments, if any.  This will only be returned when ReturnOpenPaymentsCount() is used for TipOptions.
         */
        public static final String OPEN_PAYMENT_COUNT = "clover.intent.extra.OPEN_PAYMENT_COUNT";
    }
}