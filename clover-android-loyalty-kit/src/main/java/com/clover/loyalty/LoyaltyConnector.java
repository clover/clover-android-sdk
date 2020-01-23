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

import android.accounts.Account;
import android.content.Context;

import java.util.Map;

public class LoyaltyConnector extends LoyaltyV3Connector implements ILoyaltyKit {

  public LoyaltyConnector(Context context, Account account, OnServiceConnectedListener client) {
    super(context, account, client);
  }

  @Override
  public boolean startLoyaltyService(String dynamicService, Map<String, String> dataExtras, String configuration) throws Exception {
    return start(dynamicService, dataExtras, configuration);
  }

  @Override
  public boolean stopLoyaltyService(String dynamicService) throws Exception {
    return stop(dynamicService);
  }

  @Override
  public boolean stopLoyaltyService(String dynamicService, Map<String, String> dataExtras) throws Exception {
    return stop(dynamicService, dataExtras);
  }
}
