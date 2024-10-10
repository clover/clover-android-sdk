package com.clover.sdk.v3.payments.api;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.clover.sdk.v1.Intents;
import com.clover.sdk.v3.payments.ReceiptOptionType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Use the ReceiptSelectionRequestIntentBuilder class to initiate a receipt selection activity, for the
 * merchant. It currently support payment, credits and refund receipts. It can also be used to configure
 * specific options for receipt selection such as hiding or showing options or specifying if a default
 * Clover receipt should be sent.
 */
public class SelectReceiptRequestIntentBuilder extends BaseIntentBuilder {

    final private Map<String, ReceiptOption> providedReceiptOptions = new HashMap<>();
    private Boolean cloverShouldHandleReceipts;

    private String paymentId;
    private String creditId;
    private String refundId;
//    private String creditRefundId;

    /**
     * Used to create a Payment receipt request to launch the Receipt Selection
     * Activity
     * @param paymentId
     * @return
     */
    public static SelectReceiptRequestIntentBuilder Payment(String paymentId) {
        return new SelectReceiptRequestIntentBuilder(paymentId, null, null, null);
    }
    /**
     * Used to create a Credit/Manual Refund receipt request to launch the Receipt Selection
     * Activity
     * @param creditId
     * @return
     */
    public static SelectReceiptRequestIntentBuilder Credit(String creditId) {
        return new SelectReceiptRequestIntentBuilder(null, creditId, null, null);
    }
    /**
     * Used to create a payment Refund receipt request to launch the Receipt Selection
     * Activity
     * @param refundId
     * @return
     */
    public static SelectReceiptRequestIntentBuilder Refund(String refundId) {
        return new SelectReceiptRequestIntentBuilder(null, null, refundId, null);
    }

    /* *
     * Used to create a payment CreditRefund receipt request to launch the Receipt Selection
     * Activity
     * <i>note: This is only available in some regions.</i>
     * @param creditRefundId
     * @return
     */
//    public static ReceiptSelectionRequestIntentBuilder CreditRefund(String creditRefundId) {
//        return new ReceiptSelectionRequestIntentBuilder(null, null, null, creditRefundId);
//    }

    private SelectReceiptRequestIntentBuilder receiptOptions(String type, ReceiptOption receiptOptions) {
        if(receiptOptions == null) {
            providedReceiptOptions.remove(type);
        } else {
            providedReceiptOptions.put(type, receiptOptions);
        }
        return this;
    }

    /**
     * Set SMS receipt options for the Select Receipt Activity, including enabled/disabled and a
     * pre-populated phone number
     * @param receiptOptions
     * @return
     */
    public SelectReceiptRequestIntentBuilder smsReceiptOptions(SmsReceiptOption receiptOptions) {
        return receiptOptions(ReceiptOptionType.SMS, receiptOptions);
    }
    /**
     * Set Email receipt options for the Select Receipt Activity, including enabled/disabled and a
     * pre-populated email address
     * @param receiptOptions
     * @return
     */
    public SelectReceiptRequestIntentBuilder emailReceiptOptions(EmailReceiptOption receiptOptions) {
        return receiptOptions(ReceiptOptionType.EMAIL, receiptOptions);
    }
    /**
     * Set Print receipt options for the Select Receipt Activity, including enabled/disabled
     * @param receiptOptions
     * @return
     */
    public SelectReceiptRequestIntentBuilder printReceiptOptions(PrintReceiptOption receiptOptions) {
        return receiptOptions(ReceiptOptionType.PRINT, receiptOptions);
    }
    /**
     * Set No Receipt, receipt options for the Select Receipt Activity, including enabled/disabled
     * <i>note: this will only have an effect on customer facing screens</i>
     * @param receiptOptions
     * @return
     */
    public SelectReceiptRequestIntentBuilder noReceiptReceiptOptions(NoReceiptOption receiptOptions) {
        return receiptOptions(ReceiptOptionType.NO_RECEIPT, receiptOptions);
    }


    /**
     * If true, default Clover receipts will be processed using Clover's default receipt processing.
     * If false, no Clover receipts will be processed, but the selection (and sms/email if entered) will be
     * returned so custom receipt handling can be used.
     *
     * by default, receipts will be processed using default Clover receipts
     * @param cloverShouldHandleReceipts
     * @return
     */
    public SelectReceiptRequestIntentBuilder cloverShouldHandleReceipts(Boolean cloverShouldHandleReceipts) {
        this.cloverShouldHandleReceipts = cloverShouldHandleReceipts;
        return this;
    }


    private SelectReceiptRequestIntentBuilder() {}

    private SelectReceiptRequestIntentBuilder(String paymentId, String creditId, String refundId, String creditRefundId) {
        this.paymentId = paymentId;
        this.creditId = creditId;
        this.refundId = refundId;
//        this.creditRefundId = creditRefundId;
    }

    /**
     * Base class for specific receipt options
     */
    public abstract static class ReceiptOption {
        protected boolean enabled;
        protected String type;
        protected String value;
    }

    /**
     * Options for Sms/Text receipts option
     */
    public static class SmsReceiptOption extends ReceiptOption {

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

    /**
     * Creates a <B>single use</B> intent to start the receipt selection screen Activity
     * @param context
     * @return
     */
    @Override
    public Intent build(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context must be populated with a non null value");
        }

        Intent i = super.build(context);
        i.setComponent(new ComponentName("com.clover.payment.builder.pay", "com.clover.payment.builder.pay.handler.SelectReceiptRequestHandler"));

        i.putExtra(Intents.EXTRA_PAYMENT_ID, paymentId);
        i.putExtra(Intents.EXTRA_CREDIT_ID, creditId);
        i.putExtra(Intents.EXTRA_REFUND_ID, refundId);
//        i.putExtra(Intents.EXTRA_CREDIT_REFUND_ID, creditRefundId);


        Map<String, String> enabledReceiptOptions = new HashMap<>();
        Map<String, String> disabledReceiptOptions = new HashMap<>();
        for (String key : providedReceiptOptions.keySet()) {
            ReceiptOption providedReceiptOption = providedReceiptOptions.get(key);
            if (providedReceiptOption.enabled) {
                String value;
                if (providedReceiptOption.value != null) {
                    value = providedReceiptOption.value;
                } else {
                    //We need a string value of null because GenericClient in TransactionSettings will filter out null values
                    value = "null";
                }
                enabledReceiptOptions.put(providedReceiptOption.type, value);
            } else {
                disabledReceiptOptions.put(providedReceiptOption.type, "null");
            }
        }
        i.putExtra(Intents.EXTRA_ENABLED_RECEIPT_OPTIONS, (Serializable) enabledReceiptOptions);
        i.putExtra(Intents.EXTRA_DISABLE_RECEIPT_OPTIONS, (Serializable) disabledReceiptOptions);

        if (cloverShouldHandleReceipts != null) {
            i.putExtra(Intents.EXTRA_REMOTE_RECEIPTS, !cloverShouldHandleReceipts);
        }

        return i;
    }


    public static class Response {
        /**
         * The payment the receipt was selected for.
         */
        public final static String PAYMENT = Intents.EXTRA_PAYMENT;
        /**
         * The credit the receipt was selected for.
         */
        public final static String CREDIT = Intents.EXTRA_CREDIT;
        /**
         * The refund the receipt was selected for.
         */
        public final static String REFUND = Intents.EXTRA_REFUND;
        /**
         * The customer's entered email/phone number from the customer facing receipt screen.
         */
        public static final String ENTERED_RECEIPT_VALUE = Intents.EXTRA_ENTERED_RECEIPT_VALUE;
        /**
         * The status of the customer's receipt delivery.  If cloverShouldHandleReceipts is true, then it will
         * return PROCESSED.  If cloverShouldHandleReceipts is false, then it will return REQUESTED.
         */
        public static final String RECEIPT_DELIVERY_STATUS = Intents.EXTRA_RECEIPT_DELIVERY_STATUS;
        /**
         * The type of receipt requested by the customer.
         * e.g., SMS, Email, Print, No Receipt
         */
        public static final String RECEIPT_DELIVERY_TYPE = Intents.EXTRA_RECEIPT_DELIVERY_TYPE;
        /**
         * If the customer chose to opt into marketing communication on the receipt screen.
         */
        public static final String OPTED_INTO_MARKETING = Intents.EXTRA_OPTED_INTO_MARKETING;
        /**
         * If selecting a receipt fails for any reason, there will be a failure message sent.
         */
        public static final String FAILURE_MESSAGE = Intents.EXTRA_FAILURE_MESSAGE;
    }

}
