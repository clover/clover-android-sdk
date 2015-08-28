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
package com.clover.sdk.v3.apps;

import android.os.Parcel;
import com.clover.sdk.v1.app.AppNotification;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.json.JSONObject;

public class AppNotificationTest extends TestCase {

  /**
   * Tests marshalling to and from JSON.
   */
  public void testJSON() throws Exception {
    String appEvent = "sale";
    String payload = "Sunday";
    AppNotification an = new AppNotification(appEvent, payload);

    JSONObject json = an.toJson();
    assertEquals(appEvent, json.getString("appEvent"));
    assertEquals(payload, json.getString("payload"));

    an = new AppNotification(json);
    assertEquals(appEvent, an.appEvent);
    assertEquals(payload, an.payload);
  }

  /**
   * Tests serializing to and from a {@code Parcel}.
   */
  public void testParcelable() throws Exception {
    AppNotification expected = new AppNotification("sale", "Sunday");
    Parcel p = Parcel.obtain();
    expected.writeToParcel(p, 0);

    p.setDataPosition(0);
    AppNotification actual = AppNotification.CREATOR.createFromParcel(p);
    Assert.assertEquals(expected, actual);
  }
}
