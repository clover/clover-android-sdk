package com.clover.android.sdk.examples

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.clover.android.sdk.examples.mifare.MifareCardReaderStatusActivity
import com.clover.android.sdk.examples.mifare.MifareCardReaderType

class MifareCardReaderTestActivity: AppCompatActivity() {

  var mifareCardReaderType = MifareCardReaderType.CLASSIC_CARD_READ

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_mifare_card_reader_test)



    findViewById<Button>(R.id.button_carduuid).setOnClickListener {
      mifareCardReaderType = MifareCardReaderType.CARD_UUID
      startMifareCardStatusActivity()
    }


    findViewById<Button>(R.id.button_card_ul_read).setOnClickListener {
      mifareCardReaderType = MifareCardReaderType.CARD_UL
      startMifareCardStatusActivity()
    }


    findViewById<Button>(R.id.button_card_ev1_read).setOnClickListener {
      mifareCardReaderType = MifareCardReaderType.CARD_EV1
      startMifareCardStatusActivity()
    }


    findViewById<Button>(R.id.button_card_classic_read).setOnClickListener {
      mifareCardReaderType = MifareCardReaderType.CLASSIC_CARD_READ
      startMifareCardStatusActivity()
    }


    findViewById<Button>(R.id.button_card_classic_write).setOnClickListener {
      mifareCardReaderType = MifareCardReaderType.CLASSIC_CARD_WRITE
      startMifareCardStatusActivity()
    }


    findViewById<Button>(R.id.button_mobile_driver_license).setOnClickListener {
      mifareCardReaderType = MifareCardReaderType.MOBILE_DRIVER_LICENSE
      startMifareCardStatusActivity()
    }

  }

  private fun startMifareCardStatusActivity() {
    startActivity(Intent(this, MifareCardReaderStatusActivity::class.java).apply {
      putExtra("MIFARE_CARD_READER_TYPE", mifareCardReaderType.type)
    })
  }
}