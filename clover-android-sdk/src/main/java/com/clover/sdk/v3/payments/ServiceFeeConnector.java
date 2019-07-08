package com.clover.sdk.v3.payments;

import android.accounts.Account;
import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v1.ServiceConnector;
import com.clover.sdk.v1.ServiceException;


/**
 * Manages billing service fees to an alternative MID.  The general use case applies to government agencies
 * who collect fees via credit card plus a service fee that goes to a third party.  Your merchant has to be
 * specifically configured to use this service - it is not generically available.
 */
public class ServiceFeeConnector extends ServiceConnector<IServiceFeeService> {
  private static final String SERVICE_INTENT_ACTION = "com.clover.intent.action.SERVICE_FEE_SERVICE";
  private static final String SERVICE_HOST = "com.clover.payment.builder.pay";


  /**
   * Constructs a ServiceFeeConnector.  This connector users the {@link IServiceFeeService}
   *
   * @param context the Context object, required for establishing a connection to
   *                the service.
   * @param account the Account to use with the service.
   * @param client  an optional object implementing the OnServiceConnectedListener
   */
  public ServiceFeeConnector(Context context, Account account, ServiceConnector.OnServiceConnectedListener client) {
    super(context, account, client);
  }

  @Override
  protected String getServiceIntentAction() {
    return SERVICE_INTENT_ACTION;
  }

  @Override
  protected IServiceFeeService getServiceInterface(IBinder binder) {
    return IServiceFeeService.Stub.asInterface(binder);
  }

  @Override
  protected String getServiceIntentPackage() {
    return SERVICE_HOST;
  }

  /**
   *  Calling this method will result in a payment being submitted on behalf of the associated MID that collects
   *  the service fees for your merchant.
   *
   * @param request The request object must contain the amount, the source payment id (that
   *                the service fee is related to), and the populated vaulted card object including the token
   *                The orderId will be populated internal to the implementation
   * @return PaymentResponse -The PaymentResponse will indicate if the request was successful or no.  If successful,
   *                it will contain a payment object, if not, it should include an error message
   * @throws RemoteException
   * @throws ClientException
   * @throws ServiceException
   * @throws BindingException
   */
  public PaymentResponse payServiceFee(ServiceFeeRequest request) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IServiceFeeService, PaymentResponse>()  {
      @Override
      public PaymentResponse call(IServiceFeeService service, ResultStatus status) throws RemoteException {
        return service.payServiceFee(request, status);
      }
    });
  }

  /**
   * Calling this method will refund the service fee previously charged
   *
   * @param request The ServiceFeeRefundRequest.refund object must be populated as below:
   *                refund.amount : must match the amount of the original service charge
   *                refund.payment : populated with the original payment
   * @return RefundResponse - The RefundResponse object will indicate success or failure.  In the case of failure
   *                           there will be an error message set.  In the case of success the refund object will
   *                           be echo'ed back with additional attributes (e.g id) populated
   * @throws RemoteException
   * @throws ClientException
   * @throws ServiceException
   * @throws BindingException
   */
  public RefundResponse refundServiceFee(ServiceFeeRefundRequest request) throws RemoteException, ClientException, ServiceException, BindingException {
    return execute(new ServiceCallable<IServiceFeeService, RefundResponse>() {
      @Override
      public RefundResponse call(IServiceFeeService service, ResultStatus status) throws RemoteException {
        return service.refundServiceFee(request, status);
      }
    });
  }
}
