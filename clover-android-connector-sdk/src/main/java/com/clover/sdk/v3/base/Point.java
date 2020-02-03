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

package com.clover.sdk.v3.base;

import com.clover.sdk.GenericClient;
import com.clover.sdk.GenericParcelable;

/**
 * This is an auto-generated Clover data object.
 * <p>
 * <h3>Fields</h3>
 * <ul>
 * <li>{@link #getX x}</li>
 * <li>{@link #getY y}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class Point extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  public java.lang.Long getX() {
    return genClient.cacheGet(CacheKey.x);
  }

  public java.lang.Long getY() {
    return genClient.cacheGet(CacheKey.y);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    x
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Long.class)),
    y
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

  private final GenericClient<Point> genClient;

  /**
   * Constructs a new empty instance.
   */
  public Point() {
    genClient = new GenericClient<Point>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected Point(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public Point(String json) throws IllegalArgumentException {
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
  public Point(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public Point(Point src) {
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

  /** Checks whether the 'x' field is set and is not null */
  public boolean isNotNullX() {
    return genClient.cacheValueIsNotNull(CacheKey.x);
  }

  /** Checks whether the 'y' field is set and is not null */
  public boolean isNotNullY() {
    return genClient.cacheValueIsNotNull(CacheKey.y);
  }



  /** Checks whether the 'x' field has been set, however the value could be null */
  public boolean hasX() {
    return genClient.cacheHasKey(CacheKey.x);
  }

  /** Checks whether the 'y' field has been set, however the value could be null */
  public boolean hasY() {
    return genClient.cacheHasKey(CacheKey.y);
  }


  /**
   * Sets the field 'x'.
   */
  public Point setX(java.lang.Long x) {
    return genClient.setOther(x, CacheKey.x);
  }

  /**
   * Sets the field 'y'.
   */
  public Point setY(java.lang.Long y) {
    return genClient.setOther(y, CacheKey.y);
  }


  /** Clears the 'x' field, the 'has' method for this field will now return false */
  public void clearX() {
    genClient.clear(CacheKey.x);
  }
  /** Clears the 'y' field, the 'has' method for this field will now return false */
  public void clearY() {
    genClient.clear(CacheKey.y);
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
  public Point copyChanges() {
    Point copy = new Point();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(Point src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new Point(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<Point> CREATOR = new android.os.Parcelable.Creator<Point>() {
    @Override
    public Point createFromParcel(android.os.Parcel in) {
      Point instance = new Point(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public Point[] newArray(int size) {
      return new Point[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<Point> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<Point>() {
    @Override
    public Point create(org.json.JSONObject jsonObject) {
      return new Point(jsonObject);
    }
  };

  public interface Constraints {

    public static final boolean X_IS_REQUIRED = false;
    public static final boolean Y_IS_REQUIRED = false;

  }

}
