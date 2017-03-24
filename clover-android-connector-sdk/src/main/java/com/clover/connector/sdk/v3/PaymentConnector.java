/**
 * Copyright (C) 2016 Clover Network, Inc.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clover.connector.sdk.v3;

import com.clover.sdk.v1.ServiceConnector;

import android.accounts.Account;
import android.content.Context;

public class PaymentConnector extends PaymentV3Connector {
  /**
   * Constructs a new PaymentConnector object.
   *
   * @param context the Context object, required for establishing a connection to
   *                the service.
   * @param account the Account to use with the service.
   * @param client  an optional object implementing the OnServiceConnectedListener
   */
  public PaymentConnector(Context context, Account account, ServiceConnector.OnServiceConnectedListener client) {
    super(context, account, client);
  }
}
