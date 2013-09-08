/*
 * Copyright (C) 2013 Clover Network, Inc.
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

import android.content.Intent;
import android.util.Log;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents a notification sent to an app.
 */
public class AppNotification {

  private static final String TAG = AppNotification.class.getSimpleName();

  /**
   * The app-specific event name.
   */
  public final String appEvent;

  /**
   * Up to 4k bytes of data associated with the event.
   */
  public final String payload;

  public AppNotification(String appEvent, String payload) {
    this.appEvent = appEvent;
    this.payload = payload;
  }

  public AppNotification(Intent intent) {
    Assert.assertEquals(AppNotificationIntent.ACTION_APP_NOTIFICATION, intent.getAction());
    appEvent = intent.getStringExtra(AppNotificationIntent.EXTRA_APP_EVENT);
    payload = intent.getStringExtra(AppNotificationIntent.EXTRA_PAYLOAD);
  }

  @Override
  public String toString() {
    return AppNotification.class.getSimpleName() + ": {appEvent=" + appEvent + ", payload=" + payload + "}";
  }

  public JSONObject toJson() {
    try {
      JSONObject notification = new JSONObject();
      notification.put("appEvent", appEvent);
      notification.put("payload", payload);
      return notification;
    } catch (JSONException e) {
      Log.e(TAG, "Unexpected JSON error", e);
      return null;
    }
  }
}
