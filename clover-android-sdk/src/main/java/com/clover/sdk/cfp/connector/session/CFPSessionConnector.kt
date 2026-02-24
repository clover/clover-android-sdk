package com.clover.sdk.cfp.connector.session

import android.app.PendingIntent
import android.content.ContentProviderClient
import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.RemoteException
import android.os.SharedMemory
import android.util.Log
import androidx.annotation.WorkerThread
import com.clover.sdk.cfp.connector.session.CFPSessionContract.CONTACTLESS_PAYMENTS_CONFIG_PROPERTY
import com.clover.sdk.cfp.connector.session.CFPSessionContract.IS_KIOSK_PAY_FOR_ORDER_PROPERTY
import com.clover.sdk.v3.customers.CustomerInfo
import com.clover.sdk.v3.order.DisplayOrder
import com.clover.sdk.v3.payments.Transaction
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable
import java.lang.ref.WeakReference
import java.nio.charset.StandardCharsets
import java.util.UUID
import java.util.concurrent.Executors
import java.util.concurrent.locks.ReentrantLock
import kotlin.math.max

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
 CFPMessage

*/
@Suppress("unused")
open class CFPSessionConnector @WorkerThread constructor(context: Context?) : Serializable,
  CFPSessionListener {
  @JvmField
  var messageUuid: String? = null

  open var sessionContentProviderClient: ContentProviderClient? = null
  private val contextWeakReference: WeakReference<Context?> = WeakReference<Context?>(context)
  var sessionListeners: MutableSet<CFPSessionListener> = LinkedHashSet()
  private var sessionContentObserver: SessionContentObserver? = null
  private val propertyContentObserver: ContentObserver? = null
  private var sessionData: SessionData? = SessionData()
  private var sharedMemory: SharedMemory? = null
  private val lock = Any()
  private val gson = GsonBuilder().setLenient().create()
  private val cfpSessionConnectorExecutor = Executors.newSingleThreadExecutor { r: Runnable? ->
    Thread(
      r,
      "CFPSessionConnector-executor"
    )
  }.asCoroutineDispatcher()

  private val executorScope = CoroutineScope(cfpSessionConnectorExecutor)
    class SessionData {
    // Session table columns
    var customerInfo: String? = null
    var displayOrder: String? = null
    var displayOrderModificationSupported: String? = null
    var transaction: String? = null
    var message: String? = null

    // Properties table
    var properties: MutableMap<String?, Property?> = HashMap()
  }

  class Property internal constructor(var value: String?, var src: String?)

  /*
    Helper function to ensure background work is done on a background thread.
    We don't want to spin off a background thread if we are already on one, so
    only use the executorScope if we are running on the UI thread.
   */
  private fun runWork(block: suspend CoroutineScope.() -> Unit) {
    try {
        executorScope.launch {
          block()
        }
    } catch (e: InterruptedException) {
      Log.d(TAG, "runWork interrupted: " + e.message)
      // This exception could happen if the current background thread
      // is interrupted for any reason.  If this happens, just restore
      // the interrupted state and move on.
      Thread.currentThread().interrupt()
    } catch (e: Exception) {
      Log.e(TAG, "runWork exception: " + e.message)
    }
  }
  private fun writeToSharedMemory() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
        try {
          val json = gson.toJson(sessionData)
          val bytes = json.toByteArray(StandardCharsets.UTF_8)
          val length = bytes.size
          val totalLength = bytes.size + 4

          Log.d(TAG, "Writing to shared memory: $json, size: $length, totalLength: $totalLength")

          if (sharedMemory == null) {
            sharedMemory = SharedMemory.create(
              BUNDLE_KEY_SHARED_MEMORY, max(
                INITIAL_SHARED_MEMORY_SIZE, bytes.size * 2
              )
            )
          } else if ((sharedMemory?.getSize() ?: 0) < totalLength) {
            sharedMemory?.close()
            // Create new slightly larger to avoid frequent resizing
            sharedMemory = SharedMemory.create(
              BUNDLE_KEY_SHARED_MEMORY, max(
                INITIAL_SHARED_MEMORY_SIZE, bytes.size * 2
              )
            )
          }
          val buffer = sharedMemory?.mapReadWrite()
          buffer?.let {
            it.putInt(bytes.size) // Write the 4-byte length prefix
            it.put(bytes)
            SharedMemory.unmap(it)
          }
        } catch (e: Exception) {
          Log.e(TAG, "Error writing to shared memory", e)
        }
    }
  }

  init {
    connect()
  }

  private val context: Context?
    get() = contextWeakReference.get()

  @WorkerThread
  open fun connect(): Boolean {
    val context: Context? = this.context
    if (sessionContentProviderClient == null && null != context) {
      if (getCFPSessionContentProviderClient() != null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
          Log.d(TAG, "Calling getSharedMemory")
          try {
            val bundle = getCFPSessionContentProviderClient()?.call(
              CFPSessionContract.CALL_METHOD_GET_SHARED_MEMORY,
              null,
              null
            )
            if (bundle != null) {
              synchronized(lock) {
                this.sharedMemory = bundle.getParcelable(
                  BUNDLE_KEY_SHARED_MEMORY
                )
                refreshDataFromSharedMemory()
              }
            } else {
              Log.d(TAG, "getSharedMemory returned null bundle")
            }
          } catch (e: RemoteException) {
            throw RuntimeException(e)
          }
        }
      } else {
        Log.d(TAG, "acquireUnstableContentProviderClient returned null")
      }
    } else {
      synchronized(lock) {
        refreshDataFromSharedMemory()
      }
    }
    return sessionContentProviderClient != null
  }

  private fun getCFPSessionContentProviderClient(): ContentProviderClient? {
    if (sessionContentProviderClient == null && null != context) {
      sessionContentProviderClient = context?.contentResolver
        ?.acquireUnstableContentProviderClient(CFPSessionContract.AUTHORITY)
    }
    return sessionContentProviderClient
  }

  private fun refreshDataFromSharedMemory() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
      synchronized(lock) {
        if (sharedMemory != null) {
          //Log.d(TAG, "sharedMemory exists, so we will try to read from it")
          try {
            // Map the memory as read-only to avoid accidental corruption
            val buffer = sharedMemory?.mapReadOnly()
            buffer?.let {
              val length = buffer.getInt()
              val totalLength = sharedMemory?.getSize() ?: 0
              if (length > 0 && length <= totalLength - 4) {
                val bytes = ByteArray(length)
                buffer.get(bytes)
                // Convert to string and trim any trailing nulls or whitespace
                val json = String(bytes, StandardCharsets.UTF_8).trim { it <= ' ' }
                //Log.d(TAG, "Refreshing data from shared memory: $json")
                // Deserialize back to the in-memory object
                // This replaces the existing sessionData instance
                val newData = gson.fromJson(json, SessionData::class.java)
                if (newData != null) {
                  Log.d(
                    TAG,
                    "Refreshing SessionData from shared memory. newData: " + gson.toJson(newData)
                  )
                  // Update your local state
                  this.sessionData = newData
                }
              }
              SharedMemory.unmap(buffer)
            }
          } catch (e: Exception) {
            Log.e(TAG, "Error reading from shared memory", e)
          }
        } else {
          Log.w(
            TAG,
            "SharedMemory is null after attempting initialization from provider, so exiting"
          )
        }
      }
    }
  }

  fun disconnect() {
    Log.d(TAG, "disconnecting and cleaning up the sessionContentProviderClient for context: $context")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && sessionContentProviderClient != null) {
      getCFPSessionContentProviderClient()?.close()
    }
    sessionContentProviderClient = null
    if (sessionContentObserver != null && this.context != null) {
      unregisterContentObserver(this.context, sessionContentObserver)
    }
    sessionContentObserver = null
  }

  @WorkerThread
  fun addSessionListener(listener: CFPSessionListener?) {
    val context: Context? = this.context
    if (sessionListeners.isEmpty() && null == sessionContentObserver && null != context) {
      sessionContentObserver = SessionContentObserver(this)
      registerContentObserver(context, sessionContentObserver)
    }
    sessionListeners.add(listener!!)
  }

  @WorkerThread
  fun registerForUpdates(pi: PendingIntent?, uuid: UUID?): UUID? {
    val bundle = Bundle()
    bundle.putParcelable("PENDING_INTENT", pi)
    bundle.putSerializable("uuid", uuid)
    try {
      if (connect()) {
        val result = getCFPSessionContentProviderClient()?.call("registerForUpdates", null, bundle)
        if (result != null) {
          return result.getSerializable("uuid") as UUID?
        }
      }
    } catch (e: RemoteException) {
      Log.e(TAG, e.message, e)
    }
    return null
  }

  @WorkerThread
  fun unregisterForUpdates(uuid: UUID?): PendingIntent? {
    val bundle = Bundle()
    bundle.putSerializable("uuid", uuid)
    try {
      if (connect()) {
        val result = getCFPSessionContentProviderClient()?.call("unregisterForUpdates", null, bundle)
        if (result != null) {
          return result.getParcelable("PENDING_INTENT")
        }
      }
    } catch (e: RemoteException) {
      Log.e(TAG, e.message, e)
    }
    return null
  }

  fun removeSessionListener(listener: CFPSessionListener?): Boolean {
    val context: Context? = this.context
    val result = sessionListeners.remove(listener)
    if (sessionListeners.isEmpty() && null != context) {
      unregisterContentObserver(context, sessionContentObserver)
      sessionContentObserver = null
    }
    return result
  }

  /*
     Clears all data elements and properties associated with the current customer
     order/payment transaction or interaction, with the exception of protected properties.
     */
  @WorkerThread
  fun clear(): Boolean {
    try {
      if (connect()) {
        synchronized(lock) {
          if (sharedMemory != null && sessionData != null) {
            sessionData?.displayOrder = null
            sessionData?.transaction = null
            sessionData?.message = null
            sessionData?.customerInfo = null
            sessionData?.displayOrderModificationSupported = null
            // Save protected properties before clearing and then restore.
            val kioskPayForOrderProperty = sessionData?.properties[IS_KIOSK_PAY_FOR_ORDER_PROPERTY]
            val contactlessPaymentsConfigProperty =
              sessionData?.properties[CONTACTLESS_PAYMENTS_CONFIG_PROPERTY]
            sessionData?.properties?.clear()
            sessionData?.properties[IS_KIOSK_PAY_FOR_ORDER_PROPERTY] = kioskPayForOrderProperty
            sessionData?.properties[CONTACTLESS_PAYMENTS_CONFIG_PROPERTY] =
              contactlessPaymentsConfigProperty
            writeToSharedMemory()
          } else {
            Log.w(TAG, "SharedMemory not initialized from provider, so can't save data to it")
          }
        }
        runWork {
          getCFPSessionContentProviderClient()?.call(
            CFPSessionContract.CALL_METHOD_CLEAR_SESSION,
            null,
            null
          )
        }
      }
    } catch (e: Exception) {
      Log.e(TAG, e.message, e)
    }
    return true
  }

  /*
     Similar to clearSession with the exception that this will only clear
     the DisplayOrder, Transaction and Message data.  It leaves any CustomerInfo
     or properties in place, so that they can be utilized if needed for follow-on
     orders or payments.  Primarily, this is intended to aid with loyalty customers
     who might be logged/checked-in, but the merchant has interrupted the order build
     process and doesn't want the customer to be forced to check-in again to continue
     or start a new order.
     */
  @WorkerThread
  fun pauseSession(): Boolean {
    try {
      if (connect()) {
        synchronized(lock) {
          if (sharedMemory != null && sessionData != null) {
            sessionData?.displayOrder = null
            sessionData?.transaction = null
            sessionData?.message = null
            sessionData?.displayOrderModificationSupported = null
            writeToSharedMemory()
          } else {
            Log.w(TAG, "SharedMemory not initialized from provider, so can't save data to it")
          }
        }
        runWork {
          getCFPSessionContentProviderClient()?.call(
            CFPSessionContract.CALL_METHOD_PAUSE_SESSION,
            null,
            null
          )
        }
      }
    } catch (e: Exception) {
      Log.e(TAG, e.message, e)
    }
    return true
  }

  val displayOrderModificationSupported: Boolean
    get() {
      var orderModificationSupportedBoolean = false
      try {
        if (connect()) {
          synchronized(lock) {
            orderModificationSupportedBoolean =
              (if (this.sessionData == null || sessionData?.displayOrderModificationSupported == null) false else
                this.sessionData?.displayOrderModificationSupported) as Boolean
          }
        }
      } catch (e: Exception) {
        Log.e(TAG, e.message, e)
      }
      return orderModificationSupportedBoolean
    }

  var customerInfo: CustomerInfo?
    get() {
      var customerInfo: CustomerInfo? = null
      try {
        if (connect()) {
          customerInfo = getCustomerInfoData()
        }
      } catch (e: Exception) {
        Log.e(TAG, e.message, e)
      }
      return customerInfo
    }
    @WorkerThread
    set(customerInfo) {
      try {
        if (connect()) {
          synchronized(lock) {
            if (sharedMemory != null && sessionData != null) {
              sessionData?.customerInfo = customerInfo?.jsonObject?.toString()
              writeToSharedMemory()
            } else {
              Log.w(TAG, "SharedMemory not initialized from provider, so can't save data to it")
            }
          }
          runWork {
            val bundle = Bundle()
            bundle.putParcelable(CFPSessionContract.COLUMN_CUSTOMER_INFO, customerInfo)
            getCFPSessionContentProviderClient()?.call(
              CFPSessionContract.CALL_METHOD_SET_CUSTOMER_INFO,
              null,
              bundle
            )
          }
        }
      } catch (e: Exception) {
        Log.e(TAG, e.message, e)
      }
    }

  private fun getCustomerInfoData(): CustomerInfo? {
    if (sessionData == null) return getCustomerInfoFromProvider()
    synchronized(lock) {
      val customerInfo = sessionData?.customerInfo ?: return null
      return CustomerInfo(customerInfo)
    }
  }
  private fun getCustomerInfoFromProvider(): CustomerInfo? {
    var customerInfo: CustomerInfo? = null
    getCFPSessionContentProviderClient()?.query(
      CFPSessionContract.SESSION_DISPLAY_ORDER_URI,
      null,
      null,
      null,
      null
    ).use { cursor ->
      if (null != cursor && cursor.moveToFirst()) {
        val custInfo = cursor.getString(0)
        if (custInfo != null) {
          customerInfo = CustomerInfo(custInfo)
        }
      }
    }
    return customerInfo
  }

  val displayOrder: DisplayOrder?
    get() {
      var displayOrder: DisplayOrder? = null
      try {
        if (connect()) {
          displayOrder = getDisplayOrderData()
        }
      } catch (e: Exception) {
        Log.e(TAG, e.message, e)
      }
      return displayOrder
    }

  private fun getDisplayOrderData(): DisplayOrder? {
    if (sessionData == null) return getDisplayOrderFromProvider()
    synchronized(lock) {
      val displayOrder = sessionData?.displayOrder ?: return null
      return DisplayOrder(displayOrder)
    }
  }

  private fun getDisplayOrderFromProvider(): DisplayOrder? {
    var displayOrder: DisplayOrder? = null
    getCFPSessionContentProviderClient()?.query(
      CFPSessionContract.SESSION_DISPLAY_ORDER_URI,
      null,
      null,
      null,
      null
    ).use { cursor ->
      if (null != cursor && cursor.moveToFirst()) {
        val dispOrder = cursor.getString(0)
        if (dispOrder != null) {
          displayOrder = DisplayOrder(dispOrder)
        }
      }
    }
    return displayOrder
  }

  @WorkerThread
  fun setDisplayOrder(displayOrder: DisplayOrder?, isOrderModificationSupported: Boolean) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
      Log.d(TAG, CONNECTOR_ON_MAIN_THREAD_WARNING)
    }
    try {
      if (connect()) {
        synchronized(lock) {
          if (sharedMemory != null && sessionData != null) {
            sessionData?.displayOrder = displayOrder?.jsonObject?.toString()
            sessionData?.displayOrderModificationSupported = isOrderModificationSupported.toString()
            writeToSharedMemory()
          } else {
            Log.w(TAG, "SharedMemory not initialized from provider, so can't save data to it")
          }
        }
        runWork {
          val bundle = Bundle()
          bundle.putParcelable(CFPSessionContract.COLUMN_DISPLAY_ORDER, displayOrder)
          bundle.putBoolean(
            CFPSessionContract.COLUMN_DISPLAY_ORDER_MODIFICATION_SUPPORTED,
            isOrderModificationSupported
          )
          getCFPSessionContentProviderClient()?.call(
            CFPSessionContract.CALL_METHOD_SET_ORDER,
            null,
            bundle
          )
        }
      }
    } catch (e: Exception) {
      Log.e(TAG, e.message, e)
    }
  }

  /**
   * Used to send data from the customer device/screen to a POS device/service. If the message can be sent
   * remotely, it will be send to the POS/MFD and local listeners will NOT be notified. If there isn't a
   * remote message conduit, then listeners on the current device will be notified. This allows the same
   * call and listener to be used in both a tethered, and non-tethered, scenario.
   * @param value - A string payload of the data.
   *
   *
   * setPOSProperty? -> fires on the POS device, either remote or local?
   * setCustomerProperty?
   */
  @WorkerThread
  fun setRemoteProperty(key: String?, value: String?) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
      Log.d(TAG, CONNECTOR_ON_MAIN_THREAD_WARNING)
    }
    setPropertyProtected(key, value, EXTERNAL, CFPSessionContract.CALL_METHOD_SET_REMOTE_PROPERTY)
  }

  @WorkerThread
  open fun setProperty(key: String?, value: String?) {
    setPropertyProtected(key, value, EXTERNAL, CFPSessionContract.CALL_METHOD_SET_PROPERTY)
  }

  protected fun setPropertyProtected(key: String?, value: String?, src: String, callMethod: String) {
    try {
      if (connect()) {
        if (callMethod == CFPSessionContract.CALL_METHOD_SET_PROPERTY) {
          synchronized(lock) {
            if (sharedMemory != null && sessionData != null) {
              val sessionProperties = sessionData?.properties
              sessionProperties?.set(key, Property(value, src))
              writeToSharedMemory()
            } else {
              Log.w(TAG, "SharedMemory not initialized from provider, so can't save data to it")
            }
          }
        }

        runWork {
          val bundle = Bundle()
          bundle.putString(CFPSessionContract.COLUMN_KEY, key)
          bundle.putString(CFPSessionContract.COLUMN_VALUE, value)
          bundle.putString(CFPSessionContract.COLUMN_SRC, src)
          messageUuid = UUID.randomUUID().toString()
          bundle.putString("messageUuid", messageUuid)
          sessionContentProviderClient?.call(callMethod, null, bundle)
        }
      }
    } catch (e: Exception) {
      Log.e(TAG, e.message, e)
    }
  }

  @WorkerThread
  fun getProperty(key: String?): String? {
    if (Looper.myLooper() == Looper.getMainLooper()) {
      Log.d(TAG, CONNECTOR_ON_MAIN_THREAD_WARNING)
    }
    try {
      if (connect()) {
        return getPropertyData(key)
      }
    } catch (e: Exception) {
      Log.e(TAG, e.message, e)
    }
    return null
  }

  private fun getPropertyData(key: String?): String? {
    if (sessionData == null) return getPropertyValueFromProvider(key)
    synchronized(lock) {
      return sessionData?.properties[key]?.value
    }
  }

  private fun getPropertyValueFromProvider(key: String?): String? {
    try {
      val selectionClause = CFPSessionContract.COLUMN_KEY + " = ?"
      val selectionArgs = arrayOf(key)
      getCFPSessionContentProviderClient()?.query(
        CFPSessionContract.PROPERTIES_URI,
        null,
        selectionClause,
        selectionArgs,
        null
      ).use { cursor ->
        if (null != cursor && cursor.moveToFirst()) {
          return cursor.getString(1) // 0=Key, 1=Value
        }
      }
    } catch (e: java.lang.Exception) {
      Log.e(TAG, e.message, e)
    }
    return null
  }

  private fun getPropertyWithSrc(key: String?): JSONObject? {
    try {
      if (connect()) {
        return getPropertyWithSrcData(key)
      }
    } catch (e: Exception) {
      Log.e(TAG, e.message, e)
    }
    return null
  }

  private fun getPropertyWithSrcData(key: String?): JSONObject? {
    val localSessionData = sessionData
    val property = if (localSessionData == null) getPropertyValueWithSrcFromProvider(key) else localSessionData.properties[key]
    if (property != null) {
      val jsonObject = JSONObject()
      jsonObject.put("value", property.value)
      jsonObject.put("src", property.src)
      return jsonObject
    }
    return null
  }
  private fun getPropertyValueWithSrcFromProvider(key: String?): Property? {
    try {
      val selectionClause = CFPSessionContract.COLUMN_KEY + " = ?"
      val selectionArgs = arrayOf<String?>(key)
      sessionContentProviderClient?.query(
        CFPSessionContract.PROPERTIES_URI,
        null,
        selectionClause,
        selectionArgs,
        null
      ).use { cursor ->
        if (null != cursor && cursor.moveToFirst()) {
          val property = Property(cursor.getString(1), cursor.getString(2) )
          return property
        }
      }
    } catch (e: Exception) {
      Log.e(TAG, e.message, e);
    }
    return null

  }
  fun removeProperty(key: String?) {
    try {
      if (connect()) {
        synchronized(lock) {
          if (sessionData != null) {
            sessionData?.properties?.remove(key)
            writeToSharedMemory()
          }
        }
        runWork {
          val selectionClause = CFPSessionContract.COLUMN_KEY + " = ?"
          val selectionArgs = arrayOf(key)
          getCFPSessionContentProviderClient()?.delete(
            CFPSessionContract.PROPERTIES_URI,
            selectionClause,
            selectionArgs
          )
        }
      }
    } catch (e: Exception) {
      Log.e(TAG, e.message, e)
    }
  }

  @set:WorkerThread
  var transaction: Transaction?
    get() {
      var transaction: Transaction? = null
      try {
        if (connect()) {
          transaction = getTransactionData()
        }
      } catch (e: Exception) {
        Log.e(TAG, e.message, e)
      }
      return transaction
    }

    set(transaction) {
      if (connect()) {
        try {
          synchronized(lock) {
            if (sharedMemory != null && sessionData != null) {
              sessionData?.transaction = transaction?.jsonObject?.toString()
              writeToSharedMemory()
            } else {
              Log.w(TAG, "SharedMemory not initialized from provider, so can't save data to it")
            }
          }
          runWork {
            val bundle = Bundle()
            bundle.putParcelable(BUNDLE_KEY_TRANSACTION, transaction)
            getCFPSessionContentProviderClient()?.call(
              CFPSessionContract.CALL_METHOD_SET_TRANSACTION,
              null,
              bundle
            )
          }
        } catch (e: Exception) {
          Log.e(TAG, e.message, e)
        }
      }
    }

  private fun getTransactionData(): Transaction? {
    if (sessionData == null) return getTransactionFromProvider()
    synchronized(lock) {
      val transaction = sessionData?.transaction ?: return null
      return Transaction(transaction)
    }
  }
  private fun getTransactionFromProvider(): Transaction? {
    var transaction: Transaction? = null
    getCFPSessionContentProviderClient()?.query(
      CFPSessionContract.SESSION_TRANSACTION_URI,
      null,
      null,
      null,
      null
    ).use { cursor ->
      if (null != cursor && cursor.moveToFirst()) {
        val trans = cursor.getString(0)
        if (trans != null) {
          transaction = Transaction(trans)
        }
      }
    }
    return transaction
  }

  @set:WorkerThread
  var message: CFPMessage?
    get() {
      var cfpMessage: CFPMessage? = null
      try {
        if (connect()) {
          cfpMessage = getCFPMessageData()
        }
      } catch (e: Exception) {
        Log.e(TAG, e.message, e)
      }
      return cfpMessage
    }
    set(cfpMessage) {
      if (connect()) {
        try {
          synchronized(lock) {
            if (sharedMemory != null && sessionData != null) {
              sessionData?.message = cfpMessage?.jsonObject?.toString()
              writeToSharedMemory()
            } else {
              Log.w(TAG, "SharedMemory not initialized from provider, so can't save data to it")
            }
          }
          runWork {
            val bundle = Bundle()
            bundle.putParcelable(BUNDLE_KEY_MESSAGE, cfpMessage)
            getCFPSessionContentProviderClient()?.call(
              CFPSessionContract.CALL_METHOD_SET_MESSAGE,
              null,
              bundle
            )
          }
          Log.d(TAG, "Just inserted the CFPMessage object for the session")
        } catch (e: Exception) {
          Log.e(TAG, e.message, e)
        }
      }
    }

  private fun getCFPMessageData(): CFPMessage? {
    val localSessionData = sessionData ?: return getCFPMessageFromProvider()
    synchronized(lock) {
      val cfpMessage = sessionData?.message ?: return null
      return CFPMessage(cfpMessage)
    }
  }
  private fun getCFPMessageFromProvider(): CFPMessage? {
    var cfpMessage: CFPMessage? = null
    getCFPSessionContentProviderClient()?.query(
      CFPSessionContract.SESSION_TRANSACTION_URI,
      null,
      null,
      null,
      null
    ).use { cursor ->
      if (null != cursor && cursor.moveToFirst()) {
        val message = cursor.getString(0)
        if (message != null) {
          cfpMessage = CFPMessage(message)
        }
      }
    }
    return cfpMessage
  }

  @WorkerThread
  fun sendSessionEvent(eventType: String?, data: String?) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
      Log.d(TAG, CONNECTOR_ON_MAIN_THREAD_WARNING)
    }
    val bundle = Bundle()
    bundle.putString(BUNDLE_KEY_TYPE, eventType)
    bundle.putString(BUNDLE_KEY_DATA, data)
    try {
      if (connect()) {
        getCFPSessionContentProviderClient()?.call(CFPSessionContract.CALL_METHOD_ON_EVENT, null, bundle)
      }
    } catch (e: Exception) {
      Log.e(TAG, e.message, e)
    }
  }

  @WorkerThread
  fun sendRemoteSessionEvent(eventType: String?, data: String?) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
      Log.d(TAG, CONNECTOR_ON_MAIN_THREAD_WARNING)
    }
    val bundle = Bundle()
    bundle.putString(BUNDLE_KEY_TYPE, eventType)
    bundle.putString(BUNDLE_KEY_DATA, data)
    try {
      if (connect()) {
        getCFPSessionContentProviderClient()?.call(
          CFPSessionContract.CALL_METHOD_ON_REMOTE_EVENT,
          null,
          bundle
        )
      }
    } catch (e: Exception) {
      Log.e(TAG, e.message, e)
    }
  }

  override fun onSessionDataChanged(type: String?, data: Any?) {
    val listenerSource = contextWeakReference.get()?.packageName
    for (listener in sessionListeners) {
      Log.d(
        this.javaClass.getSimpleName(),
        "onSessionDataChanged called with type = " + type + " for " + listenerSource + " with listener " + listener.javaClass.getSimpleName()
      )
      listener.onSessionDataChanged(type, data)
    }
  }

  override fun onSessionEvent(type: String?, data: String?) {
    for (listener in sessionListeners) {
      listener.onSessionEvent(type, data)
    }
  }

  /**
   * Maps method calls on the ContentObserver to the SessionConnector.
   */
  internal class SessionContentObserver(connector: CFPSessionConnector?) : ContentObserver(
    Handler(Looper.getMainLooper())
  ) {
    private var connector: CFPSessionConnector? = null
    private val connectorLock = ReentrantLock()
    private var lastUuid: String? = ""

    init {
      connectorLock.lock()
      try {
        this.connector = connector
      } finally {
        connectorLock.unlock()
      }
    }

    fun cleanupSessionConnector() {
      connectorLock.lock()
      try {
        connector = null
      } finally {
        connectorLock.unlock()
      }
    }

    override fun onChange(selfChange: Boolean, uri: Uri?) {
      val messageUuid =
        uri?.getQueryParameter("messageUuid") //this ID is to determine if this instance was the one to set the property
      //If the message has the same id, then we don't want duplicate notifications
      if ((messageUuid == null || messageUuid != lastUuid)) {
        lastUuid = messageUuid
        val localConnector = connector //copy the reference to avoid NPE
        try {
          if (localConnector != null) {
            when (CFPSessionContract.matcher.match(uri)) {
              CFPSessionContract.SESSION -> localConnector.onSessionDataChanged(
                SESSION, null
              )

              CFPSessionContract.SESSION_CUSTOMER_INFO -> {
                val customerInfo: CustomerInfo? =
                  localConnector.customerInfo
                localConnector.onSessionDataChanged(CUSTOMER_INFO, customerInfo)
              }

              CFPSessionContract.SESSION_DISPLAY_ORDER -> localConnector.onSessionDataChanged(
                DISPLAY_ORDER,
                localConnector.displayOrder
              )

              CFPSessionContract.PROPERTIES -> localConnector.onSessionDataChanged(
                PROPERTIES, null
              )

              CFPSessionContract.PROPERTIES_KEY ->                                 //We don't want to send a notification to the instance that set the property
                if (messageUuid == null || messageUuid != localConnector.messageUuid) {
                  val key = uri?.lastPathSegment
                  val property = localConnector.getPropertyWithSrc(key)
                  var value: String? = null
                  if (property != null && property.has("value")) {
                    try {
                      value = property.get("value").toString()
                    } catch (e: JSONException) {
                      Log.e(TAG, e.message, e)
                    }
                  }
                  var src: String? = null
                  if (property != null && property.has("src")) {
                    try {
                      src = property.get("src").toString()
                    } catch (e: JSONException) {
                      Log.e(TAG, e.message, e)
                    }
                  }
                  //We don't want to send internally sourced notifications
                  if (src == null || src != INTERNAL) {
                    val obj = JSONObject()
                    try {
                      obj.put(QUERY_PARAMETER_NAME, key)
                      obj.put(QUERY_PARAMETER_VALUE, value)
                    } catch (e: JSONException) {
                      throw RuntimeException(e)
                    }
                    localConnector.onSessionDataChanged(PROPERTIES, obj)
                  }
                }

              CFPSessionContract.SESSION_TRANSACTION -> localConnector.onSessionDataChanged(
                TRANSACTION,
                localConnector.transaction
              )

              CFPSessionContract.SESSION_MESSAGE -> localConnector.onSessionDataChanged(
                MESSAGE,
                localConnector.message
              )

              CFPSessionContract.EVENT -> {
                val type = uri?.lastPathSegment
                val payload = uri?.getQueryParameter(QUERY_PARAMETER_VALUE)
                localConnector.onSessionEvent(type, payload)
              }

              else -> {
                Log.d(TAG, "Unknown URI - Changed: --> $uri")
                return
              }
            }
          }
        } catch (e: Exception) {
          Log.e(TAG, e.message, e)
        }
      } else {
        Log.d(TAG, "onChange not processed for uri $uri")
      }
      super.onChange(selfChange, uri)
    }
  }

  companion object {
    const val DISPLAY_ORDER: String = "com.clover.extra.DISPLAY_ORDER"
    const val CUSTOMER_INFO: String = "com.clover.extra.CUSTOMER_INFO"
    const val CUSTOMER_PROVIDED_DATA: String = "com.clover.extra.CUSTOMER_PROVIDED_DATA"
    const val SESSION: String = "SESSION"
    const val PROPERTIES: String = "PROPERTIES"
    const val TRANSACTION: String = "TRANSACTION"
    const val MESSAGE: String = "MESSAGE"
    const val MESSAGE_DURATION: String = "MESSAGE_DURATION"
    const val PROPERTY_KEY: String = "PROPERTY_KEY"
    const val PROPERTY_VALUE: String = "PROPERTY_VALUE"

    const val QUERY_PARAMETER_VALUE: String = "value"
    const val QUERY_PARAMETER_NAME: String = "name"
    const val QUERY_PARAMETER_SRC: String = "src"
    const val BUNDLE_KEY_TYPE: String = "TYPE"
    const val BUNDLE_KEY_DATA: String = "DATA"
    const val BUNDLE_KEY_MESSAGE: String = "MESSAGE"
    const val BUNDLE_KEY_DURATION: String = "DURATION"
    const val BUNDLE_KEY_TRANSACTION: String = "TRANSACTION"
    const val BUNDLE_KEY_CUSTOMER_INFO: String = "CUSTOMER_INFO"
    const val BUNDLE_KEY_DISPLAY_ORDER: String = "DISPLAY_ORDER"

    const val BUNDLE_KEY_SHARED_MEMORY: String = "SHARED_MEMORY"

    private const val EXTERNAL = "EXTERNAL"
    private const val INTERNAL = "INTERNAL"
    private const val CUSTOMER = "CUSTOMER"
    private const val TAG = "CFPSessionConnector"
    private const val CONNECTOR_ON_MAIN_THREAD_WARNING =
      "Connector is being invoked on the main UI thread, which might result in slow processing"

    private const val INITIAL_SHARED_MEMORY_SIZE = 1024 * 1024 // 1MB
    private fun registerContentObserver(
      context: Context?,
      sessionContentObserver: SessionContentObserver?
    ) {
      if (null == context || null == sessionContentObserver) return

      // Intentionally, not registering for SessionContract.PROPERTIES_URI and SessionContract.SESSION_URI because it triggers two event notifications
      // for every change.
      context.contentResolver.registerContentObserver(
        CFPSessionContract.PROPERTIES_KEY_URI,
        true,
        sessionContentObserver
      )
      context.contentResolver
        .registerContentObserver(CFPSessionContract.EVENT_URI, true, sessionContentObserver)
      context.contentResolver.registerContentObserver(
        CFPSessionContract.SESSION_TRANSACTION_URI,
        true,
        sessionContentObserver
      )
      context.contentResolver.registerContentObserver(
        CFPSessionContract.SESSION_CUSTOMER_URI,
        true,
        sessionContentObserver
      )
      context.contentResolver.registerContentObserver(
        CFPSessionContract.SESSION_DISPLAY_ORDER_URI,
        true,
        sessionContentObserver
      )
      context.contentResolver.registerContentObserver(
        CFPSessionContract.SESSION_MESSAGE_URI,
        true,
        sessionContentObserver
      )
    }

    private fun unregisterContentObserver(
      context: Context?,
      sessionContentObserver: SessionContentObserver?
    ) {
      if (null != context && null != sessionContentObserver) {
        context.contentResolver.unregisterContentObserver(sessionContentObserver)
      }

      sessionContentObserver?.cleanupSessionConnector()
    }
  }
}