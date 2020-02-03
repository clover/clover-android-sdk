package com.clover.android.sdk.examples;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.clover.sdk.v3.device.Device;
import com.clover.sdk.v3.merchant.MerchantDevicesV2Connector;
import com.clover.sdk.v3.merchant.MerchantDevicesV2Contract;

/**
 * Test activity that exercises the contract {@link MerchantDevicesV2Contract} and the connector
 * {@link MerchantDevicesV2Connector}.
 */
public class MerchantDevicesV2TestActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

  private ListView listView;
  private MerchantDevicesV2Adapter adapter;
  private View progressView;
  private View contentView;
  private TextView thisDeviceView;
  private TextView thisSerialView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_merchant_devices_v2);

    progressView = findViewById(R.id.progress_bar);
    contentView = findViewById(R.id.content_layout);

    thisSerialView = findViewById(R.id.this_serial);
    thisDeviceView = findViewById(R.id.this_device);

    listView = findViewById(R.id.list_view);
    listView.setEmptyView(findViewById(R.id.list_view_empty));
    adapter = new MerchantDevicesV2Adapter(this);
    listView.setAdapter(adapter);
  }

  @SuppressLint("StaticFieldLeak")
  @Override
  protected void onStart() {
    super.onStart();

    progressView.setVisibility(View.VISIBLE);
    contentView.setVisibility(View.GONE);

    getLoaderManager().initLoader(0, null, this);

    new AsyncTask<Void,Void,String>() {
      @Override
      protected String doInBackground(Void... voids) {
        return new MerchantDevicesV2Connector(getApplicationContext()).getSerial();
      }

      @Override
      protected void onPostExecute(String serial) {
        if (serial == null) {
          return;
        }
        thisSerialView.setText(serial);
      }
    }.execute();
    new AsyncTask<Void,Void,Device>() {
      @Override
      protected Device doInBackground(Void... voids) {
        return new MerchantDevicesV2Connector(getApplicationContext()).getDevice();
      }

      @Override
      protected void onPostExecute(Device device) {
        if (device == null) {
          return;
        }
        thisDeviceView.setText(String.format("%s: %s %s",
            device.getProductName(), device.getModel(), device.getSerial()));
      }
    }.execute();

  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new CursorLoader(this, MerchantDevicesV2Contract.Device.CONTENT_URI, null, null, null, null);
  }

  @Override
  public void onLoadFinished(Loader loader, Cursor data) {
    adapter.swapCursor(data);

    progressView.setVisibility(View.GONE);
    contentView.setVisibility(View.VISIBLE);
  }

  @Override
  public void onLoaderReset(Loader loader) {
    // we're not binding to an adapter or hanging on to the cursor, so no need to do anything here
  }
}
