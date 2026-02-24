package com.clover.android.sdk.examples.nfc

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.clover.android.sdk.examples.NfcReaderTestActivity
import com.clover.android.sdk.examples.R
import com.clover.sdk.v3.nfc.connector.NfcReaderClient
import com.clover.sdk.v3.nfc.listener.INfcReaderClientListener
import com.clover.sdk.v3.nfc.model.FelicaCardCommand
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NfcReaderStatusActivity : AppCompatActivity() {

    private var isNfcServiceConnected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc_reader_status)
        findViewById<Button>(R.id.button_cancel).setOnClickListener {
            if (isNfcServiceConnected) {
                NfcReaderClient.getInstance(application).cancel()
            }
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch(Dispatchers.IO) {
            NfcReaderClient.getInstance(application).connect(object : INfcReaderClientListener {
                override fun onConnected() {
                    isNfcServiceConnected = true
                    startNfc()
                }

                override fun onDisconnect() {
                    isNfcServiceConnected = false
                }
            })
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun startNfc() {
        val nfcReaderOPerationExtra = intent.extras?.getInt("NFC_READER_OPERATION_ID")
        val nfcReaderOperationId = when (nfcReaderOPerationExtra) {
            0 -> NfcReaderOperationIds.FELICA_UUID
            1 -> NfcReaderOperationIds.FELICA_COMMAND
            else -> NfcReaderOperationIds.FELICA_UUID
        }

        val nfcStatusText = findViewById<TextView>(R.id.text_nfc_status)

        if (nfcReaderOperationId == NfcReaderOperationIds.FELICA_UUID) {
            if (isNfcServiceConnected) {
                lifecycleScope.launch(Dispatchers.IO) {
                    NfcReaderClient.getInstance(application)
                        .felicaUuid()?.felicaCardUuid?.let { uuidString ->
                            withContext(Dispatchers.Main) {
                                nfcStatusText.text = uuidString + "\n\nID: " + uuidString.substring(
                                    0,
                                    16
                                ) + "\nPMm: " + uuidString.substring(16, uuidString.length)
                            }
                        } ?: run {
                        withContext(Dispatchers.Main) {
                            nfcStatusText.text = "Felica card UUID read failed"
                        }
                        Log.d(
                            NfcReaderTestActivity::class.simpleName,
                            "Felica card UUID no response received"
                        )
                    }
                }
            } else {
                Log.d(
                    NfcReaderTestActivity::class.simpleName,
                    "Felica Service is disconnected, please restart activity"
                )
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
                        Log.d(
                            NfcReaderTestActivity::class.simpleName,
                            "Felica command NO response received"
                        )
                    }

                    NfcReaderClient.getInstance(application).felicaCommand(
                        FelicaCardCommand("06010F090A8000800180028003800480058006800780088009")
                    )?.cardRsp?.let { felicaCardRspData ->
                        withContext(Dispatchers.Main) {

                            // parse Suica card transaction data
                            var xatDetailsAll: String = ""
                            val felicaCardRspDataByteArray = felicaCardRspData.hexToByteArray()
                            xatDetailsAll = felicaCardRspData + "\n"
                            for (index in 0 until felicaCardRspDataByteArray.get(2)) {
                                // each transaction is 16 bytes
                                val xatDetails = suicaParseTransaction(
                                    felicaCardRspDataByteArray,
                                    3 + index * 16
                                )
                                xatDetailsAll = xatDetailsAll + xatDetails + "\n"
                            }
                            nfcStatusText.text = xatDetailsAll
                        }
                    } ?: run {
                        withContext(Dispatchers.Main) {
                            nfcStatusText.text =
                                "Felica command failed. Please check the card and try again."
                        }
                        Log.d(
                            NfcReaderTestActivity::class.simpleName,
                            "Felica command NO response received"
                        )
                    }
                }
            }
        }
    }

    override fun onStop() {
        NfcReaderClient.getInstance(application).disconnect()
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

}