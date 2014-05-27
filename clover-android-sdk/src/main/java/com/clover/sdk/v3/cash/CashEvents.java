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
