package com.clover.sdk.v3.nfc.connector

import android.content.Context
import com.clover.sdk.v3.nfc.listener.INfcReaderClientListener
import com.clover.sdk.v3.nfc.model.FelicaCardCommand
import com.clover.sdk.v3.nfc.model.FelicaCardResponse
import com.clover.sdk.v3.nfc.model.FelicaCardUuid
import com.clover.sdk.v3.payment.raw.listener.IServiceListener
import com.clover.sdk.v3.nfc.service.INfcReaderService

internal class NfcReaderClientImpl(private val context: Context) : NfcReaderClient() {
    private val nfcServiceConnector = NfcServiceConnector(context)
    private var iNfcReaderClientListener: INfcReaderClientListener? = null
    private var iNfcReaderService: INfcReaderService? = null
    private var isNfcReaderServiceConnected = false

    override fun connect(readerClientListener: INfcReaderClientListener) {
        iNfcReaderClientListener = readerClientListener
        nfcServiceConnector.connect(nfcReaderListener)
    }

    override fun disconnect() {
        nfcServiceConnector.disconnect()
    }

    override fun cancel() {
        if (isNfcServiceAvailable()) {
            iNfcReaderService?.cancel()
        } else {
            throw NfcReaderException("Cancel failed")
        }
    }

    override fun felicaUuid(): FelicaCardUuid? {
        if (isNfcServiceAvailable()) {
            return iNfcReaderService?.felicaUuid()
        }
        throw NfcReaderException("Couldn't read Felica card UUID.")
    }

    override fun felicaCommand(felicaCardCmd: FelicaCardCommand): FelicaCardResponse? {
        if (isNfcServiceAvailable()) {
            val felicaCardCmd: FelicaCardCommand =
                FelicaCardCommand(felicaCardCmd.commandDataInHex);
            return iNfcReaderService?.felicaCommand(felicaCardCmd)
        }
        throw NfcReaderException("Couldn't execute Felica card command.")
    }

    override fun felicaRfOn() {
        if (isNfcServiceAvailable()) {
            iNfcReaderService?.felicaRfOn()
        }
    }

    override fun felicaCommandRaw(felicaCardCmd: FelicaCardCommand): FelicaCardResponse? {
        if (isNfcServiceAvailable()) {
            val felicaCardCmd: FelicaCardCommand =
                FelicaCardCommand(felicaCardCmd.commandDataInHex);
            return iNfcReaderService?.felicaCommandRaw(felicaCardCmd)
        }
        throw NfcReaderException("Couldn't execute Felica card raw command.")
    }

    override fun felicaRfOff() {
        if (isNfcServiceAvailable()) {
            iNfcReaderService?.felicaRfOff()
        }
    }

    private fun isNfcServiceAvailable(): Boolean {
        return isNfcReaderServiceConnected && iNfcReaderService != null && iNfcReaderClientListener != null
    }

    private val nfcReaderListener = object : IServiceListener<INfcReaderService> {
        override fun onConnected(nfcReaderService: INfcReaderService) {
            isNfcReaderServiceConnected = true
            iNfcReaderService = nfcReaderService
            iNfcReaderClientListener?.onConnected()
        }

        override fun onDisconnected() {
            iNfcReaderClientListener?.onDisconnect()
            isNfcReaderServiceConnected = false
            iNfcReaderService = null
            iNfcReaderClientListener = null
        }
    }
}