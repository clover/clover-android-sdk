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
package com.clover.sdk.v3.apps;

import android.accounts.Account;
import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v1.ServiceConnector;
import com.clover.sdk.v1.ServiceException;

/**
 * Connector that communicates with the app service.
 */
public class AppsConnector extends ServiceConnector<IAppsService> {
  private static final String TAG = "AppsConnector";

  public AppsConnector(Context context, Account account) {
    super(context, account, null);
  }

  @Override
  protected IAppsService getServiceInterface(IBinder iBinder) {
    return IAppsService.Stub.asInterface(iBinder);
  }

  @Override
  protected String getServiceIntentAction() {
    return AppsIntent.ACTION_APPS_SERVICE;
  }

  @Override
  protected int getServiceIntentVersion() {
    return 1;
  }

  public App getApp() throws RemoteException, ServiceException, BindingException, ClientException {
    return execute(new AppCallable<App>() {
      @Override
      public App call(IAppsService service, ResultStatus status) throws RemoteException {
        return service.getApp(status);
      }
    });
  }

  public void logMetered(final String meteredId, final int numberOfEvents) throws RemoteException, ServiceException, BindingException, ClientException {
    execute(new AppCallable<Void>() {
      @Override
      public Void call(IAppsService service, ResultStatus status) throws RemoteException {
        service.logMetered(meteredId, numberOfEvents, status);
        return null;
      }
    });
  }

  public AppBillingInfo getAppBillingInfo() throws RemoteException, ServiceException, BindingException, ClientException {
    return execute(new AppCallable<AppBillingInfo>() {
      @Override
      public AppBillingInfo call(IAppsService service, ResultStatus status) throws RemoteException {
        return service.getAppBillingInfo(status);
      }
    });
  }

  public void setSmartReceiptText(final String text) throws RemoteException, ServiceException, BindingException, ClientException {
    execute(new AppCallable<Void>() {
      @Override
      public Void call(IAppsService service, ResultStatus status) throws RemoteException {
        service.setSmartReceiptText(text, status);

        return null;
      }
    });
  }

  public void setSmartReceiptUrl(final String url) throws RemoteException, ServiceException, BindingException, ClientException {
    execute(new AppCallable<Void>() {
      @Override
      public Void call(IAppsService service, ResultStatus status) throws RemoteException {
        service.setSmartReceiptUrl(url, status);

        return null;
      }
    });
  }

  private abstract static class AppCallable<T> implements ServiceCallable<IAppsService, T> {
  }
}
