
package com.clover.sdk.v1.printer.job;

import android.net.Uri;
import android.os.Bundle;
import com.clover.sdk.v1.printer.Printer;

/**
 * Contract for communication with the print jobs provider. The print jobs provider is used for interacting with
 * Clover's print queue.
 *
 * A subset of the total functionality of the print job queue is exposed in this interface. Specifically, clients
 * are allowed to add print jobs ({@link PrintJobsConnector#print(Printer, PrintJob)} and
 * get the state of a print job ({@link PrintJobsConnector#getState(String)}.
 *
 * It is highly recommended to use {@link PrintJobsConnector} to interface with the print job queue, but you can
 * also use {@link android.content.ContentProvider#call(String, String, Bundle)} directly applying the contract
 * documented by this class.
 */
public class PrintJobsContract {
  /** The authority for the modifier provider */
  private static final String AUTHORITY = "com.clover.printjobs";

  /**
   * A content:// style uri to the authority for the print job provider.
   */
  public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

  /**
   * Print job status: in queue. The print job is queued and will be printed shortly, assuming the printer
   * is not offline and is otherwise able to print.
   */
  public static final int STATE_IN_QUEUE = 0;
  /**
   * Print job status: printing. The print job is being sent to the printer.
   */
  public static final int STATE_PRINTING = 1;
  /**
   * Print job status: done. The print job has sent the printer with no errors.
   */
  public static final int STATE_DONE = 2;
  /**
   * Print job status: error. The print job was sent to the printer but printing failed. The system will re-attempt
   * to print the print job shortly.
   */
  public static final int STATE_ERROR = 3;

  /**
   * Call method name to invoke "print". When invoked, the extras bundle must include the printer
   * ({@link #EXTRA_PRINTER}) and the print job ({@link #EXTRA_PRINTJOB}).
   *
   * The result bundle will contain a {@link String} that is the print job ID at the key
   * {@link #RESULT_PRINT}.
   *
   * @see PrintJobsConnector#print(Printer, PrintJob)
   */
  public static final String METHOD_PRINT = "print";
  /**
   * Call method name to invoke "get state". When invoked, the extras bundle must include the print job ID
   * ({@link #EXTRA_PRINTJOB_ID}).
   *
   * The result bundle will contain an {@link int} that is the print job state at the key
   * {@link #RESULT_GET_STATE}.
   *
   * @see PrintJobsConnector#getState(String)
   */
  public static final String METHOD_GET_STATE = "getState";

  /**
   * Call method name to invoke "get print jobs". When invoked, the extras bundle must include a print job state
   * ({@link #EXTRA_PRINTJOB_STATE}). Only print jobs that have the given state are returned.
   *
   * The result bundle will contain an {@link java.util.ArrayList<String>} that is the matching print job IDs
   * at the key {@link #RESULT_GET_PRINTJOB_IDS}.
   *
   * @see PrintJobsConnector#getState(String)
   */
  public static final String METHOD_GET_PRINTJOB_IDS = "getPrintJobIds";

  /**
   * Extra required for call method {@link #METHOD_PRINT}, a {@link Printer} where the print job shall be printed.
   *
   * @see #METHOD_PRINT
   */
  public static final String EXTRA_PRINTER = "printer";
  /**
   * Extra required for call method {@link #METHOD_PRINT}, a {@link PrintJob}, the print job to print.
   *
   * @see #METHOD_PRINT
   */
  public static final String EXTRA_PRINTJOB = "printJob";
  /**
   * Extra required for call method {@link #METHOD_GET_STATE}, a {@link String}, the print job ID to for which to
   * obtain status.
   *
   * @see #METHOD_GET_STATE
   */
  public static final String EXTRA_PRINTJOB_ID = "printerId";
  /**
   * Extra required for call method {@link #METHOD_GET_PRINTJOB_IDS}, an {@link int}, the state of the to be returned
   * print jobs.
   *
   * @see #METHOD_GET_PRINTJOB_IDS
   */
  public static final String EXTRA_PRINTJOB_STATE = "printJobState";

  /**
   * Result returned from from call method {@link #METHOD_PRINT}, a {@link String}, the print job ID.
   *
   * @see #METHOD_PRINT
   */
  public static final String RESULT_PRINT = "print";
  /**
   * Result returned from from call method {@link #METHOD_GET_STATE}, an {@link int}, the print job state.
   *
   * @see #METHOD_GET_STATE
   *
   * @see #STATE_IN_QUEUE
   * @see #STATE_PRINTING
   * @see #STATE_DONE
   * @see #STATE_ERROR
   */
  public static final String RESULT_GET_STATE = "getState";
  /**
   * Result returned from from call method {@link #METHOD_GET_PRINTJOB_IDS}, an {@link java.util.ArrayList<String>},
   * the print job IDs with the given state.
   *
   * @see #METHOD_GET_PRINTJOB_IDS
   *
   * @see #STATE_IN_QUEUE
   * @see #STATE_PRINTING
   * @see #STATE_DONE
   * @see #STATE_ERROR
   */
  public static final String RESULT_GET_PRINTJOB_IDS = "getPrintJobIds";

}
