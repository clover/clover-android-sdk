package com.clover.sdk.cfp.connector;

/**
 * @since 4.0.0
 */
class BaseResponse {
  final ResultCode resultCode;
  private final boolean success;

  /**
   * @since 4.0.0
   */
  public BaseResponse(ResultCode resultCode) {
    success = resultCode == ResultCode.OK;
    this.resultCode = resultCode;
  }

  /**
   * @since 4.0.0
   */
  @SuppressWarnings("unused")
  public boolean isSuccess() {
    return success;
  }
}
