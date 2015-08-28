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
 * A class that encapsulates interaction with {@link com.clover.sdk.v1.printer.IPrinterService}.
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
public class PrinterConnector extends ServiceConnector<IPrinterService> {
  private static final String TAG = "PrinterConnector";

  private abstract static class PrinterCallable<T> implements ServiceCallable<IPrinterService, T> {
  }

  private abstract static class PrinterRunnable implements ServiceRunnable<IPrinterService> {
  }

  /**
   * An implementation of the {@link com.clover.sdk.v1.ServiceConnector.Callback} interface
   * for receiving asynchronous results from {@link PrinterConnector}
   * methods that provides default method implementations.
   * <p/>
   * The default implementations log the {@link com.clover.sdk.v1.ResultStatus} of the service
   * invocation.
   *
   * @param <T> the result type.
   */
  public static class PrinterCallback<T> implements Callback<T> {
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
  public PrinterConnector(Context context, Account account, OnServiceConnectedListener client) {
    super(context, account, client);
  }

  @Override
  protected String getServiceIntentAction() {
    return PrinterIntent.ACTION_PRINTER_SERVICE;
  }

  @Override
  protected int getServiceIntentVersion() {
    return 1;
  }

  @Override
  protected IPrinterService getServiceInterface(IBinder iBinder) {
    return IPrinterService.Stub.asInterface(iBinder);
  }

  /**
   * Get all printers.
   *
   * @param callback A callback that will be called when results are available.
   */
  public void getPrinters(Callback<List<Printer>> callback) {
    execute(new PrinterCallable<List<Printer>>() {
      @Override
      public List<Printer> call(IPrinterService service, ResultStatus status) throws RemoteException {
        return service.getPrinters(status);
      }
    }, callback);
  }

  public List<Printer> getPrinters() throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new PrinterCallable<List<Printer>>() {
      @Override
      public List<Printer> call(IPrinterService service, ResultStatus status) throws RemoteException {
        return service.getPrinters(status);
      }
    });
  }

  public void getPrinters(final Category category, Callback<List<Printer>> callback) {
    execute(new PrinterCallable<List<Printer>>() {
      @Override
      public List<Printer> call(IPrinterService service, ResultStatus status) throws RemoteException {
        return service.getPrintersByCategory(category, status);
      }
    }, callback);
  }

  public List<Printer> getPrinters(final Category category) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new PrinterCallable<List<Printer>>() {
      @Override
      public List<Printer> call(IPrinterService service, ResultStatus status) throws RemoteException {
        return service.getPrintersByCategory(category, status);
      }
    });
  }

  public void isPrinterSet(final Category category, Callback<Boolean> callback) {
    execute(new PrinterCallable<Boolean>() {
      @Override
      public Boolean call(IPrinterService service, ResultStatus status) throws RemoteException {
        return service.isPrinterSet(category, status);
      }
    }, callback);
  }

  public boolean isPrinterSet(final Category category) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new PrinterCallable<Boolean>() {
      @Override
      public Boolean call(IPrinterService service, ResultStatus status) throws RemoteException {
        return service.isPrinterSet(category, status);
      }
    });
  }

  public void getPrinter(final String id, Callback<Printer> callback) {
    execute(new PrinterCallable<Printer>() {
      @Override
      public Printer call(IPrinterService service, ResultStatus status) throws RemoteException {
        return service.getPrinterById(id, status);
      }
    }, callback);
  }

  public Printer getPrinter(final String id) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new PrinterCallable<Printer>() {
      @Override
      public Printer call(IPrinterService service, ResultStatus status) throws RemoteException {
        return service.getPrinterById(id, status);
      }
    });
  }

  public void setPrinter(final Printer printer, Callback<Printer> callback) {
    execute(new PrinterCallable<Printer>() {
      @Override
      public Printer call(IPrinterService service, ResultStatus status) throws RemoteException {
        return service.setPrinter(printer, status);
      }
    }, callback);
  }

  public Printer setPrinter(final Printer printer) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new PrinterCallable<Printer>() {
      @Override
      public Printer call(IPrinterService service, ResultStatus status) throws RemoteException {
        return service.setPrinter(printer, status);
      }
    });
  }

  public void removePrinter(final Printer printer, Callback<Void> callback) {
    execute(new PrinterCallable<Void>() {
      @Override
      public Void call(IPrinterService service, ResultStatus status) throws RemoteException {
        service.removePrinter(printer, status);
        return null;
      }
    }, callback);
  }

  public void removePrinter(final Printer printer) throws RemoteException, ClientException, ServiceException, BindingException {
    execute(new PrinterRunnable() {
      @Override
      public void run(IPrinterService service, ResultStatus status) throws RemoteException {
        service.removePrinter(printer, status);
      }
    });
  }
}