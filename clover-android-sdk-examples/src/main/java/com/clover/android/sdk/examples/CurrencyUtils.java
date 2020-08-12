package com.clover.android.sdk.examples;

import com.clover.sdk.v1.merchant.Merchant;

import android.content.Context;

import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Locale;

public final class CurrencyUtils {

  private final Context ctx;
  private final Merchant merchant;

  public CurrencyUtils(Context ctx, Merchant merchant) {
    this.ctx = ctx.getApplicationContext();
    this.merchant = merchant;
  }

  public Currency getMerchantCurrency() {
    return merchant.getCurrency();
  }

  public Locale getMerchantLocale() {
    return merchant.getLocale();
  }

  // Take a long and convert it to an amount string (i.e 150 -> $1.50)
  public String longToAmountString(long amt) {
    Currency currency = getMerchantCurrency();
    DecimalFormat decimalFormat = getCurrencyFormatInstance();
    decimalFormat.setMinimumFractionDigits(currency.getDefaultFractionDigits());
    decimalFormat.setMaximumFractionDigits(currency.getDefaultFractionDigits());
    return longToAmountString(currency, amt, decimalFormat);
  }

  private double longToDecimal(double num, Currency currency) {
    return num / Math.pow(10, currency.getDefaultFractionDigits());
  }

  // Take a long and convert it to an amount string (i.e 150 -> $1.50)
  private String longToAmountString(Currency currency, long amt, DecimalFormat decimalFormat) {
    return decimalFormat.format(longToDecimal(amt, currency));
  }

  // DecimalFormat is not thread safe
  private final ThreadLocal<DecimalFormat> DECIMAL_FORMAT = new ThreadLocal<DecimalFormat>() {
    @Override
    protected DecimalFormat initialValue() {
      DecimalFormat df = (DecimalFormat) DecimalFormat.getCurrencyInstance(getMerchantLocale());
      df.setCurrency(getMerchantCurrency());
      return df;
    }
  };

  private DecimalFormat getCurrencyFormatInstance() {
    return DECIMAL_FORMAT.get();
  }

}
