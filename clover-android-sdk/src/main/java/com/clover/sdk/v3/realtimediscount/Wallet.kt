package com.clover.sdk.v3.realtimediscount

data class Wallet(
    val wallet: String
) {
    companion object {
        const val JSON_KEY = "wallet"
    }
}
