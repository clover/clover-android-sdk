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
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import com.clover.sdk.util.CloverAccount;

import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Deprecated
class APG554aCashDrawer extends CashDrawer {

  private static final int VENDOR_ID = 0x07c5;
  private static final int PRODUCT_ID = 0x0500;

  static class Discovery extends CashDrawer.Discovery<APG554aCashDrawer> {

    protected Discovery(Context context) {
      super(context);
    }

    @Override
    public Set<APG554aCashDrawer> list() {
      Set<APG554aCashDrawer> cashDrawers = new HashSet<>();

      UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
      Map<String, UsbDevice> devices = usbManager.getDeviceList();
      for (Map.Entry<String, UsbDevice> e : devices.entrySet()) {
        UsbDevice d = e.getValue();
        if (d.getVendorId() == VENDOR_ID && d.getProductId() == PRODUCT_ID) {
          cashDrawers.add(new APG554aCashDrawer(context, d));
        }
      }

      return cashDrawers;
    }
  }

  private final UsbDevice usbDevice;
  private final Account cloverAccount;

  protected APG554aCashDrawer(Context context, UsbDevice usbDevice) {
    super(context, 1);
    this.usbDevice = usbDevice;
    this.cloverAccount = CloverAccount.getAccount(context);

  }

  @Override
  public String getIdentifier() {
    return "CLOVER_USB";
  }

  @Override
  public String getDisplayName() {
    return "USB Cash Drawer";
  }

  @SuppressWarnings("deprecated")
  @Override
  public boolean pop() {
    com.clover.sdk.v1.printer.CashDrawer.open(context, cloverAccount, usbDevice);
    return true;
  }

  @Override
  public String toString() {
    return "APG554aCashDrawer{usbDevice=" + usbDevice + "}";
  }
}
