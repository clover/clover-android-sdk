package com.clover.android.sdk.examples.mifare

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.clover.android.sdk.examples.MifareCardReaderTestActivity
import com.clover.android.sdk.examples.R
import com.clover.sdk.v3.mifare.connector.MifareCardReaderClient
import com.clover.sdk.v3.mifare.listener.IMifareCardReaderClientListener
import com.clover.sdk.v3.mifare.model.MifareCardDataRequest
import com.clover.sdk.v3.mifare.model.MifareCardKey
import com.clover.sdk.v3.mifare.model.MifareCardLightDataRequest
import com.clover.sdk.v3.mifare.model.MifareCardWriteRequest
import com.clover.sdk.v3.mifare.model.MifareMobileDriverLicenseRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MifareCardReaderStatusActivity: AppCompatActivity() {

  private var isMifareServiceConnected = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_mifare_card_reader_status)
    findViewById<Button>(R.id.button_cancel).setOnClickListener {
      if (isMifareServiceConnected) {
        MifareCardReaderClient.getInstance(application).cancel()
      }
      finish()
    }
  }

  override fun onStart() {
    super.onStart()
    lifecycleScope.launch(Dispatchers.IO) {
      MifareCardReaderClient.getInstance(application)
        .connect(object : IMifareCardReaderClientListener {
          override fun onConnected() {
            isMifareServiceConnected = true
            startMifare()
          }

          override fun onDisconnect() {
            isMifareServiceConnected = false
          }

        })
    }
  }

  private fun startMifare() {
    val cardReaderTypeExtra = intent.extras?.getInt("MIFARE_CARD_READER_TYPE")
    val cardReaderType = when(cardReaderTypeExtra) {
      0 -> MifareCardReaderType.CARD_UUID
      1 -> MifareCardReaderType.CARD_UL
      2 -> MifareCardReaderType.CARD_EV1
      3 -> MifareCardReaderType.CLASSIC_CARD_READ
      4 -> MifareCardReaderType.CLASSIC_CARD_WRITE
      5 -> MifareCardReaderType.MOBILE_DRIVER_LICENSE
      else -> MifareCardReaderType.CLASSIC_CARD_READ
    }

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
          MifareCardReaderClient.getInstance(application)
            .mobileDriverLicenseRead(MifareMobileDriverLicenseRequest("11223344556677889900112233445566"))?.mdlDeviceEngagementData?.let { engagementData ->
              withContext(Dispatchers.Main) {
                mifareStatusText.text = engagementData
              }
            } ?: run {
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

  override fun onStop() {
    MifareCardReaderClient.getInstance(application).disconnect()
    super.onStop()
  }
}