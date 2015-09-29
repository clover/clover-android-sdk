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

    com.clover.sdk.v1.customer.Address addAddress(in String customerId, in String address1, in String address2, in String address3, in String city, in String state, in String zip, out ResultStatus resultStatus);
    void setAddress(in String customerId, in String addressId, in String address1, in String address2, in String address3, in String city, in String state, in String zip, out ResultStatus resultStatus);
    void deleteAddress(in String customerId, in String addressId, out ResultStatus resultStatus);
    void deleteCustomer(in String customerId, out ResultStatus resultStatus);
}
