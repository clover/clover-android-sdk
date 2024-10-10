package com.clover.sdk.cfp.connector;
/**
 * @since 4.0.0
 */
public class ResetDeviceResponse extends BaseResponse {
  /**
   * @since 4.0.0
   */
  @SuppressWarnings("unused")
  public String reason;

  /**
   * @since 4.0.0
   */
  @SuppressWarnings("unused")
  public ExternalDeviceState state;

  /**
   * @since 4.0.0
   */
  public ResetDeviceResponse(ResultCode resultCode) {
    super(resultCode);
  }
}
