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

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.clover.core.internal.Objects;
import junit.framework.Assert;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents a notification sent to an app.
 */
public class AppNotification implements Parcelable {

  private static final String TAG = AppNotification.class.getSimpleName();

  /**
   * The maximum number of characters
   */
  public static final int MAX_PAYLOAD_LENGTH = 1024 * 4;

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

  public AppNotification(JSONObject json) throws JSONException {
    this.appEvent = json.getString("appEvent");
    this.payload = json.getString("payload");
  }

  public AppNotification(Intent intent) {
    Assert.assertEquals(AppNotificationIntent.ACTION_APP_NOTIFICATION, intent.getAction());
    appEvent = intent.getStringExtra(AppNotificationIntent.EXTRA_APP_EVENT);
    payload = intent.getStringExtra(AppNotificationIntent.EXTRA_PAYLOAD);
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this).maxLength(100).add("appEvent", appEvent).add("payload", payload).toString();
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

  // Parcelable implementation.
  public static final Parcelable.Creator<AppNotification> CREATOR = new Parcelable.Creator<AppNotification>() {
    @Override
    public AppNotification createFromParcel(Parcel parcel) {
      try {
        JSONObject json = new JSONObject(parcel.readString());
        return new AppNotification(json);
      } catch (JSONException e) {
        // This should not happen, since we are specifying the format of the parcel in writeToParcel().
        throw new RuntimeException("Unable to read JSON from parcel", e);
      }
    }

    @Override
    public AppNotification[] newArray(int i) {
      return new AppNotification[0];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(toJson().toString());
  }

  // Object methods.
  @Override
  public boolean equals(Object o) {
    if (o == null || !o.getClass().equals(getClass())) {
      return false;
    }
    AppNotification other = (AppNotification) o;
    return Objects.equal(appEvent, other.appEvent) && Objects.equal(payload, other.payload);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(appEvent, payload);
  }
}
