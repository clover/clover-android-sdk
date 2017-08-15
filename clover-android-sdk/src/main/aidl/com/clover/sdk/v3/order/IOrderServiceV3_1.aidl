package com.clover.sdk.v3.order;

import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v3.order.Modification;
import com.clover.sdk.v3.order.OrderSummary;
import com.clover.sdk.v3.order.IOnOrderUpdateListener;
import com.clover.sdk.v3.order.IOnOrderUpdateListener2;
import com.clover.sdk.v3.order.VoidReason;
import com.clover.sdk.v3.order.OrderFdParcelable;
import com.clover.sdk.v3.order.OrderListFdParcelable;
import com.clover.sdk.v3.order.LineItemFdParcelable;
import com.clover.sdk.v3.order.LineItemListFdParcelable;
import com.clover.sdk.v3.order.LineItemMapFdParcelable;
import com.clover.sdk.v3.order.DiscountFdParcelable;
import com.clover.sdk.v3.order.DiscountListFdParcelable;
import com.clover.sdk.v3.order.ModificationFdParcelable;
import com.clover.sdk.v3.payments.PaymentFdParcelable;
import com.clover.sdk.v3.payments.PaymentListFdParcelable;
import com.clover.sdk.v3.payments.CreditFdParcelable;
import com.clover.sdk.v3.payments.CreditRefundFdParcelable;
import com.clover.sdk.v3.payments.RefundFdParcelable;
import com.clover.sdk.v3.pay.PaymentRequestFdParcelable;
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
}
