package com.clover.sdk;

/**
 * Test object to ensure that ValueExtractorEnum still works.
 */
@SuppressWarnings("all")
public class ValueExtractorStyleTestObject extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  public java.util.List<com.clover.sdk.v3.base.Reference> getAndroidVersions() {
    return genClient.cacheGet(CacheKey.androidVersions);
  }

  public java.util.List<com.clover.sdk.v3.base.Reference> getRoms() {
    return genClient.cacheGet(CacheKey.roms);
  }




  private enum CacheKey implements com.clover.sdk.ValueExtractorEnum<ValueExtractorStyleTestObject> {
    androidVersions {
      @Override
      public Object extractValue(ValueExtractorStyleTestObject instance) {
        return instance.genClient.extractListRecord("androidVersions", com.clover.sdk.v3.base.Reference.JSON_CREATOR);
      }
    },
    roms {
      @Override
      public Object extractValue(ValueExtractorStyleTestObject instance) {
        return instance.genClient.extractListRecord("roms", com.clover.sdk.v3.base.Reference.JSON_CREATOR);
      }
    },
    ;
  }

  private GenericClient<ValueExtractorStyleTestObject> genClient;

  /**
   * Constructs a new empty instance.
   */
  public ValueExtractorStyleTestObject() {
    genClient = new GenericClient<ValueExtractorStyleTestObject>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected ValueExtractorStyleTestObject(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public ValueExtractorStyleTestObject(String json) throws IllegalArgumentException {
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
  public ValueExtractorStyleTestObject(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public ValueExtractorStyleTestObject(ValueExtractorStyleTestObject src) {
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

  /** Checks whether the 'androidVersions' field is set and is not null */
  public boolean isNotNullAndroidVersions() {
    return genClient.cacheValueIsNotNull(CacheKey.androidVersions);
  }

  /** Checks whether the 'androidVersions' field is set and is not null and is not empty */
  public boolean isNotEmptyAndroidVersions() { return isNotNullAndroidVersions() && !getAndroidVersions().isEmpty(); }

  /** Checks whether the 'roms' field is set and is not null */
  public boolean isNotNullRoms() {
    return genClient.cacheValueIsNotNull(CacheKey.roms);
  }

  /** Checks whether the 'roms' field is set and is not null and is not empty */
  public boolean isNotEmptyRoms() { return isNotNullRoms() && !getRoms().isEmpty(); }



  /** Checks whether the 'androidVersions' field has been set, however the value could be null */
  public boolean hasAndroidVersions() {
    return genClient.cacheHasKey(CacheKey.androidVersions);
  }

  /** Checks whether the 'roms' field has been set, however the value could be null */
  public boolean hasRoms() {
    return genClient.cacheHasKey(CacheKey.roms);
  }


  /**
   * Sets the field 'androidVersions'.
   *
   * Nulls in the given List are skipped. List parameter is copied, so it will not reflect any changes, but objects inside it will.
   */
  public ValueExtractorStyleTestObject setAndroidVersions(java.util.List<com.clover.sdk.v3.base.Reference> androidVersions) {
    return genClient.setArrayRecord(androidVersions, CacheKey.androidVersions);
  }

  /**
   * Sets the field 'roms'.
   *
   * Nulls in the given List are skipped. List parameter is copied, so it will not reflect any changes, but objects inside it will.
   */
  public ValueExtractorStyleTestObject setRoms(java.util.List<com.clover.sdk.v3.base.Reference> roms) {
    return genClient.setArrayRecord(roms, CacheKey.roms);
  }


  /** Clears the 'androidVersions' field, the 'has' method for this field will now return false */
  public void clearAndroidVersions() {
    genClient.clear(CacheKey.androidVersions);
  }
  /** Clears the 'roms' field, the 'has' method for this field will now return false */
  public void clearRoms() {
    genClient.clear(CacheKey.roms);
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
  public ValueExtractorStyleTestObject copyChanges() {
    ValueExtractorStyleTestObject copy = new ValueExtractorStyleTestObject();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(ValueExtractorStyleTestObject src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new ValueExtractorStyleTestObject(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<ValueExtractorStyleTestObject> CREATOR = new android.os.Parcelable.Creator<ValueExtractorStyleTestObject>() {
    @Override
    public ValueExtractorStyleTestObject createFromParcel(android.os.Parcel in) {
      ValueExtractorStyleTestObject instance = new ValueExtractorStyleTestObject(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public ValueExtractorStyleTestObject[] newArray(int size) {
      return new ValueExtractorStyleTestObject[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<ValueExtractorStyleTestObject> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<ValueExtractorStyleTestObject>() {
    @Override
    public ValueExtractorStyleTestObject create(org.json.JSONObject jsonObject) {
      return new ValueExtractorStyleTestObject(jsonObject);
    }
  };

  public interface Constraints {

    public static final boolean ANDROIDVERSIONS_IS_REQUIRED = false;
    public static final boolean ROMS_IS_REQUIRED = false;

  }

}
