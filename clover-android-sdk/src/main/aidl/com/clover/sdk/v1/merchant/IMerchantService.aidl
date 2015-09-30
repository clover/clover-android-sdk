package com.clover.sdk.v1.merchant;

import com.clover.sdk.v1.merchant.Merchant;
import com.clover.sdk.v1.merchant.MerchantAddress;
import com.clover.sdk.v1.merchant.IMerchantListener;
import com.clover.sdk.v1.ResultStatus;

/**
 * An interface for interacting with the Clover merchant service. The merchant
 * service is a bound AIDL service. Bind to this service as follows,
 * <pre>
 * <code>
 * Intent serviceIntent = new Intent(MerchantIntent.ACTION_MERCHANT_SERVICE);
 * serviceIntent.putExtra(MerchantIntent.EXTRA_ACCOUNT, CloverAccount.getAccount(context));
 * serviceIntent.putExtra(MerchantIntent.EXTRA_VERSION, 1);
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
 * {@link com.clover.sdk.v1.merchant.MerchantConnector} class, which handles binding and
 * asynchronous invocation of service methods.
 *
 * @see com.clover.sdk.v1.merchant.MerchantIntent
 * @see com.clover.sdk.util.CloverAccount
 * @see com.clover.sdk.v1.merchant.MerchantConnector
 */
 interface IMerchantService {
    /**
     * Get the merchant object. The merchant returned corresponds to the Clover
     * {@link android.accounts.Account} that was passed when binding to to the
     * service.
     *
     * @param resultStatus The result of the service method call.
     */
    Merchant getMerchant(out ResultStatus resultStatus);

    void setAddress(in MerchantAddress address, out ResultStatus resultStatus);
    void setPhoneNumber(in String phoneNumber, out ResultStatus resultStatus);

    void addListener(IMerchantListener listener, out ResultStatus resultStatus);
    void removeListener(IMerchantListener listener, out ResultStatus resultStatus);

    void setUpdateStock(in boolean updateStock, out ResultStatus resultStatus);
    void setTrackStock(in boolean trackStock, out ResultStatus resultStatus);
}
