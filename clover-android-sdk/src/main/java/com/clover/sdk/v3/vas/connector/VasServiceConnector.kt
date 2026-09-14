package com.clover.sdk.v3.vas.connector

import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.clover.sdk.v1.ServiceConnector
import com.clover.sdk.v3.payments.IVasProvider
import com.clover.sdk.v3.vas.IVasReaderService
import com.clover.sdk.v3.vas.IVasReaderSessionListener

/**
 * Service connector for VAS (Valued Added Services) Reader Service.
 *
 * This connector manages:
 * - Explicit binding to com.clover.payment.core/VasReaderService
 * - VAS session lifecycle (openSession on connect, closeSession on disconnect)
 * - 30-second timeout with automatic session cleanup
 * - Service connection/disconnection callbacks via [OnServiceConnectedListener]
 */
internal class VasServiceConnector(
    context: Context,
    account: android.accounts.Account?,
    client: OnServiceConnectedListener,
    private val onSessionOpenFailed: (String) -> Unit
) : ServiceConnector<IVasReaderService>(context, account, client) {

    private var providers: List<IVasProvider> = emptyList()
    private var sessionListener: IVasReaderSessionListener? = null
    private var sessionOpened = false

    companion object {
        private const val TAG = "VasServiceConnector"
        private const val CORE_PAYMENTS_PACKAGE = "com.clover.payment.core"
        private const val VAS_SERVICE_CLASS = "com.clover.payment.service.services.vas.VasReaderService"
        private const val VAS_SERVICE_ACTION = "com.clover.payment.service.services.vas.VAS_SERVICE"
    }

    override fun getServiceIntentAction(): String {
        // Defined in core-payments-services
        return VAS_SERVICE_ACTION
    }

    override fun getServiceIntentPackage(): String = CORE_PAYMENTS_PACKAGE

    override fun getServiceInterface(iBinder: IBinder): IVasReaderService =
        IVasReaderService.Stub.asInterface(iBinder)

    /**
     * Override to use explicit component binding rather than action-based resolution.
     * This ensures we connect to the exact VasReaderService we expect.
     */
    override fun getStartIntent(): Intent =
        Intent().setClassName(CORE_PAYMENTS_PACKAGE, VAS_SERVICE_CLASS)

    /**
     * Connect to VAS Reader Service and open a session with the given providers.
     *
     * @param providers List of VAS provider binders
     * @param sessionListener Listener for VAS session-level callbacks from the service.
     * @return Nothing
     */
    internal fun connect(providers: List<IVasProvider>, sessionListener: IVasReaderSessionListener) {
        this.providers = providers.toList()
        this.sessionListener = sessionListener
        val result = super.connect()
        if (!result) {
            clearFields()
            onSessionOpenFailed("$CORE_PAYMENTS_PACKAGE is not available")
        }
    }

    internal fun clearFields() {
        providers = emptyList()
        sessionListener = null
    }

    /**
     * Called by ServiceConnector after the service has been bound.
     * Opens the VAS session, starts the timeout timer, and notifies the client.
     */
    override fun notifyServiceConnected(client: OnServiceConnectedListener?) {
        val service = getService()
        if (service == null) {
            Log.e(TAG, "Service interface is null after binding")
            onSessionOpenFailed("Service interface is null after binding")
            disconnect()
            return
        }

        try {
            val providerBinders = providers.map { it.asBinder() }
            val sessionOpenedSuccessfully = service.openSession(providerBinders, sessionListener)
            if (!sessionOpenedSuccessfully) {
                Log.e(TAG, "VasReaderService refused to open session; disconnecting")
                sessionOpened = false
                onSessionOpenFailed("VasReaderService refused to open session")
                disconnect()
                return
            }

            sessionOpened = true
            Log.d(TAG, "Connected to VasReaderService and opened session")

            // Notify the client that the service is connected
            super.notifyServiceConnected(client)
        } catch (e: Exception) {
            Log.e(TAG, "Error opening VAS session", e)
            sessionOpened = false
            onSessionOpenFailed("Error opening VAS session")
            disconnect()
        }
    }

    /**
     * Called by ServiceConnector when the service has been disconnected.
     * Stops the session timeout timer and notifies the client.
     */
    override fun notifyServiceDisconnected(client: OnServiceConnectedListener?) {
        Log.d(TAG, "Disconnected from VasReaderService")
        sessionOpened = false
        super.notifyServiceDisconnected(client)
    }

    /**
     * Gracefully disconnect from the service, closing the VAS session if open.
     * Uses ServiceConnector's lifecycle guarantees (safe unbind, state clearing).
     */
    override fun disconnect() {
        if (sessionOpened) {
            try {
                service?.closeSession()
            } catch (e: Exception) {
                Log.e(TAG, "Error closing VAS session", e)
            }
        }
        sessionOpened = false
        providers = emptyList()
        sessionListener = null
        super.disconnect()
    }

    /**
     * Restart the timeout countdown without reconnecting or reopening the VAS session.
     *
     * @return true when the timer was reset, false when there is no active session.
     */
    fun resetTimeout(): Boolean {
        if (!sessionOpened) {
            return false
        }

        service.resetTimeout()
        return true
    }
}

