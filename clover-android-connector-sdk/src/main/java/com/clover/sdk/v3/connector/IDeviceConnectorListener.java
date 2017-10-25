package com.clover.sdk.v3.connector;

/**
 * Created by glennbedwell on 7/14/17.
 */
public interface IDeviceConnectorListener {
  /**
   * Called when the Clover device is disconnected
   */
  void onDeviceDisconnected();

  /**
   * Called when the Clover device is connected, but not ready to communicate
   */
  void onDeviceConnected();
}
