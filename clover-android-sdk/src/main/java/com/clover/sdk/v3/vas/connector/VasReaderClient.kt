package com.clover.sdk.v3.vas.connector

import android.content.Context

abstract class VasReaderClient : IVasReaderClient {

     companion object {
         const val REQUEST_CODE_VAS = 42000

         /**
          * Returns a new VasReaderClient instance. Please make sure to invoke connect before doing any operations.
          * You can use this instance to do any VAS read operations only for 30 seconds.
          * After 30 seconds, [com.clover.sdk.v3.vas.IVasReaderSessionListener.onVasReadTimeout] is called.
          */
            fun getInstance(context: Context): IVasReaderClient = VasReaderClientImpl(context)
     }
}