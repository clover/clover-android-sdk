#!/usr/bin/env python3
"""
Clover Device Attestation Verification Script (using PyJWT)

Decodes a JWS compact serialization, parses the certificate chain,
verifies the cryptographic signature using PyJWT, and validates the trust path
up to a specified Root CA.

Requirements:
    pip install PyJWT cryptography

Usage:
    python3 jwt_verify_device_attestation.py --jws attestation.jws --root device_root.pem
"""

import argparse
import base64
import json
import sys
from datetime import datetime, timezone
import jwt
from cryptography import x509
from cryptography.hazmat.primitives import hashes, serialization
from cryptography.hazmat.primitives.asymmetric import padding


def format_dn(name):
  """Formats an X.509 Distinguished Name (DN) into a readable string."""
  return ", ".join(f"{attr.rfc4514_string()}" for attr in name)


def check_validity(cert):
  """Checks if a certificate is currently active and not expired."""
  now = datetime.now(timezone.utc)
  
  # Handle older cryptography library versions that do not have _utc properties
  try:
    not_before = cert.not_valid_before_utc
    not_after = cert.not_valid_after_utc
  except AttributeError:
    not_before = cert.not_valid_before.replace(tzinfo=timezone.utc)
    not_after = cert.not_valid_after.replace(tzinfo=timezone.utc)

  if now < not_before:
    raise ValueError(f"Certificate is not active yet (starts: {not_before})")
  if now > not_after:
    raise ValueError(f"Certificate has expired (expired: {not_after})")


def verify_certificate_signature(cert, issuer_public_key):
  """Verifies that a certificate was signed by the owner of the issuer public key."""
  try:
    issuer_public_key.verify(
        cert.signature,
        cert.tbs_certificate_bytes,
        padding.PKCS1v15(),
        cert.signature_hash_algorithm,
    )
  except Exception as e:
    raise ValueError(f"Certificate signature verification failed: {e}")


def main():
  parser = argparse.ArgumentParser(description="Decode and verify a Clover JWS attestation payload using PyJWT.")
  parser.add_argument("--jws", required=True, help="Path to JWS compact file")
  parser.add_argument("--root", required=True, help="Path to Root CA PEM file")
  parser.add_argument("--certs", required=False, help="Path to PEM file containing the device certificate chain (required for 'x5t#S256' thumbprint mode)")
  args = parser.parse_args()

  print("=== Clover Device Attestation Verifier (PyJWT) ===")
  print(f"[*] JWS Input File: {args.jws}")
  print(f"[*] Root CA PEM File: {args.root}\n")

  try:
    # 1. Load JWS Compact string
    with open(args.jws, "r", encoding="utf-8") as f:
      jws_content = f.read().strip()

    # 2. Use PyJWT to extract the unverified header and the x5c certificates
    header = jwt.get_unverified_header(jws_content)
    
    # Output the raw JOSE header JSON
    print("--- JOSE Header ---")
    print(json.dumps(header, indent=2))
    print()

    # 3. Parse and display certificate chain from x5c or --certs
    x5t_s256 = header.get("x5t#S256")
    certs = []

    if "x5c" in header and header["x5c"]:
      print("--- Certificate Chain Details (from JWS Header x5c) ---")
      for idx, cert_b64 in enumerate(header["x5c"]):
        der_bytes = base64.b64decode(cert_b64)
        cert = x509.load_der_x509_certificate(der_bytes)
        certs.append(cert)
        
        print(f"[{idx}] Subject: {format_dn(cert.subject)}")
        print(f"    Issuer : {format_dn(cert.issuer)}")
        print(f"    Serial : {cert.serial_number}")
        try:
          not_before = cert.not_valid_before_utc
          not_after = cert.not_valid_after_utc
        except AttributeError:
          not_before = cert.not_valid_before
          not_after = cert.not_valid_after
        print(f"    Validity: {not_before} to {not_after}\n")
    elif x5t_s256:
      if not args.certs:
        raise ValueError("JWS uses 'x5t#S256' thumbprint mode, but no external certificate chain was supplied via --certs.")
      
      with open(args.certs, "rb") as f:
        pem_data = f.read()
      
      # Fallback manual PEM parser for older cryptography library versions
      pem_certs_bytes = pem_data.split(b"-----BEGIN CERTIFICATE-----")
      for part in pem_certs_bytes:
        if not part.strip():
          continue
        full_pem = b"-----BEGIN CERTIFICATE-----" + part
        try:
          certs.append(x509.load_pem_x509_certificate(full_pem))
        except Exception:
          pass
          
      if not certs:
        raise ValueError(f"Failed to load any valid PEM certificates from {args.certs}")
        
      print(f"--- Certificate Chain Details (from {args.certs}) ---")
      for idx, cert in enumerate(certs):
        print(f"[{idx}] Subject: {format_dn(cert.subject)}")
        print(f"    Issuer : {format_dn(cert.issuer)}")
        print(f"    Serial : {cert.serial_number}")
        try:
          not_before = cert.not_valid_before_utc
          not_after = cert.not_valid_after_utc
        except AttributeError:
          not_before = cert.not_valid_before
          not_after = cert.not_valid_after
        print(f"    Validity: {not_before} to {not_after}\n")
        
      # Verify that the leaf certificate's SHA-256 matches x5t#S256
      leaf_cert = certs[0]
      digest = hashes.Hash(hashes.SHA256())
      digest.update(leaf_cert.public_bytes(encoding=serialization.Encoding.DER))
      thumbprint_bytes = digest.finalize()
      thumbprint = base64.urlsafe_b64encode(thumbprint_bytes).decode("utf-8").rstrip("=")
      
      if thumbprint != x5t_s256:
        raise ValueError(f"Leaf certificate thumbprint mismatch!\nExpected: {x5t_s256}\nActual:   {thumbprint}")
      print(f"[✓] Leaf certificate matches 'x5t#S256' thumbprint: {x5t_s256}\n")
    else:
      raise ValueError("JWS Header lacks both 'x5c' and 'x5t#S256' certificate references.")

    # 4. Load the Root CA Certificate
    with open(args.root, "rb") as f:
      root_pem = f.read()
    root_cert = x509.load_pem_x509_certificate(root_pem)
    print(f"[R] Root CA Trusted Anchor: {format_dn(root_cert.subject)}")
    try:
      root_not_before = root_cert.not_valid_before_utc
      root_not_after = root_cert.not_valid_after_utc
    except AttributeError:
      root_not_before = root_cert.not_valid_before
      root_not_after = root_cert.not_valid_after
    print(f"    Validity: {root_not_before} to {root_not_after}\n")

    # 5. Use PyJWT to verify the signature cryptographically using the leaf public key
    print("--- Verification Progress ---")
    leaf_cert = certs[0]
    leaf_public_key = leaf_cert.public_key()

    try:
      # PyJWT automatically handles:
      # - Separating header, payload, and signature.
      # - Reconstructing the signature verification input.
      # - Performing RSA-SHA256 signature verification.
      # Disable JWT-specific claims checks (exp, iss, aud) as we are using a standard JWS payload.
      payload = jwt.decode(
          jws_content,
          leaf_public_key,
          algorithms=["RS256"],
          options={
              "verify_signature": True,
              "verify_aud": False,
              "verify_exp": False,
              "verify_iss": False,
          },
      )
      print("[✓] JWS Cryptographic Signature: VALID (Verified via PyJWT)")
    except Exception as e:
      print(f"[✗] JWS Cryptographic Signature: INVALID ({e})")
      sys.exit(1)

    # 6. Validate the trust path up to the trusted Root CA
    try:
      # Check current system clock validity on all certificates
      check_validity(root_cert)
      for cert in certs:
        check_validity(cert)

      # Verify intermediate chain signature link-by-link
      for i in range(len(certs) - 1):
        verify_certificate_signature(certs[i], certs[i + 1].public_key())
      
      # Verify final intermediate against our Root CA
      verify_certificate_signature(certs[-1], root_cert.public_key())

      print("[✓] Certificate Trust Path: VERIFIED (Successfully anchored to Root CA)")
    except Exception as e:
      print(f"[✗] Certificate Trust Path: INVALID ({e})")

    # 7. Print the formatted payload decoded by PyJWT
    print("\n--- Parsed Payload Claims ---")
    print(json.dumps(payload, indent=2))

  except Exception as e:
    print(f"\n[!] Verification halted due to error: {e}", file=sys.stderr)
    sys.exit(1)


if __name__ == "__main__":
  main()
