package com.clover.sdk.v3.device.internal

@InternalCloverApi
data class AttestationPayload(
    val deviceTruths: Map<String, String>,
    val userTruths: Map<String, String>
)
