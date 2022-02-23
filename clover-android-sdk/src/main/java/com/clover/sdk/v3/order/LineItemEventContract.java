package com.clover.sdk.v3.order;

import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;

import com.clover.sdk.v1.ResultStatus;

import java.util.Collection;

/**
 * This class defines the contract for accessing the line item events content provider.
 *
 * This content provider does not support updates or deletes.
 *
 * To insert a single line item event, use the content provider call method
 * {@link #METHOD_INSERT}, or the method
 * {@link com.clover.sdk.v3.order.LineItemEvents#insert(LineItemEvent)}.
 *
 * To insert several line item events at once, prefer the content provider call method
 * {@link #METHOD_BULK_INSERT}, or the method
 * {@link com.clover.sdk.v3.order.LineItemEvents#bulkInsert(Collection)}.
 */
public final class LineItemEventContract {
    /**
     * Content provider authority for accessing the line item events content provider.
     */
    public static final String AUTHORITY = "com.clover.line_item_events";
    /**
     * Content provider authority URI for accessing the line item events content provider.
     */
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    /**
     * Content provider call 
     * ({@link android.content.ContentResolver#call(Uri, String, String, Bundle)} method
     * to insert a {@link LineItemEvent}. 
     * 
     * This method requires that extra {@link #EXTRA_LINE_ITEM_EVENT} is included in
     * the call extras.
     * 
     * The result bundle will contain a {@link com.clover.sdk.v1.ResultStatus} at extra
     * {@link #EXTRA_RESULT_STATUS} to indicate the result of the operation.
     * 
     * Prefer to use {@link com.clover.sdk.v3.order.LineItemEvents#insert(LineItemEvent)}
     * to insert line item events.
     *
     * @see android.content.ContentResolver#call(Uri, String, String, Bundle)
     * @see LineItemEvent
     * @see ResultStatus
     * @see #EXTRA_RESULT_STATUS
     * @see #EXTRA_LINE_ITEM_EVENT
     */
    public static final String METHOD_INSERT = "insert";
    /**
     * Content provider call
     * ({@link android.content.ContentResolver#call(Uri, String, String, Bundle)} method
     * to insert an ArrayList of {@link LineItemEvent}. Only one change notification is sent when
     * the operation is successful.
     *
     * This method requires that extra {@link #EXTRA_LINE_ITEM_EVENTS} is included in
     * the call extras and is an {@code ArrayList<LineItemEvent>}.
     *
     * The result bundle will contain a {@link com.clover.sdk.v1.ResultStatus} at extra
     * {@link #EXTRA_RESULT_STATUS} to indicate the result of the operation.
     *
     * Prefer to use {@link com.clover.sdk.v3.order.LineItemEvents#bulkInsert(Collection)}
     * to insert many line item events.
     *
     * @see android.content.ContentResolver#call(Uri, String, String, Bundle)
     * @see LineItemEvent
     * @see ResultStatus
     * @see #EXTRA_RESULT_STATUS
     * @see #EXTRA_LINE_ITEM_EVENTS
     * @see #EXTRA_LINE_ITEM_EVENT_COUNT
     */
    public static final String METHOD_BULK_INSERT = "bulk_insert";
    /**
     * Content provider call 
     * ({@link android.content.ContentResolver#call(Uri, String, String, Bundle)} method
     * to retrieve a {@link LineItemEvent}. 
     *
     * This method requires that extra {@link #EXTRA_LINE_ITEM_EVENT_UUID} is included
     * in the call extras.
     *
     * In most cases fetching a single line item event by UUID won't be useful. Consider
     * using {@link android.content.ContentResolver#query(Uri, String[], String, String[], String)}
     * to obtain line item events of interest.
     *
     * Prefer to use {@link com.clover.sdk.v3.order.LineItemEvents#get(String)} to get
     * a line item event.
     * 
     * The result bundle will contain a {@link com.clover.sdk.v1.ResultStatus} at extra
     * {@link #EXTRA_RESULT_STATUS} to indicate the result of the operation, and 
     * if {@link ResultStatus#isSuccess()} returns true, a {@link LineItemEvent} at extra
     * {@link #EXTRA_LINE_ITEM_EVENT}.
     * 
     * @see android.content.ContentResolver#call(Uri, String, String, Bundle) 
     * @see LineItemEvent
     * @see ResultStatus
     * @see #EXTRA_RESULT_STATUS
     * @see #EXTRA_LINE_ITEM_EVENT_UUID
     * @see #EXTRA_LINE_ITEM_EVENT
     */
    public static final String METHOD_GET = "get";

    /**
     * A {@link LineItemEvent}.
     */
    public static final String EXTRA_LINE_ITEM_EVENT = "line_item_event";
    /**
     * An ArrayList of {@link LineItemEvent}.
     */
    public static final String EXTRA_LINE_ITEM_EVENTS = "line_item_events";
    /**
     * A return value of the number of line item events created after a bulk insert. Only valid in
     * the result bundle of a call to {@link #METHOD_BULK_INSERT}. If a call is successful, this
     * count will always be equal to the number of events in the request.
     */
    public static final String EXTRA_LINE_ITEM_EVENT_COUNT = "line_item_event_count";
    /**
     * A {@link String} that is a {@link LineItemEvent} ID. This value must be a 13-character 
     * Clover UUID.
     */
    public static final String EXTRA_LINE_ITEM_EVENT_UUID = "line_item_event_uuid";
    /**
     * A {@link ResultStatus}, the result of the operation.
     */
    public static final String EXTRA_RESULT_STATUS = "result_status";

    /**
     * Columns for the line item event content provider schema.
     */
    private interface LineItemEventColumns {
        /**
         * A {@link String}, the 13-character Clover UUID of this event.
         */
        String ID = "id";
        /**
         * A {@link String}, the 13-character Clover order UUID corresponding to this event.
         */
        String ORDER_ID = "order_id";
        /**
         * A {@link String}, the 13-character Clover line item UUID corresponding to this event.
         * If this field is null then the event is applicable to the order, but not any specific
         * line item.
         */
        String LINE_ITEM_ID = "line_item_id";
        /**
         * A {@link String}, the type of the event. This is a String and not an enum because
         * it is expected that only clients understand event types, and clients may introduce
         * new types only they understand.
         */
        String TYPE = "type";
        /**
         * A long, the time in milliseconds this event was created on the client. This is according
         * to the client's clock.
         */
        String CREATED_TIME = "created_time";
        /**
         * A long, the time in milliseconds this event was created in the cloud. This is according
         * to the server's clock.
         */
        String CLIENT_CREATED_TIME = "client_created_time";
    }

    /**
     *  The contract for accessing the line item events table in this content provider.
     */
    public static final class LineItemEvents implements BaseColumns, LineItemEventColumns {
        private LineItemEvents() {
        }

        /**
         * Base content directory (table) for line item events.
         */
        public static final String CONTENT_DIRECTORY = "line_item_events";

        /**
         * The content:// style URI for the line item events table.
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_DIRECTORY);

        /**
         * When present on a notification URI this indicates that the given item IDs
         * (database IDs, see {@link #_ID}) have changed. Use {@link Uri#getQueryParameters(String)}
         * to obtain the multiple values.
         */
        public static final String QUERY_ITEM_ID = "item_id";

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of orders.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_DIRECTORY;

        /**
         * The MIME type of a {@link #CONTENT_URI} subdirectory of a single order.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_DIRECTORY;
    }
}
