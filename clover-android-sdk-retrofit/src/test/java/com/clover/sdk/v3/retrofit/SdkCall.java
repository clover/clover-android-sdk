package com.clover.sdk.v3.retrofit;

import com.clover.sdk.JSONifiable;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * An {@link MockCall} that returns a {@link JSONifiable}'s string form as the response.
 */
public class SdkCall extends MockCall {
  private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=UTF-8");
  private static final Request REQUEST = new Request.Builder().url("https://pez.com").build();

  public SdkCall(JSONifiable jsonifiable) {
    super(REQUEST, new Response.Builder()
        .request(REQUEST)
        .code(200)
        .message("OK")
        .protocol(Protocol.HTTP_1_1)
        .body(ResponseBody.create(MEDIA_TYPE_JSON, jsonifiable.getJSONObject().toString()))
        .build());
  }
}
