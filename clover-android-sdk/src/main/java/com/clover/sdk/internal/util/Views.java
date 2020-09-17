/*
 * Copyright (C) 2016 Clover Network, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clover.sdk.internal.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ScrollView;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * For internal use only.
 */
public final class Views {

  private static final String TAG = Views.class.getSimpleName();

  private Views() { }

  // Short enough to always fit in heap, matches seiko printer max height
  private static final int BITMAP_MAX_HEIGHT = 2048;

  // NOTE: All view operations must occur on the main thread, enable strict mode to test
  public static ArrayList<String> writeBitmapChucks(final View view, OutputUriFactory outputUriFactory) throws IOException {
    if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
      throw new RuntimeException("Long running operation should not be executed on the main thread");
    }

    Point viewWH = runSynchronouslyOnMainThread(() -> {
      Point wh = new Point();
      wh.x = view.getMeasuredWidth();
      wh.y = getContentHeight(view);
      return wh;
    });

    final int measuredWidth = viewWH == null ? 0 : viewWH.x;
    final int contentHeight = viewWH == null ? 0 : viewWH.y;

    if (measuredWidth <= 0 || contentHeight <= 0) {
      throw new IllegalArgumentException("Measured view width or height is 0");
    }

    Bitmap bitmap = null;
    ArrayList<String> outUris = new ArrayList<String>();

    for (int top = 0; top < contentHeight; top += BITMAP_MAX_HEIGHT) {
      int bitmapHeight = Math.min(contentHeight - top, BITMAP_MAX_HEIGHT);

      if (bitmap == null || bitmapHeight < BITMAP_MAX_HEIGHT) {
        if (bitmap != null) {
          bitmap.recycle();
        }

        bitmap = Bitmap.createBitmap(measuredWidth, bitmapHeight, Bitmap.Config.RGB_565);
      }

      bitmap.eraseColor(0xFFFFFFFF);

      final Canvas canvas = new Canvas(bitmap);
      canvas.translate(0, -top);

      runSynchronouslyOnMainThread(() -> {
        view.draw(canvas);
        return null;
      });

      Uri outUri = outputUriFactory.createNewOutputUri();
      BufferedOutputStream bos = new BufferedOutputStream(outputUriFactory.getOutputStreamForUri(outUri));
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
      bos.flush();
      bos.close();

      outUris.add(outUri.toString());
    }

    return outUris;
  }

  private static final Handler sMainHandler = new Handler(Looper.getMainLooper());

  /**
   * Runs the given callable on the main thread synchronously. This method should be invoked on a
   * background thread, the callable must complete very quickly to prevent an ANR, generally used
   * when manipulating Views which Android requires to be on the main thread. This method eats
   * exceptions and returns null.
   */
  private static <T> T runSynchronouslyOnMainThread(Callable<T> callable) {
    try {
      FutureTask<T> futureTask = new FutureTask<>(callable);
      sMainHandler.post(futureTask);
      return futureTask.get();
    } catch (Exception e) {
      Log.w(TAG, e);
      return null;
    }
  }

  @SuppressWarnings("deprecation")
  private static int getContentHeight(View view) {
    if (view instanceof WebView) {
      WebView wv = (WebView) view;
      float scale = wv.getScale(); // Deprecated but works
      // Just in case future Android platform breaks getScale, use default scale of 100%
      if (scale <= 0.0f) {
        scale = 1.0f;
      }
      return (int) (wv.getContentHeight() * scale);
    } else if (view instanceof ScrollView) {
      return ((ScrollView) view).getChildAt(0).getMeasuredHeight();
    } else {
      return view.getMeasuredHeight();
    }
  }

}
