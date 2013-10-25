/*
 * Copyright (C) 2013 Clover Network, Inc.
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

package com.clover.sdk.internal.util;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utility methods for {@code Object}s and primitive types.
 */
public class Objects {

  /**
   * Returns {@code true} if the two objects are equal.
   *
   * @param o1 the first object, or {@code null}
   * @param o2 the second object, or {@code null}
   */
  public static boolean equal(Object o1, Object o2) {
    if (o1 == o2) {
      return true;
    }
    return o1 != null && o1.equals(o2);
  }

  /**
   * Convenience method that accepts a variable number of {@code Object}s and passes them to
   * {@link Arrays#hashCode} to calculate the combined hash code.
   */
  public static int hashCode(Object... objects) {
    return Arrays.hashCode(objects);
  }

  public static ToStringHelper toStringHelper(Object o) {
    return new ToStringHelper(o.getClass().getSimpleName());
  }

  /**
   * Provides a convenient way to generate uniform output in an implementation of
   * {@link String#toString()}.
   */
  public static class ToStringHelper {
    private String className;
    private Map<String, String> map = new LinkedHashMap<String, String>();
    int maxLength = Integer.MAX_VALUE;

    private ToStringHelper(String className) {
      this.className = className;
    }

    /**
     * Add a field and value pair that will be returned by {@link #toString()}.
     */
    public ToStringHelper add(String field, Object value) {
      String s = String.valueOf(value);
      if (s.length() > maxLength) {
        s = s.substring(0, maxLength) + "...";
      }
      map.put(field, String.valueOf(value));
      return this;
    }

    /**
     * Truncate values that are longer than this many characters.
     */
    public ToStringHelper maxLength(int n) {
      maxLength = n;
      return this;
    }

    /**
     * Return a string in following format: {@code MyClass {field1=value,field2=value,...}}.
     */
    public String toString() {
      StringBuilder sb = new StringBuilder(className).append(" {");
      boolean first = true;
      for (String key : map.keySet()) {
        if (first) {
          first = false;
        } else {
          sb.append(",");
        }
        sb.append(key + "=" + map.get(key));
      }
      sb.append("}");
      return sb.toString();
    }
  }
}
