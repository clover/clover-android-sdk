package com.clover.sdk.v3.payments.api;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.clover.sdk.v1.Intents;
import com.clover.sdk.v3.payments.ReceiptOptionType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

/**
 * Use CreditRequestIntentBuilder class to initiate a credit
 */
public class CreditRequestIntentBuilder extends BaseIntentBuilder {
    private String externalPaymentId;
    private Long amount;
    private CardOptions cardOptions;
    private ReceiptOptions receiptOptions;

    /**
     * Creates instance of the CreditRequestIntentBuilder class
     * @param externalPaymentId
     * @param amount
     */
    public CreditRequestIntentBuilder(String externalPaymentId, long amount) {
        this.externalPaymentId = externalPaymentId;
        this.amount = amount;
    }

    /**
     * Sets CardOptions on the CreditRequestIntentBuilder object
     *
     * @param cardOptions
     * @return CreditRequestIntentBuilder object with new CardOptions
     */
    public CreditRequestIntentBuilder cardOptions(CardOptions cardOptions) {
        this.cardOptions = cardOptions;
        return this;
    }

    /**
     * Sets ReceiptOptions on the CreditRequestIntentBuilder object
     *
     * @param receiptOptions
     * @return CreditRequestIntentBuilder object with new ReceiptOptions
     */
    public CreditRequestIntentBuilder receiptOptions(ReceiptOptions receiptOptions) {
        this.receiptOptions = receiptOptions;
        return this;
    }

    public Intent build(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context must be populated with a non null value");
        }
        if (externalPaymentId == null) {
            throw new IllegalArgumentException("externalPaymentId must be populated with a non null value");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("amount cannot be less than or equal to zero");
        }

        Intent i = super.build(context);
        i.setComponent(new ComponentName("com.clover.payment.builder.pay", "com.clover.payment.builder.pay.handler.CreditRequestHandler"));
        if (externalPaymentId != null) {
            i.putExtra(Intents.EXTRA_EXTERNAL_PAYMENT_ID, externalPaymentId);
        }
        if (amount != null) {
            i.putExtra(Intents.EXTRA_AMOUNT, amount);
        }
        if (cardOptions != null) {
            if (cardOptions.cardEntryMethods != null) {
                i.putExtra(Intents.EXTRA_CARD_ENTRY_METHODS, RequestIntentBuilderUtils.convert(cardOptions.cardEntryMethods));
            }
            if (cardOptions.cardNotPresent != null) {
                i.putExtra(Intents.EXTRA_CARD_NOT_PRESENT, cardOptions.cardNotPresent);
                if (cardOptions.cardNotPresent) {
                    i.putExtra(Intents.EXTRA_CARD_ENTRY_METHODS, Intents.CARD_ENTRY_METHOD_MANUAL);
                }
            }
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
        }

        return i;
    }

    /**
     * Card options that allow the Integrator to control the use of cards.
     */
    public static class CardOptions {
        private final Set<CardEntryMethod> cardEntryMethods;
        private final Boolean cardNotPresent;

        private CardOptions(Set<CardEntryMethod> cardEntryMethods, Boolean cardNotPresent) {
            this.cardEntryMethods = cardEntryMethods;
            this.cardNotPresent = cardNotPresent;
        }

        /**
         * CardOptions to control card options for a single transaction
         * @param cardEntryMethods - @see CardEntryMethod
         * @param cardNotPresent - If card is not present, will result in Manual card entry
         * @return
         */
        public static CreditRequestIntentBuilder.CardOptions Instance(Set<CardEntryMethod> cardEntryMethods, Boolean cardNotPresent) {
            return new CreditRequestIntentBuilder.CardOptions(cardEntryMethods, cardNotPresent);
        }
    }

    /**
     * Receipt options that allow the Integrator to control the receipt selection on a per-transaction level.
     */
    public static class ReceiptOptions {
        private List<ReceiptOption> providedReceiptOptions;
        private Boolean cloverShouldHandleReceipts;

        private ReceiptOptions() {}
        /**
         * Create ReceiptOptions with the default list of options displaying, with an option to have
         * the Clover default receipt processing or not.
         * @param cloverShouldHandleReceipts - <i>true</i>-Clover will process a default Clover receipt(default), or
         *                                   <i>false</i>-will return the object with a REQUESTED value if a default
         *                                   Clover receipt isn't desired. For SMS and Email, an additional
         *                                   field containing the sms number or email address will also
         *                                   be returned.
         * @return
         */
        public static ReceiptOptions Default(boolean cloverShouldHandleReceipts) {
            return new ReceiptOptions(cloverShouldHandleReceipts, null, null, null, null);
        }

        /**
         * This will cause the UI flow to skip the receipt selection screen and no customer receipt will be
         * processed
         * @return
         */
        public static ReceiptOptions SkipReceiptSelection() {
            return new ReceiptOptions(true, SmsReceiptOption.Disable(), EmailReceiptOption.Disable(), PrintReceiptOption.Disable(), NoReceiptOption.Disable());
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
         * @return
         */
        public static ReceiptOptions Instance(Boolean cloverShouldHandleReceipts, SmsReceiptOption smsReceiptOption, EmailReceiptOption emailReceiptOption, PrintReceiptOption printReceiptOption, NoReceiptOption noReceiptOption) {
            return new ReceiptOptions(cloverShouldHandleReceipts, smsReceiptOption, emailReceiptOption, printReceiptOption, noReceiptOption);
        }
        private ReceiptOptions(Boolean cloverShouldHandleReceipts, SmsReceiptOption smsReceiptOption, EmailReceiptOption emailReceiptOption, PrintReceiptOption printReceiptOption, NoReceiptOption noReceiptOption) {
            this.cloverShouldHandleReceipts = cloverShouldHandleReceipts;
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
        public static class SmsReceiptOption extends ReceiptOptions.ReceiptOption {
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
        public static class EmailReceiptOption extends ReceiptOptions.ReceiptOption {
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
        public static class PrintReceiptOption extends ReceiptOptions.ReceiptOption {

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
        public static class NoReceiptOption extends ReceiptOptions.ReceiptOption {
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
         * The resulting Credit object.
         */
        public static final String CREDIT = Intents.EXTRA_CREDIT;
        /**
         * The customer's entered email/phone number from the customer facing receipt screen.
         */
        public static final String ENTERED_RECEIPT_VALUE = Intents.EXTRA_ENTERED_RECEIPT_VALUE;
        /**
         * The type of receipt requested by the customer.
         * e.g., SMS, Email, Print, No Receipt
         */
        public static final String RECEIPT_DELIVERY_TYPE = Intents.EXTRA_RECEIPT_DELIVERY_TYPE;
        /**
         * The status of the customer's receipt delivery.  If cloverShouldHandleReceipts is true, then it will
         * return PROCESSED.  If cloverShouldHandleReceipts is false, then it will return REQUESTED.
         */
        public static final String RECEIPT_DELIVERY_STATUS = Intents.EXTRA_RECEIPT_DELIVERY_STATUS;
        /**
         * If the customer chose to opt into marketing communication on the receipt screen.
         */
        public static final String OPTED_INTO_MARKETING = Intents.EXTRA_OPTED_INTO_MARKETING;
        /**
         * If the Credit request fails for any reason, there will be a failure message sent.
         */
        public static final String FAILURE_MESSAGE = Intents.EXTRA_FAILURE_MESSAGE;
    }
}
