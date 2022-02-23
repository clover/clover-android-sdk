/**
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
package com.clover.sdk.v1.customer;

import android.accounts.Account;
import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v1.ServiceConnector;
import com.clover.sdk.v1.ServiceException;

import java.util.List;

/**
 * Service connector for {@link ICustomerService}. Please see that class for documentation on the
 * RPC methods.
 *
 * @see ICustomerService
 * @see ServiceConnector
 * @see Customer
 * @see Address
 * @see Card
 * @see EmailAddress
 * @see Order
 * @see PhoneNumber
 */
public class CustomerConnector extends ServiceConnector<ICustomerService> {
  private static final String SERVICE_HOST = "com.clover.engine";

  public CustomerConnector(Context context, Account account, OnServiceConnectedListener client) {
    super(context, account, client);
  }

  @Override
  protected String getServiceIntentAction() {
    return CustomerIntent.ACTION_CUSTOMER_SERVICE;
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
  protected ICustomerService getServiceInterface(IBinder iBinder) {
    return ICustomerService.Stub.asInterface(iBinder);
  }

  public List<Customer> getCustomers(final String query) throws ClientException, ServiceException, BindingException, RemoteException {
    return execute(new ServiceCallable<ICustomerService, List<Customer>>() {
      public List<Customer> call(ICustomerService service, ResultStatus status) throws RemoteException {
        return service.getCustomers(query, status);
      }
    });
  }

  public List<Customer> getCustomers() throws ClientException, ServiceException, BindingException, RemoteException {
    return execute(new ServiceCallable<ICustomerService, List<Customer>>() {
      public List<Customer> call(ICustomerService service, ResultStatus status) throws RemoteException {
        return service.getCustomers(null, status);
      }
    });
  }

  public Customer getCustomer(final String id) throws ClientException, ServiceException, BindingException, RemoteException {
    return execute(new ServiceCallable<ICustomerService, Customer>() {
      public Customer call(ICustomerService service, ResultStatus status) throws RemoteException {
        return service.getCustomer(id, status);
      }
    });
  }

  public Customer createCustomer(final String firstName, final String lastName, final boolean marketingAllowed) throws ClientException, ServiceException, BindingException, RemoteException {
    return execute(new ServiceCallable<ICustomerService, Customer>() {
      public Customer call(ICustomerService service, ResultStatus status) throws RemoteException {
        return service.createCustomer(firstName, lastName, marketingAllowed, status);
      }
    });
  }

  public void setName(final String customerId, final String firstName, final String lastName) throws ClientException, ServiceException, BindingException, RemoteException {
    execute(new ServiceRunnable<ICustomerService>() {
      public void run(ICustomerService service, ResultStatus status) throws RemoteException {
        service.setName(customerId, firstName, lastName, status);
      }
    });
  }

  public void setMarketingAllowed(final String customerId, final boolean marketingAllowed) throws ClientException, ServiceException, BindingException, RemoteException {
    execute(new ServiceRunnable<ICustomerService>() {
      public void run(ICustomerService service, ResultStatus status) throws RemoteException {
        service.setMarketingAllowed(customerId, marketingAllowed, status);
      }
    });
  }

  public PhoneNumber addPhoneNumber(final String customerId, final String phoneNumber) throws ClientException, ServiceException, BindingException, RemoteException {
    return execute(new ServiceCallable<ICustomerService, PhoneNumber>() {
      public PhoneNumber call(ICustomerService service, ResultStatus status) throws RemoteException {
        return service.addPhoneNumber(customerId, phoneNumber, status);
      }
    });
  }

  public void setPhoneNumber(final String customerId, final String phoneNumberId, final String phoneNumber) throws ClientException, ServiceException, BindingException, RemoteException {
    execute(new ServiceRunnable<ICustomerService>() {
      public void run(ICustomerService service, ResultStatus status) throws RemoteException {
        service.setPhoneNumber(customerId, phoneNumberId, phoneNumber, status);
      }
    });
  }

  public void deletePhoneNumber(final String customerId, final String phoneNumberId) throws ClientException, ServiceException, BindingException, RemoteException {
    execute(new ServiceRunnable<ICustomerService>() {
      public void run(ICustomerService service, ResultStatus status) throws RemoteException {
        service.deletePhoneNumber(customerId, phoneNumberId, status);
      }
    });
  }

  public EmailAddress addEmailAddress(final String customerId, final String emailAddress) throws ClientException, ServiceException, BindingException, RemoteException {
    return execute(new ServiceCallable<ICustomerService, EmailAddress>() {
      public EmailAddress call(ICustomerService service, ResultStatus status) throws RemoteException {
        return service.addEmailAddress(customerId, emailAddress, status);
      }
    });
  }

  public void setEmailAddress(final String customerId, final String emailAddressId, final String emailAddress) throws ClientException, ServiceException, BindingException, RemoteException {
    execute(new ServiceRunnable<ICustomerService>() {
      public void run(ICustomerService service, ResultStatus status) throws RemoteException {
        service.setEmailAddress(customerId, emailAddressId, emailAddress, status);
      }
    });
  }

  public void deleteEmailAddress(final String customerId, final String emailAddressId) throws ClientException, ServiceException, BindingException, RemoteException {
    execute(new ServiceRunnable<ICustomerService>() {
      public void run(ICustomerService service, ResultStatus status) throws RemoteException {
        service.deleteEmailAddress(customerId, emailAddressId, status);
      }
    });
  }

  public Address addAddress(final String customerId, final String address1, final String address2, final String address3, final String city, final String state, final String zip) throws ClientException, ServiceException, BindingException, RemoteException {
    return execute(new ServiceCallable<ICustomerService, Address>() {
      public Address call(ICustomerService service, ResultStatus status) throws RemoteException {
        return service.addAddress(customerId, address1, address2, address3, city, state, zip, status);
      }
    });
  }

  public void setAddress(final String customerId, final String addressId, final String address1, final String address2, final String address3, final String city, final String state, final String zip) throws ClientException, ServiceException, BindingException, RemoteException {
    execute(new ServiceRunnable<ICustomerService>() {
      public void run(ICustomerService service, ResultStatus status) throws RemoteException {
        service.setAddress(customerId, addressId, address1, address2, address3, city, state, zip, status);
      }
    });
  }

  public void deleteAddress(final String customerId, final String addressId) throws ClientException, ServiceException, BindingException, RemoteException {
    execute(new ServiceRunnable<ICustomerService>() {
      public void run(ICustomerService service, ResultStatus status) throws RemoteException {
        service.deleteAddress(customerId, addressId, status);
      }
    });
  }

  public void deleteCustomer(final String customerId) throws ClientException, ServiceException, BindingException, RemoteException {
    execute(new ServiceRunnable<ICustomerService>() {
      public void run(ICustomerService service, ResultStatus status) throws RemoteException {
        service.deleteCustomer(customerId, status);
      }
    });
  }

  public Card addCard(final String customerId, final Card card) throws ClientException, ServiceException, BindingException, RemoteException {
    return execute(new ServiceCallable<ICustomerService, Card>() {
      public Card call(ICustomerService service, ResultStatus status) throws RemoteException {
        return service.addCard(customerId, card, status);
      }
    });
  }

  public void setCard(final String customerId, final String cardId, final Card card) throws ClientException, ServiceException, BindingException, RemoteException {
    execute(new ServiceRunnable<ICustomerService>() {
      public void run(ICustomerService service, ResultStatus status) throws RemoteException {
        service.setCard(customerId, cardId, card, status);
      }
    });
  }

  public void deleteCard(final String customerId, final String cardId) throws ClientException, ServiceException, BindingException, RemoteException {
    execute(new ServiceRunnable<ICustomerService>() {
      public void run(ICustomerService service, ResultStatus status) throws RemoteException {
        service.deleteCard(customerId, cardId, status);
      }
    });
  }
}