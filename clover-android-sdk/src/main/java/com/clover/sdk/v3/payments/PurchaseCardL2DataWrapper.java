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
 * <li>{@link #getPurchaseCardL2 purchaseCardL2}</li>
 * <li>{@link #getPurchaseCardBillingInfo purchaseCardBillingInfo}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class PurchaseCardL2DataWrapper extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  /**
   * Purchase card object that holds l2 data corresponding to l2 fields in gt request
   */
  public PurchaseCardL2 getPurchaseCardL2() {
    return genClient.cacheGet(CacheKey.purchaseCardL2);
  }

  /**
   * Holds billing address and billing zip
   */
  public PurchaseCardBillingInfo getPurchaseCardBillingInfo() {
    return genClient.cacheGet(CacheKey.purchaseCardBillingInfo);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    purchaseCardL2
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(PurchaseCardL2.JSON_CREATOR)),
    purchaseCardBillingInfo
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(PurchaseCardBillingInfo.JSON_CREATOR)),
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

  private final GenericClient<PurchaseCardL2DataWrapper> genClient;

  /**
   * Constructs a new empty instance.
   */
  public PurchaseCardL2DataWrapper() {
    genClient = new GenericClient<PurchaseCardL2DataWrapper>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected PurchaseCardL2DataWrapper(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public PurchaseCardL2DataWrapper(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public PurchaseCardL2DataWrapper(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public PurchaseCardL2DataWrapper(PurchaseCardL2DataWrapper src) {
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

  /** Checks whether the 'purchaseCardL2' field is set and is not null */
  public boolean isNotNullPurchaseCardL2() {
    return genClient.cacheValueIsNotNull(CacheKey.purchaseCardL2);
  }

  /** Checks whether the 'purchaseCardBillingInfo' field is set and is not null */
  public boolean isNotNullPurchaseCardBillingInfo() {
    return genClient.cacheValueIsNotNull(CacheKey.purchaseCardBillingInfo);
  }



  /** Checks whether the 'purchaseCardL2' field has been set, however the value could be null */
  public boolean hasPurchaseCardL2() {
    return genClient.cacheHasKey(CacheKey.purchaseCardL2);
  }

  /** Checks whether the 'purchaseCardBillingInfo' field has been set, however the value could be null */
  public boolean hasPurchaseCardBillingInfo() {
    return genClient.cacheHasKey(CacheKey.purchaseCardBillingInfo);
  }


  /**
   * Sets the field 'purchaseCardL2'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public PurchaseCardL2DataWrapper setPurchaseCardL2(PurchaseCardL2 purchaseCardL2) {
    return genClient.setRecord(purchaseCardL2, CacheKey.purchaseCardL2);
  }

  /**
   * Sets the field 'purchaseCardBillingInfo'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public PurchaseCardL2DataWrapper setPurchaseCardBillingInfo(PurchaseCardBillingInfo purchaseCardBillingInfo) {
    return genClient.setRecord(purchaseCardBillingInfo, CacheKey.purchaseCardBillingInfo);
  }


  /** Clears the 'purchaseCardL2' field, the 'has' method for this field will now return false */
  public void clearPurchaseCardL2() {
    genClient.clear(CacheKey.purchaseCardL2);
  }
  /** Clears the 'purchaseCardBillingInfo' field, the 'has' method for this field will now return false */
  public void clearPurchaseCardBillingInfo() {
    genClient.clear(CacheKey.purchaseCardBillingInfo);
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
  public PurchaseCardL2DataWrapper copyChanges() {
    PurchaseCardL2DataWrapper copy = new PurchaseCardL2DataWrapper();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(PurchaseCardL2DataWrapper src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new PurchaseCardL2DataWrapper(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<PurchaseCardL2DataWrapper> CREATOR = new android.os.Parcelable.Creator<PurchaseCardL2DataWrapper>() {
    @Override
    public PurchaseCardL2DataWrapper createFromParcel(android.os.Parcel in) {
      PurchaseCardL2DataWrapper instance = new PurchaseCardL2DataWrapper(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public PurchaseCardL2DataWrapper[] newArray(int size) {
      return new PurchaseCardL2DataWrapper[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<PurchaseCardL2DataWrapper> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<PurchaseCardL2DataWrapper>() {
    public Class<PurchaseCardL2DataWrapper> getCreatedClass() {
      return PurchaseCardL2DataWrapper.class;
    }

    @Override
    public PurchaseCardL2DataWrapper create(org.json.JSONObject jsonObject) {
      return new PurchaseCardL2DataWrapper(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean PURCHASECARDL2_IS_REQUIRED = false;
    public static final boolean PURCHASECARDBILLINGINFO_IS_REQUIRED = false;
  }

}