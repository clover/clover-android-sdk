package com.clover.sdk.cashdrawer;

import android.accounts.Account;
import android.content.Context;
import android.os.Build;

import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.printer.Category;
import com.clover.sdk.v1.printer.Printer;
import com.clover.sdk.v1.printer.Type;

import java.util.Collections;
import java.util.Set;

/**
 * A cash drawer for Station 2018.
 */
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
