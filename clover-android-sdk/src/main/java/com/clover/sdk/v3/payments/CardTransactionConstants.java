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
package com.clover.sdk.v3.payments;

/**
 * Contains constant string identifiers (keys) for extra info that can be contained in a card transaction
 */
public class CardTransactionConstants {
  public static final String VERIFICATION = "verification";
  public static final String AVS = "avs";
  public static final String CVV = "cvv";
  public static final String APPLICATION_IDENTIFIER = "applicationIdentifier";
  public static final String APPLICATION_LABEL = "applicationLabel";
  public static final String AVAILABLE_OFFLINE_SPENDING_AMOUNT = "availableOfflineSpendingAmount";
  public static final String APPROVAL_CODE = "approvalCode";
  public static final String CVM_RESULT = "cvmResult";
  public static final String STORE_ID = "storeId";
  public static final String AUTHORIZING_NETWORK_NAME = "authorizingNetworkName";
  public static final String ROUTING_INDICATOR = "routingIndicator";
  public static final String SIGNATURE_INDICATOR = "signatureIndicator";
  public static final String GERMANY_RECEIPT_TYPE = "gerReceiptType";
  public static final String GERMANY_CONFIG_PRODUCT_LABEL = "gerConfigProductLabel";
  public static final String GERMANY_TRANSACTION_TYPE = "gerTransactionType";
  public static final String GERMANY_RECEIPT_NUMBER = "gerReceiptNumber";
  public static final String GERMANY_TRACE_NUMBER = "gerTraceNumber";
  public static final String GERMANY_OLD_TRACE_NUMBER = "gerOldTraceNumber";
  public static final String GERMANY_TX_DATE = "gerTxDate";
  public static final String GERMANY_TX_TIME = "gerTxTime";
  public static final String GERMANY_CARD_PAN = "gerCardPan";
  public static final String GERMANY_CARD_SEQ_NUMBER = "gerCardSeqNumber";
  public static final String GERMANY_EXPIRATION_DATE = "gerExpirationDate";
  public static final String GERMANY_CUSTOMER_DOL = "gerCustomerPaymentDOL";
  public static final String GERMANY_MERCHANT_DOL = "gerMerchantPaymentDOL";
  public static final String GERMANY_CONFIG_MERCHANT_ID = "gerConfigMerchantId";
  public static final String GERMANY_HOST_PRINT_DATA_BM60 = "gerHostResponsePrintDataBM60";
  public static final String GERMANY_HOST_AID_PAR_BMP53 = "gerHostResponseAidParBMP53";
  public static final String GERMANY_TERMINAL_ID = "gerTerminalID";
}
