package com.clover.sdk.v3.payments.api;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.clover.sdk.v1.Intents;

import java.util.Set;

/**
 * Use the PreAuthRequestIntentBuilder class to initiate a pre-authorized payment on a Clover device.
 */
public class PreAuthRequestIntentBuilder extends BaseIntentBuilder {
  private String externalPaymentId;
  private Long amount;
  private CardOptions cardOptions;
  private TokenizeOptions tokenizeOptions;
  private String externalReferenceId;

  /**
   * Creates an instance of the PreAuthRequestIntentBuilder class
   *
   * @param externalPaymentId
   * @param amount
   */
  public PreAuthRequestIntentBuilder(String externalPaymentId, long amount) {
    this.externalPaymentId = externalPaymentId;
    this.amount = amount;
  }

  /**
   * Sets CardOptions on the PreAuthRequestIntentBuilder object
   *
   * @param cardOptions
   * @return PreAuthRequestIntentBuilder object with new CardOptions
   */
  public PreAuthRequestIntentBuilder cardOptions(CardOptions cardOptions) {
    this.cardOptions = cardOptions;
    return this;
  }

  /**
   * Sets the tokenize options for this transaction intent
   * @param tokenizeOptions
   * @return
   */
  public PreAuthRequestIntentBuilder tokenizeOptions(TokenizeOptions tokenizeOptions) {
    this.tokenizeOptions = tokenizeOptions;
    return this;
  }

  /**
   * Sets the field 'externalReferenceId'
   *
   * @param externalReferenceId
   * @return PreAuthRequestIntentBuilder object with new externalReferenceId
   */
  public PreAuthRequestIntentBuilder externalReferenceId(String externalReferenceId) {
    this.externalReferenceId = externalReferenceId;
    return this;
  }

  /**
   * Builder method to create an Intent to be used by Integrator POS to initiate a pre-authorization
   *
   * @param context
   * @return Android Intent to be used to initiate a pre-authorization.
   * @throws IllegalArgumentException
   */
  public Intent build(Context context) {
    if (context == null) {
      throw new IllegalArgumentException("context must be populated with a non null value");
    }
    if (externalPaymentId == null) {
      throw new IllegalArgumentException("externalPaymentId must be populated with a non null value");
    }
    if (amount.longValue() <= 0) {
      throw new IllegalArgumentException("amount cannot be less than or equal to zero");
    }

    Intent i = super.build(context);
    i.setComponent(new ComponentName("com.clover.payment.builder.pay", "com.clover.payment.builder.pay.handler.PreAuthRequestHandler"));
    i.putExtra(Intents.EXTRA_EXTERNAL_PAYMENT_ID, externalPaymentId);
    i.putExtra(Intents.EXTRA_AMOUNT, amount);
    if (externalReferenceId != null) {
      i.putExtra(Intents.EXTRA_EXTERNAL_REFERENCE_ID, externalReferenceId);
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
      if (cardOptions.autoAcceptDuplicates != null) {
        i.putExtra(Intents.EXTRA_AUTO_ACCEPT_DUPLICATES, cardOptions.autoAcceptDuplicates);
      }
    }

    if (tokenizeOptions != null) {
      i.putExtra(Intents.EXTRA_SHOULD_TOKENIZE_CARD, true);
      i.putExtra(Intents.EXTRA_SUPPRESS_CONFIRMATION, tokenizeOptions.suppressConfirmation);
    }
    return i;
  }

  /**
   * Card options that allow the Integrator to control the use of cards.
   */
  public static class CardOptions {
    private final Set<CardEntryMethod> cardEntryMethods;
    private final Boolean cardNotPresent;
    private final Boolean autoAcceptDuplicates;

    private CardOptions(Set<CardEntryMethod> cardEntryMethods, Boolean cardNotPresent, Boolean autoAcceptDuplicates) {
      this.cardEntryMethods = cardEntryMethods;
      this.cardNotPresent = cardNotPresent;
      this.autoAcceptDuplicates = autoAcceptDuplicates;
    }

    /**
     * CardOptions to control card options for a single transaction
     * @param cardEntryMethods - @see CardEntryMethod
     * @param cardNotPresent - If card is not present, will result in Manual card entry
     * @param autoAcceptDuplicates - Accept, and don't prompt if potential duplicate payment is detected
     * @return
     */
    public static CardOptions Instance(Set<CardEntryMethod> cardEntryMethods, Boolean cardNotPresent, Boolean autoAcceptDuplicates) {
      return new CardOptions(cardEntryMethods, cardNotPresent, autoAcceptDuplicates);
    }
  }

  /**
   * Options to tokenize card during transaction
   */
  public static class TokenizeOptions {
    private final Boolean suppressConfirmation;

    private TokenizeOptions(boolean suppressConfirmation) {
      this.suppressConfirmation = suppressConfirmation;
    }

    /**
     * Enable tokenizing card, with an option to not ask for confirmation
     * @param suppressConfirmation
     * @return
     */
    public static TokenizeOptions Instance(boolean suppressConfirmation) {
      return new TokenizeOptions(suppressConfirmation);
    }
  }
}
