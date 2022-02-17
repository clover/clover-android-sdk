/*
 * Copyright (C) 2016 Clover Network, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clover.sdk.v3.order;

import android.accounts.Account;
import android.content.Context;

import com.clover.sdk.v1.ServiceConnector;

/**
 * Service connector for {@link IOrderServiceV3_1}. Please see that class for documentation on the
 * RPC methods.
 *
 * @see IOrderServiceV3_1
 * @see ServiceConnector
 * @see Order
 * @see LineItem
 */
public class OrderConnector extends OrderV31Connector {

  /**
   * Constructs a new OrderConnector object. This class will use the {@link IOrderServiceV3_1}
   * service.
   *
   * @param context the Context object, required for establishing a connection to
   *                the service.
   * @param account the Account to use with the service.
   * @param client  an optional object implementing the OnServiceConnectedListener
   */
  public OrderConnector(Context context, Account account, OnServiceConnectedListener client) {
    super(context, account, client);
  }
}
