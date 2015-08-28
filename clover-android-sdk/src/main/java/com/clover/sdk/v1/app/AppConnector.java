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
package com.clover.sdk.v1.app;

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
public class AppConnector extends ServiceConnector<IAppService> {

  public AppConnector(Context context, Account account) {
    super(context, account, null);
  }

  @Override
  protected IAppService getServiceInterface(IBinder iBinder) {
    return IAppService.Stub.asInterface(iBinder);
  }

  @Override
  protected String getServiceIntentAction() {
    return AppIntent.ACTION_APP_SERVICE;
  }

  @Override
  protected int getServiceIntentVersion() {
    return 1;
  }

  /**
   * Send an app notification synchronously.
   */
  public void notify(final AppNotification notification) throws RemoteException, ClientException, ServiceException, BindingException {
    execute(new ServiceRunnable<IAppService>() {
      @Override
      public void run(IAppService service, ResultStatus status) throws RemoteException {
        service.notify(notification, status);
      }
    });
  }

  /**
   * Send an app notification asynchronously.
   */
  public void notify(final AppNotification notification, Callback<Void> callback) throws RemoteException {
    execute(new ServiceRunnable<IAppService>() {
      @Override
      public void run(IAppService service, ResultStatus status) throws RemoteException {
        service.notify(notification, status);
      }
    }, callback);
  }
}
