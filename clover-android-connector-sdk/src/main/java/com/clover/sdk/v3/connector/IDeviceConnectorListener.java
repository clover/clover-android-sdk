package com.clover.sdk.v3.connector;

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
