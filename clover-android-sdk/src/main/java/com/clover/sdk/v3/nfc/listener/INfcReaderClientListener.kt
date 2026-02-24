package com.clover.sdk.v3.nfc.listener

interface INfcReaderClientListener {
    fun onConnected()
    fun onDisconnect()
}