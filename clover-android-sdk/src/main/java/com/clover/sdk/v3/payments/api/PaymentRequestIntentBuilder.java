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
 * Use the PaymentRequestIntentBuilder class to initiate a payment request on a Clover device.
 */
public class PaymentRequestIntentBuilder extends BaseIntentBuilder {
  private String externalPaymentId;
  private Long amount;
  private Long taxAmount;
  private String orderId;
  private Boolean preferOnScreen = null;
  private CardOptions cardOptions = null;
  private TipOptions tipOptions = null;
  private SignatureOptions signatureOptions = null;
  private ReceiptOptions receiptOptions = null;
  private OfflineOptions offlineOptions = null;
  private TokenizeOptions tokenizeOptions = null;
  private TenderOptions tenderOptions = null;
  private String externalReferenceId;
  private boolean kioskMode = false;

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
   * Sets TenderOptions on the PaymentRequestIntentBuilder object
   * @param tenderOptions - @see TenderOptions
   * @return PaymentRequestIntentBuilder object with new TenderOptions
   */
  public PaymentRequestIntentBuilder tenderOptions(TenderOptions tenderOptions) {
    this.tenderOptions = tenderOptions;
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

  /**
   * Set TokenizeOptions for this single transaction
   *
   * @param tokenizeOptions - @see TokenizeOptions
   * @return
   */
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
   * Set the id of the order, to which the payment will be added
   * @param orderId - id of an existing order
   * @return PaymentRequestIntentBuilder object with new orderId
   */
  public PaymentRequestIntentBuilder orderId(String orderId) {
    this.orderId = orderId;
    return this;
  }

  /**
   * return a single-use Intent to be used by Integrator POS to initiate payment
   *
   * @param context
   * @return Intent to be used to initiate a payment.
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

    if (tenderOptions != null) {
      Map<String, String> tenderOptionsMap = new HashMap<>();
      if (!tenderOptions.customDisabled) {
        tenderOptionsMap.put(TenderOption.CUSTOM_TENDER, "null");
      }
      if (!tenderOptions.cashDisabled) {
        tenderOptionsMap.put(TenderOption.CASH, "null");
      }
      i.putExtra(Intents.EXTRA_TENDER_OPTIONS, (Serializable) tenderOptionsMap);
    }

    if (externalReferenceId != null) {
      i.putExtra(Intents.EXTRA_EXTERNAL_REFERENCE_ID, externalReferenceId);
    }

    if (orderId != null) {
      i.putExtra(Intents.EXTRA_ORDER_ID, orderId);
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

    /**
     * No tip will be taken and tipAmount will default to 0.
     */
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
     * @param baseAmount - Optional amount used to compute percentage based tip options
     * @param tipSuggestions - Optional list of TipSuggestions for this transaction
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

    /**
     * CardOptions to control card options for a single transaction
     * @param cardEntryMethods - @see CardEntryMethod
     * @param cardNotPresent - If card is not present, will result in Manual card entry
     * @param autoAcceptDuplicates - Accept, and don't prompt if potential duplicate payment is detected
     * @return
     */
    public static CardOptions Instance(Set<CardEntryMethod> cardEntryMethods, Boolean cardNotPresent, Boolean autoAcceptDuplicates) {
      return new CardOptions(cardEntryMethods, cardNotPresent, autoAcceptDuplicates, null);
    }

    /**
     * Receipt options that allow the Integrator to control the receipt selection on a per-transaction level.
     *
     * CardOptions to control card options for a single transaction
     * @param cardEntryMethods - @see CardEntryMethod
     * @param cardNotPresent - If card is not present, will result in Manual card entry
     * @param autoAcceptDuplicates - Accept, and don't prompt if potential duplicate payment is detected
     * @param cashbackOptions - optional list of cashback option amounts to be displayed
     * @return
     */
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

      /**
       * Disable cashback options on screen
       * @return
       */
      public static CashbackOptions Disable() {
        return new CashbackOptions(true, null);
      }

      /**
       * Provide a list of cashback suggestions to be displayed during cashback amount selection
       * @param cashbackSuggestions
       * @return
       */
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
      return new ReceiptOptions(true, SmsReceiptOption.Disable(), EmailReceiptOption.Disable(), PrintReceiptOption.Disable(), NoReceiptOption.Disable());
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
      public static SmsReceiptOption Enable(String sms) {
        return new SmsReceiptOption(sms, true);
      }
      /**
       * The Sms Receipt option will not be displayed
       * @return
       */
      public static SmsReceiptOption Disable() {
        return new SmsReceiptOption(null, false);
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
    public static class PrintReceiptOption extends ReceiptOptions.ReceiptOption {

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
    public static class NoReceiptOption extends ReceiptOptions.ReceiptOption {
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

  /**
   * Tender options allow Integrators to control Cash and Custom Tenders on a per-transaction level.
   */
  public static class TenderOptions {
    private boolean cashDisabled;
    private boolean customDisabled;

    private TenderOptions(boolean disableCash, boolean disableCustom) {
      this.cashDisabled = disableCash;
      this.customDisabled = disableCustom;
    }

    /**
     * The option to disable the cash and/or custom tenders independently of each other
     * @param disableCash - if you would like to disable the cash tender, set to true
     * @param disableCustom - if you would like to disable the custom tenders, set to true
     * @return
     */
    public static TenderOptions Disable(boolean disableCash, boolean disableCustom) {
      return new TenderOptions(disableCash, disableCustom);
    }

    /**
     * The option to disable BOTH cash and custom tenders
     * @return
     */
    public static TenderOptions Disable() {
      return new TenderOptions(true, true);
    }
  }
}
