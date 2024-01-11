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
 * <li>{@link #getTaxAmount taxAmount}</li>
 * <li>{@link #getTaxIndicator taxIndicator}</li>
 * <li>{@link #getVatTaxAmount vatTaxAmount}</li>
 * <li>{@link #getVatTaxRate vatTaxRate}</li>
 * <li>{@link #getPurchaseIdentifier purchaseIdentifier}</li>
 * <li>{@link #getPcOrderNumber pcOrderNumber}</li>
 * <li>{@link #getDiscountAmount discountAmount}</li>
 * <li>{@link #getFreightAmount freightAmount}</li>
 * <li>{@link #getDutyAmount dutyAmount}</li>
 * <li>{@link #getDestinationPostalCode destinationPostalCode}</li>
 * <li>{@link #getShipFromPostalCode shipFromPostalCode}</li>
 * <li>{@link #getDestinationCountryCode destinationCountryCode}</li>
 * <li>{@link #getMerchantTaxId merchantTaxId}</li>
 * <li>{@link #getProductDescription productDescription}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class PurchaseCardL2 extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  /**
   * Sales tax amount.
   */
  public java.lang.Long getTaxAmount() {
    return genClient.cacheGet(CacheKey.taxAmount);
  }

  /**
   * Taxable status
   */
  public com.clover.sdk.v3.payments.PcTaxIndicator getTaxIndicator() {
    return genClient.cacheGet(CacheKey.taxIndicator);
  }

  /**
   * Tax amount for freight/shipping.
   */
  public java.lang.Long getVatTaxAmount() {
    return genClient.cacheGet(CacheKey.vatTaxAmount);
  }

  /**
   * Tax rate on freight/shipping amount.
   */
  public java.lang.Integer getVatTaxRate() {
    return genClient.cacheGet(CacheKey.vatTaxRate);
  }

  /**
   * Purchase identifier for customer/merchant.
   */
  public java.lang.String getPurchaseIdentifier() {
    return genClient.cacheGet(CacheKey.purchaseIdentifier);
  }

  /**
   * Purchase card customer code.
   */
  public java.lang.String getPcOrderNumber() {
    return genClient.cacheGet(CacheKey.pcOrderNumber);
  }

  /**
   * Applied discount amount.
   */
  public java.lang.Long getDiscountAmount() {
    return genClient.cacheGet(CacheKey.discountAmount);
  }

  /**
   * Freight amount.
   */
  public java.lang.Long getFreightAmount() {
    return genClient.cacheGet(CacheKey.freightAmount);
  }

  /**
   * Duty amount.
   */
  public java.lang.Long getDutyAmount() {
    return genClient.cacheGet(CacheKey.dutyAmount);
  }

  /**
   * Destination postal code.
   */
  public java.lang.String getDestinationPostalCode() {
    return genClient.cacheGet(CacheKey.destinationPostalCode);
  }

  /**
   * Ship from postal code.
   */
  public java.lang.String getShipFromPostalCode() {
    return genClient.cacheGet(CacheKey.shipFromPostalCode);
  }

  /**
   * Destination country code.
   */
  public java.lang.String getDestinationCountryCode() {
    return genClient.cacheGet(CacheKey.destinationCountryCode);
  }

  /**
   * Merchant tax id.
   */
  public java.lang.String getMerchantTaxId() {
    return genClient.cacheGet(CacheKey.merchantTaxId);
  }

  /**
   * Description of the product.
   */
  public java.lang.String getProductDescription() {
    return genClient.cacheGet(CacheKey.productDescription);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    taxAmount
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    taxIndicator
        (com.clover.sdk.extractors.EnumExtractionStrategy.instance(com.clover.sdk.v3.payments.PcTaxIndicator.class)),
    vatTaxAmount
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    vatTaxRate
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Integer.class)),
    purchaseIdentifier
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    pcOrderNumber
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    discountAmount
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    freightAmount
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    dutyAmount
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    destinationPostalCode
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    shipFromPostalCode
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    destinationCountryCode
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    merchantTaxId
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    productDescription
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

  private final GenericClient<PurchaseCardL2> genClient;

  /**
   * Constructs a new empty instance.
   */
  public PurchaseCardL2() {
    genClient = new GenericClient<PurchaseCardL2>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected PurchaseCardL2(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public PurchaseCardL2(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public PurchaseCardL2(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public PurchaseCardL2(PurchaseCardL2 src) {
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

    genClient.validateLength(CacheKey.purchaseIdentifier, getPurchaseIdentifier(), 22);

    genClient.validateLength(CacheKey.pcOrderNumber, getPcOrderNumber(), 16);

    genClient.validateLength(CacheKey.destinationPostalCode, getDestinationPostalCode(), 9);

    genClient.validateLength(CacheKey.shipFromPostalCode, getShipFromPostalCode(), 9);

    genClient.validateLength(CacheKey.destinationCountryCode, getDestinationCountryCode(), 3);

    genClient.validateLength(CacheKey.merchantTaxId, getMerchantTaxId(), 15);

    genClient.validateLength(CacheKey.productDescription, getProductDescription(), 160);
  }

  /** Checks whether the 'taxAmount' field is set and is not null */
  public boolean isNotNullTaxAmount() {
    return genClient.cacheValueIsNotNull(CacheKey.taxAmount);
  }

  /** Checks whether the 'taxIndicator' field is set and is not null */
  public boolean isNotNullTaxIndicator() {
    return genClient.cacheValueIsNotNull(CacheKey.taxIndicator);
  }

  /** Checks whether the 'vatTaxAmount' field is set and is not null */
  public boolean isNotNullVatTaxAmount() {
    return genClient.cacheValueIsNotNull(CacheKey.vatTaxAmount);
  }

  /** Checks whether the 'vatTaxRate' field is set and is not null */
  public boolean isNotNullVatTaxRate() {
    return genClient.cacheValueIsNotNull(CacheKey.vatTaxRate);
  }

  /** Checks whether the 'purchaseIdentifier' field is set and is not null */
  public boolean isNotNullPurchaseIdentifier() {
    return genClient.cacheValueIsNotNull(CacheKey.purchaseIdentifier);
  }

  /** Checks whether the 'pcOrderNumber' field is set and is not null */
  public boolean isNotNullPcOrderNumber() {
    return genClient.cacheValueIsNotNull(CacheKey.pcOrderNumber);
  }

  /** Checks whether the 'discountAmount' field is set and is not null */
  public boolean isNotNullDiscountAmount() {
    return genClient.cacheValueIsNotNull(CacheKey.discountAmount);
  }

  /** Checks whether the 'freightAmount' field is set and is not null */
  public boolean isNotNullFreightAmount() {
    return genClient.cacheValueIsNotNull(CacheKey.freightAmount);
  }

  /** Checks whether the 'dutyAmount' field is set and is not null */
  public boolean isNotNullDutyAmount() {
    return genClient.cacheValueIsNotNull(CacheKey.dutyAmount);
  }

  /** Checks whether the 'destinationPostalCode' field is set and is not null */
  public boolean isNotNullDestinationPostalCode() {
    return genClient.cacheValueIsNotNull(CacheKey.destinationPostalCode);
  }

  /** Checks whether the 'shipFromPostalCode' field is set and is not null */
  public boolean isNotNullShipFromPostalCode() {
    return genClient.cacheValueIsNotNull(CacheKey.shipFromPostalCode);
  }

  /** Checks whether the 'destinationCountryCode' field is set and is not null */
  public boolean isNotNullDestinationCountryCode() {
    return genClient.cacheValueIsNotNull(CacheKey.destinationCountryCode);
  }

  /** Checks whether the 'merchantTaxId' field is set and is not null */
  public boolean isNotNullMerchantTaxId() {
    return genClient.cacheValueIsNotNull(CacheKey.merchantTaxId);
  }

  /** Checks whether the 'productDescription' field is set and is not null */
  public boolean isNotNullProductDescription() {
    return genClient.cacheValueIsNotNull(CacheKey.productDescription);
  }



  /** Checks whether the 'taxAmount' field has been set, however the value could be null */
  public boolean hasTaxAmount() {
    return genClient.cacheHasKey(CacheKey.taxAmount);
  }

  /** Checks whether the 'taxIndicator' field has been set, however the value could be null */
  public boolean hasTaxIndicator() {
    return genClient.cacheHasKey(CacheKey.taxIndicator);
  }

  /** Checks whether the 'vatTaxAmount' field has been set, however the value could be null */
  public boolean hasVatTaxAmount() {
    return genClient.cacheHasKey(CacheKey.vatTaxAmount);
  }

  /** Checks whether the 'vatTaxRate' field has been set, however the value could be null */
  public boolean hasVatTaxRate() {
    return genClient.cacheHasKey(CacheKey.vatTaxRate);
  }

  /** Checks whether the 'purchaseIdentifier' field has been set, however the value could be null */
  public boolean hasPurchaseIdentifier() {
    return genClient.cacheHasKey(CacheKey.purchaseIdentifier);
  }

  /** Checks whether the 'pcOrderNumber' field has been set, however the value could be null */
  public boolean hasPcOrderNumber() {
    return genClient.cacheHasKey(CacheKey.pcOrderNumber);
  }

  /** Checks whether the 'discountAmount' field has been set, however the value could be null */
  public boolean hasDiscountAmount() {
    return genClient.cacheHasKey(CacheKey.discountAmount);
  }

  /** Checks whether the 'freightAmount' field has been set, however the value could be null */
  public boolean hasFreightAmount() {
    return genClient.cacheHasKey(CacheKey.freightAmount);
  }

  /** Checks whether the 'dutyAmount' field has been set, however the value could be null */
  public boolean hasDutyAmount() {
    return genClient.cacheHasKey(CacheKey.dutyAmount);
  }

  /** Checks whether the 'destinationPostalCode' field has been set, however the value could be null */
  public boolean hasDestinationPostalCode() {
    return genClient.cacheHasKey(CacheKey.destinationPostalCode);
  }

  /** Checks whether the 'shipFromPostalCode' field has been set, however the value could be null */
  public boolean hasShipFromPostalCode() {
    return genClient.cacheHasKey(CacheKey.shipFromPostalCode);
  }

  /** Checks whether the 'destinationCountryCode' field has been set, however the value could be null */
  public boolean hasDestinationCountryCode() {
    return genClient.cacheHasKey(CacheKey.destinationCountryCode);
  }

  /** Checks whether the 'merchantTaxId' field has been set, however the value could be null */
  public boolean hasMerchantTaxId() {
    return genClient.cacheHasKey(CacheKey.merchantTaxId);
  }

  /** Checks whether the 'productDescription' field has been set, however the value could be null */
  public boolean hasProductDescription() {
    return genClient.cacheHasKey(CacheKey.productDescription);
  }


  /**
   * Sets the field 'taxAmount'.
   */
  public PurchaseCardL2 setTaxAmount(java.lang.Long taxAmount) {
    return genClient.setOther(taxAmount, CacheKey.taxAmount);
  }

  /**
   * Sets the field 'taxIndicator'.
   */
  public PurchaseCardL2 setTaxIndicator(com.clover.sdk.v3.payments.PcTaxIndicator taxIndicator) {
    return genClient.setOther(taxIndicator, CacheKey.taxIndicator);
  }

  /**
   * Sets the field 'vatTaxAmount'.
   */
  public PurchaseCardL2 setVatTaxAmount(java.lang.Long vatTaxAmount) {
    return genClient.setOther(vatTaxAmount, CacheKey.vatTaxAmount);
  }

  /**
   * Sets the field 'vatTaxRate'.
   */
  public PurchaseCardL2 setVatTaxRate(java.lang.Integer vatTaxRate) {
    return genClient.setOther(vatTaxRate, CacheKey.vatTaxRate);
  }

  /**
   * Sets the field 'purchaseIdentifier'.
   */
  public PurchaseCardL2 setPurchaseIdentifier(java.lang.String purchaseIdentifier) {
    return genClient.setOther(purchaseIdentifier, CacheKey.purchaseIdentifier);
  }

  /**
   * Sets the field 'pcOrderNumber'.
   */
  public PurchaseCardL2 setPcOrderNumber(java.lang.String pcOrderNumber) {
    return genClient.setOther(pcOrderNumber, CacheKey.pcOrderNumber);
  }

  /**
   * Sets the field 'discountAmount'.
   */
  public PurchaseCardL2 setDiscountAmount(java.lang.Long discountAmount) {
    return genClient.setOther(discountAmount, CacheKey.discountAmount);
  }

  /**
   * Sets the field 'freightAmount'.
   */
  public PurchaseCardL2 setFreightAmount(java.lang.Long freightAmount) {
    return genClient.setOther(freightAmount, CacheKey.freightAmount);
  }

  /**
   * Sets the field 'dutyAmount'.
   */
  public PurchaseCardL2 setDutyAmount(java.lang.Long dutyAmount) {
    return genClient.setOther(dutyAmount, CacheKey.dutyAmount);
  }

  /**
   * Sets the field 'destinationPostalCode'.
   */
  public PurchaseCardL2 setDestinationPostalCode(java.lang.String destinationPostalCode) {
    return genClient.setOther(destinationPostalCode, CacheKey.destinationPostalCode);
  }

  /**
   * Sets the field 'shipFromPostalCode'.
   */
  public PurchaseCardL2 setShipFromPostalCode(java.lang.String shipFromPostalCode) {
    return genClient.setOther(shipFromPostalCode, CacheKey.shipFromPostalCode);
  }

  /**
   * Sets the field 'destinationCountryCode'.
   */
  public PurchaseCardL2 setDestinationCountryCode(java.lang.String destinationCountryCode) {
    return genClient.setOther(destinationCountryCode, CacheKey.destinationCountryCode);
  }

  /**
   * Sets the field 'merchantTaxId'.
   */
  public PurchaseCardL2 setMerchantTaxId(java.lang.String merchantTaxId) {
    return genClient.setOther(merchantTaxId, CacheKey.merchantTaxId);
  }

  /**
   * Sets the field 'productDescription'.
   */
  public PurchaseCardL2 setProductDescription(java.lang.String productDescription) {
    return genClient.setOther(productDescription, CacheKey.productDescription);
  }


  /** Clears the 'taxAmount' field, the 'has' method for this field will now return false */
  public void clearTaxAmount() {
    genClient.clear(CacheKey.taxAmount);
  }
  /** Clears the 'taxIndicator' field, the 'has' method for this field will now return false */
  public void clearTaxIndicator() {
    genClient.clear(CacheKey.taxIndicator);
  }
  /** Clears the 'vatTaxAmount' field, the 'has' method for this field will now return false */
  public void clearVatTaxAmount() {
    genClient.clear(CacheKey.vatTaxAmount);
  }
  /** Clears the 'vatTaxRate' field, the 'has' method for this field will now return false */
  public void clearVatTaxRate() {
    genClient.clear(CacheKey.vatTaxRate);
  }
  /** Clears the 'purchaseIdentifier' field, the 'has' method for this field will now return false */
  public void clearPurchaseIdentifier() {
    genClient.clear(CacheKey.purchaseIdentifier);
  }
  /** Clears the 'pcOrderNumber' field, the 'has' method for this field will now return false */
  public void clearPcOrderNumber() {
    genClient.clear(CacheKey.pcOrderNumber);
  }
  /** Clears the 'discountAmount' field, the 'has' method for this field will now return false */
  public void clearDiscountAmount() {
    genClient.clear(CacheKey.discountAmount);
  }
  /** Clears the 'freightAmount' field, the 'has' method for this field will now return false */
  public void clearFreightAmount() {
    genClient.clear(CacheKey.freightAmount);
  }
  /** Clears the 'dutyAmount' field, the 'has' method for this field will now return false */
  public void clearDutyAmount() {
    genClient.clear(CacheKey.dutyAmount);
  }
  /** Clears the 'destinationPostalCode' field, the 'has' method for this field will now return false */
  public void clearDestinationPostalCode() {
    genClient.clear(CacheKey.destinationPostalCode);
  }
  /** Clears the 'shipFromPostalCode' field, the 'has' method for this field will now return false */
  public void clearShipFromPostalCode() {
    genClient.clear(CacheKey.shipFromPostalCode);
  }
  /** Clears the 'destinationCountryCode' field, the 'has' method for this field will now return false */
  public void clearDestinationCountryCode() {
    genClient.clear(CacheKey.destinationCountryCode);
  }
  /** Clears the 'merchantTaxId' field, the 'has' method for this field will now return false */
  public void clearMerchantTaxId() {
    genClient.clear(CacheKey.merchantTaxId);
  }
  /** Clears the 'productDescription' field, the 'has' method for this field will now return false */
  public void clearProductDescription() {
    genClient.clear(CacheKey.productDescription);
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
  public PurchaseCardL2 copyChanges() {
    PurchaseCardL2 copy = new PurchaseCardL2();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(PurchaseCardL2 src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new PurchaseCardL2(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<PurchaseCardL2> CREATOR = new android.os.Parcelable.Creator<PurchaseCardL2>() {
    @Override
    public PurchaseCardL2 createFromParcel(android.os.Parcel in) {
      PurchaseCardL2 instance = new PurchaseCardL2(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public PurchaseCardL2[] newArray(int size) {
      return new PurchaseCardL2[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<PurchaseCardL2> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<PurchaseCardL2>() {
    public Class<PurchaseCardL2> getCreatedClass() {
      return PurchaseCardL2.class;
    }

    @Override
    public PurchaseCardL2 create(org.json.JSONObject jsonObject) {
      return new PurchaseCardL2(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean TAXAMOUNT_IS_REQUIRED = false;
    public static final long TAXAMOUNT_MAX_LEN = 12;
    public static final boolean TAXINDICATOR_IS_REQUIRED = false;
    public static final boolean VATTAXAMOUNT_IS_REQUIRED = false;
    public static final long VATTAXAMOUNT_MAX_LEN = 12;
    public static final boolean VATTAXRATE_IS_REQUIRED = false;
    public static final long VATTAXRATE_MAX_LEN = 4;
    public static final boolean PURCHASEIDENTIFIER_IS_REQUIRED = false;
    public static final long PURCHASEIDENTIFIER_MAX_LEN = 22;
    public static final boolean PCORDERNUMBER_IS_REQUIRED = false;
    public static final long PCORDERNUMBER_MAX_LEN = 16;
    public static final boolean DISCOUNTAMOUNT_IS_REQUIRED = false;
    public static final long DISCOUNTAMOUNT_MAX_LEN = 12;
    public static final boolean FREIGHTAMOUNT_IS_REQUIRED = false;
    public static final long FREIGHTAMOUNT_MAX_LEN = 12;
    public static final boolean DUTYAMOUNT_IS_REQUIRED = false;
    public static final long DUTYAMOUNT_MAX_LEN = 12;
    public static final boolean DESTINATIONPOSTALCODE_IS_REQUIRED = false;
    public static final long DESTINATIONPOSTALCODE_MAX_LEN = 9;
    public static final boolean SHIPFROMPOSTALCODE_IS_REQUIRED = false;
    public static final long SHIPFROMPOSTALCODE_MAX_LEN = 9;
    public static final boolean DESTINATIONCOUNTRYCODE_IS_REQUIRED = false;
    public static final long DESTINATIONCOUNTRYCODE_MAX_LEN = 3;
    public static final boolean MERCHANTTAXID_IS_REQUIRED = false;
    public static final long MERCHANTTAXID_MAX_LEN = 15;
    public static final boolean PRODUCTDESCRIPTION_IS_REQUIRED = false;
    public static final long PRODUCTDESCRIPTION_MAX_LEN = 160;
  }

}