package com.clover.sdk.v3.payments;

import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v3.payments.PaymentResponse;
import com.clover.sdk.v3.payments.RefundResponse;
import com.clover.sdk.v3.payments.ServiceFeeRequest;
import com.clover.sdk.v3.payments.ServiceFeeRefundRequest;

/**
* Manages billing service fees to an alternative MID.  The general use case applies to government agencies
* who collect fees via credit card plus a service fee that goes to a third party.  Your merchant has to be
* specifically configured to use this service - it is not generically available.
*/
interface IServiceFeeService {

  /**
   *  Calling this method will result in a payment being submitted on behalf of the associated MID that collects
   *  the service fees for your merchant.  The request object must contain the amount, the source payment id (that
   *  the service fee is related to), and the populated vaulted card object including the token
   *
   *  The orderId will be populated internal to the implementation
   *
   *  The PaymentResponse will indicate if the request was successful or no.  If successful, it will contain a payment
   *  object, if not, it should include an error message
   */
  PaymentResponse payServiceFee(in ServiceFeeRequest request, out ResultStatus resultStatus);



  /**
   *  Calling this method will refund the service fee previously charged
   *
   *  The ServiceFeeRefundRequest.refund object must be populated as below:
   *
   *  refund.amount : must match the amount of the original service charge
   *  refund.payment : populated with the original payment
   *
   *  The RefundResponse object will indicate success or failure.  In the case of failure
   *  there will be an error message set.  In the case of success the refund object will
   *  be echo'ed back with additional attributes (e.g id) populated
   *
   */
  RefundResponse refundServiceFee(in ServiceFeeRefundRequest request, out ResultStatus resultStatus);


}