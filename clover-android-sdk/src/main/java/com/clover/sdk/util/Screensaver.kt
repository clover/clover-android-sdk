package com.clover.sdk.util

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.clover.sdk.Lockscreen
import com.clover.sdk.internal.util.UnstableContentResolverClient
import com.clover.sdk.util.Screensaver.ScreensaverSetting.*

/**
 * Enable, disable, and configure the screensaver.
 *
 * Not all methods are supported on all devices. If a method is not supported, it will
 * throw [UnsupportedOperationException]. For example:
 * ```
 * val screensaver = Screensaver(context)
 * try {
 *   screensaver.gotoSleep()
 *   // Supported on this device
 * } catch (e: UnsupportedOperationException) {
 *   // Not supported on this device
 * }
 * ```
 * Alternatively, you can check if the methods in this class are supported by inspecting
 * [supported].
 *
 * All methods are blocking and should be invoked on a background thread.
 */
class Screensaver(context: Context) {

  val unstableClient = UnstableContentResolverClient(context.contentResolver, CONTENT_URI).apply {
    noDefault = true
  }

  /**
   * Forces the device to go to sleep.
   *
   * Overrides all the wake locks that are held. This is what happens when the power key is
   * pressed to turn off the screen.
   *
   * While this will cause the device to lock, prefer to use [Lockscreen.lock] if that's
   * your goal.
   *
   * @see Lockscreen
   */
  @Throws(UnsupportedOperationException::class)
  fun gotoSleep() {
    checkSupported(CALL_METHOD_GOTO_SLEEP)

    unstableClient.call(CALL_METHOD_GOTO_SLEEP, null, null, null)
  }

  /**
   * Set secure settings related to the screensaver.
   *
   * To enable the screensaver, set [screensaver_enabled] to `true`, and set one or both of
   * [screensaver_activate_on_sleep] or [screensaver_activate_on_dock] to `true`.
   *
   * [screensaver_activate_on_sleep] and [screensaver_activate_on_dock] are not
   * mutually exclusive. For example, if both are set to `true`, the screensaver will activate
   * when the device goes to sleep and when the device is docked (or charging).
   *
   * Setting [screensaver_activate_on_sleep] and [screensaver_activate_on_dock] to `false` will
   * effectively disable the screensaver.
   *
   * @see screensaver_enabled
   * @see screensaver_activate_on_sleep
   * @see screensaver_activate_on_dock
   */
  @Throws(UnsupportedOperationException::class)
  fun setScreensaverSetting(key: ScreensaverSetting, value: Boolean) {
    checkSupported(CALL_METHOD_SET_SCREENSAVER_SETTING)

    val extras = Bundle().apply {
      putString(EXTRA_SCREENSAVER_SETTING_KEY, key.name)
      putBoolean(EXTRA_SCREENSAVER_SETTING_VALUE, value)
    }

    unstableClient.call(CALL_METHOD_SET_SCREENSAVER_SETTING, null, extras, null)
  }

  /**
   * Get secure settings related to the screensaver.
   */
  @Throws(UnsupportedOperationException::class)
  fun getScreensaverSetting(key: ScreensaverSetting): Boolean {
    checkSupported(CALL_METHOD_GET_SCREENSAVER_SETTING)

    val extras = Bundle().apply {
      putString(EXTRA_SCREENSAVER_SETTING_KEY, key.name)
    }

    val response = unstableClient.call(
      CALL_METHOD_GET_SCREENSAVER_SETTING,
      null,
      extras,
      null
    ) as? Bundle

    return response?.getBoolean(EXTRA_SCREENSAVER_SETTING_VALUE, false) ?: false
  }

  /**
   * Set the active dream component.
   *
   * See Android's
   * [DreamService](https://developer.android.com/reference/android/service/dreams/DreamService)
   * documentation for more information.
   */
  @Throws(UnsupportedOperationException::class)
  fun setDreamComponents(dreamComponents: List<ComponentName>) {
    checkSupported(CALL_METHOD_SET_DREAM_COMPONENTS)

    val extras = Bundle().apply {
      putStringArray(
        EXTRA_DREAM_COMPONENTS,
        dreamComponents.map { it.flattenToString() }.toTypedArray()
      )
    }

    unstableClient.call(CALL_METHOD_SET_DREAM_COMPONENTS, "", extras, null)
  }

  /**
   * Get the active dream components.
   *
   * See Android's
   * [DreamService](https://developer.android.com/reference/android/service/dreams/DreamService)
   * documentation for more information.
   */
  @Throws(UnsupportedOperationException::class)
  fun getDreamComponents(): List<ComponentName> {
    checkSupported(CALL_METHOD_GET_DREAM_COMPONENTS)

    val response = unstableClient.call(
      CALL_METHOD_GET_DREAM_COMPONENTS,
      "",
      Bundle.EMPTY,
      null
    ) as? Bundle

    return response
      ?.getStringArray(EXTRA_DREAM_COMPONENTS)
      ?.mapNotNull { componentString ->
        componentString?.let {
          ComponentName.unflattenFromString(it)
        }
      } ?: emptyList()
  }

  /**
   * Convenience method to check if the methods in this class are supported on this device.
   */
  val supported: Boolean
    // If one is supported, they all are
    get() = isSupported(CALL_METHOD_GET_DREAM_COMPONENTS)

  private fun isSupported(method: String): Boolean {
    // History: CALL_METHOD_GET_SUPPORTED_APIS was added on SDA660 platforms. If it's
    // implemented, we use it to determine support. If it's not implemented, the platform
    // will, somewhat unexpectedly, throw SecurityException. Since we know that if
    // CALL_METHOD_GET_SUPPORTED_APIS is not implemented, neither are the other methods,
    // we can assume SecurityException means not supported.

    try {
      val result = unstableClient.call(CALL_METHOD_GET_SUPPORTED_APIS, null, null, null)
      val apis = result?.getStringArray(EXTRA_SUPPORTED_APIS) ?: return false
      return apis.any { it == method }
    } catch (e: SecurityException) {
      Log.i(TAG, "Method: $method is not supported: $e")
    } catch (e: Exception) {
      // We only expect SecurityException, but catch all exceptions to be safe.
      Log.e(TAG, "Method: $method is not supported: $e")
    }
    return false;
  }

  @Throws(UnsupportedOperationException::class)
  private fun checkSupported(method: String) {
    if (!isSupported(method)) {
      throw UnsupportedOperationException("Method: $method not supported")
    }
  }

  enum class ScreensaverSetting {
    /**
     * Is the screensaver enabled?
     */
    screensaver_enabled,
    /**
     * Should the screensaver activate when the device goes to sleep?
     */
    screensaver_activate_on_sleep,
    /**
     * Should the screensaver activate when the device is docked (or charging)?
     */
    screensaver_activate_on_dock,
  }
  companion object {
    private val TAG = Screensaver::class.simpleName

    private const val AUTHORITY = "com.clover.service.provider"
    private val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY")

    private const val CALL_METHOD_GET_SUPPORTED_APIS = "getSupportedApis"
    private const val EXTRA_SUPPORTED_APIS = "apis"

    private const val CALL_METHOD_GOTO_SLEEP = "gotoSleep"

    private const val CALL_METHOD_GET_SCREENSAVER_SETTING = "getScreensaverSetting"
    private const val CALL_METHOD_SET_SCREENSAVER_SETTING = "setScreensaverSetting"
    private const val EXTRA_SCREENSAVER_SETTING_KEY = "screensaverSettingKey"
    private const val EXTRA_SCREENSAVER_SETTING_VALUE = "screensaverSettingValue"

    private const val CALL_METHOD_SET_DREAM_COMPONENTS = "setDreamComponents"
    private const val CALL_METHOD_GET_DREAM_COMPONENTS = "getDreamComponents"
    private const val EXTRA_DREAM_COMPONENTS = "dreamComponents"

  }
}
