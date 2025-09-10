package com.clover.android.sdk.examples

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.loader.content.CursorLoader
import com.clover.android.sdk.examples.databinding.ActivityMerchantDevicesV2Binding
import com.clover.sdk.v3.merchant.MerchantDevicesV2Connector
import com.clover.sdk.v3.merchant.MerchantDevicesV2Contract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Test activity that exercises the contract [MerchantDevicesV2Contract] and the connector
 * [MerchantDevicesV2Connector].
 */
class MerchantDevicesV2TestActivity : AppCompatActivity() {
  private lateinit var adapter: MerchantDevicesV2Adapter
  private lateinit var binding: ActivityMerchantDevicesV2Binding
  private lateinit var connector: MerchantDevicesV2Connector

  protected override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    connector = MerchantDevicesV2Connector(applicationContext)

    binding = ActivityMerchantDevicesV2Binding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.listView.setEmptyView(findViewById<View?>(R.id.list_view_empty))
    adapter = MerchantDevicesV2Adapter(this)
    binding.listView.setAdapter(adapter)

    binding.reboot.setOnClickListener {
      val now = binding.rebootNow.isChecked
      lifecycleScope.launch {
        withContext(Dispatchers.IO) { connector.reboot("User requested reboot", now) }
      }
    }
  }

  @SuppressLint("StaticFieldLeak")
  override fun onStart() {
    super.onStart()

    binding.progressBar.visibility = View.VISIBLE
    binding.contentLayout.visibility = View.GONE

    lifecycleScope.launch {
      val device = withContext(Dispatchers.IO) { connector.device }
      binding.thisSerial.text = device.serial
      binding.thisModel.text = device.model
      binding.thisName.text = device.productName

      val simIccId = withContext(Dispatchers.IO) { connector.simIccId }
      binding.thisSimmIccId.text = simIccId ?: "N/A"

      CursorLoader(
          this@MerchantDevicesV2TestActivity,
          MerchantDevicesV2Contract.Device.CONTENT_URI,
          null,
          null,
          null,
          null
      ).apply {
        adapter.swapCursor(withContext(Dispatchers.IO) { loadInBackground() })
        binding.progressBar.visibility = View.GONE
        binding.contentLayout.visibility = View.VISIBLE
      }
    }
  }
}
