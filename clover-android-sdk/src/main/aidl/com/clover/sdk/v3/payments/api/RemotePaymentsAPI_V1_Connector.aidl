package com.clover.sdk.v3.payments.api;

import com.clover.sdk.v3.payments.api.RemotePaymentsAPI_V1_ConnectorListener;
import com.clover.sdk.v3.remotepay.InputOption;

interface RemotePaymentsAPI_V1_Connector {
  void start(in Intent intent, RemotePaymentsAPI_V1_ConnectorListener remotePaymentConnectorListener);
  void finish();
  void sendInputOption(in InputOption option);
}