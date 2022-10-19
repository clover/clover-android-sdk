package com.clover.sdk.v3.retrofit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okio.Timeout;

/**
 * A {@link Call} that allows the caller to specify the response. Useful for unit tests.
 */
public class MockCall implements Call {
  protected final Request request;
  protected final Response response;

  private boolean executed = false;
  private boolean canceled= false;

  public MockCall(Request request, Response response) {
    this.request = request;
    this.response = response;
  }

  @Override
  public Request request() {
    return request;
  }

  @Override
  public Response execute() {
    executed = true;
    return response;
  }

  @Override
  public void enqueue(Callback responseCallback) {
    // Nothing
  }

  @Override
  public void cancel() {
    canceled = true;
  }

  @Override
  public boolean isExecuted() {
    return executed;
  }

  @Override
  public boolean isCanceled() {
    return canceled;
  }

  @Override
  public Timeout timeout() {
    return Timeout.NONE;
  }

  @Override
  public Call clone() {
    return new MockCall(request, response);
  }
}
