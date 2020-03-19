package com.clover.sdk.v3.order;

import com.clover.core.internal.calc.Calc;
import com.clover.core.internal.calc.Decimal;
import com.clover.core.internal.calc.Price;
import com.clover.sdk.v3.inventory.TaxRate;

public class TaxRateCalc implements Calc.TaxRate {

  private static final Decimal TAX_RATE_DIVISOR = new Decimal(100000);

  private final TaxRate taxRate;

  public TaxRateCalc(final TaxRate taxRate) {
    if (taxRate == null) {
      throw new NullPointerException("taxRate cannot be null");
    }
    if (taxRate.getRate() == null) {
      throw new NullPointerException(String.format("taxRate.rate cannot be null; id: %s", taxRate.getId()));
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
    return taxRate.getRate() == 0 && taxRate.isNotNullTaxAmount() ? new Price(taxRate.getTaxAmount()) : null;
  }

  @Override
  public String getTaxType() {
    return taxRate.isNotNullTaxType() ? taxRate.getTaxType().toString() : null;
  }

  public String getName() {
    return taxRate.getName();
  }

  public int hashCode() {
    return getId() != null ? getId().hashCode() : super.hashCode();
  }

  public boolean equals(Object o) {
    if (o instanceof Calc.TaxRate) {
      return getId() != null ? getId().equals(((Calc.TaxRate) o).getId()) : super.equals(o);
    }
    return false;
  }
}
