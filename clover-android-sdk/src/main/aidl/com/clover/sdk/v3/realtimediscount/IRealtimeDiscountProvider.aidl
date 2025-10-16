package com.clover.sdk.v3.realtimediscount;

import com.clover.sdk.v3.realtimediscount.DiscountRequest;
import com.clover.sdk.v3.realtimediscount.DiscountResponse;
import com.clover.sdk.v3.realtimediscount.CapturedDiscount;
import com.clover.sdk.v3.realtimediscount.IDiscountCallback;

interface IRealtimeDiscountProvider {
    void getDiscount(in DiscountRequest request, in IDiscountCallback callback);
    void onDiscountFinalized(in CapturedDiscount capturedDiscount);
}
