package com.clover.sdk.v3.mifare.listener

interface IMifareCardReaderClientListener {
  fun onConnected()
  fun onDisconnect()
}