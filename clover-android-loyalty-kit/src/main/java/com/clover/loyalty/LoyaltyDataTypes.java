package com.clover.loyalty;
/*
 * Copyright (C) 2016 Clover Network, Inc.
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

import com.clover.sdk.v1.Intents;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class LoyaltyDataTypes {

  // We have some types we define as standards
  public static final String VAS_TYPE = "VAS";
  public static final String EMAIL_TYPE = "EMAIL";
  public static final String PHONE_TYPE = "PHONE";
  public static final String CLEAR_TYPE = "CLEAR";
  public static final String QUICKPAY_TYPE = "QUICKPAY";

  public class VAS_TYPE_KEYS {
    public static final String PUSH_URL = "PUSH_URL";
    public static final String PROTOCOL_CONFIG = "PROTOCOL_CONFIG";
    public static final String PROTOCOL_ID = "PROTOCOL_ID";
    public static final String PROVIDER_PACKAGE = "PROVIDER_PACKAGE";
    public static final String PUSH_TITLE = "PUSH_TITLE";
    public static final String SUPPORTED_SERVICES = "SUPPORTED_SERVICES";
  }

  public class QUICKPAY_TYPE_KEYS {
    public static final String QUICKPAY_DEFERRED_READ = "QUICKPAY_DEFERRED_READ";
    public static final String QUICKPAY_DEFERRED_READ_CLEAR = "QUICKPAY_DEFERRED_READ_CLEAR";
    // These two map to values that are returned by QuickPay
    public static final String QUICKPAY_DEFERRED_ID = "QUICKPAY_DEFERRED_ID";
    public static final String QUICKPAY_DATA_EXTRAS = "QUICKPAY_DATA_EXTRAS";

    public static final String QUICKPAY_FORCE_SHUTDOWN = "QUICKPAY_FORCE_SHUTDOWN";

    // Following two tied to common-payments/src/main/java/com/clover/commonpayments/SecurePayServiceConstants
    public static final String QUICKPAY_DATA_EXTRA_KEY_CARD_TYPE = "cardType";
    public static final String QUICKPAY_DATA_EXTRA_KEY_LAST4 = "last4";

    // !!!!!!!!!
    // See com.clover.loyalty.quickPay.QuickPayLoyaltyDataService.Config for usage of the below
    // values.
    // These constant names are tied to this object via serialization.
    // !!!!!!!!!
    // These map to configuration values passed in to QuickPay
    public static final String QUICKPAY_APP_SPECIFIC_MAP = "QUICKPAY_APP_SPECIFIC_MAP";
    public static final String QUICKPAY_CARD_ENTRY_METHODS = "QUICKPAY_CARD_ENTRY_METHODS";
    public static final String QUICKPAY_FORCE_PIN_ENTRY_ON_SWIPE = "QUICKPAY_FORCE_PIN_ENTRY_ON_SWIPE";
    public static final String QUICKPAY_VAS_CONFIG = "QUICKPAY_VAS_CONFIG";

    // Values that will be able to be passed in addition to the amount.
    // Values are all expected to be strings.  They can be missing or blank (but don't pass empty strings...)
    // see secure-payments-lib/src/main/java/com/clover/payment/executor/secure/transaction/TransactionState.updateFinalAmount.
    //
    // Code snippets below are provided for some context on how to put the values into the additionalUpdate map
    // see https://phabricator.dev.clover.com/D33609
    //    builder.tipAmount(Long.parseLong(tipAmount));
    public static final String QUICKPAY_EXTRA_TIP_AMOUNT = Intents.EXTRA_TIP_AMOUNT;
    //    builder.taxAmount(Long.parseLong(taxAmount));
    public static final String QUICKPAY_EXTRA_TAX_AMOUNT = Intents.EXTRA_TAX_AMOUNT;
    //    builder.orderId(orderId);
    public static final String QUICKPAY_EXTRA_ORDER_ID = Intents.EXTRA_ORDER_ID;
    //    try {
    //      InputStream is = new ByteArrayInputStream(additionalAmountsJson.getBytes(Charset.forName("UTF-8")));
    //      List<TaxableAmountRate> taxRates = Json.read(is, new TypeReference<List<TaxableAmountRate>>() {
    //      });
    //      builder.taxableAmountRates(taxRates);
    //    } catch (IOException ioe) {
    //      ALog.e(this, ioe, "Unexpected failure reading taxable amounts: %s", ioe.getMessage());
    //    }
    public static final String QUICKPAY_EXTRA_TAXABLE_AMOUNTS = Intents.EXTRA_TAXABLE_AMOUNTS;
    //    try {
    //      InputStream is = new ByteArrayInputStream(serviceCharge.getBytes("UTF-8"));
    //      ServiceChargeAmount svcCharge = Json.read(is, ServiceChargeAmount.class);
    //      builder.serviceChargeAmount(svcCharge);
    //    } catch (IOException ioe) {
    //      ALog.e(this, ioe, "Unexpected failure reading service charge amount: %s", ioe.getMessage());
    //    }
    public static final String QUICKPAY_EXTRA_SERVICE_CHARGE_AMOUNT = Intents.EXTRA_SERVICE_CHARGE_AMOUNT;
    //    builder.externalPaymentId((String)additionalUpdates.get(Intents.EXTRA_EXTERNAL_PAYMENT_ID));
    public static final String QUICKPAY_EXTRA_EXTERNAL_PAYMENT_ID = Intents.EXTRA_EXTERNAL_PAYMENT_ID;
    //    builder.externalReferenceId((String)additionalUpdates.get(Intents.EXTRA_EXTERNAL_REFERENCE_ID));
    public static final String QUICKPAY_EXTRA_EXTERNAL_REFERENCE_ID = Intents.EXTRA_EXTERNAL_REFERENCE_ID;
    //    try {
    //      InputStream is = new ByteArrayInputStream(passThroughJson.getBytes("UTF-8"));
    //      Map<String, String> passThroughValues = Json.read(is, new TypeReference<Map<String, String>>() {});
    //
    //      Map<String, String> existing = new HashMap<>();
    //      if (payIntent.passThroughValues != null) {
    //        existing.putAll(payIntent.passThroughValues);
    //      }
    //      existing.putAll(passThroughValues);
    //      builder.passThroughValues(existing);
    //    } catch (IOException ioe) {
    //      ALog.e(this, ioe, "Unexpected failure reading pass through values: %s", ioe.getMessage());
    //    }
    public static final String QUICKPAY_EXTRA_PASS_THROUGH_VALUES = Intents.EXTRA_PASS_THROUGH_VALUES;
  }

  private static final List<String> dataTypes = new ArrayList<>(3);

  public static boolean isSystemListedType(String type) {
    return (VAS_TYPE.equals(type)) ||
           (EMAIL_TYPE.equals(type)) ||
           (PHONE_TYPE.equals(type)) ||
           (CLEAR_TYPE.equals(type)) ||
           (QUICKPAY_TYPE.equals(type))
        ;
  }

  public static boolean isCustomListedType(String type) {
    return dataTypes.contains(type);
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public static boolean isListedType(String type) {
    return isSystemListedType(type) ||
           isCustomListedType(type);
  }

  public static boolean addListedType(String type) {
    if (!isListedType(type)) {
      return dataTypes.add(type);
    }
    return false; // not added, already there
  }

  public static boolean removeListedType(String type) {
    if (!isListedType(type)) {
      return dataTypes.remove(type);
    }
    return false; //not removed, not there
  }
}
