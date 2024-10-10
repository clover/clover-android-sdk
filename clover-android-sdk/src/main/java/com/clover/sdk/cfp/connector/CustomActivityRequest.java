package com.clover.sdk.cfp.connector;
/**
 * @since 4.0.0
 */
public class CustomActivityRequest {
  /**
   * @since 4.0.0
   */
  public String name;

  /**
   * @since 4.0.0
   */
  public String payload;


  /**
   * @since 4.0.0
   */
  public CustomActivityRequest(String name, String payload) {
    this.name = name;
    this.payload = payload;
  }

}
