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


    public static class Response {
        /**
         * If tokenizing the card is requested, this will return the type of Token.
         */
        public static final String TOKEN_TYPE = Intents.EXTRA_TOKEN_TYPE;
        /**
         * The token to be used for EComm transactions.
         */
        public static final String TOKEN = Intents.EXTRA_TOKEN;
        /**
         * Card information for the tokenized card.  This should  consist of information such as:
         * first6, last4, expiration month, expiration year.
         */
        public static final String CARD = Intents.EXTRA_CARD;
        /**
         * If tokenizing the card fails, the reason will be present.
         */
        public static final String REASON = Intents.EXTRA_REASON;
        /**
         * If the customer confirmation of saving their card is suppressed.
         */
        public static final String SUPPRESS_CONFIRMATION = Intents.EXTRA_SUPPRESS_CONFIRMATION;
        /**
         * If legacy vault card is requested, the resulting Vaulted Card.
         */
        public static final String VAULTED_CARD = Intents.EXTRA_VAULTED_CARD;
        /**
         * If legacy vault card is requested, the resulting card data such as track data and card holder information.
         */
        public static final String CARD_DATA = Intents.EXTRA_CARD_DATA;
        /**
         * If tokenizing or vaulting the card fails for any reason, there will be a failure message sent.
         */
        public static final String FAILURE_MESSAGE = Intents.EXTRA_FAILURE_MESSAGE;
    }
}
