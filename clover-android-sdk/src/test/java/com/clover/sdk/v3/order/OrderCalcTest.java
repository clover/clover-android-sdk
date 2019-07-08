package com.clover.sdk.v3.order;

import com.clover.sdk.v3.inventory.TaxRate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class OrderCalcTest {

  @Test
  public void testPriceWithoutVat() {
    final Order order = new Order();
    final OrderCalc orderCalc = new OrderCalc(order);

    final TaxRate percent10TaxRate = new TaxRate();
    percent10TaxRate.setName("10_pct");
    percent10TaxRate.setRate(1000000L);

    final TaxRate percent20TaxRate = new TaxRate();
    percent20TaxRate.setName("20_pct");
    percent20TaxRate.setRate(2000000L);

    final TaxRate flatTaxRate = new TaxRate();
    flatTaxRate.setName("flat_20");
    flatTaxRate.setRate(0L);
    flatTaxRate.setTaxAmount(20L);

    //no tax rate
    LineItem lineItem = new LineItem();
    lineItem.setPrice(350L);
    assertEquals(350L, orderCalc.getPriceWithoutVAT(lineItem));

    //single percent tax rate
    lineItem = new LineItem();
    lineItem.setPrice(350L);
    List<TaxRate> taxRates = new ArrayList<>();
    taxRates.add(percent20TaxRate);
    lineItem.setTaxRates(taxRates);
    assertEquals(292L, orderCalc.getPriceWithoutVAT(lineItem));

    // multiple percent tax rates
    lineItem = new LineItem();
    lineItem.setPrice(350L);
    taxRates = new ArrayList<>();
    taxRates.add(percent10TaxRate);
    taxRates.add(percent20TaxRate);
    lineItem.setTaxRates(taxRates);
    assertEquals(269L, orderCalc.getPriceWithoutVAT(lineItem));

    //more than one percent and one flat tax rate
    lineItem = new LineItem();
    lineItem.setPrice(350L);
    taxRates = new ArrayList<>();
    taxRates.add(percent10TaxRate);
    taxRates.add(percent20TaxRate);
    taxRates.add(flatTaxRate);
    lineItem.setTaxRates(taxRates);
    assertEquals(254L, orderCalc.getPriceWithoutVAT(lineItem));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPriceWithoutVAT_negativePrice() {
    final Order order = new Order();
    final OrderCalc orderCalc = new OrderCalc(order);
    final LineItem lineItem = new LineItem();
    lineItem.setPrice(-1L);
    orderCalc.getPriceWithoutVAT(lineItem);
  }

  @Test
  public void testPriceVAT() {
    final Order order = new Order();
    final OrderCalc orderCalc = new OrderCalc(order);

    final TaxRate percent13d3TaxRate = new TaxRate();
    percent13d3TaxRate.setName("13.3_pct");
    percent13d3TaxRate.setRate(1330000L);

    final TaxRate percent20TaxRate = new TaxRate();
    percent20TaxRate.setName("20_pct");
    percent20TaxRate.setRate(2000000L);

    final TaxRate flatTaxRate = new TaxRate();
    flatTaxRate.setName("flat_20");
    flatTaxRate.setRate(0L);
    flatTaxRate.setTaxAmount(20L);

    //no tax rate
    LineItem lineItem = new LineItem();
    lineItem.setPrice(350L);
    assertEquals(350L, orderCalc.getPriceWithVAT(lineItem));

    //single percent tax rate
    lineItem = new LineItem();
    lineItem.setPrice(33L);
    List<TaxRate> taxRates = new ArrayList<>();
    taxRates.add(percent13d3TaxRate);
    lineItem.setTaxRates(taxRates);
    assertEquals(37L, orderCalc.getPriceWithVAT(lineItem));

    // multiple percent tax rates
    lineItem = new LineItem();
    lineItem.setPrice(33L);
    taxRates = new ArrayList<>();
    taxRates.add(percent13d3TaxRate);
    taxRates.add(percent20TaxRate);
    lineItem.setTaxRates(taxRates);
    assertEquals(44L, orderCalc.getPriceWithVAT(lineItem));

    //mix of percent and flat tax rate
    lineItem = new LineItem();
    lineItem.setPrice(33L);
    taxRates = new ArrayList<>();
    taxRates.add(percent20TaxRate);
    taxRates.add(flatTaxRate);
    lineItem.setTaxRates(taxRates);
    assertEquals(60L, orderCalc.getPriceWithVAT(lineItem));

    //more than one percent and one flat tax rate
    lineItem = new LineItem();
    lineItem.setPrice(33L);
    taxRates = new ArrayList<>();
    taxRates.add(percent13d3TaxRate);
    taxRates.add(percent20TaxRate);
    taxRates.add(flatTaxRate);
    lineItem.setTaxRates(taxRates);
    assertEquals(64L, orderCalc.getPriceWithVAT(lineItem));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPriceWithVAT_negativePrice() {
    final Order order = new Order();
    final OrderCalc orderCalc = new OrderCalc(order);
    final LineItem lineItem = new LineItem();
    lineItem.setPrice(-1L);
    orderCalc.getPriceWithVAT(lineItem);
  }
}