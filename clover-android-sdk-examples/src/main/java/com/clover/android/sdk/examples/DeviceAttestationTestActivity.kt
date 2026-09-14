package com.clover.android.sdk.examples

import android.app.Application
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.clover.android.sdk.examples.databinding.ActivityDeviceAttestationTestBinding
import com.clover.sdk.v3.device.DeviceAttestationClient
import com.clover.sdk.v3.device.internal.AttestationPayload
import com.clover.sdk.v3.device.internal.CompactJws
import com.clover.sdk.v3.device.internal.CompactJwsSigner
import com.clover.sdk.v3.device.internal.InternalCloverApi
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import java.io.File
import android.util.Log
import com.clover.sdk.v3.device.DeviceAttestationClient.CertificateReference
import com.clover.sdk.v3.device.isProd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigInteger
import java.security.MessageDigest
import java.security.SignatureException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.Random
import java.util.UUID

@OptIn(InternalCloverApi::class)
class DeviceAttestationTestActivity : AppCompatActivity() {

  data class VerificationResult(
    val isSignatureValid: Boolean,
    val leafSubject: String,
    val leafIssuer: String,
    val isChainValid: Boolean,
    val chainError: String?,
    val rootSubject: String,
    val deviceTruths: Map<String, String>,
    val userTruths: Map<String, String>
  )

  sealed interface VerificationState {
    object Idle : VerificationState
    object Loading : VerificationState
    data class Success(val result: VerificationResult) : VerificationState
    data class Error(val error: String) : VerificationState
  }

  sealed interface AttestationState {
    object Idle : AttestationState
    object Loading : AttestationState
    data class Success(val serializedCompactJws: String) : AttestationState
    data class Error(val error: String) : AttestationState
  }

  class AttestationViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
      private const val DEVICE_ROOT_PROD_ASSET = "certs/device_root_prod.pem"
      private const val DEVICE_ROOT_DEV_ASSET = "certs/device_root_dev.pem"

      private val gson = Gson()
    }

    private val client = DeviceAttestationClient(application)

    private val _state = MutableStateFlow<AttestationState>(AttestationState.Idle)
    val state: StateFlow<AttestationState> = _state.asStateFlow()

    private val _verificationState = MutableStateFlow<VerificationState>(VerificationState.Idle)
    val verificationState: StateFlow<VerificationState> = _verificationState.asStateFlow()

    fun sign(
      userTruths: Map<String, String>,
      certificateReference: CertificateReference = CertificateReference.CERTIFICATE,
    ) {
      _state.value = AttestationState.Loading
      _verificationState.value = VerificationState.Idle
      viewModelScope.launch {
        try {
          val serializedCompactJws = client.sign(userTruths, certificateReference)
          writeAttestation(serializedCompactJws)

          val compactJws = CompactJws.decode(serializedCompactJws)
          compactJws.header.x5c?.let { writeDeviceCerts(it) }

          _state.value = AttestationState.Success(serializedCompactJws)
        } catch (e: Exception) {
          _state.value = AttestationState.Error(e.toString())
        }
      }
    }

    private suspend fun writeAttestation(serializedCompactJws: String) = withContext(Dispatchers.IO) {
      val context = getApplication<Application>()
      val cacheDir = context.externalCacheDir ?: context.cacheDir
      val file = File(cacheDir, "attestation.jws")
      file.writeText(serializedCompactJws)
      Log.i(TAG, "Wrote attestation: $file")
    }

    private suspend fun writeDeviceCerts(x5c: List<String>) = withContext(Dispatchers.IO) {
      val context = getApplication<Application>()
      val cacheDir = context.externalCacheDir ?: context.cacheDir
      val file = File(cacheDir, "device_certs.pem")
      val pemString = x5c.joinToString("\n") { base64Der ->
        val formattedDer = base64Der.chunked(64).joinToString("\n")
        "-----BEGIN CERTIFICATE-----\n$formattedDer\n-----END CERTIFICATE-----"
      }
      file.writeText(pemString)
      Log.i(TAG, "Wrote device certificates: $file")
    }

    private suspend fun loadDeviceCerts(): List<X509Certificate>? = withContext(Dispatchers.IO) {
      try {
        val context = getApplication<Application>()
        val cacheDir = context.externalCacheDir ?: context.cacheDir
        val file = File(cacheDir, "device_certs.pem")
        if (file.exists()) {
          val certFactory = CertificateFactory.getInstance("X.509")
          file.inputStream().use { stream ->
            certFactory.generateCertificates(stream).mapNotNull { it as? X509Certificate }
          }
        } else {
          null
        }
      } catch (ex: Exception) {
        Log.w(TAG, "Failed to load device certificates", ex)
        null
      }
    }

    fun verify(serializedCompactJws: String) {
      _verificationState.value = VerificationState.Loading
      viewModelScope.launch {
        try {
          val compactJws = CompactJws.decode(serializedCompactJws)

          val certFactory = CertificateFactory.getInstance("X.509")

          // Resolve intermediate certificate chain

          val x5c = compactJws.header.x5c
          val x5t = compactJws.header.x5tS256

          val certs = if (x5c != null) {
            // We have certificates
            writeDeviceCerts(x5c)
            x5c.map { base64Der ->
              val derBytes = Base64.decode(base64Der, Base64.NO_WRAP)
              certFactory.generateCertificate(ByteArrayInputStream(derBytes)) as X509Certificate
            }
          } else if (x5t != null) {
            // We have a leaf thumbprint
            val decodedCerts = loadDeviceCerts() ?: throw IllegalStateException(
                "No cached device certificates found. Run verification in certificate reference \"certificate\" to first to cache the device certificates."
            )
            val leafCert = decodedCerts.first()
            val digest = MessageDigest.getInstance("SHA-256")
            val hashBytes = digest.digest(leafCert.encoded)
            val leafThumbprint = Base64.encodeToString(
                hashBytes,
                Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
            )
            if (leafThumbprint != x5t) {
              throw SignatureException("Leaf certificate thumbprint mismatch!\nexpected: $x5t\nactual: $leafThumbprint")
            }
            decodedCerts
          } else {
            throw IllegalArgumentException("Header is missing both x5c and x5t#S256 certificate references")
          }

          require(certs.isNotEmpty()) { "Header is missing certificate chain reference" }

          // Cryptographically verify signature

          val leafCert = certs.first()
          val isSignatureValid = CompactJwsSigner.verify(compactJws, leafCert.publicKey)
          if (!isSignatureValid) {
            throw SignatureException("Cryptographic signature verification failed!")
          }

          // Parse payload JSON directly into structured typed payload and determine
          // target root CA

          val payload = gson.fromJson(compactJws.payload, AttestationPayload::class.java)
          val deviceTruths = payload.deviceTruths
          val userTruths = payload.userTruths

          val isProd = deviceTruths.isProd

          // Fetch, and validate root cert

          val rootFileName = if (isProd) DEVICE_ROOT_PROD_ASSET else DEVICE_ROOT_DEV_ASSET
          val rootCert = loadRoot(rootFileName)
          var isChainValid = false
          val chainError: String? = try {
            rootCert.checkValidity()
            for (cert in certs) {
              cert.checkValidity()
            }

            // Verify vert chain

            for (i in 0 until certs.size - 1) {
              certs[i].verify(certs[i + 1].publicKey)
            }
            certs.last().verify(rootCert.publicKey)

            isChainValid = true
            null
          } catch (e: Exception) {
            e.toString()
          }

          val result = VerificationResult(
              isSignatureValid = true,
              leafSubject = leafCert.subjectDN.toString(),
              leafIssuer = leafCert.issuerDN.toString(),
              isChainValid = isChainValid,
              chainError = chainError,
              rootSubject = rootCert.subjectDN.toString(),
              deviceTruths = deviceTruths,
              userTruths = userTruths,
          )

          _verificationState.value = VerificationState.Success(result)
        } catch (e: Exception) {
          Log.w(TAG, "Verification failed", e)
          _verificationState.value = VerificationState.Error(e.toString())
        }
      }
    }

    private fun loadRoot(fileName: String): X509Certificate {
      val application = getApplication<Application>()
      val stream = application.assets.open(fileName)
      val factory = CertificateFactory.getInstance("X.509")
      return factory.generateCertificate(stream) as X509Certificate
    }
  }

  companion object {
    private const val TAG = "device_attestation"
  }

  val userTruthNonce: String = UUID.randomUUID().toString()
  val userTruthAccountId: String = BigInteger(64, Random()).toString(10)

  private val viewModel: AttestationViewModel by viewModels()
  private lateinit var binding: ActivityDeviceAttestationTestBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityDeviceAttestationTestBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.editNonce.setText(userTruthNonce, TextView.BufferType.NORMAL)
    binding.editAccountId.setText(userTruthAccountId, TextView.BufferType.NORMAL)

    // Setup sign action listener
    binding.buttonSign.setOnClickListener {
      val selectedCertRef = if (binding.radioThumbprint.isChecked) {
        CertificateReference.THUMBPRINT
      } else {
        CertificateReference.CERTIFICATE
      }
      viewModel.sign(
          mapOf(
              "nonce" to binding.editNonce.text.toString(),
              "accountId" to binding.editAccountId.text.toString(),
          ),
          selectedCertRef
      )
    }

    // Setup verify action listener
    binding.buttonVerify.setOnClickListener {
      val state = viewModel.state.value
      if (state is AttestationState.Success) {
        viewModel.verify(state.serializedCompactJws)
      }
    }

    // Observe Attestation State Flow
    lifecycleScope.launch {
      viewModel.state.collect { state ->
        when (state) {
          is AttestationState.Idle -> {
            binding.progressBar.visibility = View.GONE
            binding.buttonSign.isEnabled = true
            binding.buttonVerify.isEnabled = false
            binding.textResult.text = "Click \"sign\" to generate attestation."
          }
          is AttestationState.Loading -> {
            binding.progressBar.visibility = View.VISIBLE
            binding.buttonSign.isEnabled = false
            binding.buttonVerify.isEnabled = false
            binding.textResult.text = "Generating attestation..."
          }
          is AttestationState.Success -> {
            binding.progressBar.visibility = View.GONE
            binding.buttonSign.isEnabled = true
            binding.buttonVerify.isEnabled = true
            val serializedCompactJws = state.serializedCompactJws
            try {
              val compactJws = CompactJws.decode(serializedCompactJws)
              binding.textResult.text = buildString {
                append(serializedCompactJws.truncateB64("JWS compact serialization")).append("\n\n")
                append(compactJws.headerB64.truncateB64("Header")).append("\n")
                append(compactJws.payloadB64.truncateB64("Payload")).append("\n")
                append(compactJws.signatureB64.truncateB64("Signature")).append("\n")
              }
            } catch (e: Exception) {
              binding.textResult.text = "Error decoding JWS:\n${e.message}"
            }
          }
          is AttestationState.Error -> {
            binding.progressBar.visibility = View.GONE
            binding.buttonSign.isEnabled = true
            binding.buttonVerify.isEnabled = false
            binding.textResult.text = "Error:\n${state.error}"
          }
        }
      }
    }

    // Observe Verification State Flow
    lifecycleScope.launch {
      viewModel.verificationState.collect { state ->
        when (state) {
          is VerificationState.Idle -> {
            // Keep current result text unchanged
          }
          is VerificationState.Loading -> {
            binding.progressBar.visibility = View.VISIBLE
            binding.buttonSign.isEnabled = false
            binding.buttonVerify.isEnabled = false
            binding.textResult.text = "Verifying cryptographic signature and trust chain..."
          }
          is VerificationState.Success -> {
            binding.progressBar.visibility = View.GONE
            binding.buttonSign.isEnabled = true
            binding.buttonVerify.isEnabled = true
            val result = state.result
            val isProd = result.deviceTruths["is_prod"]?.toBoolean() ?: false
            binding.textResult.text = buildString {
              append("Verification successful!\n\n")
              append("✓ Cryptographic signature: valid\n")
              append("  - Leaf subject: ${result.leafSubject}\n")
              append("  - Leaf issuer: ${result.leafIssuer}\n\n")
              if (result.isChainValid) {
                append("✓ Certificate trust path: verified\n")
                append("  - Trusted anchor: ${result.rootSubject}\n")
                append("  - Full chain validated up to ${if (isProd) "production" else "development"} device root certificate.")
              } else {
                append("✗ Certificate trust path: invalid\n")
                append("  - Error: ${result.chainError}\n")
                append("  - Path could not be verified up to the root CA.")
              }
              append("\n\nDevice truths:\n")
              result.deviceTruths.forEach { (k, v) -> append("  $k: $v\n") }
              append("\nUser truths:\n")
              result.userTruths.forEach { (k, v) -> append("  $k: $v\n") }
            }
          }
          is VerificationState.Error -> {
            binding.progressBar.visibility = View.GONE
            binding.buttonSign.isEnabled = true
            binding.buttonVerify.isEnabled = true
            binding.textResult.text =
                "Verification failed:\n${state.error}\n\nCheck logs for details."
          }
        }
      }
    }
  }
}

private fun String.truncateB64(label: String, size: Int = 32): String {
  val content = if (this.length > size)
    "${this.substring(0, size / 2)}…${this.substring(this.length - (size / 2) + 1, this.length)}"
  else
    this

  return "$label: $content (${this.length} bytes)"
}
