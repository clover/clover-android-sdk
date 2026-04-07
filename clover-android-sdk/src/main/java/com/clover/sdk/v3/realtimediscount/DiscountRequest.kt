package com.clover.sdk.v3.realtimediscount

import android.os.Parcelable
import com.clover.sdk.v3.base.CardData
import kotlinx.parcelize.Parcelize

/**
 * Represents a request for a real-time discount.
 *
 * @property amount The transaction amount before discount.
 * @property tipAmount The tip amount, if any.
 * @property taxAmount The tax amount, if any.
 * @property orderId The unique identifier of the order.
 * @property cardDetails The details of the card being used.
 * @property isPOSRemote Whether the request originates from a remote POS.
 * @property additionalData Map of additional data specific to the provider or region.
 */
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
