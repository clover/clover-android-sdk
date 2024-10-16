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
 * An Override of a particular Layout in Menus.
 * <p>
 * <h3>Fields</h3>
 * <ul>
 * <li>{@link #getCosUuid cosUuid}</li>
 * <li>{@link #getMenuId menuId}</li>
 * <li>{@link #getId id}</li>
 * <li>{@link #getSortOrder sortOrder}</li>
 * <li>{@link #getColorCode colorCode}</li>
 * <li>{@link #getEnabled enabled}</li>
 * <li>{@link #getModifiedTime modifiedTime}</li>
 * <li>{@link #getCreatedTime createdTime}</li>
 * <li>{@link #getDeletedTime deletedTime}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class LayoutOverride extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  /**
   * Unique Clover identifier.
   */
  public String getCosUuid() {
    return genClient.cacheGet(CacheKey.cosUuid);
  }

  /**
   * Unique Clover identifier.
   */
  public String getMenuId() {
    return genClient.cacheGet(CacheKey.menuId);
  }

  /**
   * Unique Clover identifier.
   */
  public String getId() {
    return genClient.cacheGet(CacheKey.id);
  }

  /**
   * Integer used to determine how this layout sorted against other layout.
   */
  public Integer getSortOrder() {
    return genClient.cacheGet(CacheKey.sortOrder);
  }

  /**
   * Color code.
   */
  public String getColorCode() {
    return genClient.cacheGet(CacheKey.colorCode);
  }

  /**
   * Layout override enabled.
   */
  public Boolean getEnabled() {
    return genClient.cacheGet(CacheKey.enabled);
  }

  /**
   * Timestamp when layout was modified.
   */
  public Long getModifiedTime() {
    return genClient.cacheGet(CacheKey.modifiedTime);
  }

  /**
   * Timestamp when layout was created.
   */
  public Long getCreatedTime() {
    return genClient.cacheGet(CacheKey.createdTime);
  }

  /**
   * Timestamp when layout was deleted.
   */
  public Long getDeletedTime() {
    return genClient.cacheGet(CacheKey.deletedTime);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    cosUuid
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(String.class)),
    menuId
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(String.class)),
    id
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(String.class)),
    sortOrder
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(Integer.class)),
    colorCode
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(String.class)),
    enabled
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(Boolean.class)),
    modifiedTime
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(Long.class)),
    createdTime
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(Long.class)),
    deletedTime
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

  private final GenericClient<LayoutOverride> genClient;

  /**
   * Constructs a new empty instance.
   */
  public LayoutOverride() {
    genClient = new GenericClient<LayoutOverride>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected LayoutOverride(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public LayoutOverride(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public LayoutOverride(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public LayoutOverride(LayoutOverride src) {
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
    genClient.validateCloverId(CacheKey.cosUuid, getCosUuid());

    genClient.validateLength(CacheKey.menuId, getMenuId(), 127);

    genClient.validateLength(CacheKey.id, getId(), 127);

    genClient.validateNotNull(CacheKey.sortOrder, getSortOrder());
  }

  /** Checks whether the 'cosUuid' field is set and is not null */
  public boolean isNotNullCosUuid() {
    return genClient.cacheValueIsNotNull(CacheKey.cosUuid);
  }

  /** Checks whether the 'menuId' field is set and is not null */
  public boolean isNotNullMenuId() {
    return genClient.cacheValueIsNotNull(CacheKey.menuId);
  }

  /** Checks whether the 'id' field is set and is not null */
  public boolean isNotNullId() {
    return genClient.cacheValueIsNotNull(CacheKey.id);
  }

  /** Checks whether the 'sortOrder' field is set and is not null */
  public boolean isNotNullSortOrder() {
    return genClient.cacheValueIsNotNull(CacheKey.sortOrder);
  }

  /** Checks whether the 'colorCode' field is set and is not null */
  public boolean isNotNullColorCode() {
    return genClient.cacheValueIsNotNull(CacheKey.colorCode);
  }

  /** Checks whether the 'enabled' field is set and is not null */
  public boolean isNotNullEnabled() {
    return genClient.cacheValueIsNotNull(CacheKey.enabled);
  }

  /** Checks whether the 'modifiedTime' field is set and is not null */
  public boolean isNotNullModifiedTime() {
    return genClient.cacheValueIsNotNull(CacheKey.modifiedTime);
  }

  /** Checks whether the 'createdTime' field is set and is not null */
  public boolean isNotNullCreatedTime() {
    return genClient.cacheValueIsNotNull(CacheKey.createdTime);
  }

  /** Checks whether the 'deletedTime' field is set and is not null */
  public boolean isNotNullDeletedTime() {
    return genClient.cacheValueIsNotNull(CacheKey.deletedTime);
  }



  /** Checks whether the 'cosUuid' field has been set, however the value could be null */
  public boolean hasCosUuid() {
    return genClient.cacheHasKey(CacheKey.cosUuid);
  }

  /** Checks whether the 'menuId' field has been set, however the value could be null */
  public boolean hasMenuId() {
    return genClient.cacheHasKey(CacheKey.menuId);
  }

  /** Checks whether the 'id' field has been set, however the value could be null */
  public boolean hasId() {
    return genClient.cacheHasKey(CacheKey.id);
  }

  /** Checks whether the 'sortOrder' field has been set, however the value could be null */
  public boolean hasSortOrder() {
    return genClient.cacheHasKey(CacheKey.sortOrder);
  }

  /** Checks whether the 'colorCode' field has been set, however the value could be null */
  public boolean hasColorCode() {
    return genClient.cacheHasKey(CacheKey.colorCode);
  }

  /** Checks whether the 'enabled' field has been set, however the value could be null */
  public boolean hasEnabled() {
    return genClient.cacheHasKey(CacheKey.enabled);
  }

  /** Checks whether the 'modifiedTime' field has been set, however the value could be null */
  public boolean hasModifiedTime() {
    return genClient.cacheHasKey(CacheKey.modifiedTime);
  }

  /** Checks whether the 'createdTime' field has been set, however the value could be null */
  public boolean hasCreatedTime() {
    return genClient.cacheHasKey(CacheKey.createdTime);
  }

  /** Checks whether the 'deletedTime' field has been set, however the value could be null */
  public boolean hasDeletedTime() {
    return genClient.cacheHasKey(CacheKey.deletedTime);
  }


  /**
   * Sets the field 'cosUuid'.
   */
  public LayoutOverride setCosUuid(String cosUuid) {
    return genClient.setOther(cosUuid, CacheKey.cosUuid);
  }

  /**
   * Sets the field 'menuId'.
   */
  public LayoutOverride setMenuId(String menuId) {
    return genClient.setOther(menuId, CacheKey.menuId);
  }

  /**
   * Sets the field 'id'.
   */
  public LayoutOverride setId(String id) {
    return genClient.setOther(id, CacheKey.id);
  }

  /**
   * Sets the field 'sortOrder'.
   */
  public LayoutOverride setSortOrder(Integer sortOrder) {
    return genClient.setOther(sortOrder, CacheKey.sortOrder);
  }

  /**
   * Sets the field 'colorCode'.
   */
  public LayoutOverride setColorCode(String colorCode) {
    return genClient.setOther(colorCode, CacheKey.colorCode);
  }

  /**
   * Sets the field 'enabled'.
   */
  public LayoutOverride setEnabled(Boolean enabled) {
    return genClient.setOther(enabled, CacheKey.enabled);
  }

  /**
   * Sets the field 'modifiedTime'.
   */
  public LayoutOverride setModifiedTime(Long modifiedTime) {
    return genClient.setOther(modifiedTime, CacheKey.modifiedTime);
  }

  /**
   * Sets the field 'createdTime'.
   */
  public LayoutOverride setCreatedTime(Long createdTime) {
    return genClient.setOther(createdTime, CacheKey.createdTime);
  }

  /**
   * Sets the field 'deletedTime'.
   */
  public LayoutOverride setDeletedTime(Long deletedTime) {
    return genClient.setOther(deletedTime, CacheKey.deletedTime);
  }


  /** Clears the 'cosUuid' field, the 'has' method for this field will now return false */
  public void clearCosUuid() {
    genClient.clear(CacheKey.cosUuid);
  }
  /** Clears the 'menuId' field, the 'has' method for this field will now return false */
  public void clearMenuId() {
    genClient.clear(CacheKey.menuId);
  }
  /** Clears the 'id' field, the 'has' method for this field will now return false */
  public void clearId() {
    genClient.clear(CacheKey.id);
  }
  /** Clears the 'sortOrder' field, the 'has' method for this field will now return false */
  public void clearSortOrder() {
    genClient.clear(CacheKey.sortOrder);
  }
  /** Clears the 'colorCode' field, the 'has' method for this field will now return false */
  public void clearColorCode() {
    genClient.clear(CacheKey.colorCode);
  }
  /** Clears the 'enabled' field, the 'has' method for this field will now return false */
  public void clearEnabled() {
    genClient.clear(CacheKey.enabled);
  }
  /** Clears the 'modifiedTime' field, the 'has' method for this field will now return false */
  public void clearModifiedTime() {
    genClient.clear(CacheKey.modifiedTime);
  }
  /** Clears the 'createdTime' field, the 'has' method for this field will now return false */
  public void clearCreatedTime() {
    genClient.clear(CacheKey.createdTime);
  }
  /** Clears the 'deletedTime' field, the 'has' method for this field will now return false */
  public void clearDeletedTime() {
    genClient.clear(CacheKey.deletedTime);
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
  public LayoutOverride copyChanges() {
    LayoutOverride copy = new LayoutOverride();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(LayoutOverride src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new LayoutOverride(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<LayoutOverride> CREATOR = new android.os.Parcelable.Creator<LayoutOverride>() {
    @Override
    public LayoutOverride createFromParcel(android.os.Parcel in) {
      LayoutOverride instance = new LayoutOverride(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public LayoutOverride[] newArray(int size) {
      return new LayoutOverride[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<LayoutOverride> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<LayoutOverride>() {
    public Class<LayoutOverride> getCreatedClass() {
      return LayoutOverride.class;
    }

    @Override
    public LayoutOverride create(org.json.JSONObject jsonObject) {
      return new LayoutOverride(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean COSUUID_IS_REQUIRED = false;
    public static final long COSUUID_MAX_LEN = 127;
    public static final boolean MENUID_IS_REQUIRED = false;
    public static final long MENUID_MAX_LEN = 127;
    public static final boolean ID_IS_REQUIRED = false;
    public static final long ID_MAX_LEN = 127;
    public static final boolean SORTORDER_IS_REQUIRED = true;
    public static final boolean COLORCODE_IS_REQUIRED = false;
    public static final boolean ENABLED_IS_REQUIRED = false;
    public static final boolean MODIFIEDTIME_IS_REQUIRED = false;
    public static final boolean CREATEDTIME_IS_REQUIRED = false;
    public static final boolean DELETEDTIME_IS_REQUIRED = false;
  }

}
