/**
 * Copyright (C) 2015 Clover Network, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clover.sdk;

public final class GenericClient<D> {

  private org.json.JSONObject jsonObject = null;
  private android.os.Bundle bundle = null;
  private android.os.Bundle changeLog = null;
  private volatile Object[] cache = null;
  private byte[] cacheState = null;
  private static String errorLengthMessage = "Maximum string length exceeded for ";
  private static String errorNullMessage = " is required to be non-null";
  private D callingClass = null;
  private final Object LOCK = new Object();

  public static final byte STATE_NOT_CACHED = 0;
  public static final byte STATE_CACHED_NO_VALUE = 1;
  public static final byte STATE_CACHED_VALUE = 2;

  /**
   * Constructs a new GenericClient with the instance of the declaring class saved.
   */
  public GenericClient(D callingObject) {
    callingClass = callingObject;
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public GenericClient(GenericClient src) {
    if (src.jsonObject != null) {
      this.jsonObject = com.clover.sdk.v3.JsonHelper.deepCopy(src.getJSONObject());
    }
  }

  /**
   * Replacement for the various cache methods.
   */
  public <T> T cacheGet(ValueExtractorEnum<D> cacheKey) {
    int index = cacheKey.ordinal();
    populateCache(index, cacheKey);
    return (T) cache[index];
  }

  public boolean cacheValueIsNotNull(ValueExtractorEnum<D> cacheKey) {
    int index = cacheKey.ordinal();
    populateCache(index, cacheKey);
    return cache[index] != null;
  }

  public boolean cacheHasKey(ValueExtractorEnum<D> cacheKey) {
    int index = cacheKey.ordinal();
    populateCache(index, cacheKey);
    return cacheState[index] == STATE_CACHED_VALUE;
  }

  public void cacheRemoveValue(ValueExtractorEnum<D> cacheKey) {
    int index = cacheKey.ordinal();
    populateCache(index, cacheKey);
    cache[index] = null;
    cacheState[index] = STATE_CACHED_NO_VALUE;
  }

  public void cacheMarkDirty(ValueExtractorEnum<D> cacheKey) {
    if (cache != null) {
      int index = cacheKey.ordinal();
      cache[index] = null;
      cacheState[index] = STATE_NOT_CACHED;
    }
  }

  private void populateCache(int index, ValueExtractorEnum<D> cacheKey) {
    if (cache == null) {
      synchronized(LOCK) {
        if (cache == null) {
          int size = cacheKey.getDeclaringClass().getEnumConstants().length;
          cache = new Object[size];
          cacheState = new byte[size];
        }
      }
    }

    if (cacheState[index] == STATE_NOT_CACHED) {
      if (getJSONObject().has(cacheKey.name())) {
        cache[index] = cacheKey.extractValue(callingClass);
        cacheState[index] = STATE_CACHED_VALUE;
      }
      else {
        cacheState[index] = STATE_CACHED_NO_VALUE;
      }
    }
  }

  public <T> void validateNull(T field, String name) {
    if (field == null) throw new IllegalArgumentException("" + name + errorNullMessage);
  }

  public void validateLength(String field, int maxLength) {
    if (field != null && field.length() > maxLength) throw new IllegalArgumentException(errorLengthMessage + field);
  }

  public String throwNull(String name) {
    return "" + name + errorNullMessage;
  }

  public String throwStringLengthExceeded(String name) {
    return errorLengthMessage + name;
  }

  public org.json.JSONObject getJsonObject() {
    return jsonObject;
  }

  public void setJsonObject(org.json.JSONObject newJSONObject) {
    jsonObject = newJSONObject;
  }

  /**
   * Returns the internal JSONObject backing this instance, the return value is not a copy so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public org.json.JSONObject getJSONObject() {
    if (jsonObject == null) {
      jsonObject = new org.json.JSONObject();
    }
    return jsonObject;
  }

  /**
   * Update log when necessary.
   */
  public void logChange(String field) {
    if (changeLog == null) {
      changeLog = new android.os.Bundle();
    }
    changeLog.putString(field, null);
  }

  /**
   * Remove from log when necessary.
   */
  public void unlogChange(String field) {
    if (changeLog != null) {
      changeLog.remove(field);
    }
  }

  /**
   * Returns true if this instance has any changes.
   */
  public boolean containsChanges() {
    return changeLog != null;
  }

  /**
   * Reset the log of changes made to this instance, calling copyChanges() after this would return an empty instance.
   */
  public void resetChangeLog() {
    changeLog = null;
  }

  /**
   * Change log to a new log sent in.
   */
  public void setChangeLog(android.os.Bundle newChangeLog) {
    changeLog = newChangeLog;
  }

  /**
   * Clears the 'field' field, the 'has' method for this field will now return false
   */
  public void clear(ValueExtractorEnum<D> key) {
    unlogChange(key.name());
    getJSONObject().remove(key.name());
    cacheRemoveValue(key);
  }

  /**
   * Gets a Bundle which can be used to get and set data attached to this instance. The attached Bundle will be
   * parcelled but not jsonified.
   */
  public android.os.Bundle getBundle() {
    if (bundle == null) {
      bundle = new android.os.Bundle();
    }
    return bundle;
  }

  /**
   * Change bundle to a new bundle sent in.
   */
  public void setBundle(android.os.Bundle newBundle) {
    bundle = newBundle;
  }

  /**
   * Get access to changeLog
   */
  public android.os.Bundle getChangeLog() {
    return changeLog;
  }

  public String toString() {
    String json = getJSONObject().toString();

    if (bundle != null) {
      bundle.isEmpty(); // Triggers unparcel
    }

    if (changeLog != null) {
      changeLog.isEmpty(); // Triggers unparcel
    }

    return callingClass.getClass().getSimpleName() + "{" +
            "json='" + json + "'" +
            ", bundle=" + bundle +
            ", changeLog=" + changeLog +
            '}';
  }

  /**
   * Generic method that returns a list of T items. Replacement for the "extract" methods which dealt with a list of Records.
   */
  public <T> java.util.List<T> extractListRecord(String name, com.clover.sdk.JSONifiable.Creator<T> JSON_CREATOR) {
    if (getJSONObject().isNull(name)) { return null; }

    org.json.JSONObject elementsContainer = getJSONObject().optJSONObject(name);
    org.json.JSONArray itemArray = elementsContainer.optJSONArray("elements");
    java.util.List<T> itemList = new java.util.ArrayList<T>(itemArray.length());
    for (int i = 0; i < itemArray.length(); i++) {
      org.json.JSONObject obj = itemArray.optJSONObject(i);
      if (obj == null) {
        continue;
      }
      T item = JSON_CREATOR.create(obj);
      itemList.add(item);
    }

    return java.util.Collections.unmodifiableList(itemList);
  }

  /**
   * Generic method that returns a list of T items. Replacement for the "extract" methods which dealt with a list of Enums.
   */
  public <T extends Enum<T>> java.util.List<T> extractListEnum(String name, Class<T> clazz) {
    if (getJSONObject().isNull(name)) { return null; }

    org.json.JSONObject elementsContainer = getJSONObject().optJSONObject(name);
    org.json.JSONArray itemArray = elementsContainer.optJSONArray("elements");
    java.util.List<T> itemList = new java.util.ArrayList<T>(itemArray.length());
    for (int i = 0; i < itemArray.length(); i++) {
      String enumString = itemArray.optString(i, null);
      if (enumString == null) { continue; }
      T item;
      try {
        item = Enum.valueOf(clazz, enumString);
      } catch (Exception e) {
        e.printStackTrace();
        continue;
      }
      itemList.add(item);
    }

    return java.util.Collections.unmodifiableList(itemList);
  }

  /**
   * Generic method that returns a list of T items. Replacement for the "extract" methods which dealt with a list of other items (neither Records nor Enums).
   */
  public <T> java.util.List<T> extractListOther(String name, Class<T> clazz) {
    if (getJSONObject().isNull(name)) { return null; }

    org.json.JSONObject elementsContainer = getJSONObject().optJSONObject(name);
    org.json.JSONArray itemArray = elementsContainer.optJSONArray("elements");
    java.util.List<T> itemList = new java.util.ArrayList<T>(itemArray.length());
    for (int i = 0; i < itemArray.length(); i++) {
      T item = (T) returnType(clazz, itemArray, i);
      if (item == null) { continue; }
      itemList.add(item);
    }
    return java.util.Collections.unmodifiableList(itemList);
  }

  /**
   * Generic method that replaces the "extract" methods which dealt with a Record.
   */
  public <T> T extractRecord(String name, com.clover.sdk.JSONifiable.Creator<T> JSON_CREATOR) {
    org.json.JSONObject jsonObj = getJSONObject().optJSONObject(name);
    return jsonObj != null ? JSON_CREATOR.create(jsonObj) : null;
  }

  /**
   * Generic method that replaces the "extract" methods which dealt with an Enum.
   */
  public <T extends Enum<T>> T extractEnum(String name, Class<T> clazz) {
    if (!getJSONObject().isNull(name)) {
      try {
        return Enum.valueOf(clazz, getJSONObject().optString(name));
      } catch(Exception e) {
        e.printStackTrace();
      }
    }

    return null;
  }

  /**
   * Generic method that replaces the "extract" methods which dealt with a Map.
   */
  public java.util.Map extractMap(String name) {
    if (getJSONObject().isNull(name)) return null;
    org.json.JSONObject object = getJSONObject().optJSONObject(name);
    return com.clover.sdk.v3.JsonHelper.toMap(object);
  }

  /**
   * Generic method that replaces the "extract" methods which dealt with anything else (not a Record, Enum, or Map).
   */
  public <T> T extractOther(String name, Class<T> clazz) {
    return getJSONObject().isNull(name) ? null : (T) returnType(name, clazz);
  }

  /**
   * Helper method that determines which "opt" method to use, based on the type of the item. Retrieves the information from
   * a given JSONArray
   */
  private <T> Object returnType(Class<T> item, org.json.JSONArray itemArray, int i) {
    if (item.equals(java.lang.String.class)) {
      return itemArray.optString(i);
    }
    else if (item.equals(java.lang.Boolean.class)) {
      return itemArray.optBoolean(i);
    }
    else if (item.equals(java.lang.Integer.class)) {
      return itemArray.optInt(i);
    }
    else if (item.equals(java.lang.Long.class)) {
      return itemArray.optLong(i);
    }
    else if (item.equals(java.lang.Double.class)) {
      return itemArray.optDouble(i);
    }
    else if (item.equals(org.json.JSONArray.class)) {
      return itemArray.optJSONArray(i);
    }
    else {
      return itemArray.optJSONObject(i);
    }
  }

  /**
   * Helper method that determines which "opt" method to use, based on the type of the item. Retrieves the information from
   * the genClient's JSONObject.
   */
  private <T> Object returnType(String name, Class<T> item) {
    if (item.equals(java.lang.String.class)) {
      return getJSONObject().optString(name);
    }
    else if (item.equals(java.lang.Boolean.class)) {
      return getJSONObject().optBoolean(name);
    }
    else if (item.equals(java.lang.Long.class)) {
      return getJSONObject().optLong(name);
    }
    else if (item.equals(java.lang.Integer.class)) {
      return getJSONObject().optInt(name);
    }
    else if (item.equals(java.lang.Double.class)) {
      return getJSONObject().optDouble(name);
    }
    else if (item.equals(org.json.JSONArray.class)) {
      return getJSONObject().optJSONArray(name);
    }
    else {
      return getJSONObject().optJSONObject(name);
    }
  }

  /**
   * Generic method that replaces the "set" methods which dealt with Arrays that hold Records.
   */
  public <T extends com.clover.sdk.JSONifiable> D setArrayRecord(java.util.List<T> list, ValueExtractorEnum<D> key) {
    logChange(key.name());

    try {
      if (list == null) {
        getJSONObject().put(key.name(), org.json.JSONObject.NULL);
      }
      else {
        org.json.JSONArray array = new org.json.JSONArray();
        for (T obj : list) {
          if (obj == null) { continue; }
          array.put(obj.getJSONObject());
        }

        org.json.JSONObject elementsContainer = new org.json.JSONObject();
        elementsContainer.put("elements", array);
        getJSONObject().put(key.name(), elementsContainer);
      }
    } catch (org.json.JSONException e) {
      throw new IllegalArgumentException(e);
    }
    cacheMarkDirty(key);
    return callingClass;
  }

  /**
   * Generic method that replaces the "set" methods which dealt with Arrays that hold non-Records.
   */
  public <T> D setArrayOther(java.util.List<T> list, ValueExtractorEnum<D> key) {
    logChange(key.name());

    try {
      if (list == null) {
        getJSONObject().put(key.name(), org.json.JSONObject.NULL);
      }
      else {
        org.json.JSONArray array = new org.json.JSONArray();
        for (T obj : list) {
          if (obj == null) { continue; }
          array.put(obj);
        }

        org.json.JSONObject elementsContainer = new org.json.JSONObject();
        elementsContainer.put("elements", array);
        getJSONObject().put(key.name(), elementsContainer);
      }
    } catch (org.json.JSONException e) {
      throw new IllegalArgumentException(e);
    }
    cacheMarkDirty(key);
    return callingClass;
  }

  /**
   * Generic method that replaces the "set" methods which dealt with Records.
   */
  public <T extends com.clover.sdk.JSONifiable> D setRecord(T item, ValueExtractorEnum<D> key) {
    logChange(key.name());

    try {
      getJSONObject().put(key.name(), item == null ? org.json.JSONObject.NULL : item.getJSONObject());
    } catch (org.json.JSONException e) {
      throw new IllegalArgumentException(e);
    }
    cacheMarkDirty(key);
    return callingClass;
  }

  /**
   * Generic method that replaces the "set" methods which dealt with anything else (not an Array or Record).
   */
  public <T> D setOther(T item, ValueExtractorEnum<D> key) {
    logChange(key.name());

    try {
      getJSONObject().put(key.name(), item == null ? org.json.JSONObject.NULL : com.clover.sdk.v3.JsonHelper.toJSON(item));
    } catch (org.json.JSONException e) {
      throw new IllegalArgumentException(e);
    }
    cacheMarkDirty(key);
    return callingClass;
  }

  /**
   * Generic method that replaces the "mergeChanges" method in all the classes.
   */
  public void mergeChanges(org.json.JSONObject srcObj, GenericClient srcGC) {
    try {
      // Make a copy of the source so the destination fields are copies
      org.json.JSONObject dstObj = getJSONObject();
      for (String field : srcGC.getChangeLog().keySet()) {
        dstObj.put(field, srcObj.get(field));
        logChange(field);
      }
    } catch (org.json.JSONException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Method that replaces the "writeToParcel" methods. "flags" seems to be unused, but was also passed in the original classes.
   */
  public void writeToParcel(android.os.Parcel dest, int flags) {
    com.clover.sdk.v3.JsonParcelHelper.wrap(getJSONObject()).writeToParcel(dest, 0);
    dest.writeBundle(bundle);
    dest.writeBundle(changeLog);
  }

}
