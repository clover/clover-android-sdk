package com.clover.sdk.v3.binhandler;

import com.clover.sdk.v3.binhandler.DynamicBinResponse;
import com.clover.sdk.v3.binhandler.DynamicBinHandlerRequest;

interface IDynamicBinHandlerService {
/**
     * Returns a DynamicBinResponse with responseType and further action required to check if certain card bins are eligible for EMI processing.
     * <p>
     * This call will return response with type to indicate if binDetails are accepted or not and
     * next set of action to be performed like activity action to be invoked.
     * @param DynamicBinHandlerRequest The BinDetails with card first 6 and last 4.Along with CardType and PaymentPlatform
     *{@link com.clover.emi.DynamicBinHandlerRequest}
     * @return DynamicBinResponse {@link com.clover.emi.DynamicBinResponse} object.
     */
      DynamicBinResponse checkBin(in DynamicBinHandlerRequest request);
}