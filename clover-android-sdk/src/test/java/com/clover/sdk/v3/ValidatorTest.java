package com.clover.sdk.v3;

import com.clover.sdk.GenericClient;
import com.clover.sdk.v3.base.Reference;
import com.clover.sdk.v3.developer.Developer;
import com.clover.sdk.v3.employees.Employee;
import com.clover.sdk.v3.inventory.Item;
import com.clover.sdk.v3.inventory.ItemStock;
import com.clover.sdk.v3.order.Order;
import com.clover.sdk.v3.order.OrderType;
import com.clover.sdk.v3.payments.Credit;
import com.clover.sdk.v3.payments.Payment;
import com.clover.sdk.v3.payments.Refund;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class ValidatorTest {

  private AtomicBoolean strictValidationFailed;

  @Before
  public void testSetup() {
    strictValidationFailed = new AtomicBoolean(false);
    GenericClient.setStrictValidationFailedCallback((e, f, a) -> strictValidationFailed.set(true));
  }

  @After
  public void testCleanup() {
    GenericClient.setStrictValidationFailedCallback(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void validateNull() {
    Item x = new Item(); // null name and price
    x.validate();
  }

  @Test(expected = IllegalArgumentException.class)
  public void validateMin() {
    Item x = new Item();
    x.setName("A");
    x.setPrice(-1L); // illegal negative price
    x.validate();
  }

  @Test(expected = IllegalArgumentException.class)
  public void validateCloverId1() {
    Item x = new Item();
    x.setName("A");
    x.setPrice(1L);
    x.setId(""); // too short
    x.validate();
  }

  @Test(expected = IllegalArgumentException.class)
  public void validateCloverId2() {
    Item x = new Item();
    x.setName("A");
    x.setPrice(1L);
    x.setId(".."); // illegal chars and too short
    x.validate();
  }

  @Test(expected = IllegalArgumentException.class)
  public void validateCloverId3() {
    Item x = new Item();
    x.setName("A");
    x.setPrice(1L);
    x.setId("11111111111111"); // too long
    x.validate();
  }

  @Test(expected = IllegalArgumentException.class)
  public void validateCloverId4() {
    Item x = new Item();
    x.setName("A");
    x.setPrice(1L);
    x.setId("111111111111"); // too short
    x.validate();
  }

  @Test(expected = IllegalArgumentException.class)
  public void validateCloverId5() {
    Item x = new Item();
    x.setName("A");
    x.setPrice(1L);
    x.setId("./../../../.."); // correct length but illegal chars
    x.validate();
  }

  @Test(expected = IllegalArgumentException.class)
  public void validateCloverId6() {
    Item x = new Item();
    x.setName("A");
    x.setPrice(1L);
    x.setId("~`;):)=)-<>{}"); // correct length but illegal chars
    x.validate();
  }

  @Test
  public void validateCloverId7() {
    Item x = new Item();
    x.setName("A");
    x.setPrice(1L);
    x.setId("OOOOOOOOOOOOO"); // illegal char
    x.validate();
  }

  @Test
  public void validateCloverId8() {
    Item x = new Item();
    x.setName("A");
    x.setPrice(1L);
    x.setId("1234567890123"); // valid
    x.validate();
  }

  @Test
  public void validateCloverId9() {
    Employee x = new Employee();
    x.setName("Default Employee");
    x.setId("DFLTEMPLOYEE"); // valid due to pre-existing use for quick access employee id
    x.validate();

    Order y = new Order();
    y.setEmployee(new Reference().setId("DFLTEMPLOYEE"));
    y.validate();
  }

  @Test
  public void validateCloverId10() {
    Developer x = new Developer();
    x.setId("CLOVERDEV"); // valid due to pre-existing use for internal developer account id
    x.validate();
  }

  @Test
  public void validateDeviceId1() {
    Order x = new Order();
    x.setDevice(new Reference().setId("123e4567-e89b-12d3-a456-426655440000")); // not a normal Clover ID
    x.validate();
  }

  @Test
  public void validateDeviceId2() {
    Credit x = new Credit();
    x.setDevice(new Reference().setId("123e4567-e89b-12d3-a456-426655440000")); // not a normal Clover ID
    x.validate();
  }

  @Test
  public void validateDeviceId3() {
    Refund x = new Refund();
    x.setDevice(new Reference().setId("123e4567-e89b-12d3-a456-426655440000")); // not a normal Clover ID
    x.validate();
  }

  @Test
  public void validateDeviceId4() {
    Payment x = new Payment();
    x.setDevice(new Reference().setId("123e4567-e89b-12d3-a456-426655440000")); // not a normal Clover ID
    x.validate();
  }

}
