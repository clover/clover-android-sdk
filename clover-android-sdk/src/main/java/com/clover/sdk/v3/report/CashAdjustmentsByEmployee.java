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
 * Total and details on cash added or removed from cash drawer for an employee
 * <p>
 * <h3>Fields</h3>
 * <ul>
 * <li>{@link #getRows rows}</li>
 * <li>{@link #getEmployee employee}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class CashAdjustmentsByEmployee extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  /**
   * Details on cash added or removed from cash drawer for this employee
   */
  public java.util.List<com.clover.sdk.v3.cash.CashEvent> getRows() {
    return genClient.cacheGet(CacheKey.rows);
  }

  /**
   * The employee who performed the event
   */
  public com.clover.sdk.v3.employees.Employee getEmployee() {
    return genClient.cacheGet(CacheKey.employee);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    rows
        (com.clover.sdk.extractors.RecordListExtractionStrategy.instance(com.clover.sdk.v3.cash.CashEvent.JSON_CREATOR)),
    employee
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.employees.Employee.JSON_CREATOR)),
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

  private final GenericClient<CashAdjustmentsByEmployee> genClient;

  /**
   * Constructs a new empty instance.
   */
  public CashAdjustmentsByEmployee() {
    genClient = new GenericClient<CashAdjustmentsByEmployee>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected CashAdjustmentsByEmployee(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public CashAdjustmentsByEmployee(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public CashAdjustmentsByEmployee(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public CashAdjustmentsByEmployee(CashAdjustmentsByEmployee src) {
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

  /** Checks whether the 'employee' field is set and is not null */
  public boolean isNotNullEmployee() {
    return genClient.cacheValueIsNotNull(CacheKey.employee);
  }



  /** Checks whether the 'rows' field has been set, however the value could be null */
  public boolean hasRows() {
    return genClient.cacheHasKey(CacheKey.rows);
  }

  /** Checks whether the 'employee' field has been set, however the value could be null */
  public boolean hasEmployee() {
    return genClient.cacheHasKey(CacheKey.employee);
  }


  /**
   * Sets the field 'rows'.
   *
   * Nulls in the given List are skipped. List parameter is copied, so it will not reflect any changes, but objects inside it will.
   */
  public CashAdjustmentsByEmployee setRows(java.util.List<com.clover.sdk.v3.cash.CashEvent> rows) {
    return genClient.setArrayRecord(rows, CacheKey.rows);
  }

  /**
   * Sets the field 'employee'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public CashAdjustmentsByEmployee setEmployee(com.clover.sdk.v3.employees.Employee employee) {
    return genClient.setRecord(employee, CacheKey.employee);
  }


  /** Clears the 'rows' field, the 'has' method for this field will now return false */
  public void clearRows() {
    genClient.clear(CacheKey.rows);
  }
  /** Clears the 'employee' field, the 'has' method for this field will now return false */
  public void clearEmployee() {
    genClient.clear(CacheKey.employee);
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
  public CashAdjustmentsByEmployee copyChanges() {
    CashAdjustmentsByEmployee copy = new CashAdjustmentsByEmployee();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(CashAdjustmentsByEmployee src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new CashAdjustmentsByEmployee(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<CashAdjustmentsByEmployee> CREATOR = new android.os.Parcelable.Creator<CashAdjustmentsByEmployee>() {
    @Override
    public CashAdjustmentsByEmployee createFromParcel(android.os.Parcel in) {
      CashAdjustmentsByEmployee instance = new CashAdjustmentsByEmployee(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public CashAdjustmentsByEmployee[] newArray(int size) {
      return new CashAdjustmentsByEmployee[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<CashAdjustmentsByEmployee> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<CashAdjustmentsByEmployee>() {
    public Class<CashAdjustmentsByEmployee> getCreatedClass() {
      return CashAdjustmentsByEmployee.class;
    }

    @Override
    public CashAdjustmentsByEmployee create(org.json.JSONObject jsonObject) {
      return new CashAdjustmentsByEmployee(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean ROWS_IS_REQUIRED = false;
    public static final boolean EMPLOYEE_IS_REQUIRED = false;
  }

}
