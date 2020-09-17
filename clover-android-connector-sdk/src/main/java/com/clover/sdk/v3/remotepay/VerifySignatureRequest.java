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
 * <li>{@link #getPayment payment}</li>
 * <li>{@link #getSignature signature}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class VerifySignatureRequest extends com.clover.sdk.v3.remotepay.BaseRequest {

  /**
   * Payment that the signature is verifying for
   */
  public com.clover.sdk.v3.payments.Payment getPayment() {
    return genClient.cacheGet(CacheKey.payment);
  }

  public com.clover.sdk.v3.base.Signature getSignature() {
    return genClient.cacheGet(CacheKey.signature);
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
    payment
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.payments.Payment.JSON_CREATOR)),
    signature
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.base.Signature.JSON_CREATOR)),
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

  private final GenericClient<VerifySignatureRequest> genClient;

  /**
   * Constructs a new empty instance.
   */
  public VerifySignatureRequest() {
    super(false);
    genClient = new GenericClient<VerifySignatureRequest>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected VerifySignatureRequest(boolean noInit) {
    super(false);
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public VerifySignatureRequest(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public VerifySignatureRequest(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public VerifySignatureRequest(VerifySignatureRequest src) {
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
    genClient.validateCloverId(CacheKey.requestId, getRequestId());
  }

  /** Checks whether the 'payment' field is set and is not null */
  public boolean isNotNullPayment() {
    return genClient.cacheValueIsNotNull(CacheKey.payment);
  }

  /** Checks whether the 'signature' field is set and is not null */
  public boolean isNotNullSignature() {
    return genClient.cacheValueIsNotNull(CacheKey.signature);
  }

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



  /** Checks whether the 'payment' field has been set, however the value could be null */
  public boolean hasPayment() {
    return genClient.cacheHasKey(CacheKey.payment);
  }

  /** Checks whether the 'signature' field has been set, however the value could be null */
  public boolean hasSignature() {
    return genClient.cacheHasKey(CacheKey.signature);
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
   * Sets the field 'payment'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public VerifySignatureRequest setPayment(com.clover.sdk.v3.payments.Payment payment) {
    return genClient.setRecord(payment, CacheKey.payment);
  }

  /**
   * Sets the field 'signature'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public VerifySignatureRequest setSignature(com.clover.sdk.v3.base.Signature signature) {
    return genClient.setRecord(signature, CacheKey.signature);
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


  /** Clears the 'payment' field, the 'has' method for this field will now return false */
  public void clearPayment() {
    genClient.clear(CacheKey.payment);
  }
  /** Clears the 'signature' field, the 'has' method for this field will now return false */
  public void clearSignature() {
    genClient.clear(CacheKey.signature);
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
  public VerifySignatureRequest copyChanges() {
    VerifySignatureRequest copy = new VerifySignatureRequest();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(VerifySignatureRequest src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new VerifySignatureRequest(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<VerifySignatureRequest> CREATOR = new android.os.Parcelable.Creator<VerifySignatureRequest>() {
    @Override
    public VerifySignatureRequest createFromParcel(android.os.Parcel in) {
      VerifySignatureRequest instance = new VerifySignatureRequest(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public VerifySignatureRequest[] newArray(int size) {
      return new VerifySignatureRequest[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<VerifySignatureRequest> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<VerifySignatureRequest>() {
    public Class<VerifySignatureRequest> getCreatedClass() {
      return VerifySignatureRequest.class;
    }

    @Override
    public VerifySignatureRequest create(org.json.JSONObject jsonObject) {
      return new VerifySignatureRequest(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean PAYMENT_IS_REQUIRED = false;
    public static final boolean SIGNATURE_IS_REQUIRED = false;
    public static final boolean REQUESTID_IS_REQUIRED = false;
    public static final long REQUESTID_MAX_LEN = 13;
    public static final boolean VERSION_IS_REQUIRED = false;
  }

}
