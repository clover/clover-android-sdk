package com.clover.sdk.v3.retrofit;

import com.clover.sdk.v3.merchant.Merchant;
import com.clover.sdk.v3.order.Order;

import org.jetbrains.annotations.Nullable;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 *  Utility class to hold helper functions to compare SDK objects, for unit testing
 *  purposes only. The comparisons done here are not complete and only compare
 *  a small subset of the class fields.
 *
 *  Unfortunately Clover SDK objects don't implement {@link #equals(Object)} robustly.
 *
 *  "Same" here means different objects, but identical fields.
 */
class SdkSame {
  static void assertOrderSame(@Nullable Order o1, @Nullable Order o2) {
    if (o1 != o2) {
      if (o1 == null) {
        Assert.fail("o1 is null");
      }
      if (o2 == null) {
        Assert.fail("o2 is null");
      }
      assertEquals(o1.getId(), o2.getId());
      assertEquals(o1.getNote(), o2.getNote());
      assertEquals(o1.getState(), o2.getState());
    } else {
      fail("o1 and o2 are equal");
    }
  }

  static void assertMerchantsSame(@Nullable Merchant m1, @Nullable Merchant m2) {
    if (m1 != m2) {
      if (m1 == null) {
        Assert.fail("m1 is null");
      }
      if (m2 == null) {
        Assert.fail("m2 is null");
      }
      assertEquals(m1.getId(), m2.getId());

      if (m1.getAddress() != m2.getAddress()) {
        if (m1.getAddress() == null) {
          Assert.fail("m1.address is null");
        }
        if (m2.getAddress() == null) {
          Assert.fail("m2.address is null");
        }
        assertEquals(m1.getAddress().getAddress1(), m2.getAddress().getAddress1());
        assertEquals(m1.getAddress().getCity(), m2.getAddress().getCity());
        assertEquals(m1.getAddress().getState(), m2.getAddress().getState());
        assertEquals(m1.getAddress().getZip(), m2.getAddress().getZip());
      }
    } else {
      fail("m1 and m2 are equal");
    }
  }
}
