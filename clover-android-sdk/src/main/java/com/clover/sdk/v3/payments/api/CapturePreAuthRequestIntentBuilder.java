package com.clover.sdk.v3.payments.api;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.clover.sdk.v1.Intents;
import com.clover.sdk.v3.payments.ReceiptOptionType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Use the CapturePreAuthRequestIntentBuilder class to capture a pre-authorized payment on a Clover device.
 */
public class CapturePreAuthRequestIntentBuilder extends BaseIntentBuilder {
  private Long amount;
  private String paymentId;
  private Boolean preferOnScreen = null;
  private SignatureOptions signatureOptions;
  private TipOptions tipOptions;
  private ReceiptOptions receiptOptions;

  /**
   * Creates an instance of the CapturePreAuthRequestIntentBuilder class
   *
   * @param paymentId
   * @param amount
   */
  public CapturePreAuthRequestIntentBuilder(String paymentId, long amount) {
    this.paymentId = paymentId;
    this.amount = amount;
  }

  /**
   * Sets TipOptions, SignatureOptions and preferOnScreen on the CapturePreAuthRequestIntentBuilder object
   *
   * @param tipOptions
   * @param signatureOptions
   * @param preferOnScreen - applies to both tip and signature as preferred location
   * @return CapturePreAuthRequestIntentBuilder object with new TipOptions, SignatureOptions and preferOnScreen
   */
  public CapturePreAuthRequestIntentBuilder tipAndSignatureOptions(CapturePreAuthRequestIntentBuilder.TipOptions tipOptions, CapturePreAuthRequestIntentBuilder.SignatureOptions signatureOptions, Boolean preferOnScreen) {
    this.tipOptions = tipOptions;
    this.signatureOptions = signatureOptions;
    this.preferOnScreen = preferOnScreen;
    return this;
  }

  /**
   * Sets ReceiptOptions on the CapturePreAuthRequestIntentBuilder object
   *
   * @param receiptOptions - @see com.clover.sdk.v3.payments.api.PaymentRequestIntentBuilder.ReceiptOptions
   * @return PaymentRequestIntentBuilder object with new ReceiptOptions
   */
  public CapturePreAuthRequestIntentBuilder receiptOptions(ReceiptOptions receiptOptions) {
    this.receiptOptions = receiptOptions;
    return this;
  }

  /**
   * Builder method to create an Intent to be used by Integrator POS to capture a pre-authorization
   *
   * @param context
   * @return Intent to be used to initiate a capture of a pre-authorization.
   * @throws IllegalArgumentException
   */
  public Intent build(Context context) {
    if (context == null) {
      throw new IllegalArgumentException("context must be populated with a non null value");
    }
    if (paymentId == null) {
      throw new IllegalArgumentException("paymentId must be populated with a non null value");
    }
    if (amount.longValue() <= 0) {
      throw new IllegalArgumentException("amount cannot be less than or equal to zero");
    }

    Intent i = super.build(context);
    i.setComponent(new ComponentName("com.clover.payment.builder.pay", "com.clover.payment.builder.pay.handler.CapturePreAuthRequestHandler"));
    i.putExtra(Intents.EXTRA_AMOUNT, amount);
    i.putExtra(Intents.EXTRA_PAYMENT_ID, paymentId);

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

    if (signatureOptions != null) {
      if (signatureOptions.signatureThreshold != null) {
        i.putExtra(Intents.EXTRA_SIGNATURE_THRESHOLD, signatureOptions.signatureThreshold);
      }
      if (signatureOptions.autoAcceptSignature != null) {
        i.putExtra(Intents.EXTRA_AUTO_ACCEPT_SIGNATURE, signatureOptions.autoAcceptSignature);
      }
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
        for (TipSuggestion tipSuggestion : tipOptions.tipSuggestions) {
          suggestions.add(tipSuggestion.getV3TipSuggestion());
        }
        i.putExtra(Intents.EXTRA_TIP_SUGGESTIONS, (Serializable) suggestions);
      }
    }

    if (preferOnScreen != null) {
        i.putExtra(Intents.EXTRA_API_SIGNATURE_PREFER_ON_SCREEN, preferOnScreen);
        i.putExtra(Intents.EXTRA_API_TIP_PREFER_ON_SCREEN, preferOnScreen);
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

    private TipOptions(Long tipAmount, Long baseAmount, List<TipSuggestion> tipSuggestions) {
      this.tipAmount = tipAmount;
      this.baseAmount = baseAmount;
      this.tipSuggestions = tipSuggestions;
    }

    /**
     * No tip will be taken and tipAmount will default to 0.
     */
    public static TipOptions Disable() {
      return new TipOptions(0L, null, null);
    }

    /**
     * Tips will be provided by Integrator.
     */
    public static TipOptions Provided(long tipAmount) {
      return new TipOptions(tipAmount, null, null);
    }

    /**
     * Customers will be prompted to tip.
     */
    public static TipOptions PromptCustomer(Long baseAmount, List<TipSuggestion> tipSuggestions) {
      return new TipOptions(null, baseAmount, tipSuggestions);
    }
  }

  /**
   * Signature options that allow the Integrator to control how the signature is collected on a per-transaction level.
   */
  public static class SignatureOptions {
    private final Long signatureThreshold;
    private final Boolean autoAcceptSignature;

    private SignatureOptions(Long signatureThreshold, Boolean autoAcceptSignature) {
      this.signatureThreshold = signatureThreshold;
      this.autoAcceptSignature = autoAcceptSignature;
    }

    /**
     * No signature will be collected
     */
    public static SignatureOptions Disable() {
      return new SignatureOptions(Long.MAX_VALUE, null);
    }

    /**
     * Customer will be prompted (on screen or on paper) for signature
     */
    public static SignatureOptions PromptCustomer(Long signatureThreshold, Boolean autoAcceptSignature) {
      return new SignatureOptions(signatureThreshold, autoAcceptSignature);
    }
  }

  /**
   * ReceiptOptions that allow the Integrator to control the receipt selection options on a per-transaction level.
   */
  public static class ReceiptOptions {
    private List<ReceiptOptions.ReceiptOption> providedReceiptOptions;
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
        this.type=ReceiptOptionType.PRINT;
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

}
