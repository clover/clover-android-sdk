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

package com.clover.sdk.v3.apps;

import com.clover.sdk.GenericClient;
import com.clover.sdk.GenericParcelable;

/**
 * This is an auto-generated Clover data object.
 * <p>
 * Used to track the origin of a distributed call.
 * <p>
 * <h3>Fields</h3>
 * <ul>
 * <li>{@link #getDeveloperAppId developerAppId}</li>
 * <li>{@link #getApplicationName applicationName}</li>
 * <li>{@link #getApplicationID applicationID}</li>
 * <li>{@link #getApplicationVersion applicationVersion}</li>
 * <li>{@link #getSourceSDK sourceSDK}</li>
 * <li>{@link #getSourceSDKVersion sourceSDKVersion}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class AppTracking extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  /**
   * The uuid from the developer application.  This is typically populated and used only on the back end.
   */
  public java.lang.String getDeveloperAppId() {
    return genClient.cacheGet(CacheKey.developerAppId);
  }

  /**
   * The name of the developer application.
   */
  public java.lang.String getApplicationName() {
    return genClient.cacheGet(CacheKey.applicationName);
  }

  /**
   * A string representing an application
   */
  public java.lang.String getApplicationID() {
    return genClient.cacheGet(CacheKey.applicationID);
  }

  /**
   * A string representing a semanticversion.  See http://semver.org/
   */
  public java.lang.String getApplicationVersion() {
    return genClient.cacheGet(CacheKey.applicationVersion);
  }

  /**
   * A string representing a SDK
   */
  public java.lang.String getSourceSDK() {
    return genClient.cacheGet(CacheKey.sourceSDK);
  }

  /**
   * A string representing a semanticversion.  See http://semver.org/
   */
  public java.lang.String getSourceSDKVersion() {
    return genClient.cacheGet(CacheKey.sourceSDKVersion);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    developerAppId
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    applicationName
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    applicationID
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    applicationVersion
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    sourceSDK
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    sourceSDKVersion
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

  private final GenericClient<AppTracking> genClient;

  /**
   * Constructs a new empty instance.
   */
  public AppTracking() {
    genClient = new GenericClient<AppTracking>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected AppTracking(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public AppTracking(String json) throws IllegalArgumentException {
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
  public AppTracking(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public AppTracking(AppTracking src) {
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

  /** Checks whether the 'developerAppId' field is set and is not null */
  public boolean isNotNullDeveloperAppId() {
    return genClient.cacheValueIsNotNull(CacheKey.developerAppId);
  }

  /** Checks whether the 'applicationName' field is set and is not null */
  public boolean isNotNullApplicationName() {
    return genClient.cacheValueIsNotNull(CacheKey.applicationName);
  }

  /** Checks whether the 'applicationID' field is set and is not null */
  public boolean isNotNullApplicationID() {
    return genClient.cacheValueIsNotNull(CacheKey.applicationID);
  }

  /** Checks whether the 'applicationVersion' field is set and is not null */
  public boolean isNotNullApplicationVersion() {
    return genClient.cacheValueIsNotNull(CacheKey.applicationVersion);
  }

  /** Checks whether the 'sourceSDK' field is set and is not null */
  public boolean isNotNullSourceSDK() {
    return genClient.cacheValueIsNotNull(CacheKey.sourceSDK);
  }

  /** Checks whether the 'sourceSDKVersion' field is set and is not null */
  public boolean isNotNullSourceSDKVersion() {
    return genClient.cacheValueIsNotNull(CacheKey.sourceSDKVersion);
  }



  /** Checks whether the 'developerAppId' field has been set, however the value could be null */
  public boolean hasDeveloperAppId() {
    return genClient.cacheHasKey(CacheKey.developerAppId);
  }

  /** Checks whether the 'applicationName' field has been set, however the value could be null */
  public boolean hasApplicationName() {
    return genClient.cacheHasKey(CacheKey.applicationName);
  }

  /** Checks whether the 'applicationID' field has been set, however the value could be null */
  public boolean hasApplicationID() {
    return genClient.cacheHasKey(CacheKey.applicationID);
  }

  /** Checks whether the 'applicationVersion' field has been set, however the value could be null */
  public boolean hasApplicationVersion() {
    return genClient.cacheHasKey(CacheKey.applicationVersion);
  }

  /** Checks whether the 'sourceSDK' field has been set, however the value could be null */
  public boolean hasSourceSDK() {
    return genClient.cacheHasKey(CacheKey.sourceSDK);
  }

  /** Checks whether the 'sourceSDKVersion' field has been set, however the value could be null */
  public boolean hasSourceSDKVersion() {
    return genClient.cacheHasKey(CacheKey.sourceSDKVersion);
  }


  /**
   * Sets the field 'developerAppId'.
   */
  public AppTracking setDeveloperAppId(java.lang.String developerAppId) {
    return genClient.setOther(developerAppId, CacheKey.developerAppId);
  }

  /**
   * Sets the field 'applicationName'.
   */
  public AppTracking setApplicationName(java.lang.String applicationName) {
    return genClient.setOther(applicationName, CacheKey.applicationName);
  }

  /**
   * Sets the field 'applicationID'.
   */
  public AppTracking setApplicationID(java.lang.String applicationID) {
    return genClient.setOther(applicationID, CacheKey.applicationID);
  }

  /**
   * Sets the field 'applicationVersion'.
   */
  public AppTracking setApplicationVersion(java.lang.String applicationVersion) {
    return genClient.setOther(applicationVersion, CacheKey.applicationVersion);
  }

  /**
   * Sets the field 'sourceSDK'.
   */
  public AppTracking setSourceSDK(java.lang.String sourceSDK) {
    return genClient.setOther(sourceSDK, CacheKey.sourceSDK);
  }

  /**
   * Sets the field 'sourceSDKVersion'.
   */
  public AppTracking setSourceSDKVersion(java.lang.String sourceSDKVersion) {
    return genClient.setOther(sourceSDKVersion, CacheKey.sourceSDKVersion);
  }


  /** Clears the 'developerAppId' field, the 'has' method for this field will now return false */
  public void clearDeveloperAppId() {
    genClient.clear(CacheKey.developerAppId);
  }
  /** Clears the 'applicationName' field, the 'has' method for this field will now return false */
  public void clearApplicationName() {
    genClient.clear(CacheKey.applicationName);
  }
  /** Clears the 'applicationID' field, the 'has' method for this field will now return false */
  public void clearApplicationID() {
    genClient.clear(CacheKey.applicationID);
  }
  /** Clears the 'applicationVersion' field, the 'has' method for this field will now return false */
  public void clearApplicationVersion() {
    genClient.clear(CacheKey.applicationVersion);
  }
  /** Clears the 'sourceSDK' field, the 'has' method for this field will now return false */
  public void clearSourceSDK() {
    genClient.clear(CacheKey.sourceSDK);
  }
  /** Clears the 'sourceSDKVersion' field, the 'has' method for this field will now return false */
  public void clearSourceSDKVersion() {
    genClient.clear(CacheKey.sourceSDKVersion);
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
  public AppTracking copyChanges() {
    AppTracking copy = new AppTracking();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(AppTracking src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new AppTracking(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<AppTracking> CREATOR = new android.os.Parcelable.Creator<AppTracking>() {
    @Override
    public AppTracking createFromParcel(android.os.Parcel in) {
      AppTracking instance = new AppTracking(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public AppTracking[] newArray(int size) {
      return new AppTracking[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<AppTracking> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<AppTracking>() {
    @Override
    public AppTracking create(org.json.JSONObject jsonObject) {
      return new AppTracking(jsonObject);
    }
  };

  public interface Constraints {

    public static final boolean DEVELOPERAPPID_IS_REQUIRED = false;
    public static final boolean APPLICATIONNAME_IS_REQUIRED = false;
    public static final boolean APPLICATIONID_IS_REQUIRED = false;
    public static final boolean APPLICATIONVERSION_IS_REQUIRED = false;
    public static final boolean SOURCESDK_IS_REQUIRED = false;
    public static final boolean SOURCESDKVERSION_IS_REQUIRED = false;

  }

}
