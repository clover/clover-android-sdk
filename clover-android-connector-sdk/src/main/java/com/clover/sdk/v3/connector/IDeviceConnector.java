package com.clover.sdk.v3.connector;

public interface IDeviceConnector {
  /**
   * Initialize the connector's connection. Must be called before calling any other method other than to add or remove listeners
   */
  void initializeConnection();

  /**
   * add an IDeviceConnectorListener to receive callbacks
   *
   * @param listener
   */
  void addCloverConnectorListener(IDeviceConnectorListener listener);

  /**
   * remove an IDeviceConnectorListener from receiving callbacks
   *
   * @param listener
   */
  void removeCloverConnectorListener(IDeviceConnectorListener listener);

  /**
   * Closes the connector's connection. Should be called after removing any listeners
   */
  void dispose();

}
