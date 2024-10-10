package com.clover.sdk.cfp.connector;

import java.io.Serializable;
/**
 * @since 4.0.0
 */
public enum ExternalDeviceState implements Serializable {
  /**
   * @since 4.0.0
   */
  @SuppressWarnings("unused")
  IDLE,
  /**
   * @since 4.0.0
   */
  @SuppressWarnings("unused")
  BUSY,
  /**
   * @since 4.0.0
   */
  @SuppressWarnings("unused")
  WAITING_FOR_POS,
  /**
   * @since 4.0.0
   */
  @SuppressWarnings("unused")
  WAITING_FOR_CUSTOMER,
  /**
   * @since 4.0.0
   */
  UNKNOWN
}
