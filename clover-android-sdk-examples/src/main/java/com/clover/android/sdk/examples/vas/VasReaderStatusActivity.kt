package com.clover.android.sdk.examples.vas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.clover.android.sdk.examples.R
import com.clover.sdk.v3.payments.IVasProvider
import com.clover.sdk.v3.payments.VasMode
import com.clover.sdk.v3.payments.VasPayload
import com.clover.sdk.v3.payments.VasPayloadResponse
import com.clover.sdk.v3.payments.VasPayloadResponseType
import com.clover.sdk.v3.payments.VasProtocol
import com.clover.sdk.v3.payments.VasServiceProvider
import com.clover.sdk.v3.payments.VasProviderConfig
import com.clover.sdk.v3.payments.VasSettings
import com.clover.sdk.v3.vas.connector.VasReaderClient
import com.clover.sdk.v3.vas.listener.IVasReaderClientListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VasReaderStatusActivity : AppCompatActivity() {

    private var isVasServiceConnected = false
    private var isVasServiceConnecting = false
    private var isVasReading = false
    private lateinit var vasStatusText: TextView
    private lateinit var passTypeIdsInput: EditText
    private lateinit var vasClientBtn: Button
    private lateinit var vasReadBtn: Button

    private val vasReaderClient by lazy { VasReaderClient.getInstance(application) }

    private val vasProvider = object : IVasProvider.Stub() {
        override fun handlePayload(
            payload: VasPayload?,
            vasMode: VasMode?,
            extras: Intent?
        ): VasPayloadResponse {
            return VasPayloadResponse().setResponseType(VasPayloadResponseType.ACCEPTED)
        }

        override fun getVasProviders(): MutableList<VasServiceProvider> = buildVasProviders()
    }

    private val readerClientListener = object : IVasReaderClientListener {
        override fun onConnect() {
            isVasServiceConnecting = false
            isVasServiceConnected = true
            updateStatus("Client connected. Tap Start VAS Read.")
            updateClientButton()
            updateReadButtonEnabled(true)
            updateReadButton(false)
        }

        override fun onDisconnect() {
            isVasServiceConnecting = false
            isVasServiceConnected = false
            updateStatus("Client disconnected")
            updateClientButton()
            updateReadButtonEnabled(false)
            updateReadButton(false)
        }

        override fun onUserInterventionRequired() {
            updateStatus("User action required. Please follow the device prompt.")
        }

        override fun onUserInterventionCleared() {
            updateStatus("User action cleared. Continue reading.")
        }

        override fun onVasReadTimeout() {
            stopVasRead("VAS session timed out. Please start VAS read again.")
        }

        override fun onConnectFailed() {
            updateStatus("Connect failed immediately")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vas_reader_status)

        vasStatusText = findViewById(R.id.text_vas_status)
        passTypeIdsInput = findViewById(R.id.edit_pass_type_ids)

        vasClientBtn = findViewById(R.id.button_connect_client)
        vasClientBtn.setOnClickListener {
            if (isVasServiceConnected || isVasServiceConnecting) {
                disconnectClient()
            } else {
                connectClient()
            }
        }

        vasReadBtn = findViewById<Button>(R.id.button_start_vas_read)
        vasReadBtn.isEnabled = false
        vasReadBtn.setOnClickListener {
            if (isVasReading) {
                stopVasRead()
            } else {
                startVasRead()
            }
        }

        findViewById<Button>(R.id.button_cancel).setOnClickListener {
            disconnectClient()
            finish()
        }

        updateClientButton()
        updateStatus("Client disconnected. Tap Connect Client.")
        updateReadButton(false)
    }

    private fun connectClient() {
        if (isVasServiceConnected || isVasServiceConnecting) return

        val configuredProviders = buildVasProviders()
        if (configuredProviders.isEmpty()) {
            updateStatus("Enter at least one PK passTypeId before connecting")
            return
        }

        isVasServiceConnecting = true
        updateClientButton()
        updateStatus("Connecting client with ${configuredProviders.size} passTypeId(s)...")

        /*
            connect() is non-blocking: it delegates to bindService(), which returns immediately.
            The connection result is delivered asynchronously via [com.clover.sdk.v3.vas.listener.IVasReaderClientListener].
         */
        runCatching {
            vasReaderClient.connect(
                listOf(vasProvider),
                readerClientListener
            )
        }.onFailure { error ->
            isVasServiceConnecting = false
            isVasServiceConnected = false
            Log.e(TAG, "Unable to connect VAS reader", error)
            updateStatus("Unable to connect to VAS reader")
            updateClientButton()
            updateReadButtonEnabled(false)
            updateReadButton(false)
        }
    }

    private fun disconnectClient() {
        isVasServiceConnecting = false
        isVasServiceConnected = false
        runCatching { vasReaderClient.disconnect() }
        updateStatus("Client disconnected. Tap Connect Client.")
        updateClientButton()
        updateReadButtonEnabled(false)
        updateReadButton(false)
    }

    private fun buildVasProviders(): MutableList<VasServiceProvider> {
        return parsePassTypeIds().map { passTypeId ->
            VasServiceProvider()
                .setProviderPackage(VAS_PROVIDER_PACKAGE)
                .setProtocolId(VasProtocol.PK)
                .setProtocolConfig(mapOf(VasProviderConfig.PK_PASS_TYPE_ID to passTypeId))
        }.toMutableList()
    }

    private fun parsePassTypeIds(): List<String> {
        return passTypeIdsInput.text
            ?.toString()
            .orEmpty()
            .split(',', '\n', '\r')
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .distinct()
    }

    private fun startVasRead() {
        if (!isVasServiceConnected) {
            updateStatus("VAS reader is not connected yet")
            updateReadButtonEnabled(false)
            updateReadButton(false)
            return
        }

        updateReadButtonEnabled(true)
        updateReadButton(true)
        updateStatus("Ready to read. Tap phone/card now.")
        lifecycleScope.launch {
            runCatching {
                vasReaderClient.startVasRead()
            }.onSuccess { response ->
                val responseType = response?.responseType?.name ?: "No response"
                updateReadButton(false)
                updateStatus("VAS read complete: $responseType")
            }.onFailure { error ->
                Log.e(TAG, "VAS read failed", error)
                updateReadButton(false)
                updateStatus("VAS read failed: ${error.message ?: "Unknown error"}")
            }
        }
    }

    private fun stopVasRead(message: String? = null) {
        runCatching { vasReaderClient.cancel() }
        updateReadButton(false)
        updateStatus(message ?: "VAS read stopped")
    }

    private fun updateStatus(status: String) {
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                vasStatusText.text = status
            }
        }
    }

    private fun updateReadButton(reading: Boolean) {
        isVasReading = reading
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                vasReadBtn.text = if (reading) "Stop VAS Read" else "Start VAS Read"
            }
        }
    }

    private fun updateReadButtonEnabled(enabled: Boolean) {
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                vasReadBtn.isEnabled = enabled
            }
        }
    }

    override fun onStop() {
        disconnectClient()
        super.onStop()
    }

    private fun updateClientButton() {
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                vasClientBtn.isEnabled = !isVasServiceConnecting || isVasServiceConnected
                vasClientBtn.text = when {
                    isVasServiceConnecting -> "Connecting..."
                    isVasServiceConnected -> "Disconnect Client"
                    else -> "Connect Client"
                }
            }
        }
    }

    companion object {
        private const val TAG = "VasReaderStatusActivity"
        private const val VAS_PROVIDER_PACKAGE = "com.clover.android.sdk.examples.vas"
    }
}