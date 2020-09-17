package com.clover.android.sdk.examples;

import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.merchant.Merchant;
import com.clover.sdk.v1.merchant.MerchantConnector;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.Arrays;

public final class Utils {

  private Utils() { }

  @NonNull
  public static Merchant fetchMerchantBlocking(Context ctx) throws IOException {
    MerchantConnector mc = null;
    try {
      mc = new MerchantConnector(ctx, CloverAccount.getAccount(ctx), null);
      Merchant merchant = mc.getMerchant();
      if (merchant != null) {
        return merchant;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      mc.disconnect();
    }

    throw new IOException("No merchant, are Clover services running?");
  }


  public static String intentToString(Intent intent) {
    if (intent == null) {
      return null;
    }

    return intent.toString() + " " + bundleToString(intent.getExtras());
  }

  public static String bundleToString(Bundle bundle) {
    StringBuilder out = new StringBuilder("Bundle[");

    if (bundle == null) {
      out.append("null");
    } else {
      try {
        boolean first = true;
        for (String key : bundle.keySet()) {
          if (!first) {
            out.append(", ");
          }

          out.append(key).append('=');

          Object value = bundle.get(key);

          if (value instanceof int[]) {
            out.append(Arrays.toString((int[]) value));
          } else if (value instanceof byte[]) {
            out.append(Arrays.toString((byte[]) value));
          } else if (value instanceof boolean[]) {
            out.append(Arrays.toString((boolean[]) value));
          } else if (value instanceof short[]) {
            out.append(Arrays.toString((short[]) value));
          } else if (value instanceof long[]) {
            out.append(Arrays.toString((long[]) value));
          } else if (value instanceof float[]) {
            out.append(Arrays.toString((float[]) value));
          } else if (value instanceof double[]) {
            out.append(Arrays.toString((double[]) value));
          } else if (value instanceof String[]) {
            out.append(Arrays.toString((String[]) value));
          } else if (value instanceof CharSequence[]) {
            out.append(Arrays.toString((CharSequence[]) value));
          } else if (value instanceof Parcelable[]) {
            out.append(Arrays.toString((Parcelable[]) value));
          } else if (value instanceof Bundle) {
            out.append(bundleToString((Bundle) value));
          } else {
            out.append(value);
          }

          first = false;
        }
      } catch (Exception e) {
        return "Bundle[could not unparcel extras]";
      }
    }

    out.append("]");
    return out.toString();
  }

}
