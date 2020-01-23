package com.clover.sdk.v3.merchant;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.BaseColumns;
import android.util.Log;

import com.clover.sdk.v3.base.Reference;
import com.clover.sdk.v3.device.BuildType;

import java.util.HashMap;
import java.util.Map;

/**
 * Devices associated with this merchant. This class is the contract between an app and the
 * merchant devices content provider. Use it by querying the content provider with a
 * {@link android.content.ContentResolver} supplying the URI {@link Device#CONTENT_URI}.
 * <code>
 * Cursor c = getContentResolver().query(MerchantDevicesV2Contract.Device.CONTENT_URI, null, null, null, null);
 * </code>
 * </p>
 * The contract provider backing this contract is read-only; you may only query it and
 * attempting to mutate the data therein via insert, update, or delete will result in a
 * permission exception.
 * <p/>
 * Most clients will be interested in obtaining information about the current device. For this
 * see the {@link MerchantDevicesV2Connector#getDevice()}.
 *
 * @see MerchantDevicesV2Connector
 */
public final class MerchantDevicesV2Contract {
  private static final String TAG = MerchantDevicesV2Contract.class.getSimpleName();

  public static final String AUTHORITY = "com.clover.merchant.devices.v2";
  public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

  /**
   * Get the serial of this device. Avoid having developers deal with SDK restrictions
   * around {@link Build#SERIAL} and {@link Build#getSerial()}.
   *
   * @see Build#SERIAL
   * @see Build#getSerial()
   * @see #RESULT_GET_SERIAL
   *
   */
  public static final String METHOD_GET_SERIAL = "getSerial";

  /**
   * Extra present in the response bundle from {@link #METHOD_GET_SERIAL}, a String, the serial
   * of this device.
   *
   * @see #METHOD_GET_SERIAL
   */
  public static final String RESULT_GET_SERIAL = "result_serial";

  public interface DeviceColumns {
    /**
     * A unique identifier for this device. This value is used to reference this in API calls
     * to the Clover server.
     */
    public static final String ID = "id";
    /**
     * The name of the device.
     */
    public static final String NAME = "name";
    /**
     * The model of this device.
     */
    public static final String MODEL = "model";
    /**
     * The merchant ID to which this device is associated.
     */
    public static final String MERCHANT_ID = "merchant";
    /**
     * The prefix for orders taken on this device.
     */
    public static final String ORDER_PREFIX = "order_prefix";

    /**
     * The terminal prefix for this device.
     */
    public static final String TERMINAL_PREFIX = "terminal_prefix";
    /**
     * The serial number for this device.
     *
     * @see Build#getSerial()
     */
    public static final String SERIAL = "serial";
    /**
     * The secure ID (aka Android ID) of this device.
     *
     * @see android.provider.Settings.Secure#ANDROID_ID
     */
    public static final String SECURE_ID = "secure_id";
    /**
     * The build type of this device.
     *
     * @see BuildType
     */
    public static final String BUILD_TYPE = "build_type";
    /**
     * The device type name of this device. For Clover devices this is a unique internal
     * codename for this device's hardware and hence is not typically useful for developers.
     */
    public static final String DEVICE_TYPE_NAME = "device_type_name";
    /**
     * The product name of this device. This is the Clover marketing name that ius presented
     * to users (e.g. "Flex", "Mini", etc).
     */
    public static final String PRODUCT_NAME = "product_name";
    /**
     * Are payments requiring PIN entry disabled on this device?
     */
    public static final String PIN_DISABLED = "pin_disabled";
    /**
     * Are offline payments allowed on this device?
     */
    public static final String OFFLINE_PAYMENTS = "offline_payments";
    /**
     * Unused.
     */
    public static final String OFFLINE_PAYMENTS_ALL = "offline_payments_all";
    /**
     * Offline payments limit.
     */
    public static final String OFFLINE_PAYMENTS_LIMIT = "offline_payments_limit";
    /**
     * Offline payments prompt threshold.
     */
    public static final String OFFLINE_PAYMENTS_PROMPT_THRESHOLD = "offline_payments_prompt_threshold";
    /**
     * Offline payments total payments limit.
     */
    public static final String OFFLINE_PAYMENTS_TOTAL_PAYMENTS_LIMIT = "offline_payments_total_payments_limit";

    static final String[] COLUMNS = { ID, NAME, MODEL, MERCHANT_ID, ORDER_PREFIX,
        TERMINAL_PREFIX, SERIAL, SECURE_ID, BUILD_TYPE, DEVICE_TYPE_NAME, PRODUCT_NAME,
        PIN_DISABLED, OFFLINE_PAYMENTS, OFFLINE_PAYMENTS_ALL, OFFLINE_PAYMENTS_LIMIT,
        OFFLINE_PAYMENTS_PROMPT_THRESHOLD, OFFLINE_PAYMENTS_TOTAL_PAYMENTS_LIMIT};
  }

  public static final class Device implements BaseColumns, DeviceColumns {
    private Device() {
    }

    public static final String CONTENT_DIRECTORY = "devices";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_DIRECTORY);
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/device";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/device";

    /**
     * Convert a cursor obtained by querying the URI {@link #CONTENT_URI} to a
     * {@link com.clover.sdk.v3.device.Device} object.
     * <p/>
     * Only the columns declared in {@link Device} are set.
     */
    public static com.clover.sdk.v3.device.Device fromCursor(Cursor cursor) {
      com.clover.sdk.v3.device.Device device = new com.clover.sdk.v3.device.Device();

      device.setId(getString(cursor, ID));
      device.setName(getString(cursor, NAME));
      device.setModel(getString(cursor, MODEL));
      device.setMerchant(getReference(cursor, MERCHANT_ID));
      device.setOrderPrefix(getString(cursor, ORDER_PREFIX));
      device.setTerminalPrefix(getInt(cursor, TERMINAL_PREFIX));
      device.setSerial(getString(cursor, SERIAL));
      device.setSecureId(getString(cursor, SECURE_ID));
      try {
        device.setBuildType(getObject(cursor, BUILD_TYPE, BuildType::valueOf));
      } catch (IllegalArgumentException e) {
        Log.e(TAG, "Failed getting value of build type: " + getString(cursor, BUILD_TYPE));
      }
      device.setDeviceTypeName(getString(cursor, DEVICE_TYPE_NAME));
      device.setProductName(getString(cursor, PRODUCT_NAME));
      device.setPinDisabled(is(cursor, PIN_DISABLED));
      device.setOfflinePayments(is(cursor, OFFLINE_PAYMENTS));
      device.setOfflinePaymentsAll(is(cursor, OFFLINE_PAYMENTS_ALL));
      device.setOfflinePaymentsLimit(getLong(cursor, OFFLINE_PAYMENTS_LIMIT));
      device.setOfflinePaymentsPromptThreshold(getLong(cursor, OFFLINE_PAYMENTS_PROMPT_THRESHOLD));
      device.setOfflinePaymentsTotalPaymentsLimit(getLong(cursor, OFFLINE_PAYMENTS_TOTAL_PAYMENTS_LIMIT));

      return device;
    }

    /**
     * Convert a device into a {@link ContentValues}. Only values where the {@link Device}
     * "has" the value are reflected in the returned {@link ContentValues};
     */
    public static ContentValues toContentValues(com.clover.sdk.v3.device.Device device) {
      ContentValues values = new ContentValues();

      if (device.hasId()) {
        values.put(ID, device.getId());
      }
      if (device.hasName()) {
        values.put(NAME, device.getName());
      }
      if (device.hasModel()) {
        values.put(MODEL, device.getModel());
      }
      if (device.hasMerchant()) {
        values.put(MERCHANT_ID, referenceToValue(device.getMerchant()));
      }
      if (device.hasTerminalPrefix()) {
        values.put(TERMINAL_PREFIX, device.getTerminalPrefix());
      }
      if (device.hasOrderPrefix()) {
        values.put(ORDER_PREFIX, device.getOrderPrefix());
      }
      if (device.hasSerial()) {
        values.put(SERIAL, device.getSerial());
      }
      if (device.hasSecureId()) {
        values.put(SECURE_ID, device.getSecureId());
      }
      if (device.hasBuildType()) {
        values.put(BUILD_TYPE, objectToValue(device.getBuildType(), Enum::name));
      }
      if (device.hasDeviceTypeName()) {
        values.put(DEVICE_TYPE_NAME, device.getDeviceTypeName());
      }
      if (device.hasProductName()) {
        values.put(PRODUCT_NAME, device.getProductName());
      }
      if (device.hasPinDisabled()) {
        values.put(PIN_DISABLED, booleanToValue(device::getPinDisabled));
      }
      if (device.hasOfflinePayments()) {
        values.put(OFFLINE_PAYMENTS, booleanToValue(device::getOfflinePayments));
      }
      if (device.hasOfflinePaymentsAll()) {
        values.put(OFFLINE_PAYMENTS_ALL, booleanToValue(device::getOfflinePaymentsAll));
      }
      if (device.hasOfflinePaymentsLimit()) {
        values.put(OFFLINE_PAYMENTS_LIMIT, device.getOfflinePaymentsLimit());
      }
      if (device.hasOfflinePaymentsPromptThreshold()) {
        values.put(OFFLINE_PAYMENTS_PROMPT_THRESHOLD, device.getOfflinePaymentsPromptThreshold());
      }
      if (device.hasOfflinePaymentsTotalPaymentsLimit()) {
        values.put(OFFLINE_PAYMENTS_TOTAL_PAYMENTS_LIMIT, device.getOfflinePaymentsTotalPaymentsLimit());
      }

      return values;
    }

    private interface GetValue<T> {
      T get();
    }

    private interface ToString<T> {
      String get(T t);
    }

    private static Integer booleanToValue(GetValue<Boolean> get) {
      Boolean val = get.get();
      if (val == null) {
        return null;
      }
      return val ? 1 : 0;
    }

    private static <T> String objectToValue(T object, ToString<T> t) {
      if (object == null) {
        return null;
      }
      return t.get(object);
    }

    private static String referenceToValue(Reference r) {
      if (r == null) {
        return null;
      }
      return r.getId();
    }

    private static final Map<String,Integer> columnIndices = new HashMap<>();

    private static synchronized int getColumnIndex(Cursor cursor, String name) {
      Integer val = columnIndices.get(name);
      if (val == null) {
        val = cursor.getColumnIndex(name);
        columnIndices.put(name, val);
      }

      return val;
    }

    private interface Set<T> {
      void set(T val);
    }

    private interface Deserializer<T> {
      T deserialize(String s);
    }

    private static String getString(Cursor cursor, String column) {
      int index = getColumnIndex(cursor, column);
      if (index == -1 || cursor.isNull(index)) {
        return null;
      }
      return cursor.getString(index);
    }

    private static <T> T getObject(Cursor cursor, String column, Deserializer<T> deserializer) {
      int index = getColumnIndex(cursor, column);
      if (index == -1 || cursor.isNull(index)) {
        return null;
      }
      return deserializer.deserialize(cursor.getString(index));
    }

    private static Long getLong(Cursor cursor, String column) {
      int index = getColumnIndex(cursor, column);
      if (index == -1 || cursor.isNull(index)) {
        return null;
      }
      return cursor.getLong(index);
    }

    private static Integer getInt(Cursor cursor, String column) {
      int index = getColumnIndex(cursor, column);
      if (index == -1 || cursor.isNull(index)) {
        return null;
      }
      return cursor.getInt(index);
    }

    private static Reference getReference(Cursor cursor, String column) {
      int index = getColumnIndex(cursor, column);
      if (index == -1 || cursor.isNull(index)) {
        return null;
      }
      Reference r = new Reference();
      r.setId(cursor.getString(index));
      return r;
    }

    private static Boolean is(Cursor cursor, String column) {
      int index = getColumnIndex(cursor, column);
      if (index == -1 || cursor.isNull(index)) {
        return null;
      }
      return cursor.getInt(index ) > 0;
    }
  }
}
