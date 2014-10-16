package com.clover.sdk.util;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.text.TextUtils;

public class CustomerMode {

  /**
   * Broadcast Action:  This is a sticky broadcast containing the customer mode state.
   * <p/>
   * NOTE: this is a protected intent that can only be sent by the system.
   */
  public static final String ACTION_CUSTOMER_MODE = "clover.intent.action.CUSTOMER_MODE";

  public static final String EXTRA_CUSTOMER_MODE_STATE = "clover.intent.extra.CUSTOMER_MODE_STATE";

  public enum State {
    ENABLED, DISABLED
  }

  private static final String AUTHORITY = "com.clover.service.provider";
  private static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

  private static final String SET_CUSTOMER_MODE_METHOD = "setCustomerMode";

  public static void enable(Context context) {
    try {
      context.getContentResolver().call(AUTHORITY_URI, SET_CUSTOMER_MODE_METHOD, State.ENABLED.name(), null);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      enableNonSecure(context);
    }
  }

  public static void disable(Context context) {
    try {
      context.getContentResolver().call(AUTHORITY_URI, SET_CUSTOMER_MODE_METHOD, State.DISABLED.name(), null);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
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
          return getOldStateFromIntent(state);
        }
      }
    }
    return State.DISABLED;
  }

  // TODO: old enum and methods for compatibility with older versions of leafcutter platform code. remove once all devices migrate to new version
  private enum OldState {
    SECURE, NON_SECURE, DISABLED
  }

  private static void enableSecure(Context context) {
    try {
      context.getContentResolver().call(AUTHORITY_URI, SET_CUSTOMER_MODE_METHOD, OldState.SECURE.name(), null);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    }
  }

  private static void enableNonSecure(Context context) {
    try {
      context.getContentResolver().call(AUTHORITY_URI, SET_CUSTOMER_MODE_METHOD, OldState.NON_SECURE.name(), null);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    }
  }

  private static State getOldStateFromIntent(String state) {
    if (!TextUtils.isEmpty(state)) {
      try {
        OldState oldState = OldState.valueOf(state);
        if (oldState == OldState.SECURE || oldState == OldState.NON_SECURE) {
          return State.ENABLED;
        }
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
      }
    }
    return State.DISABLED;
  }
}
