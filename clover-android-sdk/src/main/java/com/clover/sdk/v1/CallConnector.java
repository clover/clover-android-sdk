package com.clover.sdk.v1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import com.clover.sdk.internal.util.UnstableCallClient;

/**
 * Base class for implementing call-based connectors. A connector is a class that encapsulates
 * a content provider call interface.
 */
public abstract class CallConnector {
  protected final Context context;
  private final Uri uri;

  public CallConnector(Context context, Uri uri) {
    this.context = context;
    this.uri = uri;
  }

  /**
   * Invoke a content provider call function with the given URI, method, and extras. This method will re-try several
   * times if the invocation fails.
   *
   * @param method a {@link java.lang.String}, the call method name.
   * @param extras a a {@link android.os.Bundle}, the call method arguments.
   *
   * @return a {@link android.os.Bundle}, the response from the call method, or {@link null} if the call failed.
   */
  protected Bundle call(String method, Bundle extras) {
    return new UnstableCallClient(context.getContentResolver(), uri).call(method, null, extras, null);
  }
}
