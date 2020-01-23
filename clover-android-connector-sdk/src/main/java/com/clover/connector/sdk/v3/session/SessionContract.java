package com.clover.connector.sdk.v3.session;

import android.content.UriMatcher;
import android.net.Uri;

/**
 A contract class is a public final class that contains constant definitions for the URIs,
 column names, MIME types, and other meta-data that related to the provider.
 The class establishes a contract between the provider and other applications by ensuring that the provider
 can be correctly accessed even if there are changes to the actual values of URIs, column names, and so forth.
 */

public class SessionContract {
  public static final int SESSION = 10;
  public static final int SESSION_CUSTOMER_INFO = 20;
  public static final int SESSION_DISPLAY_ORDER = 30;
  public static final int SESSION_TRANSACTION = 40;
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
  public static final String COLUMN_TRANSACTION_CLASS = "TX_CLASS";

  // Session property key definitions
  public static final String PROPERTY_LOYALTY_RECEIPT_PRINTED = "LOYALTY_RECEIPT_PRINTED";
  public static final String PROPERTY_LOYALTY_POINTS_EARNED = "LOYALTY_POINTS_EARNED";
  public static final String PROPERTY_LOYALTY_POINTS_CALCULATED = "LOYALTY_POINTS_CALCULATED";
  public static final String PROPERTY_LOYALTY_UUID = "LOYALTY_UUID";
  public static final String PROPERTY_LOYALTY_ORDER_ID = "LOYALTY_ORDER_ID";
  // Session property table/column definition
  public static final String PROPERTIES_TABLE_NAME = "SESSION_PROPERTY";
  public static final String COLUMN_KEY = "KEY";
  public static final String COLUMN_VALUE = "VALUE";
  // Session event
  public static final String SESSION_EVENT = "SESSION_EVENT";

  //Authority is unique string for the app.
  public static String AUTHORITY = "com.clover.cfp.provider.session";

  public static Uri SESSION_URI = Uri.parse("content://" + AUTHORITY + "/" + SESSION_TABLE_NAME);
  public static Uri SESSION_CUSTOMER_URI = Uri.parse("content://" + AUTHORITY + "/" + SESSION_TABLE_NAME + "/" + COLUMN_CUSTOMER_INFO);
  public static Uri SESSION_DISPLAY_ORDER_URI = Uri.parse("content://" + AUTHORITY + "/" + SESSION_TABLE_NAME + "/" + COLUMN_DISPLAY_ORDER);
  public static Uri SESSION_TRANSACTION_URI = Uri.parse("content://" + AUTHORITY + "/" + SESSION_TABLE_NAME + "/" + COLUMN_TRANSACTION);
  public static Uri PROPERTIES_URI = Uri.parse("content://" + AUTHORITY + "/" + PROPERTIES_TABLE_NAME);
  public static Uri PROPERTIES_KEY_URI = Uri.parse("content://" + AUTHORITY + "/" + PROPERTIES_TABLE_NAME+ "/" + COLUMN_KEY);
  public static Uri EVENT_URI = Uri.parse("content://" + AUTHORITY + "/" + SESSION_EVENT);

  // These should match the Uri definitions above
  public static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
  static {
    // Session Data
    matcher.addURI(SessionContract.AUTHORITY, SessionContract.SESSION_TABLE_NAME, SessionContract.SESSION);
    matcher.addURI(SessionContract.AUTHORITY, SessionContract.SESSION_TABLE_NAME + "/" + SessionContract.COLUMN_CUSTOMER_INFO, SessionContract.SESSION_CUSTOMER_INFO);
    matcher.addURI(SessionContract.AUTHORITY, SessionContract.SESSION_TABLE_NAME + "/" + SessionContract.COLUMN_DISPLAY_ORDER, SessionContract.SESSION_DISPLAY_ORDER);
    matcher.addURI(SessionContract.AUTHORITY, SessionContract.SESSION_TABLE_NAME + "/" + SessionContract.COLUMN_TRANSACTION, SessionContract.SESSION_TRANSACTION);

    // Session Properties
    matcher.addURI(SessionContract.AUTHORITY, SessionContract.PROPERTIES_TABLE_NAME, SessionContract.PROPERTIES);
    matcher.addURI(SessionContract.AUTHORITY, SessionContract.PROPERTIES_TABLE_NAME + "/" + SessionContract.COLUMN_KEY, SessionContract.PROPERTIES_KEY);
    matcher.addURI(SessionContract.AUTHORITY, SessionContract.PROPERTIES_TABLE_NAME + "/" + SessionContract.COLUMN_KEY + "/*", SessionContract.PROPERTIES_KEY);

    // Session Events
    matcher.addURI(SessionContract.AUTHORITY, SessionContract.SESSION_EVENT + "/*", SessionContract.EVENT);
  }


  public static final String CALL_METHOD_ON_EVENT = "onEvent";
  public static final String CALL_METHOD_CLEAR_SESSION = "clearSession";
  public static final String CALL_METHOD_GET_MERCHANT = "getMerchant";
  public static final String CALL_METHOD_GET_EMPLOYEE = "getEmployee";
  public static final String CALL_METHOD_SET_ORDER = "setOrder";
  public static final String CALL_METHOD_SET_CUSTOMER_INFO = "setCustomerInfo";
  public static final String CALL_METHOD_SET_PROPERTY = "setProperty";
  public static final String CALL_METHOD_SET_TRANSACTION = "setTransaction";
  public static final String CALL_METHOD_ANNOUNCE_CUSTOMER_PROVIDED_DATA = "announceCustomerProvidedData";

  }