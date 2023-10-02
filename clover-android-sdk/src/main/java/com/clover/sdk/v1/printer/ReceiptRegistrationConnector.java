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
package com.clover.sdk.v1.printer;

import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v1.ServiceConnector;
import com.clover.sdk.v1.ServiceException;

import android.accounts.Account;
import android.content.Context;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;
import java.util.Locale;

/**
 * A class that encapsulates interaction with
 * {@link com.clover.sdk.v1.printer.IReceiptRegistrationService}.
 * This class automatically binds upon invocation of it's service methods
 * (e.g., {@link #register(Uri)}.
 * <p>
 * Clients of this class may optionally call {@link #connect()} to force
 * pre-binding to the underlying service, and must call {@link #disconnect()}
 * when finished interacting with the underlying service.
 * <p>
 * While this class originally provided both synchronous and asynchronous methods
 * for interacting with the service, the asynchronous methods are now deprecated.
 * <p>
 * Consider using {@link com.clover.sdk.v1.Intents#ACTION_APP_PRE_UNINSTALL} to ensure
 * your provider is unregistered when your application is uninstalled. Call
 * {@link #unregister(Uri)} in your pre-uninstall service.
 * <p>
 * If your registered receipt registration content provider fails to respond it may
 * be temporarily or permanently disabled. See {@link ReceiptContract}
 * for more information.
 *
 * @see com.clover.sdk.v1.printer.ReceiptContract
 */
public class ReceiptRegistrationConnector extends ServiceConnector<IReceiptRegistrationService> {
  private static final String TAG = "ReceiptRegistrationConnector";
  private static final String SERVICE_HOST = "com.clover.engine";

  private abstract static class ReceiptRegistrationCallable<T> implements ServiceCallable<IReceiptRegistrationService, T> {
  }

  private abstract static class ReceiptRegistrationRunnable implements ServiceRunnable<IReceiptRegistrationService> {
  }

  /**
   * An implementation of the {@link com.clover.sdk.v1.ServiceConnector.Callback} interface
   * for receiving asynchronous results from {@link com.clover.sdk.v1.printer.ReceiptRegistrationConnector}
   * methods that provides default method implementations.
   * <p>
   * The default implementations log the {@link com.clover.sdk.v1.ResultStatus} of the service
   * invocation.
   *
   * @param <T> the result type.
   */
  public static class ReceiptRegistrationCallback<T> implements Callback<T> {
    @Override
    public void onServiceSuccess(T result, ResultStatus status) {
      Log.d(TAG, String.format(Locale.US, "on service success: %s", status));
    }

    @Override
    public void onServiceFailure(ResultStatus status) {
      Log.w(TAG, String.format(Locale.US, "on service failure: %s", status));
    }

    @Override
    public void onServiceConnectionFailure() {
      Log.w(TAG, String.format(Locale.US, "on service connect failure"));
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
  protected String getServiceIntentPackage() {
    return SERVICE_HOST;
  }

  @Override
  protected int getServiceIntentVersion() {
    return 1;
  }

  @Override
  protected IReceiptRegistrationService getServiceInterface(IBinder iBinder) {
    return IReceiptRegistrationService.Stub.asInterface(iBinder);
  }


  /**
   * @deprecated Use {@link #register(Uri)} instead.
   */
  @Deprecated
  public void register(final Uri uri, Callback<Void> callback) {
    execute(new ReceiptRegistrationRunnable() {
      @Override
      public void run(IReceiptRegistrationService service, ResultStatus status) throws RemoteException {
        service.register(uri, status);
      }
    }, callback);
  }

  /**
   * Register a receipt registration {@link android.content.ContentProvider}.
   *
   * @param uri The "content://" URI of the receipt registration content provider.
   */
  public void register(final Uri uri) throws RemoteException, ClientException, ServiceException, BindingException {
    execute(new ReceiptRegistrationRunnable() {
      @Override
      public void run(IReceiptRegistrationService service, ResultStatus status) throws RemoteException {
        service.register(uri, status);
      }
    });
  }

  /**
   * @deprecated Use {@link #unregister(Uri)} instead.
   */
  @Deprecated
  public void unregister(final Uri uri, Callback<Void> callback) {
    execute(new ReceiptRegistrationRunnable() {
      @Override
      public void run(IReceiptRegistrationService service, ResultStatus status) throws RemoteException {
        service.unregister(uri, status);
      }
    }, callback);
  }

  /**
   * Unregister a receipt registration {@link android.content.ContentProvider}.
   *
   * @param uri The "content://" URI of the receipt registration content provider.
   */
  public void unregister(final Uri uri) throws RemoteException, ClientException, ServiceException, BindingException {
    execute(new ReceiptRegistrationRunnable() {
      @Override
      public void run(IReceiptRegistrationService service, ResultStatus status) throws RemoteException {
        service.unregister(uri, status);
      }
    });
  }

  /**
   * @deprecated Use {@link #getRegistrations()} instead.
   */
  @Deprecated
  public void getRegistrations(Callback<List<ReceiptRegistration>> callback) {
    execute(new ReceiptRegistrationCallable<List<ReceiptRegistration>>() {
      @Override
      public List<ReceiptRegistration> call(IReceiptRegistrationService service, ResultStatus status) throws RemoteException {
        return service.getRegistrations(status);
      }
    }, callback);
  }

  /**
   * Get the list of all registered receipt registration
   * {@link android.content.ContentProvider}s.
   */
  public List<ReceiptRegistration> getRegistrations() throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ReceiptRegistrationCallable<List<ReceiptRegistration>>() {
      @Override
      public List<ReceiptRegistration> call(IReceiptRegistrationService service, ResultStatus status) throws RemoteException {
        return service.getRegistrations(status);
      }
    });
  }
}