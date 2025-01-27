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
 * <li>{@link #getSecureEntryType secureEntryType}</li>
 * <li>{@link #getKeypadLayout keypadLayout}</li>
 * <li>{@link #getTransactionType transactionType}</li>
 * <li>{@link #getAmount amount}</li>
 * <li>{@link #getOffsetX offsetX}</li>
 * <li>{@link #getOffsetY offsetY}</li>
 * <li>{@link #getAllowSkip allowSkip}</li>
 * <li>{@link #getNumberOfPinAttemptsRemaining numberOfPinAttemptsRemaining}</li>
 * <li>{@link #getLastTry lastTry}</li>
 * <li>{@link #getWrongPIN wrongPIN}</li>
 * <li>{@link #getSupportAvocado supportAvocado}</li>
 * <li>{@link #getInjectedBlob injectedBlob}</li>
 * <li>{@link #getAppLabel appLabel}</li>
 * <li>{@link #getPreAuthType preAuthType}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class SecureEntryScreenParameters extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  public com.clover.sdk.v3.payment.raw.model.SecureEntryType getSecureEntryType() {
    return genClient.cacheGet(CacheKey.secureEntryType);
  }

  public com.clover.sdk.v3.payment.raw.model.KeypadLayout getKeypadLayout() {
    return genClient.cacheGet(CacheKey.keypadLayout);
  }

  /**
   * Type of operation being performed: SALE, REFUND, ...
   */
  public java.lang.String getTransactionType() {
    return genClient.cacheGet(CacheKey.transactionType);
  }

  /**
   * Transaction amount
   */
  public java.lang.Long getAmount() {
    return genClient.cacheGet(CacheKey.amount);
  }

  /**
   * Buttons offset X in case of RANDOMIZED screen
   */
  public java.lang.Integer getOffsetX() {
    return genClient.cacheGet(CacheKey.offsetX);
  }

  /**
   * Buttons offset Y in case of RANDOMIZED screen
   */
  public java.lang.Integer getOffsetY() {
    return genClient.cacheGet(CacheKey.offsetY);
  }

  /**
   * Flag to indicate if skip is allowed. When true, client must show the skip button
   */
  public java.lang.Boolean getAllowSkip() {
    return genClient.cacheGet(CacheKey.allowSkip);
  }

  /**
   * Number of attempts remaining to enter a correct PIN
   */
  public java.lang.Integer getNumberOfPinAttemptsRemaining() {
    return genClient.cacheGet(CacheKey.numberOfPinAttemptsRemaining);
  }

  /**
   * Flag to indicate if it is the last attempt, typically used for PIN entry. Client should show a 'Last Try' message
   */
  public java.lang.Boolean getLastTry() {
    return genClient.cacheGet(CacheKey.lastTry);
  }

  /**
   * Flag to indicate if previous PIN entered was incorrect. Client should show a 'Wrong PIN' message
   */
  public java.lang.Boolean getWrongPIN() {
    return genClient.cacheGet(CacheKey.wrongPIN);
  }

  /**
   * Flag to indicate if avocado layout is supported by secure board
   */
  public java.lang.Boolean getSupportAvocado() {
    return genClient.cacheGet(CacheKey.supportAvocado);
  }

  /**
   * The key blob injected to SB
   */
  public java.lang.String getInjectedBlob() {
    return genClient.cacheGet(CacheKey.injectedBlob);
  }

  /**
   * application label to be displayed on PIN entry screen
   */
  public java.lang.String getAppLabel() {
    return genClient.cacheGet(CacheKey.appLabel);
  }

  /**
   * Type of pre-auth transaction
   */
  public com.clover.sdk.v3.payments.PreAuthType getPreAuthType() {
    return genClient.cacheGet(CacheKey.preAuthType);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    secureEntryType
        (com.clover.sdk.extractors.EnumExtractionStrategy.instance(com.clover.sdk.v3.payment.raw.model.SecureEntryType.class)),
    keypadLayout
        (com.clover.sdk.extractors.EnumExtractionStrategy.instance(com.clover.sdk.v3.payment.raw.model.KeypadLayout.class)),
    transactionType
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    amount
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    offsetX
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Integer.class)),
    offsetY
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Integer.class)),
    allowSkip
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Boolean.class)),
    numberOfPinAttemptsRemaining
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Integer.class)),
    lastTry
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Boolean.class)),
    wrongPIN
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Boolean.class)),
    supportAvocado
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Boolean.class)),
    injectedBlob
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    appLabel
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    preAuthType
        (com.clover.sdk.extractors.EnumExtractionStrategy.instance(com.clover.sdk.v3.payments.PreAuthType.class)),
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

  private final GenericClient<SecureEntryScreenParameters> genClient;

  /**
   * Constructs a new empty instance.
   */
  public SecureEntryScreenParameters() {
    genClient = new GenericClient<SecureEntryScreenParameters>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected SecureEntryScreenParameters(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public SecureEntryScreenParameters(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public SecureEntryScreenParameters(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public SecureEntryScreenParameters(SecureEntryScreenParameters src) {
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

  /** Checks whether the 'secureEntryType' field is set and is not null */
  public boolean isNotNullSecureEntryType() {
    return genClient.cacheValueIsNotNull(CacheKey.secureEntryType);
  }

  /** Checks whether the 'keypadLayout' field is set and is not null */
  public boolean isNotNullKeypadLayout() {
    return genClient.cacheValueIsNotNull(CacheKey.keypadLayout);
  }

  /** Checks whether the 'transactionType' field is set and is not null */
  public boolean isNotNullTransactionType() {
    return genClient.cacheValueIsNotNull(CacheKey.transactionType);
  }

  /** Checks whether the 'amount' field is set and is not null */
  public boolean isNotNullAmount() {
    return genClient.cacheValueIsNotNull(CacheKey.amount);
  }

  /** Checks whether the 'offsetX' field is set and is not null */
  public boolean isNotNullOffsetX() {
    return genClient.cacheValueIsNotNull(CacheKey.offsetX);
  }

  /** Checks whether the 'offsetY' field is set and is not null */
  public boolean isNotNullOffsetY() {
    return genClient.cacheValueIsNotNull(CacheKey.offsetY);
  }

  /** Checks whether the 'allowSkip' field is set and is not null */
  public boolean isNotNullAllowSkip() {
    return genClient.cacheValueIsNotNull(CacheKey.allowSkip);
  }

  /** Checks whether the 'numberOfPinAttemptsRemaining' field is set and is not null */
  public boolean isNotNullNumberOfPinAttemptsRemaining() {
    return genClient.cacheValueIsNotNull(CacheKey.numberOfPinAttemptsRemaining);
  }

  /** Checks whether the 'lastTry' field is set and is not null */
  public boolean isNotNullLastTry() {
    return genClient.cacheValueIsNotNull(CacheKey.lastTry);
  }

  /** Checks whether the 'wrongPIN' field is set and is not null */
  public boolean isNotNullWrongPIN() {
    return genClient.cacheValueIsNotNull(CacheKey.wrongPIN);
  }

  /** Checks whether the 'supportAvocado' field is set and is not null */
  public boolean isNotNullSupportAvocado() {
    return genClient.cacheValueIsNotNull(CacheKey.supportAvocado);
  }

  /** Checks whether the 'injectedBlob' field is set and is not null */
  public boolean isNotNullInjectedBlob() {
    return genClient.cacheValueIsNotNull(CacheKey.injectedBlob);
  }

  /** Checks whether the 'appLabel' field is set and is not null */
  public boolean isNotNullAppLabel() {
    return genClient.cacheValueIsNotNull(CacheKey.appLabel);
  }

  /** Checks whether the 'preAuthType' field is set and is not null */
  public boolean isNotNullPreAuthType() {
    return genClient.cacheValueIsNotNull(CacheKey.preAuthType);
  }



  /** Checks whether the 'secureEntryType' field has been set, however the value could be null */
  public boolean hasSecureEntryType() {
    return genClient.cacheHasKey(CacheKey.secureEntryType);
  }

  /** Checks whether the 'keypadLayout' field has been set, however the value could be null */
  public boolean hasKeypadLayout() {
    return genClient.cacheHasKey(CacheKey.keypadLayout);
  }

  /** Checks whether the 'transactionType' field has been set, however the value could be null */
  public boolean hasTransactionType() {
    return genClient.cacheHasKey(CacheKey.transactionType);
  }

  /** Checks whether the 'amount' field has been set, however the value could be null */
  public boolean hasAmount() {
    return genClient.cacheHasKey(CacheKey.amount);
  }

  /** Checks whether the 'offsetX' field has been set, however the value could be null */
  public boolean hasOffsetX() {
    return genClient.cacheHasKey(CacheKey.offsetX);
  }

  /** Checks whether the 'offsetY' field has been set, however the value could be null */
  public boolean hasOffsetY() {
    return genClient.cacheHasKey(CacheKey.offsetY);
  }

  /** Checks whether the 'allowSkip' field has been set, however the value could be null */
  public boolean hasAllowSkip() {
    return genClient.cacheHasKey(CacheKey.allowSkip);
  }

  /** Checks whether the 'numberOfPinAttemptsRemaining' field has been set, however the value could be null */
  public boolean hasNumberOfPinAttemptsRemaining() {
    return genClient.cacheHasKey(CacheKey.numberOfPinAttemptsRemaining);
  }

  /** Checks whether the 'lastTry' field has been set, however the value could be null */
  public boolean hasLastTry() {
    return genClient.cacheHasKey(CacheKey.lastTry);
  }

  /** Checks whether the 'wrongPIN' field has been set, however the value could be null */
  public boolean hasWrongPIN() {
    return genClient.cacheHasKey(CacheKey.wrongPIN);
  }

  /** Checks whether the 'supportAvocado' field has been set, however the value could be null */
  public boolean hasSupportAvocado() {
    return genClient.cacheHasKey(CacheKey.supportAvocado);
  }

  /** Checks whether the 'injectedBlob' field has been set, however the value could be null */
  public boolean hasInjectedBlob() {
    return genClient.cacheHasKey(CacheKey.injectedBlob);
  }

  /** Checks whether the 'appLabel' field has been set, however the value could be null */
  public boolean hasAppLabel() {
    return genClient.cacheHasKey(CacheKey.appLabel);
  }

  /** Checks whether the 'preAuthType' field has been set, however the value could be null */
  public boolean hasPreAuthType() {
    return genClient.cacheHasKey(CacheKey.preAuthType);
  }


  /**
   * Sets the field 'secureEntryType'.
   */
  public SecureEntryScreenParameters setSecureEntryType(com.clover.sdk.v3.payment.raw.model.SecureEntryType secureEntryType) {
    return genClient.setOther(secureEntryType, CacheKey.secureEntryType);
  }

  /**
   * Sets the field 'keypadLayout'.
   */
  public SecureEntryScreenParameters setKeypadLayout(com.clover.sdk.v3.payment.raw.model.KeypadLayout keypadLayout) {
    return genClient.setOther(keypadLayout, CacheKey.keypadLayout);
  }

  /**
   * Sets the field 'transactionType'.
   */
  public SecureEntryScreenParameters setTransactionType(java.lang.String transactionType) {
    return genClient.setOther(transactionType, CacheKey.transactionType);
  }

  /**
   * Sets the field 'amount'.
   */
  public SecureEntryScreenParameters setAmount(java.lang.Long amount) {
    return genClient.setOther(amount, CacheKey.amount);
  }

  /**
   * Sets the field 'offsetX'.
   */
  public SecureEntryScreenParameters setOffsetX(java.lang.Integer offsetX) {
    return genClient.setOther(offsetX, CacheKey.offsetX);
  }

  /**
   * Sets the field 'offsetY'.
   */
  public SecureEntryScreenParameters setOffsetY(java.lang.Integer offsetY) {
    return genClient.setOther(offsetY, CacheKey.offsetY);
  }

  /**
   * Sets the field 'allowSkip'.
   */
  public SecureEntryScreenParameters setAllowSkip(java.lang.Boolean allowSkip) {
    return genClient.setOther(allowSkip, CacheKey.allowSkip);
  }

  /**
   * Sets the field 'numberOfPinAttemptsRemaining'.
   */
  public SecureEntryScreenParameters setNumberOfPinAttemptsRemaining(java.lang.Integer numberOfPinAttemptsRemaining) {
    return genClient.setOther(numberOfPinAttemptsRemaining, CacheKey.numberOfPinAttemptsRemaining);
  }

  /**
   * Sets the field 'lastTry'.
   */
  public SecureEntryScreenParameters setLastTry(java.lang.Boolean lastTry) {
    return genClient.setOther(lastTry, CacheKey.lastTry);
  }

  /**
   * Sets the field 'wrongPIN'.
   */
  public SecureEntryScreenParameters setWrongPIN(java.lang.Boolean wrongPIN) {
    return genClient.setOther(wrongPIN, CacheKey.wrongPIN);
  }

  /**
   * Sets the field 'supportAvocado'.
   */
  public SecureEntryScreenParameters setSupportAvocado(java.lang.Boolean supportAvocado) {
    return genClient.setOther(supportAvocado, CacheKey.supportAvocado);
  }

  /**
   * Sets the field 'injectedBlob'.
   */
  public SecureEntryScreenParameters setInjectedBlob(java.lang.String injectedBlob) {
    return genClient.setOther(injectedBlob, CacheKey.injectedBlob);
  }

  /**
   * Sets the field 'appLabel'.
   */
  public SecureEntryScreenParameters setAppLabel(java.lang.String appLabel) {
    return genClient.setOther(appLabel, CacheKey.appLabel);
  }

  /**
   * Sets the field 'preAuthType'.
   */
  public SecureEntryScreenParameters setPreAuthType(com.clover.sdk.v3.payments.PreAuthType preAuthType) {
    return genClient.setOther(preAuthType, CacheKey.preAuthType);
  }


  /** Clears the 'secureEntryType' field, the 'has' method for this field will now return false */
  public void clearSecureEntryType() {
    genClient.clear(CacheKey.secureEntryType);
  }
  /** Clears the 'keypadLayout' field, the 'has' method for this field will now return false */
  public void clearKeypadLayout() {
    genClient.clear(CacheKey.keypadLayout);
  }
  /** Clears the 'transactionType' field, the 'has' method for this field will now return false */
  public void clearTransactionType() {
    genClient.clear(CacheKey.transactionType);
  }
  /** Clears the 'amount' field, the 'has' method for this field will now return false */
  public void clearAmount() {
    genClient.clear(CacheKey.amount);
  }
  /** Clears the 'offsetX' field, the 'has' method for this field will now return false */
  public void clearOffsetX() {
    genClient.clear(CacheKey.offsetX);
  }
  /** Clears the 'offsetY' field, the 'has' method for this field will now return false */
  public void clearOffsetY() {
    genClient.clear(CacheKey.offsetY);
  }
  /** Clears the 'allowSkip' field, the 'has' method for this field will now return false */
  public void clearAllowSkip() {
    genClient.clear(CacheKey.allowSkip);
  }
  /** Clears the 'numberOfPinAttemptsRemaining' field, the 'has' method for this field will now return false */
  public void clearNumberOfPinAttemptsRemaining() {
    genClient.clear(CacheKey.numberOfPinAttemptsRemaining);
  }
  /** Clears the 'lastTry' field, the 'has' method for this field will now return false */
  public void clearLastTry() {
    genClient.clear(CacheKey.lastTry);
  }
  /** Clears the 'wrongPIN' field, the 'has' method for this field will now return false */
  public void clearWrongPIN() {
    genClient.clear(CacheKey.wrongPIN);
  }
  /** Clears the 'supportAvocado' field, the 'has' method for this field will now return false */
  public void clearSupportAvocado() {
    genClient.clear(CacheKey.supportAvocado);
  }
  /** Clears the 'injectedBlob' field, the 'has' method for this field will now return false */
  public void clearInjectedBlob() {
    genClient.clear(CacheKey.injectedBlob);
  }
  /** Clears the 'appLabel' field, the 'has' method for this field will now return false */
  public void clearAppLabel() {
    genClient.clear(CacheKey.appLabel);
  }
  /** Clears the 'preAuthType' field, the 'has' method for this field will now return false */
  public void clearPreAuthType() {
    genClient.clear(CacheKey.preAuthType);
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
  public SecureEntryScreenParameters copyChanges() {
    SecureEntryScreenParameters copy = new SecureEntryScreenParameters();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(SecureEntryScreenParameters src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new SecureEntryScreenParameters(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<SecureEntryScreenParameters> CREATOR = new android.os.Parcelable.Creator<SecureEntryScreenParameters>() {
    @Override
    public SecureEntryScreenParameters createFromParcel(android.os.Parcel in) {
      SecureEntryScreenParameters instance = new SecureEntryScreenParameters(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public SecureEntryScreenParameters[] newArray(int size) {
      return new SecureEntryScreenParameters[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<SecureEntryScreenParameters> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<SecureEntryScreenParameters>() {
    public Class<SecureEntryScreenParameters> getCreatedClass() {
      return SecureEntryScreenParameters.class;
    }

    @Override
    public SecureEntryScreenParameters create(org.json.JSONObject jsonObject) {
      return new SecureEntryScreenParameters(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean SECUREENTRYTYPE_IS_REQUIRED = false;
    public static final boolean KEYPADLAYOUT_IS_REQUIRED = false;
    public static final boolean TRANSACTIONTYPE_IS_REQUIRED = false;
    public static final boolean AMOUNT_IS_REQUIRED = false;
    public static final boolean OFFSETX_IS_REQUIRED = false;
    public static final boolean OFFSETY_IS_REQUIRED = false;
    public static final boolean ALLOWSKIP_IS_REQUIRED = false;
    public static final boolean NUMBEROFPINATTEMPTSREMAINING_IS_REQUIRED = false;
    public static final boolean LASTTRY_IS_REQUIRED = false;
    public static final boolean WRONGPIN_IS_REQUIRED = false;
    public static final boolean SUPPORTAVOCADO_IS_REQUIRED = false;
    public static final boolean INJECTEDBLOB_IS_REQUIRED = false;
    public static final boolean APPLABEL_IS_REQUIRED = false;
    public static final boolean PREAUTHTYPE_IS_REQUIRED = false;
  }

}
