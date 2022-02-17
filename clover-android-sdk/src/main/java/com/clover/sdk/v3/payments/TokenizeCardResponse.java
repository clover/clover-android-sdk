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
 * <li>{@link #getStatus status}</li>
 * <li>{@link #getMultiUseToken multiUseToken}</li>
 * <li>{@link #getTokenResponse tokenResponse}</li>
 * <li>{@link #getFailureReason failureReason}</li>
 * <li>{@link #getReason reason}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class TokenizeCardResponse extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  /**
   * Result of tokenization
   */
  public Status getStatus() {
    return genClient.cacheGet(CacheKey.status);
  }

  public Boolean getMultiUseToken() {
    return genClient.cacheGet(CacheKey.multiUseToken);
  }

  public com.clover.sdk.v3.tokens.CreateTokenResponse getTokenResponse() {
    return genClient.cacheGet(CacheKey.tokenResponse);
  }

  /**
   * Reason for tokenization failure
   */
  public com.clover.sdk.v3.payments.FailureReason getFailureReason() {
    return genClient.cacheGet(CacheKey.failureReason);
  }

  public String getReason() {
    return genClient.cacheGet(CacheKey.reason);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    status
        (com.clover.sdk.extractors.EnumExtractionStrategy.instance(Status.class)),
    multiUseToken
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(Boolean.class)),
    tokenResponse
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.tokens.CreateTokenResponse.JSON_CREATOR)),
    failureReason
        (com.clover.sdk.extractors.EnumExtractionStrategy.instance(com.clover.sdk.v3.payments.FailureReason.class)),
    reason
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(String.class)),
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

  private final GenericClient<TokenizeCardResponse> genClient;

  /**
   * Constructs a new empty instance.
   */
  public TokenizeCardResponse() {
    genClient = new GenericClient<TokenizeCardResponse>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected TokenizeCardResponse(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public TokenizeCardResponse(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public TokenizeCardResponse(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public TokenizeCardResponse(TokenizeCardResponse src) {
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

  /** Checks whether the 'status' field is set and is not null */
  public boolean isNotNullStatus() {
    return genClient.cacheValueIsNotNull(CacheKey.status);
  }

  /** Checks whether the 'multiUseToken' field is set and is not null */
  public boolean isNotNullMultiUseToken() {
    return genClient.cacheValueIsNotNull(CacheKey.multiUseToken);
  }

  /** Checks whether the 'tokenResponse' field is set and is not null */
  public boolean isNotNullTokenResponse() {
    return genClient.cacheValueIsNotNull(CacheKey.tokenResponse);
  }

  /** Checks whether the 'failureReason' field is set and is not null */
  public boolean isNotNullFailureReason() {
    return genClient.cacheValueIsNotNull(CacheKey.failureReason);
  }

  /** Checks whether the 'reason' field is set and is not null */
  public boolean isNotNullReason() {
    return genClient.cacheValueIsNotNull(CacheKey.reason);
  }



  /** Checks whether the 'status' field has been set, however the value could be null */
  public boolean hasStatus() {
    return genClient.cacheHasKey(CacheKey.status);
  }

  /** Checks whether the 'multiUseToken' field has been set, however the value could be null */
  public boolean hasMultiUseToken() {
    return genClient.cacheHasKey(CacheKey.multiUseToken);
  }

  /** Checks whether the 'tokenResponse' field has been set, however the value could be null */
  public boolean hasTokenResponse() {
    return genClient.cacheHasKey(CacheKey.tokenResponse);
  }

  /** Checks whether the 'failureReason' field has been set, however the value could be null */
  public boolean hasFailureReason() {
    return genClient.cacheHasKey(CacheKey.failureReason);
  }

  /** Checks whether the 'reason' field has been set, however the value could be null */
  public boolean hasReason() {
    return genClient.cacheHasKey(CacheKey.reason);
  }


  /**
   * Sets the field 'status'.
   */
  public TokenizeCardResponse setStatus(Status status) {
    return genClient.setOther(status, CacheKey.status);
  }

  /**
   * Sets the field 'multiUseToken'.
   */
  public TokenizeCardResponse setMultiUseToken(Boolean multiUseToken) {
    return genClient.setOther(multiUseToken, CacheKey.multiUseToken);
  }

  /**
   * Sets the field 'tokenResponse'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public TokenizeCardResponse setTokenResponse(com.clover.sdk.v3.tokens.CreateTokenResponse tokenResponse) {
    return genClient.setRecord(tokenResponse, CacheKey.tokenResponse);
  }

  /**
   * Sets the field 'failureReason'.
   */
  public TokenizeCardResponse setFailureReason(com.clover.sdk.v3.payments.FailureReason failureReason) {
    return genClient.setOther(failureReason, CacheKey.failureReason);
  }

  /**
   * Sets the field 'reason'.
   */
  public TokenizeCardResponse setReason(String reason) {
    return genClient.setOther(reason, CacheKey.reason);
  }


  /** Clears the 'status' field, the 'has' method for this field will now return false */
  public void clearStatus() {
    genClient.clear(CacheKey.status);
  }
  /** Clears the 'multiUseToken' field, the 'has' method for this field will now return false */
  public void clearMultiUseToken() {
    genClient.clear(CacheKey.multiUseToken);
  }
  /** Clears the 'tokenResponse' field, the 'has' method for this field will now return false */
  public void clearTokenResponse() {
    genClient.clear(CacheKey.tokenResponse);
  }
  /** Clears the 'failureReason' field, the 'has' method for this field will now return false */
  public void clearFailureReason() {
    genClient.clear(CacheKey.failureReason);
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
  public TokenizeCardResponse copyChanges() {
    TokenizeCardResponse copy = new TokenizeCardResponse();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(TokenizeCardResponse src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new TokenizeCardResponse(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<TokenizeCardResponse> CREATOR = new android.os.Parcelable.Creator<TokenizeCardResponse>() {
    @Override
    public TokenizeCardResponse createFromParcel(android.os.Parcel in) {
      TokenizeCardResponse instance = new TokenizeCardResponse(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public TokenizeCardResponse[] newArray(int size) {
      return new TokenizeCardResponse[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<TokenizeCardResponse> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<TokenizeCardResponse>() {
    public Class<TokenizeCardResponse> getCreatedClass() {
      return TokenizeCardResponse.class;
    }

    @Override
    public TokenizeCardResponse create(org.json.JSONObject jsonObject) {
      return new TokenizeCardResponse(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean STATUS_IS_REQUIRED = false;
    public static final boolean MULTIUSETOKEN_IS_REQUIRED = false;
    public static final boolean TOKENRESPONSE_IS_REQUIRED = false;
    public static final boolean FAILUREREASON_IS_REQUIRED = false;
    public static final boolean REASON_IS_REQUIRED = false;
  }

}
