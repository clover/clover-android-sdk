/**
 * Copyright (C) 2015 Clover Network, Inc.
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

public class CustomerMode {

  /**
   * Broadcast Action:  This is a sticky broadcast containing the customer mode state.
   * <p/>
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
   * <p/>
   * NOTE: doing so overrides the customerMode manifest value if present
   */
  public static final int WINDOW_FEATURE_CUSTOMER_MODE = 100;

  /**
   * Use with Activity.requestWindowFeature(WINDOW_FEATURE_CUSTOMER_MODE) to disable customer mode button in navigation bar
   * <p/>
   * NOTE: doing so overrides the customerMode manifest value if present
   */
  public static final int WINDOW_FEATURE_NO_CUSTOMER_MODE = 101;

  private static final String AUTHORITY = "com.clover.service.provider";
  private static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

  private static final String SET_CUSTOMER_MODE_METHOD = "setCustomerMode";
  private static final String CALL_METHOD_GET_CUSTOMER_MODE_REQUIRE_PIN = "getCustomerModeRequirePin";
  private static final String CALL_METHOD_SET_CUSTOMER_MODE_REQUIRE_PIN = "setCustomerModeRequirePin";
  private static final String EXTRA_REQUIRE_PIN = "requirePin";

  public static void enable(Context context) {
    if (Platform.isCloverMobile() || Platform.isCloverMini()) {
      try {
        context.getContentResolver().call(AUTHORITY_URI, SET_CUSTOMER_MODE_METHOD, State.ENABLED.name(), null);
      } catch (IllegalArgumentException e) {
      }
    }
  }

  public static void disable(Context context) {
    if (Platform.isCloverMobile() || Platform.isCloverMini()) {
      try {
        context.getContentResolver().call(AUTHORITY_URI, SET_CUSTOMER_MODE_METHOD, State.DISABLED.name(), null);
      } catch (IllegalArgumentException e) {
      }
    }
  }

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

  public static boolean getRequirePinOnExit(Context context) {
    try {
      Bundle result = context.getContentResolver().call(AUTHORITY_URI, CALL_METHOD_GET_CUSTOMER_MODE_REQUIRE_PIN, null, null);
      return result.getBoolean(EXTRA_REQUIRE_PIN);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    }
    return false;
  }

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
