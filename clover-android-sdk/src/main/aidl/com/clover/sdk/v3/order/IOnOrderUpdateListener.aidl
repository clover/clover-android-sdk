package com.clover.sdk.v3.order;

import com.clover.sdk.v3.order.Order;

interface IOnOrderUpdateListener {
  void onOrderUpdated(String orderId, boolean selfChange);
}
