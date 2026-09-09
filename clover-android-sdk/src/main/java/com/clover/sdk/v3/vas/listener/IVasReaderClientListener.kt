package com.clover.sdk.v3.vas.listener

interface IVasReaderClientListener {
    fun onConnect()
    fun onDisconnect()
    fun onUserInterventionRequired()
    fun onUserInterventionCleared()

    /**
     * Called when the active VAS session reaches the timeout threshold.
     * [com.clover.sdk.v3.vas.connector.VasReaderClient.resetTimeout] to keep the session alive
     * [com.clover.sdk.v3.vas.connector.VasReaderClient.disconnect] to finish.
     */
    fun onVasReadTimeout()

    fun onConnectFailed()
}