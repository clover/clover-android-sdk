package com.clover.sdk.v3.device.internal

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.security.KeyPairGenerator

@OptIn(InternalCloverApi::class)
@RunWith(RobolectricTestRunner::class)
class CompactJwsTest {

  @Test
  fun `sign and verify round trip succeeds`() {
    val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
    keyPairGenerator.initialize(2048)
    val keyPair = keyPairGenerator.generateKeyPair()

    val header = JwsHeader(alg = "RS256", x5c = listOf("cert_data_1", "cert_data_2"))
    val payload = "hello_payload"

    // Sign the payload
    val compactJws = CompactJwsSigner.sign(header, payload, keyPair.private)

    // Verify JWS signature
    val isVerified = CompactJwsSigner.verify(compactJws, keyPair.public)
    isVerified shouldBe true

    // Serialize and deserialize
    val serialized = compactJws.serialize()
    val decodedJws = CompactJws.decode(serialized)

    decodedJws shouldBe compactJws

    // Verify deserialized
    val isDecodedVerified = CompactJwsSigner.verify(decodedJws, keyPair.public)
    isDecodedVerified shouldBe true
  }

  @Test
  fun `verify returns false for tampered payload`() {
    val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
    keyPairGenerator.initialize(2048)
    val keyPair = keyPairGenerator.generateKeyPair()

    val header = JwsHeader(alg = "RS256", x5c = listOf("cert_data"))
    val payload = "test_payload"

    val compactJws = CompactJwsSigner.sign(header, payload, keyPair.private)

    val tamperedJws = compactJws.copy(payload = compactJws.payload + "modified")

    val isVerified = CompactJwsSigner.verify(tamperedJws, keyPair.public)
    isVerified shouldBe false
  }

  @Test
  fun `decode throws IllegalArgumentException when string has too few segments`() {
    shouldThrow<IllegalArgumentException> {
      CompactJws.decode("part1.part2")
    }
  }

  @Test
  fun `decode throws IllegalArgumentException when string has too many segments`() {
    shouldThrow<IllegalArgumentException> {
      CompactJws.decode("part1.part2.part3.part4")
    }
  }

  @Test
  fun `sign and verify round trip with x5tS256 succeeds`() {
    val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
    keyPairGenerator.initialize(2048)
    val keyPair = keyPairGenerator.generateKeyPair()

    val header = JwsHeader(alg = "RS256", x5tS256 = "dummy_thumbprint")
    val payload = "hello_payload"

    // Sign the payload
    val compactJws = CompactJwsSigner.sign(header, payload, keyPair.private)

    // Verify JWS signature
    val isVerified = CompactJwsSigner.verify(compactJws, keyPair.public)
    isVerified shouldBe true

    // Serialize and deserialize
    val serialized = compactJws.serialize()
    val decodedJws = CompactJws.decode(serialized)

    decodedJws shouldBe compactJws
    decodedJws.header.x5tS256 shouldBe "dummy_thumbprint"
    decodedJws.header.x5c shouldBe null

    // Verify deserialized
    val isDecodedVerified = CompactJwsSigner.verify(decodedJws, keyPair.public)
    isDecodedVerified shouldBe true
  }
}
