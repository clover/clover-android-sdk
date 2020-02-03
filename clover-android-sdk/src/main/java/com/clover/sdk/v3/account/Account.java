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

package com.clover.sdk.v3.account;

import com.clover.sdk.GenericClient;
import com.clover.sdk.GenericParcelable;

/**
 * This is an auto-generated Clover data object.
 * <p>
 * <h3>Fields</h3>
 * <ul>
 * <li>{@link #getId id}</li>
 * <li>{@link #getName name}</li>
 * <li>{@link #getEmail email}</li>
 * <li>{@link #getPrimaryMerchant primaryMerchant}</li>
 * <li>{@link #getPrimaryDeveloper primaryDeveloper}</li>
 * <li>{@link #getPrimaryReseller primaryReseller}</li>
 * <li>{@link #getPrimaryEnterprise primaryEnterprise}</li>
 * <li>{@link #getIsActive isActive}</li>
 * <li>{@link #getCreatedTime createdTime}</li>
 * <li>{@link #getClaimedTime claimedTime}</li>
 * <li>{@link #getLastLogin lastLogin}</li>
 * <li>{@link #getInviteSent inviteSent}</li>
 * <li>{@link #getStatus status}</li>
 * <li>{@link #getRole role}</li>
 * <li>{@link #getMerchants merchants}</li>
 * <li>{@link #getDevelopers developers}</li>
 * <li>{@link #getResellers resellers}</li>
 * <li>{@link #getCsrfToken csrfToken}</li>
 * <li>{@link #getAuthFactors authFactors}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class Account extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  public java.lang.String getId() {
    return genClient.cacheGet(CacheKey.id);
  }

  public java.lang.String getName() {
    return genClient.cacheGet(CacheKey.name);
  }

  public java.lang.String getEmail() {
    return genClient.cacheGet(CacheKey.email);
  }

  /**
   * The primary merchant
   */
  public com.clover.sdk.v3.base.Reference getPrimaryMerchant() {
    return genClient.cacheGet(CacheKey.primaryMerchant);
  }

  /**
   * The primary developer
   */
  public com.clover.sdk.v3.base.Reference getPrimaryDeveloper() {
    return genClient.cacheGet(CacheKey.primaryDeveloper);
  }

  /**
   * The primary reseller
   */
  public com.clover.sdk.v3.base.Reference getPrimaryReseller() {
    return genClient.cacheGet(CacheKey.primaryReseller);
  }

  /**
   * The primary enterprise
   */
  public com.clover.sdk.v3.base.Reference getPrimaryEnterprise() {
    return genClient.cacheGet(CacheKey.primaryEnterprise);
  }

  public java.lang.Boolean getIsActive() {
    return genClient.cacheGet(CacheKey.isActive);
  }

  public java.lang.Long getCreatedTime() {
    return genClient.cacheGet(CacheKey.createdTime);
  }

  public java.lang.Long getClaimedTime() {
    return genClient.cacheGet(CacheKey.claimedTime);
  }

  public java.lang.Long getLastLogin() {
    return genClient.cacheGet(CacheKey.lastLogin);
  }

  public java.lang.Boolean getInviteSent() {
    return genClient.cacheGet(CacheKey.inviteSent);
  }

  public java.lang.String getStatus() {
    return genClient.cacheGet(CacheKey.status);
  }

  public com.clover.sdk.v3.base.Reference getRole() {
    return genClient.cacheGet(CacheKey.role);
  }

  public java.util.List<com.clover.sdk.v3.base.Reference> getMerchants() {
    return genClient.cacheGet(CacheKey.merchants);
  }

  public java.util.List<com.clover.sdk.v3.base.Reference> getDevelopers() {
    return genClient.cacheGet(CacheKey.developers);
  }

  public java.util.List<com.clover.sdk.v3.base.Reference> getResellers() {
    return genClient.cacheGet(CacheKey.resellers);
  }

  public java.lang.String getCsrfToken() {
    return genClient.cacheGet(CacheKey.csrfToken);
  }

  public java.util.List<com.clover.sdk.v3.base.Reference> getAuthFactors() {
    return genClient.cacheGet(CacheKey.authFactors);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    id
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    name
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    email
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    primaryMerchant
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.base.Reference.JSON_CREATOR)),
    primaryDeveloper
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.base.Reference.JSON_CREATOR)),
    primaryReseller
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.base.Reference.JSON_CREATOR)),
    primaryEnterprise
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.base.Reference.JSON_CREATOR)),
    isActive
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Boolean.class)),
    createdTime
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    claimedTime
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    lastLogin
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    inviteSent
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Boolean.class)),
    status
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    role
        (com.clover.sdk.extractors.RecordExtractionStrategy.instance(com.clover.sdk.v3.base.Reference.JSON_CREATOR)),
    merchants
        (com.clover.sdk.extractors.RecordListExtractionStrategy.instance(com.clover.sdk.v3.base.Reference.JSON_CREATOR)),
    developers
        (com.clover.sdk.extractors.RecordListExtractionStrategy.instance(com.clover.sdk.v3.base.Reference.JSON_CREATOR)),
    resellers
        (com.clover.sdk.extractors.RecordListExtractionStrategy.instance(com.clover.sdk.v3.base.Reference.JSON_CREATOR)),
    csrfToken
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    authFactors
        (com.clover.sdk.extractors.RecordListExtractionStrategy.instance(com.clover.sdk.v3.base.Reference.JSON_CREATOR)),
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

  private final GenericClient<Account> genClient;

  /**
   * Constructs a new empty instance.
   */
  public Account() {
    genClient = new GenericClient<Account>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected Account(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public Account(String json) throws IllegalArgumentException {
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
  public Account(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public Account(Account src) {
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
    genClient.validateLength(getId(), 13);

    genClient.validateNull(getName(), "name");
    genClient.validateLength(getName(), 127);

    genClient.validateNull(getEmail(), "email");
    genClient.validateLength(getEmail(), 127);

    genClient.validateLength(getCsrfToken(), 127);
  }

  /** Checks whether the 'id' field is set and is not null */
  public boolean isNotNullId() {
    return genClient.cacheValueIsNotNull(CacheKey.id);
  }

  /** Checks whether the 'name' field is set and is not null */
  public boolean isNotNullName() {
    return genClient.cacheValueIsNotNull(CacheKey.name);
  }

  /** Checks whether the 'email' field is set and is not null */
  public boolean isNotNullEmail() {
    return genClient.cacheValueIsNotNull(CacheKey.email);
  }

  /** Checks whether the 'primaryMerchant' field is set and is not null */
  public boolean isNotNullPrimaryMerchant() {
    return genClient.cacheValueIsNotNull(CacheKey.primaryMerchant);
  }

  /** Checks whether the 'primaryDeveloper' field is set and is not null */
  public boolean isNotNullPrimaryDeveloper() {
    return genClient.cacheValueIsNotNull(CacheKey.primaryDeveloper);
  }

  /** Checks whether the 'primaryReseller' field is set and is not null */
  public boolean isNotNullPrimaryReseller() {
    return genClient.cacheValueIsNotNull(CacheKey.primaryReseller);
  }

  /** Checks whether the 'primaryEnterprise' field is set and is not null */
  public boolean isNotNullPrimaryEnterprise() {
    return genClient.cacheValueIsNotNull(CacheKey.primaryEnterprise);
  }

  /** Checks whether the 'isActive' field is set and is not null */
  public boolean isNotNullIsActive() {
    return genClient.cacheValueIsNotNull(CacheKey.isActive);
  }

  /** Checks whether the 'createdTime' field is set and is not null */
  public boolean isNotNullCreatedTime() {
    return genClient.cacheValueIsNotNull(CacheKey.createdTime);
  }

  /** Checks whether the 'claimedTime' field is set and is not null */
  public boolean isNotNullClaimedTime() {
    return genClient.cacheValueIsNotNull(CacheKey.claimedTime);
  }

  /** Checks whether the 'lastLogin' field is set and is not null */
  public boolean isNotNullLastLogin() {
    return genClient.cacheValueIsNotNull(CacheKey.lastLogin);
  }

  /** Checks whether the 'inviteSent' field is set and is not null */
  public boolean isNotNullInviteSent() {
    return genClient.cacheValueIsNotNull(CacheKey.inviteSent);
  }

  /** Checks whether the 'status' field is set and is not null */
  public boolean isNotNullStatus() {
    return genClient.cacheValueIsNotNull(CacheKey.status);
  }

  /** Checks whether the 'role' field is set and is not null */
  public boolean isNotNullRole() {
    return genClient.cacheValueIsNotNull(CacheKey.role);
  }

  /** Checks whether the 'merchants' field is set and is not null */
  public boolean isNotNullMerchants() {
    return genClient.cacheValueIsNotNull(CacheKey.merchants);
  }

  /** Checks whether the 'merchants' field is set and is not null and is not empty */
  public boolean isNotEmptyMerchants() { return isNotNullMerchants() && !getMerchants().isEmpty(); }

  /** Checks whether the 'developers' field is set and is not null */
  public boolean isNotNullDevelopers() {
    return genClient.cacheValueIsNotNull(CacheKey.developers);
  }

  /** Checks whether the 'developers' field is set and is not null and is not empty */
  public boolean isNotEmptyDevelopers() { return isNotNullDevelopers() && !getDevelopers().isEmpty(); }

  /** Checks whether the 'resellers' field is set and is not null */
  public boolean isNotNullResellers() {
    return genClient.cacheValueIsNotNull(CacheKey.resellers);
  }

  /** Checks whether the 'resellers' field is set and is not null and is not empty */
  public boolean isNotEmptyResellers() { return isNotNullResellers() && !getResellers().isEmpty(); }

  /** Checks whether the 'csrfToken' field is set and is not null */
  public boolean isNotNullCsrfToken() {
    return genClient.cacheValueIsNotNull(CacheKey.csrfToken);
  }

  /** Checks whether the 'authFactors' field is set and is not null */
  public boolean isNotNullAuthFactors() {
    return genClient.cacheValueIsNotNull(CacheKey.authFactors);
  }

  /** Checks whether the 'authFactors' field is set and is not null and is not empty */
  public boolean isNotEmptyAuthFactors() { return isNotNullAuthFactors() && !getAuthFactors().isEmpty(); }



  /** Checks whether the 'id' field has been set, however the value could be null */
  public boolean hasId() {
    return genClient.cacheHasKey(CacheKey.id);
  }

  /** Checks whether the 'name' field has been set, however the value could be null */
  public boolean hasName() {
    return genClient.cacheHasKey(CacheKey.name);
  }

  /** Checks whether the 'email' field has been set, however the value could be null */
  public boolean hasEmail() {
    return genClient.cacheHasKey(CacheKey.email);
  }

  /** Checks whether the 'primaryMerchant' field has been set, however the value could be null */
  public boolean hasPrimaryMerchant() {
    return genClient.cacheHasKey(CacheKey.primaryMerchant);
  }

  /** Checks whether the 'primaryDeveloper' field has been set, however the value could be null */
  public boolean hasPrimaryDeveloper() {
    return genClient.cacheHasKey(CacheKey.primaryDeveloper);
  }

  /** Checks whether the 'primaryReseller' field has been set, however the value could be null */
  public boolean hasPrimaryReseller() {
    return genClient.cacheHasKey(CacheKey.primaryReseller);
  }

  /** Checks whether the 'primaryEnterprise' field has been set, however the value could be null */
  public boolean hasPrimaryEnterprise() {
    return genClient.cacheHasKey(CacheKey.primaryEnterprise);
  }

  /** Checks whether the 'isActive' field has been set, however the value could be null */
  public boolean hasIsActive() {
    return genClient.cacheHasKey(CacheKey.isActive);
  }

  /** Checks whether the 'createdTime' field has been set, however the value could be null */
  public boolean hasCreatedTime() {
    return genClient.cacheHasKey(CacheKey.createdTime);
  }

  /** Checks whether the 'claimedTime' field has been set, however the value could be null */
  public boolean hasClaimedTime() {
    return genClient.cacheHasKey(CacheKey.claimedTime);
  }

  /** Checks whether the 'lastLogin' field has been set, however the value could be null */
  public boolean hasLastLogin() {
    return genClient.cacheHasKey(CacheKey.lastLogin);
  }

  /** Checks whether the 'inviteSent' field has been set, however the value could be null */
  public boolean hasInviteSent() {
    return genClient.cacheHasKey(CacheKey.inviteSent);
  }

  /** Checks whether the 'status' field has been set, however the value could be null */
  public boolean hasStatus() {
    return genClient.cacheHasKey(CacheKey.status);
  }

  /** Checks whether the 'role' field has been set, however the value could be null */
  public boolean hasRole() {
    return genClient.cacheHasKey(CacheKey.role);
  }

  /** Checks whether the 'merchants' field has been set, however the value could be null */
  public boolean hasMerchants() {
    return genClient.cacheHasKey(CacheKey.merchants);
  }

  /** Checks whether the 'developers' field has been set, however the value could be null */
  public boolean hasDevelopers() {
    return genClient.cacheHasKey(CacheKey.developers);
  }

  /** Checks whether the 'resellers' field has been set, however the value could be null */
  public boolean hasResellers() {
    return genClient.cacheHasKey(CacheKey.resellers);
  }

  /** Checks whether the 'csrfToken' field has been set, however the value could be null */
  public boolean hasCsrfToken() {
    return genClient.cacheHasKey(CacheKey.csrfToken);
  }

  /** Checks whether the 'authFactors' field has been set, however the value could be null */
  public boolean hasAuthFactors() {
    return genClient.cacheHasKey(CacheKey.authFactors);
  }


  /**
   * Sets the field 'id'.
   */
  public Account setId(java.lang.String id) {
    return genClient.setOther(id, CacheKey.id);
  }

  /**
   * Sets the field 'name'.
   */
  public Account setName(java.lang.String name) {
    return genClient.setOther(name, CacheKey.name);
  }

  /**
   * Sets the field 'email'.
   */
  public Account setEmail(java.lang.String email) {
    return genClient.setOther(email, CacheKey.email);
  }

  /**
   * Sets the field 'primaryMerchant'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public Account setPrimaryMerchant(com.clover.sdk.v3.base.Reference primaryMerchant) {
    return genClient.setRecord(primaryMerchant, CacheKey.primaryMerchant);
  }

  /**
   * Sets the field 'primaryDeveloper'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public Account setPrimaryDeveloper(com.clover.sdk.v3.base.Reference primaryDeveloper) {
    return genClient.setRecord(primaryDeveloper, CacheKey.primaryDeveloper);
  }

  /**
   * Sets the field 'primaryReseller'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public Account setPrimaryReseller(com.clover.sdk.v3.base.Reference primaryReseller) {
    return genClient.setRecord(primaryReseller, CacheKey.primaryReseller);
  }

  /**
   * Sets the field 'primaryEnterprise'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public Account setPrimaryEnterprise(com.clover.sdk.v3.base.Reference primaryEnterprise) {
    return genClient.setRecord(primaryEnterprise, CacheKey.primaryEnterprise);
  }

  /**
   * Sets the field 'isActive'.
   */
  public Account setIsActive(java.lang.Boolean isActive) {
    return genClient.setOther(isActive, CacheKey.isActive);
  }

  /**
   * Sets the field 'createdTime'.
   */
  public Account setCreatedTime(java.lang.Long createdTime) {
    return genClient.setOther(createdTime, CacheKey.createdTime);
  }

  /**
   * Sets the field 'claimedTime'.
   */
  public Account setClaimedTime(java.lang.Long claimedTime) {
    return genClient.setOther(claimedTime, CacheKey.claimedTime);
  }

  /**
   * Sets the field 'lastLogin'.
   */
  public Account setLastLogin(java.lang.Long lastLogin) {
    return genClient.setOther(lastLogin, CacheKey.lastLogin);
  }

  /**
   * Sets the field 'inviteSent'.
   */
  public Account setInviteSent(java.lang.Boolean inviteSent) {
    return genClient.setOther(inviteSent, CacheKey.inviteSent);
  }

  /**
   * Sets the field 'status'.
   */
  public Account setStatus(java.lang.String status) {
    return genClient.setOther(status, CacheKey.status);
  }

  /**
   * Sets the field 'role'.
   *
   * The parameter is not copied so changes to it will be reflected in this instance and vice-versa.
   */
  public Account setRole(com.clover.sdk.v3.base.Reference role) {
    return genClient.setRecord(role, CacheKey.role);
  }

  /**
   * Sets the field 'merchants'.
   *
   * Nulls in the given List are skipped. List parameter is copied, so it will not reflect any changes, but objects inside it will.
   */
  public Account setMerchants(java.util.List<com.clover.sdk.v3.base.Reference> merchants) {
    return genClient.setArrayRecord(merchants, CacheKey.merchants);
  }

  /**
   * Sets the field 'developers'.
   *
   * Nulls in the given List are skipped. List parameter is copied, so it will not reflect any changes, but objects inside it will.
   */
  public Account setDevelopers(java.util.List<com.clover.sdk.v3.base.Reference> developers) {
    return genClient.setArrayRecord(developers, CacheKey.developers);
  }

  /**
   * Sets the field 'resellers'.
   *
   * Nulls in the given List are skipped. List parameter is copied, so it will not reflect any changes, but objects inside it will.
   */
  public Account setResellers(java.util.List<com.clover.sdk.v3.base.Reference> resellers) {
    return genClient.setArrayRecord(resellers, CacheKey.resellers);
  }

  /**
   * Sets the field 'csrfToken'.
   */
  public Account setCsrfToken(java.lang.String csrfToken) {
    return genClient.setOther(csrfToken, CacheKey.csrfToken);
  }

  /**
   * Sets the field 'authFactors'.
   *
   * Nulls in the given List are skipped. List parameter is copied, so it will not reflect any changes, but objects inside it will.
   */
  public Account setAuthFactors(java.util.List<com.clover.sdk.v3.base.Reference> authFactors) {
    return genClient.setArrayRecord(authFactors, CacheKey.authFactors);
  }


  /** Clears the 'id' field, the 'has' method for this field will now return false */
  public void clearId() {
    genClient.clear(CacheKey.id);
  }
  /** Clears the 'name' field, the 'has' method for this field will now return false */
  public void clearName() {
    genClient.clear(CacheKey.name);
  }
  /** Clears the 'email' field, the 'has' method for this field will now return false */
  public void clearEmail() {
    genClient.clear(CacheKey.email);
  }
  /** Clears the 'primaryMerchant' field, the 'has' method for this field will now return false */
  public void clearPrimaryMerchant() {
    genClient.clear(CacheKey.primaryMerchant);
  }
  /** Clears the 'primaryDeveloper' field, the 'has' method for this field will now return false */
  public void clearPrimaryDeveloper() {
    genClient.clear(CacheKey.primaryDeveloper);
  }
  /** Clears the 'primaryReseller' field, the 'has' method for this field will now return false */
  public void clearPrimaryReseller() {
    genClient.clear(CacheKey.primaryReseller);
  }
  /** Clears the 'primaryEnterprise' field, the 'has' method for this field will now return false */
  public void clearPrimaryEnterprise() {
    genClient.clear(CacheKey.primaryEnterprise);
  }
  /** Clears the 'isActive' field, the 'has' method for this field will now return false */
  public void clearIsActive() {
    genClient.clear(CacheKey.isActive);
  }
  /** Clears the 'createdTime' field, the 'has' method for this field will now return false */
  public void clearCreatedTime() {
    genClient.clear(CacheKey.createdTime);
  }
  /** Clears the 'claimedTime' field, the 'has' method for this field will now return false */
  public void clearClaimedTime() {
    genClient.clear(CacheKey.claimedTime);
  }
  /** Clears the 'lastLogin' field, the 'has' method for this field will now return false */
  public void clearLastLogin() {
    genClient.clear(CacheKey.lastLogin);
  }
  /** Clears the 'inviteSent' field, the 'has' method for this field will now return false */
  public void clearInviteSent() {
    genClient.clear(CacheKey.inviteSent);
  }
  /** Clears the 'status' field, the 'has' method for this field will now return false */
  public void clearStatus() {
    genClient.clear(CacheKey.status);
  }
  /** Clears the 'role' field, the 'has' method for this field will now return false */
  public void clearRole() {
    genClient.clear(CacheKey.role);
  }
  /** Clears the 'merchants' field, the 'has' method for this field will now return false */
  public void clearMerchants() {
    genClient.clear(CacheKey.merchants);
  }
  /** Clears the 'developers' field, the 'has' method for this field will now return false */
  public void clearDevelopers() {
    genClient.clear(CacheKey.developers);
  }
  /** Clears the 'resellers' field, the 'has' method for this field will now return false */
  public void clearResellers() {
    genClient.clear(CacheKey.resellers);
  }
  /** Clears the 'csrfToken' field, the 'has' method for this field will now return false */
  public void clearCsrfToken() {
    genClient.clear(CacheKey.csrfToken);
  }
  /** Clears the 'authFactors' field, the 'has' method for this field will now return false */
  public void clearAuthFactors() {
    genClient.clear(CacheKey.authFactors);
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
  public Account copyChanges() {
    Account copy = new Account();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(Account src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new Account(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<Account> CREATOR = new android.os.Parcelable.Creator<Account>() {
    @Override
    public Account createFromParcel(android.os.Parcel in) {
      Account instance = new Account(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public Account[] newArray(int size) {
      return new Account[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<Account> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<Account>() {
    @Override
    public Account create(org.json.JSONObject jsonObject) {
      return new Account(jsonObject);
    }
  };

  public interface Constraints {

    public static final boolean ID_IS_REQUIRED = false;
    public static final long ID_MAX_LEN = 13;
    public static final boolean NAME_IS_REQUIRED = true;
    public static final long NAME_MAX_LEN = 127;
    public static final boolean EMAIL_IS_REQUIRED = true;
    public static final long EMAIL_MAX_LEN = 127;
    public static final boolean PRIMARYMERCHANT_IS_REQUIRED = false;
    public static final boolean PRIMARYDEVELOPER_IS_REQUIRED = false;
    public static final boolean PRIMARYRESELLER_IS_REQUIRED = false;
    public static final boolean PRIMARYENTERPRISE_IS_REQUIRED = false;
    public static final boolean ISACTIVE_IS_REQUIRED = false;
    public static final boolean CREATEDTIME_IS_REQUIRED = false;
    public static final boolean CLAIMEDTIME_IS_REQUIRED = false;
    public static final boolean LASTLOGIN_IS_REQUIRED = false;
    public static final boolean INVITESENT_IS_REQUIRED = false;
    public static final boolean STATUS_IS_REQUIRED = false;
    public static final boolean ROLE_IS_REQUIRED = false;
    public static final boolean MERCHANTS_IS_REQUIRED = false;
    public static final boolean DEVELOPERS_IS_REQUIRED = false;
    public static final boolean RESELLERS_IS_REQUIRED = false;
    public static final boolean CSRFTOKEN_IS_REQUIRED = false;
    public static final long CSRFTOKEN_MAX_LEN = 127;
    public static final boolean AUTHFACTORS_IS_REQUIRED = false;

  }

}
