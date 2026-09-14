package com.clover.android.sdk.examples

import android.accounts.Account
import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context.MODE_PRIVATE
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.IInterface
import android.os.ParcelFileDescriptor
import android.os.ParcelFileDescriptor.AutoCloseOutputStream
import android.provider.BaseColumns
import android.util.Log
import androidx.core.net.toUri
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.clover.sdk.SimpleSyncClient
import com.clover.sdk.internal.util.UnstableContentResolverClient
import com.clover.sdk.util.CloverAccount
import com.clover.sdk.v1.ServiceConnector
import com.clover.sdk.v1.ServiceConnector.OnServiceConnectedListener
import com.clover.sdk.v1.printer.Printer
import com.clover.sdk.v1.printer.PrinterConnector
import com.clover.sdk.v1.printer.ReceiptContentContract
import com.clover.sdk.v1.printer.job.BalanceInquiryPrintJob
import com.clover.sdk.v1.printer.job.GiftCardPrintJob
import com.clover.sdk.v1.printer.job.PrintJob
import com.clover.sdk.v1.printer.job.StaticBillPrintJob
import com.clover.sdk.v1.printer.job.StaticCreditPrintJob
import com.clover.sdk.v1.printer.job.StaticGiftReceiptPrintJob
import com.clover.sdk.v1.printer.job.StaticLabelPrintJob
import com.clover.sdk.v1.printer.job.StaticOrderBasedPrintJob
import com.clover.sdk.v1.printer.job.StaticOrderPrintJob
import com.clover.sdk.v1.printer.job.StaticPaymentPrintJob
import com.clover.sdk.v1.printer.job.StaticRefundPrintJob
import com.clover.sdk.v1.printer.job.TextPrintJob
import com.clover.sdk.v1.printer.job.TokenRequestBasedPrintJob
import com.clover.sdk.v3.device.Device
import com.clover.sdk.v3.employees.Employee
import com.clover.sdk.v3.employees.EmployeeConnector
import com.clover.sdk.v3.merchant.LogoType
import com.clover.sdk.v3.merchant.Merchant
import com.clover.sdk.v3.merchant.MerchantDevicesV2Connector
import com.clover.sdk.v3.order.Order
import com.clover.sdk.v3.order.OrderConnector
import com.clover.sdk.v3.payments.Payment
import com.clover.sdk.v3.payments.Refund
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import androidx.core.graphics.scale

class CustomReceiptProviderTest : ContentProvider(), OnServiceConnectedListener, CoroutineScope by MainScope() {

  private var printer: Printer? = null
  private var printerConnector: PrinterConnector? = null

  private val devicesConnector by lazy { MerchantDevicesV2Connector(context) }
  private var orderConnector: OrderConnector? = null
  private var employeeConnector: EmployeeConnector? = null
  private var account: Account? = null
  private var supportedReceiptWidth: Int? = null
  private var selectedFileResId = R.drawable.test_receipt_auto_select
  private var delayedResponseBitmaps: Boolean? = false

  companion object {
    const val AUTHORITY = "com.clover.android.sdk.examples.receipt.custom"
    const val CONTENT_URI = "content://$AUTHORITY/"

    /** The name of the ID column.  */
    const val COLUMN_ID = BaseColumns._ID

    /** The name of image bitmap column.  */
    const val COLUMN_NAME = "imageBitmap"

    const val DATABASE_NAME = "receipt_data"
    const val TABLE_NAME = "receipt_bitmaps"
    const val SEGMENT_URI = "segment_uri"
    lateinit var database: AppDatabase
    const val SHARED_PREFS = "customReceiptProviderPrefs"
    const val N_CHUNKS = "nChunks"
    const val SELECTED_FILE_RES_ID = "selectedFileResId"
    const val DELAYED_RESPONSE_URIS = "delayedResponseUris"
    const val DELAYED_RESPONSE_BITMAPS = "delayedResponseBitmaps"
    const val MAX_RECEIPT_HEIGHT = 2048

    /**
     * Sentinel "selected file" value (not a real drawable id): instead of returning a canned
     * test image, generate the receipt from the print job's order, payment and merchant data
     * with [SampleReceiptGenerator].
     */
    const val SELECTED_FILE_GENERATED = 0

    const val TAG = "CRPTest"
  }

  @Entity(tableName = TABLE_NAME)
  data class ReceiptUriEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    val id: Long,
    @ColumnInfo(name = COLUMN_NAME) val imageBitmap: ByteArray
  ) {
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (javaClass != other?.javaClass) return false

      other as ReceiptUriEntity

      if (id != other.id) return false
      if (!imageBitmap.contentEquals(other.imageBitmap)) return false

      return true
    }

    override fun hashCode(): Int {
      var result = id.hashCode()
      result = 31 * result + imageBitmap.contentHashCode()
      return result
    }
  }

  @Database(entities = [ReceiptUriEntity::class], version = 1)
  abstract class AppDatabase : RoomDatabase() {
    abstract fun receiptDao(): ReceiptDao
  }

  @Dao
  interface ReceiptDao {
    @Insert
    fun insertBitmap(receiptUri: ReceiptUriEntity): Long

    @Query("SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = :id")
    fun selectById(id: Long): Cursor?
  }

  override fun query(
    uri: Uri,
    projection: Array<out String>?,
    selection: String?,
    selectionArgs: Array<out String>?,
    sortOrder: String?
  ): Cursor? {
    val cursor: Cursor? = database.receiptDao().selectById(ContentUris.parseId(uri))
    cursor?.setNotificationUri(context?.contentResolver, uri)
    return cursor
  }

  override fun insert(uri: Uri, values: ContentValues?): Uri? {
    values?.let {
      val receiptUriEntity = ReceiptUriEntity(0, values.getAsByteArray(SEGMENT_URI))
      val id = database.receiptDao().insertBitmap(receiptUriEntity)
      context?.contentResolver?.notifyChange(uri, null);
      return ContentUris.withAppendedId(uri, id);
    } ?: run {
      return null
    }
  }

  override fun update(
    uri: Uri,
    values: ContentValues?,
    selection: String?,
    selectionArgs: Array<out String>?
  ): Int {
    throw UnsupportedOperationException("Not implemented")
  }

  override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
    throw UnsupportedOperationException("Not implemented")
  }

  override fun getType(uri: Uri): String? {
    throw UnsupportedOperationException("Not implemented")
  }

  override fun onCreate(): Boolean {
    account = CloverAccount.getAccount(context)
    account?.let { connect() }

    return kotlin.runCatching {
      database = context?.let {
        Room.databaseBuilder(
          it,
          AppDatabase::class.java,
          DATABASE_NAME
        ).build()
      }!!
      true
    }.getOrElse {
      Log.e(TAG, "Exception occurred! Couldn't create database!", it)
      false
    }
  }

  @Throws(FileNotFoundException::class)
  override fun openFile(contentUri: Uri, mode: String): ParcelFileDescriptor {
    // Segments are already stored as PNG bytes, so stream them as-is. Decoding and
    // re-encoding here roughly doubles the per-segment latency for no benefit.
    val segmentBytes = if (selectedFileResId == R.drawable.test_receipt_auto_select && supportedReceiptWidth != null) {
      // WARNING: Generate the receipt bitmap with width = supportedReceiptWidth and height up to
      // CustomReceiptProviderTest.MAX_RECEIPT_HEIGHT. Instead of generating a receipt bitmap
      // matching the supportedReceiptWidth, for testing purpose this app resizes the test bitmap
      // resource to supportedReceiptWidth x supportedReceiptWidth
      getReceiptSegmentBytes(contentUri)?.let { bytes ->
        val opts = BitmapFactory.Options().apply { inPreferredConfig = Bitmap.Config.RGB_565 }
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, opts)
        val rescaled = bitmap.scale(supportedReceiptWidth!!, supportedReceiptWidth!!, false)
        ByteArrayOutputStream().also {
          rescaled.compress(Bitmap.CompressFormat.PNG, 100, it)
        }.toByteArray()
      }
    } else {
      getReceiptSegmentBytes(contentUri)
    }

    if (delayedResponseBitmaps == true) {
      runBlocking { delay(ReceiptContentContract.PROVIDER_TIMEOUT + 1000) }
    }

    return openPipeHelper(
      contentUri, "*/*", null, segmentBytes
    ) { output: ParcelFileDescriptor, uri: Uri, mimeType: String?, opts: Bundle?, bytes: ByteArray? ->
      try {
        AutoCloseOutputStream(output).use { it.write(bytes ?: ByteArray(0)) }
      } catch (e: IOException) {
        Log.d(TAG, "Receipt segment pipe closed by reader: $e")
      }
    }
  }

  private fun connect() {
    disconnect()
    if (account != null) {
      printerConnector = PrinterConnector(context, account, this).apply { connect() }
      orderConnector = OrderConnector(context, account, this).apply { connect() }
      employeeConnector = EmployeeConnector(context, account, this).apply { connect() }
    }
  }

  private fun disconnect() {
    if (printerConnector != null) {
      printerConnector?.disconnect()
      printerConnector = null
    }
    orderConnector?.disconnect()
    orderConnector = null
    employeeConnector?.disconnect()
    employeeConnector = null
  }

  private fun getReceiptSegmentBytes(contentUri: Uri): ByteArray? {
    val cursor = query(contentUri, null, null, null, null)
    var bytes: ByteArray? = null

    cursor?.let {
      it.moveToFirst()
      val columnIndex = it.getColumnIndex(COLUMN_NAME)
      if (columnIndex >= 0) {
        bytes = it.getBlob(columnIndex)
      }
      it.close()
    }

    return bytes
  }

  override fun call(method: String, arg: String?, extras: Bundle?): Bundle {
    val result = Bundle()
    val sharedPrefs = context?.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
    val nChunksToSend = sharedPrefs?.getInt(N_CHUNKS, 3) ?: 3
    val delayedResponseUris = sharedPrefs?.getBoolean(DELAYED_RESPONSE_URIS, false)
    delayedResponseBitmaps = sharedPrefs?.getBoolean(DELAYED_RESPONSE_BITMAPS, false)
    selectedFileResId = sharedPrefs?.getInt(SELECTED_FILE_RES_ID, R.drawable.test_receipt_auto_select)
      ?: R.drawable.test_receipt_auto_select

    if (method == ReceiptContentContract.METHOD_GET_RECEIPT_CONTENT_URIS) {
      extras?.let {
        // set PrintJob as classloader
        it.classLoader = PrintJob::class.java.classLoader
        var printJob: PrintJob? = null
        when (it.getParcelable<PrintJob>(ReceiptContentContract.EXTRA_PRINT_JOB)) {
          is StaticBillPrintJob -> {
            printJob =
              (it.getParcelable<StaticBillPrintJob>(ReceiptContentContract.EXTRA_PRINT_JOB)) as StaticBillPrintJob
            Log.i(TAG, "StaticBillPrintJob: $printJob")
          }

          is StaticCreditPrintJob -> {
            printJob =
              (it.getParcelable<StaticCreditPrintJob>(ReceiptContentContract.EXTRA_PRINT_JOB)) as StaticCreditPrintJob
            Log.i(TAG, "StaticCreditPrintJob: $printJob")
          }

          is StaticPaymentPrintJob -> {
            printJob =
              (it.getParcelable<StaticPaymentPrintJob>(ReceiptContentContract.EXTRA_PRINT_JOB)) as StaticPaymentPrintJob
            Log.i(TAG, "isRefundReceipt?: ${(printJob.flags and PrintJob.FLAG_REFUND) == PrintJob.FLAG_REFUND}")
            Log.i(TAG, "StaticPaymentPrintJob: $printJob")
          }

          is StaticRefundPrintJob -> {
            printJob =
              (it.getParcelable<StaticRefundPrintJob>(ReceiptContentContract.EXTRA_PRINT_JOB)) as StaticRefundPrintJob
            Log.i(TAG, "StaticRefundPrintJob: $printJob")
          }

          is StaticGiftReceiptPrintJob -> {
            printJob =
              (it.getParcelable<StaticGiftReceiptPrintJob>(ReceiptContentContract.EXTRA_PRINT_JOB)) as StaticGiftReceiptPrintJob
            Log.i(TAG, "StaticGiftReceiptPrintJob: $printJob")
          }

          is TextPrintJob -> {
            printJob =
              (it.getParcelable<TextPrintJob>(ReceiptContentContract.EXTRA_PRINT_JOB)) as TextPrintJob
            Log.i(TAG, "TextPrintJob: $printJob")
          }

          is StaticOrderPrintJob -> {
            printJob =
              (it.getParcelable<StaticOrderPrintJob>(ReceiptContentContract.EXTRA_PRINT_JOB)) as StaticOrderPrintJob
            Log.i(TAG, "StaticOrderPrintJob: $printJob")
          }

          is GiftCardPrintJob -> {
            printJob =
              (it.getParcelable<GiftCardPrintJob>(ReceiptContentContract.EXTRA_PRINT_JOB)) as GiftCardPrintJob
            Log.i(TAG, "GiftCardPrintJob: $printJob")
          }

          is BalanceInquiryPrintJob -> {
            printJob =
              (it.getParcelable<BalanceInquiryPrintJob>(ReceiptContentContract.EXTRA_PRINT_JOB)) as BalanceInquiryPrintJob
            Log.i(TAG, "BalanceInquiryPrintJob: $printJob")
          }

          is TokenRequestBasedPrintJob -> {
            printJob =
              (it.getParcelable<TokenRequestBasedPrintJob>(ReceiptContentContract.EXTRA_PRINT_JOB)) as TokenRequestBasedPrintJob
            Log.i(TAG, "TokenRequestBasedPrintJob: $printJob")
          }

          is StaticLabelPrintJob -> {
            printJob =
              (it.getParcelable<StaticLabelPrintJob>(ReceiptContentContract.EXTRA_PRINT_JOB)) as StaticLabelPrintJob
            Log.i(TAG, "StaticLabelPrintJob: $printJob")
          }
        }
        printJob?.let{
          Log.i(TAG, "Print transaction is initiated by: ${printJob.callerPackageName}")
        }
        // Parse Printer object
        printer = it.getParcelable(ReceiptContentContract.EXTRA_PRINTER)
        Log.i(TAG, "Printer: $printer")
        if (printer != null) {
          launch {
            withContext(Dispatchers.IO) {
              // Fetch supported width for given printer
              supportedReceiptWidth = printerConnector?.getPrinterTypeDetails(printer)?.numDotsWidth
              Log.i(TAG, "Printer supportedWidth: $supportedReceiptWidth")
            }
          }
        }

        val contentUris: ArrayList<Uri?> = if (selectedFileResId == SELECTED_FILE_GENERATED) {
          // Generate a real receipt (line items, tax summaries, tip, total, merchant header)
          // from the print job data instead of returning a canned test image.
          buildGeneratedReceiptUris(printJob, printer)
        } else {
          val bitmapUri = storeInCP(selectedFileResId)
          Log.d(TAG, "bitmapUri: $bitmapUri")
          ArrayList(List(nChunksToSend) { bitmapUri })
        }

        if (delayedResponseUris == true) {
          runBlocking { delay(ReceiptContentContract.PROVIDER_TIMEOUT + 1000) }
        }

        result.putParcelableArrayList(
          ReceiptContentContract.EXTRA_RECEIPT_CONTENT_URIS,
          contentUris
        )
      }
    }
    return result
  }

  /**
   * Builds receipt bitmap chunks from the [printJob]'s own data and returns their content URIs
   * in print order (header chunk first). This is the path third-party receipt apps should
   * model: extract the order/payment from the print job, fall back to the connectors for
   * anything missing, compute amounts with OrderCalc, then render.
   *
   * Runs on a binder thread, so the synchronous connector calls below are safe.
   */
  private fun buildGeneratedReceiptUris(printJob: PrintJob?, printer: Printer?): ArrayList<Uri?> {
    val uris = ArrayList<Uri?>()
    val context = context ?: return uris
    if (printJob == null) {
      Log.w(TAG, "No print job in extras, cannot generate a receipt")
      return uris
    }

    var order: Order? = null
    var payment: Payment? = null
    var refund: Refund? = null
    when (printJob) {
      is StaticPaymentPrintJob -> {
        order = printJob.order
        payment = printJob.payment
        refund = printJob.refund
      }
      is StaticRefundPrintJob -> {
        order = printJob.order
        refund = printJob.refund
      }

      is StaticOrderBasedPrintJob -> order = printJob.order
      else -> Log.w(TAG, "Unsupported print job type for generated receipts: $printJob")
    }

    // Each lookup below is a blocking IPC round-trip; run the independent ones concurrently
    // instead of paying their latencies back to back on every print.
    var employee: Employee? = null
    var merchant: Merchant? = null
    var device: Device? = null
    var receiptWidth: Int? = null
    var businessLogo: Bitmap? = null
    var receiptLogo: Bitmap? = null
    runBlocking(Dispatchers.IO) {
      val orderAndEmployee = async {
        // Prints triggered from the Transactions app send a StaticPaymentPrintJob WITHOUT the
        // order. Fetching the order by id with OrderConnector is mandatory for compatibility.
        // This is a quirk of the Transactions app's print implementation and not a general
        // requirement for third-party receipt apps, but this code shows how to do it defensively
        // just in case. The order id is available in the print job for both StaticPaymentPrintJob
        // and StaticRefundPrintJob.
        var resolvedOrder = order
        if (resolvedOrder == null) {
          val orderId = payment?.order?.id
            ?: (printJob as? StaticRefundPrintJob)?.orderId
          resolvedOrder = orderId?.let { id ->
            kotlin.runCatching { orderConnector?.getOrder(id) }
              .onFailure { Log.e(TAG, "Failed to fetch order $id", it) }
              .getOrNull()
          }
        }
        // The order only carries an employee reference; resolve it for the staff number.
        val resolvedEmployee = resolvedOrder?.employee?.id?.let { id ->
          kotlin.runCatching { employeeConnector?.getEmployee(id) }
            .onFailure { Log.e(TAG, "Failed to fetch employee $id", it) }
            .getOrNull()
        }
        resolvedOrder to resolvedEmployee
      }

      val merchantAsync = async {
        // Always fetch the merchant separately; it is never included in the print job. The v3
        // merchant carries the name/address/phone for the header plus the receipt properties
        // (merchant-configured header/footer text). Reading it requires the Clover MERCHANT_R
        // permission; without it the provider returns no data.
        kotlin.runCatching {
          val authorityUri = "content://com.clover.v3.merchant"
          val result = UnstableContentResolverClient(context.contentResolver, authorityUri.toUri())
            .call(SimpleSyncClient.METHOD_GET, null, null, null)
            .getByteArray("data")
            ?: return@runCatching null
          Merchant(String(result))
        }.onFailure { Log.e(TAG, "Failed to fetch merchant", it) }.getOrNull()
      }

      val deviceAsync = async {
        // This device's record, for the merchant-assigned device name (e.g. "reg001") on the
        // register line.
        kotlin.runCatching { devicesConnector.device }
          .onFailure { Log.e(TAG, "Failed to fetch device", it) }
          .getOrNull()
      }

      val widthAsync = async {
        // The bitmap width must match the printer's dot width. call() already kicked off this
        // lookup; only ask the connector again if it hasn't landed yet.
        supportedReceiptWidth
          ?: printer?.let { printerConnector?.getPrinterTypeDetails(it)?.numDotsWidth }
      }

      val (resolvedOrder, resolvedEmployee) = orderAndEmployee.await()
      order = resolvedOrder
      employee = resolvedEmployee
      merchant = merchantAsync.await()
      device = deviceAsync.await()
      receiptWidth = widthAsync.await()

      if (merchant == null) {
        Log.w(TAG, "Merchant object is null, cannot fetch logo.")
      }
      merchant?.let {
        Log.d(TAG, "Merchant object fetched successfully.")
        val logoSync = IntegratorLogoSync(context)
        try {
          businessLogo = logoSync.getLogo(it, LogoType.BUSINESS)
          Log.d(TAG, "Business logo fetched. Is null: ${businessLogo == null}")
          receiptLogo = logoSync.getLogo(it, LogoType.RECEIPT)
          Log.d(TAG, "Receipt logo fetched. Is null: ${receiptLogo == null}")
        } catch (e: Exception) {
          Log.e(TAG, "Error fetching logos", e)
        }
      }
    }
    if (payment == null) {
      payment = order?.payments?.firstOrNull()
    }

    val width = receiptWidth
      ?: throw IllegalStateException("Failed to get printer type details: printer=$printer, printerConnector=$printerConnector")

    val chunks = SampleReceiptGenerator(context).generateReceiptChunks(
      SampleReceiptGenerator.ReceiptParams(
        printJob = printJob,
        order = order,
        payment = payment,
        refund = refund,
        merchant = merchant,
        employee = employee,
        receiptWidth = width,
        device = device,
        businessLogo = businessLogo,
        receiptLogo = receiptLogo
      )
    )
    chunks.forEach { uris.add(storeBitmapInCP(it)) }
    Log.i(TAG, "Generated receipt: ${chunks.size} chunk(s), width=$width")
    return uris
  }

  private fun storeInCP(res: Int): Uri? {
    val b: Bitmap = BitmapFactory.decodeResource(this.context?.resources, res)
    return storeBitmapInCP(b)
  }

  private fun storeBitmapInCP(bitmap: Bitmap): Uri? {
    val values = ContentValues()

    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream)
    values.put(SEGMENT_URI, stream.toByteArray())

    return context?.contentResolver?.insert(
      Uri.parse("$CONTENT_URI$TABLE_NAME"), values
    )
  }

  override fun onServiceConnected(connector: ServiceConnector<out IInterface>?) {
    Log.i(TAG, "service connected: $connector")
  }

  override fun onServiceDisconnected(connector: ServiceConnector<out IInterface>?) {
    Log.i(TAG, "service disconnected: $connector")
  }
}
