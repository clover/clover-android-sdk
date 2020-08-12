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
package com.clover.sdk;

import com.clover.sdk.extractors.ExtractionStrategy;
import com.clover.sdk.extractors.RecordExtractionStrategy;
import com.clover.sdk.extractors.RecordListExtractionStrategy;
import com.clover.sdk.v3.Validator;

import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * For Clover internal use only.
 * <p>
 * There are two copies of this file, one in clover-android-sdk and one in
 * schema-tool, please keep them in sync.
 */
public final class GenericClient<D> {

  private static final String TAG = "GenericClient";

  private JSONObject jsonObject = null;
  private Bundle bundle = null;
  private Bundle changeLog = null;
  private volatile Object[] cache = null;
  private byte[] cacheState = null;
  private D callingClass = null;
  private final Object LOCK = new Object();

  private static StrictValidationFailedCallback strictValidationFailedCallback;

  private static final byte STATE_NOT_CACHED = 0;
  private static final byte STATE_CACHED_NO_VALUE = 1;
  private static final byte STATE_CACHED_VALUE = 2;

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
  @SuppressWarnings("unchecked")
  public <T> T cacheGet(ExtractableEnum cacheKey) {
    int index = cacheKey.ordinal();
    populateCache(index, cacheKey);
    return (T) cache[index];
  }

  public boolean cacheValueIsNotNull(ExtractableEnum cacheKey) {
    int index = cacheKey.ordinal();
    populateCache(index, cacheKey);
    return cache[index] != null;
  }

  public boolean cacheHasKey(ExtractableEnum cacheKey) {
    int index = cacheKey.ordinal();
    populateCache(index, cacheKey);
    return cacheState[index] == STATE_CACHED_VALUE;
  }

  public void cacheRemoveValue(ExtractableEnum cacheKey) {
    int index = cacheKey.ordinal();
    populateCache(index, cacheKey);
    cache[index] = null;
    cacheState[index] = STATE_CACHED_NO_VALUE;
  }

  public void cacheMarkDirty(ExtractableEnum cacheKey) {
    if (cache != null) {
      int index = cacheKey.ordinal();
      cache[index] = null;
      cacheState[index] = STATE_NOT_CACHED;
    }
  }

  @SuppressWarnings("unchecked")
  private void populateCache(int index, ExtractableEnum cacheKey) {
    if (cache == null || cacheState == null) {
      synchronized (LOCK) {
        if (cache == null || cacheState == null) {
          int size = cacheKey.getDeclaringClass().getEnumConstants().length;
          cache = new Object[size];
          cacheState = new byte[size];
        }
      }
    }

    if (cacheState[index] == STATE_NOT_CACHED) {
      if (getJSONObject().has(cacheKey.name())) {
        if (cacheKey instanceof ValueExtractorEnum) {
          cache[index] = ((ValueExtractorEnum<D>) cacheKey).extractValue(callingClass);
        } else {
          cache[index] = ((ExtractionStrategyEnum) cacheKey).getExtractionStrategy().extractValue(this, cacheKey.name());
        }
        cacheState[index] = STATE_CACHED_VALUE;
      }
      else {
        cacheState[index] = STATE_CACHED_NO_VALUE;
      }
    }
  }

  private static String throwExceptionMaxLen(String name, long len, long max) {
    throw new IllegalArgumentException(String.format(Locale.ROOT, "'%s' with length '%s' exceeds maximum length '%s'", name, len, max));
  }

  private static String throwExceptionMaxVal(String name, Object value, Object max) {
    throw new IllegalArgumentException(String.format(Locale.ROOT, "'%s' with value '%s' exceeds maximum value '%s'", name, value, max));
  }

  private static String throwExceptionMinVal(String name, Object value, Object min) {
    throw new IllegalArgumentException(String.format(Locale.ROOT, "'%s' with value '%s' is below minimum value '%s'", name, value, min));
  }

  private static String throwExceptionNull(String name) {
    throw new IllegalArgumentException(String.format(Locale.ROOT, "'%s' is required to be non-null", name));
  }

  /**
   * Deprecated but kept since we do not always regenerate all objects. This
   * method is a misnomer, it should have been validateNotNull since it
   * validates that the field is not null and fails if it is.
   *
   * @deprecated Use {@link #validateNotNull(ExtractableEnum, Object)} instead
   */
  @Deprecated
  public <T> void validateNull(T field, String name) {
    if (field == null) {
      throwExceptionNull(name);
    }
  }

  public <T> void validateNotNull(ExtractableEnum cacheKey, T value) {
    if (value == null) {
      throwExceptionNull(cacheKey.name());
    }
  }

  /**
   * Deprecated but kept since we do not always regenerate all objects.
   *
   * @deprecated Use {@link #validateLength(ExtractableEnum, String, long)} instead.
   */
  @Deprecated
  public void validateLength(String value, long maxLength) {
    if (value != null && value.length() > maxLength) {
      throwExceptionMaxLen(null, value.length(), maxLength);
    }
  }

  public void validateLength(ExtractableEnum cacheKey, String value, long maxLength) {
    if (value != null && value.length() > maxLength) {
      throwExceptionMaxLen(cacheKey.name(), value.length(), maxLength);
    }
  }

  public void validateMin(ExtractableEnum cacheKey, Integer value, long min) {
    if (value != null && value < min) {
      throwExceptionMinVal(cacheKey.name(), value, min);
    }
  }

  public void validateMax(ExtractableEnum cacheKey, Integer value, long max) {
    if (value != null && value > max) {
      throwExceptionMaxVal(cacheKey.name(), value, max);
    }
  }

  public void validateMinMax(ExtractableEnum cacheKey, Integer value, long min, long max) {
    validateMin(cacheKey, value, min);
    validateMax(cacheKey, value, max);
  }

  public void validateMin(ExtractableEnum cacheKey, Long value, long min) {
    if (value != null && value < min) {
      throwExceptionMinVal(cacheKey.name(), value, min);
    }
  }

  public void validateMax(ExtractableEnum cacheKey, Long value, long max) {
    if (value != null && value > max) {
      throwExceptionMaxVal(cacheKey.name(), value, max);
    }
  }

  public void validateMinMax(ExtractableEnum cacheKey, Long value, long min, long max) {
    validateMin(cacheKey, value, min);
    validateMax(cacheKey, value, max);
  }

  public void validateMin(ExtractableEnum cacheKey, Float value, double min) {
    if (value != null && value < min) {
      throwExceptionMinVal(cacheKey.name(), value, min);
    }
  }

  public void validateMax(ExtractableEnum cacheKey, Float value, double max) {
    if (value != null && value > max) {
      throwExceptionMaxVal(cacheKey.name(), value, max);
    }
  }

  public void validateMinMax(ExtractableEnum cacheKey, Float value, double min, double max) {
    validateMin(cacheKey, value, min);
    validateMax(cacheKey, value, max);
  }

  public void validateMin(ExtractableEnum cacheKey, Double value, double min) {
    if (value != null && value < min) {
      throwExceptionMinVal(cacheKey.name(), value, min);
    }
  }

  public void validateMax(ExtractableEnum cacheKey, Double value, double max) {
    if (value != null && value > max) {
      throwExceptionMaxVal(cacheKey.name(), value, max);
    }
  }

  public void validateMinMax(ExtractableEnum cacheKey, Double value, double min, double max) {
    validateMin(cacheKey, value, min);
    validateMax(cacheKey, value, max);
  }

  /**
   * IDs Clover has used but wouldn't normally be valid but are already in use.
   */
  private final static Set<String> EXCEPTIONAL_IDS;

  /**
   * Clover IDs were supposed to be base-32, but this was not enforced and some base-36 IDs made
   * it into the system so we allow it now.
   */
  private final static Set<Character> BASE_36_DIGITS_SET;

  private final static int ID_LENGTH = 13;

  static {
    Character[] BASE_36_DIGITS = {
        '0', '1', '2', '3', '4', '5',
        '6', '7', '8', '9', 'A', 'B',
        'C', 'D', 'E', 'F', 'G', 'H',
        'I', 'J', 'K', 'L', 'M', 'N',
        'O', 'P', 'Q', 'R', 'S', 'T',
        'U', 'V', 'W', 'X', 'Y', 'Z',
    };

    BASE_36_DIGITS_SET = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(BASE_36_DIGITS)));
    EXCEPTIONAL_IDS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
        "DFLTEMPLOYEE",
        "CLOVERDEV"
    )));
  }

  private static final String ERR_CLOVER_ID = "'%s' does not have a valid Clover ID: '%s'";

  /**
   * This clover id is not base-32, but could be an exceptional case.
   */
  private void handleInvalidCloverId(String name, String value) {
    // Check here because these IDs are very rare, using HashSet is cheap but not free
    if (EXCEPTIONAL_IDS.contains(value)) {
      // These invalid IDs are actually allowed
      return;
    }

    String msg = String.format(Locale.ROOT, ERR_CLOVER_ID, name, value);
    IllegalArgumentException e = new IllegalArgumentException(msg);
    e.fillInStackTrace();

    // Invoke callback to allow tracing the source of bad Clover IDs
    strictValidationFailed(e, ERR_CLOVER_ID, name, value);

    throw e;
  }

  /**
   * Check if the given id is a valid Clover ID, if not throw IllegalArgumentException.
   */
  public void validateCloverId(ExtractableEnum cacheKey, String id) {
    // Null is allowed and ignored, there is separate checking for null
    if (id == null) {
      return;
    }

    // The length must be correct
    if (id.length() != ID_LENGTH) {
      handleInvalidCloverId(cacheKey.name(), id);
      return;
    }

    // Characters must all be base-36
    for (int i = 0; i < id.length(); i++) {
      if (!BASE_36_DIGITS_SET.contains(id.charAt(i))) {
        handleInvalidCloverId(cacheKey.name(), id);
        return;
      }
    }
  }

  public void validateReferences(ExtractionStrategyEnum cacheKey) {
    // Ignored, we thought we might validate references automatically but this is better left
    // implemented on as-needed basis by each service provider
  }

  public interface StrictValidationFailedCallback {
    void onFailed(Exception e, String format, Object... args);
  }

  private void strictValidationFailed(Exception e, String format, Object... args) {
    StrictValidationFailedCallback callback = strictValidationFailedCallback;
    if (callback != null) {
      callback.onFailed(e, format, args);
    } else {
      Log.w(callingClass.getClass().getSimpleName(), String.format(Locale.ROOT, format, args));
    }
  }

  /**
   * For internal use only.
   */
  public static void setStrictValidationFailedCallback(StrictValidationFailedCallback callback) {
    strictValidationFailedCallback = callback;
  }

  public JSONObject getJsonObject() {
    return jsonObject;
  }

  public void setJsonObject(JSONObject newJSONObject) {
    jsonObject = newJSONObject;
  }

  public void initJsonObject(String json) {
    try {
      setJsonObject(new JSONObject(json));
    } catch (JSONException e) {
      throw new IllegalArgumentException("Invalid JSON", e);
    }
  }

  /**
   * Returns the internal JSONObject backing this instance, the return value is not a copy so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public JSONObject getJSONObject() {
    if (jsonObject == null) {
      jsonObject = new JSONObject();
    }
    return jsonObject;
  }

  /**
   * Update log when necessary.
   */
  public void logChange(String field) {
    if (changeLog == null) {
      changeLog = new Bundle();
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
  public void setChangeLog(Bundle newChangeLog) {
    changeLog = newChangeLog;
  }

  /**
   * Clears the 'field' field, the 'has' method for this field will now return false
   */
  public void clear(ExtractableEnum key) {
    unlogChange(key.name());
    getJSONObject().remove(key.name());
    cacheRemoveValue(key);
  }

  /**
   * Gets a Bundle which can be used to get and set data attached to this instance. The attached Bundle will be
   * parcelled but not jsonified.
   */
  public Bundle getBundle() {
    if (bundle == null) {
      bundle = new Bundle();
    }
    return bundle;
  }

  /**
   * Change bundle to a new bundle sent in.
   */
  public void setBundle(Bundle newBundle) {
    bundle = newBundle;
  }

  /**
   * Get access to changeLog
   */
  public Bundle getChangeLog() {
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

    try {
      JSONObject elementsContainer = getJSONObject().optJSONObject(name);
      JSONArray itemArray = elementsContainer.optJSONArray("elements");
      java.util.List<T> itemList = new java.util.ArrayList<>(itemArray.length());
      for (int i = 0; i < itemArray.length(); i++) {
        JSONObject obj = itemArray.optJSONObject(i);
        if (obj == null) {
          continue;
        }
        T item = JSON_CREATOR.create(obj);
        itemList.add(item);
      }

      return java.util.Collections.unmodifiableList(itemList);
    }
    catch (NullPointerException e) {
      // on an NPE just return null (eg. when elements unexpectedly doesn't exist)
      Log.e(TAG, "exception extracting 'elements' array", e);

      return null;
    }
  }

  /**
   * Generic method that returns a list of T items. Replacement for the "extract" methods which dealt with a list of Enums.
   */
  public <T extends Enum<T>> java.util.List<T> extractListEnum(String name, Class<T> clazz) {
    if (getJSONObject().isNull(name)) { return null; }

    JSONObject elementsContainer = getJSONObject().optJSONObject(name);
    JSONArray itemArray = elementsContainer.optJSONArray("elements");
    java.util.List<T> itemList = new java.util.ArrayList<>(itemArray.length());
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
  @SuppressWarnings("unchecked")
  public <T> java.util.List<T> extractListOther(String name, Class<T> clazz) {
    if (getJSONObject().isNull(name)) { return null; }

    JSONObject elementsContainer = getJSONObject().optJSONObject(name);
    JSONArray itemArray = elementsContainer.optJSONArray("elements");
    java.util.List<T> itemList = new java.util.ArrayList<>(itemArray.length());
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
    JSONObject jsonObj = getJSONObject().optJSONObject(name);
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
    JSONObject object = getJSONObject().optJSONObject(name);
    return com.clover.sdk.v3.JsonHelper.toMap(object);
  }

  /**
   * Generic method that replaces the "extract" methods which dealt with anything else (not a Record, Enum, or Map).
   */
  @SuppressWarnings("unchecked")
  public <T> T extractOther(String name, Class<T> clazz) {
    return getJSONObject().isNull(name) ? null : (T) returnType(name, clazz);
  }

  /**
   * Helper method that determines which "opt" method to use, based on the type of the item. Retrieves the information from
   * a given JSONArray
   */
  private <T> Object returnType(Class<T> item, JSONArray itemArray, int i) {
    if (item.equals(String.class)) {
      return itemArray.optString(i);
    }
    else if (item.equals(Boolean.class)) {
      return itemArray.optBoolean(i);
    }
    else if (item.equals(Integer.class)) {
      return itemArray.optInt(i);
    }
    else if (item.equals(Long.class)) {
      return itemArray.optLong(i);
    }
    else if (item.equals(Double.class)) {
      return itemArray.optDouble(i);
    }
    else if (item.equals(JSONArray.class)) {
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
    if (item.equals(String.class)) {
      return getJSONObject().optString(name);
    }
    else if (item.equals(Boolean.class)) {
      return getJSONObject().optBoolean(name);
    }
    else if (item.equals(Long.class)) {
      return getJSONObject().optLong(name);
    }
    else if (item.equals(Integer.class)) {
      return getJSONObject().optInt(name);
    }
    else if (item.equals(Double.class)) {
      return getJSONObject().optDouble(name);
    }
    else if (item.equals(JSONArray.class)) {
      return getJSONObject().optJSONArray(name);
    }
    else {
      return getJSONObject().optJSONObject(name);
    }
  }

  /**
   * Generic method that replaces the "set" methods which dealt with Arrays that hold Records.
   */
  public <T extends com.clover.sdk.JSONifiable> D setArrayRecord(java.util.List<T> list, ExtractableEnum key) {
    logChange(key.name());

    try {
      if (list == null) {
        getJSONObject().put(key.name(), JSONObject.NULL);
      }
      else {
        JSONArray array = new JSONArray();
        for (T obj : list) {
          if (obj == null) { continue; }
          array.put(obj.getJSONObject());
        }

        JSONObject elementsContainer = new JSONObject();
        elementsContainer.put("elements", array);
        getJSONObject().put(key.name(), elementsContainer);
      }
    } catch (JSONException e) {
      throw new IllegalArgumentException(e);
    }
    cacheMarkDirty(key);
    return callingClass;
  }

  /**
   * Generic method that replaces the "set" methods which dealt with Arrays that hold non-Records.
   */
  public <T> D setArrayOther(java.util.List<T> list, ExtractableEnum key) {
    logChange(key.name());

    try {
      if (list == null) {
        getJSONObject().put(key.name(), JSONObject.NULL);
      }
      else {
        JSONArray array = new JSONArray();
        for (T obj : list) {
          if (obj == null) { continue; }
          array.put(obj);
        }

        JSONObject elementsContainer = new JSONObject();
        elementsContainer.put("elements", array);
        getJSONObject().put(key.name(), elementsContainer);
      }
    } catch (JSONException e) {
      throw new IllegalArgumentException(e);
    }
    cacheMarkDirty(key);
    return callingClass;
  }

  /**
   * Generic method that replaces the "set" methods which dealt with Records.
   */
  public <T extends com.clover.sdk.JSONifiable> D setRecord(T item, ExtractableEnum key) {
    logChange(key.name());

    try {
      getJSONObject().put(key.name(), item == null ? JSONObject.NULL : item.getJSONObject());
    } catch (JSONException e) {
      throw new IllegalArgumentException(e);
    }
    cacheMarkDirty(key);
    return callingClass;
  }

  /**
   * Generic method that replaces the "set" methods which dealt with anything else (not an Array or Record).
   */
  public <T> D setOther(T item, ExtractableEnum key) {
    logChange(key.name());

    try {
      getJSONObject().put(key.name(), item == null ? JSONObject.NULL : com.clover.sdk.v3.JsonHelper.toJSON(item));
    } catch (JSONException e) {
      throw new IllegalArgumentException(e);
    }
    cacheMarkDirty(key);
    return callingClass;
  }

  /**
   * Generic method that replaces the "mergeChanges" method in all the classes.
   */
  public void mergeChanges(JSONObject srcObj, GenericClient srcGC) {
    try {
      // Make a copy of the source so the destination fields are copies
      JSONObject dstObj = getJSONObject();
      for (String field : srcGC.getChangeLog().keySet()) {
        dstObj.put(field, srcObj.get(field));
        logChange(field);
      }
    } catch (JSONException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Method that replaces the "writeToParcel" methods. "flags" seems to be unused, but was also passed in the original classes.
   */
  public void writeToParcel(Parcel dest, int flags) {
    com.clover.sdk.v3.JsonParcelHelper.wrap(getJSONObject()).writeToParcel(dest, 0);
    dest.writeBundle(bundle);
    dest.writeBundle(changeLog);
  }

}
