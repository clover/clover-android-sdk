package com.clover.sdk.v3.payments.api;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.clover.sdk.v1.Intents;

import java.util.Set;

/**
 * Use the TokenizeCardRequestIntentBuilder class to initiate a tokenize card request on a Clover device.
 */
public class TokenizeCardRequestIntentBuilder extends BaseIntentBuilder {
    private CardOptions cardOptions;
    private Boolean suppressConfirmation;

    /**
     * set the CardOptions for a single transaction
     * @param cardOptions
     * @return
     */
    public TokenizeCardRequestIntentBuilder cardOptions(CardOptions cardOptions) {
        this.cardOptions = cardOptions;
        return this;
    }

    /**
     * Option to allow tokenization without confirmation
     * @return
     */
    public TokenizeCardRequestIntentBuilder suppressConfirmation(Boolean suppressConfirmation) {
        this.suppressConfirmation = suppressConfirmation;
        return this;
    }

    public Intent build(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context must be populated with a non null value");
        }

        Intent i = super.build(context);
        i.setComponent(new ComponentName("com.clover.payment.builder.pay", "com.clover.payment.builder.pay.handler.TokenizeCardRequestHandler"));

        if (cardOptions != null) {
            if (cardOptions.cardEntryMethods != null) {
                i.putExtra(Intents.EXTRA_CARD_ENTRY_METHODS, RequestIntentBuilderUtils.convert(cardOptions.cardEntryMethods));
            }
        }
        if (suppressConfirmation != null) {
            i.putExtra(Intents.EXTRA_SUPPRESS_CONFIRMATION, suppressConfirmation);
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

        /**
         * CardOptions to control card options for a single transaction
         * @param cardEntryMethods - @see CardEntryMethod
         * @return
         */
        public static CardOptions Instance(Set<CardEntryMethod> cardEntryMethods) {
            return new CardOptions(cardEntryMethods);
        }
    }
}
