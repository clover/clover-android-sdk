package com.clover.sdk.cfp.activity;

/**
 *
 * @since 4.0.0
 */
@SuppressWarnings("unused")
public class CFPConstants {
  public static final String CUSTOMER_INFO_EXTRA = "com.clover.extra.CUSTOMER_INFO";
  public static final String DISPLAY_ORDER_EXTRA = "com.clover.extra.DISPLAY_ORDER";

  public static final String ACTION_SESSION_DATA = "com.clover.cfp.SESSION_DATA_ACTION";
  public static final String EXTRA_PAYLOAD = "com.clover.remote.terminal.remotecontrol.extra.EXTRA_PAYLOAD";
  /**
   * @deprecated
   */
  @SuppressWarnings("DeprecatedIsStillUsed")
  public static final String ACTION_V1_MESSAGE_TO_ACTIVITY = "com.clover.remote-terminal.remotecontrol.action.V1_MESSAGE_TO_ACTIVITY";
  /**
   * @deprecated
   */
  @SuppressWarnings("DeprecatedIsStillUsed")
  public static final String ACTION_V1_MESSAGE_FROM_ACTIVITY = "com.clover.remote-terminal.remotecontrol.action.V1_MESSAGE_FROM_ACTIVITY";
  /**
   * @deprecated
   */
  @SuppressWarnings("DeprecatedIsStillUsed")
  public static final String CATEGORY_CUSTOM_ACTIVITY = "com.clover.cfp.ACTIVITY";
  /**
   * @deprecated
   */
  public static final String ACTION_V1_CUSTOM_INPUT_OPTIONS = "com.clover.remote-terminal.remotecontrol.action.V1_CUSTOM_INPUT_OPTIONS";
  /**
   * @deprecated
   */
  public static final String ACTION_V1_CUSTOM_DEVICE_EVENT = "com.clover.remote-terminal.remotecontrol.action.V1_CUSTOM_DEVICE_EVENT";
  /**
   * @deprecated
   */
  public static final String ACTION_V1_CUSTOM_KEY_PRESS = "com.clover.remote-terminal.remotecontrol.action.V1_CUSTOM_KEY_PRESS";

  /**
   * @deprecated
   */
  public static final String EXTRA_DEVICE_EVENT = "com.clover.remote.terminal.remotecontrol.extra.DEVICE_EVENT";
  /**
   * @deprecated
   */
  public static final String EXTRA_KEYPRESS = "com.clover.remote.terminal.remotecontrol.extra.KEY_PRESS";

  /**
   * @deprecated
   */
  public static final String CATEGORY_REMOTE_PROTOCOL_ACTIVITY = "com.clover.remote.protocol.ACTIVITY";

  // This section used to designate Touch Point activity actions

  // Customer Experience Touch Point actions
  /**
   * @deprecated
   */
  public static final String ACTION_START_CUSTOMER_EXPERIENCE_ACTIVITY = "com.clover.remote.terminal.action.START_CUSTOMER_EXPERIENCE_ACTIVITY";
  /**
   * @deprecated
   */
  public static final String ACTION_START_LOGO_ACTIVITY = "com.clover.remote.terminal.action.START_WELCOME_LOGO_ACTIVITY";
  /**
   * @deprecated
   */
  public static final String ACTION_START_MESSAGE_ACTIVITY = "com.clover.remote.terminal.action.START_MESSAGE_ACTIVITY";
  /**
   * @deprecated
   */
  public static final String ACTION_START_THANK_YOU_ACTIVITY = "com.clover.remote.terminal.action.START_THANK_YOU_ACTIVITY";
  /**
   * @deprecated
   */
  public static final String ACTION_START_DISPLAY_ORDER_ACTIVITY = "com.clover.remote.terminal.action.START_DISPLAY_ORDER_ACTIVITY";
  /**
   * @deprecated
   */
  public static final int LOGO_TOUCHPOINT_ACTIVITY_REQUEST_CODE = 201;
  /**
   * @deprecated
   */
  public static final int THANK_YOU_TOUCHPOINT_ACTIVITY_REQUEST_CODE = 202;
  /**
   * @deprecated
   */
  public static final int MESSAGE_TOUCHPOINT_ACTIVITY_REQUEST_CODE = 203;
  /**
   * @deprecated
   */
  public static final int DISPLAY_ORDER_TOUCHPOINT_ACTIVITY_REQUEST_CODE = 204;
}
