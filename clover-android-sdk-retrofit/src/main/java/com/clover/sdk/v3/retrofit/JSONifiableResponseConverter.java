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

import androidx.annotation.Nullable;
import okhttp3.ResponseBody;
import org.json.JSONObject;
import retrofit2.Converter;

import java.io.IOException;
import java.lang.reflect.Constructor;

class JSONifiableResponseConverter implements Converter<ResponseBody, JSONifiable> {
  final Class<? extends JSONifiable> jsonifiableClazz;

  public JSONifiableResponseConverter(Class<? extends JSONifiable> jsonifiableClazz) {
    this.jsonifiableClazz = jsonifiableClazz;
  }

  @Override
  @Nullable
  public JSONifiable convert(@Nullable ResponseBody value) throws IOException {
    if (value == null) {
      return null;
    }
    try {
      String json = value.string();
      if (json.isEmpty()) {
        final Constructor<? extends JSONifiable> c = jsonifiableClazz.getConstructor();
        return (JSONifiable) c.newInstance();
      }
      Constructor<? extends JSONifiable> c = jsonifiableClazz.getConstructor(JSONObject.class);
      return (JSONifiable) c.newInstance(new JSONObject(json));
    } catch (Exception e) {
      throw new IOException(e);
    }
  }
}
