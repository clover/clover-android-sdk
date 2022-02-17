package com.clover.sdk.v1.printer;

import com.clover.sdk.v1.printer.Printer;
import com.clover.sdk.v1.printer.TypeDetails;
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
 * Intent serviceIntent = new Intent(PrinterIntent.ACTION_PRINTER_SERVICE);
 * serviceIntent.putExtra(PrinterIntent.EXTRA_ACCOUNT, CloverAccount.getAccount(context));
 * serviceIntent.putExtra(PrinterIntent.EXTRA_VERSION, 1);
 * context.bindService(serviceIntent);
 * </code>
 * </pre>
 * For more information about bound services, refer to
 * the Android documentation:
 * <a href="https://developer.android.com/guide/components/bound-services.html#Binding">
 * Bound Services
 * </a>.
 * <br/>
 * You may also interact with the printer service through the
 * {@link com.clover.sdk.v1.printer.PrinterConnector} class, which handles binding and
 * asynchronous invocation of service methods.
 *
 * @see com.clover.sdk.v1.printer.PrinterIntent
 * @see com.clover.sdk.util.CloverAccount
 * @see com.clover.sdk.v1.printer.PrinterConnector
 */
interface IPrinterService {

  /**
   * Get all configured printers for this merchant. A printer must be associated to at least one
   * category to be considered configured. The same printer may be configured for more than one
   * category and if so will appear multiple times in the returned list.
   */
  List<Printer> getPrinters(out ResultStatus status);

  /**
   * Get all printers configured by this merchant for the given category.
   */
  List<Printer> getPrintersByCategory(in Category category, out ResultStatus status);

  /**
   * Returns true if there is any configured printer in the given category.
   */
  boolean isPrinterSet(in Category category, out ResultStatus status);

  /**
   * Returns the printer which matches the {@link Printer#getUuid()} if any, or null if none.
   * This may return a printer which has not been configured.
   */
  Printer getPrinterById(String id, out ResultStatus status);

  /**
   * Removes a printer from being configured. If a category is supplied then the printer has only
   * the configuration for that category removed. If no category is supplied then the printer is
   * completely removed.
   */
  void removePrinter(in Printer p, out ResultStatus status);

  /**
   * Adds a printer optionally with a category. If a new printer is added without a category it
   * is unconfigured. A new or existing printer may configured by supplying a category.
   */
  Printer setPrinter(in Printer p, out ResultStatus status);

  /**
   * Returns the TypeDetails for the given printer.
   */
  TypeDetails getPrinterTypeDetails(in Printer p, out ResultStatus status);

}
