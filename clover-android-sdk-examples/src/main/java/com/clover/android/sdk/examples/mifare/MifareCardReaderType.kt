package com.clover.android.sdk.examples.mifare

enum class MifareCardReaderType(val type: Int) {
  CARD_UUID(0),
  CARD_UL(1),
  CARD_EV1(2),
  CLASSIC_CARD_READ(3),
  CLASSIC_CARD_WRITE(4),
  MOBILE_DRIVER_LICENSE(5),
  CANCEL(6)
}