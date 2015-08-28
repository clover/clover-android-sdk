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

import android.content.Context;
import android.net.Uri;

import java.io.OutputStream;

public abstract class OutputUriFactory {
  private final Context mContext;

  public OutputUriFactory(Context context) {
    mContext = context;
  }

  /** Return a uri pointing to a new resource that provides an OutputStream */
  public abstract Uri createNewOutputUri();
  /** Return an OutputStream for the given uri that allows writing */
  public abstract OutputStream getOutputStreamForUri(Uri uri);

  public final Context getContext() {
    return mContext;
  }
}
