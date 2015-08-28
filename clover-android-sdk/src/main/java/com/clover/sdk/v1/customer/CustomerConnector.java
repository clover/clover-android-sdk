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

public class CustomerConnector extends ServiceConnector<ICustomerService> {
  public CustomerConnector(Context context, Account account, OnServiceConnectedListener client) {
    super(context, account, client);
  }

  @Override
  protected String getServiceIntentAction() {
    return CustomerIntent.ACTION_CUSTOMER_SERVICE;
  }

  @Override
  protected int getServiceIntentVersion() {
    return 1;
  }

  @Override
  protected ICustomerService getServiceInterface(IBinder iBinder) {
    return ICustomerService.Stub.asInterface(iBinder);
  }

  /**
   * Get a list of customers for the merchant bound to the service.
   *
   * @param query A string that we be used to match the first name, last name or phone number against.
   * @return A list of {@link com.clover.sdk.v1.customer.Customer} objects, or <code>null</code> if the service call fails..
   */
  public List<Customer> getCustomers(final String query) throws ClientException, ServiceException, BindingException, RemoteException {
    return execute(new ServiceCallable<ICustomerService, List<Customer>>() {
      public List<Customer> call(ICustomerService service, ResultStatus status) throws RemoteException {
        return service.getCustomers(query, status);
      }
    });
  }

  /**
   * Returns a list of customers for the merchant bound to the service.
   *
   * @return A list of {@link com.clover.sdk.v1.customer.Customer} objects.
   */
  public List<Customer> getCustomers() throws ClientException, ServiceException, BindingException, RemoteException {
    return execute(new ServiceCallable<ICustomerService, List<Customer>>() {
      public List<Customer> call(ICustomerService service, ResultStatus status) throws RemoteException {
        return service.getCustomers(null, status);
      }
    });
  }

  /**
   * Returns a single customer for the merchant bound to the service.
   * <p/>
   * This call will return immediately with the local version of the customer if one exists on the device,
   * it will then contact the server to check for an updated version. If a new version exists a
   * {@link com.clover.sdk.v1.customer.CustomerIntent#ACTION_CUSTOMER_UPDATE} broadcast will be sent.
   * <p/>
   * If no local version of the customer is available then the service will go directly to the server
   * to fetch the customer.
   *
   * @param id The id of the customer being requested.
   * @return A {@link com.clover.sdk.v1.customer.Customer} object.
   */
  public Customer getCustomer(final String id) throws ClientException, ServiceException, BindingException, RemoteException {
    return execute(new ServiceCallable<ICustomerService, Customer>() {
      public Customer call(ICustomerService service, ResultStatus status) throws RemoteException {
        return service.getCustomer(id, status);
      }
    });
  }

  /**
   * Creates a new customer for the merchant bound to the service.
   * <p/>
   * This call will return immediately with the new customer.
   *
   * @param firstName        The first name of the customer, can be null.
   * @param lastName         The last name of the customer, can be null.
   * @param marketingAllowed A boolean value of whether or not the customer has authorized
   *                         direct marketing. Please set to false unless you have explicitly
   *                         asked the customer.
   * @return A {@link com.clover.sdk.v1.customer.Customer} object.
   */
  public Customer createCustomer(final String firstName, final String lastName, final boolean marketingAllowed) throws ClientException, ServiceException, BindingException, RemoteException {
    return execute(new ServiceCallable<ICustomerService, Customer>() {
      public Customer call(ICustomerService service, ResultStatus status) throws RemoteException {
        return service.createCustomer(firstName, lastName, marketingAllowed, status);
      }
    });
  }

  /**
   * Update the name on the given customer.
   *
   * @param customerId The id of the customer.
   * @param firstName  The first name of the customer, can be null.
   * @param lastName   The last name of the customer, can be null.
   */
  public void setName(final String customerId, final String firstName, final String lastName) throws ClientException, ServiceException, BindingException, RemoteException {
    execute(new ServiceRunnable<ICustomerService>() {
      public void run(ICustomerService service, ResultStatus status) throws RemoteException {
        service.setName(customerId, firstName, lastName, status);
      }
    });
  }

  /**
   * Update the marketing allowed value on a given customer.
   *
   * @param customerId       The id of the customer.
   * @param marketingAllowed A boolean value of whether or not the customer has authorized
   *                         direct marketing. Please set to false unless you have explicitly
   *                         asked the customer.
   */
  public void setMarketingAllowed(final String customerId, final boolean marketingAllowed) throws ClientException, ServiceException, BindingException, RemoteException {
    execute(new ServiceRunnable<ICustomerService>() {
      public void run(ICustomerService service, ResultStatus status) throws RemoteException {
        service.setMarketingAllowed(customerId, marketingAllowed, status);
      }
    });
  }

  /**
   * Creates a new phone number and adds it to the specified customer
   * <p/>
   * This call will return immediately with the new phone number object..
   *
   * @param customerId  The id of the customer.
   * @param phoneNumber The phone number.
   * @return A {@link com.clover.sdk.v1.customer.PhoneNumber} object.
   */
  public PhoneNumber addPhoneNumber(final String customerId, final String phoneNumber) throws ClientException, ServiceException, BindingException, RemoteException {
    return execute(new ServiceCallable<ICustomerService, PhoneNumber>() {
      public PhoneNumber call(ICustomerService service, ResultStatus status) throws RemoteException {
        return service.addPhoneNumber(customerId, phoneNumber, status);
      }
    });
  }

  /**
   * Updates a given phone number on a customer.
   *
   * @param customerId    The id of the customer.
   * @param phoneNumberId The id of the phone number.
   * @param phoneNumber   The new phone number.
   */
  public void setPhoneNumber(final String customerId, final String phoneNumberId, final String phoneNumber) throws ClientException, ServiceException, BindingException, RemoteException {
    execute(new ServiceRunnable<ICustomerService>() {
      public void run(ICustomerService service, ResultStatus status) throws RemoteException {
        service.setPhoneNumber(customerId, phoneNumberId, phoneNumber, status);
      }
    });
  }

  /**
   * Deletes a given phone number from the customer.
   *
   * @param customerId    The id of the customer.
   * @param phoneNumberId The id of the phone number.
   */
  public void deletePhoneNumber(final String customerId, final String phoneNumberId) throws ClientException, ServiceException, BindingException, RemoteException {
    execute(new ServiceRunnable<ICustomerService>() {
      public void run(ICustomerService service, ResultStatus status) throws RemoteException {
        service.deletePhoneNumber(customerId, phoneNumberId, status);
      }
    });
  }

  /**
   * Creates a new email address and adds it to the specified customer
   * <p/>
   * This call will return immediately with the new email address object..
   *
   * @param customerId   The id of the customer.
   * @param emailAddress The email address.
   * @return A {@link com.clover.sdk.v1.customer.EmailAddress} object.
   */
  public EmailAddress addEmailAddress(final String customerId, final String emailAddress) throws ClientException, ServiceException, BindingException, RemoteException {
    return execute(new ServiceCallable<ICustomerService, EmailAddress>() {
      public EmailAddress call(ICustomerService service, ResultStatus status) throws RemoteException {
        return service.addEmailAddress(customerId, emailAddress, status);
      }
    });
  }

  /**
   * Updates a given email address on a customer.
   *
   * @param customerId     The id of the customer.
   * @param emailAddressId The id of the email address.
   * @param emailAddress   The new email address.
   */
  public void setEmailAddress(final String customerId, final String emailAddressId, final String emailAddress) throws ClientException, ServiceException, BindingException, RemoteException {
    execute(new ServiceRunnable<ICustomerService>() {
      public void run(ICustomerService service, ResultStatus status) throws RemoteException {
        service.setEmailAddress(customerId, emailAddressId, emailAddress, status);
      }
    });
  }

  /**
   * Deletes a given email address from the customer.
   *
   * @param customerId     The id of the customer.
   * @param emailAddressId The id of the email address.
   */
  public void deleteEmailAddress(final String customerId, final String emailAddressId) throws ClientException, ServiceException, BindingException, RemoteException {
    execute(new ServiceRunnable<ICustomerService>() {
      public void run(ICustomerService service, ResultStatus status) throws RemoteException {
        service.deleteEmailAddress(customerId, emailAddressId, status);
      }
    });
  }

  /**
   * Creates an address and adds it to the specified customer
   * <p/>
   * This call will return immediately with the new address object..
   *
   * @param customerId The id of the customer.
   * @param address1   The first row of an address.
   * @param address2   The second row of an address.
   * @param address3   The third row of an address.
   * @param city       The city.
   * @param state      The state.
   * @param zip        The zip (postal code).
   * @return A {@link com.clover.sdk.v1.customer.Address} object.
   */
  public Address addAddress(final String customerId, final String address1, final String address2, final String address3, final String city, final String state, final String zip) throws ClientException, ServiceException, BindingException, RemoteException {
    return execute(new ServiceCallable<ICustomerService, Address>() {
      public Address call(ICustomerService service, ResultStatus status) throws RemoteException {
        return service.addAddress(customerId, address1, address2, address3, city, state, zip, status);
      }
    });
  }

  /**
   * Updates a given address on a customer.
   *
   * @param customerId The id of the customer.
   * @param addressId  The id of the email address.
   * @param address1   The first row of an address.
   * @param address2   The second row of an address.
   * @param address3   The third row of an address.
   * @param city       The city.
   * @param state      The state.
   * @param zip        The zip (postal code).
   */
  public void setAddress(final String customerId, final String addressId, final String address1, final String address2, final String address3, final String city, final String state, final String zip) throws ClientException, ServiceException, BindingException, RemoteException {
    execute(new ServiceRunnable<ICustomerService>() {
      public void run(ICustomerService service, ResultStatus status) throws RemoteException {
        service.setAddress(customerId, addressId, address1, address2, address3, city, state, zip, status);
      }
    });
  }

  /**
   * Deletes a given address from the customer.
   *
   * @param customerId The id of the customer.
   * @param addressId  The id of the address.
   */
  public void deleteAddress(final String customerId, final String addressId) throws ClientException, ServiceException, BindingException, RemoteException {
    execute(new ServiceRunnable<ICustomerService>() {
      public void run(ICustomerService service, ResultStatus status) throws RemoteException {
        service.deleteAddress(customerId, addressId, status);
      }
    });
  }

  /**
   * Deletes a given customer from the merchant.
   *
   * @param customerId The id of the customer.
   */
  public void deleteCustomer(final String customerId) throws ClientException, ServiceException, BindingException, RemoteException {
    execute(new ServiceRunnable<ICustomerService>() {
      public void run(ICustomerService service, ResultStatus status) throws RemoteException {
        service.deleteCustomer(customerId, status);
      }
    });
  }
}