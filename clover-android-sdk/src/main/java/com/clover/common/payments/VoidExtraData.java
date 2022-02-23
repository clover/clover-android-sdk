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

package com.clover.common.payments;

import com.clover.sdk.GenericClient;
import com.clover.sdk.GenericParcelable;

/**
 * This used to be a genearted class and its a part of avro-internal schema. The main purpose of this class is to serve as a conduit so that we can
 * add any new objects or data without changing the internal void api's. As of the creation of this object we were only sending the TerminalManagementComponent
 * which was a part of internal package. So the avsc file was created in avro-internal schema. As of this update we wanted to add voidReasonDetails which
 * has more details about why we are trying to void a transaction this is defined in the external schema. We have a seperate task to move all the
 * TerminalManagement details out of the internal schema into external schema. Waiting for that task to complete or making that change as part of this
 * change since is not possible since its a pretty big change. That is the reason we are making this class as a manual.
 * <p>
 * <h3>Fields</h3>
 * <ul>
 * <li>{@link #getTerminalManagementComponents terminalManagementComponents}</li>
 * <li>{@link #getVoidReasonDetails voidReasonDetails}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class VoidExtraData extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  public java.util.List<TerminalManagementComponent> getTerminalManagementComponents() {
    return genClient.cacheGet(CacheKey.terminalManagementComponents);
  }

  public com.clover.sdk.v3.order.VoidReasonDetails getVoidReasonDetails() {
    return genClient.cacheGet(CacheKey.voidReasonDetails);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    terminalManagementComponents
        (com.clover.sdk.extractors.RecordListExtractionStrategy.instance(TerminalManagementComponent.JSON_CREATOR)),
    voidReasonDetails
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.order.VoidReasonDetails.JSON_CREATOR)),
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

  private final GenericClient<VoidExtraData> genClient;

  /**
   * Constructs a new empty instance.
   */
  public VoidExtraData() {
    genClient = new GenericClient<VoidExtraData>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected VoidExtraData(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public VoidExtraData(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public VoidExtraData(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public VoidExtraData(VoidExtraData src) {
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

  /** Checks whether the 'terminalManagementComponents' field is set and is not null */
  public boolean isNotNullTerminalManagementComponents() {
    return genClient.cacheValueIsNotNull(CacheKey.terminalManagementComponents);
  }

  /** Checks whether the 'terminalManagementComponents' field is set and is not null and is not empty */
  public boolean isNotEmptyTerminalManagementComponents() { return isNotNullTerminalManagementComponents() && !getTerminalManagementComponents().isEmpty(); }

  /** Checks whether the 'voidReasonDetails' field is set and is not null */
  public boolean isNotNullVoidReasonDetails() {
    return genClient.cacheValueIsNotNull(CacheKey.voidReasonDetails);
  }



  /** Checks whether the 'terminalManagementComponents' field has been set, however the value could be null */
  public boolean hasTerminalManagementComponents() {
    return genClient.cacheHasKey(CacheKey.terminalManagementComponents);
  }

  /** Checks whether the 'voidReasonDetails' field has been set, however the value could be null */
  public boolean hasVoidReasonDetails() {
    return genClient.cacheHasKey(CacheKey.voidReasonDetails);
  }


  /**
   * Sets the field 'terminalManagementComponents'.
   *
   * Nulls in the given List are skipped. List parameter is copied, so it will not reflect any changes, but objects inside it will.
   */
  public VoidExtraData setTerminalManagementComponents(java.util.List<TerminalManagementComponent> terminalManagementComponents) {
    return genClient.setArrayRecord(terminalManagementComponents, CacheKey.terminalManagementComponents);
  }

  /**
   * Sets the field 'voidReasonDetails'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public VoidExtraData setVoidReasonDetails(com.clover.sdk.v3.order.VoidReasonDetails voidReasonDetails) {
    return genClient.setRecord(voidReasonDetails, CacheKey.voidReasonDetails);
  }


  /** Clears the 'terminalManagementComponents' field, the 'has' method for this field will now return false */
  public void clearTerminalManagementComponents() {
    genClient.clear(CacheKey.terminalManagementComponents);
  }
  /** Clears the 'voidReasonDetails' field, the 'has' method for this field will now return false */
  public void clearVoidReasonDetails() {
    genClient.clear(CacheKey.voidReasonDetails);
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
  public VoidExtraData copyChanges() {
    VoidExtraData copy = new VoidExtraData();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(VoidExtraData src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new VoidExtraData(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<VoidExtraData> CREATOR = new android.os.Parcelable.Creator<VoidExtraData>() {
    @Override
    public VoidExtraData createFromParcel(android.os.Parcel in) {
      VoidExtraData instance = new VoidExtraData(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public VoidExtraData[] newArray(int size) {
      return new VoidExtraData[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<VoidExtraData> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<VoidExtraData>() {
    public Class<VoidExtraData> getCreatedClass() {
      return VoidExtraData.class;
    }

    @Override
    public VoidExtraData create(org.json.JSONObject jsonObject) {
      return new VoidExtraData(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean TERMINALMANAGEMENTCOMPONENTS_IS_REQUIRED = false;
    public static final boolean VOIDREASONDETAILS_IS_REQUIRED = false;
  }

}
