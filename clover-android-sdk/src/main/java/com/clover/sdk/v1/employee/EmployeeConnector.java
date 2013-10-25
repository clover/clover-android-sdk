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

package com.clover.sdk.v1.employee;

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

import java.util.List;

/**
 * A class that encapsulates interaction with {@link com.clover.sdk.v1.employee.IEmployeeService}.
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
public class EmployeeConnector extends ServiceConnector<IEmployeeService> {
  private static final String TAG = "EmployeeConnector";

  /**
   * A listener that is invoked when the active employee changes.
   */
  public static interface OnActiveEmployeeChangedListener {
    void onActiveEmployeeChanged(Employee employee);
  }

  private abstract static class EmployeeCallable<T> implements ServiceCallable<IEmployeeService, T> {
  }

  private abstract static class EmployeeRunnable implements ServiceRunnable<IEmployeeService> {
  }

  /**
   * An implementation of the {@link com.clover.sdk.v1.ServiceConnector.Callback} interface
   * for receiving asynchronous results from {@link com.clover.sdk.v1.employee.EmployeeConnector}
   * methods that provides default method implementations.
   * <p/>
   * The default implementations log the {@link com.clover.sdk.v1.ResultStatus} of the service
   * invocation.
   *
   * @param <T> the result type.
   */
  public static class EmployeeCallback<T> implements Callback<T> {
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

  private final IEmployeeListener iEmployeeListener = new IEmployeeListener.Stub() {
    @Override
    public void onActiveEmployeeChanged(final Employee employee) {
      if (mClient instanceof OnActiveEmployeeChangedListener) {
        OnActiveEmployeeChangedListener listener = (OnActiveEmployeeChangedListener) mClient;
        postOnActiveEmployeeChanged(employee, listener);
      }
    }
  };

  public EmployeeConnector(Context context, Account account, OnServiceConnectedListener client) {
    super(context, account, client);
  }

  @Override
  protected String getServiceIntentAction() {
    return EmployeeIntent.ACTION_EMPLOYEE_SERVICE;
  }

  @Override
  protected IEmployeeService getServiceInterface(IBinder iBinder) {
    return IEmployeeService.Stub.asInterface(iBinder);
  }

  @Override
  protected void notifyServiceConnected(OnServiceConnectedListener client) {
    super.notifyServiceConnected(client);

    if (client != null && client instanceof OnActiveEmployeeChangedListener) {
      execute(new EmployeeCallable<Void>() {
        @Override
        public Void call(IEmployeeService service, ResultStatus status) throws RemoteException {
          service.addListener(iEmployeeListener, status);
          return null;
        }
      }, new EmployeeCallback<Void>());
    }
  }

  @Override
  public void disconnect() {
    execute(
        new EmployeeCallable<Void>() {
          @Override
          public Void call(IEmployeeService service, ResultStatus status) throws RemoteException {
            service.removeListener(iEmployeeListener, status);
            return null;
          }
        }, new EmployeeCallback<Void>() {
          @Override
          public void onServiceSuccess(Void result, ResultStatus status) {
            super.onServiceSuccess(result, status);
            EmployeeConnector.super.disconnect();
          }
        }
    );
  }

  public void getEmployee(EmployeeCallback<Employee> callback) {
    execute(new EmployeeCallable<Employee>() {
      @Override
      public Employee call(IEmployeeService service, ResultStatus status) throws RemoteException {
        return service.getActiveEmployee(status);
      }
    }, callback);
  }

  public Employee getEmployee() throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new EmployeeCallable<Employee>() {
      @Override
      public Employee call(IEmployeeService service, ResultStatus status) throws RemoteException {
        return service.getActiveEmployee(status);
      }
    });
  }

  public void getEmployee(final String id, EmployeeCallback<Employee> callback) {
    execute(new EmployeeCallable<Employee>() {
      @Override
      public Employee call(IEmployeeService service, ResultStatus status) throws RemoteException {
        return service.getEmployee(id, status);
      }
    }, callback);
  }

  public Employee getEmployee(final String id) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new EmployeeCallable<Employee>() {
      @Override
      public Employee call(IEmployeeService service, ResultStatus status) throws RemoteException {
        return service.getEmployee(id, status);
      }
    });
  }

  public void getEmployees(EmployeeCallback<List<Employee>> callback) {
    execute(new EmployeeCallable<List<Employee>>() {
      @Override
      public List<Employee> call(IEmployeeService service, ResultStatus status) throws RemoteException {
        return service.getEmployees(status);
      }
    }, callback);
  }

  public List<Employee> getEmployees() throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new EmployeeCallable<List<Employee>>() {
      @Override
      public List<Employee> call(IEmployeeService service, ResultStatus status) throws RemoteException {
        return service.getEmployees(status);
      }
    });
  }

  public void login(EmployeeCallback<Void> callback) {
    execute(new EmployeeCallable<Void>() {
      @Override
      public Void call(IEmployeeService service, ResultStatus status) throws RemoteException {
        service.login(status);
        return null;
      }
    }, callback);
  }

  public void login() throws RemoteException, ClientException, ServiceException, BindingException {
    execute(new EmployeeRunnable() {
      @Override
      public void run(IEmployeeService service, ResultStatus status) throws RemoteException {
        service.login(status);
      }
    });
  }

  public void logout(EmployeeCallback<Void> callback) {
    execute(new EmployeeCallable<Void>() {
      @Override
      public Void call(IEmployeeService service, ResultStatus status) throws RemoteException {
        service.logout(status);
        return null;
      }
    }, callback);
  }

  public void logout() throws RemoteException, ClientException, ServiceException, BindingException {
    execute(new EmployeeRunnable() {
      @Override
      public void run(IEmployeeService service, ResultStatus status) throws RemoteException {
        service.logout(status);
      }
    });
  }

  private void postOnActiveEmployeeChanged(final Employee employee, final OnActiveEmployeeChangedListener listener) {
    mHandler.post(new Runnable() {
      @Override
      public void run() {
        listener.onActiveEmployeeChanged(employee);
      }
    });
  }
}