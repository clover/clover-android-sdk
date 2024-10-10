package com.clover.sdk.cfp.activity.helper;

import com.clover.sdk.cfp.activity.CFPConstants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;

import java.lang.ref.WeakReference;
import java.util.Objects;

/**
 * @since 4.1.0
 */
public class CloverCFPCommsHelper {
  /**
   * @since 4.1.0
   */
  public interface MessageListener {
    /**
     * Message sent from the merchant facing side.
     *
     * @param payload - most likely a string JSON payload
     * @since 4.0.0
     */
    void onMessage(String payload);
  }

  public static final String LOG_TAG = CloverCFPCommsHelper.class.getSimpleName();
  private final String receiver; // action id for messages sent to the Custom Activity
  private final String broadcaster; // action id for messages sent from the Custom Activity
  private final WeakReference<Context> contextRef;
  private BroadcastReceiver messageReceiver;
  private final WeakReference<MessageListener> weakReferenceMessageListener;

  /**
   * @param context -
   * @param intent  -
   * @since 4.1.0
   */
  public CloverCFPCommsHelper(Context context, Intent intent, MessageListener messageListener) {
    contextRef = new WeakReference<>(context);
    //noinspection deprecation
    receiver = intent.getStringExtra(CFPConstants.ACTION_V1_MESSAGE_TO_ACTIVITY);
    //noinspection deprecation
    broadcaster = intent.getStringExtra(CFPConstants.ACTION_V1_MESSAGE_FROM_ACTIVITY);
    weakReferenceMessageListener = new WeakReference<>(messageListener);

    if (receiver != null) {
      registerCustomActivityMessageReceiver();
    }
  }

  private void registerCustomActivityMessageReceiver() {
    if (messageReceiver != null) {
      return;
    }
    IntentFilter filter = new IntentFilter();
    filter.addAction(receiver);
    //noinspection deprecation
    filter.addCategory(CFPConstants.CATEGORY_CUSTOM_ACTIVITY);
    messageReceiver = new CustomActivityMessageReceiver();
    Context ctx = contextRef.get();
    if (ctx != null) {
      ctx.registerReceiver(messageReceiver, filter);
    } else {
      Log.w(LOG_TAG, "context is null!");
    }
  }

  /**
   * Message sent from the merchant facing side.
   *
   * @param payload - most likely a string JSON payload
   * @since 4.0.0
   */
  private void onMessage(String payload) {
    MessageListener messageListener = weakReferenceMessageListener.get();
    if (messageListener != null) {
      messageListener.onMessage(payload);
    }
  }

  /**
   * Sends a message back to the merchant facing application (i.e. POS).
   *
   * @param payload - most likely a string JSON payload
   * @throws Exception - thrown if the action id for messages sent from the Custom Activity is null
   *                   i.e. the Activity's intent is missing a CFPConstants.ACTION_V1_MESSAGE_FROM_ACTIVITY.
   * @since 4.1.0
   */
  public final void sendMessage(String payload) throws Exception {
    if (broadcaster != null) { //should always be true, but just in case.
      Intent intent = new Intent(broadcaster);
      //noinspection deprecation
      intent.addCategory(CFPConstants.CATEGORY_CUSTOM_ACTIVITY);
      intent.putExtra(CFPConstants.EXTRA_PAYLOAD, payload);
      Context ctx = contextRef.get();
      if (ctx != null) {
        ctx.sendBroadcast(intent);
      } else {
        Log.w(LOG_TAG, "context is null!");
      }
    } else {
      throw new Exception("sendMessage called without a valid Broadcaster specified.");
    }
  }

  private void unregisterCustomActivityMessageReceiver() {
    if (messageReceiver == null) {
      return;
    }

    Context context = contextRef.get();
    if (context != null) {
      context.unregisterReceiver(messageReceiver);
    } else {
      Log.w(LOG_TAG, "context is null!");
    }
    messageReceiver = null;
  }

  /**
   * Cleans up the sessionConnector, loyalty connector and
   * <p>
   * Consumers of this class will want to call this method in their Activity's onDestroy method:
   *
   * <pre>
   *   public class MyCustomActivity extends Activity {
   *       private CloverCFPCommsHelper cloverCFPCommsHelper;
   *       ...
   *       protected void onDestroy() {
   *           // Tell the helper to clean up everything
   *           cloverCFPCommsHelper.dispose();
   *           super.onDestroy();
   *       }
   *       ...
   *   }
   * </pre>
   *
   * @since 4.1.0
   */
  public void dispose() {
    unregisterCustomActivityMessageReceiver();
  }

  class CustomActivityMessageReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
      String payload = intent.getStringExtra(CFPConstants.EXTRA_PAYLOAD);
      if (Objects.equals(intent.getAction(), receiver)) {
        onMessage(payload);
      }
    }
  }
}
