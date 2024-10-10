package com.clover.sdk.cfp.connector.touchpoint;

import android.net.Uri;

public class TouchPointSettingContract {

  public static final String TOUCH_POINT_CATEGORY = "com.clover.cfp.touchpoint.category";
  public static final String DEVICE_SERIAL = "com.clover.cfp.device.serial";
  /**
   * The authority for the touch point settings provider
   */
  public static final String AUTHORITY = "com.clover.cfp.provider.touchpointsetting.authority";

  /**
   * A content:// style uri to the authority for the provider
   */
  public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

}
