package com.clover.sdk.v3.realtimediscount

/**
 * Constants for Real Time Discount additional data map keys.
 * These are used to pass Argentina-specific data through the RTD interface.
 */
object RtdConstants {
    // Request keys
    const val KEY_MERCHANT_ID = "merchantId"
    const val KEY_INSTALLMENTS = "installments"
    const val KEY_ENTRY_MODE = "entryMode"
    
    // Response keys
    const val KEY_NEW_MERCHANT_ID = "newMerchantId"
    const val KEY_NEW_INSTALLMENTS = "newInstallments"
    const val KEY_CONFIRM_CHANGES = "confirmChanges"
}
