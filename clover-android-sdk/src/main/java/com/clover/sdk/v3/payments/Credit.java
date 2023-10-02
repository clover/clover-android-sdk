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

package com.clover.sdk.v3.payments;

import com.clover.sdk.GenericClient;
import com.clover.sdk.GenericParcelable;

/**
 * This is an auto-generated Clover data object.
 * <p>
 * <h3>Fields</h3>
 * <ul>
 * <li>{@link #getId id}</li>
 * <li>{@link #getOrderRef orderRef}</li>
 * <li>{@link #getDevice device}</li>
 * <li>{@link #getTender tender}</li>
 * <li>{@link #getEmployee employee}</li>
 * <li>{@link #getCustomers customers}</li>
 * <li>{@link #getAmount amount}</li>
 * <li>{@link #getTaxAmount taxAmount}</li>
 * <li>{@link #getTaxRates taxRates}</li>
 * <li>{@link #getCreatedTime createdTime}</li>
 * <li>{@link #getClientCreatedTime clientCreatedTime}</li>
 * <li>{@link #getCardTransaction cardTransaction}</li>
 * <li>{@link #getVoided voided}</li>
 * <li>{@link #getVoidReason voidReason}</li>
 * <li>{@link #getDccInfo dccInfo}</li>
 * <li>{@link #getTransactionSettings transactionSettings}</li>
 * <li>{@link #getCreditRefunds creditRefunds}</li>
 * <li>{@link #getGermanInfo germanInfo}</li>
 * <li>{@link #getAppTracking appTracking}</li>
 * <li>{@link #getResult result}</li>
 * <li>{@link #getReason reason}</li>
 * <li>{@link #getTransactionInfo transactionInfo}</li>
 * <li>{@link #getMerchant merchant}</li>
 * <li>{@link #getExternalReferenceId externalReferenceId}</li>
 * <li>{@link #getCreditAttributes creditAttributes}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class Credit extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  /**
   * Unique identifier
   */
  public java.lang.String getId() {
    return genClient.cacheGet(CacheKey.id);
  }

  /**
   * The order with which the credit is associated
   */
  public com.clover.sdk.v3.base.Reference getOrderRef() {
    return genClient.cacheGet(CacheKey.orderRef);
  }

  /**
   * Device which processed this credit, a 128-bit UUID, not a normal base-13 Clover ID.
   */
  public com.clover.sdk.v3.base.Reference getDevice() {
    return genClient.cacheGet(CacheKey.device);
  }

  /**
   * The tender type associated with this payment, e.g. credit card, cash, etc.
   */
  public com.clover.sdk.v3.base.Tender getTender() {
    return genClient.cacheGet(CacheKey.tender);
  }

  /**
   * The employee who processed the payment
   */
  public com.clover.sdk.v3.base.Reference getEmployee() {
    return genClient.cacheGet(CacheKey.employee);
  }

  /**
   * Customer who received the credit/refund
   */
  public com.clover.sdk.v3.customers.Customer getCustomers() {
    return genClient.cacheGet(CacheKey.customers);
  }

  /**
   * Amount paid
   */
  public java.lang.Long getAmount() {
    return genClient.cacheGet(CacheKey.amount);
  }

  /**
   * Amount paid in tax
   */
  public java.lang.Long getTaxAmount() {
    return genClient.cacheGet(CacheKey.taxAmount);
  }

  public java.util.List<com.clover.sdk.v3.payments.TaxableAmountRate> getTaxRates() {
    return genClient.cacheGet(CacheKey.taxRates);
  }

  /**
   * Time payment was recorded on server
   */
  public java.lang.Long getCreatedTime() {
    return genClient.cacheGet(CacheKey.createdTime);
  }

  public java.lang.Long getClientCreatedTime() {
    return genClient.cacheGet(CacheKey.clientCreatedTime);
  }

  /**
   * Information about the card used for credit/debit card payments
   */
  public com.clover.sdk.v3.payments.CardTransaction getCardTransaction() {
    return genClient.cacheGet(CacheKey.cardTransaction);
  }

  public java.lang.Boolean getVoided() {
    return genClient.cacheGet(CacheKey.voided);
  }

  public java.lang.String getVoidReason() {
    return genClient.cacheGet(CacheKey.voidReason);
  }

  /**
   * Dynamic Currency Conversion information
   */
  public com.clover.sdk.v3.payments.DCCInfo getDccInfo() {
    return genClient.cacheGet(CacheKey.dccInfo);
  }

  /**
   * Per transaction settings for the payment
   */
  public com.clover.sdk.v3.payments.TransactionSettings getTransactionSettings() {
    return genClient.cacheGet(CacheKey.transactionSettings);
  }

  public java.util.List<com.clover.sdk.v3.payments.CreditRefund> getCreditRefunds() {
    return genClient.cacheGet(CacheKey.creditRefunds);
  }

  /**
   * German region-specific information
   */
  public com.clover.sdk.v3.payments.GermanInfo getGermanInfo() {
    return genClient.cacheGet(CacheKey.germanInfo);
  }

  /**
   * Tracking information for the app that created this credit.
   */
  public com.clover.sdk.v3.apps.AppTracking getAppTracking() {
    return genClient.cacheGet(CacheKey.appTracking);
  }

  public com.clover.sdk.v3.payments.Result getResult() {
    return genClient.cacheGet(CacheKey.result);
  }

  /**
   * Reason why the credit was initiated
   */
  public java.lang.String getReason() {
    return genClient.cacheGet(CacheKey.reason);
  }

  /**
   * Transaction information
   */
  public com.clover.sdk.v3.payments.TransactionInfo getTransactionInfo() {
    return genClient.cacheGet(CacheKey.transactionInfo);
  }

  public com.clover.sdk.v3.base.Reference getMerchant() {
    return genClient.cacheGet(CacheKey.merchant);
  }

  /**
   * The external reference id if associated with the credit
   */
  public java.lang.String getExternalReferenceId() {
    return genClient.cacheGet(CacheKey.externalReferenceId);
  }

  public java.util.Map<java.lang.String,java.lang.String> getCreditAttributes() {
    return genClient.cacheGet(CacheKey.creditAttributes);
  }



  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    id
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    orderRef
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.base.Reference.JSON_CREATOR)),
    device
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.base.Reference.JSON_CREATOR)),
    tender
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.base.Tender.JSON_CREATOR)),
    employee
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.base.Reference.JSON_CREATOR)),
    customers
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.customers.Customer.JSON_CREATOR)),
    amount
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    taxAmount
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    taxRates
        (com.clover.sdk.extractors.RecordListExtractionStrategy.instance(com.clover.sdk.v3.payments.TaxableAmountRate.JSON_CREATOR)),
    createdTime
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    clientCreatedTime
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    cardTransaction
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.payments.CardTransaction.JSON_CREATOR)),
    voided
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Boolean.class)),
    voidReason
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    dccInfo
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.payments.DCCInfo.JSON_CREATOR)),
    transactionSettings
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.payments.TransactionSettings.JSON_CREATOR)),
    creditRefunds
        (com.clover.sdk.extractors.RecordListExtractionStrategy.instance(com.clover.sdk.v3.payments.CreditRefund.JSON_CREATOR)),
    germanInfo
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.payments.GermanInfo.JSON_CREATOR)),
    appTracking
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.apps.AppTracking.JSON_CREATOR)),
    result
        (com.clover.sdk.extractors.EnumExtractionStrategy.instance(com.clover.sdk.v3.payments.Result.class)),
    reason
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    transactionInfo
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.payments.TransactionInfo.JSON_CREATOR)),
    merchant
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.base.Reference.JSON_CREATOR)),
    externalReferenceId
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    creditAttributes
        (com.clover.sdk.extractors.MapExtractionStrategy.instance()),
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

  private final GenericClient<Credit> genClient;

  /**
   * Constructs a new empty instance.
   */
  public Credit() {
    genClient = new GenericClient<Credit>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected Credit(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public Credit(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public Credit(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public Credit(Credit src) {
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
    genClient.validateCloverId(CacheKey.id, getId());

    genClient.validateLength(CacheKey.reason, getReason(), 255);
    genClient.validateReferences(CacheKey.orderRef);
    genClient.validateReferences(CacheKey.employee);
    genClient.validateReferences(CacheKey.merchant);
  }

  /** Checks whether the 'id' field is set and is not null */
  public boolean isNotNullId() {
    return genClient.cacheValueIsNotNull(CacheKey.id);
  }

  /** Checks whether the 'orderRef' field is set and is not null */
  public boolean isNotNullOrderRef() {
    return genClient.cacheValueIsNotNull(CacheKey.orderRef);
  }

  /** Checks whether the 'device' field is set and is not null */
  public boolean isNotNullDevice() {
    return genClient.cacheValueIsNotNull(CacheKey.device);
  }

  /** Checks whether the 'tender' field is set and is not null */
  public boolean isNotNullTender() {
    return genClient.cacheValueIsNotNull(CacheKey.tender);
  }

  /** Checks whether the 'employee' field is set and is not null */
  public boolean isNotNullEmployee() {
    return genClient.cacheValueIsNotNull(CacheKey.employee);
  }

  /** Checks whether the 'customers' field is set and is not null */
  public boolean isNotNullCustomers() {
    return genClient.cacheValueIsNotNull(CacheKey.customers);
  }

  /** Checks whether the 'amount' field is set and is not null */
  public boolean isNotNullAmount() {
    return genClient.cacheValueIsNotNull(CacheKey.amount);
  }

  /** Checks whether the 'taxAmount' field is set and is not null */
  public boolean isNotNullTaxAmount() {
    return genClient.cacheValueIsNotNull(CacheKey.taxAmount);
  }

  /** Checks whether the 'taxRates' field is set and is not null */
  public boolean isNotNullTaxRates() {
    return genClient.cacheValueIsNotNull(CacheKey.taxRates);
  }

  /** Checks whether the 'taxRates' field is set and is not null and is not empty */
  public boolean isNotEmptyTaxRates() { return isNotNullTaxRates() && !getTaxRates().isEmpty(); }

  /** Checks whether the 'createdTime' field is set and is not null */
  public boolean isNotNullCreatedTime() {
    return genClient.cacheValueIsNotNull(CacheKey.createdTime);
  }

  /** Checks whether the 'clientCreatedTime' field is set and is not null */
  public boolean isNotNullClientCreatedTime() {
    return genClient.cacheValueIsNotNull(CacheKey.clientCreatedTime);
  }

  /** Checks whether the 'cardTransaction' field is set and is not null */
  public boolean isNotNullCardTransaction() {
    return genClient.cacheValueIsNotNull(CacheKey.cardTransaction);
  }

  /** Checks whether the 'voided' field is set and is not null */
  public boolean isNotNullVoided() {
    return genClient.cacheValueIsNotNull(CacheKey.voided);
  }

  /** Checks whether the 'voidReason' field is set and is not null */
  public boolean isNotNullVoidReason() {
    return genClient.cacheValueIsNotNull(CacheKey.voidReason);
  }

  /** Checks whether the 'dccInfo' field is set and is not null */
  public boolean isNotNullDccInfo() {
    return genClient.cacheValueIsNotNull(CacheKey.dccInfo);
  }

  /** Checks whether the 'transactionSettings' field is set and is not null */
  public boolean isNotNullTransactionSettings() {
    return genClient.cacheValueIsNotNull(CacheKey.transactionSettings);
  }

  /** Checks whether the 'creditRefunds' field is set and is not null */
  public boolean isNotNullCreditRefunds() {
    return genClient.cacheValueIsNotNull(CacheKey.creditRefunds);
  }

  /** Checks whether the 'creditRefunds' field is set and is not null and is not empty */
  public boolean isNotEmptyCreditRefunds() { return isNotNullCreditRefunds() && !getCreditRefunds().isEmpty(); }

  /** Checks whether the 'germanInfo' field is set and is not null */
  public boolean isNotNullGermanInfo() {
    return genClient.cacheValueIsNotNull(CacheKey.germanInfo);
  }

  /** Checks whether the 'appTracking' field is set and is not null */
  public boolean isNotNullAppTracking() {
    return genClient.cacheValueIsNotNull(CacheKey.appTracking);
  }

  /** Checks whether the 'result' field is set and is not null */
  public boolean isNotNullResult() {
    return genClient.cacheValueIsNotNull(CacheKey.result);
  }

  /** Checks whether the 'reason' field is set and is not null */
  public boolean isNotNullReason() {
    return genClient.cacheValueIsNotNull(CacheKey.reason);
  }

  /** Checks whether the 'transactionInfo' field is set and is not null */
  public boolean isNotNullTransactionInfo() {
    return genClient.cacheValueIsNotNull(CacheKey.transactionInfo);
  }

  /** Checks whether the 'merchant' field is set and is not null */
  public boolean isNotNullMerchant() {
    return genClient.cacheValueIsNotNull(CacheKey.merchant);
  }

  /** Checks whether the 'externalReferenceId' field is set and is not null */
  public boolean isNotNullExternalReferenceId() {
    return genClient.cacheValueIsNotNull(CacheKey.externalReferenceId);
  }

  /** Checks whether the 'creditAttributes' field is set and is not null */
  public boolean isNotNullCreditAttributes() {
    return genClient.cacheValueIsNotNull(CacheKey.creditAttributes);
  }

  /** Checks whether the 'creditAttributes' field is set and is not null and is not empty */
  public boolean isNotEmptyCreditAttributes() { return isNotNullCreditAttributes() && !getCreditAttributes().isEmpty(); }


  /** Checks whether the 'id' field has been set, however the value could be null */
  public boolean hasId() {
    return genClient.cacheHasKey(CacheKey.id);
  }

  /** Checks whether the 'orderRef' field has been set, however the value could be null */
  public boolean hasOrderRef() {
    return genClient.cacheHasKey(CacheKey.orderRef);
  }

  /** Checks whether the 'device' field has been set, however the value could be null */
  public boolean hasDevice() {
    return genClient.cacheHasKey(CacheKey.device);
  }

  /** Checks whether the 'tender' field has been set, however the value could be null */
  public boolean hasTender() {
    return genClient.cacheHasKey(CacheKey.tender);
  }

  /** Checks whether the 'employee' field has been set, however the value could be null */
  public boolean hasEmployee() {
    return genClient.cacheHasKey(CacheKey.employee);
  }

  /** Checks whether the 'customers' field has been set, however the value could be null */
  public boolean hasCustomers() {
    return genClient.cacheHasKey(CacheKey.customers);
  }

  /** Checks whether the 'amount' field has been set, however the value could be null */
  public boolean hasAmount() {
    return genClient.cacheHasKey(CacheKey.amount);
  }

  /** Checks whether the 'taxAmount' field has been set, however the value could be null */
  public boolean hasTaxAmount() {
    return genClient.cacheHasKey(CacheKey.taxAmount);
  }

  /** Checks whether the 'taxRates' field has been set, however the value could be null */
  public boolean hasTaxRates() {
    return genClient.cacheHasKey(CacheKey.taxRates);
  }

  /** Checks whether the 'createdTime' field has been set, however the value could be null */
  public boolean hasCreatedTime() {
    return genClient.cacheHasKey(CacheKey.createdTime);
  }

  /** Checks whether the 'clientCreatedTime' field has been set, however the value could be null */
  public boolean hasClientCreatedTime() {
    return genClient.cacheHasKey(CacheKey.clientCreatedTime);
  }

  /** Checks whether the 'cardTransaction' field has been set, however the value could be null */
  public boolean hasCardTransaction() {
    return genClient.cacheHasKey(CacheKey.cardTransaction);
  }

  /** Checks whether the 'voided' field has been set, however the value could be null */
  public boolean hasVoided() {
    return genClient.cacheHasKey(CacheKey.voided);
  }

  /** Checks whether the 'voidReason' field has been set, however the value could be null */
  public boolean hasVoidReason() {
    return genClient.cacheHasKey(CacheKey.voidReason);
  }

  /** Checks whether the 'dccInfo' field has been set, however the value could be null */
  public boolean hasDccInfo() {
    return genClient.cacheHasKey(CacheKey.dccInfo);
  }

  /** Checks whether the 'transactionSettings' field has been set, however the value could be null */
  public boolean hasTransactionSettings() {
    return genClient.cacheHasKey(CacheKey.transactionSettings);
  }

  /** Checks whether the 'creditRefunds' field has been set, however the value could be null */
  public boolean hasCreditRefunds() {
    return genClient.cacheHasKey(CacheKey.creditRefunds);
  }

  /** Checks whether the 'germanInfo' field has been set, however the value could be null */
  public boolean hasGermanInfo() {
    return genClient.cacheHasKey(CacheKey.germanInfo);
  }

  /** Checks whether the 'appTracking' field has been set, however the value could be null */
  public boolean hasAppTracking() {
    return genClient.cacheHasKey(CacheKey.appTracking);
  }

  /** Checks whether the 'result' field has been set, however the value could be null */
  public boolean hasResult() {
    return genClient.cacheHasKey(CacheKey.result);
  }

  /** Checks whether the 'reason' field has been set, however the value could be null */
  public boolean hasReason() {
    return genClient.cacheHasKey(CacheKey.reason);
  }

  /** Checks whether the 'transactionInfo' field has been set, however the value could be null */
  public boolean hasTransactionInfo() {
    return genClient.cacheHasKey(CacheKey.transactionInfo);
  }

  /** Checks whether the 'merchant' field has been set, however the value could be null */
  public boolean hasMerchant() {
    return genClient.cacheHasKey(CacheKey.merchant);
  }

  /** Checks whether the 'externalReferenceId' field has been set, however the value could be null */
  public boolean hasExternalReferenceId() {
    return genClient.cacheHasKey(CacheKey.externalReferenceId);
  }

  /** Checks whether the 'creditAttributes' field has been set, however the value could be null */
  public boolean hasCreditAttributes() {
    return genClient.cacheHasKey(CacheKey.creditAttributes);
  }

  /**
   * Sets the field 'id'.
   */
  public Credit setId(java.lang.String id) {
    return genClient.setOther(id, CacheKey.id);
  }

  /**
   * Sets the field 'orderRef'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public Credit setOrderRef(com.clover.sdk.v3.base.Reference orderRef) {
    return genClient.setRecord(orderRef, CacheKey.orderRef);
  }

  /**
   * Sets the field 'device'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public Credit setDevice(com.clover.sdk.v3.base.Reference device) {
    return genClient.setRecord(device, CacheKey.device);
  }

  /**
   * Sets the field 'tender'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public Credit setTender(com.clover.sdk.v3.base.Tender tender) {
    return genClient.setRecord(tender, CacheKey.tender);
  }

  /**
   * Sets the field 'employee'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public Credit setEmployee(com.clover.sdk.v3.base.Reference employee) {
    return genClient.setRecord(employee, CacheKey.employee);
  }

  /**
   * Sets the field 'customers'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public Credit setCustomers(com.clover.sdk.v3.customers.Customer customers) {
    return genClient.setRecord(customers, CacheKey.customers);
  }

  /**
   * Sets the field 'amount'.
   */
  public Credit setAmount(java.lang.Long amount) {
    return genClient.setOther(amount, CacheKey.amount);
  }

  /**
   * Sets the field 'taxAmount'.
   */
  public Credit setTaxAmount(java.lang.Long taxAmount) {
    return genClient.setOther(taxAmount, CacheKey.taxAmount);
  }

  /**
   * Sets the field 'taxRates'.
   *
   * Nulls in the given List are skipped. List parameter is copied, so it will not reflect any changes, but objects inside it will.
   */
  public Credit setTaxRates(java.util.List<com.clover.sdk.v3.payments.TaxableAmountRate> taxRates) {
    return genClient.setArrayRecord(taxRates, CacheKey.taxRates);
  }

  /**
   * Sets the field 'createdTime'.
   */
  public Credit setCreatedTime(java.lang.Long createdTime) {
    return genClient.setOther(createdTime, CacheKey.createdTime);
  }

  /**
   * Sets the field 'clientCreatedTime'.
   */
  public Credit setClientCreatedTime(java.lang.Long clientCreatedTime) {
    return genClient.setOther(clientCreatedTime, CacheKey.clientCreatedTime);
  }

  /**
   * Sets the field 'cardTransaction'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public Credit setCardTransaction(com.clover.sdk.v3.payments.CardTransaction cardTransaction) {
    return genClient.setRecord(cardTransaction, CacheKey.cardTransaction);
  }

  /**
   * Sets the field 'voided'.
   */
  public Credit setVoided(java.lang.Boolean voided) {
    return genClient.setOther(voided, CacheKey.voided);
  }

  /**
   * Sets the field 'voidReason'.
   */
  public Credit setVoidReason(java.lang.String voidReason) {
    return genClient.setOther(voidReason, CacheKey.voidReason);
  }

  /**
   * Sets the field 'dccInfo'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public Credit setDccInfo(com.clover.sdk.v3.payments.DCCInfo dccInfo) {
    return genClient.setRecord(dccInfo, CacheKey.dccInfo);
  }

  /**
   * Sets the field 'transactionSettings'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public Credit setTransactionSettings(com.clover.sdk.v3.payments.TransactionSettings transactionSettings) {
    return genClient.setRecord(transactionSettings, CacheKey.transactionSettings);
  }

  /**
   * Sets the field 'creditRefunds'.
   *
   * Nulls in the given List are skipped. List parameter is copied, so it will not reflect any changes, but objects inside it will.
   */
  public Credit setCreditRefunds(java.util.List<com.clover.sdk.v3.payments.CreditRefund> creditRefunds) {
    return genClient.setArrayRecord(creditRefunds, CacheKey.creditRefunds);
  }

  /**
   * Sets the field 'germanInfo'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public Credit setGermanInfo(com.clover.sdk.v3.payments.GermanInfo germanInfo) {
    return genClient.setRecord(germanInfo, CacheKey.germanInfo);
  }

  /**
   * Sets the field 'appTracking'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public Credit setAppTracking(com.clover.sdk.v3.apps.AppTracking appTracking) {
    return genClient.setRecord(appTracking, CacheKey.appTracking);
  }

  /**
   * Sets the field 'result'.
   */
  public Credit setResult(com.clover.sdk.v3.payments.Result result) {
    return genClient.setOther(result, CacheKey.result);
  }

  /**
   * Sets the field 'reason'.
   */
  public Credit setReason(java.lang.String reason) {
    return genClient.setOther(reason, CacheKey.reason);
  }

  /**
   * Sets the field 'transactionInfo'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public Credit setTransactionInfo(com.clover.sdk.v3.payments.TransactionInfo transactionInfo) {
    return genClient.setRecord(transactionInfo, CacheKey.transactionInfo);
  }

  /**
   * Sets the field 'merchant'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public Credit setMerchant(com.clover.sdk.v3.base.Reference merchant) {
    return genClient.setRecord(merchant, CacheKey.merchant);
  }

  /**
   * Sets the field 'externalReferenceId'.
   */
  public Credit setExternalReferenceId(java.lang.String externalReferenceId) {
    return genClient.setOther(externalReferenceId, CacheKey.externalReferenceId);
  }

  /**
   * Sets the field 'creditAttributes'.
   */
  public Credit setCreditAttributes(java.util.Map<java.lang.String,java.lang.String> creditAttributes) {
    return genClient.setOther(creditAttributes, CacheKey.creditAttributes);
  }

  /** Clears the 'id' field, the 'has' method for this field will now return false */
  public void clearId() {
    genClient.clear(CacheKey.id);
  }
  /** Clears the 'orderRef' field, the 'has' method for this field will now return false */
  public void clearOrderRef() {
    genClient.clear(CacheKey.orderRef);
  }
  /** Clears the 'device' field, the 'has' method for this field will now return false */
  public void clearDevice() {
    genClient.clear(CacheKey.device);
  }
  /** Clears the 'tender' field, the 'has' method for this field will now return false */
  public void clearTender() {
    genClient.clear(CacheKey.tender);
  }
  /** Clears the 'employee' field, the 'has' method for this field will now return false */
  public void clearEmployee() {
    genClient.clear(CacheKey.employee);
  }
  /** Clears the 'customers' field, the 'has' method for this field will now return false */
  public void clearCustomers() {
    genClient.clear(CacheKey.customers);
  }
  /** Clears the 'amount' field, the 'has' method for this field will now return false */
  public void clearAmount() {
    genClient.clear(CacheKey.amount);
  }
  /** Clears the 'taxAmount' field, the 'has' method for this field will now return false */
  public void clearTaxAmount() {
    genClient.clear(CacheKey.taxAmount);
  }
  /** Clears the 'taxRates' field, the 'has' method for this field will now return false */
  public void clearTaxRates() {
    genClient.clear(CacheKey.taxRates);
  }
  /** Clears the 'createdTime' field, the 'has' method for this field will now return false */
  public void clearCreatedTime() {
    genClient.clear(CacheKey.createdTime);
  }
  /** Clears the 'clientCreatedTime' field, the 'has' method for this field will now return false */
  public void clearClientCreatedTime() {
    genClient.clear(CacheKey.clientCreatedTime);
  }
  /** Clears the 'cardTransaction' field, the 'has' method for this field will now return false */
  public void clearCardTransaction() {
    genClient.clear(CacheKey.cardTransaction);
  }
  /** Clears the 'voided' field, the 'has' method for this field will now return false */
  public void clearVoided() {
    genClient.clear(CacheKey.voided);
  }
  /** Clears the 'voidReason' field, the 'has' method for this field will now return false */
  public void clearVoidReason() {
    genClient.clear(CacheKey.voidReason);
  }
  /** Clears the 'dccInfo' field, the 'has' method for this field will now return false */
  public void clearDccInfo() {
    genClient.clear(CacheKey.dccInfo);
  }
  /** Clears the 'transactionSettings' field, the 'has' method for this field will now return false */
  public void clearTransactionSettings() {
    genClient.clear(CacheKey.transactionSettings);
  }
  /** Clears the 'creditRefunds' field, the 'has' method for this field will now return false */
  public void clearCreditRefunds() {
    genClient.clear(CacheKey.creditRefunds);
  }
  /** Clears the 'germanInfo' field, the 'has' method for this field will now return false */
  public void clearGermanInfo() {
    genClient.clear(CacheKey.germanInfo);
  }
  /** Clears the 'appTracking' field, the 'has' method for this field will now return false */
  public void clearAppTracking() {
    genClient.clear(CacheKey.appTracking);
  }
  /** Clears the 'result' field, the 'has' method for this field will now return false */
  public void clearResult() {
    genClient.clear(CacheKey.result);
  }
  /** Clears the 'reason' field, the 'has' method for this field will now return false */
  public void clearReason() {
    genClient.clear(CacheKey.reason);
  }
  /** Clears the 'transactionInfo' field, the 'has' method for this field will now return false */
  public void clearTransactionInfo() {
    genClient.clear(CacheKey.transactionInfo);
  }
  /** Clears the 'merchant' field, the 'has' method for this field will now return false */
  public void clearMerchant() {
    genClient.clear(CacheKey.merchant);
  }
  /** Clears the 'externalReferenceId' field, the 'has' method for this field will now return false */
  public void clearExternalReferenceId() {
    genClient.clear(CacheKey.externalReferenceId);
  }
  /** Clears the 'creditAttributes' field, the 'has' method for this field will now return false */
  public void clearCreditAttributes() {
    genClient.clear(CacheKey.creditAttributes);
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
  public Credit copyChanges() {
    Credit copy = new Credit();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(Credit src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new Credit(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<Credit> CREATOR = new android.os.Parcelable.Creator<Credit>() {
    @Override
    public Credit createFromParcel(android.os.Parcel in) {
      Credit instance = new Credit(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public Credit[] newArray(int size) {
      return new Credit[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<Credit> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<Credit>() {
    public Class<Credit> getCreatedClass() {
      return Credit.class;
    }

    @Override
    public Credit create(org.json.JSONObject jsonObject) {
      return new Credit(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean ID_IS_REQUIRED = false;
    public static final long ID_MAX_LEN = 13;
    public static final boolean ORDERREF_IS_REQUIRED = false;
    public static final boolean DEVICE_IS_REQUIRED = false;
    public static final boolean TENDER_IS_REQUIRED = false;
    public static final boolean EMPLOYEE_IS_REQUIRED = false;
    public static final boolean CUSTOMERS_IS_REQUIRED = false;
    public static final boolean AMOUNT_IS_REQUIRED = false;
    public static final boolean TAXAMOUNT_IS_REQUIRED = false;
    public static final boolean TAXRATES_IS_REQUIRED = false;
    public static final boolean CREATEDTIME_IS_REQUIRED = false;
    public static final boolean CLIENTCREATEDTIME_IS_REQUIRED = false;
    public static final boolean CARDTRANSACTION_IS_REQUIRED = false;
    public static final boolean VOIDED_IS_REQUIRED = false;
    public static final boolean VOIDREASON_IS_REQUIRED = false;
    public static final boolean DCCINFO_IS_REQUIRED = false;
    public static final boolean TRANSACTIONSETTINGS_IS_REQUIRED = false;
    public static final boolean CREDITREFUNDS_IS_REQUIRED = false;
    public static final boolean GERMANINFO_IS_REQUIRED = false;
    public static final boolean APPTRACKING_IS_REQUIRED = false;
    public static final boolean RESULT_IS_REQUIRED = false;
    public static final boolean REASON_IS_REQUIRED = false;
    public static final long REASON_MAX_LEN = 255;
    public static final boolean TRANSACTIONINFO_IS_REQUIRED = false;
    public static final boolean MERCHANT_IS_REQUIRED = false;
    public static final boolean EXTERNALREFERENCEID_IS_REQUIRED = false;
    public static final boolean CREDITATTRIBUTES_IS_REQUIRED = false;
  }

}
