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
 * <li>{@link #getItem item}</li>
 * <li>{@link #getCategory category}</li>
 * </ul>
 * <p>
 * @see com.clover.sdk.v3.inventory.IInventoryService
 */
@SuppressWarnings("all")
public class CategoryItem extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  public com.clover.sdk.v3.inventory.Item getItem() {
    return genClient.cacheGet(CacheKey.item);
  }

  public com.clover.sdk.v3.inventory.Category getCategory() {
    return genClient.cacheGet(CacheKey.category);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    item
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.inventory.Item.JSON_CREATOR)),
    category
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.inventory.Category.JSON_CREATOR)),
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

  private final GenericClient<CategoryItem> genClient;

  /**
   * Constructs a new empty instance.
   */
  public CategoryItem() {
    genClient = new GenericClient<CategoryItem>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected CategoryItem(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public CategoryItem(String json) throws IllegalArgumentException {
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
  public CategoryItem(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public CategoryItem(CategoryItem src) {
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
    genClient.validateNull(getItem(), "item");

    genClient.validateNull(getCategory(), "category");
  }

  /** Checks whether the 'item' field is set and is not null */
  public boolean isNotNullItem() {
    return genClient.cacheValueIsNotNull(CacheKey.item);
  }

  /** Checks whether the 'category' field is set and is not null */
  public boolean isNotNullCategory() {
    return genClient.cacheValueIsNotNull(CacheKey.category);
  }



  /** Checks whether the 'item' field has been set, however the value could be null */
  public boolean hasItem() {
    return genClient.cacheHasKey(CacheKey.item);
  }

  /** Checks whether the 'category' field has been set, however the value could be null */
  public boolean hasCategory() {
    return genClient.cacheHasKey(CacheKey.category);
  }


  /**
   * Sets the field 'item'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public CategoryItem setItem(com.clover.sdk.v3.inventory.Item item) {
    return genClient.setRecord(item, CacheKey.item);
  }

  /**
   * Sets the field 'category'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public CategoryItem setCategory(com.clover.sdk.v3.inventory.Category category) {
    return genClient.setRecord(category, CacheKey.category);
  }


  /** Clears the 'item' field, the 'has' method for this field will now return false */
  public void clearItem() {
    genClient.clear(CacheKey.item);
  }
  /** Clears the 'category' field, the 'has' method for this field will now return false */
  public void clearCategory() {
    genClient.clear(CacheKey.category);
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
  public CategoryItem copyChanges() {
    CategoryItem copy = new CategoryItem();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(CategoryItem src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new CategoryItem(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<CategoryItem> CREATOR = new android.os.Parcelable.Creator<CategoryItem>() {
    @Override
    public CategoryItem createFromParcel(android.os.Parcel in) {
      CategoryItem instance = new CategoryItem(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public CategoryItem[] newArray(int size) {
      return new CategoryItem[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<CategoryItem> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<CategoryItem>() {
    @Override
    public CategoryItem create(org.json.JSONObject jsonObject) {
      return new CategoryItem(jsonObject);
    }
  };

  public interface Constraints {

    public static final boolean ITEM_IS_REQUIRED = true;
    public static final boolean CATEGORY_IS_REQUIRED = true;

  }

}
