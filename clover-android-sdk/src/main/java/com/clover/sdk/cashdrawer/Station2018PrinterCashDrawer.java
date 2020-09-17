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
package com.clover.sdk.cashdrawer;

import android.accounts.Account;
import android.content.Context;
import android.os.Build;

import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.printer.Category;
import com.clover.sdk.v1.printer.Printer;
import com.clover.sdk.v1.printer.Type;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

@Deprecated
class Station2018PrinterCashDrawer extends CashDrawer {

  private static final Type GOLDEN_OAK_USB = new Type("GOLDEN_OAK_USB");

  static class Discovery extends CashDrawer.Discovery<Station2018PrinterCashDrawer> {

    Discovery(Context context) {
      super(context);
    }

    @Override
    public Set<Station2018PrinterCashDrawer> list() {
      if ("goldenoak".equals(Build.DEVICE)) {
        return Collections.singleton(new Station2018PrinterCashDrawer(context));
      }
      return Collections.emptySet();
    }
  }

  private final Account cloverAccount;
  private final Printer stationPrinter;

  private Station2018PrinterCashDrawer(Context context) {
    super(context, 1);
    this.cloverAccount = CloverAccount.getAccount(context);
    this.stationPrinter = new Printer.Builder()
        .type(GOLDEN_OAK_USB)
        .category(Category.RECEIPT)
        .build();
  }

  @Override
  public String getIdentifier() {
    return "CLOVER_ACORN_USB";
  }

  @Override
  public String getDisplayName() {
    return "RJ12 Cash Drawer";
  }

  @SuppressWarnings("deprecated")
  @Override
  public boolean pop() {
    com.clover.sdk.v1.printer.CashDrawer.open(context, cloverAccount, stationPrinter);
    return true;
  }

  @Override
  public String toString() {
    return "Station2018PrinterCashDrawer{" +
        "cloverAccount=" + cloverAccount +
        ", stationPrinter=" + stationPrinter +
        '}';
  }

}
