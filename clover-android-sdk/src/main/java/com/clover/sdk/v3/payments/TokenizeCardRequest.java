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
 * <li>{@link #getApiKey apiKey}</li>
 * <li>{@link #getMultiUseToken multiUseToken}</li>
 * <li>{@link #getSuppressConfirmation suppressConfirmation}</li>
 * <li>{@link #getCardMessage cardMessage}</li>
 * <li>{@link #getCardEntryMode cardEntryMode}</li>
 * <li>{@link #getAppTracking appTracking}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class TokenizeCardRequest extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  public java.lang.String getApiKey() {
    return genClient.cacheGet(CacheKey.apiKey);
  }

  public java.lang.Boolean getMultiUseToken() {
    return genClient.cacheGet(CacheKey.multiUseToken);
  }

  /**
   * Flag indicating whether or not the confirmation is presented to the user
   */
  public java.lang.Boolean getSuppressConfirmation() {
    return genClient.cacheGet(CacheKey.suppressConfirmation);
  }

  /**
   * Message to be displayed
   */
  public java.lang.String getCardMessage() {
    return genClient.cacheGet(CacheKey.cardMessage);
  }

  /**
   * Card Entry Mode supported for the transaction. Bits #1 indicates MAG_STRIPE, #2 indicates CONTACT, #3 indicates CONTACTLESS, #4 indicates MANUAL
   */
  public java.lang.Integer getCardEntryMode() {
    return genClient.cacheGet(CacheKey.cardEntryMode);
  }

  /**
   * Tracking information for the app that created this payment.
   */
  public com.clover.sdk.v3.apps.AppTracking getAppTracking() {
    return genClient.cacheGet(CacheKey.appTracking);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    apiKey
      (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    multiUseToken
      (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Boolean.class)),
    suppressConfirmation
      (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Boolean.class)),
    cardMessage
      (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    cardEntryMode
      (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Integer.class)),
    appTracking
      (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.apps.AppTracking.JSON_CREATOR)),
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

  private final GenericClient<TokenizeCardRequest> genClient;

  /**
   * Constructs a new empty instance.
   */
  public TokenizeCardRequest() {
    genClient = new GenericClient<TokenizeCardRequest>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected TokenizeCardRequest(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public TokenizeCardRequest(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public TokenizeCardRequest(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public TokenizeCardRequest(TokenizeCardRequest src) {
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

  /** Checks whether the 'apiKey' field is set and is not null */
  public boolean isNotNullApiKey() {
    return genClient.cacheValueIsNotNull(CacheKey.apiKey);
  }

  /** Checks whether the 'multiUseToken' field is set and is not null */
  public boolean isNotNullMultiUseToken() {
    return genClient.cacheValueIsNotNull(CacheKey.multiUseToken);
  }

  /** Checks whether the 'suppressConfirmation' field is set and is not null */
  public boolean isNotNullSuppressConfirmation() {
    return genClient.cacheValueIsNotNull(CacheKey.suppressConfirmation);
  }

  /** Checks whether the 'cardMessage' field is set and is not null */
  public boolean isNotNullCardMessage() {
    return genClient.cacheValueIsNotNull(CacheKey.cardMessage);
  }

  /** Checks whether the 'cardEntryMode' field is set and is not null */
  public boolean isNotNullCardEntryMode() {
    return genClient.cacheValueIsNotNull(CacheKey.cardEntryMode);
  }

  /** Checks whether the 'appTracking' field is set and is not null */
  public boolean isNotNullAppTracking() {
    return genClient.cacheValueIsNotNull(CacheKey.appTracking);
  }



  /** Checks whether the 'apiKey' field has been set, however the value could be null */
  public boolean hasApiKey() {
    return genClient.cacheHasKey(CacheKey.apiKey);
  }

  /** Checks whether the 'multiUseToken' field has been set, however the value could be null */
  public boolean hasMultiUseToken() {
    return genClient.cacheHasKey(CacheKey.multiUseToken);
  }

  /** Checks whether the 'suppressConfirmation' field has been set, however the value could be null */
  public boolean hasSuppressConfirmation() {
    return genClient.cacheHasKey(CacheKey.suppressConfirmation);
  }

  /** Checks whether the 'cardMessage' field has been set, however the value could be null */
  public boolean hasCardMessage() {
    return genClient.cacheHasKey(CacheKey.cardMessage);
  }

  /** Checks whether the 'cardEntryMode' field has been set, however the value could be null */
  public boolean hasCardEntryMode() {
    return genClient.cacheHasKey(CacheKey.cardEntryMode);
  }

  /** Checks whether the 'appTracking' field has been set, however the value could be null */
  public boolean hasAppTracking() {
    return genClient.cacheHasKey(CacheKey.appTracking);
  }


  /**
   * Sets the field 'apiKey'.
   */
  public TokenizeCardRequest setApiKey(java.lang.String apiKey) {
    return genClient.setOther(apiKey, CacheKey.apiKey);
  }

  /**
   * Sets the field 'multiUseToken'.
   */
  public TokenizeCardRequest setMultiUseToken(java.lang.Boolean multiUseToken) {
    return genClient.setOther(multiUseToken, CacheKey.multiUseToken);
  }

  /**
   * Sets the field 'suppressConfirmation'.
   */
  public TokenizeCardRequest setSuppressConfirmation(java.lang.Boolean suppressConfirmation) {
    return genClient.setOther(suppressConfirmation, CacheKey.suppressConfirmation);
  }

  /**
   * Sets the field 'cardMessage'.
   */
  public TokenizeCardRequest setCardMessage(java.lang.String cardMessage) {
    return genClient.setOther(cardMessage, CacheKey.cardMessage);
  }

  /**
   * Sets the field 'cardEntryMode'.
   */
  public TokenizeCardRequest setCardEntryMode(java.lang.Integer cardEntryMode) {
    return genClient.setOther(cardEntryMode, CacheKey.cardEntryMode);
  }

  /**
   * Sets the field 'appTracking'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public TokenizeCardRequest setAppTracking(com.clover.sdk.v3.apps.AppTracking appTracking) {
    return genClient.setRecord(appTracking, CacheKey.appTracking);
  }


  /** Clears the 'apiKey' field, the 'has' method for this field will now return false */
  public void clearApiKey() {
    genClient.clear(CacheKey.apiKey);
  }
  /** Clears the 'multiUseToken' field, the 'has' method for this field will now return false */
  public void clearMultiUseToken() {
    genClient.clear(CacheKey.multiUseToken);
  }
  /** Clears the 'suppressConfirmation' field, the 'has' method for this field will now return false */
  public void clearSuppressConfirmation() {
    genClient.clear(CacheKey.suppressConfirmation);
  }
  /** Clears the 'cardMessage' field, the 'has' method for this field will now return false */
  public void clearCardMessage() {
    genClient.clear(CacheKey.cardMessage);
  }
  /** Clears the 'cardEntryMode' field, the 'has' method for this field will now return false */
  public void clearCardEntryMode() {
    genClient.clear(CacheKey.cardEntryMode);
  }
  /** Clears the 'appTracking' field, the 'has' method for this field will now return false */
  public void clearAppTracking() {
    genClient.clear(CacheKey.appTracking);
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
  public TokenizeCardRequest copyChanges() {
    TokenizeCardRequest copy = new TokenizeCardRequest();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(TokenizeCardRequest src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new TokenizeCardRequest(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<TokenizeCardRequest> CREATOR = new android.os.Parcelable.Creator<TokenizeCardRequest>() {
    @Override
    public TokenizeCardRequest createFromParcel(android.os.Parcel in) {
      TokenizeCardRequest instance = new TokenizeCardRequest(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public TokenizeCardRequest[] newArray(int size) {
      return new TokenizeCardRequest[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<TokenizeCardRequest> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<TokenizeCardRequest>() {
    public Class<TokenizeCardRequest> getCreatedClass() {
      return TokenizeCardRequest.class;
    }

    @Override
    public TokenizeCardRequest create(org.json.JSONObject jsonObject) {
      return new TokenizeCardRequest(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean APIKEY_IS_REQUIRED = false;
    public static final boolean MULTIUSETOKEN_IS_REQUIRED = false;
    public static final boolean SUPPRESSCONFIRMATION_IS_REQUIRED = false;
    public static final boolean CARDMESSAGE_IS_REQUIRED = false;
    public static final boolean CARDENTRYMODE_IS_REQUIRED = false;
    public static final boolean APPTRACKING_IS_REQUIRED = false;
  }

}
