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
package com.clover.sdk.v1.printer;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.clover.sdk.JSONifiable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * This is a replacement for the {@link Type} class. Instances of this class may be obtained via
 * the {@link PrinterConnector#getPrinterTypeDetails(Printer)} method. The
 * {@link #fromCursor(Cursor)} method may also be used to read an instance passed via JSON in
 * Cursor rows from the column {@link PrinterContract.Devices#TYPE_DETAILS}.
 */
public class TypeDetails implements Parcelable, JSONifiable {

  private final String typeName;
  private final String model;
  private final List<Category> supportedCategories;
  private final boolean local;
  private final int numDotsWidth;
  private final int numCashDrawersSupported;

  public TypeDetails(String typeName, String model, List<Category> supportedCategories,
                     int numDotsWidth, boolean local, int numCashDrawersSupported) {
    if (TextUtils.isEmpty(typeName) || TextUtils.isEmpty(model) || supportedCategories == null
        || supportedCategories.size() == 0) {
      throw new IllegalArgumentException();
    }

    this.typeName = typeName;
    this.model = model;
    this.supportedCategories = Collections.unmodifiableList(supportedCategories);
    this.numDotsWidth = numDotsWidth;
    this.local = local;
    this.numCashDrawersSupported = numCashDrawersSupported;
  }

  /**
   * A unique string that identifies this type of printer (matches {@link Type#name()}).
   */
  public String getTypeName() {
    return typeName;
  }

  /**
   * A human readable model name for this printer.
   */
  public String getModel() {
    return model;
  }

  /**
   * A list of the categories this printer supports.
   */
  public List<Category> getSupportedCategories() {
    return supportedCategories;
  }

  /**
   * If this printer can print bitmaps this is number of dots per that the printer is capable
   * of printing. If the printer cannot print bitmaps this returns 0.
   */
  public int getNumDotsWidth() {
    return numDotsWidth;
  }

  /**
   * True if this printer is physically attached in some way as opposed to being connected via
   * network or other wireless interface.
   */
  public boolean isLocal() {
    return local;
  }

  /**
   * Returns the number of cash drawers that this type of printer can provide.
   */
  public int getNumCashDrawersSupported() {
    return numCashDrawersSupported;
  }

  /**
   * Searches for a {@link PrinterContract.Devices#TYPE_DETAILS} column in the given cursor and
   * extracts a TypeDetails instance from it. Returns null if the column cannot be found.
   */
  public static TypeDetails fromCursor(Cursor cursor) {
    int index;

    index = cursor.getColumnIndex(PrinterContract.Devices.TYPE_DETAILS);
    if (index != -1) {
      String s = cursor.getString(index);
      try {
        return JSON_CREATOR.create(new JSONObject(s));
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }

    return null;
  }

  private static final String KEY_TYPE_NAME = "typeName";
  private static final String KEY_MODEL = "model";
  private static final String KEY_SUPPORTED_CATS = "supportedCats";
  private static final String KEY_NUM_DOTS_WIDTH = "numDotsWidth";
  private static final String KEY_LOCAL = "local";
  private static final String KEY_NUM_CASH_DRAWERS = "numCashDrawers";

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(final Parcel dest, final int flags) {
    ArrayList<String> categoryStrings = new ArrayList<>();
    for (Category category : supportedCategories) {
      categoryStrings.add(category.name());
    }

    Bundle b = new Bundle();
    b.putString(KEY_TYPE_NAME, typeName);
    b.putString(KEY_MODEL, model);
    b.putStringArrayList(KEY_SUPPORTED_CATS, categoryStrings);
    b.putInt(KEY_NUM_DOTS_WIDTH, numDotsWidth);
    b.putBoolean(KEY_LOCAL, local);
    b.putInt(KEY_NUM_CASH_DRAWERS, numCashDrawersSupported);
    dest.writeBundle(b);
  }

  public static final Parcelable.Creator<TypeDetails> CREATOR = new Parcelable.Creator<TypeDetails>() {
    @Override
    public TypeDetails createFromParcel(final Parcel source) {
      Bundle b = source.readBundle(getClass().getClassLoader());

      String typeName = b.getString(KEY_TYPE_NAME);
      String model = b.getString(KEY_MODEL);
      ArrayList<String> categoryStrings = b.getStringArrayList(KEY_SUPPORTED_CATS);
      int numDotsWidth = b.getInt(KEY_NUM_DOTS_WIDTH);
      boolean local = b.getBoolean(KEY_LOCAL);
      int numCashDrawersSupported = b.getInt(KEY_NUM_CASH_DRAWERS);

      List<Category> categories = new ArrayList<>();
      if (categoryStrings != null) {
        for (String categoryString : categoryStrings) {
          try {
            categories.add(Category.valueOf(categoryString));
          } catch (IllegalArgumentException e) {
            e.printStackTrace();
            // skip unknown category
          }
        }
      }

      return new TypeDetails(typeName, model, categories, numDotsWidth, local, numCashDrawersSupported);
    }

    @Override
    public TypeDetails[] newArray(final int size) {
      return new TypeDetails[size];
    }
  };

  public static final JSONifiable.Creator<TypeDetails> JSON_CREATOR = new JSONifiable.Creator<TypeDetails>() {

    @Override
    public TypeDetails create(JSONObject source) {

      try {
        String typeName = source.getString(KEY_TYPE_NAME);
        String model = source.getString(KEY_MODEL);

        List<Category> supportedCategories = new ArrayList<>();
        JSONArray categoryArray = source.getJSONArray(KEY_SUPPORTED_CATS);
        for (int i = 0; i < categoryArray.length(); i++) {
          try {
            supportedCategories.add(Category.valueOf(categoryArray.getString(i)));
          } catch (IllegalArgumentException e) {
            // Unknown category, skip it
          }
        }

        int numDotsWidth = source.getInt(KEY_NUM_DOTS_WIDTH);
        boolean local = source.getBoolean(KEY_LOCAL);
        int numCashDrawersSupported = source.getInt(KEY_NUM_CASH_DRAWERS);

        return new TypeDetails(typeName, model, supportedCategories, numDotsWidth, local, numCashDrawersSupported);
      } catch (JSONException e) {
        e.printStackTrace();
        return null;
      }
    }
  };

  @Override
  public JSONObject getJSONObject() {
    JSONObject obj = new JSONObject();

    try {
      obj.put(KEY_TYPE_NAME, typeName);
      obj.put(KEY_MODEL, model);
      JSONArray categoryArray = new JSONArray();
      obj.put(KEY_SUPPORTED_CATS, categoryArray);
      for (Category category : supportedCategories) {
        categoryArray.put(category.name());
      }
      obj.put(KEY_NUM_DOTS_WIDTH, numDotsWidth);
      obj.put(KEY_LOCAL, local);
      obj.put(KEY_NUM_CASH_DRAWERS, numCashDrawersSupported);
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }

    return obj;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TypeDetails that = (TypeDetails) o;

    if (local != that.local) return false;
    if (numDotsWidth != that.numDotsWidth) return false;
    if (numCashDrawersSupported != that.numCashDrawersSupported) return false;
    if (typeName != null ? !typeName.equals(that.typeName) : that.typeName != null) return false;
    if (model != null ? !model.equals(that.model) : that.model != null) return false;
    return supportedCategories != null ? supportedCategories.equals(that.supportedCategories) : that.supportedCategories == null;
  }

  @Override
  public int hashCode() {
    int result = typeName != null ? typeName.hashCode() : 0;
    result = 31 * result + (model != null ? model.hashCode() : 0);
    result = 31 * result + (supportedCategories != null ? supportedCategories.hashCode() : 0);
    result = 31 * result + (local ? 1 : 0);
    result = 31 * result + numDotsWidth;
    result = 31 * result + numCashDrawersSupported;
    return result;
  }

  @Override
  public String toString() {
    return "TypeDetails{" +
        "typeName='" + typeName + '\'' +
        ", model='" + model + '\'' +
        ", supportedCategories=" + supportedCategories +
        ", local=" + local +
        ", numDotsWidth=" + numDotsWidth +
        ", numCashDrawersSupported=" + numCashDrawersSupported +
        '}';
  }

}
