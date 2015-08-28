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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Handles an {@link com.clover.sdk.v1.app.AppNotificationIntent#ACTION_APP_NOTIFICATION} intent.
 */
abstract public class AppNotificationReceiver extends BroadcastReceiver {

  private Context context;

  /**
   * Create a new instance.
   */
  public AppNotificationReceiver() {
  }

  /**
   * Create a new instance and register it with the given context.
   *
   * @see #register(android.content.Context)
   */
  public AppNotificationReceiver(Context context) {
    this.context = context;
    register(context);
  }

  /**
   * Register this receiver with the given context.
   */
  public void register(Context context) {
    this.context = context;
    context.registerReceiver(this, new IntentFilter(AppNotificationIntent.ACTION_APP_NOTIFICATION));
  }

  /**
   * Unregister this receiver.  This method must be called from {@link android.app.Activity#onPause()}
   * or {@link android.app.Activity#onDestroy()}, to unregister the underlying {@link android.content.BroadcastReceiver}.
   */
  public void unregister() {
    context.unregisterReceiver(this);
  }

  @Override
  public final void onReceive(Context context, Intent intent) {
    AppNotificationReceiver.this.onReceive(context, new AppNotification(intent));
  }

  /**
   * Implement this method to respond to notifications.
   */
  abstract public void onReceive(Context context, AppNotification notification);
}
