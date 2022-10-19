/**
 * Copyright (C) 2022 Clover Network, Inc.
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
package com.clover.sdk.v3.retrofit;

import com.clover.sdk.JSONifiable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

class JSONifiableRequestConverter implements Converter<JSONifiable, RequestBody> {
  private final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

  final Class<? extends JSONifiable> jsonifiableClazz;

  public JSONifiableRequestConverter(@NonNull Class<? extends JSONifiable> jsonifiableClazz) {
    this.jsonifiableClazz = jsonifiableClazz;
  }

  @Override
  @Nullable
  public RequestBody convert(@Nullable JSONifiable value) {
    if (value == null) {
      return null;
    }
    if (value.getJSONObject() == null) {
      return null;
    }
    return RequestBody.create(MEDIA_TYPE, value.getJSONObject().toString());
  }
}
