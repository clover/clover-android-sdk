/**
 * Copyright (C) 2015 Clover Network, Inc.
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