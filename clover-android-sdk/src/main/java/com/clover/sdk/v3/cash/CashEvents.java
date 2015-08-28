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
package com.clover.sdk.v3.cash;

import android.accounts.Account;
import android.content.Context;
import android.os.Bundle;
import com.clover.sdk.v1.printer.CashDrawer;

public class CashEvents {
  public static final String METHOD_ADD_ENTRY = "addEntry";

  public static final String ARG_ACCOUNT = "account";
  public static final String ARG_CASH_EVENT = "cashEvent";

  public static final String ARG_SUCCESS = "success";

  private final Context context;
  private final Account account;

  public CashEvents(Context context, Account account) {
    this.context = context;
    this.account = account;
  }

  public boolean addCash(final long amount, final String note) {
    return amount >= 0 && insertCashAdjustment(amount, note);
  }

  public boolean removeCash(final long amount, final String note) {
    return amount <= 0 && insertCashAdjustment(amount, note);
  }

  private boolean insertCashAdjustment(final long amount, final String note) {
    Bundle extras = new Bundle();
    extras.putParcelable(ARG_ACCOUNT, account);
    CashEvent cashEvent = new CashEvent();
    cashEvent.setType(Type.ADJUSTMENT);
    cashEvent.setAmountChange(amount);
    cashEvent.setNote(note);
    extras.putParcelable(ARG_CASH_EVENT, cashEvent);
    Bundle response = context.getContentResolver().call(CashContract.CashEvent.CONTENT_URI, METHOD_ADD_ENTRY, null, extras);

    if (response != null && response.getBoolean(ARG_SUCCESS, false)) {
      CashDrawer.open(context, account);
      return true;
    }
    return false;
  }
}
