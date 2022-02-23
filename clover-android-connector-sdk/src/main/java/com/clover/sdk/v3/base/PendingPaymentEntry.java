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

package com.clover.sdk.v3.base;

import com.clover.sdk.GenericClient;
import com.clover.sdk.GenericParcelable;

/**
 * This is an auto-generated Clover data object.
 * <p>
 * <h3>Fields</h3>
 * <ul>
 * <li>{@link #getAmount amount}</li>
 * <li>{@link #getPaymentId paymentId}</li>
 * <li>{@link #getExternalPaymentId externalPaymentId}</li>
 * <li>{@link #getExternalId externalId}</li>
 * <li>{@link #getTipAmount tipAmount}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class PendingPaymentEntry extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  /**
   * Total amount paid
   */
  public java.lang.Long getAmount() {
    return genClient.cacheGet(CacheKey.amount);
  }

  /**
   * Unique identifier for a payment
   */
  public java.lang.String getPaymentId() {
    return genClient.cacheGet(CacheKey.paymentId);
  }

  /**
   * An id that will be persisted with transactions.
   */
  public java.lang.String getExternalPaymentId() {
    return genClient.cacheGet(CacheKey.externalPaymentId);
  }

  /**
   * @deprecated - use com.clover.sdk.v3.base.PendingPaymentEntry#setExternalPaymentId(java.lang.Long)
   */
  public java.lang.String getExternalId() {
    return genClient.cacheGet(CacheKey.externalId);
  }

  /**
   * Included tip
   */
  public java.lang.Long getTipAmount() {
    return genClient.cacheGet(CacheKey.tipAmount);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    amount
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    paymentId
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    externalPaymentId
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    externalId
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    tipAmount
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
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

  private final GenericClient<PendingPaymentEntry> genClient;

  /**
   * Constructs a new empty instance.
   */
  public PendingPaymentEntry() {
    genClient = new GenericClient<PendingPaymentEntry>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected PendingPaymentEntry(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public PendingPaymentEntry(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public PendingPaymentEntry(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public PendingPaymentEntry(PendingPaymentEntry src) {
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

    genClient.validateCloverId(CacheKey.paymentId, getPaymentId());
  }

  /** Checks whether the 'amount' field is set and is not null */
  public boolean isNotNullAmount() {
    return genClient.cacheValueIsNotNull(CacheKey.amount);
  }

  /** Checks whether the 'paymentId' field is set and is not null */
  public boolean isNotNullPaymentId() {
    return genClient.cacheValueIsNotNull(CacheKey.paymentId);
  }

  /** Checks whether the 'externalPaymentId' field is set and is not null */
  public boolean isNotNullExternalPaymentId() {
    return genClient.cacheValueIsNotNull(CacheKey.externalPaymentId);
  }

  /** Checks whether the 'externalId' field is set and is not null */
  public boolean isNotNullExternalId() {
    return genClient.cacheValueIsNotNull(CacheKey.externalId);
  }

  /** Checks whether the 'tipAmount' field is set and is not null */
  public boolean isNotNullTipAmount() {
    return genClient.cacheValueIsNotNull(CacheKey.tipAmount);
  }



  /** Checks whether the 'amount' field has been set, however the value could be null */
  public boolean hasAmount() {
    return genClient.cacheHasKey(CacheKey.amount);
  }

  /** Checks whether the 'paymentId' field has been set, however the value could be null */
  public boolean hasPaymentId() {
    return genClient.cacheHasKey(CacheKey.paymentId);
  }

  /** Checks whether the 'externalPaymentId' field has been set, however the value could be null */
  public boolean hasExternalPaymentId() {
    return genClient.cacheHasKey(CacheKey.externalPaymentId);
  }

  /** Checks whether the 'externalId' field has been set, however the value could be null */
  public boolean hasExternalId() {
    return genClient.cacheHasKey(CacheKey.externalId);
  }

  /** Checks whether the 'tipAmount' field has been set, however the value could be null */
  public boolean hasTipAmount() {
    return genClient.cacheHasKey(CacheKey.tipAmount);
  }


  /**
   * Sets the field 'amount'.
   */
  public PendingPaymentEntry setAmount(java.lang.Long amount) {
    return genClient.setOther(amount, CacheKey.amount);
  }

  /**
   * Sets the field 'paymentId'.
   */
  public PendingPaymentEntry setPaymentId(java.lang.String paymentId) {
    return genClient.setOther(paymentId, CacheKey.paymentId);
  }

  /**
   * Sets the field 'externalPaymentId'.
   */
  public PendingPaymentEntry setExternalPaymentId(java.lang.String externalPaymentId) {
    return genClient.setOther(externalPaymentId, CacheKey.externalPaymentId);
  }

  /**
   * Sets the field 'externalId'.
   */
  public PendingPaymentEntry setExternalId(java.lang.String externalId) {
    return genClient.setOther(externalId, CacheKey.externalId);
  }

  /**
   * Sets the field 'tipAmount'.
   */
  public PendingPaymentEntry setTipAmount(java.lang.Long tipAmount) {
    return genClient.setOther(tipAmount, CacheKey.tipAmount);
  }


  /** Clears the 'amount' field, the 'has' method for this field will now return false */
  public void clearAmount() {
    genClient.clear(CacheKey.amount);
  }
  /** Clears the 'paymentId' field, the 'has' method for this field will now return false */
  public void clearPaymentId() {
    genClient.clear(CacheKey.paymentId);
  }
  /** Clears the 'externalPaymentId' field, the 'has' method for this field will now return false */
  public void clearExternalPaymentId() {
    genClient.clear(CacheKey.externalPaymentId);
  }
  /** Clears the 'externalId' field, the 'has' method for this field will now return false */
  public void clearExternalId() {
    genClient.clear(CacheKey.externalId);
  }
  /** Clears the 'tipAmount' field, the 'has' method for this field will now return false */
  public void clearTipAmount() {
    genClient.clear(CacheKey.tipAmount);
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
  public PendingPaymentEntry copyChanges() {
    PendingPaymentEntry copy = new PendingPaymentEntry();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(PendingPaymentEntry src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new PendingPaymentEntry(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<PendingPaymentEntry> CREATOR = new android.os.Parcelable.Creator<PendingPaymentEntry>() {
    @Override
    public PendingPaymentEntry createFromParcel(android.os.Parcel in) {
      PendingPaymentEntry instance = new PendingPaymentEntry(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public PendingPaymentEntry[] newArray(int size) {
      return new PendingPaymentEntry[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<PendingPaymentEntry> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<PendingPaymentEntry>() {
    public Class<PendingPaymentEntry> getCreatedClass() {
      return PendingPaymentEntry.class;
    }

    @Override
    public PendingPaymentEntry create(org.json.JSONObject jsonObject) {
      return new PendingPaymentEntry(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean AMOUNT_IS_REQUIRED = false;
    public static final boolean PAYMENTID_IS_REQUIRED = false;
    public static final long PAYMENTID_MAX_LEN = 13;
    public static final boolean EXTERNALPAYMENTID_IS_REQUIRED = false;
    public static final boolean EXTERNALID_IS_REQUIRED = false;
    public static final boolean TIPAMOUNT_IS_REQUIRED = false;
  }

}
