/**
 * Copyright (C) 2016 Clover Network, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * This class allows an app to check and update the state of customer mode. Customer mode is a
 * feature available on Clover devices (except Station) that allows an Activity run fullscreen
 * without the navigation bar or status bar and without the ability to do an offscreen swipe down
 * to bring them back.
 * <p>
 * You should ensure that your application always has some way to exit customer mode so the user
 * cannot get stuck.
 * <p>
 * If you are writing an application for Clover Station, this class won't function properly.
 * Instead using the following code snippet to get Clover Station into customer mode.
 * <code>
 *       getWindow().getDecorView().setSystemUiVisibility(
 *           View.SYSTEM_UI_FLAG_LAYOUT_STABLE
 *           | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
 *           | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
 *           | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
 *           | View.SYSTEM_UI_FLAG_LOW_PROFILE
 *           | View.SYSTEM_UI_FLAG_FULLSCREEN
 *           | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
 * </code>
 */
public class CustomerMode {

  /**
   * Broadcast Action: This is a sticky broadcast containing the customer mode state.
   * <p>
   * NOTE: this is a protected intent that can only be sent by the system.
   */
  public static final String ACTION_CUSTOMER_MODE = "clover.intent.action.CUSTOMER_MODE";

  public static final String EXTRA_CUSTOMER_MODE_STATE = "clover.intent.extra.CUSTOMER_MODE_STATE";

  public static final String CLOVER_CUSTOMER_MODE_REQUIRE_PIN_ON_EXIT = "clover_customer_mode_require_pin_on_exit";

  public enum State {
    ENABLED, DISABLED
  }

  /**
   * Use with Activity.requestWindowFeature(WINDOW_FEATURE_CUSTOMER_MODE) to enable customer mode button in navigation bar
   * <p>
   * NOTE: doing so overrides the customerMode manifest value if present
   */
  public static final int WINDOW_FEATURE_CUSTOMER_MODE = 100;

  /**
   * Use with Activity.requestWindowFeature(WINDOW_FEATURE_CUSTOMER_MODE) to disable customer mode button in navigation bar
   * <p>
   * NOTE: doing so overrides the customerMode manifest value if present
   */
  public static final int WINDOW_FEATURE_NO_CUSTOMER_MODE = 101;

  private static final String AUTHORITY = "com.clover.service.provider";
  private static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

  private static final String SET_CUSTOMER_MODE_METHOD = "setCustomerMode";
  private static final String CALL_METHOD_GET_CUSTOMER_MODE_REQUIRE_PIN = "getCustomerModeRequirePin";
  private static final String CALL_METHOD_SET_CUSTOMER_MODE_REQUIRE_PIN = "setCustomerModeRequirePin";
  private static final String EXTRA_REQUIRE_PIN = "requirePin";

  /**
   * Enable customer mode. Hide the navigation bar and status bar.
   */
  public static void enable(Context context) {
    if (Platform.supportsFeature(Platform.Feature.CUSTOMER_MODE)) {
      try {
        context.getContentResolver().call(AUTHORITY_URI, SET_CUSTOMER_MODE_METHOD, State.ENABLED.name(), null);
      } catch (IllegalArgumentException e) {
      }
    }
  }

  /**
   * Disable customer mode. Bring back the navigation bar and status bar.
   */
  public static void disable(Context context) {
    disable(context, false);
  }

  /**
   * Disable customer mode. Bring back the navigation bar and status bar.
   *
   * @param requirePin true if you want to force the employee lockscreen to appear without the "default employee" option
   */
  public static void disable(Context context, boolean requirePin) {
    if (Platform.supportsFeature(Platform.Feature.CUSTOMER_MODE)) {
      try {
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_REQUIRE_PIN, requirePin);
        context.getContentResolver().call(AUTHORITY_URI, SET_CUSTOMER_MODE_METHOD, State.DISABLED.name(), bundle);
      } catch (IllegalArgumentException e) {
      }
    }
  }

  /**
   * Returns the current state of customer mode, either ENABLED or DISABLED.
   */
  public static CustomerMode.State getState(Context context) {
    IntentFilter filter = new IntentFilter(ACTION_CUSTOMER_MODE);
    Intent intent = context.registerReceiver(null, filter);
    return getStateFromIntent(intent);
  }

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
   * @deprecated
   */
  @Deprecated
  public static boolean getRequirePinOnExit(Context context) {
    try {
      Bundle result = context.getContentResolver().call(AUTHORITY_URI, CALL_METHOD_GET_CUSTOMER_MODE_REQUIRE_PIN, null, null);
      return result.getBoolean(EXTRA_REQUIRE_PIN);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * @deprecated
   */
  @Deprecated
  public static void setRequirePinOnExit(Context context, boolean requirePinOnExit) {
    try {
      Bundle bundle = new Bundle();
      bundle.putBoolean(EXTRA_REQUIRE_PIN, requirePinOnExit);
      context.getContentResolver().call(AUTHORITY_URI, CALL_METHOD_SET_CUSTOMER_MODE_REQUIRE_PIN, null, bundle);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    }
  }
}
