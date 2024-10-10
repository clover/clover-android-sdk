package com.clover.sdk.cfp.connector;

import java.io.Serializable;
/**
 * @since 4.0.0
 */
public class ExternalDeviceStateData implements Serializable {
  public final String externalPaymentId;
  public final String customActivityId;

  /**
   * @since 4.0.0
   */
  @SuppressWarnings("unused")
  public ExternalDeviceStateData() {
    this.externalPaymentId = null;
    this.customActivityId = null;
  }

  /**
   * @since 4.0.0
   */
  public ExternalDeviceStateData(String externalPaymentId, String customActivityId) {
    this.externalPaymentId = externalPaymentId;
    this.customActivityId = customActivityId;
  }
}
