package com.clover.sdk.v3.connector;

import java.security.SecureRandom;

/*
 * Copyright (C) 2016 Clover Network, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public final class ExternalIdUtils {
  static final String alpha = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
  static SecureRandom secureRandom = new SecureRandom();

  private ExternalIdUtils() {
  }

  private static String genString( int length ){
    StringBuilder stringBuilder = new StringBuilder(length);
    for( int i = 0; i < length; i++ )
      stringBuilder.append( alpha.charAt( secureRandom.nextInt(alpha.length()) ) );
    return stringBuilder.toString();
  }

  public static String generateNewID() {
    return genString(32);
  }
}
