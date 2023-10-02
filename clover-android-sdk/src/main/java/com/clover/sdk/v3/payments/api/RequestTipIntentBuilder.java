package com.clover.sdk.v3.payments.api;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.clover.sdk.v1.Intents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Use the RequestTipIntentBuilder class to initiate a customer facing tip screen.
 */
public class RequestTipIntentBuilder extends BaseIntentBuilder {

    /* this is ultimately the tippable amount. if we wanted to have both "total" and "tippable",
        we could either
     1. rename in the TipOptions for Pay and PreAuth to use "tippableAmount" instead of "baseAmount". This is
        a documentation (name) changed, not an api change
     2. add a new field to this class which is "orderAmount" or "totalAmount" and that would be the amount
        and "baseAmount" would be the tippable amount
     */
    Long baseAmount;
    List<TipSuggestion> tipSuggestions = new ArrayList<TipSuggestion>();

    /**
     * Constructor that takes an amount that is displayed on the screen and is
     * also used used to calculate percentage based tips.
     * @param baseAmount
     * @return
     */
    public RequestTipIntentBuilder(long baseAmount) {
        this.baseAmount = baseAmount;
    }

    /**
     * Options list of up to 4 tip suggestions that can be either
     * percentage or fixed amounts. Percentage based suggestions are calculcated
     * based on the baseAmount
     * @param tipSuggestions
     * @return
     */
    public RequestTipIntentBuilder tipSuggestions(List<TipSuggestion> tipSuggestions) {
        this.tipSuggestions = tipSuggestions;
        return this;
    }



    @Override
    public Intent build(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context must be populated with a non null value");
        }

        Intent i = super.build(context);
        i.setComponent(new ComponentName("com.clover.payment.builder.pay", "com.clover.payment.builder.pay.handler.RequestTipHandler"));

        i.putExtra(Intents.EXTRA_AMOUNT, baseAmount);
        i.putExtra(Intents.EXTRA_TIPPABLE_AMOUNT, baseAmount);
        if(tipSuggestions.size() > 0) {
            List<com.clover.sdk.v3.merchant.TipSuggestion> suggestions = new ArrayList<>();
            for (com.clover.sdk.v3.payments.api.TipSuggestion tipSuggestion : tipSuggestions) {
                suggestions.add(tipSuggestion.getV3TipSuggestion());
            }
            i.putExtra(Intents.EXTRA_TIP_SUGGESTIONS, (Serializable) suggestions);
        }

        return i;
    }
}
