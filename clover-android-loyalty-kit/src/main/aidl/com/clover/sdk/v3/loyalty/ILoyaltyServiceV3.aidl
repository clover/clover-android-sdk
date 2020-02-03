// ILoyaltyServiceV3.aidl
package com.clover.sdk.v3.loyalty;

// Declare any non-default types here with import statements
import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v3.loyalty.LoyaltyDataConfig;
import java.util.Map;

interface ILoyaltyServiceV3 {
   void announceCustomerProvidedData(in LoyaltyDataConfig config, String payload, out ResultStatus status);
   List<LoyaltyDataConfig> getDesiredDataConfig(out ResultStatus status); // this is the super set of the data that the consumers would like to see
   boolean start(String dynamicServiceType, in Map dataExtras, String configuration, out ResultStatus status);
   boolean stop(String dynamicServiceType, out ResultStatus status);
   void updateServiceState(String dynamicService, String state, out ResultStatus status);
   void announceCustomerProvidedDataWithEventId(String uuid, in LoyaltyDataConfig config, String payload, out ResultStatus status);
   boolean stopWithConfiguration(String dynamicServiceType, in Map configuration, out ResultStatus status);
}
