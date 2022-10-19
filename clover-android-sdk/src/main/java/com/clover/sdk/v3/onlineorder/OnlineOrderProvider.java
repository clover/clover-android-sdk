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
 * <li>{@link #getName name}</li>
 * <li>{@link #getLogoUrl logoUrl}</li>
 * <li>{@link #getDeveloperAppId developerAppId}</li>
 * <li>{@link #getDeveloperAppPackageName developerAppPackageName}</li>
 * <li>{@link #getToken token}</li>
 * <li>{@link #getAuthtokenUrl authtokenUrl}</li>
 * <li>{@link #getBackendInstall backendInstall}</li>
 * <li>{@link #getAutoOnline autoOnline}</li>
 * <li>{@link #getTrackCustomer trackCustomer}</li>
 * <li>{@link #getManageServices manageServices}</li>
 * <li>{@link #getCreatedTime createdTime}</li>
 * <li>{@link #getModifiedTime modifiedTime}</li>
 * <li>{@link #getDeletedTime deletedTime}</li>
 * <li>{@link #getEnableDeliveryMinimum enableDeliveryMinimum}</li>
 * <li>{@link #getProviderType providerType}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class OnlineOrderProvider extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  /**
   * Provider id
   */
  public java.lang.String getId() {
    return genClient.cacheGet(CacheKey.id);
  }

  /**
   * Provider name
   */
  public java.lang.String getName() {
    return genClient.cacheGet(CacheKey.name);
  }

  /**
   * The url where to retrieve logo
   */
  public java.lang.String getLogoUrl() {
    return genClient.cacheGet(CacheKey.logoUrl);
  }

  /**
   * The developer app id
   */
  public java.lang.String getDeveloperAppId() {
    return genClient.cacheGet(CacheKey.developerAppId);
  }

  /**
   * The developer app package name
   */
  public java.lang.String getDeveloperAppPackageName() {
    return genClient.cacheGet(CacheKey.developerAppPackageName);
  }

  /**
   * The access token used by provider
   */
  public java.lang.String getToken() {
    return genClient.cacheGet(CacheKey.token);
  }

  /**
   * The url where to receive the authtoken when installing the developer app in the backend
   */
  public java.lang.String getAuthtokenUrl() {
    return genClient.cacheGet(CacheKey.authtokenUrl);
  }

  /**
   * If the provider's app needs to be installed silently in backend
   */
  public java.lang.Boolean getBackendInstall() {
    return genClient.cacheGet(CacheKey.backendInstall);
  }

  /**
   * If the merchant's online order service and merchant provider status gets online automatically
   */
  public java.lang.Boolean getAutoOnline() {
    return genClient.cacheGet(CacheKey.autoOnline);
  }

  /**
   * If track customer of the online order
   */
  public java.lang.Boolean getTrackCustomer() {
    return genClient.cacheGet(CacheKey.trackCustomer);
  }

  /**
   * If the provider's services can be managed individually
   */
  public java.lang.Boolean getManageServices() {
    return genClient.cacheGet(CacheKey.manageServices);
  }

  /**
   * Timestamp when the online ordering merchant was created
   */
  public java.lang.Long getCreatedTime() {
    return genClient.cacheGet(CacheKey.createdTime);
  }

  /**
   * Timestamp when the online ordering merchant was last modified
   */
  public java.lang.Long getModifiedTime() {
    return genClient.cacheGet(CacheKey.modifiedTime);
  }

  /**
   * Timestamp when online ordering merchant was last deleted
   */
  public java.lang.Long getDeletedTime() {
    return genClient.cacheGet(CacheKey.deletedTime);
  }

  /**
   * Delivery minimum enabled for the merchant
   */
  public java.lang.Boolean getEnableDeliveryMinimum() {
    return genClient.cacheGet(CacheKey.enableDeliveryMinimum);
  }

  /**
   * Type of provider denotes whether provider is from restaurant or retail
   */
  public com.clover.sdk.v3.onlineorder.ProviderType getProviderType() {
    return genClient.cacheGet(CacheKey.providerType);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    id
            (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    name
            (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    logoUrl
            (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    developerAppId
            (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    developerAppPackageName
            (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    token
            (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    authtokenUrl
            (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    backendInstall
            (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Boolean.class)),
    autoOnline
            (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Boolean.class)),
    trackCustomer
            (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Boolean.class)),
    manageServices
            (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Boolean.class)),
    createdTime
            (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    modifiedTime
            (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    deletedTime
            (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    enableDeliveryMinimum
            (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Boolean.class)),
    providerType
            (com.clover.sdk.extractors.EnumExtractionStrategy.instance(com.clover.sdk.v3.onlineorder.ProviderType.class)),
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

  private final GenericClient<OnlineOrderProvider> genClient;

  /**
   * Constructs a new empty instance.
   */
  public OnlineOrderProvider() {
    genClient = new GenericClient<OnlineOrderProvider>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected OnlineOrderProvider(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public OnlineOrderProvider(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public OnlineOrderProvider(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public OnlineOrderProvider(OnlineOrderProvider src) {
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

    genClient.validateLength(CacheKey.name, getName(), 31);

    genClient.validateLength(CacheKey.logoUrl, getLogoUrl(), 255);

    genClient.validateNotNull(CacheKey.developerAppId, getDeveloperAppId());
    genClient.validateCloverId(CacheKey.developerAppId, getDeveloperAppId());

    genClient.validateLength(CacheKey.developerAppPackageName, getDeveloperAppPackageName(), 255);

    genClient.validateLength(CacheKey.token, getToken(), 255);

    genClient.validateLength(CacheKey.authtokenUrl, getAuthtokenUrl(), 255);
  }

  /** Checks whether the 'id' field is set and is not null */
  public boolean isNotNullId() {
    return genClient.cacheValueIsNotNull(CacheKey.id);
  }

  /** Checks whether the 'name' field is set and is not null */
  public boolean isNotNullName() {
    return genClient.cacheValueIsNotNull(CacheKey.name);
  }

  /** Checks whether the 'logoUrl' field is set and is not null */
  public boolean isNotNullLogoUrl() {
    return genClient.cacheValueIsNotNull(CacheKey.logoUrl);
  }

  /** Checks whether the 'developerAppId' field is set and is not null */
  public boolean isNotNullDeveloperAppId() {
    return genClient.cacheValueIsNotNull(CacheKey.developerAppId);
  }

  /** Checks whether the 'developerAppPackageName' field is set and is not null */
  public boolean isNotNullDeveloperAppPackageName() {
    return genClient.cacheValueIsNotNull(CacheKey.developerAppPackageName);
  }

  /** Checks whether the 'token' field is set and is not null */
  public boolean isNotNullToken() {
    return genClient.cacheValueIsNotNull(CacheKey.token);
  }

  /** Checks whether the 'authtokenUrl' field is set and is not null */
  public boolean isNotNullAuthtokenUrl() {
    return genClient.cacheValueIsNotNull(CacheKey.authtokenUrl);
  }

  /** Checks whether the 'backendInstall' field is set and is not null */
  public boolean isNotNullBackendInstall() {
    return genClient.cacheValueIsNotNull(CacheKey.backendInstall);
  }

  /** Checks whether the 'autoOnline' field is set and is not null */
  public boolean isNotNullAutoOnline() {
    return genClient.cacheValueIsNotNull(CacheKey.autoOnline);
  }

  /** Checks whether the 'trackCustomer' field is set and is not null */
  public boolean isNotNullTrackCustomer() {
    return genClient.cacheValueIsNotNull(CacheKey.trackCustomer);
  }

  /** Checks whether the 'manageServices' field is set and is not null */
  public boolean isNotNullManageServices() {
    return genClient.cacheValueIsNotNull(CacheKey.manageServices);
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

  /** Checks whether the 'enableDeliveryMinimum' field is set and is not null */
  public boolean isNotNullEnableDeliveryMinimum() {
    return genClient.cacheValueIsNotNull(CacheKey.enableDeliveryMinimum);
  }

  /** Checks whether the 'providerType' field is set and is not null */
  public boolean isNotNullProviderType() {
    return genClient.cacheValueIsNotNull(CacheKey.providerType);
  }



  /** Checks whether the 'id' field has been set, however the value could be null */
  public boolean hasId() {
    return genClient.cacheHasKey(CacheKey.id);
  }

  /** Checks whether the 'name' field has been set, however the value could be null */
  public boolean hasName() {
    return genClient.cacheHasKey(CacheKey.name);
  }

  /** Checks whether the 'logoUrl' field has been set, however the value could be null */
  public boolean hasLogoUrl() {
    return genClient.cacheHasKey(CacheKey.logoUrl);
  }

  /** Checks whether the 'developerAppId' field has been set, however the value could be null */
  public boolean hasDeveloperAppId() {
    return genClient.cacheHasKey(CacheKey.developerAppId);
  }

  /** Checks whether the 'developerAppPackageName' field has been set, however the value could be null */
  public boolean hasDeveloperAppPackageName() {
    return genClient.cacheHasKey(CacheKey.developerAppPackageName);
  }

  /** Checks whether the 'token' field has been set, however the value could be null */
  public boolean hasToken() {
    return genClient.cacheHasKey(CacheKey.token);
  }

  /** Checks whether the 'authtokenUrl' field has been set, however the value could be null */
  public boolean hasAuthtokenUrl() {
    return genClient.cacheHasKey(CacheKey.authtokenUrl);
  }

  /** Checks whether the 'backendInstall' field has been set, however the value could be null */
  public boolean hasBackendInstall() {
    return genClient.cacheHasKey(CacheKey.backendInstall);
  }

  /** Checks whether the 'autoOnline' field has been set, however the value could be null */
  public boolean hasAutoOnline() {
    return genClient.cacheHasKey(CacheKey.autoOnline);
  }

  /** Checks whether the 'trackCustomer' field has been set, however the value could be null */
  public boolean hasTrackCustomer() {
    return genClient.cacheHasKey(CacheKey.trackCustomer);
  }

  /** Checks whether the 'manageServices' field has been set, however the value could be null */
  public boolean hasManageServices() {
    return genClient.cacheHasKey(CacheKey.manageServices);
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

  /** Checks whether the 'enableDeliveryMinimum' field has been set, however the value could be null */
  public boolean hasEnableDeliveryMinimum() {
    return genClient.cacheHasKey(CacheKey.enableDeliveryMinimum);
  }

  /** Checks whether the 'providerType' field has been set, however the value could be null */
  public boolean hasProviderType() {
    return genClient.cacheHasKey(CacheKey.providerType);
  }


  /**
   * Sets the field 'id'.
   */
  public OnlineOrderProvider setId(java.lang.String id) {
    return genClient.setOther(id, CacheKey.id);
  }

  /**
   * Sets the field 'name'.
   */
  public OnlineOrderProvider setName(java.lang.String name) {
    return genClient.setOther(name, CacheKey.name);
  }

  /**
   * Sets the field 'logoUrl'.
   */
  public OnlineOrderProvider setLogoUrl(java.lang.String logoUrl) {
    return genClient.setOther(logoUrl, CacheKey.logoUrl);
  }

  /**
   * Sets the field 'developerAppId'.
   */
  public OnlineOrderProvider setDeveloperAppId(java.lang.String developerAppId) {
    return genClient.setOther(developerAppId, CacheKey.developerAppId);
  }

  /**
   * Sets the field 'developerAppPackageName'.
   */
  public OnlineOrderProvider setDeveloperAppPackageName(java.lang.String developerAppPackageName) {
    return genClient.setOther(developerAppPackageName, CacheKey.developerAppPackageName);
  }

  /**
   * Sets the field 'token'.
   */
  public OnlineOrderProvider setToken(java.lang.String token) {
    return genClient.setOther(token, CacheKey.token);
  }

  /**
   * Sets the field 'authtokenUrl'.
   */
  public OnlineOrderProvider setAuthtokenUrl(java.lang.String authtokenUrl) {
    return genClient.setOther(authtokenUrl, CacheKey.authtokenUrl);
  }

  /**
   * Sets the field 'backendInstall'.
   */
  public OnlineOrderProvider setBackendInstall(java.lang.Boolean backendInstall) {
    return genClient.setOther(backendInstall, CacheKey.backendInstall);
  }

  /**
   * Sets the field 'autoOnline'.
   */
  public OnlineOrderProvider setAutoOnline(java.lang.Boolean autoOnline) {
    return genClient.setOther(autoOnline, CacheKey.autoOnline);
  }

  /**
   * Sets the field 'trackCustomer'.
   */
  public OnlineOrderProvider setTrackCustomer(java.lang.Boolean trackCustomer) {
    return genClient.setOther(trackCustomer, CacheKey.trackCustomer);
  }

  /**
   * Sets the field 'manageServices'.
   */
  public OnlineOrderProvider setManageServices(java.lang.Boolean manageServices) {
    return genClient.setOther(manageServices, CacheKey.manageServices);
  }

  /**
   * Sets the field 'createdTime'.
   */
  public OnlineOrderProvider setCreatedTime(java.lang.Long createdTime) {
    return genClient.setOther(createdTime, CacheKey.createdTime);
  }

  /**
   * Sets the field 'modifiedTime'.
   */
  public OnlineOrderProvider setModifiedTime(java.lang.Long modifiedTime) {
    return genClient.setOther(modifiedTime, CacheKey.modifiedTime);
  }

  /**
   * Sets the field 'deletedTime'.
   */
  public OnlineOrderProvider setDeletedTime(java.lang.Long deletedTime) {
    return genClient.setOther(deletedTime, CacheKey.deletedTime);
  }

  /**
   * Sets the field 'enableDeliveryMinimum'.
   */
  public OnlineOrderProvider setEnableDeliveryMinimum(java.lang.Boolean enableDeliveryMinimum) {
    return genClient.setOther(enableDeliveryMinimum, CacheKey.enableDeliveryMinimum);
  }

  /**
   * Sets the field 'providerType'.
   */
  public OnlineOrderProvider setProviderType(com.clover.sdk.v3.onlineorder.ProviderType providerType) {
    return genClient.setOther(providerType, CacheKey.providerType);
  }


  /** Clears the 'id' field, the 'has' method for this field will now return false */
  public void clearId() {
    genClient.clear(CacheKey.id);
  }
  /** Clears the 'name' field, the 'has' method for this field will now return false */
  public void clearName() {
    genClient.clear(CacheKey.name);
  }
  /** Clears the 'logoUrl' field, the 'has' method for this field will now return false */
  public void clearLogoUrl() {
    genClient.clear(CacheKey.logoUrl);
  }
  /** Clears the 'developerAppId' field, the 'has' method for this field will now return false */
  public void clearDeveloperAppId() {
    genClient.clear(CacheKey.developerAppId);
  }
  /** Clears the 'developerAppPackageName' field, the 'has' method for this field will now return false */
  public void clearDeveloperAppPackageName() {
    genClient.clear(CacheKey.developerAppPackageName);
  }
  /** Clears the 'token' field, the 'has' method for this field will now return false */
  public void clearToken() {
    genClient.clear(CacheKey.token);
  }
  /** Clears the 'authtokenUrl' field, the 'has' method for this field will now return false */
  public void clearAuthtokenUrl() {
    genClient.clear(CacheKey.authtokenUrl);
  }
  /** Clears the 'backendInstall' field, the 'has' method for this field will now return false */
  public void clearBackendInstall() {
    genClient.clear(CacheKey.backendInstall);
  }
  /** Clears the 'autoOnline' field, the 'has' method for this field will now return false */
  public void clearAutoOnline() {
    genClient.clear(CacheKey.autoOnline);
  }
  /** Clears the 'trackCustomer' field, the 'has' method for this field will now return false */
  public void clearTrackCustomer() {
    genClient.clear(CacheKey.trackCustomer);
  }
  /** Clears the 'manageServices' field, the 'has' method for this field will now return false */
  public void clearManageServices() {
    genClient.clear(CacheKey.manageServices);
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
  /** Clears the 'enableDeliveryMinimum' field, the 'has' method for this field will now return false */
  public void clearEnableDeliveryMinimum() {
    genClient.clear(CacheKey.enableDeliveryMinimum);
  }
  /** Clears the 'providerType' field, the 'has' method for this field will now return false */
  public void clearProviderType() {
    genClient.clear(CacheKey.providerType);
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
  public OnlineOrderProvider copyChanges() {
    OnlineOrderProvider copy = new OnlineOrderProvider();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(OnlineOrderProvider src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new OnlineOrderProvider(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<OnlineOrderProvider> CREATOR = new android.os.Parcelable.Creator<OnlineOrderProvider>() {
    @Override
    public OnlineOrderProvider createFromParcel(android.os.Parcel in) {
      OnlineOrderProvider instance = new OnlineOrderProvider(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public OnlineOrderProvider[] newArray(int size) {
      return new OnlineOrderProvider[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<OnlineOrderProvider> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<OnlineOrderProvider>() {
    public Class<OnlineOrderProvider> getCreatedClass() {
      return OnlineOrderProvider.class;
    }

    @Override
    public OnlineOrderProvider create(org.json.JSONObject jsonObject) {
      return new OnlineOrderProvider(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean ID_IS_REQUIRED = false;
    public static final long ID_MAX_LEN = 13;
    public static final boolean NAME_IS_REQUIRED = false;
    public static final long NAME_MAX_LEN = 31;
    public static final boolean LOGOURL_IS_REQUIRED = false;
    public static final long LOGOURL_MAX_LEN = 255;
    public static final boolean DEVELOPERAPPID_IS_REQUIRED = true;
    public static final long DEVELOPERAPPID_MAX_LEN = 13;
    public static final boolean DEVELOPERAPPPACKAGENAME_IS_REQUIRED = false;
    public static final long DEVELOPERAPPPACKAGENAME_MAX_LEN = 255;
    public static final boolean TOKEN_IS_REQUIRED = false;
    public static final long TOKEN_MAX_LEN = 255;
    public static final boolean AUTHTOKENURL_IS_REQUIRED = false;
    public static final long AUTHTOKENURL_MAX_LEN = 255;
    public static final boolean BACKENDINSTALL_IS_REQUIRED = false;
    public static final boolean AUTOONLINE_IS_REQUIRED = false;
    public static final boolean TRACKCUSTOMER_IS_REQUIRED = false;
    public static final boolean MANAGESERVICES_IS_REQUIRED = false;
    public static final boolean CREATEDTIME_IS_REQUIRED = false;
    public static final boolean MODIFIEDTIME_IS_REQUIRED = false;
    public static final boolean DELETEDTIME_IS_REQUIRED = false;
    public static final boolean ENABLEDELIVERYMINIMUM_IS_REQUIRED = false;
    public static final boolean PROVIDERTYPE_IS_REQUIRED = false;
  }

}
