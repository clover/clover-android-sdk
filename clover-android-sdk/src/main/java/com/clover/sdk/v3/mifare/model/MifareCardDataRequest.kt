package com.clover.sdk.v3.mifare.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MifareCardDataRequest(
  val blockNum: Int,
  val numBlocks: Int,
  val mifareCardKey: MifareCardKey
): Parcelable
