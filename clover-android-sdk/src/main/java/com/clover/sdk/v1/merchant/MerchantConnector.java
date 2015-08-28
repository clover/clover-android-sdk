/**
 * Copyright (C) 2015 Clover Network, Inc.
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
package com.clover.sdk.v1.merchant;

import android.accounts.Account;
import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v1.ServiceCallback;
import com.clover.sdk.v1.ServiceConnector;
import com.clover.sdk.v1.ServiceException;

/**
 * A class that encapsulates interaction with {@link com.clover.sdk.v1.merchant.IMerchantService}.
 * This class automatically binds and provides both synchronous and asynchronous service
 * method invocation.
 * <p/>
 * Clients of this class may optionally call {@link #connect()} to force
 * pre-binding to the underlying service, and must call {@link #disconnect()}
 * when finished interacting with the underlying service.
 * <p/>
 * For all service methods, this class provides both synchronous and asynchronous call options.
 * The synchronous methods must not be called on the UI thread.
 */
public class MerchantConnector extends ServiceConnector<IMerchantService> {
  private static final String TAG = "MerchantConnector";

  /**
   * A listener that is invoked when the merchant changes.
   */
  public static interface OnMerchantChangedListener {
    void onMerchantChanged(Merchant merchant);
  }

  private abstract static class MerchantCallable<T> implements ServiceCallable<IMerchantService, T> {
  }

  private abstract static class MerchantRunnable implements ServiceRunnable<IMerchantService> {
  }

  /**
   * An implementation of the {@link com.clover.sdk.v1.ServiceConnector.Callback} interface
   * for receiving asynchronous results from {@link com.clover.sdk.v1.merchant.MerchantConnector}
   * methods that provides default method implementations.
   * <p/>
   * The default implementations log the {@link com.clover.sdk.v1.ResultStatus} of the service
   * invocation.
   *
   * @param <T> the result type.
   */
  public static class MerchantCallback<T> implements Callback<T> {
    @Override
    public void onServiceSuccess(T result, ResultStatus status) {
      Log.d(TAG, String.format("on service success: %s", status));
    }

    @Override
    public void onServiceFailure(ResultStatus status) {
      Log.w(TAG, String.format("on service failure: %s", status));
    }

    @Override
    public void onServiceConnectionFailure() {
      Log.w(TAG, String.format("on service connect failure"));
    }
  }

  private OnMerchantChangedListener merchantChangedListener;

  private class OnMerchantChangedListenerParent extends IMerchantListener.Stub {

    private MerchantConnector mConnector;

    private OnMerchantChangedListenerParent(MerchantConnector connector) {
      mConnector = connector;
    }

    @Override
    public void onMerchantChanged(final Merchant merchant) {
      if (merchantChangedListener != null) {
        mHandler.post(new Runnable() {
          @Override
          public void run() {
            merchantChangedListener.onMerchantChanged(merchant);
          }
        });
      }
    }

    // This method must be called when the callback is no longer needed to prevent a memory leak. Due to the design of
    // AIDL services Android unnecessarily retains pointers to otherwise unreferenced instances of this class which in
    // turn are referencing Context objects that consume large amounts of memory.
    public void destroy() {
      mConnector = null;
    }
  }

  private OnMerchantChangedListenerParent mListener;

  /**
   * Construct a new merchant connector.
   *
   * @param context The Context in which this connector will bind to the underlying service.
   * @param account The Clover account which is used when binding to the underlying service.
   * @param client  A listener, or null to receive no notifications.
   */
  public MerchantConnector(Context context, Account account, OnServiceConnectedListener client) {
    super(context, account, client);
  }

  /**
   * Set the listener that will be called when merchant properties change.
   */
  public void setOnMerchantChangedListener(OnMerchantChangedListener listener) {
    merchantChangedListener = listener;
  }

  @Override
  protected String getServiceIntentAction() {
    return MerchantIntent.ACTION_MERCHANT_SERVICE;
  }

  @Override
  protected int getServiceIntentVersion() {
    return 1;
  }

  @Override
  protected IMerchantService getServiceInterface(IBinder iBinder) {
    return IMerchantService.Stub.asInterface(iBinder);
  }

  @Override
  protected void notifyServiceConnected(OnServiceConnectedListener client) {
    super.notifyServiceConnected(client);

    if (client != null && client instanceof OnMerchantChangedListener) {
      execute(new MerchantCallable<Void>() {
        @Override
        public Void call(IMerchantService service, ResultStatus status) throws RemoteException {
          if (mListener == null) {
            mListener = new OnMerchantChangedListenerParent(MerchantConnector.this);
          }
          service.addListener(mListener, status);
          return null;
        }
      }, new MerchantCallback<Void>());
    }
  }

  @Override
  public void disconnect() {
    if (mListener != null) {
      try {
        mService.removeListener(mListener, new ResultStatus());
      } catch (RemoteException e) {
        e.printStackTrace();
      }
      mListener.destroy();
      mListener = null;
    }
    super.disconnect();
  }

  /**
   * Get the merchant object.
   *
   * @param callback A callback to receive invocation results.
   */
  public void getMerchant(Callback<Merchant> callback) {
    execute(new MerchantCallable<Merchant>() {
      @Override
      public Merchant call(IMerchantService service, ResultStatus status) throws RemoteException {
        return service.getMerchant(status);
      }
    }, callback);
  }

  /**
   * Get the merchant object. This method must not be called on the UI thread.
   */
  public Merchant getMerchant() throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new MerchantCallable<Merchant>() {
      @Override
      public Merchant call(IMerchantService service, ResultStatus status) throws RemoteException {
        return service.getMerchant(status);
      }
    });
  }

  /**
   * Set the merchant's address.
   *
   * @param address  The address of the merchant.
   * @param callback A callback to receive invocation results.
   */
  public void setAddress(final MerchantAddress address, Callback<Void> callback) {
    execute(new MerchantCallable<Void>() {
      @Override
      public Void call(IMerchantService service, ResultStatus status) throws RemoteException {
        service.setAddress(address, status);
        return null;
      }
    }, callback);
  }

  /**
   * Set the merchant address. This method must not be called on the main thread.
   */
  public void setAddress(final MerchantAddress address) throws RemoteException, ClientException, ServiceException, BindingException {
    execute(new MerchantRunnable() {
      @Override
      public void run(IMerchantService service, ResultStatus status) throws RemoteException {
        service.setAddress(address, status);
      }
    });
  }

  /**
   * Set the merchant's phone number.
   *
   * @param phoneNumber The phone number of the merchant.
   * @param callback    A callback to receive invocation results.
   */
  public void setPhoneNumber(final String phoneNumber, Callback<Void> callback) {
    execute(new MerchantCallable<Void>() {
      @Override
      public Void call(IMerchantService service, ResultStatus status) throws RemoteException {
        service.setPhoneNumber(phoneNumber, status);
        return null;
      }
    }, callback);
  }

  /**
   * Set the merchant's phone number. This method must not be called on the main thread.
   */
  public void setPhoneNumber(final String phoneNumber) throws RemoteException, ClientException, ServiceException, BindingException {
    execute(new MerchantRunnable() {
      @Override
      public void run(IMerchantService service, ResultStatus status) throws RemoteException {
        service.setPhoneNumber(phoneNumber, status);
      }
    });
  }

  /**
   * Set to true to have Clover update stock, false to disable Clover stock updates.
   */
  public void setUpdateStock(final boolean updateStock, Callback<Void> callback) {
    execute(new MerchantCallable<Void>() {
      @Override
      public Void call(IMerchantService service, ResultStatus status) throws RemoteException {
        service.setUpdateStock(updateStock, status);
        return null;
      }
    }, callback);
  }

  /**
   * Set to true to have Clover update stock, false to disable Clover stock updates. This method must not be called on the main thread.
   */
  public void setUpdateStock(final boolean updateStock) throws RemoteException, ClientException, ServiceException, BindingException {
    execute(new MerchantRunnable() {
      @Override
      public void run(IMerchantService service, ResultStatus status) throws RemoteException {
        service.setUpdateStock(updateStock, status);
      }
    });
  }

  /**
   * Set to true to enable stock tracking.
   */
  public void setTrackStock(final boolean trackStock, Callback<Void> callback) {
    execute(new MerchantCallable<Void>() {
      @Override
      public Void call(IMerchantService service, ResultStatus status) throws RemoteException {
        service.setTrackStock(trackStock, status);
        return null;
      }
    }, callback);
  }

  /**
   * Set to true to have Clover update stock, false to disable Clover stock updates. This method must not be called on the main thread.
   */
  public void setTrackStock(final boolean trackStock) throws RemoteException, ClientException, ServiceException, BindingException {
    execute(new MerchantRunnable() {
      @Override
      public void run(IMerchantService service, ResultStatus status) throws RemoteException {
        service.setTrackStock(trackStock, status);
      }
    });
  }


}
