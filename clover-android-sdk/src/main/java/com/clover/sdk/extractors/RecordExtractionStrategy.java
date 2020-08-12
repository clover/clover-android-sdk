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
import com.clover.sdk.JSONifiable;

import java.util.HashMap;

/**
 * For Clover internal use only.
 * <p>
 * There are two copies of this file, one in clover-android-sdk and one in
 * schema-tool, please keep them in sync.
 */
public final class RecordExtractionStrategy extends ExtractionStrategy {

  private final JSONifiable.Creator creator;

  private RecordExtractionStrategy(JSONifiable.Creator creator) {
    this.creator = creator;
  }

  @Override
  public Object extractValue(GenericClient g, String name) {
    return g.extractRecord(name, creator);
  }

  public Class getRecordClass() {
    return creator.getCreatedClass();
  }

  private static final HashMap<JSONifiable.Creator, RecordExtractionStrategy> cache = new HashMap<>();

  public synchronized static RecordExtractionStrategy instance(JSONifiable.Creator creator) {
    RecordExtractionStrategy instance = cache.get(creator);
    if (instance == null) {
      instance = new RecordExtractionStrategy(creator);
      cache.put(creator, instance);
    }

    return instance;
  }

}