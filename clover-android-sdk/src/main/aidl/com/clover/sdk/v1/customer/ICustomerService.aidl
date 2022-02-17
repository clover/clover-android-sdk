package com.clover.sdk.v1.customer;

import com.clover.sdk.v1.customer.Card;
import com.clover.sdk.v1.customer.Customer;
import com.clover.sdk.v1.customer.PhoneNumber;
import com.clover.sdk.v1.customer.EmailAddress;
import com.clover.sdk.v1.customer.Address;
import com.clover.sdk.v1.ResultStatus;

/**
 * An interface for interacting with the Clover customer service. The customer
 * service is a bound AIDL service. Bind to this service as follows:
 * <pre>
 * <code>
 * Intent serviceIntent = new Intent(CustomerIntent.ACTION_CUSTOMER_SERVICE);
 * serviceIntent.putExtra(Intents.EXTRA_ACCOUNT, CloverAccount.getAccount(context));
 * serviceIntent.putExtra(Intents.EXTRA_VERSION, 1);
 * context.bindService(serviceIntent);
 * </code>
 * </pre>
 * For more information about bound services, refer to
 * the Android documentation:
 * <a href="https://developer.android.com/guide/components/bound-services.html#Binding">
 * Bound Services
 * </a>
 * <br/><br/>
 * You may also interact with the customer service through the
 * {@link com.clover.sdk.v1.customer.CustomerConnector} class, which handles binding and
 * asynchronous invocation of service methods.
 * <p>
 * @see com.clover.sdk.v1.customer.CustomerIntent
 * @see com.clover.sdk.util.CloverAccount
 * @see com.clover.sdk.v1.customer.CustomerConnector
 */
interface ICustomerService {

    /**
     * Get a list of customers for the merchant bound to the service.
     *
     * @param query A string that we be used to match the first name, last name or phone number against.
     * @return A list of {@link com.clover.sdk.v1.customer.Customer} objects, or <code>null</code> if the service call fails.
     * @clover.perm CUSTOMERS_R
     */
    List<Customer> getCustomers(in String query, out ResultStatus resultStatus);

    /**
     * Returns a single customer for the merchant bound to the service.
     * <p>
     * This call will return immediately with the local version of the customer if one exists on the device,
     * it will then contact the server to check for an updated version. If a new version exists a
     * {@link com.clover.sdk.v1.customer.CustomerIntent#ACTION_CUSTOMER_UPDATE} broadcast will be sent.
     * <p>
     * If no local version of the customer is available then the service will go directly to the server
     * to fetch the customer.
     *
     * @param id The id of the customer being requested.
     * @return A {@link com.clover.sdk.v1.customer.Customer} object.
     * @clover.perm CUSTOMERS_R
     */
    Customer getCustomer(in String customerId, out ResultStatus resultStatus);

    /**
     * Creates a new customer for the merchant bound to the service.
     * <p>
     * This call will return immediately with the new customer.
     *
     * @param firstName        The first name of the customer, can be null.
     * @param lastName         The last name of the customer, can be null.
     * @param marketingAllowed A boolean value of whether or not the customer has authorized
     *                         direct marketing. Please set to false unless you have explicitly
     *                         asked the customer.
     * @return A {@link com.clover.sdk.v1.customer.Customer} object.
     * @clover.perm CUSTOMERS_W
     */
    Customer createCustomer(in String firstName, in String lastName, in boolean marketingAllowed, out ResultStatus resultStatus);

    /**
     * Update the name on the given customer.
     *
     * @param customerId The id of the customer.
     * @param firstName  The first name of the customer, can be null.
     * @param lastName   The last name of the customer, can be null.
     * @clover.perm CUSTOMERS_W
     */
    void setName(in String customerId, in String firstName, in String lastName, out ResultStatus resultStatus);

    /**
     * Update the marketing allowed value on a given customer.
     *
     * @param customerId       The id of the customer.
     * @param marketingAllowed A boolean value of whether or not the customer has authorized
     *                         direct marketing. Please set to false unless you have explicitly
     *                         asked the customer.
     * @clover.perm CUSTOMERS_W or CUSTOMERS_MARKETING_W (EU)
     */
    void setMarketingAllowed(in String customerId, in boolean marketingAllowed, out ResultStatus resultStatus);

    /**
     * Creates a new phone number and adds it to the specified customer
     * <p>
     * This call will return immediately with the new phone number object.
     *
     * @param customerId  The id of the customer.
     * @param phoneNumber The phone number.
     * @return A {@link com.clover.sdk.v1.customer.PhoneNumber} object.
     * @clover.perm CUSTOMERS_W or CUSTOMERS_PHONE_W (EU)
     */
    PhoneNumber addPhoneNumber(in String customerId, in String phoneNumber, out ResultStatus resultStatus);

    /**
     * Updates a given phone number on a customer.
     *
     * @param customerId    The id of the customer.
     * @param phoneNumberId The id of the phone number.
     * @param phoneNumber   The new phone number.
     * @clover.perm CUSTOMERS_W or CUSTOMERS_PHONE_W (EU)
     */
    void setPhoneNumber(in String customerId, in String phoneNumberId, in String phoneNumber, out ResultStatus resultStatus);

    /**
     * Deletes a given phone number from the customer.
     *
     * @param customerId    The id of the customer.
     * @param phoneNumberId The id of the phone number.
     * @clover.perm CUSTOMERS_W or CUSTOMERS_PHONE_W (EU)
     */
    void deletePhoneNumber(in String customerId, in String phoneNumber, out ResultStatus resultStatus);

    /**
     * Creates a new email address and adds it to the specified customer
     * <p>
     * This call will return immediately with the new email address object..
     *
     * @param customerId   The id of the customer.
     * @param emailAddress The email address.
     * @return A {@link com.clover.sdk.v1.customer.EmailAddress} object.
     * @clover.perm CUSTOMERS_W or CUSTOMERS_EMAIL_W (EU)
     */
    EmailAddress addEmailAddress(in String customerId, in String emailAddress, out ResultStatus resultStatus);

    /**
     * Updates a given email address on a customer.
     *
     * @param customerId     The id of the customer.
     * @param emailAddressId The id of the email address.
     * @param emailAddress   The new email address.
     * @clover.perm CUSTOMERS_W or CUSTOMERS_EMAIL_W (EU)
     */
    void setEmailAddress(in String customerId, in String emailAddressId, in String emailAddress, out ResultStatus resultStatus);

    /**
     * Deletes a given email address from the customer.
     *
     * @param customerId     The id of the customer.
     * @param emailAddressId The id of the email address.
     * @clover.perm CUSTOMERS_W or CUSTOMERS_EMAIL_W (EU)
     */
    void deleteEmailAddress(in String customerId, in String emailAddressId, out ResultStatus resultStatus);

    /**
     * Creates an address and adds it to the specified customer
     * <p>
     * This call will return immediately with the new address object.
     *
     * @param customerId The id of the customer.
     * @param address1   The first row of an address.
     * @param address2   The second row of an address.
     * @param address3   The third row of an address.
     * @param city       The city.
     * @param state      The state.
     * @param zip        The zip (postal code).
     * @return A {@link com.clover.sdk.v1.customer.Address} object.
     * @clover.perm CUSTOMERS_W or CUSTOMERS_ADDRESS_W (EU)
     */
    com.clover.sdk.v1.customer.Address addAddress(in String customerId, in String address1, in String address2, in String address3, in String city, in String state, in String zip, out ResultStatus resultStatus);

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
     * @clover.perm CUSTOMERS_W or CUSTOMERS_ADDRESS_W (EU)
     */
    void setAddress(in String customerId, in String addressId, in String address1, in String address2, in String address3, in String city, in String state, in String zip, out ResultStatus resultStatus);

    /**
     * Deletes a given address from the customer.
     *
     * @param customerId The id of the customer.
     * @param addressId  The id of the address.
     * @clover.perm CUSTOMERS_W or CUSTOMERS_ADDRESS_W (EU)
     */
    void deleteAddress(in String customerId, in String addressId, out ResultStatus resultStatus);

    /**
     * Deletes a given customer from the merchant.
     *
     * @param customerId The id of the customer.
     * @clover.perm CUSTOMERS_W
     */
    void deleteCustomer(in String customerId, out ResultStatus resultStatus);

    /**
     * Creates a new vaulted credit/debit card record and adds it to the specified customer
     * <p>
     * This call will return immediately with the new Card object
     *
     * @param customerId   The id of the customer.
     * @param card         The card info.
     * @return A {@link com.clover.sdk.v1.customer.Card} object.
     * @clover.perm CUSTOMERS_W or CUSTOMERS_CARDS_W (EU)
     */
    Card addCard(in String customerId, in Card card, out ResultStatus resultStatus);

    /**
     * Updates a given debit/credit card record for a customer.
     *
     * @param customerId     The id of the customer.
     * @param cardId         The id of the credit/debit card.
     * @param card           The new card info.
     * @clover.perm CUSTOMERS_W or CUSTOMERS_CARDS_W (EU)
     */
    void setCard(in String customerId, in String cardId, in Card card, out ResultStatus resultStatus);

    /**
     * Deletes a given card record from the customer.
     *
     * @param customerId     The id of the customer.
     * @param cardId         The id of the credit/debit card.
     * @clover.perm CUSTOMERS_W or CUSTOMERS_CARDS_W (EU)
     */
    void deleteCard(in String customerId, in String cardId, out ResultStatus resultStatus);

}
