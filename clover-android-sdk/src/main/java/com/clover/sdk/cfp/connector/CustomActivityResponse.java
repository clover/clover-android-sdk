package com.clover.sdk.cfp.connector;
/**
 * @since 4.0.0
 */
public class CustomActivityResponse extends BaseResponse {
  /**
   * @since 4.0.0
   */
  @SuppressWarnings("unused")
  public static final int CUSTOM_ACTIVITY_RESULT_OK = -1;

  /**
   * @since 4.0.0
   */
  public static final int CUSTOM_ACTIVITY_RESULT_CANCEL = 0;

  /**
   * The identifier for the custom activity
   * @since 4.0.0
   */
  public final String name;

  /**
   * The Activity's result code when it finishes
   * @since 4.0.0
   */
  public final int result;

  /**
   * The Activity's PAYLOAD extra in the result
   * @since 4.0.0
   */
  public final String payload;

  /**
   * Optionally extra information if the custom activity failed to start
   * @since 4.0.0
   */
  public final String failReason;

  /**
   * @since 4.0.0
   */
  public CustomActivityResponse(String name, int result, String payload, String failReason) {
    this(ResultCode.OK, name, result, payload, failReason);
  }

  /**
   * @since 4.0.0
   */
  public CustomActivityResponse(ResultCode code, String name, int result, String payload, String failReason) {
    super(code);
    this.name = name;
    this.result = result;
    this.payload = payload;
    this.failReason = failReason;
  }
}
