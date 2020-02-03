package com.clover.loyalty;
/*
 * Copyright (C) 2016 Clover Network, Inc.
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

import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v1.ServiceConnector;
import com.clover.sdk.v1.ServiceException;
import com.clover.sdk.v3.loyalty.ILoyaltyServiceV3;
import com.clover.sdk.v3.loyalty.LoyaltyDataConfig;

import android.accounts.Account;
import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.List;
import java.util.Map;

public class LoyaltyV3Connector extends ServiceConnector<ILoyaltyServiceV3> {

  // This needs to be the value of the package from the AndroidManifest of the
  // APK that contains the Service for this.
  //
  // <manifest xmlns:android="http://schemas.android.com/apk/res/android"
  //  xmlns:tools="http://schemas.android.com/tools"
  //      package="com.clover.payment.builder.pay"
  //  android:versionCode="1"
  //  android:versionName="1.0">
  private static final String SERVICE_HOST = "com.clover.payment.builder.pay";

  public LoyaltyV3Connector(Context context, Account account, OnServiceConnectedListener client) {
    super(context, account, client);
  }

  @Override
  protected String getServiceIntentAction() {
    // This needs to be the value of the action from the AndroidManifest of the
    // APK that contains the Service for this.
    //
    //    <service
    //    android:name="com.clover.loyalty.LoyaltyService"
    //    android:exported="true">
    //      <intent-filter>
    //        <action android:name="com.clover.intent.action.LOYALTY_SERVICE_V3"/>
    //      </intent-filter>
    //    </service>
    return LoyaltyIntent.ACTION_LOYALTY_SERVICE_V3;
  }

  @Override
  protected String getServiceIntentPackage() {
    return SERVICE_HOST;
  }

  @Override
  protected ILoyaltyServiceV3 getServiceInterface(IBinder iBinder) {
    return ILoyaltyServiceV3.Stub.asInterface(iBinder);
  }

  public void announceCustomerProvidedData(final LoyaltyDataConfig config, final String payload)  throws RemoteException, ClientException, ServiceException, BindingException {
    execute(new ServiceCallable<ILoyaltyServiceV3, Void>() {
      @Override
      public Void call(ILoyaltyServiceV3 service, ResultStatus status) throws RemoteException {
        service.announceCustomerProvidedData(config, payload, status);
        return null;
      }
    });
  }

  public void announceCustomerProvidedDataWithEventId(final String uuid, final LoyaltyDataConfig config, final String payload) throws RemoteException, ClientException, ServiceException, BindingException {
    execute(new ServiceCallable<ILoyaltyServiceV3, Void>() {
      @Override
      public Void call(ILoyaltyServiceV3 service, ResultStatus status) throws RemoteException {
        service.announceCustomerProvidedDataWithEventId(uuid, config, payload, status);
        return null;
      }
    });
  }


  public List<LoyaltyDataConfig> getDesiredDataConfig() throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<ILoyaltyServiceV3, List<LoyaltyDataConfig>>() {
      @Override
      public List<LoyaltyDataConfig> call(ILoyaltyServiceV3 service, ResultStatus status) throws RemoteException {
        return service.getDesiredDataConfig(status);
      }
    });
  }

  @SuppressWarnings({"Autoboxing", "AutoUnboxing"})
  boolean start(final String dynamicService, final Map<String, String> dataExtras, final String configuration) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<ILoyaltyServiceV3, Boolean>() {
      @Override
      public Boolean call(ILoyaltyServiceV3 service, ResultStatus status) throws RemoteException {
        Boolean serviceResult = service.start(dynamicService, dataExtras, configuration, status);
        // see com.clover.sdk.v1.ServiceConnector.throwOnFailure.
        // status.isSuccess() and status.getStatusCode() == ResultStatus.NOT_FOUND just prevent an
        //  exception from being thrown.
        return serviceResult != null && serviceResult;
      }
    });
  }

  @SuppressWarnings({"Autoboxing", "AutoUnboxing"})
  boolean stop(final String dynamicService) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<ILoyaltyServiceV3, Boolean>() {
      @Override
      public Boolean call(ILoyaltyServiceV3 service, ResultStatus status) throws RemoteException {
        Boolean serviceResult = service.stop(dynamicService, status);
        // see com.clover.sdk.v1.ServiceConnector.throwOnFailure.
        // status.isSuccess() and status.getStatusCode() == ResultStatus.NOT_FOUND just prevent an
        //  exception from being thrown.
        return serviceResult != null && serviceResult;
      }
    });
  }

  @SuppressWarnings({"Autoboxing", "AutoUnboxing"})
  boolean stop(final String dynamicService, Map<String, String> dataExtras) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<ILoyaltyServiceV3, Boolean>() {
      @Override
      public Boolean call(ILoyaltyServiceV3 service, ResultStatus status) throws RemoteException {
        Boolean serviceResult = service.stopWithConfiguration(dynamicService, dataExtras, status);
        // see com.clover.sdk.v1.ServiceConnector.throwOnFailure.
        // status.isSuccess() and status.getStatusCode() == ResultStatus.NOT_FOUND just prevent an
        //  exception from being thrown.
        return serviceResult != null && serviceResult;
      }
    });
  }


  @SuppressWarnings({"Autoboxing", "AutoUnboxing"})
  public void updateServiceState(final String dynamicService, final String state) throws RemoteException, ClientException, ServiceException, BindingException {
    execute(new ServiceCallable<ILoyaltyServiceV3, Void>() {
      @Override
      public Void call(ILoyaltyServiceV3 service, ResultStatus status) throws RemoteException {
        service.updateServiceState(dynamicService, state, status);
        return null;
      }
    });
  }
}