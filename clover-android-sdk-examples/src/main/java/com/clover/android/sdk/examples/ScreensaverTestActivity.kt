 package com.clover.android.sdk.examples

import android.app.Application
import android.content.ComponentName
import android.os.Build
import android.os.Bundle
import android.view.View
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

class ScreensaverTestActivity: AppCompatActivity() {
    data class ScreensaverSettings(
        val enabled: Boolean = false,
        val activateOnSleep: Boolean = false,
        val activateOnDock: Boolean = false,
    )

    internal class ScreensaverViewModel(application: Application): AndroidViewModel(application) {
        private val screensaver = Screensaver(application)

        private val _isSupported = MutableStateFlow(false)
        val isSupported: StateFlow<Boolean> = _isSupported.asStateFlow()

        private val _details = MutableStateFlow(deviceDetails)
        val unsupportedDetails: StateFlow<String> = _details.asStateFlow()
        val supportedDetails: StateFlow<String> = _details.asStateFlow()

        private val _dreamComponents = MutableStateFlow<List<ComponentName>>(emptyList())
        val dreamComponents: StateFlow<List<ComponentName>> = _dreamComponents.asStateFlow()

        private val _dreamsEnabledOnBattery = MutableStateFlow(false)
        val dreamsEnabledOnBattery: StateFlow<Boolean> = _dreamsEnabledOnBattery.asStateFlow()

        private val _settings = MutableStateFlow(ScreensaverSettings())
        val settings: StateFlow<ScreensaverSettings> = _settings.asStateFlow()

        // Individual data loading functions
        fun loadSupported() {
            viewModelScope.launch {
                _isSupported.value =
                    withContext(Dispatchers.IO) { screensaver.supported }
            }
        }

        fun loadDreamComponents() {
            viewModelScope.launch {
                _dreamComponents.value =
                    withContext(Dispatchers.IO) { screensaver.getDreamComponents() }
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

        fun setDreamsEnabledOnBattery(value: Boolean) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) { screensaver.dreamsEnabledOnBattery = value }
                loadDreamsEnabledOnBattery()
            }
        }

        fun setComponent(component: ComponentName) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) { screensaver.setDreamComponents(listOf(component)) }
                loadDreamComponents()
            }
        }

        fun setSetting(key: Screensaver.ScreensaverSetting, value: Boolean) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) { screensaver.setScreensaverSetting(key, value) }
                loadSettings()
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

        companion object {
            internal val DESKCLOCK_SCREENSAVER =
                ComponentName("com.android.deskclock", "com.android.deskclock.Screensaver")
            internal val TOASTERS_SCREENSAVER =
                ComponentName("com.clover.android.sdk.examples", ToasterDreamService::class.java.name)

            private val deviceDetails = 
                "${Build.MANUFACTURER} ${Build.MODEL} ${Build.VERSION.INCREMENTAL.formatAsIncrementalVersion()}"
        }
    }

    private lateinit var binding: ActivityScreensaverTestBinding
    private val viewModel: ScreensaverViewModel by viewModels(
        factoryProducer = { ViewModelProvider.AndroidViewModelFactory.getInstance(application) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScreensaverTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        viewModel.loadDreamComponents()
        viewModel.loadDreamsEnabledOnBattery()
        viewModel.loadSettings()

        lifecycleScope.launch {
            viewModel.supportedDetails.collectLatest {
                binding.supportedDetails.text = it
            }
        }

        lifecycleScope.launch {
            viewModel.dreamComponents.collectLatest {
                binding.components.text = it.joinToString("\n")
            }
        }

        binding.setClock.setOnClickListener {
            viewModel.setComponent(ScreensaverViewModel.DESKCLOCK_SCREENSAVER)
        }

        binding.setToasters.setOnClickListener {
            viewModel.setComponent(ScreensaverViewModel.TOASTERS_SCREENSAVER)
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