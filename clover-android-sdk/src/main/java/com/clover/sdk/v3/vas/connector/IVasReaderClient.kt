package com.clover.sdk.v3.vas.connector

import androidx.annotation.WorkerThread
import com.clover.common2.payments.PayIntent
import com.clover.sdk.v3.payments.IVasProvider
import com.clover.sdk.v3.payments.VasPayloadResponse
import com.clover.sdk.v3.payments.VasSettings
import com.clover.sdk.v3.vas.listener.IVasReaderClientListener

interface IVasReaderClient {

    /**
     * Connect VAS reader service client to start VAS operations.
     * Please make sure to finish any VAS reading operations within 30 seconds
     */
    fun connect(vasProviders: List<IVasProvider>, readerClientListener: IVasReaderClientListener)

    /**
     * Resets the active session timeout without reconnecting or reinjecting providers.
     *
     * @return true if there is an active session and the timer was reset.
     */
    fun resetTimeout(): Boolean

    /**
     * Disconnect the VAS reader client after finishing operation.
     */
    fun disconnect()

    /**
     * Cancel any running existing VAS operation. It will throw error in current running operation.
     */
    @Throws(VasReaderException::class)
    fun cancel()

    /**
     * Starts communication with Secureboard to read VAS data.
     * It will return VAS payload response if the operation is successful, otherwise null.
     */
    @Throws(VasReaderException::class)
    suspend fun startVasRead(vasSettings: VasSettings? = null): VasPayloadResponse?

    @Throws(VasReaderException::class)
    @WorkerThread
    fun startVasReadBlocking(): VasPayloadResponse?

    /**
     * Launches core-payments activity to start VAS read with payment.
     * @param vasSettings the settings for VAS read operation, must set [com.clover.sdk.v3.payments.VasMode]
     * @param payIntent the payment intent for payment operation.
     *
     * @return nothing, the result of the activity will be returned in onActivityResult with request code [VasReaderClient.REQUEST_CODE_VAS].
     */
    @Throws(VasReaderException::class)
    fun startVasReadWithPayment(
        vasSettings: VasSettings,
        payIntent: PayIntent
    )
}