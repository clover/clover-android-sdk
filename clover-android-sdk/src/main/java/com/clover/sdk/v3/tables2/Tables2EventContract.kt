package com.clover.sdk.v3.tables2

import android.net.Uri

/**
 * Contract for tables2 EventsProvider
 *
 * Whenever any of [Tables2Events] action is performed in dining app, that event will be inserted
 * into db with necessary data. @see [Tables2EventsColumns]
 *
 * App can access these events in multiple ways:
 * - Observe for content changes in EventsProvider using content uri:
 *
 * Ex:
 * ```
 *   private val eventsObserver: ContentObserver = object : ContentObserver(null) {
 *     override fun onChange(selfChange: Boolean, uri: Uri?) {
 *       lifecycleScope.launch{
 *         val cursor = contentResolver.query(uri!!, null, null, null, null)
 *         cursor?.let {
 *           while (cursor.moveToNext()) {
 *             val eventName = cursor.getString(cursor.getColumnIndexOrThrow(Tables2EventsContract.Tables2EventsColumns.EVENT_NAME))
 *             val sourceOrderId = cursor.getString(cursor.getColumnIndexOrThrow(Tables2EventsContract.Tables2EventsColumns.SOURCE_ORDER_ID))
 *             ......
 *           }
 *         }
 *       }
 *     }
 *   }
 *
 *   override fun onCreate(savedInstanceState: Bundle?) {
 *      ...
 *      contentResolver.registerContentObserver(uri, true, eventObserver)
 *   }
 *   override fun onDestroy() {
 *     super.onDestroy()
 *     contentResolver.unregisterContentObserver(eventObserver)
 *   }
 *
 * ```
 * - Query all events from provider
 * ```
 *    val uri = Tables2EventsContract.contentUriForEvents()
 *    val cursor = contentResolver.query(uri, null, null, null, null)
 * ```
 * - Query event by id from provider
 * ```
 *    val uri = Tables2EventsContract.contentUriForEventById("5")
 *    val cursor = contentResolver.query(uri, null, null, null, null)
 * ```
 * - Query events by createdSince time
 * ```
 *    val uri = Tables2EventsContract.contentUriForEventsCreatedSince(1740690052943)
 *    val cursor = contentResolver.query(uri, null, null, null, null)
 * ```
 *
 * Detailed example of event data parsing is available in
 * clover-android-sdk-examples#DiningEventsActivity
 *
 * ###### Note that events older than 48 hours will be deleted from the database.
 *
 * @see Tables2Events
 * @see Tables2EventsColumns
 * @see android.content.ContentResolver
 * @see android.content.ContentResolver.registerContentObserver
 * @see android.content.ContentProvider.query
 */
object Tables2EventsContract {
  const val AUTHORITY = "com.clover.tables2_events"
  const val CONTENT_DIRECTORY = "events"
  val CONTENT_URI = Uri.parse("content://$AUTHORITY")

  const val CREATED_SINCE_PARAM = "createdSince"

  /**
   * Builds a content uri for querying all events
   * Ex. content://com.clover.tables2_events/events
   * */
  fun contentUriForEvents(): Uri {
    return CONTENT_URI.buildUpon().appendEncodedPath(CONTENT_DIRECTORY).build()
  }

  /**
   * Builds a content uri for querying event by id
   * Ex. content://com.clover.tables2_events/events/5
   * */
  fun contentUriForEventById(eventId: String): Uri {
    return CONTENT_URI.buildUpon()
      .appendEncodedPath(CONTENT_DIRECTORY)
      .appendEncodedPath(eventId)
      .build()
  }

  /**
   * Builds a content uri for querying all events created since give timestamp
   * Ex. content://com.clover.tables2_events/events?createdSince=<timestamp>
   * */
  fun contentUriForEventsCreatedSince(timestamp: Long): Uri {
    return CONTENT_URI.buildUpon()
      .appendEncodedPath(CONTENT_DIRECTORY)
      .appendQueryParameter(CREATED_SINCE_PARAM, timestamp.toString())
      .build()
  }

  /**
   * Columns for Tables2Events database
   * */
  object Tables2EventsColumns {

    /**
     * Unique id for this event
     * */
    const val ID = "id"

    /**
     * Name of this event as defined in [Tables2Events]
     * */
    const val EVENT_NAME = "event_name"

    /**
     * UUID of the [com.clover.sdk.v3.order.Order] on which this event is performed
     * */
    const val SOURCE_ORDER_ID = "source_order_id"

    /**
     * This column holds a json blob. Supported field names in this blob are defined in
     * [Tables2EventsData]. It can be parsed as a string from the cursor like this:
     * ```
     * val data = cursor.getString(cursor.getColumnIndexOrThrow(Tables2EventsContract.Tables2EventsColumns.DATA))
     * ```
     *
     * Once you get the json string, you can use json parser libs to extract the values:
     *
     * Ex. using Gson()
     * ```
     * val jsonObj = Gson().fromJson(data, JsonObject::class.java)
     * val destinationOrderId = jsonObj.get(Tables2EventsData.DESTINATION_ORDER_ID).asString
     * ......
     * ......
     * val sourceTableString = jsonObj.get(Tables2EventsData.SOURCE_TABLE).asString
     * val table = Table(sourceTableString)
     * ```
     *
     * @see Table
     * @see Tables2EventsData
     * */
    const val DATA = "data"

    /**
     * The time at which this event was inserted into db
     * */
    const val CREATED_TIME = "created_time"
  }

  /**
   * Defines the key names for the fields present in json blob [Tables2EventsColumns.DATA]
   * */
  object Tables2EventsData {
    const val DESTINATION_ORDER_ID = "destinationOrderId"
    /**
     * This will only be present when [Tables2Events.CREATE_GUEST_ORDER] is performed
     * */
    const val GUEST_ORDER_ID = "guestOrderId"
    const val SOURCE_TABLE = "sourceTable"
    const val DESTINATION_TABLE = "destinationTable"
    const val SOURCE_GUEST_NAME = "sourceGuestName"
    const val DESTINATION_GUEST_NAME = "destinationGuestName"
    /**
     * This will only be present when [Tables2Events.SPLIT_WHOLE_TABLE_ITEMS_BETWEEN_GUESTS] is performed
     * */
    const val DESTINATION_GUEST_LIST = "destinationGuestList"
  }
}