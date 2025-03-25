package com.clover.android.sdk.examples

import android.app.Application
import android.content.ComponentName
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.clover.android.sdk.examples.databinding.ActivityScreensaverTestBinding
import com.clover.sdk.util.Screensaver
import com.clover.sdk.util.Screensaver.ScreensaverSetting.screensaver_activate_on_dock
import com.clover.sdk.util.Screensaver.ScreensaverSetting.screensaver_activate_on_sleep
import com.clover.sdk.util.Screensaver.ScreensaverSetting.screensaver_enabled
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScreensaverTestActivity: AppCompatActivity() {
    data class ScreensaverSettings(
        val enabled: Boolean = false,
        val activateOnSleep: Boolean = false,
        val activateOnDock: Boolean = false,
    )

    class ScreensaverViewModel(application: Application): AndroidViewModel(application) {
        private val screensaver = Screensaver(application)

        private val _supported = object: MutableLiveData<Boolean>() {
            override fun onActive() {
                isSupported()
            }
        }
        val isSupported: LiveData<Boolean> = _supported

        private val _unsupported_details =
            MutableLiveData("${Build.MANUFACTURER} ${Build.MODEL} ${Build.VERSION.INCREMENTAL.formatAsIncrementalVersion()}")
        val unsupported_details: LiveData<String> = _unsupported_details
        val supported_details: LiveData<String> = _unsupported_details

        private val _dreamComponents = object: MutableLiveData<List<ComponentName>>() {
            override fun onActive() {
                getComponents()
            }
        }
        val dreamComponents: LiveData<List<ComponentName>> = _dreamComponents

        private val _settings = object: MutableLiveData<ScreensaverSettings>() {
            override fun onActive() {
                getSettings()
            }
        }
        val settings: LiveData<ScreensaverSettings> = _settings

        fun isSupported() {
            viewModelScope.launch {
                _supported.value = withContext(Dispatchers.IO) { screensaver.supported }
            }
        }

        fun getComponents() {
            viewModelScope.launch {
                _dreamComponents.value = withContext(Dispatchers.IO) { screensaver.getDreamComponents() }
            }
        }

        fun setComponent(component: ComponentName) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    screensaver.setDreamComponents(listOf(component))
                }
                getComponents()
            }
        }

        fun setSetting(key: Screensaver.ScreensaverSetting, value: Boolean) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    screensaver.setScreensaverSetting(key, value)
                }
            }
        }

        fun getSettings() {
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

        fun gotoSleep() {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    screensaver.gotoSleep()
                }
            }
        }

        companion object {
            internal val DESKCLOCK_SCREENSAVER =
                ComponentName("com.android.deskclock", "com.android.deskclock.Screensaver")
            internal val TOASTERS_SCREENSAVER =
                ComponentName("com.clover.android.sdk.examples", ToasterDreamService::class.java.name)
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

        viewModel.isSupported.observe(this) { supported ->
            binding.supported.visibility = if (supported) View.VISIBLE else View.INVISIBLE
            binding.unsupported.visibility = if (supported) View.INVISIBLE else View.VISIBLE

            if (supported) {
                viewModel.supported_details.observe(this) {
                    binding.supportedDetails.text = it
                }

                viewModel.dreamComponents.observe(this) {
                    binding.components.text = it.joinToString("\n")
                }

                binding.setClock.setOnClickListener {
                    viewModel.setComponent(ScreensaverViewModel.DESKCLOCK_SCREENSAVER)
                }
                binding.setToasters.setOnClickListener {
                    viewModel.setComponent(ScreensaverViewModel.TOASTERS_SCREENSAVER)
                }

                binding.enabled.setOnCheckedChangeListener { _, isChecked ->
                    viewModel.viewModelScope.launch {
                        withContext(Dispatchers.IO) {
                            viewModel.setSetting(screensaver_enabled, isChecked)
                        }
                    }
                }
                binding.activateOnSleep.setOnCheckedChangeListener { _, isChecked ->
                    viewModel.viewModelScope.launch {
                        withContext(Dispatchers.IO) {
                            viewModel.setSetting(screensaver_activate_on_sleep, isChecked)
                        }
                    }
                }
                binding.activateOnDock.setOnCheckedChangeListener { _, isChecked ->
                    viewModel.viewModelScope.launch {
                        withContext(Dispatchers.IO) {
                            viewModel.setSetting(screensaver_activate_on_dock, isChecked)
                        }
                    }
                }
                viewModel.settings.observe(this) {
                    binding.enabled.isChecked = it.enabled
                    binding.activateOnSleep.isChecked = it.activateOnSleep
                    binding.activateOnDock.isChecked = it.activateOnDock
                }

                binding.gotoSleep.setOnClickListener {
                    viewModel.gotoSleep()
                }
            } else {
                viewModel.unsupported_details.observe(this) {
                    binding.unsupportedDetails.text = it
                }
            }
        }
    }
}

private fun String.formatAsIncrementalVersion() =
    "${substring(0, 2)}.${substring(2, 4)}.${substring(4, 7)}"
