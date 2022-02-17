package com.clover.sdk.v3.order;

import com.clover.sdk.internal.util.UnstableContentResolverClient;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ResultStatus;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.Collection;

import static com.clover.sdk.v3.order.LineItemEventContract.*;

/**
 * This class provides an interface creating and reading {@link LineItemEvent}s.
 *
 * To query {@link LineItemEvent}s
 * use {@link android.content.ContentResolver#query(Uri, String[], String, String[], String)}.
 * See {@link LineItemEventContract}.
 *
 * This class is a thin wrapper over the content provider call methods defined in
 * {@link LineItemEventContract}. Consumers should prefer this simpler interface however.
 *
 * @see LineItemEvent
 * @see LineItemEventContract
 */
public class LineItemEvents {
    private static final ResultStatus SERVICE_ERROR = new ResultStatus();
    static {
        SERVICE_ERROR.setStatus(ResultStatus.SERVICE_ERROR,
                "Failed communicating with service");
    }

    @NonNull
    private final Context context;

    public LineItemEvents(@NonNull Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * Insert a new line item event. Do not set the ID of the event. The ID will be available
     * in the returned {@link LineItemEvent}.
     *
     * @return The created {@link LineItemEvent}. The returned {@link LineItemEvent} will have its
     * UUID set.
     *
     * @throws ClientException if the event could not be inserted. Inspect
     *  {@link ClientException#getResultStatus()} to understand the reason.
     *
     * @see LineItemEventContract#METHOD_INSERT
     */
    public LineItemEvent insert(@NonNull final LineItemEvent event) throws ClientException {
        final Bundle extras = new Bundle();
        extras.putParcelable(EXTRA_LINE_ITEM_EVENT, event);
        final Bundle result = call(METHOD_INSERT, extras);
        final LineItemEvent created = result.getParcelable(EXTRA_LINE_ITEM_EVENT);
        if (created == null) {
            throw new ClientException(SERVICE_ERROR);
        }
        return created;
    }

    /**
     * Insert new line item events. Either all events are inserted, or none if a failure occurs.
     *
     * Do not set the IDs of the events as they will be generated automatically. Query the
     * ContentProvider using {@link LineItemEventContract} to get a list of LineItemEvents.
     *
     * @param events a collection of events to create.
     * @return The number of events created. This will always be equal to {@code events.size()}.
     * @throws ClientException if an event could not be inserted, in which case no events will be
     *         inserted. Inspect {@link ClientException#getResultStatus()} to understand the reason.
     *
     * @see LineItemEventContract#METHOD_BULK_INSERT
     */
    public int bulkInsert(@NonNull final Collection<LineItemEvent> events) throws ClientException {
        final Bundle extras = new Bundle();
        extras.putParcelableArrayList(EXTRA_LINE_ITEM_EVENTS, new ArrayList<>(events));
        final Bundle result = call(METHOD_BULK_INSERT, extras);
        final int count = result.getInt(EXTRA_LINE_ITEM_EVENT_COUNT, -1);
        if (count < 0) {
            throw new ClientException(SERVICE_ERROR);
        }
        return count;
    }

    private Bundle call(final String method, final Bundle extras) throws ClientException {
        final Bundle result = new UnstableContentResolverClient(context.getContentResolver(),
                AUTHORITY_URI)
                .call(method, null, extras, null);
        if (result == null) {
            throw new ClientException(SERVICE_ERROR);
        }
        result.setClassLoader(getClass().getClassLoader());
        final ResultStatus resultStatus = result.getParcelable(EXTRA_RESULT_STATUS);
        if (resultStatus == null) {
            throw new ClientException(SERVICE_ERROR);
        }
        if (!resultStatus.isSuccess()) {
            throw new ClientException(resultStatus);
        }
        return result;
    }

    /**
     * Get a single line item events by UUID. This method is package private as it is intended
     * for unit testing only. In practice, since line item events are immutable, once
     * {@link #insert(LineItemEvent)} it called and the return value obtained there is
     * no reason retrieve the record again. Consumers should prefer using
     * {@link LineItemEventContract.LineItemEvents} to query line item events using
     * {@link android.content.ContentResolver#query(Uri, String[], String, String[], String)}.
     *
     * @param uuid The 13-character Clover UUID identifying the line item event.
     *
     * @return The line item event. If the event is not found, {@link ClientException}
     * is thrown via a {@link ResultStatus} of {@link ResultStatus#NOT_FOUND}.
     *
     * @throws ClientException if the event could not be retrieved. Inspect
     *  {@link ClientException#getResultStatus()} to understand the reason.
     *
     * @see LineItemEventContract#METHOD_GET
     */
    @VisibleForTesting
    @NonNull
    LineItemEvent get(@NonNull String uuid) throws ClientException {
        final Bundle extras = new Bundle();
        extras.putString(EXTRA_LINE_ITEM_EVENT_UUID, uuid);
        final Bundle result = new UnstableContentResolverClient(context.getContentResolver(),
                AUTHORITY_URI).call(METHOD_GET, null, extras, null);
        if (result == null) {
            throw new ClientException(SERVICE_ERROR);
        }
        result.setClassLoader(getClass().getClassLoader());
        final ResultStatus resultStatus = result.getParcelable(EXTRA_RESULT_STATUS);
        if (resultStatus == null) {
            throw new ClientException(SERVICE_ERROR);
        }
        final LineItemEvent event = result.getParcelable(EXTRA_LINE_ITEM_EVENT);
        if (!resultStatus.isSuccess()) {
            throw new ClientException(resultStatus);
        }
        if (event == null) {
            // This is not expected. A success result with no event.
            throw new ClientException(SERVICE_ERROR);
        }
        return event;
    }
}
