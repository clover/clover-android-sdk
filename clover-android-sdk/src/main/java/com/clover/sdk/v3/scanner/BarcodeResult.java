/**
 * Copyright (C) 2015 Clover Network, Inc.
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
 * User: michael
 * Date: 3/24/15
 * Time: 9:29 AM
 */

public class BarcodeResult {
  public static final String INTENT_ACTION = "com.clover.BarcodeBroadcast";

  private static final String INTENT_BARCODE_RESULT_EXTRA = "Barcode";
  private static final String INTENT_BARCODE_TYPE_EXTRA = "BarcodeType";

  private String action;
  private String barcode;
  private String type;

  public BarcodeResult(Intent intent) {
    action = intent.getAction();

    if (isBarcodeAction()) {
      barcode = intent.getStringExtra(INTENT_BARCODE_RESULT_EXTRA);
      type = intent.getStringExtra(INTENT_BARCODE_TYPE_EXTRA);
    }
  }

  public boolean isBarcodeAction() {
    return INTENT_ACTION.equals(action);
  }

  public String getBarcode() {
    return barcode;
  }

  public String getType() {
    return type;
  }

  // Helper to determine is a scan types from built in scanners
  public boolean isQRCode() {
    return "qr_code".equals(type) || "28".equals(type);
  }

  public boolean isCode128() {
    return "CODE_128".equals(type) || "3".equals(type);
  }

  public boolean isUPCA() {
    return "UPC_A".equals(type) || "8".equals(type);
  }

  public boolean isUPCE() {
    return "UPC_E".equals(type) || "9".equals(type);
  }

  // Since the USB devices don't specify type have helpers that do both
  public boolean isUSB() {
    return "usb".equals(type);
  }

  public boolean isQRCodeOrUSB() {
    return isQRCode() || isUSB();
  }

  public boolean isCode128OrUSB() {
    return isCode128() || isUSB();
  }

  public boolean isUPCAOrUSB() {
    return isUPCA() || isUSB();
  }

  public boolean isUPCEOrUSB() {
    return isUPCE() || isUSB();
  }


}
