package com.clover.sdk.v3.payments.api;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.clover.sdk.internal.util.Strings;
import com.clover.sdk.v1.Intents;

/**
 * Use the TipAdjustRequestIntentBuilder to build an intent for a merchant to add a tip to a payment. This
 * is <b>NOT to prompt the customer</b> for a tip, rather it allows a merchant to apply a tip to an existing
 * payment, for example, from a tip amount left on a receipt. If the tip amount is not provided (null)
 * then a UI will be displayed allowing a merchant to enter a tip amount.
 */
public class TipAdjustRequestIntentBuilder extends BaseIntentBuilder {
    String paymentId;
    Long tipAmount;

    /**
     * Create the build with a required paymentId of the payment to apply the tip
     * @param paymentId
     */
    public TipAdjustRequestIntentBuilder(String paymentId) {
        if(Strings.isNullOrEmpty(paymentId)) {
            throw new IllegalArgumentException("payment id is required.");
        }
        this.paymentId = paymentId;
    }

    /**
     * The amount of the tip. If null, the Clover Tip app will open for the user
     * to enter the tip for the payment
     * @param tipAmount
     * @return
     */
    public TipAdjustRequestIntentBuilder tipAmount(Long tipAmount) {
        this.tipAmount = tipAmount;
        return this;
    }

    @Override
    public Intent build(Context context) {

        if (context == null) {
            throw new IllegalArgumentException("context must be populated with a non null value");
        }

        Intent i = super.build(context);
        i.setComponent(new ComponentName("com.clover.payment.builder.pay", "com.clover.payment.builder.pay.handler.TipAdjustRequestHandler"));
        if (paymentId != null) {
            i.putExtra(Intents.EXTRA_PAYMENT_ID, paymentId);
        }

        if (tipAmount != null) {
            i.putExtra(Intents.EXTRA_TIP_AMOUNT, tipAmount);
        }

        return i;
    }
}
