package com.clover.sdk.v1.printer;

import com.clover.sdk.v1.printer.Printer;
import com.clover.sdk.v1.printer.Category;
import com.clover.sdk.v1.ResultStatus;


/**
 * An interface for adding, listing, and removing Clover printers. To print to a Clover printer,
 * use the classes in the {@link com.clover.sdk.v1.printer.job} package. To open a cash
 * drawer connected to a Clover printer, use the {@link com.clover.sdk.v1.printer.CashDrawer} class.
 * To interact with the Clover print queue, use the {@link com.clover.sdk.v1.printer.PrintQueue}
 * class.
 * <br/><br/>
 * The Printer
 * service is a bound AIDL service. Bind to this service as follows,
 * <pre>
 * <code>
 * Intent serviceIntent = new Intent(PrinterIntent.ACTION_MERCHANT_SERVICE);
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
 * You may also interact with the printer service through the
 * {@link com.clover.sdk.v1.printer.PrinterConnector} class, which handles binding and
 * asynchronous invocation of service methods.
 *
 * @see com.clover.sdk.v1.printer.PrinterIntent
 * @see com.clover.sdk.util.CloverAccount
 * @see com.clover.sdk.v1.printer.PrinterConnector
 */
interface IPrinterService {
  List<Printer> getPrinters(out ResultStatus status);
  List<Printer> getPrintersByCategory(in Category category, out ResultStatus status);
  boolean isPrinterSet(in Category category, out ResultStatus status);
  Printer getPrinterById(String id, out ResultStatus status);
  void removePrinter(in Printer p, out ResultStatus status);
  Printer setPrinter(in Printer p, out ResultStatus status);
}
