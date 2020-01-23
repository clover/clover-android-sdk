package com.clover.android.sdk.examples;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.clover.sdk.v3.device.Device;
import com.clover.sdk.v3.merchant.MerchantDevicesV2Contract;

public class MerchantDevicesV2Adapter extends CursorAdapter {

  public MerchantDevicesV2Adapter(Context context) {
    super(context, null, FLAG_AUTO_REQUERY);
  }

  @Override
  public long getItemId(int i) {
    return 0;
  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent) {
    return LayoutInflater.from(context).inflate(R.layout.item_device, null);
  }

  @Override
  public void bindView(View view, Context context, Cursor cursor) {
    Device device = MerchantDevicesV2Contract.Device.fromCursor(cursor);

    TextView name = view.findViewById(R.id.device_name);
    TextView model = view.findViewById(R.id.device_model);
    TextView id = view.findViewById(R.id.device_id);

    name.setText(device.getProductName());
    model.setText(device.getModel());
    id.setText(device.getSerial());
  }
}
