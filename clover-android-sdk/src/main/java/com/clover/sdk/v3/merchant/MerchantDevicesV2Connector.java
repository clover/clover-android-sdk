package com.clover.sdk.v3.merchant;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import com.clover.sdk.v1.CallConnector;
import com.clover.sdk.v3.device.Device;

/**
 * Access information about devices associated with this merchant. This class contains
 * convenience wrappers for accessing the data stored in the content provider at
 * URI {@link MerchantDevicesV2Contract.Device#CONTENT_URI}.
 * <p/>
 * As the total number of devices for a merchant can be large, use
 * {@link MerchantDevicesV2Contract.Device} to query for all (or a subset) of the
 * merchant's devices.
 */
public class MerchantDevicesV2Connector extends CallConnector {
  public MerchantDevicesV2Connector(Context context) {
    super(context, MerchantDevicesV2Contract.Device.CONTENT_URI);
  }

  /**
   * Get the serial of this device.
   */
  public String getSerial() {
    Bundle result = context.getContentResolver().call(MerchantDevicesV2Contract.AUTHORITY_URI, MerchantDevicesV2Contract.METHOD_GET_SERIAL, null, null);
    if (result == null) {
      return null;
    }
    return result.getString(MerchantDevicesV2Contract.RESULT_GET_SERIAL);
  }

  /**
   * Get the device with the given serial.
   */
  public Device getDevice(String serial) {
    String where = MerchantDevicesV2Contract.Device.SERIAL + "=?";
    String[] whereArgs = new String[]{serial};

    return getDevice(where, whereArgs);
  }

  /**
   * Get this device.
   */
  public Device getDevice() {
    return getDevice(getSerial());
  }

  /**
   * Get the device with the given ID.
   */
  public Device getDeviceById(String id) {
    String where = MerchantDevicesV2Contract.Device.ID + "=?";
    String[] whereArgs = new String[]{id};

    return getDevice(where, whereArgs);
  }

  private Device getDevice(String where, String[] whereArgs) {
    Cursor c = null;
    try {
      c = context.getContentResolver().query(MerchantDevicesV2Contract.Device.CONTENT_URI, null, where, whereArgs, null);
      if (c != null && c.moveToFirst()) {
        return MerchantDevicesV2Contract.Device.fromCursor(c);
      }
    } finally {
      if (c != null) {
        c.close();
      }
    }

    return null;
  }
}