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

package com.clover.sdk.v3.inventory;

import com.clover.sdk.GenericClient;
import com.clover.sdk.GenericParcelable;

/**
 * This is an auto-generated Clover data object.
 * <p>
 * <h3>Fields</h3>
 * <ul>
 * <li>{@link #getId id}</li>
 * <li>{@link #getName name}</li>
 * <li>{@link #getAmount amount}</li>
 * <li>{@link #getPercentage percentage}</li>
 * <li>{@link #getIsDefault isDefault}</li>
 * <li>{@link #getIsHidden isHidden}</li>
 * <li>{@link #getReadOnly readOnly}</li>
 * <li>{@link #getTaxRates taxRates}</li>
 * <li>{@link #getCreatedTime createdTime}</li>
 * <li>{@link #getModifiedTime modifiedTime}</li>
 * <li>{@link #getDeletedTime deletedTime}</li>
 * <li>{@link #getType type}</li>
 * <li>{@link #getServiceChargeUuid serviceChargeUuid}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class OrderFee extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  public String getId() {
    return genClient.cacheGet(CacheKey.id);
  }

  public String getName() {
    return genClient.cacheGet(CacheKey.name);
  }

  /**
   * For a flat order fee, expressed as number of cents
   */
  public Long getAmount() {
    return genClient.cacheGet(CacheKey.amount);
  }

  /**
   * For percentage based order fee, percent to charge times 10000, e.g. 12.5% will be 125000
   */
  public Long getPercentage() {
    return genClient.cacheGet(CacheKey.percentage);
  }

  public Boolean getIsDefault() {
    return genClient.cacheGet(CacheKey.isDefault);
  }

  public Boolean getIsHidden() {
    return genClient.cacheGet(CacheKey.isHidden);
  }

  public Boolean getReadOnly() {
    return genClient.cacheGet(CacheKey.readOnly);
  }

  /**
   * A reference to an associated tax rate applied to the order fee
   */
  public java.util.List<TaxRate> getTaxRates() {
    return genClient.cacheGet(CacheKey.taxRates);
  }

  /**
   * Timestamp when the order fee was created
   */
  public Long getCreatedTime() {
    return genClient.cacheGet(CacheKey.createdTime);
  }

  /**
   * Timestamp when the order fee was last modified
   */
  public Long getModifiedTime() {
    return genClient.cacheGet(CacheKey.modifiedTime);
  }

  /**
   * Timestamp when order fee was last deleted
   */
  public Long getDeletedTime() {
    return genClient.cacheGet(CacheKey.deletedTime);
  }

  /**
   * Used to define type of order fee, e.g. gratuity
   */
  public String getType() {
    return genClient.cacheGet(CacheKey.type);
  }

  /**
   * Original service charge uuid
   */
  public String getServiceChargeUuid() {
    return genClient.cacheGet(CacheKey.serviceChargeUuid);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    id
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(String.class)),
    name
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(String.class)),
    amount
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(Long.class)),
    percentage
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(Long.class)),
    isDefault
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(Boolean.class)),
    isHidden
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(Boolean.class)),
    readOnly
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(Boolean.class)),
    taxRates
        (com.clover.sdk.extractors.RecordListExtractionStrategy.instance(TaxRate.JSON_CREATOR)),
    createdTime
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(Long.class)),
    modifiedTime
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(Long.class)),
    deletedTime
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(Long.class)),
    type
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(String.class)),
    serviceChargeUuid
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(String.class)),
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

  private final GenericClient<OrderFee> genClient;

  /**
   * Constructs a new empty instance.
   */
  public OrderFee() {
    genClient = new GenericClient<OrderFee>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected OrderFee(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public OrderFee(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public OrderFee(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public OrderFee(OrderFee src) {
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

    genClient.validateNotNull(CacheKey.name, getName());
    genClient.validateLength(CacheKey.name, getName(), 127);

    genClient.validateMin(CacheKey.amount, getAmount(), 0L);

    genClient.validateMinMax(CacheKey.percentage, getPercentage(), 0L, 1000000L);

    genClient.validateLength(CacheKey.serviceChargeUuid, getServiceChargeUuid(), 13);
  }

  /** Checks whether the 'id' field is set and is not null */
  public boolean isNotNullId() {
    return genClient.cacheValueIsNotNull(CacheKey.id);
  }

  /** Checks whether the 'name' field is set and is not null */
  public boolean isNotNullName() {
    return genClient.cacheValueIsNotNull(CacheKey.name);
  }

  /** Checks whether the 'amount' field is set and is not null */
  public boolean isNotNullAmount() {
    return genClient.cacheValueIsNotNull(CacheKey.amount);
  }

  /** Checks whether the 'percentage' field is set and is not null */
  public boolean isNotNullPercentage() {
    return genClient.cacheValueIsNotNull(CacheKey.percentage);
  }

  /** Checks whether the 'isDefault' field is set and is not null */
  public boolean isNotNullIsDefault() {
    return genClient.cacheValueIsNotNull(CacheKey.isDefault);
  }

  /** Checks whether the 'isHidden' field is set and is not null */
  public boolean isNotNullIsHidden() {
    return genClient.cacheValueIsNotNull(CacheKey.isHidden);
  }

  /** Checks whether the 'readOnly' field is set and is not null */
  public boolean isNotNullReadOnly() {
    return genClient.cacheValueIsNotNull(CacheKey.readOnly);
  }

  /** Checks whether the 'taxRates' field is set and is not null */
  public boolean isNotNullTaxRates() {
    return genClient.cacheValueIsNotNull(CacheKey.taxRates);
  }

  /** Checks whether the 'taxRates' field is set and is not null and is not empty */
  public boolean isNotEmptyTaxRates() { return isNotNullTaxRates() && !getTaxRates().isEmpty(); }

  /** Checks whether the 'createdTime' field is set and is not null */
  public boolean isNotNullCreatedTime() {
    return genClient.cacheValueIsNotNull(CacheKey.createdTime);
  }

  /** Checks whether the 'modifiedTime' field is set and is not null */
  public boolean isNotNullModifiedTime() {
    return genClient.cacheValueIsNotNull(CacheKey.modifiedTime);
  }

  /** Checks whether the 'deletedTime' field is set and is not null */
  public boolean isNotNullDeletedTime() {
    return genClient.cacheValueIsNotNull(CacheKey.deletedTime);
  }

  /** Checks whether the 'type' field is set and is not null */
  public boolean isNotNullType() {
    return genClient.cacheValueIsNotNull(CacheKey.type);
  }

  /** Checks whether the 'serviceChargeUuid' field is set and is not null */
  public boolean isNotNullServiceChargeUuid() {
    return genClient.cacheValueIsNotNull(CacheKey.serviceChargeUuid);
  }



  /** Checks whether the 'id' field has been set, however the value could be null */
  public boolean hasId() {
    return genClient.cacheHasKey(CacheKey.id);
  }

  /** Checks whether the 'name' field has been set, however the value could be null */
  public boolean hasName() {
    return genClient.cacheHasKey(CacheKey.name);
  }

  /** Checks whether the 'amount' field has been set, however the value could be null */
  public boolean hasAmount() {
    return genClient.cacheHasKey(CacheKey.amount);
  }

  /** Checks whether the 'percentage' field has been set, however the value could be null */
  public boolean hasPercentage() {
    return genClient.cacheHasKey(CacheKey.percentage);
  }

  /** Checks whether the 'isDefault' field has been set, however the value could be null */
  public boolean hasIsDefault() {
    return genClient.cacheHasKey(CacheKey.isDefault);
  }

  /** Checks whether the 'isHidden' field has been set, however the value could be null */
  public boolean hasIsHidden() {
    return genClient.cacheHasKey(CacheKey.isHidden);
  }

  /** Checks whether the 'readOnly' field has been set, however the value could be null */
  public boolean hasReadOnly() {
    return genClient.cacheHasKey(CacheKey.readOnly);
  }

  /** Checks whether the 'taxRates' field has been set, however the value could be null */
  public boolean hasTaxRates() {
    return genClient.cacheHasKey(CacheKey.taxRates);
  }

  /** Checks whether the 'createdTime' field has been set, however the value could be null */
  public boolean hasCreatedTime() {
    return genClient.cacheHasKey(CacheKey.createdTime);
  }

  /** Checks whether the 'modifiedTime' field has been set, however the value could be null */
  public boolean hasModifiedTime() {
    return genClient.cacheHasKey(CacheKey.modifiedTime);
  }

  /** Checks whether the 'deletedTime' field has been set, however the value could be null */
  public boolean hasDeletedTime() {
    return genClient.cacheHasKey(CacheKey.deletedTime);
  }

  /** Checks whether the 'type' field has been set, however the value could be null */
  public boolean hasType() {
    return genClient.cacheHasKey(CacheKey.type);
  }

  /** Checks whether the 'serviceChargeUuid' field has been set, however the value could be null */
  public boolean hasServiceChargeUuid() {
    return genClient.cacheHasKey(CacheKey.serviceChargeUuid);
  }


  /**
   * Sets the field 'id'.
   */
  public OrderFee setId(String id) {
    return genClient.setOther(id, CacheKey.id);
  }

  /**
   * Sets the field 'name'.
   */
  public OrderFee setName(String name) {
    return genClient.setOther(name, CacheKey.name);
  }

  /**
   * Sets the field 'amount'.
   */
  public OrderFee setAmount(Long amount) {
    return genClient.setOther(amount, CacheKey.amount);
  }

  /**
   * Sets the field 'percentage'.
   */
  public OrderFee setPercentage(Long percentage) {
    return genClient.setOther(percentage, CacheKey.percentage);
  }

  /**
   * Sets the field 'isDefault'.
   */
  public OrderFee setIsDefault(Boolean isDefault) {
    return genClient.setOther(isDefault, CacheKey.isDefault);
  }

  /**
   * Sets the field 'isHidden'.
   */
  public OrderFee setIsHidden(Boolean isHidden) {
    return genClient.setOther(isHidden, CacheKey.isHidden);
  }

  /**
   * Sets the field 'readOnly'.
   */
  public OrderFee setReadOnly(Boolean readOnly) {
    return genClient.setOther(readOnly, CacheKey.readOnly);
  }

  /**
   * Sets the field 'taxRates'.
   *
   * Nulls in the given List are skipped. List parameter is copied, so it will not reflect any changes, but objects inside it will.
   */
  public OrderFee setTaxRates(java.util.List<TaxRate> taxRates) {
    return genClient.setArrayRecord(taxRates, CacheKey.taxRates);
  }

  /**
   * Sets the field 'createdTime'.
   */
  public OrderFee setCreatedTime(Long createdTime) {
    return genClient.setOther(createdTime, CacheKey.createdTime);
  }

  /**
   * Sets the field 'modifiedTime'.
   */
  public OrderFee setModifiedTime(Long modifiedTime) {
    return genClient.setOther(modifiedTime, CacheKey.modifiedTime);
  }

  /**
   * Sets the field 'deletedTime'.
   */
  public OrderFee setDeletedTime(Long deletedTime) {
    return genClient.setOther(deletedTime, CacheKey.deletedTime);
  }

  /**
   * Sets the field 'type'.
   */
  public OrderFee setType(String type) {
    return genClient.setOther(type, CacheKey.type);
  }

  /**
   * Sets the field 'serviceChargeUuid'.
   */
  public OrderFee setServiceChargeUuid(String serviceChargeUuid) {
    return genClient.setOther(serviceChargeUuid, CacheKey.serviceChargeUuid);
  }


  /** Clears the 'id' field, the 'has' method for this field will now return false */
  public void clearId() {
    genClient.clear(CacheKey.id);
  }
  /** Clears the 'name' field, the 'has' method for this field will now return false */
  public void clearName() {
    genClient.clear(CacheKey.name);
  }
  /** Clears the 'amount' field, the 'has' method for this field will now return false */
  public void clearAmount() {
    genClient.clear(CacheKey.amount);
  }
  /** Clears the 'percentage' field, the 'has' method for this field will now return false */
  public void clearPercentage() {
    genClient.clear(CacheKey.percentage);
  }
  /** Clears the 'isDefault' field, the 'has' method for this field will now return false */
  public void clearIsDefault() {
    genClient.clear(CacheKey.isDefault);
  }
  /** Clears the 'isHidden' field, the 'has' method for this field will now return false */
  public void clearIsHidden() {
    genClient.clear(CacheKey.isHidden);
  }
  /** Clears the 'readOnly' field, the 'has' method for this field will now return false */
  public void clearReadOnly() {
    genClient.clear(CacheKey.readOnly);
  }
  /** Clears the 'taxRates' field, the 'has' method for this field will now return false */
  public void clearTaxRates() {
    genClient.clear(CacheKey.taxRates);
  }
  /** Clears the 'createdTime' field, the 'has' method for this field will now return false */
  public void clearCreatedTime() {
    genClient.clear(CacheKey.createdTime);
  }
  /** Clears the 'modifiedTime' field, the 'has' method for this field will now return false */
  public void clearModifiedTime() {
    genClient.clear(CacheKey.modifiedTime);
  }
  /** Clears the 'deletedTime' field, the 'has' method for this field will now return false */
  public void clearDeletedTime() {
    genClient.clear(CacheKey.deletedTime);
  }
  /** Clears the 'type' field, the 'has' method for this field will now return false */
  public void clearType() {
    genClient.clear(CacheKey.type);
  }
  /** Clears the 'serviceChargeUuid' field, the 'has' method for this field will now return false */
  public void clearServiceChargeUuid() {
    genClient.clear(CacheKey.serviceChargeUuid);
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
  public OrderFee copyChanges() {
    OrderFee copy = new OrderFee();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(OrderFee src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new OrderFee(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<OrderFee> CREATOR = new android.os.Parcelable.Creator<OrderFee>() {
    @Override
    public OrderFee createFromParcel(android.os.Parcel in) {
      OrderFee instance = new OrderFee(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public OrderFee[] newArray(int size) {
      return new OrderFee[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<OrderFee> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<OrderFee>() {
    public Class<OrderFee> getCreatedClass() {
      return OrderFee.class;
    }

    @Override
    public OrderFee create(org.json.JSONObject jsonObject) {
      return new OrderFee(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean ID_IS_REQUIRED = false;
    public static final long ID_MAX_LEN = 13;
    public static final boolean NAME_IS_REQUIRED = true;
    public static final long NAME_MAX_LEN = 127;
    public static final boolean AMOUNT_IS_REQUIRED = false;
    public static final long AMOUNT_MIN = 0;
    public static final boolean PERCENTAGE_IS_REQUIRED = false;
    public static final long PERCENTAGE_MIN = 0;
    public static final long PERCENTAGE_MAX = 1000000;
    public static final boolean ISDEFAULT_IS_REQUIRED = false;
    public static final boolean ISHIDDEN_IS_REQUIRED = false;
    public static final boolean READONLY_IS_REQUIRED = false;
    public static final boolean TAXRATES_IS_REQUIRED = false;
    public static final boolean CREATEDTIME_IS_REQUIRED = false;
    public static final boolean MODIFIEDTIME_IS_REQUIRED = false;
    public static final boolean DELETEDTIME_IS_REQUIRED = false;
    public static final boolean TYPE_IS_REQUIRED = false;
    public static final boolean SERVICECHARGEUUID_IS_REQUIRED = false;
    public static final long SERVICECHARGEUUID_MAX_LEN = 13;
  }

}
