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
 * <li>{@link #getDisclaimerText disclaimerText}</li>
 * <li>{@link #getDisclaimerValues disclaimerValues}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class SignatureDisclaimer extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  public java.lang.String getDisclaimerText() {
    return genClient.cacheGet(CacheKey.disclaimerText);
  }

  /**
   * Values that will be substituted in standard disclaimer text (txn date/time, account number, product label, etc.
   */
  public java.util.Map<java.lang.String,java.lang.String> getDisclaimerValues() {
    return genClient.cacheGet(CacheKey.disclaimerValues);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    disclaimerText
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    disclaimerValues
        (com.clover.sdk.extractors.MapExtractionStrategy.instance()),
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

  private final GenericClient<SignatureDisclaimer> genClient;

  /**
   * Constructs a new empty instance.
   */
  public SignatureDisclaimer() {
    genClient = new GenericClient<SignatureDisclaimer>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected SignatureDisclaimer(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public SignatureDisclaimer(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public SignatureDisclaimer(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public SignatureDisclaimer(SignatureDisclaimer src) {
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

  /** Checks whether the 'disclaimerText' field is set and is not null */
  public boolean isNotNullDisclaimerText() {
    return genClient.cacheValueIsNotNull(CacheKey.disclaimerText);
  }

  /** Checks whether the 'disclaimerValues' field is set and is not null */
  public boolean isNotNullDisclaimerValues() {
    return genClient.cacheValueIsNotNull(CacheKey.disclaimerValues);
  }

  /** Checks whether the 'disclaimerValues' field is set and is not null and is not empty */
  public boolean isNotEmptyDisclaimerValues() { return isNotNullDisclaimerValues() && !getDisclaimerValues().isEmpty(); }



  /** Checks whether the 'disclaimerText' field has been set, however the value could be null */
  public boolean hasDisclaimerText() {
    return genClient.cacheHasKey(CacheKey.disclaimerText);
  }

  /** Checks whether the 'disclaimerValues' field has been set, however the value could be null */
  public boolean hasDisclaimerValues() {
    return genClient.cacheHasKey(CacheKey.disclaimerValues);
  }


  /**
   * Sets the field 'disclaimerText'.
   */
  public SignatureDisclaimer setDisclaimerText(java.lang.String disclaimerText) {
    return genClient.setOther(disclaimerText, CacheKey.disclaimerText);
  }

  /**
   * Sets the field 'disclaimerValues'.
   */
  public SignatureDisclaimer setDisclaimerValues(java.util.Map<java.lang.String,java.lang.String> disclaimerValues) {
    return genClient.setOther(disclaimerValues, CacheKey.disclaimerValues);
  }


  /** Clears the 'disclaimerText' field, the 'has' method for this field will now return false */
  public void clearDisclaimerText() {
    genClient.clear(CacheKey.disclaimerText);
  }
  /** Clears the 'disclaimerValues' field, the 'has' method for this field will now return false */
  public void clearDisclaimerValues() {
    genClient.clear(CacheKey.disclaimerValues);
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
  public SignatureDisclaimer copyChanges() {
    SignatureDisclaimer copy = new SignatureDisclaimer();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(SignatureDisclaimer src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new SignatureDisclaimer(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<SignatureDisclaimer> CREATOR = new android.os.Parcelable.Creator<SignatureDisclaimer>() {
    @Override
    public SignatureDisclaimer createFromParcel(android.os.Parcel in) {
      SignatureDisclaimer instance = new SignatureDisclaimer(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public SignatureDisclaimer[] newArray(int size) {
      return new SignatureDisclaimer[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<SignatureDisclaimer> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<SignatureDisclaimer>() {
    public Class<SignatureDisclaimer> getCreatedClass() {
      return SignatureDisclaimer.class;
    }

    @Override
    public SignatureDisclaimer create(org.json.JSONObject jsonObject) {
      return new SignatureDisclaimer(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean DISCLAIMERTEXT_IS_REQUIRED = false;
    public static final boolean DISCLAIMERVALUES_IS_REQUIRED = false;
  }

}
