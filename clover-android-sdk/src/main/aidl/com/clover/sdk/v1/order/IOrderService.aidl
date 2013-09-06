package com.clover.sdk.v1.order;

import com.clover.sdk.v1.inventory.Item;
import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v1.order.LineItem;
import com.clover.sdk.v1.order.Order;
import com.clover.sdk.v1.order.IOnOrderUpdateListener;

interface IOrderService {
  void addOnOrderUpdatedListener(IOnOrderUpdateListener listener);

  void removeOnOrderUpdatedListener(IOnOrderUpdateListener listener);

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

  LineItem addLineItemById(String orderId, String itemId, int unitQuantity, String binName, String userData, out ResultStatus status);

  LineItem addLineItem(String orderId, in Item item, int unitQuantity, String binName, String userData, out ResultStatus status);

  void deleteLineItem(String orderId, in String lineItemId, out ResultStatus status);
}
