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
package com.clover.sdk;

/**
 * An interface to get a org.json.JSONObject.
 * <p>
 * There are two copies of this file, one in clover-android-sdk and one in
 * schema-tool, please keep them in sync.
 */
public interface JSONifiable {

  public org.json.JSONObject getJSONObject();

  /**
   * An interface for a class with a factory method that creates an instance from a JSONObject.
   */
  public interface Creator<T> {

    default public Class<T> getCreatedClass() {
      return null;
    }

    public T create(org.json.JSONObject source);

  }

}