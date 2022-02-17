/*
 * Copyright (C) 2016 Clover Network, Inc.
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
package com.clover.sdk.util;

import com.clover.sdk.internal.util.UnstableContentResolverClient;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;

import java.util.Locale;


/**
 * This class allows an app to check and update the state of customer mode. Customer mode is a
 * feature available on Clover devices (except Station) that allows an activity run fullscreen
 * on the primary display without the navigation bar or status bar and without the ability to
 * do a gesture to bring them back.
 * <p/>
 * Unless you intend for all users of the device to be locked out of all other applications you
 * should ensure that your application always has some way to exit customer mode so the operator
 * can exit your app and manage the device.
 *
 * <h3>Secondary Display Support</h3>
 *
 * Activities running on secondary display of a multi-display device need not invoke the methods
 * here since secondary display has no navigation bar or status bar and has no exit gestures by
 * default. The enable and disable methods will attempt to detect if the passed context is an
 * activity and if so will perform no action on a secondary display activity.
 *
 * <h3>Original Station</h3>
 *
 * If you are writing an application for the original Clover Station, this class won't function
 * properly. Instead using the following code snippet to get Clover Station into customer mode.
 * <pre class="prettyprint">
 *   if (!Platform2.supportsFeature(context, Platform2.Feature.CUSTOMER_MODE)) {
 *       getWindow().getDecorView().setSystemUiVisibility(
 *           View.SYSTEM_UI_FLAG_LAYOUT_STABLE
 *           | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
 *           | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
 *           | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
 *           | View.SYSTEM_UI_FLAG_LOW_PROFILE
 *           | View.SYSTEM_UI_FLAG_FULLSCREEN
 *           | 0x10000000);
 *   }
 * </pre>
 */
public class CustomerMode {

  /**
   * Broadcast Action: This is a sticky broadcast containing the customer mode state.
   * <p/>
   * NOTE: this is a protected intent that can only be sent by the system.
   */
  public static final String ACTION_CUSTOMER_MODE = "clover.intent.action.CUSTOMER_MODE";

  /**
   * String extra with one of the {@link State} enum values.
   */
  public static final String EXTRA_CUSTOMER_MODE_STATE = "clover.intent.extra.CUSTOMER_MODE_STATE";

  /**
   * State of customer mode.
   */
  public enum State {
    /**
     * Customer mode is enabled on the primary display.
     */
    ENABLED,
    /**
     * Customer mode is not enabled on the primary display.
     */
    DISABLED,
  }

  /**
   * Deprecated. Most devices do not support this feature, supply your own user interface for
   * entering and exiting customer mode instead.
   */
  @Deprecated
  public static final int WINDOW_FEATURE_CUSTOMER_MODE = 100;

  /**
   * Deprecated. Most devices do not support this feature, supply your own user interface for
   * entering and exiting customer mode instead.
   */
  @Deprecated
  public static final int WINDOW_FEATURE_NO_CUSTOMER_MODE = 101;

  private static final String AUTHORITY = "com.clover.service.provider";
  private static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

  private static final String SET_CUSTOMER_MODE_METHOD = "setCustomerMode";
  /** Old terminology, actually refers to the employee passcode */
  private static final String EXTRA_REQUIRE_PIN = "requirePin";

  /**
   * Instances of this class have never been necessary and do nothing, all methods and fields are
   * static.
   */
  @Deprecated
  public CustomerMode() {
  }

  /**
   * Enable customer mode. Hide the navigation bar and status bar on the primary display. This
   * method has no effect if the passed context is an activity which is running on a non-primary
   * display.
   * <p/>
   * Deprecated in favor of {@link #enable(Activity)}.
   */
  @Deprecated
  public static void enable(Context context) {
    if (Platform2.supportsFeature(context, Platform2.Feature.CUSTOMER_MODE)) {
      if (context instanceof Activity) {
        Activity activity = (Activity) context;
        if (!isShownOnPrimaryDisplay(activity)) {
          return;
        }
      }

      try {
        UnstableContentResolverClient client = new UnstableContentResolverClient(context.getContentResolver(), AUTHORITY_URI);
        client.call(SET_CUSTOMER_MODE_METHOD, State.ENABLED.name(), null, null);
      } catch (IllegalArgumentException e) {
      }
    }
  }

  /**
   * Enable customer mode. Hide the navigation bar and status bar on the primary display. This
   * method has no effect if the passed activity is running on a non-primary display.
   */
  public static void enable(Activity activity) {
    enable((Context) activity);
  }

  /**
   * Disable customer mode. Bring back the navigation bar and status bar on the primary display
   * and show the lockscreen. This method has no effect if the passed context is an activity which
   * is running on a non-primary display.
   * <p/>
   * Deprecated in favor of {@link #disable(Activity)}.
   */
  @Deprecated
  public static void disable(Context context) {
    disable(context, false);
  }

  /**
   * Disable customer mode. Bring back the navigation bar and status bar on the primary display
   * and show the lockscreen. This method has no effect if the passed context is an activity which
   * is running on a non-primary display.
   */
  public static void disable(Activity activity) {
    disable((Context) activity);
  }

  /**
   * Disable customer mode. Bring back the navigation bar and status bar on the primary display
   * and show the lockscreen. This method has no effect if the passed context is an activity which
   * is running on a non-primary display.
   * <p/>
   * Deprecated in favor of {@link #disable(Activity, boolean)}.
   *
   * @param requireEmployeePasscode true if you want to force the employee lockscreen to appear
   *                                without the "default employee" option if the merchant has
   *                                enabled "default employee".
   */
  @Deprecated
  public static void disable(Context context, boolean requireEmployeePasscode) {
    if (Platform2.supportsFeature(context, Platform2.Feature.CUSTOMER_MODE)) {
      if (context instanceof Activity) {
        Activity activity = (Activity) context;
        if (!isShownOnPrimaryDisplay(activity)) {
          return;
        }
      }

      try {
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_REQUIRE_PIN, requireEmployeePasscode);
        UnstableContentResolverClient client = new UnstableContentResolverClient(context.getContentResolver(), AUTHORITY_URI);
        client.call(SET_CUSTOMER_MODE_METHOD, State.DISABLED.name(), bundle, null);
      } catch (IllegalArgumentException e) {
      }
    }
  }

  /**
   * Disable customer mode. Bring back the navigation bar and status bar on the primary display
   * and show the lockscreen. This method has no effect if the passed context is an activity which
   * is running on a non-primary display.
   *
   * @param requireEmployeePasscode true if you want to force the employee lockscreen to appear
   *                                without the "default employee" option if the merchant has
   *                                enabled "default employee".
   */
  public static void disable(Activity activity, boolean requireEmployeePasscode) {
    disable((Context) activity, requireEmployeePasscode);
  }

  /**
   * Returns the current state of customer mode. If the passed context is an activity then it
   * always returns DISABLED if the activity is running on the non-primary display.
   * <p/>
   * Deprecated in favor of {@link #getState(Activity)}.
   */
  @Deprecated
  public static CustomerMode.State getState(Context context) {
    if (!Platform2.supportsFeature(context, Platform2.Feature.CUSTOMER_MODE)) {
      return State.DISABLED;
    }

    if (context instanceof Activity) {
      Activity activity = (Activity) context;
      if (!isShownOnPrimaryDisplay(activity)) {
        return State.ENABLED;
      }
    }

    IntentFilter filter = new IntentFilter(ACTION_CUSTOMER_MODE);
    Intent intent = context.registerReceiver(null, filter);
    return getStateFromIntent(intent);
  }

  /**
   * Returns the current state of customer mode. Always returns DISABLED if the passed activity is
   * running on the non-primary display.
   */
  public static CustomerMode.State getState(Activity activity) {
    return getState((Context) activity);
  }

  /**
   * Returns true if the given activity is shown on the primary display of a device capable of
   * entering customer mode. Must be called during or after
   * {@link android.app.Activity#onCreate(Bundle)}. The value false indicates either the device
   * does not support customer mode at all (such as the original Clover Station) or the activity
   * is shown on secondary display.
   */
  public static boolean isShownOnPrimaryDisplay(Activity activity) {
    // Oreo is the first version to allow activities on secondary displays
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
      return true;
    }

    Display d = activity.getWindowManager().getDefaultDisplay();
    // Display may be null in very early startup: before super#onCreate()
    if (d == null) {
      String warning = String.format(Locale.US, "Unable to detect display for Activity %s. "
                                                + "Ensure that CustomerMode.isShownOnPrimaryDisplay() is invoked after super.onCreate().",
          activity.getClass());
      Throwable throwable = new Exception(warning).fillInStackTrace();
      Log.w("CustomerMode", throwable);
      return true;
    }

    return d.getDisplayId() == Display.DEFAULT_DISPLAY;
  }

  /**
   * Return the current state of customer mode given the {@link #EXTRA_CUSTOMER_MODE_STATE} string
   * that was obtained from a {@link #ACTION_CUSTOMER_MODE} broadcast.
   */
  public static State getStateFromIntent(Intent intent) {
    if (intent != null) {
      String state = intent.getStringExtra(EXTRA_CUSTOMER_MODE_STATE);

      if (!TextUtils.isEmpty(state)) {
        try {
          return State.valueOf(state);
        } catch (IllegalArgumentException e) {
          return State.DISABLED;
        }
      }
    }
    return State.DISABLED;
  }

  /**
   * Convenience method to register broadcast receiver of customer mode change.
   * This method has no effect if the passed context is an activity which
   * is running on a non-primary display.
   */
  public static void registerReceiver(Activity activity, BroadcastReceiver customerModeReceiver) {
    if (Platform2.supportsFeature(activity, Platform2.Feature.CUSTOMER_MODE)) {
      if (!isShownOnPrimaryDisplay(activity)) {
        return;
      }
      IntentFilter intentFilter = new IntentFilter(ACTION_CUSTOMER_MODE);
      activity.registerReceiver(customerModeReceiver, intentFilter);
    }
  }

  /**
   * Convenience method to unregister broadcast receiver of customer mode change.
   * This method has no effect if the passed context is an activity which
   * is running on a non-primary display.
   */
  public static void unregisterReceiver(Activity activity, BroadcastReceiver customerModeReceiver) {
    if (Platform2.supportsFeature(activity, Platform2.Feature.CUSTOMER_MODE)) {
      if (!isShownOnPrimaryDisplay(activity)) {
        return;
      }
      activity.unregisterReceiver(customerModeReceiver);
    }
  }

}
