package com.clover.sdk.v3.mifare.connector

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import com.clover.sdk.v3.mifare.service.IMifareCardReaderService
import com.clover.sdk.v3.payment.raw.listener.IServiceListener

internal class MifareServiceConnector(private val context: Context) {

  private var bound = false
  private var serviceConnection: ServiceConnection? = null
  private var mifareServiceBinder: IMifareCardReaderService? = null
  private var timer: CountDownTimer? = null

  companion object {
    private const val MIFARE_SERVICE_CLASS = "com.clover.payment.service.services.mifare.MifareCardReaderService"
    private const val CORE_PAYMENTS_PACKAGE = "com.clover.payment.core"
    private const val DEVICE_TIMEOUT: Long = 30 * 1000 // 30 seconds
    private const val TIME_INTERVAL_ONE_SEC: Long = 1000 // 1 sec
  }



  internal fun connect(serviceListener: IServiceListener<IMifareCardReaderService>) {
    val serviceIntent = Intent()
    serviceIntent.setClassName(CORE_PAYMENTS_PACKAGE, MIFARE_SERVICE_CLASS)
    if (!isServiceAvailable(context, serviceIntent)){
      throw RuntimeException("$CORE_PAYMENTS_PACKAGE is not available")
    }

    serviceConnection = object : ServiceConnection {
      override fun onServiceConnected(componentName: ComponentName, binder: IBinder) {
        val localMifareServiceBinder = IMifareCardReaderService.Stub.asInterface(binder)
        localMifareServiceBinder.openSession()
        bound = true
        mifareServiceBinder = localMifareServiceBinder
        startTimer()
        serviceListener.onConnected(localMifareServiceBinder)
      }

      override fun onServiceDisconnected(p0: ComponentName?) {
        bound = false
        mifareServiceBinder?.closeSession()
        timer?.cancel()
        serviceListener.onDisconnected()
      }
    }
    context.bindService(serviceIntent, serviceConnection as ServiceConnection, Context.BIND_AUTO_CREATE)
  }

  internal fun disconnect(): Boolean {
    if (bound) {
      serviceConnection?.let { localServiceConnection ->
        context.unbindService(localServiceConnection)
      }?:run {
        Log.i(MifareServiceConnector::class.simpleName, "Mifare service not connected on disconnect")
      }
      timer?.cancel()
      bound = false
      serviceConnection = null
      return true
    }
    return false
  }

  internal fun isServiceAvailable(context: Context, serviceIntent: Intent): Boolean {
    val infos = context.packageManager.queryIntentServices(serviceIntent, PackageManager.GET_META_DATA)
    return infos.isNotEmpty()
  }

  private fun startTimer() {
    timer = object: CountDownTimer(DEVICE_TIMEOUT, TIME_INTERVAL_ONE_SEC) {
      override fun onTick(tickTime: Long) {
        // Not needed in current logic
      }

      override fun onFinish() {
        disconnect()
      }
    }.apply {
      start()
    }

  }

}