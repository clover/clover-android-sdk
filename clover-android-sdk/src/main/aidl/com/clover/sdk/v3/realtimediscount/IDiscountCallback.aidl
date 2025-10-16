package com.clover.sdk.v3.realtimediscount;

import com.clover.sdk.v3.realtimediscount.DiscountResponse;

interface IDiscountCallback {
    void onResult(in DiscountResponse response);
    void onError(int errorCode, String errorMessage);
}
