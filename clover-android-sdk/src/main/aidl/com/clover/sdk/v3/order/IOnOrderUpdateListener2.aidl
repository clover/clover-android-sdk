package com.clover.sdk.v3.order;

import com.clover.sdk.v3.order.Order;
import com.clover.sdk.v3.order.LineItemInfo;

interface IOnOrderUpdateListener2 {
  void onOrderUpdated(String orderId, boolean selfChange);
  void onOrderCreated(String orderId);
  void onOrderDeleted(String orderId);
  void onOrderDiscountAdded(String orderId, String discountId);
  void onOrderDiscountsDeleted(String orderId, in List<String> discountIds);
  void onLineItemsAdded(String orderId, in List<String> lineItemIds);
  void onLineItemsUpdated(String orderId, in List<String> lineItemIds);
  void onLineItemsDeleted(String orderId, in List<String> lineItemIds);
  void onLineItemModificationsAdded(String orderId, in List<String> lineItemIds, in List<String> modificationIds);
  void onLineItemDiscountsAdded(String orderId, in List<String> lineItemIds, in List<String> discountIds);
  void onLineItemExchanged(String orderId, String oldLineItemId, String newLineItemId);
  void onPaymentProcessed(String orderId, String paymentId);
  void onRefundProcessed(String orderId, String refundId);
  void onCreditProcessed(String orderId, String creditId);
  void onLineItemModificationsDeleted(String orderId, in List<String> lineItemIds, in List<String> modificationIds);
  void onOrderFeeAdded(String orderId, String orderFeeLineItemId);
  void onOrderFeeDeleted(String orderId, String orderFeeLineItemId);
  void onLineItemInfoUpdated(String orderId, String lineItemId, in LineItemInfo lineItemInfo);
}
