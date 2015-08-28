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
package com.clover.sdk.internal.util;

/**
 * String utility methods.
 */
public class Strings {

  /**
   * Return either {@code s}, or an empty string if {@code s} is {@code null}.
   */
  public static String nullToEmpty(String s) {
    return s == null ? "" : s;
  }

  /**
   * Return {@code true} if the given string is null or empty.
   */
  public static boolean isNullOrEmpty(String s) {
    return s == null || s.isEmpty();
  }
}