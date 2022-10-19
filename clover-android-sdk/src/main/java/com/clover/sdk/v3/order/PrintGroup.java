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

package com.clover.sdk.v3.order;

import com.clover.sdk.GenericClient;
import com.clover.sdk.GenericParcelable;

/**
 * This is an auto-generated Clover data object.
 * <p>
 * Groups together a set of line items to display under a heading on receipts.
 * <p>
 * <h3>Fields</h3>
 * <ul>
 * <li>{@link #getId id}</li>
 * <li>{@link #getOrderRef orderRef}</li>
 * <li>{@link #getName name}</li>
 * <li>{@link #getSortOrder sortOrder}</li>
 * <li>{@link #getFired fired}</li>
 * <li>{@link #getPrintTime printTime}</li>
 * </ul>
 * <p>
 * @see com.clover.sdk.v3.order.IOrderService
 */
@SuppressWarnings("all")
public class PrintGroup extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  /**
   * Unique identifier.
   */
  public String getId() {
    return genClient.cacheGet(CacheKey.id);
  }

  /**
   * Reference to the order associated with this PrintGroup.
   */
  public com.clover.sdk.v3.base.Reference getOrderRef() {
    return genClient.cacheGet(CacheKey.orderRef);
  }

  /**
   * The print group heading that will be displayed on receipts.
   */
  public String getName() {
    return genClient.cacheGet(CacheKey.name);
  }

  /**
   * The order in which this print group is displayed relative to other print groups on the same receipt. Print groups with identical sort orders will be ordered by name.
   */
  public Integer getSortOrder() {
    return genClient.cacheGet(CacheKey.sortOrder);
  }

  /**
   * True if this print group has been sent to an order printer.
   */
  public Boolean getFired() {
    return genClient.cacheGet(CacheKey.fired);
  }

  /**
   * Timestamp of when this print group should print or had printed relative to the merchant's Clover device system time.
   */
  public Long getPrintTime() {
    return genClient.cacheGet(CacheKey.printTime);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    id
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(String.class)),
    orderRef
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.base.Reference.JSON_CREATOR)),
    name
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(String.class)),
    sortOrder
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(Integer.class)),
    fired
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(Boolean.class)),
    printTime
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(Long.class)),
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

  private final GenericClient<PrintGroup> genClient;

  /**
   * Constructs a new empty instance.
   */
  public PrintGroup() {
    genClient = new GenericClient<PrintGroup>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected PrintGroup(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public PrintGroup(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public PrintGroup(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public PrintGroup(PrintGroup src) {
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

    genClient.validateLength(CacheKey.name, getName(), 127);

    genClient.validateMin(CacheKey.sortOrder, getSortOrder(), 0L);
    genClient.validateReferences(CacheKey.orderRef);
  }

  /** Checks whether the 'id' field is set and is not null */
  public boolean isNotNullId() {
    return genClient.cacheValueIsNotNull(CacheKey.id);
  }

  /** Checks whether the 'orderRef' field is set and is not null */
  public boolean isNotNullOrderRef() {
    return genClient.cacheValueIsNotNull(CacheKey.orderRef);
  }

  /** Checks whether the 'name' field is set and is not null */
  public boolean isNotNullName() {
    return genClient.cacheValueIsNotNull(CacheKey.name);
  }

  /** Checks whether the 'sortOrder' field is set and is not null */
  public boolean isNotNullSortOrder() {
    return genClient.cacheValueIsNotNull(CacheKey.sortOrder);
  }

  /** Checks whether the 'fired' field is set and is not null */
  public boolean isNotNullFired() {
    return genClient.cacheValueIsNotNull(CacheKey.fired);
  }

  /** Checks whether the 'printTime' field is set and is not null */
  public boolean isNotNullPrintTime() {
    return genClient.cacheValueIsNotNull(CacheKey.printTime);
  }



  /** Checks whether the 'id' field has been set, however the value could be null */
  public boolean hasId() {
    return genClient.cacheHasKey(CacheKey.id);
  }

  /** Checks whether the 'orderRef' field has been set, however the value could be null */
  public boolean hasOrderRef() {
    return genClient.cacheHasKey(CacheKey.orderRef);
  }

  /** Checks whether the 'name' field has been set, however the value could be null */
  public boolean hasName() {
    return genClient.cacheHasKey(CacheKey.name);
  }

  /** Checks whether the 'sortOrder' field has been set, however the value could be null */
  public boolean hasSortOrder() {
    return genClient.cacheHasKey(CacheKey.sortOrder);
  }

  /** Checks whether the 'fired' field has been set, however the value could be null */
  public boolean hasFired() {
    return genClient.cacheHasKey(CacheKey.fired);
  }

  /** Checks whether the 'printTime' field has been set, however the value could be null */
  public boolean hasPrintTime() {
    return genClient.cacheHasKey(CacheKey.printTime);
  }


  /**
   * Sets the field 'id'.
   */
  public PrintGroup setId(String id) {
    return genClient.setOther(id, CacheKey.id);
  }

  /**
   * Sets the field 'orderRef'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public PrintGroup setOrderRef(com.clover.sdk.v3.base.Reference orderRef) {
    return genClient.setRecord(orderRef, CacheKey.orderRef);
  }

  /**
   * Sets the field 'name'.
   */
  public PrintGroup setName(String name) {
    return genClient.setOther(name, CacheKey.name);
  }

  /**
   * Sets the field 'sortOrder'.
   */
  public PrintGroup setSortOrder(Integer sortOrder) {
    return genClient.setOther(sortOrder, CacheKey.sortOrder);
  }

  /**
   * Sets the field 'fired'.
   */
  public PrintGroup setFired(Boolean fired) {
    return genClient.setOther(fired, CacheKey.fired);
  }

  /**
   * Sets the field 'printTime'.
   */
  public PrintGroup setPrintTime(Long printTime) {
    return genClient.setOther(printTime, CacheKey.printTime);
  }


  /** Clears the 'id' field, the 'has' method for this field will now return false */
  public void clearId() {
    genClient.clear(CacheKey.id);
  }
  /** Clears the 'orderRef' field, the 'has' method for this field will now return false */
  public void clearOrderRef() {
    genClient.clear(CacheKey.orderRef);
  }
  /** Clears the 'name' field, the 'has' method for this field will now return false */
  public void clearName() {
    genClient.clear(CacheKey.name);
  }
  /** Clears the 'sortOrder' field, the 'has' method for this field will now return false */
  public void clearSortOrder() {
    genClient.clear(CacheKey.sortOrder);
  }
  /** Clears the 'fired' field, the 'has' method for this field will now return false */
  public void clearFired() {
    genClient.clear(CacheKey.fired);
  }
  /** Clears the 'printTime' field, the 'has' method for this field will now return false */
  public void clearPrintTime() {
    genClient.clear(CacheKey.printTime);
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
  public PrintGroup copyChanges() {
    PrintGroup copy = new PrintGroup();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(PrintGroup src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new PrintGroup(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<PrintGroup> CREATOR = new android.os.Parcelable.Creator<PrintGroup>() {
    @Override
    public PrintGroup createFromParcel(android.os.Parcel in) {
      PrintGroup instance = new PrintGroup(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public PrintGroup[] newArray(int size) {
      return new PrintGroup[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<PrintGroup> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<PrintGroup>() {
    public Class<PrintGroup> getCreatedClass() {
      return PrintGroup.class;
    }

    @Override
    public PrintGroup create(org.json.JSONObject jsonObject) {
      return new PrintGroup(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean ID_IS_REQUIRED = false;
    public static final long ID_MAX_LEN = 13;
    public static final boolean ORDERREF_IS_REQUIRED = false;
    public static final boolean NAME_IS_REQUIRED = false;
    public static final long NAME_MAX_LEN = 127;
    public static final boolean SORTORDER_IS_REQUIRED = false;
    public static final long SORTORDER_MIN = 0;
    public static final boolean FIRED_IS_REQUIRED = false;
    public static final boolean PRINTTIME_IS_REQUIRED = false;
  }

}
