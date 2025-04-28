package com.clover.sdk.v3.mifare.connector

import android.annotation.SuppressLint
import android.content.Context

abstract class MifareCardReaderClient: IMifareCardReaderClient {

  companion object {
    @SuppressLint("StaticFieldLeak")
    private var instance: IMifareCardReaderClient? = null

    /**
     * Returns instance of Mifare card client. Please make sure to invoke connect before doing any operations.
     * You can use this instance to do any card reader operations only for 30 seconds.
     * After 30 seconds please invoke connect again.
     */
    fun getInstance(context: Context): IMifareCardReaderClient {
      return instance ?: synchronized(this) {
        instance ?: MifareCardReaderClientImpl(context).also { instance = it }
      }
    }
  }
}