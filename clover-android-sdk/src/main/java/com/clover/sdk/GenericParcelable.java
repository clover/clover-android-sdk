package com.clover.sdk;

import android.os.Parcelable;

/**
 * Created by michaelhampton on 11/8/16.
 */
public abstract class GenericParcelable implements Parcelable {

  /**
   * Gets a Bundle which can be used to get and set data attached to this instance. The attached Bundle will be
   * parcelled but not jsonified.
   */
  public final android.os.Bundle getBundle() {
    return getGenericClient().getBundle();
  }

  @Override
  public final String toString() {
    return getGenericClient().toString();
  }

  @Override
  public final int describeContents() {
    return 0;
  }

  @Override
  public final void writeToParcel(android.os.Parcel dest, int flags) {
    getGenericClient().writeToParcel(dest, flags);
  }

  protected abstract GenericClient getGenericClient();
}
