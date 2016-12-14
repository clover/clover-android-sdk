package com.clover.sdk.cashdrawer;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class APG554aCashDrawer extends CashDrawer {
  private static final int VENDOR_ID = 0x07c5;
  private static final int PRODUCT_ID = 0x0500;

  private static final byte[] CMD_POP = new byte[]{0x00, 0x01};


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

  protected APG554aCashDrawer(Context context, UsbDevice usbDevice) {
    super(context, 1);
    this.usbDevice = usbDevice;
  }

  @Override
  public boolean pop() {
    UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
    UsbDeviceConnection connection = usbManager.openDevice(usbDevice);
    if (connection == null) {
      return false;
    }
    try {
      UsbInterface intf = usbDevice.getInterface(0);
      if (intf == null) {
        return false;
      }
      if (!connection.claimInterface(intf, true)) {
        return false;
      }
      try {
        int c = connection.controlTransfer(0x21, 0x09, 0x200, 0, CMD_POP, CMD_POP.length, 0);
        if (c != CMD_POP.length) {
          return false;
        }
      } finally {
        connection.releaseInterface(intf);
      }
    } finally {
      connection.close();
    }

    return true;
  }

  @Override
  public String toString() {
    return "APG554aCashDrawer{usbDevice=" + usbDevice + "}";
  }
}
