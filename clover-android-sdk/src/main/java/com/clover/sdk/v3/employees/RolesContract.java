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
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Pair;
import com.clover.sdk.v3.base.Reference;

public final class RolesContract {
  public static final String PARAM_ACCOUNT_NAME = "account_name";
  public static final String PARAM_ACCOUNT_TYPE = "account_type";

  public static final String AUTHORITY = Role.AUTHORITY;
  public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

  public interface RoleColumns {
    public static final String UUID = "id";
    public static final String NAME = "name";
    public static final String SYSTEM_ROLE = "system_role";
  }

  public static final class Roles implements BaseColumns, RoleColumns {
    public static final String CONTENT_DIRECTORY = "roles";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_DIRECTORY);
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/roles";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/roles";

    private Roles() {
    }

    public static Uri contentUriWithAccount(Account account) {
      Uri.Builder builder = CONTENT_URI.buildUpon();
      if (account != null) {
        builder.appendQueryParameter(PARAM_ACCOUNT_NAME, account.name);
        builder.appendQueryParameter(PARAM_ACCOUNT_TYPE, account.type);
      }
      return builder.build();
    }

    public static ContentValues toContentValues(Role role) {
      ContentValues values = new ContentValues();
      if (role.hasId()) {
        values.put(UUID, role.getId().toUpperCase());
      }
      if (role.hasName()) {
        values.put(NAME, role.getName());
      }
      if (role.hasSystemRole()) {
        values.put(SYSTEM_ROLE, role.getSystemRole().name());
      }

      return values;
    }

    public static Role fromCursor(Cursor c) {
      Role role = new Role();
      if (c.getColumnIndex(UUID) != -1) {
        role.setId(c.getString(c.getColumnIndex(UUID)));
      }
      if (c.getColumnIndex(NAME) != -1) {
        role.setName(c.getString(c.getColumnIndex(NAME)));
      }
      if (c.getColumnIndex(SYSTEM_ROLE) != -1) {
        role.setSystemRole(AccountRole.valueOf(c.getString(c.getColumnIndex(SYSTEM_ROLE))));
      }
      return role;
    }
  }

  public interface PermissionSetsColumns {
    public static final String UUID = "id";
    public static final String NAME = "name";
    public static final String LABEL = "label";
    public static final String APP_ID = "app_id";
    public static final String BITS = "bits";

    public static String ROLE_IDS = "(SELECT group_concat(" + RolesContract.Roles.CONTENT_DIRECTORY + "." + RolesContract.Roles.UUID + ",',') "
        + "FROM " + RolesContract.PermissionSetRoles.CONTENT_DIRECTORY + " "
        + "LEFT OUTER JOIN " + RolesContract.Roles.CONTENT_DIRECTORY + " "
        + "ON " + RolesContract.Roles.CONTENT_DIRECTORY + "." + RolesContract.Roles.UUID + "=" + RolesContract.PermissionSetRoles.CONTENT_DIRECTORY + "." + RolesContract.PermissionSetRoles.ROLE_ID + " "
        + "WHERE " + RolesContract.PermissionSets.CONTENT_DIRECTORY + "." + RolesContract.PermissionSets.UUID + "=" + RolesContract.PermissionSetRoles.CONTENT_DIRECTORY + "." + RolesContract.PermissionSetRoles.PERMISSION_SET_ID + ")";
    public static String ROLE_NAMES = "(SELECT group_concat(" + RolesContract.Roles.CONTENT_DIRECTORY + "." + RolesContract.Roles.NAME + ",',') "
        + "FROM " + RolesContract.PermissionSetRoles.CONTENT_DIRECTORY + " "
        + "LEFT OUTER JOIN " + RolesContract.Roles.CONTENT_DIRECTORY + " "
        + "ON " + RolesContract.Roles.CONTENT_DIRECTORY + "." + RolesContract.Roles.UUID + "=" + RolesContract.PermissionSetRoles.CONTENT_DIRECTORY + "." + RolesContract.PermissionSetRoles.ROLE_ID + " "
        + "WHERE " + RolesContract.PermissionSets.CONTENT_DIRECTORY + "." + RolesContract.PermissionSets.UUID + "=" + RolesContract.PermissionSetRoles.CONTENT_DIRECTORY + "." + RolesContract.PermissionSetRoles.PERMISSION_SET_ID + ")";
    public static String COUNTS_BY_APP = "(SELECT count(*) from " + RolesContract.PermissionSets.CONTENT_DIRECTORY + " AS inner "
        + "WHERE inner." + PermissionSets.APP_ID + "=" + PermissionSets.CONTENT_DIRECTORY + "." + PermissionSets.APP_ID + ")";
  }

  public static final class PermissionSets implements BaseColumns, PermissionSetsColumns {
    private PermissionSets() {
    }

    public static final String CONTENT_DIRECTORY = "permission_sets";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_DIRECTORY);
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.clover.permissions.permission_sets";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.clover.permissions.permission_sets";

    public static Uri contentUriWithAccount(Account account) {
      Uri.Builder builder = CONTENT_URI.buildUpon();
      if (account != null) {
        builder.appendQueryParameter(PARAM_ACCOUNT_NAME, account.name);
        builder.appendQueryParameter(PARAM_ACCOUNT_TYPE, account.type);
      }
      return builder.build();
    }

    public static ContentValues toContentValues(PermissionSet permissionSet) {
      ContentValues values = new ContentValues();
      if (permissionSet.hasId()) {
        values.put(UUID, permissionSet.getId().toUpperCase());
      }
      if (permissionSet.hasName()) {
        values.put(NAME, permissionSet.getName());
      }
      if (permissionSet.hasLabel()) {
        values.put(LABEL, permissionSet.getLabel());
      }
      if (permissionSet.hasApp()) {
        values.put(APP_ID, permissionSet.getApp().getId().toUpperCase());
      }
      if (permissionSet.hasPermissions()) {
        values.put(BITS, permissionSet.getPermissions().getBits());
      }
      return values;
    }

    public static PermissionSet fromCursor(Cursor c) {
      PermissionSet permissionSet = new PermissionSet();
      if (c.getColumnIndex(UUID) != -1) {
        permissionSet.setId(c.getString(c.getColumnIndex(UUID)));
      }
      if (c.getColumnIndex(NAME) != -1) {
        permissionSet.setName(c.getString(c.getColumnIndex(NAME)));
      }
      if (c.getColumnIndex(LABEL) != -1) {
        permissionSet.setLabel(c.getString(c.getColumnIndex(LABEL)));
      }
      if (c.getColumnIndex(BITS) != -1) {
        Permissions permissions = new Permissions();
        permissions.setBits(c.getLong(c.getColumnIndex(BITS)));
        permissionSet.setPermissions(permissions);
      }
      if (c.getColumnIndex(APP_ID) != -1) {
        permissionSet.setApp(toReference(c, APP_ID));
      }

      return permissionSet;
    }
  }

  public interface PermissionSetRolesColumns {
    public static final String UUID = "id";
    public static final String ROLE_ID = "role_id";
    public static final String PERMISSION_SET_ID = "permission_set_id";
  }

  public static final class PermissionSetRoles implements BaseColumns, PermissionSetRolesColumns {
    private PermissionSetRoles() {
    }

    public static final String CONTENT_DIRECTORY = "permission_set_roles";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_DIRECTORY);
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.clover.permissions.permission_set_roles";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.clover.permissions.permission_set_roles";

    public static Uri contentUriWithAccount(Account account) {
      Uri.Builder builder = CONTENT_URI.buildUpon();
      if (account != null) {
        builder.appendQueryParameter(PARAM_ACCOUNT_NAME, account.name);
        builder.appendQueryParameter(PARAM_ACCOUNT_TYPE, account.type);
      }
      return builder.build();
    }

    public static ContentValues toContentValues(PermissionSetRole permissionSetRole) {
      ContentValues values = new ContentValues();
      if (permissionSetRole.hasId()) {
        values.put(UUID, permissionSetRole.getId());
      }
      if (permissionSetRole.hasRole()) {
        values.put(ROLE_ID, permissionSetRole.getRole().getId().toUpperCase());
      }
      if (permissionSetRole.hasPermissionSet()) {
        values.put(PERMISSION_SET_ID, permissionSetRole.getPermissionSet().getId().toUpperCase());
      }
      return values;
    }

    public static PermissionSetRole fromCursor(Cursor c) {
      PermissionSetRole permissionSetRole = new PermissionSetRole();
      if (c.getColumnIndex(UUID) != -1) {
        permissionSetRole.setId(c.getString(c.getColumnIndex(UUID)));
      }
      if (c.getColumnIndex(ROLE_ID) != -1) {
        permissionSetRole.setRole(toReference(c, ROLE_ID));
      }
      if (c.getColumnIndex(PERMISSION_SET_ID) != -1) {
        permissionSetRole.setPermissionSet(toReference(c, PERMISSION_SET_ID));
      }

      return permissionSetRole;
    }
  }

  private static Reference toReference(Cursor c, String column) {
    Reference ref = new Reference();
    ref.setId(c.getString(c.getColumnIndex(column)));
    return ref;
  }

  public interface PermissionSetRolesJoinColumns extends BaseColumns {
    public static final String UUID = "id";

    public static final String PERMISSION_SET_ID = "permission_set_id";
    public static final String PERMISSION_SET_NAME = "permission_set_name";
    public static final String PERMISSION_SET_LABEL = "permission_set_label";
    public static final String PERMISSION_SET_APP_ID = "permission_set_app_id";
    public static final String PERMISSION_SET_BITS = "permission_set_bits";
    public static final String ROLE_ID = "role_id";
    public static final String ROLE_NAME = "role_name";
  }

  public static final class PermissionSetRolesJoin implements BaseColumns, PermissionSetRolesJoinColumns {
    private PermissionSetRolesJoin() {
    }

    public static final String CONTENT_DIRECTORY = "permission_set_roles_join";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_DIRECTORY);
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.clover.permissions.permission_set_roles_join";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.clover.permissions.permission_set_roles_join";

    public static Uri contentUriWithAccount(Account account) {
      Uri.Builder builder = CONTENT_URI.buildUpon();
      if (account != null) {
        builder.appendQueryParameter(PARAM_ACCOUNT_NAME, account.name);
        builder.appendQueryParameter(PARAM_ACCOUNT_TYPE, account.type);
      }
      return builder.build();
    }

    public static Pair<PermissionSet, Role> fromCursor(Cursor c) {
      PermissionSet permissionSet = new PermissionSet();
      if (c.getColumnIndex(PERMISSION_SET_ID) != -1) {
        permissionSet.setId(c.getString(c.getColumnIndex(PERMISSION_SET_ID)));
      }
      if (c.getColumnIndex(PERMISSION_SET_NAME) != -1) {
        permissionSet.setName(c.getString(c.getColumnIndex(PERMISSION_SET_NAME)));
      }
      if (c.getColumnIndex(PERMISSION_SET_LABEL) != -1) {
        permissionSet.setName(c.getString(c.getColumnIndex(PERMISSION_SET_LABEL)));
      }
      if (c.getColumnIndex(PERMISSION_SET_BITS) != -1) {
        Permissions permissions = new Permissions();
        permissions.setBits(c.getLong(c.getColumnIndex(PERMISSION_SET_BITS)));
        permissionSet.setPermissions(permissions);
      }
      if (c.getColumnIndex(PERMISSION_SET_APP_ID) != -1) {
        permissionSet.setApp(toReference(c, PERMISSION_SET_APP_ID));
      }

      Role role = new Role();
      if (c.getColumnIndex(ROLE_ID) != -1) {
        role.setId(c.getString(c.getColumnIndex(ROLE_ID)));
      }
      if (c.getColumnIndex(ROLE_NAME) != -1) {
        role.setName(c.getString(c.getColumnIndex(ROLE_NAME)));
      }

      return new Pair<PermissionSet, Role>(permissionSet, role);
    }
  }

}
