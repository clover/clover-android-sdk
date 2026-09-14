package com.clover.android.sdk.examples

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.clover.sdk.v1.printer.job.PrintJob
import com.clover.sdk.v3.device.Device
import com.clover.sdk.v3.employees.Employee
import com.clover.sdk.v3.merchant.Merchant
import com.clover.sdk.v3.order.LineItem
import com.clover.sdk.v3.order.Order
import com.clover.sdk.v3.order.OrderCalc
import com.clover.sdk.v3.payments.Payment
import com.clover.sdk.v3.payments.Refund
import org.json.JSONObject
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Currency
import java.util.Date
import java.util.Locale
import kotlin.math.min
import kotlin.math.pow

/**
 * Generates printable receipt bitmaps from real order, payment and merchant data instead of
 * canned test images.
 *
 * This class intentionally mirrors how the native Clover receipt engine renders receipts:
 * receipt data is computed up front (totals, tax summaries, tips via [OrderCalc]), each receipt
 * section is generated as an Android [View] element, the sections are stacked in a vertical
 * [LinearLayout], and the laid-out view is drawn to one or more [Bitmap] chunks no taller than
 * [CustomReceiptProviderTest.MAX_RECEIPT_HEIGHT] pixels, at exactly the printer's dot width.
 *
 * Receipt data elements demonstrated here, and where each one comes from:
 *
 *  - Store name: v3 [Merchant.getName], fetched with `MerchantV3SyncClient.getMerchant()`
 *    (requires the Clover `MERCHANT_R` permission).
 *  - Store address / phone number: [Merchant.getAddress] / [Merchant.getPhoneNumber].
 *    (Store logo and tax registration number (TIN) are not yet exposed through the SDK
 *    merchant objects.)
 *  - Receipt header and footer comments: the merchant's
 *    `MerchantProperties.getReceiptProperties()` JSON (keys `storeHeadline` and
 *    `customFooter`).
 *  - Date and time: [Payment.getCreatedTime] for a sale, [Refund.getCreatedTime] for a refund,
 *    with [Order.getCreatedTime] used for the original order/accounting date.
 *  - Staff number: [Order.getEmployee] is a reference; resolve it with
 *    `EmployeeConnector.getEmployee(id)`. [Employee.getCustomId] is the best fit for a short
 *    numeric staff number; fall back to nickname/name.
 *  - Slip name / order number: [Order.getId]; order pickup number: [Order.getTitle] (only
 *    populated when the merchant has order titles/rolling order numbers configured).
 *  - Transaction number: [Payment.getId].
 *  - Item names and prices: [Order.getLineItems], [LineItem.getName], [LineItem.getPrice],
 *    [LineItem.getUnitQty] (unit quantity is stored in thousandths).
 *  - Reduced tax rate indicator: inspect [LineItem.getTaxRates]; rates are encoded as
 *    1% == 100,000. Items taxed at [REDUCED_TAX_RATE] are prefixed with
 *    [REDUCED_TAX_MARKER].
 *  - Total amount: [OrderCalc.getTotal] — handles discounts, service charges and both
 *    tax-inclusive (VAT) and tax-exclusive merchants.
 *  - Total tax amount: [OrderCalc.getTax].
 *  - Tax summaries (per-rate net subtotal and tax, e.g. a reduced vs standard rate split):
 *    [OrderCalc.getTaxSummaries]. For partial payments use the overload that takes a split
 *    percent.
 *  - Tip: [Payment.getTipAmount] (or [OrderCalc.getTip] across all payments).
 *  - Payment method: [Payment.getTender] label plus card type/last four from
 *    [Payment.getCardTransaction].
 *  - Receipt type / flags: [PrintJob.flags] (reprint, void, refund, customer vs merchant copy,
 *    bill).
 *  - Device: the human-friendly device name (e.g. "reg001") from [Device.getName], fetched
 *    with `MerchantDevicesV2Connector.getDevice()`; falls back to [Device.getSerial].
 *  - Barcode: not rendered here; encode [Payment.getId] or [Order.getId] with any barcode
 *    library and add the resulting bitmap as another section view.
 *  - Total customers (guest count): count of distinct non-empty [LineItem.getBinName] values
 *    across the order's line items. Each unique binName represents one guest/seat; computed
 *    with [getGuestCountByBinName] and rendered near the top of a sale receipt.
 */
class SampleReceiptGenerator(private val context: Context) {

  /**
   * Everything the generator needs. [order] should already be hydrated — when a print job
   * arrives without an embedded order (e.g. reprints from the Transactions app), fetch it with
   * `OrderConnector.getOrder(orderId)` before calling [generateReceiptChunks].
   */
  data class ReceiptParams(
    val printJob: PrintJob,
    val order: Order?,
    val payment: Payment?,
    val refund: Refund?,
    val merchant: Merchant?,
    val employee: Employee?,
    val receiptWidth: Int,
    val device: Device? = null,
    val businessLogo: Bitmap? = null,
    val receiptLogo: Bitmap? = null
  )

  companion object {
    const val TAG = "SampleReceiptGenerator"

    /**
     * Clover adds a taxtype to each line item tax rate; for a standard tax rate item, the label is
     */
    const val STANDARD_TAX_RATE_LABEL_KEY= "com.clover.tax.rate.standard"

    /**
     *  Clover adds a taxType to each line item tax rate; for a reduced tax rate item, the taxType is
     */
    const val REDUCED_TAX_RATE_LABEL_KEY = "com.clover.tax.rate.reduced"

    /**
     * Clover adds a taxType to each line item tax rate; for a no-tax tax rate item, the taxType is
     */
    const val NO_TAX_RATE_TAX_TYPE_LABEL_KEY = "com.clover.tax.rate.no_tax"

    /** Marker printed in front of items taxed at the reduced rate. */
    const val REDUCED_TAX_MARKER = "※"
    private const val RATE_PER_PERCENT = 100_000.0
    private const val TEXT_SIZE_SMALL = 20f
    private const val TEXT_SIZE_MEDIUM = 28f
    private const val TEXT_SIZE_LARGE = 36f
  }

  /**
   * A display group of receipt lines that are identical except for quantity. Quantities use
   * Clover's thousandths representation so both separate rows and unitQty are accumulated.
   */
  internal data class GroupedLineItem(
    val lineItem: LineItem,
    var quantityThousandths: Long
  )

  private data class LineItemGroupKey(
    val itemIdentity: String,
    val displayName: String?,
    val alternateName: String?,
    val price: Long?,
    val binName: String?,
    val refunded: Boolean?,
    val modifications: List<String>,
    val discounts: List<String>,
    val taxRates: List<String>
  )

  private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

  /**
   * Builds the receipt view from [params] and renders it to RGB_565 bitmap chunks of at most
   * [maxChunkHeight] pixels, in print order (header chunk first).
   */
  fun generateReceiptChunks(
    params: ReceiptParams,
    maxChunkHeight: Int = CustomReceiptProviderTest.MAX_RECEIPT_HEIGHT
  ): List<Bitmap> {
    val receiptView = generateReceiptView(params)
    return renderToChunks(receiptView, params.receiptWidth, maxChunkHeight)
  }

  /**
   * Stacks receipt sections in the same top-to-bottom order as the SMCC receipt layout/questions.
   * Fields owned by Stera GW are intentionally omitted because they are not available from the
   * Clover objects passed to this sample.
   */
  fun generateReceiptView(params: ReceiptParams): View {
    val orderCalc = params.order?.let { OrderCalc(it) }
    val root = LinearLayout(context).apply {
      orientation = LinearLayout.VERTICAL
      setBackgroundColor(Color.WHITE)
      val pad = params.receiptWidth / 24
      setPadding(pad, pad, pad, pad)
    }

    val isRefund = params.refund != null ||
      params.printJob.flags and PrintJob.FLAG_REFUND == PrintJob.FLAG_REFUND

    val sections = mutableListOf<View?>()
    sections += generateHeaderLogoView(params)
    if (!isRefund) {
      sections += generateSlipAndCustomerView(params)
    }
    sections += generateMerchantIdentityView(params)
    if (isRefund) {
      sections += generateFlagBannerView(params)
      sections += generateHeaderCommentView(params)
    } else {
      sections += generateHeaderCommentView(params)
      sections += generateFlagBannerView(params)
    }
    sections += generateTransactionDatesView(params, isRefund)
    sections += generateLineItemsView(params)
    sections += generateTotalsView(params, orderCalc)
    sections += generateTotalItemCountView(params)
    sections += generateTenderView(params)
    sections += generateFooterView(params)
    sections += generateReducedTaxNoteView(params)
    sections += generateMerchantAndTerminalInfoView(params)
    sections += generateTransactionIdentifierView(params)
    if (!isRefund) {
      sections += generateOrderNumberView(params)
    }

    sections.filterNotNull().forEachIndexed { index, section ->
      if (index > 0) root.addView(divider(params))
      root.addView(section)
    }
    return root
  }

  private fun generateHeaderLogoView(params: ReceiptParams): View {
    val logoBitmap = params.businessLogo ?: params.receiptLogo
    Log.d(TAG, "generateHeaderLogoView: businessLogo is null: ${params.businessLogo == null}, receiptLogo is null: ${params.receiptLogo == null}")
    return verticalSection(params) {
      logoBitmap?.let {
        Log.d(TAG, "Logo bitmap is not null, adding to receipt view.")
        val imageView = android.widget.ImageView(context)
        imageView.setImageBitmap(it)
        imageView.layoutParams = LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.MATCH_PARENT,
          LinearLayout.LayoutParams.WRAP_CONTENT
        )
        (imageView.layoutParams as LinearLayout.LayoutParams).gravity = Gravity.CENTER_HORIZONTAL
        addView(imageView)
      } ?: run {
        Log.d(TAG, "Logo bitmap is null, not adding to receipt view.")
      }
    }
  }

  /** Slip/order ID followed by the guest count, matching the first SMCC receipt rows. */
  private fun generateSlipAndCustomerView(params: ReceiptParams): View {
    val totalGuests = getGuestCountByBinName(params.order)
    return verticalSection(params) {
      params.order?.id?.let { addView(row("Order", it, params)) }
      addView(
        row(
          label = "Total customers",
          value = totalGuests.toString(),
          params = params,
          bold = true
        )
      )
    }
  }

  /** Receipt type banner derived from [PrintJob.flags]: reprint, void, refund, bill, copy. */
  private fun generateFlagBannerView(params: ReceiptParams): View? {
    val flags = params.printJob.flags
    val labels = mutableListOf<String>()
    if (flags and PrintJob.FLAG_REPRINT == PrintJob.FLAG_REPRINT) labels += "*** REPRINT ***"
    if (flags and PrintJob.FLAG_PRINT_VOID_RECEIPT == PrintJob.FLAG_PRINT_VOID_RECEIPT) labels += "*** VOIDED ***"
    if (flags and PrintJob.FLAG_REFUND == PrintJob.FLAG_REFUND || params.refund != null) labels += "*** REFUND ***"
    if (flags and PrintJob.FLAG_BILL == PrintJob.FLAG_BILL) labels += "BILL — NOT A RECEIPT"
    if (flags and PrintJob.FLAG_MERCHANT == PrintJob.FLAG_MERCHANT) labels += "MERCHANT COPY"
    if (flags and PrintJob.FLAG_CUSTOMER == PrintJob.FLAG_CUSTOMER) labels += "CUSTOMER COPY"
    if (labels.isEmpty()) return null

    return verticalSection(params) {
      labels.forEach { addView(centeredText(it, params, bold = true)) }
    }
  }

  /** Store name, address and phone from the v3 [Merchant] object. */
  private fun generateMerchantIdentityView(params: ReceiptParams): View {
    val merchant = params.merchant
    return verticalSection(params) {
      addView(centeredText(merchant?.name ?: "Merchant name unavailable", params, bold = true, sizePx = TEXT_SIZE_LARGE))
      merchant?.address?.let { address ->
        listOfNotNull(
          address.address1,
          address.address2,
          listOfNotNull(address.city, address.state, address.zip)
            .filter { it.isNotEmpty() }
            .joinToString(" ")
        )
          .filter { it.isNotEmpty() }
          .forEach { addView(centeredText(it, params)) }
      }
      merchant?.phoneNumber?.takeIf { it.isNotEmpty() }?.let {
        addView(centeredText(it, params))
      }
    }
  }

  /** Merchant-configured receipt header text, placed after merchant identity. */
  private fun generateHeaderCommentView(params: ReceiptParams): View? {
    val headline = receiptProperty(params, "storeHeadline") ?: return null
    return verticalSection(params) { addView(centeredText(headline, params)) }
  }

  /** Sale date, or refund operation date followed by the original accounting date. */
  private fun generateTransactionDatesView(params: ReceiptParams, isRefund: Boolean): View? {
    val section = verticalSection(params) {
      if (isRefund) {
        params.refund?.createdTime?.let {
          addView(row("Refund date", dateFormat.format(Date(it)), params))
        }
        params.order?.createdTime?.let {
          addView(row("Original date", dateFormat.format(Date(it)), params))
        }
      } else {
        val createdTime = params.payment?.createdTime ?: params.order?.createdTime
        createdTime?.let { addView(row("Date", dateFormat.format(Date(it)), params)) }
      }
    }
    return section.takeIf { it.childCount > 0 }
  }

  /** One row per identical-item group, with combined quantity, tax marker, name and price. */
  private fun generateLineItemsView(params: ReceiptParams): View {
    val lineItems = params.order?.lineItems
    return verticalSection(params) {
      if (lineItems.isNullOrEmpty()) {
        addView(centeredText("(no line items)", params))
        return@verticalSection
      }
      groupLineItems(lineItems).forEach { group ->
        val line = group.lineItem
        val marker = if (isReducedTaxRate(line)) "$REDUCED_TAX_MARKER " else ""
        val name = line.name ?: line.alternateName ?: "(unnamed item)"
        val qty = group.quantityThousandths / 1000.0
        val qtyLabel = if (qty != 1.0) "${trimQty(qty)} x " else ""
        addView(row("$qtyLabel$marker$name", formatAmount(line.price, params.merchant), params))
      }
    }
  }

  /**
   * Subtotal, service charge, per-rate tax summaries, total, tip — all computed with
   * [OrderCalc] so discounts and tax-inclusive (VAT) pricing are handled the same way the
   * native receipt engine handles them.
   */
  private fun generateTotalsView(params: ReceiptParams, orderCalc: OrderCalc?): View {
    val lineItems = params.order?.lineItems
    return verticalSection(params) {
      if (orderCalc == null || lineItems == null) {
        addView(centeredText("(order data unavailable)", params))
        return@verticalSection
      }

      addView(row("Subtotal", formatAmount(orderCalc.getLineSubtotal(lineItems), params.merchant), params))

      val discounted = orderCalc.getDiscountedSubtotal(lineItems)
      val undiscounted = orderCalc.getLineSubtotal(lineItems)
      if (discounted != undiscounted) {
        addView(row("Discounts", formatAmount(discounted - undiscounted, params.merchant), params))
      }

      val serviceCharge = orderCalc.getServiceCharge(lineItems)
      if (serviceCharge > 0) {
        val name = params.order?.serviceCharge?.name ?: "Service charge"
        addView(row(name, formatAmount(serviceCharge, params.merchant), params))
      }

      // Per-rate tax summaries: one net/tax pair per distinct tax rate on the order.
      // summary.net is the taxable subtotal at that rate, summary.tax the tax collected.
      orderCalc.getTaxSummaries(lineItems).forEach { summary ->
        val ratePercent = (summary.taxRate.getRateAsLong() ?: 0L) / RATE_PER_PERCENT
        val rateLabel = "${summary.taxRate.getName()} (${trimQty(ratePercent)}%)"
        addView(row("Net $rateLabel", formatAmount(summary.net.cents, params.merchant), params))
        addView(row("Tax $rateLabel", formatAmount(summary.tax.cents, params.merchant), params))
      }
      addView(row("Total tax", formatAmount(orderCalc.tax, params.merchant), params))
      addView(row("Total", formatAmount(orderCalc.getTotal(lineItems), params.merchant), params, bold = true, sizePx = TEXT_SIZE_LARGE))

      val tip = params.payment?.tipAmount ?: orderCalc.tip
      if (tip > 0) {
        addView(row("Tip", formatAmount(tip, params.merchant), params))
        addView(row("Total + tip", formatAmount(orderCalc.getTotal(lineItems) + tip, params.merchant), params, bold = true))
      }
    }
  }

  /** Total quantity follows totals in the SMCC layout; unitQty is stored in thousandths. */
  private fun generateTotalItemCountView(params: ReceiptParams): View? {
    val lineItems = params.order?.lineItems
    if (lineItems.isNullOrEmpty()) return null
    val totalQuantity = groupLineItems(lineItems).sumOf { it.quantityThousandths }
    return verticalSection(params) {
      addView(row("Total items", trimQty(totalQuantity / 1000.0), params, bold = true))
    }
  }

  /**
   * Groups line items using the SMCC data-model rules. Item identity, price, modifiers and
   * discounts must match. Tax treatment, guest/bin and refunded state are also included so rows
   * that require different receipt labels are never merged.
   */
  internal fun groupLineItems(lineItems: List<LineItem>): List<GroupedLineItem> {
    val groups = linkedMapOf<LineItemGroupKey, GroupedLineItem>()
    lineItems.forEach { line ->
      val key = lineItemGroupKey(line)
      val quantity = line.unitQty?.toLong() ?: 1_000L
      val existing = groups[key]
      if (existing == null) {
        groups[key] = GroupedLineItem(line, quantity)
      } else {
        existing.quantityThousandths += quantity
      }
    }
    return groups.values.toList()
  }

  private fun lineItemGroupKey(line: LineItem): LineItemGroupKey {
    val itemIdentity = line.item?.id?.let { "item:$it" }
      ?: "custom:${line.name.orEmpty()}:${line.alternateName.orEmpty()}"
    val modifications = line.modifications.orEmpty().map { modification ->
      listOf(
        modification.id,
        modification.name,
        modification.alternateName,
        modification.amount?.toString()
      ).joinToString("|") { it.orEmpty() }
    }.sorted()
    val discounts = line.discounts.orEmpty().map { discount ->
      listOf(
        discount.id,
        discount.name,
        discount.amount?.toString(),
        discount.percentage?.toString()
      ).joinToString("|") { it.orEmpty() }
    }.sorted()
    val taxRates = line.taxRates.orEmpty().map { taxRate ->
      listOf(
        taxRate.id,
        taxRate.name,
        taxRate.rate?.toString(),
        taxRate.systemTaxRate?.labelKey
      ).joinToString("|") { it.orEmpty() }
    }.sorted()
    return LineItemGroupKey(
      itemIdentity = itemIdentity,
      displayName = line.name,
      alternateName = line.alternateName,
      price = line.price,
      binName = line.binName,
      refunded = line.refunded,
      modifications = modifications,
      discounts = discounts,
      taxRates = taxRates
    )
  }

  /** Payment method, card details, amount paid and transaction/refund identifiers. */
  private fun generateTenderView(params: ReceiptParams): View? {
    if (params.payment == null && params.refund == null) return null
    return verticalSection(params) {
      params.payment?.let { payment ->
        val tenderLabel = payment.tender?.label ?: "Payment"
        addView(row(tenderLabel, formatAmount(payment.amount, params.merchant), params))
        payment.cardTransaction?.let { card ->
          val cardLabel = listOfNotNull(card.cardType?.name, card.last4?.let { "****$it" })
            .joinToString(" ")
          if (cardLabel.isNotEmpty()) addView(row(cardLabel, null, params))
          card.authCode?.let { addView(row("Auth code", it, params)) }
        }
        payment.result?.let { addView(row("Result", it.name, params)) }
      }
      params.refund?.let { refund ->
        addView(row("Refund", formatAmount(refund.amount, params.merchant), params))
      }
    }
  }

  private fun generateFooterView(params: ReceiptParams): View {
    return verticalSection(params) {
      // Merchant-configured receipt footer text from the v3 merchant's receipt properties.
      val footer = receiptProperty(params, "customFooter") ?: "Thank you!"
      addView(centeredText(footer, params))
      addView(centeredText("Printed by ${params.printJob.callerPackageName ?: context.packageName}", params, sizePx = TEXT_SIZE_SMALL))
    }
  }

  /** Reduced-rate legend follows the configured footer in the SMCC layout. */
  private fun generateReducedTaxNoteView(params: ReceiptParams): View? {
    if (params.order?.lineItems?.any(::isReducedTaxRate) != true) return null
    return verticalSection(params) {
      addView(centeredText("$REDUCED_TAX_MARKER Reduced tax rate item", params))
    }
  }

  /** Tax registration, register and staff fields appear near the bottom of the SMCC layout. */
  private fun generateMerchantAndTerminalInfoView(params: ReceiptParams): View? {
    val section = verticalSection(params) {
      getBusinessRegistrationNumber(params).takeIf { it.isNotEmpty() }?.let {
        addView(row("Registration", it, params))
      }
      params.device?.let { device ->
        (device.name ?: device.serial)?.let { addView(row("Register", it, params)) }
      }
      params.employee?.let { employee ->
        val staffNumber = employee.customId ?: employee.nickname ?: employee.name
        staffNumber?.let { addView(row("Staff", it, params)) }
      }
    }
    return section.takeIf { it.childCount > 0 }
  }

  /** Transaction number is near the bottom rather than inside the tender block. */
  private fun generateTransactionIdentifierView(params: ReceiptParams): View? {
    val section = verticalSection(params) {
      params.payment?.id?.let { addView(row("Transaction", it, params)) }
      params.refund?.id?.let { addView(row("Refund ID", it, params)) }
    }
    return section.takeIf { it.childCount > 0 }
  }

  /** Order title is the SMCC call/order number printed at the end of a sale receipt. */
  private fun generateOrderNumberView(params: ReceiptParams): View? {
    val orderNumber = params.order?.title?.takeIf { it.isNotEmpty() } ?: return null
    return verticalSection(params) {
      addView(row("Order No.", orderNumber, params))
    }
  }

  /**
   * Reads one key from the merchant's receipt properties — a JSON string on
   * `MerchantProperties.getReceiptProperties()`. The receipt header text is under
   * `storeHeadline` and the footer text under `customFooter`.
   */
  private fun receiptProperty(params: ReceiptParams, key: String): String? {
    val json = params.merchant?.properties?.receiptProperties ?: return null
    return kotlin.runCatching { JSONObject(json).optString(key).takeIf { it.isNotEmpty() } }
      .getOrNull()
  }

  /**
   * Renders a built receipt view to bitmap chunks: measure at exactly [widthPx], lay out, then
   * draw 2048px-tall (max) windows into RGB_565 bitmaps — the same chunking contract the
   * native bitmap providers follow.
   */
  fun renderToChunks(view: View, widthPx: Int, maxChunkHeight: Int): List<Bitmap> {
    view.measure(
      View.MeasureSpec.makeMeasureSpec(widthPx, View.MeasureSpec.EXACTLY),
      View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    )
    view.layout(0, 0, view.measuredWidth, view.measuredHeight)

    val totalHeight = view.measuredHeight.coerceAtLeast(1)
    val chunks = mutableListOf<Bitmap>()
    var top = 0
    while (top < totalHeight) {
      val chunkHeight = min(maxChunkHeight, totalHeight - top)
      val bitmap = Bitmap.createBitmap(widthPx, chunkHeight, Bitmap.Config.RGB_565)
      val canvas = Canvas(bitmap)
      canvas.drawColor(Color.WHITE)
      canvas.translate(0f, -top.toFloat())
      view.draw(canvas)
      chunks.add(bitmap)
      top += chunkHeight
    }
    return chunks
  }

  private fun isReducedTaxRate(line: LineItem): Boolean =
    line.taxRates?.any {
      //This is the expected attribute on a tax-exempt item, but it's not currently populated in the sandbox or on real orders.
      //Will be part of the next SDK release. For now, we can only check that the tax rate is 8%.

      //it.systemTaxRate.labelKey == REDUCED_TAX_RATE_LABEL_KEY
      800000L == it.rate
    } == true

  private fun isTaxExcept(line: LineItem): Boolean =
    line.taxRates?.any {
      //This is the expected attribute on a tax-exempt item, but it's not currently populated in the sandbox or on real orders.
      //Will be part of the next SDK release. For now, we can only check that the tax rate is zero, which is a necessary but not sufficient condition for tax exemption.

      //it.systemTaxRate.labelKey == NO_TAX_RATE_TAX_TYPE_LABEL_KEY
      0L == it.rate
    } == true

  /**
   * Formats an amount in the merchant's currency. Clover amounts are expressed in the
   * currency's minor unit (e.g. 1234 == $12.34 for USD; currencies without minor units are
   * not scaled), so scale by the currency's default fraction digits.
   *
   * The v3 merchant carries these as strings: [Merchant.getDefaultCurrency] is an ISO 4217
   * code (e.g. "USD") and the properties' locale a language tag (e.g. "en-US").
   */
  private fun formatAmount(amount: Long?, merchant: Merchant?): String {
    if (amount == null) return ""
    val locale = merchant?.properties?.locale
      ?.let { Locale.forLanguageTag(it.replace('_', '-')) }
      ?: Locale.getDefault()
    val currency = (merchant?.defaultCurrency ?: merchant?.properties?.defaultCurrency)
      ?.let { runCatching { Currency.getInstance(it) }.getOrNull() }
      ?: Currency.getInstance(Locale.getDefault())
    val format = NumberFormat.getCurrencyInstance(locale).apply { this.currency = currency }
    return format.format(amount / 10.0.pow(currency.defaultFractionDigits))
  }

  private fun trimQty(value: Double): String =
    if (value == value.toLong().toDouble()) value.toLong().toString() else value.toString()

  private fun divider(params: ReceiptParams): View = View(context).apply {
    setBackgroundColor(Color.BLACK)
    layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2).apply {
      val margin = (TEXT_SIZE_MEDIUM / 2).toInt()
      topMargin = margin
      bottomMargin = margin
    }
  }

  private fun verticalSection(params: ReceiptParams, build: LinearLayout.() -> Unit): LinearLayout =
    LinearLayout(context).apply {
      orientation = LinearLayout.VERTICAL
      setPadding(0, (TEXT_SIZE_MEDIUM / 2).toInt(), 0, (TEXT_SIZE_MEDIUM / 2).toInt())
      build()
    }

  private fun row(
    label: String,
    value: String?,
    params: ReceiptParams,
    bold: Boolean = false,
    sizePx: Float = TEXT_SIZE_MEDIUM
  ): View = LinearLayout(context).apply {
    orientation = LinearLayout.HORIZONTAL
    addView(text(label, params, bold, sizePx).apply {
      layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
    })
    value?.let {
      addView(text(it, params, bold, sizePx).apply { gravity = Gravity.END })
    }
  }

  private fun centeredText(value: String, params: ReceiptParams, bold: Boolean = false, sizePx: Float = TEXT_SIZE_MEDIUM): TextView =
    text(value, params, bold, sizePx).apply {
      gravity = Gravity.CENTER_HORIZONTAL
      layoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
      )
    }

  private fun text(value: String, params: ReceiptParams, bold: Boolean = false, sizePx: Float = TEXT_SIZE_MEDIUM): TextView =
    TextView(context).apply {
      text = value
      setTextColor(Color.BLACK)
      typeface = receiptTypeface(params, bold)
      setTextSize(TypedValue.COMPLEX_UNIT_PX, sizePx)
      // Match the native renderer: no font padding, and no anti-aliasing — gray AA edges
      // dither into fuzzy dots on a thermal printer.
      includeFontPadding = false
      paint.isAntiAlias = false
    }

  /**
   * The native receipt clover renders 384-dot receipts in Roboto Condensed and wider
   * (576-dot) receipts in the default font; "sans-serif-condensed" is the system Roboto
   * Condensed family.
   */
  private fun receiptTypeface(params: ReceiptParams, bold: Boolean): Typeface =
    if (params.receiptWidth > 384) {
      Typeface.defaultFromStyle(if (bold) Typeface.BOLD else Typeface.NORMAL)
    } else {
      Typeface.create("sans-serif-condensed", if (bold) Typeface.BOLD else Typeface.NORMAL)
    }

  /**
   * Returns total guest count for an order where each distinct, non-empty binName represents one guest.
   */
  fun getGuestCountByBinName(order: Order?): Int {
    if (order == null || order.lineItems.isNullOrEmpty()) {
      return 0
    }
    return getGuestCountByBinName(order.lineItems)
  }

  /**
   * Returns true if the print job is eligible for stamp duty, based on the FLAG_STAMP_DUTY_ELIGIBLE flag.
   */
  fun isStampDutyEligible(params: ReceiptParams): Boolean {
    val flags = params.printJob.flags
    return flags and PrintJob.FLAG_STAMP_DUTY_ELIGIBLE == PrintJob.FLAG_STAMP_DUTY_ELIGIBLE
  }

  /**
   * Returns true if the print job is an RSS receipt, based on the FLAG_PRINT_RSS_RECEIPT flag.
   */
  fun isRSSRReceipt(params: ReceiptParams): Boolean {
    val flags = params.printJob.flags
    return flags and PrintJob.FLAG_PRINT_RSS_RECEIPT == PrintJob.FLAG_PRINT_RSS_RECEIPT
  }

  /**
   * Returns the Japanese business registration number (BRN) from the merchant's gateway, or an
   * empty string if not available.
   */
  fun getBusinessRegistrationNumber(params: ReceiptParams): String {
    return params.merchant?.gateway?.brn ?: ""
  }

  /**
   * Returns total guest count from a list of line items, using distinct non-empty binName values.
   */
  fun getGuestCountByBinName(lineItems: List<LineItem>?): Int {
    if (lineItems.isNullOrEmpty()) {
      return 0
    }

    val guestBins = mutableSetOf<String>()
    lineItems.forEach { lineItem ->
      val normalizedBinName = lineItem.binName?.trim()
      if (!normalizedBinName.isNullOrEmpty()) {
        guestBins.add(normalizedBinName)
      }
    }

    return guestBins.size
  }
}
