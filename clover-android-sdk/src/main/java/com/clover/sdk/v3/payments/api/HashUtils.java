package com.clover.sdk.v3.payments.api;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {
  @NotNull static public byte[] hashStrings(String algorithm, @NotNull String[] strings) throws NoSuchAlgorithmException, IOException {
    if (strings == null) {
      throw new IllegalArgumentException("2nd arguments can't be null");
    }
    MessageDigest digest = MessageDigest.getInstance(algorithm != null ? algorithm : "MD5");

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    for (String str : strings) {
      if (str == null) {
        str = "";
      }
      byte[] hash = digest.digest(str.getBytes("UTF-8"));
      outputStream.write(hash);
    }

    byte concat[] = outputStream.toByteArray();

    byte[] result = digest.digest(concat);
    return result;
  }
}
