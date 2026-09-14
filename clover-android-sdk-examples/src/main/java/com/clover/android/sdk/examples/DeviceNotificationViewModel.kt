package com.clover.android.sdk.examples

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.clover.android.sdk.examples.DeviceNotificationViewModel.LogEntry.Level
import com.clover.android.sdk.examples.DeviceNotificationViewModel.LogEntry.Level.*
import com.clover.sdk.util.CloverAuth
import com.clover.sdk.v3.merchant.MerchantDevicesV2Contract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class DeviceItem(
  val id: String,
  val name: String,
  val serial: String,
  var isChecked: Boolean = false
)

class DeviceNotificationViewModel(application: Application) : AndroidViewModel(application) {

  data class LogEntry(val level: Level, val message: String) {
    enum class Level { INFO, ERROR, SUCCESS }
  }

  companion object {
    private val TAG = DeviceNotificationViewModel::class.java.simpleName
    private val okHttpClient = OkHttpClient()
  }

  private val _authResult = MutableLiveData<CloverAuth.AuthResult>()
  val authResult: LiveData<CloverAuth.AuthResult> = _authResult

  private val _devices = MutableLiveData<List<DeviceItem>>()
  val devices: LiveData<List<DeviceItem>> = _devices

  private val _logEntry = MutableLiveData<LogEntry>()
  val logEntry: LiveData<LogEntry> = _logEntry

  fun authenticate() {
    viewModelScope.launch {
      try {
        val result = withContext(Dispatchers.IO) {
          CloverAuth.authenticate(
              getApplication<Application>().applicationContext,
              false,
              10, TimeUnit.SECONDS
          )
        }
        _authResult.value = result
        log(SUCCESS, "Authentication successful")
      } catch (e: Exception) {
        log(ERROR, "Authentication failed: $e")
      }
    }
  }

  fun loadDevices() {
    viewModelScope.launch {
      try {
        val loadedDevices = mutableListOf<DeviceItem>()
        withContext(Dispatchers.IO) {
          val context = getApplication<Application>().applicationContext
          val cursor = context.contentResolver.query(
              MerchantDevicesV2Contract.Device.CONTENT_URI,
              null, null, null, null
          )

          cursor?.use {
            while (it.moveToNext()) {
              val device = MerchantDevicesV2Contract.Device.fromCursor(it)
              // Filter out all the junk entries that are bound to exist
              // in a development environment.
              if (device.id != null &&
                  device.model != null &&
                  device.serial != null &&
                  device.model.startsWith("Clover_")
              ) {
                loadedDevices.add(
                    DeviceItem(
                        device.id,
                        device.name ?: "",
                        device.serial,
                    )
                )
              }
            }
          }
        }
        loadedDevices.sortBy { it.serial }
        _devices.value = loadedDevices
        log(INFO, "Loaded ${loadedDevices.size} devices")
      } catch (e: Exception) {
        log(ERROR, "Failed to load devices: $e")
      }
    }
  }

  fun toggleDevice(deviceId: String, isChecked: Boolean) {
    val currentDevices = _devices.value?.toMutableList() ?: return
    val index = currentDevices.indexOfFirst { it.id == deviceId }
    if (index != -1) {
      currentDevices[index] = currentDevices[index].copy(isChecked = isChecked)
      _devices.value = currentDevices
    }
  }

  fun sendReboot(authResult: CloverAuth.AuthResult, targetDeviceIds: List<String>, force: Boolean) {
    if (targetDeviceIds.isEmpty()) {
      log(INFO, "No devices selected.")
      return
    }

    val eventName = if (force) "test_force_reboot" else "test_reboot"
    log(INFO, "Sending ${if (force) "force " else ""}reboot notification ($eventName) to ${targetDeviceIds.size} devices...")

    viewModelScope.launch {
      withContext(Dispatchers.IO) {
        val mediaType = MediaType.parse("application/json; charset=utf-8")
        val requestBody = RequestBody.create(mediaType, JSONObject().apply {
          put("event", eventName)
          put("payload", "Trigger reboot")
        }.toString())

        for (deviceId in targetDeviceIds) {
          try {
            val uri =
                "${authResult.baseUrl}/v3/apps/${authResult.appId}/devices/$deviceId/notifications"

            val request = Request.Builder()
                .url(uri)
                .addHeader("Authorization", "Bearer ${authResult.authToken}")
                .post(requestBody)
                .build()

            okHttpClient.newCall(request).execute().use { response ->
              if (!response.isSuccessful) {
                throw Exception("Received non-OK status from server: HTTP/1.1 ${response.code()} ${response.message()}")
              }
              log(SUCCESS, "Notification sent to $deviceId")
            }
          } catch (e: Exception) {
            log(ERROR, "Failed to send to $deviceId: $e")
          }
        }
      }
    }
  }

  private fun log(level: Level, message: String) {
    when (level) {
      ERROR -> Log.e(TAG, message)
      INFO -> Log.i(TAG, message)
      SUCCESS -> Log.i(TAG, message)
    }
    viewModelScope.launch(Dispatchers.Main) {
      _logEntry.value = LogEntry(level, message)
    }
  }
}
