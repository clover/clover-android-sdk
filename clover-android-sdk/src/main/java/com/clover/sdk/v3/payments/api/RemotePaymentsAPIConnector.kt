package com.clover.sdk.v3.payments.api

import android.app.Activity
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.clover.sdk.v3.remotepay.CloverDeviceEvent
import com.clover.sdk.v3.remotepay.InputOption

/**
 * A connector that enables device requests (e.g. Read Card), using Android Payments API
 * created intents on Customer Facing devices. This will ONLY work on remote devices that are tethered
 * to a Merchant Facing Clover device.
 */
class RemotePaymentsAPIConnector(var context: Context) {
    private var _rpapiConnector: RemotePaymentsAPI_V1_Connector? = null
    private var _listener:RemotePaymentsAPI_V1_ConnectorListener? = null
    private var _intent:Intent? = null;

    private var serviceConnection = object:ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            RemotePaymentsAPI_V1_Connector.Stub.asInterface(binder)?.also {
                _rpapiConnector = it
                _intent?.also {
                    intent ->
                    _rpapiConnector?.start(_intent, object:RemotePaymentsAPI_V1_ConnectorListener.Stub(){
                        override fun onDeviceEvent(cloverDeviceEvent: CloverDeviceEvent, isStartEvent:Boolean) {
                            _listener?.onDeviceEvent(cloverDeviceEvent, isStartEvent)
                        }
                        override fun onResult(resultCode:Int, data:Intent) {
                            data.setExtrasClassLoader(this.javaClass.classLoader)
                            _listener?.onResult(resultCode, data)
                            disconnect()
                        }
                    })
                } ?: {
                    Log.i(this::class.java.simpleName, "No intent to start")
                    _listener?.onResult(Activity.RESULT_CANCELED, _intent)
                    disconnect()
                }

            } ?: {
                Log.i(this::class.java.simpleName, "Wrong interface returned to connector")
                disconnect()
            }
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            disconnect()
        }
    }

    /**
     * This will start processing the request on a Customer Facing Device
     * @param intent - Intent created using Android Payments API Intent Builders
     * @param listener - listener for the result and UI state events.
     * @return - true if the start processed. It may not if there is another request in progress, or the remote device isn't available
     */
    fun start(intent:Intent, listener:RemotePaymentsAPIConnectorListener):Boolean {
        if(_rpapiConnector != null) {
            Log.i(this::class.java.simpleName, "Calling start on an active connector")
            listener.onResult(Activity.RESULT_CANCELED, null)
            return false;
        }
        _intent = intent
        _listener = listener
        context.startService(intent)
        if(!context.bindService(intent, serviceConnection, Service.BIND_ABOVE_CLIENT)) {
            _listener?.onResult(Activity.RESULT_CANCELED, Intent())
            disconnect()
            return false;
        }
        return true
    }

    /**
     * This will reset the Customer Facing device, and call back on the listener onResult method
     * if there is a transaction in process
     * @return - returns false if there isn't an active request in progress, so can't finish
     */
    fun finish():Boolean {
        if(_rpapiConnector == null) {
            Log.i(this::class.java.simpleName, "Calling finish on a disconnected connector")
            return false
        }
        _rpapiConnector?.finish()
        return true
    }

    /**
     * This method will send an input option, that is provided by the listener's
     * onDeviceEvent callback, to enable an integrator to "select" an option on behalf
     * of the Customer
     * @param - InputOption
     * @return
     */
    fun sendInputOption(option:InputOption):Boolean {
        if(_rpapiConnector == null) {
            Log.i(this::class.java.simpleName, "Calling sendInputOption on a disposed connector")
            return false
        }
        _rpapiConnector?.sendInputOption(option)
        return true
    }

    private fun disconnect() {
        context.unbindService(serviceConnection)
        _rpapiConnector = null;
    }

}