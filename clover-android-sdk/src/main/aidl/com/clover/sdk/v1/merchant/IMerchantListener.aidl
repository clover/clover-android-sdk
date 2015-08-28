/*
 * Copyright (C) 2013 Clover Network, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
