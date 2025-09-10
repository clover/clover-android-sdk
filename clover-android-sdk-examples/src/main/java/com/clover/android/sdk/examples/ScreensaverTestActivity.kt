package com.clover.android.sdk.examples

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.service.dreams.DreamService
import android.view.View
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.clover.android.sdk.examples.databinding.ActivityScreensaverTestBinding
import com.clover.sdk.util.Screensaver
import com.clover.sdk.util.Screensaver.ScreensaverSetting.screensaver_activate_on_dock
import com.clover.sdk.util.Screensaver.ScreensaverSetting.screensaver_activate_on_sleep
import com.clover.sdk.util.Screensaver.ScreensaverSetting.screensaver_enabled
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.content.edit
import androidx.core.widget.doAfterTextChanged

class ScreensaverTestActivity : AppCompatActivity() {
  data class ScreensaverSettings(
    val enabled: Boolean = false,
    val activateOnSleep: Boolean = false,
    val activateOnDock: Boolean = false,
  )

  internal class ScreensaverTestViewModel(application: Application) :
      AndroidViewModel(application) {
    private val screensaver = Screensaver(application)
    private val prefs = application.getSharedPreferences(PREFS_SCREENSAVER, MODE_PRIVATE)

    private val _isSupported = MutableStateFlow(false)
    val isSupported: StateFlow<Boolean> = _isSupported.asStateFlow()

    private val _details = MutableStateFlow(deviceDetails)
    val unsupportedDetails: StateFlow<String> = _details.asStateFlow()
    val supportedDetails: StateFlow<String> = _details.asStateFlow()

    private val _availableDreamComponents = MutableStateFlow<List<ComponentName>>(emptyList())
    val availableDreamComponents: StateFlow<List<ComponentName>> =
        _availableDreamComponents.asStateFlow()

    private val _selectedDreamComponents = MutableStateFlow<List<ComponentName>>(emptyList())
    val selectedDreamComponents: StateFlow<List<ComponentName>> =
        _selectedDreamComponents.asStateFlow()

    private val _dreamsEnabledOnBattery = MutableStateFlow(false)
    val dreamsEnabledOnBattery: StateFlow<Boolean> = _dreamsEnabledOnBattery.asStateFlow()

    private val _settings = MutableStateFlow(ScreensaverSettings())
    val settings: StateFlow<ScreensaverSettings> = _settings.asStateFlow()

    private val _dreamBrightTime = MutableStateFlow(DEFAULT_DREAM_BRIGHT_MINUTES)
    val dreamBrightTime: StateFlow<Int> = _dreamBrightTime.asStateFlow()

    private val _dreamDimTime = MutableStateFlow(DEFAULT_DREAM_DIM_MINUTES)
    val dreamDimTime: StateFlow<Int> = _dreamDimTime.asStateFlow()

    // Individual data loading functions
    fun loadSupported() {
      viewModelScope.launch {
        _isSupported.value =
            withContext(Dispatchers.IO) { screensaver.supported }
      }
    }

    fun loadSelectedDreamComponents() {
      viewModelScope.launch {
        _selectedDreamComponents.value =
            withContext(Dispatchers.IO) { screensaver.getDreamComponents() }
      }
    }

    fun loadAvailableDreamComponents() {
      viewModelScope.launch {
        _availableDreamComponents.value = emptyList()
        _availableDreamComponents.value =
            withContext(Dispatchers.IO) { resolveDreamServices() }
      }
    }

    fun loadDreamsEnabledOnBattery() {
      viewModelScope.launch {
        _dreamsEnabledOnBattery.value =
            withContext(Dispatchers.IO) { screensaver.dreamsEnabledOnBattery }
      }
    }

    fun loadSettings() {
      viewModelScope.launch {
        _settings.value = withContext(Dispatchers.IO) {
          ScreensaverSettings(
              enabled = screensaver.getScreensaverSetting(screensaver_enabled),
              activateOnSleep = screensaver.getScreensaverSetting(screensaver_activate_on_sleep),
              activateOnDock = screensaver.getScreensaverSetting(screensaver_activate_on_dock),
          )
        }
      }
    }

    fun loadDreamBrightTime() {
      viewModelScope.launch {
        _dreamBrightTime.value =
            withContext(Dispatchers.IO) {
              prefs.getInt(
                  PREF_DREAM_BRIGHT_MINUTES,
                  DEFAULT_DREAM_BRIGHT_MINUTES
              )
            }
      }
    }

    fun loadDreamDimTime() {
      viewModelScope.launch {
        _dreamDimTime.value =
            withContext(Dispatchers.IO) {
              prefs.getInt(
                  PREF_DREAM_DIM_MINUTES,
                  DEFAULT_DREAM_DIM_MINUTES
              )
            }
      }
    }

    fun setDreamsEnabledOnBattery(value: Boolean) {
      viewModelScope.launch {
        withContext(Dispatchers.IO) { screensaver.dreamsEnabledOnBattery = value }
        loadDreamsEnabledOnBattery()
      }
    }

    fun setSelectedComponent(component: ComponentName) {
      viewModelScope.launch {
        withContext(Dispatchers.IO) { screensaver.setDreamComponents(listOf(component)) }
        loadSelectedDreamComponents()
      }
    }

    fun setSetting(key: Screensaver.ScreensaverSetting, value: Boolean) {
      viewModelScope.launch {
        withContext(Dispatchers.IO) { screensaver.setScreensaverSetting(key, value) }
        loadSettings()
      }
    }

    fun setDreamBrightTime(minutes: Int) {
      _dreamBrightTime.value = minutes
      viewModelScope.launch {
        withContext(Dispatchers.IO) {
          prefs.edit {
            putInt(
                PREF_DREAM_BRIGHT_MINUTES,
                minutes
            )
          }
        }
        loadDreamBrightTime()
      }
    }

    fun setDreamDimTime(minutes: Int) {
      _dreamDimTime.value = minutes
      viewModelScope.launch {
        withContext(Dispatchers.IO) {
          prefs.edit {
            putInt(
                PREF_DREAM_DIM_MINUTES,
                minutes
            )
          }
        }
        loadDreamDimTime()
      }
    }

    fun startDreaming() {
      viewModelScope.launch {
        withContext(Dispatchers.IO) { screensaver.start() }
      }
    }

    fun gotoSleep() {
      viewModelScope.launch {
        withContext(Dispatchers.IO) { screensaver.gotoSleep() }
      }
    }

    fun resolveDreamServices(): List<ComponentName> {
      val pm: PackageManager = getApplication<Application>().packageManager
      val dreamIntent = Intent(DreamService.SERVICE_INTERFACE)

      val services = pm.queryIntentServices(dreamIntent, PackageManager.GET_META_DATA)

      return services.mapNotNull { resolveInfo ->
        val serviceInfo = resolveInfo.serviceInfo
        if (serviceInfo != null) {
          ComponentName(serviceInfo.packageName, serviceInfo.name)
        } else {
          null
        }
      }
    }

    companion object {
      internal val DESKCLOCK_SCREENSAVER =
          ComponentName("com.android.deskclock", "com.android.deskclock.Screensaver")
      internal val TOASTERS_SCREENSAVER =
          ComponentName("com.clover.android.sdk.examples", ToasterDreamService::class.java.name)
      internal val QIX_SCREENSAVER =
          ComponentName("com.clover.android.sdk.examples", QixDreamService::class.java.name)

      private val deviceDetails =
          "${Build.MANUFACTURER} ${Build.MODEL} ${Build.VERSION.INCREMENTAL.formatAsIncrementalVersion()}"

      internal val DEFAULT_DREAM_BRIGHT_MINUTES = 2
      internal val DEFAULT_DREAM_DIM_MINUTES = 1

      internal val PREFS_SCREENSAVER = "screensaver_test"
      internal val PREF_DREAM_BRIGHT_MINUTES = "pref_dream_bright_time_minutes"
      internal val PREF_DREAM_DIM_MINUTES = "pref_dream_dim_time_minutes"
    }
  }

  private lateinit var binding: ActivityScreensaverTestBinding
  private val viewModel: ScreensaverTestViewModel by viewModels(
      factoryProducer = { ViewModelProvider.AndroidViewModelFactory.getInstance(application) }
  )
  private lateinit var availableComponentsAdapter: ArrayAdapter<String>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityScreensaverTestBinding.inflate(layoutInflater)
    setContentView(binding.root)

    availableComponentsAdapter = ArrayAdapter<String>(
        this@ScreensaverTestActivity,
        android.R.layout.simple_spinner_item,
        mutableListOf<String>(),
    ).apply {
      setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
      binding.availableComponents.adapter = this
    }

    viewModel.loadSupported()
    lifecycleScope.launch {
      viewModel.isSupported.collectLatest { supported ->
        if (supported) {
          setupSupportedUI()
        } else {
          setupUnsupportedUI()
        }
      }
    }
  }

  private fun setupSupportedUI() {
    binding.supported.visibility = View.VISIBLE
    binding.unsupported.visibility = View.GONE

    viewModel.loadSelectedDreamComponents()
    viewModel.loadAvailableDreamComponents()
    viewModel.loadDreamsEnabledOnBattery()
    viewModel.loadSettings()
    viewModel.loadDreamBrightTime()
    viewModel.loadDreamDimTime()

    lifecycleScope.launch {
      viewModel.supportedDetails.collectLatest {
        binding.supportedDetails.text = it
      }
    }

    lifecycleScope.launch {
      viewModel.selectedDreamComponents.collectLatest {
        if (it.isNotEmpty()) {
          if (it.first().packageName == "com.clover.android.sdk.examples") {
            binding.dreamTime.visibility = View.VISIBLE
          } else {
            binding.dreamTime.visibility = View.GONE
          }
        }
        viewModel.loadAvailableDreamComponents()
      }
    }

    lifecycleScope.launch {
      viewModel.availableDreamComponents.collectLatest { list ->
        availableComponentsAdapter.clear()
        availableComponentsAdapter.addAll(list.map { it.flattenToString() })

        viewModel.selectedDreamComponents.value.firstOrNull()?.let { selectedComponent ->
          list.map { it.flattenToString() }.indexOf(selectedComponent.flattenToString())
            .let { index ->
              binding.availableComponents.post {
                binding.availableComponents.setSelection(index)
              }
            }
        }
      }
    }


    binding.enabled.setOnCheckedChangeListener { _, isChecked ->
      viewModel.setSetting(screensaver_enabled, isChecked)
    }

    binding.activateOnSleep.setOnCheckedChangeListener { _, isChecked ->
      viewModel.setSetting(screensaver_activate_on_sleep, isChecked)
    }

    binding.activateOnDock.setOnCheckedChangeListener { _, isChecked ->
      viewModel.setSetting(screensaver_activate_on_dock, isChecked)
    }

    lifecycleScope.launch {
      viewModel.settings.collectLatest {
        binding.enabled.isChecked = it.enabled
        binding.activateOnSleep.isChecked = it.activateOnSleep
        binding.activateOnDock.isChecked = it.activateOnDock
      }
    }

    lifecycleScope.launch {
      viewModel.dreamsEnabledOnBattery.collectLatest {
        binding.dreamsEnabledOnBattery.isChecked = it
      }
    }

    binding.dreamsEnabledOnBattery.setOnCheckedChangeListener { _, isChecked ->
      viewModel.setDreamsEnabledOnBattery(isChecked)
    }

    binding.startDreaming.setOnClickListener {
      viewModel.startDreaming()
    }

    binding.gotoSleep.setOnClickListener {
      viewModel.gotoSleep()
    }

    lifecycleScope.launch {
      viewModel.dreamBrightTime.collectLatest { minutes ->
        if (binding.brightTime.text.toString() != minutes.toString()) {
          binding.brightTime.setText(minutes.toString())
        }
      }
    }

    lifecycleScope.launch {
      viewModel.dreamDimTime.collectLatest { minutes ->
        if (binding.dimTime.text.toString() != minutes.toString()) {
          binding.dimTime.setText(minutes.toString())
        }
      }
    }

    binding.brightTime.doAfterTextChanged { text ->
      val minutes = text.toString().toIntOrNull() ?: 0
      viewModel.setDreamBrightTime(minutes)
    }

    binding.dimTime.doAfterTextChanged { text ->
      val minutes = text.toString().toIntOrNull() ?: 0
      viewModel.setDreamDimTime(minutes)
    }

    binding.availableComponents.onItemSelectedListener = object: OnItemSelectedListener {
      override fun onItemSelected(
        parent: android.widget.AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
      ) {
        viewModel.availableDreamComponents.value.getOrNull(position)?.let {
          viewModel.setSelectedComponent(it)
        }
      }

      override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {
        // No action needed
      }

    }

  }

  private fun setupUnsupportedUI() {
    binding.supported.visibility = View.GONE
    binding.unsupported.visibility = View.VISIBLE

    lifecycleScope.launch {
      viewModel.unsupportedDetails.collectLatest {
        binding.unsupportedDetails.text = it
      }
    }
  }
}

private fun String.formatAsIncrementalVersion() =
    "${substring(0, 2)}.${substring(2, 4)}.${substring(4, 7)}"