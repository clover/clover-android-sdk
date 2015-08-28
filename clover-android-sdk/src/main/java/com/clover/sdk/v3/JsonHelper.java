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
package com.clover.sdk.v3;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonHelper {
  public static Object toJSON(Object object) {
    if (object instanceof Map) {
      JSONObject json = new JSONObject();
      Map map = (Map) object;
      for (Object key : map.keySet()) {
        try {
          json.putOpt(key.toString(), toJSON(map.get(key)));
        } catch (JSONException e) { /* ignore for now */ }
      }
      return json;
    } else if (object instanceof Iterable) {
      JSONArray json = new JSONArray();
      for (Object value : ((Iterable) object)) {
        json.put(value);
      }
      return json;
    } else if (object instanceof Enum) {
      return ((Enum) object).name();
    } else {
      return object;
    }
  }

  public static boolean isEmptyObject(JSONObject object) {
    return object.names() == null;
  }

  public static Map<String, Object> getMap(JSONObject object, String key) {
    return toMap(object.optJSONObject(key));
  }

  public static Map toMap(JSONObject object) {
    Map map = new HashMap();
    Iterator keys = object.keys();
    while (keys.hasNext()) {
      String key = (String) keys.next();
      map.put(key, fromJson(object.opt(key)));
    }
    return map;
  }

  public static List toList(JSONArray array) {
    List list = new ArrayList();
    for (int i = 0; i < array.length(); i++) {
      list.add(fromJson(array.opt(i)));
    }
    return list;
  }

  private static Object fromJson(Object json) {
    if (json == JSONObject.NULL || json == null) {
      return null;
    } else if (json instanceof JSONObject) {
      return toMap((JSONObject) json);
    } else if (json instanceof JSONArray) {
      return toList((JSONArray) json);
    } else {
      return json;
    }
  }

  public static JSONObject deepCopy(JSONObject jsonObject) {
    return (JSONObject)deepCopy((Object)jsonObject);
  }

  private static Object deepCopy(Object object) {
    if (object == null) {
      return null;
    } else if (object == JSONObject.NULL) {
      return JSONObject.NULL;
    } else {
      Class<?> c = object.getClass();
      if (c == JSONObject.class) {
        JSONObject src = ((JSONObject)object);
        JSONObject dst = new JSONObject();

        Iterator<String> srcKeys = src.keys();
        while (srcKeys.hasNext()) {
          String srcKey = srcKeys.next();
          try {
            dst.put(srcKey, deepCopy(src.get(srcKey)));
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }

        return dst;
      } else if (c == JSONArray.class) {
        JSONArray src = ((JSONArray)object);
        JSONArray dst = new JSONArray();

        for (int i = 0, count = src.length(); i < count; i++) {
          try {
            dst.put(deepCopy(src.get(i)));
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }

        return dst;
      } else {
        if (c == String.class ||
            c == Long.class ||
            c == Boolean.class ||
            c == Integer.class ||
            c == Double.class ||
            c == Float.class) {
          return object;
        } else {
          throw new RuntimeException("Unsupported object type: " + c.getSimpleName());
        }
      }

    }
  }
}