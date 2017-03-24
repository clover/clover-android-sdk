package com.clover.sdk.v3.common;

import com.clover.common2.payments.PayIntent;
import com.clover.sdk.v3.order.LineItem;
import com.clover.sdk.v3.order.Order;
import com.clover.sdk.v3.order.OrderConnector;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public abstract class CreateOrUpdateOrderTask extends ConnectorSafeAsyncTask<String, Void, Order> {
  public static final String LINE_ITEM_DEFAULT_NAME = "Manual Transaction";
  private static final String TAG = CreateOrUpdateOrderTask.class.getSimpleName();
  private final OrderConnector orderConnector;
  private final PayIntent payIntent;
  private final Order order;

  public CreateOrUpdateOrderTask(Context context,
                                 OrderConnector orderConnector,
                                 PayIntent payIntent,
                                 Order order) {
    super(context);

    this.payIntent = payIntent;
    this.order = order;
    this.orderConnector = orderConnector;
  }

  /**
   * 1. Passed no order ID, and no order- ERROR
   * 2. Passed order ID and no order- try to load load, and fail if we can't
   * 3. Passed no order ID and an order- set order ID to order ID of order
   * 4. Passed order ID and order- ensure order IDs match
   */
  @Override
  protected Order doInBackground(String... params) {
    String lineItemName = LINE_ITEM_DEFAULT_NAME;
    if (params != null && params.length > 0 && !params[0].equals("")) {
      lineItemName = params[0];
    }
    if (payIntent.orderId == null && order == null) {
      return createOrder(payIntent.amount, lineItemName);
    }
    if (payIntent.orderId != null && order == null) {
      return loadOrder(payIntent.orderId);
    }
    if (payIntent.orderId == null && order != null) {
      return updateOrder(order);
    }
    // payIntent.orderId != null && order != null
    return updateOrder(order);
  }

  @Override
  protected final void onSafePostExecute(Order order) {
    onOrderCreatedOrUpdated(order);
  }

  protected abstract void onOrderCreatedOrUpdated(Order order);

  private Order loadOrder(String orderId) {
    Order o = null;
    try {
      o = orderConnector.getOrder(orderId);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return o;
  }

  private Order updateOrder(Order order) {
    Order o = null;
    try {
      o = orderConnector.updateOrder(order);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return o;
  }

  private Order createOrder(long amount, String lineItemName) {
    // String lineItemName = getContext().getString(R.string.default_manual_line_item_name);
    try {
      Order order = orderConnector.createOrder(new Order().setManualTransaction(true));

      LineItem customLineItem = new LineItem().setName(lineItemName).setAlternateName("").setPrice(amount);
      customLineItem = orderConnector.addCustomLineItem(order.getId(), customLineItem, false);
      List<LineItem> lineItems;
      if (order.hasLineItems()) {
        lineItems = new ArrayList<LineItem>(order.getLineItems());
      } else {
        lineItems = new ArrayList<LineItem>();
      }
      lineItems.add(customLineItem);
      order.setLineItems(lineItems);

      // TODO: What about taxes/service charge?

      return orderConnector.getOrder(order.getId());
    } catch (Exception e) {
      Log.w(TAG, "create order failed", e);
    }
    return null;
  }
}
