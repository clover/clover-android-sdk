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

package com.clover.sdk.v1.merchant;

import android.accounts.Account;
import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ResultStatus;
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
      Log.w(TAG, String.format("on service success: %s", status));
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

  ;

  private final IMerchantListener iMerchantListener = new IMerchantListener.Stub() {
    @Override
    public void onMerchantChanged(final Merchant merchant) {
      if (mClient instanceof OnMerchantChangedListener) {
        OnMerchantChangedListener listener = (OnMerchantChangedListener) mClient;
        postOnMerchantChanged(merchant, listener);
      }
    }
  };

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

  @Override
  protected String getServiceIntentAction() {
    return MerchantIntent.ACTION_MERCHANT_SERVICE;
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
          service.addListener(iMerchantListener, status);
          return null;
        }
      }, new MerchantCallback<Void>());
    }
  }

  @Override
  public void disconnect() {
    if (mService != null) {
      try {
        mService.removeListener(iMerchantListener, new ResultStatus());
      } catch (RemoteException e) {
        e.printStackTrace();
      }
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

  private void postOnMerchantChanged(final Merchant merchant, final OnMerchantChangedListener listener) {
    mHandler.post(new Runnable() {
      @Override
      public void run() {
        listener.onMerchantChanged(merchant);
      }
    });
  }

}