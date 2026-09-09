package com.clover.sdk.v3.device.internal

import android.util.Base64
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature

@InternalCloverApi
data class JwsHeader(
  val alg: String = "RS256",
  val x5c: List<String>? = null,
  @SerializedName("x5t#S256")
  val x5tS256: String? = null
)

@InternalCloverApi
data class CompactJws(
  val header: JwsHeader,
  val payload: String,
  val signatureBytes: ByteArray
) {
  private val gson = Gson()

  val headerB64: String by lazy {
    val headerJson = gson.toJson(header)
    Base64.encodeToString(
        headerJson.toByteArray(Charsets.UTF_8),
        Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
    )
  }

  val payloadB64: String by lazy {
    Base64.encodeToString(
        payload.toByteArray(Charsets.UTF_8),
        Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
    )
  }

  val signatureB64: String by lazy {
    Base64.encodeToString(
        signatureBytes,
        Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
    )
  }

  fun serialize(): String = "$headerB64.$payloadB64.$signatureB64"

  companion object {
    private val gson = Gson()

    fun decode(serializedCompactJws: String): CompactJws {
      val parts = serializedCompactJws.split(".")
      require(parts.size == 3) { "Invalid JWS compact serialization format" }

      val headerJson = String(Base64.decode(parts[0], Base64.URL_SAFE or Base64.NO_WRAP), Charsets.UTF_8)
      val header = gson.fromJson(headerJson, JwsHeader::class.java)

      val payload = String(Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_WRAP), Charsets.UTF_8)
      val signatureBytes = Base64.decode(parts[2], Base64.URL_SAFE or Base64.NO_WRAP)

      return CompactJws(header, payload, signatureBytes)
    }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as CompactJws
    if (header != other.header) return false
    if (payload != other.payload) return false
    if (!signatureBytes.contentEquals(other.signatureBytes)) return false
    return true
  }

  override fun hashCode(): Int {
    var result = header.hashCode()
    result = 31 * result + payload.hashCode()
    result = 31 * result + signatureBytes.contentHashCode()
    return result
  }
}

@InternalCloverApi
object CompactJwsSigner {

  fun sign(
      header: JwsHeader,
      payload: String,
      privateKey: PrivateKey,
      signatureProvider: () -> Signature = { Signature.getInstance("SHA256withRSA") }
  ): CompactJws {
    val tempJws = CompactJws(header, payload, byteArrayOf())
    val signingInput = "${tempJws.headerB64}.${tempJws.payloadB64}"

    val signatureEngine = signatureProvider().apply {
      initSign(privateKey)
      update(signingInput.toByteArray(Charsets.UTF_8))
    }
    val signatureBytes = signatureEngine.sign()

    return CompactJws(header, payload, signatureBytes)
  }

  fun verify(
      compactJws: CompactJws,
      publicKey: PublicKey,
      signatureProvider: () -> Signature = { Signature.getInstance("SHA256withRSA") }
  ): Boolean {
    val signingInput = "${compactJws.headerB64}.${compactJws.payloadB64}"
    val signatureEngine = signatureProvider().apply {
      initVerify(publicKey)
      update(signingInput.toByteArray(Charsets.UTF_8))
    }
    return signatureEngine.verify(compactJws.signatureBytes)
  }
}
