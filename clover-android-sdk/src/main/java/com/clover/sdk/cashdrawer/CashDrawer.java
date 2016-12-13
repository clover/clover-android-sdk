package com.clover.sdk.cashdrawer;

import android.content.Context;

import java.util.Set;

/**
 * An abstraction for a cash drawer. Implement this class to add support for cash drawers not supported by Clover.
 */
public abstract class CashDrawer {
  /**
   * An method for discovering connected cash drawers.
   * @param <T>
   */
  public static abstract class Discovery<T extends CashDrawer> {
    protected final Context context;

    protected Discovery(Context context) {
      this.context = context.getApplicationContext();
    }

    /**
     * List connected cash drawers of this type.
     */
    public abstract Set<T> list();
  }

  protected final Context context;
  protected final int drawerNumber;

  protected CashDrawer(Context context, int drawerNumber) {
    this.context = context.getApplicationContext();
    this.drawerNumber = drawerNumber;
  }

  /**
   * Pop (open) this cash drawer.
   *
   * This method may perform blocking IO operations depending on the specific cash drawer hardware.
   * It should be called from a background (non-UI) thread.
   *
   * @return <code>true</code> if the hardware received the command to open the cash drawer,
   * otherwise <code>false</code>. Note that a <code>true</code> return value does not necessarily mean
   * the drawer physically opened.
   */
  public abstract boolean pop();

  /**
   * Get this cash drawer's number (typically either <code>1</code> or <code>2</code>).
   *
   * @return This cash drawer's number.
   */
  public int getDrawerNumber() {
    return drawerNumber;
  }

  public abstract String toString();
}
