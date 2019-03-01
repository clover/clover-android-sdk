/**
 * Copyright (C) 2016 Clover Network, Inc.
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

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;

import com.clover.sdk.v1.Intents;

import java.util.List;

/**
 * This class is a replacement for the com.clover.sdk.util.Platform class. The previous class
 * required applications to be recompiled with the latest version for each new device release.
 * This class will function properly on future devices. In some cases this class may be a
 * near drop-in replacement for the old Platform class.
 * </p><p>
 * Some features have been removed compared to the previous class. In particular the
 * Feature.BATTERY was unclear since devices such as Station 2018 which can function for a limited
 * time on battery power but lack certain abilities such as ethernet and printing when running on
 * battery.
 * </p><p>
 * It is recommended you read
 * <a href="https://developer.android.com/guide/practices/compatibility">
 *   https://developer.android.com/guide/practices/compatibility
 * </a>
 * for more information about developing compatible apps. However please be aware that Clover
 * devices are technically not "Android Compatible" since they are not CTS qualified; though we
 * strive to make them as compatible as possible. Clover devices do not have the Google Play
 * Store, Google APIs or Google services.
 * </p><p>
 * Here are some additional links that may help when developing compatible apps:
 * </p><p>
 * Screen size: {@link android.content.res.Configuration#screenLayout} or
 * {@link android.content.res.Configuration#smallestScreenWidthDp}
 * <br/>
 * API level: {@link android.os.Build.VERSION#SDK_INT}
 * <br/>
 * Battery state: {@link android.os.PowerManager}
 * </p>
 */
public final class Platform2 {

  private Platform2() { }

  private static final String CARDHU = "cardhu";
  private static final String MAPLECUTTER = "maplecutter";
  private static final String LEAFCUTTER = "leafcutter";
  private static final String BAYLEAF = "bayleaf";
  private static final String GOLDENOAK = "goldenoak";
  private static final String KNOTTYPINE = "knottypine";

  /**
   * List of features that may be present on Clover devices. Non-Clover devices will return false
   * for all features.
   */
  public enum Feature {
    /**
     * Device supports taking card present payments using the {@link Intents#ACTION_SECURE_PAY}
     * intent.
     */
    SECURE_PAYMENTS {
      @Override
      public boolean isSupportedInternal(Context context) {
        // Check if an app is installed which can handle this intent
        Intent actionSecurePay = new Intent(Intents.ACTION_SECURE_PAY);
        List<ResolveInfo> infoList = context.getPackageManager()
            .queryIntentActivities(actionSecurePay, 0);
        if (infoList != null && infoList.size() > 0) {
          return true;
        }

        return false;
      }
    },
    /**
     * Device supports the {@link CustomerMode} class for checking, enabling and disabling
     * customer mode.
     */
    CUSTOMER_MODE {
      @Override
      public boolean isSupportedInternal(Context context) {
        // Old devices didn't advertise this system feature so hardcoded here
        String device = Build.DEVICE;
        switch (device) {
          case MAPLECUTTER:
          case LEAFCUTTER:
          case BAYLEAF:
          case GOLDENOAK:
          case KNOTTYPINE:
            return true;
        }

        return context.getPackageManager().hasSystemFeature("clover.software.customer_mode");
      }
    },
    /**
     * Device includes cellular mobile data hardware. The device may or may not currently have SIM
     * card, be in airplane mode, or have signal/connectivity.
     */
    MOBILE_DATA {
      @Override
      public boolean isSupportedInternal(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
      }
    },
    /**
     * Device supports login by a "default employee" without a PIN. The feature may or may not
     * actually be enabled, see {@link com.clover.sdk.v3.merchant.MerchantProperties} to check.
     */
    DEFAULT_EMPLOYEE {
      @Override
      protected boolean isSupportedInternal(Context context) {
        // Old devices didn't advertise this system feature so hardcoded here
        String device = Build.DEVICE;
        switch (device) {
          case CARDHU:
          case MAPLECUTTER:
          case LEAFCUTTER:
          case BAYLEAF:
          case GOLDENOAK:
          case KNOTTYPINE:
            return true;
        }

        return context.getPackageManager().hasSystemFeature("clover.software.default_employee");
      }
    },
    /**
     * Device has an ethernet port or has an officially supported ethernet port attachment. It may
     * or may not be currently enabled, attached or connected.
     */
    ETHERNET {
      @Override
      public boolean isSupportedInternal(Context context) {
        // Old devices didn't advertise this system feature so hardcoded here
        String device = Build.DEVICE;
        switch (device) {
          case BAYLEAF:
          case CARDHU:
          case MAPLECUTTER:
          case GOLDENOAK:
          case KNOTTYPINE:
            return true;
        }
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_ETHERNET);
      }
    },
    /**
     * Device has a secure touch screen.
     */
    SECURE_TOUCH {
      @Override
      protected boolean isSupportedInternal(Context context) {
        // Old devices didn't advertise this system feature so hardcoded here
        String device = Build.DEVICE;
        switch (device) {
          case MAPLECUTTER:
          case LEAFCUTTER:
          case BAYLEAF:
          case KNOTTYPINE:
            return true;
        }

        return context.getPackageManager().hasSystemFeature("clover.hardware.secure_touch");
      }
    },
    /**
     * Device supports customer facing external display. It may or may not be currently connected.
     */
    CUSTOMER_FACING_EXTERNAL_DISPLAY {
      @Override
      protected boolean isSupportedInternal(Context context) {
        // Old devices didn't advertise this system feature so hardcoded here
        String device = Build.DEVICE;
        switch (device) {
          case GOLDENOAK:
            return true;
        }

        return context.getPackageManager().hasSystemFeature("clover.hardware.customer_facing_external_display");
      }
    },
    /**
     * Device supports merchant to customer rotation with landscape->portrait configuration change.
     */
    CUSTOMER_ROTATION {
      @Override
      protected boolean isSupportedInternal(Context context) {
        // Old devices didn't advertise this system feature so hardcoded here
        String device = Build.DEVICE;
        switch (device) {
          case CARDHU:
          case GOLDENOAK:
            return true;
        }

        return context.getPackageManager().hasSystemFeature("clover.hardware.customer_rotation");
      }
    },
    ;

    protected abstract boolean isSupportedInternal(Context context);

    // Cached value
    private Boolean supported;

    public final boolean isSupported(Context context) {
      // synchronization not needed, getting/setting references is thread-safe
      if (supported != null) {
        return supported;
      }

      supported = isSupportedInternal(context);
      return supported;
    }
  }

  public enum Orientation {
    LANDSCAPE,
    PORTRAIT,
  }

  private static final String CLOVER_MANUFACTURER = "Clover";

  /**
   * Returns true when running on Clover hardware.
   */
  public static boolean isClover() {
    return Build.MANUFACTURER.equals(CLOVER_MANUFACTURER);
  }

  /**
   * Return true if the specified feature is supported on this device.
   */
  public static boolean supportsFeature(Context context, Feature f) {
    return f.isSupported(context);
  }

  // Cached value
  private static Orientation defaultOrientation;

  /**
   * Get the default orientation under which this device is normally used.
   * <b>This method does not return the current orientation!</b>
   */
  public static Orientation defaultOrientation(Context context) {
    // synchronization not needed, getting/setting references is thread-safe
    if (defaultOrientation != null) {
      return defaultOrientation;
    }

    WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    Configuration config = context.getResources().getConfiguration();

    int rotation = windowManager.getDefaultDisplay().getRotation();
    if (((rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) &&
        config.orientation == Configuration.ORIENTATION_LANDSCAPE)
        || ((rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) &&
        config.orientation == Configuration.ORIENTATION_PORTRAIT)) {
      defaultOrientation = Orientation.LANDSCAPE;
    } else {
      defaultOrientation = Orientation.PORTRAIT;
    }

    return defaultOrientation;
  }

}
