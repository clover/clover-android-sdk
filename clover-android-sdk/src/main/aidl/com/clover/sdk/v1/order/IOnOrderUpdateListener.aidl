package com.clover.sdk.v1.order;

oneway interface IOnOrderUpdateListener {
  void onOrderUpdated(String orderId);
}
