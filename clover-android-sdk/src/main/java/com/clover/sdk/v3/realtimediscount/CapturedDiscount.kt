package com.clover.sdk.v3.realtimediscount

import android.os.Parcelable
import com.clover.sdk.v3.base.CardData
import kotlinx.parcelize.Parcelize

/**
 * Represents a discount that has been captured (finalized or partially approved).
 *
 * @property capturedAmount The amount that was captured/authorized.
 * @property taxAmount The tax amount associated with the captured transaction.
 * @property tipAmount The tip amount associated with the captured transaction.
 * @property cardDetails The details of the card used for the transaction.
 * @property orderId The unique identifier of the order.
 * @property status The status of the capture (FULL, PARTIAL, DECLINED).
 * @property isPOSRemote Whether the transaction originated from a remote POS.
 * @property additionalData Map of additional data specific to the provider or region.
 */
@Parcelize
data class CapturedDiscount(
  val capturedAmount: Long,
  val taxAmount: Long?,
  val tipAmount: Long?,
  val cardDetails: CardData,
  val orderId: String,
  val status: CaptureStatus,
  val isPOSRemote: Boolean = false,
  val additionalData: Map<String, String>? = null
) : Parcelable

@Parcelize
enum class CaptureStatus : Parcelable {
  FULL,
  PARTIAL,
  DECLINED
}