package com.clover.sdk.v3.mifare.connector

import com.clover.sdk.v3.mifare.listener.IMifareCardReaderClientListener
import com.clover.sdk.v3.mifare.model.MifareCard
import com.clover.sdk.v3.mifare.model.MifareCardDataRequest
import com.clover.sdk.v3.mifare.model.MifareCardLightDataRequest
import com.clover.sdk.v3.mifare.model.MifareCardUuid
import com.clover.sdk.v3.mifare.model.MifareCardWriteRequest
import com.clover.sdk.v3.mifare.model.MifareMobileDriverLicense
import com.clover.sdk.v3.mifare.model.MifareMobileDriverLicenseRequest

/**
 * Mifare Client to read mifare and mobile card read operations.
 * This will help in reading mifare card operations.
 * Please make sure to finish any card reading/writing operation with in 30 seconds.
 * If you couldn't finish any operation with in 30 seconds, please invoke connect again.
 */
interface IMifareCardReaderClient {

  /**
   * Connect Mifare client to start card reading operation.
   * Please make sure to finish any card reading/writing operation with in 30 seconds
   */
  fun connect(cardReaderClientListener: IMifareCardReaderClientListener)

  /**
   * Disconnect the mifare client after finishing operation.
   */
  fun disconnect()

  /**
   * Returns Mifare Card UUID that can be used to generate Mifare card key.
   * Please make sure to do this operation before classic card read/write.
   */
  fun cardUuid(): MifareCardUuid?

  /**
   * Returns Mifare UL Card details. You don't need any key to authenticate this operation.
   */
  fun cardUltralightRead(mifareCardLightDataRequest: MifareCardLightDataRequest): MifareCard?

  /**
   * Returns Mifare EV1 Card details. You don't need any key to authenticate this operation.
   */
  fun cardUltralightEv1Read(mifareCardDataRequest: MifareCardLightDataRequest): MifareCard?

  /**
   * Returns Mifare Classic Card details. You need key to do this operation.
   * Please use #cardUuid function to get Card UUID and use this UUID to generate Mifare classic card key.
   * Please make sure card is tapped until cardUUID & cardClassicRead operations are not completed.
   */
  fun cardClassicRead(mifareCardDataRequest: MifareCardDataRequest): MifareCard?

  /**
   * Writes hex formatted Mifare classic card data back to the card. You need key to do this operation.
   * Please use #cardUuid function to get Card UUID and use this UUID to generate Mifare classic card key.
   * Please make sure card is tapped until cardUUID & writeCard operations are not completed.
   */
  fun writeCard(mifareCardWriteRequest: MifareCardWriteRequest): Boolean?

  /**
   * Read driver license using this api. You will need BLE UUID to read mobile driver license.
   */
  fun mobileDriverLicenseRead(mobileDriverLicense: MifareMobileDriverLicenseRequest): MifareMobileDriverLicense?

  /**
   * Cancel any running existing operation like writeCard or cardClassicRead. It will throw error in current running operation.
   */
  fun cancel()
}