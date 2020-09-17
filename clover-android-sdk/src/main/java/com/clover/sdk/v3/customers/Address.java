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

package com.clover.sdk.v3.customers;

import com.clover.sdk.GenericClient;
import com.clover.sdk.GenericParcelable;

/**
 * This is an auto-generated Clover data object.
 * <p>
 * <h3>Fields</h3>
 * <ul>
 * <li>{@link #getId id}</li>
 * <li>{@link #getAddress1 address1}</li>
 * <li>{@link #getAddress2 address2}</li>
 * <li>{@link #getAddress3 address3}</li>
 * <li>{@link #getCity city}</li>
 * <li>{@link #getCountry country}</li>
 * <li>{@link #getState state}</li>
 * <li>{@link #getZip zip}</li>
 * <li>{@link #getCustomer customer}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class Address extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  public java.lang.String getId() {
    return genClient.cacheGet(CacheKey.id);
  }

  public java.lang.String getAddress1() {
    return genClient.cacheGet(CacheKey.address1);
  }

  public java.lang.String getAddress2() {
    return genClient.cacheGet(CacheKey.address2);
  }

  public java.lang.String getAddress3() {
    return genClient.cacheGet(CacheKey.address3);
  }

  public java.lang.String getCity() {
    return genClient.cacheGet(CacheKey.city);
  }

  public java.lang.String getCountry() {
    return genClient.cacheGet(CacheKey.country);
  }

  public java.lang.String getState() {
    return genClient.cacheGet(CacheKey.state);
  }

  public java.lang.String getZip() {
    return genClient.cacheGet(CacheKey.zip);
  }

  /**
   * Customer who this address belongs to.
   */
  public com.clover.sdk.v3.base.Reference getCustomer() {
    return genClient.cacheGet(CacheKey.customer);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    id
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    address1
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    address2
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    address3
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    city
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    country
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    state
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    zip
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    customer
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.base.Reference.JSON_CREATOR)),
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

  private final GenericClient<Address> genClient;

  /**
   * Constructs a new empty instance.
   */
  public Address() {
    genClient = new GenericClient<Address>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected Address(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public Address(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public Address(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public Address(Address src) {
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

    genClient.validateNotNull(CacheKey.address1, getAddress1());
    genClient.validateLength(CacheKey.address1, getAddress1(), 127);

    genClient.validateLength(CacheKey.address2, getAddress2(), 127);

    genClient.validateLength(CacheKey.address3, getAddress3(), 127);

    genClient.validateNotNull(CacheKey.city, getCity());
    genClient.validateLength(CacheKey.city, getCity(), 127);

    genClient.validateNotNull(CacheKey.country, getCountry());
    genClient.validateLength(CacheKey.country, getCountry(), 2);

    genClient.validateNotNull(CacheKey.state, getState());
    genClient.validateLength(CacheKey.state, getState(), 127);

    genClient.validateNotNull(CacheKey.zip, getZip());
    genClient.validateLength(CacheKey.zip, getZip(), 10);
    genClient.validateReferences(CacheKey.customer);
  }

  /** Checks whether the 'id' field is set and is not null */
  public boolean isNotNullId() {
    return genClient.cacheValueIsNotNull(CacheKey.id);
  }

  /** Checks whether the 'address1' field is set and is not null */
  public boolean isNotNullAddress1() {
    return genClient.cacheValueIsNotNull(CacheKey.address1);
  }

  /** Checks whether the 'address2' field is set and is not null */
  public boolean isNotNullAddress2() {
    return genClient.cacheValueIsNotNull(CacheKey.address2);
  }

  /** Checks whether the 'address3' field is set and is not null */
  public boolean isNotNullAddress3() {
    return genClient.cacheValueIsNotNull(CacheKey.address3);
  }

  /** Checks whether the 'city' field is set and is not null */
  public boolean isNotNullCity() {
    return genClient.cacheValueIsNotNull(CacheKey.city);
  }

  /** Checks whether the 'country' field is set and is not null */
  public boolean isNotNullCountry() {
    return genClient.cacheValueIsNotNull(CacheKey.country);
  }

  /** Checks whether the 'state' field is set and is not null */
  public boolean isNotNullState() {
    return genClient.cacheValueIsNotNull(CacheKey.state);
  }

  /** Checks whether the 'zip' field is set and is not null */
  public boolean isNotNullZip() {
    return genClient.cacheValueIsNotNull(CacheKey.zip);
  }

  /** Checks whether the 'customer' field is set and is not null */
  public boolean isNotNullCustomer() {
    return genClient.cacheValueIsNotNull(CacheKey.customer);
  }



  /** Checks whether the 'id' field has been set, however the value could be null */
  public boolean hasId() {
    return genClient.cacheHasKey(CacheKey.id);
  }

  /** Checks whether the 'address1' field has been set, however the value could be null */
  public boolean hasAddress1() {
    return genClient.cacheHasKey(CacheKey.address1);
  }

  /** Checks whether the 'address2' field has been set, however the value could be null */
  public boolean hasAddress2() {
    return genClient.cacheHasKey(CacheKey.address2);
  }

  /** Checks whether the 'address3' field has been set, however the value could be null */
  public boolean hasAddress3() {
    return genClient.cacheHasKey(CacheKey.address3);
  }

  /** Checks whether the 'city' field has been set, however the value could be null */
  public boolean hasCity() {
    return genClient.cacheHasKey(CacheKey.city);
  }

  /** Checks whether the 'country' field has been set, however the value could be null */
  public boolean hasCountry() {
    return genClient.cacheHasKey(CacheKey.country);
  }

  /** Checks whether the 'state' field has been set, however the value could be null */
  public boolean hasState() {
    return genClient.cacheHasKey(CacheKey.state);
  }

  /** Checks whether the 'zip' field has been set, however the value could be null */
  public boolean hasZip() {
    return genClient.cacheHasKey(CacheKey.zip);
  }

  /** Checks whether the 'customer' field has been set, however the value could be null */
  public boolean hasCustomer() {
    return genClient.cacheHasKey(CacheKey.customer);
  }


  /**
   * Sets the field 'id'.
   */
  public Address setId(java.lang.String id) {
    return genClient.setOther(id, CacheKey.id);
  }

  /**
   * Sets the field 'address1'.
   */
  public Address setAddress1(java.lang.String address1) {
    return genClient.setOther(address1, CacheKey.address1);
  }

  /**
   * Sets the field 'address2'.
   */
  public Address setAddress2(java.lang.String address2) {
    return genClient.setOther(address2, CacheKey.address2);
  }

  /**
   * Sets the field 'address3'.
   */
  public Address setAddress3(java.lang.String address3) {
    return genClient.setOther(address3, CacheKey.address3);
  }

  /**
   * Sets the field 'city'.
   */
  public Address setCity(java.lang.String city) {
    return genClient.setOther(city, CacheKey.city);
  }

  /**
   * Sets the field 'country'.
   */
  public Address setCountry(java.lang.String country) {
    return genClient.setOther(country, CacheKey.country);
  }

  /**
   * Sets the field 'state'.
   */
  public Address setState(java.lang.String state) {
    return genClient.setOther(state, CacheKey.state);
  }

  /**
   * Sets the field 'zip'.
   */
  public Address setZip(java.lang.String zip) {
    return genClient.setOther(zip, CacheKey.zip);
  }

  /**
   * Sets the field 'customer'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public Address setCustomer(com.clover.sdk.v3.base.Reference customer) {
    return genClient.setRecord(customer, CacheKey.customer);
  }


  /** Clears the 'id' field, the 'has' method for this field will now return false */
  public void clearId() {
    genClient.clear(CacheKey.id);
  }
  /** Clears the 'address1' field, the 'has' method for this field will now return false */
  public void clearAddress1() {
    genClient.clear(CacheKey.address1);
  }
  /** Clears the 'address2' field, the 'has' method for this field will now return false */
  public void clearAddress2() {
    genClient.clear(CacheKey.address2);
  }
  /** Clears the 'address3' field, the 'has' method for this field will now return false */
  public void clearAddress3() {
    genClient.clear(CacheKey.address3);
  }
  /** Clears the 'city' field, the 'has' method for this field will now return false */
  public void clearCity() {
    genClient.clear(CacheKey.city);
  }
  /** Clears the 'country' field, the 'has' method for this field will now return false */
  public void clearCountry() {
    genClient.clear(CacheKey.country);
  }
  /** Clears the 'state' field, the 'has' method for this field will now return false */
  public void clearState() {
    genClient.clear(CacheKey.state);
  }
  /** Clears the 'zip' field, the 'has' method for this field will now return false */
  public void clearZip() {
    genClient.clear(CacheKey.zip);
  }
  /** Clears the 'customer' field, the 'has' method for this field will now return false */
  public void clearCustomer() {
    genClient.clear(CacheKey.customer);
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
  public Address copyChanges() {
    Address copy = new Address();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(Address src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new Address(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<Address> CREATOR = new android.os.Parcelable.Creator<Address>() {
    @Override
    public Address createFromParcel(android.os.Parcel in) {
      Address instance = new Address(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public Address[] newArray(int size) {
      return new Address[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<Address> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<Address>() {
    public Class<Address> getCreatedClass() {
      return Address.class;
    }

    @Override
    public Address create(org.json.JSONObject jsonObject) {
      return new Address(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean ID_IS_REQUIRED = false;
    public static final boolean ADDRESS1_IS_REQUIRED = true;
    public static final long ADDRESS1_MAX_LEN = 127;
    public static final boolean ADDRESS2_IS_REQUIRED = false;
    public static final long ADDRESS2_MAX_LEN = 127;
    public static final boolean ADDRESS3_IS_REQUIRED = false;
    public static final long ADDRESS3_MAX_LEN = 127;
    public static final boolean CITY_IS_REQUIRED = true;
    public static final long CITY_MAX_LEN = 127;
    public static final boolean COUNTRY_IS_REQUIRED = true;
    public static final long COUNTRY_MAX_LEN = 2;
    public static final boolean STATE_IS_REQUIRED = true;
    public static final long STATE_MAX_LEN = 127;
    public static final boolean ZIP_IS_REQUIRED = true;
    public static final long ZIP_MAX_LEN = 10;
    public static final boolean CUSTOMER_IS_REQUIRED = false;
  }

}
