package com.clover.sdk.v3.order;

import com.clover.sdk.v3.inventory.TaxRate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(RobolectricTestRunner.class)
public class TaxRateCalcTest {
  @Test
  public void testTaxRateExceptionMessageHandlesNullId() {
    NullPointerException thrown = null;
    final TaxRate taxRate = new TaxRate();
    try {
      new TaxRateCalc(taxRate);
    } catch (NullPointerException e) {
      thrown = e;
    }
    assertThat(thrown, is(notNullValue()));
    assertThat(thrown.getMessage(), is(notNullValue()));
    assertThat(thrown.getMessage().contains("null"), is(true));
  }

  @Test
  public void testTaxRateExceptionMessageIncludesId() {
    Exception thrown = null;
    final TaxRate taxRate = new TaxRate();
    taxRate.setId("ABC123");
    try {
      new TaxRateCalc(taxRate);
    } catch (Exception e) {
      thrown = e;
    }
    assertThat(thrown, is(notNullValue()));
    assertThat(thrown.getMessage(), is(notNullValue()));
    assertThat(thrown.getMessage().contains("ABC123"), is(true));
  }
}
