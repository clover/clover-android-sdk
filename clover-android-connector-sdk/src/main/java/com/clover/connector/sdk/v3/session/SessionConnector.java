package com.clover.connector.sdk.v3.session;

import com.clover.sdk.v1.merchant.Merchant;
import com.clover.sdk.v3.customers.CustomerInfo;
import com.clover.sdk.v3.employees.Employee;
import com.clover.sdk.v3.order.DisplayOrder;
import com.clover.sdk.v3.payments.Transaction;

import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/*
  This connector exposes session data used by Remote Pay during the processing
  of idle states, payment flows and custom activities. It wraps the key data
  elements involved in the above states/flows and also allows integrators to inject
  their own key/value properties into the session object, which trigger change events
  and allow retrieval during a "session".  For general purposes, a session represents
  any interaction with a single customer.  The key data objects stored by default in
  the session are:

  CustomerInfo
  DisplayOrder
  Transaction
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class SessionConnector implements Serializable, SessionListener {
    public static final String DISPLAY_ORDER = "com.clover.extra.DISPLAY_ORDER";
    public static final String CUSTOMER_INFO = "com.clover.extra.CUSTOMER_INFO";
    public static final String SESSION = "SESSION";
    public static final String PROPERTIES = "PROPERTIES";
    public static final String TRANSACTION = "TRANSACTION";
    public static final String MESSAGE = "MESSAGE";
    public static final String MESSAGE_DURATION = "MESSAGE_DURATION";

    public static final String QUERY_PARAMETER_VALUE = "value";
    public static final String QUERY_PARAMETER_NAME = "name";
    public static final String BUNDLE_KEY_MERCHANT = "Merchant";
    public static final String BUNDLE_KEY_EMPLOYEE = "Employee";
    public static final String BUNDLE_KEY_TYPE = "TYPE";
    public static final String BUNDLE_KEY_DATA = "DATA";
    public static final String BUNDLE_KEY_MESSAGE = "MESSAGE";
    public static final String BUNDLE_KEY_DURATION = "DURATION";
    public static final String BUNDLE_KEY_TRANSACTION = "TRANSACTION";

    private static final String TAG = "SessionConnector";

    private ContentProviderClient sessionContentProviderClient;
    private WeakReference<Context> contextWeakReference;
    CustomerInfo customerInfo;
    DisplayOrder displayOrder;
    Map<String, String> properties = new HashMap<>();
    Transaction transaction;

    Set<SessionListener> sessionListeners = new LinkedHashSet<>();
    private SessionContentObserver sessionContentObserver = null;
    private ContentObserver propertyContentObserver;

    public SessionConnector(Context context) {
        this.contextWeakReference = new WeakReference<>(context);
        connect();
    }

    private Context getContext() {
        return (null != contextWeakReference) ? contextWeakReference.get() : null;
    }

    public boolean connect() {
        Context context = getContext();
        if (sessionContentProviderClient == null && null != context) {
            sessionContentProviderClient = context.getContentResolver().acquireContentProviderClient(SessionContract.AUTHORITY);
        }
        return sessionContentProviderClient != null;
    }

    public void disconnect() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sessionContentProviderClient.close();
        }
        sessionContentProviderClient = null;
    }

    public boolean addSessionListener(SessionListener listener) {
        Context context = getContext();
        if (sessionListeners.size() == 0 && null == sessionContentObserver && null != context) {
            sessionContentObserver = new SessionContentObserver(this);
            registerContentObserver(context, sessionContentObserver);
        }
        return sessionListeners.add(listener);
    }

    public boolean removeSessionListener(SessionListener listener) {
        Context context = getContext();
        boolean result = sessionListeners.remove(listener);
        if (sessionListeners.size() == 0 && null != context) {
            unregisterContentObserver(context, sessionContentObserver);
            sessionContentObserver = null;
        }
        return result;
    }

    public boolean clear() {
        customerInfo = null;
        displayOrder = null;
        properties.clear();
        transaction = null;
        try {
            if (connect()) {
                sessionContentProviderClient.call(SessionContract.CALL_METHOD_CLEAR_SESSION, null, null);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return true;
    }

    public CustomerInfo getCustomerInfo() {
        customerInfo = null;
        try {
            if (connect()) {
                try (Cursor cursor = sessionContentProviderClient.query(SessionContract.SESSION_CUSTOMER_URI, null, null, null, null)) {
                    if (null != cursor && cursor.moveToFirst()) {
                        String custInfo = cursor.getString(0);
                        if (custInfo != null) {
                            customerInfo = new CustomerInfo(custInfo);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return customerInfo;
    }

    public void setCustomerInfo(CustomerInfo customerInfo) {
        try {
            if (connect()) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(SessionContract.COLUMN_CUSTOMER_INFO, customerInfo);
                sessionContentProviderClient.call(SessionContract.CALL_METHOD_SET_CUSTOMER_INFO, null, bundle);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public DisplayOrder getDisplayOrder() {
        displayOrder = null;
        try {
            if (connect()) {
                try (Cursor cursor = sessionContentProviderClient.query(SessionContract.SESSION_DISPLAY_ORDER_URI, null, null, null, null)) {
                    if (null != cursor && cursor.moveToFirst()) {
                        String dispOrder = cursor.getString(0);
                        if (dispOrder != null) {
                            displayOrder = new DisplayOrder(dispOrder);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return displayOrder;
    }

    public void setDisplayOrder(DisplayOrder displayOrder, boolean isOrderModificationSupported) {
        try {
            if (connect()) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(SessionContract.COLUMN_DISPLAY_ORDER, displayOrder);
                bundle.putBoolean(SessionContract.COLUMN_DISPLAY_ORDER_MODIFICATION_SUPPORTED, isOrderModificationSupported);
                sessionContentProviderClient.call(SessionContract.CALL_METHOD_SET_ORDER, null, bundle);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public Map<String, String> getProperties() {
        properties = new HashMap<>();
        try {
            if (connect()) {
                try (Cursor cursor = sessionContentProviderClient.query(SessionContract.PROPERTIES_URI, null, null, null, null)) {
                    if (null != cursor && cursor.moveToFirst()) {
                        do {
                            String key = cursor.getString(0);
                            String value = cursor.getString(1);
                            properties.put(key, value);
                        } while (cursor.moveToNext());
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return properties;
    }

    public Merchant getMerchantInfo() {
        try {
            Log.d(TAG, "Calling getMerchant");
            Bundle result = sessionContentProviderClient.call(SessionContract.CALL_METHOD_GET_MERCHANT, null, null);
            return result == null ? null : (Merchant) result.getParcelable(BUNDLE_KEY_MERCHANT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Employee getEmployee() {
        try {
            Log.d(TAG, "Calling getEmployee");
            Bundle result = sessionContentProviderClient.call(SessionContract.CALL_METHOD_GET_EMPLOYEE, null, null);
            return result == null ? null : (Employee) result.getParcelable(BUNDLE_KEY_EMPLOYEE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setProperty(String key, String value) {
        try {
            if (connect()) {
                Bundle bundle = new Bundle();
                bundle.putString(SessionContract.COLUMN_KEY, key);
                bundle.putString(SessionContract.COLUMN_VALUE, value);
                sessionContentProviderClient.call(SessionContract.CALL_METHOD_SET_PROPERTY, null, bundle);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public String getProperty(String key) {
        try {
            if (!connect()) return null;

            ContentValues values = new ContentValues();
            values.put(SessionContract.COLUMN_KEY, key);
            Uri propUri = SessionContract.PROPERTIES_KEY_URI.buildUpon().appendPath(key).build();
            try (Cursor cursor = sessionContentProviderClient.query(propUri, null, null, null, null)) {
                if (null != cursor && cursor.moveToFirst()) {
                    return cursor.getString(1); // 0=Key, 1=Value
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    public void removeProperty(String key) {
        try {
            if (!connect()) return;

            Uri propUri = SessionContract.PROPERTIES_KEY_URI.buildUpon().appendPath(key).build();
            sessionContentProviderClient.delete(propUri, null, null);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public Transaction getTransaction() {
        try {
            if (connect()) {
                try (Cursor cursor = sessionContentProviderClient.query(SessionContract.SESSION_TRANSACTION_URI, null, null, null, null)) {
                    if (null != cursor && cursor.moveToFirst()) {
                        String trans = cursor.getString(0);
                        if (trans != null) {
                            transaction = new Transaction(trans);
                        }
                        return transaction;
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    public void setTransaction(Transaction transaction) {
        if (connect()) {
            try {
                Bundle bundle = new Bundle();
                bundle.putParcelable(BUNDLE_KEY_TRANSACTION, transaction);
                sessionContentProviderClient.call(SessionContract.CALL_METHOD_SET_TRANSACTION, null, bundle);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    public void sendSessionEvent(String eventType, String data) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_TYPE, eventType);
        bundle.putString(BUNDLE_KEY_DATA, data);
        try {
            if (connect()) {
                sessionContentProviderClient.call(SessionContract.CALL_METHOD_ON_EVENT, null, bundle);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public void onSessionDataChanged(String type, Object data) {
        for (SessionListener listener : sessionListeners) {
            listener.onSessionDataChanged(type, data);
        }
    }

    @Override
    public void onSessionEvent(String type, String data) {
        for (SessionListener listener : sessionListeners) {
            listener.onSessionEvent(type, data);
        }
    }

    public void announceCustomerProvidedData(String type, String data) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_TYPE, type);
        bundle.putString(BUNDLE_KEY_DATA, data);

        try {
            sessionContentProviderClient.call(SessionContract.CALL_METHOD_ANNOUNCE_CUSTOMER_PROVIDED_DATA, null, bundle);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public void start(String type) {

    }

    private static void registerContentObserver(Context context, SessionContentObserver sessionContentObserver) {
        if (null == context || null == sessionContentObserver) return;

        // Intentionally, not registering for SessionContract.SESSION_URI because it triggers two event notifications
        // for every change.
        context.getContentResolver().registerContentObserver(SessionContract.PROPERTIES_URI, true, sessionContentObserver);
        context.getContentResolver().registerContentObserver(SessionContract.PROPERTIES_KEY_URI, true, sessionContentObserver);
        context.getContentResolver().registerContentObserver(SessionContract.EVENT_URI, true, sessionContentObserver);
        context.getContentResolver().registerContentObserver(SessionContract.SESSION_TRANSACTION_URI, true, sessionContentObserver);
        context.getContentResolver().registerContentObserver(SessionContract.SESSION_CUSTOMER_URI, true, sessionContentObserver);
        context.getContentResolver().registerContentObserver(SessionContract.SESSION_DISPLAY_ORDER_URI, true, sessionContentObserver);
    }

    private static void unregisterContentObserver(Context context, SessionContentObserver sessionContentObserver) {
        if (null != context && null != sessionContentObserver) {
            context.getContentResolver().unregisterContentObserver(sessionContentObserver);
        }

        if (null != sessionContentObserver) {
            sessionContentObserver.cleanupSessionConnector();
        }
    }

    /**
     * Maps method calls on the ContentObserver to the SessionConnector.
     */
    static class SessionContentObserver extends ContentObserver {
        private SessionConnector connector;

        SessionContentObserver(SessionConnector connector) {
            super(new Handler());
            this.connector = connector;
        }

        SessionConnector getConnector() {
            return connector;
        }

        void cleanupSessionConnector() {
            this.connector = null;
        }

        @Override
        public boolean deliverSelfNotifications() {
            return false;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            if (null == getConnector()) return;

            switch (SessionContract.matcher.match(uri)) {
                case SessionContract.SESSION:
                    Log.d(TAG, "Session Changed: --> " + uri.toString());
                    getConnector().onSessionDataChanged(SESSION, null);
                    break;
                case SessionContract.SESSION_CUSTOMER_INFO:
                    Log.d(TAG, "CustomerInfo Changed: --> " + uri.toString());
                    getConnector().onSessionDataChanged(CUSTOMER_INFO, getConnector().getCustomerInfo());
                    break;
                case SessionContract.SESSION_DISPLAY_ORDER:
                    Log.d(TAG, "DisplayOrder Changed: --> " + uri.toString());
                    getConnector().onSessionDataChanged(DISPLAY_ORDER, getConnector().getDisplayOrder());
                    break;
                case SessionContract.PROPERTIES:
                    Log.d(TAG, "Properties Changed: --> " + uri.toString());
                    getConnector().onSessionDataChanged(PROPERTIES, null);
                    break;
                case SessionContract.PROPERTIES_KEY:
                    String key = uri.getLastPathSegment();
                    String value = getConnector().getProperty(key);
                    Log.d(TAG, "Property key: " + key + " with value: " + value + " Changed: --> " + uri.toString());

                    JsonObject obj = new JsonObject();
                    obj.addProperty(QUERY_PARAMETER_NAME, key);
                    obj.addProperty(QUERY_PARAMETER_VALUE, value);
                    getConnector().onSessionDataChanged(PROPERTIES, obj);
                    break;
                case SessionContract.SESSION_TRANSACTION:
                    Log.d(TAG, "Transaction Changed: --> " + uri.toString());
                    getConnector().onSessionDataChanged(TRANSACTION, getConnector().getTransaction());
                    break;
                case SessionContract.EVENT:
                    String type = uri.getLastPathSegment();
                    String payload = uri.getQueryParameter(QUERY_PARAMETER_VALUE);
                    getConnector().onSessionEvent(type, payload);
                    break;
                default:
                    Log.d(TAG, "Unknown URI - Changed: --> " + uri.toString());
                    return;
            }
            super.onChange(selfChange, uri);
        }
    }
}
