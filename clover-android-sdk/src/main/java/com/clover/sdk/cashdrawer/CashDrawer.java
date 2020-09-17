/*
 * Copyright (C) 2020 Clover Network, Inc.
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

import android.content.Context;

import java.util.Locale;
import java.util.Set;

/**
 * An abstraction for a cash drawer. The identifier and drawer number together may be used to
 * uniquely identify each particular cash drawer. New identifiers may be added as new cash drawers
 * are supported by Clover but values for existing cash drawers will not change and may be saved in
 * persistent storage.
 *
 * Here are example values:
 * <pre>
 *   identifier                     drawer number
 *   ----------                     -------------
 *   com.clover.BAYLEAF             1
 *   com.clover.BAYLEAF             2
 *   com.clover.USB                 1
 *   com.clover.USB                 2
 *   com.clover.USB                 3
 *   com.clover.FUTUREX             1
 *   org.otherapp.XYZ77             1
 * </pre>
 * No two drawers will ever have the same pair of identifier and drawer number.
 */
public abstract class CashDrawer {

  protected static final String TAG = CashDrawer.class.getSimpleName();

  /**
   * A method for discovering connected cash drawers.
   */
  public static abstract class Discovery<T extends CashDrawer> {

    protected final Context context;

    protected Discovery(Context context) {
      this.context = context.getApplicationContext();
    }

    /**
     * @see CashDrawers#list()
     */
    public abstract Set<T> list();
  }

  protected Context context;
  protected final int drawerNumber;

  CashDrawer(int drawerNumber) {
    this.drawerNumber = drawerNumber;
  }

  protected CashDrawer(Context context, int drawerNumber) {
    this.context = context.getApplicationContext();
    this.drawerNumber = drawerNumber;
  }

  /**
   * The identifier String along with the drawer number uniquely identify cash drawer. Some
   * examples (not exhaustive) are:
   * <pre>
   *   com.clover.BAYLEAF
   *   com.clover.USB
   *   org.otherapp.XYZ77
   * </pre>
   * Subclasses written by 3rd party developers should never prefix their cash drawer identifier
   * with "com.clover" to avoid conflicts with cash drawers discovered by Clover Services.
   *
   * @return the identifier, never null
   */
  public abstract String getIdentifier();

  /**
   * Get the cash drawer number for this identifier. Numbers start from 1 and counting up with no
   * gaps. Each identifier has it's own sequence.
   */
  public int getDrawerNumber() {
    return drawerNumber;
  }

  /**
   * A human readable name for this cash drawer. The name does not include the drawer number. The
   * name is adjusted to the currently chosen device locale.
   */
  public abstract String getDisplayName();

  /**
   * Attempt to pop (open) this cash drawer.
   * <p>
   * This method may perform blocking IO operations depending on the specific cash drawer hardware.
   * It must not be called from the main (UI) thread.
   * <p>
   * Direct use of this method is not recommended, prefer to use the
   * {@link com.clover.sdk.v3.cash.CashEvents} class to pop the cash drawer so that cash event
   * logging is also performed.
   *
   * @return false indicates the cash drawer definitely did not open, true indicates the cash
   *     drawer likely opened but possibly did not open due to mechanical or electrical issues or
   *     may simply be locked.
   */
  public abstract boolean pop();

  // Display name is not considered when determining if cash drawers are equal

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CloverServiceCashDrawer that = (CloverServiceCashDrawer) o;

    if (drawerNumber != that.drawerNumber) {
      return false;
    }
    return getIdentifier().equals(that.getIdentifier());
  }

  @Override
  public int hashCode() {
    int result = getIdentifier().hashCode();
    result = 31 * result + drawerNumber;
    return result;
  }

  /**
   * Get a completely unique identifier for this cash drawer as a String which includes the
   * identifier and drawer number.
   */
  public final String getUniqueIdentifier() {
    return String.format(Locale.ROOT, "%s_%03d", getIdentifier(), getDrawerNumber());
  }

}
