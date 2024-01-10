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
 * <li>{@link #getAmount amount}</li>
 * <li>{@link #getOrderId orderId}</li>
 * <li>{@link #getSourcePaymentId sourcePaymentId}</li>
 * <li>{@link #getVaultedCard vaultedCard}</li>
 * <li>{@link #getSourcePayment sourcePayment}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class ServiceFeeRequest extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  /**
   * amount (cents) of the service fee
   */
  public java.lang.Long getAmount() {
    return genClient.cacheGet(CacheKey.amount);
  }

  /**
   * Unique identifier of the order with which this payment is associated (will be auto-populated by client connector)
   */
  public java.lang.String getOrderId() {
    return genClient.cacheGet(CacheKey.orderId);
  }

  /**
   * Unique identifier of the primary payment the serivce fee is associated with
   */
  public java.lang.String getSourcePaymentId() {
    return genClient.cacheGet(CacheKey.sourcePaymentId);
  }

  /**
   * Vaulted card to use to bill the service fee
   */
  public com.clover.sdk.v3.payments.VaultedCard getVaultedCard() {
    return genClient.cacheGet(CacheKey.vaultedCard);
  }

  /**
   * Only required when using auth server
   * Payment object
   * /auth/service_fees/payments
   */
  public com.clover.sdk.v3.payments.Payment getSourcePayment() {
    return genClient.cacheGet(CacheKey.sourcePayment);
  }



  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    amount
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    orderId
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    sourcePaymentId
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    sourcePayment
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.payments.Payment.JSON_CREATOR)),
    vaultedCard
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.payments.VaultedCard.JSON_CREATOR)),
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

  private final GenericClient<ServiceFeeRequest> genClient;

  /**
   * Constructs a new empty instance.
   */
  public ServiceFeeRequest() {
    genClient = new GenericClient<ServiceFeeRequest>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected ServiceFeeRequest(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public ServiceFeeRequest(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public ServiceFeeRequest(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public ServiceFeeRequest(ServiceFeeRequest src) {
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

    genClient.validateCloverId(CacheKey.sourcePaymentId, getSourcePaymentId());
  }

  /** Checks whether the 'amount' field is set and is not null */
  public boolean isNotNullAmount() {
    return genClient.cacheValueIsNotNull(CacheKey.amount);
  }

  /** Checks whether the 'orderId' field is set and is not null */
  public boolean isNotNullOrderId() {
    return genClient.cacheValueIsNotNull(CacheKey.orderId);
  }

  /** Checks whether the 'sourcePaymentId' field is set and is not null */
  public boolean isNotNullSourcePaymentId() {
    return genClient.cacheValueIsNotNull(CacheKey.sourcePaymentId);
  }

  /** Checks whether the 'vaultedCard' field is set and is not null */
  public boolean isNotNullVaultedCard() {
    return genClient.cacheValueIsNotNull(CacheKey.vaultedCard);
  }

  public boolean isNotNullSourcePayment() {
    return genClient.cacheValueIsNotNull(CacheKey.sourcePayment);
  }

  /** Checks whether the 'amount' field has been set, however the value could be null */
  public boolean hasAmount() {
    return genClient.cacheHasKey(CacheKey.amount);
  }

  /** Checks whether the 'orderId' field has been set, however the value could be null */
  public boolean hasOrderId() {
    return genClient.cacheHasKey(CacheKey.orderId);
  }

  /** Checks whether the 'sourcePaymentId' field has been set, however the value could be null */
  public boolean hasSourcePaymentId() {
    return genClient.cacheHasKey(CacheKey.sourcePaymentId);
  }

  /** Checks whether the 'vaultedCard' field has been set, however the value could be null */
  public boolean hasVaultedCard() {
    return genClient.cacheHasKey(CacheKey.vaultedCard);
  }

  /** Checks whether the 'sourcePayment' field has been set, however the value could be null */
  public boolean hasSourcePayment() {
    return genClient.cacheHasKey(CacheKey.sourcePayment);
  }

  /**
   * Sets the field 'amount'.
   */
  public ServiceFeeRequest setAmount(java.lang.Long amount) {
    return genClient.setOther(amount, CacheKey.amount);
  }

  /**
   * Sets the field 'orderId'.
   */
  public ServiceFeeRequest setOrderId(java.lang.String orderId) {
    return genClient.setOther(orderId, CacheKey.orderId);
  }

  /**
   * Sets the field 'sourcePaymentId'.
   */
  public ServiceFeeRequest setSourcePaymentId(java.lang.String sourcePaymentId) {
    return genClient.setOther(sourcePaymentId, CacheKey.sourcePaymentId);
  }

  /**
   * Sets the field 'vaultedCard'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public ServiceFeeRequest setVaultedCard(com.clover.sdk.v3.payments.VaultedCard vaultedCard) {
    return genClient.setRecord(vaultedCard, CacheKey.vaultedCard);
  }

  /**
   * Sets the field 'sourcePayment'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public ServiceFeeRequest setSourcePayment(com.clover.sdk.v3.payments.Payment sourcePayment) {
    return genClient.setRecord(sourcePayment, CacheKey.sourcePayment);
  }

  /** Clears the 'amount' field, the 'has' method for this field will now return false */
  public void clearAmount() {
    genClient.clear(CacheKey.amount);
  }
  /** Clears the 'orderId' field, the 'has' method for this field will now return false */
  public void clearOrderId() {
    genClient.clear(CacheKey.orderId);
  }
  /** Clears the 'sourcePaymentId' field, the 'has' method for this field will now return false */
  public void clearSourcePaymentId() {
    genClient.clear(CacheKey.sourcePaymentId);
  }
  /** Clears the 'vaultedCard' field, the 'has' method for this field will now return false */
  public void clearVaultedCard() {
    genClient.clear(CacheKey.vaultedCard);
  }

  /** Clears the 'sourcePayment' field, the 'has' method for this field will now return false */
  public void clearSourcePayment() {
    genClient.clear(CacheKey.sourcePayment);
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
  public ServiceFeeRequest copyChanges() {
    ServiceFeeRequest copy = new ServiceFeeRequest();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(ServiceFeeRequest src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new ServiceFeeRequest(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<ServiceFeeRequest> CREATOR = new android.os.Parcelable.Creator<ServiceFeeRequest>() {
    @Override
    public ServiceFeeRequest createFromParcel(android.os.Parcel in) {
      ServiceFeeRequest instance = new ServiceFeeRequest(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public ServiceFeeRequest[] newArray(int size) {
      return new ServiceFeeRequest[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<ServiceFeeRequest> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<ServiceFeeRequest>() {
    public Class<ServiceFeeRequest> getCreatedClass() {
      return ServiceFeeRequest.class;
    }

    @Override
    public ServiceFeeRequest create(org.json.JSONObject jsonObject) {
      return new ServiceFeeRequest(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean AMOUNT_IS_REQUIRED = false;
    public static final boolean ORDERID_IS_REQUIRED = false;
    public static final long ORDERID_MAX_LEN = 13;
    public static final boolean SOURCEPAYMENTID_IS_REQUIRED = false;
    public static final long SOURCEPAYMENTID_MAX_LEN = 13;
    public static final boolean VAULTEDCARD_IS_REQUIRED = false;
    public static final boolean SOURCE_PAYMENT_IS_REQUIRED = false;
  }

}
