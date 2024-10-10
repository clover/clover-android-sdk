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

package com.clover.sdk.cfp.connector.session;

import com.clover.sdk.GenericClient;
import com.clover.sdk.GenericParcelable;

/**
 * This is an auto-generated Clover data object.
 * <p>
 * <h3>Fields</h3>
 * <ul>
 * <li>{@link #getMessage message}</li>
 * <li>{@link #getBeep beep}</li>
 * <li>{@link #getDuration duration}</li>
 * <li>{@link #getMessageId messageId}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class CFPMessage extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  public java.lang.String getMessage() {
    return genClient.cacheGet(CacheKey.message);
  }

  public java.lang.Boolean getBeep() {
    return genClient.cacheGet(CacheKey.beep);
  }

  public java.lang.Integer getDuration() {
    return genClient.cacheGet(CacheKey.duration);
  }

  public java.lang.Integer getMessageId() {
    return genClient.cacheGet(CacheKey.messageId);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    message
      (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    beep
      (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Boolean.class)),
    duration
      (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Integer.class)),
    messageId
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

  private final GenericClient<CFPMessage> genClient;

  /**
   * Constructs a new empty instance.
   */
  public CFPMessage() {
    genClient = new GenericClient<CFPMessage>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected CFPMessage(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public CFPMessage(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public CFPMessage(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public CFPMessage(CFPMessage src) {
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

  /** Checks whether the 'message' field is set and is not null */
  public boolean isNotNullMessage() {
    return genClient.cacheValueIsNotNull(CacheKey.message);
  }

  /** Checks whether the 'beep' field is set and is not null */
  public boolean isNotNullBeep() {
    return genClient.cacheValueIsNotNull(CacheKey.beep);
  }

  /** Checks whether the 'duration' field is set and is not null */
  public boolean isNotNullDuration() {
    return genClient.cacheValueIsNotNull(CacheKey.duration);
  }

  /** Checks whether the 'messageId' field is set and is not null */
  public boolean isNotNullMessageId() {
    return genClient.cacheValueIsNotNull(CacheKey.messageId);
  }



  /** Checks whether the 'message' field has been set, however the value could be null */
  public boolean hasMessage() {
    return genClient.cacheHasKey(CacheKey.message);
  }

  /** Checks whether the 'beep' field has been set, however the value could be null */
  public boolean hasBeep() {
    return genClient.cacheHasKey(CacheKey.beep);
  }

  /** Checks whether the 'duration' field has been set, however the value could be null */
  public boolean hasDuration() {
    return genClient.cacheHasKey(CacheKey.duration);
  }

  /** Checks whether the 'messageId' field has been set, however the value could be null */
  public boolean hasMessageId() {
    return genClient.cacheHasKey(CacheKey.messageId);
  }


  /**
   * Sets the field 'message'.
   */
  public CFPMessage setMessage(java.lang.String message) {
    return genClient.setOther(message, CacheKey.message);
  }

  /**
   * Sets the field 'beep'.
   */
  public CFPMessage setBeep(java.lang.Boolean beep) {
    return genClient.setOther(beep, CacheKey.beep);
  }

  /**
   * Sets the field 'duration'.
   */
  public CFPMessage setDuration(java.lang.Integer duration) {
    return genClient.setOther(duration, CacheKey.duration);
  }

  /**
   * Sets the field 'messageId'.
   */
  public CFPMessage setMessageId(java.lang.Integer messageId) {
    return genClient.setOther(messageId, CacheKey.messageId);
  }


  /** Clears the 'message' field, the 'has' method for this field will now return false */
  public void clearMessage() {
    genClient.clear(CacheKey.message);
  }
  /** Clears the 'beep' field, the 'has' method for this field will now return false */
  public void clearBeep() {
    genClient.clear(CacheKey.beep);
  }
  /** Clears the 'duration' field, the 'has' method for this field will now return false */
  public void clearDuration() {
    genClient.clear(CacheKey.duration);
  }
  /** Clears the 'messageId' field, the 'has' method for this field will now return false */
  public void clearMessageId() {
    genClient.clear(CacheKey.messageId);
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
  public CFPMessage copyChanges() {
    CFPMessage copy = new CFPMessage();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(CFPMessage src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new CFPMessage(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<CFPMessage> CREATOR = new android.os.Parcelable.Creator<CFPMessage>() {
    @Override
    public CFPMessage createFromParcel(android.os.Parcel in) {
      CFPMessage instance = new CFPMessage(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public CFPMessage[] newArray(int size) {
      return new CFPMessage[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<CFPMessage> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<CFPMessage>() {
    public Class<CFPMessage> getCreatedClass() {
      return CFPMessage.class;
    }

    @Override
    public CFPMessage create(org.json.JSONObject jsonObject) {
      return new CFPMessage(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean MESSAGE_IS_REQUIRED = false;
    public static final boolean BEEP_IS_REQUIRED = false;
    public static final boolean DURATION_IS_REQUIRED = false;
    public static final boolean MESSAGEID_IS_REQUIRED = false;
  }

}
