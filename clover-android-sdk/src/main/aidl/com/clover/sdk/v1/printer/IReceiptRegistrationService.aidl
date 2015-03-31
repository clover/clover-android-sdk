/*
 * Copyright (C) 2015 Clover Network, Inc.
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
package com.clover.sdk.v1.printer;

import com.clover.sdk.v1.ResultStatus;
import android.net.Uri;
import com.clover.sdk.v1.printer.ReceiptRegistration;

/**
 * An interface for registering components that wish to add additional data to customer receipts.
 * <br/><br/>
 * Developers that wish to add additional data to customer receipts should call the
 * {@link #register(Uri,ResultStatus)} method to register the URI of a content provider
 * that will provide receipt data.  Said content provider must conform to the
 * {@link ReceiptContract} contract.
 * <br/><br/>
 * The ReceiptRegistration
 * service is a bound AIDL service. Bind to this service as follows,
 * <pre>
 * <code>
 * Intent serviceIntent = new Intent(PrinterIntent.ACTION_RECEIPT_REGISTRATION);
 * serviceIntent.putExtra(PrinterIntent.EXTRA_ACCOUNT, CloverAccount.getAccount(context));
 * serviceIntent.putExtra(PrinterIntent.EXTRA_VERSION, 1);
 * context.bindService(serviceIntent);
 * </code>
 * </pre>
 * For more information about bound services, refer to
 * the Android documentation:
 * <a href="http://developer.android.com/guide/components/bound-services.html#Binding">
 * Bound Services
 * </a>.
 * <br/><br/>
 * You may also interact with the receipt registration service through the
 * {@link com.clover.sdk.v1.printer.ReceiptRegistrationConnector} class, which handles binding and
 * asynchronous invocation of service methods.
 *
 * @see com.clover.sdk.v1.printer.PrinterIntent
 * @see com.clover.sdk.util.CloverAccount
 * @see com.clover.sdk.v1.printer.ReceiptRegistrationConnector
 * @see ReceiptContract
 */
interface IReceiptRegistrationService {
    void register(in Uri uri, out ResultStatus resultStatus);
    void unregister(in Uri uri, out ResultStatus resultStatus);
    List<ReceiptRegistration> getRegistrations(out ResultStatus resultStatus);
}
