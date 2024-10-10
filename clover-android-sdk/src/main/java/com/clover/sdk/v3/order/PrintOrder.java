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

package com.clover.sdk.v3.order;

import com.clover.sdk.GenericClient;
import com.clover.sdk.GenericParcelable;

/**
 * This is an auto-generated Clover data object.
 * <p>
 * <h3>Fields</h3>
 * <ul>
 * <li>{@link #getId id}</li>
 * <li>{@link #getOrderRef orderRef}</li>
 * <li>{@link #getDeviceRef deviceRef}</li>
 * <li>{@link #getCategory category}</li>
 * <li>{@link #getState state}</li>
 * <li>{@link #getOrderSnapshot orderSnapshot}</li>
 * <li>{@link #getCreatedTime createdTime}</li>
 * <li>{@link #getModifiedTime modifiedTime}</li>
 * <li>{@link #getDeletedTime deletedTime}</li>
 * <li>{@link #getPrintTime printTime}</li>
 * <li>{@link #getClientPrintingTime clientPrintingTime}</li>
 * <li>{@link #getErrorMessage errorMessage}</li>
 * </ul>
 * <p>
 * @see com.clover.sdk.v3.order.IOrderService
 */
@SuppressWarnings("all")
public class PrintOrder extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  /**
   * Unique identifier
   */
  public java.lang.String getId() {
    return genClient.cacheGet(CacheKey.id);
  }

  /**
   * Reference to the order to be printed
   */
  public com.clover.sdk.v3.base.Reference getOrderRef() {
    return genClient.cacheGet(CacheKey.orderRef);
  }

  /**
   * The printing device. A 128-bit UUID, not a normal base-13 Clover ID.
   */
  public com.clover.sdk.v3.base.Reference getDeviceRef() {
    return genClient.cacheGet(CacheKey.deviceRef);
  }

  /**
   * The print category
   */
  public com.clover.sdk.v3.printer.PrintCategory getCategory() {
    return genClient.cacheGet(CacheKey.category);
  }

  /**
   * print event state
   */
  public com.clover.sdk.v3.order.PrintState getState() {
    return genClient.cacheGet(CacheKey.state);
  }

  /**
   * Snapshot of the order at the time of print request
   */
  public java.lang.String getOrderSnapshot() {
    return genClient.cacheGet(CacheKey.orderSnapshot);
  }

  /**
   * Timestamp when the print event was created
   */
  public java.lang.Long getCreatedTime() {
    return genClient.cacheGet(CacheKey.createdTime);
  }

  /**
   * Timestamp when the print event was last modified
   */
  public java.lang.Long getModifiedTime() {
    return genClient.cacheGet(CacheKey.modifiedTime);
  }

  /**
   * Timestamp when the print event was last deleted
   */
  public java.lang.Long getDeletedTime() {
    return genClient.cacheGet(CacheKey.deletedTime);
  }

  /**
   * Timestamp when the print event needs to fire to printer
   */
  public java.lang.Long getPrintTime() {
    return genClient.cacheGet(CacheKey.printTime);
  }

  /**
   * Timestamp when the device client starts printing, for internal tracing only.
   */
  public java.lang.Long getClientPrintingTime() {
    return genClient.cacheGet(CacheKey.clientPrintingTime);
  }

  /**
   * Error message when device client fails to print the event, for internal tracing only.
   */
  public java.lang.String getErrorMessage() {
    return genClient.cacheGet(CacheKey.errorMessage);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    id
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    orderRef
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.base.Reference.JSON_CREATOR)),
    deviceRef
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.base.Reference.JSON_CREATOR)),
    category
        (com.clover.sdk.extractors.EnumExtractionStrategy.instance(com.clover.sdk.v3.printer.PrintCategory.class)),
    state
        (com.clover.sdk.extractors.EnumExtractionStrategy.instance(com.clover.sdk.v3.order.PrintState.class)),
    orderSnapshot
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    createdTime
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    modifiedTime
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    deletedTime
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    printTime
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    clientPrintingTime
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    errorMessage
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

  private final GenericClient<PrintOrder> genClient;

  /**
   * Constructs a new empty instance.
   */
  public PrintOrder() {
    genClient = new GenericClient<PrintOrder>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected PrintOrder(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public PrintOrder(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public PrintOrder(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public PrintOrder(PrintOrder src) {
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
    genClient.validateLength(CacheKey.id, getId(), 13);

    genClient.validateLength(CacheKey.orderSnapshot, getOrderSnapshot(), 5242880);

    genClient.validateLength(CacheKey.errorMessage, getErrorMessage(), 65535);
    genClient.validateReferences(CacheKey.orderRef);
  }

  /** Checks whether the 'id' field is set and is not null */
  public boolean isNotNullId() {
    return genClient.cacheValueIsNotNull(CacheKey.id);
  }

  /** Checks whether the 'orderRef' field is set and is not null */
  public boolean isNotNullOrderRef() {
    return genClient.cacheValueIsNotNull(CacheKey.orderRef);
  }

  /** Checks whether the 'deviceRef' field is set and is not null */
  public boolean isNotNullDeviceRef() {
    return genClient.cacheValueIsNotNull(CacheKey.deviceRef);
  }

  /** Checks whether the 'category' field is set and is not null */
  public boolean isNotNullCategory() {
    return genClient.cacheValueIsNotNull(CacheKey.category);
  }

  /** Checks whether the 'state' field is set and is not null */
  public boolean isNotNullState() {
    return genClient.cacheValueIsNotNull(CacheKey.state);
  }

  /** Checks whether the 'orderSnapshot' field is set and is not null */
  public boolean isNotNullOrderSnapshot() {
    return genClient.cacheValueIsNotNull(CacheKey.orderSnapshot);
  }

  /** Checks whether the 'createdTime' field is set and is not null */
  public boolean isNotNullCreatedTime() {
    return genClient.cacheValueIsNotNull(CacheKey.createdTime);
  }

  /** Checks whether the 'modifiedTime' field is set and is not null */
  public boolean isNotNullModifiedTime() {
    return genClient.cacheValueIsNotNull(CacheKey.modifiedTime);
  }

  /** Checks whether the 'deletedTime' field is set and is not null */
  public boolean isNotNullDeletedTime() {
    return genClient.cacheValueIsNotNull(CacheKey.deletedTime);
  }

  /** Checks whether the 'printTime' field is set and is not null */
  public boolean isNotNullPrintTime() {
    return genClient.cacheValueIsNotNull(CacheKey.printTime);
  }

  /** Checks whether the 'clientPrintingTime' field is set and is not null */
  public boolean isNotNullClientPrintingTime() {
    return genClient.cacheValueIsNotNull(CacheKey.clientPrintingTime);
  }

  /** Checks whether the 'errorMessage' field is set and is not null */
  public boolean isNotNullErrorMessage() {
    return genClient.cacheValueIsNotNull(CacheKey.errorMessage);
  }



  /** Checks whether the 'id' field has been set, however the value could be null */
  public boolean hasId() {
    return genClient.cacheHasKey(CacheKey.id);
  }

  /** Checks whether the 'orderRef' field has been set, however the value could be null */
  public boolean hasOrderRef() {
    return genClient.cacheHasKey(CacheKey.orderRef);
  }

  /** Checks whether the 'deviceRef' field has been set, however the value could be null */
  public boolean hasDeviceRef() {
    return genClient.cacheHasKey(CacheKey.deviceRef);
  }

  /** Checks whether the 'category' field has been set, however the value could be null */
  public boolean hasCategory() {
    return genClient.cacheHasKey(CacheKey.category);
  }

  /** Checks whether the 'state' field has been set, however the value could be null */
  public boolean hasState() {
    return genClient.cacheHasKey(CacheKey.state);
  }

  /** Checks whether the 'orderSnapshot' field has been set, however the value could be null */
  public boolean hasOrderSnapshot() {
    return genClient.cacheHasKey(CacheKey.orderSnapshot);
  }

  /** Checks whether the 'createdTime' field has been set, however the value could be null */
  public boolean hasCreatedTime() {
    return genClient.cacheHasKey(CacheKey.createdTime);
  }

  /** Checks whether the 'modifiedTime' field has been set, however the value could be null */
  public boolean hasModifiedTime() {
    return genClient.cacheHasKey(CacheKey.modifiedTime);
  }

  /** Checks whether the 'deletedTime' field has been set, however the value could be null */
  public boolean hasDeletedTime() {
    return genClient.cacheHasKey(CacheKey.deletedTime);
  }

  /** Checks whether the 'printTime' field has been set, however the value could be null */
  public boolean hasPrintTime() {
    return genClient.cacheHasKey(CacheKey.printTime);
  }

  /** Checks whether the 'clientPrintingTime' field has been set, however the value could be null */
  public boolean hasClientPrintingTime() {
    return genClient.cacheHasKey(CacheKey.clientPrintingTime);
  }

  /** Checks whether the 'errorMessage' field has been set, however the value could be null */
  public boolean hasErrorMessage() {
    return genClient.cacheHasKey(CacheKey.errorMessage);
  }


  /**
   * Sets the field 'id'.
   */
  public PrintOrder setId(java.lang.String id) {
    return genClient.setOther(id, CacheKey.id);
  }

  /**
   * Sets the field 'orderRef'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public PrintOrder setOrderRef(com.clover.sdk.v3.base.Reference orderRef) {
    return genClient.setRecord(orderRef, CacheKey.orderRef);
  }

  /**
   * Sets the field 'deviceRef'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public PrintOrder setDeviceRef(com.clover.sdk.v3.base.Reference deviceRef) {
    return genClient.setRecord(deviceRef, CacheKey.deviceRef);
  }

  /**
   * Sets the field 'category'.
   */
  public PrintOrder setCategory(com.clover.sdk.v3.printer.PrintCategory category) {
    return genClient.setOther(category, CacheKey.category);
  }

  /**
   * Sets the field 'state'.
   */
  public PrintOrder setState(com.clover.sdk.v3.order.PrintState state) {
    return genClient.setOther(state, CacheKey.state);
  }

  /**
   * Sets the field 'orderSnapshot'.
   */
  public PrintOrder setOrderSnapshot(java.lang.String orderSnapshot) {
    return genClient.setOther(orderSnapshot, CacheKey.orderSnapshot);
  }

  /**
   * Sets the field 'createdTime'.
   */
  public PrintOrder setCreatedTime(java.lang.Long createdTime) {
    return genClient.setOther(createdTime, CacheKey.createdTime);
  }

  /**
   * Sets the field 'modifiedTime'.
   */
  public PrintOrder setModifiedTime(java.lang.Long modifiedTime) {
    return genClient.setOther(modifiedTime, CacheKey.modifiedTime);
  }

  /**
   * Sets the field 'deletedTime'.
   */
  public PrintOrder setDeletedTime(java.lang.Long deletedTime) {
    return genClient.setOther(deletedTime, CacheKey.deletedTime);
  }

  /**
   * Sets the field 'printTime'.
   */
  public PrintOrder setPrintTime(java.lang.Long printTime) {
    return genClient.setOther(printTime, CacheKey.printTime);
  }

  /**
   * Sets the field 'clientPrintingTime'.
   */
  public PrintOrder setClientPrintingTime(java.lang.Long clientPrintingTime) {
    return genClient.setOther(clientPrintingTime, CacheKey.clientPrintingTime);
  }

  /**
   * Sets the field 'errorMessage'.
   */
  public PrintOrder setErrorMessage(java.lang.String errorMessage) {
    return genClient.setOther(errorMessage, CacheKey.errorMessage);
  }


  /** Clears the 'id' field, the 'has' method for this field will now return false */
  public void clearId() {
    genClient.clear(CacheKey.id);
  }
  /** Clears the 'orderRef' field, the 'has' method for this field will now return false */
  public void clearOrderRef() {
    genClient.clear(CacheKey.orderRef);
  }
  /** Clears the 'deviceRef' field, the 'has' method for this field will now return false */
  public void clearDeviceRef() {
    genClient.clear(CacheKey.deviceRef);
  }
  /** Clears the 'category' field, the 'has' method for this field will now return false */
  public void clearCategory() {
    genClient.clear(CacheKey.category);
  }
  /** Clears the 'state' field, the 'has' method for this field will now return false */
  public void clearState() {
    genClient.clear(CacheKey.state);
  }
  /** Clears the 'orderSnapshot' field, the 'has' method for this field will now return false */
  public void clearOrderSnapshot() {
    genClient.clear(CacheKey.orderSnapshot);
  }
  /** Clears the 'createdTime' field, the 'has' method for this field will now return false */
  public void clearCreatedTime() {
    genClient.clear(CacheKey.createdTime);
  }
  /** Clears the 'modifiedTime' field, the 'has' method for this field will now return false */
  public void clearModifiedTime() {
    genClient.clear(CacheKey.modifiedTime);
  }
  /** Clears the 'deletedTime' field, the 'has' method for this field will now return false */
  public void clearDeletedTime() {
    genClient.clear(CacheKey.deletedTime);
  }
  /** Clears the 'printTime' field, the 'has' method for this field will now return false */
  public void clearPrintTime() {
    genClient.clear(CacheKey.printTime);
  }
  /** Clears the 'clientPrintingTime' field, the 'has' method for this field will now return false */
  public void clearClientPrintingTime() {
    genClient.clear(CacheKey.clientPrintingTime);
  }
  /** Clears the 'errorMessage' field, the 'has' method for this field will now return false */
  public void clearErrorMessage() {
    genClient.clear(CacheKey.errorMessage);
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
  public PrintOrder copyChanges() {
    PrintOrder copy = new PrintOrder();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(PrintOrder src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new PrintOrder(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<PrintOrder> CREATOR = new android.os.Parcelable.Creator<PrintOrder>() {
    @Override
    public PrintOrder createFromParcel(android.os.Parcel in) {
      PrintOrder instance = new PrintOrder(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public PrintOrder[] newArray(int size) {
      return new PrintOrder[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<PrintOrder> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<PrintOrder>() {
    public Class<PrintOrder> getCreatedClass() {
      return PrintOrder.class;
    }

    @Override
    public PrintOrder create(org.json.JSONObject jsonObject) {
      return new PrintOrder(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean ID_IS_REQUIRED = false;
    public static final long ID_MAX_LEN = 13;
    public static final boolean ORDERREF_IS_REQUIRED = false;
    public static final boolean DEVICEREF_IS_REQUIRED = false;
    public static final boolean CATEGORY_IS_REQUIRED = false;
    public static final boolean STATE_IS_REQUIRED = false;
    public static final boolean ORDERSNAPSHOT_IS_REQUIRED = false;
    public static final long ORDERSNAPSHOT_MAX_LEN = 5242880;
    public static final boolean CREATEDTIME_IS_REQUIRED = false;
    public static final boolean MODIFIEDTIME_IS_REQUIRED = false;
    public static final boolean DELETEDTIME_IS_REQUIRED = false;
    public static final boolean PRINTTIME_IS_REQUIRED = false;
    public static final boolean CLIENTPRINTINGTIME_IS_REQUIRED = false;
    public static final boolean ERRORMESSAGE_IS_REQUIRED = false;
    public static final long ERRORMESSAGE_MAX_LEN = 65535;
  }

}
