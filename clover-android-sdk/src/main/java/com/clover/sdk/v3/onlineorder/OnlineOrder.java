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

package com.clover.sdk.v3.onlineorder;

import com.clover.sdk.GenericClient;
import com.clover.sdk.GenericParcelable;

/**
 * This is an auto-generated Clover data object.
 * <p>
 * <h3>Fields</h3>
 * <ul>
 * <li>{@link #getId id}</li>
 * <li>{@link #getOnlineOrderId onlineOrderId}</li>
 * <li>{@link #getReceiptId receiptId}</li>
 * <li>{@link #getService service}</li>
 * <li>{@link #getProvider provider}</li>
 * <li>{@link #getProviderId providerId}</li>
 * <li>{@link #getOrderState orderState}</li>
 * <li>{@link #getOnlineOrderCustomer onlineOrderCustomer}</li>
 * <li>{@link #getDeliverTime deliverTime}</li>
 * <li>{@link #getLeadTime leadTime}</li>
 * <li>{@link #getScheduledPickupTime scheduledPickupTime}</li>
 * <li>{@link #getReason reason}</li>
 * <li>{@link #getNote note}</li>
 * <li>{@link #getScheduled scheduled}</li>
 * <li>{@link #getServiceFee serviceFee}</li>
 * <li>{@link #getServiceFeeItem serviceFeeItem}</li>
 * <li>{@link #getCreatedTime createdTime}</li>
 * <li>{@link #getModifiedTime modifiedTime}</li>
 * <li>{@link #getDeletedTime deletedTime}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class OnlineOrder extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  /**
   * order id
   */
  public java.lang.String getId() {
    return genClient.cacheGet(CacheKey.id);
  }

  /**
   * Online Order id
   */
  public java.lang.String getOnlineOrderId() {
    return genClient.cacheGet(CacheKey.onlineOrderId);
  }

  /**
   * Receipt id
   */
  public java.lang.String getReceiptId() {
    return genClient.cacheGet(CacheKey.receiptId);
  }

  /**
   * online order service
   */
  public com.clover.sdk.v3.base.Reference getService() {
    return genClient.cacheGet(CacheKey.service);
  }

  /**
   * online order provider
   */
  public com.clover.sdk.v3.base.Reference getProvider() {
    return genClient.cacheGet(CacheKey.provider);
  }

  /**
   * Deprecated, please ignore.
   */
  public com.clover.sdk.v3.onlineorder.ProviderId getProviderId() {
    return genClient.cacheGet(CacheKey.providerId);
  }

  /**
   * online order status
   */
  public com.clover.sdk.v3.onlineorder.OrderState getOrderState() {
    return genClient.cacheGet(CacheKey.orderState);
  }

  /**
   * the customer who submitted the order request from provider
   */
  public com.clover.sdk.v3.onlineorder.OnlineOrderCustomer getOnlineOrderCustomer() {
    return genClient.cacheGet(CacheKey.onlineOrderCustomer);
  }

  /**
   * The timestamp the order gets delivered
   */
  public java.lang.Long getDeliverTime() {
    return genClient.cacheGet(CacheKey.deliverTime);
  }

  /**
   * Minutes of the lead time which overrides the default lead time
   */
  public java.lang.Integer getLeadTime() {
    return genClient.cacheGet(CacheKey.leadTime);
  }

  /**
   * The timestamp the order is scheduled to be picked up
   */
  public java.lang.Long getScheduledPickupTime() {
    return genClient.cacheGet(CacheKey.scheduledPickupTime);
  }

  /**
   * The reason the online order gets rejected or cancelled
   */
  public com.clover.sdk.v3.onlineorder.Reason getReason() {
    return genClient.cacheGet(CacheKey.reason);
  }

  /**
   * An arbitrary string with information about this online order from customer to merchant, may be printed on the order receipt and displayed in apps
   */
  public java.lang.String getNote() {
    return genClient.cacheGet(CacheKey.note);
  }

  /**
   * If the online order is a scheduled order or ASAP order
   */
  public java.lang.Boolean getScheduled() {
    return genClient.cacheGet(CacheKey.scheduled);
  }

  /**
   * amount of the service fee for the fulfillment service, such as delivery fee
   */
  public java.lang.Long getServiceFee() {
    return genClient.cacheGet(CacheKey.serviceFee);
  }

  public com.clover.sdk.v3.base.Reference getServiceFeeItem() {
    return genClient.cacheGet(CacheKey.serviceFeeItem);
  }

  /**
   * Timestamp when the online ordering item was created
   */
  public java.lang.Long getCreatedTime() {
    return genClient.cacheGet(CacheKey.createdTime);
  }

  /**
   * Timestamp when the online ordering item was last modified
   */
  public java.lang.Long getModifiedTime() {
    return genClient.cacheGet(CacheKey.modifiedTime);
  }

  /**
   * Timestamp when online ordering item was last deleted
   */
  public java.lang.Long getDeletedTime() {
    return genClient.cacheGet(CacheKey.deletedTime);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    id
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    onlineOrderId
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    receiptId
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    service
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.base.Reference.JSON_CREATOR)),
    provider
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.base.Reference.JSON_CREATOR)),
    providerId
        (com.clover.sdk.extractors.EnumExtractionStrategy.instance(com.clover.sdk.v3.onlineorder.ProviderId.class)),
    orderState
        (com.clover.sdk.extractors.EnumExtractionStrategy.instance(com.clover.sdk.v3.onlineorder.OrderState.class)),
    onlineOrderCustomer
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.onlineorder.OnlineOrderCustomer.JSON_CREATOR)),
    deliverTime
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    leadTime
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Integer.class)),
    scheduledPickupTime
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    reason
        (com.clover.sdk.extractors.EnumExtractionStrategy.instance(com.clover.sdk.v3.onlineorder.Reason.class)),
    note
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    scheduled
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Boolean.class)),
    serviceFee
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    serviceFeeItem
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.base.Reference.JSON_CREATOR)),
    createdTime
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    modifiedTime
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    deletedTime
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

  private final GenericClient<OnlineOrder> genClient;

  /**
   * Constructs a new empty instance.
   */
  public OnlineOrder() {
    genClient = new GenericClient<OnlineOrder>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected OnlineOrder(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public OnlineOrder(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public OnlineOrder(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public OnlineOrder(OnlineOrder src) {
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
    genClient.validateCloverId(CacheKey.id, getId());
    genClient.validateReferences(CacheKey.service);
    genClient.validateReferences(CacheKey.provider);
    genClient.validateReferences(CacheKey.serviceFeeItem);
  }

  /** Checks whether the 'id' field is set and is not null */
  public boolean isNotNullId() {
    return genClient.cacheValueIsNotNull(CacheKey.id);
  }

  /** Checks whether the 'onlineOrderId' field is set and is not null */
  public boolean isNotNullOnlineOrderId() {
    return genClient.cacheValueIsNotNull(CacheKey.onlineOrderId);
  }

  /** Checks whether the 'receiptId' field is set and is not null */
  public boolean isNotNullReceiptId() {
    return genClient.cacheValueIsNotNull(CacheKey.receiptId);
  }

  /** Checks whether the 'service' field is set and is not null */
  public boolean isNotNullService() {
    return genClient.cacheValueIsNotNull(CacheKey.service);
  }

  /** Checks whether the 'provider' field is set and is not null */
  public boolean isNotNullProvider() {
    return genClient.cacheValueIsNotNull(CacheKey.provider);
  }

  /** Checks whether the 'providerId' field is set and is not null */
  public boolean isNotNullProviderId() {
    return genClient.cacheValueIsNotNull(CacheKey.providerId);
  }

  /** Checks whether the 'orderState' field is set and is not null */
  public boolean isNotNullOrderState() {
    return genClient.cacheValueIsNotNull(CacheKey.orderState);
  }

  /** Checks whether the 'onlineOrderCustomer' field is set and is not null */
  public boolean isNotNullOnlineOrderCustomer() {
    return genClient.cacheValueIsNotNull(CacheKey.onlineOrderCustomer);
  }

  /** Checks whether the 'deliverTime' field is set and is not null */
  public boolean isNotNullDeliverTime() {
    return genClient.cacheValueIsNotNull(CacheKey.deliverTime);
  }

  /** Checks whether the 'leadTime' field is set and is not null */
  public boolean isNotNullLeadTime() {
    return genClient.cacheValueIsNotNull(CacheKey.leadTime);
  }

  /** Checks whether the 'scheduledPickupTime' field is set and is not null */
  public boolean isNotNullScheduledPickupTime() {
    return genClient.cacheValueIsNotNull(CacheKey.scheduledPickupTime);
  }

  /** Checks whether the 'reason' field is set and is not null */
  public boolean isNotNullReason() {
    return genClient.cacheValueIsNotNull(CacheKey.reason);
  }

  /** Checks whether the 'note' field is set and is not null */
  public boolean isNotNullNote() {
    return genClient.cacheValueIsNotNull(CacheKey.note);
  }

  /** Checks whether the 'scheduled' field is set and is not null */
  public boolean isNotNullScheduled() {
    return genClient.cacheValueIsNotNull(CacheKey.scheduled);
  }

  /** Checks whether the 'serviceFee' field is set and is not null */
  public boolean isNotNullServiceFee() {
    return genClient.cacheValueIsNotNull(CacheKey.serviceFee);
  }

  /** Checks whether the 'serviceFeeItem' field is set and is not null */
  public boolean isNotNullServiceFeeItem() {
    return genClient.cacheValueIsNotNull(CacheKey.serviceFeeItem);
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



  /** Checks whether the 'id' field has been set, however the value could be null */
  public boolean hasId() {
    return genClient.cacheHasKey(CacheKey.id);
  }

  /** Checks whether the 'onlineOrderId' field has been set, however the value could be null */
  public boolean hasOnlineOrderId() {
    return genClient.cacheHasKey(CacheKey.onlineOrderId);
  }

  /** Checks whether the 'receiptId' field has been set, however the value could be null */
  public boolean hasReceiptId() {
    return genClient.cacheHasKey(CacheKey.receiptId);
  }

  /** Checks whether the 'service' field has been set, however the value could be null */
  public boolean hasService() {
    return genClient.cacheHasKey(CacheKey.service);
  }

  /** Checks whether the 'provider' field has been set, however the value could be null */
  public boolean hasProvider() {
    return genClient.cacheHasKey(CacheKey.provider);
  }

  /** Checks whether the 'providerId' field has been set, however the value could be null */
  public boolean hasProviderId() {
    return genClient.cacheHasKey(CacheKey.providerId);
  }

  /** Checks whether the 'orderState' field has been set, however the value could be null */
  public boolean hasOrderState() {
    return genClient.cacheHasKey(CacheKey.orderState);
  }

  /** Checks whether the 'onlineOrderCustomer' field has been set, however the value could be null */
  public boolean hasOnlineOrderCustomer() {
    return genClient.cacheHasKey(CacheKey.onlineOrderCustomer);
  }

  /** Checks whether the 'deliverTime' field has been set, however the value could be null */
  public boolean hasDeliverTime() {
    return genClient.cacheHasKey(CacheKey.deliverTime);
  }

  /** Checks whether the 'leadTime' field has been set, however the value could be null */
  public boolean hasLeadTime() {
    return genClient.cacheHasKey(CacheKey.leadTime);
  }

  /** Checks whether the 'scheduledPickupTime' field has been set, however the value could be null */
  public boolean hasScheduledPickupTime() {
    return genClient.cacheHasKey(CacheKey.scheduledPickupTime);
  }

  /** Checks whether the 'reason' field has been set, however the value could be null */
  public boolean hasReason() {
    return genClient.cacheHasKey(CacheKey.reason);
  }

  /** Checks whether the 'note' field has been set, however the value could be null */
  public boolean hasNote() {
    return genClient.cacheHasKey(CacheKey.note);
  }

  /** Checks whether the 'scheduled' field has been set, however the value could be null */
  public boolean hasScheduled() {
    return genClient.cacheHasKey(CacheKey.scheduled);
  }

  /** Checks whether the 'serviceFee' field has been set, however the value could be null */
  public boolean hasServiceFee() {
    return genClient.cacheHasKey(CacheKey.serviceFee);
  }

  /** Checks whether the 'serviceFeeItem' field has been set, however the value could be null */
  public boolean hasServiceFeeItem() {
    return genClient.cacheHasKey(CacheKey.serviceFeeItem);
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


  /**
   * Sets the field 'id'.
   */
  public OnlineOrder setId(java.lang.String id) {
    return genClient.setOther(id, CacheKey.id);
  }

  /**
   * Sets the field 'onlineOrderId'.
   */
  public OnlineOrder setOnlineOrderId(java.lang.String onlineOrderId) {
    return genClient.setOther(onlineOrderId, CacheKey.onlineOrderId);
  }

  /**
   * Sets the field 'receiptId'.
   */
  public OnlineOrder setReceiptId(java.lang.String receiptId) {
    return genClient.setOther(receiptId, CacheKey.receiptId);
  }

  /**
   * Sets the field 'service'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public OnlineOrder setService(com.clover.sdk.v3.base.Reference service) {
    return genClient.setRecord(service, CacheKey.service);
  }

  /**
   * Sets the field 'provider'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public OnlineOrder setProvider(com.clover.sdk.v3.base.Reference provider) {
    return genClient.setRecord(provider, CacheKey.provider);
  }

  /**
   * Sets the field 'providerId'.
   */
  public OnlineOrder setProviderId(com.clover.sdk.v3.onlineorder.ProviderId providerId) {
    return genClient.setOther(providerId, CacheKey.providerId);
  }

  /**
   * Sets the field 'orderState'.
   */
  public OnlineOrder setOrderState(com.clover.sdk.v3.onlineorder.OrderState orderState) {
    return genClient.setOther(orderState, CacheKey.orderState);
  }

  /**
   * Sets the field 'onlineOrderCustomer'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public OnlineOrder setOnlineOrderCustomer(com.clover.sdk.v3.onlineorder.OnlineOrderCustomer onlineOrderCustomer) {
    return genClient.setRecord(onlineOrderCustomer, CacheKey.onlineOrderCustomer);
  }

  /**
   * Sets the field 'deliverTime'.
   */
  public OnlineOrder setDeliverTime(java.lang.Long deliverTime) {
    return genClient.setOther(deliverTime, CacheKey.deliverTime);
  }

  /**
   * Sets the field 'leadTime'.
   */
  public OnlineOrder setLeadTime(java.lang.Integer leadTime) {
    return genClient.setOther(leadTime, CacheKey.leadTime);
  }

  /**
   * Sets the field 'scheduledPickupTime'.
   */
  public OnlineOrder setScheduledPickupTime(java.lang.Long scheduledPickupTime) {
    return genClient.setOther(scheduledPickupTime, CacheKey.scheduledPickupTime);
  }

  /**
   * Sets the field 'reason'.
   */
  public OnlineOrder setReason(com.clover.sdk.v3.onlineorder.Reason reason) {
    return genClient.setOther(reason, CacheKey.reason);
  }

  /**
   * Sets the field 'note'.
   */
  public OnlineOrder setNote(java.lang.String note) {
    return genClient.setOther(note, CacheKey.note);
  }

  /**
   * Sets the field 'scheduled'.
   */
  public OnlineOrder setScheduled(java.lang.Boolean scheduled) {
    return genClient.setOther(scheduled, CacheKey.scheduled);
  }

  /**
   * Sets the field 'serviceFee'.
   */
  public OnlineOrder setServiceFee(java.lang.Long serviceFee) {
    return genClient.setOther(serviceFee, CacheKey.serviceFee);
  }

  /**
   * Sets the field 'serviceFeeItem'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public OnlineOrder setServiceFeeItem(com.clover.sdk.v3.base.Reference serviceFeeItem) {
    return genClient.setRecord(serviceFeeItem, CacheKey.serviceFeeItem);
  }

  /**
   * Sets the field 'createdTime'.
   */
  public OnlineOrder setCreatedTime(java.lang.Long createdTime) {
    return genClient.setOther(createdTime, CacheKey.createdTime);
  }

  /**
   * Sets the field 'modifiedTime'.
   */
  public OnlineOrder setModifiedTime(java.lang.Long modifiedTime) {
    return genClient.setOther(modifiedTime, CacheKey.modifiedTime);
  }

  /**
   * Sets the field 'deletedTime'.
   */
  public OnlineOrder setDeletedTime(java.lang.Long deletedTime) {
    return genClient.setOther(deletedTime, CacheKey.deletedTime);
  }


  /** Clears the 'id' field, the 'has' method for this field will now return false */
  public void clearId() {
    genClient.clear(CacheKey.id);
  }
  /** Clears the 'onlineOrderId' field, the 'has' method for this field will now return false */
  public void clearOnlineOrderId() {
    genClient.clear(CacheKey.onlineOrderId);
  }
  /** Clears the 'receiptId' field, the 'has' method for this field will now return false */
  public void clearReceiptId() {
    genClient.clear(CacheKey.receiptId);
  }
  /** Clears the 'service' field, the 'has' method for this field will now return false */
  public void clearService() {
    genClient.clear(CacheKey.service);
  }
  /** Clears the 'provider' field, the 'has' method for this field will now return false */
  public void clearProvider() {
    genClient.clear(CacheKey.provider);
  }
  /** Clears the 'providerId' field, the 'has' method for this field will now return false */
  public void clearProviderId() {
    genClient.clear(CacheKey.providerId);
  }
  /** Clears the 'orderState' field, the 'has' method for this field will now return false */
  public void clearOrderState() {
    genClient.clear(CacheKey.orderState);
  }
  /** Clears the 'onlineOrderCustomer' field, the 'has' method for this field will now return false */
  public void clearOnlineOrderCustomer() {
    genClient.clear(CacheKey.onlineOrderCustomer);
  }
  /** Clears the 'deliverTime' field, the 'has' method for this field will now return false */
  public void clearDeliverTime() {
    genClient.clear(CacheKey.deliverTime);
  }
  /** Clears the 'leadTime' field, the 'has' method for this field will now return false */
  public void clearLeadTime() {
    genClient.clear(CacheKey.leadTime);
  }
  /** Clears the 'scheduledPickupTime' field, the 'has' method for this field will now return false */
  public void clearScheduledPickupTime() {
    genClient.clear(CacheKey.scheduledPickupTime);
  }
  /** Clears the 'reason' field, the 'has' method for this field will now return false */
  public void clearReason() {
    genClient.clear(CacheKey.reason);
  }
  /** Clears the 'note' field, the 'has' method for this field will now return false */
  public void clearNote() {
    genClient.clear(CacheKey.note);
  }
  /** Clears the 'scheduled' field, the 'has' method for this field will now return false */
  public void clearScheduled() {
    genClient.clear(CacheKey.scheduled);
  }
  /** Clears the 'serviceFee' field, the 'has' method for this field will now return false */
  public void clearServiceFee() {
    genClient.clear(CacheKey.serviceFee);
  }
  /** Clears the 'serviceFeeItem' field, the 'has' method for this field will now return false */
  public void clearServiceFeeItem() {
    genClient.clear(CacheKey.serviceFeeItem);
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
  public OnlineOrder copyChanges() {
    OnlineOrder copy = new OnlineOrder();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(OnlineOrder src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new OnlineOrder(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<OnlineOrder> CREATOR = new android.os.Parcelable.Creator<OnlineOrder>() {
    @Override
    public OnlineOrder createFromParcel(android.os.Parcel in) {
      OnlineOrder instance = new OnlineOrder(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public OnlineOrder[] newArray(int size) {
      return new OnlineOrder[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<OnlineOrder> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<OnlineOrder>() {
    public Class<OnlineOrder> getCreatedClass() {
      return OnlineOrder.class;
    }

    @Override
    public OnlineOrder create(org.json.JSONObject jsonObject) {
      return new OnlineOrder(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean ID_IS_REQUIRED = false;
    public static final long ID_MAX_LEN = 13;
    public static final boolean ONLINEORDERID_IS_REQUIRED = false;
    public static final boolean RECEIPTID_IS_REQUIRED = false;
    public static final boolean SERVICE_IS_REQUIRED = false;
    public static final boolean PROVIDER_IS_REQUIRED = false;
    public static final boolean PROVIDERID_IS_REQUIRED = false;
    public static final boolean ORDERSTATE_IS_REQUIRED = false;
    public static final boolean ONLINEORDERCUSTOMER_IS_REQUIRED = false;
    public static final boolean DELIVERTIME_IS_REQUIRED = false;
    public static final boolean LEADTIME_IS_REQUIRED = false;
    public static final boolean SCHEDULEDPICKUPTIME_IS_REQUIRED = false;
    public static final boolean REASON_IS_REQUIRED = false;
    public static final boolean NOTE_IS_REQUIRED = false;
    public static final boolean SCHEDULED_IS_REQUIRED = false;
    public static final boolean SERVICEFEE_IS_REQUIRED = false;
    public static final boolean SERVICEFEEITEM_IS_REQUIRED = false;
    public static final boolean CREATEDTIME_IS_REQUIRED = false;
    public static final boolean MODIFIEDTIME_IS_REQUIRED = false;
    public static final boolean DELETEDTIME_IS_REQUIRED = false;
  }

}