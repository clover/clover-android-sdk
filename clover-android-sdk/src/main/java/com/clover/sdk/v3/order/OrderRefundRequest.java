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
 * <li>{@link #getRefunds refunds}</li>
 * <li>{@link #getLineItems lineItems}</li>
 * <li>{@link #getIncludeTip includeTip}</li>
 * <li>{@link #getReason reason}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class OrderRefundRequest extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

    public java.util.List<com.clover.sdk.v3.payments.Refund> getRefunds() {
        return genClient.cacheGet(CacheKey.refunds);
    }

    public java.util.List<com.clover.sdk.v3.base.Reference> getLineItems() {
        return genClient.cacheGet(CacheKey.lineItems);
    }

    public java.lang.Boolean getIncludeTip() {
        return genClient.cacheGet(CacheKey.includeTip);
    }

    /**
     * Describes the reason for the refund
     */
    public java.lang.String getReason() {
        return genClient.cacheGet(CacheKey.reason);
    }




    private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
        refunds
                (com.clover.sdk.extractors.RecordListExtractionStrategy.instance(com.clover.sdk.v3.payments.Refund.JSON_CREATOR)),
        lineItems
                (com.clover.sdk.extractors.RecordListExtractionStrategy.instance(com.clover.sdk.v3.base.Reference.JSON_CREATOR)),
        includeTip
                (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Boolean.class)),
        reason
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

    private final GenericClient<OrderRefundRequest> genClient;

    /**
     * Constructs a new empty instance.
     */
    public OrderRefundRequest() {
        genClient = new GenericClient<OrderRefundRequest>(this);
    }

    @Override
    protected GenericClient getGenericClient() {
        return genClient;
    }

    /**
     * Constructs a new empty instance.
     */
    protected OrderRefundRequest(boolean noInit) {
        genClient = null;
    }

    /**
     * Constructs a new instance from the given JSON String.
     */
    public OrderRefundRequest(String json) throws IllegalArgumentException {
        this();
        genClient.initJsonObject(json);
    }

    /**
     * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
     * reflected in this instance and vice-versa.
     */
    public OrderRefundRequest(org.json.JSONObject jsonObject) {
        this();
        genClient.setJsonObject(jsonObject);
    }

    /**
     * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
     */
    public OrderRefundRequest(OrderRefundRequest src) {
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
        genClient.validateReferences(CacheKey.lineItems);
    }

    /** Checks whether the 'refunds' field is set and is not null */
    public boolean isNotNullRefunds() {
        return genClient.cacheValueIsNotNull(CacheKey.refunds);
    }

    /** Checks whether the 'refunds' field is set and is not null and is not empty */
    public boolean isNotEmptyRefunds() { return isNotNullRefunds() && !getRefunds().isEmpty(); }

    /** Checks whether the 'lineItems' field is set and is not null */
    public boolean isNotNullLineItems() {
        return genClient.cacheValueIsNotNull(CacheKey.lineItems);
    }

    /** Checks whether the 'lineItems' field is set and is not null and is not empty */
    public boolean isNotEmptyLineItems() { return isNotNullLineItems() && !getLineItems().isEmpty(); }

    /** Checks whether the 'includeTip' field is set and is not null */
    public boolean isNotNullIncludeTip() {
        return genClient.cacheValueIsNotNull(CacheKey.includeTip);
    }

    /** Checks whether the 'reason' field is set and is not null */
    public boolean isNotNullReason() {
        return genClient.cacheValueIsNotNull(CacheKey.reason);
    }



    /** Checks whether the 'refunds' field has been set, however the value could be null */
    public boolean hasRefunds() {
        return genClient.cacheHasKey(CacheKey.refunds);
    }

    /** Checks whether the 'lineItems' field has been set, however the value could be null */
    public boolean hasLineItems() {
        return genClient.cacheHasKey(CacheKey.lineItems);
    }

    /** Checks whether the 'includeTip' field has been set, however the value could be null */
    public boolean hasIncludeTip() {
        return genClient.cacheHasKey(CacheKey.includeTip);
    }

    /** Checks whether the 'reason' field has been set, however the value could be null */
    public boolean hasReason() {
        return genClient.cacheHasKey(CacheKey.reason);
    }


    /**
     * Sets the field 'refunds'.
     *
     * Nulls in the given List are skipped. List parameter is copied, so it will not reflect any changes, but objects inside it will.
     */
    public OrderRefundRequest setRefunds(java.util.List<com.clover.sdk.v3.payments.Refund> refunds) {
        return genClient.setArrayRecord(refunds, CacheKey.refunds);
    }

    /**
     * Sets the field 'lineItems'.
     *
     * Nulls in the given List are skipped. List parameter is copied, so it will not reflect any changes, but objects inside it will.
     */
    public OrderRefundRequest setLineItems(java.util.List<com.clover.sdk.v3.base.Reference> lineItems) {
        return genClient.setArrayRecord(lineItems, CacheKey.lineItems);
    }

    /**
     * Sets the field 'includeTip'.
     */
    public OrderRefundRequest setIncludeTip(java.lang.Boolean includeTip) {
        return genClient.setOther(includeTip, CacheKey.includeTip);
    }

    /**
     * Sets the field 'reason'.
     */
    public OrderRefundRequest setReason(java.lang.String reason) {
        return genClient.setOther(reason, CacheKey.reason);
    }


    /** Clears the 'refunds' field, the 'has' method for this field will now return false */
    public void clearRefunds() {
        genClient.clear(CacheKey.refunds);
    }
    /** Clears the 'lineItems' field, the 'has' method for this field will now return false */
    public void clearLineItems() {
        genClient.clear(CacheKey.lineItems);
    }
    /** Clears the 'includeTip' field, the 'has' method for this field will now return false */
    public void clearIncludeTip() {
        genClient.clear(CacheKey.includeTip);
    }
    /** Clears the 'reason' field, the 'has' method for this field will now return false */
    public void clearReason() {
        genClient.clear(CacheKey.reason);
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
    public OrderRefundRequest copyChanges() {
        OrderRefundRequest copy = new OrderRefundRequest();
        copy.mergeChanges(this);
        copy.resetChangeLog();
        return copy;
    }

    /**
     * Copy all the changed fields from the given source to this instance.
     */
    public void mergeChanges(OrderRefundRequest src) {
        if (src.genClient.getChangeLog() != null) {
            genClient.mergeChanges(new OrderRefundRequest(src).getJSONObject(), src.genClient);
        }
    }

    public static final android.os.Parcelable.Creator<OrderRefundRequest> CREATOR = new android.os.Parcelable.Creator<OrderRefundRequest>() {
        @Override
        public OrderRefundRequest createFromParcel(android.os.Parcel in) {
            OrderRefundRequest instance = new OrderRefundRequest(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
            instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
            instance.genClient.setChangeLog(in.readBundle());
            return instance;
        }

        @Override
        public OrderRefundRequest[] newArray(int size) {
            return new OrderRefundRequest[size];
        }
    };

    public static final com.clover.sdk.JSONifiable.Creator<OrderRefundRequest> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<OrderRefundRequest>() {
        public Class<OrderRefundRequest> getCreatedClass() {
            return OrderRefundRequest.class;
        }

        @Override
        public OrderRefundRequest create(org.json.JSONObject jsonObject) {
            return new OrderRefundRequest(jsonObject);
        }
    };

    public interface Constraints {
        public static final boolean REFUNDS_IS_REQUIRED = false;
        public static final boolean LINEITEMS_IS_REQUIRED = false;
        public static final boolean INCLUDETIP_IS_REQUIRED = false;
        public static final boolean REASON_IS_REQUIRED = false;
    }

}