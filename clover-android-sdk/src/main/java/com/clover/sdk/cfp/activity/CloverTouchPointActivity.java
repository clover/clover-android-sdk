package com.clover.sdk.cfp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.CallSuper;

import com.clover.sdk.cfp.activity.helper.CloverCFPActivityHelper;
import com.clover.sdk.cfp.connector.session.CFPSessionConnector;
import com.clover.sdk.cfp.connector.session.CFPSessionListener;

/**
 * A convenience base class for a TouchPoint Custom Activity that provides capabilities to simplify
 * data flow in, and out of, a custom activity
 * <p>
 * Note: <i>This class is NOT strictly required for an activity to be launched as a touch point activity.
 * CloverCFPActivityHelper, CloverCFPLoyaltyHelper and SessionConnector can be used to get the same
 * capabilities without extending this activity.  However, extending this class is recommended,
 * as there are other included behaviors which aid in proper function within the Clover platform.</i>
 *
 * @since 4.0.0
 */

public abstract class CloverTouchPointActivity extends Activity implements CFPSessionListener {
  private static final String LOG_TAG = CloverTouchPointActivity.class.getSimpleName();
  private CloverCFPActivityHelper cloverCFPActivityHelper;
  protected CFPSessionConnector sessionConnector;

  /**
   * Override a standard lifecycle method to create the helpers. Subclasses should call super.onCreate()
   *
   * @since 4.0.0
   */
  @Override
  @CallSuper
  public void onCreate(Bundle savedInstance) {
    Log.d(LOG_TAG, "onCreate() called");
    super.onCreate(null); // Do not save state!
    cloverCFPActivityHelper = new CloverCFPActivityHelper(this);
    if (sessionConnector == null) {
      Log.i(LOG_TAG, "Adding new sessionConnector");
      sessionConnector = new CFPSessionConnector(getApplicationContext());
      Log.i(LOG_TAG, "Adding sessionConnector data change listener");
      sessionConnector.addSessionListener(this);
    }
  }

  /**
   * Override a standard lifecycle method to dispose of the helpers. Subclasses should call super.onDestroy()
   *
   * @since 4.0.0
   */
  @Override
  @CallSuper
  public void onDestroy() {
    super.onDestroy();
    if (cloverCFPActivityHelper != null) {
      cloverCFPActivityHelper.dispose();
    }
    if (sessionConnector != null) {
      Log.i(LOG_TAG, "Setting sessionConnector = null");
      sessionConnector.removeSessionListener(this);
      sessionConnector = null;
    }
  }

  /**
   * @since 4.0.0
   */
  @Override
  @CallSuper
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == ExitTouchHandler.EXITING_ON_TOUCH) {
      cloverCFPActivityHelper.setResultAndFinish(ExitTouchHandler.EXITING_ON_TOUCH, null);
    }
  }

  /**
   * @since 4.0.0
   */
  @Override
  public void onSessionDataChanged(String type, Object data) {
  }

  /**
   * @since 4.0.0
   */
  @Override
  public void onSessionEvent(String type, String data) {
  }
}