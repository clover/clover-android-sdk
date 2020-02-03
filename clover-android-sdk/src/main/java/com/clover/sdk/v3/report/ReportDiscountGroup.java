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
 * <li>{@link #getName name}</li>
 * <li>{@link #getTotal total}</li>
 * <li>{@link #getDiscountRows discountRows}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class ReportDiscountGroup extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  /**
   * Name of discount
   */
  public java.lang.String getName() {
    return genClient.cacheGet(CacheKey.name);
  }

  /**
   * Total value of all line item and order discounts
   */
  public com.clover.sdk.v3.report.ReportDiscountRow getTotal() {
    return genClient.cacheGet(CacheKey.total);
  }

  /**
   * Summary of orders containing discount group's discount
   */
  public java.util.List<com.clover.sdk.v3.report.ReportDiscountRow> getDiscountRows() {
    return genClient.cacheGet(CacheKey.discountRows);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    name
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    total
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.report.ReportDiscountRow.JSON_CREATOR)),
    discountRows
        (com.clover.sdk.extractors.RecordListExtractionStrategy.instance(com.clover.sdk.v3.report.ReportDiscountRow.JSON_CREATOR)),
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

  private final GenericClient<ReportDiscountGroup> genClient;

  /**
   * Constructs a new empty instance.
   */
  public ReportDiscountGroup() {
    genClient = new GenericClient<ReportDiscountGroup>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected ReportDiscountGroup(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public ReportDiscountGroup(String json) throws IllegalArgumentException {
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
  public ReportDiscountGroup(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public ReportDiscountGroup(ReportDiscountGroup src) {
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

  /** Checks whether the 'name' field is set and is not null */
  public boolean isNotNullName() {
    return genClient.cacheValueIsNotNull(CacheKey.name);
  }

  /** Checks whether the 'total' field is set and is not null */
  public boolean isNotNullTotal() {
    return genClient.cacheValueIsNotNull(CacheKey.total);
  }

  /** Checks whether the 'discountRows' field is set and is not null */
  public boolean isNotNullDiscountRows() {
    return genClient.cacheValueIsNotNull(CacheKey.discountRows);
  }

  /** Checks whether the 'discountRows' field is set and is not null and is not empty */
  public boolean isNotEmptyDiscountRows() { return isNotNullDiscountRows() && !getDiscountRows().isEmpty(); }



  /** Checks whether the 'name' field has been set, however the value could be null */
  public boolean hasName() {
    return genClient.cacheHasKey(CacheKey.name);
  }

  /** Checks whether the 'total' field has been set, however the value could be null */
  public boolean hasTotal() {
    return genClient.cacheHasKey(CacheKey.total);
  }

  /** Checks whether the 'discountRows' field has been set, however the value could be null */
  public boolean hasDiscountRows() {
    return genClient.cacheHasKey(CacheKey.discountRows);
  }


  /**
   * Sets the field 'name'.
   */
  public ReportDiscountGroup setName(java.lang.String name) {
    return genClient.setOther(name, CacheKey.name);
  }

  /**
   * Sets the field 'total'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public ReportDiscountGroup setTotal(com.clover.sdk.v3.report.ReportDiscountRow total) {
    return genClient.setRecord(total, CacheKey.total);
  }

  /**
   * Sets the field 'discountRows'.
   *
   * Nulls in the given List are skipped. List parameter is copied, so it will not reflect any changes, but objects inside it will.
   */
  public ReportDiscountGroup setDiscountRows(java.util.List<com.clover.sdk.v3.report.ReportDiscountRow> discountRows) {
    return genClient.setArrayRecord(discountRows, CacheKey.discountRows);
  }


  /** Clears the 'name' field, the 'has' method for this field will now return false */
  public void clearName() {
    genClient.clear(CacheKey.name);
  }
  /** Clears the 'total' field, the 'has' method for this field will now return false */
  public void clearTotal() {
    genClient.clear(CacheKey.total);
  }
  /** Clears the 'discountRows' field, the 'has' method for this field will now return false */
  public void clearDiscountRows() {
    genClient.clear(CacheKey.discountRows);
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
  public ReportDiscountGroup copyChanges() {
    ReportDiscountGroup copy = new ReportDiscountGroup();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(ReportDiscountGroup src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new ReportDiscountGroup(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<ReportDiscountGroup> CREATOR = new android.os.Parcelable.Creator<ReportDiscountGroup>() {
    @Override
    public ReportDiscountGroup createFromParcel(android.os.Parcel in) {
      ReportDiscountGroup instance = new ReportDiscountGroup(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public ReportDiscountGroup[] newArray(int size) {
      return new ReportDiscountGroup[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<ReportDiscountGroup> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<ReportDiscountGroup>() {
    @Override
    public ReportDiscountGroup create(org.json.JSONObject jsonObject) {
      return new ReportDiscountGroup(jsonObject);
    }
  };

  public interface Constraints {

    public static final boolean NAME_IS_REQUIRED = false;
    public static final boolean TOTAL_IS_REQUIRED = false;
    public static final boolean DISCOUNTROWS_IS_REQUIRED = false;

  }

}
