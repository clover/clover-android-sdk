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
package com.clover.sdk.v1.base;


@SuppressWarnings("all")
public class Reference implements android.os.Parcelable {

  protected String jsonString = null;
  protected org.json.JSONObject jsonObject = null;

  public Reference(String json, boolean ignore) {
    this.jsonString = json;
  }

  public Reference(org.json.JSONObject jsonObject) {
    this.jsonObject = jsonObject;
  }

  public Reference(java.lang.Long element) throws org.json.JSONException {
    setElement(element);
  }

  public Reference(java.lang.String id, java.lang.String href, java.lang.Long element) throws org.json.JSONException {
    setId(id);
    setHref(href);
    setElement(element);
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


  /**
   * Unique identifier
   */
  public java.lang.String getId() {
    return getJSONObject().optString("id");
  }

  public java.lang.String getHref() {
    return getJSONObject().optString("href");
  }

  /**
   * Reference Object
   */
  public java.lang.Long getElement() {
    return getJSONObject().optLong("element");
  }


  /**
   * Checks whether the 'id' field has been set
   */
  public boolean hasId() {
    return getJSONObject().has("id");
  }

  /**
   * Checks whether the 'href' field has been set
   */
  public boolean hasHref() {
    return getJSONObject().has("href");
  }

  /**
   * Checks whether the 'element' field has been set
   */
  public boolean hasElement() {
    return getJSONObject().has("element");
  }

  public void setId(java.lang.String id) throws org.json.JSONException {
    if (id != null && id.length() > 13) {
      throw new IllegalArgumentException("Maximum string length exceeded for 'id'");
    }
    getJSONObject().put("id", id);
  }

  public void setHref(java.lang.String href) throws org.json.JSONException {

    getJSONObject().put("href", href);
  }

  public void setElement(java.lang.Long element) throws org.json.JSONException {

    getJSONObject().put("element", element);
  }


  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(android.os.Parcel dest, int flags) {
    String json = jsonString != null ? jsonString : getJSONObject().toString();
    dest.writeString(json);
  }

  public static final android.os.Parcelable.Creator<Reference> CREATOR = new android.os.Parcelable.Creator<Reference>() {
    public Reference createFromParcel(android.os.Parcel in) {
      String json = in.readString();
      return new Reference(json, true);
    }

    public Reference[] newArray(int size) {
      return new Reference[size];
    }
  };

  public static class Builder {

    private java.lang.Long element;

    public Builder() {
    }

    public Builder element(java.lang.Long element) {

      this.element = element;
      return this;
    }

    public Reference build() throws org.json.JSONException {
      return new Reference(element);
    }
  }

}
