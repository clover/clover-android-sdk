package com.clover.sdk.v3.vas.connector

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.IInterface
import android.util.Log
import com.clover.sdk.v1.BindingException
import com.clover.common2.payments.PayIntent
import com.clover.sdk.v1.ServiceConnector
import com.clover.sdk.v3.payments.IVasProvider
import com.clover.sdk.v3.payments.VasMode
import com.clover.sdk.v3.payments.VasPayloadResponse
import com.clover.sdk.v3.payments.VasSettings
import com.clover.sdk.v3.vas.IVasReaderSessionListener
import com.clover.sdk.v3.vas.listener.IVasReaderClientListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

internal class VasReaderClientImpl(private val context: Context) : VasReaderClient() {

    private var iVasReaderClientListener: IVasReaderClientListener? = null
    private var iVasReaderSessionListener: IVasReaderSessionListener? = null

    private val vasServiceConnector: VasServiceConnector by lazy {
        VasServiceConnector(
            context,
            null,
            vasReaderListener,
            { reason ->
                iVasReaderClientListener?.onConnectFailed()
                iVasReaderClientListener?.onDisconnect()
                iVasReaderClientListener = null
                iVasReaderSessionListener = null
            })
    }

    override fun connect(vasProviders: List<IVasProvider>, readerClientListener: IVasReaderClientListener) {
        if (iVasReaderClientListener != null || iVasReaderSessionListener != null) {
            Log.i(this.javaClass.simpleName, "Already connected. Disconnecting first.")
            disconnect()
        }
        iVasReaderClientListener = readerClientListener
        iVasReaderSessionListener = object: IVasReaderSessionListener.Stub() {
            override fun onUserInterventionRequired() = iVasReaderClientListener?.onUserInterventionRequired() ?: Unit
            override fun onUserInterventionCleared() = iVasReaderClientListener?.onUserInterventionCleared() ?: Unit
            override fun onVasReadTimeout() = iVasReaderClientListener?.onVasReadTimeout() ?: Unit
        }

        try {
            requireNotNull(iVasReaderSessionListener).also { vasServiceConnector.connect(vasProviders, it) }
        } catch (e: BindingException) {
            Log.e(this.javaClass.simpleName, "Failed to bind to VAS service", e)
            vasServiceConnector.clearFields()
            iVasReaderClientListener = null
            iVasReaderSessionListener = null
        }
    }

    override fun disconnect() {
        vasServiceConnector.disconnect()
        iVasReaderClientListener?.onDisconnect()
        iVasReaderClientListener = null
        iVasReaderSessionListener = null
    }

    override fun resetTimeout(): Boolean = vasServiceConnector.resetTimeout()

    override fun cancel() {
        vasServiceConnector.service?.cancel()
            ?: throw VasReaderException("VAS service unavailable")
    }

    override suspend fun startVasRead(vasSettings: VasSettings?): VasPayloadResponse? {
        if (vasServiceConnector.service == null) {
            throw VasReaderException("VAS service unavailable")
        }
        return withContext(Dispatchers.IO) {
            vasServiceConnector.service?.startVasRead(vasSettings ?: VasSettings().apply { vasMode = VasMode.VAS_ONLY })
        }
    }

    override fun startVasReadBlocking(): VasPayloadResponse? =
        runBlocking { startVasRead(VasSettings()) }

    override fun startVasReadWithPayment(
        vasSettings: VasSettings,
        payIntent: PayIntent,
    ) {
        if (payIntent == null) {
            throw VasReaderException("PayIntent is required for startVasReadWithPayment")
        }

        if (context !is Activity) {
            throw VasReaderException("Must launch PayIntent from an Activity context")
        }

        val payIntent = PayIntent.Builder()
            .payIntent(payIntent)
            .vasSettings(vasSettings)
            .build()

        val intent = Intent().apply { payIntent.addTo(this) }

        context.startActivityForResult(intent, REQUEST_CODE_VAS)
    }

    private val vasReaderListener = object : ServiceConnector.OnServiceConnectedListener {
        override fun onServiceConnected(connector: ServiceConnector<out IInterface?>?) {
            iVasReaderClientListener?.onConnect()
        }

        override fun onServiceDisconnected(connector: ServiceConnector<out IInterface?>?) {
            iVasReaderClientListener?.onDisconnect()
        }
    }
}