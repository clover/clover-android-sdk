package com.clover.sdk.v3.payments;

import com.clover.sdk.v3.payments.VasMode;
import com.clover.sdk.v3.payments.VasPayload;
import com.clover.sdk.v3.payments.VasPayloadElement;
import com.clover.sdk.v3.payments.VasPayloadResponse;
import com.clover.sdk.v3.payments.VasServiceProvider;

/**
 *   Implement this interface in your application to participate in the exchange of Value Added Services data with
 *   mobile devices.  Your application must define an exported service that includes
 *   clover.intent.action.VAS_PROVIDER in its intent filter.
 */
interface IVasProvider {

  /**
   *  Called when an applicable VasPayload is retrieved from the mobile device.  Note implementors may NOT block
   *  while handling the payload - the VasPayloadResponse must be returned immediately and the payload handled out-of-band
   *
   *  Parameters:
   *  payload: the actual payload from the mobile device
   *  vasMode: the VasMode relevant to the payload (may help receiver make workflow decisions)
   *  extras: intent contains string key/value mappings only.  Non-string values are not supported.
   *          additional information about the transaction IF AVAILABLE (may not be present in some use cases)
   *          possible values include EXTRA_ORDER_ID (with the Clover Order UUID) and EXTRA_PAYMENT_ID (if a payment
   *          was completed at the same time as reading the VAS data).
   *
   *  Returns:
   *  VasPayloadResponse with ResponseType of ACCEPTED (to indicate the data has been received), TXN_UPDATE (to indicate
   *  that the data was received and an update to the Order or Amount is pending, to inclue partial payments)
   */
  VasPayloadResponse handlePayload(in VasPayload payload, in VasMode vasMode, in Intent extras);

  /**
   *  Called to load available providers before starting a txn
   */
  List<VasServiceProvider> getVasProviders();

}