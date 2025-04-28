package com.clover.sdk.v3.mifare.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class MifareCardType: Parcelable {
  Unknown,
  MIFARE_ULTRALIGHT,
  MIFARE_CLASSIC_1K,
  MIFARE_CLASSIC_4K,
  MIFARE_DESFIRE,
  TYPE_A_LAYER_3,
  TYPE_A_LAYER_4,
  TYPE_B_LAYER_3,
  TYPE_B_LAYER_4,
  ISO_15693,
  ISO_8000
}