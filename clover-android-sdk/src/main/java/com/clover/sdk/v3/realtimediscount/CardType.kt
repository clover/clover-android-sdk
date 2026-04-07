package com.clover.sdk.v3.realtimediscount

/**
 * Represents a specific card type (e.g., VISA, MASTERCARD).
 *
 * @property cardType The string representation of the card type.
 */
data class CardType(
    val cardType: String
) {
    companion object {
        const val JSON_KEY = "cardType"
    }
}
