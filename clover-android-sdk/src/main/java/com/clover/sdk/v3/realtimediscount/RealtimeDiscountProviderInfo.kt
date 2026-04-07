package com.clover.sdk.v3.realtimediscount

/**
 * Information about a resolved Real-Time Discount provider.
 *
 * @property packageName The package name of the provider application.
 * @property serviceName The name of the service component to bind to.
 * @property displayName A human-readable name for the provider.
 * @property priority The priority of this provider (higher values indicate higher priority).
 * @property remotePackageName The package name of the remote provider, if applicable (for cloud-based discounts).
 */
data class RealtimeDiscountProviderInfo(
    val packageName: String,
    val serviceName: String,
    val displayName: String? = null,
    val priority: Int = 0,
    val remotePackageName: String? = null
)
