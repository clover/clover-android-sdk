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
 * <li>{@link #getTransactionType transactionType}</li>
 * <li>{@link #getAmount amount}</li>
 * <li>{@link #getCashbackAmount cashbackAmount}</li>
 * <li>{@link #getCurrencyCode currencyCode}</li>
 * <li>{@link #getCurrencyExponent currencyExponent}</li>
 * <li>{@link #getEmvData emvData}</li>
 * <li>{@link #getTerminalRiskManagement terminalRiskManagement}</li>
 * <li>{@link #getEmvTagList emvTagList}</li>
 * <li>{@link #getPanExceptionList panExceptionList}</li>
 * <li>{@link #getPinBypassDisabled pinBypassDisabled}</li>
 * <li>{@link #getDisableOfflineApproval disableOfflineApproval}</li>
 * <li>{@link #getPinCaptureTimeout pinCaptureTimeout}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class GoOnChipRequest extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  public java.lang.String getTransactionType() {
    return genClient.cacheGet(CacheKey.transactionType);
  }

  public java.lang.Long getAmount() {
    return genClient.cacheGet(CacheKey.amount);
  }

  public java.lang.Long getCashbackAmount() {
    return genClient.cacheGet(CacheKey.cashbackAmount);
  }

  public java.lang.Integer getCurrencyCode() {
    return genClient.cacheGet(CacheKey.currencyCode);
  }

  public java.lang.Integer getCurrencyExponent() {
    return genClient.cacheGet(CacheKey.currencyExponent);
  }

  public java.lang.String getEmvData() {
    return genClient.cacheGet(CacheKey.emvData);
  }

  public java.lang.String getTerminalRiskManagement() {
    return genClient.cacheGet(CacheKey.terminalRiskManagement);
  }

  public java.util.List<java.lang.String> getEmvTagList() {
    return genClient.cacheGet(CacheKey.emvTagList);
  }

  public java.lang.Boolean getPanExceptionList() {
    return genClient.cacheGet(CacheKey.panExceptionList);
  }

  public java.lang.Boolean getPinBypassDisabled() {
    return genClient.cacheGet(CacheKey.pinBypassDisabled);
  }

  public java.lang.Boolean getDisableOfflineApproval() {
    return genClient.cacheGet(CacheKey.disableOfflineApproval);
  }

  public java.lang.Long getPinCaptureTimeout() {
    return genClient.cacheGet(CacheKey.pinCaptureTimeout);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    transactionType
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    amount
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    cashbackAmount
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    currencyCode
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Integer.class)),
    currencyExponent
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Integer.class)),
    emvData
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    terminalRiskManagement
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    emvTagList
        (com.clover.sdk.extractors.BasicListExtractionStrategy.instance(java.lang.String.class)),
    panExceptionList
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Boolean.class)),
    pinBypassDisabled
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Boolean.class)),
    disableOfflineApproval
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Boolean.class)),
    pinCaptureTimeout
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

  private final GenericClient<GoOnChipRequest> genClient;

  /**
   * Constructs a new empty instance.
   */
  public GoOnChipRequest() {
    genClient = new GenericClient<GoOnChipRequest>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected GoOnChipRequest(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public GoOnChipRequest(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public GoOnChipRequest(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public GoOnChipRequest(GoOnChipRequest src) {
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

  /** Checks whether the 'transactionType' field is set and is not null */
  public boolean isNotNullTransactionType() {
    return genClient.cacheValueIsNotNull(CacheKey.transactionType);
  }

  /** Checks whether the 'amount' field is set and is not null */
  public boolean isNotNullAmount() {
    return genClient.cacheValueIsNotNull(CacheKey.amount);
  }

  /** Checks whether the 'cashbackAmount' field is set and is not null */
  public boolean isNotNullCashbackAmount() {
    return genClient.cacheValueIsNotNull(CacheKey.cashbackAmount);
  }

  /** Checks whether the 'currencyCode' field is set and is not null */
  public boolean isNotNullCurrencyCode() {
    return genClient.cacheValueIsNotNull(CacheKey.currencyCode);
  }

  /** Checks whether the 'currencyExponent' field is set and is not null */
  public boolean isNotNullCurrencyExponent() {
    return genClient.cacheValueIsNotNull(CacheKey.currencyExponent);
  }

  /** Checks whether the 'emvData' field is set and is not null */
  public boolean isNotNullEmvData() {
    return genClient.cacheValueIsNotNull(CacheKey.emvData);
  }

  /** Checks whether the 'terminalRiskManagement' field is set and is not null */
  public boolean isNotNullTerminalRiskManagement() {
    return genClient.cacheValueIsNotNull(CacheKey.terminalRiskManagement);
  }

  /** Checks whether the 'emvTagList' field is set and is not null */
  public boolean isNotNullEmvTagList() {
    return genClient.cacheValueIsNotNull(CacheKey.emvTagList);
  }

  /** Checks whether the 'emvTagList' field is set and is not null and is not empty */
  public boolean isNotEmptyEmvTagList() { return isNotNullEmvTagList() && !getEmvTagList().isEmpty(); }

  /** Checks whether the 'panExceptionList' field is set and is not null */
  public boolean isNotNullPanExceptionList() {
    return genClient.cacheValueIsNotNull(CacheKey.panExceptionList);
  }

  /** Checks whether the 'pinBypassDisabled' field is set and is not null */
  public boolean isNotNullPinBypassDisabled() {
    return genClient.cacheValueIsNotNull(CacheKey.pinBypassDisabled);
  }

  /** Checks whether the 'disableOfflineApproval' field is set and is not null */
  public boolean isNotNullDisableOfflineApproval() {
    return genClient.cacheValueIsNotNull(CacheKey.disableOfflineApproval);
  }

  /** Checks whether the 'pinCaptureTimeout' field is set and is not null */
  public boolean isNotNullPinCaptureTimeout() {
    return genClient.cacheValueIsNotNull(CacheKey.pinCaptureTimeout);
  }



  /** Checks whether the 'transactionType' field has been set, however the value could be null */
  public boolean hasTransactionType() {
    return genClient.cacheHasKey(CacheKey.transactionType);
  }

  /** Checks whether the 'amount' field has been set, however the value could be null */
  public boolean hasAmount() {
    return genClient.cacheHasKey(CacheKey.amount);
  }

  /** Checks whether the 'cashbackAmount' field has been set, however the value could be null */
  public boolean hasCashbackAmount() {
    return genClient.cacheHasKey(CacheKey.cashbackAmount);
  }

  /** Checks whether the 'currencyCode' field has been set, however the value could be null */
  public boolean hasCurrencyCode() {
    return genClient.cacheHasKey(CacheKey.currencyCode);
  }

  /** Checks whether the 'currencyExponent' field has been set, however the value could be null */
  public boolean hasCurrencyExponent() {
    return genClient.cacheHasKey(CacheKey.currencyExponent);
  }

  /** Checks whether the 'emvData' field has been set, however the value could be null */
  public boolean hasEmvData() {
    return genClient.cacheHasKey(CacheKey.emvData);
  }

  /** Checks whether the 'terminalRiskManagement' field has been set, however the value could be null */
  public boolean hasTerminalRiskManagement() {
    return genClient.cacheHasKey(CacheKey.terminalRiskManagement);
  }

  /** Checks whether the 'emvTagList' field has been set, however the value could be null */
  public boolean hasEmvTagList() {
    return genClient.cacheHasKey(CacheKey.emvTagList);
  }

  /** Checks whether the 'panExceptionList' field has been set, however the value could be null */
  public boolean hasPanExceptionList() {
    return genClient.cacheHasKey(CacheKey.panExceptionList);
  }

  /** Checks whether the 'pinBypassDisabled' field has been set, however the value could be null */
  public boolean hasPinBypassDisabled() {
    return genClient.cacheHasKey(CacheKey.pinBypassDisabled);
  }

  /** Checks whether the 'disableOfflineApproval' field has been set, however the value could be null */
  public boolean hasDisableOfflineApproval() {
    return genClient.cacheHasKey(CacheKey.disableOfflineApproval);
  }

  /** Checks whether the 'pinCaptureTimeout' field has been set, however the value could be null */
  public boolean hasPinCaptureTimeout() {
    return genClient.cacheHasKey(CacheKey.pinCaptureTimeout);
  }


  /**
   * Sets the field 'transactionType'.
   */
  public GoOnChipRequest setTransactionType(java.lang.String transactionType) {
    return genClient.setOther(transactionType, CacheKey.transactionType);
  }

  /**
   * Sets the field 'amount'.
   */
  public GoOnChipRequest setAmount(java.lang.Long amount) {
    return genClient.setOther(amount, CacheKey.amount);
  }

  /**
   * Sets the field 'cashbackAmount'.
   */
  public GoOnChipRequest setCashbackAmount(java.lang.Long cashbackAmount) {
    return genClient.setOther(cashbackAmount, CacheKey.cashbackAmount);
  }

  /**
   * Sets the field 'currencyCode'.
   */
  public GoOnChipRequest setCurrencyCode(java.lang.Integer currencyCode) {
    return genClient.setOther(currencyCode, CacheKey.currencyCode);
  }

  /**
   * Sets the field 'currencyExponent'.
   */
  public GoOnChipRequest setCurrencyExponent(java.lang.Integer currencyExponent) {
    return genClient.setOther(currencyExponent, CacheKey.currencyExponent);
  }

  /**
   * Sets the field 'emvData'.
   */
  public GoOnChipRequest setEmvData(java.lang.String emvData) {
    return genClient.setOther(emvData, CacheKey.emvData);
  }

  /**
   * Sets the field 'terminalRiskManagement'.
   */
  public GoOnChipRequest setTerminalRiskManagement(java.lang.String terminalRiskManagement) {
    return genClient.setOther(terminalRiskManagement, CacheKey.terminalRiskManagement);
  }

  /**
   * Sets the field 'emvTagList'.
   *
   * Nulls in the given List are skipped. List parameter is copied, so it will not reflect any changes, but objects inside it will.
   */
  public GoOnChipRequest setEmvTagList(java.util.List<java.lang.String> emvTagList) {
    return genClient.setArrayOther(emvTagList, CacheKey.emvTagList);
  }

  /**
   * Sets the field 'panExceptionList'.
   */
  public GoOnChipRequest setPanExceptionList(java.lang.Boolean panExceptionList) {
    return genClient.setOther(panExceptionList, CacheKey.panExceptionList);
  }

  /**
   * Sets the field 'pinBypassDisabled'.
   */
  public GoOnChipRequest setPinBypassDisabled(java.lang.Boolean pinBypassDisabled) {
    return genClient.setOther(pinBypassDisabled, CacheKey.pinBypassDisabled);
  }

  /**
   * Sets the field 'disableOfflineApproval'.
   */
  public GoOnChipRequest setDisableOfflineApproval(java.lang.Boolean disableOfflineApproval) {
    return genClient.setOther(disableOfflineApproval, CacheKey.disableOfflineApproval);
  }

  /**
   * Sets the field 'pinCaptureTimeout'.
   */
  public GoOnChipRequest setPinCaptureTimeout(java.lang.Long pinCaptureTimeout) {
    return genClient.setOther(pinCaptureTimeout, CacheKey.pinCaptureTimeout);
  }


  /** Clears the 'transactionType' field, the 'has' method for this field will now return false */
  public void clearTransactionType() {
    genClient.clear(CacheKey.transactionType);
  }
  /** Clears the 'amount' field, the 'has' method for this field will now return false */
  public void clearAmount() {
    genClient.clear(CacheKey.amount);
  }
  /** Clears the 'cashbackAmount' field, the 'has' method for this field will now return false */
  public void clearCashbackAmount() {
    genClient.clear(CacheKey.cashbackAmount);
  }
  /** Clears the 'currencyCode' field, the 'has' method for this field will now return false */
  public void clearCurrencyCode() {
    genClient.clear(CacheKey.currencyCode);
  }
  /** Clears the 'currencyExponent' field, the 'has' method for this field will now return false */
  public void clearCurrencyExponent() {
    genClient.clear(CacheKey.currencyExponent);
  }
  /** Clears the 'emvData' field, the 'has' method for this field will now return false */
  public void clearEmvData() {
    genClient.clear(CacheKey.emvData);
  }
  /** Clears the 'terminalRiskManagement' field, the 'has' method for this field will now return false */
  public void clearTerminalRiskManagement() {
    genClient.clear(CacheKey.terminalRiskManagement);
  }
  /** Clears the 'emvTagList' field, the 'has' method for this field will now return false */
  public void clearEmvTagList() {
    genClient.clear(CacheKey.emvTagList);
  }
  /** Clears the 'panExceptionList' field, the 'has' method for this field will now return false */
  public void clearPanExceptionList() {
    genClient.clear(CacheKey.panExceptionList);
  }
  /** Clears the 'pinBypassDisabled' field, the 'has' method for this field will now return false */
  public void clearPinBypassDisabled() {
    genClient.clear(CacheKey.pinBypassDisabled);
  }
  /** Clears the 'disableOfflineApproval' field, the 'has' method for this field will now return false */
  public void clearDisableOfflineApproval() {
    genClient.clear(CacheKey.disableOfflineApproval);
  }
  /** Clears the 'pinCaptureTimeout' field, the 'has' method for this field will now return false */
  public void clearPinCaptureTimeout() {
    genClient.clear(CacheKey.pinCaptureTimeout);
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
  public GoOnChipRequest copyChanges() {
    GoOnChipRequest copy = new GoOnChipRequest();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(GoOnChipRequest src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new GoOnChipRequest(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<GoOnChipRequest> CREATOR = new android.os.Parcelable.Creator<GoOnChipRequest>() {
    @Override
    public GoOnChipRequest createFromParcel(android.os.Parcel in) {
      GoOnChipRequest instance = new GoOnChipRequest(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public GoOnChipRequest[] newArray(int size) {
      return new GoOnChipRequest[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<GoOnChipRequest> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<GoOnChipRequest>() {
    public Class<GoOnChipRequest> getCreatedClass() {
      return GoOnChipRequest.class;
    }

    @Override
    public GoOnChipRequest create(org.json.JSONObject jsonObject) {
      return new GoOnChipRequest(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean TRANSACTIONTYPE_IS_REQUIRED = false;
    public static final boolean AMOUNT_IS_REQUIRED = false;
    public static final boolean CASHBACKAMOUNT_IS_REQUIRED = false;
    public static final boolean CURRENCYCODE_IS_REQUIRED = false;
    public static final boolean CURRENCYEXPONENT_IS_REQUIRED = false;
    public static final boolean EMVDATA_IS_REQUIRED = false;
    public static final boolean TERMINALRISKMANAGEMENT_IS_REQUIRED = false;
    public static final boolean EMVTAGLIST_IS_REQUIRED = false;
    public static final boolean PANEXCEPTIONLIST_IS_REQUIRED = false;
    public static final boolean PINBYPASSDISABLED_IS_REQUIRED = false;
    public static final boolean DISABLEOFFLINEAPPROVAL_IS_REQUIRED = false;
    public static final boolean PINCAPTURETIMEOUT_IS_REQUIRED = false;
  }

}
