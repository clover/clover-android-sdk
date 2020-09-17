/*
 * Copyright (C) 2019 Clover Network, Inc.
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
package com.clover.sdk.extractors;

import com.clover.sdk.GenericClient;

import java.util.HashMap;

/**
 * For Clover internal use only.
 * <p>
 * There are two copies of this file, one in clover-android-sdk and one in
 * schema-tool, please keep them in sync.
 */
public final class BasicListExtractionStrategy extends ExtractionStrategy {

  private final Class<?> clazz;

  private BasicListExtractionStrategy(Class<?> clazz) {
    this.clazz = clazz;
  }

  @Override
  public Object extractValue(GenericClient g, String name) {
    return g.extractListOther(name, clazz);
  }

  private static final HashMap<Class<?>, BasicListExtractionStrategy> cache = new HashMap<>();

  public synchronized static BasicListExtractionStrategy instance(Class<?> clazz) {
    BasicListExtractionStrategy instance = cache.get(clazz);
    if (instance == null) {
      instance = new BasicListExtractionStrategy(clazz);
      cache.put(clazz, instance);
    }

    return instance;
  }

}