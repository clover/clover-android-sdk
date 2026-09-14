package com.clover.android.sdk.examples

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.clover.sdk.v3.merchant.LogoType
import com.clover.sdk.v3.merchant.Merchant
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class IntegratorLogoSync(private val context: Context) {

    private val inMemoryCache = mutableMapOf<String, Bitmap>()
    private val diskCacheDir = File(context.cacheDir, "logo_cache")
    private val client = OkHttpClient()

    companion object {
        private const val TAG = "IntegratorLogoSync"
    }

    init {
        if (!diskCacheDir.exists()) {
            diskCacheDir.mkdirs()
        }
    }

    @Throws(IOException::class)
    fun getLogo(merchant: Merchant, type: LogoType): Bitmap? {
        val logo = merchant.logos?.find { it.logoType == type }
        val url = logo?.url

        Log.d(TAG, "Attempting to get logo of type $type. URL: $url")

        if (url == null || url.isEmpty()) {
            Log.d(TAG, "Logo URL is null or empty.")
            return null
        }

        // Check in-memory cache
        var bitmap = inMemoryCache[url]
        if (bitmap != null) {
            Log.d(TAG, "Logo found in in-memory cache.")
            return bitmap
        }
        Log.d(TAG, "Logo not found in in-memory cache.")

        // Check disk cache
        val cacheFile = File(diskCacheDir, url.hashCode().toString())
        if (cacheFile.exists()) {
            try {
                bitmap = BitmapFactory.decodeFile(cacheFile.absolutePath)
                if (bitmap != null) {
                    Log.d(TAG, "Logo found in disk cache.")
                    inMemoryCache[url] = bitmap
                    return bitmap
                } else {
                    Log.w(TAG, "Failed to decode logo from disk cache.")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error reading logo from disk cache", e)
            }
        }
        Log.d(TAG, "Logo not found in disk cache.")

        // Fetch from network
        Log.d(TAG, "Fetching logo from network: $url")
        try {
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()

            Log.d(TAG, "Network response code: ${response.code()}")

            if (response.isSuccessful) {
                response.body()?.byteStream()?.use { inputStream ->
                    bitmap = BitmapFactory.decodeStream(inputStream)
                    if (bitmap != null) {
                        Log.d(TAG, "Successfully downloaded and decoded logo from network.")
                        inMemoryCache[url] = bitmap
                        // Save to disk cache
                        try {
                            FileOutputStream(cacheFile).use { outputStream ->
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                                Log.d(TAG, "Logo saved to disk cache.")
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Error saving logo to disk cache", e)
                        }
                        return bitmap
                    } else {
                        Log.w(TAG, "Failed to decode logo from network stream.")
                    }
                }
            } else {
                Log.w(TAG, "Failed to fetch logo from network. Response: $response")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching logo from network", e)
        }

        Log.d(TAG, "Returning null, could not get logo.")
        return null
    }
}
