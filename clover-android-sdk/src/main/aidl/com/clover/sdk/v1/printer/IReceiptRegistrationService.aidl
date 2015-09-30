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
