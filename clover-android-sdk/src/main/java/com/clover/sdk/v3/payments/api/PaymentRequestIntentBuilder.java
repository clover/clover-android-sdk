package com.clover.sdk.v3.payments.api;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.clover.sdk.v1.Intents;
import com.clover.sdk.v3.merchant.CashbackSuggestion;
import com.clover.sdk.v3.payments.ReceiptOptionType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

/**
 * Use the PaymentRequestIntentBuilder class to initiate a payment request on an Android device.
 */
public class PaymentRequestIntentBuilder extends BaseIntentBuilder {
  private String externalPaymentId;
  private Long amount;
  private Long taxAmount;
  private Boolean preferOnScreen = null;
  private CardOptions cardOptions = null;
  private TipOptions tipOptions = null;
  private SignatureOptions signatureOptions = null;
  private ReceiptOptions receiptOptions = null;
  private OfflineOptions offlineOptions = null;
  private TokenizeOptions tokenizeOptions = null;
  private String externalReferenceId;

  private PaymentRequestIntentBuilder() {
  }

  /**
   * Sets TipOptions, SignatureOptions and preferOnScreen on the PaymentRequestIntentBuilder object
   *
   * @param tipOptions
   * @param signatureOptions
   * @param preferOnScreen - applies to both tip and signature as preferred location
   * @return PaymentRequestIntentBuilder object with new TipOptions, SignatureOptions and preferOnScreen
   */
  public PaymentRequestIntentBuilder tipAndSignatureOptions(TipOptions tipOptions, SignatureOptions signatureOptions, Boolean preferOnScreen) {
    this.tipOptions = tipOptions;
    this.signatureOptions = signatureOptions;
    this.preferOnScreen = preferOnScreen;
    return this;
  }

  /**
   * Creates an instance of the PaymentRequestIntentBuilder class
   *
   * @param externalPaymentId
   * @param amount
   */
  public PaymentRequestIntentBuilder(String externalPaymentId, long amount) {
    this.externalPaymentId = externalPaymentId;
    this.amount = amount;
  }

  /**
   * Sets CardOptions on the PaymentRequestIntentBuilder object
   *
   * @param cardOptions
   * @return PaymentRequestIntentBuilder object with new CardOptions
   */
  public PaymentRequestIntentBuilder cardOptions(CardOptions cardOptions) {
    this.cardOptions = cardOptions;
    return this;
  }

  /**
   * Sets ReceiptOptions on the PaymentRequestIntentBuilder object
   *
   * @param receiptOptions
   * @return PaymentRequestIntentBuilder object with new ReceiptOptions
   */
  public PaymentRequestIntentBuilder receiptOptions(ReceiptOptions receiptOptions) {
    this.receiptOptions = receiptOptions;
    return this;
  }

  /**
   * Sets OfflineOptions on the PaymentRequestIntentBuilder object
   *
   * @param offlineOptions
   * @return PaymentRequestIntentBuilder object with new OfflineOptions
   */
  public PaymentRequestIntentBuilder offlineOptions(OfflineOptions offlineOptions) {
    this.offlineOptions = offlineOptions;
    return this;
  }

  public PaymentRequestIntentBuilder tokenizeOptions(TokenizeOptions tokenizeOptions) {
    this.tokenizeOptions = tokenizeOptions;
    return this;
  }

  /**
   * Sets the field 'externalReferenceId'
   *
   * @param externalReferenceId
   * @return PaymentRequestIntentBuilder object with new externalReferenceId
   */
  public PaymentRequestIntentBuilder externalReferenceId(String externalReferenceId) {
    this.externalReferenceId = externalReferenceId;
    return this;
  }

  /**
   * Sets the field 'taxAmount'.
   * NOTE: This will not affect the total amount.
   * @param taxAmount
   * @return PaymentRequestIntentBuilder object with new taxAmount
   */
  public PaymentRequestIntentBuilder taxAmount(Long taxAmount) {
    this.taxAmount = taxAmount;
    return this;
  }

  /**
   * Builder method to create an Intent to be used by Integrator POS to initiate payment
   *
   * @param context
   * @return Android Intent to be used to initiate a payment.
   * @throws IllegalArgumentException
   */
  public Intent build(Context context) throws IllegalArgumentException {
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
    i.setComponent(new ComponentName("com.clover.payment.builder.pay", "com.clover.payment.builder.pay.handler.PaymentRequestHandler"));
    if (externalPaymentId != null) {
      i.putExtra(Intents.EXTRA_EXTERNAL_PAYMENT_ID, externalPaymentId);
    }
    if (amount != null) {
      i.putExtra(Intents.EXTRA_AMOUNT, amount);
    }

    if (taxAmount != null) {
      i.putExtra(Intents.EXTRA_TAX_AMOUNT, taxAmount);
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
      if (cardOptions.cashbackOptions != null) {
        if (cardOptions.cashbackOptions.disableCashback != null) {
          i.putExtra(Intents.EXTRA_DISABLE_CASHBACK, cardOptions.cashbackOptions.disableCashback);
        }
        if (cardOptions.cashbackOptions.cashbackSuggestions != null) {
          List<CashbackSuggestion> cashbackSuggestions = new ArrayList<>();
          for (Long amount : cardOptions.cashbackOptions.cashbackSuggestions) {
            CashbackSuggestion cashbackSuggestion = new CashbackSuggestion();
            cashbackSuggestion.setAmount(amount);
            cashbackSuggestions.add(cashbackSuggestion);
          }
          i.putExtra(Intents.EXTRA_CASHBACK_SUGGESTIONS, (Serializable) cashbackSuggestions);
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

    if (signatureOptions != null) {
      if (signatureOptions.signatureThreshold != null) {
        i.putExtra(Intents.EXTRA_SIGNATURE_THRESHOLD, signatureOptions.signatureThreshold);
      }
      if (signatureOptions.autoAcceptSignature != null) {
        i.putExtra(Intents.EXTRA_AUTO_ACCEPT_SIGNATURE, signatureOptions.autoAcceptSignature);
      }

      if (preferOnScreen != null && !signatureOptions.disablePromptForSignature) {
        i.putExtra(Intents.EXTRA_API_SIGNATURE_PREFER_ON_SCREEN, preferOnScreen.booleanValue());
      }
    } else {
      if (preferOnScreen != null) {
        i.putExtra(Intents.EXTRA_API_SIGNATURE_PREFER_ON_SCREEN, preferOnScreen.booleanValue());
      }
    }

    if (tokenizeOptions != null) {
      i.putExtra(Intents.EXTRA_SHOULD_TOKENIZE_CARD, true);
      i.putExtra(Intents.EXTRA_SUPPRESS_CONFIRMATION, tokenizeOptions.suppressConfirmation);
    }

    if (tipOptions != null) {
      if (tipOptions.tipAmount != null) {
        i.putExtra(Intents.EXTRA_TIP_AMOUNT, tipOptions.tipAmount);
      }
      if (tipOptions.baseAmount != null) {
        i.putExtra(Intents.EXTRA_TIPPABLE_AMOUNT, tipOptions.baseAmount);
      }
      if (tipOptions.tipSuggestions != null) {
//        tipOptions.tipSuggestions.stream().map(ts -> ts.getV3TipSuggestion()).collect(Collectors.toList());
        List<com.clover.sdk.v3.merchant.TipSuggestion> suggestions = new ArrayList<>();
        for (com.clover.sdk.v3.payments.api.TipSuggestion tipSuggestion : tipOptions.tipSuggestions) {
          suggestions.add(tipSuggestion.getV3TipSuggestion());
        }
        i.putExtra(Intents.EXTRA_TIP_SUGGESTIONS, (Serializable) suggestions);
      }
      if (preferOnScreen != null && !tipOptions.disablePromptForTips) {
        i.putExtra(Intents.EXTRA_API_TIP_PREFER_ON_SCREEN, preferOnScreen.booleanValue());
      }
    } else {
      if (preferOnScreen != null) {
        i.putExtra(Intents.EXTRA_API_TIP_PREFER_ON_SCREEN, preferOnScreen.booleanValue());
      }
    }

    if (externalReferenceId != null) {
      i.putExtra(Intents.EXTRA_EXTERNAL_REFERENCE_ID, externalReferenceId);
    }

    return i;
  }

  /**
   * Tip options that allow the Integrator to control tipping on a per-transaction level.
   */
  public static class TipOptions {
    private final Long tipAmount;
    private final List<TipSuggestion> tipSuggestions;
    private final Long baseAmount;
    private final boolean disablePromptForTips;

    private TipOptions(Long tipAmount, Long baseAmount, List<TipSuggestion> tipSuggestions, Boolean disableTips) {
      this.tipAmount = tipAmount;
      this.baseAmount = baseAmount;
      this.tipSuggestions = tipSuggestions;
      this.disablePromptForTips = disableTips;
    }

    public static PaymentRequestIntentBuilder.TipOptions Disable() {
      return new PaymentRequestIntentBuilder.TipOptions(0L, null, null, true);
    }

    /**
     * Tips will be provided by Integrator.
     */
    public static PaymentRequestIntentBuilder.TipOptions Provided(long tipAmount) {
      return new PaymentRequestIntentBuilder.TipOptions(tipAmount, null, null, true);
    }

    /**
     * Customers will be prompted to tip.
     */
    public static PaymentRequestIntentBuilder.TipOptions PromptCustomer(Long baseAmount, List<TipSuggestion> tipSuggestions) {
      return new PaymentRequestIntentBuilder.TipOptions(null, baseAmount, tipSuggestions, false);
    }


  }

  /**
   * Signature options that allow the Integrator to control how the signature is collected on a per-transaction level.
   */
  public static class SignatureOptions {
    private final Long signatureThreshold;
    private final Boolean autoAcceptSignature;
    private final boolean disablePromptForSignature;

    private SignatureOptions(Long signatureThreshold, Boolean autoAcceptSignature, boolean disablePromptForSignature) {
      this.signatureThreshold = signatureThreshold;
      this.autoAcceptSignature = autoAcceptSignature;
      this.disablePromptForSignature = disablePromptForSignature;
    }

    /**
     * No signature will be collected
     */
    public static PaymentRequestIntentBuilder.SignatureOptions Disable() {
      return new PaymentRequestIntentBuilder.SignatureOptions(Long.MAX_VALUE, null, true);
    }

    /**
     * Customer will be prompted (on screen or on paper) for signature
     */
    public static PaymentRequestIntentBuilder.SignatureOptions PromptCustomer(Long signatureThreshold, Boolean autoAcceptSignature) {
      return new PaymentRequestIntentBuilder.SignatureOptions(signatureThreshold, autoAcceptSignature, false);
    }
  }

  /**
   * Card options that allow the Integrator to control the use of cards.
   */
  public static class CardOptions {
    private final Set<CardEntryMethod> cardEntryMethods;
    private final Boolean cardNotPresent;
    private final Boolean autoAcceptDuplicates;
    private final CashbackOptions cashbackOptions;

    private CardOptions(Set<CardEntryMethod> cardEntryMethods, Boolean cardNotPresent, Boolean autoAcceptDuplicates, CashbackOptions cashbackOptions) {
      this.cardEntryMethods = cardEntryMethods;
      this.cardNotPresent = cardNotPresent;
      this.autoAcceptDuplicates = autoAcceptDuplicates;
      this.cashbackOptions = cashbackOptions;
    }

    public static CardOptions Instance(Set<CardEntryMethod> cardEntryMethods, Boolean cardNotPresent, Boolean autoAcceptDuplicates) {
      return new CardOptions(cardEntryMethods, cardNotPresent, autoAcceptDuplicates, null);
    }

    public static CardOptions Instance(Set<CardEntryMethod> cardEntryMethods, Boolean cardNotPresent, Boolean autoAcceptDuplicates, CashbackOptions cashbackOptions) {
      return new CardOptions(cardEntryMethods, cardNotPresent, autoAcceptDuplicates, cashbackOptions);
    }

    /**
     * CashbackOptions give you the option to disable Cashback suggestions or set your own
     * cashback suggestions.
     */
    public static class CashbackOptions {
      private final Boolean disableCashback;
      private final List<Long> cashbackSuggestions;

      private CashbackOptions(Boolean disableCashback, List<Long> cashbackSuggestions) {
        this.disableCashback = disableCashback;
        this.cashbackSuggestions = cashbackSuggestions;
      }

      public static CashbackOptions Disable() {
        return new CashbackOptions(true, null);
      }

      public static CashbackOptions Suggestions(List<Long> cashbackSuggestions) {
        return new CashbackOptions(false, cashbackSuggestions);
      }
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

    public static OfflineOptions Instance(Boolean allowOfflinePayment, Boolean approveOfflinePaymentWithoutPrompt, Boolean forceOfflinePayment) {
      return new OfflineOptions(allowOfflinePayment, approveOfflinePaymentWithoutPrompt, forceOfflinePayment);
    }
  }

  public static class TokenizeOptions {
    private final Boolean suppressConfirmation;

    private TokenizeOptions(boolean suppressConfirmation) {
      this.suppressConfirmation = suppressConfirmation;
    }

    public static TokenizeOptions Instance(boolean suppressConfirmation) {
      return new TokenizeOptions(suppressConfirmation);
    }
  }
}
