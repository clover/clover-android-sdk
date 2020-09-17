/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */

/*
 * Copyright (C) 2019 Clover Network, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.clover.sdk.v3.remotepay;

import com.clover.sdk.GenericClient;

/**
 * This is an auto-generated Clover data object.
 * <p>
 * <h3>Fields</h3>
 * <ul>
 * </ul>
 */
@SuppressWarnings("all")
public class ManualRefundRequest extends com.clover.sdk.v3.remotepay.BaseTransactionRequest {

  /**
   * Identifier for the order to apply this to. The order must exist in the clover system.  **NOTE**  THIS FIELD IS ONLY USED BY THE PAYMENT CONNECTOR. It provides functionality currently available to the native PayIntent but not supported yet by the remote pay SDKs
   */
  @Override
  public java.lang.String getOrderId() {
    return genClient.cacheGet(CacheKey.orderId);
  }

  /**
   * If true, then do not print using the clover printer.  Return print information.
   */
  @Override
  public java.lang.Boolean getDisablePrinting() {
    return genClient.cacheGet(CacheKey.disablePrinting);
  }

  /**
   * Do not show the receipt options screen
   */
  @Override
  public java.lang.Boolean getDisableReceiptSelection() {
    return genClient.cacheGet(CacheKey.disableReceiptSelection);
  }

  /**
   * Do not do heuristic duplicate checking
   */
  @Override
  public java.lang.Boolean getDisableDuplicateChecking() {
    return genClient.cacheGet(CacheKey.disableDuplicateChecking);
  }

  /**
   * If true then card not present is accepted
   */
  @Override
  public java.lang.Boolean getCardNotPresent() {
    return genClient.cacheGet(CacheKey.cardNotPresent);
  }

  /**
   * If the transaction times out or fails because of decline, do not restart it
   */
  @Override
  public java.lang.Boolean getDisableRestartTransactionOnFail() {
    return genClient.cacheGet(CacheKey.disableRestartTransactionOnFail);
  }

  /**
   * Total amount paid
   */
  @Override
  public java.lang.Long getAmount() {
    return genClient.cacheGet(CacheKey.amount);
  }

  /**
   * Allowed entry methods
   */
  @Override
  public java.lang.Integer getCardEntryMethods() {
    return genClient.cacheGet(CacheKey.cardEntryMethods);
  }

  /**
   * A saved card
   */
  @Override
  public com.clover.sdk.v3.payments.VaultedCard getVaultedCard() {
    return genClient.cacheGet(CacheKey.vaultedCard);
  }

  /**
   * An id that will be persisted with transactions.
   */
  @Override
  public java.lang.String getExternalId() {
    return genClient.cacheGet(CacheKey.externalId);
  }

  /**
   * The type of the transaction.
   */
  @Override
  public com.clover.sdk.v3.remotepay.TransactionType getType() {
    return genClient.cacheGet(CacheKey.type);
  }

  /**
   * Do not show/send potential duplicate challenges
   */
  @Override
  public java.lang.Boolean getAutoAcceptPaymentConfirmations() {
    return genClient.cacheGet(CacheKey.autoAcceptPaymentConfirmations);
  }

  /**
   * Extra pass-through data used by external systems.
   */
  @Override
  public java.util.Map<java.lang.String,java.lang.String> getExtras() {
    return genClient.cacheGet(CacheKey.extras);
  }

  /**
   * A map of values for regional specific data
   */
  @Override
  public java.util.Map<java.lang.String,java.lang.String> getRegionalExtras() {
    return genClient.cacheGet(CacheKey.regionalExtras);
  }

  /**
   * An id that can be passed to the merchant's gateway, and ultimately appear in settlement records.
   */
  @Override
  public java.lang.String getExternalReferenceId() {
    return genClient.cacheGet(CacheKey.externalReferenceId);
  }

  /**
   * Identifier for the request
   */
  @Override
  public java.lang.String getRequestId() {
    return genClient.cacheGet(CacheKey.requestId);
  }

  /**
   * Identifier for the version
   */
  @Override
  public java.lang.Integer getVersion() {
    return genClient.cacheGet(CacheKey.version);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    orderId
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    disablePrinting
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Boolean.class)),
    disableReceiptSelection
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Boolean.class)),
    disableDuplicateChecking
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Boolean.class)),
    cardNotPresent
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Boolean.class)),
    disableRestartTransactionOnFail
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Boolean.class)),
    amount
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    cardEntryMethods
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Integer.class)),
    vaultedCard
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.payments.VaultedCard.JSON_CREATOR)),
    externalId
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    type
        (com.clover.sdk.extractors.EnumExtractionStrategy.instance(com.clover.sdk.v3.remotepay.TransactionType.class)),
    autoAcceptPaymentConfirmations
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Boolean.class)),
    extras
        (com.clover.sdk.extractors.MapExtractionStrategy.instance()),
    regionalExtras
        (com.clover.sdk.extractors.MapExtractionStrategy.instance()),
    externalReferenceId
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    requestId
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    version
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Integer.class)),
      ;

    private final com.clover.sdk.extractors.ExtractionStrategy extractionStrategy;

    private CacheKey(com.clover.sdk.extractors.ExtractionStrategy s) {
      extractionStrategy = s;
    }

    @Override
    public com.clover.sdk.extractors.ExtractionStrategy getExtractionStrategy() {
      return extractionStrategy;
    }
  }

  private final GenericClient<ManualRefundRequest> genClient;

  /**
   * Constructs a new empty instance.
   */
  public ManualRefundRequest() {
    super(false);
    genClient = new GenericClient<ManualRefundRequest>(this);
    this.setType(com.clover.sdk.v3.remotepay.TransactionType.CREDIT);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected ManualRefundRequest(boolean noInit) {
    super(false);
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public ManualRefundRequest(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public ManualRefundRequest(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public ManualRefundRequest(ManualRefundRequest src) {
    this();
    if (src.genClient.getJsonObject() != null) {
      genClient.setJsonObject(com.clover.sdk.v3.JsonHelper.deepCopy(src.genClient.getJSONObject()));
    }
  }

  /**
   * Returns the internal JSONObject backing this instance, the return value is not a copy so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public org.json.JSONObject getJSONObject() {
    return genClient.getJSONObject();
  }

  @Override
  public void validate() {

    genClient.validateCloverId(CacheKey.orderId, getOrderId());

    genClient.validateNotNull(CacheKey.amount, getAmount());

    genClient.validateNotNull(CacheKey.externalId, getExternalId());
    genClient.validateCloverId(CacheKey.requestId, getRequestId());
  }

  /** Checks whether the 'orderId' field is set and is not null */
  @Override
  public boolean isNotNullOrderId() {
    return genClient.cacheValueIsNotNull(CacheKey.orderId);
  }

  /** Checks whether the 'disablePrinting' field is set and is not null */
  @Override
  public boolean isNotNullDisablePrinting() {
    return genClient.cacheValueIsNotNull(CacheKey.disablePrinting);
  }

  /** Checks whether the 'disableReceiptSelection' field is set and is not null */
  @Override
  public boolean isNotNullDisableReceiptSelection() {
    return genClient.cacheValueIsNotNull(CacheKey.disableReceiptSelection);
  }

  /** Checks whether the 'disableDuplicateChecking' field is set and is not null */
  @Override
  public boolean isNotNullDisableDuplicateChecking() {
    return genClient.cacheValueIsNotNull(CacheKey.disableDuplicateChecking);
  }

  /** Checks whether the 'cardNotPresent' field is set and is not null */
  @Override
  public boolean isNotNullCardNotPresent() {
    return genClient.cacheValueIsNotNull(CacheKey.cardNotPresent);
  }

  /** Checks whether the 'disableRestartTransactionOnFail' field is set and is not null */
  @Override
  public boolean isNotNullDisableRestartTransactionOnFail() {
    return genClient.cacheValueIsNotNull(CacheKey.disableRestartTransactionOnFail);
  }

  /** Checks whether the 'amount' field is set and is not null */
  @Override
  public boolean isNotNullAmount() {
    return genClient.cacheValueIsNotNull(CacheKey.amount);
  }

  /** Checks whether the 'cardEntryMethods' field is set and is not null */
  @Override
  public boolean isNotNullCardEntryMethods() {
    return genClient.cacheValueIsNotNull(CacheKey.cardEntryMethods);
  }

  /** Checks whether the 'vaultedCard' field is set and is not null */
  @Override
  public boolean isNotNullVaultedCard() {
    return genClient.cacheValueIsNotNull(CacheKey.vaultedCard);
  }

  /** Checks whether the 'externalId' field is set and is not null */
  @Override
  public boolean isNotNullExternalId() {
    return genClient.cacheValueIsNotNull(CacheKey.externalId);
  }

  /** Checks whether the 'type' field is set and is not null */
  @Override
  public boolean isNotNullType() {
    return genClient.cacheValueIsNotNull(CacheKey.type);
  }

  /** Checks whether the 'autoAcceptPaymentConfirmations' field is set and is not null */
  @Override
  public boolean isNotNullAutoAcceptPaymentConfirmations() {
    return genClient.cacheValueIsNotNull(CacheKey.autoAcceptPaymentConfirmations);
  }

  /** Checks whether the 'extras' field is set and is not null */
  @Override
  public boolean isNotNullExtras() {
    return genClient.cacheValueIsNotNull(CacheKey.extras);
  }

  /** Checks whether the 'extras' field is set and is not null and is not empty */
  public boolean isNotEmptyExtras() { return isNotNullExtras() && !getExtras().isEmpty(); }

  /** Checks whether the 'regionalExtras' field is set and is not null */
  @Override
  public boolean isNotNullRegionalExtras() {
    return genClient.cacheValueIsNotNull(CacheKey.regionalExtras);
  }

  /** Checks whether the 'regionalExtras' field is set and is not null and is not empty */
  public boolean isNotEmptyRegionalExtras() { return isNotNullRegionalExtras() && !getRegionalExtras().isEmpty(); }

  /** Checks whether the 'externalReferenceId' field is set and is not null */
  @Override
  public boolean isNotNullExternalReferenceId() {
    return genClient.cacheValueIsNotNull(CacheKey.externalReferenceId);
  }

  /** Checks whether the 'requestId' field is set and is not null */
  @Override
  public boolean isNotNullRequestId() {
    return genClient.cacheValueIsNotNull(CacheKey.requestId);
  }

  /** Checks whether the 'version' field is set and is not null */
  @Override
  public boolean isNotNullVersion() {
    return genClient.cacheValueIsNotNull(CacheKey.version);
  }



  /** Checks whether the 'orderId' field has been set, however the value could be null */
  @Override
  public boolean hasOrderId() {
    return genClient.cacheHasKey(CacheKey.orderId);
  }

  /** Checks whether the 'disablePrinting' field has been set, however the value could be null */
  @Override
  public boolean hasDisablePrinting() {
    return genClient.cacheHasKey(CacheKey.disablePrinting);
  }

  /** Checks whether the 'disableReceiptSelection' field has been set, however the value could be null */
  @Override
  public boolean hasDisableReceiptSelection() {
    return genClient.cacheHasKey(CacheKey.disableReceiptSelection);
  }

  /** Checks whether the 'disableDuplicateChecking' field has been set, however the value could be null */
  @Override
  public boolean hasDisableDuplicateChecking() {
    return genClient.cacheHasKey(CacheKey.disableDuplicateChecking);
  }

  /** Checks whether the 'cardNotPresent' field has been set, however the value could be null */
  @Override
  public boolean hasCardNotPresent() {
    return genClient.cacheHasKey(CacheKey.cardNotPresent);
  }

  /** Checks whether the 'disableRestartTransactionOnFail' field has been set, however the value could be null */
  @Override
  public boolean hasDisableRestartTransactionOnFail() {
    return genClient.cacheHasKey(CacheKey.disableRestartTransactionOnFail);
  }

  /** Checks whether the 'amount' field has been set, however the value could be null */
  @Override
  public boolean hasAmount() {
    return genClient.cacheHasKey(CacheKey.amount);
  }

  /** Checks whether the 'cardEntryMethods' field has been set, however the value could be null */
  @Override
  public boolean hasCardEntryMethods() {
    return genClient.cacheHasKey(CacheKey.cardEntryMethods);
  }

  /** Checks whether the 'vaultedCard' field has been set, however the value could be null */
  @Override
  public boolean hasVaultedCard() {
    return genClient.cacheHasKey(CacheKey.vaultedCard);
  }

  /** Checks whether the 'externalId' field has been set, however the value could be null */
  @Override
  public boolean hasExternalId() {
    return genClient.cacheHasKey(CacheKey.externalId);
  }

  /** Checks whether the 'type' field has been set, however the value could be null */
  @Override
  public boolean hasType() {
    return genClient.cacheHasKey(CacheKey.type);
  }

  /** Checks whether the 'autoAcceptPaymentConfirmations' field has been set, however the value could be null */
  @Override
  public boolean hasAutoAcceptPaymentConfirmations() {
    return genClient.cacheHasKey(CacheKey.autoAcceptPaymentConfirmations);
  }

  /** Checks whether the 'extras' field has been set, however the value could be null */
  @Override
  public boolean hasExtras() {
    return genClient.cacheHasKey(CacheKey.extras);
  }

  /** Checks whether the 'regionalExtras' field has been set, however the value could be null */
  @Override
  public boolean hasRegionalExtras() {
    return genClient.cacheHasKey(CacheKey.regionalExtras);
  }

  /** Checks whether the 'externalReferenceId' field has been set, however the value could be null */
  @Override
  public boolean hasExternalReferenceId() {
    return genClient.cacheHasKey(CacheKey.externalReferenceId);
  }

  /** Checks whether the 'requestId' field has been set, however the value could be null */
  @Override
  public boolean hasRequestId() {
    return genClient.cacheHasKey(CacheKey.requestId);
  }

  /** Checks whether the 'version' field has been set, however the value could be null */
  @Override
  public boolean hasVersion() {
    return genClient.cacheHasKey(CacheKey.version);
  }


  /**
   * Sets the field 'orderId'.
   */
  @Override
  public BaseTransactionRequest setOrderId(java.lang.String orderId) {
    return genClient.setOther(orderId, CacheKey.orderId);
  }

  /**
   * Sets the field 'disablePrinting'.
   */
  @Override
  public BaseTransactionRequest setDisablePrinting(java.lang.Boolean disablePrinting) {
    return genClient.setOther(disablePrinting, CacheKey.disablePrinting);
  }

  /**
   * Sets the field 'disableReceiptSelection'.
   */
  @Override
  public BaseTransactionRequest setDisableReceiptSelection(java.lang.Boolean disableReceiptSelection) {
    return genClient.setOther(disableReceiptSelection, CacheKey.disableReceiptSelection);
  }

  /**
   * Sets the field 'disableDuplicateChecking'.
   */
  @Override
  public BaseTransactionRequest setDisableDuplicateChecking(java.lang.Boolean disableDuplicateChecking) {
    return genClient.setOther(disableDuplicateChecking, CacheKey.disableDuplicateChecking);
  }

  /**
   * Sets the field 'cardNotPresent'.
   */
  @Override
  public BaseTransactionRequest setCardNotPresent(java.lang.Boolean cardNotPresent) {
    return genClient.setOther(cardNotPresent, CacheKey.cardNotPresent);
  }

  /**
   * Sets the field 'disableRestartTransactionOnFail'.
   */
  @Override
  public BaseTransactionRequest setDisableRestartTransactionOnFail(java.lang.Boolean disableRestartTransactionOnFail) {
    return genClient.setOther(disableRestartTransactionOnFail, CacheKey.disableRestartTransactionOnFail);
  }

  /**
   * Sets the field 'amount'.
   */
  @Override
  public BaseTransactionRequest setAmount(java.lang.Long amount) {
    return genClient.setOther(amount, CacheKey.amount);
  }

  /**
   * Sets the field 'cardEntryMethods'.
   */
  @Override
  public BaseTransactionRequest setCardEntryMethods(java.lang.Integer cardEntryMethods) {
    return genClient.setOther(cardEntryMethods, CacheKey.cardEntryMethods);
  }

  /**
   * Sets the field 'vaultedCard'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  @Override
  public BaseTransactionRequest setVaultedCard(com.clover.sdk.v3.payments.VaultedCard vaultedCard) {
    return genClient.setRecord(vaultedCard, CacheKey.vaultedCard);
  }

  /**
   * Sets the field 'externalId'.
   */
  @Override
  public BaseTransactionRequest setExternalId(java.lang.String externalId) {
    return genClient.setOther(externalId, CacheKey.externalId);
  }

  /**
   * Sets the field 'type'.
   */
  @Override
  public BaseTransactionRequest setType(com.clover.sdk.v3.remotepay.TransactionType type) {
    return genClient.setOther(type, CacheKey.type);
  }

  /**
   * Sets the field 'autoAcceptPaymentConfirmations'.
   */
  @Override
  public BaseTransactionRequest setAutoAcceptPaymentConfirmations(java.lang.Boolean autoAcceptPaymentConfirmations) {
    return genClient.setOther(autoAcceptPaymentConfirmations, CacheKey.autoAcceptPaymentConfirmations);
  }

  /**
   * Sets the field 'extras'.
   */
  @Override
  public BaseTransactionRequest setExtras(java.util.Map<java.lang.String,java.lang.String> extras) {
    return genClient.setOther(extras, CacheKey.extras);
  }

  /**
   * Sets the field 'regionalExtras'.
   */
  @Override
  public BaseTransactionRequest setRegionalExtras(java.util.Map<java.lang.String,java.lang.String> regionalExtras) {
    return genClient.setOther(regionalExtras, CacheKey.regionalExtras);
  }

  /**
   * Sets the field 'externalReferenceId'.
   */
  @Override
  public BaseTransactionRequest setExternalReferenceId(java.lang.String externalReferenceId) {
    return genClient.setOther(externalReferenceId, CacheKey.externalReferenceId);
  }

  /**
   * Sets the field 'requestId'.
   */
  @Override
  public BaseRequest setRequestId(java.lang.String requestId) {
    return genClient.setOther(requestId, CacheKey.requestId);
  }

  /**
   * Sets the field 'version'.
   */
  @Override
  public BaseRequest setVersion(java.lang.Integer version) {
    return genClient.setOther(version, CacheKey.version);
  }


  /** Clears the 'orderId' field, the 'has' method for this field will now return false */
  @Override
  public void clearOrderId() {
    genClient.clear(CacheKey.orderId);
  }
  /** Clears the 'disablePrinting' field, the 'has' method for this field will now return false */
  @Override
  public void clearDisablePrinting() {
    genClient.clear(CacheKey.disablePrinting);
  }
  /** Clears the 'disableReceiptSelection' field, the 'has' method for this field will now return false */
  @Override
  public void clearDisableReceiptSelection() {
    genClient.clear(CacheKey.disableReceiptSelection);
  }
  /** Clears the 'disableDuplicateChecking' field, the 'has' method for this field will now return false */
  @Override
  public void clearDisableDuplicateChecking() {
    genClient.clear(CacheKey.disableDuplicateChecking);
  }
  /** Clears the 'cardNotPresent' field, the 'has' method for this field will now return false */
  @Override
  public void clearCardNotPresent() {
    genClient.clear(CacheKey.cardNotPresent);
  }
  /** Clears the 'disableRestartTransactionOnFail' field, the 'has' method for this field will now return false */
  @Override
  public void clearDisableRestartTransactionOnFail() {
    genClient.clear(CacheKey.disableRestartTransactionOnFail);
  }
  /** Clears the 'amount' field, the 'has' method for this field will now return false */
  @Override
  public void clearAmount() {
    genClient.clear(CacheKey.amount);
  }
  /** Clears the 'cardEntryMethods' field, the 'has' method for this field will now return false */
  @Override
  public void clearCardEntryMethods() {
    genClient.clear(CacheKey.cardEntryMethods);
  }
  /** Clears the 'vaultedCard' field, the 'has' method for this field will now return false */
  @Override
  public void clearVaultedCard() {
    genClient.clear(CacheKey.vaultedCard);
  }
  /** Clears the 'externalId' field, the 'has' method for this field will now return false */
  @Override
  public void clearExternalId() {
    genClient.clear(CacheKey.externalId);
  }
  /** Clears the 'type' field, the 'has' method for this field will now return false */
  @Override
  public void clearType() {
    genClient.clear(CacheKey.type);
  }
  /** Clears the 'autoAcceptPaymentConfirmations' field, the 'has' method for this field will now return false */
  @Override
  public void clearAutoAcceptPaymentConfirmations() {
    genClient.clear(CacheKey.autoAcceptPaymentConfirmations);
  }
  /** Clears the 'extras' field, the 'has' method for this field will now return false */
  @Override
  public void clearExtras() {
    genClient.clear(CacheKey.extras);
  }
  /** Clears the 'regionalExtras' field, the 'has' method for this field will now return false */
  @Override
  public void clearRegionalExtras() {
    genClient.clear(CacheKey.regionalExtras);
  }
  /** Clears the 'externalReferenceId' field, the 'has' method for this field will now return false */
  @Override
  public void clearExternalReferenceId() {
    genClient.clear(CacheKey.externalReferenceId);
  }
  /** Clears the 'requestId' field, the 'has' method for this field will now return false */
  @Override
  public void clearRequestId() {
    genClient.clear(CacheKey.requestId);
  }
  /** Clears the 'version' field, the 'has' method for this field will now return false */
  @Override
  public void clearVersion() {
    genClient.clear(CacheKey.version);
  }


  /**
   * Returns true if this instance has any changes.
   */
  public boolean containsChanges() {
    return genClient.containsChanges();
  }

  /**
   * Reset the log of changes made to this instance, calling copyChanges() after this would return an empty instance.
   */
  public void resetChangeLog() {
    genClient.resetChangeLog();
  }

  /**
   * Create a copy of this instance that contains only fields that were set after the constructor was called.
   */
  public ManualRefundRequest copyChanges() {
    ManualRefundRequest copy = new ManualRefundRequest();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(ManualRefundRequest src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new ManualRefundRequest(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<ManualRefundRequest> CREATOR = new android.os.Parcelable.Creator<ManualRefundRequest>() {
    @Override
    public ManualRefundRequest createFromParcel(android.os.Parcel in) {
      ManualRefundRequest instance = new ManualRefundRequest(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public ManualRefundRequest[] newArray(int size) {
      return new ManualRefundRequest[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<ManualRefundRequest> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<ManualRefundRequest>() {
    public Class<ManualRefundRequest> getCreatedClass() {
      return ManualRefundRequest.class;
    }

    @Override
    public ManualRefundRequest create(org.json.JSONObject jsonObject) {
      return new ManualRefundRequest(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean ORDERID_IS_REQUIRED = false;
    public static final long ORDERID_MAX_LEN = 13;
    public static final boolean DISABLEPRINTING_IS_REQUIRED = false;
    public static final boolean DISABLERECEIPTSELECTION_IS_REQUIRED = false;
    public static final boolean DISABLEDUPLICATECHECKING_IS_REQUIRED = false;
    public static final boolean CARDNOTPRESENT_IS_REQUIRED = false;
    public static final boolean DISABLERESTARTTRANSACTIONONFAIL_IS_REQUIRED = false;
    public static final boolean AMOUNT_IS_REQUIRED = true;
    public static final boolean CARDENTRYMETHODS_IS_REQUIRED = false;
    public static final boolean VAULTEDCARD_IS_REQUIRED = false;
    public static final boolean EXTERNALID_IS_REQUIRED = true;
    public static final boolean TYPE_IS_REQUIRED = false;
    public static final boolean AUTOACCEPTPAYMENTCONFIRMATIONS_IS_REQUIRED = false;
    public static final boolean EXTRAS_IS_REQUIRED = false;
    public static final boolean REGIONALEXTRAS_IS_REQUIRED = false;
    public static final boolean EXTERNALREFERENCEID_IS_REQUIRED = false;
    public static final boolean REQUESTID_IS_REQUIRED = false;
    public static final long REQUESTID_MAX_LEN = 13;
    public static final boolean VERSION_IS_REQUIRED = false;
  }

}
