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
package com.clover.sdk.v1.merchant;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A class representing a tip suggestion associated with a merchant. Instances of this object are returned
 * as part of a {@link com.clover.sdk.v1.merchant.Merchant} object.
 */
public class TipSuggestion implements Parcelable {
  public static class Builder {
    private String id = null;
    private String name = null;
    private long percentage = 0;
    private boolean enabled = false;

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder percentage(long percentage) {
      this.percentage = percentage;
      return this;
    }

    public Builder enabled(boolean enabled) {
      this.enabled = enabled;
      return this;
    }

    public TipSuggestion build() {
      return new TipSuggestion(id, name, percentage, enabled);
    }
  }

  private final JSONObject data;

  private TipSuggestion(String id, String name, long percentage, boolean enabled) {
    data = new JSONObject();
    try {
      data.put("id", id);
      data.put("name", name);
      data.put("percentage", percentage);
      data.put("enabled", enabled);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  TipSuggestion(String json) throws JSONException {
    this.data = new JSONObject(json);
  }

  TipSuggestion(Parcel in) throws JSONException {
    String json = in.readString();
    this.data = new JSONObject(json);
  }

  public TipSuggestion(JSONObject data) {
    this.data = data;
  }

  public String getId() {
    return data.optString("id", null);
  }

  public String getName() {
    return data.optString("name", null);
  }


  public long getPercentage() {
    return data.optLong("percentage", 0);
  }


  public boolean isEnabled() {
    return data.optBoolean("enabled", false);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    String json = data.toString();
    dest.writeString(json);
  }

  public static final Creator<TipSuggestion> CREATOR = new Creator<TipSuggestion>() {
    public TipSuggestion createFromParcel(Parcel in) {
      try {
        return new TipSuggestion(in);
      } catch (JSONException e) {
        throw new RuntimeException(e);
      }
    }

    public TipSuggestion[] newArray(int size) {
      return new TipSuggestion[size];
    }
  };
}