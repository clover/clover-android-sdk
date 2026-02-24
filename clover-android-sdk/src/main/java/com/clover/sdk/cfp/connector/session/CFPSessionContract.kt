package com.clover.sdk.cfp.connector.session

import android.content.UriMatcher
import android.net.Uri

/**
 * A contract class is a public final class that contains constant definitions for the URIs,
 * column names, MIME types, and other meta-data that related to the provider.
 * The class establishes a contract between the provider and other applications by ensuring that the provider
 * can be correctly accessed even if there are changes to the actual values of URIs, column names, and so forth.
 */
object CFPSessionContract {
  const val SESSION: Int = 10
  const val SESSION_CUSTOMER_INFO: Int = 20
  const val SESSION_DISPLAY_ORDER: Int = 30
  const val SESSION_TRANSACTION: Int = 40
  const val SESSION_MESSAGE: Int = 45
  const val PROPERTIES: Int = 50
  const val PROPERTIES_KEY: Int = 60
  const val EVENT: Int = 70

  // Session table/column definitions
  const val SESSION_TABLE_NAME: String = "SESSION"
  const val COLUMN_ID: String = "_ID"
  const val COLUMN_CUSTOMER_INFO: String = "CUSTOMER_INFO"
  const val COLUMN_DISPLAY_ORDER: String = "DISPLAY_ORDER"
  const val COLUMN_DISPLAY_ORDER_MODIFICATION_SUPPORTED: String =
    "DISPLAY_ORDER_MODIFICATION_SUPPORTED"
  const val COLUMN_TRANSACTION: String = "TX"
  const val COLUMN_MESSAGE: String = "CFP_MESSAGE"

  // Session property table/column definition
  const val PROPERTIES_TABLE_NAME: String = "SESSION_PROPERTY"
  const val COLUMN_KEY: String = "KEY"
  const val COLUMN_VALUE: String = "VALUE"
  const val COLUMN_SRC: String = "SRC"
    const val IS_KIOSK_PAY_FOR_ORDER_PROPERTY: String =
    "com.clover.remote.IS_KIOSK_PAY_FOR_ORDER_PROPERTY"
  const val CONTACTLESS_PAYMENTS_CONFIG_PROPERTY: String = "CONTACTLESS_PAYMENTS_CONFIG_PROPERTY"

  // Session event
  const val SESSION_EVENT: String = "SESSION_EVENT"

  //Authority is unique string for the app.
  @JvmField
  var AUTHORITY: String = "com.clover.engine.providers.cfp.session"
  @JvmField
  var SESSION_URI: Uri = Uri.parse("content://$AUTHORITY/$SESSION_TABLE_NAME")
  @JvmField
  var SESSION_CUSTOMER_URI: Uri =
      Uri.parse("content://$AUTHORITY/$SESSION_TABLE_NAME/$COLUMN_CUSTOMER_INFO")
  @JvmField
  var SESSION_DISPLAY_ORDER_URI: Uri =
    Uri.parse("content://$AUTHORITY/$SESSION_TABLE_NAME/$COLUMN_DISPLAY_ORDER")
  @JvmField
  var SESSION_TRANSACTION_URI: Uri =
    Uri.parse("content://$AUTHORITY/$SESSION_TABLE_NAME/$COLUMN_TRANSACTION")
  @JvmField
  var SESSION_MESSAGE_URI: Uri =
    Uri.parse("content://$AUTHORITY/$SESSION_TABLE_NAME/$COLUMN_MESSAGE")
  @JvmField
  var PROPERTIES_URI: Uri = Uri.parse("content://$AUTHORITY/$PROPERTIES_TABLE_NAME")
  @JvmField
  var PROPERTIES_KEY_URI: Uri =
    Uri.parse("content://$AUTHORITY/$PROPERTIES_TABLE_NAME/$COLUMN_KEY")
  @JvmField
  var EVENT_URI: Uri = Uri.parse("content://$AUTHORITY/$SESSION_EVENT")

  // These should match the Uri definitions above
  @JvmField
  val matcher: UriMatcher = UriMatcher(UriMatcher.NO_MATCH)

  init {
    // Session Data
    matcher.addURI(AUTHORITY, SESSION_TABLE_NAME, SESSION)
    matcher.addURI(
      AUTHORITY,
      "$SESSION_TABLE_NAME/$COLUMN_CUSTOMER_INFO",
      SESSION_CUSTOMER_INFO
    )
    matcher.addURI(
      AUTHORITY,
      "$SESSION_TABLE_NAME/$COLUMN_DISPLAY_ORDER",
      SESSION_DISPLAY_ORDER
    )
    matcher.addURI(AUTHORITY, "$SESSION_TABLE_NAME/$COLUMN_TRANSACTION", SESSION_TRANSACTION)
    matcher.addURI(AUTHORITY, "$SESSION_TABLE_NAME/$COLUMN_MESSAGE", SESSION_MESSAGE)

    // Session Properties
    matcher.addURI(AUTHORITY, PROPERTIES_TABLE_NAME, PROPERTIES)
    matcher.addURI(AUTHORITY, "$PROPERTIES_TABLE_NAME/$COLUMN_KEY", PROPERTIES_KEY)
    matcher.addURI(AUTHORITY, "$PROPERTIES_TABLE_NAME/$COLUMN_KEY/*", PROPERTIES_KEY)

    // Session Events
    matcher.addURI(AUTHORITY, "$SESSION_EVENT/*", EVENT)
  }

  const val CALL_METHOD_ON_EVENT: String = "onEvent"
  const val CALL_METHOD_ON_REMOTE_EVENT: String = "onRemoteEvent"

  /*
    Clears all session data and non-protected properties.  Normally
    used as part of doing a reset on the CFD.
   */
  const val CALL_METHOD_CLEAR_SESSION: String = "clearSession"

  /*
    Clears the DisplayOrder, Message & Transaction data from the current session.
    This allows CustomerInfo and potential associated properties to survive,
    should there be a need to retain/revive order association when
    processing pauses between application switching and then restarts.
   */
  const val CALL_METHOD_PAUSE_SESSION: String = "pauseSession"
  const val CALL_METHOD_SET_ORDER: String = "setOrder"
  const val CALL_METHOD_SET_CUSTOMER_INFO: String = "setCustomerInfo"
  const val CALL_METHOD_SET_PROPERTY: String = "setProperty"
  const val CALL_METHOD_SET_REMOTE_PROPERTY: String = "setRemoteProperty"
  const val CALL_METHOD_SET_TRANSACTION: String = "setTransaction"
  const val CALL_METHOD_SET_MESSAGE: String = "setMessage"
  const val CALL_METHOD_GET_SHARED_MEMORY: String = "getSharedMemory"
}