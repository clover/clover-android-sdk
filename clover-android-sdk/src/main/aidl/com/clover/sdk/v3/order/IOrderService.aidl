package com.clover.sdk.v3.order;

import com.clover.sdk.v3.inventory.Item;
import com.clover.sdk.v3.inventory.Modifier;
import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v3.order.Discount;
import com.clover.sdk.v3.order.LineItem;
import com.clover.sdk.v3.order.Modification;
import com.clover.sdk.v3.order.Order;
import com.clover.sdk.v3.order.OrderSummary;
import com.clover.sdk.v3.order.IOnOrderUpdateListener;
import com.clover.sdk.v3.order.IOnOrderUpdateListener2;
import com.clover.sdk.v3.payments.Credit;
import com.clover.sdk.v3.payments.Refund;
import com.clover.sdk.v3.payments.Payment;
import com.clover.sdk.v3.pay.PaymentRequest;
import com.clover.sdk.v3.order.VoidReason;

interface IOrderService {
  void addOnOrderUpdatedListener(IOnOrderUpdateListener listener);

  void removeOnOrderUpdatedListener(IOnOrderUpdateListener listener);

  Order getOrder(String orderId, out ResultStatus status);

  List<Order> getOrders(in List<String> orderIds, out ResultStatus status);

  Order createOrder(in Order order, out ResultStatus status);

  Order updateOrder(in Order order, out ResultStatus status);

  boolean deleteOrder(String orderId, out ResultStatus status);

  Order addServiceCharge(String orderId, String serviceChargeId, out ResultStatus status);

  Order deleteServiceCharge(String orderId, String serviceChargeId, out ResultStatus status);

  LineItem addFixedPriceLineItem(String orderId, String itemId, String binName, String userData, out ResultStatus status);

  LineItem addPerUnitLineItem(String orderId, String itemId, int unitQuantity, String binName, String userData, out ResultStatus status);

  LineItem addVariablePriceLineItem(String orderId, String itemId, long price, String binName, String userData, out ResultStatus status);

  LineItem addCustomLineItem(String orderId, in LineItem lineItem, boolean isTaxable, out ResultStatus status);

  List<LineItem> updateLineItems(String orderId, in List<LineItem> lineItemIds, out ResultStatus status);

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

  Order addTip(String orderId, String paymentId, long amount, boolean online, out ResultStatus status);

  Payment pay(String orderId, in PaymentRequest paymentRequest, boolean isAllowOffline, String note, out ResultStatus status);

  Order addPayment(String orderId, in Payment payment, in List<LineItem> lineItems, out ResultStatus status);

  Order voidPayment(String orderId, String paymentId, out ResultStatus status);

  Credit addCredit(String orderId, in Credit payment, out ResultStatus status);

  Order deleteCredit(String orderId, String creditId, out ResultStatus status);

  Refund addRefund(String orderId, in Refund payment, out ResultStatus status);

  Order deleteRefund(String orderId, String refundId, out ResultStatus status);

  boolean deleteOrderOnline(String orderId, out ResultStatus status);

  Order addBatchLineItemModifications(String orderId, in List<String> lineItemIds, in Modifier modifier, int quantity, out ResultStatus status);

  Order addBatchLineItemDiscounts(String orderId, in List<String> lineItemIds, in List<Discount> discounts, out ResultStatus status);

  Map createLineItemsFrom(String sourceOrderId, String destinationOrderId, in List<String> lineItemIds, out ResultStatus status);

  boolean fire(String sourceOrderId, out ResultStatus status);

  Payment updatePayment(String orderId, in Payment payment, out ResultStatus status);

  Order voidPayment2(String orderId, String paymentId, String iccContainer, in VoidReason reason, String source, out ResultStatus status);

  Order removePayment(String orderId, String paymentId, out ResultStatus status);

  Refund addRefundOffline(String orderId, in Refund payment, out ResultStatus status);

  Refund refund(String orderId, in Refund payment, out ResultStatus status);

  void addOnOrderUpdatedListener2(IOnOrderUpdateListener2 listener);

  void removeOnOrderUpdatedListener2(IOnOrderUpdateListener2 listener);

  Discount addDiscount2(String orderId, in Discount discount, out ResultStatus status);

  Discount addLineItemDiscount2(String orderId, String lineItemId, in Discount discount, out ResultStatus status);
}
