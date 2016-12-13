package com.clover.sdk;

import com.clover.sdk.v3.order.Order;

import android.app.Application;
import android.os.Parcel;
import android.test.ApplicationTestCase;

public class FdParcelableTest extends ApplicationTestCase<Application> {
  public FdParcelableTest() {
    super(Application.class);
  }

  public void testNullInputValue() {
    Order o = null;
    FdParcelable<Order> fdo = new FdParcelable<>(o);
    assertEquals(null, fdo.getValue());
  }

  public void testEmptyInputBytes() {
    FdParcelable<Order> fdo = new FdParcelable<Order>(new byte[0]);
    assertEquals(null, fdo.getValue());
  }

  public void testNullInputParcel() {
    FdParcelable<Order> fdo = new FdParcelable<Order>((byte[])null);
    assertEquals(null, fdo.getValue());
  }
}
