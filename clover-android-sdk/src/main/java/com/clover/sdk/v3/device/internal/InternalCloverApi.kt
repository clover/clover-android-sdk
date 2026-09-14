package com.clover.sdk.v3.device.internal

@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "This is an internal Clover API and must not be used by third-party applications."
)
@Retention(AnnotationRetention.BINARY)
annotation class InternalCloverApi
