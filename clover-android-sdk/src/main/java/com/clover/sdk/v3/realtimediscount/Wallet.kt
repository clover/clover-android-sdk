package com.clover.sdk.v3.realtimediscount

/**
 * Represents a digital wallet (e.g., GOOGLE_PAY, APPLE_PAY).
 *
 * @property wallet The string identifier for the wallet type.
 */
data class Wallet(
    val wallet: String
) {
    companion object {
        const val JSON_KEY = "wallet"
    }
}
