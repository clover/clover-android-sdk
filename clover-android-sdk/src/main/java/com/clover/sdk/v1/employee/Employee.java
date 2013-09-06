/*
 * Copyright (C) 2013 Clover Network, Inc.
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

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * A class representing an employee.
 */
public class Employee implements Parcelable {
  private static final String KEY_ID = "employeeId";
  private static final String KEY_NICKNAME = "employeeNickname";
  private static final String KEY_NAME = "employeeName";
  private static final String KEY_ROLE = "employeeRole";

  /**
   * Employee roles.
   *
   * @see #getRole()
   */
  public static enum Role {ADMIN, EMPLOYEE, MANAGER};

  private final Bundle data;

  public Employee(Bundle data) {
    this.data = data;
  }

  public Employee(Parcel in) {
    this.data = in.readBundle();
  }

  /**
   * Get the employee ID.
   */
  public String getId() {
    return data.getString(KEY_ID, null);
  }

  /**
   * Get the employee nick name.
   */
  public String getNickname() {
    return data.getString(KEY_NICKNAME, null);
  }

  /**
   * Get the employee name.
   */
  public String getName() {
    return data.getString(KEY_NAME, null);
  }

  /**
   * Get the employee display name.
   */
  public String getDisplayName() {
    String str = getNickname();
    if (TextUtils.isEmpty(str)) {
      str = getName();
    }
    return str;
  }

  /**
   * Get the employee role.
   */
  public Employee.Role getRole() {
    String str = data.getString(KEY_ROLE, "EMPLOYEE");
    if (!TextUtils.isEmpty(str)) {
      return Employee.Role.valueOf(str);
    }
    return Employee.Role.EMPLOYEE;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeBundle(data);
  }

  public static final Parcelable.Creator<Employee> CREATOR = new Parcelable.Creator<Employee>() {
    public Employee createFromParcel(Parcel in) {
      return new Employee(in);
    }

    public Employee[] newArray(int size) {
      return new Employee[size];
    }
  };

  @Override
  public String toString() {
    return String.format("%s{id=%s, name=%s, nickName=%s, role=%s}", getClass().getSimpleName(), getId(), getName(), getNickname(), getRole());
  }
}
