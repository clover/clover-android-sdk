package com.clover.sdk.v1.order;

import com.clover.sdk.v1.inventory.Discount;
import com.clover.sdk.v1.inventory.Item;
import com.clover.sdk.v1.inventory.Modifier;
import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v1.order.Adjustment;
import com.clover.sdk.v1.order.LineItem;
import com.clover.sdk.v1.order.Modification;
import com.clover.sdk.v1.order.Order;
import com.clover.sdk.v1.order.OrderSummary;
import com.clover.sdk.v1.order.IOnOrderUpdateListener;

interface IOrderService {
  void addOnOrderUpdatedListener(IOnOrderUpdateListener listener);

  void removeOnOrderUpdatedListener(IOnOrderUpdateListener listener);

  List<OrderSummary> getOrders(int offset, int count, out ResultStatus status);

  Order getOrder(String orderId, out ResultStatus status);

  Order create(out ResultStatus status);

  void deleteOrder(String orderId, out ResultStatus status);

  void setTitle(String orderId, String title, out ResultStatus status);

  void setNote(String orderId, String note, out ResultStatus status);

  void setType(String orderId, String typeId, out ResultStatus status);

  void deleteType(String orderId, out ResultStatus status);

  void setState(String orderId, String state, out ResultStatus status);

  void setTotal(String orderId, long total, out ResultStatus status);

  void setGroupLineItems(String orderId, boolean groupLineItems, out ResultStatus status);

  void setTaxRemoved(String orderId, boolean taxRemoved, out ResultStatus status);

  void setTestMode(String orderId, boolean testMode, out ResultStatus status);

  void setManualTransaction(String orderId, boolean manualTransaction, out ResultStatus status);

  void setCustomer(String orderId, String customerId, out ResultStatus status);

  void addServiceCharge(String orderId, String serviceChargeId, out ResultStatus status);

  void deleteServiceCharge(String orderId, String serviceChargeId, out ResultStatus status);

  Adjustment addAdjustment(String orderId, in Discount discount, String note, out ResultStatus status);

  void deleteAdjustment(String orderId, in String adjustmentId, out ResultStatus status);

  LineItem addLineItemById(String orderId, String itemId, int unitQuantity, String binName, String userData, out ResultStatus status);

  LineItem addLineItem(String orderId, in Item item, int unitQuantity, String binName, String userData, out ResultStatus status);

  void deleteLineItem(String orderId, in String lineItemId, out ResultStatus status);

  void setLineItemNote(String orderId, in String lineItemId, String note, out ResultStatus status);

  Modification addLineItemModification(String orderId, String lineItemId, in Modifier modifier, out ResultStatus status);

  void deleteLineItemModification(String orderId, in String lineItemId, in String modificationId, out ResultStatus status);

  Adjustment addLineItemAdjustment(String orderId, String lineItemId, in Discount discount, String note, out ResultStatus status);

  void deleteLineItemAdjustment(String orderId, in String lineItemId, in String adjustmentId, out ResultStatus status);

  LineItem exchangeItemById(String orderId, String oldLineItemId, String itemId, int unitQuantity, String binName, String userData, out ResultStatus status);

  LineItem exchangeItem(String orderId, String oldLineItemId, in Item item, int unitQuantity, String binName, String userData, out ResultStatus status);

  LineItem addCustomLineItem(String orderId, in String name, long amount, boolean taxable, String binName, String userData, out ResultStatus status);
}
