package com.clover.sdk.v3.mifare.connector

import android.annotation.SuppressLint
import android.content.Context
import com.clover.sdk.v3.mifare.listener.IMifareCardReaderClientListener
import com.clover.sdk.v3.mifare.model.MifareCard
import com.clover.sdk.v3.mifare.model.MifareCardDataRequest
import com.clover.sdk.v3.mifare.model.MifareCardLightDataRequest
import com.clover.sdk.v3.mifare.model.MifareCardUuid
import com.clover.sdk.v3.mifare.model.MifareCardWriteRequest
import com.clover.sdk.v3.mifare.model.MifareMobileDriverLicense
import com.clover.sdk.v3.mifare.model.MifareMobileDriverLicenseRequest
import com.clover.sdk.v3.mifare.service.IMifareCardReaderService
import com.clover.sdk.v3.payment.raw.listener.IServiceListener

internal class MifareCardReaderClientImpl(private val context: Context):
    MifareCardReaderClient() {
  private val mifareServiceConnector = MifareServiceConnector(context)
  private var iMifareCardReaderClientListener: IMifareCardReaderClientListener? = null
  private var iMifareCardReaderService: IMifareCardReaderService? = null
  private var isMifareServiceConnected = false

  override fun connect(cardReaderClientListener: IMifareCardReaderClientListener) {
    iMifareCardReaderClientListener = cardReaderClientListener
    mifareServiceConnector.connect(mifareCardReaderListener)
  }

  override fun disconnect() {
    mifareServiceConnector.disconnect()
  }

  override fun cardUuid(): MifareCardUuid? {
    if (isMifareServiceAvailable()) {
      return iMifareCardReaderService?.cardUuid()
    }
    throw MifareCardReaderException("Couldn't read MifareUuid.")
  }

  override fun cardUltralightRead(mifareCardLightDataRequest: MifareCardLightDataRequest): MifareCard? {
    if (isMifareServiceAvailable()) {
      return iMifareCardReaderService?.cardUltralightRead(mifareCardLightDataRequest)
    }
    throw MifareCardReaderException("Couldn't read MifareUltralight card.")
  }

  override fun cardUltralightEv1Read(mifareCardDataRequest: MifareCardLightDataRequest): MifareCard? {
    if (isMifareServiceAvailable()) {
      return iMifareCardReaderService?.cardUltralightEv1Read(mifareCardDataRequest)
    }
    throw MifareCardReaderException("Couldn't read MifareUltralightEv1 card.")
  }

  override fun cardClassicRead(mifareCardDataRequest: MifareCardDataRequest): MifareCard? {
    if (isMifareServiceAvailable()) {
      return iMifareCardReaderService?.cardClassicRead(mifareCardDataRequest)
    }
    throw MifareCardReaderException("Couldn't read MifareClassic card.")
  }

  override fun writeCard(mifareCardWriteRequest: MifareCardWriteRequest): Boolean? {
    if (isMifareServiceAvailable()) {
      return iMifareCardReaderService?.writeCard(mifareCardWriteRequest)
    }
    throw MifareCardReaderException("Couldn't write data in MifareClassic card.")
  }

  override fun mobileDriverLicenseRead(mobileDriverLicense: MifareMobileDriverLicenseRequest): MifareMobileDriverLicense? {
    if (isMifareServiceAvailable()) {
      return iMifareCardReaderService?.mobileDriverLicenseRead(mobileDriverLicense)
    }
    throw MifareCardReaderException("Couldn't read mobile driver license")
  }

  override fun cancel() {
    if (isMifareServiceAvailable()) {
      iMifareCardReaderService?.cancel()
    } else {
      throw MifareCardReaderException("Cancel failed")
    }
  }

  private fun isMifareServiceAvailable(): Boolean {
    return isMifareServiceConnected && iMifareCardReaderService != null && iMifareCardReaderClientListener != null
  }


  private val mifareCardReaderListener = object: IServiceListener<IMifareCardReaderService> {
    override fun onConnected(mifareCardReaderService: IMifareCardReaderService) {
      isMifareServiceConnected = true
      iMifareCardReaderService = mifareCardReaderService
      iMifareCardReaderClientListener?.onConnected()
    }

    override fun onDisconnected() {
      iMifareCardReaderClientListener?.onDisconnect()
      isMifareServiceConnected = false
      iMifareCardReaderService = null
      iMifareCardReaderClientListener = null
    }

  }
}