package com.clover.sdk;

import com.clover.sdk.v3.order.Order;

import android.support.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class FdParcelableTest  {

  @Test
  public void testNullInputValue() {
    Order o = null;
    FdParcelable<Order> fdo = new FdParcelable<>(o);
    assertEquals(null, fdo.getValue());
  }

  @Test
  public void testEmptyInputBytes() {
    FdParcelable<Order> fdo = new FdParcelable<Order>(new byte[0]);
    assertEquals(null, fdo.getValue());
  }

  @Test
  public void testNullInputParcel() {
    FdParcelable<Order> fdo = new FdParcelable<Order>((byte[])null);
    assertEquals(null, fdo.getValue());
  }
}
