package com.clover.android.sdk.examples

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.clover.sdk.util.DeviceCounters
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DeviceCountersViewModel(application: Application) : AndroidViewModel(application) {

  private val deviceCounters = DeviceCounters(application)

  private val _countersMap = MutableStateFlow<Map<String, Long>>(emptyMap())
  val countersMap: StateFlow<Map<String, Long>> = _countersMap.asStateFlow()

  private val _status = MutableStateFlow<String?>(null)
  val status: StateFlow<String?> = _status.asStateFlow()

  fun refreshCounters() {
    viewModelScope.launch {
      _status.value = null

      deviceCounters.query()
        .onSuccess { map -> _countersMap.value = map }
        .onFailure { e -> _status.value = "$e" }
    }
  }

  fun add(key: String, delta: Long) {
    if (key.isBlank()) {
      _status.value = "Key cannot be empty"
      return
    }

    viewModelScope.launch {
      _status.value = null

      deviceCounters.add(key, delta)
        .onSuccess { refreshCounters() }
        .onFailure { e -> _status.value = "$e" }
    }
  }
}
