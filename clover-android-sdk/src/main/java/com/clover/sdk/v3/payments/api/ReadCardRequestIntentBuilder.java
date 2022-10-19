package com.clover.sdk.v3.payments.api;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.clover.sdk.v1.Intents;

import java.util.Set;

public class ReadCardRequestIntentBuilder extends BaseIntentBuilder {
    private CardOptions cardOptions = null;



    public ReadCardRequestIntentBuilder cardOptions(CardOptions cardOptions) {
        this.cardOptions = cardOptions;
        return this;
    }

    public Intent build(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context must be populated with a non null value");
        }

        Intent i = super.build(context);
        i.setComponent(new ComponentName("com.clover.payment.builder.pay", "com.clover.payment.builder.pay.handler.ReadCardRequestHandler"));

        if (cardOptions != null) {
            if (cardOptions.cardEntryMethods != null) {
                i.putExtra(Intents.EXTRA_CARD_ENTRY_METHODS, RequestIntentBuilderUtils.convert(cardOptions.cardEntryMethods));
            }
        }

        return i;
    }
    
    /**
     * Card options that allow the Integrator to control the use of cards.
     */
    public static class CardOptions {
        private final Set<CardEntryMethod> cardEntryMethods;
        private CardOptions(Set<CardEntryMethod> cardEntryMethods) {
            this.cardEntryMethods = cardEntryMethods;
        }

        public static CardOptions Instance(Set<CardEntryMethod> cardEntryMethods) {
            return new CardOptions(cardEntryMethods);
        }
    }
}
