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
package com.clover.sdk.v1.printer;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class Printer implements Parcelable {

  public static class Builder {
    private String uuid = null;
    private Type type = null;
    private String name = null;
    private String ip = null;
    private String mac = null;

    private Category category = null;

    public Builder printer(Printer printer) {
      this.uuid = printer.uuid;
      this.type = printer.type;
      this.name = printer.name;
      this.ip = printer.ip;
      this.mac = printer.mac;

      this.category = printer.category;

      return this;
    }

    public Builder cursor(Cursor cursor) {
      int index;

      index = cursor.getColumnIndex(PrinterContract.DeviceCategories.UUID);
      if (index != -1) {
        uuid(cursor.getString(index));
      }
      if (uuid == null) {
        index = cursor.getColumnIndex(PrinterContract.DeviceCategories.DEVICE_UUID);
        if (index != -1) {
          uuid(cursor.getString(index));
        }
      }

      index = cursor.getColumnIndex(PrinterContract.DeviceCategories.TYPE);
      if (index != -1) {
        String t = cursor.getString(index);
        type(Type.valueOf(t));
      }

      index = cursor.getColumnIndex(PrinterContract.DeviceCategories.NAME);
      if (index != -1) {
        name(cursor.getString(index));
      }

      index = cursor.getColumnIndex(PrinterContract.DeviceCategories.MAC);
      if (index != -1) {
        mac(cursor.getString(index));
      }

      index = cursor.getColumnIndex(PrinterContract.DeviceCategories.IP);
      if (index != -1) {
        ip(cursor.getString(index));
      }

      index = cursor.getColumnIndex(PrinterContract.DeviceCategories.CATEGORY);
      if (index != -1) {
        String c = cursor.getString(index);
        category(Category.valueOf(c));
      }

      return this;
    }

    public Builder uuid(String uuid) {
      this.uuid = uuid;
      return this;
    }

    public Builder type(Type type) {
      this.type = type;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder ip(String ip) {
      this.ip = ip;
      return this;
    }

    public Builder mac(String mac) {
      this.mac = mac;
      return this;
    }

    public Builder category(Category category) {
      this.category = category;
      return this;
    }

    public Printer build() {
      return new Printer(uuid, type, name, mac, ip, category);
    }
  }

  public final String uuid;
  public final Type type;
  public final String name;
  public final String ip;
  public final String mac;

  public final Category category;

  private Printer(Parcel in) {
    uuid = in.readString();
    type = in.readParcelable(getClass().getClassLoader());
    name = in.readString();
    ip = in.readString();
    mac = in.readString();
    category = in.readParcelable(getClass().getClassLoader());
  }

  private Printer(String uuid, Type type, String name, String mac, String ip, Category category) {
    this.uuid = uuid;
    this.type = type;
    this.name = name;
    this.ip = ip;
    this.mac = mac;
    this.category = category;
  }

  public String getUuid() {
    return uuid;
  }

  public Type getType() {
    return type;
  }

  public String getName() {
    return name;
  }

  public String getIp() {
    return ip;
  }

  public String getMac() {
    return mac;
  }

  public Category getCategory() {
    return category;
  }

  @Override
  public boolean equals(Object o) {

    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Printer printer = (Printer) o;

    if (category != printer.category) {
      return false;
    }
    if (ip != null ? !ip.equals(printer.ip) : printer.ip != null) {
      return false;
    }
    if (mac != null ? !mac.equals(printer.mac) : printer.mac != null) {
      return false;
    }
    if (name != null ? !name.equals(printer.name) : printer.name != null) {
      return false;
    }
    if (type != printer.type) {
      return false;
    }
    if (uuid != null ? !uuid.equals(printer.uuid) : printer.uuid != null) {
      return false;
    }

    return true;
  }

  public boolean isLocal() {
    String mac = getMac();
    return TextUtils.isEmpty(mac);
  }

  /**
   * This is not the same as UUID, the returned value depends on the printer and is used to associate tags.
   */
  public String getUniqueId() {
    String uid = getMac();
    if (TextUtils.isEmpty(uid)) {
      uid = "MY_LOCAL"; // See android-common class com.clover.common.data.printer.PrinterType
    }
    return uid;
  }

  @Override
  public int hashCode() {
    int result = uuid != null ? uuid.hashCode() : 0;
    result = 31 * result + (type != null ? type.hashCode() : 0);
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (ip != null ? ip.hashCode() : 0);
    result = 31 * result + (mac != null ? mac.hashCode() : 0);
    result = 31 * result + (category != null ? category.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return String.format("%s{uuid=%s, type=%s, name=%s, mac=%s, ip=%s, category=%s}", getClass().getSimpleName(), uuid, type, name, mac, ip, category);
  }

  public static boolean equal(Object x, Object y) {
    return (x == null ? y == null : x.equals(y));
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    writeToParcel(parcel);
  }

  public void writeToParcel(Parcel out) {
    out.writeString(uuid);
    out.writeParcelable(type, 0);
    out.writeString(name);
    out.writeString(ip);
    out.writeString(mac);
    out.writeParcelable(category, 0);
  }

  public static final Creator<Printer> CREATOR = new Creator<Printer>() {
    public Printer createFromParcel(Parcel in) {
      return new Printer(in);
    }

    public Printer[] newArray(int size) {
      return new Printer[size];
    }
  };
}
