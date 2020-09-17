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
 * <li>{@link #getRequestSuccessful requestSuccessful}</li>
 * <li>{@link #getResponseErrorMessage responseErrorMessage}</li>
 * <li>{@link #getPayment payment}</li>
 * <li>{@link #getClientData clientData}</li>
 * <li>{@link #getSyncPaymentObject syncPaymentObject}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class PaymentResponse extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  public java.lang.Boolean getRequestSuccessful() {
    return genClient.cacheGet(CacheKey.requestSuccessful);
  }

  public java.lang.String getResponseErrorMessage() {
    return genClient.cacheGet(CacheKey.responseErrorMessage);
  }

  public com.clover.sdk.v3.payments.Payment getPayment() {
    return genClient.cacheGet(CacheKey.payment);
  }

  /**
   * Additional data sent back from the gateway
   */
  public java.util.Map<java.lang.String,java.lang.String> getClientData() {
    return genClient.cacheGet(CacheKey.clientData);
  }

  public java.lang.Boolean getSyncPaymentObject() {
    return genClient.cacheGet(CacheKey.syncPaymentObject);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    requestSuccessful
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Boolean.class)),
    responseErrorMessage
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    payment
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.payments.Payment.JSON_CREATOR)),
    clientData
        (com.clover.sdk.extractors.MapExtractionStrategy.instance()),
    syncPaymentObject
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Boolean.class)),
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

  private final GenericClient<PaymentResponse> genClient;

  /**
   * Constructs a new empty instance.
   */
  public PaymentResponse() {
    genClient = new GenericClient<PaymentResponse>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected PaymentResponse(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public PaymentResponse(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public PaymentResponse(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public PaymentResponse(PaymentResponse src) {
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

  /** Checks whether the 'requestSuccessful' field is set and is not null */
  public boolean isNotNullRequestSuccessful() {
    return genClient.cacheValueIsNotNull(CacheKey.requestSuccessful);
  }

  /** Checks whether the 'responseErrorMessage' field is set and is not null */
  public boolean isNotNullResponseErrorMessage() {
    return genClient.cacheValueIsNotNull(CacheKey.responseErrorMessage);
  }

  /** Checks whether the 'payment' field is set and is not null */
  public boolean isNotNullPayment() {
    return genClient.cacheValueIsNotNull(CacheKey.payment);
  }

  /** Checks whether the 'clientData' field is set and is not null */
  public boolean isNotNullClientData() {
    return genClient.cacheValueIsNotNull(CacheKey.clientData);
  }

  /** Checks whether the 'clientData' field is set and is not null and is not empty */
  public boolean isNotEmptyClientData() { return isNotNullClientData() && !getClientData().isEmpty(); }

  /** Checks whether the 'syncPaymentObject' field is set and is not null */
  public boolean isNotNullSyncPaymentObject() {
    return genClient.cacheValueIsNotNull(CacheKey.syncPaymentObject);
  }



  /** Checks whether the 'requestSuccessful' field has been set, however the value could be null */
  public boolean hasRequestSuccessful() {
    return genClient.cacheHasKey(CacheKey.requestSuccessful);
  }

  /** Checks whether the 'responseErrorMessage' field has been set, however the value could be null */
  public boolean hasResponseErrorMessage() {
    return genClient.cacheHasKey(CacheKey.responseErrorMessage);
  }

  /** Checks whether the 'payment' field has been set, however the value could be null */
  public boolean hasPayment() {
    return genClient.cacheHasKey(CacheKey.payment);
  }

  /** Checks whether the 'clientData' field has been set, however the value could be null */
  public boolean hasClientData() {
    return genClient.cacheHasKey(CacheKey.clientData);
  }

  /** Checks whether the 'syncPaymentObject' field has been set, however the value could be null */
  public boolean hasSyncPaymentObject() {
    return genClient.cacheHasKey(CacheKey.syncPaymentObject);
  }


  /**
   * Sets the field 'requestSuccessful'.
   */
  public PaymentResponse setRequestSuccessful(java.lang.Boolean requestSuccessful) {
    return genClient.setOther(requestSuccessful, CacheKey.requestSuccessful);
  }

  /**
   * Sets the field 'responseErrorMessage'.
   */
  public PaymentResponse setResponseErrorMessage(java.lang.String responseErrorMessage) {
    return genClient.setOther(responseErrorMessage, CacheKey.responseErrorMessage);
  }

  /**
   * Sets the field 'payment'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public PaymentResponse setPayment(com.clover.sdk.v3.payments.Payment payment) {
    return genClient.setRecord(payment, CacheKey.payment);
  }

  /**
   * Sets the field 'clientData'.
   */
  public PaymentResponse setClientData(java.util.Map<java.lang.String,java.lang.String> clientData) {
    return genClient.setOther(clientData, CacheKey.clientData);
  }

  /**
   * Sets the field 'syncPaymentObject'.
   */
  public PaymentResponse setSyncPaymentObject(java.lang.Boolean syncPaymentObject) {
    return genClient.setOther(syncPaymentObject, CacheKey.syncPaymentObject);
  }


  /** Clears the 'requestSuccessful' field, the 'has' method for this field will now return false */
  public void clearRequestSuccessful() {
    genClient.clear(CacheKey.requestSuccessful);
  }
  /** Clears the 'responseErrorMessage' field, the 'has' method for this field will now return false */
  public void clearResponseErrorMessage() {
    genClient.clear(CacheKey.responseErrorMessage);
  }
  /** Clears the 'payment' field, the 'has' method for this field will now return false */
  public void clearPayment() {
    genClient.clear(CacheKey.payment);
  }
  /** Clears the 'clientData' field, the 'has' method for this field will now return false */
  public void clearClientData() {
    genClient.clear(CacheKey.clientData);
  }
  /** Clears the 'syncPaymentObject' field, the 'has' method for this field will now return false */
  public void clearSyncPaymentObject() {
    genClient.clear(CacheKey.syncPaymentObject);
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
  public PaymentResponse copyChanges() {
    PaymentResponse copy = new PaymentResponse();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(PaymentResponse src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new PaymentResponse(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<PaymentResponse> CREATOR = new android.os.Parcelable.Creator<PaymentResponse>() {
    @Override
    public PaymentResponse createFromParcel(android.os.Parcel in) {
      PaymentResponse instance = new PaymentResponse(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public PaymentResponse[] newArray(int size) {
      return new PaymentResponse[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<PaymentResponse> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<PaymentResponse>() {
    public Class<PaymentResponse> getCreatedClass() {
      return PaymentResponse.class;
    }

    @Override
    public PaymentResponse create(org.json.JSONObject jsonObject) {
      return new PaymentResponse(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean REQUESTSUCCESSFUL_IS_REQUIRED = false;
    public static final boolean RESPONSEERRORMESSAGE_IS_REQUIRED = false;
    public static final boolean PAYMENT_IS_REQUIRED = false;
    public static final boolean CLIENTDATA_IS_REQUIRED = false;
    public static final boolean SYNCPAYMENTOBJECT_IS_REQUIRED = false;
  }

}
