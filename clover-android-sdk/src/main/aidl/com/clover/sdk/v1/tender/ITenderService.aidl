package com.clover.sdk.v1.tender;

import com.clover.sdk.v1.tender.Tender;
import com.clover.sdk.v1.ResultStatus;

/**
 * An interface for interacting with the Clover tender service. The tender
 * service is a bound AIDL service. Bind to this service as follows,
 * <pre>
 * <code>
 * Intent serviceIntent = new Intent(TenderIntent.ACTION_TENDER_SERVICE);
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
 * <br/><br/>
 * You may also interact with the service through the
 * {@link com.clover.sdk.v1.tender.TenderConnector} class, which handles binding and
 * asynchronous invocation of service methods.
 * <p>
 * A tender is a method of payment allowed by the Merchant such as Credit Card, Cash or Check.
 * Merchants may support custom tenders to allow customers to pay via special reward programs for
 * example. Some tenders are built-in and cannot be deleted or modified, these are called system
 * tenders.
 * <p>
 * @see com.clover.sdk.v1.tender.TenderIntent
 * @see com.clover.sdk.util.CloverAccount
 * @see com.clover.sdk.v1.tender.TenderConnector
 */
interface ITenderService {

  /**
   * Get all the tenders for this merchant. Note that some tenders may not be enabled.
   */
  List<Tender> getTenders(out ResultStatus resultStatus);

  /**
   * Create a new tender with the given label and seetings. The label key must be the package name
   * of your application.
   *
   * @param label String shown to merchant for this Tender
   * @param labelKey The package name of your application, unless you are {@code com.clover.*}
   * @param enabled true if you want the Tender to appear to the Merchant
   * @param opensCashDrawer true if you want the cash drawer to open when a payment with this tender
   *                        is taken
   * @return null if the tender could not be created
   */
  Tender checkAndCreateTender(String label, String labelKey, boolean enabled, boolean opensCashDrawer, out ResultStatus resultStatus);

  /**
   * Enabling or disabling a tender causes the tender to either be shown in or hidden by the Clover
   * Register and Sale apps.
   */
  Tender setEnabled(in String tenderId, in boolean enabled, out ResultStatus resultStatus);

  /**
   * Delete the tender for the given id.
   */
  void delete(in String tenderId, out ResultStatus resultStatus);

  /**
   * When a payment is made with this tender the cash drawer can be made to automatically open or
   * not.
   */
  void setOpensCashDrawer(in String tenderId, in boolean opensCashDrawer, out ResultStatus resultStatus);

  /**
   * Change the label for the given tender id.
   */
  void setLabel(in String tenderId, in String tenderLable, out ResultStatus resultStatus);

}
