/**
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
package com.clover.sdk.v3.scanner;

import android.content.Intent;

/**
 * This helper class can be constructed from an Intent received by a broadcast receiver that is
 * registered for {@link BarcodeResult#INTENT_ACTION}. Once constructed methods in this class are
 * used to obtain information about the barcode tha was scanned.
 * <p>
 * <pre>
 * public void onStart() {
 *     IntentFilter filter = new IntentFilter(BarcodeResult.INTENT_ACTION);
 *     registerReceiver(mBarcodeReceiver, filter);
 * }
 *
 * private final BroadcastReceiver mBarcodeReceiver = new BroadcastReceiver() {
 *     public void onReceive(Context context, Intent intent) {
 *         BarcodeResult br = new BarcodeResult(intent);
 *         Log.d(TAG, "Received scan with result" + br.getBarcode());
 *     }
 * };
 *
 * public void onStop() {
 *     unregisterReceiver(mBarcodeReceiver);
 * }
 * </pre>
 */
public class BarcodeResult {

  /**
   * Broadcast Action: Sent by Clover when a barcode is scanned.
   */
  public static final String INTENT_ACTION = "com.clover.BarcodeBroadcast";

  private static final String INTENT_BARCODE_RESULT_EXTRA = "Barcode";
  private static final String INTENT_BARCODE_TYPE_EXTRA = "BarcodeType";

  private String action;
  private String barcode;
  private String type;

  /**
   * Constructs an instance of this class from an Intent received with the action
   * {@link #INTENT_ACTION}.
   */
  public BarcodeResult(Intent intent) {
    action = intent.getAction();

    if (isBarcodeAction()) {
      barcode = intent.getStringExtra(INTENT_BARCODE_RESULT_EXTRA);
      type = intent.getStringExtra(INTENT_BARCODE_TYPE_EXTRA);
    }
  }

  /**
   * True unless the Intent provided does not have the action {@link #INTENT_ACTION}. When used
   * properly calling this method isn't necessary.
   */
  public boolean isBarcodeAction() {
    return INTENT_ACTION.equals(action);
  }

  /**
   * Returns the data encoded by the barcode as a String.
   */
  public String getBarcode() {
    return barcode;
  }

  /**
   * Returns a String that describes the type of barcode. Returns "usb" for barcodes scanned by
   * USB barcode scanners since the actual type is unknown.
   */
  public String getType() {
    return type;
  }

  /**
   * True if this is a QR code, false if not or if scanner is USB barcode scanner.
   */
  public boolean isQRCode() {
    return "qr_code".equalsIgnoreCase(type) || "28".equals(type);
  }

  /**
   * True if this is a code 128, false if not or if scanner is USB barcode scanner.
   */
  public boolean isCode128() {
    return "CODE_128".equals(type) || "3".equals(type);
  }

  /**
   * True if this is a UPCA, false if not or if scanner is USB barcode scanner.
   */
  public boolean isUPCA() {
    return "UPC_A".equals(type) || "8".equals(type);
  }

  /**
   * True if this is a UPCE, false if not or if scanner is USB barcode scanner.
   */
  public boolean isUPCE() {
    return "UPC_E".equals(type) || "9".equals(type);
  }

  /**
   * Returns true if the Barcode originated from an external USB barcode scanner. Barcodes from
   * such scanners will not have a type and all the standard <pre>is</pre> methods will return
   * false.
   */
  public boolean isUSB() {
    return "usb".equals(type) || "USB".equals(type);
  }

  /**
   * Returns true if {@link #isQRCode()} is true or scanner is a USB barcode scanner.
   */
  public boolean isQRCodeOrUSB() {
    return isQRCode() || isUSB();
  }

  /**
   * Returns true if {@link #isCode128()} is true or scanner is a USB barcode scanner.
   */
  public boolean isCode128OrUSB() {
    return isCode128() || isUSB();
  }

  /**
   * Returns true if {@link #isUPCA()} is true or scanner is a USB barcode scanner.
   */
  public boolean isUPCAOrUSB() {
    return isUPCA() || isUSB();
  }

  /**
   * Returns true if {@link #isUPCE()} is true or scanner is a USB barcode scanner.
   */
  public boolean isUPCEOrUSB() {
    return isUPCE() || isUSB();
  }

}
