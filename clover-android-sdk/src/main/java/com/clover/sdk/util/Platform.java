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
package com.clover.sdk.util;

import android.os.Build;

public enum Platform {
  TF300T, C100, C200, C201, C300, C301, OTHER;

  private static final String TAG = "Platform";
  private static final String CLOVER = "Clover";

  public static Platform get() {
    Platform platform;
    try {
      platform = Platform.valueOf(Build.MODEL);
    } catch (IllegalArgumentException e) {
      platform = OTHER;
    }

    return platform;
  }

  public static boolean isClover() {
    return Build.MANUFACTURER.equals(CLOVER);
  }

  public static boolean isCloverStation() {
    Platform platform = get();
    return (platform == C100);
  }

  public static boolean isCloverMobile() {
    Platform platform = get();
    return (platform == C200 || platform == C201);
  }

  public static boolean isCloverMini() {
    Platform platform = get();
    return (platform == C300 || platform == C301);
  }

  public static boolean is3g() {
    Platform platform = get();
    return platform == C201 || platform == C301;
  }

  public static boolean isSecureBoardPresent() {
    return isCloverMobile() || isCloverMini();
  }

}
