package com.clover.sdk.v3.payments.api;

import android.content.Intent;
import com.clover.sdk.v3.remotepay.CloverDeviceEvent;

interface RemotePaymentsAPI_V1_ConnectorListener {
  void onResult(in int resultCode, in Intent data);
  void onDeviceEvent(in CloverDeviceEvent deviceEvent, in boolean isStartEvent);
}
