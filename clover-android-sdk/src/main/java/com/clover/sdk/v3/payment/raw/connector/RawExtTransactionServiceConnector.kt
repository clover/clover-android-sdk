package com.clover.sdk.v3.payment.raw.connector

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.IBinder
import com.clover.sdk.v3.payment.raw.listener.IServiceListener
import com.clover.sdk.v3.payment.raw.service.IRawExtTransactionService

class RawExtTransactionServiceConnector constructor(private val context: Context) {

    private var bound = false
    private var serviceConnection: ServiceConnection? = null

    companion object {
        private const val CORE_PAYMENTS_SERVICE_CLASS = "com.clover.payment.service.services.RawExtTransactionService"
        private const val CORE_PAYMENTS_PACKAGE = "com.clover.payment.core"
    }

    fun connect(serviceListener: IServiceListener<IRawExtTransactionService>) {
        val serviceIntent = Intent()
        serviceIntent.setClassName(CORE_PAYMENTS_PACKAGE, CORE_PAYMENTS_SERVICE_CLASS)
        if (!isServiceAvailable(context, serviceIntent)){
            throw RuntimeException("$CORE_PAYMENTS_PACKAGE is not available")
        }

        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(componentName: ComponentName, binder: IBinder) {
                val rawTxnService = IRawExtTransactionService.Stub.asInterface(binder)
                bound = true
                serviceListener.onConnected(rawTxnService)
            }

            override fun onServiceDisconnected(p0: ComponentName?) {
                bound = false
                serviceListener.onDisconnected()
            }
        }
        context.bindService(serviceIntent, serviceConnection as ServiceConnection, Context.BIND_AUTO_CREATE)
    }

    fun disconnect(): Boolean {
        if (bound) {
            if (serviceConnection != null) {
                context.unbindService(serviceConnection!!)
            }
            bound = false
            return true
        }
        return false
    }

    fun isServiceAvailable(context: Context, serviceIntent: Intent): Boolean {
        val infos = context.packageManager.queryIntentServices(serviceIntent, PackageManager.GET_META_DATA)
        return !infos.isNullOrEmpty()
    }
}