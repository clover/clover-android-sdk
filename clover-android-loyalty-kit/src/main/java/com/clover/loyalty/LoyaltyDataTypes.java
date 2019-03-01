package com.clover.loyalty;
/*
 * Copyright (C) 2016 Clover Network, Inc.
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaelhampton on 6/8/18.
 */
public class LoyaltyDataTypes {

  // We have some types we define as standards
  public static final String VAS_TYPE = "VAS";
  public static final String EMAIL_TYPE = "EMAIL";
  public static final String PHONE_TYPE = "PHONE";
  public static final String CLEAR_TYPE = "CLEAR";

  public class VAS_TYPE_KEYS {
    public static final String PUSH_URL = "PUSH_URL";
    public static final String PROTOCOL_CONFIG = "PROTOCOL_CONFIG";
    public static final String PROTOCOL_ID = "PROTOCOL_ID";
    public static final String PROVIDER_PACKAGE = "PROVIDER_PACKAGE";
    public static final String PUSH_TITLE = "PUSH_TITLE";
    public static final String SUPPORTED_SERVICES = "SUPPORTED_SERVICES";
  }

  private static final List<String> dataTypes = new ArrayList<>(3);

  public static final boolean isSystemListedType(String type) {
    return (VAS_TYPE.equals(type)) ||(EMAIL_TYPE.equals(type)) ||(PHONE_TYPE.equals(type) || (CLEAR_TYPE.equals(type)));
  }

  public static final boolean isCustomListedType(String type) {
    return dataTypes.contains(type);
  }

  public static final boolean isListedType(String type) {
    return isSystemListedType(type) ||
           isCustomListedType(type);
  }

  public static final boolean addListedType(String type) {
    if (!isListedType(type)) {
      return dataTypes.add(type);
    }
    return false; // not added, already there
  }

  public static final boolean removeListedType(String type) {
    if (!isListedType(type)) {
      return dataTypes.remove(type);
    }
    return false; //not removed, not there
  }
}
