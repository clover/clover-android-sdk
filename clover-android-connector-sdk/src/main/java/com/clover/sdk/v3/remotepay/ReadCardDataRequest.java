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

package com.clover.sdk.v3.remotepay;

import com.clover.sdk.GenericClient;

/**
 * This is an auto-generated Clover data object.
 * <p>
 * <h3>Fields</h3>
 * <ul>
 * <li>{@link #getIsForceSwipePinEntry isForceSwipePinEntry}</li>
 * <li>{@link #getCardEntryMethods cardEntryMethods}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class ReadCardDataRequest extends com.clover.sdk.v3.remotepay.BaseRequest {

  /**
   * If true, then if the card is swiped, a pin entry must be done.
   */
  public java.lang.Boolean getIsForceSwipePinEntry() {
    return genClient.cacheGet(CacheKey.isForceSwipePinEntry);
  }

  /**
   * The bit mask of allowable card read types.
   */
  public java.lang.Integer getCardEntryMethods() {
    return genClient.cacheGet(CacheKey.cardEntryMethods);
  }

  public java.lang.String getReadCardMode() {
    return genClient.cacheGet(CacheKey.readCardMode);
  }

  /**
   * Identifier for the request
   */
  @Override
  public java.lang.String getRequestId() {
    return genClient.cacheGet(CacheKey.requestId);
  }

  /**
   * Identifier for the version
   */
  @Override
  public java.lang.Integer getVersion() {
    return genClient.cacheGet(CacheKey.version);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    isForceSwipePinEntry
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Boolean.class)),
    cardEntryMethods
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Integer.class)),
    requestId
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    readCardMode
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    version
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Integer.class)),
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

  private final GenericClient<ReadCardDataRequest> genClient;

  /**
   * Constructs a new empty instance.
   */
  public ReadCardDataRequest() {
    super(false);
    genClient = new GenericClient<ReadCardDataRequest>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected ReadCardDataRequest(boolean noInit) {
    super(false);
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public ReadCardDataRequest(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public ReadCardDataRequest(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public ReadCardDataRequest(ReadCardDataRequest src) {
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
    genClient.validateCloverId(CacheKey.requestId, getRequestId());
  }

  /** Checks whether the 'isForceSwipePinEntry' field is set and is not null */
  public boolean isNotNullIsForceSwipePinEntry() {
    return genClient.cacheValueIsNotNull(CacheKey.isForceSwipePinEntry);
  }

  /** Checks whether the 'cardEntryMethods' field is set and is not null */
  public boolean isNotNullCardEntryMethods() {
    return genClient.cacheValueIsNotNull(CacheKey.cardEntryMethods);
  }

  /** Checks whether the 'requestId' field is set and is not null */
  @Override
  public boolean isNotNullRequestId() {
    return genClient.cacheValueIsNotNull(CacheKey.requestId);
  }

  /** Checks whether the 'version' field is set and is not null */
  @Override
  public boolean isNotNullVersion() {
    return genClient.cacheValueIsNotNull(CacheKey.version);
  }

  public boolean isNotNullReadCardMode() {
    return genClient.cacheValueIsNotNull(CacheKey.readCardMode);
  }



  /** Checks whether the 'isForceSwipePinEntry' field has been set, however the value could be null */
  public boolean hasIsForceSwipePinEntry() {
    return genClient.cacheHasKey(CacheKey.isForceSwipePinEntry);
  }

  /** Checks whether the 'cardEntryMethods' field has been set, however the value could be null */
  public boolean hasCardEntryMethods() {
    return genClient.cacheHasKey(CacheKey.cardEntryMethods);
  }

  /** Checks whether the 'requestId' field has been set, however the value could be null */
  @Override
  public boolean hasRequestId() {
    return genClient.cacheHasKey(CacheKey.requestId);
  }

  /** Checks whether the 'version' field has been set, however the value could be null */
  @Override
  public boolean hasVersion() {
    return genClient.cacheHasKey(CacheKey.version);
  }

  public boolean hasReadCardMode() {
    return genClient.cacheHasKey(CacheKey.readCardMode);
  }

  /**
   * Sets the field 'isForceSwipePinEntry'.
   */
  public ReadCardDataRequest setIsForceSwipePinEntry(java.lang.Boolean isForceSwipePinEntry) {
    return genClient.setOther(isForceSwipePinEntry, CacheKey.isForceSwipePinEntry);
  }

  /**
   * Sets the field 'cardEntryMethods'.
   */
  public ReadCardDataRequest setCardEntryMethods(java.lang.Integer cardEntryMethods) {
    return genClient.setOther(cardEntryMethods, CacheKey.cardEntryMethods);
  }

  /**
   * Sets the field 'requestId'.
   */
  @Override
  public BaseRequest setRequestId(java.lang.String requestId) {
    return genClient.setOther(requestId, CacheKey.requestId);
  }

  /**
   * Sets the field 'version'.
   */
  @Override
  public BaseRequest setVersion(java.lang.Integer version) {
    return genClient.setOther(version, CacheKey.version);
  }

  public ReadCardDataRequest setReadCardMode(java.lang.String readCardMode) {
    return genClient.setOther(readCardMode, CacheKey.readCardMode);
  }


  /** Clears the 'isForceSwipePinEntry' field, the 'has' method for this field will now return false */
  public void clearIsForceSwipePinEntry() {
    genClient.clear(CacheKey.isForceSwipePinEntry);
  }
  /** Clears the 'cardEntryMethods' field, the 'has' method for this field will now return false */
  public void clearCardEntryMethods() {
    genClient.clear(CacheKey.cardEntryMethods);
  }
  /** Clears the 'requestId' field, the 'has' method for this field will now return false */
  @Override
  public void clearRequestId() {
    genClient.clear(CacheKey.requestId);
  }
  /** Clears the 'version' field, the 'has' method for this field will now return false */
  @Override
  public void clearVersion() {
    genClient.clear(CacheKey.version);
  }

  public void clearReadCardMode() {
    genClient.clear(CacheKey.readCardMode);
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
  public ReadCardDataRequest copyChanges() {
    ReadCardDataRequest copy = new ReadCardDataRequest();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(ReadCardDataRequest src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new ReadCardDataRequest(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<ReadCardDataRequest> CREATOR = new android.os.Parcelable.Creator<ReadCardDataRequest>() {
    @Override
    public ReadCardDataRequest createFromParcel(android.os.Parcel in) {
      ReadCardDataRequest instance = new ReadCardDataRequest(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public ReadCardDataRequest[] newArray(int size) {
      return new ReadCardDataRequest[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<ReadCardDataRequest> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<ReadCardDataRequest>() {
    public Class<ReadCardDataRequest> getCreatedClass() {
      return ReadCardDataRequest.class;
    }

    @Override
    public ReadCardDataRequest create(org.json.JSONObject jsonObject) {
      return new ReadCardDataRequest(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean ISFORCESWIPEPINENTRY_IS_REQUIRED = false;
    public static final boolean CARDENTRYMETHODS_IS_REQUIRED = false;
    public static final boolean REQUESTID_IS_REQUIRED = false;
    public static final long REQUESTID_MAX_LEN = 13;
    public static final boolean VERSION_IS_REQUIRED = false;
    public static final boolean READ_CARD_MODE_REQUIRED = false;
  }

}
