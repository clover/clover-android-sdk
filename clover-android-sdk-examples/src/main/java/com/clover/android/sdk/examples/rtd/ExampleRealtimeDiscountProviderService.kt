package com.clover.android.sdk.examples.rtd

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.clover.sdk.v3.realtimediscount.CapturedDiscount
import com.clover.sdk.v3.realtimediscount.DiscountRequest
import com.clover.sdk.v3.realtimediscount.DiscountResponse
import com.clover.sdk.v3.realtimediscount.IDiscountCallback
import com.clover.sdk.v3.realtimediscount.IRealtimeDiscountProvider

/**
 * Example implementation of a Realtime Discount Provider Service in Kotlin.
 * Third-party developers can use this as a reference for implementing their own discount logic.
 * To turn on RealtimeDiscount for this sample service Clover `PAYMENTS_R` permission is required
 */
class ExampleRealtimeDiscountProviderService : Service() {

    private val binder = object : IRealtimeDiscountProvider.Stub() {
        override fun getDiscount(request: DiscountRequest, callback: IDiscountCallback) {
            Log.d(TAG, "getDiscount called for order: ${request.orderId}")

            // Example logic: Apply a 10% discount if the amount is greater than $100
            var discountAmount = 0L
            var description = "No Discount"

            // No discount provided for now . plugin your code here to provide discount
            val discountedAmount = request.amount - discountAmount

            val response = DiscountResponse(
              discountedAmount = discountedAmount,
              tipAmount = request.tipAmount,
              taxAmount = request.taxAmount,
              discountType = "ExampleRTD",
              orderId = request.orderId,
              success = true,
              errorMessage = null,
              additionalData = null
            )

            callback.onResult(response)
        }

        override fun onDiscountFinalized(capturedDiscount: CapturedDiscount) {
            Log.d(TAG, "onDiscountFinalized called for order: ${capturedDiscount.orderId}")
            // Perform any post-transaction cleanup or logging here
        }

        override fun onPartialApproval(capturedDiscount: CapturedDiscount, callback: IDiscountCallback) {
            Log.d(TAG, "onPartialApproval called for order: ${capturedDiscount.orderId}")

            // Example logic: Recalculate discount based on the partially approved amount
            // For this example, we'll keep the same with 0% logic but apply it to the captured (approved) amount

            var newDiscountAmount = 0L

            // In real case you might need to adjust for this example no adjustments
            val newDiscountedAmount = capturedDiscount.capturedAmount - newDiscountAmount

            // In a real scenario, you might want to check if the discount should remain fixed or be pro-rated.
            val response = DiscountResponse(
              discountedAmount = newDiscountedAmount,
              tipAmount = capturedDiscount.tipAmount,
              taxAmount = capturedDiscount.taxAmount,
              discountType = "EasyRTD",
              orderId = capturedDiscount.orderId,
              success = true,
              errorMessage = null,
              additionalData = null
            )

            callback.onResult(response)
        }
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    companion object {
        private const val TAG = "ExampleRTDService"
    }
}
