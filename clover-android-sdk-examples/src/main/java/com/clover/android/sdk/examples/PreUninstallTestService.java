package com.clover.android.sdk.examples;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;
import android.util.Log;


public class PreUninstallTestService extends IntentService {

  public PreUninstallTestService() {
    super(PreUninstallTestService.class.getName());
  }

  @Override
  protected void onHandleIntent(@Nullable Intent intent) {
    Log.i("preuninstall", "Pre-uninstall triggered!");

    // This example starts an activity for explanatory purposes only. Any implementation
    // of a pre-uninstall service outside of development must complete its work in the
    // background, preferably within the implementation of the service's
    // onHandleIntent() method.
    Intent countDownIntent = new Intent(this, PreUninstallTestActivity.class);
    countDownIntent.setAction(PreUninstallTestActivity.ACTION_PREUNINSTALL_COUNTDOWN);
    countDownIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(countDownIntent);
  }
}
