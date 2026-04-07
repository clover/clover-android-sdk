package com.clover.sdk.v3.realtimediscount

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents the response from a real-time discount provider.
 *
 * @property discountedAmount **The new total transaction amount after the discount has been applied.**
 *   This value **replaces** the original auth request amount — it is NOT a deduction delta.
 *   - If your discount takes $10 off a $100 order, return `discountedAmount = 9000` (i.e. $90.00).
 *   - If no discount applies, echo back the original [DiscountRequest.amount] unchanged.
 *     **Never return 0 for a no-discount result** — 0 would instruct the engine to attempt
 *     a $0 authorisation.
 * @property tipAmount The updated tip amount, if modified by the provider (optional).
 * @property taxAmount The updated tax amount, if modified by the provider (optional).
 * @property orderId The unique identifier of the order, echoed from the request.
 * @property discountType A label for the discount programme applied (e.g. "BANK_PROMO", "NONE").
 *   Used for receipt printing and reconciliation logging.
 * @property success Whether the discount request was processed successfully.
 * @property errorMessage A human-readable error message if the request failed.
 * @property additionalData Map of additional data returned by the provider. Use [RtdConstants]
 *   keys for region-specific fields (e.g. [RtdConstants.KEY_CONFIRM_CHANGES]).
 */
@Parcelize
data class DiscountResponse(
    val discountedAmount: Long,
    val tipAmount: Long? = null,
    val taxAmount: Long? = null,
    val orderId: String? = null,
    val discountType: String,
    val success: Boolean,
    val errorMessage: String? = null,
    val additionalData: Map<String, String>? = null
) : Parcelable
