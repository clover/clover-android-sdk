package com.clover.sdk.v3.nfc.connector

import com.clover.sdk.v3.nfc.listener.INfcReaderClientListener
import com.clover.sdk.v3.nfc.model.FelicaCardCommand
import com.clover.sdk.v3.nfc.model.FelicaCardResponse
import com.clover.sdk.v3.nfc.model.FelicaCardUuid

/**
 * Nfc reader Client to perform NFC reader operations.
 * Please make sure to finish any NFC reading/writing operations within 30 seconds.
 * If you couldn't finish an operation with in 30 seconds, please invoke connect again.
 */
interface INfcReaderClient {

    /**
     * Connect Nfc reader service client to start NFC operations.
     * Please make sure to finish any NFC reading/writing operations with in 30 seconds
     */
    fun connect(readerClientListener: INfcReaderClientListener)

    /**
     * Disconnect the NFC reader client after finishing operation.
     */
    fun disconnect()

    /**
     * Cancel any running existing NFC operation. It will throw error in current running operation.
     */
    fun cancel()

    /**
     * Returns Felica card UUID.
     * This function turns on the RF and starts polling.
     */
    fun felicaUuid(): FelicaCardUuid?

    /**
     * Returns Felica card response.
     * Use this function to send Felica commands after felicaUuid() is called.
     * Please make sure card is tapped until felicaUuid & felicaCommand operations are completed.
     */
    fun felicaCommand(felicaCardCmd: FelicaCardCommand): FelicaCardResponse?

    /**
     * Turn on RF and initialize NFC controller for Felica operations
     */
    fun felicaRfOn()

    /**
     * Returns Felica card response.
     * Use this function to send Felica commands after felicaRfOn() is called.
     * This function sends the commands as it without NFC reader library pre-processing.
     * Please make sure card is tapped until all felicaCommandRaw operations are completed.
     */
    fun felicaCommandRaw(felicaCardCmd: FelicaCardCommand): FelicaCardResponse?

    /**
     * Turn off RF.
     * Call this function after all card operations are done.
     */
    fun felicaRfOff()
}