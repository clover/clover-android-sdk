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
 * Use the KioskPayRequestIntentBuilder class to initiate Kiosk mode payments on a Clover Kiosk.
 * Making a request using KioskPayRequestIntentBuilder can result in:
 * 1. A fully paid order/full amount collected.
 * 2. No active payments, including voiding any partial payments if collected.
 */
public class KioskPayRequestIntentBuilder extends BaseIntentBuilder {
    private Long amount;
    private String externalReferenceId;
    
    private String orderId;
    
    private Long taxAmount;
    
    private Boolean allowPartialPayments;
    
    private TipOptions tipOptions;
    
    private ReceiptOptions receiptOptions;
    
    private SignatureOptions signatureOptions;

    private OfflineOptions offlineOptions;
    
    
    
    private KioskPayRequestIntentBuilder() {}

    /**
     * Creates an instance of the KioskPayRequestIntentBuilder class.
     * This is to be used when Clover orders aren't being used, and only when a payment amount is being requested.
     * @param amount
     * @param externalReferenceId
     */
    public KioskPayRequestIntentBuilder(long amount, String externalReferenceId) {
        this.amount = amount;
        this.externalReferenceId = externalReferenceId;
    }

    /**
     * Creates an instance of the KioskPayRequestIntentBuilder class.
     * This is to be used when an Order has already been created to be paid for.
     * @param orderId
     */
    public KioskPayRequestIntentBuilder(String orderId) {
        this.orderId = orderId;
    }

    /**
     * Sets the field 'taxAmount'.
     * NOTE: This will not affect the total amount.
     * @param taxAmount
     * @return KioskPayRequestIntentBuilder object with new taxAmount
     */
    public KioskPayRequestIntentBuilder taxAmount(long taxAmount) {
        this.taxAmount = taxAmount;
        return this;
    }

    /**
     * Sets the field 'allowPartialPayments'.
     * This informs Clover to accept a payment that only partially pays for an Order.
     * NOTE: This is true by default.
     * @param allowPartialPayments
     * @return
     */
    public KioskPayRequestIntentBuilder allowPartialPayments(boolean allowPartialPayments) {
        this.allowPartialPayments = allowPartialPayments;
        return this;
    }

    /**
     * Sets TipOptions on the KioskPayRequestIntentBuilder object.
     * @see TipOptions
     * @param tipOptions
     * @return
     */
    public KioskPayRequestIntentBuilder tipOptions(TipOptions tipOptions) {
        this.tipOptions = tipOptions;
        return this;
    }

    /**
     * Sets ReceiptOptions on the KioskPayRequestIntentBuilder object.
     * @see ReceiptOptions
     * @param receiptOptions
     * @return
     */
    public KioskPayRequestIntentBuilder receiptOptions(ReceiptOptions receiptOptions) {
        this.receiptOptions = receiptOptions;
        return this;
    }

    /**
     * Sets SignatureOptions on the KioskPayRequestIntentBuilder object.
     * @see SignatureOptions
     * @param signatureOptions
     * @return
     */
    public KioskPayRequestIntentBuilder signatureOptions(SignatureOptions signatureOptions) {
        this.signatureOptions = signatureOptions;
        return this;
    }

    /**
     * Sets OfflineOptions on the KioskPayRequestIntentBuilder object.
     * @see OfflineOptions
     * @param offlineOptions
     * @return
     */
    public KioskPayRequestIntentBuilder offlineOptions(OfflineOptions offlineOptions) {
        this.offlineOptions = offlineOptions;
        return this;
    }

    /**
     * Returns a single-use Intent to be used by Integrator POS to initiate a Kiosk payment
     * @param context
     * @return Intent to be used to initiate a Kiosk payment
     * @throws IllegalArgumentException
     */
    public Intent build(Context context) throws IllegalArgumentException {
        if (context == null) {
            throw new IllegalArgumentException("context must be populated with a non null value");
        }
        if (amount != null && amount.longValue() <= 0L) {
            throw new IllegalArgumentException("amount cannot be less than or equal to zero");
        }
        if (externalReferenceId != null && externalReferenceId.isEmpty()) {
            throw new IllegalArgumentException("externalReferenceId must be populated with a non-empty value");
        }
        Intent i = super.build(context);
        i.setComponent(new ComponentName("com.clover.payment.builder.pay", "com.clover.payment.builder.pay.handler.KioskPayRequestHandler"));
        
        if (amount != null) {
            i.putExtra(Intents.EXTRA_AMOUNT, amount);
        }
        if (orderId != null) {
            i.putExtra(Intents.EXTRA_ORDER_ID, orderId);
        }
        if (externalReferenceId != null) {
            i.putExtra(Intents.EXTRA_EXTERNAL_REFERENCE_ID, externalReferenceId);
        }
        if (taxAmount != null) {
            i.putExtra(Intents.EXTRA_TAX_AMOUNT, taxAmount);
        }
        if (allowPartialPayments != null) {
            i.putExtra(Intents.EXTRA_ALLOW_PARTIAL_AUTH, allowPartialPayments);
        }
        if (tipOptions != null) {
            if (tipOptions.tipAmount  != null) {
                i.putExtra(Intents.EXTRA_TIP_AMOUNT, tipOptions.tipAmount);
            }
            if (tipOptions.tipSuggestions != null) {
                List<com.clover.sdk.v3.merchant.TipSuggestion> suggestions = new ArrayList<>();
                for (com.clover.sdk.v3.payments.api.TipSuggestion tipSuggestion : tipOptions.tipSuggestions) {
                    suggestions.add(tipSuggestion.getV3TipSuggestion());
                }
                i.putExtra(Intents.EXTRA_TIP_SUGGESTIONS, (Serializable) suggestions);
            }
            if (tipOptions.baseAmount  != null) {
                i.putExtra(Intents.EXTRA_TIPPABLE_AMOUNT, tipOptions.baseAmount);
            }
        }
        if (receiptOptions != null) {
            if (receiptOptions.providedReceiptOptions != null) {
                Map<String, String> enabledReceiptOptions = new HashMap<>();
                for (ReceiptOptions.ReceiptOption providedReceiptOption : receiptOptions.providedReceiptOptions) {
                    if (providedReceiptOption.enabled) {
                        String value;
                        if (providedReceiptOption.value != null) {
                            value = providedReceiptOption.value;
                        } else {
                            //We need a string value because GenericClient in TransactionSettings will filter out null values
                            value = "";
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
        if (signatureOptions != null) {
            if (signatureOptions.signatureThreshold != null) {
                i.putExtra(Intents.EXTRA_SIGNATURE_THRESHOLD, signatureOptions.signatureThreshold);
            }
        }
        if (offlineOptions != null) {
            if (offlineOptions.allowOfflinePayment != null) {
                i.putExtra(Intents.EXTRA_ALLOW_OFFLINE_PAYMENT, offlineOptions.allowOfflinePayment);
            }
            if (offlineOptions.approveOfflinePaymentWithoutPrompt != null) {
                i.putExtra(Intents.EXTRA_APPROVE_OFFLINE_PAYMENT_WITHOUT_PROMPT, offlineOptions.approveOfflinePaymentWithoutPrompt);
            }
            if (offlineOptions.forceOfflinePayment != null) {
                i.putExtra(Intents.EXTRA_FORCE_OFFLINE, offlineOptions.forceOfflinePayment);
            }
        }

        return i;
    }


    /**
     * Tip options that allow the Integrator to control tipping on a per-transaction level.
     * tipAmount - A provided tip amount that will be used as the Payment's tipAmount, bypassing customer selection.
     * baseAmount - The amount to be tipped on (perhaps less than the transaction's total amount)
     * tipSuggestions - Provides the ability to override the default amounts and labels of Tip Suggestions. (Maximum of 4)
     */
    public static class TipOptions {
        private final Long tipAmount;
        private final List<TipSuggestion> tipSuggestions;
        private final Long baseAmount;

        private TipOptions(Long tipAmount, Long baseAmount, List<TipSuggestion> tipSuggestions) {
            this.tipAmount = tipAmount;
            this.baseAmount = baseAmount;
            this.tipSuggestions = tipSuggestions;
        }

        /**
         * No tip will be taken and tipAmount will default to 0.
         */
        public static TipOptions Disable() {
            return new TipOptions(0L, null, null);
        }

        /**
         * Tips will be provided by Integrator.
         */
        public static TipOptions Provided(long tipAmount) {
            return new TipOptions(tipAmount, null, null);
        }

        /**
         * Customers will be prompted to tip.
         * @param baseAmount - Optional amount used to compute percentage based tip options
         * @param tipSuggestions - Optional list of TipSuggestions for this transaction
         */
        public static TipOptions PromptCustomer(Long baseAmount, List<TipSuggestion> tipSuggestions) {
            return new TipOptions(null, baseAmount, tipSuggestions);
        }
    }

    /**
     * Receipt options that allow the Integrator to control the receipt selection on a per-transaction level.
     */
    public static class ReceiptOptions {
        private List<ReceiptOptions.ReceiptOption> providedReceiptOptions;
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
            return new ReceiptOptions(true, ReceiptOptions.SmsReceiptOption.Disable(), ReceiptOptions.EmailReceiptOption.Disable(), ReceiptOptions.PrintReceiptOption.Disable(), ReceiptOptions.NoReceiptOption.Disable());
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
        public static ReceiptOptions Instance(Boolean cloverShouldHandleReceipts, ReceiptOptions.SmsReceiptOption smsReceiptOption, ReceiptOptions.EmailReceiptOption emailReceiptOption, ReceiptOptions.PrintReceiptOption printReceiptOption, ReceiptOptions.NoReceiptOption noReceiptOption) {
            return new ReceiptOptions(cloverShouldHandleReceipts, smsReceiptOption, emailReceiptOption, printReceiptOption, noReceiptOption);
        }
        private ReceiptOptions(Boolean cloverShouldHandleReceipts, ReceiptOptions.SmsReceiptOption smsReceiptOption, ReceiptOptions.EmailReceiptOption emailReceiptOption, ReceiptOptions.PrintReceiptOption printReceiptOption, ReceiptOptions.NoReceiptOption noReceiptOption) {
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
            public static ReceiptOptions.SmsReceiptOption Enable(String sms) {
                return new ReceiptOptions.SmsReceiptOption(sms, true);
            }
            /**
             * The Sms Receipt option will not be displayed
             * @return
             */
            public static ReceiptOptions.SmsReceiptOption Disable() {
                return new ReceiptOptions.SmsReceiptOption(null, false);
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
            public static ReceiptOptions.EmailReceiptOption Enable(String email) {
                return new ReceiptOptions.EmailReceiptOption(email, true);
            }
            /**
             * The Email Receipt option will not be displayed
             * @return
             */
            public static ReceiptOptions.EmailReceiptOption Disable() {
                return new ReceiptOptions.EmailReceiptOption(null, false);
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
            public static ReceiptOptions.PrintReceiptOption Enable() {
                return new ReceiptOptions.PrintReceiptOption(true);
            }
            /**
             * The Print Receipt option will not be displayed
             * @return
             */
            public static ReceiptOptions.PrintReceiptOption Disable() {
                return new ReceiptOptions.PrintReceiptOption(false);
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
            public static ReceiptOptions.NoReceiptOption Enable() {
                return new ReceiptOptions.NoReceiptOption(true);
            }
            /**
             * The No Receipt option will not be displayed on the customer screen
             * <i>note:</i> This will only hide the No Receipt option from the customer screen
             * @return
             */
            public static ReceiptOptions.NoReceiptOption Disable() {
                return new ReceiptOptions.NoReceiptOption(false);
            }
        }
    }

    /**
     * Signature options that allow the Integrator to control how the signature is collected on a per-transaction level.
     */
    public static class SignatureOptions {
        private final Long signatureThreshold;

        private SignatureOptions(Long signatureThreshold) {
            this.signatureThreshold = signatureThreshold;
        }

        /**
         * No signature will be collected
         */
        public static SignatureOptions Disable() {
            return new SignatureOptions(Long.MAX_VALUE);
        }

        /**
         * Customer will be prompted (on screen or on paper) for signature
         */
        public static SignatureOptions PromptCustomer(Long signatureThreshold) {
            return new SignatureOptions(signatureThreshold);
        }
    }

    /**
     * Offline options that allow the Integrator to control a transaction's offline state on a per-transaction level.
     */
    public static class OfflineOptions {
        private final Boolean allowOfflinePayment;
        private final Boolean approveOfflinePaymentWithoutPrompt;
        private final Boolean forceOfflinePayment;

        private OfflineOptions(Boolean allowOfflinePayment, Boolean approveOfflinePaymentWithoutPrompt, Boolean forceOfflinePayment) {
            this.allowOfflinePayment = allowOfflinePayment;
            this.approveOfflinePaymentWithoutPrompt = approveOfflinePaymentWithoutPrompt;
            this.forceOfflinePayment = forceOfflinePayment;
        }

        /**
         * Offline option that can be used per transaction
         * @param allowOfflinePayment - if merchant is configured, will enable an offline payment
         * @param approveOfflinePaymentWithoutPrompt - if an offline payment is needed, it will allow it without
         *                                           prompting the merchant
         * @param forceOfflinePayment - take the payment offline, even if the device is online
         * @return
         */
        public static OfflineOptions Instance(Boolean allowOfflinePayment, Boolean approveOfflinePaymentWithoutPrompt, Boolean forceOfflinePayment) {
            return new OfflineOptions(allowOfflinePayment, approveOfflinePaymentWithoutPrompt, forceOfflinePayment);
        }
    }

    public static class Response {
        /**
         * The resulting payments.
         */
        public final static String PAYMENTS = Intents.EXTRA_PAYMENTS;

        /**
         * The resulting Order.
         */
        public final static String ORDER = Intents.EXTRA_ORDER;
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
         * If taken, customer's signature.
         */
        public static final String SIGNATURE = Intents.EXTRA_SIGNATURE;

        /**
         * IDs of the payments voided, if any.
         */
        public static final String VOIDED_PAYMENT_IDS = Intents.EXTRA_VOIDED_PAYMENT_IDS;
        /**
         * If the payments fail for any reason, there will be a failure message sent.
         */
        public static final String FAILURE_MESSAGE = Intents.EXTRA_FAILURE_MESSAGE;
    }
}
