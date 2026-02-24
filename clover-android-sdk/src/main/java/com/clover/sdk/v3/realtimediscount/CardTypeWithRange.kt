package com.clover.sdk.v3.realtimediscount

data class CardTypeWithRange(
    val cardType: String,
    val lowBin: String,
    val highBin: String
) {
    companion object {
        const val JSON_KEY = "cardTypeWithRange"
    }
}
