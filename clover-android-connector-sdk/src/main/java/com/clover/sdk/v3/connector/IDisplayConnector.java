/**
 * Copyright (C) 2016 Clover Network, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clover.sdk.v3.connector;

import com.clover.sdk.v3.order.DisplayOrder;

/**
 * Interface into displays.  Used to allow direct control of
 * a single display, or possibly multiple displays (depending on instance).
 */
public interface IDisplayConnector extends IDeviceConnector{
  /**
   * Show the welcome display on the device.
   */
  void showWelcomeScreen();

  /**
   * Show a text message on the device.
   *
   * @param message a text message to display
   */
  void showMessage(String message);

  /**
   * Show the thank you display on the device.
   */
  void showThankYouScreen();

  /**
   * Display order information on the screen. Calls to this method will cause the DisplayOrder
   * to show on the clover device. If a DisplayOrder is already showing on the Clover device,
   * it will replace the existing DisplayOrder on the device.
   *
   * @param order a com.clover.sdk.v3.order.DisplayOrder
   */
  void showDisplayOrder(DisplayOrder order);

  /**
   * Destroy the connector.  After this is called, the connection to the displays is severed, and this object is
   * no longer usable
   */
  void dispose();
}
