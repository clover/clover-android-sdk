package com.clover.sdk.v3.realtimediscount

/**
 * Standardized error codes for IDiscountCallback.
 */
object DiscountErrorCode {
    const val UNKNOWN_ERROR = 1000
    const val INTERNAL_ERROR = 1001
    const val NETWORK_FAILURE = 1002
    const val TIMEOUT = 1003
    const val INVALID_REQUEST = 1004

    @JvmStatic
    fun nameOf(code: Int): String {
        return when (code) {
            UNKNOWN_ERROR -> "UNKNOWN_ERROR"
            INTERNAL_ERROR -> "INTERNAL_ERROR"
            NETWORK_FAILURE -> "NETWORK_FAILURE"
            TIMEOUT -> "TIMEOUT"
            INVALID_REQUEST -> "INVALID_REQUEST"
            else -> "UNKNOWN_CODE_$code"
        }
    }
}
