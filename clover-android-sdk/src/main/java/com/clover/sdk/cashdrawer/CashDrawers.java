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

import com.clover.sdk.internal.util.UnstableCallClient;
import com.clover.sdk.internal.util.UnstableContentResolverClient;

import android.content.ContentProviderClient;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * This class provides functions for working with cash drawers. Specifically, it allows clients to
 * list all connected cash drawers from known hardware types. More types of cash drawers may be
 * added over time.
 * <p/>
 * This class differs from the {@link com.clover.sdk.v1.printer.CashDrawer} class in that it
 * abstracts any hardware cash drawer, where {@link com.clover.sdk.v1.printer.CashDrawer} only
 * works with cash drawers connected to configured Clover printers.
 * <p/>
 * Old implementations of this class used a hardcoded list of supported cash drawers, this newer
 * implementation allows Clover to add support for new cash drawer types as they become available.
 *
 */
public class CashDrawers {

  private static final String TAG = CashDrawers.class.getSimpleName();

  /**
   * For internal use only.
   */
  public static final class Contract {
    public static final String CASH_DRAWER2_AUTHORITY = "com.clover.engine.cashdrawerprovider2";

    public static final Uri CASH_DRAWER_AUTHORITY_URI = Uri.parse("content://" + CASH_DRAWER2_AUTHORITY);

    public static final String METHOD_LIST = "list";

    public static final String METHOD_POP = "pop";

    /**
     * Extra of type ArrayList of com.clover.sdk.cashdrawer.CloverServiceCashDrawer
     */
    public static final String EXTRA_CASH_DRAWER_LIST = "listResults";

    /**
     * Extra of type com.clover.sdk.cashdrawer.CloverServiceCashDrawer
     */
    public static final String EXTRA_CLOVER_SERVICE_CASH_DRAWER = "cloverServiceCashDrawer";

    /**
     * Extra of type boolean
     */
    public static final String EXTRA_SUCCESS = "success";
  }

  // Once we determine api 2 is supported no need to keep checking, we cannot go back
  private static Boolean sApi2Supported;

  private final Context context;

  private final List<CashDrawer.Discovery<? extends CashDrawer>> discoverers = new ArrayList<>();

  public CashDrawers(Context context) {
    this.context = context.getApplicationContext();

    if (isCashDrawer2ApiSupported()) {
      CashDrawer.Discovery<CloverServiceCashDrawer> engineDiscovery
          = new CashDrawer.Discovery<CloverServiceCashDrawer>(context) {
        @Override
        public Set<CloverServiceCashDrawer> list() {
          Bundle result = call(Contract.METHOD_LIST);

          Set<CloverServiceCashDrawer> drawerSet = new HashSet<>();
          if (result != null) {
            result.setClassLoader(context.getClassLoader());
            Collection<CloverServiceCashDrawer> drawerList
                = result.getParcelableArrayList(Contract.EXTRA_CASH_DRAWER_LIST);
            if (drawerList != null) {
              for (CloverServiceCashDrawer drawer : drawerList) {
                drawer.context = context;
              }
              drawerSet.addAll(drawerList);
            }
          }
          return drawerSet;
        }
      };

      discoverers.add(engineDiscovery);
    } else {
      // Old hardcoded list of cash drawers, newer cash drawers will not be added here. Eventually
      // this code and the check for api 2 support can be removed once Clover services has been
      // updated for all merchants.
      // Suggest removal in early 2021.
      discoverers.add(new StationPrinterCashDrawer.Discovery(context));
      discoverers.add(new MiniPrinterCashDrawer.Discovery(context));
      discoverers.add(new APG554aCashDrawer.Discovery(context));
      discoverers.add(new Station2018PrinterCashDrawer.Discovery(context));
    }
  }

  /**
   * Discover cash drawers supported at this time by this device. Discovery of a cash drawer does
   * not guarantee that it is actually connected at this very moment. Discovery of a cash drawer
   * only indicates that it is either connected now, or there is a available hardware port allowing
   * one to be connected.
   * <p>
   * For example local RJ12-type cash drawers may or may not actually be connected at the time this
   * method is invoked but will be returned regardless. Other types of cash drawers may only be
   * discovered if they are physically connected such as USB cash drawers.
   * <p>
   * This method may perform blocking I/O and must not be invoked on the main thread.
   *
   * @see CashDrawer
   */
  public Set<CashDrawer> list() {
    if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
      Log.w(TAG, "list method must not be invoked on the main thread",
          new Exception().fillInStackTrace());
    }

    Set<CashDrawer> drawers = new HashSet<>();
    for (CashDrawer.Discovery<? extends CashDrawer> d : discoverers) {
      drawers.addAll(d.list());
    }
    return drawers;
  }

  /**
   * Register a new cash drawer hardware type. Subsequent calls to {@link #list()} will invoke this
   * discovery mechanism.
   */
  public void registerDiscovery(CashDrawer.Discovery<? extends CashDrawer> d) {
    discoverers.add(d);
  }

  private boolean isCashDrawer2ApiSupported() {
    if (Boolean.TRUE.equals(sApi2Supported)) {
      return true;
    }

    ContentProviderClient client = null;
    try {
      client = context.getContentResolver()
          .acquireUnstableContentProviderClient(Contract.CASH_DRAWER_AUTHORITY_URI);
      if (client != null) {
        sApi2Supported = Boolean.TRUE;
        return true;
      }
    } catch (Exception e) {
      // ignored
    } finally {
      if (client != null) {
        client.release();
      }
    }

    return false;
  }

  Bundle call(String method) {
    return call(method, null, null);
  }

  Bundle call(String method, String arg, Bundle extras) {
    UnstableContentResolverClient client = new UnstableContentResolverClient(context.getContentResolver(),
        Contract.CASH_DRAWER_AUTHORITY_URI);
    return client.call(method, arg, extras, new Bundle());
  }

}
