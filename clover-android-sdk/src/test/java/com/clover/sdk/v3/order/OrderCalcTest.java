package com.clover.sdk.v3.order;

import com.clover.sdk.v3.inventory.TaxRate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class OrderCalcTest {

  private OrderCalc orderCalc;

  @Before
  public void setUp() throws Exception {
    final Order order = new Order();
    orderCalc = new OrderCalc(order);
  }

  private static TaxRate newPercentRate(final String name, final long rate) {
    final TaxRate taxRate = new TaxRate();
    taxRate.setName(name);
    taxRate.setRate(rate);
    return taxRate;
  }

  private static TaxRate newFlatTax() {
    final TaxRate flatTaxRate = new TaxRate();
    flatTaxRate.setName("flat_20");
    flatTaxRate.setRate(0L);
    flatTaxRate.setTaxAmount(20L);
    return flatTaxRate;
  }

  @Test
  public void testPriceWithoutVat() {
    //no tax rate
    LineItem lineItem = new LineItem();
    lineItem.setPrice(350L);
    assertEquals(350L, orderCalc.getPriceWithoutVAT(lineItem));
  }

  @Test
  public void testSinglePercentRateWithoutVat() {
    final TaxRate percent20TaxRate = newPercentRate("20_pct", 2000000L);

    //single percent tax rate
    final LineItem lineItem = new LineItem();
    lineItem.setPrice(350L);
    List<TaxRate> taxRates = new ArrayList<>();
    taxRates.add(percent20TaxRate);
    lineItem.setTaxRates(taxRates);
    assertEquals(292L, orderCalc.getPriceWithoutVAT(lineItem));
  }

  @Test
  public void testMultiplePercentRatesWithoutVat() {
    final TaxRate percent10TaxRate = newPercentRate("10_pct", 1000000L);
    final TaxRate percent20TaxRate = newPercentRate("20_pct", 2000000L);

    // multiple percent tax rates
    final LineItem lineItem = new LineItem();
    lineItem.setPrice(350L);
    final List<TaxRate> taxRates = new ArrayList<>();
    taxRates.add(percent10TaxRate);
    taxRates.add(percent20TaxRate);
    lineItem.setTaxRates(taxRates);
    assertEquals(269L, orderCalc.getPriceWithoutVAT(lineItem));
  }

  @Test
  public void testMultiplePercentRatesAndFlatRatesWithoutVat() {
    final TaxRate percent10TaxRate = newPercentRate("10_pct", 1000000L);
    final TaxRate percent20TaxRate = newPercentRate("20_pct", 2000000L);
    final TaxRate taxRate = newFlatTax();

    //more than one percent and one flat tax rate
    final LineItem lineItem = new LineItem();
    lineItem.setPrice(350L);
    final List<TaxRate> taxRates = new ArrayList<>();
    taxRates.add(percent10TaxRate);
    taxRates.add(percent20TaxRate);
    taxRates.add(taxRate);
    lineItem.setTaxRates(taxRates);
    assertEquals(254L, orderCalc.getPriceWithoutVAT(lineItem));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPriceWithoutVAT_negativePrice() {
    final LineItem lineItem = new LineItem();
    lineItem.setPrice(-1L);
    orderCalc.getPriceWithoutVAT(lineItem);
  }

  @Test
  public void testPriceVAT() {
    //no tax rate
    LineItem lineItem = new LineItem();
    lineItem.setPrice(350L);
    assertEquals(350L, orderCalc.getPriceWithVAT(lineItem));
  }

  @Test
  public void testSinglePercentRateVat() {
    final TaxRate percent13d3TaxRate = newPercentRate("13.3_pct", 1330000L);

    //single percent tax rate
    final LineItem lineItem = new LineItem();
    lineItem.setPrice(33L);
    List<TaxRate> taxRates = new ArrayList<>();
    taxRates.add(percent13d3TaxRate);
    lineItem.setTaxRates(taxRates);
    assertEquals(37L, orderCalc.getPriceWithVAT(lineItem));

  }

  @Test
  public void testMultiplePercentRatesVat() {
    final TaxRate percent13d3TaxRate = newPercentRate("13.3_pct", 1330000L);
    final TaxRate percent20TaxRate = newPercentRate("20_pct", 2000000L);

    // multiple percent tax rates
    final LineItem lineItem = new LineItem();
    lineItem.setPrice(33L);
    final List<TaxRate> taxRates = new ArrayList<>();
    taxRates.add(percent13d3TaxRate);
    taxRates.add(percent20TaxRate);
    lineItem.setTaxRates(taxRates);
    assertEquals(44L, orderCalc.getPriceWithVAT(lineItem));

  }

  @Test
  public void testPercentAndFlatRateVat() {
    final TaxRate percent20TaxRate = newPercentRate("20_pct", 2000000L);
    final TaxRate flatTaxRate = newFlatTax();

    //mix of percent and flat tax rate
    final LineItem lineItem = new LineItem();
    lineItem.setPrice(33L);
    final List<TaxRate> taxRates = new ArrayList<>();
    taxRates.add(percent20TaxRate);
    taxRates.add(flatTaxRate);
    lineItem.setTaxRates(taxRates);
    assertEquals(60L, orderCalc.getPriceWithVAT(lineItem));
  }

  @Test
  public void testMultiplePercentAndFlatRatesVat() {
    final TaxRate percent13d3TaxRate = newPercentRate("13.3_pct", 1330000L);
    final TaxRate percent20TaxRate = newPercentRate("20_pct", 2000000L);
    final TaxRate flatTaxRate = newFlatTax();

    //more than one percent and one flat tax rate
    final LineItem lineItem = new LineItem();
    lineItem.setPrice(33L);
    final List<TaxRate> taxRates = new ArrayList<>();
    taxRates.add(percent13d3TaxRate);
    taxRates.add(percent20TaxRate);
    taxRates.add(flatTaxRate);
    lineItem.setTaxRates(taxRates);
    assertEquals(64L, orderCalc.getPriceWithVAT(lineItem));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPriceWithVAT_negativePrice() {
    final LineItem lineItem = new LineItem();
    lineItem.setPrice(-1L);
    orderCalc.getPriceWithVAT(lineItem);
  }
}