package com.clover.sdk.cfp.activity.helper;

import com.clover.sdk.cfp.activity.CFPConstants;
import com.clover.sdk.cfp.activity.ExitTouchHandler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import java.lang.ref.WeakReference;

import static android.app.Activity.RESULT_CANCELED;

/**
 * Provides helper methods for handling Activities on Clover devices.
 *
 * If you use CloverCFPLoyaltyHelper, it will use this class internally to interact with the Activity.
 *
 * <p>
 * In particular, attaches the 4-finger exit handler (ExitHandler) to the activity.
 *
 * @since 4.1.0
 */
public class CloverCFPActivityHelper extends ExitTouchHandler {
  private static final int NUCLEAR_FINISH_ANY_ACTIVITY = -999;
  private static final int DEFAULT_ACTIVITY_INTENT_REQUEST_CODE = -998;
  private static final int DEFAULT_FINISH_RECEIVER_INTENT_REQUEST_CODE = -997;
  private static final String ACTION_FINISH_ACTIVITY = "com.clover.cfp.activity.FINISH";
  private static final String ACTION_ACTIVITY_STARTED = "com.clover.cfp.activity.STARTED";
  private static final String ACTION_ACTIVITY_FINISHED = "com.clover.cfp.activity.FINISHED";
  public static final String EXTRA_ACTIVITY_REQCODE = "com.clover.remote.terminal.remotecontrol.extra.ACTIVITY_REQCODE";
  private static final String EXTRA_ACTIVITY_ACTION = "com.clover.remote.terminal.remotecontrol.extra.ACTIVITY_ACTION";

  protected String LOG_TAG;
  protected WeakReference<Activity> activityRef;
  protected String action; // action from the intent used to start this activity
  protected int reqCode; // the reqCode used to start this activity
  protected Intent resultIntent; // The intent from setResultAndFinish()
  protected Intent originalIntent; // The intent that started the referenced Activity
  private FinishMessageReceiver finishReceiver;
  protected boolean receivedFinishRequest;

  /**
   * Sets the ExitHandler on the activity.
   *
   * @param activity - the activity you want to manage with this helper class.
   *
   * @since 4.1.0
   */
  @SuppressLint("ClickableViewAccessibility")
  public CloverCFPActivityHelper(Activity activity) {
    super(activity);
    if (activity != null) {
      activityRef = new WeakReference<>(activity);
      originalIntent = activity.getIntent();
      action = originalIntent.getAction();
      registerFinishReceiver();
      LOG_TAG = activity.getLocalClassName();
      reqCode = activity.getIntent().getIntExtra(EXTRA_ACTIVITY_REQCODE, DEFAULT_ACTIVITY_INTENT_REQUEST_CODE);
      View view = activity.findViewById(android.R.id.content).getRootView();
      if (view != null) {
        /*
         * Called when a touch event is dispatched to a view. This allows listeners to
         * get a chance to respond before the target view.
         *
         * This allows the "4-finger exit touch handler" (i.e. ExitTouchHandler) to handle the event.
         */
        view.setOnTouchListener((view1, motionEvent) -> {
          CloverCFPActivityHelper.this.onTouchEvent(motionEvent);
          return true;
        });
      }
      //  Announce that the activity has started and pass back the data from the original intent
      Intent startedIntent = new Intent(ACTION_ACTIVITY_STARTED);
      startedIntent.putExtra(EXTRA_ACTIVITY_ACTION, action);
      startedIntent.putExtras(activity.getIntent());
      activity.sendBroadcast(startedIntent);
      @SuppressLint("DefaultLocale") String logMessage = String.format("CloverCFPActivityHelper.constructor: Sending ACTION_ACTIVITY_STARTED broadcast with requestCode: %d and intent: %s", reqCode, startedIntent);
      Log.d(LOG_TAG, logMessage);
    }
  }

  private void registerFinishReceiver() {
    if (finishReceiver != null) {
      return;
    }
    Activity activity = activityRef.get();
    if (activity != null) {
      finishReceiver = new FinishMessageReceiver();
      activity.registerReceiver(finishReceiver, new IntentFilter(ACTION_FINISH_ACTIVITY));
    }
  }

  private void unregisterFinishReceiver() {
    if (finishReceiver != null) {
      Activity activity = activityRef.get();
      if (activity != null) {
        activity.unregisterReceiver(finishReceiver);
        finishReceiver = null;
      }
    }
  }

  /**
   * Sets the result and finishes an activity with an associated string payload.
   *
   * @param resultCode - @see Activity (like RESULT_CANCELED(0), RESULT_OK(-1), etc.)
   * @param payload    - Most likely a JSON string or null.
   *
   * @since 4.1.0
   */
  public void setResultAndFinish(int resultCode, String payload) {
    if (shouldFinishActivity(originalIntent, false)) {
      receivedFinishRequest = true;
      @SuppressLint("DefaultLocale") String logMessage = String.format("CloverCFPActivityHelper.setResultAndFinish() called with resultCode: %d and payload:  %s", resultCode, payload);
      Log.d(LOG_TAG, logMessage);
      resultIntent = new Intent(action);
      resultIntent.putExtra(CFPConstants.EXTRA_PAYLOAD, payload);
      Activity activity = activityRef.get();
      if (activity != null) {
        activity.setResult(resultCode, resultIntent);
        // Create a handler for "finishing" the activity.
        new Handler(activity.getMainLooper()).post(activity::finish);
      } else {
        Log.w(LOG_TAG, "activity is null!");
      }
    }
  }

  private void setResultAndFinishFromReceiver(Intent receiverIntent) {
    int finishRequestCode = receiverIntent.getIntExtra(EXTRA_ACTIVITY_REQCODE, DEFAULT_FINISH_RECEIVER_INTENT_REQUEST_CODE);
    if (shouldFinishActivity(receiverIntent, true)) {
      receivedFinishRequest = true;
      resultIntent = new Intent(action);
      Activity activity = activityRef.get();
      if (activity != null) {
        activity.setResult(RESULT_CANCELED, resultIntent);
        // Create a handler for "finishing" the activity.
        new Handler(activity.getMainLooper()).post(activity::finish);
      } else {
        Log.w(LOG_TAG, "activity is null!");
      }
    } else {
      @SuppressLint("DefaultLocale") String logMessage = String.format("CloverCFPActivityHelper.setResultAndFinishFromReceiver(): Received intent to explicitly finish activity with requestCode: %d, but was already in the process of finishing or request code didn't match this activity reqCode of %d.  Ignoring request.", finishRequestCode, reqCode);
      Log.d(LOG_TAG, logMessage);
    }
  }

  /**
   * Called when the ExitTouchHandler is triggered to finish the activity.
   *
   * @since 4.1.0
   */
  @Override
  protected void onTrigger() {
    Activity activity = activityRef.get();
    if (activity != null) {
      Intent exitOnTouchBroadcastIntent = new Intent(ACTION_EXIT_ON_TOUCH);
      activity.sendOrderedBroadcast(exitOnTouchBroadcastIntent, null);
    }
    setResultAndFinish(ExitTouchHandler.EXITING_ON_TOUCH, null);
  }

  /**
   *
   * @since 4.1.0
   */
  @SuppressWarnings("unused")
  public String getInitialPayload() {
    Activity activity = activityRef.get();
    if (activity != null && activity.getIntent() != null) {
      return activity.getIntent().getStringExtra(CFPConstants.EXTRA_PAYLOAD);
    } else {
      Log.w(LOG_TAG, "activity is null!");
    }
    return null;
  }

  /**
   * Removes the weak reference for the activity.
   * <p>
   * Called by:
   * <ul>
   *   <li>CloverCFPActivity#onDestroy(), and</li>
   *   <li>CloverCFPLoyaltyHelper#dispose()</li>
   * </ul>
   *
   * @since 4.1.0
   */
  public void dispose() {
    unregisterFinishReceiver();
    //  Announce that the activity has finished using either the normal result intent
    //  or a generic finished intent (likely due to a finish request)
    Activity activity = activityRef.get();
    if (activity != null) {
      if (resultIntent != null) {
        resultIntent.putExtra(EXTRA_ACTIVITY_ACTION, action);
        resultIntent.setAction(ACTION_ACTIVITY_FINISHED);
        resultIntent.putExtra(EXTRA_ACTIVITY_REQCODE, reqCode);
        activity.sendBroadcast(resultIntent);
      } else {
        Intent finishedIntent = new Intent(ACTION_ACTIVITY_FINISHED);
        finishedIntent.putExtra(EXTRA_ACTIVITY_REQCODE, reqCode);
        activity.sendBroadcast(finishedIntent);
      }
    }
    activityRef = null;
  }

  /*
    This method should only return true if the finish intent
    matches the request code of the current activity.  If the
    current activity doesn't specify a request code, then it
    should only be finished if utilizing the nuclear option.
   */
  private boolean shouldFinishActivity(Intent intent, boolean fromReceiver) {
    Activity activity = activityRef.get();
    if (activity != null) {
      // Basically we want to finish the activity if:
      //  a) it's not already in the process of finishing
      //  AND
      //  b) The finish requestCode matches this activity
      //  OR
      //  c) The finish is the nuclear finish
      int finishRequestCode = intent != null ? intent.getIntExtra(EXTRA_ACTIVITY_REQCODE, fromReceiver ? DEFAULT_FINISH_RECEIVER_INTENT_REQUEST_CODE : DEFAULT_ACTIVITY_INTENT_REQUEST_CODE) : reqCode;
      boolean shouldFinish = (!activity.isFinishing() && !receivedFinishRequest &&
                                ((reqCode == finishRequestCode) ||
                                   finishRequestCode == NUCLEAR_FINISH_ANY_ACTIVITY));
      if (shouldFinish) {
        @SuppressLint("DefaultLocale") String logMessage = String.format(fromReceiver ? "CloverCFPActivityHelper.setResultAndFinishFromReceiver() called with requestCode: %d and resultCode: %d." : "CloverCFPActivityHelper.setResultAndFinish() called with requestCode: %d and resultCode: %d.", finishRequestCode, RESULT_CANCELED);
        Log.d(LOG_TAG, logMessage);
      }
      return shouldFinish;
    }
    return false;
  }

  private class FinishMessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      setResultAndFinishFromReceiver(intent);
    }
  }
}
