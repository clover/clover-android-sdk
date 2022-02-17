package com.clover.sdk.v3.apps;

import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v3.apps.App;
import com.clover.sdk.v3.apps.AppBillingInfo;

/**
 * An interface for interacting with the v3 Clover apps service. The apps
 * service is a bound AIDL service. Bind to this service as follows:
 * <pre>
 * <code>
 * Intent serviceIntent = new Intent(AppsIntent.ACTION_APPS_SERVICE);
 * serviceIntent.putExtra(Intents.EXTRA_ACCOUNT, CloverAccount.getAccount(context));
 * serviceIntent.putExtra(Intents.EXTRA_VERSION, 1);
 * context.bindService(serviceIntent);
 * </code>
 * </pre>
 * For more information about bound services, refer to
 * the Android documentation:
 * <a href="https://developer.android.com/guide/components/bound-services.html#Binding">
 * Bound Services
 * </a>.
 * <p>
 * You may also interact with the merchant service through the
 * {@link AppsConnector} class, which handles binding and
 * asynchronous invocation of service methods.
 * <p>
 * @see com.clover.sdk.v3.apps.AppsConnector
 */
interface IAppsService {

  /**
   * Get App object with information about this app.
   *
   * @param resultStatus an output parameter with the result of the operation
   */
  App getApp(out ResultStatus resultStatus);

  /**
   * Log a metered event occurance. The merchant will be billed for this number of events.
   *
   * @param meteredId the id of the meter option to log
   * @param numberOfEvent the count of how many events occured
   * @param resultStatus an output parameter with the result of the operation
   */
  void logMetered(String meteredId, int numberOfEvent, out ResultStatus resultStatus);

  /**
   * Get AppBillingInfo object with billing information for the merchant running this app.
   *
   * @param resultStatus an output parameter with the result of the operation
   */
  AppBillingInfo getAppBillingInfo(out ResultStatus resultStatus);

  /**
   * Update the Smart Receipt text for this app. See the
   * <a href="https://docs.clover.com/build/smart-reciepts/" target="_blank">Clover developer site
   * for more information about Smart Receipts.</a>
   *
   * @param text the text to display on the receipt
   * @param resultStatus an output parameter with the result of the operation
   */
  void setSmartReceiptText(String text, out ResultStatus resultStatus);

  /**
   * Update the Smart Receipt URL for this app. See the
   * <a href="https://docs.clover.com/build/smart-reciepts/" target="_blank">Clover developer site
   * for more information about Smart Receipts.</a>
   *
   * @param text the URL to be used on the receipt
   * @param resultStatus an output parameter with the result of the operation
   */
  void setSmartReceiptUrl(String url, out ResultStatus resultStatus);

}
