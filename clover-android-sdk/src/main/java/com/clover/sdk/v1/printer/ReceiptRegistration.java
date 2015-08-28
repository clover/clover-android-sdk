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

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class ReceiptRegistration implements Parcelable {
  public static class Builder {
    private String pkg = null;
    private Uri uri = null;

    public Builder pkg(String pkg) {
      this.pkg = pkg;
      return this;
    }

    public Builder uri(Uri uri) {
      this.uri = uri;
      return this;
    }

    public Builder uri(String u) {
      this.uri = uri.parse(u);
      return this;
    }

    public ReceiptRegistration build() {
      return new ReceiptRegistration(pkg, uri);
    }
  }

  public final String pkg;
  public final Uri uri;

  private ReceiptRegistration(String pkg, Uri uri) {
    this.pkg = pkg;
    this.uri = uri;
  }

  private ReceiptRegistration(Parcel in) {
    this.pkg = in.readString();
    this.uri = in.readParcelable(null);
  }

  @Override
  public String toString() {
    return String.format("%s{pkg=%s, uri=%s}", getClass().getSimpleName(), pkg, uri);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ReceiptRegistration that = (ReceiptRegistration) o;

    if (!pkg.equals(that.pkg)) {
      return false;
    }
    if (!uri.equals(that.uri)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = pkg.hashCode();
    result = 31 * result + uri.hashCode();
    return result;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(pkg);
    parcel.writeParcelable(uri, 0);
  }

  public static final Creator<ReceiptRegistration> CREATOR = new Creator<ReceiptRegistration>() {
    public ReceiptRegistration createFromParcel(Parcel in) {
      return new ReceiptRegistration(in);
    }

    public ReceiptRegistration[] newArray(int size) {
      return new ReceiptRegistration[size];
    }
  };
}
