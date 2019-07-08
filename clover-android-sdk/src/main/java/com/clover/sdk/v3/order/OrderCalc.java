/**
 * Copyright (C) 2016 Clover Network, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.clover.sdk.v3.order;

import com.clover.core.internal.Lists;
import com.clover.core.internal.calc.Calc;
import com.clover.core.internal.calc.Decimal;
import com.clover.core.internal.calc.Price;
import com.clover.sdk.v3.base.ServiceCharge;
import com.clover.sdk.v3.inventory.TaxRate;
import com.clover.sdk.v3.payments.Payment;

import android.os.Bundle;
import android.util.Log;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Interfaces with {@link Calc} to calculate total, discount, and
 * tax of an {@link Order}.
 */
public class OrderCalc {

  private static final String TAG = OrderCalc.class.getSimpleName();
  private static final Decimal TAX_RATE_DIVISOR = new Decimal(100000);
  private static final Decimal SERVICE_CHARGE_DIVISOR = new Decimal(10000);
  private static final Decimal UNIT_QUANTITY_DIVISOR = new Decimal(1000);
  private static final Decimal HUNDRED = new Decimal(100);

  Order order;
  CalcOrder calcOrder;

  private final Calc.Logger logger = new Calc.Logger() {
    @Override
    public void warn(String s) {
      Log.w(TAG, s);
    }
  };

  private class CalcOrder implements Calc.Order {
    @Override
    public boolean isVat() {
      return order.isNotNullIsVat() && order.getIsVat();
    }

    @Override
    public boolean isTaxRemoved() {
      return order.isNotNullTaxRemoved() ? order.getTaxRemoved() : false;
    }

    @Override
    public Collection<Calc.LineItem> getLineItems() {
      if (order.getLineItems() == null) {
        return Collections.emptyList();
      }
      List<Calc.LineItem> calcLines = Lists.newArrayList();
      for (LineItem line : order.getLineItems()) {
        calcLines.add(new CalcLineItem(line));
      }
      return calcLines;
    }

    @Override
    public Price getComboDiscount() {
      //long discount = -sumOfAdjustments(order.getComboAdjustments(), Adjustment.AdjustmentType.COMBO_DISCOUNT);
      return new Price(0);
    }

    @Override
    public Price getAmountDiscount() {
      long discount = -sumOfAdjustments(order.getDiscounts());
      return new Price(discount);
    }

    @Override
    public Collection<Decimal> getPercentDiscounts() {
      return OrderCalc.getPercentDiscounts(order.getDiscounts());
    }

    @Override
    public Price getTip() {
      long totalTips = 0;
      if (order.isNotNullPayments()) {
        for (Payment p : order.getPayments()) {
          if (p.isNotNullTipAmount()) {
            totalTips += p.getTipAmount();
          }
        }
      }
      return new Price(totalTips);
    }

    @Override
    public Decimal getPercentServiceCharge() {
      ServiceCharge sc = order.getServiceCharge();
      Decimal percentageDecimal = null;
      if (sc != null) {
        if (sc.getPercentageDecimal() != null) {
          percentageDecimal = new Decimal(sc.getPercentageDecimal()).divide(SERVICE_CHARGE_DIVISOR);
        } else if (sc.getPercentage() != null) {
          percentageDecimal = new Decimal(sc.getPercentage());
        }
      }
      if (sc == null || percentageDecimal == null) {
        return Decimal.ZERO;
      }
      return percentageDecimal;
    }
  }

  private class CalcLineItem implements Calc.LineItem {

    LineItem line;

    CalcLineItem(LineItem line) {
      if (line == null) {
        throw new NullPointerException("line cannot be null");
      }
      this.line = line;
    }

    @Override
    public Price getPrice() {
      if (line.getPrice() != null) {
        return new Price(line.getPrice());
      } else {
        return null;
      }
    }

    @Override
    public Decimal getUnitQuantity() {
      if (line.getUnitQty() == null) {
        return null;
      }
      return new Decimal(line.getUnitQty()).divide(UNIT_QUANTITY_DIVISOR);
    }

    @Override
    public boolean isRefunded() {
      return line.isNotNullRefunded() && line.getRefunded();
    }

    @Override
    public boolean isExchanged() {
      return line.isNotNullExchanged() && line.getExchanged();
    }

    @Override
    public Collection<Calc.TaxRate> getTaxRates() {
      List<Calc.TaxRate> taxRates = Lists.newArrayList();
      if (line.isNotNullTaxRates()) {
        for (TaxRate rate : line.getTaxRates()) {
          taxRates.add(new TaxRateCalc(rate));
        }
      }
      return taxRates;
    }

    @Override
    public Price getModification() {
      if (!line.isNotNullModifications()) {
        return Price.ZERO;
      }
      long modTotal = 0;
      for (Modification m : line.getModifications()) {
        if (m.getAmount() != null) {
          modTotal += m.getAmount();
        }
      }
      return new Price(modTotal);
    }

    @Override
    public Price getAmountDiscount() {
      long discount = -sumOfAdjustments(line.getDiscounts());
      return new Price(discount);
    }

    @Override
    public Collection<Decimal> getPercentDiscounts() {
      return OrderCalc.getPercentDiscounts(line.getDiscounts());
    }

    @Override
    public boolean allowNegativePrice() {
      return order.isNotNullManualTransaction() && order.getManualTransaction();
    }

    public static final String KEY_PERCENT = "percent";

    @Override
    public Decimal getSplitPercent() {
      // TODO: consider removing when making this sdk public - used for transient calculations
      Bundle bundle = line.getBundle();
      LineItemPercent lineItemPercent = bundle.getParcelable(KEY_PERCENT);
      if (lineItemPercent != null) {
        return lineItemPercent.percent.multiply(HUNDRED);
      }
      return HUNDRED;
    }
  }

  private class TaxRateCalc implements Calc.TaxRate {
    private TaxRate taxRate;

    TaxRateCalc(TaxRate taxRate) {
      if (taxRate == null) {
        throw new NullPointerException("taxRate cannot be null");
      }
      this.taxRate = taxRate;
    }

    @Override
    public String getId() {
      return taxRate.getId();
    }

    @Override
    public Decimal getRate() {
      return new Decimal(taxRate.getRate()).divide(TAX_RATE_DIVISOR);
    }

    public Long getRateAsLong() {
      return taxRate.getRate();
    }

    @Override
    public Price getFlatTaxAmount() {
      return taxRate.isNotNullTaxAmount() ? new Price(taxRate.getTaxAmount()) : null;
    }

    @Override
    public String getTaxType() {
      return taxRate.isNotNullTaxType() ? taxRate.getTaxType().toString() : null;
    }

    public String getName() {
      return taxRate.getName();
    }

    public int hashCode() {
      return (getId() != null ? getId().hashCode() : super.hashCode());
    }

    public boolean equals(Object o) {
      if (o instanceof Calc.TaxRate) {
        return getId() != null ? getId().equals(((Calc.TaxRate) o).getId()) : super.equals(o);
      }
      return false;
    }
  }

  /**
   * Constructs a new instance to calculate values for the given order. Does
   * not make a copy of the provided order.
   */
  public OrderCalc(Order order) {
    this.order = order;
    this.calcOrder = new CalcOrder();
  }

  private Calc getCalc() {
    return new Calc(calcOrder, logger);
  }

  private static long sumOfAdjustments(Collection<Discount> discounts) {
    if (discounts == null) {
      return 0;
    }

    long total = 0;
    for (Discount d : discounts) {
      if (d.getAmount() != null) {
        total += d.getAmount();
      }
    }
    return total;
  }

  private static Collection<Decimal> getPercentDiscounts(Collection<Discount> discounts) {
    if (discounts == null || discounts.isEmpty()) {
      return Collections.emptyList();
    }
    List<Decimal> result = Lists.newArrayList();
    for (Discount d : discounts) {
      Long percent = d.getPercentage();
      if (percent != null) {
        result.add(new Decimal(percent, 0));
      }
    }
    return result;
  }

  public long getTax() {
    return getCalc().getTax().getCents();
  }

  public long getTax(Collection<LineItem> lines) {
    if (lines == null) {
      return 0;
    }
    Price tax = getCalc().getTax(toCalcLines(lines));
    return tax.getCents();
  }

  public long getTip() {
    return calcOrder.getTip().getCents();
  }

  public long getTotal(Collection<LineItem> lines) {
    return getCalc().getTotal(toCalcLines(lines)).getCents();
  }

  private List<CalcLineItem> toCalcLines(Collection<LineItem> lines) {
    List<CalcLineItem> calcLines = Lists.newArrayList();
    if (lines != null) {
      for (LineItem line : lines) {
        calcLines.add(new CalcLineItem(line));
      }
    }
    return calcLines;
  }

  public long getDiscountedSubtotal(Collection<LineItem> lines) {
    return getCalc().getDiscountedSubtotal(toCalcLines(lines)).getCents();
  }

  public long getLineSubtotal(Collection<LineItem> lines) {
    return getCalc().getLineSubtotal(toCalcLines(lines)).getCents();
  }

  public long getLineSubtotalBeforeRefunds(final Collection<LineItem> lines) {
    return getCalc().getLineSubtotalBeforeRefunds(toCalcLines(lines)).getCents();
  }

  public long getLineSubtotalWithoutDiscounts(Collection<LineItem> lines) {
    return getCalc().getLineSubtotalWithoutDiscounts(toCalcLines(lines)).getCents();
  }

  /** @return the line item price without discounts and modifiers. */
  public long getLineExtendedPrice(LineItem line) {
    return getCalc().getExtendedPrice(new CalcLineItem(line)).getCents();
  }

  public long getServiceCharge() {
    return getCalc().getServiceCharge().getCents();
  }

  public long getServiceCharge(Collection<LineItem> lines) {
    return getCalc().getServiceCharge(toCalcLines(lines)).getCents();
  }

  public long getServiceChargeBeforeRefunds() {
    return getCalc().getServiceChargeBeforeRefunds().getCents();
  }

  public long getServiceChargeBeforeRefunds(Collection<LineItem> lines) {
    return getCalc().getServiceChargeBeforeRefunds(toCalcLines(lines)).getCents();
  }

  public long getTotalWithTip(Collection<LineItem> lines) {
    Price total = getCalc().getTotal(toCalcLines(lines));
    return total.add(calcOrder.getTip()).getCents();
  }

  public List<Calc.TaxSummary> getTaxSummaries(Collection<LineItem> lines) {
    return getCalc().getTaxSummaries(toCalcLines(lines));
  }

  public Decimal getDiscountMultiplier() {
    return getCalc().getDiscountMultiplier();
  }

  public Calc.PaymentDetails getPaymentDetails(long paymentAmount) {
    return getCalc().getPaymentDetails(new Price(paymentAmount));
  }

  public Calc.PaymentDetails getPaymentDetails(long paymentAmount, Collection<LineItem> lines) {
    return getCalc().getPaymentDetails(new Price(paymentAmount), toCalcLines(lines));
  }

  /**
   * Pass VAT inclusive price Line Item and get back VAT exclusive price
   *
   * @param lineItem input Line Item whose VAT exclusive price needs to find out
   * @return VAT exclusive price
   * @throws IllegalArgumentException if price on Line item is negative or Flat tax rate on line Item is grater than item
   *                                  itself
   */
  public long getPriceWithoutVAT(LineItem lineItem) {
    CalcLineItem calcLineItem = new CalcLineItem(lineItem);
    return getCalc().getPriceWithoutVat(calcLineItem).getCents();
  }

  /**
   * Pass VAT exclusive price Line Item and get back VAT inclusive price
   *
   * @param lineItem input Line Item whose VAT inclusive price needs to find out
   * @return VAT inclusive price
   * @throws IllegalArgumentException if price on Line Item is negative
   */
  public long getPriceWithVAT(LineItem lineItem) {
    CalcLineItem calcLineItem = new CalcLineItem(lineItem);
    return getCalc().getPriceWithVat(calcLineItem).getCents();
  }
}
