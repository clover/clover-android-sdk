package com.clover.sdk.v3.device

import android.content.Context
import android.net.Uri
import android.os.Bundle
import com.clover.sdk.v3.device.DeviceAttestationClient.DeviceAttestationContract.Companion.CONTENT_URI
import com.clover.sdk.v3.device.DeviceAttestationClient.DeviceAttestationContract.Companion.METHOD_SIGN
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * All possible device truths that can be expected in the signed payload. For convenience,
 * prefer access them using the [Map.serial], [Map.cloverId], [Map.mid], [Map.storeId],
 * [Map.timestamp], and [Map.isProd]
 * extensions.
 */
enum class DeviceTruth(val key: String) {
  SERIAL("serial"),
  CLOVER_ID("clover_id"),
  MID("mid"),
  STORE_ID("store_id"),
  TIMESTAMP("timestamp"),
  IS_PROD("is_prod")
}

val Map<String, String>.serial: String?
  get() = this[DeviceTruth.SERIAL.key]
val Map<String, String>.cloverId: String?
  get() = this[DeviceTruth.CLOVER_ID.key]
val Map<String,String>.mid: String?
  get() = this[DeviceTruth.MID.key]
val Map<String, String>.timestamp: String?
  get() = this[DeviceTruth.TIMESTAMP.key]
val Map<String, String>.isProd: Boolean
  get() = this[DeviceTruth.IS_PROD.key]?.toBoolean() ?: false
val Map<String, String>.storeId: String?
  get() = this[DeviceTruth.STORE_ID.key]

/**
 * Create device attestation messages.
 */
class DeviceAttestationClient(private val context: Context) {

  companion object {}

  enum class CertificateReference {
    /**
     * Store the certificate chain in the JWS header. This results in a larger
     * message, but it is self-contained. The message can be verified offline.
     */
    CERTIFICATE,

    /**
     * Store a thumbprint (SHA-256 hash) of the leaf certificate in the JWS header. This results
     * in a smaller message, but requires the verifier to obtain the actual certificate chain
     * values in another manner.
     *
     * Verifiers must ensure the stored hash matches that of the actual leaf certificate
     * during the verification process.
     */
    THUMBPRINT
  }

  class DeviceAttestationContract {
    companion object {
      const val AUTHORITY = "com.clover.device.attestation"
      val CONTENT_URI: Uri = Uri.Builder().scheme("content").authority(AUTHORITY).build()

      const val METHOD_SIGN = "sign"

      /**
       * A [Bundle] containing key-value user truth pairs (String -> String), passed in the call [METHOD_SIGN].
       */
      const val EXTRA_USER_TRUTHS = "user_truths"

      /**
       * A [String] containing the JWS compact serialization representation, returned from the call [METHOD_SIGN].
       */
      const val EXTRA_SERIALIZED_COMPACT_JWS = "serialized_compact_jws"

      /**
       * A [String] specifying how the signing certificate/chain is referenced in the JWS header.
       * Must be the name of a [CertificateReference] enum value (e.g., `VALUE` or `THUMBPRINT`).
       *
       * @see CertificateReference
       */
      const val EXTRA_CERTIFICATE_REFERENCE = "certificate_reference"
    }
  }

  /**
   * Signs the requested user truths with the specified certificate reference option and returns the JWS compact serialization.
   * Invocations are rate limited.
   *
   * @throws [IllegalStateException] if the request cannot be signed.
   * @throws [SecurityException] if the caller is rate-limited.
   * @throws [IllegalArgumentException] if the request is invalid, or this service is not
   *   supported on this device.
   */
  @JvmOverloads
  @Throws(IllegalStateException::class, SecurityException::class, IllegalArgumentException::class)
  suspend fun sign(
    userTruths: Map<String, String>,
    certificateReference: CertificateReference = CertificateReference.CERTIFICATE
  ): String = withContext(Dispatchers.IO) {
    val userTruthsBundle = Bundle().apply {
      for ((key, value) in userTruths) {
        putString(key, value)
      }
    }
    val extras = Bundle().apply {
      putBundle(DeviceAttestationContract.EXTRA_USER_TRUTHS, userTruthsBundle)
      putString(DeviceAttestationContract.EXTRA_CERTIFICATE_REFERENCE, certificateReference.name)
    }

    val result = checkNotNull(
        context.contentResolver.call(CONTENT_URI, METHOD_SIGN, null, extras)
    ) { "Failed to create attestation" }

    val serializedCompactJws = checkNotNull(
        result.getString(DeviceAttestationContract.EXTRA_SERIALIZED_COMPACT_JWS)
    ) { "Missing signed JWS compact serialization from the response" }

    serializedCompactJws
  }
}
