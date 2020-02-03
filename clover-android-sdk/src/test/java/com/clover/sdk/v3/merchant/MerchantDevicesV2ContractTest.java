package com.clover.sdk.v3.merchant;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;

import com.clover.sdk.v3.base.Reference;
import com.clover.sdk.v3.device.BuildType;
import com.clover.sdk.v3.device.Device;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Arrays;
import java.util.Objects;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class MerchantDevicesV2ContractTest {

  /**
   * Test if given an empty device with no fields set (meaning not set to null either), that
   * the conversion to values and back to a device will result in all fields being set
   * to null.
   * <p/>
   * The argument could be made that the resulting device should not have the fields set
   * if they weren't set in the starting device. It's however not possible to maintain
   * this with a real database since the DB cannot maintain a "not set" state for a column;
   * it's either null or has a value.
   */
  @Test
  public void testNullMaintained_emptyDevice() {
    Device device1 = new Device();

    ContentValues values = MerchantDevicesV2Contract.Device.toContentValues(device1);
    assertNotNull(values);

    Device device2 = MerchantDevicesV2Contract.Device.fromCursor(toCursor(values));

    assertNotNull(device2);
    assertAllFieldsNull(device2);
  }

  /**
   * Test if given a device will all synced fields set to null, that the conversion to values
   * and back to device will result in all field set to null.
   */
  @Test
  public void testNullMaintained_nulledDevice() {
    Device device1 = new Device();

    device1.setId(null);
    device1.setName(null);
    device1.setModel(null);
    device1.setMerchant(null);
    device1.setOrderPrefix(null);
    device1.setTerminalPrefix(null);
    device1.setSerial(null);
    device1.setSecureId(null);
    device1.setBuildType(null);
    device1.setDeviceTypeName(null);
    device1.setProductName(null);
    device1.setPinDisabled(null);
    device1.setOfflinePayments(null);
    device1.setOfflinePaymentsAll(null);
    device1.setOfflinePaymentsLimit(null);
    device1.setOfflinePaymentsPromptThreshold(null);
    device1.setOfflinePaymentsTotalPaymentsLimit(null);

    ContentValues values = MerchantDevicesV2Contract.Device.toContentValues(device1);
    assertNotNull(values);

    Device device2 = MerchantDevicesV2Contract.Device.fromCursor(toCursor(values));

    assertNotNull(device2);
    assertAllFieldsNull(device2);
  }

  /**
   * Test that given a device with fields set to non-null values that those values are maintained
   * when we convert to values and then back to a device.
   */
  @Test
  public void testValuesMaintained() {
    Device device1 = new Device();

    device1.setId("01234567890AB");
    device1.setName("DEVICE_NAME");
    device1.setModel("Clover_C100");
    device1.setMerchant(newReference("ABCDEFGIJKLMN"));
    device1.setOrderPrefix("ORDER_PREFIX");
    device1.setTerminalPrefix(123);
    device1.setSerial("1234567890ABC");
    device1.setSecureId("SECURE_ID");
    device1.setBuildType(BuildType.ENG);
    device1.setDeviceTypeName("GOLDLEAF");
    device1.setProductName("Clover Station");
    device1.setPinDisabled(true);
    device1.setOfflinePayments(true);
    device1.setOfflinePaymentsAll(true);
    device1.setOfflinePaymentsLimit(10000L);
    device1.setOfflinePaymentsPromptThreshold(1000L);
    device1.setOfflinePaymentsTotalPaymentsLimit(100000L);

    ContentValues values = MerchantDevicesV2Contract.Device.toContentValues(device1);
    assertNotNull(values);

    Device device2 = MerchantDevicesV2Contract.Device.fromCursor(toCursor(values));

    assertNotNull(device2);
    assertFieldsEqual(device1, device2);
  }

  private static Reference newReference(String id) {
    Reference r = new Reference();
    r.setId(id);
    return r;
  }

  /**
   * Because avro objects do not implement any sort of meaningful equals.
   */
  private static void assertReferenceEquals(Reference r1, Reference r2) {
    if (r1 == r2) return;
    if (r1 == null || r2.getClass() != r1.getClass()) fail("References not equal");
    if (!Objects.equals(r1.getId(), r2.getId())) {
      fail("References not equal");
    }
  }

  private void assertAllFieldsNull(Device device) {
    assertTrue(device.hasId());
    assertNull(device.getId());

    assertTrue(device.hasName());
    assertNull(device.getName());

    assertTrue(device.hasModel());
    assertNull(device.getModel());

    assertTrue(device.hasMerchant());
    assertNull(device.getMerchant());

    assertTrue(device.hasOrderPrefix());
    assertNull(device.getOrderPrefix());

    assertTrue(device.hasTerminalPrefix());
    assertNull(device.getTerminalPrefix());

    assertTrue(device.hasSerial());
    assertNull(device.getSerial());

    assertTrue(device.hasSecureId());
    assertNull(device.getSecureId());

    assertTrue(device.hasBuildType());
    assertNull(device.getBuildType());

    assertTrue(device.hasDeviceTypeName());
    assertNull(device.getDeviceTypeName());

    assertTrue(device.hasProductName());
    assertNull(device.getProductName());

    assertTrue(device.hasPinDisabled());
    assertNull(device.getPinDisabled());

    assertTrue(device.hasOfflinePayments());
    assertNull(device.getOfflinePayments());

    assertTrue(device.hasOfflinePaymentsAll());
    assertNull(device.getOfflinePaymentsAll());

    assertTrue(device.hasOfflinePaymentsLimit());
    assertNull(device.getOfflinePaymentsLimit());

    assertTrue(device.hasOfflinePaymentsPromptThreshold());
    assertNull(device.getOfflinePaymentsPromptThreshold());

    assertTrue(device.hasOfflinePaymentsTotalPaymentsLimit());
    assertNull(device.getOfflinePaymentsTotalPaymentsLimit());
  }

  private void assertFieldsEqual(Device device1, Device device2) {
    assertEquals(device1.hasId(), device2.hasId());
    assertEquals(device1.getId(), device2.getId());

    assertEquals(device1.hasName(), device2.hasName());
    assertEquals(device1.getName(), device2.getName());

    assertEquals(device1.hasModel(), device2.hasModel());
    assertEquals(device1.getModel(), device2.getModel());

    assertEquals(device1.hasMerchant(), device2.hasMerchant());
    assertReferenceEquals(device1.getMerchant(), device2.getMerchant());

    assertEquals(device1.hasOrderPrefix(), device2.hasOrderPrefix());
    assertEquals(device1.getOrderPrefix(), device2.getOrderPrefix());

    assertEquals(device1.hasTerminalPrefix(), device2.hasTerminalPrefix());
    assertEquals(device1.getTerminalPrefix(), device2.getTerminalPrefix());

    assertEquals(device1.hasSerial(), device2.hasSerial());
    assertEquals(device1.getSerial(), device2.getSerial());

    assertEquals(device1.hasSecureId(), device2.hasSecureId());
    assertEquals(device1.getSecureId(), device2.getSecureId());

    assertEquals(device1.hasBuildType(), device2.hasBuildType());
    assertEquals(device1.getBuildType(), device2.getBuildType());

    assertEquals(device1.hasDeviceTypeName(), device2.hasDeviceTypeName());
    assertEquals(device1.getDeviceTypeName(), device2.getDeviceTypeName());

    assertEquals(device1.hasProductName(), device2.hasProductName());
    assertEquals(device1.getProductName(), device2.getProductName());

    assertEquals(device1.hasPinDisabled(), device2.hasPinDisabled());
    assertEquals(device1.getPinDisabled(), device2.getPinDisabled());

    assertEquals(device1.hasOfflinePayments(), device2.hasOfflinePayments());
    assertEquals(device1.hasOfflinePayments(), device2.hasOfflinePayments());

    assertEquals(device1.hasOfflinePaymentsAll(), device2.hasOfflinePaymentsAll());
    assertEquals(device1.getOfflinePaymentsAll(), device2.getOfflinePaymentsAll());

    assertEquals(device1.hasOfflinePaymentsLimit(), device2.hasOfflinePaymentsLimit());
    assertEquals(device1.getOfflinePaymentsLimit(), device2.getOfflinePaymentsLimit());

    assertEquals(device1.hasOfflinePaymentsPromptThreshold(), device2.hasOfflinePaymentsPromptThreshold());
    assertEquals(device1.getOfflinePaymentsPromptThreshold(), device2.getOfflinePaymentsPromptThreshold());

    assertEquals(device1.hasOfflinePaymentsTotalPaymentsLimit(), device2.hasOfflinePaymentsTotalPaymentsLimit());
    assertEquals(device1.getOfflinePaymentsTotalPaymentsLimit(), device2.getOfflinePaymentsTotalPaymentsLimit());
  }

  /**
   * Create a cursor, mimicking what would be returned from a database query. Column values
   * are set according to the provided values, and are otherwise null.
   */
  private Cursor toCursor(ContentValues values) {
    MatrixCursor c = new MatrixCursor(MerchantDevicesV2Contract.Device.COLUMNS, 1);
    MatrixCursor.RowBuilder rb = c.newRow();
    Arrays.stream(MerchantDevicesV2Contract.Device.COLUMNS).forEach((column) -> rb.add(values.get(column)));
    c.moveToFirst();
    return c;
  }
}