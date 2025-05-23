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
 * <li>{@link #getApproved approved}</li>
 * <li>{@link #getEmvData emvData}</li>
 * <li>{@link #getIssuerScriptResults issuerScriptResults}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class FinishChipResponse extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  public java.lang.Boolean getApproved() {
    return genClient.cacheGet(CacheKey.approved);
  }

  public java.lang.String getEmvData() {
    return genClient.cacheGet(CacheKey.emvData);
  }

  public java.lang.String getIssuerScriptResults() {
    return genClient.cacheGet(CacheKey.issuerScriptResults);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    approved
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Boolean.class)),
    emvData
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    issuerScriptResults
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

  private final GenericClient<FinishChipResponse> genClient;

  /**
   * Constructs a new empty instance.
   */
  public FinishChipResponse() {
    genClient = new GenericClient<FinishChipResponse>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected FinishChipResponse(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public FinishChipResponse(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public FinishChipResponse(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public FinishChipResponse(FinishChipResponse src) {
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

  /** Checks whether the 'approved' field is set and is not null */
  public boolean isNotNullApproved() {
    return genClient.cacheValueIsNotNull(CacheKey.approved);
  }

  /** Checks whether the 'emvData' field is set and is not null */
  public boolean isNotNullEmvData() {
    return genClient.cacheValueIsNotNull(CacheKey.emvData);
  }

  /** Checks whether the 'issuerScriptResults' field is set and is not null */
  public boolean isNotNullIssuerScriptResults() {
    return genClient.cacheValueIsNotNull(CacheKey.issuerScriptResults);
  }



  /** Checks whether the 'approved' field has been set, however the value could be null */
  public boolean hasApproved() {
    return genClient.cacheHasKey(CacheKey.approved);
  }

  /** Checks whether the 'emvData' field has been set, however the value could be null */
  public boolean hasEmvData() {
    return genClient.cacheHasKey(CacheKey.emvData);
  }

  /** Checks whether the 'issuerScriptResults' field has been set, however the value could be null */
  public boolean hasIssuerScriptResults() {
    return genClient.cacheHasKey(CacheKey.issuerScriptResults);
  }


  /**
   * Sets the field 'approved'.
   */
  public FinishChipResponse setApproved(java.lang.Boolean approved) {
    return genClient.setOther(approved, CacheKey.approved);
  }

  /**
   * Sets the field 'emvData'.
   */
  public FinishChipResponse setEmvData(java.lang.String emvData) {
    return genClient.setOther(emvData, CacheKey.emvData);
  }

  /**
   * Sets the field 'issuerScriptResults'.
   */
  public FinishChipResponse setIssuerScriptResults(java.lang.String issuerScriptResults) {
    return genClient.setOther(issuerScriptResults, CacheKey.issuerScriptResults);
  }


  /** Clears the 'approved' field, the 'has' method for this field will now return false */
  public void clearApproved() {
    genClient.clear(CacheKey.approved);
  }
  /** Clears the 'emvData' field, the 'has' method for this field will now return false */
  public void clearEmvData() {
    genClient.clear(CacheKey.emvData);
  }
  /** Clears the 'issuerScriptResults' field, the 'has' method for this field will now return false */
  public void clearIssuerScriptResults() {
    genClient.clear(CacheKey.issuerScriptResults);
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
  public FinishChipResponse copyChanges() {
    FinishChipResponse copy = new FinishChipResponse();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(FinishChipResponse src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new FinishChipResponse(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<FinishChipResponse> CREATOR = new android.os.Parcelable.Creator<FinishChipResponse>() {
    @Override
    public FinishChipResponse createFromParcel(android.os.Parcel in) {
      FinishChipResponse instance = new FinishChipResponse(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public FinishChipResponse[] newArray(int size) {
      return new FinishChipResponse[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<FinishChipResponse> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<FinishChipResponse>() {
    public Class<FinishChipResponse> getCreatedClass() {
      return FinishChipResponse.class;
    }

    @Override
    public FinishChipResponse create(org.json.JSONObject jsonObject) {
      return new FinishChipResponse(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean APPROVED_IS_REQUIRED = false;
    public static final boolean EMVDATA_IS_REQUIRED = false;
    public static final boolean ISSUERSCRIPTRESULTS_IS_REQUIRED = false;
  }

}
