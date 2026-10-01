package com.clover.android.sdk.examples.mifare

import android.nfc.NfcAdapter
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.nfc.tech.MifareClassic
import android.nfc.tech.MifareUltralight
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.clover.android.sdk.examples.BuildConfig
import com.clover.android.sdk.examples.MifareCardReaderTestActivity
import com.clover.android.sdk.examples.R
import com.clover.sdk.util.Platform2
import com.clover.sdk.v3.merchant.MerchantDevicesV2Connector
import com.clover.sdk.v3.mifare.connector.MifareCardReaderClient
import com.clover.sdk.v3.mifare.listener.IMifareCardReaderClientListener
import com.clover.sdk.v3.mifare.model.MifareCardDataRequest
import com.clover.sdk.v3.mifare.model.MifareCardKey
import com.clover.sdk.v3.mifare.model.MifareCardLightDataRequest
import com.clover.sdk.v3.mifare.model.MifareCardWriteRequest
import com.clover.sdk.v3.mifare.model.MifareMobileDriverLicenseRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.SecureRandom

class MifareCardReaderStatusActivity: AppCompatActivity() {

  private var isMifareServiceConnected = false
  private var useNativeNfc = false
  private var nfcAdapter: NfcAdapter? = null
  private var nfcStartupJob: Job? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_mifare_card_reader_status)
    findViewById<Button>(R.id.button_cancel).setOnClickListener {
      if (!useNativeNfc && isMifareServiceConnected) {
        MifareCardReaderClient.getInstance(application).cancel()
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

      findViewById<TextView>(R.id.text_mifare_status_title).text =
        "Clover model: $modelDescription\n\nPlease tap Mifare card on the screen"

      useNativeNfc = listOfNotNull(productName, model, Build.MODEL).any {
        it.equals(MINI_4_PRODUCT_NAME, ignoreCase = true) ||
          it.equals(MINI_4_MODEL, ignoreCase = true)
      }

      if (useNativeNfc) {
        startNativeNfc()
      } else {
        connectCloverMifareService()
      }
    }
  }

  private fun connectCloverMifareService() {
    lifecycleScope.launch(Dispatchers.IO) {
      MifareCardReaderClient.getInstance(application)
        .connect(object : IMifareCardReaderClientListener {
          override fun onConnected() {
            isMifareServiceConnected = true
            startCloverMifare()
          }

          override fun onDisconnect() {
            isMifareServiceConnected = false
          }

        })
    }
  }

  private fun getCardReaderType(): MifareCardReaderType {
    return when(intent.extras?.getInt("MIFARE_CARD_READER_TYPE")) {
      0 -> MifareCardReaderType.CARD_UUID
      1 -> MifareCardReaderType.CARD_UL
      2 -> MifareCardReaderType.CARD_EV1
      3 -> MifareCardReaderType.CLASSIC_CARD_READ
      4 -> MifareCardReaderType.CLASSIC_CARD_WRITE
      5 -> MifareCardReaderType.MOBILE_DRIVER_LICENSE
      else -> MifareCardReaderType.CLASSIC_CARD_READ
    }
  }

  private fun startCloverMifare() {
    val cardReaderType = getCardReaderType()

    val mifareStatusText = findViewById<TextView>(R.id.text_mifare_status)


    if (cardReaderType == MifareCardReaderType.CARD_UUID) {
      if (isMifareServiceConnected) {
        lifecycleScope.launch(Dispatchers.IO) {
          MifareCardReaderClient.getInstance(application)
            .cardUuid()?.cardCardUuid?.let { uuidString ->
              withContext(Dispatchers.Main) {
                mifareStatusText.text = uuidString
              }
            } ?: run {
            withContext(Dispatchers.Main) {
              mifareStatusText.text = "Card Uuid read failed"
            }
            Log.d(
                MifareCardReaderTestActivity::class.simpleName,
                "Mifare Card Uuid No response received"
            )
          }
        }
      } else {
        Log.d(
            MifareCardReaderTestActivity::class.simpleName,
            "Mifare Service is disconnected, please restart activity"
        )
      }
    }


    if (cardReaderType == MifareCardReaderType.CARD_UL) {
      if (isMifareServiceConnected) {
        lifecycleScope.launch(Dispatchers.IO) {
          MifareCardReaderClient.getInstance(application)
            .cardUltralightRead(MifareCardLightDataRequest(numBlocks = 12))?.cardData?.let { cardData ->
              withContext(Dispatchers.Main) {
                mifareStatusText.text = cardData
              }
            } ?: run {
            withContext(Dispatchers.Main) {
              mifareStatusText.text = "Card UL read failed. Please check the card and try again."
            }
            Log.d(
                MifareCardReaderTestActivity::class.simpleName,
                "Mifare Card UL Read No response received"
            )
          }
        }
      } else {
        Log.d(
            MifareCardReaderTestActivity::class.simpleName,
            "Mifare Service is disconnected, please restart activity"
        )
      }
    }



    if (cardReaderType == MifareCardReaderType.CARD_EV1) {
      if (isMifareServiceConnected) {
        lifecycleScope.launch(Dispatchers.IO) {
          MifareCardReaderClient.getInstance(application)
            .cardUltralightEv1Read(MifareCardLightDataRequest(numBlocks = 24))?.cardData?.let { cardData ->
              withContext(Dispatchers.Main) {
                mifareStatusText.text = cardData
              }
            } ?: run {
            withContext(Dispatchers.Main) {
              mifareStatusText.text = "Card EV1 read failed. Please check the card and try again."
            }
            Log.d(
                MifareCardReaderTestActivity::class.simpleName,
                "Mifare Card EV1 Read No response received"
            )
          }
        }
      } else {
        Log.d(
            MifareCardReaderTestActivity::class.simpleName,
            "Mifare Service is disconnected, please restart activity"
        )
      }
    }



    if (cardReaderType == MifareCardReaderType.CLASSIC_CARD_READ) {
      if (isMifareServiceConnected) {
        lifecycleScope.launch(Dispatchers.IO) {
          MifareCardReaderClient.getInstance(application).cardUuid()?.let { cardUuid ->
            MifareCardReaderClient.getInstance(application)
              .cardClassicRead(
                  MifareCardDataRequest(
                      blockNum = 4, numBlocks = 3,
                      mifareCardKey = MifareCardKey(0x0B, 0, 1, "FFFFFFFFFFFFFFFFFFFFFFFF")
                  )
              )?.cardData?.let { cardData ->
                withContext(Dispatchers.Main) {
                  mifareStatusText.text = cardData
                }
              } ?: run {
              withContext(Dispatchers.Main) {
                mifareStatusText.text =
                    "Card Classic read failed. Please check the card and try again."
              }
              Log.d(
                  MifareCardReaderTestActivity::class.simpleName,
                  "Mifare Card Classic Read No response received"
              )
            }
          }?: run {
            withContext(Dispatchers.Main) {
              mifareStatusText.text =
                  "Card Classic read failed. Please check the card and try again."
            }
          }

        }
      } else {
        Log.d(
            MifareCardReaderTestActivity::class.simpleName,
            "Mifare Service is disconnected, please restart activity"
        )
      }
    }



    if (cardReaderType == MifareCardReaderType.CLASSIC_CARD_WRITE) {
      if (isMifareServiceConnected) {
        lifecycleScope.launch(Dispatchers.IO) {
          MifareCardReaderClient.getInstance(application).cardUuid()?.let {
            MifareCardReaderClient.getInstance(application)
              .writeCard(
                  MifareCardWriteRequest(
                      MifareCardDataRequest(
                          blockNum = 4,
                          numBlocks = 3,
                          mifareCardKey = MifareCardKey(0x0A, 0, 1, "FFFFFFFFFFFFFFFFFFFFFFFF")
                      ),
                      "11223344556677889900112233445566"
                  )
              ).also { isSuccess ->
                withContext(Dispatchers.Main) {
                  mifareStatusText.text =
                      if (isSuccess == true) "Card Write successful" else "Card Write failed"
                }
              }
          }?: run {
            withContext(Dispatchers.Main) {
              mifareStatusText.text = "Card Write failed"
            }
          }
        }
      } else {
        Log.d(
            MifareCardReaderTestActivity::class.simpleName,
            "Mifare Service is disconnected, please restart activity"
        )
      }
    }



    if (cardReaderType == MifareCardReaderType.MOBILE_DRIVER_LICENSE) {
      if (isMifareServiceConnected) {
        lifecycleScope.launch(Dispatchers.IO) {
          val startedAt = SystemClock.elapsedRealtime()
          val response = try {
            MifareCardReaderClient.getInstance(application)
              .mobileDriverLicenseRead(MifareMobileDriverLicenseRequest(MDL_BLE_UUID))
          } finally {
            logMdlTiming("custom total", startedAt)
          }
          Log.d(
            TAG,
            "mDL custom response: present=${response != null}, " +
              "nfcDataLength=${response?.mdlNfcData?.length ?: 0}, " +
              "engagementDataLength=${response?.mdlDeviceEngagementData?.length ?: 0}"
          )
          response?.mdlDeviceEngagementData?.takeIf { it.isNotBlank() }?.let { engagementData ->
              withContext(Dispatchers.Main) {
                mifareStatusText.text = engagementData
              }
            } ?: run {
            Log.w(TAG, "mDL custom API returned no device engagement data")
            withContext(Dispatchers.Main) {
              mifareStatusText.text =
                  "Mobile Driver License failed. Please check the card and try again."
            }
          }
        }
      } else {
        Log.d(
            MifareCardReaderTestActivity::class.simpleName,
            "Mifare Service is disconnected, please restart activity"
        )
      }
    }
  }

  private fun startNativeNfc() {
    val mifareStatusText = findViewById<TextView>(R.id.text_mifare_status)
    nfcAdapter = NfcAdapter.getDefaultAdapter(this)
    val adapter = nfcAdapter
    if (adapter == null) {
      mifareStatusText.text = "Native NFC is not available on this device."
      return
    }
    if (!adapter.isEnabled) {
      mifareStatusText.text = "NFC is disabled. Enable NFC and try again."
      return
    }

    adapter.enableReaderMode(
      this,
      { tag -> handleNativeTag(tag) },
      NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_NFC_B,
      null
    )
  }

  @OptIn(ExperimentalStdlibApi::class)
  private fun handleNativeTag(tag: Tag) {
    Log.d(
      TAG,
      "Native NFC tag detected: idLength=${tag.id.size}, technologies=${tag.techList.joinToString()}"
    )
    lifecycleScope.launch(Dispatchers.IO) {
      val result = runCatching {
        when (getCardReaderType()) {
          MifareCardReaderType.CARD_UUID -> tag.id.toHexString().uppercase()
          MifareCardReaderType.CARD_UL -> readNativeUltralight(tag, 12)
          MifareCardReaderType.CARD_EV1 -> readNativeUltralight(tag, 24)
          MifareCardReaderType.CLASSIC_CARD_READ -> readNativeClassic(tag)
          MifareCardReaderType.CLASSIC_CARD_WRITE -> writeNativeClassic(tag)
          MifareCardReaderType.MOBILE_DRIVER_LICENSE ->
            readNativeMobileDriverLicense(tag, MDL_BLE_UUID)
          MifareCardReaderType.CANCEL -> "Card operation cancelled"
        }
      }.onFailure {
        Log.w(TAG, "Native Mifare operation failed", it)
      }.getOrElse {
        "Card operation failed. Please check the card and try again."
      }

      withContext(Dispatchers.Main) {
        findViewById<TextView>(R.id.text_mifare_status).text = result
      }
    }
  }

  @OptIn(ExperimentalStdlibApi::class)
  private fun readNativeUltralight(tag: Tag, numPages: Int): String {
    val ultralight = MifareUltralight.get(tag)
      ?: error("The detected card is not MIFARE Ultralight compatible")
    return try {
      ultralight.connect()
      buildList<Byte> {
        for (page in 0 until numPages step MIFARE_ULTRALIGHT_PAGES_PER_READ) {
          addAll(ultralight.readPages(page).asList())
        }
      }.take(numPages * MIFARE_ULTRALIGHT_PAGE_SIZE).toByteArray()
        .toHexString().uppercase()
    } finally {
      if (ultralight.isConnected) ultralight.close()
    }
  }

  @OptIn(ExperimentalStdlibApi::class)
  private fun readNativeClassic(tag: Tag): String {
    val classic = MifareClassic.get(tag)
      ?: error("The detected card is not MIFARE Classic compatible")
    return try {
      classic.connect()
      authenticateClassicBlock(classic, CLASSIC_START_BLOCK, useKeyB = false)
      buildList<Byte> {
        for (block in CLASSIC_START_BLOCK until CLASSIC_START_BLOCK + CLASSIC_BLOCK_COUNT) {
          addAll(classic.readBlock(block).asList())
        }
      }.toByteArray().toHexString().uppercase()
    } finally {
      if (classic.isConnected) classic.close()
    }
  }

  @OptIn(ExperimentalStdlibApi::class)
  private fun writeNativeClassic(tag: Tag): String {
    val classic = MifareClassic.get(tag)
      ?: error("The detected card is not MIFARE Classic compatible")
    return try {
      classic.connect()
      authenticateClassicBlock(classic, CLASSIC_START_BLOCK, useKeyB = false)
      classic.writeBlock(CLASSIC_START_BLOCK, CLASSIC_WRITE_DATA.hexToByteArray())
      "Card Write successful"
    } finally {
      if (classic.isConnected) classic.close()
    }
  }

  @OptIn(ExperimentalStdlibApi::class)
  private fun authenticateClassicBlock(classic: MifareClassic, block: Int, useKeyB: Boolean) {
    val sector = classic.blockToSector(block)
    val keyData = CLASSIC_KEY_DATA.hexToByteArray()
    val keyA = keyData.copyOfRange(0, MIFARE_CLASSIC_KEY_SIZE)
    val keyB = keyData.copyOfRange(MIFARE_CLASSIC_KEY_SIZE, keyData.size)
    val authenticated = if (useKeyB) {
      classic.authenticateSectorWithKeyB(sector, keyB)
    } else {
      classic.authenticateSectorWithKeyA(sector, keyA)
    }
    check(authenticated) {
      "MIFARE Classic authentication failed"
    }
  }

  @OptIn(ExperimentalStdlibApi::class)
  private suspend fun readNativeMobileDriverLicense(tag: Tag, bleUuid: String): String {
    val operationStartedAt = SystemClock.elapsedRealtime()
    val isoDep = IsoDep.get(tag) ?: error("The detected device is not an NFC Type 4 tag")
    return try {
      var phaseStartedAt = SystemClock.elapsedRealtime()
      isoDep.connect()
      isoDep.timeout = MDL_ISO_DEP_TIMEOUT_MS
      Log.d(TAG, "mDL IsoDep connected: maxTransceiveLength=${isoDep.maxTransceiveLength}")
      transceiveApdu(isoDep, SELECT_NDEF_APPLICATION_APDU)
      transceiveApdu(isoDep, SELECT_CAPABILITY_CONTAINER_APDU)

      val capabilityContainerLength = readBinary(isoDep, 0, 2).toUnsignedShort()
      Log.d(TAG, "mDL Type 4 capability container length=$capabilityContainerLength")
      val capabilityContainer = readBinary(isoDep, 0, capabilityContainerLength)
      logHex("mDL Type 4 capability container", capabilityContainer)
      val type4Capabilities = parseType4Capabilities(capabilityContainer, isoDep.maxTransceiveLength)
      Log.d(TAG, "mDL Type 4 NDEF file discovered")
      logHex("mDL Type 4 NDEF file ID", type4Capabilities.ndefFileId)
      transceiveApdu(
        isoDep,
        byteArrayOf(0x00, 0xA4.toByte(), 0x00, 0x0C, 0x02) + type4Capabilities.ndefFileId
      )
      logMdlTiming("native Type 4 setup", phaseStartedAt)

      phaseStartedAt = SystemClock.elapsedRealtime()
      val initialLength = readBinary(isoDep, 0, 2, type4Capabilities.maxReadLength).toUnsignedShort()
      check(initialLength > 0) { "No mobile driver license handover message found" }
      val initialMessage = NdefMessage(
        readBinary(isoDep, 2, initialLength, type4Capabilities.maxReadLength)
      )
      logNdefMessage("mDL initial", initialMessage)
      logHex("mDL initial NDEF", initialMessage.toByteArray())
      logMdlTiming("native initial NDEF read", phaseStartedAt)

      findDeviceEngagement(initialMessage)?.let {
        Log.d(TAG, "mDL static handover engagement found: payloadLength=${it.size}")
        return it.toHexString().uppercase()
      }

      val tnepService = findTnepService(initialMessage)
        ?: error("No mobile driver license TNEP service found")
      Log.d(
        TAG,
        "mDL TNEP service found: uriLength=${tnepService.uri.size}, " +
          "wait=${tnepService.waitTimeMs} ms, extensions=${tnepService.maxWaitExtensions}"
      )

      val serviceSelect = buildTnepServiceSelect(tnepService.uri)
      Log.d(TAG, "mDL writing TNEP service select: messageLength=${serviceSelect.toByteArray().size}")
      logHex("mDL TNEP service select NDEF", serviceSelect.toByteArray())
      phaseStartedAt = SystemClock.elapsedRealtime()
      writeType4NdefMessage(isoDep, serviceSelect, type4Capabilities.maxWriteLength)
      logMdlTiming("native TNEP service-select write", phaseStartedAt)
      val handoverRequest = buildHandoverRequest(bleUuid.hexToByteArray())
      Log.d(TAG, "mDL writing handover request: messageLength=${handoverRequest.toByteArray().size}")
      logHex("mDL handover request NDEF", handoverRequest.toByteArray())
      phaseStartedAt = SystemClock.elapsedRealtime()
      writeType4NdefMessage(isoDep, handoverRequest, type4Capabilities.maxWriteLength)
      logMdlTiming("native handover-request write", phaseStartedAt)
      Log.d(TAG, "mDL NDEF writes complete; polling on the active IsoDep session")

      phaseStartedAt = SystemClock.elapsedRealtime()
      val handoverSelect = pollType4HandoverResponse(
        isoDep,
        type4Capabilities.maxReadLength,
        tnepService
      )
      logMdlTiming("native handover-response polling", phaseStartedAt)
      phaseStartedAt = SystemClock.elapsedRealtime()
      logNdefMessage("mDL handover response", handoverSelect)
      val engagementData = findDeviceEngagement(handoverSelect)?.toHexString()?.uppercase()
        ?: error("No mobile driver license engagement data found in handover response")
      logMdlTiming("native engagement extraction", phaseStartedAt)
      engagementData
    } finally {
      runCatching { isoDep.close() }
      logMdlTiming("native total", operationStartedAt)
    }
  }

  private fun writeType4NdefMessage(
    isoDep: IsoDep,
    message: NdefMessage,
    maxWriteLength: Int
  ) {
    val data = message.toByteArray()
    require(data.size <= UShort.MAX_VALUE.toInt()) { "NDEF message is too large" }
    updateBinary(isoDep, 0, byteArrayOf(0x00, 0x00), maxWriteLength)
    var offset = 0
    while (offset < data.size) {
      val chunkLength = minOf(data.size - offset, maxWriteLength)
      updateBinary(
        isoDep,
        offset + 2,
        data.copyOfRange(offset, offset + chunkLength),
        maxWriteLength
      )
      offset += chunkLength
    }
    updateBinary(
      isoDep,
      0,
      byteArrayOf((data.size ushr 8).toByte(), data.size.toByte()),
      maxWriteLength
    )
  }

  private fun updateBinary(isoDep: IsoDep, offset: Int, data: ByteArray, maxWriteLength: Int) {
    require(offset in 0..UShort.MAX_VALUE.toInt()) { "UPDATE BINARY offset is out of range" }
    require(data.isNotEmpty() && data.size <= maxWriteLength) {
      "UPDATE BINARY data length is out of range"
    }
    val command = byteArrayOf(
      0x00,
      0xD6.toByte(),
      (offset ushr 8).toByte(),
      offset.toByte(),
      data.size.toByte()
    ) + data
    transceiveApdu(isoDep, command)
  }

  private suspend fun pollType4HandoverResponse(
    isoDep: IsoDep,
    maxReadLength: Int,
    tnepService: TnepService
  ): NdefMessage {
    val attemptCount = tnepService.maxWaitExtensions + 1
    repeat(attemptCount) { attempt ->
      delay(tnepService.waitTimeMs)
      val ndefLength = readBinary(isoDep, 0, 2, maxReadLength).toUnsignedShort()
      Log.d(TAG, "mDL handover response poll ${attempt + 1}/$attemptCount: ndefLength=$ndefLength")
      if (ndefLength > 0) {
        val responseData = readBinary(isoDep, 2, ndefLength, maxReadLength)
        logHex("mDL handover poll ${attempt + 1} NDEF", responseData)
        val response = NdefMessage(responseData)
        logNdefMessage("mDL handover poll ${attempt + 1}", response)
        if (findDeviceEngagement(response) != null) {
          Log.d(TAG, "mDL handover response ready: messageLength=$ndefLength")
          return response
        }
        Log.d(TAG, "mDL handover response not ready; continuing to poll")
      }
    }
    error("No mobile driver license engagement response received")
  }

  private fun parseType4Capabilities(
    capabilityContainer: ByteArray,
    maxTransceiveLength: Int
  ): Type4Capabilities {
    require(capabilityContainer.size >= TYPE_4_CC_TLV_OFFSET) {
      "NFC Type 4 capability container is too short"
    }
    val advertisedReadLength = capabilityContainer.copyOfRange(3, 5).toUnsignedShort()
    val advertisedWriteLength = capabilityContainer.copyOfRange(5, 7).toUnsignedShort()
    val maxReadLength = minOf(advertisedReadLength, TYPE_4_SHORT_APDU_MAX_DATA_LENGTH)
    val maxWriteLength = minOf(
      advertisedWriteLength,
      TYPE_4_SHORT_APDU_MAX_DATA_LENGTH,
      maxTransceiveLength - TYPE_4_UPDATE_BINARY_HEADER_LENGTH
    )
    require(maxReadLength > 0 && maxWriteLength > 0) {
      "NFC Type 4 capability container advertises invalid transfer limits"
    }

    var offset = TYPE_4_CC_TLV_OFFSET
    while (offset + 1 < capabilityContainer.size) {
      val type = capabilityContainer[offset].toInt() and 0xFF
      val length = capabilityContainer[offset + 1].toInt() and 0xFF
      val valueOffset = offset + 2
      if ((type == TYPE_4_NDEF_FILE_CONTROL_TLV || type == TYPE_4_EXTENDED_NDEF_FILE_CONTROL_TLV) &&
        length >= 2 && valueOffset + length <= capabilityContainer.size
      ) {
        return Type4Capabilities(
          capabilityContainer.copyOfRange(valueOffset, valueOffset + 2),
          maxReadLength,
          maxWriteLength
        )
      }
      offset = valueOffset + length
    }
    error("No NDEF file was advertised by the NFC Type 4 tag")
  }

  private fun readBinary(
    isoDep: IsoDep,
    offset: Int,
    length: Int,
    maxReadLength: Int = TYPE_4_SHORT_APDU_MAX_DATA_LENGTH
  ): ByteArray {
    require(offset in 0..UShort.MAX_VALUE.toInt()) { "READ BINARY offset is out of range" }
    require(length in 1..UShort.MAX_VALUE.toInt()) { "READ BINARY length is out of range" }
    return buildList<Byte> {
      var readOffset = offset
      var remaining = length
      while (remaining > 0) {
        val chunkLength = minOf(remaining, maxReadLength)
        val command = byteArrayOf(
          0x00,
          0xB0.toByte(),
          (readOffset ushr 8).toByte(),
          readOffset.toByte(),
          chunkLength.toByte()
        )
        addAll(transceiveApdu(isoDep, command).asList())
        readOffset += chunkLength
        remaining -= chunkLength
      }
    }.toByteArray()
  }

  @OptIn(ExperimentalStdlibApi::class)
  private fun transceiveApdu(isoDep: IsoDep, command: ByteArray): ByteArray {
    Log.d(
      TAG,
      "mDL APDU send: cla=${command[0].toHexString()}, ins=${command[1].toHexString()}, length=${command.size}"
    )
    if (LOG_APDU_HEX_DATA) {
      logHex("mDL APDU command", command)
    }
    val response = isoDep.transceive(command)
    if (LOG_APDU_HEX_DATA) {
      logHex("mDL APDU response", response)
    }
    check(response.size >= 2) { "Invalid NFC Type 4 response" }
    val status = response.copyOfRange(response.size - 2, response.size)
    Log.d(TAG, "mDL APDU response: dataLength=${response.size - 2}, status=${status.toHexString().uppercase()}")
    check(status.contentEquals(APDU_SUCCESS_STATUS)) {
      "NFC Type 4 command failed with status ${status.toHexString().uppercase()}"
    }
    return response.copyOf(response.size - 2)
  }

  private fun ByteArray.toUnsignedShort(): Int {
    check(size == 2) { "Expected a two-byte unsigned value" }
    return ((this[0].toInt() and 0xFF) shl 8) or (this[1].toInt() and 0xFF)
  }

  private fun findTnepService(message: NdefMessage): TnepService? {
    val payload = message.records.firstOrNull {
      it.tnf == NdefRecord.TNF_WELL_KNOWN && it.type.contentEquals(TNEP_SERVICE_PARAMETER_TYPE)
    }?.payload ?: return null
    if (payload.size < TNEP_SERVICE_PARAMETER_FIXED_LENGTH) return null

    val uriLength = payload[1].toInt() and 0xFF
    val parametersOffset = uriLength + 2
    if (uriLength == 0 || payload.size < parametersOffset + TNEP_SERVICE_TRAILING_PARAMETER_LENGTH) {
      return null
    }
    val waitTimeExponent = payload[parametersOffset + 1].toInt() and 0xFF
    if (waitTimeExponent > TNEP_MAX_WAIT_TIME_EXPONENT) return null
    return TnepService(
      payload.copyOfRange(2, parametersOffset),
      ((1L shl waitTimeExponent) + MICROSECONDS_PER_MILLISECOND - 1) /
        MICROSECONDS_PER_MILLISECOND,
      payload[parametersOffset + 2].toInt() and 0xFF
    )
  }

  private fun buildTnepServiceSelect(serviceUri: ByteArray): NdefMessage {
    require(serviceUri.size <= UByte.MAX_VALUE.toInt()) { "TNEP service URI is too long" }
    val payload = byteArrayOf(serviceUri.size.toByte()) + serviceUri
    return NdefMessage(
      arrayOf(NdefRecord(NdefRecord.TNF_WELL_KNOWN, TNEP_SERVICE_SELECT_TYPE, byteArrayOf(), payload))
    )
  }

  private fun buildHandoverRequest(bleUuid: ByteArray): NdefMessage {
    require(bleUuid.size == MDL_BLE_UUID_SIZE) { "BLE UUID must be 16 bytes" }

    val collisionResolution = NdefRecord(
      NdefRecord.TNF_WELL_KNOWN,
      COLLISION_RESOLUTION_TYPE,
      byteArrayOf(),
      ByteArray(COLLISION_RESOLUTION_RANDOM_LENGTH).also(SECURE_RANDOM::nextBytes)
    )
    val alternativeCarrier = NdefRecord(
      NdefRecord.TNF_WELL_KNOWN,
      ALTERNATIVE_CARRIER_TYPE,
      byteArrayOf(),
      byteArrayOf(0x01, 0x01, BLE_CARRIER_REFERENCE, 0x00)
    )
    val handoverRequest = NdefRecord(
      NdefRecord.TNF_WELL_KNOWN,
      NdefRecord.RTD_HANDOVER_REQUEST,
      byteArrayOf(),
      byteArrayOf(HANDOVER_VERSION) +
        NdefMessage(arrayOf(collisionResolution, alternativeCarrier)).toByteArray()
    )
    val bleCarrier = NdefRecord(
      NdefRecord.TNF_MIME_MEDIA,
      BLE_OOB_MIME_TYPE,
      byteArrayOf(BLE_CARRIER_REFERENCE),
      byteArrayOf(0x02, BLE_ROLE_AD_TYPE, 0x00, 0x11, BLE_UUID_AD_TYPE) + bleUuid
    )
    return NdefMessage(arrayOf(handoverRequest, bleCarrier))
  }

  private data class Type4Capabilities(
    val ndefFileId: ByteArray,
    val maxReadLength: Int,
    val maxWriteLength: Int
  )

  private data class TnepService(
    val uri: ByteArray,
    val waitTimeMs: Long,
    val maxWaitExtensions: Int
  )

  private fun findDeviceEngagement(message: NdefMessage?): ByteArray? {
    return message?.records?.firstOrNull {
      it.tnf == NdefRecord.TNF_EXTERNAL_TYPE &&
        it.type.contentEquals(MDL_DEVICE_ENGAGEMENT_TYPE) &&
        it.id.contentEquals(MDL_DEVICE_ENGAGEMENT_ID)
    }?.payload
  }

  private fun logNdefMessage(stage: String, message: NdefMessage) {
    val records = message.records.joinToString { record ->
      val type = record.type.toString(Charsets.US_ASCII).take(MAX_LOGGED_NDEF_TYPE_LENGTH)
      "tnf=${record.tnf}, type=$type, payloadLength=${record.payload.size}"
    }
    Log.d(TAG, "$stage: recordCount=${message.records.size}, records=[$records]")
  }

  private fun logMdlTiming(stage: String, startedAt: Long) {
    Log.i(TAG, "mDL timing: $stage=${SystemClock.elapsedRealtime() - startedAt} ms")
  }

  @OptIn(ExperimentalStdlibApi::class)
  private fun logHex(stage: String, data: ByteArray) {
    if (!BuildConfig.DEBUG) return
    val hex = data.toHexString().uppercase()
    if (hex.isEmpty()) {
      Log.d(TAG, "$stage hex: <empty>")
      return
    }
    hex.chunked(LOGCAT_HEX_CHUNK_SIZE).forEachIndexed { index, chunk ->
      Log.d(TAG, "$stage hex[$index]: $chunk")
    }
  }

  override fun onStop() {
    nfcStartupJob?.cancel()
    nfcStartupJob = null
    if (useNativeNfc) {
      nfcAdapter?.disableReaderMode(this)
    } else {
      MifareCardReaderClient.getInstance(application).disconnect()
    }
    isMifareServiceConnected = false
    super.onStop()
  }

  @OptIn(ExperimentalStdlibApi::class)
  private companion object {
    val TAG: String = MifareCardReaderStatusActivity::class.java.simpleName
    const val MINI_4_PRODUCT_NAME = "Mini 4"
    const val MINI_4_MODEL = "Clover_C306"
    const val MIFARE_ULTRALIGHT_PAGES_PER_READ = 4
    const val MIFARE_ULTRALIGHT_PAGE_SIZE = 4
    const val CLASSIC_START_BLOCK = 4
    const val CLASSIC_BLOCK_COUNT = 3
    const val MIFARE_CLASSIC_KEY_SIZE = 6
    const val CLASSIC_KEY_DATA = "FFFFFFFFFFFFFFFFFFFFFFFF"
    const val CLASSIC_WRITE_DATA = "11223344556677889900112233445566"
    const val MDL_BLE_UUID = "11223344556677889900112233445566"
    const val MDL_BLE_UUID_SIZE = 16
    const val MDL_ISO_DEP_TIMEOUT_MS = 1000
    const val TYPE_4_CC_TLV_OFFSET = 7
    const val TYPE_4_NDEF_FILE_CONTROL_TLV = 0x04
    const val TYPE_4_EXTENDED_NDEF_FILE_CONTROL_TLV = 0x06
    const val TYPE_4_SHORT_APDU_MAX_DATA_LENGTH = 0xFF
    const val TYPE_4_UPDATE_BINARY_HEADER_LENGTH = 5
    const val TNEP_SERVICE_PARAMETER_FIXED_LENGTH = 7
    const val TNEP_SERVICE_TRAILING_PARAMETER_LENGTH = 5
    const val TNEP_MAX_WAIT_TIME_EXPONENT = 20
    const val MICROSECONDS_PER_MILLISECOND = 1000L
    const val COLLISION_RESOLUTION_RANDOM_LENGTH = 2
    const val MAX_LOGGED_NDEF_TYPE_LENGTH = 64
    const val LOGCAT_HEX_CHUNK_SIZE = 3000
    const val LOG_APDU_HEX_DATA = false
    const val HANDOVER_VERSION: Byte = 0x15
    const val BLE_CARRIER_REFERENCE: Byte = 0x30
    const val BLE_ROLE_AD_TYPE: Byte = 0x1C
    const val BLE_UUID_AD_TYPE: Byte = 0x07
    val TNEP_SERVICE_PARAMETER_TYPE = "Tp".toByteArray(Charsets.US_ASCII)
    val TNEP_SERVICE_SELECT_TYPE = "Ts".toByteArray(Charsets.US_ASCII)
    val COLLISION_RESOLUTION_TYPE = "cr".toByteArray(Charsets.US_ASCII)
    val ALTERNATIVE_CARRIER_TYPE = "ac".toByteArray(Charsets.US_ASCII)
    val BLE_OOB_MIME_TYPE = "application/vnd.bluetooth.le.oob".toByteArray(Charsets.US_ASCII)
    val MDL_DEVICE_ENGAGEMENT_TYPE =
      "iso.org:18013:deviceengagement".toByteArray(Charsets.US_ASCII)
    val MDL_DEVICE_ENGAGEMENT_ID = "mdoc".toByteArray(Charsets.US_ASCII)
    val SELECT_NDEF_APPLICATION_APDU =
      "00A4040007D276000085010100".hexToByteArray()
    val SELECT_CAPABILITY_CONTAINER_APDU = "00A4000C02E103".hexToByteArray()
    val APDU_SUCCESS_STATUS = byteArrayOf(0x90.toByte(), 0x00)
    val SECURE_RANDOM = SecureRandom()
  }
}