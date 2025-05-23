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
 * <li>{@link #getRows rows}</li>
 * <li>{@link #getLocalCurrencyTotal localCurrencyTotal}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class DynamicCurrencyConversionSection extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  /**
   * Summary for each foreign currency that has an associated payment in the report period
   */
  public java.util.List<com.clover.sdk.v3.report.DynamicCurrencyConversionSummaryRow> getRows() {
    return genClient.cacheGet(CacheKey.rows);
  }

  /**
   * Total of dynamic currency conversion expressed in local currency
   */
  public com.clover.sdk.v3.report.ReportPaymentsV2Row getLocalCurrencyTotal() {
    return genClient.cacheGet(CacheKey.localCurrencyTotal);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    rows
        (com.clover.sdk.extractors.RecordListExtractionStrategy.instance(com.clover.sdk.v3.report.DynamicCurrencyConversionSummaryRow.JSON_CREATOR)),
    localCurrencyTotal
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.report.ReportPaymentsV2Row.JSON_CREATOR)),
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

  private final GenericClient<DynamicCurrencyConversionSection> genClient;

  /**
   * Constructs a new empty instance.
   */
  public DynamicCurrencyConversionSection() {
    genClient = new GenericClient<DynamicCurrencyConversionSection>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected DynamicCurrencyConversionSection(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public DynamicCurrencyConversionSection(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public DynamicCurrencyConversionSection(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public DynamicCurrencyConversionSection(DynamicCurrencyConversionSection src) {
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

  /** Checks whether the 'rows' field is set and is not null */
  public boolean isNotNullRows() {
    return genClient.cacheValueIsNotNull(CacheKey.rows);
  }

  /** Checks whether the 'rows' field is set and is not null and is not empty */
  public boolean isNotEmptyRows() { return isNotNullRows() && !getRows().isEmpty(); }

  /** Checks whether the 'localCurrencyTotal' field is set and is not null */
  public boolean isNotNullLocalCurrencyTotal() {
    return genClient.cacheValueIsNotNull(CacheKey.localCurrencyTotal);
  }



  /** Checks whether the 'rows' field has been set, however the value could be null */
  public boolean hasRows() {
    return genClient.cacheHasKey(CacheKey.rows);
  }

  /** Checks whether the 'localCurrencyTotal' field has been set, however the value could be null */
  public boolean hasLocalCurrencyTotal() {
    return genClient.cacheHasKey(CacheKey.localCurrencyTotal);
  }


  /**
   * Sets the field 'rows'.
   *
   * Nulls in the given List are skipped. List parameter is copied, so it will not reflect any changes, but objects inside it will.
   */
  public DynamicCurrencyConversionSection setRows(java.util.List<com.clover.sdk.v3.report.DynamicCurrencyConversionSummaryRow> rows) {
    return genClient.setArrayRecord(rows, CacheKey.rows);
  }

  /**
   * Sets the field 'localCurrencyTotal'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public DynamicCurrencyConversionSection setLocalCurrencyTotal(com.clover.sdk.v3.report.ReportPaymentsV2Row localCurrencyTotal) {
    return genClient.setRecord(localCurrencyTotal, CacheKey.localCurrencyTotal);
  }


  /** Clears the 'rows' field, the 'has' method for this field will now return false */
  public void clearRows() {
    genClient.clear(CacheKey.rows);
  }
  /** Clears the 'localCurrencyTotal' field, the 'has' method for this field will now return false */
  public void clearLocalCurrencyTotal() {
    genClient.clear(CacheKey.localCurrencyTotal);
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
  public DynamicCurrencyConversionSection copyChanges() {
    DynamicCurrencyConversionSection copy = new DynamicCurrencyConversionSection();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(DynamicCurrencyConversionSection src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new DynamicCurrencyConversionSection(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<DynamicCurrencyConversionSection> CREATOR = new android.os.Parcelable.Creator<DynamicCurrencyConversionSection>() {
    @Override
    public DynamicCurrencyConversionSection createFromParcel(android.os.Parcel in) {
      DynamicCurrencyConversionSection instance = new DynamicCurrencyConversionSection(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public DynamicCurrencyConversionSection[] newArray(int size) {
      return new DynamicCurrencyConversionSection[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<DynamicCurrencyConversionSection> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<DynamicCurrencyConversionSection>() {
    public Class<DynamicCurrencyConversionSection> getCreatedClass() {
      return DynamicCurrencyConversionSection.class;
    }

    @Override
    public DynamicCurrencyConversionSection create(org.json.JSONObject jsonObject) {
      return new DynamicCurrencyConversionSection(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean ROWS_IS_REQUIRED = false;
    public static final boolean LOCALCURRENCYTOTAL_IS_REQUIRED = false;
  }

}
