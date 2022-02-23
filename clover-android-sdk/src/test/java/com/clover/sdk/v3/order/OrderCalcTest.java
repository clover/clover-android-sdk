package com.clover.sdk.v3.order;

import com.clover.core.internal.calc.Calc;
import com.clover.core.internal.calc.Price;
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

  @Test
  public void testGetAdditionalChargeSummaries() {
    final LineItem lineItem1 = new LineItem();
    lineItem1.setPrice(new Price(333).getCents());

    final LineItem lineItem2 = new LineItem();
    lineItem2.setPrice(new Price(666).getCents());

    final List<LineItem> lineItems = new ArrayList<>();
    lineItems.add(lineItem1);
    lineItems.add(lineItem2);

    final Order order = new Order();
    order.setLineItems(lineItems);

    final AdditionalCharge additionalChargePercent = new AdditionalCharge();
    additionalChargePercent.setType(AdditionalChargeType.DELIVERY_FEE);
    additionalChargePercent.setPercentageDecimal(10000L);

    final AdditionalCharge additionalChargeFixed = new AdditionalCharge();
    additionalChargeFixed.setAmount(34L);
    additionalChargeFixed.setType(AdditionalChargeType.DELIVERY_FEE);

    final List<AdditionalCharge> additionalCharges = new ArrayList<>();
    additionalCharges.add(additionalChargePercent);
    additionalCharges.add(additionalChargeFixed);
    order.setAdditionalCharges(additionalCharges);

    final OrderCalc orderCalc = new OrderCalc(order);
    final List<Calc.AdditionalChargeSummary> additionalChargeSummaries =
        orderCalc.getAdditionalChargeSummaries(lineItems);

    assertEquals(additionalChargePercent.getType().name(),
        additionalChargeSummaries.get(0).additionalCharge.getType());
    assertEquals(new Price(10), additionalChargeSummaries.get(0).charge);

    assertEquals(additionalChargePercent.getType().name(),
        additionalChargeSummaries.get(1).additionalCharge.getType());
    assertEquals(new Price(34), additionalChargeSummaries.get(1).charge);
  }

  @Test
  public void testGetAdditionalChargeSummaries_noChargesAvailable() {
    final List<LineItem> lineItems = new ArrayList<>();

    final Order order = new Order();
    order.setLineItems(lineItems);

    final OrderCalc orderCalc = new OrderCalc(order);
    final List<Calc.AdditionalChargeSummary> additionalChargeSummaries =
        orderCalc.getAdditionalChargeSummaries(lineItems);

    assertEquals(0, additionalChargeSummaries.size());
  }
}
