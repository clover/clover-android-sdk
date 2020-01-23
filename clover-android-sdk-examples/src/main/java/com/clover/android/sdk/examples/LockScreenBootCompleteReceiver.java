package com.clover.android.sdk.examples;

import com.clover.sdk.Lockscreen;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

/**
 * This is called when device boots and checks if an employee was selected from {@link LockScreenTestActivity}
 * before the device was shut down last time.
 * <p>
 * If yes, it logs in with the selected user and displays a notification in the device.
 */
public class LockScreenBootCompleteReceiver extends BroadcastReceiver {
  private static final String TAG = LockScreenBootCompleteReceiver.class.getSimpleName();
  private static final String EMPLOYEE_PREF = "EmployeePreference";
  private static final String EMPLOYEE_ID = "EmployeeId";
  private static final String EMPLOYEE_NAME = "EmployeeName";
  private static final String CHANNEL_NAME = "LockscreenSDKSampleChannel";
  private static final String CHANNEL_DESCRIPTION = "LockscreenSDKSampleDesc";
  private static final String CHANNEL_ID = "LockscreenSDKTest";
  private static final String NOTIFICATION_TITLE = "LockscreenNotification";


  @Override
  public void onReceive(Context context, Intent intent) {
    if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
      createNotificationChannel(context);
      SharedPreferences prefs = context.getSharedPreferences(EMPLOYEE_PREF, MODE_PRIVATE);
      String employeeId = prefs.getString(EMPLOYEE_ID, null);
      String employeeName = prefs.getString(EMPLOYEE_NAME, null);
      Log.i(TAG, "Logged in as name :" + employeeName + " id : " + employeeId);
      if (employeeId != null && employeeName != null) {
        /**
         * Resetting the Shared Preferences to null after a successful login.
         * */
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(EMPLOYEE_ID, null);
        editor.putString(EMPLOYEE_NAME, null);
        editor.apply();
        Lockscreen lockscreen = new Lockscreen(context);
        lockscreen.unlock(employeeId);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.clover_logo_green)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(employeeName)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());

      }
    }
  }

  private void createNotificationChannel(Context context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      int importance = NotificationManager.IMPORTANCE_DEFAULT;
      NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
      channel.setDescription(CHANNEL_DESCRIPTION);
      NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
      notificationManager.createNotificationChannel(channel);
    }
  }
}
