package com.clover.sdk.v3.mifare.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MifareCardWriteRequest(
  val mifareCardDataRequest: MifareCardDataRequest,
  val cardData: String): Parcelable
