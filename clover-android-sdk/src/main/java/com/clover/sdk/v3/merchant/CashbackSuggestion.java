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

package com.clover.sdk.v3.merchant;

import com.clover.sdk.GenericClient;
import com.clover.sdk.GenericParcelable;

/**
 * This is an auto-generated Clover data object.
 * <p>
 * <h3>Fields</h3>
 * <ul>
 * <li>{@link #getAmount amount}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class CashbackSuggestion extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  /**
   * Suggested cashback amount
   */
  public java.lang.Long getAmount() {
    return genClient.cacheGet(CacheKey.amount);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    amount
            (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
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

  private final GenericClient<CashbackSuggestion> genClient;

  /**
   * Constructs a new empty instance.
   */
  public CashbackSuggestion() {
    genClient = new GenericClient<CashbackSuggestion>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected CashbackSuggestion(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public CashbackSuggestion(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public CashbackSuggestion(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public CashbackSuggestion(CashbackSuggestion src) {
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
    genClient.validateMin(CacheKey.amount, getAmount(), 0L);
  }

  /** Checks whether the 'amount' field is set and is not null */
  public boolean isNotNullAmount() {
    return genClient.cacheValueIsNotNull(CacheKey.amount);
  }



  /** Checks whether the 'amount' field has been set, however the value could be null */
  public boolean hasAmount() {
    return genClient.cacheHasKey(CacheKey.amount);
  }


  /**
   * Sets the field 'amount'.
   */
  public CashbackSuggestion setAmount(java.lang.Long amount) {
    return genClient.setOther(amount, CacheKey.amount);
  }


  /** Clears the 'amount' field, the 'has' method for this field will now return false */
  public void clearAmount() {
    genClient.clear(CacheKey.amount);
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
  public CashbackSuggestion copyChanges() {
    CashbackSuggestion copy = new CashbackSuggestion();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(CashbackSuggestion src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new CashbackSuggestion(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<CashbackSuggestion> CREATOR = new android.os.Parcelable.Creator<CashbackSuggestion>() {
    @Override
    public CashbackSuggestion createFromParcel(android.os.Parcel in) {
      CashbackSuggestion instance = new CashbackSuggestion(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public CashbackSuggestion[] newArray(int size) {
      return new CashbackSuggestion[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<CashbackSuggestion> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<CashbackSuggestion>() {
    public Class<CashbackSuggestion> getCreatedClass() {
      return CashbackSuggestion.class;
    }

    @Override
    public CashbackSuggestion create(org.json.JSONObject jsonObject) {
      return new CashbackSuggestion(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean AMOUNT_IS_REQUIRED = false;
    public static final long AMOUNT_MIN = 0;
  }

}
