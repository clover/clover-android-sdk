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

package com.clover.sdk.v3.payment.raw.model;

import com.clover.sdk.GenericClient;
import com.clover.sdk.GenericParcelable;

/**
 * This is an auto-generated Clover data object.
 * <p>
 * <h3>Fields</h3>
 * <ul>
 * <li>{@link #getPinBlock pinBlock}</li>
 * <li>{@link #getPinBlockKsn pinBlockKsn}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class GetPinResponse extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  public java.lang.String getPinBlock() {
    return genClient.cacheGet(CacheKey.pinBlock);
  }

  public java.lang.String getPinBlockKsn() {
    return genClient.cacheGet(CacheKey.pinBlockKsn);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    pinBlock
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    pinBlockKsn
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

  private final GenericClient<GetPinResponse> genClient;

  /**
   * Constructs a new empty instance.
   */
  public GetPinResponse() {
    genClient = new GenericClient<GetPinResponse>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected GetPinResponse(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public GetPinResponse(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public GetPinResponse(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public GetPinResponse(GetPinResponse src) {
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

  /** Checks whether the 'pinBlock' field is set and is not null */
  public boolean isNotNullPinBlock() {
    return genClient.cacheValueIsNotNull(CacheKey.pinBlock);
  }

  /** Checks whether the 'pinBlockKsn' field is set and is not null */
  public boolean isNotNullPinBlockKsn() {
    return genClient.cacheValueIsNotNull(CacheKey.pinBlockKsn);
  }



  /** Checks whether the 'pinBlock' field has been set, however the value could be null */
  public boolean hasPinBlock() {
    return genClient.cacheHasKey(CacheKey.pinBlock);
  }

  /** Checks whether the 'pinBlockKsn' field has been set, however the value could be null */
  public boolean hasPinBlockKsn() {
    return genClient.cacheHasKey(CacheKey.pinBlockKsn);
  }


  /**
   * Sets the field 'pinBlock'.
   */
  public GetPinResponse setPinBlock(java.lang.String pinBlock) {
    return genClient.setOther(pinBlock, CacheKey.pinBlock);
  }

  /**
   * Sets the field 'pinBlockKsn'.
   */
  public GetPinResponse setPinBlockKsn(java.lang.String pinBlockKsn) {
    return genClient.setOther(pinBlockKsn, CacheKey.pinBlockKsn);
  }


  /** Clears the 'pinBlock' field, the 'has' method for this field will now return false */
  public void clearPinBlock() {
    genClient.clear(CacheKey.pinBlock);
  }
  /** Clears the 'pinBlockKsn' field, the 'has' method for this field will now return false */
  public void clearPinBlockKsn() {
    genClient.clear(CacheKey.pinBlockKsn);
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
  public GetPinResponse copyChanges() {
    GetPinResponse copy = new GetPinResponse();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(GetPinResponse src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new GetPinResponse(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<GetPinResponse> CREATOR = new android.os.Parcelable.Creator<GetPinResponse>() {
    @Override
    public GetPinResponse createFromParcel(android.os.Parcel in) {
      GetPinResponse instance = new GetPinResponse(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public GetPinResponse[] newArray(int size) {
      return new GetPinResponse[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<GetPinResponse> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<GetPinResponse>() {
    public Class<GetPinResponse> getCreatedClass() {
      return GetPinResponse.class;
    }

    @Override
    public GetPinResponse create(org.json.JSONObject jsonObject) {
      return new GetPinResponse(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean PINBLOCK_IS_REQUIRED = false;
    public static final boolean PINBLOCKKSN_IS_REQUIRED = false;
  }

}
