package com.clover.sdk.util;

import android.os.Build;
import android.util.Log;

public enum Platform {
  TF300T, C100, C200, C201, OTHER;

  private static final String TAG = "Platform";
  private static final String CLOVER = "Clover";

  public static Platform get() {
    Platform platform;
    try {
      platform = Platform.valueOf(Build.MODEL);
    } catch (IllegalArgumentException e) {
      platform = OTHER;
    }

    Log.i(TAG, "platform: " + platform);
    return platform;
  }

  public static boolean isClover() {
    return Build.MANUFACTURER.equals(CLOVER);
  }

  public static boolean isCloverMobile() {
    Platform platform = get();

    if (platform == C200 || platform == C201) {
      return true;
    }

    return false;
  }
}
