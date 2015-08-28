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
package com.clover.sdk.internal.util;

import android.annotation.TargetApi;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;

public class UnstableCallClient {
  private static final String TAG = "UnstableCallClient";

  private static final int MAX_RETRY_ATTEMPTS = 5;
  private static final long RETRY_DELAY_MS = 300;

  private final ContentResolver contentResolver;
  private final Uri uri;

  public UnstableCallClient(ContentResolver contentResolver, Uri uri) {
    this.contentResolver = contentResolver;
    this.uri = uri;
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
  public Bundle call(String method, String arg, Bundle extras, Bundle defaultResult) {
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
      //Log.d(TAG, "ContentProviderClient.call() not supported, using ContentResolver.call()");
      return contentResolver.call(uri, method, arg, extras);
    }

    int retryCount = 0;
    ContentProviderClient client = null;

    while (retryCount < MAX_RETRY_ATTEMPTS) {
      retryCount++;
      //Log.d(TAG, "retryCount: " + retryCount);
      try {
        if (client == null) {
          //Log.d(TAG, "attempting to obtain new client ...");
          client = contentResolver.acquireUnstableContentProviderClient(uri);
          if (client == null) {
            //Log.d(TAG, "failed to obtain new client, sleeping ...");
            SystemClock.sleep(RETRY_DELAY_MS);
            continue;
          }
          //Log.d(TAG, "calling ...");
          Bundle result = client.call(method, arg, extras);
          //Log.d(TAG, "call finished successfully");
          return result;
        }
      } catch (RemoteException e) {
        //Log.d(TAG, "got exception, sleeping ...", e);
        SystemClock.sleep(RETRY_DELAY_MS);
      } catch (Exception e) {
        //Log.d(TAG, "got exception, failing", e);
        break;
      } finally {
        if (client != null) {
          //Log.d(TAG, "releasing client ...");
          client.release();
          client = null;
          //Log.d(TAG, "done releasing client");
        }
      }
    }

    return defaultResult;
  }
}
