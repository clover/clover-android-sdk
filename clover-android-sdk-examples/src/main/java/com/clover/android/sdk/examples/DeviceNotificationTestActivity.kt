package com.clover.android.sdk.examples

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.CheckBox
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.clover.sdk.util.CloverAccount
import com.clover.sdk.v1.app.AppNotification
import com.clover.sdk.v1.app.AppNotificationReceiver
import com.clover.sdk.v3.merchant.MerchantDevicesV2Connector
import com.clover.android.sdk.examples.databinding.ActivityDeviceNotificationsTestBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.graphics.toColorInt
import com.clover.android.sdk.examples.DeviceNotificationViewModel.LogEntry.Level
import com.clover.android.sdk.examples.DeviceNotificationViewModel.LogEntry.Level.*

class DeviceNotificationTestActivity : AppCompatActivity() {
  companion object {
    private val TAG = DeviceNotificationTestActivity::class.simpleName

    private const val EVENT_TEST_REBOOT = "test_reboot"
    private const val EVENT_TEST_FORCE_REBOOT = "test_force_reboot"
  }

  private val viewModel: DeviceNotificationViewModel by viewModels()

  private lateinit var binding: ActivityDeviceNotificationsTestBinding

  private val devicesConnector = MerchantDevicesV2Connector(this)

  private val receiver = object : AppNotificationReceiver() {
    override fun onReceive(context: Context, notification: AppNotification) {
      log(INFO, "Received Notification: event=${notification.appEvent}")

      if (notification.appEvent == EVENT_TEST_REBOOT ||
          notification.appEvent == EVENT_TEST_FORCE_REBOOT
      ) {
        val force = notification.appEvent == EVENT_TEST_FORCE_REBOOT
        log(INFO, "Received event: '${notification.appEvent}' rebooting (force=$force)...")
        lifecycleScope.launch {
          withContext(Dispatchers.IO) {
            devicesConnector.reboot("Test reboot triggered by device notification", force)
          }
        }
      }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityDeviceNotificationsTestBinding.inflate(layoutInflater)
    setContentView(binding.root)

    // Verify we have an account
    if (CloverAccount.getAccount(this) == null) {
      log(ERROR, "Clover account not found")
      return
    }

    receiver.register(this)

    viewModel.logEntry.observe(this) { entry ->
      log(entry.level, entry.message)
    }

    viewModel.devices.observe(this) { devices ->
      binding.devicesContainer.removeAllViews()
      for (device in devices) {
        val checkBox = CheckBox(this).apply {
          text = device.serial
          if (device.name.isNotEmpty()) {
            append(" (${device.name})")
          }

          isChecked = device.isChecked
          setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleDevice(device.id, isChecked)
          }
        }

        binding.devicesContainer.addView(checkBox)
      }
    }

    class RebootClickListener(private val force: Boolean) : View.OnClickListener {
      override fun onClick(p0: View?) {
        val authResult = viewModel.authResult.value
        if (authResult == null) {
          log(ERROR, "Not authenticated")
          return
        }

        val targetDeviceIds = viewModel.devices.value
          ?.filter { it.isChecked }
          ?.map { it.id }
            ?: emptyList()

        viewModel.sendReboot(authResult, targetDeviceIds, force)
      }
    }

    binding.sendRebootButton.setOnClickListener(RebootClickListener(false))
    binding.sendForceRebootButton.setOnClickListener(RebootClickListener(true))

    viewModel.authenticate()
    viewModel.loadDevices()
  }

  override fun onDestroy() {
    super.onDestroy()
    receiver.unregister()
  }

  private fun log(level: Level, text: String) {
    when (level) {
      ERROR -> Log.e(TAG, text)
      INFO -> Log.i(TAG, text)
      SUCCESS -> Log.i(TAG, text)
    }

    lifecycleScope.launch(Dispatchers.Main) {
      val newLine = if (binding.logText.text.isEmpty()) "" else "\n"
      val fullText = "$newLine$text"
      binding.logText.append(SpannableString(fullText).setSpan(level, fullText))

      binding.logScrollView.post {
        binding.logScrollView.fullScroll(View.FOCUS_DOWN)
      }
    }
  }
}

fun SpannableString.setSpan(level: Level, text: String): SpannableString {
  setSpan(
      ForegroundColorSpan(level.color()),
      0,
      text.length,
      Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
  )
  return this
}

private fun Level.color() = when (this) {
  ERROR -> Color.RED
  SUCCESS -> "#008800".toColorInt() // Dark green
  INFO -> Color.BLACK
}
