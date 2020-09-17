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

package com.clover.sdk.v3.remotepay;

import com.clover.sdk.GenericClient;

/**
 * This is an auto-generated Clover data object.
 * <p>
 * <h3>Fields</h3>
 * <ul>
 * <li>{@link #getOrderId orderId}</li>
 * <li>{@link #getPaymentId paymentId}</li>
 * <li>{@link #getEmployeeId employeeId}</li>
 * <li>{@link #getVoidReason voidReason}</li>
 * <li>{@link #getDisablePrinting disablePrinting}</li>
 * <li>{@link #getDisableReceiptSelection disableReceiptSelection}</li>
 * <li>{@link #getExtras extras}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class VoidPaymentRequest extends com.clover.sdk.v3.remotepay.BaseRequest {

  /**
   * Unique identifier
   */
  public java.lang.String getOrderId() {
    return genClient.cacheGet(CacheKey.orderId);
  }

  /**
   * Unique identifier
   */
  public java.lang.String getPaymentId() {
    return genClient.cacheGet(CacheKey.paymentId);
  }

  /**
   * Unique identifier
   */
  public java.lang.String getEmployeeId() {
    return genClient.cacheGet(CacheKey.employeeId);
  }

  /**
   * Reason for void
   */
  public java.lang.String getVoidReason() {
    return genClient.cacheGet(CacheKey.voidReason);
  }

  /**
   * If true, then do not print using the clover printer.  Return print information.
   */
  public java.lang.Boolean getDisablePrinting() {
    return genClient.cacheGet(CacheKey.disablePrinting);
  }

  /**
   * Do not show the receipt options screen
   */
  public java.lang.Boolean getDisableReceiptSelection() {
    return genClient.cacheGet(CacheKey.disableReceiptSelection);
  }

  /**
   * Extra pass-through data used by external systems.
   */
  public java.util.Map<java.lang.String,java.lang.String> getExtras() {
    return genClient.cacheGet(CacheKey.extras);
  }

  /**
   * Identifier for the request
   */
  @Override
  public java.lang.String getRequestId() {
    return genClient.cacheGet(CacheKey.requestId);
  }

  /**
   * Identifier for the version
   */
  @Override
  public java.lang.Integer getVersion() {
    return genClient.cacheGet(CacheKey.version);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    orderId
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    paymentId
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    employeeId
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    voidReason
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    disablePrinting
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Boolean.class)),
    disableReceiptSelection
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Boolean.class)),
    extras
        (com.clover.sdk.extractors.MapExtractionStrategy.instance()),
    requestId
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    version
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Integer.class)),
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

  private final GenericClient<VoidPaymentRequest> genClient;

  /**
   * Constructs a new empty instance.
   */
  public VoidPaymentRequest() {
    super(false);
    genClient = new GenericClient<VoidPaymentRequest>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected VoidPaymentRequest(boolean noInit) {
    super(false);
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public VoidPaymentRequest(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public VoidPaymentRequest(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public VoidPaymentRequest(VoidPaymentRequest src) {
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

    genClient.validateCloverId(CacheKey.orderId, getOrderId());

    genClient.validateCloverId(CacheKey.paymentId, getPaymentId());

    genClient.validateCloverId(CacheKey.employeeId, getEmployeeId());
    genClient.validateCloverId(CacheKey.requestId, getRequestId());
  }

  /** Checks whether the 'orderId' field is set and is not null */
  public boolean isNotNullOrderId() {
    return genClient.cacheValueIsNotNull(CacheKey.orderId);
  }

  /** Checks whether the 'paymentId' field is set and is not null */
  public boolean isNotNullPaymentId() {
    return genClient.cacheValueIsNotNull(CacheKey.paymentId);
  }

  /** Checks whether the 'employeeId' field is set and is not null */
  public boolean isNotNullEmployeeId() {
    return genClient.cacheValueIsNotNull(CacheKey.employeeId);
  }

  /** Checks whether the 'voidReason' field is set and is not null */
  public boolean isNotNullVoidReason() {
    return genClient.cacheValueIsNotNull(CacheKey.voidReason);
  }

  /** Checks whether the 'disablePrinting' field is set and is not null */
  public boolean isNotNullDisablePrinting() {
    return genClient.cacheValueIsNotNull(CacheKey.disablePrinting);
  }

  /** Checks whether the 'disableReceiptSelection' field is set and is not null */
  public boolean isNotNullDisableReceiptSelection() {
    return genClient.cacheValueIsNotNull(CacheKey.disableReceiptSelection);
  }

  /** Checks whether the 'extras' field is set and is not null */
  public boolean isNotNullExtras() {
    return genClient.cacheValueIsNotNull(CacheKey.extras);
  }

  /** Checks whether the 'extras' field is set and is not null and is not empty */
  public boolean isNotEmptyExtras() { return isNotNullExtras() && !getExtras().isEmpty(); }

  /** Checks whether the 'requestId' field is set and is not null */
  @Override
  public boolean isNotNullRequestId() {
    return genClient.cacheValueIsNotNull(CacheKey.requestId);
  }

  /** Checks whether the 'version' field is set and is not null */
  @Override
  public boolean isNotNullVersion() {
    return genClient.cacheValueIsNotNull(CacheKey.version);
  }



  /** Checks whether the 'orderId' field has been set, however the value could be null */
  public boolean hasOrderId() {
    return genClient.cacheHasKey(CacheKey.orderId);
  }

  /** Checks whether the 'paymentId' field has been set, however the value could be null */
  public boolean hasPaymentId() {
    return genClient.cacheHasKey(CacheKey.paymentId);
  }

  /** Checks whether the 'employeeId' field has been set, however the value could be null */
  public boolean hasEmployeeId() {
    return genClient.cacheHasKey(CacheKey.employeeId);
  }

  /** Checks whether the 'voidReason' field has been set, however the value could be null */
  public boolean hasVoidReason() {
    return genClient.cacheHasKey(CacheKey.voidReason);
  }

  /** Checks whether the 'disablePrinting' field has been set, however the value could be null */
  public boolean hasDisablePrinting() {
    return genClient.cacheHasKey(CacheKey.disablePrinting);
  }

  /** Checks whether the 'disableReceiptSelection' field has been set, however the value could be null */
  public boolean hasDisableReceiptSelection() {
    return genClient.cacheHasKey(CacheKey.disableReceiptSelection);
  }

  /** Checks whether the 'extras' field has been set, however the value could be null */
  public boolean hasExtras() {
    return genClient.cacheHasKey(CacheKey.extras);
  }

  /** Checks whether the 'requestId' field has been set, however the value could be null */
  @Override
  public boolean hasRequestId() {
    return genClient.cacheHasKey(CacheKey.requestId);
  }

  /** Checks whether the 'version' field has been set, however the value could be null */
  @Override
  public boolean hasVersion() {
    return genClient.cacheHasKey(CacheKey.version);
  }


  /**
   * Sets the field 'orderId'.
   */
  public VoidPaymentRequest setOrderId(java.lang.String orderId) {
    return genClient.setOther(orderId, CacheKey.orderId);
  }

  /**
   * Sets the field 'paymentId'.
   */
  public VoidPaymentRequest setPaymentId(java.lang.String paymentId) {
    return genClient.setOther(paymentId, CacheKey.paymentId);
  }

  /**
   * Sets the field 'employeeId'.
   */
  public VoidPaymentRequest setEmployeeId(java.lang.String employeeId) {
    return genClient.setOther(employeeId, CacheKey.employeeId);
  }

  /**
   * Sets the field 'voidReason'.
   */
  public VoidPaymentRequest setVoidReason(java.lang.String voidReason) {
    return genClient.setOther(voidReason, CacheKey.voidReason);
  }

  /**
   * Sets the field 'disablePrinting'.
   */
  public VoidPaymentRequest setDisablePrinting(java.lang.Boolean disablePrinting) {
    return genClient.setOther(disablePrinting, CacheKey.disablePrinting);
  }

  /**
   * Sets the field 'disableReceiptSelection'.
   */
  public VoidPaymentRequest setDisableReceiptSelection(java.lang.Boolean disableReceiptSelection) {
    return genClient.setOther(disableReceiptSelection, CacheKey.disableReceiptSelection);
  }

  /**
   * Sets the field 'extras'.
   */
  public VoidPaymentRequest setExtras(java.util.Map<java.lang.String,java.lang.String> extras) {
    return genClient.setOther(extras, CacheKey.extras);
  }

  /**
   * Sets the field 'requestId'.
   */
  @Override
  public BaseRequest setRequestId(java.lang.String requestId) {
    return genClient.setOther(requestId, CacheKey.requestId);
  }

  /**
   * Sets the field 'version'.
   */
  @Override
  public BaseRequest setVersion(java.lang.Integer version) {
    return genClient.setOther(version, CacheKey.version);
  }


  /** Clears the 'orderId' field, the 'has' method for this field will now return false */
  public void clearOrderId() {
    genClient.clear(CacheKey.orderId);
  }
  /** Clears the 'paymentId' field, the 'has' method for this field will now return false */
  public void clearPaymentId() {
    genClient.clear(CacheKey.paymentId);
  }
  /** Clears the 'employeeId' field, the 'has' method for this field will now return false */
  public void clearEmployeeId() {
    genClient.clear(CacheKey.employeeId);
  }
  /** Clears the 'voidReason' field, the 'has' method for this field will now return false */
  public void clearVoidReason() {
    genClient.clear(CacheKey.voidReason);
  }
  /** Clears the 'disablePrinting' field, the 'has' method for this field will now return false */
  public void clearDisablePrinting() {
    genClient.clear(CacheKey.disablePrinting);
  }
  /** Clears the 'disableReceiptSelection' field, the 'has' method for this field will now return false */
  public void clearDisableReceiptSelection() {
    genClient.clear(CacheKey.disableReceiptSelection);
  }
  /** Clears the 'extras' field, the 'has' method for this field will now return false */
  public void clearExtras() {
    genClient.clear(CacheKey.extras);
  }
  /** Clears the 'requestId' field, the 'has' method for this field will now return false */
  @Override
  public void clearRequestId() {
    genClient.clear(CacheKey.requestId);
  }
  /** Clears the 'version' field, the 'has' method for this field will now return false */
  @Override
  public void clearVersion() {
    genClient.clear(CacheKey.version);
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
  public VoidPaymentRequest copyChanges() {
    VoidPaymentRequest copy = new VoidPaymentRequest();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(VoidPaymentRequest src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new VoidPaymentRequest(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<VoidPaymentRequest> CREATOR = new android.os.Parcelable.Creator<VoidPaymentRequest>() {
    @Override
    public VoidPaymentRequest createFromParcel(android.os.Parcel in) {
      VoidPaymentRequest instance = new VoidPaymentRequest(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public VoidPaymentRequest[] newArray(int size) {
      return new VoidPaymentRequest[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<VoidPaymentRequest> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<VoidPaymentRequest>() {
    public Class<VoidPaymentRequest> getCreatedClass() {
      return VoidPaymentRequest.class;
    }

    @Override
    public VoidPaymentRequest create(org.json.JSONObject jsonObject) {
      return new VoidPaymentRequest(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean ORDERID_IS_REQUIRED = false;
    public static final long ORDERID_MAX_LEN = 13;
    public static final boolean PAYMENTID_IS_REQUIRED = false;
    public static final long PAYMENTID_MAX_LEN = 13;
    public static final boolean EMPLOYEEID_IS_REQUIRED = false;
    public static final long EMPLOYEEID_MAX_LEN = 13;
    public static final boolean VOIDREASON_IS_REQUIRED = false;
    public static final boolean DISABLEPRINTING_IS_REQUIRED = false;
    public static final boolean DISABLERECEIPTSELECTION_IS_REQUIRED = false;
    public static final boolean EXTRAS_IS_REQUIRED = false;
    public static final boolean REQUESTID_IS_REQUIRED = false;
    public static final long REQUESTID_MAX_LEN = 13;
    public static final boolean VERSION_IS_REQUIRED = false;
  }

}
