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
package com.clover.sdk.v1.printer;

import android.accounts.Account;
import android.content.Context;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v1.ServiceConnector;
import com.clover.sdk.v1.ServiceException;

import java.util.List;

/**
 * A class that encapsulates interaction with
 * {@link com.clover.sdk.v1.printer.IReceiptRegistrationService}.
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
public class ReceiptRegistrationConnector extends ServiceConnector<IReceiptRegistrationService> {
  private static final String TAG = "ReceiptRegistrationConnector";

  private abstract static class ReceiptRegistrationCallable<T> implements ServiceCallable<IReceiptRegistrationService, T> {
  }

  private abstract static class ReceiptRegistrationRunnable implements ServiceRunnable<IReceiptRegistrationService> {
  }

  /**
   * An implementation of the {@link com.clover.sdk.v1.ServiceConnector.Callback} interface
   * for receiving asynchronous results from {@link com.clover.sdk.v1.printer.ReceiptRegistrationConnector}
   * methods that provides default method implementations.
   * <p/>
   * The default implementations log the {@link com.clover.sdk.v1.ResultStatus} of the service
   * invocation.
   *
   * @param <T> the result type.
   */
  public static class ReceiptRegistrationCallback<T> implements Callback<T> {
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

  /**
   * Construct a new printer connector.
   *
   * @param context The Context in which this connector will bind to the underlying service.
   * @param account The Clover account which is used when binding to the underlying service.
   * @param client  A listener, or null to receive no notifications.
   */
  public ReceiptRegistrationConnector(Context context, Account account, OnServiceConnectedListener client) {
    super(context, account, client);
  }

  @Override
  protected String getServiceIntentAction() {
    return PrinterIntent.ACTION_RECEIPT_REGISTRATION_SERVICE;
  }

  @Override
  protected int getServiceIntentVersion() {
    return 1;
  }

  @Override
  protected IReceiptRegistrationService getServiceInterface(IBinder iBinder) {
    return IReceiptRegistrationService.Stub.asInterface(iBinder);
  }


  public void register(final Uri uri, Callback<Void> callback) {
    execute(new ReceiptRegistrationRunnable() {
      @Override
      public void run(IReceiptRegistrationService service, ResultStatus status) throws RemoteException {
        service.register(uri, status);
      }
    }, callback);
  }

  public void register(final Uri uri) throws RemoteException, ClientException, ServiceException, BindingException {
    execute(new ReceiptRegistrationRunnable() {
      @Override
      public void run(IReceiptRegistrationService service, ResultStatus status) throws RemoteException {
        service.register(uri, status);
      }
    });
  }

  public void unregister(final Uri uri, Callback<Void> callback) {
    execute(new ReceiptRegistrationRunnable() {
      @Override
      public void run(IReceiptRegistrationService service, ResultStatus status) throws RemoteException {
        service.unregister(uri, status);
      }
    }, callback);
  }

  public void unregister(final Uri uri) throws RemoteException, ClientException, ServiceException, BindingException {
    execute(new ReceiptRegistrationRunnable() {
      @Override
      public void run(IReceiptRegistrationService service, ResultStatus status) throws RemoteException {
        service.unregister(uri, status);
      }
    });
  }

  public void getRegistrations(Callback<List<ReceiptRegistration>> callback) {
    execute(new ReceiptRegistrationCallable<List<ReceiptRegistration>>() {
      @Override
      public List<ReceiptRegistration> call(IReceiptRegistrationService service, ResultStatus status) throws RemoteException {
        return service.getRegistrations(status);
      }
    }, callback);
  }

  public List<ReceiptRegistration> getRegistrations() throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ReceiptRegistrationCallable<List<ReceiptRegistration>>() {
      @Override
      public List<ReceiptRegistration> call(IReceiptRegistrationService service, ResultStatus status) throws RemoteException {
        return service.getRegistrations(status);
      }
    });
  }
}