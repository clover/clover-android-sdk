package com.clover.sdk.v3.payments.api;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.clover.sdk.v1.Intents;
import com.clover.sdk.v3.payments.ReceiptOptionType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Use the ReversePaymentIntentBuilder class to initiate a payment reversal (void or refund)
 */
public class ReversePaymentRequestIntentBuilder extends BaseIntentBuilder {
    private String paymentId;
    private Long amount;

    private ReceiptOptions receiptOptions;

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

    public ReversePaymentRequestIntentBuilder receiptOptions(ReceiptOptions receiptOptions) {
        this.receiptOptions = receiptOptions;
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

        if (receiptOptions != null) {
            //if providedReceiptOptions is null, we will proceed with default receipt options.
            //if providedReceiptOptions is not null, we will check for enabled receipt options.
            //if providedReceiptOptions are all disabled, we will skip receipt screen.
            if (receiptOptions.providedReceiptOptions != null) {
                Map<String, String> enabledReceiptOptions = new HashMap<>();
                for (ReceiptOptions.ReceiptOption providedReceiptOption : receiptOptions.providedReceiptOptions) {
                    if (providedReceiptOption.enabled) {
                        String value;
                        if (providedReceiptOption.value != null) {
                            value = providedReceiptOption.value;
                        } else {
                            //We need a string value of null because GenericClient in TransactionSettings will filter out null values
                            value = "null";
                        }
                        enabledReceiptOptions.put(providedReceiptOption.type, value);
                    }
                }
                i.putExtra(Intents.EXTRA_ENABLED_RECEIPT_OPTIONS, (Serializable) enabledReceiptOptions);
                //All Receipt Options were disabled, so skip the receipt screen
                i.putExtra(Intents.EXTRA_SKIP_RECEIPT_SCREEN, !(enabledReceiptOptions.size() > 0));
            }

            if (receiptOptions.cloverShouldHandleReceipts != null) {
                i.putExtra(Intents.EXTRA_REMOTE_RECEIPTS, !receiptOptions.cloverShouldHandleReceipts);
            }
            if (receiptOptions.timeoutThreshold != null) {
                i.putExtra(Intents.EXTRA_RECEIPT_SELECTION_TIMEOUT_THRESHOLD, receiptOptions.timeoutThreshold);
            }
            if (!i.hasExtra(Intents.EXTRA_SKIP_RECEIPT_SCREEN)) {
                i.putExtra(Intents.EXTRA_SKIP_RECEIPT_SCREEN, receiptOptions.disableReceiptSelection);
            }
        }

        return i;
    }

    public static class ReceiptOptions {
        private Boolean disableReceiptSelection;

        private Boolean cloverShouldHandleReceipts;

        private List<ReceiptOption> providedReceiptOptions;

        private Long timeoutThreshold;

        private ReceiptOptions() {}

        private ReceiptOptions(boolean disableReceiptSelection) {
            this.disableReceiptSelection = disableReceiptSelection;
        }

        public static ReceiptOptions DisableReceiptSelection() {
            return new ReceiptOptions(true);
        }

        public static ReceiptOptions Default(boolean cloverShouldHandleReceipts) {
            return new ReceiptOptions(cloverShouldHandleReceipts, null, null, null, null, null);
        }

        /**
         * Builds a ReceiptOptions where some options may be specified
         * @param cloverShouldHandleReceipts - <i>true</i>-Clover will process a default Clover receipt(default), or
         *                                   <i>false</i>-will return the object with a REQUESTED value if a default
         *                                   Clover receipt isn't desired. For SMS and Email, an additional
         *                                   field containing the sms number or email address will also
         *                                   be returned.
         * @param smsReceiptOption - @see SmsReceiptOption
         * @param emailReceiptOption - @see EmailReceiptOptions
         * @param printReceiptOption - @see PrintReceiptOption
         * @param noReceiptOption - @see NoReceiptOption
         * @param timeoutThreshold - the timeout value in milliseconds where the receipt selection screen will timeout
         * @return
         */

        public static ReceiptOptions Instance(Boolean cloverShouldHandleReceipts, SmsReceiptOption smsReceiptOption, EmailReceiptOption emailReceiptOption, PrintReceiptOption printReceiptOption, NoReceiptOption noReceiptOption, Long timeoutThreshold) {
            return new ReceiptOptions(cloverShouldHandleReceipts, smsReceiptOption, emailReceiptOption, printReceiptOption, noReceiptOption, timeoutThreshold);
        }

        private ReceiptOptions(Boolean cloverShouldHandleReceipts, SmsReceiptOption smsReceiptOption, EmailReceiptOption emailReceiptOption, PrintReceiptOption printReceiptOption, NoReceiptOption noReceiptOption, Long timeoutThreshold) {
            this.cloverShouldHandleReceipts = cloverShouldHandleReceipts;
            this.timeoutThreshold = timeoutThreshold;
            //if all receipt options are null, then providedReceiptOptions will be null (default behavior)
            if (smsReceiptOption != null || emailReceiptOption != null || printReceiptOption != null || noReceiptOption != null) {
                this.providedReceiptOptions = new ArrayList<>();
                if (smsReceiptOption != null) {
                    this.providedReceiptOptions.add(smsReceiptOption);
                }
                if (emailReceiptOption != null) {
                    this.providedReceiptOptions.add(emailReceiptOption);
                }
                if (printReceiptOption != null) {
                    this.providedReceiptOptions.add(printReceiptOption);
                }
                if (noReceiptOption != null) {
                    this.providedReceiptOptions.add(noReceiptOption);
                }
            }
        }

        private static class ReceiptOption {
            protected boolean enabled;
            protected String type;
            protected String value;
        }

        /**
         * SmsReceiptOption that allows the Integrator to control the Sms receipt option.
         */
        public static class SmsReceiptOption extends ReceiptOption {

            private SmsReceiptOption(String sms, boolean enabled) {
                this.type = ReceiptOptionType.SMS;
                this.value = sms;
                this.enabled = enabled;
            }
            /**
             * The Sms Receipt option will be displayed, with an optional sms number provided
             * @param sms - optional sms number that will pre-fill the number field
             * @return
             */
            public static SmsReceiptOption Enable(String sms) {
                return new SmsReceiptOption(sms, true);
            }
            /**
             * The Sms Receipt option will not be displayed
             * @return
             */
            public static SmsReceiptOption Disable() {
                return new SmsReceiptOption(null, false);
            }

        }

        /**
         * EmailReceiptOption that allows the Integrator to control the Email receipt option.
         */
        public static class EmailReceiptOption extends ReceiptOption {

            private EmailReceiptOption(String email, boolean enable) {
                this.type = ReceiptOptionType.EMAIL;
                this.value = email;
                this.enabled = enable;
            }
            /**
             * The Email Receipt option will be displayed, with an optional email address provided
             * @param email - optional email address that will pre-fill the email address field
             * @return
             */
            public static EmailReceiptOption Enable(String email) {
                return new EmailReceiptOption(email, true);
            }
            /**
             * The Email Receipt option will not be displayed
             * @return
             */
            public static EmailReceiptOption Disable() {
                return new EmailReceiptOption(null, false);
            }
        }

        /**
         * PrintReceiptOption that allows the Integrator to control the Print receipt option.
         */
        public static class PrintReceiptOption extends ReceiptOption {

            private PrintReceiptOption(boolean enable){
                this.type = ReceiptOptionType.PRINT;
                this.enabled = enable;
            }
            /**
             * The Print Receipt option will be displayed
             * @return
             */
            public static PrintReceiptOption Enable() {
                return new PrintReceiptOption(true);
            }
            /**
             * The Print Receipt option will not be displayed
             * @return
             */
            public static PrintReceiptOption Disable() {
                return new PrintReceiptOption(false);
            }
        }

        /**
         * PrintReceiptOption that allows the Integrator to control the Print receipt option.
         */
        public static class NoReceiptOption extends ReceiptOption {
            private NoReceiptOption(boolean enable) {
                this.type = ReceiptOptionType.NO_RECEIPT;
                this.enabled = enable;
            }
            /**
             * The No Receipt option will be displayed
             * @return
             */
            public static NoReceiptOption Enable() {
                return new NoReceiptOption(true);
            }
            /**
             * The No Receipt option will not be displayed on the customer screen
             * <i>note:</i> This will only hide the No Receipt option from the customer screen
             * @return
             */
            public static NoReceiptOption Disable() {
                return new NoReceiptOption(false);
            }
        }
    }

    public static class Response {
        /**
         * The result of reversing the payment.
         * @see ReversePaymentResult
         */
        public static final String REVERSE_PAYMENT_RESULT = Intents.EXTRA_REVERSE_PAYMENT_RESULT;
        /**
         * The ID of the payment to be reversed.
         */
        public static final String PAYMENT_ID = Intents.EXTRA_PAYMENT_ID;
        /**
         * If refunded, the resulting Refund object.
         */
        public static final String REFUND = Intents.EXTRA_REFUND;
        /**
         * If refunded, the Tender object.
         */
        public static final String TENDER = Intents.EXTRA_TENDER;
        /**
         * If reversing the payment fails for any reason, there will be a failure message sent.
         */
        public static final String FAILURE_MESSAGE = Intents.EXTRA_FAILURE_MESSAGE;
    }
}
