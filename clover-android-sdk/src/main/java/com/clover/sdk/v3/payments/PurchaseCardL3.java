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
 * <li>{@link #getServiceCode serviceCode}</li>
 * <li>{@link #getMagneticStripeInd magneticStripeInd}</li>
 * <li>{@link #getLevel3LineItems level3LineItems}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class PurchaseCardL3 extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  /**
   * Service code extracted from the track data in Field 35. For all card types, Service code is mandatory for all merchants who directly settle through First Data. 
   */
  public String getServiceCode() {
    return genClient.cacheGet(CacheKey.serviceCode);
  }

  /**
   * If the card was swiped via magnetic strip reader for payment, set this indicator true. Also serviceCode will be mandatory if this is true
   */
  public Boolean getMagneticStripeInd() {
    return genClient.cacheGet(CacheKey.magneticStripeInd);
  }

  /**
   * List of line items constituting the order using Level3 Purchase card
   */
  public java.util.List<Level3LineItem> getLevel3LineItems() {
    return genClient.cacheGet(CacheKey.level3LineItems);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    serviceCode
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(String.class)),
    magneticStripeInd
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(Boolean.class)),
    level3LineItems
        (com.clover.sdk.extractors.RecordListExtractionStrategy.instance(Level3LineItem.JSON_CREATOR)),
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

  private final GenericClient<PurchaseCardL3> genClient;

  /**
   * Constructs a new empty instance.
   */
  public PurchaseCardL3() {
    genClient = new GenericClient<PurchaseCardL3>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected PurchaseCardL3(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public PurchaseCardL3(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public PurchaseCardL3(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public PurchaseCardL3(PurchaseCardL3 src) {
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
    genClient.validateLength(CacheKey.serviceCode, getServiceCode(), 3);
  }

  /** Checks whether the 'serviceCode' field is set and is not null */
  public boolean isNotNullServiceCode() {
    return genClient.cacheValueIsNotNull(CacheKey.serviceCode);
  }

  /** Checks whether the 'magneticStripeInd' field is set and is not null */
  public boolean isNotNullMagneticStripeInd() {
    return genClient.cacheValueIsNotNull(CacheKey.magneticStripeInd);
  }

  /** Checks whether the 'level3LineItems' field is set and is not null */
  public boolean isNotNullLevel3LineItems() {
    return genClient.cacheValueIsNotNull(CacheKey.level3LineItems);
  }

  /** Checks whether the 'level3LineItems' field is set and is not null and is not empty */
  public boolean isNotEmptyLevel3LineItems() { return isNotNullLevel3LineItems() && !getLevel3LineItems().isEmpty(); }



  /** Checks whether the 'serviceCode' field has been set, however the value could be null */
  public boolean hasServiceCode() {
    return genClient.cacheHasKey(CacheKey.serviceCode);
  }

  /** Checks whether the 'magneticStripeInd' field has been set, however the value could be null */
  public boolean hasMagneticStripeInd() {
    return genClient.cacheHasKey(CacheKey.magneticStripeInd);
  }

  /** Checks whether the 'level3LineItems' field has been set, however the value could be null */
  public boolean hasLevel3LineItems() {
    return genClient.cacheHasKey(CacheKey.level3LineItems);
  }


  /**
   * Sets the field 'serviceCode'.
   */
  public PurchaseCardL3 setServiceCode(String serviceCode) {
    return genClient.setOther(serviceCode, CacheKey.serviceCode);
  }

  /**
   * Sets the field 'magneticStripeInd'.
   */
  public PurchaseCardL3 setMagneticStripeInd(Boolean magneticStripeInd) {
    return genClient.setOther(magneticStripeInd, CacheKey.magneticStripeInd);
  }

  /**
   * Sets the field 'level3LineItems'.
   *
   * Nulls in the given List are skipped. List parameter is copied, so it will not reflect any changes, but objects inside it will.
   */
  public PurchaseCardL3 setLevel3LineItems(java.util.List<Level3LineItem> level3LineItems) {
    return genClient.setArrayRecord(level3LineItems, CacheKey.level3LineItems);
  }


  /** Clears the 'serviceCode' field, the 'has' method for this field will now return false */
  public void clearServiceCode() {
    genClient.clear(CacheKey.serviceCode);
  }
  /** Clears the 'magneticStripeInd' field, the 'has' method for this field will now return false */
  public void clearMagneticStripeInd() {
    genClient.clear(CacheKey.magneticStripeInd);
  }
  /** Clears the 'level3LineItems' field, the 'has' method for this field will now return false */
  public void clearLevel3LineItems() {
    genClient.clear(CacheKey.level3LineItems);
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
  public PurchaseCardL3 copyChanges() {
    PurchaseCardL3 copy = new PurchaseCardL3();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(PurchaseCardL3 src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new PurchaseCardL3(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<PurchaseCardL3> CREATOR = new android.os.Parcelable.Creator<PurchaseCardL3>() {
    @Override
    public PurchaseCardL3 createFromParcel(android.os.Parcel in) {
      PurchaseCardL3 instance = new PurchaseCardL3(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public PurchaseCardL3[] newArray(int size) {
      return new PurchaseCardL3[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<PurchaseCardL3> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<PurchaseCardL3>() {
    public Class<PurchaseCardL3> getCreatedClass() {
      return PurchaseCardL3.class;
    }

    @Override
    public PurchaseCardL3 create(org.json.JSONObject jsonObject) {
      return new PurchaseCardL3(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean SERVICECODE_IS_REQUIRED = false;
    public static final long SERVICECODE_MAX_LEN = 3;
    public static final boolean MAGNETICSTRIPEIND_IS_REQUIRED = false;
    public static final boolean LEVEL3LINEITEMS_IS_REQUIRED = false;
  }

}
