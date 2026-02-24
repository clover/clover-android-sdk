package com.clover.sdk.v3.nfc.connector

import android.annotation.SuppressLint
import android.content.Context

abstract class NfcReaderClient : INfcReaderClient {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: INfcReaderClient? = null

        /**
         * Returns instance of NFC reader service client. Please make sure to invoke connect before doing any operations.
         * You can use this instance to do any NFC reader operations only for 30 seconds.
         * After 30 seconds, please invoke connect again.
         */
        fun getInstance(context: Context): INfcReaderClient {
            return instance ?: synchronized(this) {
                instance ?: NfcReaderClientImpl(context).also { instance = it }
            }
        }
    }
}