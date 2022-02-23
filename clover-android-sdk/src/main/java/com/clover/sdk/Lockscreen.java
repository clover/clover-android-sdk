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
package com.clover.sdk;

import com.clover.sdk.internal.util.UnstableContentResolverClient;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

/**
 * Lock and unlock a Clover device's lock screen. On Clover devices
 * lock and unlocking the screen is tied to logging out and logging in Clover employees.
 * <p/>
 * Please consider use of these methods carefully.
 * Clover devices' passcode login system provides security and auditing to
 * merchants. Misuse of this class can circumvent these systems. If you plan to use this class
 * for a production Clover application please post in
 * <a href="https://community.clover.com">community.clover.com</a>
 * to ensure your use case will pass Clover's approval process.
 * <p/>
 * Developers should prefer to unlock to the default employee if enabled by the merchant. This
 * can be handled transparently with the {@link #unlock()} method that will prefer the default
 * employee if enabled, and otherwise will unlock to the owner employee.
 * <p/>
 * To explicitly unlock to the default employee use {@link #unlockDefault()}. Note that this will
 * fail if the default employee is not enabled by the merchant.
 * <p/>
 * To check if the merchant has a default employee use the following code:
 * <pre>
 *   EmployeeConnector ec; // Obtain an instance in the usual way
 *   Employee defaultEmployee = ec.getEmployee(Lockscreen.DEFAULT_EMPLOYEE_ID);
 *   boolean isDefaultEmployeeEnabled = defaultEmployee != null;
 * </pre>
 * To unlock to a specific employee use {@link #unlock(String)}. To get a list of employees
 * and their UUIDs use {@link com.clover.sdk.v3.employees.EmployeeConnector#getEmployees()}.
 * <p/>
 * All lock / unlock methods return a boolean indicating if the operation was successful.
 * Attempting to unlock a device that is already unlocked will fail.
 * Attempting to lock a device that is already locked will fail.
 * <p/>
 * To understand if a Clover device is currently locked use
 * {@link @link com.clover.sdk.v3.employees.EmployeeConnector#getEmployee()}.
 * This will return either the currently logged in employee, or null
 * otherwise.
 * <p/>
 * To understand if the merchant has enabled the default employee, use
 * {@link com.clover.sdk.v3.employees.EmployeeConnector#getEmployee(String)} with and argument
 * of {@link #DEFAULT_EMPLOYEE_ID}.
 * <p/>
 * The behavior of these methods on non-Clover devices and Android emulators differs. On
 * non-Clover devices, Clover software does not control the lock screen. While the methods in this
 * class will logout or login Clover employees, they will not cause the device's lock screen
 * to show or hide.
 */
public class Lockscreen {

  /**
   * An ID for the default employee which is a employee that has no passcode.
   * Not all merchants enable the default employee.
   *
   * @see #unlockDefault()
   */
  public static final String DEFAULT_EMPLOYEE_ID = "DFLTEMPLOYEE";

  public static class Contract {
    public static final String AUTHORITY = "com.clover.lockscreen";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String METHOD_LOCK = "lockscreen_lock";
    public static final String METHOD_UNLOCK = "lockscreen_unlock";

    public static final String EXTRA_EMPLOYEE_ID = "lockscreen_employee_id";

    public static final String RESULT_LOCK = "result";
    public static final String RESULT_UNLOCK = "result";
  }

  private final Context context;

  public Lockscreen(Context context) {
    this.context = context;
  }

  /**
   * Lock the device.
   */
  public boolean lock() {
    try {
      Bundle r = new UnstableContentResolverClient(context.getContentResolver(), Contract.CONTENT_URI)
          .call(Contract.METHOD_LOCK, null, new Bundle(), null);
      if (r == null) {
        return false;
      }
      return r.getBoolean(Contract.RESULT_LOCK, false);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Unlock the device as the default employee. If the merchant does not have a default
   * employee, login as the owner employee.
   * <p/>
   * This is equivalent to calling {@link #unlock(String)} with a null employee ID.
   */
  public boolean unlock() {
    return unlock(null);
  }

  /**
   * Unlock the device as the default employee. This will return false if the merchant
   * has not enabled the default employee.
   */
  public boolean unlockDefault() {
    return unlock(DEFAULT_EMPLOYEE_ID);
  }

  /**
   * Unlock the device as the given employee UUID. This method returns false if the
   * provided employee ID does not match an employee for this merchant.
   */
  public boolean unlock(String employeeId) {
    Bundle extras = new Bundle();
    extras.putString(Contract.EXTRA_EMPLOYEE_ID, employeeId);
    try {
      Bundle r = new UnstableContentResolverClient(context.getContentResolver(), Contract.CONTENT_URI)
          .call(Contract.METHOD_UNLOCK, null, extras, null);
      if (r == null) {
        return false;
      }
      return r.getBoolean(Contract.RESULT_UNLOCK, false);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }
}
