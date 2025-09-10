package com.clover.sdk.v3.payments;

public final class RegionalExtras {
  //As of now, these are Argentina specific keys
  public static final String FISCAL_INVOICE_NUMBER_KEY = "com.clover.regionalextras.ar.FISCAL_INVOICE_NUMBER_KEY";
  public static final String INSTALLMENT_NUMBER_KEY = "com.clover.regionalextras.ar.INSTALLMENT_NUMBER_KEY";
  public static final String INSTALLMENT_PLAN_KEY = "com.clover.regionalextras.ar.INSTALLMENT_PLAN_KEY";
  public static final String MERCHANT_ID_KEY = "com.clover.regionalextras.ar.MERCHANT_ID_KEY";

  //New regional extras to add a selected currency
  public static final String TX_EXTRA_CURRENCY = "com.clover.regionalextras.EXTRA_CURRENCY";

  // Keys to be used in Argentina as filters inside the RegionalExtras
  public static final String CARD_SYMBOL_KEY = "com.clover.regionalextras.ar.CARD_SYMBOL_KEY";
  public static final String CASHBACK_AMOUNT_KEY = "com.clover.regionalextras.ar.CASHBACK_AMOUNT_KEY";
  public static final String SUB_MERCHANT_KEY = "com.clover.regionalextras.ar.SUB_MERCHANT_KEY";
  public static final String BUSINESS_ID_KEY = "com.clover.regionalextras.ar.BUSINESS_ID_KEY";
  public static final String DYNAMIC_MERCHANT_NAME_KEY = "com.clover.regionalextras.ar.DYNAMIC_MERCHANT_NAME_KEY";


  //  Values - can be used in conjunction with keys to induce the desired effect during
  //  the processing of a transaction on the Clover payment device.

  //  Use with FISCAL_INVOICE_NUMBER_KEY
  public static final String SKIP_FISCAL_INVOICE_NUMBER_SCREEN_VALUE = "com.clover.regionalextras.ar.SKIP_FISCAL_INVOICE_NUMBER_SCREEN_VALUE";
  public static final String CONFIRM_FISCAL_INVOICE_NUMBER_PROVIDED_VALUE = "com.clover.regionalextras.ar.CONFIRM_FISCAL_INVOICE_NUMBER_PROVIDED_VALUE";

  //  Use with INSTALLMENT_NUMBER_KEY
  public static final String INSTALLMENT_NUMBER_DEFAULT_VALUE = "1";
}
