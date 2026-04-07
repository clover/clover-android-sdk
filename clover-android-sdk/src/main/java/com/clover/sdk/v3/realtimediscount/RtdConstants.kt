package com.clover.sdk.v3.realtimediscount

/**
 * Constants for Real Time Discount additional data map keys.
 * These are used to pass Argentina-specific and other provider-specific data through the RTD interface.
 */
object RtdConstants {
    // Request keys
    /** Key for passing the merchant ID in the discount request additional data. */
    const val KEY_MERCHANT_ID = "merchantId"
    /** Key for passing the number of installments in the discount request additional data. */
    const val KEY_INSTALLMENTS = "installments"
    /** Key for passing the entry mode (e.g., SWIPED, DIPPED) in the discount request additional data. */
    const val KEY_ENTRY_MODE = "entryMode"
    
    // Response keys
    /** Key for returning a new merchant ID in the discount response additional data. */
    const val KEY_NEW_MERCHANT_ID = "newMerchantId"
    /** Key for returning a new installment count in the discount response additional data. */
    const val KEY_NEW_INSTALLMENTS = "newInstallments"
    /** Key for indicating if changes should be confirmed/applied in the discount response additional data. */
    const val KEY_CONFIRM_CHANGES = "confirmChanges"
}
