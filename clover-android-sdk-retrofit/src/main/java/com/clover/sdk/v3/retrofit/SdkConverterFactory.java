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
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * A {@link Converter.Factory} that facilitates conversion between OkHttp
 * {@link okhttp3.Response} and {@link JSONifiable} objects. To register with
 * {@link retrofit2.Retrofit}:
 * <p/>
 * <pre>
 * Retrofit retrofit = new Retrofit.Builder()
 *  ...
 *  .addConverterFactory(new SdkConverterFactory())
 *  .build();
 * </pre>
 */
public class SdkConverterFactory extends Converter.Factory {
  @Override
  public Converter<ResponseBody, ?> responseBodyConverter(@NonNull Type type, @NonNull Annotation[] annotations, @NonNull Retrofit retrofit) {
    assertAssignableFrom(type);
    return new JSONifiableResponseConverter((Class<? extends JSONifiable>) type);  }

  @Override
  public Converter<?, RequestBody> requestBodyConverter(@NonNull Type type, @NonNull Annotation[] parameterAnnotations, @NonNull Annotation[] methodAnnotations, @NonNull Retrofit retrofit) {
    assertAssignableFrom(type);
    return new JSONifiableRequestConverter((Class<? extends JSONifiable>) type);
  }

  private void assertAssignableFrom(Type type) {
    if (!(type instanceof Class)) {
      throw new IllegalArgumentException("Type: " + type + " is not an instance of: " + Class.class);
    }
    if (!JSONifiable.class.isAssignableFrom((Class<?>) type)) {
      throw new IllegalArgumentException("Type: " + type + " is not assignable from: " + JSONifiable.class);
    }
  }
}
