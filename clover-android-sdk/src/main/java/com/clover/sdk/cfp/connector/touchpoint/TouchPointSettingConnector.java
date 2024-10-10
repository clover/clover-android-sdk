package com.clover.sdk.cfp.connector.touchpoint;

import android.content.Context;
import android.os.Bundle;

import com.clover.sdk.v1.CallConnector;
import com.clover.sdk.v3.device.Device;
import com.clover.sdk.v3.merchant.MerchantDevicesV2Connector;

/**
 * A class that encapsulates interaction with the CFP_TOUCH_POINT Setting. Changes
 * to this setting are synced to the cloud.
 *
 * @see TouchPointSettingContract
 */
public class TouchPointSettingConnector extends CallConnector {

  public static final String METHOD_GET_TOUCH_POINT_SETTING = "METHOD_GET_TOUCH_POINT_SETTING";
  public static final String METHOD_SET_TOUCH_POINT_SETTING = "METHOD_SET_TOUCH_POINT_SETTING";
  public static final String SETTING_VALUE = "SETTING_VALUE";

  public TouchPointSettingConnector(Context context) {
    super(context, TouchPointSettingContract.AUTHORITY_URI);
  }

  /**
   * Get the Touch Point setting.
   *
   * @return The merchant-level setting value if available, or the global default value if merchant-level is not available.
   */
  public String getTouchPointSetting() {
    final Bundle result = context.getContentResolver().call(TouchPointSettingContract.AUTHORITY_URI, METHOD_GET_TOUCH_POINT_SETTING, null, null);
    if (result != null) {
      return result.getString(SETTING_VALUE);
    }
    return null;
  }

  /**
   * Set a custom Touch Point category for your app activity that corresponds
   * to the "com.clover.remote.terminal.action.START_CUSTOMER_EXPERIENCE_ACTIVITY" action
   *
   * @param category The category string from the manifest of your Take-over Touch Point activity
   * @return The merchant-level setting value if available, or the global default value if merchant-level is not available.
   */
  public String setTouchPointCategory(String category) {
    return setTouchPointCategory(category, false);
  }

  /**
   * Set a custom Touch Point category for your app activity that corresponds
   * to the "com.clover.remote.terminal.action.START_CUSTOMER_EXPERIENCE_ACTIVITY" action
   *
   * @param category The category string from the manifest of your Take-over Touch Point activity
   * @return The merchant-level setting value if available, or the global default value if merchant-level is not available.
   */
  public String setTouchPointCategory(String category, boolean deviceLevel) {
    checkForNullArgument(category);
    Bundle extras = new Bundle();
    extras.putString(TouchPointSettingContract.TOUCH_POINT_CATEGORY, category);
    Device device = new MerchantDevicesV2Connector(context).getDevice();
    String deviceId = device != null ? device.getId() : null;
    if (deviceLevel && deviceId != null) {
      extras.putString(TouchPointSettingContract.DEVICE_SERIAL, deviceId);
    }
    final Bundle result = context.getContentResolver().call(TouchPointSettingContract.AUTHORITY_URI, METHOD_SET_TOUCH_POINT_SETTING, null, extras);
    if (result != null) {
      return result.getString(SETTING_VALUE);
    }
    return null;
  }

  /**
   * Sets the Touch Point category back to the default Clover customer experience activity category
   * @return The merchant-level setting value if available, or the global default value if merchant-level is not available.
   */
  public String restoreDefaultTouchPointCategory() {
    final Bundle result = context.getContentResolver().call(TouchPointSettingContract.AUTHORITY_URI, METHOD_SET_TOUCH_POINT_SETTING, null, null);
    if (result != null) {
      return result.getString(SETTING_VALUE);
    }
    return null;
  }

  protected static <T> void checkForNullArgument(T arg) {
    if (arg == null) {
      throw new IllegalArgumentException();
    }
  }

}
