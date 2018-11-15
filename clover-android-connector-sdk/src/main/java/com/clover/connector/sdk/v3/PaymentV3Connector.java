/**
 * Copyright (C) 2016 Clover Network, Inc.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clover.connector.sdk.v3;

import com.clover.sdk.FdParcelable;
import com.clover.sdk.v1.ServiceConnector;
import com.clover.sdk.v3.order.LineItem;
import com.clover.sdk.v3.order.Order;
import com.clover.sdk.v3.remotepay.AuthResponse;
import com.clover.sdk.v3.remotepay.CapturePreAuthResponse;
import com.clover.sdk.v3.remotepay.CloseoutResponse;
import com.clover.sdk.v3.remotepay.ConfirmPaymentRequest;
import com.clover.sdk.v3.remotepay.ManualRefundResponse;
import com.clover.sdk.v3.remotepay.PreAuthResponse;
import com.clover.sdk.v3.remotepay.ReadCardDataResponse;
import com.clover.sdk.v3.remotepay.RefundPaymentResponse;
import com.clover.sdk.v3.remotepay.RetrievePaymentResponse;
import com.clover.sdk.v3.remotepay.RetrievePendingPaymentsResponse;
import com.clover.sdk.v3.remotepay.SaleResponse;
import com.clover.sdk.v3.remotepay.TipAdded;
import com.clover.sdk.v3.remotepay.TipAdjustAuthResponse;
import com.clover.sdk.v3.remotepay.VaultCardResponse;
import com.clover.sdk.v3.remotepay.VerifySignatureRequest;
import com.clover.sdk.v3.remotepay.VoidPaymentRefundResponse;
import com.clover.sdk.v3.remotepay.VoidPaymentResponse;

import android.accounts.Account;
import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Service connector for {@link IPaymentServiceV3}. Please see that class for documentation on the
 * RPC methods.
 *
 * @see IPaymentServiceV3
 * @see ServiceConnector
 * @see Order
 * @see LineItem
 */
public class PaymentV3Connector extends ServiceConnector<IPaymentServiceV3> {
  private static final String SERVICE_HOST = "com.clover.payment.builder.pay";

  private final List<WeakReference<PaymentServiceListener>> mPaymentServiceListener = new CopyOnWriteArrayList<WeakReference<PaymentServiceListener>>();
  private PaymentServiceListenerParent mListener;

  /**
   * Constructs a new OrderConnector object.
   *
   * @param context the Context object, required for establishing a connection to
   *                the service.
   * @param account the Account to use with the service.
   * @param client  an optional object implementing the OnServiceConnectedListener
   *                interface, for receiving connection notifications from the service.
   */
  public PaymentV3Connector(Context context, Account account, OnServiceConnectedListener client) {
    super(context, account, client);
  }

  /**
   * Null-safe wrapper around {@link FdParcelable#getValue()}. Call this over
   * {@link FdParcelable#getValue()} directly to protect against remote exceptions
   * resulting in null return values.
   *
   * @param fdp The {@link FdParcelable} on which to call {@link FdParcelable#getValue()}.
   * @param <V> The value type.
   * @param <P> The parcelable type.
   *
   * @return An object of type V, or null if the argument was null or the argument's
   * {@link FdParcelable#getValue()} method returned null.
   */
  private static <V, P extends FdParcelable<V>> V getValue(P fdp) {
    if (fdp == null) {
      return null;
    }
    return fdp.getValue();
  }

  @Override
  protected String getServiceIntentAction() {
    return PaymentIntent.ACTION_PAYMENT_SERVICE_V3;
  }

  @Override
  protected String getServiceIntentPackage() {
    return SERVICE_HOST;
  }

  @Override
  protected int getServiceIntentVersion() {
    return 3;
  }

  @Override
  protected IPaymentServiceV3 getServiceInterface(IBinder iBinder) {
    return IPaymentServiceV3.Stub.asInterface(iBinder);
  }

  private void postOnPreAuthResponse(final PreAuthResponse response, final PaymentServiceListener listener) {
    mHandler.post(new Runnable() {
      @Override
      public void run() {
        listener.onPreAuthResponse(response);
      }
    });
  }

  protected void notifyServiceConnected(OnServiceConnectedListener client) {
    super.notifyServiceConnected(client);

    try {
      if (mListener == null) {
        mListener = new PaymentServiceListenerParent(this);
      }
      mService.addPaymentServiceListener(mListener);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void disconnect() {
    mPaymentServiceListener.clear();
    if (mListener != null) {
      if (mService != null) {
        try {
          mService.removePaymentServiceListener(mListener);
        } catch (RemoteException e) {
          e.printStackTrace();
        }
      }
      mListener.destroy();
      mListener = null;
    }
    super.disconnect();
  }

  public void addPaymentServiceListener(PaymentServiceListener listener) {
    boolean duplicate = false;
    for (WeakReference<PaymentServiceListener> weakReference : mPaymentServiceListener) {
      PaymentServiceListener listener1 = weakReference.get();
      if (listener1 != null && listener1 == listener) {
        duplicate = true;
        break;
      }
    }
    if (!duplicate) {
      mPaymentServiceListener.add(new WeakReference<PaymentServiceListener>(listener));
    }
  }

  public void removePaymentServiceListener(PaymentServiceListener listener) {
    if (mPaymentServiceListener != null && !mPaymentServiceListener.isEmpty()) {
      WeakReference<PaymentServiceListener> listenerWeakReference = null;
      for (WeakReference<PaymentServiceListener> weakReference : mPaymentServiceListener) {
        PaymentServiceListener listener1 = weakReference.get();
        if (listener1 != null && listener1 == listener) {
          listenerWeakReference = weakReference;
          break;
        }
      }
      if (listenerWeakReference != null) {
        mPaymentServiceListener.remove(listenerWeakReference);
      }
    }
  }

  /**
   * Interface used to allow application to be notified of order updates.
   */
  public interface PaymentServiceListener {

    /**
     * Called in response to a pre auth request
     *
     * @param response
     */
    void onPreAuthResponse(PreAuthResponse response);

    /**
     * Called in response to an auth request
     *
     * @param response
     */
    void onAuthResponse(AuthResponse response);

    /**
     * Called in response to a tip adjust of an auth payment
     *
     * @param response
     */
    void onTipAdjustAuthResponse(TipAdjustAuthResponse response);

    /**
     * Called in response to a capture of a pre auth payment
     *
     * @param response
     */
    void onCapturePreAuthResponse(CapturePreAuthResponse response);

    /**
     * Called when the Clover device requires a signature to be verified
     *
     * @param request
     */
    void onVerifySignatureRequest(VerifySignatureRequest request);

    /**
     * Called when the Clover device requires confirmation for a payment
     * e.g. Duplicates or Offline
     *
     * @param request
     */
    void onConfirmPaymentRequest(ConfirmPaymentRequest request);

    /**
     * Called in response to a sale request
     *
     * @param response
     */
    void onSaleResponse(SaleResponse response);

    /**
     * Called in response to a manual refund request
     *
     * @param response
     */
    void onManualRefundResponse(ManualRefundResponse response);

    /**
     * Called in response to a refund payment request
     *
     * @param response
     */
    void onRefundPaymentResponse(RefundPaymentResponse response);

    /**
     * Called when a customer selects a tip amount on the Clover device screen
     *
     * @param tipAdded
     */
    void onTipAdded(TipAdded tipAdded);

    /**
     * Called in response to a void payment request
     *
     * @param response
     */
    void onVoidPaymentResponse(VoidPaymentResponse response);

    /**
     * Called in response to a vault card request
     *
     * @param response
     */
    void onVaultCardResponse(VaultCardResponse response);

    /**
     * Called in response to a retrievePendingPayment(...) request.
     * @param retrievePendingPaymentResponse
     */
    void onRetrievePendingPaymentsResponse(RetrievePendingPaymentsResponse retrievePendingPaymentResponse);

    /**
     * Called in response to a readCardData(...) request.
     * @param response
     */

    void onReadCardDataResponse(ReadCardDataResponse response);

    /**
     * Called in response to a RetrievePaymentRequest
     *
     * @param response The response
     */
    void onRetrievePaymentResponse(RetrievePaymentResponse response);

    /**
     * Called in response to a closeout being processed
     *
     * @param response The response
     */
    void onCloseoutResponse(CloseoutResponse response);

    /**
     * Called in response to a VoidPaymentRefundRequest
     *
     * @param response The response
     */
    void onVoidPaymentRefundResponse(VoidPaymentRefundResponse response);

  }

  private static class PaymentServiceListenerParent extends IPaymentServiceListener.Stub {

    private PaymentV3Connector mConnector;
    private PaymentServiceListenerParent(PaymentV3Connector connector) {
      mConnector = connector;
    }

    abstract class PaymentConnectorTask implements Runnable {
      private PaymentServiceListener listener;
      public PaymentServiceListener getListener() {
        return listener;
      }

      public void setListener(PaymentServiceListener listener) {
        this.listener = listener;
      }
    }

    private void conditionallyRunTask(PaymentConnectorTask task) {
      final PaymentV3Connector paymentConnector = mConnector;

      if (paymentConnector == null) {
        return; // Shouldn't get here, but bail just in case
      }
      if (paymentConnector.mPaymentServiceListener != null && !paymentConnector.mPaymentServiceListener.isEmpty()) {
        // NOTE: because of the use of CopyOnWriteArrayList, we *must* use an iterator to perform the dispatching. The
        // iterator is a safe guard against listeners that could mutate the list by calling the add/remove methods. This
        // array never changes during the lifetime of the iterator, so interference is impossible and the iterator is guaranteed
        // not to throw ConcurrentModificationException.
        for (WeakReference<PaymentServiceListener> weakReference : paymentConnector.mPaymentServiceListener) {
          final PaymentServiceListener listener = weakReference.get();
          if (listener != null) {
            task.setListener(listener);
            paymentConnector.mHandler.post(task);
          }
        }
      }
    }

    @Override
    public void onPreAuthResponse(final PreAuthResponse response) throws RemoteException {
      conditionallyRunTask(new PaymentConnectorTask() {
        @Override
        public void run() {
          getListener().onPreAuthResponse(response);
        }
      });
    }

    @Override
    public void onAuthResponse(final AuthResponse response) throws RemoteException {
      conditionallyRunTask(new PaymentConnectorTask() {
        @Override
        public void run() {
          getListener().onAuthResponse(response);
        }
      });
    }

    @Override
    public void onTipAdjustAuthResponse(final TipAdjustAuthResponse response) throws RemoteException {
      conditionallyRunTask(new PaymentConnectorTask() {
        @Override
        public void run() {
          getListener().onTipAdjustAuthResponse(response);
        }
      });
    }

    @Override
    public void onCapturePreAuthResponse(final CapturePreAuthResponse response) throws RemoteException {
      conditionallyRunTask(new PaymentConnectorTask() {
        @Override
        public void run() {
          getListener().onCapturePreAuthResponse(response);
        }
      });
    }

    @Override
    public void onVerifySignatureRequest(final VerifySignatureRequest request) throws RemoteException {
      conditionallyRunTask(new PaymentConnectorTask() {
        @Override
        public void run() {
          getListener().onVerifySignatureRequest(request);
        }
      });
    }

    @Override
    public void onConfirmPaymentRequest(final ConfirmPaymentRequest request) throws RemoteException {
      conditionallyRunTask(new PaymentConnectorTask() {
        @Override
        public void run() {
          getListener().onConfirmPaymentRequest(request);
        }
      });
    }

    @Override
    public void onSaleResponse(final SaleResponse response) throws RemoteException {
      conditionallyRunTask(new PaymentConnectorTask() {
        @Override
        public void run() {
          getListener().onSaleResponse(response);
        }
      });
    }

    @Override
    public void onManualRefundResponse(final ManualRefundResponse response) throws RemoteException {
      conditionallyRunTask(new PaymentConnectorTask() {
        @Override
        public void run() {
          getListener().onManualRefundResponse(response);
        }
      });
    }

    @Override
    public void onRefundPaymentResponse(final RefundPaymentResponse response) throws RemoteException {
      conditionallyRunTask(new PaymentConnectorTask() {
        @Override
        public void run() {
          getListener().onRefundPaymentResponse(response);
        }
      });
    }

    @Override
    public void onTipAdded(final TipAdded tipAdded) throws RemoteException {
      conditionallyRunTask(new PaymentConnectorTask() {
        @Override
        public void run() {
          getListener().onTipAdded(tipAdded);
        }
      });
    }

    @Override
    public void onVoidPaymentResponse(final VoidPaymentResponse response) throws RemoteException {
      conditionallyRunTask(new PaymentConnectorTask() {
        @Override
        public void run() {
          getListener().onVoidPaymentResponse(response);
        }
      });
    }

    @Override
    public void onVaultCardResponse(final VaultCardResponse response) throws RemoteException {
      conditionallyRunTask(new PaymentConnectorTask() {
        @Override
        public void run() {
          getListener().onVaultCardResponse(response);
        }
      });
    }

    @Override
    public void onRetrievePendingPaymentsResponse(final RetrievePendingPaymentsResponse retrievePendingPaymentResponse) throws RemoteException {
      conditionallyRunTask(new PaymentConnectorTask() {
        @Override
        public void run() {
          getListener().onRetrievePendingPaymentsResponse(retrievePendingPaymentResponse);
        }
      });
    }

    @Override
    public void onReadCardDataResponse(final ReadCardDataResponse response) throws RemoteException {
      conditionallyRunTask(new PaymentConnectorTask() {
        @Override
        public void run() {
          getListener().onReadCardDataResponse(response);
        }
      });
    }

    @Override
    public void onRetrievePaymentResponse(final RetrievePaymentResponse response) throws RemoteException {
      conditionallyRunTask(new PaymentConnectorTask() {
        @Override
        public void run() {
          getListener().onRetrievePaymentResponse(response);
        }
      });
    }

    @Override
    public void onCloseoutResponse(final CloseoutResponse response) throws RemoteException {
      conditionallyRunTask(new PaymentConnectorTask() {
        @Override
        public void run() {
          getListener().onCloseoutResponse(response);
        }
      });
    }

    @Override
    public void onVoidPaymentRefundResponse(final VoidPaymentRefundResponse response) throws RemoteException {
      conditionallyRunTask(new PaymentConnectorTask() {
        @Override
        public void run() {
          getListener().onVoidPaymentRefundResponse(response);
        }
      });
    }

    // This method must be called when the callback is no longer needed to prevent a memory leak. Due to the design of
    // AIDL services Android unnecessarily retains pointers to otherwise unreferenced instances of this class which in
    // turn are referencing Context objects that consume large amounts of memory.
    public void destroy() {
      mConnector = null;
    }
  }
}
