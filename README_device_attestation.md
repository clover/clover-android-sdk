Device attestation
===
The Device Attestation SDK creates an _attestation_—a message signed with the Clover device private key. This is used to assert a set of truths about the device or the system state, such that the integrity and authenticity of the message are cryptographically guaranteed by the Clover hardware.

# Create a signed message
```
val userTruths = mapOf(
    "nonce" to theNonce,
    "accountId" to theAccountId,
    ...,
)

// Generate with certificate chain in JWS header
val response = DeviceAttestationClient(context).sign(userTruths, CertificateReference.CERTIFICATE)
// OR
// Generate with SHA-256 certificate thumbprint reference
val response = DeviceAttestationClient(context).sign(userTruths, CertificateReference.THUMBPRINT)
```
`message` is a [Java Web Signature](https://datatracker.ietf.org/doc/html/rfc7515) (JWS) [compact serialization](https://datatracker.ietf.org/doc/html/rfc7515#page-7). JWS is fairly simple, but if you are not comfortable with the specification, there are multiple robust client libraries:
- **Node.js / TypeScript**: jose (https://www.npmjs.com/package/jose) or jsonwebtoken (https://www.npmjs.com/package/jsonwebtoken)
- **Python**: PyJWT (https://pyjwt.readthedocs.io/) or authlib (https://authlib.org/)
- **Java / Kotlin**: nimbus-jose-jwt (https://connect2id.com/products/nimbus-jose-jwt) or okta-jwt-verifier (https://github.com/okta/okta-jwt-verifier-java)
- **Go**: go-jose (https://github.com/go-jose/go-jose)
- **C# / .NET**: System.IdentityModel.Tokens.Jwt (https://www.nuget.org/packages/System.IdentityModel.Tokens.Jwt/)

The following sections describe what to expect in a Clover device attestation JWS compact serialization. Refer to the JWS specification for details.

## Header
The JWS header contains _either_:
- `x5c`: The Clover device intermediate certificate chain (default, or when `CertificateReference.CERTIFICATE` is used in signing).
- `x5t#S256`: The base64, url-encoded SHA-256 certificate thumbprint reference (when `CertificateReference.THUMBPRINT` is used in signing).

When invoking `DeviceAttestationClient.sign`, an optional `CertificateReference` can be supplied. This is either:
- `CertificateReference.CERTIFICATE`: The entire intermediate certificate chain is encoded in the JWS header, in the `x5c` field.
- `CertificateReference.THUMBPRINT`: A SHA-256 hash of the leaf certificate is encoded in the JWS header, in the `x5t#256` field.

`CERTIFICATE` generates a larger message (roughly ~5k bytes, plus encoded payload length), but it is completely self-contained, and can be verified completely offline.

`THUMBPRINT` generates a smaller message (roughly 800 bytes, plus encoded payload length). However, the verifier must obtain, or otherwise have access to, the intermediate certificate chain. The verifier must verify that the thumbprint (hash) in the JWS header matches the hash of the actual SHA-256 leaf certificate (in addition to verifying the signature, and validating the certificate chain).

## Payload
JWS does not define a payload format. Clover uses a well-defined JSON object that defines *device* and *user truths*.
```
{
  "deviceTruths": {
    "timestamp": "2026-06-16T21:12:36Z",
    "clover_id": "GARENZRPEFZ6E",
    "serial": "C051UQ03660028",
    "mid": "12345678901",
    "is_prod": "false"
  },
  "userTruths": {
    "nonce": "c2519b9f-a8bd-459d-bcda-4fba2a014d65",
    "accountId": "16433789684660082549"
  }
}
```

### Device truths
Device truths are attestations made about the signing device, by the signing device.

- `timestamp`: The time the attestation was signed.
- `clover_id`: The Clover merchant UUID.
- `serial`: The Clover device serial number.
- `mid`: The merchant identifier (must not be confused with the merchant UUID).
- `is_prod`: A flag indicating if the message was signed on a production, or otherwise development, Clover device. This must be used to select the correct device root, to complete message verification.

The set of device truths is fixed; these same device truth keys exist in all signed messages.

### User truths
User truths are attestations made by the caller about the device's relationship with their software. While not required, it is recommended that this at least contain a server-generated [cryptographic nonce](https://en.wikipedia.org/wiki/Cryptographic_nonce) that can be authenticated at verification time. It might also contain things like user or account IDs.

## Signature
Per the JWS specification, the signature signs the header and payload.

# Verify a signed message
To verify a message:
1. Decode the JWS compact serialization.
2. Verify the cryptographic signature.
3. Verify that the certificate chain chains to either the development or production device Clover root certificate.

If `THUMBPRINT` mode is used, the verifier must first retrieve and match the intermediate certificate chain before performing signature and path validation.

A Kotlin and Python samples demonstrating verification is provided. See "Samples" below.

The device production and development root certificate, in PEM format, can be found at:
- `certs/device_root_prod.pem` (use with production Clover devices)
- `certs/device_root_dev.pem` (use with development Clover devices)
- `certs/device_root_dev_legacy.pem` (use with older development Clover devices)

Because there are two development root certificates, you may have to write your code to attempt verification with each.

respectively.

> [!IMPORTANT]
> Some older, primarily development devices, may have expired intermediate certificates. This does not affect the operation of the device, or the authenticity of the signature.  

# Rate limiting
Callers are limited to 24 signing requests per day (24 invocations of `DeviceAttestationClient.sign`).

# Samples
To demonstrate message signing and verification, run the Clover Android SDK Examples application, and select "Device attestation test". Find the functions `DeviceAttestationViewModel::sign` and `::verify`. Of course the signer and verifier will never be the same entity, outside sample code. 

Two python samples can be found in `clover-android-sdk-examples/scripts/`:
- `verify_device_attestation.py`: Manual parse and decode the JWS compact serialization, and manual cryptographic verification.
- `jwt_verify_device_attestation.py`: Uses the [PyJWT](https://pyjwt.readthedocs.io/en/stable/) Python module to do the same.

To use them, first generate a signed message using the "Device attestation test" screen in the Clover Android SDK Examples app on the device. In the device's log, look for messages like:
```
06-16 15:41:14.030  9492  9492 I device_attestation: Wrote attestation: /storage/emulated/0/Android/data/com.clover.android.sdk.examples/cache/attestation.jws
06-16 15:41:14.035  9492  9492 I device_attestation: Wrote device certificates: /storage/emulated/0/Android/data/com.clover.android.sdk.examples/cache/device_certs.pem
```
Pull these files from the device to your host PC:
```
adb pull /storage/emulated/0/Android/data/com.clover.android.sdk.examples/cache/attestation.jws
adb pull /storage/emulated/0/Android/data/com.clover.android.sdk.examples/cache/device_certs.pem
```
and run either Python script to verify it.

For messages generated with a `CERTIFICATE` certificate reference:
```
clover-android-sdk-examples/scripts/verify_device_attestation.py --jws attestation.jws --root device_root.pem
# OR
clover-android-sdk-examples/scripts/jwt_verify_device_attestation.py --jws attestation.jws --root device_root.pem
```

For messages generated with a `THUMBPRINT` certificate reference:
```
clover-android-sdk-examples/scripts/verify_device_attestation.py --jws attestation.jws --root device_root.pem --certs device_certs.pem
# OR
clover-android-sdk-examples/scripts/jwt_verify_device_attestation.py --jws attestation.jws --root device_root.pem --certs device_certs.pem
```