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

import com.clover.sdk.v3.loyalty.LoyaltyDataConfig;

import java.util.List;
import java.util.Map;

/**
 * The interface of the Loyalty Kit
 */
public interface ILoyaltyKit {
  /**
   * Used to announce data that was collected from a customer to interested parties.
   *
   * @param config - the configuration that was used that resulted in the announcement
   * @param payload - the data that was collected.  This is a non-constrained string
   *                (from the viewpoint of this interface), and relies upon the listeners to interpret the data in it.
   * @throws Exception if something bad happens.
   */
  void announceCustomerProvidedData(LoyaltyDataConfig config, String payload) throws Exception;

  /**
   * Used to announce data that was collected from a customer to interested parties. This
   * method is provided for propagation of the eventId in a delegation model.  The eventId should only
   * be the uuid that is sent to com.clover.sdk.v3.loyalty.ILoyaltyServiceProvider#onCustomerProvidedData
   *
   * @param uuid - this is the event id that is delivered to com.clover.sdk.v3.loyalty.ILoyaltyServiceProvider#onCustomerProvidedData.  This
   *             method is provided strictly for propagation of the eventId.
   * @param config - the configuration that was used that resulted in the announcement
   * @param payload - the data that was collected.  This is a non-constrained string
   *                (from the viewpoint of this interface), and relies upon the listeners to interpret the data in it.
   * @throws Exception if something bad happens.
   */
  void announceCustomerProvidedDataWithEventId(String uuid, LoyaltyDataConfig config, String payload) throws Exception;

  /**
   * @return the configurations that are currently registered to this kit.  These are the configurations that may
   *      be returned as the first parameter to the com.clover.loyalty.ILoyaltyKit#announceCustomerProvidedData(com.clover.sdk.v3.loyalty.LoyaltyDataConfig, java.lang.String)
   *
   * @throws Exception if something bad happens.
   */
  List<LoyaltyDataConfig> getDesiredDataConfig() throws Exception; // this is the super set of the data that the consumers would like to see

  /**
   * Starts a dynamic loyalty service
   *
   * @param dynamicService - the 'type' of the service to start
   * @param dataExtras - additional data passed when starting services that might be used by the service as runtime initialization
   * @param configuration - any runtime configuration information needed to start the service.  This can be a json
   *                      object, or some other format.  It is up to the service to interpret the configuration
   * @return true if an attempt was made to start the service.
   * @throws Exception if something bad happens.
   *
   * @see ILoyaltyDataService#EXTRA_LOYALTY_SERVICE_CONFIGURATION for the configuration receipt in the service
   */
  boolean startLoyaltyService(final String dynamicService, Map<String, String> dataExtras, String configuration) throws Exception;

  /**
   * Stops a dynamic loyalty service
   *
   * @param dynamicService - the 'type' of the service to stop
   * @return true if an attempt was made to stop the service.
   * @throws Exception if something bad happens.
   */
  boolean stopLoyaltyService(final String dynamicService) throws Exception;

  boolean stopLoyaltyService(final String dynamicService, Map<String, String> dataExtras) throws Exception;

  /**
   * Called by dynamic services to indicate an update in the service state.
   * @param dynamicService - the 'type' of the service to stop
   * @param state - a string indicating the state
   * @throws Exception if something bad happens.
   *
   * @see LoyaltyDataTypes
   * @see ILoyaltyDataService#LOYALTY_SERVICE_STATE_EVENT_RUNNING
   * @see ILoyaltyDataService#LOYALTY_SERVICE_STATE_EVENT_NOT_RUNNING
   */
  void updateServiceState(String dynamicService, String state) throws Exception;
}
