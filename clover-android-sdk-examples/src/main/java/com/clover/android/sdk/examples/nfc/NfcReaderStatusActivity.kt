package com.clover.android.sdk.examples.nfc

import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.NfcF
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.clover.android.sdk.examples.R
import com.clover.sdk.util.Platform2
import com.clover.sdk.v3.merchant.MerchantDevicesV2Connector
import com.clover.sdk.v3.nfc.connector.NfcReaderClient
import com.clover.sdk.v3.nfc.listener.INfcReaderClientListener
import com.clover.sdk.v3.nfc.model.FelicaCardCommand
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NfcReaderStatusActivity : AppCompatActivity() {

    private var isNfcServiceConnected = false
    private var useNativeNfc = false
    private var nfcAdapter: NfcAdapter? = null
    private var nfcStartupJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc_reader_status)
        findViewById<Button>(R.id.button_cancel).setOnClickListener {
            if (!useNativeNfc && isNfcServiceConnected) {
                NfcReaderClient.getInstance(application).cancel()
            }
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        nfcStartupJob = lifecycleScope.launch {
            val device = withContext(Dispatchers.IO) {
                if (Platform2.isClover()) {
                    runCatching {
                        MerchantDevicesV2Connector(applicationContext).device
                    }.onFailure {
                        Log.w(TAG, "Unable to query Clover device information", it)
                    }.getOrNull()
                } else {
                    null
                }
            }

            val model = device?.model ?: Build.MODEL
            val productName = device?.productName
            val modelDescription = productName
                ?.takeUnless { it.equals(model, ignoreCase = true) }
                ?.let { "$it ($model)" }
                ?: model

            findViewById<TextView>(R.id.text_nfc_status_title).text =
                "Clover model: $modelDescription\n\nPlease tap NFC card on the screen"

            useNativeNfc = listOfNotNull(productName, model, Build.MODEL).any {
                it.equals(MINI_4_PRODUCT_NAME, ignoreCase = true) ||
                    it.equals(MINI_4_MODEL, ignoreCase = true)
            }

            if (useNativeNfc) {
                startNativeNfc()
            } else {
                connectCloverNfcService()
            }
        }
    }

    private fun connectCloverNfcService() {
        lifecycleScope.launch(Dispatchers.IO) {
            NfcReaderClient.getInstance(application).connect(object : INfcReaderClientListener {
                override fun onConnected() {
                    isNfcServiceConnected = true
                    startCloverNfc()
                }

                override fun onDisconnect() {
                    isNfcServiceConnected = false
                }
            })
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun startCloverNfc() {
        val nfcReaderOperationId = getNfcReaderOperationId()
        val nfcStatusText = findViewById<TextView>(R.id.text_nfc_status)

        if (nfcReaderOperationId == NfcReaderOperationIds.FELICA_UUID) {
            if (isNfcServiceConnected) {
                lifecycleScope.launch(Dispatchers.IO) {
                    NfcReaderClient.getInstance(application)
                        .felicaUuid()?.felicaCardUuid?.let { uuidString ->
                            withContext(Dispatchers.Main) {
                                displayFelicaUuid(nfcStatusText, uuidString)
                            }
                        } ?: run {
                        withContext(Dispatchers.Main) {
                            nfcStatusText.text = "Felica card UUID read failed"
                        }
                        Log.d(TAG, "Felica card UUID no response received")
                    }
                }
            } else {
                Log.d(TAG, "Felica Service is disconnected, please restart activity")
            }
        }

        if (nfcReaderOperationId == NfcReaderOperationIds.FELICA_COMMAND) {
            if (isNfcServiceConnected) {
                lifecycleScope.launch(Dispatchers.IO) {
                    NfcReaderClient.getInstance(application).felicaCommand(
                        FelicaCardCommand("0000030107")
                    )?.cardRsp?.let { felicaCardRspData ->
                        withContext(Dispatchers.Main) {
                            nfcStatusText.text = felicaCardRspData
                        }
                    } ?: run {
                        withContext(Dispatchers.Main) {
                            nfcStatusText.text =
                                "Felica command failed. Please check the card and try again."
                        }
                        Log.d(TAG, "Felica command NO response received")
                    }

                    NfcReaderClient.getInstance(application).felicaCommand(
                        FelicaCardCommand(SUICA_READ_COMMAND)
                    )?.cardRsp?.let { felicaCardRspData ->
                        withContext(Dispatchers.Main) {
                            displaySuicaTransactions(nfcStatusText, felicaCardRspData)
                        }
                    } ?: run {
                        withContext(Dispatchers.Main) {
                            nfcStatusText.text =
                                "Felica command failed. Please check the card and try again."
                        }
                        Log.d(TAG, "Felica command NO response received")
                    }
                }
            }
        }
    }

    private fun getNfcReaderOperationId(): NfcReaderOperationIds {
        return when (intent.extras?.getInt("NFC_READER_OPERATION_ID")) {
            0 -> NfcReaderOperationIds.FELICA_UUID
            1 -> NfcReaderOperationIds.FELICA_COMMAND
            else -> NfcReaderOperationIds.FELICA_UUID
        }
    }

    private fun startNativeNfc() {
        val nfcStatusText = findViewById<TextView>(R.id.text_nfc_status)
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        val adapter = nfcAdapter
        if (adapter == null) {
            nfcStatusText.text = "Native NFC is not available on this device."
            return
        }
        if (!adapter.isEnabled) {
            nfcStatusText.text = "NFC is disabled. Enable NFC and try again."
            return
        }

        adapter.enableReaderMode(
            this,
            { tag -> handleNativeFelicaTag(tag) },
            NfcAdapter.FLAG_READER_NFC_F or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
            null
        )
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun handleNativeFelicaTag(tag: Tag) {
        lifecycleScope.launch(Dispatchers.IO) {
            val nfcStatusText = findViewById<TextView>(R.id.text_nfc_status)
            val nfcF = NfcF.get(tag)
            if (nfcF == null) {
                withContext(Dispatchers.Main) {
                    nfcStatusText.text = "The detected card is not a FeliCa card."
                }
                return@launch
            }

            try {
                if (getNfcReaderOperationId() == NfcReaderOperationIds.FELICA_UUID) {
                    val uuidString = (tag.id + nfcF.manufacturer).toHexString().uppercase()
                    withContext(Dispatchers.Main) {
                        displayFelicaUuid(nfcStatusText, uuidString)
                    }
                    return@launch
                }

                nfcF.connect()
                val response = nfcF.transceive(buildNativeFelicaCommand(tag.id, SUICA_READ_COMMAND))
                val cardResponse = normalizeNativeFelicaResponse(response).toHexString().uppercase()
                withContext(Dispatchers.Main) {
                    displaySuicaTransactions(nfcStatusText, cardResponse)
                }
            } catch (exception: Exception) {
                Log.w(TAG, "Native FeliCa operation failed", exception)
                withContext(Dispatchers.Main) {
                    nfcStatusText.text =
                        "Felica command failed. Please check the card and try again."
                }
            } finally {
                if (nfcF.isConnected) {
                    nfcF.close()
                }
            }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun buildNativeFelicaCommand(idm: ByteArray, commandHex: String): ByteArray {
        val command = commandHex.hexToByteArray()
        return byteArrayOf((command.size + idm.size + 1).toByte(), command[0]) +
            idm + command.copyOfRange(1, command.size)
    }

    private fun normalizeNativeFelicaResponse(response: ByteArray): ByteArray {
        require(response.size >= FELICA_RESPONSE_HEADER_SIZE) {
            "FeliCa response is too short"
        }
        require(response[1] == FELICA_READ_RESPONSE_CODE) {
            "Unexpected FeliCa response code"
        }
        return response.copyOfRange(FELICA_RESPONSE_HEADER_SIZE, response.size)
    }

    private fun displayFelicaUuid(nfcStatusText: TextView, uuidString: String) {
        if (uuidString.length < FELICA_UUID_HEX_LENGTH) {
            nfcStatusText.text = "Felica card UUID read failed"
            return
        }
        nfcStatusText.text = "$uuidString\n\nID: ${uuidString.substring(0, 16)}" +
            "\nPMm: ${uuidString.substring(16)}"
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun displaySuicaTransactions(nfcStatusText: TextView, felicaCardRspData: String) {
        val responseBytes = felicaCardRspData.hexToByteArray()
        if (responseBytes.size < 3 || responseBytes[0] != 0.toByte() ||
            responseBytes[1] != 0.toByte()) {
            nfcStatusText.text = "Felica card returned an error."
            return
        }

        var transactionDetails = "$felicaCardRspData\n"
        for (index in 0 until responseBytes[2].toInt()) {
            val dataOffset = 3 + index * SUICA_TRANSACTION_SIZE
            if (dataOffset + SUICA_TRANSACTION_SIZE > responseBytes.size) {
                break
            }
            transactionDetails += suicaParseTransaction(responseBytes, dataOffset) + "\n"
        }
        nfcStatusText.text = transactionDetails
    }

    override fun onStop() {
        nfcStartupJob?.cancel()
        nfcStartupJob = null
        if (useNativeNfc) {
            nfcAdapter?.disableReaderMode(this)
        } else {
            NfcReaderClient.getInstance(application).disconnect()
        }
        isNfcServiceConnected = false
        super.onStop()
    }

    // this is for Suica transaction data parsing
    // Define the valid IDs as private constants at the top of your class
    private val SHOPPING_PROC_IDS = setOf(70, 73, 74, 75, 198, 203)
    private val BUS_PROC_IDS = setOf(13, 15, 31, 35)

    /**
     * Checks if the process ID corresponds to a shopping transaction.
     */
    private fun suicaIsShopping(procId: Int): Boolean {
        return procId in SHOPPING_PROC_IDS
    }

    /**
     * Checks if the process ID corresponds to a bus transaction.
     */
    private fun suicaIsBus(procId: Int): Boolean {
        return procId in BUS_PROC_IDS
    }

    private fun suicaParseTransaction(xatData: ByteArray, dataOffset: Int): String {
        val balance = toInt(xatData, dataOffset, 11, 10)
        val xatDate = toInt(xatData, dataOffset, 4, 5)
        val xatYear = (xatDate shr 9) and 0x007F
        val xatMonth = (xatDate shr 5) and 0x000F
        val xatDay = xatDate and 0x001F

        val xatKind = xatData[dataOffset + 1]
        var xatKindText = "JR"
        if (suicaIsShopping(xatKind.toInt())) {
            xatKindText = "物販"
        } else {
            if (suicaIsBus(xatKind.toInt())) {
                xatKindText = "バス"
            } else {
                if (xatData[dataOffset + 1] >= 0x80) {
                    xatKindText = "公営/私鉄"
                }
            }
        }

        return "残高:¥$balance 日付:${2000 + xatYear}年${xatMonth}月${xatDay}日 処理:$xatKindText"
    }

    private fun toInt(res: ByteArray, off: Int, vararg idx: Int): Int {
        var num = 0
        for (j in idx) {
            num = num shl 8
            num += (res[off + j].toInt()) and 0x0ff
        }
        return num
    }

    private companion object {
        val TAG: String = NfcReaderStatusActivity::class.java.simpleName
        const val MINI_4_PRODUCT_NAME = "Mini 4"
        const val MINI_4_MODEL = "Clover_C306"
        const val SUICA_READ_COMMAND =
            "06010F090A8000800180028003800480058006800780088009"
        const val SUICA_TRANSACTION_SIZE = 16
        const val FELICA_UUID_HEX_LENGTH = 32
        const val FELICA_RESPONSE_HEADER_SIZE = 10
        const val FELICA_READ_RESPONSE_CODE: Byte = 0x07
    }
}