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
 * Object to capture reason for deleting an order
 * <p>
 * <h3>Fields</h3>
 * <ul>
 * <li>{@link #getReason reason}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class OrderDeleteReason extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  public java.lang.String getReason() {
    return genClient.cacheGet(CacheKey.reason);
  }



  public static final String AUTHORITY = "com.clover.orders";

  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    reason
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
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

  private final GenericClient<OrderDeleteReason> genClient;

  /**
   * Constructs a new empty instance.
   */
  public OrderDeleteReason() {
    genClient = new GenericClient<OrderDeleteReason>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected OrderDeleteReason(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public OrderDeleteReason(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public OrderDeleteReason(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public OrderDeleteReason(OrderDeleteReason src) {
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

  /** Checks whether the 'reason' field is set and is not null */
  public boolean isNotNullReason() {
    return genClient.cacheValueIsNotNull(CacheKey.reason);
  }



  /** Checks whether the 'reason' field has been set, however the value could be null */
  public boolean hasReason() {
    return genClient.cacheHasKey(CacheKey.reason);
  }


  /**
   * Sets the field 'reason'.
   */
  public OrderDeleteReason setReason(java.lang.String reason) {
    return genClient.setOther(reason, CacheKey.reason);
  }


  /** Clears the 'reason' field, the 'has' method for this field will now return false */
  public void clearReason() {
    genClient.clear(CacheKey.reason);
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
  public OrderDeleteReason copyChanges() {
    OrderDeleteReason copy = new OrderDeleteReason();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(OrderDeleteReason src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new OrderDeleteReason(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<OrderDeleteReason> CREATOR = new android.os.Parcelable.Creator<OrderDeleteReason>() {
    @Override
    public OrderDeleteReason createFromParcel(android.os.Parcel in) {
      OrderDeleteReason instance = new OrderDeleteReason(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public OrderDeleteReason[] newArray(int size) {
      return new OrderDeleteReason[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<OrderDeleteReason> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<OrderDeleteReason>() {
    public Class<OrderDeleteReason> getCreatedClass() {
      return OrderDeleteReason.class;
    }

    @Override
    public OrderDeleteReason create(org.json.JSONObject jsonObject) {
      return new OrderDeleteReason(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean REASON_IS_REQUIRED = false;
  }

}
