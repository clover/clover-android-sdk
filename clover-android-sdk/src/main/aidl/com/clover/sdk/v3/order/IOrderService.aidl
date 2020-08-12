package com.clover.sdk.v3.order;

import com.clover.sdk.v3.inventory.Modifier;
import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v3.order.Discount;
import com.clover.sdk.v3.order.LineItem;
import com.clover.sdk.v3.order.Modification;
import com.clover.sdk.v3.order.Order;
import com.clover.sdk.v3.order.IOnOrderUpdateListener;
import com.clover.sdk.v3.order.IOnOrderUpdateListener2;
import com.clover.sdk.v3.payments.Credit;
import com.clover.sdk.v3.payments.CreditRefund;
import com.clover.sdk.v3.payments.Refund;
import com.clover.sdk.v3.payments.Payment;
import com.clover.sdk.v3.payments.TransactionInfo;
import com.clover.sdk.v3.pay.PaymentRequest;
import com.clover.sdk.v3.pay.PaymentRequestCardDetails;
import com.clover.sdk.v3.order.VoidReason;

/**
 * An interface for interacting with the Clover order service. The order
 * service is a bound AIDL service. Bind to this service as follows:
 * <pre>
 * <code>
 * Intent serviceIntent = new Intent(OrderIntent.ACTION_ORDER_SERVICE_V3);
 * serviceIntent.putExtra(Intents.EXTRA_ACCOUNT, CloverAccount.getAccount(context));
 * serviceIntent.putExtra(Intents.EXTRA_VERSION, 3);
 * context.bindService(serviceIntent);
 * </code>
 * </pre>
 * For more information about bound services, refer to
 * the Android documentation:
 * <a href="http://developer.android.com/guide/components/bound-services.html#Binding">
 * Bound Services
 * </a>.
 * <p>
 * You may also interact with this service through the
 * {@link OrderConnector} class, which handles binding and
 * asynchronous invocation of service methods.
 * <p>
 * This service is backed by a local database which is synced to cloud. Thus changes made
 * by calling methods here will be reflected on all of a merchant's devices and on the web.
 * <p>
 * Most methods require ORDERS_R and/or ORDERS_W permission.
 *
 * @deprecated Please use the {@link IOrderServiceV3_1} via {@link OrderConnector}
 * instead, it offers all the same functionality but is designed to handle
 * large orders (over 1MB) that cause this API to fail. New methods may not be
 * added to this class going forward.
 */
interface IOrderService {

  void addOnOrderUpdatedListener(IOnOrderUpdateListener listener);

  void removeOnOrderUpdatedListener(IOnOrderUpdateListener listener);

  /**
   * Get the {@link Order} for the given ID. If the order is not synchronized on this device, the order is fetched
   * from the server.
   *
   * @param orderId The ID of the {@link Order} to retrieve.
   * @return The {@link Order}s corresponding to the provided ID, or {@link null} if the order does not exists locally
   * and it cannot be fetched from the server. Note that this may be because the server is not reachable or because
   * the order for the given ID does not exist.
   */
  Order getOrder(String orderId, out ResultStatus status);

  List<Order> getOrders(in List<String> orderIds, out ResultStatus status);

  /**
   * Create a new {@link Order}. Only the order title is used for creation; all other fields are ignored. The following
   * fields are set automatically:
   * <ul>
   *   <li>id</li>
   *   <li>device - set the this device.</li>
   *   <li>currency - set to the merchant's currency.</li>
   *   <li>vat - set according to the merchant's setting.</li>
   *   <li>groupLineItems - set according to the merchant's setting.</li>
   *   <li>testMode - set according to the merchant's setting.</li>
   *   <li>createdTime - set to this device's current time.</li>
   *   <li>taxRemoved - set to false.</li>
   *   <li>total - set to 0.</li>
   *   <li>employee - set to the current employee logged into this device.</li>
   * </ul>
   *
   * This method requires ORDERS_W permission.
   *
   * @param order The order to create.
   */
  Order createOrder(in Order order, out ResultStatus status);

  /**
   * Update an {@link Order}. The following fields may be updated,
   * <ul>
   *   <li>total</li>
   *   <li>title</li>
   *   <li>note</li>
   *   <li>state</li>
   *   <li>taxRemoved</li>
   *   <li>groupLineItems</li>
   *   <li>manualTransactions</li>
   *   <li>testMode</li>
   *   <li>orderType.id</li>
   *   <li>customerId</li>
   *   <li>payType</li>
   *   <li>createdTime</li>
   *   <li>employee.id</li>
   * </ul>
   * All other fields are ignored.
   *
   * This method requires ORDERS_W permission.
   *
   * @param order The {@link Order} to updated.
   */
  Order updateOrder(in Order order, out ResultStatus status);

  /**
   * Delete an {@link Order}.
   *
   * @param orderId The ID of the {@link Order} to be deleted.
   * @return true if the {@link Order} was deleted successfully, otherwise false.
   * @see #deleteOrderOnline
   */
  boolean deleteOrder(String orderId, out ResultStatus status);

  /**
   * Add a {@link com.clover.sdk.v3.base.ServiceCharge} to an order.
   *
   * @param orderId The order ID on which to add the service charge.
   * @param serviceChargeId The ID of the service charge to be added to the order.
   * @return The updated order with the service charge added.
   */
  Order addServiceCharge(String orderId, String serviceChargeId, out ResultStatus status);

  /**
   * Add a {@link com.clover.sdk.v3.base.ServiceCharge} to an order.
   *
   * @param orderId The order ID on which to add the service charge.
   * @param serviceChargeId The ID of the service charge to be added to the order.
   * @return The updated order with the service charge removed.
   */
  Order deleteServiceCharge(String orderId, String serviceChargeId, out ResultStatus status);

  /**
   * Add a fixed-price line item to an order. A fixed price line item is priced per item.
   *
   * {@link LineItem}s are linked to {@link com.clover.sdk.v3.inventory.Item}s with an item ID. Think of the
   * {@link com.clover.sdk.v3.inventory.Item} as a template for creating a {@link LineItem}, and a
   * {@link LineItem} as the order's copy of an {@link com.clover.sdk.v3.inventory.Item}.
   *
   * @param orderId The ID of the order to which to add the line item.
   * @param itemId The item ID from which to create the line item to be added to the order.
   * @param binName The BIN name for the line item. May be {@link null}.
   * @param userData Meta-data to attach to the line item. May be {@link null}.
   * @return The newly created {@link LineItem}.
   */
  LineItem addFixedPriceLineItem(String orderId, String itemId, String binName, String userData, out ResultStatus status);

  /**
   * Add a per-unit line item to an order. A per unit line item is priced per unit, not per item. A good example
   * is items that are sold by weight (e.g., per ounce).
   *
   * {@link LineItem}s are linked to {@link com.clover.sdk.v3.inventory.Item}s with an item ID. Think of the
   * {@link com.clover.sdk.v3.inventory.Item} as a template for creating a {@link LineItem}, and a
   * {@link LineItem} as the order's copy of an {@link com.clover.sdk.v3.inventory.Item}.
   *
   * @param orderId The ID of the order to which to add the line item.
   * @param itemId The item ID from which to create the line item to be added to the order.
   * @param unitQuantity The unit quantity for the line item (e.g., "10 ounces").
   * @param binName The BIN name for the line item. May be {@link null}.
   * @param userData Meta-data to attach to the line item. May be {@link null}.
   * @return The newly created {@link LineItem}.
   */
  LineItem addPerUnitLineItem(String orderId, String itemId, int unitQuantity, String binName, String userData, out ResultStatus status);

  /**
   * Add a variably-priced line item to the order. A variably priced line item's price is determined at the time of
   * sale.
   *
   * Note that this method is not consistent with others in this interface as it returns a {@link LineItem}. All other
   * methods return the complete, updated {@link Order}.
   *
   * @param orderId The ID of the order to which to add the line item.
   * @param itemId The item ID from which to create the line item to be added to the order.
   * @param price The price of the line item.
   * @param binName The BIN name for the line item. May be {@link null}.
   * @param userData Meta-data to attach to the line item. May be {@link null}.
   */
  LineItem addVariablePriceLineItem(String orderId, String itemId, long price, String binName, String userData, out ResultStatus status);

  /**
   * Add a custom line item to an order. Custom line items are not associated with an inventory item.
   *
   * Note that this method is not consistent with others in this interface as it returns a {@link LineItem}. All other
   * methods return the complete, updated {@link Order}.

   * @param orderId The ID of the order to which to add the line item.
   * @param lineItem The line item to add to the order.
   * @param isTaxable true if this line item is taxable, otherwise false.
   */
  LineItem addCustomLineItem(String orderId, in LineItem lineItem, boolean isTaxable, out ResultStatus status);

  /**
   * Update {@link LineItem}s on an {@link Order}. Only the following fields may be updated,
   * <ul>
   *   <li>binName</li>
   *   <li>printed</li>
   *   <li>note</li>
   *   <li>userData</li>
   * </ul>
   * All other fields are ignored.
   *
   * @param orderId The ID of the order on which to update the line items.
   * @param lineItemIds The {@link LineItem}s to update on the order.
   * @return The updated {@link LineItem}s.
   */
  List<LineItem> updateLineItems(String orderId, in List<LineItem> lineItemIds, out ResultStatus status);

  /**
   * Delete {@link LineItem}s from an {@link Order}.
   *
   * @param orderId The ID of the {@link Order} from which to delete the line items.
   * @param lineItemIds The {@link LineItem} IDs to delete.
   * @return The updated {@link Order}.
   */
  Order deleteLineItems(String orderId, in List<String> lineItemIds, out ResultStatus status);

  List<LineItem> copyLineItems(String sourceOrderId, String destinationOrderId, in List<String> lineItemIds, out ResultStatus status);

  Order setLineItemNote(String orderId, String lineItemId, String note, out ResultStatus status);

  Order addLineItemModification(String orderId, String lineItemId, in Modifier modifier, out ResultStatus status);

  Order deleteLineItemModifications(String orderId, String lineItemId, in List<String> modificationIds, out ResultStatus status);

  LineItem exchangeItem(String orderId, String oldLineItemId, String itemId, String binName, String userData, out ResultStatus status);

  Order addDiscount(String orderId, in Discount discount, out ResultStatus status);

  Order deleteDiscounts(String orderId, in List<String> discountIds, out ResultStatus status);

  Order addLineItemDiscount(String orderId, String lineItemId, in Discount discount, out ResultStatus status);

  Order deleteLineItemDiscounts(String orderId, String lineItemId, in List<String> discountIds, out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  Order addTip(String orderId, String paymentId, long amount, boolean online, out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  Payment pay(String orderId, in PaymentRequest paymentRequest, boolean isAllowOffline, String note, out ResultStatus status);

  /**
   * If necessary, use other methods to open the cash drawer and log cash events.
   * @deprecated Use {@link #addPayment2}.
   * @y.exclude
   */
  Order addPayment(String orderId, in Payment payment, in List<LineItem> lineItems, out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   * @deprecated Use {@link #voidPayment2}.
   * @y.exclude
   */
  Order voidPayment(String orderId, String paymentId, out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  Credit addCredit(String orderId, in Credit payment, out ResultStatus status);

  /**
   * Not implemented.
   */
  Order deleteCredit(String orderId, String creditId, out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  Refund addRefund(String orderId, in Refund payment, out ResultStatus status);

  /**
   * Not implemented.
   */
  Order deleteRefund(String orderId, String refundId, out ResultStatus status);

  /**
   * Delete an {@link Order} synchronously on the server. Differing from {@link #deleteOrder(String)}, this method only
   * completes successfully if this device can reach the server and retrieve confirmation that the order was deleted.
   *
   * @param orderId The ID of the order to be deleted.
   * @return true if the order was deleted successfully, otherwise false.
   * @see #deleteOrder
   */
  boolean deleteOrderOnline(String orderId, out ResultStatus status);

  Order addBatchLineItemModifications(String orderId, in List<String> lineItemIds, in Modifier modifier, int quantity, out ResultStatus status);

  Order addBatchLineItemDiscounts(String orderId, in List<String> lineItemIds, in List<Discount> discounts, out ResultStatus status);

  Map createLineItemsFrom(String sourceOrderId, String destinationOrderId, in List<String> lineItemIds, out ResultStatus status);

  /**
   * Print line items to the kitchen or order printer quickly. Only prints inventory items that are
   * associated with a printer. The association is done by linking an item and a printer with a tag.
   * It will only print line items once, subsequent invocations will not cause additional prints,
   * but the method will still return true.
   *
   * @return true, unless the order has no line items in it that can be fired to a printer, will
   * return true but not print anything if all items have been already printed
   */
  boolean fire(String sourceOrderId, out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  Payment updatePayment(String orderId, in Payment payment, out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  Order voidPayment2(String orderId, String paymentId, String iccContainer, in VoidReason reason, String source, out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  Order removePayment(String orderId, String paymentId, out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  Refund addRefundOffline(String orderId, in Refund payment, out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  Refund refund(String orderId, in Refund payment, out ResultStatus status);

  void addOnOrderUpdatedListener2(IOnOrderUpdateListener2 listener);

  void removeOnOrderUpdatedListener2(IOnOrderUpdateListener2 listener);

  /**
   * Just like {@link #addDiscount} but returns a {@link Discount} instead of an {@link Order}.
   */
  Discount addDiscount2(String orderId, in Discount discount, out ResultStatus status);

  /**
   * Just like {@link #addLineItemDiscount} but returns a {@link Discount} instead of an {@link Order}.
   */
  Discount addLineItemDiscount2(String orderId, String lineItemId, in Discount discount, out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   *
   * Add a payment to an order. The payment is only added to the local DB: the change is not persisted on the server.
   * This method differs from {@link #addPayment} in that it does not add cash events or
   * open the cash drawer. Not available to non-Clover apps.
   *
   * @param orderId, the order ID.
   * @param payment, the payment.
   * @param lineItems, the line items that were paid by this payment.

   * @return the updated order.
   * @y.exclude
   */
  Order addPayment2(String orderId, in Payment payment, in List<LineItem> lineItems, out ResultStatus status);

  /**
   * Just like {@link #fire} but additionally when requireAllItems is set to true it will not print
   * and return false if some items on the order haven't been printed yet and would not be printed
   * because they are not associated with an order printer.
   *
   * @return just like {@link #fire}, but additionally returns false if there are unprinted items
   *         without a printer associated.
   */
  boolean fire2(String sourceOrderid, boolean requireAllItems, out ResultStatus status);

  /**
   * Just like {@link #createLineItemsFrom} but additionally when copyPrinted is set to true it will copy print flags on
   * line items (normally did not), and when broadcastLineItems is set to true it will 
   * broadcastLineItems (normally did).
   */
  Map createLineItemsFrom2(String sourceOrderId, String destinationOrderId, in List<String> lineItemIds, in boolean copyPrinted, in boolean broadcastLineItems, out ResultStatus status);

  /**
   * Just like {@link #deleteOrder} but additionally when allowDeleteIfLineItemPrinted is true it will delete the order
   * when line items are printed (normally did not).
   */
  boolean deleteOrder2(String orderId, in boolean allowDeleteIfLineItemPrinted, out ResultStatus status);

  /**
   * This pulls pending payments from the local device db
   */
  List<Payment> getPendingPayments(out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  CreditRefund addCreditRefund(String orderId, in CreditRefund creditRefund, out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  Order deleteCreditRefund(String orderId, in String creditRefundId, out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  Order voidPaymentWithCard(String orderId, String paymentId, String iccContainer, in PaymentRequestCardDetails card, in VoidReason reason, String source, out ResultStatus status);

  /**
   * Get list of lineitem ids for the order that has printtag to print.
   * The items that are already printed are not part of the list
   *
   * Not available to non-Clover apps.
   * @y.exclude
   */
  List<String> getLineItemsToFire(String orderId, out ResultStatus status);

  /**
   * Reprint all lineitems that has a printtag even if they are printed.
   *
   * Not available to non-Clover apps.
   * @y.exclude
   */
  boolean refire(String orderId, out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   * @deprecated Use {@link #deleteOrder3}.
   * @y.exclude
   */
  boolean deleteOrderOnline2(String orderId, boolean usePermissionForOrderDeletions, out ResultStatus status);

  /**
   * Delete an {@link Order}. This method allows switching between online deletion like {@link #deleteOrderOnline(String)}
   * and offline (deleting loaclly and adding message to server queue) like {@link #deleteOrder(String)}
   * This method allows an override toggle for the employee permissions and printed line items checks.
   *
   * @param orderId The ID of the order to be deleted.
   * @param deleteOnline true to delete an order synchronously on the server @see #deleteOrderOnline
   * @param allowDeleteIfLineItemPrinted true to allow deleting order if it has printed line items
   * @param allowDeleteIfNoEmployeePermission true to allow deletion regardless of employee permission.
   * @return true if the order was deleted successfully, otherwise false.
   *
   * Not available to non-Clover apps.
   * @y.exclude
   */
  boolean deleteOrder3(String orderId, boolean deleteOnline, boolean allowDeleteIfLineItemPrinted, boolean allowDeleteIfNoEmployeePermission, out ResultStatus status);

  /**
   * Card present void
   *
   * Not available to non-Clover apps.
   * @y.exclude
   */
  Order voidPaymentCardPresent(String orderId, String paymentId, String iccContainer, in PaymentRequestCardDetails card, in TransactionInfo transactionInfo, in VoidReason reason, String source, out ResultStatus status);

  /**
   * @param orderId The ID of the order to be updated.
   * @param creditId The ID of the credit to be refunded.
   * @return the CreditRefund object constructed using the RefundResponse the serverf returns
   * Not available to non-Clover apps.
   * @y.exclude
  */
  CreditRefund vaultedCreditRefund(in String orderId, in String creditId, out ResultStatus status);
}
