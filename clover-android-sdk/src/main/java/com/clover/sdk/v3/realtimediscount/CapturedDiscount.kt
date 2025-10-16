package com.clover.sdk.v3.realtimediscount

import android.os.Parcelable
import com.clover.sdk.v3.base.CardData
import kotlinx.parcelize.Parcelize

@Parcelize
enum class CaptureStatus : Parcelable {
  FULL,
  PARTIAL,
  DECLINED
}

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