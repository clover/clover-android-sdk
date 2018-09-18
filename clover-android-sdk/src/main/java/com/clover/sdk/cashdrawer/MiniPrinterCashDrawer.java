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

class MiniPrinterCashDrawer extends CashDrawer {

  private static final Type SEIKO_MINI_USB = new Type("SEIKO_MINI_USB");

  static class Discovery extends CashDrawer.Discovery<MiniPrinterCashDrawer> {

    protected Discovery(Context context) {
      super(context);
    }

    @Override
    public Set<MiniPrinterCashDrawer> list() {
      if ("maplecutter".equals(Build.DEVICE) || "knottypine".equals(Build.DEVICE)) {
        return Collections.singleton(new MiniPrinterCashDrawer(context));
      }
      return Collections.emptySet();
    }
  }

  private final Account cloverAccount;
  private final Printer miniPrinter;

  protected MiniPrinterCashDrawer(Context context) {
    super(context, 1);
    this.cloverAccount = CloverAccount.getAccount(context);
    this.miniPrinter = new Printer.Builder().type(SEIKO_MINI_USB).category(Category.RECEIPT).build();
  }

  @Override
  public boolean pop() {
    com.clover.sdk.v1.printer.CashDrawer.open(context, cloverAccount, miniPrinter);
    return true;
  }

  @Override
  public String toString() {
    return "MiniPrinterCashDrawer{" +
        "cloverAccount=" + cloverAccount +
        ", miniPrinter=" + miniPrinter +
        '}';
  }

}
