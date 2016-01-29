/**
 * Copyright (C) 2015 Clover Network, Inc.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clover.sdk.v3.employees;

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

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A class that encapsulates interaction with {@link com.clover.sdk.v3.employees.IEmployeeService}.
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

  private final List<WeakReference<OnActiveEmployeeChangedListener>> mOnActiveEmployeeChangedListener = new CopyOnWriteArrayList<WeakReference<OnActiveEmployeeChangedListener>>();

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
   * for receiving asynchronous results from {@link com.clover.sdk.v3.employees.EmployeeConnector}
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

  private static class OnEmployeeListenerParent extends IEmployeeListener.Stub {

    private EmployeeConnector mConnector;

    private OnEmployeeListenerParent(EmployeeConnector connector) {
      mConnector = connector;
    }

    @Override
    public void onActiveEmployeeChanged(final Employee employee) {
      final EmployeeConnector employeeConnector = mConnector;

      if (employeeConnector == null) {
        return; // Shouldn't get here, but bail just in case
      }
      if (employeeConnector.mOnActiveEmployeeChangedListener != null && !employeeConnector.mOnActiveEmployeeChangedListener.isEmpty()) {
        // NOTE: because of the use of CopyOnWriteArrayList, we *must* use an iterator to perform the dispatching. The
        // iterator is a safe guard against listeners that could mutate the list by calling the add/remove methods. This
        // array never changes during the lifetime of the iterator, so interference is impossible and the iterator is guaranteed
        // not to throw ConcurrentModificationException.
        for (WeakReference<OnActiveEmployeeChangedListener> weakReference : employeeConnector.mOnActiveEmployeeChangedListener) {
          OnActiveEmployeeChangedListener listener = weakReference.get();
          if (listener != null) {
            employeeConnector.postOnActiveEmployeeChanged(employee, listener);
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

  private OnEmployeeListenerParent mListener;

  public EmployeeConnector(Context context, Account account, OnServiceConnectedListener client) {
    super(context, account, client);
  }

  @Override
  protected String getServiceIntentAction() {
    return EmployeeIntent.ACTION_EMPLOYEE_SERVICE_V3;
  }

  @Override
  protected int getServiceIntentVersion() {
    return 1;
  }

  @Override
  protected IEmployeeService getServiceInterface(IBinder iBinder) {
    return IEmployeeService.Stub.asInterface(iBinder);
  }

  @Override
  public void disconnect() {
    mOnActiveEmployeeChangedListener.clear();

    synchronized (this) {
      if (mListener != null) {
        if (mService != null) {
          try {
            mService.removeListener(mListener, new ResultStatus());
          } catch (RemoteException e) {
            e.printStackTrace();
          }
        }
        mListener.destroy();
        mListener = null;
      }
    }
    super.disconnect();
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

  public Employee createEmployee(final Employee employee) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new EmployeeCallable<Employee>() {
      @Override
      public Employee call(IEmployeeService service, ResultStatus status) throws RemoteException {
        return service.createEmployee(employee, status);
      }
    });
  }

  public void createEmployee(final Employee employee, EmployeeCallback<Employee> callback) {
    execute(new EmployeeCallable<Employee>() {
      @Override
      public Employee call(IEmployeeService service, ResultStatus status) throws RemoteException {
        return service.createEmployee(employee, status);
      }
    }, callback);
  }

  public Employee updateEmployee(final Employee employee) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new EmployeeCallable<Employee>() {
      @Override
      public Employee call(IEmployeeService service, ResultStatus status) throws RemoteException {
        return service.updateEmployee(employee, status);
      }
    });
  }

  public void setEmployeePin(final String id, final String pin, EmployeeCallback<Employee> callback) {
    execute(new EmployeeCallable<Employee>() {
      @Override
      public Employee call(IEmployeeService service, ResultStatus status) throws RemoteException {
        return service.setEmployeePin(id, pin, status);
      }
    }, callback);
  }

  public Employee setEmployeePin(final String id, final String pin) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new EmployeeCallable<Employee>() {
      @Override
      public Employee call(IEmployeeService service, ResultStatus status) throws RemoteException {
        return service.setEmployeePin(id, pin, status);
      }
    });
  }

  public void updateEmployee(final Employee employee, EmployeeCallback<Employee> callback) {
    execute(new EmployeeCallable<Employee>() {
      @Override
      public Employee call(IEmployeeService service, ResultStatus status) throws RemoteException {
        return service.updateEmployee(employee, status);
      }
    }, callback);
  }

  public void deleteEmployee(final String employeeId) throws RemoteException, ClientException, ServiceException, BindingException {
    execute(new EmployeeCallable<Void>() {
      @Override
      public Void call(IEmployeeService service, ResultStatus status) throws RemoteException {
        service.deleteEmployee(employeeId, status);
        return null;
      }
    });
  }

  public void deleteEmployee(final String employeeId, EmployeeCallback<Void> callback) {
    execute(new EmployeeCallable<Void>() {
      @Override
      public Void call(IEmployeeService service, ResultStatus status) throws RemoteException {
        service.deleteEmployee(employeeId, status);
        return null;
      }
    }, callback);
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

  public void addOnActiveEmployeeChangedListener(OnActiveEmployeeChangedListener listener) {
    if (mOnActiveEmployeeChangedListener.isEmpty()) {
      execute(new EmployeeCallable<Void>() {
        @Override
        public Void call(IEmployeeService service, ResultStatus status) throws RemoteException {
          synchronized (EmployeeConnector.this) {
            if (mListener == null) {
              mListener = new OnEmployeeListenerParent(EmployeeConnector.this);
              service.addListener(mListener, status);
            }
          }
          return null;
        }
      }, new EmployeeCallback<Void>());
    }
    mOnActiveEmployeeChangedListener.add(new WeakReference<OnActiveEmployeeChangedListener>(listener));
  }

  public void removeOnActiveEmployeeChangedListener(OnActiveEmployeeChangedListener listener) {
    if (mOnActiveEmployeeChangedListener != null && !mOnActiveEmployeeChangedListener.isEmpty()) {
      WeakReference<OnActiveEmployeeChangedListener> listenerWeakReference = null;
      for (WeakReference<OnActiveEmployeeChangedListener> weakReference : mOnActiveEmployeeChangedListener) {
        OnActiveEmployeeChangedListener listener1 = weakReference.get();
        if (listener1 != null && listener1 == listener) {
          listenerWeakReference = weakReference;
          break;
        }
      }
      if (listenerWeakReference != null) {
        mOnActiveEmployeeChangedListener.remove(listenerWeakReference);
      }

      if (mOnActiveEmployeeChangedListener.isEmpty()) {
        synchronized (EmployeeConnector.this) {
          if (isConnected() && mListener != null) {
            try {
              mService.removeListener(mListener, new ResultStatus());
              mListener.destroy();
              mListener = null;
            } catch (RemoteException e) {
              e.printStackTrace();
            }
          }
        }
      }
    }
  }
}
