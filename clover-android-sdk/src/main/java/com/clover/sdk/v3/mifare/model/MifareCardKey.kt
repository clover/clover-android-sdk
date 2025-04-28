package com.clover.sdk.v3.mifare.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MifareCardKey(
  val keyType: Int,
  val keyVersion: Int,
  val keyNum: Int,
  val keyData: String): Parcelable
