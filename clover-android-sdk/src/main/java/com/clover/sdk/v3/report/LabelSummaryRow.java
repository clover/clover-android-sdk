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

package com.clover.sdk.v3.report;

import com.clover.sdk.GenericClient;
import com.clover.sdk.GenericParcelable;

/**
 * This is an auto-generated Clover data object.
 * <p>
 * Each instance of this represents a summary of all the line items with the label in the name field
 * <p>
 * <h3>Fields</h3>
 * <ul>
 * <li>{@link #getName name}</li>
 * <li>{@link #getNumLineItems numLineItems}</li>
 * <li>{@link #getNet net}</li>
 * <li>{@link #getTotalAmount totalAmount}</li>
 * <li>{@link #getRevenueAmount revenueAmount}</li>
 * <li>{@link #getTaxAmount taxAmount}</li>
 * <li>{@link #getNonRevenueAmount nonRevenueAmount}</li>
 * <li>{@link #getDiscountAmount discountAmount}</li>
 * <li>{@link #getRefundAmount refundAmount}</li>
 * <li>{@link #getPercentNetSale percentNetSale}</li>
 * <li>{@link #getNetQuantity netQuantity}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class LabelSummaryRow extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  /**
   * The name of the row, like Food, Drinks, Alcohol, etc.
   */
  public java.lang.String getName() {
    return genClient.cacheGet(CacheKey.name);
  }

  /**
   * Total number of line items in this category.
   */
  public java.lang.Long getNumLineItems() {
    return genClient.cacheGet(CacheKey.numLineItems);
  }

  /**
   * Total amount minus refund amount.
   */
  public java.lang.Long getNet() {
    return genClient.cacheGet(CacheKey.net);
  }

  /**
   * Line item price with modifiers and discounts plus tax
   */
  public java.lang.Long getTotalAmount() {
    return genClient.cacheGet(CacheKey.totalAmount);
  }

  /**
   * Line item price with modifiers and discounts
   */
  public java.lang.Long getRevenueAmount() {
    return genClient.cacheGet(CacheKey.revenueAmount);
  }

  /**
   * Total amount of taxes collected.
   */
  public java.lang.Long getTaxAmount() {
    return genClient.cacheGet(CacheKey.taxAmount);
  }

  /**
   * non revenue Line item price
   */
  public java.lang.Long getNonRevenueAmount() {
    return genClient.cacheGet(CacheKey.nonRevenueAmount);
  }

  /**
   * Total amount of discounts
   */
  public java.lang.Long getDiscountAmount() {
    return genClient.cacheGet(CacheKey.discountAmount);
  }

  /**
   * Total amount of refunds. This amount includes the line item price with modifiers and discounts plus tax.
   */
  public java.lang.Long getRefundAmount() {
    return genClient.cacheGet(CacheKey.refundAmount);
  }

  /**
   * Optional field, percent net sale of item sold
   */
  public java.lang.Double getPercentNetSale() {
    return genClient.cacheGet(CacheKey.percentNetSale);
  }

  /**
   * Optional field, total quantity of items sold
   */
  public java.lang.Double getNetQuantity() {
    return genClient.cacheGet(CacheKey.netQuantity);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    name
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    numLineItems
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    net
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    totalAmount
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    revenueAmount
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    taxAmount
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    nonRevenueAmount
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    discountAmount
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    refundAmount
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    percentNetSale
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Double.class)),
    netQuantity
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Double.class)),
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

  private final GenericClient<LabelSummaryRow> genClient;

  /**
   * Constructs a new empty instance.
   */
  public LabelSummaryRow() {
    genClient = new GenericClient<LabelSummaryRow>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected LabelSummaryRow(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public LabelSummaryRow(String json) throws IllegalArgumentException {
    this();
    try {
      genClient.setJsonObject(new org.json.JSONObject(json));
    } catch (org.json.JSONException e) {
      throw new IllegalArgumentException("invalid json", e);
    }
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public LabelSummaryRow(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public LabelSummaryRow(LabelSummaryRow src) {
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

  /** Checks whether the 'name' field is set and is not null */
  public boolean isNotNullName() {
    return genClient.cacheValueIsNotNull(CacheKey.name);
  }

  /** Checks whether the 'numLineItems' field is set and is not null */
  public boolean isNotNullNumLineItems() {
    return genClient.cacheValueIsNotNull(CacheKey.numLineItems);
  }

  /** Checks whether the 'net' field is set and is not null */
  public boolean isNotNullNet() {
    return genClient.cacheValueIsNotNull(CacheKey.net);
  }

  /** Checks whether the 'totalAmount' field is set and is not null */
  public boolean isNotNullTotalAmount() {
    return genClient.cacheValueIsNotNull(CacheKey.totalAmount);
  }

  /** Checks whether the 'revenueAmount' field is set and is not null */
  public boolean isNotNullRevenueAmount() {
    return genClient.cacheValueIsNotNull(CacheKey.revenueAmount);
  }

  /** Checks whether the 'taxAmount' field is set and is not null */
  public boolean isNotNullTaxAmount() {
    return genClient.cacheValueIsNotNull(CacheKey.taxAmount);
  }

  /** Checks whether the 'nonRevenueAmount' field is set and is not null */
  public boolean isNotNullNonRevenueAmount() {
    return genClient.cacheValueIsNotNull(CacheKey.nonRevenueAmount);
  }

  /** Checks whether the 'discountAmount' field is set and is not null */
  public boolean isNotNullDiscountAmount() {
    return genClient.cacheValueIsNotNull(CacheKey.discountAmount);
  }

  /** Checks whether the 'refundAmount' field is set and is not null */
  public boolean isNotNullRefundAmount() {
    return genClient.cacheValueIsNotNull(CacheKey.refundAmount);
  }

  /** Checks whether the 'percentNetSale' field is set and is not null */
  public boolean isNotNullPercentNetSale() {
    return genClient.cacheValueIsNotNull(CacheKey.percentNetSale);
  }

  /** Checks whether the 'netQuantity' field is set and is not null */
  public boolean isNotNullNetQuantity() {
    return genClient.cacheValueIsNotNull(CacheKey.netQuantity);
  }



  /** Checks whether the 'name' field has been set, however the value could be null */
  public boolean hasName() {
    return genClient.cacheHasKey(CacheKey.name);
  }

  /** Checks whether the 'numLineItems' field has been set, however the value could be null */
  public boolean hasNumLineItems() {
    return genClient.cacheHasKey(CacheKey.numLineItems);
  }

  /** Checks whether the 'net' field has been set, however the value could be null */
  public boolean hasNet() {
    return genClient.cacheHasKey(CacheKey.net);
  }

  /** Checks whether the 'totalAmount' field has been set, however the value could be null */
  public boolean hasTotalAmount() {
    return genClient.cacheHasKey(CacheKey.totalAmount);
  }

  /** Checks whether the 'revenueAmount' field has been set, however the value could be null */
  public boolean hasRevenueAmount() {
    return genClient.cacheHasKey(CacheKey.revenueAmount);
  }

  /** Checks whether the 'taxAmount' field has been set, however the value could be null */
  public boolean hasTaxAmount() {
    return genClient.cacheHasKey(CacheKey.taxAmount);
  }

  /** Checks whether the 'nonRevenueAmount' field has been set, however the value could be null */
  public boolean hasNonRevenueAmount() {
    return genClient.cacheHasKey(CacheKey.nonRevenueAmount);
  }

  /** Checks whether the 'discountAmount' field has been set, however the value could be null */
  public boolean hasDiscountAmount() {
    return genClient.cacheHasKey(CacheKey.discountAmount);
  }

  /** Checks whether the 'refundAmount' field has been set, however the value could be null */
  public boolean hasRefundAmount() {
    return genClient.cacheHasKey(CacheKey.refundAmount);
  }

  /** Checks whether the 'percentNetSale' field has been set, however the value could be null */
  public boolean hasPercentNetSale() {
    return genClient.cacheHasKey(CacheKey.percentNetSale);
  }

  /** Checks whether the 'netQuantity' field has been set, however the value could be null */
  public boolean hasNetQuantity() {
    return genClient.cacheHasKey(CacheKey.netQuantity);
  }


  /**
   * Sets the field 'name'.
   */
  public LabelSummaryRow setName(java.lang.String name) {
    return genClient.setOther(name, CacheKey.name);
  }

  /**
   * Sets the field 'numLineItems'.
   */
  public LabelSummaryRow setNumLineItems(java.lang.Long numLineItems) {
    return genClient.setOther(numLineItems, CacheKey.numLineItems);
  }

  /**
   * Sets the field 'net'.
   */
  public LabelSummaryRow setNet(java.lang.Long net) {
    return genClient.setOther(net, CacheKey.net);
  }

  /**
   * Sets the field 'totalAmount'.
   */
  public LabelSummaryRow setTotalAmount(java.lang.Long totalAmount) {
    return genClient.setOther(totalAmount, CacheKey.totalAmount);
  }

  /**
   * Sets the field 'revenueAmount'.
   */
  public LabelSummaryRow setRevenueAmount(java.lang.Long revenueAmount) {
    return genClient.setOther(revenueAmount, CacheKey.revenueAmount);
  }

  /**
   * Sets the field 'taxAmount'.
   */
  public LabelSummaryRow setTaxAmount(java.lang.Long taxAmount) {
    return genClient.setOther(taxAmount, CacheKey.taxAmount);
  }

  /**
   * Sets the field 'nonRevenueAmount'.
   */
  public LabelSummaryRow setNonRevenueAmount(java.lang.Long nonRevenueAmount) {
    return genClient.setOther(nonRevenueAmount, CacheKey.nonRevenueAmount);
  }

  /**
   * Sets the field 'discountAmount'.
   */
  public LabelSummaryRow setDiscountAmount(java.lang.Long discountAmount) {
    return genClient.setOther(discountAmount, CacheKey.discountAmount);
  }

  /**
   * Sets the field 'refundAmount'.
   */
  public LabelSummaryRow setRefundAmount(java.lang.Long refundAmount) {
    return genClient.setOther(refundAmount, CacheKey.refundAmount);
  }

  /**
   * Sets the field 'percentNetSale'.
   */
  public LabelSummaryRow setPercentNetSale(java.lang.Double percentNetSale) {
    return genClient.setOther(percentNetSale, CacheKey.percentNetSale);
  }

  /**
   * Sets the field 'netQuantity'.
   */
  public LabelSummaryRow setNetQuantity(java.lang.Double netQuantity) {
    return genClient.setOther(netQuantity, CacheKey.netQuantity);
  }


  /** Clears the 'name' field, the 'has' method for this field will now return false */
  public void clearName() {
    genClient.clear(CacheKey.name);
  }
  /** Clears the 'numLineItems' field, the 'has' method for this field will now return false */
  public void clearNumLineItems() {
    genClient.clear(CacheKey.numLineItems);
  }
  /** Clears the 'net' field, the 'has' method for this field will now return false */
  public void clearNet() {
    genClient.clear(CacheKey.net);
  }
  /** Clears the 'totalAmount' field, the 'has' method for this field will now return false */
  public void clearTotalAmount() {
    genClient.clear(CacheKey.totalAmount);
  }
  /** Clears the 'revenueAmount' field, the 'has' method for this field will now return false */
  public void clearRevenueAmount() {
    genClient.clear(CacheKey.revenueAmount);
  }
  /** Clears the 'taxAmount' field, the 'has' method for this field will now return false */
  public void clearTaxAmount() {
    genClient.clear(CacheKey.taxAmount);
  }
  /** Clears the 'nonRevenueAmount' field, the 'has' method for this field will now return false */
  public void clearNonRevenueAmount() {
    genClient.clear(CacheKey.nonRevenueAmount);
  }
  /** Clears the 'discountAmount' field, the 'has' method for this field will now return false */
  public void clearDiscountAmount() {
    genClient.clear(CacheKey.discountAmount);
  }
  /** Clears the 'refundAmount' field, the 'has' method for this field will now return false */
  public void clearRefundAmount() {
    genClient.clear(CacheKey.refundAmount);
  }
  /** Clears the 'percentNetSale' field, the 'has' method for this field will now return false */
  public void clearPercentNetSale() {
    genClient.clear(CacheKey.percentNetSale);
  }
  /** Clears the 'netQuantity' field, the 'has' method for this field will now return false */
  public void clearNetQuantity() {
    genClient.clear(CacheKey.netQuantity);
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
  public LabelSummaryRow copyChanges() {
    LabelSummaryRow copy = new LabelSummaryRow();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(LabelSummaryRow src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new LabelSummaryRow(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<LabelSummaryRow> CREATOR = new android.os.Parcelable.Creator<LabelSummaryRow>() {
    @Override
    public LabelSummaryRow createFromParcel(android.os.Parcel in) {
      LabelSummaryRow instance = new LabelSummaryRow(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public LabelSummaryRow[] newArray(int size) {
      return new LabelSummaryRow[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<LabelSummaryRow> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<LabelSummaryRow>() {
    @Override
    public LabelSummaryRow create(org.json.JSONObject jsonObject) {
      return new LabelSummaryRow(jsonObject);
    }
  };

  public interface Constraints {

    public static final boolean NAME_IS_REQUIRED = false;
    public static final boolean NUMLINEITEMS_IS_REQUIRED = false;
    public static final boolean NET_IS_REQUIRED = false;
    public static final boolean TOTALAMOUNT_IS_REQUIRED = false;
    public static final boolean REVENUEAMOUNT_IS_REQUIRED = false;
    public static final boolean TAXAMOUNT_IS_REQUIRED = false;
    public static final boolean NONREVENUEAMOUNT_IS_REQUIRED = false;
    public static final boolean DISCOUNTAMOUNT_IS_REQUIRED = false;
    public static final boolean REFUNDAMOUNT_IS_REQUIRED = false;
    public static final boolean PERCENTNETSALE_IS_REQUIRED = false;
    public static final boolean NETQUANTITY_IS_REQUIRED = false;

  }

}
