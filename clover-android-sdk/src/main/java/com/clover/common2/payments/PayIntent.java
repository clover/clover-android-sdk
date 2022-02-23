package com.clover.common2.payments;

/*
  This class contains code to assist in building a PayIntent object.  The Builder class can construct a PayIntent
  using a variety of input sources.

  Some of the Builder setters contain "** Backward Compatibility **" comments that indicate specialized coding to handle
  the deprecated PayIntent fields that were moved into the TransactionSettings object.  The purpose of the code is
  to handle keeping the TransactionSettings object in sync with updates to the deprecated fields.  This can be
  tricky, as a Builder taking an Intent as input (which builds a TransactionSettings object from the intent) and
  then chain on additional setters against any/all of the deprecated fields.  This could produce inconsistencies
  between the deprecated values and the TransactionSettings values.  Since the point of moving these fields to the
  TransactionSettings object was to isolate fields that directly effect the flow or calculation of a payment, we want
  to get rid of the individual field-level extras at some future point and use only the values from the
  TransactionSettings to make decisions or calculations.

  Also note that the TransactionSettingsResolver should be utilized for any field/decision level logic involving the
  TransactionSettings fields.  Never use the TransactionSettings object directly.  This is so that Clover Merchant
  defaults and any other relevant factors involving the TransactionSettings overrides are applied consistently across
  the code base.
*/

import com.clover.sdk.v1.Intents;
import com.clover.sdk.v1.configuration.Themes;
import com.clover.sdk.v3.apps.AppTracking;
import com.clover.sdk.v3.base.Tender;
import com.clover.sdk.v3.payments.Authorization;
import com.clover.sdk.v3.payments.CardTransaction;
import com.clover.sdk.v3.payments.CashAdvanceCustomerIdentification;
import com.clover.sdk.v3.payments.Credit;
import com.clover.sdk.v3.payments.GermanInfo;
import com.clover.sdk.v3.payments.Payment;
import com.clover.sdk.v3.payments.Refund;
import com.clover.sdk.v3.payments.ServiceChargeAmount;
import com.clover.sdk.v3.payments.TaxableAmountRate;
import com.clover.sdk.v3.payments.TokenizeCardRequest;
import com.clover.sdk.v3.payments.TokenizeCardResponse;
import com.clover.sdk.v3.payments.TransactionSettings;
import com.clover.sdk.v3.payments.VasSettings;
import com.clover.sdk.v3.payments.VaultedCard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayIntent implements Parcelable {

  public enum TransactionType {
    PAYMENT(Intents.TRANSACTION_TYPE_PAYMENT),
    CREDIT(Intents.TRANSACTION_TYPE_CREDIT),
    AUTH(Intents.TRANSACTION_TYPE_AUTH),
    DATA(Intents.TRANSACTION_TYPE_CARD_DATA),
    BALANCE_INQUIRY(Intents.TRANSACTION_TYPE_BALANCE_INQUIRY),
    PAYMENT_REVERSAL(Intents.TRANSACTION_TYPE_MANUAL_REVERSAL_PAYMENT),
    PAYMENT_ADJUSTMENT(Intents.TRANSACTION_TYPE_ADJUSTMENT_PAYMENT),
    CREDIT_REVERSAL(Intents.TRANSACTION_TYPE_MANUAL_REVERSAL_REFUND),
    REFUND_ADJUSTMENT(Intents.TRANSACTION_TYPE_ADJUSTMENT_REFUND),
    CASH_ADVANCE(Intents.TRANSACTION_TYPE_CASH_ADVANCE),
    CAPTURE_PREAUTH(Intents.TRANSACTION_TYPE_CAPTURE_PREAUTH),
    VAS_DATA(Intents.TRANSACTION_TYPE_VAS_DATA),
    VERIFY_CARD(Intents.TRANSACTION_TYPE_VERIFY_CARD),
    TOKENIZE_CARD(Intents.TRANSACTION_TYPE_TOKENIZE_CARD)
    ;

    public final String intentValue;

    TransactionType(String intentValue) {
      this.intentValue = intentValue;
    }

    public static TransactionType getForValue(String intentValue) {
      for (TransactionType type : TransactionType.values()) {
        if (type.intentValue.equals(intentValue)) {
          return type;
        }
      }
      return null;
    }
  }

  public static class Builder {
    private String action;
    private Long amount;
    @Deprecated // Please use TransactionSettings
    private Long tippableAmount;
    private Long tipAmount;
    private Long taxAmount;
    private Long cashbackAmount;
    private String orderId;
    private String paymentId;
    private String employeeId;
    private TransactionType transactionType;
    private ArrayList<TaxableAmountRate> taxableAmountRates;
    private ServiceChargeAmount serviceChargeAmount;
    @Deprecated // Please use TransactionSettings
    private boolean isDisableCashBack;
    private boolean isTesting;
    @Deprecated // Please use TransactionSettings
    private int cardEntryMethods;
    private String voiceAuthCode;
    private String postalCode;
    private String streetAddress;
    private boolean isCardNotPresent;
    private String cardDataMessage;
    @Deprecated // Please use TransactionSettings
    private boolean remotePrint;
    @Deprecated // generated internally in the SPS/SPA
    private String transactionNo;
    @Deprecated // Please use TransactionSettings
    private boolean isForceSwipePinEntry;
    @Deprecated // Please use TransactionSettings
    private boolean disableRestartTransactionWhenFailed;
    // Can be set to the properly formatted uuid for a payment (
    private String externalPaymentId;
    private String externalReferenceId;
    private String originatingPaymentPackage;
    private VaultedCard vaultedCard;
    @Deprecated // Please use TransactionSettings
    private Boolean allowOfflinePayment;
    @Deprecated // Please use TransactionSettings
    private Boolean approveOfflinePaymentWithoutPrompt;
    private Boolean requiresRemoteConfirmation;
    private Boolean requiresFinalRemoteApproval;
    private Boolean skipELVLimitOverride;
    private AppTracking applicationTracking;
    private Boolean allowPartialAuth = true;
    private boolean useLastSwipe;
    private GermanInfo germanInfo;
    private String germanELVTransaction;
    private CashAdvanceCustomerIdentification cashAdvanceCustomerIdentification;
    private TransactionSettings transactionSettings;
    private VasSettings vasSettings;
    @Deprecated // use originatingPayment instead
    private CardTransaction originatingTransaction;
    private Themes themeName;
    private Payment originatingPayment;
    private Credit originatingCredit;
    private Refund refund;
    private Tender customerTender;
    // Optional map of values passed through to the server NOT used in payment processing or persisted
    private Map<String, String> passThroughValues;
    //Optional map of application specific values
    private Map<String,String> applicationSpecificValues;
    private boolean isDisableCreditSurcharge;
    private boolean isPresentQrcOnly;
    private boolean isManualCardEntryByPassMode;
    private boolean isAllowManualCardEntryOnMFD;
    private String quickPaymentTransactionUuid;
    private Authorization authorization;
    private TokenizeCardResponse tokenizeCardResponse;
    private TokenizeCardRequest tokenizeCardRequest;

    public Builder intent(Intent intent) {
      action = intent.getAction();

      if (intent.hasExtra(Intents.EXTRA_AMOUNT)) {
        amount = intent.getLongExtra(Intents.EXTRA_AMOUNT, 0L);
      }
      if (intent.hasExtra(Intents.EXTRA_TIPPABLE_AMOUNT)) {
        tippableAmount = intent.getLongExtra(Intents.EXTRA_TIPPABLE_AMOUNT, -1L);
      }

      transactionType = TransactionType.getForValue(intent.getStringExtra(Intents.EXTRA_TRANSACTION_TYPE));
      if (transactionType == null) {
        transactionType = TransactionType.PAYMENT;
      }

      if (intent.hasExtra(Intents.EXTRA_ORDER_ID)) {
        orderId = intent.getStringExtra(Intents.EXTRA_ORDER_ID);
      }
      if (intent.hasExtra(Intents.EXTRA_PAYMENT_ID)){
        paymentId = intent.getStringExtra(Intents.EXTRA_PAYMENT_ID);
      }

      if (intent.hasExtra(Intents.EXTRA_EMPLOYEE_ID)) {
        employeeId = intent.getStringExtra(Intents.EXTRA_EMPLOYEE_ID);
      }
      tipAmount = intent.hasExtra(Intents.EXTRA_TIP_AMOUNT) ? intent.getLongExtra(Intents.EXTRA_TIP_AMOUNT, 0L) : null;
      taxAmount = intent.getLongExtra(Intents.EXTRA_TAX_AMOUNT, 0L);
      cashbackAmount = intent.hasExtra(Intents.EXTRA_CASHBACK_AMOUNT) ? intent.getLongExtra(Intents.EXTRA_CASHBACK_AMOUNT, 0L) : null;

      taxableAmountRates = intent.getParcelableArrayListExtra(Intents.EXTRA_TAXABLE_AMOUNTS);
      serviceChargeAmount = intent.getParcelableExtra(Intents.EXTRA_SERVICE_CHARGE_AMOUNT);
      isDisableCashBack = intent.getBooleanExtra(Intents.EXTRA_DISABLE_CASHBACK, false);
      isTesting = intent.getBooleanExtra(Intents.EXTRA_IS_TESTING, false);
      cardEntryMethods = intent.getIntExtra(Intents.EXTRA_CARD_ENTRY_METHODS, Intents.CARD_ENTRY_METHOD_ALL);
      voiceAuthCode = intent.getStringExtra(Intents.EXTRA_VOICE_AUTH_CODE);
      postalCode = intent.getStringExtra(Intents.EXTRA_AVS_POSTAL_CODE);
      streetAddress = intent.getStringExtra(Intents.EXTRA_AVS_STREET_ADDRESS);
      themeName = (Themes) intent.getSerializableExtra(Intents.EXTRA_THEME_NAME);
      isCardNotPresent = intent.getBooleanExtra(Intents.EXTRA_CARD_NOT_PRESENT, false);
      cardDataMessage = intent.getStringExtra(Intents.EXTRA_CARD_DATA_MESSAGE);
      remotePrint = intent.getBooleanExtra(Intents.EXTRA_REMOTE_PRINT, false);
      transactionNo = intent.getStringExtra(Intents.EXTRA_TRANSACTION_NO);
      isForceSwipePinEntry = intent.getBooleanExtra(Intents.EXTRA_FORCE_SWIPE_PIN_ENTRY, false);
      disableRestartTransactionWhenFailed = intent.getBooleanExtra(
          Intents.EXTRA_DISABLE_RESTART_TRANSACTION_WHEN_FAILED, false);
      externalPaymentId = intent.getStringExtra(
          Intents.EXTRA_EXTERNAL_PAYMENT_ID);
      externalReferenceId = intent.getStringExtra(Intents.EXTRA_EXTERNAL_REFERENCE_ID);
      vaultedCard = intent.getParcelableExtra(Intents.EXTRA_VAULTED_CARD);

      if (intent.hasExtra(Intents.EXTRA_ALLOW_OFFLINE_ACCEPTANCE)) {
        allowOfflinePayment = (Boolean) intent.getExtras().get(Intents.EXTRA_ALLOW_OFFLINE_ACCEPTANCE);
      }
      if (intent.hasExtra(Intents.EXTRA_OFFLINE_NO_PROMPT)) {
        approveOfflinePaymentWithoutPrompt = (Boolean) intent.getExtras().get(Intents.EXTRA_OFFLINE_NO_PROMPT);
      }
      if (intent.hasExtra(Intents.EXTRA_REQUIRES_REMOTE_CONFIRMATION)) {
        requiresRemoteConfirmation = (Boolean) intent.getExtras().get(Intents.EXTRA_REQUIRES_REMOTE_CONFIRMATION);
      }
      if (intent.hasExtra(Intents.EXTRA_REQUIRES_FINAL_REMOTE_APPROVAL)) {
        requiresFinalRemoteApproval = (Boolean) intent.getExtras().get(Intents.EXTRA_REQUIRES_FINAL_REMOTE_APPROVAL);
      }
      if (intent.hasExtra(Intents.EXTRA_SKIP_ELV_LIMIT_OVERRIDE)) {
        skipELVLimitOverride = (Boolean) intent.getExtras().get(Intents.EXTRA_SKIP_ELV_LIMIT_OVERRIDE);
      }
      if (intent.hasExtra(Intents.EXTRA_APP_TRACKING_ID)) {
        applicationTracking = intent.getParcelableExtra(Intents.EXTRA_APP_TRACKING_ID);
      }
      if (intent.hasExtra(Intents.EXTRA_ALLOW_PARTIAL_AUTH)) {
        allowPartialAuth = intent.getBooleanExtra(Intents.EXTRA_ALLOW_PARTIAL_AUTH, true);
      }
      if (intent.hasExtra(Intents.EXTRA_USE_LAST_SWIPE)) {
        useLastSwipe = intent.getBooleanExtra(Intents.EXTRA_USE_LAST_SWIPE, false);
      }
      if (intent.hasExtra(Intents.GERMAN_INFO)) {
        germanInfo = intent.getParcelableExtra(Intents.GERMAN_INFO);
      }
      if (intent.hasExtra(Intents.EXTRA_GERMAN_ELV)) {
        germanELVTransaction = intent.getStringExtra(Intents.EXTRA_GERMAN_ELV);
      }
      if (intent.hasExtra(Intents.CASHADVANCE_CUSTOMER_IDENTIFICATION)) {
        cashAdvanceCustomerIdentification = intent.getParcelableExtra(Intents.CASHADVANCE_CUSTOMER_IDENTIFICATION);
      }
      if (intent.hasExtra(Intents.EXTRA_TRANSACTION_SETTINGS)) {
        transactionSettings = intent.getParcelableExtra(Intents.EXTRA_TRANSACTION_SETTINGS);
      } else { //move the settings into the transactionSettings object for deprecated intents ** TEMPORARY
        transactionSettings = buildTransactionSettingsFromIntentValues();
      }
      if (intent.hasExtra(Intents.EXTRA_VAS_SETTINGS)) {
        vasSettings = intent.getParcelableExtra(Intents.EXTRA_VAS_SETTINGS);
      }
      if (intent.hasExtra(Intents.EXTRA_ORIGINATING_PAYMENT)) {
        originatingPayment = intent.getParcelableExtra(Intents.EXTRA_ORIGINATING_PAYMENT);
        if (originatingPayment != null) {
          originatingTransaction = originatingPayment.getCardTransaction();
        }
      } else if (intent.hasExtra(Intents.EXTRA_ORIGINATING_TRANSACTION)) {
        originatingTransaction = intent.getParcelableExtra(Intents.EXTRA_ORIGINATING_TRANSACTION);
      }
      if (intent.hasExtra(Intents.EXTRA_ORIGINATING_CREDIT)) {
        originatingCredit = intent.getParcelableExtra(Intents.EXTRA_ORIGINATING_CREDIT);
      }
      if (intent.hasExtra(Intents.EXTRA_PASS_THROUGH_VALUES)) {
        passThroughValues = (Map<String, String>) intent.getSerializableExtra(Intents.EXTRA_PASS_THROUGH_VALUES);
      }
      if (intent.hasExtra(Intents.EXTRA_APPLICATION_SPECIFIC_VALUES)) {
        applicationSpecificValues = (Map<String, String>) intent.getSerializableExtra(Intents.EXTRA_APPLICATION_SPECIFIC_VALUES);
      }
      if (intent.hasExtra(Intents.EXTRA_REFUND)) {
        refund = intent.getParcelableExtra(Intents.EXTRA_REFUND);
      }
      if (intent.hasExtra(Intents.EXTRA_CUSTOMER_TENDER)) {
        customerTender = intent.getParcelableExtra(Intents.EXTRA_CUSTOMER_TENDER);
      }
      if (intent.hasExtra(Intents.EXTRA_ORIGINATING_PAYMENT_PACKAGE)) {
        originatingPaymentPackage = intent.getStringExtra(Intents.EXTRA_ORIGINATING_PAYMENT_PACKAGE);
      }
      if (intent.hasExtra(Intents.EXTRA_DISABLE_CREDIT_SURCHARGE)) {
        isDisableCreditSurcharge = intent.getBooleanExtra(Intents.EXTRA_DISABLE_CREDIT_SURCHARGE, false);
      }
      if (intent.hasExtra(Intents.EXTRA_PRESENT_QRC_ONLY)) {
        isPresentQrcOnly = intent.getBooleanExtra(Intents.EXTRA_PRESENT_QRC_ONLY, false);
      }

      if (intent.hasExtra(Intents.EXTRA_MANUAL_CARD_ENTRY_BYPASS_MODE)) {
        isManualCardEntryByPassMode = intent.getBooleanExtra(Intents.EXTRA_MANUAL_CARD_ENTRY_BYPASS_MODE, false);
      }

      if (intent.hasExtra(Intents.EXTRA_ALLOW_MANUAL_CARD_ENTRY_ON_MFD)) {
        isAllowManualCardEntryOnMFD = intent.getBooleanExtra(Intents.EXTRA_ALLOW_MANUAL_CARD_ENTRY_ON_MFD, false);
      }

      quickPaymentTransactionUuid = intent.getStringExtra(Intents.EXTRA_QUICK_PAYMENT_TRANSACTION_ID);

      if (intent.hasExtra(Intents.EXTRA_AUTHORIZATION)) {
        authorization = intent.getParcelableExtra(Intents.EXTRA_AUTHORIZATION);
      }

      tokenizeCardRequest = intent.getParcelableExtra(Intents.EXTRA_C_TOKEN_RESULT);
      tokenizeCardResponse = intent.getParcelableExtra(Intents.EXTRA_C_TOKEN_REQUEST);

      return this;
    }

    private TransactionSettings buildTransactionSettingsFromIntentValues() {
      TransactionSettings transactionSettings = new TransactionSettings();

      transactionSettings.setCloverShouldHandleReceipts(!remotePrint);
      transactionSettings.setDisableRestartTransactionOnFailure(disableRestartTransactionWhenFailed);
      transactionSettings.setForcePinEntryOnSwipe(isForceSwipePinEntry);
      transactionSettings.setDisableCashBack(isDisableCashBack);
      transactionSettings.setAllowOfflinePayment(allowOfflinePayment);
      transactionSettings.setApproveOfflinePaymentWithoutPrompt(approveOfflinePaymentWithoutPrompt);
      transactionSettings.setCardEntryMethods(cardEntryMethods);
      transactionSettings.setDisableDuplicateCheck(false);
      transactionSettings.setDisableReceiptSelection(false);
      transactionSettings.setSignatureEntryLocation(null); // will default to clover setting
      transactionSettings.setTipMode(null); // will default to clover setting
      transactionSettings.setTippableAmount(tippableAmount);
      transactionSettings.setDisableCreditSurcharge(isDisableCreditSurcharge);

      return transactionSettings;
    }

    public TransactionSettings buildTransactionSettingsFromIntent(Intent intent) {

      TransactionSettings transactionSettings = new TransactionSettings();

      if (intent.hasExtra(Intents.EXTRA_AMOUNT)) {
        amount = intent.getLongExtra(Intents.EXTRA_AMOUNT, 0L);
      }
      transactionSettings.setCloverShouldHandleReceipts(!intent.getBooleanExtra(Intents.EXTRA_REMOTE_PRINT, false));
      transactionSettings.setDisableRestartTransactionOnFailure(intent.getBooleanExtra(
          Intents.EXTRA_DISABLE_RESTART_TRANSACTION_WHEN_FAILED, false));
      transactionSettings.setForcePinEntryOnSwipe(intent.getBooleanExtra(Intents.EXTRA_FORCE_SWIPE_PIN_ENTRY, false));
      transactionSettings.setDisableCashBack(intent.getBooleanExtra(Intents.EXTRA_DISABLE_CASHBACK, false));
      if(intent.hasExtra(Intents.EXTRA_ALLOW_OFFLINE_ACCEPTANCE)) {
        transactionSettings.setAllowOfflinePayment((Boolean) intent.getExtras().get(Intents.EXTRA_ALLOW_OFFLINE_ACCEPTANCE));
      }
      if (intent.hasExtra(Intents.EXTRA_OFFLINE_NO_PROMPT)) {
        transactionSettings.setApproveOfflinePaymentWithoutPrompt((Boolean) intent.getExtras().get(Intents.EXTRA_OFFLINE_NO_PROMPT));
      }
      transactionSettings.setCardEntryMethods(intent.getIntExtra(Intents.EXTRA_CARD_ENTRY_METHODS, Intents.CARD_ENTRY_METHOD_ALL));
      transactionSettings.setDisableDuplicateCheck(false);
      transactionSettings.setDisableReceiptSelection(false);
      transactionSettings.setSignatureEntryLocation(null); // will default to clover setting
      transactionSettings.setTipMode(null); // will default to clover setting
      if (intent.hasExtra(Intents.EXTRA_DISABLE_CREDIT_SURCHARGE)) {
        transactionSettings.setDisableCreditSurcharge(intent.getBooleanExtra(Intents.EXTRA_DISABLE_CREDIT_SURCHARGE, false));
      }

      return transactionSettings;
    }

    public TransactionSettings buildTransactionSettingsFromPayIntent(PayIntent payIntent) {
      TransactionSettings transactionSettings = new TransactionSettings();

      transactionSettings.setCloverShouldHandleReceipts(!payIntent.remotePrint);
      transactionSettings.setDisableRestartTransactionOnFailure(payIntent.disableRestartTransactionWhenFailed);
      transactionSettings.setForcePinEntryOnSwipe(payIntent.isForceSwipePinEntry);
      transactionSettings.setDisableCashBack(payIntent.isDisableCashBack);
      transactionSettings.setAllowOfflinePayment(payIntent.allowOfflinePayment);
      transactionSettings.setApproveOfflinePaymentWithoutPrompt(payIntent.approveOfflinePaymentWithoutPrompt);
      transactionSettings.setCardEntryMethods(payIntent.cardEntryMethods);
      transactionSettings.setDisableDuplicateCheck(false); // default
      transactionSettings.setDisableReceiptSelection(false); // default
      transactionSettings.setSignatureEntryLocation(null); // will default to clover setting
      transactionSettings.setTipMode(null); // will default to clover setting
      transactionSettings.setTippableAmount(payIntent.tippableAmount);
      transactionSettings.setDisableCreditSurcharge(payIntent.isDisableCreditSurcharge);

      return transactionSettings;
    }

    public Builder payment(Payment payment) {
      this.amount = payment.getAmount();
      this.tipAmount = payment.getTipAmount();
      this.taxAmount = payment.getTaxAmount();
      this.employeeId = payment.getEmployee().getId();
      this.transactionNo = payment.hasCardTransaction() ? payment.getCardTransaction().getTransactionNo() : null;
      this.transactionSettings = payment.getTransactionSettings();
      return this;
    }

    public Builder payIntent(PayIntent payIntent) {
      this.action = payIntent.action;
      this.amount = payIntent.amount;
      this.tippableAmount = payIntent.tippableAmount;
      this.tipAmount = payIntent.tipAmount;
      this.taxAmount = payIntent.taxAmount;
      this.cashbackAmount = payIntent.cashbackAmount;
      this.orderId = payIntent.orderId;
      this.paymentId = payIntent.paymentId;
      this.employeeId = payIntent.employeeId;
      this.transactionType = payIntent.transactionType;
      this.taxableAmountRates = payIntent.taxableAmountRateList;
      this.serviceChargeAmount = payIntent.serviceChargeAmount;
      this.isDisableCashBack = payIntent.isDisableCashBack;
      this.isTesting = payIntent.isTesting;
      this.cardEntryMethods = payIntent.cardEntryMethods;
      this.voiceAuthCode = payIntent.voiceAuthCode;
      this.postalCode = payIntent.postalCode;
      this.streetAddress = payIntent.streetAddress;
      this.isCardNotPresent = payIntent.isCardNotPresent;
      this.cardDataMessage = payIntent.cardDataMessage;
      this.remotePrint = payIntent.remotePrint;
      this.transactionNo = payIntent.transactionNo;
      this.isForceSwipePinEntry = payIntent.isForceSwipePinEntry;
      this.disableRestartTransactionWhenFailed = payIntent.disableRestartTransactionWhenFailed;
      this.externalPaymentId = payIntent.externalPaymentId;
      this.externalReferenceId = payIntent.externalReferenceId;
      this.originatingPaymentPackage = payIntent.originatingPaymentPackage;
      this.vaultedCard = payIntent.vaultedCard;
      this.allowOfflinePayment = payIntent.allowOfflinePayment;
      this.approveOfflinePaymentWithoutPrompt = payIntent.approveOfflinePaymentWithoutPrompt;
      this.requiresRemoteConfirmation = payIntent.requiresRemoteConfirmation;
      this.requiresFinalRemoteApproval = payIntent.requiresFinalRemoteApproval;
      this.skipELVLimitOverride = payIntent.skipELVLimitOverride;
      this.applicationTracking = payIntent.applicationTracking;
      this.allowPartialAuth = payIntent.allowPartialAuth;
      this.useLastSwipe = payIntent.useLastSwipe;
      this.themeName = payIntent.themeName;
      this.germanInfo = payIntent.germanInfo;
      this.germanELVTransaction = payIntent.germanELVTransaction;
      if (payIntent.transactionSettings != null) {
        this.transactionSettings = payIntent.transactionSettings;
      } else {
        this.transactionSettings = buildTransactionSettingsFromPayIntent(payIntent);
      }
      this.cashAdvanceCustomerIdentification = payIntent.cashAdvanceCustomerIdentification;
      this.vasSettings = payIntent.vasSettings;
      this.originatingPayment = payIntent.originatingPayment;
      if (this.originatingPayment != null) {
        this.originatingTransaction = this.originatingPayment.getCardTransaction();
      } else {
        this.originatingTransaction = payIntent.originatingTransaction;
      }
      this.originatingCredit = payIntent.originatingCredit;
      this.passThroughValues = payIntent.passThroughValues;
      if (payIntent.applicationSpecificValues != null) {
        this.applicationSpecificValues = new HashMap<>(payIntent.applicationSpecificValues);
      }
      this.refund = payIntent.refund;
      this.customerTender = payIntent.customerTender;
      this.isDisableCreditSurcharge = payIntent.isDisableCreditSurcharge;
      this.isPresentQrcOnly = payIntent.isPresentQrcOnly;
      this.isManualCardEntryByPassMode = payIntent.isManualCardEntryByPassMode;
      this.isAllowManualCardEntryOnMFD = payIntent.isAllowManualCardEntryOnMFD;
      this.quickPaymentTransactionUuid = payIntent.quickPaymentTransactionUuid;
      this.authorization = payIntent.authorization;
      this.tokenizeCardRequest = payIntent.tokenizeCardRequest;
      this.tokenizeCardResponse = payIntent.tokenizeCardResponse;
      return this;
    }

    public Builder action(String action) {
      this.action = action;
      return this;
    }

    public Builder amount(long amount) {
      this.amount = amount;
      return this;
    }

    @Deprecated
    public Builder tippableAmount(long tippableAmount) {
      this.tippableAmount = tippableAmount;
      if (transactionSettings != null) { // ** Backward Compatibility **
        transactionSettings.setTippableAmount(tippableAmount);
      }
      return this;
    }

    public Builder taxAmount(long taxAmount) {
      this.taxAmount = taxAmount;
      return this;
    }

    public Builder employeeId(String employeeId) {
      this.employeeId = employeeId;
      return this;
    }

    public Builder tipAmount(long tipAmount) {
      this.tipAmount = tipAmount;
      return this;
    }

    public Builder cashbackAmount(long cashbackAmount) {
      this.cashbackAmount = cashbackAmount;
      return this;
    }

    public Builder transactionType(TransactionType transactionType) {
      this.transactionType = transactionType;
      return this;
    }

    @Deprecated
    public Builder cardEntryMethods(int cardEntryMethods) {
      this.cardEntryMethods = cardEntryMethods;
      return this;
    }

    public Builder cardDataMessage(String cardDataMessage) {
      this.cardDataMessage = cardDataMessage;
      return this;
    }

    public Builder taxableAmountRates(List<TaxableAmountRate> taxableAmountRates) {
      this.taxableAmountRates = new ArrayList<TaxableAmountRate>(taxableAmountRates);
      return this;
    }

    public Builder serviceChargeAmount(ServiceChargeAmount serviceChargeAmount) {
      this.serviceChargeAmount = serviceChargeAmount;
      return this;
    }

    public Builder orderId(String orderId) {
      this.orderId = orderId;
      return this;
    }

    public Builder paymentId(String paymentId){
      this.paymentId = paymentId;
      return this;
    }

    @Deprecated
    public Builder remotePrint(boolean remotePrint) {
      this.remotePrint = remotePrint;
      if (transactionSettings != null) { // ** Backward Compatibility **
        transactionSettings.setCloverShouldHandleReceipts(!remotePrint);
      }
      return this;
    }

    @Deprecated
    public Builder disableCashback(boolean disableCashBack) {
      this.isDisableCashBack = disableCashBack;
      if (transactionSettings != null) { // ** Backward Compatibility **
        transactionSettings.setDisableCashBack(disableCashBack);
      }
      return this;
    }

    /**
     * @deprecated , generated internally in the SPS/SPA
     */
    @Deprecated
    public Builder transactionNo(String transactionNo) {
      this.transactionNo = transactionNo;
      return this;
    }

    @Deprecated
    public Builder forceSwipePinEntry(boolean isForceSwipePinEntry) {
      this.isForceSwipePinEntry = isForceSwipePinEntry;
      if (transactionSettings != null) { // ** Backward Compatibility **
        transactionSettings.setForcePinEntryOnSwipe(isForceSwipePinEntry);
      }
      return this;
    }

    @Deprecated
    public Builder disableRestartTransactionWhenFailed(boolean disableRestartTransactionWhenFailed) {
      this.disableRestartTransactionWhenFailed = disableRestartTransactionWhenFailed;
      if (transactionSettings != null) { // ** Backward Compatibility **
        transactionSettings.setDisableRestartTransactionOnFailure(disableRestartTransactionWhenFailed);
      }
      return this;
    }

    public Builder externalPaymentId(String externalPaymentId) {
      this.externalPaymentId = externalPaymentId;
      return this;
    }

    public Builder externalReferenceId(String externalReferenceId) {
      this.externalReferenceId = externalReferenceId;
      return this;
    }

    public Builder originatingPaymentPackage(String originatingPaymentPackage) {
      this.originatingPaymentPackage = originatingPaymentPackage;
      return this;
    }

    public Builder vaultedCard(VaultedCard vaultedCard) {
      this.vaultedCard = vaultedCard;
      return this;
    }

    @Deprecated
    public Builder allowOfflinePayment(Boolean allowOfflinePayment) {
      this.allowOfflinePayment = allowOfflinePayment;
      if (transactionSettings != null) { // ** Backward Compatibility **
        transactionSettings.setAllowOfflinePayment(allowOfflinePayment);
      }
      return this;
    }

    @Deprecated
    public Builder approveOfflinePaymentWithoutPrompt(Boolean approveOfflinePaymentWithoutPrompt) {
      this.approveOfflinePaymentWithoutPrompt = approveOfflinePaymentWithoutPrompt;
      if (transactionSettings != null) { // ** Backward Compatibility **
        transactionSettings.setApproveOfflinePaymentWithoutPrompt(approveOfflinePaymentWithoutPrompt);
      }
      return this;
    }

    public Builder themeName(Themes themeName) {
      this.themeName = themeName;
      return this;
    }

    public Builder requiresRemoteConfirmation(Boolean requiresRemoteConfirmation) {
      this.requiresRemoteConfirmation = requiresRemoteConfirmation;
      return this;
    }

    public Builder requiresFinalRemoteApproval(Boolean requiresFinalRemoteApproval) {
      this.requiresFinalRemoteApproval = requiresFinalRemoteApproval;
      return this;
    }

    public Builder skipELVLimitOverride(Boolean skipELVLimitOverride) {
      this.skipELVLimitOverride = skipELVLimitOverride;
      return this;
    }

    public Builder applicationTracking(AppTracking applicationTracking) {
      this.applicationTracking = applicationTracking;
      return this;
    }

    public Builder allowPartialAuth(boolean allowPartialAuth) {
      this.allowPartialAuth = allowPartialAuth;
      return this;
    }

    public Builder useLastSwipe(boolean useLastSwipe) {
      this.useLastSwipe = useLastSwipe;
      return this;
    }

    public Builder germanInfo(GermanInfo germanInfo) {
      this.germanInfo = germanInfo;
      return this;
    }

    public Builder germanELVTransaction(String germanELVTransaction) {
      this.germanELVTransaction = germanELVTransaction;
      return this;
    }

    public Builder customerIdentification(CashAdvanceCustomerIdentification customerIdentification) {
      this.cashAdvanceCustomerIdentification = customerIdentification;
      return this;
    }

    public Builder transactionSettings(TransactionSettings transactionSettings) {
      this.transactionSettings = transactionSettings;
      return this;
    }

    public Builder cardNotPresent(boolean cardNotPresent) {
      this.isCardNotPresent = cardNotPresent;
      return this;
    }

    public Builder vasSettings(VasSettings vasSettings) {
      this.vasSettings = vasSettings;
      return this;
    }

    /**
     * @deprecated pass originatingPayment instead, originating tx will be auto-populated from the payment card tx
     */
    public Builder originatingTransaction(CardTransaction originatingTransaction) {
      this.originatingTransaction = originatingTransaction;
      return this;
    }

    public Builder originatingPayment(Payment originatingPayment) {
      this.originatingPayment = originatingPayment;
      if (this.originatingPayment != null) {
        this.originatingTransaction = originatingPayment.getCardTransaction();
      }
      return this;
    }

    public Builder originatingCredit(Credit originatingCredit) {
      this.originatingCredit = originatingCredit;
      return this;
    }

    public Builder passThroughValues(Map<String, String> originatingPassThroughValues) {
      boolean create = originatingPassThroughValues != null && !(originatingPassThroughValues instanceof Serializable);
      this.passThroughValues = create ? new HashMap<>(originatingPassThroughValues) : originatingPassThroughValues;
      return this;
    }

    public Builder applicationSpecificValues(Map<String, String> originatingappSpecificValues) {
      boolean create = originatingappSpecificValues != null && !(originatingappSpecificValues instanceof Serializable);
      this.applicationSpecificValues = create ? new HashMap<>(originatingappSpecificValues) : originatingappSpecificValues;
      return this;
    }

    public Builder refund(Refund refund) {
      this.refund = refund;
      return this;
    }

    /**
     * Pre-selected customer tender. If present, pay with this tender.
     * The customer will not have the option to select a different tender or pay with a card.
     * If the tender is not valid for this merchant this extra is ignored.
     *
     * @see Intents#ACTION_CUSTOMER_TENDER
     */
    public Builder customerTender(Tender customerTender) {
      this.customerTender = customerTender;
      return this;
    }

    public Builder disableCreditSurcharge(boolean disableCreditSurcharge) {
      this.isDisableCreditSurcharge = disableCreditSurcharge;
      if (transactionSettings != null) { // ** Backward Compatibility **
        transactionSettings.setDisableCreditSurcharge(disableCreditSurcharge);
      }
      return this;
    }

    public  Builder isPresentQrcOnly(boolean isPresentQrcOnly) {
      this.isPresentQrcOnly = isPresentQrcOnly;
      return this;
    }

    public  Builder isManualCardEntryByPassMode(boolean isManualCardEntryByPassMode) {
      this.isManualCardEntryByPassMode = isManualCardEntryByPassMode;
      return this;
    }

    public  Builder isAllowManualCardEntryOnMFD(boolean isAllowManualCardEntryOnMFD) {
      this.isAllowManualCardEntryOnMFD = isAllowManualCardEntryOnMFD;
      return this;
    }

    public Builder quickPaymentTransactionUuid(String quickPaymentTransactionUuid) {
      this.quickPaymentTransactionUuid = quickPaymentTransactionUuid;
      return this;
    }

    public Builder tokenRequest(TokenizeCardRequest tokenizeCardRequest) {
      this.tokenizeCardRequest = tokenizeCardRequest;
      return this;
    }

    public Builder tokenResponse(TokenizeCardResponse tokenizeCardResponse) {
      this.tokenizeCardResponse = tokenizeCardResponse;
      return this;
    }

    @Deprecated
    public Builder testing(boolean isTesting) {
      this.isTesting = isTesting;
      return this;
    }

    public PayIntent build() {
      return new PayIntent(action, amount, tippableAmount, tipAmount, taxAmount, cashbackAmount, orderId, paymentId, employeeId,
          transactionType, taxableAmountRates, serviceChargeAmount, isDisableCashBack, isTesting, cardEntryMethods,
          voiceAuthCode, postalCode, streetAddress, isCardNotPresent, cardDataMessage, remotePrint, transactionNo,
          isForceSwipePinEntry, disableRestartTransactionWhenFailed, externalPaymentId, externalReferenceId, originatingPaymentPackage, vaultedCard, allowOfflinePayment,
          approveOfflinePaymentWithoutPrompt, requiresRemoteConfirmation, requiresFinalRemoteApproval, skipELVLimitOverride, applicationTracking, allowPartialAuth, useLastSwipe, germanInfo,
          germanELVTransaction, cashAdvanceCustomerIdentification, transactionSettings, vasSettings,
          originatingPayment != null ? originatingPayment.getCardTransaction() : originatingTransaction,
          themeName, originatingPayment, originatingCredit, passThroughValues, applicationSpecificValues, refund,
          customerTender, isDisableCreditSurcharge, isPresentQrcOnly, isManualCardEntryByPassMode,isAllowManualCardEntryOnMFD, quickPaymentTransactionUuid,
          authorization,tokenizeCardRequest,tokenizeCardResponse);
    }
  }

  public final String action;
  public final Long amount;
  @Deprecated // Please use TransactionSettings
  public final Long tippableAmount;
  public final Long tipAmount;
  public final Long taxAmount;
  public final Long cashbackAmount;
  public final String orderId;
  public final String paymentId;
  public final String employeeId;
  public final TransactionType transactionType;
  public final ArrayList<TaxableAmountRate> taxableAmountRateList;
  public final ServiceChargeAmount serviceChargeAmount;
  @Deprecated // Please use TransactionSettings
  public final boolean isDisableCashBack;
  public final boolean isTesting;
  @Deprecated // Please use TransactionSettings
  public final int cardEntryMethods;
  public final String voiceAuthCode;
  public final String postalCode;
  public final String streetAddress;
  public final boolean isCardNotPresent;
  public final String cardDataMessage;
  @Deprecated // Please use TransactionSettings
  public final boolean remotePrint;
  /**
   * @deprecated , generated internally in the SPS/SPA
   */
  @Deprecated
  public final String transactionNo;
  @Deprecated // Please use TransactionSettings
  public final boolean isForceSwipePinEntry;
  @Deprecated // Please use TransactionSettings
  public final boolean disableRestartTransactionWhenFailed;
  public final String externalPaymentId;
  public final String externalReferenceId;
  public final String originatingPaymentPackage;
  public final VaultedCard vaultedCard;
  @Deprecated // Please use TransactionSettings
  public final Boolean allowOfflinePayment;
  @Deprecated // Please use TransactionSettings
  public final Boolean approveOfflinePaymentWithoutPrompt;
  public final Boolean requiresRemoteConfirmation;
  public final Boolean requiresFinalRemoteApproval;
  public final Boolean skipELVLimitOverride;
  public final AppTracking applicationTracking;
  public final boolean allowPartialAuth;
  public final boolean useLastSwipe;
  public final Themes themeName;
  public final GermanInfo germanInfo;
  public final String germanELVTransaction;
  public final CashAdvanceCustomerIdentification cashAdvanceCustomerIdentification;
  public final TransactionSettings transactionSettings;
  public final VasSettings vasSettings;
  public final CardTransaction originatingTransaction;
  public final Payment originatingPayment;
  public final Credit originatingCredit;
  public final Refund refund;
  public final Tender customerTender;
  public final Map<String, String> passThroughValues;
  public final Map<String, String> applicationSpecificValues;
  public boolean isDisableCreditSurcharge;
  public boolean isPresentQrcOnly;
  public boolean isManualCardEntryByPassMode;
  public boolean isAllowManualCardEntryOnMFD;
  public final String quickPaymentTransactionUuid;
  public Authorization authorization;
  public TokenizeCardRequest tokenizeCardRequest;
  public TokenizeCardResponse tokenizeCardResponse;


  private PayIntent(String action, Long amount, Long tippableAmount,
                    Long tipAmount, Long taxAmount, Long cashbackAmount, String orderId, String paymentId, String employeeId,
                    TransactionType transactionType, ArrayList<TaxableAmountRate> taxableAmountRateList,
                    ServiceChargeAmount serviceChargeAmount, boolean isDisableCashBack, boolean isTesting,
                    int cardEntryMethods, String voiceAuthCode, String postalCode, String streetAddress,
                    boolean isCardNotPresent, String cardDataMessage, boolean remotePrint, String transactionNo,
                    boolean isForceSwipePinEntry, boolean disableRestartTransactionWhenFailed, String externalPaymentId, String externalReferenceId, String originatingPaymentPackage,
                    VaultedCard vaultedCard, Boolean allowOfflinePayment, Boolean approveOfflinePaymentWithoutPrompt,
                    Boolean requiresRemoteConfirmation, Boolean requiresFinalRemoteApproval, Boolean skipELVLimitOverride,
                    AppTracking applicationTracking, boolean allowPartialAuth, boolean useLastSwipe,
                    GermanInfo germanInfo, String germanELVTransaction, CashAdvanceCustomerIdentification cashAdvanceCustomerIdentification,
                    TransactionSettings transactionSettings, VasSettings vasSettings, CardTransaction originatingTransaction,
                    Themes themeName, Payment originatingPayment, Credit originatingCredit, Map<String, String> passThroughValues,
                    Map<String, String> applicationSpecificValues, Refund refund, Tender customerTender, boolean isDisableCreditSurcharge,
                    boolean isPresntQrcOnly, boolean isManualCardEntryByPassMode, boolean isAllowManualCardEntryOnMFD, String quickPaymentTransactionUuid,
                    Authorization authorization,TokenizeCardRequest tokenizeCardRequest, TokenizeCardResponse tokenizeCardResponse) {
    this.action = action;
    this.amount = amount;
    this.tippableAmount = tippableAmount;
    this.tipAmount = tipAmount;
    this.taxAmount = taxAmount;
    this.cashbackAmount = cashbackAmount;
    this.orderId = orderId;
    this.paymentId = paymentId;
    this.employeeId = employeeId;
    this.transactionType = transactionType;
    this.taxableAmountRateList = taxableAmountRateList;
    this.serviceChargeAmount = serviceChargeAmount;
    this.isDisableCashBack = isDisableCashBack;
    this.isTesting = isTesting;
    this.cardEntryMethods = cardEntryMethods;
    this.voiceAuthCode = voiceAuthCode;
    this.postalCode = postalCode;
    this.streetAddress = streetAddress;
    this.isCardNotPresent = isCardNotPresent;
    this.cardDataMessage = cardDataMessage;
    this.remotePrint = remotePrint;
    this.transactionNo = transactionNo;
    this.isForceSwipePinEntry = isForceSwipePinEntry;
    this.disableRestartTransactionWhenFailed = disableRestartTransactionWhenFailed;
    this.externalPaymentId = externalPaymentId;
    this.externalReferenceId = externalReferenceId;
    this.originatingPaymentPackage = originatingPaymentPackage;
    this.vaultedCard = vaultedCard;
    this.allowOfflinePayment = allowOfflinePayment;
    this.approveOfflinePaymentWithoutPrompt = approveOfflinePaymentWithoutPrompt;
    this.requiresRemoteConfirmation = requiresRemoteConfirmation;
    this.requiresFinalRemoteApproval = requiresFinalRemoteApproval;
    this.skipELVLimitOverride = skipELVLimitOverride;
    this.applicationTracking = applicationTracking;
    this.allowPartialAuth = allowPartialAuth;
    this.useLastSwipe = useLastSwipe;
    this.themeName = themeName;
    this.germanInfo = germanInfo;
    this.germanELVTransaction = germanELVTransaction;
    this.cashAdvanceCustomerIdentification = cashAdvanceCustomerIdentification;
    this.originatingTransaction = originatingTransaction;
    this.originatingPayment = originatingPayment;
    this.originatingCredit = originatingCredit;
    this.refund = refund;
    this.customerTender = customerTender;
    this.isDisableCreditSurcharge = isDisableCreditSurcharge;
    this.isPresentQrcOnly = isPresntQrcOnly;
    this.isManualCardEntryByPassMode =  isManualCardEntryByPassMode;
    this.isAllowManualCardEntryOnMFD = isAllowManualCardEntryOnMFD;
    this.quickPaymentTransactionUuid = quickPaymentTransactionUuid;
    this.authorization = authorization;
    this.tokenizeCardResponse = tokenizeCardResponse;
    this.tokenizeCardRequest = tokenizeCardRequest;

    if (transactionSettings != null) {
      this.transactionSettings = transactionSettings;
    } else {
      this.transactionSettings = buildTransactionSettingsPrivate(tippableAmount, isDisableCashBack,
          cardEntryMethods, remotePrint, isForceSwipePinEntry, disableRestartTransactionWhenFailed, allowOfflinePayment,
          approveOfflinePaymentWithoutPrompt);
    }

    this.vasSettings = vasSettings;
    this.passThroughValues = passThroughValues;
    this.applicationSpecificValues = applicationSpecificValues;
  }

  private TransactionSettings buildTransactionSettingsPrivate(Long tippableAmountIn, boolean isDisableCashBackIn, int cardEntryMethodsIn,
                                                              boolean remotePrintIn, boolean isForceSwipePinEntryIn,
                                                              boolean disableRestartTransactionWhenFailedIn, Boolean allowOfflinePaymentIn,
                                                              Boolean approveOfflinePaymentWithoutPromptIn) {
    TransactionSettings transactionSettings = new TransactionSettings();

    transactionSettings.setCloverShouldHandleReceipts(!remotePrintIn);
    transactionSettings.setDisableRestartTransactionOnFailure(disableRestartTransactionWhenFailedIn);
    transactionSettings.setForcePinEntryOnSwipe(isForceSwipePinEntryIn);
    transactionSettings.setDisableCashBack(isDisableCashBackIn);
    transactionSettings.setAllowOfflinePayment(allowOfflinePaymentIn);
    transactionSettings.setApproveOfflinePaymentWithoutPrompt(approveOfflinePaymentWithoutPromptIn);
    transactionSettings.setCardEntryMethods(cardEntryMethodsIn);
    transactionSettings.setDisableDuplicateCheck(false);
    transactionSettings.setDisableReceiptSelection(false);
    transactionSettings.setSignatureEntryLocation(null); // will default to clover setting
    transactionSettings.setTipMode(null); // will default to clover setting
    transactionSettings.setTippableAmount(tippableAmountIn);
    transactionSettings.setDisableCreditSurcharge(isDisableCreditSurcharge);

    return transactionSettings;
  }


  public String toLogMessage() {
    StringBuilder s = new StringBuilder();
    s
        .append("Transaction details:\n")
        .append("\tOrder ID: ").append(orderId).append("\n")
        .append("\tPayment ID: ").append(paymentId).append("\n")
        .append("\tEmployee ID: ").append(employeeId).append("\n")
        .append("\tAmount: ").append(amount).append("\n")
        .append("\tTransaction type: ").append(transactionType).append("\n");

    return s.toString();
  }

  public void addTo(Intent intent) {
    intent.setExtrasClassLoader(PayIntent.class.getClassLoader());

    if (action != null) {
      intent.setAction(action);
    }
    if (amount != null) {
      intent.putExtra(Intents.EXTRA_AMOUNT, amount);
    }
    if (tippableAmount != null) {
      intent.putExtra(Intents.EXTRA_TIPPABLE_AMOUNT, tippableAmount);
    }
    if (tipAmount != null) {
      intent.putExtra(Intents.EXTRA_TIP_AMOUNT, tipAmount);
    }
    if (taxAmount != null) {
      intent.putExtra(Intents.EXTRA_TAX_AMOUNT, taxAmount);
    }
    if (cashbackAmount != null) {
      intent.putExtra(Intents.EXTRA_CASHBACK_AMOUNT, cashbackAmount);
    }
    if (orderId != null) {
      intent.putExtra(Intents.EXTRA_ORDER_ID, orderId);
    }
    if (paymentId != null) {
      intent.putExtra(Intents.EXTRA_PAYMENT_ID, paymentId);
    }
    if (employeeId != null) {
      intent.putExtra(Intents.EXTRA_EMPLOYEE_ID, employeeId);
    }
    if (transactionType != null) {
      intent.putExtra(Intents.EXTRA_TRANSACTION_TYPE, transactionType.intentValue);
    }
    if (serviceChargeAmount != null) {
      intent.putExtra(Intents.EXTRA_SERVICE_CHARGE_AMOUNT, serviceChargeAmount);
    }
    if (taxableAmountRateList != null) {
      intent.putParcelableArrayListExtra(Intents.EXTRA_TAXABLE_AMOUNTS, taxableAmountRateList);
    }
    intent.putExtra(Intents.EXTRA_DISABLE_CASHBACK, isDisableCashBack);
    intent.putExtra(Intents.EXTRA_IS_TESTING, isTesting);
    intent.putExtra(Intents.EXTRA_CARD_ENTRY_METHODS, cardEntryMethods);
    if (voiceAuthCode != null) {
      intent.putExtra(Intents.EXTRA_VOICE_AUTH_CODE, voiceAuthCode);
    }
    intent.putExtra(Intents.EXTRA_CARD_NOT_PRESENT, isCardNotPresent);
    if (streetAddress != null) {
      intent.putExtra(Intents.EXTRA_AVS_STREET_ADDRESS, streetAddress);
    }
    if (postalCode != null) {
      intent.putExtra(Intents.EXTRA_AVS_POSTAL_CODE, postalCode);
    }
    if (cardDataMessage != null) {
      intent.putExtra(Intents.EXTRA_CARD_DATA_MESSAGE, cardDataMessage);
    }
    intent.putExtra(Intents.EXTRA_REMOTE_PRINT, remotePrint);
    if (transactionNo != null) {
      intent.putExtra(Intents.EXTRA_TRANSACTION_NO, transactionNo);
    }
    intent.putExtra(Intents.EXTRA_FORCE_SWIPE_PIN_ENTRY, isForceSwipePinEntry);

    intent.putExtra(Intents.EXTRA_DISABLE_RESTART_TRANSACTION_WHEN_FAILED, disableRestartTransactionWhenFailed);

    intent.putExtra(Intents.EXTRA_EXTERNAL_PAYMENT_ID, externalPaymentId);

    intent.putExtra(Intents.EXTRA_EXTERNAL_REFERENCE_ID, externalReferenceId);

    intent.putExtra(Intents.EXTRA_VAULTED_CARD, vaultedCard);

    if (allowOfflinePayment != null) {
      intent.putExtra(Intents.EXTRA_ALLOW_OFFLINE_ACCEPTANCE, allowOfflinePayment);
    }
    if (approveOfflinePaymentWithoutPrompt != null) {
      intent.putExtra(Intents.EXTRA_OFFLINE_NO_PROMPT, approveOfflinePaymentWithoutPrompt);
    }
    if (requiresRemoteConfirmation != null) {
      intent.putExtra(Intents.EXTRA_REQUIRES_REMOTE_CONFIRMATION, requiresRemoteConfirmation);
    }
    if (requiresFinalRemoteApproval != null) {
      intent.putExtra(Intents.EXTRA_REQUIRES_FINAL_REMOTE_APPROVAL, requiresFinalRemoteApproval);
    }
    if (skipELVLimitOverride != null) {
      intent.putExtra(Intents.EXTRA_SKIP_ELV_LIMIT_OVERRIDE, skipELVLimitOverride);
    }
    if (applicationTracking != null) {
      intent.putExtra(Intents.EXTRA_APP_TRACKING_ID, applicationTracking);
    }
    intent.putExtra(Intents.EXTRA_ALLOW_PARTIAL_AUTH, allowPartialAuth);
    if (germanInfo != null) {
      intent.putExtra(Intents.GERMAN_INFO, germanInfo);
    }
    if (germanELVTransaction != null) {
      intent.putExtra(Intents.EXTRA_GERMAN_ELV, germanELVTransaction);
    }
    if (cashAdvanceCustomerIdentification != null) {
      intent.putExtra(Intents.CASHADVANCE_CUSTOMER_IDENTIFICATION, cashAdvanceCustomerIdentification);
    }
    if (transactionSettings != null) {
      intent.putExtra(Intents.EXTRA_TRANSACTION_SETTINGS, transactionSettings);
    }
    if (vasSettings != null) {
      intent.putExtra(Intents.EXTRA_VAS_SETTINGS, vasSettings);
    }

    if (originatingPayment != null) {
      intent.putExtra(Intents.EXTRA_ORIGINATING_PAYMENT, originatingPayment);
      if (originatingPayment != null) {
        intent.putExtra(Intents.EXTRA_ORIGINATING_TRANSACTION, originatingPayment.getCardTransaction());
      }
    } else if (originatingTransaction != null) {
      intent.putExtra(Intents.EXTRA_ORIGINATING_TRANSACTION, originatingTransaction);
    }
    if (originatingCredit != null) {
      intent.putExtra(Intents.EXTRA_ORIGINATING_CREDIT, originatingCredit);
    }

    if (passThroughValues != null) {
      intent.putExtra(Intents.EXTRA_PASS_THROUGH_VALUES, (Serializable) passThroughValues);
    }
    if (applicationSpecificValues != null) {
      intent.putExtra(Intents.EXTRA_APPLICATION_SPECIFIC_VALUES, (Serializable)applicationSpecificValues);
    }

    if (refund != null) {
      intent.putExtra(Intents.EXTRA_REFUND, refund);
    }
    if (customerTender != null) {
      intent.putExtra(Intents.EXTRA_CUSTOMER_TENDER, customerTender);
    }
    intent.putExtra(Intents.EXTRA_DISABLE_CREDIT_SURCHARGE, isDisableCreditSurcharge);
    intent.putExtra(Intents.EXTRA_PRESENT_QRC_ONLY, isPresentQrcOnly);
    intent.putExtra(Intents.EXTRA_MANUAL_CARD_ENTRY_BYPASS_MODE, isManualCardEntryByPassMode);
    intent.putExtra(Intents.EXTRA_ALLOW_MANUAL_CARD_ENTRY_ON_MFD, isAllowManualCardEntryOnMFD);
    if (quickPaymentTransactionUuid != null) {
      intent.putExtra(Intents.EXTRA_QUICK_PAYMENT_TRANSACTION_ID, quickPaymentTransactionUuid);
    }

    if (authorization != null) {
      intent.putExtra(Intents.EXTRA_AUTHORIZATION, authorization);
    }

    if (tokenizeCardRequest != null) {
      intent.putExtra(Intents.EXTRA_C_TOKEN_REQUEST, tokenizeCardRequest);
    }

    if (tokenizeCardResponse !=null) {
      intent.putExtra(Intents.EXTRA_C_TOKEN_RESULT, tokenizeCardResponse);
    }
  }

  @Override
  public String toString() {
    return "PayIntent{" +
           "action='" + action + '\'' +
           ", amount=" + amount +
           ", tippableAmount=" + tippableAmount +
           ", tipAmount=" + tipAmount +
           ", taxAmount=" + taxAmount +
           ", cashbackAmount = " + cashbackAmount +
           ", orderId='" + orderId + '\'' +
           ", paymentId='" + paymentId + '\'' +
           ", employeeId='" + employeeId + '\'' +
           ", transactionType=" + transactionType +
           ", taxableAmountRateList=" + taxableAmountRateList +
           ", serviceChargeAmount=" + serviceChargeAmount +
           ", isDisableCashBack=" + isDisableCashBack +
           ", isTesting=" + isTesting +
           ", cardEntryMethods=" + cardEntryMethods +
           ", voiceAuthCode='" + voiceAuthCode + '\'' +
           ", postalCode='" + postalCode + '\'' +
           ", streetAddress='" + streetAddress + '\'' +
           ", isCardNotPresent=" + isCardNotPresent +
           ", cardDataMessage='" + cardDataMessage + '\'' +
           ", remotePrint=" + remotePrint +
           ", transactionNo='" + transactionNo + '\'' +
           ", isForceSwipePinEntry=" + isForceSwipePinEntry +
           ", disableRestartTransactionWhenFailed=" + disableRestartTransactionWhenFailed +
           ", externalPaymentId='" + externalPaymentId + '\'' +
           ", externalReferenceId='" + externalReferenceId + '\'' +
           ", originatingPaymentPackage='" + originatingPaymentPackage + '\'' +
           ", vaultedCard=" + vaultedCard + '\'' +
           ", allowOfflinePayment=" + allowOfflinePayment + '\'' +
           ", approveOfflinePaymentWithoutPrompt=" + approveOfflinePaymentWithoutPrompt +
           ", requiresRemoteConfirmation=" + requiresRemoteConfirmation +
           ", requiresFinalRemoteApproval=" + requiresFinalRemoteApproval +
           ", skipELVLimitOverride=" + skipELVLimitOverride +
           ", applicationTracking=" + applicationTracking +
           ", allowPartialAuth=" + allowPartialAuth +
           ", useLastSwipe=" + useLastSwipe +
           ", germanInfo=" + germanInfo +
           ", germanELVTransaction=" + germanELVTransaction +
           ", transactionSettings=" + transactionSettings +
           ", vasSettings=" + vasSettings +
           ", themeName=" + themeName +
           ", passThroughValues=" + passThroughValues +
           ", applicationSpecificValues=" + applicationSpecificValues +
           ", refund=" + refund +
           ", customerTender=" + customerTender +
           ", isDisableCreditSurcharge=" + isDisableCreditSurcharge +
           ", isPresentQrcOnly=" + isPresentQrcOnly +
           ", isManualCardEntryByPassMode" + isManualCardEntryByPassMode +
           ", isAllowManualCardEntryOnMFD" + isAllowManualCardEntryOnMFD +
           ", authorization" + authorization +
           ", tokenRequest=" + tokenizeCardRequest +
           ", tokenResponse=" + tokenizeCardResponse +
           '}';
  }

  // Parcelable interface impl
  private static final String BUNDLE_KEY_ACTION = "a";

  @Override
  public int describeContents() {
    return 0;
  }

  public void writeToParcel(Parcel out, int flags) {
    // create a bundle
    final Bundle bundle = new Bundle(PayIntent.class.getClassLoader());

    bundle.putString(BUNDLE_KEY_ACTION, action);

    if (amount != null) {
      bundle.putLong(Intents.EXTRA_AMOUNT, amount);
    }
    if (tippableAmount != null) {
      bundle.putLong(Intents.EXTRA_TIPPABLE_AMOUNT, tippableAmount);
    }
    if (tipAmount != null) {
      bundle.putLong(Intents.EXTRA_TIP_AMOUNT, tipAmount);
    }
    if (taxAmount != null) {
      bundle.putLong(Intents.EXTRA_TAX_AMOUNT, taxAmount);
    }
    if (cashbackAmount != null) {
      bundle.putLong(Intents.EXTRA_CASHBACK_AMOUNT, cashbackAmount);
    }
    if (orderId != null) {
      bundle.putString(Intents.EXTRA_ORDER_ID, orderId);
    }
    if (paymentId != null) {
      bundle.putString(Intents.EXTRA_PAYMENT_ID, paymentId);
    }
    if (employeeId != null) {
      bundle.putString(Intents.EXTRA_EMPLOYEE_ID, employeeId);
    }
    if (transactionType != null) {
      bundle.putString(Intents.EXTRA_TRANSACTION_TYPE, transactionType.intentValue);
    }
    if (serviceChargeAmount != null) {
      bundle.putParcelable(Intents.EXTRA_SERVICE_CHARGE_AMOUNT, serviceChargeAmount);
    }
    if (taxableAmountRateList != null) {
      bundle.putParcelableArrayList(Intents.EXTRA_TAXABLE_AMOUNTS, taxableAmountRateList);
    }
    bundle.putBoolean(Intents.EXTRA_DISABLE_CASHBACK, isDisableCashBack);
    bundle.putBoolean(Intents.EXTRA_IS_TESTING, isTesting);
    bundle.putInt(Intents.EXTRA_CARD_ENTRY_METHODS, cardEntryMethods);
    if (voiceAuthCode != null) {
      bundle.putString(Intents.EXTRA_VOICE_AUTH_CODE, voiceAuthCode);
    }
    bundle.putBoolean(Intents.EXTRA_CARD_NOT_PRESENT, isCardNotPresent);
    if (streetAddress != null) {
      bundle.putString(Intents.EXTRA_AVS_STREET_ADDRESS, streetAddress);
    }
    if (postalCode != null) {
      bundle.putString(Intents.EXTRA_AVS_POSTAL_CODE, postalCode);
    }
    if (cardDataMessage != null) {
      bundle.putString(Intents.EXTRA_CARD_DATA_MESSAGE, cardDataMessage);
    }
    bundle.putBoolean(Intents.EXTRA_REMOTE_PRINT, remotePrint);
    if (transactionNo != null) {
      bundle.putString(Intents.EXTRA_TRANSACTION_NO, transactionNo);
    }
    bundle.putBoolean(Intents.EXTRA_FORCE_SWIPE_PIN_ENTRY, isForceSwipePinEntry);

    bundle.putBoolean(Intents.EXTRA_DISABLE_RESTART_TRANSACTION_WHEN_FAILED, disableRestartTransactionWhenFailed);

    if (vaultedCard != null) {
      bundle.putParcelable(Intents.EXTRA_VAULTED_CARD, vaultedCard);
    }

    if (externalPaymentId != null) {
      bundle.putString(Intents.EXTRA_EXTERNAL_PAYMENT_ID, externalPaymentId);
    }

    if (externalReferenceId != null) {
      bundle.putString(Intents.EXTRA_EXTERNAL_REFERENCE_ID, externalReferenceId);
    }

    if (originatingPaymentPackage != null) {
      bundle.putString(Intents.EXTRA_ORIGINATING_PAYMENT_PACKAGE, originatingPaymentPackage);
    }

    if (allowOfflinePayment != null) {
      bundle.putBoolean(Intents.EXTRA_ALLOW_OFFLINE_ACCEPTANCE, allowOfflinePayment);
    }

    if (approveOfflinePaymentWithoutPrompt != null) {
      bundle.putBoolean(Intents.EXTRA_OFFLINE_NO_PROMPT, approveOfflinePaymentWithoutPrompt);
    }

    if (requiresRemoteConfirmation != null) {
      bundle.putBoolean(Intents.EXTRA_REQUIRES_REMOTE_CONFIRMATION, requiresRemoteConfirmation);
    }

    if (requiresFinalRemoteApproval != null) {
      bundle.putBoolean(Intents.EXTRA_REQUIRES_FINAL_REMOTE_APPROVAL, requiresFinalRemoteApproval);
    }

    if (skipELVLimitOverride != null) {
      bundle.putBoolean(Intents.EXTRA_SKIP_ELV_LIMIT_OVERRIDE, skipELVLimitOverride);
    }

    if (applicationTracking != null) {
      bundle.putParcelable(Intents.EXTRA_APP_TRACKING_ID, applicationTracking);
    }

    bundle.putBoolean(Intents.EXTRA_ALLOW_PARTIAL_AUTH, allowPartialAuth);

    bundle.putBoolean(Intents.EXTRA_USE_LAST_SWIPE, useLastSwipe);

    if (germanInfo != null) {
      bundle.putParcelable(Intents.GERMAN_INFO, germanInfo);
    }
    if (germanELVTransaction != null) {
      bundle.putString(Intents.EXTRA_GERMAN_ELV, germanELVTransaction);
    }
    if (cashAdvanceCustomerIdentification != null) {
      bundle.putParcelable(Intents.CASHADVANCE_CUSTOMER_IDENTIFICATION, cashAdvanceCustomerIdentification);
    }

    if (transactionSettings != null) {
      bundle.putParcelable(Intents.EXTRA_TRANSACTION_SETTINGS, transactionSettings);
    }

    if (vasSettings != null) {
      bundle.putParcelable(Intents.EXTRA_VAS_SETTINGS, vasSettings);
    }

    if (originatingTransaction != null) {
      bundle.putParcelable(Intents.EXTRA_ORIGINATING_TRANSACTION, originatingTransaction);
    }

    if(themeName != null) {
      bundle.putSerializable(Intents.EXTRA_THEME_NAME, themeName);
    }

    if (originatingPayment != null) {
      bundle.putParcelable(Intents.EXTRA_ORIGINATING_PAYMENT, originatingPayment);
    }

    if (originatingCredit != null) {
      bundle.putParcelable(Intents.EXTRA_ORIGINATING_CREDIT, originatingCredit);
    }

    if (passThroughValues != null) {
      bundle.putSerializable(Intents.EXTRA_PASS_THROUGH_VALUES, (Serializable) passThroughValues);
    }

    if (applicationSpecificValues != null && !applicationSpecificValues.isEmpty()) {
      bundle.putSerializable(Intents.EXTRA_APPLICATION_SPECIFIC_VALUES, (Serializable)applicationSpecificValues);
    }

    if (refund != null) {
      bundle.putParcelable(Intents.EXTRA_REFUND, refund);
    }

    if (customerTender != null) {
      bundle.putParcelable(Intents.EXTRA_CUSTOMER_TENDER, customerTender);
    }

    bundle.putBoolean(Intents.EXTRA_DISABLE_CREDIT_SURCHARGE, isDisableCreditSurcharge);

    bundle.putBoolean(Intents.EXTRA_PRESENT_QRC_ONLY, isPresentQrcOnly);

    bundle.putBoolean(Intents.EXTRA_MANUAL_CARD_ENTRY_BYPASS_MODE, isManualCardEntryByPassMode);

    bundle.putBoolean(Intents.EXTRA_ALLOW_MANUAL_CARD_ENTRY_ON_MFD, isAllowManualCardEntryOnMFD);

    bundle.putParcelable(Intents.EXTRA_AUTHORIZATION, authorization);

    if (tokenizeCardRequest != null) {
      bundle.putParcelable(Intents.EXTRA_C_TOKEN_REQUEST, tokenizeCardRequest);
    }

    if (tokenizeCardResponse != null) {
      bundle.putParcelable(Intents.EXTRA_C_TOKEN_RESULT, tokenizeCardResponse);
    }

    // write out
    out.writeBundle(bundle);
  }

  public static final Creator<PayIntent> CREATOR = new Creator<PayIntent>() {
    public PayIntent createFromParcel(Parcel in) {
      final Bundle bundle = in.readBundle(PayIntent.class.getClassLoader());

      final Builder builder = new Builder();

      builder.action(bundle.getString(BUNDLE_KEY_ACTION));

      if (bundle.containsKey(Intents.EXTRA_AMOUNT)) {
        builder.amount( bundle.getLong(Intents.EXTRA_AMOUNT) );
      }
      if (bundle.containsKey(Intents.EXTRA_TIPPABLE_AMOUNT)) {
        builder.tippableAmount(bundle.getLong(Intents.EXTRA_TIPPABLE_AMOUNT));
      }
      if (bundle.containsKey(Intents.EXTRA_TIP_AMOUNT)) {
        builder.tipAmount(bundle.getLong(Intents.EXTRA_TIP_AMOUNT));
      }
      if (bundle.containsKey(Intents.EXTRA_TAX_AMOUNT)) {
        builder.taxAmount(bundle.getLong(Intents.EXTRA_TAX_AMOUNT));
      }
      if (bundle.containsKey(Intents.EXTRA_CASHBACK_AMOUNT)) {
        builder.cashbackAmount(bundle.getLong(Intents.EXTRA_CASHBACK_AMOUNT));
      }
      if (bundle.containsKey(Intents.EXTRA_ORDER_ID)) {
        builder.orderId(bundle.getString(Intents.EXTRA_ORDER_ID));
      }
      if (bundle.containsKey(Intents.EXTRA_PAYMENT_ID)){
        builder.paymentId(bundle.getString(Intents.EXTRA_PAYMENT_ID));
      }
      if (bundle.containsKey(Intents.EXTRA_EMPLOYEE_ID)) {
        builder.employeeId(bundle.getString(Intents.EXTRA_EMPLOYEE_ID));
      }
      if (bundle.containsKey(Intents.EXTRA_TRANSACTION_TYPE)) {
        builder.transactionType(TransactionType.getForValue(bundle.getString(Intents.EXTRA_TRANSACTION_TYPE)));
      }
      if (bundle.containsKey(Intents.EXTRA_SERVICE_CHARGE_AMOUNT)) {
        final Parcelable serviceChargeAmount = bundle.getParcelable(Intents.EXTRA_SERVICE_CHARGE_AMOUNT);
        if (serviceChargeAmount instanceof ServiceChargeAmount) {
          builder.serviceChargeAmount((ServiceChargeAmount)serviceChargeAmount);
        }
      }
      if (bundle.containsKey(Intents.EXTRA_TAXABLE_AMOUNTS)) {
        final ArrayList<TaxableAmountRate> taxableAmounts = bundle.getParcelableArrayList(Intents.EXTRA_TAXABLE_AMOUNTS);
        if (taxableAmounts != null && taxableAmounts.size() > 0) {
          builder.taxableAmountRates(taxableAmounts);
        }
      }
      builder.disableCashback(bundle.getBoolean(Intents.EXTRA_DISABLE_CASHBACK));
      builder.isTesting = bundle.getBoolean(Intents.EXTRA_IS_TESTING);
      builder.cardEntryMethods(bundle.getInt(Intents.EXTRA_CARD_ENTRY_METHODS));
      if (bundle.containsKey(Intents.EXTRA_VOICE_AUTH_CODE)) {
        builder.voiceAuthCode = bundle.getString(Intents.EXTRA_VOICE_AUTH_CODE);
      }
      builder.isCardNotPresent = bundle.getBoolean(Intents.EXTRA_CARD_NOT_PRESENT);
      if (bundle.containsKey(Intents.EXTRA_AVS_STREET_ADDRESS)) {
        builder.streetAddress = bundle.getString(Intents.EXTRA_AVS_STREET_ADDRESS);
      }
      if (bundle.containsKey(Intents.EXTRA_AVS_POSTAL_CODE)) {
        builder.postalCode = bundle.getString(Intents.EXTRA_AVS_POSTAL_CODE);
      }
      if (bundle.containsKey(Intents.EXTRA_CARD_DATA_MESSAGE)) {
        builder.cardDataMessage(bundle.getString(Intents.EXTRA_CARD_DATA_MESSAGE));
      }
      builder.remotePrint = bundle.getBoolean(Intents.EXTRA_REMOTE_PRINT);
      if (bundle.containsKey(Intents.EXTRA_TRANSACTION_NO)) {
        builder.transactionNo(bundle.getString(Intents.EXTRA_TRANSACTION_NO));
      }
      builder.isForceSwipePinEntry = bundle.getBoolean(Intents.EXTRA_FORCE_SWIPE_PIN_ENTRY);
      builder.disableRestartTransactionWhenFailed(
          bundle.getBoolean(Intents.EXTRA_DISABLE_RESTART_TRANSACTION_WHEN_FAILED));
      if (bundle.containsKey(Intents.EXTRA_EXTERNAL_PAYMENT_ID)) {
        builder.externalPaymentId(bundle.getString(Intents.EXTRA_EXTERNAL_PAYMENT_ID));
      }
      if (bundle.containsKey(Intents.EXTRA_EXTERNAL_REFERENCE_ID)) {
        builder.externalReferenceId(bundle.getString(Intents.EXTRA_EXTERNAL_REFERENCE_ID));
      }

      if (bundle.containsKey(Intents.EXTRA_ORIGINATING_PAYMENT_PACKAGE)) {
        builder.originatingPaymentPackage(bundle.getString(Intents.EXTRA_ORIGINATING_PAYMENT_PACKAGE));
      }

      if (bundle.containsKey(Intents.EXTRA_VAULTED_CARD)) {
        Parcelable parcelable = bundle.getParcelable(Intents.EXTRA_VAULTED_CARD);
        if (parcelable instanceof VaultedCard) {
          builder.vaultedCard((VaultedCard)parcelable);
        }
      }

      if (bundle.containsKey(Intents.EXTRA_ALLOW_OFFLINE_ACCEPTANCE)) {
        builder.allowOfflinePayment((Boolean)bundle.get(Intents.EXTRA_ALLOW_OFFLINE_ACCEPTANCE));
      }
      if (bundle.containsKey(Intents.EXTRA_OFFLINE_NO_PROMPT)) {
        builder.approveOfflinePaymentWithoutPrompt((Boolean)bundle.get(Intents.EXTRA_OFFLINE_NO_PROMPT));
      }
      if (bundle.containsKey(Intents.EXTRA_REQUIRES_REMOTE_CONFIRMATION)) {
        builder.requiresRemoteConfirmation((Boolean)bundle.get(Intents.EXTRA_REQUIRES_REMOTE_CONFIRMATION));
      }
      if (bundle.containsKey(Intents.EXTRA_REQUIRES_FINAL_REMOTE_APPROVAL)) {
        builder.requiresFinalRemoteApproval((Boolean)bundle.get(Intents.EXTRA_REQUIRES_FINAL_REMOTE_APPROVAL));
      }
      if (bundle.containsKey(Intents.EXTRA_SKIP_ELV_LIMIT_OVERRIDE)) {
        builder.skipELVLimitOverride((Boolean)bundle.get(Intents.EXTRA_SKIP_ELV_LIMIT_OVERRIDE));
      }
      if (bundle.containsKey(Intents.EXTRA_APP_TRACKING_ID)) {
        final Parcelable applicationTracking = bundle.getParcelable(Intents.EXTRA_APP_TRACKING_ID);
        if (applicationTracking instanceof AppTracking) {
          builder.applicationTracking((AppTracking)applicationTracking);
        }
      }

      if (bundle.containsKey(Intents.EXTRA_ALLOW_PARTIAL_AUTH)) {
        builder.allowPartialAuth(bundle.getBoolean(Intents.EXTRA_ALLOW_PARTIAL_AUTH));
      }

      if (bundle.containsKey(Intents.EXTRA_USE_LAST_SWIPE)) {
        builder.useLastSwipe(bundle.getBoolean(Intents.EXTRA_USE_LAST_SWIPE));
      }

      if (bundle.containsKey(Intents.GERMAN_INFO)) {
        final Parcelable germanInfo = bundle.getParcelable(Intents.GERMAN_INFO);
        if (germanInfo instanceof GermanInfo) {
          builder.germanInfo((GermanInfo) germanInfo);
        }
      }
      if (bundle.containsKey(Intents.EXTRA_GERMAN_ELV)) {
        final String germanELVTransaction = bundle.getString(Intents.EXTRA_GERMAN_ELV);
        builder.germanELVTransaction(germanELVTransaction);
      }
      if (bundle.containsKey(Intents.CASHADVANCE_CUSTOMER_IDENTIFICATION)) {
        final Parcelable customerIdentification = bundle.getParcelable(Intents.CASHADVANCE_CUSTOMER_IDENTIFICATION);
        if (customerIdentification instanceof CashAdvanceCustomerIdentification) {
          builder.customerIdentification((CashAdvanceCustomerIdentification) customerIdentification);
        }
      }
      if (bundle.containsKey(Intents.EXTRA_TRANSACTION_SETTINGS)) {
        final Parcelable transactionSettings = bundle.getParcelable(Intents.EXTRA_TRANSACTION_SETTINGS);
        if (transactionSettings instanceof TransactionSettings) {
          builder.transactionSettings((TransactionSettings) transactionSettings);
        }
      }
      if (bundle.containsKey(Intents.EXTRA_VAS_SETTINGS)) {
        final Parcelable vasSettingsPacelable = bundle.getParcelable(Intents.EXTRA_VAS_SETTINGS);
        if (vasSettingsPacelable instanceof VasSettings) {
          builder.vasSettings((VasSettings)vasSettingsPacelable);
        }
      }
      if (bundle.containsKey(Intents.EXTRA_ORIGINATING_TRANSACTION)) {
        final Parcelable originatingTransactionParcelable = bundle.getParcelable(Intents.EXTRA_ORIGINATING_TRANSACTION);
        if (originatingTransactionParcelable instanceof CardTransaction) {
          builder.originatingTransaction((CardTransaction)originatingTransactionParcelable);
        }
      }

      if(bundle.containsKey(Intents.EXTRA_THEME_NAME)) {
        builder.themeName((Themes) bundle.getSerializable(Intents.EXTRA_THEME_NAME));
      }

      if (bundle.containsKey(Intents.EXTRA_ORIGINATING_PAYMENT)) {
        final Parcelable originatingPaymentParcelable = bundle.getParcelable(Intents.EXTRA_ORIGINATING_PAYMENT);
        if (originatingPaymentParcelable instanceof Payment) {
          builder.originatingPayment((Payment)originatingPaymentParcelable);
        }
      }

      if (bundle.containsKey(Intents.EXTRA_ORIGINATING_CREDIT)) {
        final Parcelable originatingCreditParcelable = bundle.getParcelable(Intents.EXTRA_ORIGINATING_CREDIT);
        if (originatingCreditParcelable instanceof Credit) {
          builder.originatingCredit((Credit)originatingCreditParcelable);
        }
      }

      if (bundle.containsKey(Intents.EXTRA_PASS_THROUGH_VALUES)) {
        final Serializable originatingPassThroughValues = bundle.getSerializable(Intents.EXTRA_PASS_THROUGH_VALUES);
        if (originatingPassThroughValues instanceof Map) {
          builder.passThroughValues((Map<String, String>) originatingPassThroughValues);
        }
      }

      if (bundle.containsKey(Intents.EXTRA_APPLICATION_SPECIFIC_VALUES)) {
        final Serializable originatingAppSpecificValues = bundle.getSerializable(Intents.EXTRA_APPLICATION_SPECIFIC_VALUES);
        if (originatingAppSpecificValues instanceof Map) {
          builder.applicationSpecificValues((Map<String, String>) originatingAppSpecificValues);
        }
      }

      if (bundle.containsKey(Intents.EXTRA_REFUND)) {
        final Parcelable refundParcelable = bundle.getParcelable(Intents.EXTRA_REFUND);
        if (refundParcelable instanceof Refund) {
          builder.refund((Refund) refundParcelable);
        }
      }

      if (bundle.containsKey(Intents.EXTRA_CUSTOMER_TENDER)) {
        final Parcelable customerTenderParcelable = bundle.getParcelable(Intents.EXTRA_CUSTOMER_TENDER);
        if (customerTenderParcelable instanceof Tender) {
          builder.customerTender((Tender) customerTenderParcelable);
        }
      }

      builder.disableCreditSurcharge(bundle.getBoolean(Intents.EXTRA_DISABLE_CREDIT_SURCHARGE, false));

      builder.isPresentQrcOnly(bundle.getBoolean(Intents.EXTRA_PRESENT_QRC_ONLY, false));

      builder.isManualCardEntryByPassMode(bundle.getBoolean(Intents.EXTRA_MANUAL_CARD_ENTRY_BYPASS_MODE, false));

      builder.isAllowManualCardEntryOnMFD(bundle.getBoolean(Intents.EXTRA_ALLOW_MANUAL_CARD_ENTRY_ON_MFD, false));

      builder.authorization = bundle.getParcelable(Intents.EXTRA_AUTHORIZATION);

      if (bundle.containsKey(Intents.EXTRA_C_TOKEN_REQUEST)) {
        builder.tokenRequest(bundle.getParcelable(Intents.EXTRA_C_TOKEN_REQUEST));
      }

      if (bundle.containsKey(Intents.EXTRA_C_TOKEN_RESULT)) {
        builder.tokenResponse(bundle.getParcelable(Intents.EXTRA_C_TOKEN_RESULT));
      }
      // build
      return builder.build();
    }

    public PayIntent[] newArray(int size) {
      return new PayIntent[size];
    }
  };
}
