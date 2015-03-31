/*
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

import com.clover.sdk.v1.customer.Customer;
import com.clover.sdk.v1.customer.PhoneNumber;
import com.clover.sdk.v1.customer.EmailAddress;
import com.clover.sdk.v1.customer.Address;
import com.clover.sdk.v1.ResultStatus;

interface ICustomerService {
    List<Customer> getCustomers(in String query, out ResultStatus resultStatus);
    Customer getCustomer(in String customerId, out ResultStatus resultStatus);

    Customer createCustomer(in String firstName, in String lastName, in boolean marketingAllowed, out ResultStatus resultStatus);
    void setName(in String customerId, in String firstName, in String lastName, out ResultStatus resultStatus);
    void setMarketingAllowed(in String customerId, in boolean marketingAllowed, out ResultStatus resultStatus);

    PhoneNumber addPhoneNumber(in String customerId, in String phoneNumber, out ResultStatus resultStatus);
    void setPhoneNumber(in String customerId, in String phoneNumberId, in String phoneNumber, out ResultStatus resultStatus);
    void deletePhoneNumber(in String customerId, in String phoneNumber, out ResultStatus resultStatus);

    EmailAddress addEmailAddress(in String customerId, in String emailAddress, out ResultStatus resultStatus);
    void setEmailAddress(in String customerId, in String emailAddressId, in String emailAddress, out ResultStatus resultStatus);
    void deleteEmailAddress(in String customerId, in String emailAddressId, out ResultStatus resultStatus);

    Address addAddress(in String customerId, in String address1, in String address2, in String address3, in String city, in String state, in String zip, out ResultStatus resultStatus);
    void setAddress(in String customerId, in String addressId, in String address1, in String address2, in String address3, in String city, in String state, in String zip, out ResultStatus resultStatus);
    void deleteAddress(in String customerId, in String addressId, out ResultStatus resultStatus);
}
