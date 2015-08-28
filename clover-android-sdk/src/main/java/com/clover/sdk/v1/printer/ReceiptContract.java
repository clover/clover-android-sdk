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

import android.provider.BaseColumns;

/**
 * A class defining the contract that must be implemented by content providers that wish
 * to provide additional data on customer receipts.
 * To register such a content provider with the Clover platform, use the
 * {@link IReceiptRegistrationService#register(android.net.Uri, com.clover.sdk.v1.ResultStatus)}
 * method.
 * <br/>
 * <br/>
 * Such a content provider must support either the
 * {@link Text#CONTENT_TYPE} or {@link Image#CONTENT_TYPE}
 * content type. If it supports the {@link Text#CONTENT_TYPE} content type, it must implement
 * {@link android.content.ContentProvider#query(android.net.Uri, String[], String, String[], String)}
 * method, and the returned cursor must must contain the columns defined by the {@link Text}
 * class. If the content provider supports the {@link Image#CONTENT_TYPE} content type,
 * it must implement the
 * {@link android.content.ContentProvider#openFile(android.net.Uri, String)}
 * method, and the returned file descriptor must point to a file that contains bitmap
 * data that is decodable using
 * {@link android.graphics.BitmapFactory#decodeStream(java.io.InputStream)}. For example,
 * it can be a PNG, GIF, or JPG encoded file.
 * <br/>
 * <br/>
 * When called, a registered content provider is passed the {@code PARAM_*} query parameters
 * listed below. This may be used as input to other Clover
 * services to obtain additional information about the order, the customer, etc.
 * <br/>
 * <br/>
 * Registered content providers have a maximum of 2 seconds to return receipt data from their
 * content provider. It is advisable to stay well below this limit. You must not perform
 * long-running tasks such as network I/O.
 *
 * @see com.clover.sdk.v1.printer.IReceiptRegistrationService
 * @see android.content.ContentProvider
 */
public final class ReceiptContract {
  /**
   * The order ID of the receipt to be printed.
   */
  public static final String PARAM_ORDER_ID = "order_id";
  /**
   * The Clover account that is printing the receipt.
   */
  public static final String PARAM_ACCOUNT_NAME = "account_name";
  /**
   * The Clover account type is printing the receipt.
   */
  public static final String PARAM_ACCOUNT_TYPE = "account_type";
  /**
   * The merchant ID.
   */
  public static final String PARAM_MERCHANT_ID = "merchant_id";
  /**
   * The print flags.
   */
  public static final String PARAM_FLAGS = "flags";

  /**
   * Column name for returning text-only receipt data. Must map to a string column value.
   */
  public interface TextColumns {
    public static final String TEXT = "text";
  }

  public static final class Image {
    /**
     * Image receipt data content type.
     */
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/receipt_image";
  }

  public static final class Text implements BaseColumns, TextColumns {
    /**
     * Text receipt data content type.
     */
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/receipt_text";
  }
}
