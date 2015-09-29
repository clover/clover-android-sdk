package com.clover.sdk.v1.merchant;

import com.clover.sdk.v1.merchant.Merchant;

/**
 * An interface for receiving events pertaining to a merchant. Add a listener as follows,
 * <pre>
 * <code>
 * iMerchantService.addListener(new IMerchantListener.Stub() {
 *     {@literal @}Override
 *     public void onMerchantChanged(Merchant merchant) {
 *       // merchant has changed, use it here
 *     }
 * };
 * </code>
 * </pre>
 * If using {@link com.clover.sdk.v1.merchant.MerchantConnector} to interact with the merchant
 * serivce, you may add listeners there.
 *
 * @see com.clover.sdk.v1.merchant.IMerchantService
 * @see com.clover.sdk.v1.merchant.IMerchantService#addListener(IMerchantListener,ResultStatus)
 * @see com.clover.sdk.v1.merchant.IMerchantService#removeListener(IMerchantListener,ResultStatus)
 * @see com.clover.sdk.v1.merchant.MerchantConnector
 */
oneway interface IMerchantListener {
    void onMerchantChanged(in Merchant merchant);
}
