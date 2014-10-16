package com.clover.sdk.v3.scanner;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class BarcodeScanner {
  private static final String CALL_METHOD_GET_STATE = "getState";
  private static final String CALL_METHOD_START_SCAN = "startScan";
  private static final String CALL_METHOD_STOP_SCAN = "stopScan";

  private static final String AUTHORITY = "com.clover.barcodescanner";
  private static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

  public static final String ARG_SUCCESS = "success";

  private final Context context;

  public BarcodeScanner(Context context) {
    this.context = context;
  }

  // Note: Extras are define in Intents.java

  public boolean startScan(Bundle extras) {
    try {
      Bundle response = context.getContentResolver().call(AUTHORITY_URI, CALL_METHOD_START_SCAN, null, extras);
      return response != null;
    } catch (IllegalArgumentException ex) {
      Log.e("com.clover.android.sdk", "Failed to start barcode scanner. The scanner is only available on physical devices: " + ex.getLocalizedMessage());
      return false;
    }
  }

  public boolean stopScan(Bundle extras) {
    try {
      Bundle response = context.getContentResolver().call(AUTHORITY_URI, CALL_METHOD_STOP_SCAN, null, extras);
      return response != null;
    } catch (IllegalArgumentException ex) {
      Log.e("com.clover.android.sdk", "Failed to stop barcode scanner. The scanner is only available on physical devices: " + ex.getLocalizedMessage());
      return false;
    }
  }

  public boolean isProcessing() {
    Bundle response = context.getContentResolver().call(AUTHORITY_URI, CALL_METHOD_GET_STATE, null, null);
    return response!=null && response.getBoolean("processing", false);
  }

  /**
   *
   * @param extras see {@link com.clover.sdk.v1.Intents#EXTRA_SCAN_QR_CODE} and {@link com.clover.sdk.v1.Intents#EXTRA_SCAN_1D_CODE}
   */
  public void executeStartScan(Bundle extras) {
    new AsyncTask<Bundle, Void, Boolean>() {
      @Override
      protected Boolean doInBackground(Bundle... params) {
        return startScan(params[0]);
      }
    }.execute(extras);
  }

  public void executeStopScan(Bundle extras) {
    new AsyncTask<Bundle, Void, Boolean>() {
      @Override
      protected Boolean doInBackground(Bundle... params) {
        return stopScan(params[0]);
      }
    }.execute(extras);
  }
}
