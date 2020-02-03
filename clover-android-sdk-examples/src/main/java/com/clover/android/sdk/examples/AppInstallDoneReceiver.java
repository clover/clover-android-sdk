package com.clover.android.sdk.examples;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AppInstallDoneReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    AppInstallDoneService.enqueueWork(context, intent);
  }
}
