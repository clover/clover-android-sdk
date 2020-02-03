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
 * <li>{@link #getSuccess success}</li>
 * <li>{@link #getEndpointName endpointName}</li>
 * <li>{@link #getBatchNumber batchNumber}</li>
 * <li>{@link #getTerminalId terminalId}</li>
 * <li>{@link #getCount count}</li>
 * <li>{@link #getTotal total}</li>
 * <li>{@link #getCardTotals cardTotals}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class EndpointTotalStats extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  /**
   * Indicates if the closeout for terminalId/endpointName/batchNumber was successfully performed or not
   */
  public Boolean getSuccess() {
    return genClient.cacheGet(CacheKey.success);
  }

  /**
   * Name for the given endpoint
   */
  public String getEndpointName() {
    return genClient.cacheGet(CacheKey.endpointName);
  }

  /**
   * The batchNumber for this endpoint
   */
  public String getBatchNumber() {
    return genClient.cacheGet(CacheKey.batchNumber);
  }

  /**
   * The terminal id for this endpoint & batch details
   */
  public String getTerminalId() {
    return genClient.cacheGet(CacheKey.terminalId);
  }

  /**
   * Total count of endpoint transactions
   */
  public Long getCount() {
    return genClient.cacheGet(CacheKey.count);
  }

  /**
   * Total amount for transactions
   */
  public Long getTotal() {
    return genClient.cacheGet(CacheKey.total);
  }

  public java.util.List<BatchCardTotal> getCardTotals() {
    return genClient.cacheGet(CacheKey.cardTotals);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    success
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(Boolean.class)),
    endpointName
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(String.class)),
    batchNumber
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(String.class)),
    terminalId
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(String.class)),
    count
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(Long.class)),
    total
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(Long.class)),
    cardTotals
        (com.clover.sdk.extractors.RecordListExtractionStrategy.instance(BatchCardTotal.JSON_CREATOR)),
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

  private final GenericClient<EndpointTotalStats> genClient;

  /**
   * Constructs a new empty instance.
   */
  public EndpointTotalStats() {
    genClient = new GenericClient<EndpointTotalStats>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected EndpointTotalStats(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public EndpointTotalStats(String json) throws IllegalArgumentException {
    this();
    try {
      genClient.setJsonObject(new org.json.JSONObject(json));
    } catch (org.json.JSONException e) {
      throw new IllegalArgumentException("invalid json", e);
    }
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public EndpointTotalStats(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public EndpointTotalStats(EndpointTotalStats src) {
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
  }

  /** Checks whether the 'success' field is set and is not null */
  public boolean isNotNullSuccess() {
    return genClient.cacheValueIsNotNull(CacheKey.success);
  }

  /** Checks whether the 'endpointName' field is set and is not null */
  public boolean isNotNullEndpointName() {
    return genClient.cacheValueIsNotNull(CacheKey.endpointName);
  }

  /** Checks whether the 'batchNumber' field is set and is not null */
  public boolean isNotNullBatchNumber() {
    return genClient.cacheValueIsNotNull(CacheKey.batchNumber);
  }

  /** Checks whether the 'terminalId' field is set and is not null */
  public boolean isNotNullTerminalId() {
    return genClient.cacheValueIsNotNull(CacheKey.terminalId);
  }

  /** Checks whether the 'count' field is set and is not null */
  public boolean isNotNullCount() {
    return genClient.cacheValueIsNotNull(CacheKey.count);
  }

  /** Checks whether the 'total' field is set and is not null */
  public boolean isNotNullTotal() {
    return genClient.cacheValueIsNotNull(CacheKey.total);
  }

  /** Checks whether the 'cardTotals' field is set and is not null */
  public boolean isNotNullCardTotals() {
    return genClient.cacheValueIsNotNull(CacheKey.cardTotals);
  }

  /** Checks whether the 'cardTotals' field is set and is not null and is not empty */
  public boolean isNotEmptyCardTotals() { return isNotNullCardTotals() && !getCardTotals().isEmpty(); }



  /** Checks whether the 'success' field has been set, however the value could be null */
  public boolean hasSuccess() {
    return genClient.cacheHasKey(CacheKey.success);
  }

  /** Checks whether the 'endpointName' field has been set, however the value could be null */
  public boolean hasEndpointName() {
    return genClient.cacheHasKey(CacheKey.endpointName);
  }

  /** Checks whether the 'batchNumber' field has been set, however the value could be null */
  public boolean hasBatchNumber() {
    return genClient.cacheHasKey(CacheKey.batchNumber);
  }

  /** Checks whether the 'terminalId' field has been set, however the value could be null */
  public boolean hasTerminalId() {
    return genClient.cacheHasKey(CacheKey.terminalId);
  }

  /** Checks whether the 'count' field has been set, however the value could be null */
  public boolean hasCount() {
    return genClient.cacheHasKey(CacheKey.count);
  }

  /** Checks whether the 'total' field has been set, however the value could be null */
  public boolean hasTotal() {
    return genClient.cacheHasKey(CacheKey.total);
  }

  /** Checks whether the 'cardTotals' field has been set, however the value could be null */
  public boolean hasCardTotals() {
    return genClient.cacheHasKey(CacheKey.cardTotals);
  }


  /**
   * Sets the field 'success'.
   */
  public EndpointTotalStats setSuccess(Boolean success) {
    return genClient.setOther(success, CacheKey.success);
  }

  /**
   * Sets the field 'endpointName'.
   */
  public EndpointTotalStats setEndpointName(String endpointName) {
    return genClient.setOther(endpointName, CacheKey.endpointName);
  }

  /**
   * Sets the field 'batchNumber'.
   */
  public EndpointTotalStats setBatchNumber(String batchNumber) {
    return genClient.setOther(batchNumber, CacheKey.batchNumber);
  }

  /**
   * Sets the field 'terminalId'.
   */
  public EndpointTotalStats setTerminalId(String terminalId) {
    return genClient.setOther(terminalId, CacheKey.terminalId);
  }

  /**
   * Sets the field 'count'.
   */
  public EndpointTotalStats setCount(Long count) {
    return genClient.setOther(count, CacheKey.count);
  }

  /**
   * Sets the field 'total'.
   */
  public EndpointTotalStats setTotal(Long total) {
    return genClient.setOther(total, CacheKey.total);
  }

  /**
   * Sets the field 'cardTotals'.
   *
   * Nulls in the given List are skipped. List parameter is copied, so it will not reflect any changes, but objects inside it will.
   */
  public EndpointTotalStats setCardTotals(java.util.List<BatchCardTotal> cardTotals) {
    return genClient.setArrayRecord(cardTotals, CacheKey.cardTotals);
  }


  /** Clears the 'success' field, the 'has' method for this field will now return false */
  public void clearSuccess() {
    genClient.clear(CacheKey.success);
  }
  /** Clears the 'endpointName' field, the 'has' method for this field will now return false */
  public void clearEndpointName() {
    genClient.clear(CacheKey.endpointName);
  }
  /** Clears the 'batchNumber' field, the 'has' method for this field will now return false */
  public void clearBatchNumber() {
    genClient.clear(CacheKey.batchNumber);
  }
  /** Clears the 'terminalId' field, the 'has' method for this field will now return false */
  public void clearTerminalId() {
    genClient.clear(CacheKey.terminalId);
  }
  /** Clears the 'count' field, the 'has' method for this field will now return false */
  public void clearCount() {
    genClient.clear(CacheKey.count);
  }
  /** Clears the 'total' field, the 'has' method for this field will now return false */
  public void clearTotal() {
    genClient.clear(CacheKey.total);
  }
  /** Clears the 'cardTotals' field, the 'has' method for this field will now return false */
  public void clearCardTotals() {
    genClient.clear(CacheKey.cardTotals);
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
  public EndpointTotalStats copyChanges() {
    EndpointTotalStats copy = new EndpointTotalStats();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(EndpointTotalStats src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new EndpointTotalStats(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<EndpointTotalStats> CREATOR = new android.os.Parcelable.Creator<EndpointTotalStats>() {
    @Override
    public EndpointTotalStats createFromParcel(android.os.Parcel in) {
      EndpointTotalStats instance = new EndpointTotalStats(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public EndpointTotalStats[] newArray(int size) {
      return new EndpointTotalStats[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<EndpointTotalStats> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<EndpointTotalStats>() {
    @Override
    public EndpointTotalStats create(org.json.JSONObject jsonObject) {
      return new EndpointTotalStats(jsonObject);
    }
  };

  public interface Constraints {

    public static final boolean SUCCESS_IS_REQUIRED = false;
    public static final boolean ENDPOINTNAME_IS_REQUIRED = false;
    public static final boolean BATCHNUMBER_IS_REQUIRED = false;
    public static final boolean TERMINALID_IS_REQUIRED = false;
    public static final boolean COUNT_IS_REQUIRED = false;
    public static final boolean TOTAL_IS_REQUIRED = false;
    public static final boolean CARDTOTALS_IS_REQUIRED = false;

  }

}
