// ILoyaltyServiceProvider.aidl
package com.clover.sdk.v3.loyalty;

// Declare any non-default types here with import statements
import com.clover.sdk.v3.loyalty.LoyaltyDataConfig;
import com.clover.sdk.v3.loyalty.CustomerProvidedDataResponse;

interface ILoyaltyServiceProvider {

    /**
    */
    CustomerProvidedDataResponse onCustomerProvidedData(in String uuid, in LoyaltyDataConfig config, in String payload);

    /**
    */
    List<LoyaltyDataConfig> getLoyaltyDataConfigOfInterest();
}
