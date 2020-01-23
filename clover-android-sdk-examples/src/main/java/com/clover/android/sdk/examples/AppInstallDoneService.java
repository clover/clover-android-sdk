package com.clover.android.sdk.examples;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import android.util.Log;

public class AppInstallDoneService extends JobIntentService {

  static void enqueueWork(Context context, Intent work) {
    enqueueWork(context, AppInstallDoneService.class, 13, work);
  }

  @Override
  protected void onHandleWork(@NonNull Intent intent) {
    Log.i(AppInstallDoneService.class.getSimpleName(),
        "Hello, app install done for package: " + getPackageName());
  }
}
