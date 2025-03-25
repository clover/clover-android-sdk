package com.clover.android.sdk.examples

import android.annotation.SuppressLint
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.database.getStringOrNull
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clover.android.sdk.examples.databinding.ActivityDiningEventsBinding
import com.clover.sdk.v3.tables2.Table
import com.clover.sdk.v3.tables2.Tables2EventsContract
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DiningEventsActivity : AppCompatActivity() {

  companion object {
    val uri = Tables2EventsContract.contentUriForEvents()
    const val TAG = "DiningEventsActivity"
  }

  private lateinit var binding: ActivityDiningEventsBinding
  private lateinit var adapter: DiningEventAdapter
  val gson = GsonBuilder().setPrettyPrinting().create()

  private val eventObserver: ContentObserver = object : ContentObserver(null) {
    override fun onChange(selfChange: Boolean, uri: Uri?) {
      if (!selfChange) {
        lifecycleScope.launch {
          val cursor = contentResolver.query(uri!!, null, null, null, null)
          cursor?.let {
            while (cursor.moveToNext()) {
              val eventName = cursor.getString(cursor.getColumnIndexOrThrow(Tables2EventsContract.Tables2EventsColumns.EVENT_NAME))
              withContext(Dispatchers.Main) {
                Toast.makeText(this@DiningEventsActivity, eventName, Toast.LENGTH_LONG).show()
              }
            }
          }.also { cursor?.close() }
        }
      }
    }
  }

  @RequiresApi(Build.VERSION_CODES.M)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityDiningEventsBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.btnShowAllEvents.setOnClickListener {
      lifecycleScope.launch {
        val cursor = contentResolver.query(uri!!, null, null, null, null)
        withContext(Dispatchers.Main) {
          cursor?.let {
            adapter.updateData(getDiningEventsFromCursor(it))
            Toast.makeText(this@DiningEventsActivity, "Data Updated!", Toast.LENGTH_SHORT).show()
          }.also { cursor?.close() }
        }
      }
    }

    binding.btnShowAllEventsSince.setOnClickListener {
      val createdSinceUri = Tables2EventsContract.contentUriForEventsCreatedSince(
        dateTimeToMilliseconds(
          binding.datePicker.year,
          binding.datePicker.month,
          binding.datePicker.dayOfMonth,
          binding.timePicker.hour,
          binding.timePicker.minute
        )
      )
      lifecycleScope.launch {
        val cursor = contentResolver.query(createdSinceUri, null, null, null, null)
        withContext(Dispatchers.Main) {
          cursor?.let {
            adapter.updateData(getDiningEventsFromCursor(it))
            Toast.makeText(this@DiningEventsActivity, "Data Updated!", Toast.LENGTH_SHORT).show()
          }.also { cursor?.close() }
        }
      }
    }

    val recyclerView = binding.rvEvents
    recyclerView.layoutManager = LinearLayoutManager(this)
    adapter = DiningEventAdapter()
    recyclerView.adapter = adapter

    contentResolver.registerContentObserver(uri, true, eventObserver)
  }

  override fun onDestroy() {
    super.onDestroy()
    contentResolver.unregisterContentObserver(eventObserver)
  }

  data class DiningEvent(
    val id: Int,
    val eventName: String,
    val sourceOrderId: String,
    val data: String?,
    val createdTime: String
  )

  private fun getDiningEventsFromCursor(cursor: Cursor): List<String> {
    val events = mutableListOf<String>()
    while (cursor.moveToNext()) {
      val event = DiningEvent(
        id = cursor.getInt(cursor.getColumnIndexOrThrow(Tables2EventsContract.Tables2EventsColumns.ID)),
        eventName = cursor.getString(cursor.getColumnIndexOrThrow(Tables2EventsContract.Tables2EventsColumns.EVENT_NAME)),
        sourceOrderId = cursor.getString(cursor.getColumnIndexOrThrow(Tables2EventsContract.Tables2EventsColumns.SOURCE_ORDER_ID)),
        data = cursor.getStringOrNull(cursor.getColumnIndexOrThrow(Tables2EventsContract.Tables2EventsColumns.DATA)),
        createdTime = millisecondsToEstString(cursor.getString(cursor.getColumnIndexOrThrow(Tables2EventsContract.Tables2EventsColumns.CREATED_TIME)).toLong()),
      )

      //Example on how to parse the data
      parseData(event)

      val eventJson = gson.toJson(event)
      events.add(eventJson)
    }

    return events
  }

  // Demonstrates the event data parsing
  private fun parseData(event: DiningEvent) {
    Log.i(TAG, "-------------------------- Dining Event: ${event.eventName} --------------------------")
    Log.i(TAG, "Event Id: ${event.id}")
    Log.i(TAG, "Event Name: ${event.eventName}")
    Log.i(TAG, "Source order id: ${event.sourceOrderId}")

    if(!event.data.isNullOrEmpty()) {
      val jsonObj = gson.fromJson(event.data, JsonObject::class.java)

      val destinationOrderId = jsonObj?.get(Tables2EventsContract.Tables2EventsData.DESTINATION_ORDER_ID)?.asString
      Log.i(TAG, "Destination order id: $destinationOrderId")
      val guestOrderId = jsonObj?.get(Tables2EventsContract.Tables2EventsData.GUEST_ORDER_ID)?.asString
      Log.i(TAG, "Guest order id: $guestOrderId")
      jsonObj?.get(Tables2EventsContract.Tables2EventsData.SOURCE_TABLE)?.let {
        val srcTable = Table(it.asString)
        Log.i(TAG, "Source Table Name: ${srcTable.name}")
      }
      jsonObj?.get(Tables2EventsContract.Tables2EventsData.DESTINATION_TABLE)?.let {
        val destTable = Table(it.asString)
        Log.i(TAG, "Destination Table Name: ${destTable.name}")
      }
      val sourceGuestName = jsonObj?.get(Tables2EventsContract.Tables2EventsData.SOURCE_GUEST_NAME)?.asString
      Log.i(TAG, "Source guest name: $sourceGuestName")
      val destinationGuestName = jsonObj?.get(Tables2EventsContract.Tables2EventsData.DESTINATION_GUEST_NAME)?.asString
      Log.i(TAG, "Destination guest name: $destinationGuestName")
      val destinationGuestList = jsonObj?.get(Tables2EventsContract.Tables2EventsData.DESTINATION_GUEST_LIST)?.asString
      Log.i(TAG, "Destination guest list: $destinationGuestList")
    }

    Log.i(TAG, "Event created time: ${event.createdTime}")
  }

  private fun millisecondsToEstString(milliseconds: Long): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US)
    val date = Date(milliseconds)
    return dateFormat.format(date)
  }

  private fun dateTimeToMilliseconds(year: Int, month: Int, day: Int, hour: Int, minute: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.set(year, month, day, hour, minute)
    return calendar.timeInMillis
  }

  class DiningEventAdapter() : RecyclerView.Adapter<DiningEventAdapter.ViewHolder>() {

    private val eventList = mutableListOf<String>()

    class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val textView = LayoutInflater.from(parent.context)
        .inflate(android.R.layout.simple_list_item_1, parent, false) as TextView
      return ViewHolder(textView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      holder.textView.text = eventList[position]
    }

    override fun getItemCount() = eventList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newEvents: List<String>) {
      eventList.clear()
      eventList.addAll(newEvents)
      notifyDataSetChanged()
    }
  }
}
