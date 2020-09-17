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
 * <h3>Fields</h3>
 * <ul>
 * <li>{@link #getItems items}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class CreateLineItemsRequest extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  /**
   * List of LineItem objects to create
   */
  public java.util.List<com.clover.sdk.v3.order.LineItem> getItems() {
    return genClient.cacheGet(CacheKey.items);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    items
        (com.clover.sdk.extractors.RecordListExtractionStrategy.instance(com.clover.sdk.v3.order.LineItem.JSON_CREATOR)),
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

  private final GenericClient<CreateLineItemsRequest> genClient;

  /**
   * Constructs a new empty instance.
   */
  public CreateLineItemsRequest() {
    genClient = new GenericClient<CreateLineItemsRequest>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected CreateLineItemsRequest(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public CreateLineItemsRequest(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public CreateLineItemsRequest(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public CreateLineItemsRequest(CreateLineItemsRequest src) {
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

  /** Checks whether the 'items' field is set and is not null */
  public boolean isNotNullItems() {
    return genClient.cacheValueIsNotNull(CacheKey.items);
  }

  /** Checks whether the 'items' field is set and is not null and is not empty */
  public boolean isNotEmptyItems() { return isNotNullItems() && !getItems().isEmpty(); }



  /** Checks whether the 'items' field has been set, however the value could be null */
  public boolean hasItems() {
    return genClient.cacheHasKey(CacheKey.items);
  }


  /**
   * Sets the field 'items'.
   *
   * Nulls in the given List are skipped. List parameter is copied, so it will not reflect any changes, but objects inside it will.
   */
  public CreateLineItemsRequest setItems(java.util.List<com.clover.sdk.v3.order.LineItem> items) {
    return genClient.setArrayRecord(items, CacheKey.items);
  }


  /** Clears the 'items' field, the 'has' method for this field will now return false */
  public void clearItems() {
    genClient.clear(CacheKey.items);
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
  public CreateLineItemsRequest copyChanges() {
    CreateLineItemsRequest copy = new CreateLineItemsRequest();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(CreateLineItemsRequest src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new CreateLineItemsRequest(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<CreateLineItemsRequest> CREATOR = new android.os.Parcelable.Creator<CreateLineItemsRequest>() {
    @Override
    public CreateLineItemsRequest createFromParcel(android.os.Parcel in) {
      CreateLineItemsRequest instance = new CreateLineItemsRequest(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public CreateLineItemsRequest[] newArray(int size) {
      return new CreateLineItemsRequest[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<CreateLineItemsRequest> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<CreateLineItemsRequest>() {
    public Class<CreateLineItemsRequest> getCreatedClass() {
      return CreateLineItemsRequest.class;
    }

    @Override
    public CreateLineItemsRequest create(org.json.JSONObject jsonObject) {
      return new CreateLineItemsRequest(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean ITEMS_IS_REQUIRED = false;
  }

}
