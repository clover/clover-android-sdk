/*
 * Copyright (C) 2013 Clover Network, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.clover.sdk.v3.order;

import android.accounts.Account;
import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v1.ServiceConnector;
import com.clover.sdk.v1.ServiceException;
import com.clover.sdk.v3.inventory.Modifier;
import com.clover.sdk.v3.pay.PaymentRequest;
import com.clover.sdk.v3.payments.Credit;
import com.clover.sdk.v3.payments.Payment;
import com.clover.sdk.v3.payments.Refund;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class OrderConnector extends ServiceConnector<IOrderService> {
  private static final String TAG = "OrderConnector";

  private final List<WeakReference<OnOrderUpdateListener>> mOnOrderChangedListener = new CopyOnWriteArrayList<WeakReference<OnOrderUpdateListener>>();

  /**
   * Constructs a new OrderConnector object.
   *
   * @param context the Context object, required for establishing a connection to
   *                the service.
   * @param account the Account to use with the service.
   * @param client  an optional object implementing the OnServiceConnectedListener
   *                interface, for receiving connection notifications from the service.
   */
  public OrderConnector(Context context, Account account, OnServiceConnectedListener client) {
    super(context, account, client);
  }

  @Override
  protected String getServiceIntentAction() {
    return OrderIntent.ACTION_ORDER_SERVICE_V3;
  }

  @Override
  protected int getServiceIntentVersion() {
    return 3;
  }

  @Override
  protected IOrderService getServiceInterface(IBinder iBinder) {
    return IOrderService.Stub.asInterface(iBinder);
  }

  public interface OnOrderUpdateListener {
    void onOrderUpdated(String orderId, boolean selfChange);
  }

  private static class OnOrderUpdateListenerParent extends IOnOrderUpdateListener.Stub {

    private OrderConnector mConnector;

    private OnOrderUpdateListenerParent(OrderConnector connector) {
      mConnector = connector;
    }

    @Override
    public void onOrderUpdated(String orderId, boolean selfChange) throws RemoteException {
      if (mConnector == null) {
        return; // Shouldn't get here, but bail just in case
      }
      if (mConnector.mOnOrderChangedListener != null && !mConnector.mOnOrderChangedListener.isEmpty()) {
        // NOTE: because of the use of CopyOnWriteArrayList, we *must* use an iterator to perform the dispatching. The
        // iterator is a safe guard against listeners that could mutate the list by calling the add/remove methods. This
        // array never changes during the lifetime of the iterator, so interference is impossible and the iterator is guaranteed
        // not to throw ConcurrentModificationException.
        for (WeakReference<OnOrderUpdateListener> weakReference : mConnector.mOnOrderChangedListener) {
          OnOrderUpdateListener listener = weakReference.get();
          if (listener != null) {
            mConnector.postOnOrderUpdated(orderId, selfChange, listener);
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

  ;

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
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void disconnect() {
    mOnOrderChangedListener.clear();
    if (mListener != null) {
      try {
        mService.removeOnOrderUpdatedListener(mListener);
      } catch (RemoteException e) {
        e.printStackTrace();
      }
      mListener.destroy();
      mListener = null;
    }
    super.disconnect();
  }

  public com.clover.sdk.v3.order.Order getOrder(final String orderId) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, com.clover.sdk.v3.order.Order>() {
      @Override
      public com.clover.sdk.v3.order.Order call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.getOrder(orderId, status);
      }
    });
  }

  public List<Order> getOrders(final List<String> orderIds) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, List<Order>>() {
      @Override
      public List<Order> call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.getOrders(orderIds, status);
      }
    });
  }

  public Order createOrder(final Order order) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, Order>() {
      @Override
      public com.clover.sdk.v3.order.Order call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.createOrder(order, status);
      }
    });
  }

  public Order updateOrder(final Order order) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, Order>() {
      @Override
      public com.clover.sdk.v3.order.Order call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.updateOrder(order, status);
      }
    });
  }

  public boolean deleteOrder(final String orderId) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, Boolean>() {
      @Override
      public Boolean call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.deleteOrder(orderId, status);
      }
    });
  }

  public boolean deleteOrderOnline(final String orderId) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, Boolean>() {
      @Override
      public Boolean call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.deleteOrderOnline(orderId, status);
      }
    });
  }

  public Order addServiceCharge(final String orderId, final String serviceChargeId) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, Order>() {
      @Override
      public Order call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.addServiceCharge(orderId, serviceChargeId, status);
      }
    });
  }

  public Order deleteServiceCharge(final String orderId, final String serviceChargeId) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, Order>() {
      @Override
      public Order call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.deleteServiceCharge(orderId, serviceChargeId, status);
      }
    });
  }

  public LineItem addFixedPriceLineItem(final String orderId, final String itemId, final String binName, final String userData) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, LineItem>() {
      @Override
      public LineItem call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.addFixedPriceLineItem(orderId, itemId, binName, userData, status);
      }
    });
  }

  public LineItem addPerUnitLineItem(final String orderId, final String itemId, final int unitQuantity, final String binName, final String userData) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, LineItem>() {
      @Override
      public LineItem call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.addPerUnitLineItem(orderId, itemId, unitQuantity, binName, userData, status);
      }
    });
  }

  public LineItem addVariablePriceLineItem(final String orderId, final String itemId, final long price, final String binName, final String userData) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, LineItem>() {
      @Override
      public LineItem call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.addVariablePriceLineItem(orderId, itemId, price, binName, userData, status);
      }
    });
  }

  public LineItem addCustomLineItem(final String orderId, final LineItem lineItem, final boolean isTaxable) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, LineItem>() {
      @Override
      public LineItem call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.addCustomLineItem(orderId, lineItem, isTaxable, status);
      }
    });
  }

  public List<LineItem> updateLineItems(final String orderId, final List<LineItem> lineItems) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, List<LineItem>>() {
      @Override
      public List<LineItem> call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.updateLineItems(orderId, lineItems, status);
      }
    });
  }

  public Order deleteLineItems(final String orderId, final List<String> lineItemIds) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, Order>() {
      @Override
      public Order call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.deleteLineItems(orderId, lineItemIds, status);
      }
    });
  }

  @Deprecated
  public List<LineItem> copyLineItems(final String sourceOrderId, final String destinationOrderId, final List<String> srclineItemIds) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, List<LineItem>>() {
      @Override
      public List<LineItem> call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.copyLineItems(sourceOrderId, destinationOrderId, srclineItemIds, status);
      }
    });
  }

  public Map<String, List<LineItem>> createLineItemsFrom(final String sourceOrderId, final String destinationOrderId, final List<String> srclineItemIds) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, Map<String, List<LineItem>>>() {
      @Override
      public Map<String, List<LineItem>> call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.createLineItemsFrom(sourceOrderId, destinationOrderId, srclineItemIds, status);
      }
    });
  }

  public Order setLineItemNote(final String orderId, final String lineItemId, final String note) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, Order>() {
      @Override
      public Order call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.setLineItemNote(orderId, lineItemId, note, status);
      }
    });
  }

  public Order addLineItemModification(final String orderId, final String lineItemId, final Modifier modifier) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, Order>() {
      @Override
      public Order call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.addLineItemModification(orderId, lineItemId, modifier, status);
      }
    });
  }

  public Order addBatchLineItemModification(final String orderId, final List<String> lineItemIds, final Modifier modifier, final int quantity) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, Order>() {
      @Override
      public Order call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.addBatchLineItemModifications(orderId, lineItemIds, modifier, quantity, status);
      }
    });
  }

  public Order deleteLineItemModifications(final String orderId, final String lineItemId, final List<String> modificationIds) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, Order>() {
      @Override
      public Order call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.deleteLineItemModifications(orderId, lineItemId, modificationIds, status);
      }
    });
  }

  public LineItem exchangeItem(final String orderId, final String oldLineItemId, final String itemId, final String binName, final String userData) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, LineItem>() {
      @Override
      public LineItem call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.exchangeItem(orderId, oldLineItemId, itemId, binName, userData, status);
      }
    });
  }

  public Order addDiscount(final String orderId, final Discount discount) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, Order>() {
      @Override
      public Order call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.addDiscount(orderId, discount, status);
      }
    });
  }

  public Order deleteDiscounts(final String orderId, final List<String> discountIds) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, Order>() {
      @Override
      public Order call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.deleteDiscounts(orderId, discountIds, status);
      }
    });
  }

  public Order addLineItemDiscount(final String orderId, final String lineItemId, final Discount discount) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, Order>() {
      @Override
      public Order call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.addLineItemDiscount(orderId, lineItemId, discount, status);
      }
    });
  }

  public Order addBatchLineItemDiscounts(final String orderId, final List<String> lineItemIds, final List<Discount> discounts) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, Order>() {
      @Override
      public Order call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.addBatchLineItemDiscounts(orderId, lineItemIds, discounts, status);
      }
    });
  }

  public Order deleteLineItemDiscounts(final String orderId, final String lineItemId, final List<String> discountIds) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, Order>() {
      @Override
      public Order call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.deleteLineItemDiscounts(orderId, lineItemId, discountIds, status);
      }
    });
  }

  public Order addTip(final String orderId, final String paymentId, final long amount, final boolean online) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, Order>() {
      @Override
      public Order call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.addTip(orderId, paymentId, amount, online, status);
      }
    });
  }

  public Payment pay(final String orderId, final PaymentRequest paymentRequest, final boolean isAllowOffline, final String note) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, Payment>() {
      @Override
      public Payment call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.pay(orderId, paymentRequest, isAllowOffline, note, status);
      }
    });
  }

  public Order addPayment(final String orderId, final Payment payment, final List<LineItem> lineItems) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, Order>() {
      @Override
      public Order call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.addPayment(orderId, payment, lineItems, status);
      }
    });
  }

  public Order voidPayment(final String orderId, final String paymentId) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, Order>() {
      @Override
      public Order call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.voidPayment(orderId, paymentId, status);
      }
    });
  }

  public Credit addCredit(final String orderId, final com.clover.sdk.v3.payments.Credit credit) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, Credit>() {
      @Override
      public Credit call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.addCredit(orderId, credit, status);
      }
    });
  }

  public Order deleteCredit(final String orderId, final String creditId) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, Order>() {
      @Override
      public Order call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.deleteCredit(orderId, creditId, status);
      }
    });
  }

  public Refund addRefund(final String orderId, final Refund refund) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, Refund>() {
      @Override
      public Refund call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.addRefund(orderId, refund, status);
      }
    });
  }

  public Order deleteRefund(final String orderId, final String refundId) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, Order>() {
      @Override
      public Order call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.deleteRefund(orderId, refundId, status);
      }
    });
  }

  public boolean fire(final String orderId) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, Boolean>() {
      @Override
      public Boolean call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.fire(orderId , status);
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
}
