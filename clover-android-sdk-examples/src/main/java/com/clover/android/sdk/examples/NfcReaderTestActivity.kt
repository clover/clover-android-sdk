package com.clover.android.sdk.examples

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.clover.android.sdk.examples.mifare.MifareCardReaderType
import com.clover.android.sdk.examples.nfc.NfcReaderStatusActivity
import com.clover.android.sdk.examples.nfc.NfcReaderOperationIds

class NfcReaderTestActivity : AppCompatActivity() {

    var nfcReaderOperationId = NfcReaderOperationIds.FELICA_UUID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_nfc_reader_test)

        findViewById<Button>(R.id.button_felica_uuid).setOnClickListener {
            nfcReaderOperationId = NfcReaderOperationIds.FELICA_UUID
            startNfcReaderStatusActivity()
        }

        findViewById<Button>(R.id.button_felica_suica_read).setOnClickListener {
            nfcReaderOperationId = NfcReaderOperationIds.FELICA_COMMAND
            startNfcReaderStatusActivity()
        }
    }

    private fun startNfcReaderStatusActivity() {
        startActivity(Intent(this, NfcReaderStatusActivity::class.java).apply {
            putExtra("NFC_READER_OPERATION_ID", nfcReaderOperationId.type)
        })
    }
}