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
package com.clover.sdk.v1;

import android.accounts.Account;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;

/**
 * Base class for implementing service connectors. A service connector is a class that encapsulates
 * a bound AIDL service.  Subclasses of this class implement methods that are specific to the
 * service that they're interacting with.<p/>
 * <p/>
 * Connection with the service happens implicitly.  However, the user of a {@code ServiceConnector}
 * <em>must</em> call {@link #disconnect} when he or she is done with the instance.  For example,
 * when using a {@code ServiceConnector} in an {@code Activity}, call {@link #disconnect()} from
 * {@code onPause()} or {@code onDestroy()}.
 *
 * @param <S> The service interface that is being encapsulated.
 */
public abstract class ServiceConnector<S extends IInterface> implements ServiceConnection {
  private static final String TAG = "ServiceConnector";

  private static final int SERVICE_CONNECTION_TIMEOUT = 3000;
  private static final int MAX_RETRY_ATTEMPTS = 3;

  protected final Context mContext;
  protected final Account mAccount;
  protected final OnServiceConnectedListener mClient;
  protected final Handler mHandler = new Handler(Looper.getMainLooper());

  protected S mService;
  protected boolean mConnected; // true if connect() has been called since last disconnect()

  public interface OnServiceConnectedListener {
    void onServiceConnected(ServiceConnector<? extends IInterface> connector);

    void onServiceDisconnected(ServiceConnector<? extends IInterface> connector);
  }

  /**
   * Responds to the results of a call to a {@code ServiceConnector} method.  All the callback
   * methods are executed on the main thread.
   *
   * @param <T> the data type returned by the method call
   */
  public interface Callback<T> {
    void onServiceSuccess(T result, ResultStatus status);

    void onServiceFailure(ResultStatus status);

    void onServiceConnectionFailure();
  }

  protected interface ServiceCallable<S, T> {
    T call(S service, ResultStatus status) throws RemoteException;
  }

  protected interface ServiceRunnable<S> {
    void run(S service, ResultStatus status) throws RemoteException;
  }

  /**
   * Constructs a new ServiceConnector object.
   *
   * @param context the Context object, required for establishing a connection to
   *                the service.
   * @param account the Account to use with the service.
   * @param client  an optional object implementing the OnServiceConnectedListener
   *                interface, for receiving connection notifications from the service.
   */
  public ServiceConnector(Context context, Account account, OnServiceConnectedListener client) {
    mContext = context;
    mAccount = account;
    mClient = client;
  }

  protected abstract String getServiceIntentAction();

  @Deprecated
  protected int getServiceIntentVersion() {
    return 1;
  }

  protected abstract S getServiceInterface(IBinder iBinder);

  public boolean connect() {
    boolean result;
    synchronized (this) {
      if (mConnected) {
        result = true;
      } else {
        Intent intent = new Intent(getServiceIntentAction());
        intent.putExtra(Intents.EXTRA_ACCOUNT, mAccount);
        intent.putExtra(Intents.EXTRA_VERSION, getServiceIntentVersion());
        result = mContext.bindService(intent, this, Context.BIND_AUTO_CREATE);
        mConnected = result;
      }
    }
    return result;
  }

  public void disconnect() {
    synchronized (this) {
      if (mConnected) {
        try {
          mContext.unbindService(this);
        } catch (IllegalArgumentException ex) {
          if (false) {
            Log.v(TAG, "disconnect failed: " + ex);
          }
        }
        mConnected = false;
      }
    }
  }

  protected synchronized S waitForConnection() throws BindingException {

    int retryCount = 0;
    boolean result;

    while (!isConnected() && retryCount < MAX_RETRY_ATTEMPTS) {
      result = connect();

      try {
        wait(SERVICE_CONNECTION_TIMEOUT);
      } catch (InterruptedException e) {
        Log.i(TAG, "waitForConnection interrupted...");
        return null;
      }
      Log.i(TAG, "waitForConnection result: " + result + ", retryCount: " + retryCount);
      retryCount++;
    }

    S service = getService();
    if (service == null) {
      throw new BindingException("Could not bind to Android service");
    }

    return service;
  }

  /**
   * Returns whether we are connected to the order service
   *
   * @return true if we are connected, false otherwise
   */
  public synchronized boolean isConnected() {
    return (mService != null && mConnected);
  }

  /**
   * Returns the order service interface
   *
   * @return Service if we are connected, null otherwise
   */
  public synchronized S getService() {
    return mService;
  }


  protected void execute(ServiceRunnable<S> runnable) throws RemoteException, ClientException, ServiceException, BindingException {
    if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
      throw new IllegalThreadStateException("service invoked on main thread");
    }

    ResultStatus status = new ResultStatus();

    final S service = waitForConnection();

    runnable.run(service, status);

    throwOnFailure(status);
  }

  protected <T> T execute(ServiceCallable<S, T> callable) throws RemoteException, ClientException, ServiceException, BindingException {
    if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
      throw new IllegalThreadStateException("service invoked on main thread");
    }

    ResultStatus status = new ResultStatus();

    final S service = waitForConnection();

    T result = callable.call(service, status);

    throwOnFailure(status);

    return result;
  }

  protected <T> void execute(final ServiceCallable<S, T> callable, final Callback<T> callback) {
    connect();

    AsyncTask.SERIAL_EXECUTOR.execute(new Runnable() {
      @Override
      public void run() {
        ResultStatus status = new ResultStatus();

        try {
          S service = waitForConnection();

          T result = callable.call(service, status);

          postResults(result, status, callback);
        } catch (Exception e) {
          e.printStackTrace();
          mHandler.post(new Runnable() {
            @Override
            public void run() {
              callback.onServiceConnectionFailure();
            }
          });
        }

      }
    });
  }

  protected void execute(final ServiceRunnable<S> runnable, final Callback<Void> callback) {
    connect();

    AsyncTask.SERIAL_EXECUTOR.execute(new Runnable() {
      @Override
      public void run() {
        ResultStatus status = new ResultStatus();

        try {
          S service = waitForConnection();

          runnable.run(service, status);

          postResults(null, status, callback);
        } catch (Exception e) {
          e.printStackTrace();
          mHandler.post(new Runnable() {
            @Override
            public void run() {
              callback.onServiceConnectionFailure();
            }
          });
        }

      }
    });
  }

  protected void throwOnFailure(ResultStatus status) throws ClientException, ServiceException {
    if (status.isSuccess() || status.getStatusCode() == ResultStatus.NOT_FOUND) {
      // do nothing; this is the default case
    } else if (status.getStatusCode() == ResultStatus.FORBIDDEN) {
      throw new ForbiddenException(status);
    } else if (status.isClientError() || status.getStatusCode() == ResultStatus.OTHER || status.getStatusCode() == ResultStatus.UNKNOWN) {
      throw new ClientException(status);
    } else if (status.isServiceError()) {
      throw new ServiceException(status);
    }
  }

  protected <T> void postResults(final T result, final ResultStatus status, final Callback<T> callback) {
    if (callback != null) {
      mHandler.post(new Runnable() {
        @Override
        public void run() {
          // if the service wrapper's ServiceCommand did not utilize the ResultStatus parameter, the status
          // code will be UNKNOWN by default and we are forced to assume that this constitutes 'success'
          // if the status code was set, we look for a response code that indicates success, or a 'Not Found'
          // error (similar to an HTTP 404 status), which we don't want to treat as failure
          if (status.getStatusCode() == ResultStatus.UNKNOWN ||
              status.getStatusCode() == ResultStatus.NOT_FOUND ||
              status.isSuccess()) {
            callback.onServiceSuccess(result, status);
          } else {
            callback.onServiceFailure(status);
          }
        }
      });
    }
  }

  protected void notifyServiceConnected(OnServiceConnectedListener client) {
    if (client != null) {
      client.onServiceConnected(this);
    }
  }

  protected void notifyServiceDisconnected(OnServiceConnectedListener client) {
    if (client != null) {
      client.onServiceDisconnected(this);
    }
  }

  /**
   * Part of the ServiceConnection interface.  Do not call.
   */
  public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
    OnServiceConnectedListener client = null;

    synchronized (this) {
      mService = getServiceInterface(iBinder);
      if (mService != null) {
        client = mClient;
      }
      notifyAll();
    }

    notifyServiceConnected(client);
  }

  /**
   * Part of the ServiceConnection interface.  Do not call.
   */
  public final void onServiceDisconnected(ComponentName componentName) {
    OnServiceConnectedListener client = null;

    synchronized (this) {
      if (mService != null) {
        client = mClient;
      }
      mService = null;
      mConnected = false;
    }

    notifyServiceDisconnected(client);
  }
}
