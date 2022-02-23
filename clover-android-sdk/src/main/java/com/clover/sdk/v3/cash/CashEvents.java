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
package com.clover.sdk.v3.cash;

import android.accounts.Account;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.clover.sdk.cashdrawer.CashDrawer;
import com.clover.sdk.cashdrawer.CashDrawers;
import com.clover.sdk.internal.util.UnstableCallClient;
import com.clover.sdk.internal.util.UnstableContentResolverClient;

import java.util.Set;

/**
 * This interface provides a mechanism to pop a cash drawer and record miscellaneous cash events,
 * it should not be used not for cash payments or refunds.
 * <p/>
 * To read recorded cash events use the {@link CashContract}.
 *
 * @see CashDrawers
 */
public class CashEvents {

  private static final String TAG = CashEvents.class.getSimpleName();

  /**
   * For internal use only.
   */
  public static final String METHOD_ADD_ENTRY = "addEntry";

  /**
   * Extra type: {@link android.accounts.Account}. This is not an arg, actually an extra.
   * <p/>
   * For internal use only.
   */
  public static final String ARG_ACCOUNT = "account";

  /**
   * Extra type: {@link CashEvent}. This is not an arg, actually an extra.
   * <p/>
   * For internal use only.
   */
  public static final String ARG_CASH_EVENT = "cashEvent";

  /**
   * Extra type: String.
   * <p/>
   * For internal use only.
   *
   * @see CashDrawer#getIdentifier()
   */
  public static final String EXTRA_CASH_DRAWER_IDENTIFIER = "cashDrawerIdentifier";

  /**
   * Extra type: Integer.
   * <p/>
   * For internal use only.
   *
   * @see CashDrawer#getDrawerNumber()
   */
  public static final String EXTRA_CASH_DRAWER_NUMBER = "cashDrawerNumber";

  /**
   * For internal use only.
   */
  public static final String ARG_SUCCESS = "success";

  private final Context context;
  private final Account account;

  /**
   * Constructor.
   *
   * @param context
   * @param account Obtain account with {@link com.clover.sdk.util.CloverAccount#getAccount(Context)}
   */
  public CashEvents(Context context, Account account) {
    this.context = context.getApplicationContext();
    this.account = account;
  }

  /**
   * Record a miscellaneous "add cash" event and pops one cash drawer. Not for
   * payments or refunds.
   * <p/>
   * This method may perform blocking IO operations depending on the specific cash drawer hardware.
   * It must not be called from the main (UI) thread.
   *
   * @param amount amount being added, must be positive
   * @param note human readable string explaining why cash was added
   * @return false if the cash event was not recorded, this may occur because the cash drawer did
   *     not pop or there was an internal error with Clover services
   */
  public boolean addCash(long amount, String note) {
    return amount >= 0 && insertCashAdjustment(amount, note, null);
  }

  /**
   * Record a miscellaneous "remove cash" event and pops one cash drawer. Not for
   * payments or refunds.
   * <p/>
   * This method may perform blocking IO operations depending on the specific cash drawer hardware.
   * It must not be called from the main (UI) thread.
   *
   * @param amount amount being removed, must be negative
   * @param note human readable string explaining why cash was added
   * @return false if the cash event was not recorded, this may occur because the cash drawer did
   *     not pop or there was an internal error with Clover services
   */
  public boolean removeCash(long amount, String note) {
    return amount <= 0 && insertCashAdjustment(amount, note, null);
  }

  /**
   * Record a miscellaneous "add cash" event and pops the given cash drawer. Not for
   * payments or refunds.
   * <p/>
   * This method may perform blocking IO operations depending on the specific cash drawer hardware.
   * It must not be called from the main (UI) thread.
   *
   * @param amount amount being added, must be positive
   * @param note human readable string explaining why cash was added
   * @param cashDrawer the drawer to pop and have the event recorded against
   * @return false if the cash event was not recorded, this may occur because the cash drawer did
   *     not pop or there was an internal error with Clover services
   *
   * @see CashDrawers
   */
  public boolean addCash(long amount, String note, CashDrawer cashDrawer) {
    return amount >= 0 && insertCashAdjustment(amount, note, cashDrawer);
  }

  /**
   * Record a miscellaneous "remove cash" event and pops the given cash drawer. Not for
   * payments or refunds.
   * <p/>
   * This method may perform blocking IO operations depending on the specific cash drawer hardware.
   * It must not be called from the main (UI) thread.
   *
   * @param amount amount being removed, must be negative
   * @param note human readable string explaining why cash was added
   * @param cashDrawer the drawer to pop and have the event recorded against
   * @return false if the cash event was not recorded, this may occur because the cash drawer did
   *     not pop or there was an internal error with Clover services
   *
   * @see CashDrawers
   */
  public boolean removeCash(long amount, String note, CashDrawer cashDrawer) {
    return amount <= 0 && insertCashAdjustment(amount, note, cashDrawer);
  }

  private boolean insertCashAdjustment(long amount, String note, CashDrawer cashDrawer) {
    CashEvent cashEvent = new CashEvent();
    cashEvent.setType(Type.ADJUSTMENT);
    cashEvent.setAmountChange(amount);
    cashEvent.setNote(note);

    Bundle extras = new Bundle();
    extras.putParcelable(ARG_ACCOUNT, account);
    extras.putParcelable(ARG_CASH_EVENT, cashEvent);

    // If no cash drawer was provided try to find one
    if (cashDrawer == null) {
      Set<CashDrawer> drawers = new CashDrawers(context).list();
      if (drawers.size() == 0) {
        Log.w(TAG, "No cash drawer found to open, cash event not recorded");
        return false;
      }

      // Just use the first cash drawer, whatever it is
      cashDrawer = drawers.iterator().next();
    }

    // First try to pop, if that fails there is no point in recording the event because the
    // employee couldn't actually do anything (pop only returns false when the drawer certainly
    // did not open)
    if (!cashDrawer.pop()) {
      Log.w(TAG, "Cash drawer pop failed, cash event not recorded");
      return false;
    }

    // Record which cash drawer was affected
    extras.putString(EXTRA_CASH_DRAWER_IDENTIFIER, cashDrawer.getIdentifier());
    extras.putInt(EXTRA_CASH_DRAWER_NUMBER, cashDrawer.getDrawerNumber());

    // Record the event
    UnstableContentResolverClient client = new UnstableContentResolverClient(context.getContentResolver(),
        CashContract.CashEvent.CONTENT_URI);
    Bundle response = client.call(METHOD_ADD_ENTRY, null, extras, null);
    if (response == null || !response.getBoolean(ARG_SUCCESS, false)) {
      Log.w(TAG, "Cash event recording error, cash event not recorded");
      return false;
    }

    return true;
  }
}
