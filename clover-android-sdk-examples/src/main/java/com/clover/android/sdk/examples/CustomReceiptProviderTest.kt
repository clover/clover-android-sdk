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
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
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
import com.clover.sdk.v1.printer.job.StaticOrderPrintJob
import com.clover.sdk.v1.printer.job.StaticPaymentPrintJob
import com.clover.sdk.v1.printer.job.StaticRefundPrintJob
import com.clover.sdk.v1.printer.job.TextPrintJob
import com.clover.sdk.v1.printer.job.TokenRequestBasedPrintJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException

class CustomReceiptProviderTest : ContentProvider(), OnServiceConnectedListener, CoroutineScope by MainScope() {

  private var printer: Printer? = null
  private var printerConnector: PrinterConnector? = null
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
    const val TAG = "CustomReceiptProviderTest"
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
    val bitmap = getReceiptSegmentBitmap(contentUri)
    val rescaledBitmap = if (selectedFileResId == R.drawable.test_receipt_auto_select && bitmap != null && supportedReceiptWidth != null) {
      // WARNING: Generate the receipt bitmap with width = supportedReceiptWidth and height up to
      // CustomReceiptProviderTest.MAX_RECEIPT_HEIGHT. Instead of generating a receipt bitmap
      // matching the supportedReceiptWidth, for testing purpose this app resizes the test bitmap
      // resource to supportedReceiptWidth x supportedReceiptWidth
      Bitmap.createScaledBitmap(bitmap, supportedReceiptWidth!!, supportedReceiptWidth!!, false)
    } else {
      getReceiptSegmentBitmap(contentUri)
    }

    if (delayedResponseBitmaps == true) {
      runBlocking { delay(ReceiptContentContract.PROVIDER_TIMEOUT + 1000) }
    }

    return openPipeHelper<Bitmap>(
      contentUri, "*/*", null, rescaledBitmap
    ) { output: ParcelFileDescriptor, uri: Uri, mimeType: String?, opts: Bundle?, args: Bitmap? ->
      try {
        AutoCloseOutputStream(output).use {
          rescaledBitmap?.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
      } catch (e: IOException) {
        e.printStackTrace()
      }
    }
  }

  private fun connect() {
    disconnect()
    if (account != null) {
      printerConnector = PrinterConnector(context, account, this)
      printerConnector?.connect()
    }
  }

  private fun disconnect() {
    if (printerConnector != null) {
      printerConnector?.disconnect()
      printerConnector = null
    }
  }

  private fun getReceiptSegmentBitmap(contentUri: Uri): Bitmap? {
    val cursor = query(contentUri, null, null, null, null)
    var bitmap: Bitmap? = null

    cursor?.let {
      it.moveToFirst()
      val bitmapData = it.getBlob(it.getColumnIndex(COLUMN_NAME))
      val opts = BitmapFactory.Options()
      opts.inPreferredConfig = Bitmap.Config.RGB_565
      bitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.size, opts)
      it.close()
    }

    return bitmap
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
        val printJob: PrintJob?
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

        val bitmapUri = storeInCP(selectedFileResId)
        Log.d(TAG, "bitmapUri: $bitmapUri")

        if (delayedResponseUris == true) {
          runBlocking { delay(ReceiptContentContract.PROVIDER_TIMEOUT + 1000) }
        }

        result.putParcelableArrayList(
          ReceiptContentContract.EXTRA_RECEIPT_CONTENT_URIS,
          ArrayList(List(nChunksToSend){bitmapUri})
        )
      }
    }
    return result
  }

  private fun storeInCP(res: Int): Uri? {
    val values = ContentValues()

    val stream = ByteArrayOutputStream()
    val b: Bitmap = BitmapFactory.decodeResource(this.context?.resources, res)
    b.compress(Bitmap.CompressFormat.PNG, 0, stream)
    val blob = stream.toByteArray()

    values.put(SEGMENT_URI, blob)

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
