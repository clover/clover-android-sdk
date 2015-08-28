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
package com.clover.sdk.v3.employees;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.clover.sdk.internal.util.UnstableCallClient;
import com.clover.sdk.v1.Intents;

public class Roles {
  public static final String METHOD_INSERT_PERMISSION_SET = "insertPermissionSet";
  public static final String METHOD_DELETE_PERMISSION_SET = "deletePermissionSet";
  public static final String METHOD_IS_PERMISSION_ALLOWED = "isPermissionAllowed";
  public static final String METHOD_SET_ROLE = "setRole";
  public static final String METHOD_UNSET_ROLE = "unsetRole";
  public static final String METHOD_IS_PERMISSION_EXISTS = "isPermissionExists";

  public static final String ARG_ACCOUNT = "account";
  public static final String ARG_PERMISSION_SET = "permissionSet";
  public static final String ARG_ROLE_ID = "roleId";
  public static final String ARG_EMPLOYEE_ID = "employeeId";
  public static final String ARG_MINIMUM_ROLE = "minimumRole";
  public static final String ARG_PERMISSION_SET_ID = "permissionSetId";
  public static final String ARG_PERMISSION_SET_NAME = "permissionSetName";
  public static final String ARG_PACKAGE_NAME = "packageName";

  public static final String RETURN_IS_PERMISSION_ALLOWED = "isPermissionAllowed";
  public static final String RETURN_IS_PERMISSION_EXISTS = "isPermissionExists";

  public static final String PERMISSION_ACCESS = "_ACCESS";

  private final Context context;
  private final Account account;

  public Roles(Context context, Account account) {
    this.context = context;
    this.account = account;
  }

  public void insertPermissionSet(PermissionSet permissionSet, String roleId) {
    Bundle extras = new Bundle();
    extras.putParcelable(ARG_ACCOUNT, account);
    extras.putParcelable(ARG_PERMISSION_SET, permissionSet);
    extras.putString(ARG_ROLE_ID, roleId);

    new UnstableCallClient(context.getContentResolver(), RolesContract.PermissionSets.CONTENT_URI).call(METHOD_INSERT_PERMISSION_SET, null, extras, null);
  }

  public void deletePermissionSet(String permissionSetId) {
    Bundle extras = new Bundle();
    extras.putParcelable(ARG_ACCOUNT, account);
    extras.putString(ARG_PERMISSION_SET_ID, permissionSetId);

    new UnstableCallClient(context.getContentResolver(), RolesContract.PermissionSets.CONTENT_URI).call(METHOD_DELETE_PERMISSION_SET, null, extras, null);
  }

  public boolean isPermissionAllowed(String permissionSetName) {
    return isPermissionAllowed(permissionSetName, null);
  }

  public boolean isPermissionAllowed(String permissionSetName, String packageName, String employeeId) {
    return isPermissionAllowed(permissionSetName, packageName, employeeId, null);
  }

  /**
   *
   * @param permissionSetName permission name
   * @param packageName package name of the app
   * @param employeeId  employee id to verify the role
   * @param minimumRole minimum required role to allow access if the permissionSetName is not found
   * @return  if the employee with employee id is allowed for this permission
   */
  public boolean isPermissionAllowed(String permissionSetName, String packageName, String employeeId, AccountRole minimumRole) {
    Bundle extras = new Bundle();
    extras.putParcelable(ARG_ACCOUNT, account);
    extras.putString(ARG_PERMISSION_SET_NAME, permissionSetName);
    if (packageName != null) {
      extras.putString(ARG_PACKAGE_NAME, packageName);
    }
    if (employeeId != null && !employeeId.isEmpty()) {
      extras.putString(ARG_EMPLOYEE_ID, employeeId);
    }
    if (minimumRole != null) {
      extras.putString(ARG_MINIMUM_ROLE, minimumRole.name());
    }

    Bundle defaultResult = new Bundle();
    defaultResult.putBoolean(RETURN_IS_PERMISSION_ALLOWED, false);

    Bundle result = new UnstableCallClient(context.getContentResolver(), RolesContract.PermissionSets.CONTENT_URI).call(METHOD_IS_PERMISSION_ALLOWED, null, extras, defaultResult);
    return result.getBoolean(RETURN_IS_PERMISSION_ALLOWED);
  }

  public boolean isPermissionAllowed(String permissionSetName, String packageName) {
    return isPermissionAllowed(permissionSetName, packageName, null);
  }

  public boolean isPermissionExists(String permissionSetName) {
    return isPermissionExists(permissionSetName, null);
  }

  public boolean isPermissionExists(String permissionSetName, String packageName) {
    Bundle extras = new Bundle();
    extras.putParcelable(ARG_ACCOUNT, account);
    extras.putString(ARG_PERMISSION_SET_NAME, permissionSetName);
    if (packageName != null) {
      extras.putString(ARG_PACKAGE_NAME, packageName);
    }

    Bundle defaultResult = new Bundle();
    defaultResult.putBoolean(RETURN_IS_PERMISSION_EXISTS, false);

    Bundle result = new UnstableCallClient(context.getContentResolver(), RolesContract.PermissionSets.CONTENT_URI).call(METHOD_IS_PERMISSION_EXISTS, null, extras, defaultResult);
    return result.getBoolean(RETURN_IS_PERMISSION_EXISTS);
  }

  public void setRole(String permissionSetId, String roleId) {
    Bundle extras = new Bundle();
    extras.putParcelable(ARG_ACCOUNT, account);
    extras.putString(ARG_PERMISSION_SET_ID, permissionSetId);
    extras.putString(ARG_ROLE_ID, roleId);

    new UnstableCallClient(context.getContentResolver(), RolesContract.PermissionSets.CONTENT_URI).call(METHOD_SET_ROLE, null, extras, null);
  }

  public void unsetRole(String permissionSetId, String roleId) {
    Bundle extras = new Bundle();
    extras.putParcelable(ARG_ACCOUNT, account);
    extras.putString(ARG_PERMISSION_SET_ID, permissionSetId);
    extras.putString(ARG_ROLE_ID, roleId);

    new UnstableCallClient(context.getContentResolver(), RolesContract.PermissionSets.CONTENT_URI).call(METHOD_UNSET_ROLE, null, extras, null);
  }

  /**
   * Start activity to authenticate employee with requested permission.
   * See {@link com.clover.sdk.v1.Intents#ACTION_AUTHENTICATE_EMPLOYEE}
   *
   * @param activity
   * @param permission requested permission
   * @param requestCode see {@link Activity#startActivityForResult(android.content.Intent, int)}
   */
  public static void requestPermission(Activity activity, String permission, int requestCode) {
    Intent intent = new Intent(Intents.ACTION_AUTHENTICATE_EMPLOYEE);
    intent.putExtra(Intents.EXTRA_PERMISSIONS, permission);
    activity.startActivityForResult(intent, requestCode);
  }

}
