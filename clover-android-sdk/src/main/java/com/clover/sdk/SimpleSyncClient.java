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
package com.clover.sdk;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

public abstract class SimpleSyncClient {
  public static final String METHOD_GET = "get";
  public static final String METHOD_PUT = "put";

  public static final String EXTRA_DATA = "data";

  protected final Context context;

  public SimpleSyncClient(Context context) {
    this.context = context;
  }

  public byte[] getData() {
    Bundle result = context.getContentResolver().call(getAuthorityUri(), METHOD_GET, null, null);
    if (result == null) {
      return null;
    }
    return result.getByteArray("data");
  }

  protected abstract String getAuthority();

  protected Uri getAuthorityUri() {
    return Uri.parse("content://" + getAuthority());
  }
}
