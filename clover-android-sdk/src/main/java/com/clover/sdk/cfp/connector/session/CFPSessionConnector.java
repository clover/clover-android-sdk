package com.clover.sdk.cfp.connector.session;

import com.clover.sdk.v3.customers.CustomerInfo;
import com.clover.sdk.v3.order.DisplayOrder;
import com.clover.sdk.v3.payments.Transaction;

import android.content.ContentProviderClient;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.*;

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
public class CFPSessionConnector implements Serializable, CFPSessionListener {
    public static final String DISPLAY_ORDER = "com.clover.extra.DISPLAY_ORDER";
    public static final String CUSTOMER_INFO = "com.clover.extra.CUSTOMER_INFO";
    public static final String SESSION = "SESSION";
    public static final String PROPERTIES = "PROPERTIES";
    public static final String TRANSACTION = "TRANSACTION";
    public static final String MESSAGE = "MESSAGE";
    public static final String MESSAGE_DURATION = "MESSAGE_DURATION";

    public static final String QUERY_PARAMETER_VALUE = "value";
    public static final String QUERY_PARAMETER_NAME = "name";
    public static final String QUERY_PARAMETER_SRC = "src";
    public static final String BUNDLE_KEY_TYPE = "TYPE";
    public static final String BUNDLE_KEY_DATA = "DATA";
    public static final String BUNDLE_KEY_MESSAGE = "MESSAGE";
    public static final String BUNDLE_KEY_DURATION = "DURATION";
    public static final String BUNDLE_KEY_TRANSACTION = "TRANSACTION";
    private static final String EXTERNAL = "EXTERNAL";
    private static final String INTERNAL = "INTERNAL";
    protected String messageUuid;

    private static final String TAG = "SessionConnector";

    private ContentProviderClient sessionContentProviderClient;
    private WeakReference<Context> contextWeakReference;
    Set<CFPSessionListener> sessionListeners = new LinkedHashSet<>();
    private SessionContentObserver sessionContentObserver = null;
    private ContentObserver propertyContentObserver;
    public CFPSessionConnector(Context context) {
        this.contextWeakReference = new WeakReference<>(context);
        connect();
    }

    private Context getContext() {
        return (null != contextWeakReference) ? contextWeakReference.get() : null;
    }

    public boolean connect() {
        Context context = getContext();
        if (sessionContentProviderClient == null && null != context) {
            sessionContentProviderClient = context.getContentResolver().acquireContentProviderClient(CFPSessionContract.AUTHORITY);
        }
        return sessionContentProviderClient != null;
    }

    public void disconnect() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sessionContentProviderClient.close();
        }
        sessionContentProviderClient = null;
    }

    public boolean addSessionListener(CFPSessionListener listener) {
        Context context = getContext();
        if (sessionListeners.size() == 0 && null == sessionContentObserver && null != context) {
            sessionContentObserver = new SessionContentObserver(this);
            registerContentObserver(context, sessionContentObserver);
        }
        return sessionListeners.add(listener);
    }

    public boolean removeSessionListener(CFPSessionListener listener) {
        Context context = getContext();
        boolean result = sessionListeners.remove(listener);
        if (sessionListeners.size() == 0 && null != context) {
            unregisterContentObserver(context, sessionContentObserver);
            sessionContentObserver = null;
        }
        return result;
    }

    public boolean clear() {
        try {
            if (connect()) {
                sessionContentProviderClient.call(CFPSessionContract.CALL_METHOD_CLEAR_SESSION, null, null);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return true;
    }

    protected ContentProviderClient getSessionContentProviderClient() {
        return sessionContentProviderClient;
    }

    public CustomerInfo getCustomerInfo() {
        CustomerInfo customerInfo = null;
        try {
            if (connect()) {
                try (Cursor cursor = sessionContentProviderClient.query(CFPSessionContract.SESSION_CUSTOMER_URI, null, null, null, null)) {
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
                bundle.putParcelable(CFPSessionContract.COLUMN_CUSTOMER_INFO, customerInfo);
                sessionContentProviderClient.call(CFPSessionContract.CALL_METHOD_SET_CUSTOMER_INFO, null, bundle);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public DisplayOrder getDisplayOrder() {
        DisplayOrder displayOrder = null;
        try {
            if (connect()) {
                try (Cursor cursor = sessionContentProviderClient.query(CFPSessionContract.SESSION_DISPLAY_ORDER_URI, null, null, null, null)) {
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
                bundle.putParcelable(CFPSessionContract.COLUMN_DISPLAY_ORDER, displayOrder);
                bundle.putBoolean(CFPSessionContract.COLUMN_DISPLAY_ORDER_MODIFICATION_SUPPORTED, isOrderModificationSupported);
                sessionContentProviderClient.call(CFPSessionContract.CALL_METHOD_SET_ORDER, null, bundle);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public void setProperty(String key, String value) {
        try {
            if (connect()) {
                Bundle bundle = new Bundle();
                bundle.putString(CFPSessionContract.COLUMN_KEY, key);
                bundle.putString(CFPSessionContract.COLUMN_VALUE, value);
                bundle.putString(CFPSessionContract.COLUMN_SRC, EXTERNAL);
                messageUuid = UUID.randomUUID().toString();
                bundle.putString("messageUuid", messageUuid);
                sessionContentProviderClient.call(CFPSessionContract.CALL_METHOD_SET_PROPERTY, null, bundle);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public String getProperty(String key) {
        try {
            if (!connect()) return null;
            String selectionClause = CFPSessionContract.COLUMN_KEY + " = ?";
            String[] selectionArgs = {key};
            try (Cursor cursor = sessionContentProviderClient.query(CFPSessionContract.PROPERTIES_URI, null, selectionClause, selectionArgs, null)) {
                if (null != cursor && cursor.moveToFirst()) {
                    return cursor.getString(1); // 0=Key, 1=Value
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    private JSONObject getPropertyWithSrc(String key) {
        try {
            if (!connect()) return null;
            String selectionClause = CFPSessionContract.COLUMN_KEY + " = ?";
            String[] selectionArgs = {key};
            try (Cursor cursor = sessionContentProviderClient.query(CFPSessionContract.PROPERTIES_URI, null, selectionClause, selectionArgs, null)) {
                if (null != cursor && cursor.moveToFirst()) {
                    JSONObject JSONObject = new JSONObject();
                    JSONObject.put("value", cursor.getString(1));
                    JSONObject.put("src", cursor.getString(2));
                    return JSONObject;
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
            String selectionClause = CFPSessionContract.COLUMN_KEY + " = ?";
            String[] selectionArgs = {key};
            sessionContentProviderClient.delete(CFPSessionContract.PROPERTIES_URI, selectionClause, selectionArgs);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public Transaction getTransaction() {
        Transaction transaction = null;
        try {
            if (connect()) {
                try (Cursor cursor = sessionContentProviderClient.query(CFPSessionContract.SESSION_TRANSACTION_URI, null, null, null, null)) {
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
                sessionContentProviderClient.call(CFPSessionContract.CALL_METHOD_SET_TRANSACTION, null, bundle);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    public CFPMessage getMessage() {
        CFPMessage cfpMessage = null;
        try {
            if (connect()) {
                try (Cursor cursor = sessionContentProviderClient.query(CFPSessionContract.SESSION_MESSAGE_URI, null, null, null, null)) {
                    if (null != cursor && cursor.moveToFirst()) {
                        String msg = cursor.getString(0);
                        if (msg != null) {
                            cfpMessage = new CFPMessage(msg);
                        }
                        return cfpMessage;
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    public void setMessage(CFPMessage cfpMessage) {
        if (connect()) {
            try {
                Bundle bundle = new Bundle();
                bundle.putParcelable(BUNDLE_KEY_MESSAGE, cfpMessage);
                sessionContentProviderClient.call(CFPSessionContract.CALL_METHOD_SET_MESSAGE, null, bundle);
                Log.d(TAG, "Just inserted the CFPMessage object for the session");
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
                sessionContentProviderClient.call(CFPSessionContract.CALL_METHOD_ON_EVENT, null, bundle);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public void onSessionDataChanged(String type, Object data) {
        String listenerSource = contextWeakReference.get().getPackageName();
        for (CFPSessionListener listener : sessionListeners) {
            Log.d(this.getClass().getSimpleName(), "onSessionDataChanged called with type = " + type + " for " + listenerSource + " with listener " + listener.getClass().getSimpleName());

            listener.onSessionDataChanged(type, data);
        }
    }

    @Override
    public void onSessionEvent(String type, String data) {
        for (CFPSessionListener listener : sessionListeners) {
            listener.onSessionEvent(type, data);
        }
    }

    private static void registerContentObserver(Context context, SessionContentObserver sessionContentObserver) {
        if (null == context || null == sessionContentObserver) return;

        // Intentionally, not registering for SessionContract.PROPERTIES_URI and SessionContract.SESSION_URI because it triggers two event notifications
        // for every change.
        context.getContentResolver().registerContentObserver(CFPSessionContract.PROPERTIES_KEY_URI, true, sessionContentObserver);
        context.getContentResolver().registerContentObserver(CFPSessionContract.EVENT_URI, true, sessionContentObserver);
        context.getContentResolver().registerContentObserver(CFPSessionContract.SESSION_TRANSACTION_URI, true, sessionContentObserver);
        context.getContentResolver().registerContentObserver(CFPSessionContract.SESSION_CUSTOMER_URI, true, sessionContentObserver);
        context.getContentResolver().registerContentObserver(CFPSessionContract.SESSION_DISPLAY_ORDER_URI, true, sessionContentObserver);
        context.getContentResolver().registerContentObserver(CFPSessionContract.SESSION_MESSAGE_URI, true, sessionContentObserver);
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
    class SessionContentObserver extends ContentObserver {
        private CFPSessionConnector connector;
        private String lastUuid="";

        SessionContentObserver(CFPSessionConnector connector) {
            super(new Handler());
            this.connector = connector;
        }

        CFPSessionConnector getConnector() {
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

            String messageUuid = uri.getQueryParameter("messageUuid"); //this ID is to determine if this instance was the one to set the property
            //If the message has the same id, then we don't want duplicate notifications
            if ((messageUuid == null || !messageUuid.equals(lastUuid))) {
                lastUuid = messageUuid;
                switch (CFPSessionContract.matcher.match(uri)) {
                    case CFPSessionContract.SESSION:
                        getConnector().onSessionDataChanged(SESSION, null);
                        break;
                    case CFPSessionContract.SESSION_CUSTOMER_INFO:
                        getConnector().onSessionDataChanged(CUSTOMER_INFO, getConnector().getCustomerInfo());
                        break;
                    case CFPSessionContract.SESSION_DISPLAY_ORDER:
                        getConnector().onSessionDataChanged(DISPLAY_ORDER, getConnector().getDisplayOrder());
                        break;
                    case CFPSessionContract.PROPERTIES:
                        getConnector().onSessionDataChanged(PROPERTIES, null);
                        break;
                    case CFPSessionContract.PROPERTIES_KEY:
                        //We don't want to send a notification to the instance that set the property
                        if (messageUuid == null || !messageUuid.equals(getConnector().messageUuid)) {
                            String key = uri.getLastPathSegment();
                            JSONObject property = getConnector().getPropertyWithSrc(key);
                            String value = null;
                            if (property != null && property.has("value")) {
                                try {
                                    value = property.get("value").toString();
                                } catch (JSONException e) {
                                    Log.e(TAG, e.getMessage(), e);
                                }
                            }
                            String src = null;
                            if (property != null && property.has("src")) {
                                try {
                                    src = property.get("src").toString();
                                } catch (JSONException e) {
                                    Log.e(TAG, e.getMessage(), e);
                                }
                            }
                            //We don't want to send internally sourced notifications
                            if (src == null || !src.equals(INTERNAL)) {
                                JSONObject obj = new JSONObject();
                                try {
                                    obj.put(QUERY_PARAMETER_NAME, key);
                                    obj.put(QUERY_PARAMETER_VALUE, value);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                getConnector().onSessionDataChanged(PROPERTIES, obj);
                            }
                        }
                        break;
                    case CFPSessionContract.SESSION_TRANSACTION:
                        getConnector().onSessionDataChanged(TRANSACTION, getConnector().getTransaction());
                        break;
                    case CFPSessionContract.SESSION_MESSAGE:
                        getConnector().onSessionDataChanged(MESSAGE, getConnector().getMessage());
                        break;
                    case CFPSessionContract.EVENT:
                        String type = uri.getLastPathSegment();
                        String payload = uri.getQueryParameter(QUERY_PARAMETER_VALUE);
                        getConnector().onSessionEvent(type, payload);
                        break;
                    default:
                        Log.d(TAG, "Unknown URI - Changed: --> " + uri.toString());
                        return;
                }
            } else {
                Log.d(TAG, "onChange not processed for uri " + uri);
            }
            super.onChange(selfChange, uri);
        }
    }
}
