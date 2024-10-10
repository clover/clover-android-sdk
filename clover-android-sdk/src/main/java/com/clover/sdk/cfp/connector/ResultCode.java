package com.clover.sdk.cfp.connector;

import java.io.Serializable;

/**
 * @since 4.0.0
 */
public enum ResultCode implements Serializable {
  /**
   * completed normally
   * @since 4.0.0
   */
  OK,
  /**
   * An Error occurred, reason unknown.
   * @since 4.0.0
   */
  ERROR,
  /**
   * Request failed to execute.
   * @since 4.0.0
   */
  FAIL,
  /**
   * Request wasn't sent because of a connection failure
   * @since 4.0.0
   */
  DISCONNECTED,
  /**
   * Request was cancelled, and completed
   * @since 4.0.0
   */
  @SuppressWarnings("unused")
  CANCEL,
  /**
   * There was an interruption waiting for the response, so the status is unknown
   * @since 4.0.0
   */
  INTERRUPTED,
  /**
   * We have an unknown result code
   * @since 4.0.0
   */
  UNKNOWN
}
