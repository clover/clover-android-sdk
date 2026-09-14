package com.clover.sdk.v3.device

import android.content.ContentResolver
import android.content.Context
import android.os.Bundle
import com.clover.sdk.v3.device.DeviceAttestationClient.DeviceAttestationContract
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.any
import org.mockito.Mockito.eq
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DeviceAttestationClientTest {

  private lateinit var mockContext: Context
  private lateinit var mockContentResolver: ContentResolver
  private lateinit var client: DeviceAttestationClient

  @Before
  fun setup() {
    mockContext = mock(Context::class.java)
    mockContentResolver = mock(ContentResolver::class.java)
    `when`(mockContext.contentResolver).thenReturn(mockContentResolver)
    client = DeviceAttestationClient(mockContext)
  }

  @Test
  fun `sign returns compact serialized JWS when content resolver call succeeds`() {
    runBlocking {
      val userTruths = mapOf("nonce" to "12345")
      val expectedJwsCompact = "test_header.test_payload.test_signature"

      val resultBundle = Bundle().apply {
        putString(DeviceAttestationContract.EXTRA_SERIALIZED_COMPACT_JWS, expectedJwsCompact)
      }

      `when`(
        mockContentResolver.call(
          eq(DeviceAttestationContract.CONTENT_URI),
          eq(DeviceAttestationContract.METHOD_SIGN),
          eq(null),
          any(Bundle::class.java)
        )
      ).thenReturn(resultBundle)

      val actualResponse = client.sign(userTruths)

      actualResponse shouldBe expectedJwsCompact
    }
  }

  @Test
  fun `sign with certificate reference passes EXTRA_CERTIFICATE_REFERENCE to content resolver`() {
    runBlocking {
      val userTruths = mapOf("nonce" to "12345")
      val expectedJwsCompact = "test_header.test_payload.test_signature"

      val resultBundle = Bundle().apply {
        putString(DeviceAttestationContract.EXTRA_SERIALIZED_COMPACT_JWS, expectedJwsCompact)
      }

      `when`(
        mockContentResolver.call(
          eq(DeviceAttestationContract.CONTENT_URI),
          eq(DeviceAttestationContract.METHOD_SIGN),
          eq(null),
          any(Bundle::class.java)
        )
      ).thenReturn(resultBundle)

      val actualResponse = client.sign(userTruths, DeviceAttestationClient.CertificateReference.THUMBPRINT)

      actualResponse shouldBe expectedJwsCompact

      val bundleCaptor = ArgumentCaptor.forClass(Bundle::class.java)
      verify(mockContentResolver).call(
        eq(DeviceAttestationContract.CONTENT_URI),
        eq(DeviceAttestationContract.METHOD_SIGN),
        eq(null),
        bundleCaptor.capture()
      )
      val capturedBundle = bundleCaptor.value
      capturedBundle.getString(DeviceAttestationContract.EXTRA_CERTIFICATE_REFERENCE) shouldBe "THUMBPRINT"
    }
  }

  @Test
  fun `sign throws IllegalStateException when content resolver call returns null`() {
    runBlocking {
      val userTruths = mapOf("nonce" to "12345")

      `when`(
        mockContentResolver.call(
          eq(DeviceAttestationContract.CONTENT_URI),
          eq(DeviceAttestationContract.METHOD_SIGN),
          eq(null),
          any(Bundle::class.java)
        )
      ).thenReturn(null)

      shouldThrow<IllegalStateException> {
        client.sign(userTruths)
      }
    }
  }

  @Test
  fun `sign throws IllegalStateException when returned bundle is missing response`() {
    runBlocking {
      val userTruths = mapOf("nonce" to "12345")
      val emptyBundle = Bundle()

      `when`(
        mockContentResolver.call(
          eq(DeviceAttestationContract.CONTENT_URI),
          eq(DeviceAttestationContract.METHOD_SIGN),
          eq(null),
          any(Bundle::class.java)
        )
      ).thenReturn(emptyBundle)

      shouldThrow<IllegalStateException> {
        client.sign(userTruths)
      }
    }
  }

  @Test
  fun `sign throws IllegalArgumentException when content resolver call throws IllegalArgumentException`() {
    runBlocking {
      val userTruths = mapOf("nonce" to "12345")

      `when`(
        mockContentResolver.call(
          eq(DeviceAttestationContract.CONTENT_URI),
          eq(DeviceAttestationContract.METHOD_SIGN),
          eq(null),
          any(Bundle::class.java)
        )
      ).thenThrow(IllegalArgumentException("Unknown authority"))

      shouldThrow<IllegalArgumentException> {
        client.sign(userTruths)
      }
    }
  }
}
