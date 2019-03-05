package com.clover.sdk.cashdrawer;

import android.content.Context;

import java.util.HashSet;
import java.util.Set;

/**
 * This class provides functions for working with cash drawers. Specifically, it allows clients to list all
 * connected cash drawers from registered hardware types. By default this includes support for the following
 * hardware types,
 * <ul>
 *   <li>Clover Station Printer connected cash drawer</li>
 *   <li>Clover Mini Printer connected cash drawer</li>
 *   <li>APG 554a USB cash drawer</li>
 * </ul>
 * <p>
 * This class differs from the {@link com.clover.sdk.v1.printer.CashDrawer} class in that it abstracts any hardware
 * cash drawer, where {@link com.clover.sdk.v1.printer.CashDrawer} only works with cash drawers connected to
 * configured Clover printers.
 */
public class CashDrawers {
  private final Context context;
  private final Set<CashDrawer.Discovery<? extends CashDrawer>> discoverers = new HashSet<>();

  public CashDrawers(Context context) {
    this.context = context.getApplicationContext();

    discoverers.add(new StationPrinterCashDrawer.Discovery(context));
    discoverers.add(new MiniPrinterCashDrawer.Discovery(context));
    discoverers.add(new APG554aCashDrawer.Discovery(context));
    discoverers.add(new Station2018PrinterCashDrawer.Discovery(context));
  }

  /**
   * @return The set of all connected cash drawers for all registered hardware types.
   */
  public Set<CashDrawer> list() {
    Set<CashDrawer> drawers = new HashSet<>();
    for (CashDrawer.Discovery<? extends CashDrawer> d: discoverers) {
      drawers.addAll(d.list());
    }
    return drawers;
  }

  /**
   * Register a new cash drawer hardware type. Subsequent calls to {@link #list()} will return cash drawers
   * of this hardware type, if the implementation reports them as connected.
   */
  public void registerDiscovery(CashDrawer.Discovery<?> d) {
    discoverers.add(d);
  }
}
