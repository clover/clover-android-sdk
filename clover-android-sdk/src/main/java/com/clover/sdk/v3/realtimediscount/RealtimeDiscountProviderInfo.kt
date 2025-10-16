package com.clover.sdk.v3.realtimediscount

data class RealtimeDiscountProviderInfo(
    val packageName: String,
    val serviceName: String,
    val displayName: String? = null,
    val priority: Int = 0
)
