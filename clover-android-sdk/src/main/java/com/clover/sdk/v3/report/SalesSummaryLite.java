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

package com.clover.sdk.v3.report;

import com.clover.sdk.GenericClient;
import com.clover.sdk.GenericParcelable;

/**
 * This is an auto-generated Clover data object.
 * <p>
 * <h3>Fields</h3>
 * <ul>
 * <li>{@link #getNumTransactions numTransactions}</li>
 * <li>{@link #getAmountCollected amountCollected}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class SalesSummaryLite extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  /**
   * Total number of transactions = numPayments + numCredits + numRefunds
   */
  public java.lang.Long getNumTransactions() {
    return genClient.cacheGet(CacheKey.numTransactions);
  }

  /**
   * Total amount of money collected minus total amount refunded or manually refunded. Includes tax, tip, service charge, non-revenue items, paid gift card activations and loads and discounts
   */
  public java.lang.Long getAmountCollected() {
    return genClient.cacheGet(CacheKey.amountCollected);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    numTransactions
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    amountCollected
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

  private final GenericClient<SalesSummaryLite> genClient;

  /**
   * Constructs a new empty instance.
   */
  public SalesSummaryLite() {
    genClient = new GenericClient<SalesSummaryLite>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected SalesSummaryLite(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public SalesSummaryLite(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public SalesSummaryLite(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public SalesSummaryLite(SalesSummaryLite src) {
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

  /** Checks whether the 'numTransactions' field is set and is not null */
  public boolean isNotNullNumTransactions() {
    return genClient.cacheValueIsNotNull(CacheKey.numTransactions);
  }

  /** Checks whether the 'amountCollected' field is set and is not null */
  public boolean isNotNullAmountCollected() {
    return genClient.cacheValueIsNotNull(CacheKey.amountCollected);
  }



  /** Checks whether the 'numTransactions' field has been set, however the value could be null */
  public boolean hasNumTransactions() {
    return genClient.cacheHasKey(CacheKey.numTransactions);
  }

  /** Checks whether the 'amountCollected' field has been set, however the value could be null */
  public boolean hasAmountCollected() {
    return genClient.cacheHasKey(CacheKey.amountCollected);
  }


  /**
   * Sets the field 'numTransactions'.
   */
  public SalesSummaryLite setNumTransactions(java.lang.Long numTransactions) {
    return genClient.setOther(numTransactions, CacheKey.numTransactions);
  }

  /**
   * Sets the field 'amountCollected'.
   */
  public SalesSummaryLite setAmountCollected(java.lang.Long amountCollected) {
    return genClient.setOther(amountCollected, CacheKey.amountCollected);
  }


  /** Clears the 'numTransactions' field, the 'has' method for this field will now return false */
  public void clearNumTransactions() {
    genClient.clear(CacheKey.numTransactions);
  }
  /** Clears the 'amountCollected' field, the 'has' method for this field will now return false */
  public void clearAmountCollected() {
    genClient.clear(CacheKey.amountCollected);
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
  public SalesSummaryLite copyChanges() {
    SalesSummaryLite copy = new SalesSummaryLite();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(SalesSummaryLite src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new SalesSummaryLite(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<SalesSummaryLite> CREATOR = new android.os.Parcelable.Creator<SalesSummaryLite>() {
    @Override
    public SalesSummaryLite createFromParcel(android.os.Parcel in) {
      SalesSummaryLite instance = new SalesSummaryLite(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public SalesSummaryLite[] newArray(int size) {
      return new SalesSummaryLite[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<SalesSummaryLite> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<SalesSummaryLite>() {
    public Class<SalesSummaryLite> getCreatedClass() {
      return SalesSummaryLite.class;
    }

    @Override
    public SalesSummaryLite create(org.json.JSONObject jsonObject) {
      return new SalesSummaryLite(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean NUMTRANSACTIONS_IS_REQUIRED = false;
    public static final boolean AMOUNTCOLLECTED_IS_REQUIRED = false;
  }

}
