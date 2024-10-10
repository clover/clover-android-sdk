package com.clover.sdk.cfp.connector;
/**
 * @since 4.0.0
 */
public class MessageFromActivity {
  /**
   * @since 4.0.0
   */
  public final String name;

  /**
   * @since 4.0.0
   */
  public final String payload;

  /**
   * @since 4.0.0
   */
  public MessageFromActivity(String name, String payload) {
    this.name = name;
    this.payload = payload;
  }
}
