/**
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
  public static final String FUNC = "func";
  public static final String ALIPAY_TRANS_ID = "aliPayTransId";
  public static final String ALIPAY_BUYER_USER_ID = "buyerUserId";
  public static final String ALIPAY_EXCHANGE_RATE = "exchangeRate";
  public static final String ALIPAY_CNY_TRANS_AMOUNT = "cnyTransAmount";
  public static final String WECHAT_PAY_TRANS_ID = "weChatPayTransId";
  public static final String MAC = "mac";
  public static final String MAC_KSN = "macKsn";
  public static final String IPG_TXID = "ipgTxId";
  public static final String PROCESSOR_REFERENCE_NUMBER = "processorReferenceNumber";
  public static final String IPG_MID = "mid";
  public static final String IPG_TID = "tid";
  public static final String IPG_BATCH_NUMBER = "batchNumber";
  public static final String IPG_RECEIPT_NUMBER = "receiptNumber";
  public static final String IPG_RESPONSE_MESSAGE = "responseMessage";
  public static final String IPG_RESPONSE_CODE = "responseCode";
  public static final String IPG_PROMOTIONAL_MESSAGE = "promotionalMessage";
  public static final String NETWORK_TRANS_ID = "networkTransId";
  public static final String TERMINAL_ID = "terminalId";
  public static final String AMEX_SE_ID = "amexServiceEstablishmentNumber";
  public static final String TRANSACTION_CERTIFICATE = "transactionCertificate";
  public static final String ORIGINAL_AMOUNT = "orig_amt";
  public static final String NETWORK_EXTRA_CARD = "card";
  public static final String POS_ENTRY_MODE = "posEntryMode";
  public static final String INSTALLMENT_PLAN_RESPONSE = "installmentPlanResponse";

}
