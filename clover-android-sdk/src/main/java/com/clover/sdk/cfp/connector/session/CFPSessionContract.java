package com.clover.sdk.cfp.connector.session;

import android.content.UriMatcher;
import android.net.Uri;

/**
 A contract class is a public final class that contains constant definitions for the URIs,
 column names, MIME types, and other meta-data that related to the provider.
 The class establishes a contract between the provider and other applications by ensuring that the provider
 can be correctly accessed even if there are changes to the actual values of URIs, column names, and so forth.
 */

public class CFPSessionContract {
  public static final int SESSION = 10;
  public static final int SESSION_CUSTOMER_INFO = 20;
  public static final int SESSION_DISPLAY_ORDER = 30;
  public static final int SESSION_TRANSACTION = 40;
  public static final int SESSION_MESSAGE = 45;
  public static final int PROPERTIES = 50;
  public static final int PROPERTIES_KEY = 60;
  public static final int EVENT = 70;

  // Session table/column definitions
  public static final String SESSION_TABLE_NAME = "SESSION";
  public static final String COLUMN_ID = "_ID";
  public static final String COLUMN_CUSTOMER_INFO = "CUSTOMER_INFO";
  public static final String COLUMN_DISPLAY_ORDER = "DISPLAY_ORDER";
  public static final String COLUMN_DISPLAY_ORDER_MODIFICATION_SUPPORTED = "DISPLAY_ORDER_MODIFICATION_SUPPORTED";
  public static final String COLUMN_TRANSACTION = "TX";
  public static final String COLUMN_MESSAGE = "CFP_MESSAGE";
  // Session property table/column definition
  public static final String PROPERTIES_TABLE_NAME = "SESSION_PROPERTY";
  public static final String COLUMN_KEY = "KEY";
  public static final String COLUMN_VALUE = "VALUE";
  public static final String COLUMN_SRC = "SRC";
  // Session event
  public static final String SESSION_EVENT = "SESSION_EVENT";

  //Authority is unique string for the app.
  public static String AUTHORITY = "com.clover.engine.providers.cfp.session";

  public static Uri SESSION_URI = Uri.parse("content://" + AUTHORITY + "/" + SESSION_TABLE_NAME);
  public static Uri SESSION_CUSTOMER_URI = Uri.parse("content://" + AUTHORITY + "/" + SESSION_TABLE_NAME + "/" + COLUMN_CUSTOMER_INFO);
  public static Uri SESSION_DISPLAY_ORDER_URI = Uri.parse("content://" + AUTHORITY + "/" + SESSION_TABLE_NAME + "/" + COLUMN_DISPLAY_ORDER);
  public static Uri SESSION_TRANSACTION_URI = Uri.parse("content://" + AUTHORITY + "/" + SESSION_TABLE_NAME + "/" + COLUMN_TRANSACTION);
  public static Uri SESSION_MESSAGE_URI = Uri.parse("content://" + AUTHORITY + "/" + SESSION_TABLE_NAME + "/" + COLUMN_MESSAGE);
  public static Uri PROPERTIES_URI = Uri.parse("content://" + AUTHORITY + "/" + PROPERTIES_TABLE_NAME);
  public static Uri PROPERTIES_KEY_URI = Uri.parse("content://" + AUTHORITY + "/" + PROPERTIES_TABLE_NAME+ "/" + COLUMN_KEY);
  public static Uri EVENT_URI = Uri.parse("content://" + AUTHORITY + "/" + SESSION_EVENT);

  // These should match the Uri definitions above
  public static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
  static {
    // Session Data
    matcher.addURI(CFPSessionContract.AUTHORITY, CFPSessionContract.SESSION_TABLE_NAME, CFPSessionContract.SESSION);
    matcher.addURI(CFPSessionContract.AUTHORITY, CFPSessionContract.SESSION_TABLE_NAME + "/" + CFPSessionContract.COLUMN_CUSTOMER_INFO, CFPSessionContract.SESSION_CUSTOMER_INFO);
    matcher.addURI(CFPSessionContract.AUTHORITY, CFPSessionContract.SESSION_TABLE_NAME + "/" + CFPSessionContract.COLUMN_DISPLAY_ORDER, CFPSessionContract.SESSION_DISPLAY_ORDER);
    matcher.addURI(CFPSessionContract.AUTHORITY, CFPSessionContract.SESSION_TABLE_NAME + "/" + CFPSessionContract.COLUMN_TRANSACTION, CFPSessionContract.SESSION_TRANSACTION);
    matcher.addURI(CFPSessionContract.AUTHORITY, CFPSessionContract.SESSION_TABLE_NAME + "/" + CFPSessionContract.COLUMN_MESSAGE, CFPSessionContract.SESSION_MESSAGE);

    // Session Properties
    matcher.addURI(CFPSessionContract.AUTHORITY, CFPSessionContract.PROPERTIES_TABLE_NAME, CFPSessionContract.PROPERTIES);
    matcher.addURI(CFPSessionContract.AUTHORITY, CFPSessionContract.PROPERTIES_TABLE_NAME + "/" + CFPSessionContract.COLUMN_KEY, CFPSessionContract.PROPERTIES_KEY);
    matcher.addURI(CFPSessionContract.AUTHORITY, CFPSessionContract.PROPERTIES_TABLE_NAME + "/" + CFPSessionContract.COLUMN_KEY + "/*", CFPSessionContract.PROPERTIES_KEY);

    // Session Events
    matcher.addURI(CFPSessionContract.AUTHORITY, CFPSessionContract.SESSION_EVENT + "/*", CFPSessionContract.EVENT);
  }

  public static final String CALL_METHOD_ON_EVENT = "onEvent";
  public static final String CALL_METHOD_ON_REMOTE_EVENT = "onRemoteEvent";
  /*
    Clears all session data and non-protected properties.  Normally
    used as part of doing a reset on the CFD.
   */
  public static final String CALL_METHOD_CLEAR_SESSION = "clearSession";
  /*
    Clears the DisplayOrder, Message & Transaction data from the current session.
    This allows CustomerInfo and potential associated properties to survive,
    should there be a need to retain/revive order association when
    processing pauses between application switching and then restarts.
   */
  public static final String CALL_METHOD_PAUSE_SESSION = "pauseSession";
  public static final String CALL_METHOD_SET_ORDER = "setOrder";
  public static final String CALL_METHOD_SET_CUSTOMER_INFO = "setCustomerInfo";
  public static final String CALL_METHOD_SET_PROPERTY = "setProperty";
  public static final String CALL_METHOD_SET_REMOTE_PROPERTY = "setRemoteProperty";
  public static final String CALL_METHOD_SET_TRANSACTION = "setTransaction";
  public static final String CALL_METHOD_SET_MESSAGE = "setMessage";
}