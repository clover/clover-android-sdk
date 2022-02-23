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
package com.clover.sdk.v3.scanner;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.clover.sdk.internal.util.UnstableContentResolverClient;
import com.clover.sdk.v1.Intents;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * This class provides methods for interacting with the Clover barcode scanning app available on
 * Clover devices. The Clover barcode scanning app uses the cameras built-in to Clover devices to
 * scan barcodes. This class does not support interfacing with external barcode scanners.
 */
public class BarcodeScanner {

  private static final String CALL_METHOD_GET_STATE = "getState";
  private static final String CALL_METHOD_START_SCAN = "startScan";
  private static final String CALL_METHOD_STOP_SCAN = "stopScan";

  /**
   * Return an int array of the available barcode scanners.
   *
   * @see #RESULT_GET_AVAILABLE
   */
  private static final String CALL_METHOD_GET_AVAILABLE = "getAvailable";

  /**
   * Result extra from {@link #CALL_METHOD_GET_AVAILABLE}, an array of ints.
   */
  private static final String RESULT_GET_AVAILABLE = "resultGetAvailable";

  private static final String AUTHORITY = "com.clover.barcodescanner";
  private static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

  /**
   * This field is not useful when working with this class, it should not be used.
   */
  @Deprecated
  public static final String ARG_SUCCESS = "success";

  /**
   * Barcode scanner that can face and be used by either the merchant or customer.
   *
   * @see BarcodeScanner#getAvailable()
   * @see Intents#EXTRA_SCANNER_FACING
   */
  public static final int BARCODE_SCANNER_FACING_DUAL = 0;

  /**
   * Barcode scanner that primarily faces and is intended to be used by the merchant.
   *
   * @see BarcodeScanner#getAvailable()
   * @see BarcodeScanner#startScan(Bundle)
   * @see Intents#EXTRA_SCANNER_FACING
   */
  public static final int BARCODE_SCANNER_FACING_MERCHANT = 1;

  /**
   * Barcode scanner that primarily faces and is intended to be used by the customer.
   *
   * @see BarcodeScanner#getAvailable()
   * @see BarcodeScanner#startScan(Bundle)
   * @see Intents#EXTRA_SCANNER_FACING
   */
  public static final int BARCODE_SCANNER_FACING_CUSTOMER = 2;


  private final Context context;

  public BarcodeScanner(Context context) {
    this.context = context;
  }

  protected UnstableContentResolverClient getUnstableClient() {
    return new UnstableContentResolverClient(context.getContentResolver(), AUTHORITY_URI);
  }

  /**
   * Open the barcode scanning app and begin scanning. See {@link BarcodeResult} for details on
   * receiving the results. May block momentarily, prefer {@link #executeStartScan()}.
   */
  public void startScan() {
    startScan(null);
  }

  /**
   * Open the barcode scanning app and begin scanning. See {@link BarcodeResult} for details on
   * receiving the results. May block momentarily, prefer {@link #executeStartScan(Bundle)}.
   *
   * @param extras optional parameters, currently supports:
   * <ul>
   * <li>{@link Intents#EXTRA_SCANNER_FACING} - if there are multiple cameras which one to scan from, see {@link #getAvailable()}</li>>
   * <li>{@link Intents#EXTRA_SHOW_PREVIEW} - whether scanner preview video will be shown, default is true</li>
   * <li>{@link Intents#EXTRA_LED_ON} - whether LED will be on (selected devices only), default is false</li>
   * <li>{@link Intents#EXTRA_SCAN_QR_CODE} - whether QR codes will be scanned, default is true</li>
   * <li>{@link Intents#EXTRA_SCAN_1D_CODE} - whether 1D codes will be scanned, default is true</li>
   * <li>{@link Intents#EXTRA_SHOW_CLOSE_BUTTON} - whether scanner preview will have a 'close' button, default is true</li>
   * <li>{@link Intents#EXTRA_SHOW_LED_BUTTON} - whether scanner preview will have an LED toggle, default is true</li>
   * </ul>
   * @return true if the scanning app started (it would be very unusual for it to fail)
   */
  public boolean startScan(Bundle extras) {
    try {
      UnstableContentResolverClient client = getUnstableClient();
      Bundle response = client.call(CALL_METHOD_START_SCAN, null, extras, null);
      return response != null;
    } catch (IllegalArgumentException ex) {
      Log.e("com.clover.android.sdk",
          "Failed to start barcode scanner. The scanner is only available on physical devices: "
          + ex.getLocalizedMessage());
      return false;
    }
  }

  /**
   * Stop the barcode scanning app. May block momentarily, prefer {@link #executeStopScan()}.
   */
  public void stopScan() {
    stopScan(null);
  }

  /**
   * Stop the barcode scanning app. May block momentarily, prefer {@link #executeStopScan(Bundle)}.
   *
   * @param extras ignored
   * @return true if the scanning app stopped (it would be very unusual for it to fail)
   */
  public boolean stopScan(Bundle extras) {
    try {
      UnstableContentResolverClient client = getUnstableClient();
      Bundle response = client.call(CALL_METHOD_STOP_SCAN, null, extras, null);
      return response != null;
    } catch (IllegalArgumentException ex) {
      Log.e("com.clover.android.sdk",
          "Failed to stop barcode scanner. The scanner is only available on physical devices: "
          + ex.getLocalizedMessage());
      return false;
    }
  }

  /**
   * Retrieve the builtin barcode scanning cameras that are available on this device.
   * <p/>
   * One of the values returned may be passed as the extra
   * {@link Intents#EXTRA_SCANNER_FACING} in the bundle argument to {@link #startScan(Bundle)}
   * to select the appropriate barcode scanner.
   *
   * @return A {@link List} of {@link Integer}s that are the available barcode scanners on this device. The list will contain
   * {@link #BARCODE_SCANNER_FACING_MERCHANT} and / or {@link #BARCODE_SCANNER_FACING_CUSTOMER}, OR
   * {@link #BARCODE_SCANNER_FACING_DUAL}.
   *
   * @see #startScan(Bundle)
   * @see Intents#EXTRA_SCANNER_FACING
   * @see #BARCODE_SCANNER_FACING_MERCHANT
   * @see #BARCODE_SCANNER_FACING_CUSTOMER
   * @see #BARCODE_SCANNER_FACING_DUAL
   */
  public List<Integer> getAvailable() {
    try {
      Bundle response = getUnstableClient().call(CALL_METHOD_GET_AVAILABLE, null, null, null);
      return response != null && response.containsKey(RESULT_GET_AVAILABLE)
          ? response.getIntegerArrayList(RESULT_GET_AVAILABLE)
          : Collections.singletonList(BarcodeScanner.BARCODE_SCANNER_FACING_DUAL);
    } catch (IllegalArgumentException ex) {
      Log.e("com.clover.android.sdk",
          "Failed to get available scanners. The scanner is only available on physical devices: "
          + ex.getLocalizedMessage());
    }
    // This means the barcode scanner provider did not exist
    return Collections.emptyList();
  }

  /**
   * Returns true if the barcode scanner app is currently running.
   */
  public boolean isProcessing() {
    Bundle response = context.getContentResolver().call(AUTHORITY_URI, CALL_METHOD_GET_STATE, null, null);
    return response != null && response.getBoolean("processing", false);
  }

  private static final Executor sExecutor = Executors.newSingleThreadExecutor((runnable) ->
      new Thread(runnable, "BarcodeScannerClient-Thread"));

  /**
   * Calls {@link #startScan()} from a background thread.
   */
  public void executeStartScan() {
    sExecutor.execute(this::startScan);
  }

  /**
   * Calls {@link #startScan(Bundle)} from a background thread.
   */
  public void executeStartScan(Bundle extras) {
    sExecutor.execute(() -> startScan(extras));
  }

  /**
   * Calls {@link #stopScan()} from a background thread.
   */
  public void executeStopScan() {
    sExecutor.execute(this::stopScan);
  }

  /**
   * Calls {@link #stopScan(Bundle)} from a background thread.
   * 
   * @param extras see {@link #stopScan(Bundle)}
   */
  public void executeStopScan(Bundle extras) {
    sExecutor.execute(() -> stopScan(extras));
  }
}
