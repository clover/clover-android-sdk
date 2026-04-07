package com.clover.sdk.util

import android.content.ContentProviderClient
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.RemoteException
import android.provider.BaseColumns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

/**
 * KEEP THIS CLASS IN SYNC WITH THE VERSION IN THE DEVICE OS!
 */
private object DeviceCountersContract {
  const val AUTHORITY = "com.clover.android.csf.device_counters"
  val AUTHORITY_URI: Uri = Uri.parse("content://$AUTHORITY")

  const val METHOD_ADD = "add"
  const val EXTRA_KEY = "key"
  const val EXTRA_AMOUNT = "amount"
  const val RESULT_VALUE = "value"

  object DeviceCounters {
    const val TABLE_NAME = "device_counters"
    val CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath(TABLE_NAME).build()
    const val CONTENT_TYPE = "vnd.android.cursor.dir/device_counters"
    const val CONTENT_ITEM_TYPE = "vnd.android.cursor.item/device_counters"

    const val _ID = BaseColumns._ID
    const val KEY = "key"
    const val VALUE = "value"
  }
}

/**
 * Retrieve and update device counters that persist across device (factory) resets. Device
 * counters are string keys to integer values.
 *
 * Any caller can retrieve counters, even those created by other applications. Do not use this
 * system to store any sensitive data.
 *
 * The functions in this class: [isAvailable], [query], and [add] are all suspending
 * functions. They are marked [JvmSynthetic], hiding them from Java callers. Java callers
 * should use the blocking equivalents: [isAvailableBlocking], [queryBlocking], and
 * [addBlocking].
 */
class DeviceCounters(private val context: Context) {
  companion object {
  }

  /**
   * Are device counters available on this device? Calling other methods when this method
   * returns false will throw an [UnsupportedOperationException].
   */
  @JvmSynthetic
  suspend fun isAvailable(): Boolean {
    return withContext(Dispatchers.IO) {
      context.contentResolver.acquireContentProviderClient(DeviceCountersContract.AUTHORITY)?.let {
        it.release()
        true
      } ?: false
    }
  }

  fun isAvailableBlocking() = runBlocking {
    isAvailable()
  }

  /**
   * Query all device counters.
   *
   * While all counters are returned, the semantics of each are defined by the creating
   * application. In most cases it will only make sense to consider counters that your
   * application "owns" (created, and is responsible for updating), or otherwise has a
   * relationship with the owner. To filter for counters that your application owns:
   * ```
   * val result = DeviceCounters(context).query()
   * result.onSuccess { counters ->
   *   val myCounters = counters.filter {
   *     it.key.substringBeforeLast(".") == context.packageName
   *   }
   * }
   * ```
   *
   * @return a [Result] containing a [Map] of [String] key to [Long] value.
   */
  @JvmSynthetic
  suspend fun query(): Result<Map<String, Long>> {
    return withContext(Dispatchers.IO) {
      if (!isAvailable()) {
        return@withContext Result.failure(UnsupportedOperationException("Device counters not available"))
      }

      val projection = arrayOf(
          DeviceCountersContract.DeviceCounters.KEY,
          DeviceCountersContract.DeviceCounters.VALUE
      )

      safeCall { client ->
        val cursor = client.query(
            DeviceCountersContract.DeviceCounters.CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        mutableMapOf<String, Long>().apply {
          cursor?.use { c ->
            val keyIndex =
                c.getColumnIndexOrThrow(DeviceCountersContract.DeviceCounters.KEY)
            val valueIndex =
                c.getColumnIndexOrThrow(DeviceCountersContract.DeviceCounters.VALUE)
            while (c.moveToNext()) {
              val key = c.getString(keyIndex)
              val value = c.getLong(valueIndex)
              put(key, value)
            }
          }
        }
      }
    }
  }

  @Throws(
      UnsupportedOperationException::class,
      RemoteException::class,
      IllegalStateException::class
  )
  fun queryBlocking(): Map<String, Long> = runBlocking {
    query().getOrThrow()
  }

  /**
   * Add to a specific counter. If the counter does not exist, it is created, with
   * an initial value of [amount]. If the counter exists, it is increased by [amount].
   *
   * Counters keys must be of the form:
   * ```
   * <your.dot.separated.package.name>.<your_key_name_without_dots>
   * ```
   * For example:
   * ```
   * com.clover.my_app.lines_printed
   * ```
   * (Assuming the calling application's package is com.clover.my_app).  If you try to add to a
   * a counter key that is not of this form, a [SecurityException] is returned in a failure
   * [Result]. This has the side effect of preventing mutation of counters that the caller
   * does not "own".
   *
   * @return a [Result] containing the new [Long] value of the counter.
   */
  @JvmSynthetic
  suspend fun add(key: String, amount: Long): Result<Long> {
    return withContext(Dispatchers.IO) {
      if (!isAvailable()) {
        return@withContext Result.failure(UnsupportedOperationException("Device counters not available"))
      }

      val extras = Bundle().apply {
        putString(DeviceCountersContract.EXTRA_KEY, key)
        putLong(DeviceCountersContract.EXTRA_AMOUNT, amount)
      }

      safeCall { client ->
        val result = client.call(
            DeviceCountersContract.METHOD_ADD,
            null,
            extras
        ) ?: throw IllegalStateException("Provider returned null result")

        val value = result.getLong(DeviceCountersContract.RESULT_VALUE, -1L)
        if (value < 0) {
          throw SecurityException("Invalid counter key: $key")
        }
        value
      }
    }
  }

  @Throws(
      UnsupportedOperationException::class,
      SecurityException::class,
      RemoteException::class,
      IllegalStateException::class
  )
  fun addBlocking(key: String, amount: Long): Long = runBlocking {
    add(key, amount).getOrThrow()
  }

  /**
   * Convenience method for adding 1 to a counter.
   *
   * @see add
   */
  @JvmSynthetic
  suspend fun inc(key: String): Result<Long> = add(key, 1)

  fun incBlocking(key: String): Long = runBlocking {
    inc(key).getOrThrow()
  }

  private fun <T> safeCall(block: (ContentProviderClient) -> T): Result<T> {
    val client = context.contentResolver.acquireUnstableContentProviderClient(
        DeviceCountersContract.AUTHORITY_URI
    ) ?: return Result.failure(
        IllegalStateException(
            "Failed to acquire content provider client for URI: ${DeviceCountersContract.AUTHORITY_URI}"
        )
    )
    return try {
      Result.success(block(client))
    } catch (e: Exception) {
      Result.failure(e)
    } finally {
      client.release()
    }
  }
}
