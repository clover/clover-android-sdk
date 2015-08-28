/**
 * Copyright (C) 2015 Clover Network, Inc.
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
package com.clover.sdk.v1.app;

public class AppNotificationIntent {

  /**
   * Handled by apps that want to receive notifications.  This intent includes
   * the {@link #EXTRA_APP_EVENT} and {@link #EXTRA_PAYLOAD} extras.
   */
  public static final String ACTION_APP_NOTIFICATION = "com.clover.sdk.app.intent.action.APP_NOTIFICATION";

  /**
   * A required {@code String} extra sent with an {@link #ACTION_APP_NOTIFICATION} intent
   * that specifies the app-specific category of the event being sent.
   */
  public static final String EXTRA_APP_EVENT = "appEvent";

  /**
   * An optional {@code String} extra sent with an {@link #ACTION_APP_NOTIFICATION} intent
   * that stores up to 4k of data associated with an app notification.
   */
  public static final String EXTRA_PAYLOAD = "payload";
}
