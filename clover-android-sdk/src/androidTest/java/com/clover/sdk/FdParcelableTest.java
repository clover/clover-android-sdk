package com.clover.sdk;

import com.clover.sdk.v3.order.Order;
import com.clover.sdk.v3.payments.Payment;

import android.content.Context;
import android.os.Parcel;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class FdParcelableTest {
  private Context context;

  protected static String toString(InputStream inputStream) {
    try {
      BufferedInputStream bis = new BufferedInputStream(inputStream);
      ByteArrayOutputStream buf = new ByteArrayOutputStream();
      int result = bis.read();
      while (result != -1) {
        buf.write((byte) result);
        result = bis.read();
      }
      return buf.toString();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Before
  public void setUp() throws Exception {
    context = InstrumentationRegistry.getContext();
  }

  @After
  public void tearDown() throws Exception {
  }


  @Test
  public void testNullInputValue() {
    Order o = null;
    FdParcelable<Order> fdo = new FdParcelable<>(o);
    assertNotNull(fdo);
    assertEquals(null, fdo.getValue());

    Parcel out = Parcel.obtain();
    fdo.writeToParcel(out, 0);

    fdo = new FdParcelable<Order>(out);
    assertNotNull(fdo);
    assertEquals(null, fdo.getValue());
  }

  @Test
  public void testEmptyInputBytes() {
    FdParcelable<Order> fdo = new FdParcelable<Order>(new byte[0]);
    assertNotNull(fdo);
    assertEquals(null, fdo.getValue());

    Parcel out = Parcel.obtain();
    fdo.writeToParcel(out, 0);

    fdo = new FdParcelable<Order>(out);
    assertNotNull(fdo);
    assertEquals(null, fdo.getValue());
  }

  @Test
  public void testNullInputParcel() {
    FdParcelable<Order> fdo = new FdParcelable<Order>((byte[]) null);
    assertNotNull(fdo);
    assertEquals(null, fdo.getValue());

    Parcel out = Parcel.obtain();
    fdo.writeToParcel(out, 0);

    fdo = new FdParcelable<Order>(out);
    assertNotNull(fdo);
    assertEquals(null, fdo.getValue());
  }

  @Test
  public void testSmallOrder() throws Exception {
    Order o = new Order(toString(context.getAssets().open("orders/order_items_1.json")));
    assertNotNull(o);
    FdParcelable<Order> fdo = new FdParcelable<>(o);
    assertNotNull(fdo);

    o = fdo.getValue();
    assertEquals(1, o.getLineItems().size());

    Parcel out = Parcel.obtain();
    fdo.writeToParcel(out, 0);
    out.setDataPosition(0);

    fdo = new FdParcelable<Order>(out);
    assertNotNull(fdo);
    assertNotNull(fdo.getValue());
    assertEquals(1, fdo.getValue().getLineItems().size());
  }

  @Test
  public void testLargeOrder() throws Exception {
    Order o = new Order(toString(context.getAssets().open("orders/order_items_400.json")));
    assertNotNull(o);
    FdParcelable<Order> fdo = new FdParcelable<>(o);
    assertNotNull(fdo);

    o = fdo.getValue();
    assertEquals(400, o.getLineItems().size());

    Parcel out = Parcel.obtain();
    fdo.writeToParcel(out, 0);
    out.setDataPosition(0);

    fdo = new FdParcelable<Order>(out);
    assertNotNull(fdo);
    assertNotNull(fdo.getValue());
    assertEquals(400, fdo.getValue().getLineItems().size());
  }

  @Test
  public void testSmallOrder1000() throws Exception {
    for (int i = 0; i < 1000; i++) {
      testSmallOrder();
    }
  }

  @Test
  public void testLargeOrder100() throws Exception {
    for (int i = 0; i < 100; i++) {
      testLargeOrder();
    }
  }

  @Test
  public void testMixedParallel() throws Exception {
    Thread large = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          testLargeOrder100();
        } catch (Exception e) {
          fail("testLargeOrder100 failed in thread: " + e);
        }
      }
    });
    Thread small = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          testSmallOrder1000();
        } catch (Exception e) {
          fail("testSmallOrder1000 failed in thread: " + e);
        }
      }
    });

    large.start();
    small.start();

    large.join();
    small.join();
  }
}