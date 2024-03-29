/**
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
package com.clover.sdk.v1.printer;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * A class defining the contract to be implemented by content providers that wish
 * to provide additional content on customer receipts.
 * <p>
 * To register a receipt registration content provider with the Clover platform, use the
 * {@link ReceiptRegistrationConnector#register(Uri)} method.
 * <p/>
 * Receipt registration content providers must support either, or both the
 * {@link Text#CONTENT_TYPE} and {@link Image#CONTENT_TYPE} content types.
 * <p>
 * If the provider supports the {@link Text#CONTENT_TYPE} content type, it must implement the
 * {@link android.content.ContentProvider#query(android.net.Uri, String[], String, String[], String)}
 * method. If the returned cursor contains the column {@link Text#TEXT}, the string
 * value of the column is added to the receipt. The content provider may
 * return a null cursor, or a cursor that does not contain the {@link Text#TEXT} column to
 * indicate that the receipt should not be modified.
 * <p>
 * If the content provider supports the {@link Image#CONTENT_TYPE} content type,
 * it must implement the {@link android.content.ContentProvider#openFile(android.net.Uri, String)}
 * method, and the returned file descriptor value must either:
 * <ul>
 *   <li>
 *     Point to a file that contains bitmap data that can be decoded using
 *     {@link android.graphics.BitmapFactory#decodeStream(java.io.InputStream)}.
 *     For example, it can be a PNG, GIF, or JPG encoded file.
 *   </li>
 *   <li>
 *     Be {@code null}, indicating that the content provider declined to add
 *     image content to this receipt.
 *   </li>
 * </ul>
 * <p/>
 * When called, a receipt registration content provider is passed the {@code PARAM_*}
 * query parameters listed below. This may be used as input to other Clover
 * services to obtain additional information about the order, customer, etc.
 * <p/>
 * Receipt registration content providers have a maximum of 2 seconds to return receipt
 * content. It is advisable to stay well below this limit. You must not perform
 * long-running tasks such as network I/O. If the receipt registration content provider
 * repeatedly fails to respond in the allotted time, throws exceptions, or otherwise
 * fails to return valid content as specified above, the provider may be temporarily
 * or permanently disabled.
 * <p>
 * It is recommended that apps registering receipt registration providers implement
 * a pre-uninstall hook and unregister the content provider. See:
 * {@link com.clover.sdk.v1.Intents#ACTION_APP_PRE_UNINSTALL} for more information.
 *
 * @see com.clover.sdk.v1.printer.IReceiptRegistrationService
 * @see android.content.ContentProvider
 * @see ReceiptRegistrationConnector
 * @see com.clover.sdk.v1.Intents#ACTION_APP_PRE_UNINSTALL
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
   * The receipt width
   */
  public static final String PARAM_RECEIPT_WIDTH = "receipt_width";

  /**
   * The payment ID of the receipt to be printed.
   */
  public static final String PARAM_PAYMENT_ID = "payment_id";

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
