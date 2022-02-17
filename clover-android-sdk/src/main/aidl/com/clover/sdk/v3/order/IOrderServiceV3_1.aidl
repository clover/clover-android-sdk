package com.clover.sdk.v3.order;

import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v3.order.ClientEventType;
import com.clover.sdk.v3.order.Modification;
import com.clover.sdk.v3.order.OrderSummary;
import com.clover.sdk.v3.order.IOnOrderUpdateListener;
import com.clover.sdk.v3.order.IOnOrderUpdateListener2;
import com.clover.sdk.v3.order.VoidReason;
import com.clover.sdk.v3.order.OrderFdParcelable;
import com.clover.sdk.v3.order.OrderListFdParcelable;
import com.clover.sdk.v3.order.PrintGroupFdParcelable;
import com.clover.sdk.v3.order.LineItemFdParcelable;
import com.clover.sdk.v3.order.LineItemListFdParcelable;
import com.clover.sdk.v3.order.LineItemMapFdParcelable;
import com.clover.sdk.v3.order.DiscountFdParcelable;
import com.clover.sdk.v3.order.DiscountListFdParcelable;
import com.clover.sdk.v3.order.ModificationFdParcelable;
import com.clover.sdk.v3.onlineorder.OrderState;
import com.clover.sdk.v3.onlineorder.Reason;
import com.clover.sdk.v3.payments.PaymentFdParcelable;
import com.clover.sdk.v3.payments.PaymentListFdParcelable;
import com.clover.sdk.v3.payments.CreditFdParcelable;
import com.clover.sdk.v3.payments.CreditRefundFdParcelable;
import com.clover.sdk.v3.payments.CreditRefund;
import com.clover.sdk.v3.payments.RefundFdParcelable;
import com.clover.sdk.v3.payments.TransactionInfo;
import com.clover.sdk.v3.pay.PaymentRequestFdParcelable;
import com.clover.sdk.v3.pay.PaymentRequestCardDetails;
import com.clover.sdk.v3.inventory.ModifierFdParcelable;
import com.clover.common.payments.TerminalManagementComponent;
import com.clover.common.payments.VoidExtraData;
import com.clover.sdk.v3.payments.AdditionalChargeAmount;

/**
 * An interface for interacting with the Clover order service.
 * <p>
 * You may also interact with this service through the
 * {@link OrderConnector} class, which handles binding and
 * asynchronous invocation of service methods.
 * <p>
 * This service is backed by a local database which is synced to cloud.
 * <p>
 * Searching and listing orders may done via the {@link OrderContract}.
 * <h3>Implementation Details</h3>
 * <p>
 * This service mirrors the functionality of {@link IOrderService} but uses a different
 * mechanism for trasferring Clover SDK objects. Specifically, as can be seen from
 * the interface definition below, CloverSDK objects are transferred wrapped by a
 * {@link FdParcelable}. This includes SDK objects passed to the service and objects
 * returned from the server.
 * <p>
 * For example, to create an FD parcelablable input for an {@link Order} argument,
 * <pre>
 *   {@code
 *   OrderFdParcelable fdo = new OrderFdParcelable(order);
 *   service.updateOrder(fdo, ...);
 *   }
 * </pre>
 * To obtain an Order object return value from an FD parcelable,
 * <pre>
 *   {@code
 *   OrderFdParcelable fdo = service.getOrder(...);
 *   Order order = fdo.getValue();
 *   }
 * </pre>
 */
interface IOrderServiceV3_1 {

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
   *
   * @clover.perm ORDERS_R
   */
  OrderFdParcelable getOrder(String orderId, out ResultStatus status);

  /**
  * Get the {@link Order}s for the given IDs. If the orders are not synchronized on this device, they are fetched
  * from the server.
  *
  * @param orderIds The list of orders to retrieve
  * @return The list of {@link Order}s corresponding to the provided IDs
  *
  * @clover.perm ORDERS_R
  */
  OrderListFdParcelable getOrders(in List<String> orderIds, out ResultStatus status);

  /**
   * Create a new {@link Order}. Only the following fields are allowed during creation:
   * <ul>
   *   <li>title</li>
   *   <li>List<customer.id></li>
   *   <li>orderType.id</li>
   *   <li>note</li>
   * </ul>
   * <p>
   * The following fields are set automatically by this call:
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
   * <p>
   * Any other fields included in this call will result in failure to create the order. Adding
   * line items and making other changes should be done after order creation using other methods
   * in this class.
   *
   * @param order The order to create.
   * @clover.perm ORDERS_W
   */
  OrderFdParcelable createOrder(in OrderFdParcelable fdOrder, out ResultStatus status);

  /**
   * Update an {@link Order}. The following fields may be updated,
   * <ul>
   *   <li>title</li>
   *   <li>note</li>
   *   <li>taxRemoved</li>
   *   <li>groupLineItems</li>
   *   <li>manualTransactions</li>
   *   <li>testMode</li>
   *   <li>orderType.id</li>
   *   <li>List<customer.id></li>
   *   <li>payType</li>
   *   <li>employee.id</li>
   * </ul>
   * All other fields are ignored.
   *
   * @param order The {@link Order} to updated.
   * @clover.perm ORDERS_W
   */
  OrderFdParcelable updateOrder(in OrderFdParcelable fdOrder, out ResultStatus status);

  /**
   * Delete an {@link Order}.
   *
   * @param orderId The ID of the {@link Order} to be deleted.
   * @return true if the {@link Order} was deleted successfully, otherwise false.
   * @see #deleteOrderOnline
   * @clover.perm ORDERS_W
   */
  boolean deleteOrder(String orderId, out ResultStatus status);

  /**
   * Add a {@link com.clover.sdk.v3.base.ServiceCharge} to an order.
   *
   * @param orderId The order ID on which to add the service charge.
   * @param serviceChargeId The ID of the service charge to be added to the order.
   * @return The updated order with the service charge added.
   * @clover.perm ORDERS_W
   */
  OrderFdParcelable addServiceCharge(String orderId, String serviceChargeId, out ResultStatus status);

  /**
   * Add a {@link com.clover.sdk.v3.base.ServiceCharge} to an order.
   *
   * @param orderId The order ID on which to add the service charge.
   * @param serviceChargeId The ID of the service charge to be added to the order.
   * @return The updated order with the service charge removed.
   * @clover.perm ORDERS_W
   */
  OrderFdParcelable deleteServiceCharge(String orderId, String serviceChargeId, out ResultStatus status);

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
   * @clover.perm ORDERS_W
   */
  LineItemFdParcelable addFixedPriceLineItem(String orderId, String itemId, String binName, String userData, out ResultStatus status);

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
   * @clover.perm ORDERS_W
   */
  LineItemFdParcelable addPerUnitLineItem(String orderId, String itemId, int unitQuantity, String binName, String userData, out ResultStatus status);

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
   * @clover.perm ORDERS_W
   */
  LineItemFdParcelable addVariablePriceLineItem(String orderId, String itemId, long price, String binName, String userData, out ResultStatus status);

  /**
   * Add a custom line item to an order. Custom line items are not associated with an inventory item.
   *
   * Note that this method is not consistent with others in this interface as it returns a {@link LineItem}. All other
   * methods return the complete, updated {@link Order}.

   * @param orderId The ID of the order to which to add the line item.
   * @param lineItem The line item to add to the order.
   * @param isTaxable true if this line item is taxable, otherwise false.
   * @clover.perm ORDERS_W
   */
  LineItemFdParcelable addCustomLineItem(String orderId, in LineItemFdParcelable fdLineItem, boolean isTaxable, out ResultStatus status);

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
   * @clover.perm ORDERS_W
   */
  LineItemListFdParcelable updateLineItems(String orderId, in LineItemListFdParcelable fdLineItems, out ResultStatus status);

  /**
   * Delete {@link LineItem}s from an {@link Order}.
   *
   * @param orderId The ID of the {@link Order} from which to delete the line items.
   * @param lineItemIds The {@link LineItem} IDs to delete.
   * @return The updated {@link Order}.
   * @clover.perm ORDERS_W
   */
  OrderFdParcelable deleteLineItems(String orderId, in List<String> lineItemIds, out ResultStatus status);

  LineItemListFdParcelable copyLineItems(String sourceOrderId, String destinationOrderId, in List<String> lineItemIds, out ResultStatus status);

  /**
  * @clover.perm ORDERS_W
  */
  OrderFdParcelable setLineItemNote(String orderId, String lineItemId, String note, out ResultStatus status);

  /**
  * @clover.perm ORDERS_W
  */
  OrderFdParcelable addLineItemModification(String orderId, String lineItemId, in ModifierFdParcelable fdModifier, out ResultStatus status);

  /**
  * @clover.perm ORDERS_W
  */
  OrderFdParcelable deleteLineItemModifications(String orderId, String lineItemId, in List<String> modificationIds, out ResultStatus status);

  /**
  * @clover.perm ORDERS_W
  */
  LineItemFdParcelable exchangeItem(String orderId, String oldLineItemId, String itemId, String binName, String userData, out ResultStatus status);

  /**
  * @clover.perm ORDERS_W
  */
  OrderFdParcelable addDiscount(String orderId, in DiscountFdParcelable fdDiscount, out ResultStatus status);

  /**
  * @clover.perm ORDERS_W
  */
  OrderFdParcelable deleteDiscounts(String orderId, in List<String> discountIds, out ResultStatus status);

  /**
  * @clover.perm ORDERS_W
  */
  OrderFdParcelable addLineItemDiscount(String orderId, String lineItemId, in DiscountFdParcelable fdDiscount, out ResultStatus status);

  /**
  * @clover.perm ORDERS_W
  */
  OrderFdParcelable deleteLineItemDiscounts(String orderId, String lineItemId, in List<String> discountIds, out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  OrderFdParcelable addTip(String orderId, String paymentId, long amount, boolean online, out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  PaymentFdParcelable pay(String orderId, in PaymentRequestFdParcelable fdPaymentRequest, boolean isAllowOffline, String note, out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   * @deprecated Use {@link #addPayment2}.
   * @y.exclude
   */
  OrderFdParcelable addPayment(String orderId, in PaymentFdParcelable fdPayment, in LineItemListFdParcelable fdLineItems, out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   * @deprecated Use {@link #voidPayment2}.
   * @y.exclude
   */
  OrderFdParcelable voidPayment(String orderId, String paymentId, out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  CreditFdParcelable addCredit(String orderId, in CreditFdParcelable fdCredit, out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  OrderFdParcelable deleteCredit(String orderId, String creditId, out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  RefundFdParcelable addRefund(String orderId, in RefundFdParcelable refund, out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  OrderFdParcelable deleteRefund(String orderId, String refundId, out ResultStatus status);

  /**
   * Delete an {@link Order} synchronously on the server. Differing from {@link #deleteOrder(String)}, this method only
   * completes successfully if this device can reach the server and retrieve confirmation that the order was deleted.
   *
   * @param orderId The ID of the order to be deleted.
   * @return true if the order was deleted successfully, otherwise false.
   * @see #deleteOrder
   * @clover.perm ORDERS_W
   */
  boolean deleteOrderOnline(String orderId, out ResultStatus status);

  /**
  * @clover.perm ORDERS_W
  */
  OrderFdParcelable addBatchLineItemModifications(String orderId, in List<String> lineItemIds, in ModifierFdParcelable fdModifier, int quantity, out ResultStatus status);

  /**
  * @clover.perm ORDERS_W
  */
  OrderFdParcelable addBatchLineItemDiscounts(String orderId, in List<String> lineItemIds, in DiscountListFdParcelable discounts, out ResultStatus status);

  /**
  * @clover.perm ORDERS_W
  */
  LineItemMapFdParcelable createLineItemsFrom(String sourceOrderId, String destinationOrderId, in List<String> lineItemIds, out ResultStatus status);

  /**
   * Print line items to the kitchen or order printer quickly. Only prints inventory items that are
   * associated with a printer. The association is done by linking an item and a printer with a tag.
   * It will only print line items once, subsequent invocations will not cause additional prints,
   * but the method will still return true.
   *
   * @return true, unless the order has no line items in it that can be fired to a printer, will
   * return true but not print anything if all items have been already printed
   * @clover.perm ORDERS_W
   *
   * @see com.clover.sdk.v3.inventory.IInventoryService.assignTagsToItem
   */
  boolean fire(String sourceOrderId, out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  PaymentFdParcelable updatePayment(String orderId, in PaymentFdParcelable fdPayment, out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  OrderFdParcelable voidPayment2(String orderId, String paymentId, String iccContainer, in VoidReason reason, String source, out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  OrderFdParcelable removePayment(String orderId, String paymentId, out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  RefundFdParcelable addRefundOffline(String orderId, in RefundFdParcelable fdRefund, out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  RefundFdParcelable refund(String orderId, in RefundFdParcelable fdRefund, out ResultStatus status);

  void addOnOrderUpdatedListener2(IOnOrderUpdateListener2 listener);

  void removeOnOrderUpdatedListener2(IOnOrderUpdateListener2 listener);

  /**
   * Just like {@link #addDiscount} but returns a {@link Discount} instead of an {@link Order}.
   * @clover.perm ORDERS_W
   */
  DiscountFdParcelable addDiscount2(String orderId, in DiscountFdParcelable fdDiscount, out ResultStatus status);

  /**
   * Just like {@link #addLineItemDiscount} but returns a {@link Discount} instead of an {@link Order}.
   * @clover.perm ORDERS_W
   */
  DiscountFdParcelable addLineItemDiscount2(String orderId, String lineItemId, in DiscountFdParcelable fdDiscount, out ResultStatus status);

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
  OrderFdParcelable addPayment2(String orderId, in PaymentFdParcelable fdPayment, in LineItemListFdParcelable fdLineItems, out ResultStatus status);

  /**
   * Just like {@link #fire} but additionally when requireAllItems is set to true it will not print
   * and return false if some items on the order haven't been printed yet and would not be printed
   * because they are not associated with an order printer.
   *
   * @return just like {@link #fire}, but additionally returns false if there are unprinted items
   *         without a printer associated.
   * @clover.perm ORDERS_W
   */
  boolean fire2(String sourceOrderid, boolean requireAllItems, out ResultStatus status);

  /**
   * Just like {@link #createLineItemsFrom} but additionally when copyPrinted is set to true it will copy print flags on
   * line items (normally did not), and when broadcastLineItems is set to true it will
   * broadcastLineItems (normally did).
   * @clover.perm ORDERS_W
   */
  LineItemMapFdParcelable createLineItemsFrom2(String sourceOrderId, String destinationOrderId, in List<String> lineItemIds, in boolean copyPrinted, in boolean broadcastLineItems, out ResultStatus status);

  /**
   * Just like {@link #deleteOrder} but additionally when allowDeleteIfLineItemPrinted is true it will delete the order
   * when line items are printed (normally did not).
   * @clover.perm ORDERS_W
   */
  boolean deleteOrder2(String orderId, in boolean allowDeleteIfLineItemPrinted, out ResultStatus status);

  /**
   * This pulls pending payments from the local device db
   * @clover.perm ORDERS_W
   */
  PaymentListFdParcelable getPendingPayments(out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  CreditRefundFdParcelable addCreditRefund(String orderId, in CreditRefundFdParcelable creditRefund, out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  OrderFdParcelable deleteCreditRefund(String orderId, String creditRefundId, out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  OrderFdParcelable addPreAuth(String orderId, in PaymentFdParcelable preAuth, out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  OrderFdParcelable capturePreAuth(String orderId, in PaymentFdParcelable preAuth, in LineItemListFdParcelable fdLineItems, out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  OrderFdParcelable voidPreAuth(String orderId, String preAuthId, String iccContainer, in VoidReason voidReason, String source, out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  OrderFdParcelable voidPreAuthOnline(String orderId, String preAuthId, String iccContainer, in VoidReason voidReason, String source, out ResultStatus status);

  /**
   * Delete {@link LineItem}s from an {@link Order}.
   *
   * @param orderId The ID of the {@link Order} from which to delete the line items.
   * @param lineItemIds The {@link LineItem} IDs to delete.
   * @param reason optional. Why was the line item removed?
   * @param clientEventType optional. What app did the delete come from?
   * @return The updated {@link Order}.
   * @clover.perm ORDERS_W
   */
  OrderFdParcelable deleteLineItemsWithReason(String orderId, in List<String> lineItemIds, in String reason, in ClientEventType clientEventType, out ResultStatus status);

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  OrderFdParcelable voidPaymentWithCard(String orderId, String paymentId, String iccContainer, in PaymentRequestCardDetails card, in VoidReason reason, String source, out ResultStatus status);

  /**
   * Get list of lineitem ids for the order that has printtag to print.
   * The items that are already printed are not part of the list
   *
   * @param orderId The ID of the {@link Order} from which to delete the line items.
   * @clover.perm ORDERS_W
   */
  List<String> getLineItemsToFire(String orderId, out ResultStatus status);

  /**
   * Reprint all lineitems that has a printtag even if they are printed.
   *
   * @param orderId The ID of the {@link Order} from which to delete the line items.
   */
  boolean refire(String orderId, out ResultStatus status);

  /**
   * Add 1 or more fixed-price line item to an order. A fixed price line item is priced per item.
   *
   * {@link LineItem}s are linked to {@link com.clover.sdk.v3.inventory.Item}s with an item ID. Think of the
   * {@link com.clover.sdk.v3.inventory.Item} as a template for creating a {@link LineItem}, and a
   * {@link LineItem} as the order's copy of an {@link com.clover.sdk.v3.inventory.Item}.
   *
   * @param orderId The ID of the order to which to add the line item.
   * @param itemId The item ID from which to create the line item to be added to the order.
   * @param binName The BIN name for the line item. May be {@link null}.
   * @param userData Meta-data to attach to the line item. May be {@link null}.
   * @param numItems number of {@link LineItem}s to create
   * @return The newly created {@link LineItem}.
   * @clover.perm ORDERS_W
   */
  LineItemListFdParcelable addFixedPriceLineItems(String orderId, String itemId, String binName, String userData, int numItems, out ResultStatus status);

  /**
   * Add 1 or more per-unit line item to an order. A per unit line item is priced per unit, not per item. A good example
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
   * @param numItems number of {@link LineItem}s to create
   * @return The newly created {@link LineItem}.
   * @clover.perm ORDERS_W
   */
  LineItemListFdParcelable addPerUnitLineItems(String orderId, String itemId, int unitQuantity, String binName, String userData, int numItems, out ResultStatus status);

  /**
   * Add 1 or more variably-priced line item to the order. A variably priced line item's price is determined at the time of
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
   * @param numItems number of {@link LineItem}s to create
   * @return The newly created {@link LineItem}.
   * @clover.perm ORDERS_W
   */
  LineItemListFdParcelable addVariablePriceLineItems(String orderId, String itemId, long price, String binName, String userData, int numItems, out ResultStatus status);

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
  OrderFdParcelable voidPaymentCardPresent(String orderId, String paymentId, String iccContainer, in PaymentRequestCardDetails card, in TransactionInfo transactionInfo, in VoidReason reason, String source, out ResultStatus status);

  /**
   * Card not present void with passthrough data
   *
   * Not available to non-Clover apps.
   * @y.exclude
   */
  OrderFdParcelable voidPayment3(String orderId, String paymentId, String iccContainer, in Map passThroughExtras, in VoidReason reason, String source, out ResultStatus status);

  /**
   * Card present void with passthrough data
   *
   * Not available to non-Clover apps.
   * @y.exclude
   */
  OrderFdParcelable voidPaymentCardPresent2(String orderId, String paymentId, String iccContainer, in PaymentRequestCardDetails card, in TransactionInfo transactionInfo, in Map passThroughExtras, in VoidReason reason, String source, out ResultStatus resultStatus);

  /**
   * Refund with passthrough data
   *
   * Not available to non-Clover apps.
   * @y.exclude
   */
  RefundFdParcelable refund2(String orderId, in RefundFdParcelable fdRefund, in Map passThroughExtras, out ResultStatus resultStatus);

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  OrderFdParcelable cleanUpPreAuthAfterTransaction(String orderId, in VoidReason voidReason, out ResultStatus status);

  /**
   * Splits line items without taking a payment. Each {@link LineItem} in {@code lineItemIds} is
   * equally split across all bins in {@code binNames}.
   *
   * For example, given a single line item ID and bin names "a" and "b", then two new line items are
   * created. One for bin "a" and another for bin "b". The original line item is then deleted. Each
   * new line item represents an equal fraction of the original by using a fraction of the
   * original's unit quantity.
   *
   * Using this method allows for items to be shared without locking the order.
   *
   * <p>Restrictions
   *
   * A line item may not be split more than once. If a line item cannot be split, it is skipped.
   *
   * @param orderId the ID of the order to modify.
   * @param lineItemIds the line items to split.
   * @param binNames the bin names to split {@code lineItemIds} across.
   *
   * @return the newly created {@link LineItem}s.
   * @clover.perm ORDERS_W
   */
  LineItemListFdParcelable splitLineItems(String orderId, in List<String> lineItemIds, in List<String> binNames, out ResultStatus resultStatus);

  /**
   * @param orderId The ID of the order to be updated.
   * @param creditId The ID of the credit to be refunded.
   * @return the CreditRefund object constructed using the RefundResponse the serverf returns
   * Not available to non-Clover apps.
   * @y.exclude
   */
  CreditRefund vaultedCreditRefund(in String orderId, in String creditId, out ResultStatus status);

  /**
   * Update the online order state.
   *
   * @Param orderId The ID of the order to be updated
   * @Param orderState The new orderstate of the online order
   * @Param reason A reason if the order is calcelled or declined.
   *
   */
  void updateOnlineOrderState(in String orderId, in OrderState orderState, in Reason reason, out ResultStatus resultStatus);

  /**
   * Add new {@link PrintGroup} on order object.
   *
   * @param orderId The ID of the order to be updated
   * @param fdPrintGroup PrintGroup to be added to an Order
   * @return the updated order
   * @clover.perm ORDERS_W
   */
  OrderFdParcelable addPrintGroup(String orderId, in PrintGroupFdParcelable fdPrintGroup, out ResultStatus status);

  /**
     * Delete {@link LineItem}s from an {@link Order} with optional approver employee id and clientEventType.
     *
     * @param orderId The ID of the {@link Order} from which to delete the line items.
     * @param lineItemIds The {@link LineItem} IDs to delete.
     * @param clientEventType What app did the delete come from? If null, no clientEventType will be added.
     * @param approvedByEmployeeId Approval id of the employee who approved the deletion of the line item, if null no approver is added.
     * @return The updated {@link Order}.
     * @clover.perm ORDERS_W
     */
  OrderFdParcelable deleteLineItems2(String orderId, in List<String> lineItemIds, in ClientEventType clientEventType, in String approvedByEmployeeId, out ResultStatus status);

  /**
   * Card present void with passthrough data and POI components.
   *
   * Not available to non-Clover apps.
   * @y.exclude
   */
  OrderFdParcelable voidPaymentCardPresent3(String orderId, String paymentId, String iccContainer, in PaymentRequestCardDetails card, in TransactionInfo transactionInfo, in Map passThroughExtras, in VoidReason reason, in VoidExtraData voidExtraData, String source, out ResultStatus resultStatus);

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  OrderFdParcelable addTipWithAdditionalCharges(String orderId, String paymentId, long amount, in List<AdditionalChargeAmount> addtionalChargeAmounts, boolean online, out ResultStatus status);

  /**
   * Send all line items in the given print groups to the kitchen or order printer. Only prints
   * items that have tags (also called labels) associating them with a printer.
   *
   * Line items will only be printed once. Subsequent invocations will not cause additional prints,
   * but the method will still return true.
   *
   * @param orderId the ID of the order to fire.
   * @param printGroupIds fire items from all given PrintGroups. If null, fire all PrintGroups.
   * @param requireAllItems when true, it will not print and return false if some items in the
   *        PrintGroups haven't been printed and will not be printed because they are not associated
   *        with an order printer.
   * @return behaves like {@link #fire(String, ResultStatus)} when {@code requireAllItems} is false.
   *         When {@code requireAllItems} is true, returns false if there are unprinted items
   *         without a printer associated.
   * @clover.perm ORDERS_W
   *
   * @see com.clover.sdk.v3.inventory.IInventoryService.assignTagsToItem
   * @see com.clover.sdk.v3.order.IOrderServiceV3_1.addPrintGroup
   */
  boolean firePrintGroups(String orderId, in List<String> printGroupsIds, boolean requireAllItems, out ResultStatus status);

  /**
   * Add a {@link com.clover.sdk.v3.base.ServiceCharge} to an order with a flag if it was added automatically or not.
   * @param orderId The order ID on which to add the service charge.
   * @param serviceChargeId The ID of the service charge to be added to the order.
   * @param isAutoApplied The flag indicating if service charge is applied automatically or not.
   * @return The updated order with the service charge added.
   * @clover.perm ORDERS_W
   */
    OrderFdParcelable addServiceCharge2(String orderId, String serviceChargeId, boolean isAutoApplied, out ResultStatus status);
}
