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
package com.clover.android.sdk.examples;

import android.accounts.Account;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import com.clover.sdk.v1.Intents;
import com.clover.sdk.v3.scanner.BarcodeResult;
import com.clover.sdk.v3.scanner.BarcodeScanner;


public class BarcodeScannerTestActivity extends Activity {
  private static final String TAG = "BarcodeScannerTestActivity";

  private Account mAccount;
  private BarcodeScanner mBarcodeScanner;

  private CheckBox qrCode;
  private CheckBox code1D;
  private TextView resultTV;

  private BroadcastReceiver barcodeReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      BarcodeResult barcodeResult = new BarcodeResult(intent);

      if (barcodeResult.isBarcodeAction()) {
        String barcode = barcodeResult.getBarcode();
        resultTV.setText("Result: " + barcode);
      }
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_barcode_scanner_test);

    qrCode = (CheckBox) findViewById(R.id.checkBoxQRCode);
    code1D = (CheckBox) findViewById(R.id.checkBox1DCode);

    resultTV = (TextView) findViewById(R.id.resultTV);

    mBarcodeScanner = new BarcodeScanner(this);

    Button start = (Button) findViewById(R.id.start);
    start.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Bundle extras = new Bundle();
        extras.putBoolean(Intents.EXTRA_LED_ON, false);
        extras.putBoolean(Intents.EXTRA_SCAN_QR_CODE, qrCode.isChecked());
        extras.putBoolean(Intents.EXTRA_SCAN_1D_CODE, code1D.isChecked());
        mBarcodeScanner.executeStartScan(extras);

        resultTV.setText("");
      }
    });

    Button stop = (Button) findViewById(R.id.stop);
    stop.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mBarcodeScanner.executeStopScan(null);
      }
    });

  }

  @Override
  protected void onResume() {
    super.onResume();
    registerBarcodeScanner();
  }

  @Override
  protected void onPause() {
    super.onPause();
    unregisterBarcodeScanner();
  }

  private void registerBarcodeScanner() {
    registerReceiver(barcodeReceiver, new IntentFilter(BarcodeResult.INTENT_ACTION));
  }

  private void unregisterBarcodeScanner() {
    unregisterReceiver(barcodeReceiver);
  }
}
