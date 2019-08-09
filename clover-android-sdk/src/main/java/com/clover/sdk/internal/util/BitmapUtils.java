package com.clover.sdk.internal.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;

public class BitmapUtils {

  public static final String TAG = "BitmapUtils";
  public static final int MAX_ALLOWED_SIZE = (int) Math.pow(2, 21);
  // com.clover.engine.services.ReceiptPrinterPlugins.Star.StarPrinter#RECEIPT_WIDTH is 576

  public static final String MAX_SIZE_EXCEEDED = "URL Image size of %d exceeds allowed maximum size of " +
      MAX_ALLOWED_SIZE;

  public Bitmap decodeSampledBitmapFromURL(String bitmapUrlString, int reqWidth) {
    return decodeSampledBitmapFromURL(bitmapUrlString, reqWidth, null);
  }

  public Bitmap decodeSampledBitmapFromURL(String bitmapUrlString, int reqWidth, final Bitmap inBitmap) {
    Bitmap bitmap = null;
    BitmapFactory.Options options = new BitmapFactory.Options();

    // The stream will be opened and closed multiple times, because it cannot be reused,
    // and for large graphics, this will be more scalable.  The 'markSupported()' call
    // could be used here as well, however this could cause excessive memory usage -
    // which is one of the issues this tries to address.  In tests, this performs
    // acceptably, but will be constrained by the source of the data at the other
    // end of this URL.

    // This handler just decodes the bounds of the image, and sets these values in the options.
    HandleUrl decodeBounds = new HandleUrl() {
      @Override
      protected boolean handleInputStream(InputStream bitmapInputStream, BitmapFactory.Options options) {
        if (bitmapInputStream == null) {
          return false;
        }
        options.inJustDecodeBounds = true;
        options.inScaled = false;
        BitmapFactory.decodeStream(bitmapInputStream, null, options);
        return true;
      }
    };
    if(decodeBounds.handleStringURL(bitmapUrlString, options)) {
      options.inSampleSize = calculateInSampleSize(options, reqWidth);
      int sampledHeight = options.outHeight / options.inSampleSize;
      int sampledWidth = options.outWidth / options.inSampleSize;

      int samplesize = sampledHeight * sampledWidth;
      if ((sampledHeight * sampledWidth) > MAX_ALLOWED_SIZE) {
        throw new RuntimeException(String.format(Locale.US,MAX_SIZE_EXCEEDED, samplesize));
      }

      // This handler attempts to decode into an existing bitmap which may fail,
      // if there is no existing bitmap this should succeed
      HandleUrl getBitmapFirstAttempt = new HandleUrl() {
        @Override
        protected boolean handleInputStream(InputStream bitmapInputStream, BitmapFactory.Options options) {
          options.inJustDecodeBounds = false;
          options.inBitmap = inBitmap;
          try {
            setBitmap(BitmapFactory.decodeStream(bitmapInputStream, null, options));
          } catch (Exception e) {
            if (inBitmap == null) {
              // If the one that was passed in was null, then there is no reason to try again, it would
              // be the exact same thing we just did.  Bail out.
              Log.w(TAG, "Image decoding failed", e);
              return false;
            }
            setBitmap(null);
          }
          return true;
        }
      };
      if(getBitmapFirstAttempt.handleStringURL(bitmapUrlString, options)) {
        bitmap = getBitmapFirstAttempt.getBitmap();
        if (bitmap == null) {
          // Reusing bitmap failed, that's okay let BitmapFactory create a new bitmap
          HandleUrl getBitmapSecondAttempt = new HandleUrl() {
            @Override
            protected boolean handleInputStream(InputStream bitmapInputStream, BitmapFactory.Options options) {
              options.inBitmap = null;
              setBitmap(BitmapFactory.decodeStream(bitmapInputStream, null, options));
              return true;
            }
          };
          if(getBitmapSecondAttempt.handleStringURL(bitmapUrlString, options)) {
            bitmap = getBitmapSecondAttempt.getBitmap();
          }
        }
      }
    }
    return bitmap;
  }

  public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth) {
    // Raw height and width of image
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (width > reqWidth) {
      final int halfWidth = width / 2;

      // Calculate the largest inSampleSize value that is a power of 2 and keeps both
      // height and width larger than the requested height and width.
      while ((halfWidth / inSampleSize) > reqWidth) {
        inSampleSize *= 2;
      }
    }
    return inSampleSize;
  }

  /**
   * Utility class to centralize the opening and handling of url streams for this class.
   */
  abstract class HandleUrl {
    /**
     * Timeout for the url connection
     * @see URLConnection#setConnectTimeout(int)
     */
    int connectionTimeout = 5000;// More?  Less?

    /**
     * Timeout for the inputstream read
     * @see URLConnection#setReadTimeout(int)
     */
    int readTimeout = 5000;// More?  Less?

    /**
     * Temporary storage of a bitmap object.
     */
    private Bitmap bitmap;

    /**
     * Handle the opening and closing of the url stream.
     *
     * @param bitmapUrlString - the url to open
     * @param options - bitmap options that are changed (effectively an input/output parameter)
     * @return true if processing should continue, else false
     */
    public boolean handleStringURL(String bitmapUrlString, BitmapFactory.Options options) {
      InputStream bitmapInputStream = null;
      try {
        URL url = new URL(bitmapUrlString);
        URLConnection conn = url.openConnection();
        conn.setConnectTimeout(connectionTimeout);
        conn.setReadTimeout(readTimeout);
        bitmapInputStream = conn.getInputStream();

        handleInputStream(bitmapInputStream, options);
      } catch (IOException ioe) {
        Log.w(TAG, String.format(Locale.US,"Could not load image from url %s",
            bitmapUrlString), ioe);
        return false;
      } finally {
        try {
          bitmapInputStream.close();
        } catch (Exception e) {
          Log.w(TAG, "Could not close bitmap stream", e);
        }
      }
      return true;
    }

    public Bitmap getBitmap() {
      return bitmap;
    }
    public void setBitmap(Bitmap bitmap) {
      this.bitmap = bitmap;
    }

    /**
     * Does some form of processing of the passed stream, using the passed options and possibly changing the passed
     * options.
     *
     * @param bitmapInputStream - an open stream that is an image, positioned at the beginning of the stream
     * @param options - bitmap options with some configuration of settings.
     * @return true if processing should continue, else false
     */
    protected abstract boolean handleInputStream(InputStream bitmapInputStream, BitmapFactory.Options options);
  }
}
