package com.clover.sdk.v3.retrofit;

import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * An {@link MockCall} that returns a given string as the response.
 */
public class StringCall extends MockCall {
  private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=UTF-8");
  private static final Request REQUEST = new Request.Builder().url("https://pez.com").build();

  public StringCall(String body) {
    super(REQUEST, new Response.Builder()
        .request(REQUEST)
        .code(200)
        .message("OK")
        .protocol(Protocol.HTTP_1_1)
        .body(ResponseBody.create(MEDIA_TYPE_JSON, body))
        .build());
  }
}
