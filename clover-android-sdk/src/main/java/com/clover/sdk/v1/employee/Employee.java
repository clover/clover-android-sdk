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
import com.clover.sdk.v1.base.Reference;

@SuppressWarnings("all")
public class Employee implements android.os.Parcelable {
  protected String jsonString = null;
  protected org.json.JSONObject jsonObject = null;

  public Employee(String json, boolean ignore) {
    this.jsonString = json;
  }

  public Employee(org.json.JSONObject jsonObject) {
    this.jsonObject = jsonObject;
  }

  public Employee(java.lang.String name, java.lang.String nickname, java.lang.String customId, java.lang.String email, java.lang.String pin, AccountRole role, java.util.List<Reference> shifts) throws org.json.JSONException {
    if (name == null) {
      throw new IllegalArgumentException("'name' is required to be non-null");
    }
    setName(name);
    setNickname(nickname);
    setCustomId(customId);
    setEmail(email);
    setPin(pin);
    setRole(role);
    setShifts(shifts);
  }

  public Employee(java.lang.String id, java.lang.String name, java.lang.String nickname, java.lang.String customId, java.lang.String email, java.lang.String pin, AccountRole role, java.lang.Boolean isOwner, java.util.List<Reference> shifts) throws org.json.JSONException {
    if (name == null) {
      throw new IllegalArgumentException("'name' is required to be non-null");
    }
    setId(id);
    setName(name);
    setNickname(nickname);
    setCustomId(customId);
    setEmail(email);
    setPin(pin);
    setRole(role);
    setIsOwner(isOwner);
    setShifts(shifts);
  }

  public org.json.JSONObject getJSONObject() {
    try {
      if (jsonObject == null) {
        if (jsonString != null) {
          jsonObject = (org.json.JSONObject) new org.json.JSONTokener(jsonString).nextValue();
          jsonString = null; // null this so it will be recreated if jsonObject is modified
        } else {
          jsonObject = new org.json.JSONObject();
        }
      }
    } catch (org.json.JSONException e) {
      // print some kind of error
    }
    return jsonObject;
  }

  public void validate() throws org.json.JSONException {
    if (getName() == null) {
      throw new IllegalArgumentException("'name' is required to be non-null");
    }
    java.lang.String id = getId();
    if (id != null && id.length() > 13) {
      throw new IllegalArgumentException("Maximum string length exceeded for 'id'");
    }
    java.lang.String name = getName();
    if (name != null && name.length() > 127) {
      throw new IllegalArgumentException("Maximum string length exceeded for 'name'");
    }
  }

  /**
   * Unique identifier
   */
  public java.lang.String getId() {
    return getJSONObject().optString("id", null);
  }

  /**
   * Full name of the employee
   */
  public java.lang.String getName() {
    return getJSONObject().optString("name", null);
  }

  /**
   * Nickname of the employee (shows up on receipts)
   */
  public java.lang.String getNickname() {
    return getJSONObject().optString("nickname", null);
  }

  /**
   * Custom ID of the employee
   */
  public java.lang.String getCustomId() {
    return getJSONObject().optString("customId", null);
  }

  /**
   * Email of the employee (optional)
   */
  public java.lang.String getEmail() {
    return getJSONObject().optString("email", null);
  }

  /**
   * Employee PIN
   */
  public java.lang.String getPin() {
    return getJSONObject().optString("pin", null);
  }

  /**
   * Employee Role
   */
  public AccountRole getRole() {
    AccountRole[] enumValues = AccountRole.class.getEnumConstants();
    // from last to first, so that in case of duplicate values, first wins
    for (int i = enumValues.length; --i >= 0; ) {
      AccountRole e = enumValues[i];
      if (e.toString().equals(getJSONObject().optString("role"))) {
        return e;
      }
    }
    // throw exception or return null?
    return null;
  }

  /**
   * Returns true if this employee is the owner account for this merchant
   */
  public java.lang.Boolean getIsOwner() {
    return getJSONObject().optBoolean("isOwner");
  }

  /**
   * Reference to employee shifts
   */
  public java.util.List<Reference> getShifts() {
    java.util.List<Reference> itemList = null;
    if (getJSONObject().has("shifts")) {
      itemList = new java.util.ArrayList<Reference>();
      org.json.JSONArray itemArray = getJSONObject().optJSONArray("shifts");
      for (int i = 0; i < itemArray.length(); i++) {
        org.json.JSONObject obj = itemArray.optJSONObject(i);
        Reference item = new Reference(obj);
        itemList.add(item);
      }
    }
    return itemList;
  }


  /**
   * Checks whether the 'id' field has been set
   */
  public boolean hasId() {
    return getJSONObject().has("id");
  }

  /**
   * Checks whether the 'name' field has been set
   */
  public boolean hasName() {
    return getJSONObject().has("name");
  }

  /**
   * Checks whether the 'nickname' field has been set
   */
  public boolean hasNickname() {
    return getJSONObject().has("nickname");
  }

  /**
   * Checks whether the 'customId' field has been set
   */
  public boolean hasCustomId() {
    return getJSONObject().has("customId");
  }

  /**
   * Checks whether the 'email' field has been set
   */
  public boolean hasEmail() {
    return getJSONObject().has("email");
  }

  /**
   * Checks whether the 'pin' field has been set
   */
  public boolean hasPin() {
    return getJSONObject().has("pin");
  }

  /**
   * Checks whether the 'role' field has been set
   */
  public boolean hasRole() {
    return getJSONObject().has("role");
  }

  /**
   * Checks whether the 'isOwner' field has been set
   */
  public boolean hasIsOwner() {
    return getJSONObject().has("isOwner");
  }

  /**
   * Checks whether the 'shifts' field has been set
   */
  public boolean hasShifts() {
    return getJSONObject().has("shifts");
  }

  public void setId(java.lang.String id) throws org.json.JSONException {
    if (id != null && id.length() > 13) {
      throw new IllegalArgumentException("Maximum string length exceeded for 'id'");
    }
    getJSONObject().put("id", id);
  }

  public void setName(java.lang.String name) throws org.json.JSONException {
    if (name != null && name.length() > 127) {
      throw new IllegalArgumentException("Maximum string length exceeded for 'name'");
    }
    getJSONObject().put("name", name);
  }

  public void setNickname(java.lang.String nickname) throws org.json.JSONException {

    getJSONObject().put("nickname", nickname);
  }

  public void setCustomId(java.lang.String customId) throws org.json.JSONException {

    getJSONObject().put("customId", customId);
  }

  public void setEmail(java.lang.String email) throws org.json.JSONException {

    getJSONObject().put("email", email);
  }

  public void setPin(java.lang.String pin) throws org.json.JSONException {
    getJSONObject().put("pin", pin);
  }

  public void setRole(AccountRole role) throws org.json.JSONException {

    getJSONObject().put("role", role);
  }

  public void setIsOwner(java.lang.Boolean isOwner) throws org.json.JSONException {

    getJSONObject().put("isOwner", isOwner);
  }

  public void setShifts(java.util.List<Reference> shifts) throws org.json.JSONException {
    if (shifts != null) {
      org.json.JSONArray array = new org.json.JSONArray();
      for (Reference obj : shifts) {
        array.put(obj.getJSONObject());
      }
      getJSONObject().put("shifts", array);
    }
  }


  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(android.os.Parcel dest, int flags) {
    // Backward compatibility section
    Bundle data = new Bundle();
    data.putString("employeeId", getId());
    data.putString("employeeNickname", getNickname());
    data.putString("employeeName", getName());
    data.putString("employeeRole", getRole().toString());
    dest.writeBundle(data);
    // End backward compatibility
    String json = jsonString != null ? jsonString : getJSONObject().toString();
    dest.writeString(json);
  }

  public static final android.os.Parcelable.Creator<Employee> CREATOR = new android.os.Parcelable.Creator<Employee>() {
    public Employee createFromParcel(android.os.Parcel in) {
      Bundle ignore = in.readBundle();
      String json = in.readString();
      return new Employee(json, true);
    }

    public Employee[] newArray(int size) {
      return new Employee[size];
    }
  };

  public static class Builder {

    private java.lang.String name;
    private java.lang.String nickname;
    private java.lang.String customId;
    private java.lang.String email;
    private java.lang.String pin;
    private AccountRole role;
    private java.util.List<Reference> shifts;

    public Builder() {
    }

    public Builder name(java.lang.String name) {
      if (name != null && name.length() > 127) {
        throw new IllegalArgumentException("Maximum string length exceeded for 'name'");
      }
      this.name = name;
      return this;
    }

    public Builder nickname(java.lang.String nickname) {

      this.nickname = nickname;
      return this;
    }

    public Builder customId(java.lang.String customId) {

      this.customId = customId;
      return this;
    }

    public Builder email(java.lang.String email) {

      this.email = email;
      return this;
    }

    public Builder pin(java.lang.String pin) {
      this.pin = pin;
      return this;
    }

    public Builder role(AccountRole role) {

      this.role = role;
      return this;
    }

    public Builder shifts(java.util.List<Reference> shifts) {

      this.shifts = shifts;
      return this;
    }

    public Employee build() throws org.json.JSONException {
      return new Employee(name, nickname, customId, email, pin, role, shifts);
    }
  }

}
