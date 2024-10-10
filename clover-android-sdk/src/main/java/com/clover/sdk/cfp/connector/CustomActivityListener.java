package com.clover.sdk.cfp.connector;

/**
 * @since 4.0.0
 */
public interface CustomActivityListener {
  /**
   * @since 4.0.0
   */
  void onMessageFromActivity(MessageFromActivity message);

  /**
   * @since 4.0.0
   */
  void onCustomActivityResult(CustomActivityResponse result);
}
