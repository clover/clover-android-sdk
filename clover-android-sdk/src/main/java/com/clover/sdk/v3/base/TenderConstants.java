/**
 * Copyright (C) 2015 Clover Network, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clover.sdk.v3.base;

public class TenderConstants {

  public static final String CASH = "com.clover.tender.cash";
  public static final String CREDIT_CARD = "com.clover.tender.credit_card";
  public static final String CHECK = "com.clover.tender.check";
  public static final String EXTERNAL_GIFT_CARD = "com.clover.tender.external_gift_card";
  public static final String EXTERNAL_PAYMENT = "com.clover.tender.external_payment";
  public static final String EXTERNAL_PIN_DEBIT = "com.clover.tender.external_pin_debit";
  public static final String DEBIT_CARD = "com.clover.tender.debit_card";

  private TenderConstants() {
  }

  public static boolean isCreditOrDebit(final String labelKey) {
    return isSystemCreditCard(labelKey) || isSystemDebitCard(labelKey);
  }

  public static boolean isSystemCreditCard(final String labelKey) {
    return isLabelKey(labelKey, CREDIT_CARD);
  }

  public static boolean isSystemDebitCard(final String labelKey) {
    return isLabelKey(labelKey, DEBIT_CARD);
  }

  public static boolean isSystemExternalPayment(final String labelKey) {
    return isLabelKey(labelKey, EXTERNAL_PAYMENT);
  }

  public static boolean isSystemCash(final String labelKey) {
    return isLabelKey(labelKey, CASH);
  }

  public static boolean isSystemCheck(final String labelKey) {
    return isLabelKey(labelKey, CHECK);
  }


  public static boolean isSystemExternalPinDebit(final String labelKey) {
    return isLabelKey(labelKey, EXTERNAL_PIN_DEBIT);
  }

  public static boolean isSystemTender(final String labelKey) {
    if (labelKey == null) {
      return false;
    }
    return isSystemCreditCard(labelKey) || isSystemCash(labelKey) || isSystemCheck(labelKey) || isSystemExternalPayment(labelKey)  || isSystemExternalPinDebit(labelKey);
  }

  public static boolean isLabelKey(final String labelKey,final String type) {
    return type.equals(labelKey);
  }
}
