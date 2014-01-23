/*
 * Copyright (C) 2013 Clover Network, Inc.
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

package com.clover.sdk.v1.printer.job;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ScrollView;

import com.clover.sdk.v1.printer.Category;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

/**
 * Create a PrintJob from a given {@link android.view.View}. This may take time, must not be built on the main thread.
 */
public class ViewPrintJob extends PrintJob implements Serializable {

  // Short enough to always fit in heap, matches seiko printer max height
  private static final int BITMAP_MAX_HEIGHT = 512;

  private static final String TAG = "ViewPrintJob";

  public static class Builder extends PrintJob.Builder {
    protected View view;

    public Builder view(View view) {
      this.view = view;
      return this;
    }

    @Override
    public ViewPrintJob build() {
      return new ViewPrintJob(view, flags);
    }
  }

  public final ArrayList<String> imageFiles;

  protected ViewPrintJob(View view, int flags) {
    super(flags);
    ArrayList<String> files = null;
    try {
      files = producePrintableBitmapFileChucks(view);
    } catch (Exception e) {
      Log.e(TAG, "Unable to produce image files for print", e);
    }
    imageFiles = files;
  }

  @Override
  public Category getPrinterCategory() {
    return Category.RECEIPT;
  }

  // NOTE: All view operations must occur on the main thread, enable strict mode to test
  private static ArrayList<String> producePrintableBitmapFileChucks(final View view) throws IOException {
    if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
      throw new RuntimeException("Long running operation should not be executed on the main thread");
    }

    Point viewWH = runSynchronouslyOnMainThread(new Callable<Point>() {
      @Override
      public Point call() throws Exception {
        Point wh = new Point();
        wh.x = view.getMeasuredWidth();
        wh.y = getContentHeight(view);
        return wh;
      }
    });

    final int measuredWidth = viewWH.x;
    final int contentHeight = viewWH.y;

    if (measuredWidth <= 0 || contentHeight <= 0) {
      throw new IllegalArgumentException("Measured view width or height is 0");
    }

    Bitmap bitmap = null;
    ArrayList<String> outFiles = new ArrayList<String>();

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

      runSynchronouslyOnMainThread(new Callable<Void>() {
        @Override
        public Void call() throws Exception {
          view.draw(canvas);
          return null;
        }
      });

      File dir = new File("/sdcard", "clover" + File.separator + "image-print");
      if (!dir.exists()) {
        dir.mkdirs();
      }
      File outFile = File.createTempFile("image-", ".png", dir);

      BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outFile));
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
      bos.flush();
      bos.close();

      outFiles.add(outFile.getAbsolutePath());
    }

    return outFiles;
  }

  private static final Handler sMainHandler = new Handler(Looper.getMainLooper());

  private static final Executor sMainThreadExecutor = new Executor() {
    @Override
    public void execute(Runnable runnable) {
      sMainHandler.post(runnable);
    }
  };

  private static <T> T runSynchronouslyOnMainThread(final Callable<T> callable) {
    AsyncTask<Void, Void, T> task = new AsyncTask<Void, Void, T>() {
      @Override
      protected T doInBackground(Void... voids) {
        T result;
        try {
          result = callable.call();
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
        return result;
      }
    };

    task.executeOnExecutor(sMainThreadExecutor);

    T result;
    try {
      result = task.get();
    } catch (Exception e) {
      throw new RuntimeException("Error, try creating print job on same thread as view was created", e);
    }
    return result;
  }

  private static int getContentHeight(View view) {
    if (view instanceof WebView) {
      return ((WebView)view).getContentHeight();
    } else if (view instanceof ScrollView) {
      return ((ScrollView)view).getChildAt(0).getMeasuredHeight();
    } else {
      return view.getMeasuredHeight();
    }
  }

}
