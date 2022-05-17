package com.clover.sdk.v3.payments.api;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.clover.sdk.v1.Intents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Use the CapturePreAuthRequestIntentBuilder class to capture a pre-authorized payment on an Android device.
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
   * @param receiptOptions
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
   * @return Android Intent to be used to initiate a capture of a pre-authorization.
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
      i.putExtra(Intents.EXTRA_SKIP_RECEIPT_SCREEN, receiptOptions.skipReceiptSelection);
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
   * Receipt options that allow the Integrator to control the receipt selection on a per-transaction level.
   */
  public static class ReceiptOptions {
    public final Boolean skipReceiptSelection;

    private ReceiptOptions(Boolean skipReceiptSelection) {
      this.skipReceiptSelection = skipReceiptSelection;
    }

    /**
     * Receipt selection screen will be skipped.
     */
    public static ReceiptOptions Disable() {
      return new ReceiptOptions(true);
    }
  }

}
