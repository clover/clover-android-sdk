/*
 * Copyright (C) 2016 Clover Network, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clover.sdk.v3.order;

import com.clover.common.payments.VoidExtraData;
import com.clover.sdk.FdParcelable;
import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v1.ServiceConnector;
import com.clover.sdk.v1.ServiceException;
import com.clover.sdk.v3.inventory.Modifier;
import com.clover.sdk.v3.inventory.ModifierFdParcelable;
import com.clover.sdk.v3.onlineorder.OrderState;
import com.clover.sdk.v3.onlineorder.Reason;
import com.clover.sdk.v3.pay.PaymentRequest;
import com.clover.sdk.v3.pay.PaymentRequestCardDetails;
import com.clover.sdk.v3.pay.PaymentRequestFdParcelable;
import com.clover.sdk.v3.payments.AdditionalChargeAmount;
import com.clover.sdk.v3.payments.Credit;
import com.clover.sdk.v3.payments.CreditFdParcelable;
import com.clover.sdk.v3.payments.CreditRefund;
import com.clover.sdk.v3.payments.CreditRefundFdParcelable;
import com.clover.sdk.v3.payments.Payment;
import com.clover.sdk.v3.payments.PaymentFdParcelable;
import com.clover.sdk.v3.payments.Refund;
import com.clover.sdk.v3.payments.RefundFdParcelable;
import com.clover.sdk.v3.payments.TransactionInfo;

import android.accounts.Account;
import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Service connector for {@link IOrderServiceV3_1}. Please see that class for documentation on the
 * RPC methods. Developers should use {@link OrderConnector} instead of using this class directly.
 *
 * @see OrderConnector
 * @see IOrderServiceV3_1
 * @see ServiceConnector
 * @see Order
 * @see LineItem
 */
public class OrderV31Connector extends ServiceConnector<IOrderServiceV3_1> {
  private static final String SERVICE_HOST = "com.clover.engine";

  private final List<WeakReference<OnOrderUpdateListener>> mOnOrderChangedListener = new CopyOnWriteArrayList<WeakReference<OnOrderUpdateListener>>();
  private final List<WeakReference<OnOrderUpdateListener2>> mOnOrderChangedListener2 = new CopyOnWriteArrayList<WeakReference<OnOrderUpdateListener2>>();

  /**
   * Constructs a new OrderConnector object.
   *
   * @param context the Context object, required for establishing a connection to
   *                the service.
   * @param account the Account to use with the service.
   * @param client  an optional object implementing the OnServiceConnectedListener
   *                interface, for receiving connection notifications from the service.
   */
  public OrderV31Connector(Context context, Account account, OnServiceConnectedListener client) {
    super(context, account, client);
  }

  @Override
  protected String getServiceIntentAction() {
    return OrderIntent.ACTION_ORDER_SERVICE_V3_1;
  }

  @Override
  protected String getServiceIntentPackage() {
    return SERVICE_HOST;
  }

  @Override
  protected int getServiceIntentVersion() {
    return 3;
  }

  @Override
  protected IOrderServiceV3_1 getServiceInterface(IBinder iBinder) {
    return IOrderServiceV3_1.Stub.asInterface(iBinder);
  }

  /**
   * Interface used to allow application to be notified of order updates.
   */
  public interface OnOrderUpdateListener {
    /**
     *
     * @param orderId The ID of the updated order.
     * @param selfChange True if the update was triggered by the listening application running on the same device.
     */
    void onOrderUpdated(String orderId, boolean selfChange);
  }

  private static class OnOrderUpdateListenerParent extends IOnOrderUpdateListener.Stub {

    private OrderV31Connector mConnector;

    private OnOrderUpdateListenerParent(OrderV31Connector connector) {
      mConnector = connector;
    }

    @Override
    public void onOrderUpdated(String orderId, boolean selfChange) throws RemoteException {
      final OrderV31Connector orderConnector = mConnector;

      if (orderConnector == null) {
        return; // Shouldn't get here, but bail just in case
      }
      if (orderConnector.mOnOrderChangedListener != null && !orderConnector.mOnOrderChangedListener.isEmpty()) {
        // NOTE: because of the use of CopyOnWriteArrayList, we *must* use an iterator to perform the dispatching. The
        // iterator is a safe guard against listeners that could mutate the list by calling the add/remove methods. This
        // array never changes during the lifetime of the iterator, so interference is impossible and the iterator is guaranteed
        // not to throw ConcurrentModificationException.
        for (WeakReference<OnOrderUpdateListener> weakReference : orderConnector.mOnOrderChangedListener) {
          OnOrderUpdateListener listener = weakReference.get();
          if (listener != null) {
            orderConnector.postOnOrderUpdated(orderId, selfChange, listener);
          }
        }
      }
    }

    // This method must be called when the callback is no longer needed to prevent a memory leak. Due to the design of
    // AIDL services Android unnecessarily retains pointers to otherwise unreferenced instances of this class which in
    // turn are referencing Context objects that consume large amounts of memory.
    public void destroy() {
      mConnector = null;
    }
  }

  private OnOrderUpdateListenerParent mListener;

  private void postOnOrderUpdated(final String orderId, final boolean selfChange, final OnOrderUpdateListener listener) {
    mHandler.post(new Runnable() {
      @Override
      public void run() {
        listener.onOrderUpdated(orderId, selfChange);
      }
    });
  }

  protected void notifyServiceConnected(OnServiceConnectedListener client) {
    super.notifyServiceConnected(client);

    try {
      if (mListener == null) {
        mListener = new OnOrderUpdateListenerParent(this);
      }
      mService.addOnOrderUpdatedListener(mListener);

      if (mListener2 == null) {
        mListener2 = new OnOrderUpdateListenerParent2(this);
      }
      mService.addOnOrderUpdatedListener2(mListener2);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void disconnect() {
    mOnOrderChangedListener.clear();
    if (mListener != null) {
      if (mService != null) {
        try {
          mService.removeOnOrderUpdatedListener(mListener);
        } catch (RemoteException e) {
          e.printStackTrace();
        }
      }
      mListener.destroy();
      mListener = null;
    }
    if (mListener2 != null) {
      if (mService != null) {
        try {
          mService.removeOnOrderUpdatedListener2(mListener2);
        } catch (RemoteException e) {
          e.printStackTrace();
        }
      }
      mListener2.destroy();
      mListener2 = null;
    }
    super.disconnect();
  }

  public Order getOrder(final String orderId) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.getOrder(orderId, status));
      }
    });
  }

  public List<Order> getOrders(final List<String> orderIds) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, List<Order>>() {
      @Override
      public List<Order> call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.getOrders(orderIds, status));
      }
    });
  }

  public List<Payment> getPendingPayments() throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, List<Payment>>() {
      @Override
      public List<Payment> call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.getPendingPayments(status));
      }
    });
  }

  public Order createOrder(final Order order) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.createOrder(new OrderFdParcelable(order), status));
      }
    });
  }

  public Order updateOrder(final Order order) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.updateOrder(new OrderFdParcelable(order), status));
      }
    });
  }

  public boolean deleteOrder(final String orderId) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Boolean>() {
      @Override
      public Boolean call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return service.deleteOrder(orderId, status);
      }
    });
  }

  public boolean deleteOrderOnline(final String orderId) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Boolean>() {
      @Override
      public Boolean call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return service.deleteOrderOnline(orderId, status);
      }
    });
  }

  public Order addServiceCharge(final String orderId, final String serviceChargeId) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.addServiceCharge(orderId, serviceChargeId, status));
      }
    });
  }

  public Order deleteServiceCharge(final String orderId, final String serviceChargeId) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.deleteServiceCharge(orderId, serviceChargeId, status));
      }
    });
  }

  public LineItem addFixedPriceLineItem(final String orderId, final String itemId, final String binName, final String userData) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, LineItem>() {
      @Override
      public LineItem call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.addFixedPriceLineItem(orderId, itemId, binName, userData, status));
      }
    });
  }

  public LineItem addPerUnitLineItem(final String orderId, final String itemId, final int unitQuantity, final String binName, final String userData) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, LineItem>() {
      @Override
      public LineItem call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.addPerUnitLineItem(orderId, itemId, unitQuantity, binName, userData, status));
      }
    });
  }

  public LineItem addVariablePriceLineItem(final String orderId, final String itemId, final long price, final String binName, final String userData) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, LineItem>() {
      @Override
      public LineItem call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.addVariablePriceLineItem(orderId, itemId, price, binName, userData, status));
      }
    });
  }

  public List<LineItem> addFixedPriceLineItems(final String orderId, final String itemId, final String binName, final String userData, final int numItems) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, List<LineItem>>() {
      @Override
      public List<LineItem> call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.addFixedPriceLineItems(orderId, itemId, binName, userData, numItems, status));
      }
    });
  }

  public List<LineItem> addPerUnitLineItems(final String orderId, final String itemId, final int unitQuantity, final String binName, final String userData, final int numItems) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, List<LineItem>>() {
      @Override
      public List<LineItem> call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.addPerUnitLineItems(orderId, itemId, unitQuantity, binName, userData, numItems, status));
      }
    });
  }

  public List<LineItem> addVariablePriceLineItems(final String orderId, final String itemId, final long price, final String binName, final String userData, final int numItems) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, List<LineItem>>() {
      @Override
      public List<LineItem> call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.addVariablePriceLineItems(orderId, itemId, price, binName, userData, numItems, status));
      }
    });
  }

  public LineItem addCustomLineItem(final String orderId, final LineItem lineItem, final boolean isTaxable) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, LineItem>() {
      @Override
      public LineItem call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.addCustomLineItem(orderId, new LineItemFdParcelable(lineItem), isTaxable, status));
      }
    });
  }

  public List<LineItem> splitLineItems(final String orderId, final List<String> lineItemIds, final List<String> binNames) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, List<LineItem>>() {
      @Override
      public List<LineItem> call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.splitLineItems(orderId, lineItemIds, binNames, status));
      }
    });
  }

  public List<LineItem> updateLineItems(final String orderId, final List<LineItem> lineItems) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, List<LineItem>>() {
      @Override
      public List<LineItem> call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.updateLineItems(orderId, new LineItemListFdParcelable(lineItems), status));
      }
    });
  }

  public Order deleteLineItems(final String orderId, final List<String> lineItemIds) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.deleteLineItems(orderId, lineItemIds, status));
      }
    });
  }

  public Order deleteLineItems2(final String orderId, final List<String> lineItemIds, final ClientEventType clientEventType, final String approvedByEmployeeId) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute((service, status) -> {
      return getValue(service.deleteLineItems2(orderId, lineItemIds, clientEventType, approvedByEmployeeId, status));
    });
  }

  /**
   * This method is being deprecated as there was no real usage of the "reason" parameter.
   * Note that there is no direct replacement method.
   * @deprecated you can use {@link OrderV31Connector#deleteLineItems2}
   */
  @Deprecated
  public Order deleteLineItemsWithReason(final String orderId, final List<String> lineItemIds, final String reason, final ClientEventType clientEventType) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.deleteLineItemsWithReason(orderId, lineItemIds, reason, clientEventType, status));
      }
    });
  }

  @Deprecated
  public List<LineItem> copyLineItems(final String sourceOrderId, final String destinationOrderId, final List<String> srclineItemIds) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, List<LineItem>>() {
      @Override
      public List<LineItem> call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.copyLineItems(sourceOrderId, destinationOrderId, srclineItemIds, status));
      }
    });
  }

  public Map<String, List<LineItem>> createLineItemsFrom(final String sourceOrderId, final String destinationOrderId, final List<String> srclineItemIds) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Map<String, List<LineItem>>>() {
      @Override
      public Map<String, List<LineItem>> call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.createLineItemsFrom(sourceOrderId, destinationOrderId, srclineItemIds, status));
      }
    });
  }

  public Order setLineItemNote(final String orderId, final String lineItemId, final String note) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.setLineItemNote(orderId, lineItemId, note, status));
      }
    });
  }

  public Order addLineItemModification(final String orderId, final String lineItemId, final Modifier modifier) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.addLineItemModification(orderId, lineItemId, new ModifierFdParcelable(modifier), status));
      }
    });
  }

  public Order addBatchLineItemModification(final String orderId, final List<String> lineItemIds, final Modifier modifier, final int quantity) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.addBatchLineItemModifications(orderId, lineItemIds, new ModifierFdParcelable(modifier), quantity, status));
      }
    });
  }

  public Order deleteLineItemModifications(final String orderId, final String lineItemId, final List<String> modificationIds) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.deleteLineItemModifications(orderId, lineItemId, modificationIds, status));
      }
    });
  }

  public LineItem exchangeItem(final String orderId, final String oldLineItemId, final String itemId, final String binName, final String userData) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, LineItem>() {
      @Override
      public LineItem call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.exchangeItem(orderId, oldLineItemId, itemId, binName, userData, status));
      }
    });
  }

  public Order addPrintGroup(final String orderId, final PrintGroup printGroup) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.addPrintGroup(orderId, new PrintGroupFdParcelable(printGroup), status));
      }
    });
  }

  public Order addDiscount(final String orderId, final Discount discount) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.addDiscount(orderId, new DiscountFdParcelable(discount), status));
      }
    });
  }

  public Discount addDiscount2(final String orderId, final Discount discount) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Discount>() {
      @Override
      public Discount call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.addDiscount2(orderId, new DiscountFdParcelable(discount), status));
      }
    });
  }

  public Order deleteDiscounts(final String orderId, final List<String> discountIds) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.deleteDiscounts(orderId, discountIds, status));
      }
    });
  }

  public Order addLineItemDiscount(final String orderId, final String lineItemId, final Discount discount) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.addLineItemDiscount(orderId, lineItemId, new DiscountFdParcelable(discount), status));
      }
    });
  }

  public Discount addLineItemDiscount2(final String orderId, final String lineItemId, final Discount discount) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Discount>() {
      @Override
      public Discount call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.addLineItemDiscount2(orderId, lineItemId, new DiscountFdParcelable(discount), status));
      }
    });
  }


  public Order addBatchLineItemDiscounts(final String orderId, final List<String> lineItemIds, final List<Discount> discounts) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.addBatchLineItemDiscounts(orderId, lineItemIds, new DiscountListFdParcelable(discounts), status));
      }
    });
  }

  public Order deleteLineItemDiscounts(final String orderId, final String lineItemId, final List<String> discountIds) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.deleteLineItemDiscounts(orderId, lineItemId, discountIds, status));
      }
    });
  }

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  public Order addTip(final String orderId, final String paymentId, final long amount, final boolean online) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.addTip(orderId, paymentId, amount, online, status));
      }
    });
  }

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  public Payment pay(final String orderId, final PaymentRequest paymentRequest, final boolean isAllowOffline, final String note) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Payment>() {
      @Override
      public Payment call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.pay(orderId, new PaymentRequestFdParcelable(paymentRequest), isAllowOffline, note, status));
      }
    });
  }

  /**
   * Not available to non-Clover apps.
   * @deprecated Use {@link #addPayment2}.
   * @y.exclude
   */
  @Deprecated
  public Order addPayment(final String orderId, final Payment payment, final List<LineItem> lineItems) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.addPayment(orderId, new PaymentFdParcelable(payment), new LineItemListFdParcelable(lineItems), status));
      }
    });
  }

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  public Order addPayment2(final String orderId, final Payment payment, final List<LineItem> lineItems) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.addPayment2(orderId, new PaymentFdParcelable(payment), new LineItemListFdParcelable(lineItems), status));
      }
    });
  }

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  public Order removePayment(final String orderId, final String paymentId) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.removePayment(orderId, paymentId, status));
      }
    });
  }

  /**
   * Not available to non-Clover apps.
   * @deprecated Use {@link #voidPayment2}.
   * @y.exclude
   */
  @Deprecated
  public Order voidPayment(final String orderId, final String paymentId) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.voidPayment(orderId, paymentId, status));
      }
    });
  }

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  public Order voidPayment2(final String orderId, final String paymentId, final String iccContainer, final VoidReason reason, final String source) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.voidPayment2(orderId, paymentId, iccContainer, reason, source, status));
      }
    });
  }

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  public Order voidPayment3(final String orderId, final String paymentId, final String iccContainer, final Map<String, String> passThroughExtras, final VoidReason reason, final String source) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.voidPayment3(orderId, paymentId, iccContainer, passThroughExtras, reason, source, status));
      }
    });
  }

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  public Order voidPaymentWithCard(final String orderId, final String paymentId, final String iccContainer,
                                   final PaymentRequestCardDetails card, final VoidReason reason, final String source) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.voidPaymentWithCard(orderId, paymentId, iccContainer, card, reason, source, status));
      }
    });
  }

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  public Order voidPaymentCardPresent(final String orderId, final String paymentId, final String iccContainer,
                                      final PaymentRequestCardDetails card, final TransactionInfo transactionInfo, final VoidReason reason, final String source) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.voidPaymentCardPresent(orderId, paymentId, iccContainer, card, transactionInfo, reason, source, status));
      }
    });
  }

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  public Order voidPaymentCardPresent2(final String orderId, final String paymentId, final String iccContainer,
                                      final PaymentRequestCardDetails card, final TransactionInfo transactionInfo, final Map<String, String> passThroughExtras, final VoidReason reason, final String source) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.voidPaymentCardPresent2(orderId, paymentId, iccContainer, card, transactionInfo, passThroughExtras, reason, source, status));
      }
    });
  }

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  public Order voidPaymentCardPresent3(final String orderId, final String paymentId, final String iccContainer,
                                       final PaymentRequestCardDetails card, final TransactionInfo transactionInfo, final Map<String, String> passThroughExtras, final VoidReason reason, VoidExtraData voidExtraData, final String source) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.voidPaymentCardPresent3(orderId, paymentId, iccContainer, card, transactionInfo, passThroughExtras, reason, voidExtraData, source, status));
      }
    });
  }
  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  public Credit addCredit(final String orderId, final Credit credit) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Credit>() {
      @Override
      public Credit call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.addCredit(orderId, new CreditFdParcelable(credit), status));
      }
    });
  }

  public Order deleteCredit(final String orderId, final String creditId) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.deleteCredit(orderId, creditId, status));
      }
    });
  }

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  public Refund addRefund(final String orderId, final Refund refund) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Refund>() {
      @Override
      public Refund call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.addRefund(orderId, new RefundFdParcelable(refund), status));
      }
    });
  }

  public Order deleteRefund(final String orderId, final String refundId) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.deleteRefund(orderId, refundId, status));
      }
    });
  }

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  public CreditRefund addCreditRefund(final String orderId, final CreditRefund creditRefund) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, CreditRefund>() {
      @Override
      public CreditRefund call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.addCreditRefund(orderId, new CreditRefundFdParcelable(creditRefund), status));
      }
    });
  }

  public Order deleteCreditRefund(final String orderId, final String creditRefundId) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.deleteCreditRefund(orderId, creditRefundId, status));
      }
    });
  }

  public boolean fire(final String orderId) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Boolean>() {
      @Override
      public Boolean call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return service.fire(orderId, status);
      }
    });
  }

  public boolean fire2(final String orderId, final boolean requireAllItems) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Boolean>() {
      @Override
      public Boolean call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return service.fire2(orderId, requireAllItems, status);
      }
    });
  }

  public boolean firePrintGroups(final String orderId, @Nullable final List<String> printGroupIds, final boolean requireAllItems)
      throws RemoteException, ServiceException, BindingException, ClientException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Boolean>() {
      @Override
      public Boolean call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return service.firePrintGroups(orderId, printGroupIds, requireAllItems, status);
      }
    });
  }

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  public Payment updatePayment(final String orderId, final Payment payment) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Payment>() {
      @Override
      public Payment call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.updatePayment(orderId, new PaymentFdParcelable(payment), status));
      }
    });
  }

  public void addOnOrderChangedListener(OnOrderUpdateListener listener) {
    mOnOrderChangedListener.add(new WeakReference<OnOrderUpdateListener>(listener));
  }

  public void removeOnOrderChangedListener(OnOrderUpdateListener listener) {
    if (mOnOrderChangedListener != null && !mOnOrderChangedListener.isEmpty()) {
      WeakReference<OnOrderUpdateListener> listenerWeakReference = null;
      for (WeakReference<OnOrderUpdateListener> weakReference : mOnOrderChangedListener) {
        OnOrderUpdateListener listener1 = weakReference.get();
        if (listener1 != null && listener1 == listener) {
          listenerWeakReference = weakReference;
          break;
        }
      }
      if (listenerWeakReference != null) {
        mOnOrderChangedListener.remove(listenerWeakReference);
      }
    }
  }

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  public Refund addRefundOffline(final String orderId, final Refund refund) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Refund>() {
      @Override
      public Refund call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.addRefundOffline(orderId, new RefundFdParcelable(refund), status));
      }
    });
  }

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  public Refund refund(final String orderId, final Refund refund) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Refund>() {
      @Override
      public Refund call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.refund(orderId, new RefundFdParcelable(refund), status));
      }
    });
  }

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  public Refund refund2(final String orderId, final Refund refund, final Map<String, String> passThroughExtras) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Refund>() {
      @Override
      public Refund call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.refund2(orderId, new RefundFdParcelable(refund), passThroughExtras, status));
      }
    });
  }

  public Order addPreAuth(final String orderId, final Payment preAuth) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.addPreAuth(orderId, new PaymentFdParcelable(preAuth), status));
      }
    });
  }

  public Order capturePreAuth(final String orderId, final Payment preAuth, final List<LineItem> lineItems) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.capturePreAuth(orderId, new PaymentFdParcelable(preAuth), new LineItemListFdParcelable(lineItems), status));
      }
    });
  }

  public Order voidPreAuth(final String orderId, final String preAuthId, final String iccContainer, final VoidReason voidReason, final String source) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.voidPreAuth(orderId, preAuthId, iccContainer, voidReason, source, status));
      }
    });
  }

  public Order voidPreAuthOnline(final String orderId, final String preAuthId, final String iccContainer, final VoidReason voidReason, final String source) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.voidPreAuthOnline(orderId, preAuthId, iccContainer, voidReason, source, status));
      }
    });
  }

  public Order cleanUpPreAuthAfterTransaction(final String orderId,
                                              final VoidReason voidReason) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.cleanUpPreAuthAfterTransaction(orderId, voidReason, status));
      }
    });
  }

  public interface OnOrderUpdateListener2 {
    void onOrderUpdated(String orderId, boolean selfChange);

    void onOrderCreated(String orderId);

    void onOrderDeleted(String orderId);

    void onOrderDiscountAdded(String orderId, String discountId);

    void onOrderDiscountsDeleted(String orderId, List<String> discountIds);

    void onLineItemsAdded(String orderId, List<String> lineItemIds);

    void onLineItemsUpdated(String orderId, List<String> lineItemIds);

    void onLineItemsDeleted(String orderId, List<String> lineItemIds);

    void onLineItemModificationsAdded(String orderId, List<String> lineItemIds, List<String> modificationIds);

    void onLineItemDiscountsAdded(String orderId, List<String> lineItemIds, List<String> discountIds);

    void onLineItemExchanged(String orderId, String oldLineItemId, String newLineItemId);

    void onPaymentProcessed(String orderId, String paymentId);

    void onRefundProcessed(String orderId, String refundId);

    void onCreditProcessed(String orderId, String creditId);
  }

  private static class OnOrderUpdateListenerParent2 extends IOnOrderUpdateListener2.Stub {

    private OrderV31Connector mConnector;

    private OnOrderUpdateListenerParent2(OrderV31Connector connector) {
      mConnector = connector;
    }

    public interface ListenerRunnable {

      public void run(OnOrderUpdateListener2 listener);
    }

    private void postChange(final ListenerRunnable listenerRunnable) {
      final OrderV31Connector orderConnector = mConnector;

      if (orderConnector == null) {
        return; // Shouldn't get here, but bail just in case
      }

      if (orderConnector.mOnOrderChangedListener2 != null && !orderConnector.mOnOrderChangedListener2.isEmpty()) {
        // NOTE: because of the use of CopyOnWriteArrayList, we *must* use an iterator to perform the dispatching. The
        // iterator is a safe guard against listeners that could mutate the list by calling the add/remove methods. This
        // array never changes during the lifetime of the iterator, so interference is impossible and the iterator is guaranteed
        // not to throw ConcurrentModificationException.
        for (WeakReference<OnOrderUpdateListener2> weakReference : orderConnector.mOnOrderChangedListener2) {
          final OnOrderUpdateListener2 listener = weakReference.get();
          if (listener != null) {
            orderConnector.mHandler.post(new Runnable() {
              public void run() {
                listenerRunnable.run(listener);
              }
            });
          }
        }
      }
    }

    @Override
    public void onOrderUpdated(final String orderId, final boolean selfChange) throws RemoteException {
      postChange(new ListenerRunnable() {
        public void run(OnOrderUpdateListener2 listener) {
          listener.onOrderUpdated(orderId, selfChange);
        }
      });
    }

    @Override
    public void onOrderCreated(final String orderId) throws RemoteException {
      postChange(new ListenerRunnable() {
        public void run(OnOrderUpdateListener2 listener) {
          listener.onOrderCreated(orderId);
        }
      });
    }

    @Override
    public void onOrderDeleted(final String orderId) throws RemoteException {
      postChange(new ListenerRunnable() {
        public void run(OnOrderUpdateListener2 listener) {
          listener.onOrderDeleted(orderId);
        }
      });
    }

    @Override
    public void onOrderDiscountAdded(final String orderId, final String discountId) throws RemoteException {
      postChange(new ListenerRunnable() {
        public void run(OnOrderUpdateListener2 listener) {
          listener.onOrderDiscountAdded(orderId, discountId);
        }
      });
    }

    @Override
    public void onOrderDiscountsDeleted(final String orderId, final List<String> discountIds) throws RemoteException {
      postChange(new ListenerRunnable() {
        public void run(OnOrderUpdateListener2 listener) {
          listener.onOrderDiscountsDeleted(orderId, discountIds);
        }
      });
    }

    @Override
    public void onLineItemsAdded(final String orderId, final List<String> lineItemIds) throws RemoteException {
      postChange(new ListenerRunnable() {
        public void run(OnOrderUpdateListener2 listener) {
          listener.onLineItemsAdded(orderId, lineItemIds);
        }
      });
    }

    @Override
    public void onLineItemsUpdated(final String orderId, final List<String> lineItemIds) throws RemoteException {
      postChange(new ListenerRunnable() {
        public void run(OnOrderUpdateListener2 listener) {
          listener.onLineItemsUpdated(orderId, lineItemIds);
        }
      });
    }

    @Override
    public void onLineItemsDeleted(final String orderId, final List<String> lineItemIds) throws RemoteException {
      postChange(new ListenerRunnable() {
        public void run(OnOrderUpdateListener2 listener) {
          listener.onLineItemsDeleted(orderId, lineItemIds);
        }
      });
    }

    @Override
    public void onLineItemModificationsAdded(final String orderId, final List<String> lineItemIds, final List<String> modificationIds) throws RemoteException {
      postChange(new ListenerRunnable() {
        public void run(OnOrderUpdateListener2 listener) {
          listener.onLineItemModificationsAdded(orderId, lineItemIds, modificationIds);
        }
      });
    }

    @Override
    public void onLineItemDiscountsAdded(final String orderId, final List<String> lineItemIds, final List<String> discountIds) throws RemoteException {
      postChange(new ListenerRunnable() {
        public void run(OnOrderUpdateListener2 listener) {
          listener.onLineItemDiscountsAdded(orderId, lineItemIds, discountIds);
        }
      });
    }

    @Override
    public void onLineItemExchanged(final String orderId, final String oldLineItemId, final String newLineItemId) throws RemoteException {
      postChange(new ListenerRunnable() {
        public void run(OnOrderUpdateListener2 listener) {
          listener.onLineItemExchanged(orderId, oldLineItemId, newLineItemId);
        }
      });
    }

    @Override
    public void onPaymentProcessed(final String orderId, final String paymentId) throws RemoteException {
      postChange(new ListenerRunnable() {
        public void run(OnOrderUpdateListener2 listener) {
          listener.onPaymentProcessed(orderId, paymentId);
        }
      });
    }

    @Override
    public void onRefundProcessed(final String orderId, final String refundId) throws RemoteException {
      postChange(new ListenerRunnable() {
        public void run(OnOrderUpdateListener2 listener) {
          listener.onRefundProcessed(orderId, refundId);
        }
      });
    }

    @Override
    public void onCreditProcessed(final String orderId, final String creditId) throws RemoteException {
      postChange(new ListenerRunnable() {
        public void run(OnOrderUpdateListener2 listener) {
          listener.onCreditProcessed(orderId, creditId);
        }
      });
    }

    // This method must be called when the callback is no longer needed to prevent a memory leak. Due to the design of
    // AIDL services Android unnecessarily retains pointers to otherwise unreferenced instances of this class which in
    // turn are referencing Context objects that consume large amounts of memory.
    public void destroy() {
      mConnector = null;
    }
  }

  private OnOrderUpdateListenerParent2 mListener2;

  private void postOnOrderUpdated2(final String orderId, final boolean selfChange, final OnOrderUpdateListener listener) {
    mHandler.post(new Runnable() {
      @Override
      public void run() {
        listener.onOrderUpdated(orderId, selfChange);
      }
    });
  }

  public void addOnOrderChangedListener(OnOrderUpdateListener2 listener) {
    mOnOrderChangedListener2.add(new WeakReference<OnOrderUpdateListener2>(listener));
  }

  public void removeOnOrderChangedListener(OnOrderUpdateListener2 listener) {
    if (mOnOrderChangedListener2 != null && !mOnOrderChangedListener2.isEmpty()) {
      WeakReference<OnOrderUpdateListener2> listenerWeakReference = null;
      for (WeakReference<OnOrderUpdateListener2> weakReference : mOnOrderChangedListener2) {
        OnOrderUpdateListener2 listener1 = weakReference.get();
        if (listener1 != null && listener1 == listener) {
          listenerWeakReference = weakReference;
          break;
        }
      }
      if (listenerWeakReference != null) {
        mOnOrderChangedListener2.remove(listenerWeakReference);
      }
    }
  }

  public Map<String, List<LineItem>> createLineItemsFrom2(final String sourceOrderId, final String destinationOrderId, final List<String> srclineItemIds, final boolean copyPrinted, final boolean broadcastLineItems) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Map<String, List<LineItem>>>() {
      @Override
      public Map<String, List<LineItem>> call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.createLineItemsFrom2(sourceOrderId, destinationOrderId, srclineItemIds, copyPrinted, broadcastLineItems, status));
      }
    });
  }

  public boolean deleteOrder2(final String orderId, final boolean allowDeleteIfLineItemPrinted) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Boolean>() {
      @Override
      public Boolean call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return service.deleteOrder2(orderId, allowDeleteIfLineItemPrinted, status);
      }
    });
  }

  public List<String> getLineItemsToFire(final String orderId) throws RemoteException, ServiceException, BindingException, ClientException {
    return execute(new ServiceCallable<IOrderServiceV3_1, List<String>>() {
      @Override
      public List<String> call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return service.getLineItemsToFire(orderId, status);
      }
    });
  }

  public boolean refire(final String orderId) throws RemoteException, ServiceException, BindingException, ClientException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Boolean>() {
      @Override
      public Boolean call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return service.refire(orderId, status);
      }
    });
  }

  /**
   * Not available to non-Clover apps.
   * @deprecated Use {@link #deleteOrder3}.
   * @y.exclude
   */
  @Deprecated
  public boolean deleteOrderOnline2(final String orderId, final boolean usePermissionForOrderDeletions) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Boolean>() {
      @Override
      public Boolean call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return service.deleteOrderOnline2(orderId, usePermissionForOrderDeletions, status);
      }
    });
  }

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  public boolean deleteOrder3(final String orderId, final boolean deleteOnline, final boolean allowDeleteIfLineItemPrinted, final boolean allowDeleteIfNoEmployeePermission) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Boolean>() {
      @Override
      public Boolean call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return service.deleteOrder3(orderId, deleteOnline, allowDeleteIfLineItemPrinted, allowDeleteIfNoEmployeePermission, status);
      }
    });
  }

  /**
   * Null-safe wrapper around {@link FdParcelable#getValue()}. Call this over
   * {@link FdParcelable#getValue()} directly to protect against remote exceptions
   * resulting in null return values.
   *
   * @param fdp The {@link FdParcelable} on which to call {@link FdParcelable#getValue()}.
   * @param <V> The value type.
   * @param <P> The parcelable type.
   *
   * @return An object of type V, or null if the argument was null or the argument's
   * {@link FdParcelable#getValue()} method returned null.
   */
  private static <V, P extends FdParcelable<V>> V getValue(P fdp) {
    if (fdp == null) {
      return null;
    }
    return fdp.getValue();
  }

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  public CreditRefund vaultedCreditRefund(final String orderId, final String creditId) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, CreditRefund>() {
      @Override
      public CreditRefund call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return service.vaultedCreditRefund(orderId, creditId, status);
      }
    });
  }

  public void updateOnlineOrderState(String orderId, OrderState orderState, Reason reason) throws RemoteException, ClientException, ServiceException, BindingException {
    execute((ServiceRunnable<IOrderServiceV3_1>) (service, status) -> service.updateOnlineOrderState(orderId, orderState, reason, status));
  }

  /**
   * Not available to non-Clover apps.
   * @y.exclude
   */
  public Order addTipWithAdditionalCharges(final String orderId, final String paymentId, final long amount, final List<AdditionalChargeAmount> additionalChargeAmounts, final boolean online) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.addTipWithAdditionalCharges(orderId, paymentId, amount, additionalChargeAmounts, online, status));
      }
    });
  }

  /**
   * Adds service charge to the order
   * @param orderId ID of the order to which service charge is added
   * @param isAutoApplied a flag to indicate if service charge is auto applied
   * @param serviceChargeId the ID of service charge which is to be added to order
   */
  public Order addServiceCharge2(final String orderId, final String serviceChargeId, final boolean isAutoApplied) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderServiceV3_1, Order>() {
      @Override
      public Order call(IOrderServiceV3_1 service, ResultStatus status) throws RemoteException {
        return getValue(service.addServiceCharge2(orderId, serviceChargeId, isAutoApplied, status));
      }
    });
  }
}
