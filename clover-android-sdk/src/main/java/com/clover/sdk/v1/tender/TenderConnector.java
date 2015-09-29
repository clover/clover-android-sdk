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
package com.clover.sdk.v1.tender;

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

public class TenderConnector extends ServiceConnector<ITenderService> {
  private static final String TAG = "TenderConnector";

  private abstract static class TenderCallable<T> implements ServiceCallable<ITenderService, T> {
  }

  private abstract static class TenderRunnable implements ServiceRunnable<ITenderService> {
  }

  /**
   * Construct a new merchant connector.
   *
   * @param context The Context in which this connector will bind to the underlying service.
   * @param account The Clover account which is used when binding to the underlying service.
   * @param client  A listener, or null to receive no notifications.
   */
  public TenderConnector(Context context, Account account, OnServiceConnectedListener client) {
    super(context, account, client);
  }

  @Override
  protected String getServiceIntentAction() {
    return TenderIntent.ACTION_TENDER_SERVICE;
  }

  @Override
  protected int getServiceIntentVersion() {
    return 1;
  }

  @Override
  protected ITenderService getServiceInterface(IBinder iBinder) {
    return ITenderService.Stub.asInterface(iBinder);
  }

  public static class TenderCallback<T> implements Callback<T> {
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

  ;

  /**
   * Get the merchant tenders
   */
  public List<Tender> getTenders() throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new TenderCallable<List<Tender>>() {
      @Override
      public List<Tender> call(ITenderService service, ResultStatus status) throws RemoteException {
        return service.getTenders(status);
      }
    });
  }

  public void getTenders(ServiceConnector.Callback<List<Tender>> callback) {
    execute(new TenderCallable<List<Tender>>() {
      @Override
      public List<Tender> call(ITenderService service, ResultStatus status) throws RemoteException {
        return service.getTenders(status);
      }
    }, callback);
  }

  /**
   * Will create a merchant tender for if a tender with the label key.
   */
  public Tender checkAndCreateTender(final String label, final String labelKey, final boolean enabled, final boolean opensCashDrawer) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new TenderCallable<Tender>() {
      @Override
      public Tender call(ITenderService service, ResultStatus status) throws RemoteException {
        return service.checkAndCreateTender(label, labelKey, enabled, opensCashDrawer, status);
      }
    });
  }

  public void checkAndCreateTender(final String label, final String labelKey, final boolean enabled, final boolean opensCashDrawer, ServiceConnector.Callback<Tender> callback) {
    execute(new TenderCallable<Tender>() {
      @Override
      public Tender call(ITenderService service, ResultStatus status) throws RemoteException {
        return service.checkAndCreateTender(label, labelKey, enabled, opensCashDrawer, status);
      }
    }, callback);
  }

  public void deleteTender(final String tenderId) throws RemoteException, ClientException, ServiceException, BindingException {
    execute(new TenderCallable<Void>() {
      @Override
      public Void call(ITenderService service, ResultStatus status) throws RemoteException {
        service.delete(tenderId, status);
        return null;
      }
    });
  }

  public void setOpensCashDrawer(final String tenderId, final boolean opensCashDrawer) throws RemoteException, ClientException, ServiceException, BindingException {
    execute(new TenderCallable<Void>() {
      @Override
      public Void call(ITenderService service, ResultStatus status) throws RemoteException {
        service.setOpensCashDrawer(tenderId, opensCashDrawer, status);
        return null;
      }
    });
  }

  public void setLabel(final String tenderId, final String tenderLabel) throws RemoteException, ClientException, ServiceException, BindingException {
    execute(new TenderCallable<Void>() {
      @Override
      public Void call(ITenderService service, ResultStatus status) throws RemoteException {
        service.setLabel(tenderId, tenderLabel, status);
        return null;
      }
    });
  }

  /**
   * Will set a merchant tenders visibility
   */
  public Tender setEnabled(final String tenderId, final boolean enabled) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new TenderCallable<Tender>() {
      @Override
      public Tender call(ITenderService service, ResultStatus status) throws RemoteException {
        return service.setEnabled(tenderId, enabled, status);
      }
    });
  }

  public void setEnabled(final String tenderId, final boolean enabled, ServiceConnector.Callback<Tender> callback) {
    execute(new TenderCallable<Tender>() {
      @Override
      public Tender call(ITenderService service, ResultStatus status) throws RemoteException {
        return service.setEnabled(tenderId, enabled, status);
      }
    }, callback);
  }

}
