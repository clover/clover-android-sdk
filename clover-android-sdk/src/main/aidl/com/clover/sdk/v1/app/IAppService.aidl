package com.clover.sdk.v1.app;

import com.clover.sdk.v1.app.AppNotification;
import com.clover.sdk.v1.ResultStatus;

/**
 * An interface for interacting with the v1 Clover app service. The app
 * service is a bound AIDL service. Bind to this service as follows:
 * <pre>
 * <code>
 * Intent serviceIntent = new Intent(AppIntent.ACTION_APP_SERVICE);
 * serviceIntent.putExtra(Intents.EXTRA_ACCOUNT, CloverAccount.getAccount(context));
 * serviceIntent.putExtra(Intents.EXTRA_VERSION, 1);
 * context.bindService(serviceIntent);
 * </code>
 * </pre>
 * For more information about bound services, refer to
 * the Android documentation:
 * <a href="http://developer.android.com/guide/components/bound-services.html#Binding">
 * Bound Services
 * </a>.
 * <br/><br/>
 * You may also interact with the merchant service through the
 * {@link com.clover.sdk.v1.app.AppConnector} class, which handles binding and
 * asynchronous invocation of service methods.
 * <p>
 * @see com.clover.sdk.v1.app.AppConnector
 * @see com.clover.sdk.v3.apps.IAppsService
 */
interface IAppService {

    /**
     * Send a notification to all devices running this app for this merchant.
     * The notification is queued and sent in the background, so it's safe to make this
     * call on the main thread.
     */
    void notify(in AppNotification notification, out ResultStatus resultStatus);

}
