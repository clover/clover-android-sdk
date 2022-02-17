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

import com.clover.sdk.v3.customers.CustomerInfo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface ILoyaltyDataService {

  String LOYALTY_SERVICE_ACTION_PREFIX = "com.clover.loyalty.service";

  String LOYALTY_SERVICE_STATE_EVENT = LOYALTY_SERVICE_ACTION_PREFIX + ".state";
  String LOYALTY_SERVICE_STATE_EVENT_RUNNING = LOYALTY_SERVICE_STATE_EVENT + ".running";
  String LOYALTY_SERVICE_STATE_EVENT_RUNNING_LISTENING = LOYALTY_SERVICE_STATE_EVENT + ".running.listening";
  String LOYALTY_SERVICE_STATE_EVENT_RUNNING_PENDING = LOYALTY_SERVICE_STATE_EVENT + ".running.pending";
  String LOYALTY_SERVICE_STATE_EVENT_NOT_RUNNING = LOYALTY_SERVICE_STATE_EVENT + ".not.running";

  String LOYALTY_SERVICE_STATE_EVENT_NOT_RUNNING_FORCE = LOYALTY_SERVICE_STATE_EVENT_NOT_RUNNING + ".force";

  String EXTRA_LOYALTY_SERVICE_CONFIGURATION = LOYALTY_SERVICE_ACTION_PREFIX + ".extra.configuration";
  String EXTRA_LOYALTY_SERVICE_DATA_VALUES = LOYALTY_SERVICE_ACTION_PREFIX + ".extra.data_values";

  /**
   * Standard keys used when starting Loyalty Services
   */
  class Configuration {
    public class Customer {
      public static final String CUSTOMER_CONFIGURATION_PREFIX = "customer";

      public static final String EXTERNAL_ID = CUSTOMER_CONFIGURATION_PREFIX + ".externalId";
      public static final String EXTERNAL_SYSTEM_NAME = CUSTOMER_CONFIGURATION_PREFIX + ".externalSystemName";
      public static final String CLOVER_ID = CUSTOMER_CONFIGURATION_PREFIX + ".id";
    }

    public class Order {
      public static final String ORDER_CONFIGURATION_PREFIX = "order";

      public static final String CLOVER_ID = ORDER_CONFIGURATION_PREFIX + ".id";
    }
  }

  class Util {
    private static final String LOYALTY_SERVICE_STATE_PATTERN = LOYALTY_SERVICE_ACTION_PREFIX + ".%s.state";
    private static final String LOYALTY_SERVICE_ACTION_PATTERN = LOYALTY_SERVICE_ACTION_PREFIX + ".%s";

    /**
     * Produces the proper string to use in an event receiver to 'listen' for state change events from
     * a dynamic service.
     *
     * @param dynamicService the dynamic service name.
     * @return the proper string to use in an event receiver to 'listen' for state change events from
     * a dynamic service.
     * @see LoyaltyDataTypes
     */
    public static String getServiceStateEventAction(String dynamicService) {
      return String.format(Locale.US, LOYALTY_SERVICE_STATE_PATTERN, dynamicService);
    }

    /**
     * Produces the string used to start a dynamic service.  This will be used in the future.
     *
     * @param dynamicService the dynamic service name.
     * @return the string used to start a dynamic service.
     * @see LoyaltyDataTypes
     */
    public static String getServiceAction(String dynamicService) {
      return String.format(Locale.US, LOYALTY_SERVICE_ACTION_PATTERN, dynamicService);
    }

    // Before calling this, a system would typically put in VasSettings extras.
    //  This then...
    // puts in the 'CustomerInfo.extras' if there is a CustomerInfo
    // Then puts in NULL values for
    //    ILoyaltyDataService.Configuration.Customer.EXTERNAL_ID,
    //    ILoyaltyDataService.Configuration.Customer.EXTERNAL_SYSTEM_NAME,
    //    ILoyaltyDataService.Configuration.Customer.CLOVER_ID);
    //  To make sure they are NOT set by anything else
    // Then  set
    //    ILoyaltyDataService.Configuration.Customer.EXTERNAL_ID,
    //    ILoyaltyDataService.Configuration.Customer.EXTERNAL_SYSTEM_NAME,
    //    ILoyaltyDataService.Configuration.Customer.CLOVER_ID
    //   to the values from CustomerInfo if there is one
    public static Map<String, String> addToLoyaltyServiceExtras(Map<String, String> map, CustomerInfo customerInfo, String orderId) {
      map = (map == null) ? new HashMap<String, String>() : map;

      // This may override values that were sent in in the map
      if (customerInfo != null) {
        if (customerInfo.getExtras() != null) {
          // Put any extras in directly.  This means that these values may be overridden if the same keys are used as
          // those we use for our 'standard' values.
          map.putAll(customerInfo.getExtras());
        }
      }

      // Clear restricted keys.
      //
      // We have some keys that we will not accept from the POS.  The system sets these values
      // if they are available but we want to ensure they are set to something, at the least 'null'
      // TODO:  This set may need to include additional values. TBD
      List<String> restrictedKeys = Arrays.asList(
          ILoyaltyDataService.Configuration.Customer.EXTERNAL_ID,
          ILoyaltyDataService.Configuration.Customer.EXTERNAL_SYSTEM_NAME,
          ILoyaltyDataService.Configuration.Customer.CLOVER_ID,
          ILoyaltyDataService.Configuration.Order.CLOVER_ID);
      for (String restictedKey : restrictedKeys) {
        map.put(restictedKey, null);
      }

      /*
      Set the specific values below.  These are values that have a specific special meaning to the
      system
       */
      // If the Customer info object is not null, then set the values according to the object valuers.
      //  this could just be null again
      if (customerInfo != null) {
        map.put(com.clover.loyalty.ILoyaltyDataService.Configuration.Customer.EXTERNAL_ID, customerInfo.getExternalId());
        map.put(com.clover.loyalty.ILoyaltyDataService.Configuration.Customer.EXTERNAL_SYSTEM_NAME, customerInfo.getExternalSystemName());
        String customerId = null; // SEMI-2968
        if (customerInfo.getCustomer() != null) {
          customerId = customerInfo.getCustomer().getId();
        }
        map.put(com.clover.loyalty.ILoyaltyDataService.Configuration.Customer.CLOVER_ID, customerId);
      }
      map.put(ILoyaltyDataService.Configuration.Order.CLOVER_ID, orderId);
      return map;
    }
  }
}
