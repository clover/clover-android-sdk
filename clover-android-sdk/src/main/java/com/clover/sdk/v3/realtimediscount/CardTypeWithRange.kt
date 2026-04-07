package com.clover.sdk.v3.realtimediscount

/**
 * Represents a card type with a specific BIN (Bank Identification Number) range.
 * Used for filtering discount providers based on the card issued.
 *
 * @property cardType The string representation of the card type.
 * @property lowBin The lower bound of the BIN range (inclusive).
 * @property highBin The upper bound of the BIN range (inclusive).
 */
data class CardTypeWithRange(
    val cardType: String,
    val lowBin: String,
    val highBin: String
) {
    companion object {
        const val JSON_KEY = "cardTypeWithRange"
    }
}
