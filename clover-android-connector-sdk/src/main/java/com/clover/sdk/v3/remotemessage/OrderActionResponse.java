/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */


/*
 * Copyright (C) 2016 Clover Network, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.clover.sdk.v3.remotemessage;

import com.clover.sdk.GenericClient;
import com.clover.sdk.GenericParcelable;

/**
 * This is an auto-generated Clover data object.
 * <p>
 * <h3>Fields</h3>
 * <ul>
 * <li>{@link #getAccepted accepted}</li>
 * <li>{@link #getId id}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class OrderActionResponse extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  public java.lang.Boolean getAccepted() {
    return genClient.cacheGet(CacheKey.accepted);
  }

  public java.lang.String getId() {
    return genClient.cacheGet(CacheKey.id);
  }




  private enum CacheKey implements com.clover.sdk.ValueExtractorEnum<OrderActionResponse> {
    accepted {
      @Override
      public Object extractValue(OrderActionResponse instance) {
        return instance.genClient.extractOther("accepted", java.lang.Boolean.class);
      }
    },
    id {
      @Override
      public Object extractValue(OrderActionResponse instance) {
        return instance.genClient.extractOther("id", java.lang.String.class);
      }
    },
      ;
  }

  private GenericClient<OrderActionResponse> genClient;

  /**
  * Constructs a new empty instance.
  */
  public OrderActionResponse() {
    genClient = new GenericClient<OrderActionResponse>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
  * Constructs a new empty instance.
  */
  protected OrderActionResponse(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public OrderActionResponse(String json) throws IllegalArgumentException {
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
  public OrderActionResponse(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public OrderActionResponse(OrderActionResponse src) {
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

  /** Checks whether the 'accepted' field is set and is not null */
  public boolean isNotNullAccepted() {
    return genClient.cacheValueIsNotNull(CacheKey.accepted);
  }

  /** Checks whether the 'id' field is set and is not null */
  public boolean isNotNullId() {
    return genClient.cacheValueIsNotNull(CacheKey.id);
  }



  /** Checks whether the 'accepted' field has been set, however the value could be null */
  public boolean hasAccepted() {
    return genClient.cacheHasKey(CacheKey.accepted);
  }

  /** Checks whether the 'id' field has been set, however the value could be null */
  public boolean hasId() {
    return genClient.cacheHasKey(CacheKey.id);
  }


  /**
   * Sets the field 'accepted'.
   */
  public OrderActionResponse setAccepted(java.lang.Boolean accepted) {
    return genClient.setOther(accepted, CacheKey.accepted);
  }

  /**
   * Sets the field 'id'.
   */
  public OrderActionResponse setId(java.lang.String id) {
    return genClient.setOther(id, CacheKey.id);
  }


  /** Clears the 'accepted' field, the 'has' method for this field will now return false */
  public void clearAccepted() {
    genClient.clear(CacheKey.accepted);
  }
  /** Clears the 'id' field, the 'has' method for this field will now return false */
  public void clearId() {
    genClient.clear(CacheKey.id);
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
  public OrderActionResponse copyChanges() {
    OrderActionResponse copy = new OrderActionResponse();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(OrderActionResponse src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new OrderActionResponse(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<OrderActionResponse> CREATOR = new android.os.Parcelable.Creator<OrderActionResponse>() {
    @Override
    public OrderActionResponse createFromParcel(android.os.Parcel in) {
      OrderActionResponse instance = new OrderActionResponse(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public OrderActionResponse[] newArray(int size) {
      return new OrderActionResponse[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<OrderActionResponse> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<OrderActionResponse>() {
    @Override
    public OrderActionResponse create(org.json.JSONObject jsonObject) {
      return new OrderActionResponse(jsonObject);
    }
  };

  public interface Constraints {

    public static final boolean ACCEPTED_IS_REQUIRED = false;
    public static final boolean ID_IS_REQUIRED = false;

  }

}