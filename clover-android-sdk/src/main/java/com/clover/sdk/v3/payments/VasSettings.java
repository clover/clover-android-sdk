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

package com.clover.sdk.v3.payments;

import com.clover.sdk.GenericClient;
import com.clover.sdk.GenericParcelable;

/**
 * This is an auto-generated Clover data object.
 * <p>
 * <h3>Fields</h3>
 * <ul>
 * <li>{@link #getVasMode vasMode}</li>
 * <li>{@link #getServiceTypes serviceTypes}</li>
 * <li>{@link #getExtras extras}</li>
 * <li>{@link #getPushMode pushMode}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class VasSettings extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  /**
   * Populated per tx.  If not passed PAY_ONLY is the default behavior
   */
  public com.clover.sdk.v3.payments.VasMode getVasMode() {
    return genClient.cacheGet(CacheKey.vasMode);
  }

  /**
   * Vas service types of interest for this txn
   */
  public java.util.List<com.clover.sdk.v3.payments.VasDataType> getServiceTypes() {
    return genClient.cacheGet(CacheKey.serviceTypes);
  }

  /**
   * Additional context relevant extras such as EXTRA_ORDER_ID
   */
  public java.util.Map<java.lang.String,java.lang.String> getExtras() {
    return genClient.cacheGet(CacheKey.extras);
  }

  /**
   * Indicates if vas is push url only, no push, or push in addition to GET
   */
  public com.clover.sdk.v3.payments.VasPushMode getPushMode() {
    return genClient.cacheGet(CacheKey.pushMode);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    vasMode
        (com.clover.sdk.extractors.EnumExtractionStrategy.instance(com.clover.sdk.v3.payments.VasMode.class)),
    serviceTypes
        (com.clover.sdk.extractors.RecordListExtractionStrategy.instance(com.clover.sdk.v3.payments.VasDataType.JSON_CREATOR)),
    extras
        (com.clover.sdk.extractors.MapExtractionStrategy.instance()),
    pushMode
        (com.clover.sdk.extractors.EnumExtractionStrategy.instance(com.clover.sdk.v3.payments.VasPushMode.class)),
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

  private final GenericClient<VasSettings> genClient;

  /**
   * Constructs a new empty instance.
   */
  public VasSettings() {
    genClient = new GenericClient<VasSettings>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected VasSettings(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public VasSettings(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public VasSettings(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public VasSettings(VasSettings src) {
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

  /** Checks whether the 'vasMode' field is set and is not null */
  public boolean isNotNullVasMode() {
    return genClient.cacheValueIsNotNull(CacheKey.vasMode);
  }

  /** Checks whether the 'serviceTypes' field is set and is not null */
  public boolean isNotNullServiceTypes() {
    return genClient.cacheValueIsNotNull(CacheKey.serviceTypes);
  }

  /** Checks whether the 'serviceTypes' field is set and is not null and is not empty */
  public boolean isNotEmptyServiceTypes() { return isNotNullServiceTypes() && !getServiceTypes().isEmpty(); }

  /** Checks whether the 'extras' field is set and is not null */
  public boolean isNotNullExtras() {
    return genClient.cacheValueIsNotNull(CacheKey.extras);
  }

  /** Checks whether the 'extras' field is set and is not null and is not empty */
  public boolean isNotEmptyExtras() { return isNotNullExtras() && !getExtras().isEmpty(); }

  /** Checks whether the 'pushMode' field is set and is not null */
  public boolean isNotNullPushMode() {
    return genClient.cacheValueIsNotNull(CacheKey.pushMode);
  }



  /** Checks whether the 'vasMode' field has been set, however the value could be null */
  public boolean hasVasMode() {
    return genClient.cacheHasKey(CacheKey.vasMode);
  }

  /** Checks whether the 'serviceTypes' field has been set, however the value could be null */
  public boolean hasServiceTypes() {
    return genClient.cacheHasKey(CacheKey.serviceTypes);
  }

  /** Checks whether the 'extras' field has been set, however the value could be null */
  public boolean hasExtras() {
    return genClient.cacheHasKey(CacheKey.extras);
  }

  /** Checks whether the 'pushMode' field has been set, however the value could be null */
  public boolean hasPushMode() {
    return genClient.cacheHasKey(CacheKey.pushMode);
  }


  /**
   * Sets the field 'vasMode'.
   */
  public VasSettings setVasMode(com.clover.sdk.v3.payments.VasMode vasMode) {
    return genClient.setOther(vasMode, CacheKey.vasMode);
  }

  /**
   * Sets the field 'serviceTypes'.
   *
   * Nulls in the given List are skipped. List parameter is copied, so it will not reflect any changes, but objects inside it will.
   */
  public VasSettings setServiceTypes(java.util.List<com.clover.sdk.v3.payments.VasDataType> serviceTypes) {
    return genClient.setArrayRecord(serviceTypes, CacheKey.serviceTypes);
  }

  /**
   * Sets the field 'extras'.
   */
  public VasSettings setExtras(java.util.Map<java.lang.String,java.lang.String> extras) {
    return genClient.setOther(extras, CacheKey.extras);
  }

  /**
   * Sets the field 'pushMode'.
   */
  public VasSettings setPushMode(com.clover.sdk.v3.payments.VasPushMode pushMode) {
    return genClient.setOther(pushMode, CacheKey.pushMode);
  }


  /** Clears the 'vasMode' field, the 'has' method for this field will now return false */
  public void clearVasMode() {
    genClient.clear(CacheKey.vasMode);
  }
  /** Clears the 'serviceTypes' field, the 'has' method for this field will now return false */
  public void clearServiceTypes() {
    genClient.clear(CacheKey.serviceTypes);
  }
  /** Clears the 'extras' field, the 'has' method for this field will now return false */
  public void clearExtras() {
    genClient.clear(CacheKey.extras);
  }
  /** Clears the 'pushMode' field, the 'has' method for this field will now return false */
  public void clearPushMode() {
    genClient.clear(CacheKey.pushMode);
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
  public VasSettings copyChanges() {
    VasSettings copy = new VasSettings();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(VasSettings src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new VasSettings(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<VasSettings> CREATOR = new android.os.Parcelable.Creator<VasSettings>() {
    @Override
    public VasSettings createFromParcel(android.os.Parcel in) {
      VasSettings instance = new VasSettings(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public VasSettings[] newArray(int size) {
      return new VasSettings[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<VasSettings> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<VasSettings>() {
    public Class<VasSettings> getCreatedClass() {
      return VasSettings.class;
    }

    @Override
    public VasSettings create(org.json.JSONObject jsonObject) {
      return new VasSettings(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean VASMODE_IS_REQUIRED = false;
    public static final boolean SERVICETYPES_IS_REQUIRED = false;
    public static final boolean EXTRAS_IS_REQUIRED = false;
    public static final boolean PUSHMODE_IS_REQUIRED = false;
  }

}
