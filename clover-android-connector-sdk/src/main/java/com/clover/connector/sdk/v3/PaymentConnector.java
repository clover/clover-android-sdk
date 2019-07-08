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

import com.clover.android.connector.sdk.R;
import com.clover.sdk.v1.ServiceConnector;
import com.clover.sdk.v1.configuration.UIConfiguration;
import com.clover.sdk.v3.apps.AppTracking;
import com.clover.sdk.v3.base.Challenge;
import com.clover.sdk.v3.connector.IDeviceConnector;
import com.clover.sdk.v3.connector.IDeviceConnectorListener;
import com.clover.sdk.v3.connector.IPaymentConnector;
import com.clover.sdk.v3.connector.IPaymentConnectorListener;
import com.clover.sdk.v3.payments.CardTransactionType;
import com.clover.sdk.v3.payments.Payment;
import com.clover.sdk.v3.payments.Result;
import com.clover.sdk.v3.remotepay.AuthRequest;
import com.clover.sdk.v3.remotepay.AuthResponse;
import com.clover.sdk.v3.remotepay.CapturePreAuthRequest;
import com.clover.sdk.v3.remotepay.CapturePreAuthResponse;
import com.clover.sdk.v3.remotepay.CloseoutRequest;
import com.clover.sdk.v3.remotepay.CloseoutResponse;
import com.clover.sdk.v3.remotepay.ConfirmPaymentRequest;
import com.clover.sdk.v3.remotepay.ManualRefundRequest;
import com.clover.sdk.v3.remotepay.ManualRefundResponse;
import com.clover.sdk.v3.remotepay.PaymentResponse;
import com.clover.sdk.v3.remotepay.PreAuthRequest;
import com.clover.sdk.v3.remotepay.PreAuthResponse;
import com.clover.sdk.v3.remotepay.ReadCardDataRequest;
import com.clover.sdk.v3.remotepay.ReadCardDataResponse;
import com.clover.sdk.v3.remotepay.RefundPaymentRequest;
import com.clover.sdk.v3.remotepay.RefundPaymentResponse;
import com.clover.sdk.v3.remotepay.RetrievePaymentRequest;
import com.clover.sdk.v3.remotepay.RetrievePaymentResponse;
import com.clover.sdk.v3.remotepay.RetrievePendingPaymentsResponse;
import com.clover.sdk.v3.remotepay.SaleRequest;
import com.clover.sdk.v3.remotepay.SaleResponse;
import com.clover.sdk.v3.remotepay.TipAdded;
import com.clover.sdk.v3.remotepay.TipAdjustAuthRequest;
import com.clover.sdk.v3.remotepay.TipAdjustAuthResponse;
import com.clover.sdk.v3.remotepay.VaultCardResponse;
import com.clover.sdk.v3.remotepay.VerifySignatureRequest;
import com.clover.sdk.v3.remotepay.VoidPaymentRefundRequest;
import com.clover.sdk.v3.remotepay.VoidPaymentRefundResponse;
import com.clover.sdk.v3.remotepay.VoidPaymentRequest;
import com.clover.sdk.v3.remotepay.VoidPaymentResponse;

import android.accounts.Account;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class PaymentConnector implements IPaymentConnector {
  /**
   * Constructs a new PaymentConnector object.
   *
   * @param context the Context object, required for establishing a connection to
   *                the service.
   * @param account the Account to use with the service.
   * @param client  an optional object implementing the IPaymentConnectorListener
   */
  private PaymentV3Connector paymentV3Connector;
  private AsyncTask waitingTask;
  private ArrayList<IDeviceConnectorListener> listeners = new ArrayList<>();
  private final PaymentV3Connector.PaymentServiceListener paymentServiceListener = new PaymentV3Connector.PaymentServiceListener() {
    @Override
    public void onPreAuthResponse(PreAuthResponse response) {
      setPaymentTypes(response);
      for (IDeviceConnectorListener listener:listeners) {
        ((IPaymentConnectorListener)listener).onPreAuthResponse(response);
      }
    }

    @Override
    public void onAuthResponse(AuthResponse response) {
      setPaymentTypes(response);
      for (IDeviceConnectorListener listener:listeners) {
        ((IPaymentConnectorListener)listener).onAuthResponse(response);
      }
    }

    @Override
    public void onTipAdjustAuthResponse(TipAdjustAuthResponse response) {
      for (IDeviceConnectorListener listener:listeners) {
        ((IPaymentConnectorListener)listener).onTipAdjustAuthResponse(response);
      }
    }

    @Override
    public void onCapturePreAuthResponse(CapturePreAuthResponse response) {
      for (IDeviceConnectorListener listener:listeners) {
        ((IPaymentConnectorListener)listener).onCapturePreAuthResponse(response);
      }
    }

    @Override
    public void onVerifySignatureRequest(VerifySignatureRequest request) {
      for (IDeviceConnectorListener listener:listeners) {
        ((IPaymentConnectorListener)listener).onVerifySignatureRequest(request);
      }
    }

    @Override
    public void onConfirmPaymentRequest(ConfirmPaymentRequest request) {
      for (IDeviceConnectorListener listener:listeners) {
        ((IPaymentConnectorListener)listener).onConfirmPaymentRequest(request);
      }
    }

    @Override
    public void onSaleResponse(SaleResponse response) {
      setPaymentTypes(response);
      for (IDeviceConnectorListener listener:listeners) {
        ((IPaymentConnectorListener)listener).onSaleResponse(response);
      }
    }

    private void setPaymentTypes(PaymentResponse response) {
      // Evaluate the payment that is returned and set the appropriate type
      // in the response.  There are times when the payment processed doesn't always
      // match the payment requested due to many factors including merchant configuration
      // and gateway configuration
      response.setIsSale(false);
      response.setIsAuth(false);
      response.setIsPreAuth(false);
      Payment payment = response.getPayment();
      if (payment != null && payment.getCardTransaction() != null) {
        if (CardTransactionType.AUTH.equals(payment.getCardTransaction().getType()) &&
            Result.SUCCESS.equals(payment.getResult())) {
          response.setIsSale(true);
        } else
        if (CardTransactionType.PREAUTH.equals(payment.getCardTransaction().getType()) &&
            Result.SUCCESS.equals(payment.getResult())) {
          response.setIsAuth(true);
        } else
        if (CardTransactionType.PREAUTH.equals(payment.getCardTransaction().getType()) &&
            Result.AUTH.equals(payment.getResult())) {
          response.setIsPreAuth(true);
        }
      }
    }

    @Override
    public void onManualRefundResponse(ManualRefundResponse response) {
      for (IDeviceConnectorListener listener:listeners) {
        ((IPaymentConnectorListener)listener).onManualRefundResponse(response);
      }
    }

    @Override
    public void onRefundPaymentResponse(RefundPaymentResponse response) {
      for (IDeviceConnectorListener listener:listeners) {
        ((IPaymentConnectorListener)listener).onRefundPaymentResponse(response);
      }
    }

    @Override
    public void onTipAdded(TipAdded tipAdded) {
      for (IDeviceConnectorListener listener:listeners) {
        ((IPaymentConnectorListener)listener).onTipAdded(tipAdded);
      }
    }

    @Override
    public void onVoidPaymentResponse(VoidPaymentResponse response) {
      for (IDeviceConnectorListener listener:listeners) {
        ((IPaymentConnectorListener)listener).onVoidPaymentResponse(response);
      }
    }

    @Override
    public void onVaultCardResponse(VaultCardResponse response) {
      for (IDeviceConnectorListener listener:listeners) {
        ((IPaymentConnectorListener)listener).onVaultCardResponse(response);
      }
    }

    @Override
    public void onRetrievePendingPaymentsResponse(RetrievePendingPaymentsResponse retrievePendingPaymentResponse) {
      for (IDeviceConnectorListener listener:listeners) {
        ((IPaymentConnectorListener)listener).onRetrievePendingPaymentsResponse(retrievePendingPaymentResponse);
      }
    }

    @Override
    public void onReadCardDataResponse(ReadCardDataResponse response) {
      for (IDeviceConnectorListener listener:listeners) {
        ((IPaymentConnectorListener)listener).onReadCardDataResponse(response);
      }
    }

    /**
     * Called in response to a RetrievePaymentRequest
     *
     * @param response The response
     */
    @Override
    public void onRetrievePaymentResponse(RetrievePaymentResponse response) {
      for (IDeviceConnectorListener listener:listeners) {
        ((IPaymentConnectorListener)listener).onRetrievePaymentResponse(response);
      }
    }

    /**
     * Called in response to a closeout being processed
     *
     * @param response The response
     */
    @Override
    public void onCloseoutResponse(CloseoutResponse response) {
      for (IDeviceConnectorListener listener:listeners) {
        ((IPaymentConnectorListener)listener).onCloseoutResponse(response);
      }
    }

    @Override
    public void onVoidPaymentRefundResponse(VoidPaymentRefundResponse response) {
      for (IDeviceConnectorListener listener: listeners) {
        ((IPaymentConnectorListener) listener).onVoidPaymentRefundResponse(response);
      }
    }
  };

  /**
   * @deprecated This constructor is deprecated. Please use {@link #PaymentConnector(Context, Account, IPaymentConnectorListener, String)}
   * @param context
   * @param account
   * @param paymentConnectorListener
   */
  @Deprecated
  public PaymentConnector(Context context, Account account, IPaymentConnectorListener paymentConnectorListener) {
    this(context, account, paymentConnectorListener, null);
  }

  public PaymentConnector(Context context, Account account, IPaymentConnectorListener paymentConnectorListener, String remoteApplicationId) {
    if (paymentConnectorListener != null) {
      addCloverConnectorListener(paymentConnectorListener);
    }

    connectToPaymentService(context, account, remoteApplicationId);
  }

  private void connectToPaymentService(final Context context, Account account, final String remoteApplicationId) {
    if (paymentV3Connector == null) {
      paymentV3Connector = new PaymentV3Connector(context, account, new ServiceConnector.OnServiceConnectedListener() {
        @Override
        public void onServiceConnected(ServiceConnector connector) {
          Log.d(this.getClass().getSimpleName(), "onServiceConnected " + connector);
          paymentV3Connector.addPaymentServiceListener(paymentServiceListener);

          AsyncTask tempWaitingTask = waitingTask;
          waitingTask = null;

          if (tempWaitingTask != null) {
            tempWaitingTask.execute();
          }
          for (IDeviceConnectorListener listener:listeners) {
            listener.onDeviceConnected();
          }
          try {
            String sourceSDK = context.getString(R.string.sdkName);
            String sourceSDKVersion = context.getString(R.string.sdkVersion);
            final AppTracking appTracking = new AppTracking()
                .setApplicationID(remoteApplicationId)
                .setApplicationVersion(getAppVersion(context))
                .setSourceSDK(sourceSDK)
                .setSourceSDKVersion(sourceSDKVersion);
            Log.d(this.getClass().getSimpleName(), "appTracking = " + appTracking.getJSONObject().toString());
            Log.d(this.getClass().getSimpleName(), "before sendAppTracking call ");
            //try {
            paymentV3Connector.getService().sendAppTracking(appTracking);
            //} catch (RemoteException e) {
            //  Log.e(this.getClass().getSimpleName(), " sendAppTracking ", e);
            // }
            Log.d(this.getClass().getSimpleName(), "after sendAppTracking call ");
          } catch (RemoteException e) {
            Log.e(this.getClass().getSimpleName(), " sale", e);
          }
        }

        @Override
        public void onServiceDisconnected(ServiceConnector connector) {
          Log.d(this.getClass().getSimpleName(), "onServiceDisconnected " + connector);
          for (IDeviceConnectorListener listener : listeners) {
            listener.onDeviceDisconnected();
          }
        }
      });
      paymentV3Connector.connect();
    } else {
      if (!paymentV3Connector.isConnected()) {
        paymentV3Connector.connect();
      }
    }
  }

  /**
   * add an IDeviceConnectorListener to receive callbacks
   *
   * @param listener
   */
  @Override
  public void addCloverConnectorListener(IDeviceConnectorListener listener) {
    if (!listeners.contains(listener)) {
      listeners.add(listener);
    }
  }

  /**
   * remove an ICloverConnectorListener from receiving callbacks
   *
   * @param listener
   */
  @Override
  public void removeCloverConnectorListener(IDeviceConnectorListener listener) {
    listeners.remove(listener);
  }

  /**
   * Initialize the CloverConnector's connection. Must be called before calling any other method other than to add or remove listeners
   */
  @Override
  public void initializeConnection() {
    paymentV3Connector.connect();
  }

  /**
   * Closes the PaymentConnector's connection. Should be called after removing any listeners
   */
  @Override
  public void dispose() {
    paymentV3Connector.disconnect();
    listeners.clear();
    paymentV3Connector = null;
  }

  /**
   * Sale method, aka "purchase"
   *
   * @param request    - A SaleRequest object containing basic information needed for the transaction
   */
  @Override
  public void sale(final SaleRequest request) {
    try {
      if (request != null) {
        request.setVersion(2); // this version supports validation
      }
      if (paymentV3Connector != null) {
        if (paymentV3Connector.isConnected()) {
          paymentV3Connector.getService().sale(request);
        } else {
          this.paymentV3Connector.connect();
          waitingTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
              try {
                paymentV3Connector.getService().sale(request);
              } catch (RemoteException e) {
                Log.e(this.getClass().getSimpleName(), " sale", e);
              }
              return null;
            }
          };
        }
      }
    } catch (IllegalArgumentException e) {
      Log.e(this.getClass().getSimpleName(), " sale", e);
    } catch (RemoteException e) {
      Log.e(this.getClass().getSimpleName(), " sale", e);
    }
  }

  /**
   * If signature is captured during a Sale, this method accepts the signature as entered
   *
   * @param request        -
   **/
  @Override
  public void acceptSignature(final VerifySignatureRequest request) {
    try {
      if (paymentV3Connector != null) {
        if (paymentV3Connector.isConnected()) {
          paymentV3Connector.getService().acceptSignature(request);
        } else {
          this.paymentV3Connector.connect();
          waitingTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
              try {
                paymentV3Connector.getService().acceptSignature(request);
              } catch (RemoteException e) {
                Log.e(this.getClass().getSimpleName(), " sale", e);
              }
              return null;
            }
          };
        }
      }
    } catch (IllegalArgumentException e) {
      Log.e(this.getClass().getSimpleName(), " acceptSignature", e);
    } catch (RemoteException e) {
      Log.e(this.getClass().getSimpleName(), " acceptSignature", e);
    }

  }

  /**
   * If signature is captured during a Sale, this method rejects the signature as entered
   *
   * @param request        -
   **/
  @Override
  public void rejectSignature(final VerifySignatureRequest request) {
    try {
      if (paymentV3Connector != null) {
        if (paymentV3Connector.isConnected()) {
          paymentV3Connector.getService().rejectSignature(request);
        } else {
          this.paymentV3Connector.connect();
          waitingTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
              try {
                paymentV3Connector.getService().rejectSignature(request);
              } catch (RemoteException e) {
                Log.e(this.getClass().getSimpleName(), " sale", e);
              }
              return null;
            }
          };
        }
      }
    } catch (IllegalArgumentException e) {
      Log.e(this.getClass().getSimpleName(), " rejectSignature", e);
    } catch (RemoteException e) {
      Log.e(this.getClass().getSimpleName(), " rejectSignature", e);
    }
  }

  /**
   * If payment confirmation is required during a Sale, this method accepts the payment
   *
   * @param payment        -
   **/
  @Override
  public void acceptPayment(final Payment payment) {
    try {
      if (paymentV3Connector != null) {
        if (paymentV3Connector.isConnected()) {
          paymentV3Connector.getService().acceptPayment(payment);
        } else {
          this.paymentV3Connector.connect();
          waitingTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
              try {
                paymentV3Connector.getService().acceptPayment(payment);
              } catch (RemoteException e) {
                Log.e(this.getClass().getSimpleName(), " acceptPayment", e);
              }
              return null;
            }
          };
        }
      }
    } catch (IllegalArgumentException e) {
      Log.e(this.getClass().getSimpleName(), " acceptPayment", e);
    } catch (RemoteException e) {
      Log.e(this.getClass().getSimpleName(), " acceptPayment", e);
    }
  }

  /**
   * If payment confirmation is required during a Sale, this method rejects the payment
   *
   * @param payment        -
   * @param challenge      -
   **/
  @Override
  public void rejectPayment(final Payment payment, final Challenge challenge) {
    try {
      if (paymentV3Connector != null) {
        if (paymentV3Connector.isConnected()) {
          paymentV3Connector.getService().acceptPayment(payment);
        } else {
          this.paymentV3Connector.connect();
          waitingTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
              try {
                paymentV3Connector.getService().rejectPayment(payment, challenge);
              } catch (RemoteException e) {
                Log.e(this.getClass().getSimpleName(), " rejectPayment", e);
              }
              return null;
            }
          };
        }
      }
    } catch (IllegalArgumentException e) {
      Log.e(this.getClass().getSimpleName(), " rejectPayment", e);
    } catch (RemoteException e) {
      Log.e(this.getClass().getSimpleName(), " rejectPayment", e);
    }
  }

  /**
   * Auth method to obtain an Auth payment that can be used as the payment
   * to call tipAdjust
   *
   * @param request        -
   **/
  @Override
  public void auth(final AuthRequest request) {
    try {
      if (request != null) {
        request.setVersion(2); // this version supports validation
      }
      if (paymentV3Connector != null) {
        if (paymentV3Connector.isConnected()) {
          paymentV3Connector.getService().auth(request);
        } else {
          this.paymentV3Connector.connect();
          waitingTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
              try {
                paymentV3Connector.getService().auth(request);
              } catch (RemoteException e) {
                Log.e(this.getClass().getSimpleName(), " auth", e);
              }
              return null;
            }
          };
        }
      }
    } catch (IllegalArgumentException e) {
      Log.e(this.getClass().getSimpleName(), " auth", e);
    } catch (RemoteException e) {
      Log.e(this.getClass().getSimpleName(), " auth", e);
    }

  }

  /**
   * PreAuth method to obtain a Pre-Auth for a card
   *
   * @param request        -
   **/
  @Override
  public void preAuth(final PreAuthRequest request) {
    try {
      if (request != null) {
        request.setVersion(2); // this version supports validation
      }
      if (paymentV3Connector != null) {
        if (paymentV3Connector.isConnected()) {
          paymentV3Connector.getService().preAuth(request);
        } else {
          this.paymentV3Connector.connect();
          waitingTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
              try {
                paymentV3Connector.getService().preAuth(request);
              } catch (RemoteException e) {
                Log.e(this.getClass().getSimpleName(), " preAuth", e);
              }
              return null;
            }
          };
        }
      }
    } catch (IllegalArgumentException e) {
      Log.e(this.getClass().getSimpleName(), " preAuth", e);
    } catch (RemoteException e) {
      Log.e(this.getClass().getSimpleName(), " preAuth", e);
    }
  }

  /**
   * Capture a previous Auth. Note: Should only be called if request's PaymentID is from an AuthResponse
   *
   * @param request -
   **/
  @Override
  public void capturePreAuth(final CapturePreAuthRequest request) {
    try {
      if (request != null) {
        request.setVersion(2); // this version supports validation
      }
      if (paymentV3Connector != null) {
        if (paymentV3Connector.isConnected()) {
          paymentV3Connector.getService().capturePreAuth(request);
        } else {
          this.paymentV3Connector.connect();
          waitingTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
              try {
                paymentV3Connector.getService().capturePreAuth(request);
              } catch (RemoteException e) {
                Log.e(this.getClass().getSimpleName(), " capturePreAuth", e);
              }
              return null;
            }
          };
        }
      }
    } catch (IllegalArgumentException e) {
      Log.e(this.getClass().getSimpleName(), " capturePreAuth", e);
    } catch (RemoteException e) {
      Log.e(this.getClass().getSimpleName(), " capturePreAuth", e);
    }
  }

  /**
   * Adjust the tip for a previous Auth. Note: Should only be called if request's PaymentID is from an AuthResponse
   *
   * @param request -
   **/
  @Override
  public void tipAdjustAuth(final TipAdjustAuthRequest request) {
    try {
      if (request != null) {
        request.setVersion(2); // this version supports validation
      }
      if (paymentV3Connector != null) {
        if (paymentV3Connector.isConnected()) {
          paymentV3Connector.getService().tipAdjustAuth(request);
        } else {
          this.paymentV3Connector.connect();
          waitingTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
              try {
                paymentV3Connector.getService().tipAdjustAuth(request);
              } catch (RemoteException e) {
                Log.e(this.getClass().getSimpleName(), " tipAdjustAuth", e);
              }
              return null;
            }
          };
        }
      }
    } catch (IllegalArgumentException e) {
      Log.e(this.getClass().getSimpleName(), " tipAdjustAuth", e);
    } catch (RemoteException e) {
      Log.e(this.getClass().getSimpleName(), " tipAdjustAuth", e);
    }
  }

  /**
   * Void a transaction, given a previously used order ID and/or payment ID
   *
   * @param request - A VoidRequest object containing basic information needed to void the transaction
   **/
  @Override
  public void voidPayment(final VoidPaymentRequest request) {
    try {
      if (request != null) {
        request.setVersion(2); // this version supports validation
      }
      if (paymentV3Connector != null) {
        if (paymentV3Connector.isConnected()) {
          paymentV3Connector.getService().voidPayment(request);
        } else {
          this.paymentV3Connector.connect();
          waitingTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
              try {
                paymentV3Connector.getService().voidPayment(request);
              } catch (RemoteException e) {
                Log.e(this.getClass().getSimpleName(), " voidPayment", e);
              }
              return null;
            }
          };
        }
      }
    } catch (IllegalArgumentException e) {
      Log.e(this.getClass().getSimpleName(), " voidPayment", e);
    } catch (RemoteException e) {
      Log.e(this.getClass().getSimpleName(), " voidPayment", e);
    }
  }

  /**
   * Refund a specific payment
   *
   * @param request -
   **/
  @Override
  public void refundPayment(final RefundPaymentRequest request) {
    try {
      if (request != null) {
        request.setVersion(2); // this version supports validation
      }
      if (paymentV3Connector != null) {
        if (paymentV3Connector.isConnected()) {
          paymentV3Connector.getService().refundPayment(request);
        } else {
          this.paymentV3Connector.connect();
          waitingTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
              try {
                paymentV3Connector.getService().refundPayment(request);
              } catch (RemoteException e) {
                Log.e(this.getClass().getSimpleName(), " refundPayment", e);
              }
              return null;
            }
          };
        }
      }
    } catch (IllegalArgumentException e) {
      Log.e(this.getClass().getSimpleName(), " refundPayment", e);
    } catch (RemoteException e) {
      Log.e(this.getClass().getSimpleName(), " refundPayment", e);
    }
  }

  /**
   * Manual refund method, aka "naked credit"
   *
   * @param request        - A ManualRefundRequest object
   **/
  @Override
  public void manualRefund(final ManualRefundRequest request) {
    try {
      if (request != null) {
        request.setVersion(2); // this version supports validation
      }
      if (paymentV3Connector != null) {
        if (paymentV3Connector.isConnected()) {
          paymentV3Connector.getService().manualRefund(request);
        } else {
          this.paymentV3Connector.connect();
          waitingTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
              try {
                paymentV3Connector.getService().manualRefund(request);
              } catch (RemoteException e) {
                Log.e(this.getClass().getSimpleName(), " manualRefund", e);
              }
              return null;
            }
          };
        }
      }
    } catch (IllegalArgumentException e) {
      Log.e(this.getClass().getSimpleName(), " manualRefund", e);
    } catch (RemoteException e) {
      Log.e(this.getClass().getSimpleName(), " manualRefund", e);
    }
  }

  /**
   * Vault card information. Requests the mini capture card information and request a payment token from the payment gateway.
   * The value returned in the response is a card, with all the information necessary to use for payment in a SaleRequest or AuthRequest
   *
   * @param cardEntryMethods - The card entry methods allowed to capture the payment token. null will provide default values
   **/
  @Override
  public void vaultCard(final Integer cardEntryMethods) {
    try {
      if (paymentV3Connector != null) {
        if (paymentV3Connector.isConnected()) {
          paymentV3Connector.getService().vaultCard(cardEntryMethods);
        } else {
          this.paymentV3Connector.connect();
          waitingTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
              try {
                paymentV3Connector.getService().vaultCard(cardEntryMethods);
              } catch (RemoteException e) {
                Log.e(this.getClass().getSimpleName(), " vaultCard", e);
              }
              return null;
            }
          };
        }
      }
    } catch (IllegalArgumentException e) {
      Log.e(this.getClass().getSimpleName(), " vaultCard", e);
    } catch (RemoteException e) {
      Log.e(this.getClass().getSimpleName(), " vaultCard", e);
    }
  }

  /**
   * Used to request a list of pending payments that have been taken offline, but
   * haven't processed yet. will trigger an onRetrievePendingPaymentsResponse callback
   */
  @Override
  public void retrievePendingPayments() {
    try {
      if (paymentV3Connector != null) {
        if (paymentV3Connector.isConnected()) {
          paymentV3Connector.getService().retrievePendingPayments();
        } else {
          this.paymentV3Connector.connect();
          waitingTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
              try {
                paymentV3Connector.getService().retrievePendingPayments();
              } catch (RemoteException e) {
                Log.e(this.getClass().getSimpleName(), " retrievePendingPayments", e);
              }
              return null;
            }
          };
        }
      }
    } catch (IllegalArgumentException e) {
      Log.e(this.getClass().getSimpleName(), " retrievePendingPayments", e);
    } catch (RemoteException e) {
      Log.e(this.getClass().getSimpleName(), " retrievePendingPayments", e);
    }
  }

  /**
   * Used to request card information. Specifically track1 and track2 information
   *
   * @param request        - A ReadCardDataRequest object
   */
  @Override
  public void readCardData(final ReadCardDataRequest request) {
    try {
      if (request != null) {
        request.setVersion(2); // this version supports validation
      }
      if (paymentV3Connector != null) {
        if (paymentV3Connector.isConnected()) {
          paymentV3Connector.getService().readCardData(request);
        } else {
          this.paymentV3Connector.connect();
          waitingTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
              try {
                paymentV3Connector.getService().readCardData(request);
              } catch (RemoteException e) {
                Log.e(this.getClass().getSimpleName(), " readCardData", e);
              }
              return null;
            }
          };
        }
      }
    } catch (IllegalArgumentException e) {
      Log.e(this.getClass().getSimpleName(), " readCardData", e);
    } catch (RemoteException e) {
      Log.e(this.getClass().getSimpleName(), " readCardData", e);
    }
  }

  /**
   * Used to request a closeout for open transactions.
   *
   * @param request - A CloseoutRequest object
   */
  @Override
  public void closeout(final CloseoutRequest request) {
    if (request != null) {
      request.setVersion(2); // this version supports validation
    }
    try {
      if (paymentV3Connector != null) {
        if (paymentV3Connector.isConnected()) {
          paymentV3Connector.getService().closeout(request);
        } else {
          this.paymentV3Connector.connect();
          waitingTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
              try {
                paymentV3Connector.getService().closeout(request);
              } catch (RemoteException e) {
                Log.e(this.getClass().getSimpleName(), " closeout", e);
              }
              return null;
            }
          };
        }
      }
    } catch (IllegalArgumentException e) {
      Log.e(this.getClass().getSimpleName(), " closeout", e);
    } catch (RemoteException e) {
      Log.e(this.getClass().getSimpleName(), " closeout", e);
    }
  }

  /**
   * Sends a request to get a payment.
   * Only valid for payments made in the past 24 hours on the device queried
   *
   * @param request - A RetrievePaymentRequest object
   */
  @Override
  public void retrievePayment(final RetrievePaymentRequest request) {
    if (request != null) {
      request.setVersion(2); // this version supports validation
    }
    try {
      if (paymentV3Connector != null) {
        if (paymentV3Connector.isConnected()) {
          paymentV3Connector.getService().retrievePayment(request);
        } else {
          this.paymentV3Connector.connect();
          waitingTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
              try {
                paymentV3Connector.getService().retrievePayment(request);
              } catch (RemoteException e) {
                Log.e(this.getClass().getSimpleName(), " retrievePayment", e);
              }
              return null;
            }
          };
        }
      }
    } catch (IllegalArgumentException e) {
      Log.e(this.getClass().getSimpleName(), " retrievePayment", e);
    } catch (RemoteException e) {
      Log.e(this.getClass().getSimpleName(), " retrievePayment", e);
    }
  }


  @Override
  public void setUIConfiguration(final UIConfiguration configuration) {
    if(configuration != null) {
      try {
        if (paymentV3Connector != null) {
          if (paymentV3Connector.isConnected()) {
            paymentV3Connector.getService().setUIConfiguration(configuration);
          } else {
            this.paymentV3Connector.connect();
            waitingTask = new AsyncTask() {
              @Override
              protected Object doInBackground(Object[] params) {
                try {
                  paymentV3Connector.getService().setUIConfiguration(configuration);
                } catch (RemoteException e) {
                  Log.e(this.getClass().getSimpleName(), " setUIConfiguration", e);
                }
                return null;
              }
            };
          }
        }
      } catch (IllegalArgumentException e) {
        Log.e(this.getClass().getSimpleName(), " setUIConfiguration", e);
      } catch (RemoteException e) {
        Log.e(this.getClass().getSimpleName(), " setUIConfiguration", e);
      }
    }
  }

  @Override
  public void sendDebugLog(final String message) {
    try {
      if(paymentV3Connector != null) {
        if(paymentV3Connector.isConnected()) {
          paymentV3Connector.getService().sendDebugLog(message);
        } else {
          this.paymentV3Connector.connect();
          waitingTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
              try {
                paymentV3Connector.getService().sendDebugLog(message);
              } catch (RemoteException e) {
                Log.e(this.getClass().getSimpleName(), "sendDebugLog", e);
              }
              return null;
            }
          };
        }
      }
    } catch (IllegalArgumentException e) {
      Log.e(this.getClass().getSimpleName(), " sendDebugLog", e);
    } catch (RemoteException e) {
      Log.e(this.getClass().getSimpleName(), " sendDebugLog", e);
    }
  }

  @Override
  public void voidPaymentRefund(final VoidPaymentRefundRequest request) {
    try {
      if(request != null) {
        request.setVersion(2); //allows for validation
      }
      if(paymentV3Connector != null) {
        if(paymentV3Connector.isConnected()) {
          paymentV3Connector.getService().voidPaymentRefund(request);
        } else {
          this.paymentV3Connector.connect();
          waitingTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
              try {
                paymentV3Connector.getService().voidPaymentRefund(request);
              } catch (RemoteException e) {
                Log.e(this.getClass().getSimpleName(), "voidPaymentRefund", e);
              }
              return null;
            }
          };
        }
      }
    } catch (IllegalArgumentException e) {
      Log.e(this.getClass().getSimpleName(), " voidPaymentRefund", e);
    } catch (RemoteException e) {
      Log.e(this.getClass().getSimpleName(), " voidPaymentRefund", e);
    }
  }

  @Override
  public void resetDevice() {
    try {
      if(paymentV3Connector != null) {
        if(paymentV3Connector.isConnected()) {
          paymentV3Connector.getService().resetDevice();
        } else {
          this.paymentV3Connector.connect();
          waitingTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
              try {
                paymentV3Connector.getService().resetDevice();
              } catch (RemoteException e) {
                Log.e(this.getClass().getSimpleName(), "resetDevice", e);
              }
              return null;
            }
          };
        }
      }
    } catch (IllegalArgumentException e) {
      Log.e(this.getClass().getSimpleName(), " resetDevice", e);
    } catch (RemoteException e) {
      Log.e(this.getClass().getSimpleName(), " resetDevice", e);
    }
  }

  public static String getAppVersion(Context context) {
    String version = "Unknown";
    try {
      PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
      version = pInfo.versionName;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return version;
  }

}
