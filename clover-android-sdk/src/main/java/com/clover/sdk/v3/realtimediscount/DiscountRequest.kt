package com.clover.sdk.v3.realtimediscount

import android.os.Parcelable
import com.clover.sdk.v3.base.CardData
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiscountRequest(
    val amount: Long,
    val tipAmount: Long? = null,
    val taxAmount: Long? = null,
    val orderId: String? = null,
    val cardDetails: CardData? = null,
    val isPOSRemote: Boolean = false,
    val additionalData: Map<String, String>? = null
) : Parcelable
