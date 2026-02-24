package com.clover.sdk.v3.payments.api

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver

interface ResultCallback {
    fun onReceiveResult(resultCode: Int, resultData: Bundle?)
}

class PAPIResultReceiver(var callback: ResultCallback) : ResultReceiver(Handler(Looper.getMainLooper())) {
    override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
        callback.onReceiveResult(resultCode, resultData)
    }
}