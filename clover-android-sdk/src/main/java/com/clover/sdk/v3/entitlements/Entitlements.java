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
package com.clover.sdk.v3.entitlements;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import com.clover.sdk.SimpleSyncClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Entitlements extends SimpleSyncClient {
  public static final String AUTHORITY = "com.clover.entitlements";
  public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

  public static final String METHOD_IS_ALLOWED = "isAllowed";

  public static final String EXTRA_ENTITLEMENT = "entitlement";
  public static final String EXTRA_ALLOWED = "allowed";

  public Entitlements(Context context) {
    super(context);
  }

  @Override
  protected String getAuthority() {
    return AUTHORITY;
  }

  public Map<String, MerchantGatewayEntitlement> getEntitlements() {
    byte[] data = getData();
    return parseEntitlements(data);
  }

  public boolean isAllowed(String entitlement) {
    Bundle extras = new Bundle();
    extras.putString(EXTRA_ENTITLEMENT, entitlement);
    Bundle result = context.getContentResolver().call(getAuthorityUri(), METHOD_IS_ALLOWED, null, extras);
    if (result == null) {
      return false;
    }
    return result.getBoolean(EXTRA_ALLOWED, false);
  }

  public static Map<String, MerchantGatewayEntitlement> parseEntitlements(byte[] data) {
    Map<String, MerchantGatewayEntitlement> mges = new HashMap<String, MerchantGatewayEntitlement>();
    if (data == null) {
      return mges;
    }
    String s = new String(data);

    try {
      JSONObject jsonObject = new JSONObject(s);
      JSONArray elements = jsonObject.getJSONArray("elements");
      for (int i = 0; i < elements.length(); i++) {
        JSONObject jo = elements.getJSONObject(i);
        MerchantGatewayEntitlement mge = new MerchantGatewayEntitlement(jo);
        mges.put(mge.getName(), mge);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return mges;
  }
}
