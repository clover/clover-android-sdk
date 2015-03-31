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
}
