package com.clover.sdk.cfp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import androidx.annotation.MainThread;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Added to an activity and used for handling touch events, this class determines whether to exit the activity once the
 * user has touched the four corners of the display.
 * <p>
 * You can use this class in one of three ways:  CloverCFPActivityHelper, CloverCFPLoyaltyHelper or direct usage.
 * <p>
 * The benefits of using CloverCFPActivityHelper is that it:
 * 1. Allows you to use any Activity subclass
 * 2. Reduces the number of api calls to setup the ExitTouchHandler
 * 3. Provides an easy to overload method "onTrigger()" for triggering the handling of the touch event
 * 4. Handles cleanup automatically
 * <p>
 * The benefits of using CloverCFPLoyaltyHelper is that it:
 * 1. Delegates to CloverCFPActivityHelper
 * 2. Handles Loyalty
 * <p>
 *
 * Via CloverCFPActivityHelper:
 * <pre>
 * public class YourActivity extends Activity {
 *     private CloverCFPActivityHelper cloverCFPActivityHelper;
 *     ...
 *     protected void onCreate(Bundle savedInstanceState) {
 *         super.onCreate(savedInstanceState);
 *         cloverCFPActivityHelper = new CloverCFPActivityHelper(this);
 *     }
 * }
 *
 * Direct Usage:
 * <pre>
 * public class YourActivity extends Activity {
 *     private ExitTouchHandler exitTouchHandler;
 *     ...
 *     protected void onCreate(Bundle savedInstanceState) {
 *         super.onCreate(savedInstanceState);
 *         exitTouchHandler = new ExitTouchHandler(this);
 *     }
 *     public boolean onTouchEvent(MotionEvent event) {
 *         exitTouchHandler.onTouchEvent(event);
 *         return super.onTouchEvent(event);
 *     }
 * }
 * </pre>
 *
 * @since 4.0.0
 */
public class ExitTouchHandler {

  private enum Corner {LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM}

  private static final String TAG = ExitTouchHandler.class.getSimpleName();

  /**
   * @since 4.0.0
   */
  public static int EXITING_ON_TOUCH = -111;
  public static final String ACTION_EXIT_ON_TOUCH = "com.clover.cfp.activity.EXIT_ON_TOUCH";

  /**
   * Dimensions (square) of the hot area at each corner of screen.
   */
  private static final float TRIGGER_SIZE = 150;

  /**
   * Sequence must be triggered in this amount of time or is reset.
   */
  private static final long TRIGGER_TIMEOUT = TimeUnit.SECONDS.toMillis(10);

  private final Handler uiHandler = new Handler();
  private final int width;
  private final int height;
  private final WeakReference<Activity> activityWeakReference;
  private final Set<Corner> corners = new HashSet<>();
  private final boolean isRunningOnSecondaryDisplay;

  /**
   * Constructor - determines the height and width of the display
   *
   * @param activity - you want triggered ended when the 4-finger touch criteria is satisfied
   * @since 4.0.0
   */
  @SuppressLint("ObsoleteSdkInt")
  @SuppressWarnings("ConstantConditions")
  public ExitTouchHandler(Activity activity) {
    this.activityWeakReference = new WeakReference<>(activity);

    WindowManager w = activity.getWindowManager();
    Display d = w.getDefaultDisplay();
    DisplayMetrics metrics = new DisplayMetrics();
    d.getMetrics(metrics);

    int width = metrics.widthPixels;
    int height = metrics.heightPixels;

    // includes window decorations (statusbar bar/menu bar)
    if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17) {
      try {
        //noinspection JavaReflectionMemberAccess
        width = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
        //noinspection JavaReflectionMemberAccess
        height = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
      } catch (Exception ignored) {
      }
    }

    // includes window decorations (statusbar bar/menu bar)
    if (Build.VERSION.SDK_INT >= 17) {
      try {
        Point realSize = new Point();
        Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
        width = realSize.x;
        height = realSize.y;
      } catch (Exception ignored) {
      }
    }

    this.width = width;
    this.height = height;

    // NOTE:  This logic was copied from PlatformUtil to prevent introducing a dependency
    // on an internal clover library (e.g. android-common)
    // Oreo is the first version to allow activities on secondary displays
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
      isRunningOnSecondaryDisplay = false;
    } else {
      isRunningOnSecondaryDisplay = d.getDisplayId() != Display.DEFAULT_DISPLAY;
    }
  }

  /**
   * Receives the touch event from an activity and then decides whether to finish the activity or not.
   *
   * Consumers of this method can do one of two approaches:
   *
   * On a view, add a touch listener that directly calls this method:
   * <pre>
   *   view.setOnTouchListener((view1, motionEvent) -> {
   *         CloverCFPActivityHelper.this.onTouchEvent(motionEvent);
   *         return true;
   *       });
   * </pre>
   *
   * or, directly call it from the onTouchEvent() method on an Activity:
   * <pre>
   * public class YourActivity extends Activity {
   *     private ExitTouchHandler exitTouchHandler;
   *     ...
   *     public boolean onTouchEvent(MotionEvent event) {
   *         exitTouchHandler.onTouchEvent(event);
   *         return super.onTouchEvent(event);
   *     }
   * }
   * </pre>
   *
   * @since 4.0.0
   */
  public void onTouchEvent(MotionEvent event) {
    if (isRunningOnSecondaryDisplay) {
      // Disable touch events if running on a secondary display
      return;
    }

    if ((event.getActionMasked() == MotionEvent.ACTION_DOWN || event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN)) {
      uiHandler.removeCallbacksAndMessages(null);

      Corner c = getCorner(event);
      if (c == null) {
        Log.d(TAG, "no corner found, clearing corners");
        corners.clear();
        return;
      }

      Log.d(TAG, "touch in corner: " + c + ", corners: " + corners);
      corners.add(c);

      if (corners.size() >= 4) {
        corners.clear();
        Activity activity = activityWeakReference.get();
        if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
          Log.d(TAG, "triggering");
          onTrigger();
        }
      } else {
        uiHandler.postDelayed(() -> {
          Log.d(TAG, "timeout reached, clearing corners");
          corners.clear();
        }, TRIGGER_TIMEOUT);
      }
    }
  }

  /**
   * Finishes the activity.  Called by the onTouchEvent() method.
   *
   * @see ExitTouchHandler#onTouchEvent
   * @since 4.0.0
   */
  @MainThread
  protected void onTrigger() {
    Activity activity = activityWeakReference.get();
    if (activity != null) {
      activity.setResult(EXITING_ON_TOUCH);
      // Should already be on the UI Thread
      activity.finish();
    }
  }

  private Corner getCorner(MotionEvent event) {
    if (isInRegion(0, 0, TRIGGER_SIZE, TRIGGER_SIZE, event)) {
      return Corner.LEFT_TOP;
    }
    if (isInRegion(width - TRIGGER_SIZE, 0, width, TRIGGER_SIZE, event)) {
      return Corner.LEFT_BOTTOM;
    }
    if (isInRegion(0, height - TRIGGER_SIZE, TRIGGER_SIZE, height, event)) {
      return Corner.RIGHT_TOP;
    }
    if (isInRegion(width - TRIGGER_SIZE, height - TRIGGER_SIZE, width, height, event)) {
      return Corner.RIGHT_BOTTOM;
    }
    //noinspection IntegerDivisionInFloatingPointContext
    if (isInRegion((width / 2) - TRIGGER_SIZE, (height / 2) - TRIGGER_SIZE, (width / 2) + TRIGGER_SIZE, (height / 2) + TRIGGER_SIZE, event)) {
      corners.clear();
    }

    return null;
  }

  private boolean isInRegion(float x1, float y1, float x2, float y2, MotionEvent event) {
    float x = event.getX(event.getActionIndex());
    float y = event.getY(event.getActionIndex());
    return x > x1 && x < x2 && y > y1 && y < y2;
  }
}