package com.clover.sdk.cfp.connector;

/**
 * @since 4.0.0
 */
public class DeviceStatusResponse extends BaseResponse {
  /**
   * @since 4.0.0
   */
  public ExternalDeviceState state;

  /**
   * @since 4.0.0
   */
  public ExternalDeviceStateData data;

  /**
   * @since 4.0.0
   */
  public DeviceStatusResponse(ResultCode resultCode) {
    super(resultCode);
    state = null;
    data = null;
  }

  /**
   * @since 4.0.0
   */
  public DeviceStatusResponse(ResultCode resultCode, ExternalDeviceState state, ExternalDeviceStateData data) {
    super(resultCode);
    this.state = state;
    this.data = data;
  }

  /**
   * @since 4.0.0
   */
  @SuppressWarnings("unused")
  public ExternalDeviceState getState() {
    return state;
  }

  /**
   * @since 4.0.0
   */
  @SuppressWarnings("unused")
  public ExternalDeviceStateData getData() {
    return data;
  }
}
