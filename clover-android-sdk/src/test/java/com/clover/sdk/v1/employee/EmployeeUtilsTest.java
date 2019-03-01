/**
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
package com.clover.sdk.v1.employee;

import com.clover.sdk.v3.employees.Employee;
import com.clover.sdk.v3.employees.EmployeeUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class EmployeeUtilsTest {

  @Test
  public void testDisplayName() throws org.json.JSONException {
    // Test that display name uses primary 'name' attribute when nickname is not set
    Employee employee = new Employee();
    employee.setName("Name 1");
    String displayName = EmployeeUtils.getDisplayName(employee);
    assertEquals(displayName, "Name 1");

    // Test that display name uses 'nickname' attribute when nickname is set
    employee = new Employee();
    employee.setName("Nickname 2");
    displayName = EmployeeUtils.getDisplayName(employee);
    assertEquals(displayName, "Nickname 2");

    // Test that display name equals null when employee is null
    displayName = EmployeeUtils.getDisplayName(null);
    assertEquals(displayName, null);
  }
}
