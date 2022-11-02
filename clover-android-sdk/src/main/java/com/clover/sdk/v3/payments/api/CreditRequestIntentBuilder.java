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
        public static ReceiptOptions Default(boolean cloverShouldHandleReceipts) {
            return new ReceiptOptions(cloverShouldHandleReceipts, null, null, null, null);
        }

        public static ReceiptOptions SkipReceiptSelection() {
            return new ReceiptOptions(true, SmsReceiptOption.Disable(), EmailReceiptOption.Disable(), PrintReceiptOption.Disable(), NoReceiptOption.Disable());
        }

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

        public static class SmsReceiptOption extends ReceiptOptions.ReceiptOption {
            private SmsReceiptOption(String sms, boolean enabled) {
                this.type = ReceiptOptionType.SMS;
                this.value = sms;
                this.enabled = enabled;
            }
            public static SmsReceiptOption Enable(String sms) {
                return new SmsReceiptOption(sms, true);
            }
            public static SmsReceiptOption Disable() {
                return new SmsReceiptOption(null, false);
            }

        }

        public static class EmailReceiptOption extends ReceiptOptions.ReceiptOption {
            private EmailReceiptOption(String email, boolean enable) {
                this.type = ReceiptOptionType.EMAIL;
                this.value = email;
                this.enabled = enable;
            }
            public static EmailReceiptOption Enable(String email) {
                return new EmailReceiptOption(email, true);
            }
            public static EmailReceiptOption Disable() {
                return new EmailReceiptOption(null, false);
            }
        }

        public static class PrintReceiptOption extends ReceiptOptions.ReceiptOption {

            private PrintReceiptOption(boolean enable){
                this.type = ReceiptOptionType.PRINT;
                this.enabled = enable;
            }
            public static PrintReceiptOption Enable() {
                return new PrintReceiptOption(true);
            }
            public static PrintReceiptOption Disable() {
                return new PrintReceiptOption(false);
            }
        }

        public static class NoReceiptOption extends ReceiptOptions.ReceiptOption {
            private NoReceiptOption(boolean enable) {
                this.type = ReceiptOptionType.NO_RECEIPT;
                this.enabled = enable;
            }
            public static NoReceiptOption Enable() {
                return new NoReceiptOption(true);
            }
            public static NoReceiptOption Disable() {
                return new NoReceiptOption(false);
            }
        }
    }
}
