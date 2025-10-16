package com.clover.sdk.v3.realtimediscount

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiscountResponse(
    val discountAmount: Long,
    val tipAmount: Long? = null,
    val taxAmount: Long? = null,
    val orderId: String? = null,
    val discountType: String,
    val success: Boolean,
    val errorMessage: String? = null
) : Parcelable
