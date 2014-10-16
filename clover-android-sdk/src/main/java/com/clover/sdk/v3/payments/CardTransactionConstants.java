/*
 * Copyright (C) 2013 Clover Network, Inc.
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
  public static final String AVS = "avs";
  public static final String CVV = "cvv";
  public static final String APPLICATION_IDENTIFIER = "applicationIdentifier";
  public static final String AVAILABLE_OFFLINE_SPENDING_AMOUNT = "availableOfflineSpendingAmount";
  public static final String APPROVAL_CODE = "approvalCode";
  public static final String CVM_RESULT = "cvmResult";
  public static final String STORE_ID = "storeId";
}
