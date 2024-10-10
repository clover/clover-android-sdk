package com.clover.loyalty.activity.helper;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IInterface;
import android.util.Log;

import androidx.annotation.Nullable;

import com.clover.sdk.cfp.activity.CFPConstants;
import com.clover.sdk.cfp.activity.helper.CloverCFPActivityHelper;
import com.clover.sdk.cfp.activity.helper.CloverCFPCommsHelper;
import com.clover.sdk.cfp.connector.session.CFPSessionConnector;
import com.clover.sdk.cfp.connector.session.CFPSessionListener;
import com.clover.loyalty.ILoyaltyDataService;
import com.clover.loyalty.LoyaltyConnector;
import com.clover.loyalty.LoyaltyDataTypes;
import com.clover.sdk.v1.ServiceConnector;
import com.clover.sdk.v3.customers.CustomerInfo;
import com.clover.sdk.v3.loyalty.LoyaltyDataConfig;
import com.clover.sdk.v3.order.DisplayOrder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Used by an Activity on the customer facing side to allow loyalty and customer information to be passed between
 * the customer and merchant facing sides.
 * <p>
 * Allows an integrator to use whatever base class they want to use for their Activity.
 *
 * @since 4.1.0
 */
public class CloverCFPLoyaltyHelper {
  /**
   * Interface implemented on the customer facing side.
   * <p>
   * This extends an interface which adds: Interface implemented on the customer facing side to:
   * <ul>
   *   <li>Via onMessage(), receive messages from the merchant facing side, or</li>
   *   <li>Via sendMessage(), send messages back to the merchant facing side</li>
   * </ul>
   *
   * @since 4.1.0
   */
  public interface LoyaltyListener {

    /**
     * Message sent from the merchant facing side.
     *
     * @param payload - most likely a string JSON payload
     * @since 4.0.0
     */
    void onMessage(String payload);

    /**
     * Sends a message back to the merchant facing side.
     *
     * @param payload - most likely a string JSON payload
     * @throws Exception - thrown if the action id for messages sent from the Custom Activity is null
     * @since 4.0.0
     */
    @SuppressWarnings("unused")
    void sendMessage(String payload) throws Exception;

    /**
     * When the implementing class connects, it will receive the current list of LoyaltyDataConfigs and current
     * customerInfo.
     *
     * @param configs      - the list of loyalty data configs
     * @param customerInfo - customer info
     * @since 4.1.0
     */
    void onLoyaltyDataLoaded(List<LoyaltyDataConfig> configs, CustomerInfo customerInfo);

    /**
     * Notified when the state of a loyalty service changes.
     *
     * @param type  - something like:  "EMAIL", "PHONE", "CLEAR"
     * @param state - a string that describes whether the service is "up" or "down".
     * @since 4.1.0
     */
    void onLoyaltyServiceStateChanged(String type, String state);
  }

  public static final String LOG_TAG = CloverCFPLoyaltyHelper.class.getSimpleName();

  private final Gson gson = new GsonBuilder().serializeNulls().create();

  protected WeakReference<Activity> activityWeakReference;
  private CFPSessionConnector sessionConnector;
  private LoyaltyConnector loyaltyConnector;

  // Private session listener that forwards session events to the "externalSessionListener" - the session listener
  // passed in on the constructor for this class.
  private CFPSessionListener internalSessionListener;

  // We use this flag to determine if dispose() has been called.  Once dispose() is called, there's no point to handling
  // "in-flight" events.
  private boolean isStopping = false;

  private List<LoyaltyDataConfig> loyaltyDataConfigList = new ArrayList<>();

  Map<String, LoyaltyDataConfig> typeToConfig = new HashMap<>();

  private CustomerInfo customerInfo;
  private DisplayOrder displayOrder;

  private WeakReference<LoyaltyListener> weakReferenceLoyaltyListener;
  private final WeakReference<CFPSessionListener> weakReferenceSessionListener;
  private final Map<String, BroadcastReceiver> loyaltyServiceStateChangeReceiver = new HashMap<>();
  private final CloverCFPActivityHelper cloverCFPActivityHelper;
  private final CloverCFPCommsHelper cloverCFPCommsHelper;

  Executor executor = Executors.newSingleThreadExecutor();

  /**
   * Main constructor.
   * <p>
   * Example usage:
   *
   * <pre>
   *   public class CloverLoyaltyCustomActivity extends Activity
   *                                            implements SessionListener, CloverCFPLoyaltyHelper.LoyaltyListener {
   *       private CloverCFPLoyaltyHelper cloverCFPLoyaltyHelper;
   *       ...
   *       protected void onCreate(Bundle savedInstanceState) {
   *           ...
   *           cloverCFPLoyaltyHelper = new CloverCFPLoyaltyHelper(this, this, this);
   *           ...
   *       }
   *       ...
   *   }
   * </pre>
   *
   * @param activity                - a reference to the activity that is using this helper class.
   * @param loyaltyListener         - a client that receives the onLoyaltyDataLoaded(), onLoyaltyServiceStateChanged(), onMessage()
   *                                events, and sends an event via sendMessage().
   * @param externalSessionListener - a client that will receive onSessionDataChanged and onSessionEvent events.
   * @since 4.1.0
   */
  public CloverCFPLoyaltyHelper(final Activity activity, final LoyaltyListener loyaltyListener, final CFPSessionListener externalSessionListener) {
    activityWeakReference = new WeakReference<>(activity);
    weakReferenceLoyaltyListener = new WeakReference<>(loyaltyListener);
    weakReferenceSessionListener = new WeakReference<>(externalSessionListener);

    sessionConnector = new CFPSessionConnector(activity);
    this.internalSessionListener = new CFPSessionListener() {
      /**
       * Data can be any type of string such as:
       * <pre>
       *   {
       *   "customer": {
       *     "firstName": "John Smith",
       *     "phoneNumbers": {
       *       "elements": [
       *         {
       *           "phoneNumber": "(855) 853-8340"
       *         }
       *       ]
       *     },
       *     "id": "10"
       *   },
       *   "externalId": "44XBAPX69H",
       *   "extras": {
       *     "POINTS": "42",
       *     "OFFERS": "[{\"cost\":10,\"description\":\"\",\"id\":\"R1911\",\"item\":{\"id\":\"R1911\",\"name\":\"Small Drink\",\"price\":0,\"taxable\":true,\"tippable\":true},\"label\":\"Free small drink\"},{\"cost\":50,\"description\":\"\",\"discount\":{\"_amountOff\":500,\"_percentageOff\":0.0,\"name\":\"$5 Off\"},\"id\":\"2J411\",\"label\":\"$5 off\"},{\"cost\":40,\"description\":\"\",\"discount\":{\"_amountOff\":0,\"_percentageOff\":0.1,\"name\":\"10% Off\"},\"id\":\"P4610\",\"label\":\"10% Off\"}]"
       *   },
       *   "displayString": "Welcome back John Smith"
       * }
       * </pre>
       * @param type - any type of string (ex. "com.clover.extra.CUSTOMER_INFO")
       * @param data - most likely a JSON serialized string (See above).
       */
      @Override
      public void onSessionDataChanged(String type, Object data) {
        // "externalSessionListener" is the session listener passed in on the constructor for this class.
        CFPSessionListener externalSessionListener = weakReferenceSessionListener.get();
        if (externalSessionListener != null) {
          externalSessionListener.onSessionDataChanged(type, data);
        }
      }

      @Override
      public void onSessionEvent(String type, String data) {
        // "externalSessionListener" is the session listener passed in on the constructor for this class.
        CFPSessionListener externalSessionListener = weakReferenceSessionListener.get();
        if (externalSessionListener != null) {
          externalSessionListener.onSessionEvent(type, data);
        }
      }
    };
    sessionConnector.addSessionListener(internalSessionListener);
    cloverCFPActivityHelper = new CloverCFPActivityHelper(activity);
    cloverCFPCommsHelper = new CloverCFPCommsHelper(activity, activity.getIntent(), new CloverCFPCommsHelper.MessageListener() {
      /**
       * Message sent from the merchant facing side.  This anonymous inner class is forwarding the method request to
       * a private method on the CloverCFPLoyaltyHelper class which then forwards it to a listener class.
       *
       * @param payload - most likely a string JSON payload
       */
      @Override
      public void onMessage(String payload) {
        CloverCFPLoyaltyHelper.this.onMessage(payload);
      }
    });

    // should also be able to get these from onLoyaltyDataLoaded call back
    Intent intent = activity.getIntent();
    setCustomerInfo(intent.getParcelableExtra(CFPConstants.CUSTOMER_INFO_EXTRA));
    displayOrder = intent.getParcelableExtra(CFPConstants.DISPLAY_ORDER_EXTRA);

    initializeLoyaltyConnector();
  }

  /**
   * Use this method to know if dispose() has been called on this class.
   *
   * @return true, if dispose() has been called.
   * @since 4.1.0
   */
  @SuppressWarnings("unused")
  public boolean isStopping() {
    return isStopping;
  }

  /**
   * @since 4.1.0
   */
  @Nullable
  @SuppressWarnings("unused")
  public CustomerInfo getCustomerInfo() {
    return customerInfo;
  }

  /**
   * @since 4.1.0
   */
  public void setCustomerInfo(@Nullable CustomerInfo customerInfo) {
    this.customerInfo = customerInfo;
  }

  /**
   * @since 4.1.0
   */
  @SuppressWarnings("unused")
  public DisplayOrder getDisplayOrder() {
    return displayOrder;
  }

  /**
   * @since 4.1.0
   */
  @SuppressWarnings("unused")
  public void setDisplayOrder(DisplayOrder displayOrder) {
    this.displayOrder = displayOrder;
  }

  /**
   * @since 4.1.0
   */
  @SuppressWarnings("unused")
  public String getOrderTotal() {
    return hasOrder() ? displayOrder.getTotal() : "";
  }

  /**
   * @since 4.1.0
   */
  public boolean hasOrder() {
    return (displayOrder != null);
  }

  private void initializeLoyaltyConnector() {
    if (loyaltyConnector == null) {
      Activity activity = getActivity();
      if (activity == null) {
        Log.w(LOG_TAG, "Context is null!");
        return;
      }

      ServiceConnector.OnServiceConnectedListener client = new ServiceConnector.OnServiceConnectedListener() {
        @Override
        public void onServiceConnected(ServiceConnector<? extends IInterface> serviceConnector) {
          Log.d(LOG_TAG, "Connected!");
          executor.execute(() -> {
            try {
              // Get desired LoyaltyDataConfigs and pass them to the onLoyaltyDataLoaded
              final List<LoyaltyDataConfig> desiredDataConfig = loyaltyConnector.getDesiredDataConfig();

              new Handler(activity.getMainLooper()).post(() -> onLoyaltyDataLoaded(desiredDataConfig, sessionConnector.getCustomerInfo()));
            } catch (Exception e) {
              Log.e(LOG_TAG, "Error getting desired configs", e);
            }
          });
        }

        @Override
        public void onServiceDisconnected(ServiceConnector<? extends IInterface> serviceConnector) {
          Log.d(LOG_TAG, "Disconnected!");
        }
      };
      loyaltyConnector = new LoyaltyConnector(activity, null, client) {
        @Override
        public void onBindingDied(ComponentName name) {
          Log.d(LOG_TAG, name + " onBindingDied");
        }
      };
      if (!loyaltyConnector.connect()) {
        Log.d(LOG_TAG, "LoyaltyConnector failed to connect.");
        loyaltyConnector = null;
      }
    }
  }

  @SuppressWarnings("unused")
  public void startLoyaltyServices() {
    if (isStopping) { // onDestroy will set this flag to true.
      return;
    }
  }

  /**
   * Mirrors the Activity Lifecycle method (@see Activity#onStart())
   * Consumers of this class should call this method in their activity's "onStart()" method:
   * <pre>
   *   public class CloverLoyaltyCustomActivity extends Activity {
   *       private CloverCFPLoyaltyHelper cloverCFPLoyaltyHelper;
   *       ...
   *       protected void onStart() {
   *           super.onStart();
   *           cloverCFPLoyaltyHelper.onStart();
   *       }
   *       ...
   *   }
   * </pre>
   *
   * @since 4.1.0
   */
  @SuppressWarnings("unused")
  public void onStart() {
    Log.d(LOG_TAG, "Activity lifecycle being started");
  }

  /**
   * Mirrors the Activity Lifecycle method (@see Activity#onStop())
   * Consumers of this class should call this method in their activity's "onStop()" method:
   * <pre>
   *   public class CloverLoyaltyCustomActivity extends Activity {
   *       private CloverCFPLoyaltyHelper cloverCFPLoyaltyHelper;
   *       ...
   *       protected void onStop() {
   *           super.onStop();
   *           cloverCFPLoyaltyHelper.onStop();
   *       }
   *       ...
   *   }
   * </pre>
   *
   * @since 4.1.0
   */
  @SuppressWarnings("unused")
  public void onStop() {
    Log.d(LOG_TAG, "Activity lifecycle being stopped");
  }

  /**
   * Given a config type, returns the corresponding LoyaltyDataConfig.
   * <p>
   * Type will resemble something like:  "EMAIL", "PHONE", "CLEAR"
   *
   * @param type - a string representing the LoyaltyDataConfig key
   * @return null, if none exists
   * @see LoyaltyDataTypes
   * @since 4.1.0
   */
  @Nullable
  @SuppressWarnings("unused")
  public LoyaltyDataConfig getLoyaltyDataConfig(@Nullable String type) {
    if (typeToConfig == null || type == null) {
      return null;
    }

    return typeToConfig.get(type);
  }

  /**
   * This method is called by the ServiceConnector.OnServiceConnectedListener#onServiceConnected when it receives
   * a connection notification.
   * <p>
   * Consumers of this class who want to receive the onLoyaltyDataLoaded() event will want to implement the
   * CloverCFPLoyaltyHelper.LoyaltyListener interface:
   * <pre>
   *   public class CloverLoyaltyCustomActivity extends Activity implements CloverCFPLoyaltyHelper.LoyaltyListener {
   *       private CloverCFPLoyaltyHelper cloverCFPLoyaltyHelper;
   *       ...
   *       public void onLoyaltyDataLoaded(List<LoyaltyDataConfig> loyaltyDataConfigList, CustomerInfo customerInfo) {
   *           // Handle it here...
   *           // Use it to update various UI widgets...maybe like the Customer Panel, etc.
   *       }
   *       ...
   *   }
   * </pre>
   *
   * @param configs      - from the LoyaltyConnector - a list of the desired loyalty data configs
   * @param customerInfo - from the SessionConnector
   * @since 4.1.0
   */
  final protected void onLoyaltyDataLoaded(List<LoyaltyDataConfig> configs, CustomerInfo customerInfo) {
    Log.d(LOG_TAG, "onLoyaltyDataLoaded " + loyaltyDataConfigList);

    for (LoyaltyDataConfig config : configs) {
      typeToConfig.put(config.getType(), config);
    }

    loyaltyDataConfigList = configs;
    // Here is where we give integrators the ability to also handle the onLoyaltyDataLoaded event too.
    LoyaltyListener loyaltyListener = weakReferenceLoyaltyListener.get();
    if (loyaltyListener != null) {
      loyaltyListener.onLoyaltyDataLoaded(configs, customerInfo);
    }
  }

  /**
   * Message sent from the merchant facing side.  This method just forwards the request to the LoyaltyListener class.
   * <p>
   * It is called by the CloverCFPCommsHelper#onMessage() method from within the CloverCFPLoyaltyHelper's constructor.
   * <p>
   * Consumers of this class who want to receive the onLoyaltyDataLoaded() event will want to implement the
   * ICloverCFP interface:
   * <pre>
   *   public class CloverLoyaltyCustomActivity extends Activity implements CloverCFPLoyaltyHelper.LoyaltyListener {
   *       private CloverCFPLoyaltyHelper cloverCFPLoyaltyHelper;
   *       ...
   *       //Use this method to receive a message from the merchant side
   *       public void onMessage(String payload) {
   *          // Handle it here..
   *       }
   *
   *       //Use this method to send a message back to the merchant side
   *       public void sendMessage(String payload) throws Exception {
   *           cloverCFPLoyaltyHelper.sendMessage(payload);
   *       }
   *       ...
   *   }
   * </pre>
   *
   * @param payload - most likely a string JSON payload
   * @since 4.1.0
   */
  private void onMessage(String payload) {
    // Here is where we give integrators the ability to handle the onMessage event too.
    LoyaltyListener loyaltyListener = weakReferenceLoyaltyListener.get();
    if (loyaltyListener != null) {
      loyaltyListener.onMessage(payload);
    }
  }

  /**
   * Send a message back to the POS.  Used by Activities that desire to send a message back to the merchant facing
   * side.
   *
   * @param payload - mostly likely a JSON serialized string.
   * @throws Exception - thrown when the action id for messages sent from the Custom Activity have not been set
   * @see #onMessage(String) javadoc
   * @since 4.1.0
   */
  @SuppressWarnings("unused")
  public void sendMessage(String payload) throws Exception {
    cloverCFPCommsHelper.sendMessage(payload);
  }

  /**
   * Cleans up the sessionConnector, loyalty connector and
   * <p>
   * Consumers of this class will want to call this method in their Activity's onDestroy method:
   *
   * <pre>
   *   public class CloverLoyaltyCustomActivity extends Activity {
   *       private CloverCFPLoyaltyHelper cloverCFPLoyaltyHelper;
   *       ...
   *       protected void onDestroy() {
   *           // Tell the helper to clean up everything
   *           cloverCFPLoyaltyHelper.dispose();
   *           super.onDestroy();
   *       }
   *       ...
   *   }
   * </pre>
   *
   * @since 4.1.0
   */
  public void dispose() {
    Log.d(LOG_TAG, "Activity lifecycle dispose() is being called");

    isStopping = true;

    if (sessionConnector != null) {
      boolean result = sessionConnector.removeSessionListener(internalSessionListener);
      Log.d(LOG_TAG, String.format("Removing CloverCFPLoyaltyHelper as a session listener: %s", result));
      sessionConnector.disconnect();
    }

    if (loyaltyConnector != null) {
      loyaltyConnector.disconnect();
    }

    internalSessionListener = null;
    sessionConnector = null;
    loyaltyConnector = null;

    Activity activity = getActivity();
    if (activity != null) {
      for (BroadcastReceiver receiver : loyaltyServiceStateChangeReceiver.values()) {
        activity.unregisterReceiver(receiver);
      }
    } else {
      Log.w(LOG_TAG, "context is null!");
    }

    if (weakReferenceLoyaltyListener != null) {
      weakReferenceLoyaltyListener = null;
    }

    loyaltyServiceStateChangeReceiver.clear();

    cloverCFPCommsHelper.dispose();
    cloverCFPActivityHelper.dispose();
  }

  /**
   * @since 4.1.0
   */
  protected final Map<String, BroadcastReceiver> getLoyaltyServiceStateChangeReceiver() {
    return loyaltyServiceStateChangeReceiver;
  }

  /**
   * @since 4.1.0
   */
  protected final Activity getActivity() {
    return (activityWeakReference != null) ? activityWeakReference.get() : null;
  }

  /**
   * @since 4.1.0
   */
  protected final LoyaltyConnector getLoyaltyConnector() {
    return loyaltyConnector;
  }

  /**
   * Finish the activity setting RESULT_OK with a null payload.
   * <p>
   * Here's an example of using this method:
   *
   * <pre>
   *   public class CloverLoyaltyCustomActivity extends Activity {
   *       private CloverCFPLoyaltyHelper cloverCFPLoyaltyHelper;
   *       ...
   *       public void onMessage(String payload) {
   *           // this is a custom message that will finish the activity if a "finish" message is sent
   *           // by the pos
   *           if ("finish".equals(payload)) {
   *               cloverCFPLoyaltyHelper.finishActivity();
   *           } else {
   *               ...
   *           }
   *       }
   *       ...
   *   }
   * </pre>
   *
   * @since 4.1.0
   */
  @SuppressWarnings("unused")
  public final void finishActivity() {
    cloverCFPActivityHelper.setResultAndFinish(RESULT_OK, null);
  }

  /**
   * @param type the LoyaltyServiceType to start
   * @since 4.1.0
   * @deprecated - use com.clover.cfp.activity.CloverCFPLoyaltyHelper#start(java.lang.String, java.util.Map, java.lang.String)
   * with start(type, null, null) for all services
   *
   */
  public void start(final String type) {
    String config = null;
    start(type, null, config);
  }


  /**
   * use to start a loyalty data service.
   *
   * @param type         - will look like:  "EMAIL", "PHONE", "CLEAR"
   * @param dataExtrasIn -
   * @param config       - Usually a JSON serialized string
   * @since 4.1.0
   */
  public void start(final String type, @Nullable final Map<String, String> dataExtrasIn, final String config) {
    callConnector(new StartRunnable(this, type, dataExtrasIn, config));
  }

  private static class StartRunnable implements Runnable {
    private final WeakReference<CloverCFPLoyaltyHelper> cloverCFPLoyaltyHelperWeakReference;
    private final String type;
    private final Map<String, String> dataExtrasIn;
    private final String config;

    StartRunnable(CloverCFPLoyaltyHelper cloverCFPLoyaltyHelper, final String type, final Map<String, String> dataExtrasIn, final String config) {
      this.cloverCFPLoyaltyHelperWeakReference = new WeakReference<>(cloverCFPLoyaltyHelper);
      this.type = type;
      this.dataExtrasIn = dataExtrasIn;
      this.config = config;
    }

    @Override
    public void run() {
      try {
        Log.d(LOG_TAG, String.format("Calling connector.start(%s)", type));

        CloverCFPLoyaltyHelper cloverCFPLoyaltyHelper = cloverCFPLoyaltyHelperWeakReference.get();
        if (cloverCFPLoyaltyHelper == null) {
          Log.d(LOG_TAG, "Unable to start b/c CloverCFPLoyaltyHelper was null!");
          return;
        }

        String key = ILoyaltyDataService.Util.getServiceStateEventAction(type);
        if (cloverCFPLoyaltyHelper.getLoyaltyServiceStateChangeReceiver().get(key) == null) {
          BroadcastReceiver statusReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
              String state = intent.getStringExtra(ILoyaltyDataService.LOYALTY_SERVICE_STATE_EVENT);
              cloverCFPLoyaltyHelper.onLoyaltyServiceStateChanged(type, state);
            }
          };

          Context context = cloverCFPLoyaltyHelper.getActivity();
          if (context != null) {
            context.registerReceiver(statusReceiver, new IntentFilter(key));
          }
          cloverCFPLoyaltyHelper.getLoyaltyServiceStateChangeReceiver().put(key, statusReceiver);
        }
        Map<String, String> dataExtras = cloverCFPLoyaltyHelper.addToLoyaltyServiceExtras(dataExtrasIn);
        dataExtras.put(
          "com.clover.payment.executor.secure.EXTRA_PROXY_PROVIDER", // todo: This constant needs to be in ONE place
          "com.clover.payment.builder.pay");

        LoyaltyConnector loyaltyConnector = cloverCFPLoyaltyHelper.getLoyaltyConnector();
        if (loyaltyConnector != null) {
          loyaltyConnector.startLoyaltyService(type, dataExtras, config);
        } else {
          Log.w(LOG_TAG, "Unable to start Loyalty Service because it is null!");
        }
      } catch (Exception e) {
        Log.e(LOG_TAG, String.format("Error when starting service of type %s", type), e);
      }
    }
  }

  /**
   * Override this to get updates to loyalty data service.
   *
   * @param type  - something like:  "EMAIL", "PHONE", "CLEAR"
   * @param state - a string that looks like:  "com.clover.loyalty.service.state.running" or "com.clover.loyalty.service.state.running"
   * @since 4.1.0
   */
  final protected void onLoyaltyServiceStateChanged(String type, String state) {
    LoyaltyListener loyaltyListener = weakReferenceLoyaltyListener.get();
    if (loyaltyListener != null) {
      loyaltyListener.onLoyaltyServiceStateChanged(type, state);
    }
  }

  /**
   * Used to stop a loyalty data service
   *
   * @param type -
   * @since 4.1.0
   */
  public void stop(final String type) {
    final Runnable runLater = new StopRunnable(loyaltyConnector, type);
    callConnector(runLater);
  }

  private static class StopRunnable implements Runnable {
    private final WeakReference<LoyaltyConnector> loyaltyConnectorWeakReference;
    private final String type;

    StopRunnable(LoyaltyConnector loyaltyConnector, String type) {
      this.loyaltyConnectorWeakReference = new WeakReference<>(loyaltyConnector);
      this.type = type;
    }

    @Override
    public void run() {
      try {
        LoyaltyConnector loyaltyConnector = loyaltyConnectorWeakReference.get();
        if (loyaltyConnector == null) {
          Log.w(LOG_TAG, "Unable to stop the Loyalty Service because it is null!");
          return;
        }
        Log.d(LOG_TAG, String.format("Calling connector.stop(%s)", type));
        loyaltyConnector.stopLoyaltyService(type);
      } catch (Exception e) {
        Log.e(LOG_TAG, "Ow!", e);
      }
    }
  }

  /**
   * Used by custom activities to announce loyalty data, collected by the custom activity
   * and put it in the loyalty platform.
   *
   * @param loyaltyDataConfig - the corresponding data config for the given data
   * @param data              - mostly likely a JSON serialized string.
   * @since 4.1.0
   */
  public void announceCustomerProvidedData(final LoyaltyDataConfig loyaltyDataConfig, final String data) {
    executor.execute(() -> callConnector(new AnnounceDataRunnable(loyaltyConnector, loyaltyDataConfig, data)));
  }

  private static class AnnounceDataRunnable implements Runnable {
    private final LoyaltyConnector loyaltyConnector;
    private final LoyaltyDataConfig loyaltyDataConfig;
    private final String data;

    AnnounceDataRunnable(LoyaltyConnector loyaltyConnector, LoyaltyDataConfig loyaltyDataConfig, String data) {
      this.loyaltyConnector = loyaltyConnector;
      this.loyaltyDataConfig = loyaltyDataConfig;
      this.data = data;
    }

    @Override
    public void run() {
      try {
        Log.d(LOG_TAG, "Calling connector.announceCustomerProvidedData");
        loyaltyConnector.announceCustomerProvidedData(loyaltyDataConfig, data);
      } catch (Exception e) {
        Log.e(LOG_TAG, "Ow!", e);
      }
    }
  }

  /**
   * Add information to the map of extra information in the VasSettings
   *
   * @param map - the possibly null map of extras
   * @return the non null map of extra data
   * <p>
   * see com.clover.remote.terminal.kiosk.RemoteTerminalKioskActivity#addToVasExtras(java.util.Map, com.clover.sdk.v3.customers.CustomerInfo)
   */
  private Map<String, String> addToLoyaltyServiceExtras(Map<String, String> map) {
    DisplayOrder displayOrder = sessionConnector.getDisplayOrder();
    return
      ILoyaltyDataService.Util.addToLoyaltyServiceExtras(map, sessionConnector.getCustomerInfo(), displayOrder != null ? displayOrder.getId() : null);
  }

  private void callConnector(final Runnable runLater) {
    if (loyaltyConnector == null) {
      Context context = activityWeakReference.get();

      if (context == null) {
        return;
      }

      ServiceConnector.OnServiceConnectedListener client = new ServiceConnector.OnServiceConnectedListener() {
        Runnable delayedRun = runLater;

        @Override
        public void onServiceConnected(ServiceConnector<? extends IInterface> connector) {
          Runnable tempDelayedRun = delayedRun;
          if (tempDelayedRun != null) {
            delayedRun = null;
            // We could thread it, but we are already in a thread...?
            Log.d(LOG_TAG, "Calling delayedRun.run!");
            executor.execute(tempDelayedRun);
          }
          Log.d(LOG_TAG, "Connected!");
        }

        @Override
        public void onServiceDisconnected(ServiceConnector<? extends IInterface> connector) {
          Log.d(LOG_TAG, "Disconnected!");
        }
      };
      loyaltyConnector = new LoyaltyConnector(context, null, client) {
        @Override
        public void onBindingDied(ComponentName name) {
          Log.d(LOG_TAG, name + " onBindingDied");
        }
      };
      if (!loyaltyConnector.connect()) {
        Log.d(LOG_TAG, "Connect failed!!!!");
        loyaltyConnector = null;
      }
    } else {
      // LoyaltyConnector is already initialized, just call the runnable.
      executor.execute(runLater);
    }
  }

  /**
   * used to force stop a loyalty data service
   *
   * @param type      - the type of loyalty
   * @param forceStop - true to force stop.
   * @since 4.1.0
   */
  public void stop(final String type, boolean forceStop) {
    callConnector(new ForceStopRunnable(loyaltyConnector, type, forceStop));
  }

  private static class ForceStopRunnable implements Runnable {

    private final LoyaltyConnector loyaltyConnector;
    private final String type;
    private final boolean forceStop;

    public ForceStopRunnable(LoyaltyConnector connector, String type, boolean forceStop) {
      this.loyaltyConnector = connector;
      this.type = type;
      this.forceStop = forceStop;
    }

    @Override
    public void run() {
      try {
        Log.d(LOG_TAG, String.format("Calling connector.stop(%s)", type));
        //For now, we will send over if we are forcing stop or not.  We can fill this config map with
        //whatever we need later.
        Map<String, String> config = new HashMap<>();
        config.put(ILoyaltyDataService.LOYALTY_SERVICE_STATE_EVENT_NOT_RUNNING_FORCE, String.valueOf(forceStop));
        loyaltyConnector.stopLoyaltyService(type, config);
      } catch (Exception e) {
        Log.e(LOG_TAG, "Ow!", e);
      }
    }
  }
}

