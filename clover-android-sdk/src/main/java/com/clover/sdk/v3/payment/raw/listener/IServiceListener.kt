package com.clover.sdk.v3.payment.raw.listener

interface IServiceListener<T> {

    fun onConnected(t:T)

    fun onDisconnected()
}
