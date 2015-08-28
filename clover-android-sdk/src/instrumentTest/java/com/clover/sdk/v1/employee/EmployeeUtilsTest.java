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
package com.clover.sdk.v1.employee;

import junit.framework.TestCase;

public class EmployeeUtilsTest extends TestCase {
  public void testDisplayName() throws org.json.JSONException {
    // Test that display name uses primary 'name' attribute when nickname is not set
    Employee employee = new Employee("Name 1", null, null, null, null, null, null);
    String displayName = EmployeeUtils.getDisplayName(employee);
    assertEquals(displayName, "Name 1");

    // Test that display name uses 'nickname' attribute when nickname is set
    employee = new Employee("Name 2", "Nickname 2", null, null, null, null, null);
    displayName = EmployeeUtils.getDisplayName(employee);
    assertEquals(displayName, "Nickname 2");

    // Test that display name equals null when employee is null
    displayName = EmployeeUtils.getDisplayName(null);
    assertEquals(displayName, null);
  }
}
