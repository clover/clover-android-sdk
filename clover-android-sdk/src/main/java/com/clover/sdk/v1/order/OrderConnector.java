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

package com.clover.sdk.v1.order;

import android.accounts.Account;
import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;

import com.clover.sdk.v1.ServiceConnector;
import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v1.ServiceException;
import com.clover.sdk.v1.inventory.Item;

public class OrderConnector extends ServiceConnector<IOrderService> {
  private static final String TAG = "OrderConnector";

  public static final String ACTION_ORDER_SERVICE = "com.clover.intent.action.ORDER_SERVICE";

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
    return ACTION_ORDER_SERVICE;
  }

  @Override
  protected IOrderService getServiceInterface(IBinder iBinder) {
    return IOrderService.Stub.asInterface(iBinder);
  }

  public interface OnOrderUpdateListener {
    void onOrderUpdated(String orderId);
  }

  private final IOnOrderUpdateListener.Stub mListener = new IOnOrderUpdateListener.Stub() {
    @Override
    public void onOrderUpdated(String orderId) throws RemoteException {
      if (mClient != null && mClient instanceof OnOrderUpdateListener) {
        ((OnOrderUpdateListener)mClient).onOrderUpdated(orderId);
      }
    }
  };

  protected void notifyServiceConnected(OnServiceConnectedListener client) {
    super.notifyServiceDisconnected(client);

    if (client != null && client instanceof OnOrderUpdateListener) {
      try {
        mService.addOnOrderUpdatedListener(mListener);
      } catch (RemoteException e) {
        e.printStackTrace();
      }
    }
  }

  protected void notifyServiceDisconnected(OnServiceConnectedListener client) {
    super.notifyServiceDisconnected(client);

    if (client != null && client instanceof OnOrderUpdateListener) {
      try {
        mService.removeOnOrderUpdatedListener(mListener);
      } catch (RemoteException e) {
        e.printStackTrace();
      }
    }
  }

  public Order getOrder(final String id) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, Order>() {
      @Override
      public Order call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.getOrder(id, status);
      }
    });
  }

  public void getOrder(final String id, Callback<Order> callback) throws RemoteException {
    execute(new ServiceCallable<IOrderService, Order>() {
      @Override
      public Order call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.getOrder(id, status);
      }
    }, callback);
  }

  public Order create() throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, Order>() {
      @Override
      public Order call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.create(status);
      }
    });
  }

  public void create(Callback<Order> callback) throws RemoteException {
    execute(new ServiceCallable<IOrderService, Order>() {
      @Override
      public Order call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.create(status);
      }
    }, callback);
  }

  public void deleteOrder(final String id) throws RemoteException, ClientException, ServiceException, BindingException {
    execute(new ServiceRunnable<IOrderService>() {
      @Override
      public void run(IOrderService service, ResultStatus status) throws RemoteException {
        service.deleteOrder(id, status);
      }
    });
  }

  public void deleteOrder(final String id, Callback<Void> callback) throws RemoteException {
    executeOnThread(new ServiceRunnable<IOrderService>() {
      @Override
      public void run(IOrderService service, ResultStatus status) throws RemoteException {
        service.deleteOrder(id, status);
      }
    }, callback);
  }

  public void setTitle(final String id, final String title) throws RemoteException, ClientException, ServiceException, BindingException {
    execute(new ServiceRunnable<IOrderService>() {
      @Override
      public void run(IOrderService service, ResultStatus status) throws RemoteException {
        service.setTitle(id, title, status);
      }
    });
  }

  public void setTitle(final String id, final String title, Callback<Void> callback) throws RemoteException {
    executeOnThread(new ServiceRunnable<IOrderService>() {
      @Override
      public void run(IOrderService service, ResultStatus status) throws RemoteException {
        service.setTitle(id, title, status);
      }
    }, callback);
  }

  public void setNote(final String id, final String note) throws RemoteException, ClientException, ServiceException, BindingException {
    execute(new ServiceRunnable<IOrderService>() {
      @Override
      public void run(IOrderService service, ResultStatus status) throws RemoteException {
        service.setNote(id, note, status);
      }
    });
  }

  public void setNote(final String id, final String note, Callback<Void> callback) throws RemoteException {
    executeOnThread(new ServiceRunnable<IOrderService>() {
      @Override
      public void run(IOrderService service, ResultStatus status) throws RemoteException {
        service.setNote(id, note, status);
      }
    }, callback);
  }

  public void setType(final String id, final String type) throws RemoteException, ClientException, ServiceException, BindingException {
    execute(new ServiceRunnable<IOrderService>() {
      @Override
      public void run(IOrderService service, ResultStatus status) throws RemoteException {
        service.setType(id, type, status);
      }
    });
  }

  public void setType(final String id, final String type, Callback<Void> callback) throws RemoteException {
    executeOnThread(new ServiceRunnable<IOrderService>() {
      @Override
      public void run(IOrderService service, ResultStatus status) throws RemoteException {
        service.setType(id, type, status);
      }
    }, callback);
  }

  public void setState(final String id, final String state) throws RemoteException, ClientException, ServiceException, BindingException {
    execute(new ServiceRunnable<IOrderService>() {
      @Override
      public void run(IOrderService service, ResultStatus status) throws RemoteException {
        service.setState(id, state, status);
      }
    });
  }

  public void setState(final String id, final String state, Callback<Void> callback) throws RemoteException {
    executeOnThread(new ServiceRunnable<IOrderService>() {
      @Override
      public void run(IOrderService service, ResultStatus status) throws RemoteException {
        service.setState(id, state, status);
      }
    }, callback);
  }

  public void setTotal(final String id, final long total) throws RemoteException, ClientException, ServiceException, BindingException {
    execute(new ServiceRunnable<IOrderService>() {
      @Override
      public void run(IOrderService service, ResultStatus status) throws RemoteException {
        service.setTotal(id, total, status);
      }
    });
  }

  public void setTotal(final String id, final long total, Callback<Void> callback) throws RemoteException {
    executeOnThread(new ServiceRunnable<IOrderService>() {
      @Override
      public void run(IOrderService service, ResultStatus status) throws RemoteException {
        service.setTotal(id, total, status);
      }
    }, callback);
  }

  public void setGroupLineItems(final String id, final boolean groupLineItems) throws RemoteException, ClientException, ServiceException, BindingException {
    execute(new ServiceRunnable<IOrderService>() {
      @Override
      public void run(IOrderService service, ResultStatus status) throws RemoteException {
        service.setGroupLineItems(id, groupLineItems, status);
      }
    });
  }

  public void setGroupLineItems(final String id, final boolean groupLineItems, Callback<Void> callback) throws RemoteException {
    executeOnThread(new ServiceRunnable<IOrderService>() {
      @Override
      public void run(IOrderService service, ResultStatus status) throws RemoteException {
        service.setGroupLineItems(id, groupLineItems, status);
      }
    }, callback);
  }

  public void setTaxRemoved(final String id, final boolean taxRemoved) throws RemoteException, ClientException, ServiceException, BindingException {
    execute(new ServiceRunnable<IOrderService>() {
      @Override
      public void run(IOrderService service, ResultStatus status) throws RemoteException {
        service.setTaxRemoved(id, taxRemoved, status);
      }
    });
  }

  public void setTaxRemoved(final String id, final boolean taxRemoved, Callback<Void> callback) throws RemoteException {
    executeOnThread(new ServiceRunnable<IOrderService>() {
      @Override
      public void run(IOrderService service, ResultStatus status) throws RemoteException {
        service.setTaxRemoved(id, taxRemoved, status);
      }
    }, callback);
  }

  public LineItem addLineItem(final String id, final String itemId, final int unitQuantity, final String binName, final String userData) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, LineItem>() {
      @Override
      public LineItem call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.addLineItemById(id, itemId, unitQuantity, binName, userData, status);
      }
    });
  }

  public void addLineItem(final String id, final String itemId, final int unitQuantity, final String binName, final String userData, Callback<LineItem> callback) throws RemoteException {
    execute(new ServiceCallable<IOrderService, LineItem>() {
      @Override
      public LineItem call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.addLineItemById(id, itemId, unitQuantity, binName, userData, status);
      }
    }, callback);
  }

  public LineItem addLineItem(final String id, final Item item, final int unitQuantity, final String binName, final String userData) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IOrderService, LineItem>() {
      @Override
      public LineItem call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.addLineItem(id, item, unitQuantity, binName, userData, status);
      }
    });
  }

  public void addLineItem(final String id, final Item item, final int unitQuantity, final String binName, final String userData, Callback<LineItem> callback) throws RemoteException {
    execute(new ServiceCallable<IOrderService, LineItem>() {
      @Override
      public LineItem call(IOrderService service, ResultStatus status) throws RemoteException {
        return service.addLineItem(id, item, unitQuantity, binName, userData, status);
      }
    }, callback);
  }

  public void deleteLineItem(final String id, final String lineItemId) throws RemoteException, ClientException, ServiceException, BindingException {
    execute(new ServiceRunnable<IOrderService>() {
      @Override
      public void run(IOrderService service, ResultStatus status) throws RemoteException {
        service.deleteLineItem(id, lineItemId, status);
      }
    });
  }

  public void deleteLineItem(final String id, final String lineItemId, Callback<Void> callback) throws RemoteException {
    executeOnThread(new ServiceRunnable<IOrderService>() {
      @Override
      public void run(IOrderService service, ResultStatus status) throws RemoteException {
        service.deleteLineItem(id, lineItemId, status);
      }
    }, callback);
  }
}
