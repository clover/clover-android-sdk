/*
 * Copyright (C) 2016 Clover Network, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clover.sdk.internal.util;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;

/**
 * For internal use only.
 * <p>
 * @deprecated Use {@link UnstableContentResolverClient#call(String, String, Bundle, Bundle)} instead.
 */
@Deprecated
public class UnstableCallClient {

  private final UnstableContentResolverClient client;

  public UnstableCallClient(ContentResolver contentResolver, Uri uri) {
    client = new UnstableContentResolverClient(contentResolver, uri);
  }

  public Bundle call(String method, String arg, Bundle extras, Bundle defaultResult) {
    return client.call(method, arg, extras, defaultResult);
  }



}
