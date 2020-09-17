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
 * <h3>Fields</h3>
 * <ul>
 * <li>{@link #getName name}</li>
 * <li>{@link #getLocale locale}</li>
 * <li>{@link #getSmall small}</li>
 * <li>{@link #getMedium medium}</li>
 * <li>{@link #getLarge large}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class Screenshot extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  /**
   * URL for the app screenshot
   */
  public java.lang.String getName() {
    return genClient.cacheGet(CacheKey.name);
  }

  /**
   * https://docs.oracle.com/javase/7/docs/api/java/util/Locale.html ISO 639-1 ISO_3166-1_alpha-2 Examples: en_US, de_DE, en_CA, fr_CA
   */
  public java.lang.String getLocale() {
    return genClient.cacheGet(CacheKey.locale);
  }

  /**
   * URL for the small version (80 x 80) of the app screenshot
   */
  public java.lang.String getSmall() {
    return genClient.cacheGet(CacheKey.small);
  }

  /**
   * URL for the medium version (200 x 200) of the app screenshot
   */
  public java.lang.String getMedium() {
    return genClient.cacheGet(CacheKey.medium);
  }

  /**
   * URL for the large version (800 x 800) of the app screenshot
   */
  public java.lang.String getLarge() {
    return genClient.cacheGet(CacheKey.large);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    name
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    locale
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    small
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    medium
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    large
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

  private final GenericClient<Screenshot> genClient;

  /**
   * Constructs a new empty instance.
   */
  public Screenshot() {
    genClient = new GenericClient<Screenshot>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected Screenshot(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public Screenshot(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public Screenshot(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public Screenshot(Screenshot src) {
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
    genClient.validateLength(CacheKey.name, getName(), 255);

    genClient.validateLength(CacheKey.locale, getLocale(), 5);

    genClient.validateLength(CacheKey.small, getSmall(), 255);

    genClient.validateLength(CacheKey.medium, getMedium(), 255);

    genClient.validateLength(CacheKey.large, getLarge(), 255);
  }

  /** Checks whether the 'name' field is set and is not null */
  public boolean isNotNullName() {
    return genClient.cacheValueIsNotNull(CacheKey.name);
  }

  /** Checks whether the 'locale' field is set and is not null */
  public boolean isNotNullLocale() {
    return genClient.cacheValueIsNotNull(CacheKey.locale);
  }

  /** Checks whether the 'small' field is set and is not null */
  public boolean isNotNullSmall() {
    return genClient.cacheValueIsNotNull(CacheKey.small);
  }

  /** Checks whether the 'medium' field is set and is not null */
  public boolean isNotNullMedium() {
    return genClient.cacheValueIsNotNull(CacheKey.medium);
  }

  /** Checks whether the 'large' field is set and is not null */
  public boolean isNotNullLarge() {
    return genClient.cacheValueIsNotNull(CacheKey.large);
  }



  /** Checks whether the 'name' field has been set, however the value could be null */
  public boolean hasName() {
    return genClient.cacheHasKey(CacheKey.name);
  }

  /** Checks whether the 'locale' field has been set, however the value could be null */
  public boolean hasLocale() {
    return genClient.cacheHasKey(CacheKey.locale);
  }

  /** Checks whether the 'small' field has been set, however the value could be null */
  public boolean hasSmall() {
    return genClient.cacheHasKey(CacheKey.small);
  }

  /** Checks whether the 'medium' field has been set, however the value could be null */
  public boolean hasMedium() {
    return genClient.cacheHasKey(CacheKey.medium);
  }

  /** Checks whether the 'large' field has been set, however the value could be null */
  public boolean hasLarge() {
    return genClient.cacheHasKey(CacheKey.large);
  }


  /**
   * Sets the field 'name'.
   */
  public Screenshot setName(java.lang.String name) {
    return genClient.setOther(name, CacheKey.name);
  }

  /**
   * Sets the field 'locale'.
   */
  public Screenshot setLocale(java.lang.String locale) {
    return genClient.setOther(locale, CacheKey.locale);
  }

  /**
   * Sets the field 'small'.
   */
  public Screenshot setSmall(java.lang.String small) {
    return genClient.setOther(small, CacheKey.small);
  }

  /**
   * Sets the field 'medium'.
   */
  public Screenshot setMedium(java.lang.String medium) {
    return genClient.setOther(medium, CacheKey.medium);
  }

  /**
   * Sets the field 'large'.
   */
  public Screenshot setLarge(java.lang.String large) {
    return genClient.setOther(large, CacheKey.large);
  }


  /** Clears the 'name' field, the 'has' method for this field will now return false */
  public void clearName() {
    genClient.clear(CacheKey.name);
  }
  /** Clears the 'locale' field, the 'has' method for this field will now return false */
  public void clearLocale() {
    genClient.clear(CacheKey.locale);
  }
  /** Clears the 'small' field, the 'has' method for this field will now return false */
  public void clearSmall() {
    genClient.clear(CacheKey.small);
  }
  /** Clears the 'medium' field, the 'has' method for this field will now return false */
  public void clearMedium() {
    genClient.clear(CacheKey.medium);
  }
  /** Clears the 'large' field, the 'has' method for this field will now return false */
  public void clearLarge() {
    genClient.clear(CacheKey.large);
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
  public Screenshot copyChanges() {
    Screenshot copy = new Screenshot();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(Screenshot src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new Screenshot(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<Screenshot> CREATOR = new android.os.Parcelable.Creator<Screenshot>() {
    @Override
    public Screenshot createFromParcel(android.os.Parcel in) {
      Screenshot instance = new Screenshot(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public Screenshot[] newArray(int size) {
      return new Screenshot[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<Screenshot> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<Screenshot>() {
    public Class<Screenshot> getCreatedClass() {
      return Screenshot.class;
    }

    @Override
    public Screenshot create(org.json.JSONObject jsonObject) {
      return new Screenshot(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean NAME_IS_REQUIRED = false;
    public static final long NAME_MAX_LEN = 255;
    public static final boolean LOCALE_IS_REQUIRED = false;
    public static final long LOCALE_MAX_LEN = 5;
    public static final boolean SMALL_IS_REQUIRED = false;
    public static final long SMALL_MAX_LEN = 255;
    public static final boolean MEDIUM_IS_REQUIRED = false;
    public static final long MEDIUM_MAX_LEN = 255;
    public static final boolean LARGE_IS_REQUIRED = false;
    public static final long LARGE_MAX_LEN = 255;
  }

}
