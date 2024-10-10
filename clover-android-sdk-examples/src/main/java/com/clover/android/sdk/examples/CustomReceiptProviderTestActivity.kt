package com.clover.android.sdk.examples

import android.app.Activity
import android.content.ComponentName
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.clover.android.sdk.examples.CustomReceiptProviderTest.Companion.N_CHUNKS
import com.clover.android.sdk.examples.CustomReceiptProviderTest.Companion.SELECTED_FILE_RES_ID

class CustomReceiptProviderTestActivity : Activity() {

  lateinit var enableBtn: Button
  lateinit var disableBtn: Button
  lateinit var save: Button
  lateinit var numberOfBitmapChunks: EditText
  lateinit var testReceiptSizeSelector: Spinner
  lateinit var sharedPrefs: SharedPreferences
  var selectedFileResId: Int = R.drawable.test_receipt_extra_large
  private val receiptRes = ArrayList<Int>(
    listOf(
      R.drawable.test_receipt_extra_large,
      R.drawable.test_receipt_small,
      R.drawable.test_receipt_medium,
      R.drawable.test_receipt_large
    )
  )

  companion object {
    const val TAG = "CustomReceiptProviderTestActivity"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_custom_receipt_provider_test)

    sharedPrefs = getSharedPreferences(CustomReceiptProviderTest.SHARED_PREFS, MODE_PRIVATE)

    enableBtn = findViewById(R.id.enable_provider)
    disableBtn = findViewById(R.id.disable_provider)
    numberOfBitmapChunks = findViewById(R.id.number_of_bitmap_chunks)
    numberOfBitmapChunks.setText(sharedPrefs.getInt(N_CHUNKS, 3).toString())
    testReceiptSizeSelector = findViewById(R.id.test_receipt_size_selector)
    save = findViewById(R.id.save_number_of_bitmap_chunks)

    ArrayAdapter.createFromResource(
      this,
      R.array.test_receipt_sizes_array,
      android.R.layout.simple_spinner_item
    ).also { adapter ->
      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
      testReceiptSizeSelector.adapter = adapter
    }

    testReceiptSizeSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
      override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        selectedFileResId = receiptRes[pos]
        Log.d(TAG, "Selected file: ${resources.getResourceEntryName(selectedFileResId)}")
      }

      override fun onNothingSelected(parent: AdapterView<*>) {
        return
      }
    }

    save.setOnClickListener {
      val numberOfBitmapChunks = numberOfBitmapChunks.text.toString()
      val nChunks = if (numberOfBitmapChunks.isEmpty()) 3 else numberOfBitmapChunks.toInt()
      if (numberOfBitmapChunks.isEmpty() || nChunks < 0 || nChunks > 20) {
        Toast.makeText(this, "Entered number of chunks are not between 0 and 20. " +
          "Testing with default 3 chunks", Toast.LENGTH_LONG).show()
        sharedPrefs.edit().putInt(N_CHUNKS, 3).apply()
      } else {
        sharedPrefs.edit().putInt(N_CHUNKS, nChunks).apply()
      }
      sharedPrefs.edit().putInt(SELECTED_FILE_RES_ID, selectedFileResId).apply()
    }

    enableBtn.setOnClickListener {
      val conProvCN = ComponentName(this,
        "com.clover.android.sdk.examples.CustomReceiptProviderTest")
      val pm: PackageManager = this.packageManager
      pm.setComponentEnabledSetting(conProvCN,
        PackageManager.COMPONENT_ENABLED_STATE_ENABLED, 0)
    }

    disableBtn.setOnClickListener {
      val conProvCN = ComponentName(this,
        "com.clover.android.sdk.examples.CustomReceiptProviderTest")
      val pm: PackageManager = this.packageManager
      pm.setComponentEnabledSetting(conProvCN,
        PackageManager.COMPONENT_ENABLED_STATE_DISABLED, 0)
    }
  }
}