package com.clover.sdk.v1.printer;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Contract defining interface to insert and read and write receipt files for use in Clover applications.
 *
 * This is used internally by the SDK to write receipt files to storage before they are printed and is not
 * generally used directly by applications.
 */
public final class ReceiptFileContract {

  private ReceiptFileContract() { }

  public static final class ReceiptFiles implements BaseColumns, ReceiptFileColumns {

    private ReceiptFiles() { }

    public static final String AUTHORITY = "com.clover.receiptfiles";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    public static final String CONTENT_DIRECTORY = "receipts";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_DIRECTORY);
  }

  public static final class ReceiptFileFactory implements BaseColumns, ReceiptFileColumns {

    private ReceiptFileFactory() { }

    public static final String AUTHORITY = "com.clover.receiptfilefactory";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    public static final String CONTENT_DIRECTORY = "receipts";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_DIRECTORY);
  }

  public static interface ReceiptFileColumns {
    public static final String FILE_EXTENSION = "file_ext";
  }
}