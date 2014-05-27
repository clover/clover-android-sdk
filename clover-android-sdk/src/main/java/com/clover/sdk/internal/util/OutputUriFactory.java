package com.clover.sdk.internal.util;

import android.content.Context;
import android.net.Uri;

import java.io.OutputStream;

public abstract class OutputUriFactory {
  private final Context mContext;

  public OutputUriFactory(Context context) {
    mContext = context;
  }

  /** Return a uri pointing to a new resource that provides an OutputStream */
  public abstract Uri createNewOutputUri();
  /** Return an OutputStream for the given uri that allows writing */
  public abstract OutputStream getOutputStreamForUri(Uri uri);

  public final Context getContext() {
    return mContext;
  }
}
