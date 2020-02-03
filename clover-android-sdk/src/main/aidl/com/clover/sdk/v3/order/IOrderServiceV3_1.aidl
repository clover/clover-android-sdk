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

/**
 * This service mirrors the functionality of {@link IOrderService} but uses a different
 * mechanism for trasferring Clover SDK objects. Specifically, as can be seen from
 * the interface definition below, CloverSDK objects are transferred wrapped by a
 * {@link FdParcelable}. This includes SDK objects passed to the service and objects
 * returned from the server.
 * <p/>
 * For example, to create an FD parcelablable input for an {@link Order} argument,
 * <pre>
 *   {@code
 *   OrderFdParcelable fdo = new OrderFdParcelable(order);
 *   service.updateOrder(fdo, ...);
 *   }
 * </pre>
 * <p/>
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

  OrderFdParcelable getOrder(String orderId, out ResultStatus status);

  OrderListFdParcelable getOrders(in List<String> orderIds, out ResultStatus status);

  OrderFdParcelable createOrder(in OrderFdParcelable fdOrder, out ResultStatus status);

  OrderFdParcelable updateOrder(in OrderFdParcelable fdOrder, out ResultStatus status);

  boolean deleteOrder(String orderId, out ResultStatus status);

  OrderFdParcelable addServiceCharge(String orderId, String serviceChargeId, out ResultStatus status);

  OrderFdParcelable deleteServiceCharge(String orderId, String serviceChargeId, out ResultStatus status);

  LineItemFdParcelable addFixedPriceLineItem(String orderId, String itemId, String binName, String userData, out ResultStatus status);

  LineItemFdParcelable addPerUnitLineItem(String orderId, String itemId, int unitQuantity, String binName, String userData, out ResultStatus status);

  LineItemFdParcelable addVariablePriceLineItem(String orderId, String itemId, long price, String binName, String userData, out ResultStatus status);

  LineItemFdParcelable addCustomLineItem(String orderId, in LineItemFdParcelable fdLineItem, boolean isTaxable, out ResultStatus status);

  LineItemListFdParcelable updateLineItems(String orderId, in LineItemListFdParcelable fdLineItems, out ResultStatus status);

  OrderFdParcelable deleteLineItems(String orderId, in List<String> lineItemIds, out ResultStatus status);

  LineItemListFdParcelable copyLineItems(String sourceOrderId, String destinationOrderId, in List<String> lineItemIds, out ResultStatus status);

  OrderFdParcelable setLineItemNote(String orderId, String lineItemId, String note, out ResultStatus status);

  OrderFdParcelable addLineItemModification(String orderId, String lineItemId, in ModifierFdParcelable fdModifier, out ResultStatus status);

  OrderFdParcelable deleteLineItemModifications(String orderId, String lineItemId, in List<String> modificationIds, out ResultStatus status);

  LineItemFdParcelable exchangeItem(String orderId, String oldLineItemId, String itemId, String binName, String userData, out ResultStatus status);

  OrderFdParcelable addDiscount(String orderId, in DiscountFdParcelable fdDiscount, out ResultStatus status);

  OrderFdParcelable deleteDiscounts(String orderId, in List<String> discountIds, out ResultStatus status);

  OrderFdParcelable addLineItemDiscount(String orderId, String lineItemId, in DiscountFdParcelable fdDiscount, out ResultStatus status);

  OrderFdParcelable deleteLineItemDiscounts(String orderId, String lineItemId, in List<String> discountIds, out ResultStatus status);

  OrderFdParcelable addTip(String orderId, String paymentId, long amount, boolean online, out ResultStatus status);

  PaymentFdParcelable pay(String orderId, in PaymentRequestFdParcelable fdPaymentRequest, boolean isAllowOffline, String note, out ResultStatus status);

  OrderFdParcelable addPayment(String orderId, in PaymentFdParcelable fdPayment, in LineItemListFdParcelable fdLineItems, out ResultStatus status);

  OrderFdParcelable voidPayment(String orderId, String paymentId, out ResultStatus status);

  CreditFdParcelable addCredit(String orderId, in CreditFdParcelable fdCredit, out ResultStatus status);

  OrderFdParcelable deleteCredit(String orderId, String creditId, out ResultStatus status);

  RefundFdParcelable addRefund(String orderId, in RefundFdParcelable refund, out ResultStatus status);

  OrderFdParcelable deleteRefund(String orderId, String refundId, out ResultStatus status);

  boolean deleteOrderOnline(String orderId, out ResultStatus status);

  OrderFdParcelable addBatchLineItemModifications(String orderId, in List<String> lineItemIds, in ModifierFdParcelable fdModifier, int quantity, out ResultStatus status);

  OrderFdParcelable addBatchLineItemDiscounts(String orderId, in List<String> lineItemIds, in DiscountListFdParcelable discounts, out ResultStatus status);

  LineItemMapFdParcelable createLineItemsFrom(String sourceOrderId, String destinationOrderId, in List<String> lineItemIds, out ResultStatus status);

  boolean fire(String sourceOrderId, out ResultStatus status);

  PaymentFdParcelable updatePayment(String orderId, in PaymentFdParcelable fdPayment, out ResultStatus status);

  OrderFdParcelable voidPayment2(String orderId, String paymentId, String iccContainer, in VoidReason reason, String source, out ResultStatus status);

  OrderFdParcelable removePayment(String orderId, String paymentId, out ResultStatus status);

  RefundFdParcelable addRefundOffline(String orderId, in RefundFdParcelable fdRefund, out ResultStatus status);

  RefundFdParcelable refund(String orderId, in RefundFdParcelable fdRefund, out ResultStatus status);

  void addOnOrderUpdatedListener2(IOnOrderUpdateListener2 listener);

  void removeOnOrderUpdatedListener2(IOnOrderUpdateListener2 listener);

  DiscountFdParcelable addDiscount2(String orderId, in DiscountFdParcelable fdDiscount, out ResultStatus status);

  DiscountFdParcelable addLineItemDiscount2(String orderId, String lineItemId, in DiscountFdParcelable fdDiscount, out ResultStatus status);

  OrderFdParcelable addPayment2(String orderId, in PaymentFdParcelable fdPayment, in LineItemListFdParcelable fdLineItems, out ResultStatus status);

  boolean fire2(String sourceOrderid, boolean requireAllItems, out ResultStatus status);

  LineItemMapFdParcelable createLineItemsFrom2(String sourceOrderId, String destinationOrderId, in List<String> lineItemIds, in boolean copyPrinted, in boolean broadcastLineItems, out ResultStatus status);

  boolean deleteOrder2(String orderId, in boolean allowDeleteIfLineItemPrinted, out ResultStatus status);

  PaymentListFdParcelable getPendingPayments(out ResultStatus status);

  CreditRefundFdParcelable addCreditRefund(String orderId, in CreditRefundFdParcelable creditRefund, out ResultStatus status);

  OrderFdParcelable deleteCreditRefund(String orderId, String creditRefundId, out ResultStatus status);

  OrderFdParcelable addPreAuth(String orderId, in PaymentFdParcelable preAuth, out ResultStatus status);

  OrderFdParcelable capturePreAuth(String orderId, in PaymentFdParcelable preAuth, in LineItemListFdParcelable fdLineItems, out ResultStatus status);

  OrderFdParcelable voidPreAuth(String orderId, String preAuthId, String iccContainer, in VoidReason voidReason, String source, out ResultStatus status);

  OrderFdParcelable voidPreAuthOnline(String orderId, String preAuthId, String iccContainer, in VoidReason voidReason, String source, out ResultStatus status);

  /**
   * Delete {@link LineItem}s from an {@link Order}.
   *
   * @param orderId The ID of the {@link Order} from which to delete the line items.
   * @param lineItemIds The {@link LineItem} IDs to delete.
   * @param reason optional. Why was the line item removed?
   * @param clientEventType optional. What app did the delete come from?
   * @return The updated {@link Order}.
   */
  OrderFdParcelable deleteLineItemsWithReason(String orderId, in List<String> lineItemIds, in String reason, in ClientEventType clientEventType, out ResultStatus status);

  OrderFdParcelable voidPaymentWithCard(String orderId, String paymentId, String iccContainer, in PaymentRequestCardDetails card, in VoidReason reason, String source, out ResultStatus status);

  /**
   * Get list of lineitem ids for the order that has printtag to print.
   * The items that are already printed are not part of the list
   *
   * @param orderId The ID of the {@link Order} from which to delete the line items.
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
   *
  */
  OrderFdParcelable addPrintGroup(String orderId, in PrintGroupFdParcelable fdPrintGroup, out ResultStatus status);
}
