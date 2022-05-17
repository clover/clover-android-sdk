package com.clover.sdk.v3.payments.api;

import android.content.Context;
import android.content.Intent;
import com.clover.sdk.util.CloverAuth;
import com.clover.sdk.v1.Intents;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


public class BaseIntentBuilder {
  public static final String PAPI_SDK_NAME = BaseIntentBuilder.class.getPackage().getName() + ":apapi";
  public Intent build(Context context) {
    if (context == null) {
      throw new IllegalArgumentException("context cannot be null");
    }

    Intent i = new Intent();

    String callingAppPackage = context.getPackageName();
    long timestamp = System.currentTimeMillis();
    UUID requestId = UUID.randomUUID();

    try {
      i.putExtra(Intents.EXTRA_APP_PACKAGE_NAME, callingAppPackage);
      i.putExtra(Intents.EXTRA_REQUEST_ID, requestId);
      i.putExtra(Intents.EXTRA_TIMESTAMP, timestamp);
      i.putExtra(Intents.EXTRA_SDK_NAME, PAPI_SDK_NAME);
      i.putExtra(Intents.EXTRA_SDK_VERSION, "1");

      CloverAuth.AuthResult oauth = CloverAuth.authenticate(context);
      i.putExtra(Intents.EXTRA_APP_ID, oauth.appId);

      // hash oauth token so we can verify in the handling activity
      // OAuth token, request id, timestamp
      byte[] pkgNameOauthHash = HashUtils.hashStrings("MD5", new String[] { String.format("%s", oauth.authToken), requestId.toString(), String.format("%s", timestamp)});

      i.putExtra(Intents.EXTRA_MD5_HASH, pkgNameOauthHash);

    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      // TODO: what to do here...
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (TimeoutException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return i;
  }
}
