package com.clover.sdk.v1.printer.job;

import android.content.Context;
import android.os.Bundle;
import com.clover.sdk.v1.CallConnector;
import com.clover.sdk.v1.printer.Printer;

import java.util.List;

import static com.clover.sdk.v1.printer.job.PrintJobsContract.EXTRA_PRINTER;
import static com.clover.sdk.v1.printer.job.PrintJobsContract.EXTRA_PRINTJOB;
import static com.clover.sdk.v1.printer.job.PrintJobsContract.EXTRA_PRINTJOB_ID;
import static com.clover.sdk.v1.printer.job.PrintJobsContract.EXTRA_PRINTJOB_STATE;
import static com.clover.sdk.v1.printer.job.PrintJobsContract.METHOD_GET_PRINTJOB_IDS;
import static com.clover.sdk.v1.printer.job.PrintJobsContract.METHOD_GET_STATE;
import static com.clover.sdk.v1.printer.job.PrintJobsContract.METHOD_PRINT;
import static com.clover.sdk.v1.printer.job.PrintJobsContract.RESULT_GET_PRINTJOB_IDS;
import static com.clover.sdk.v1.printer.job.PrintJobsContract.RESULT_GET_STATE;
import static com.clover.sdk.v1.printer.job.PrintJobsContract.RESULT_PRINT;

/**
 * A class that encapsulates interaction with Clover's print job queue.
 *
 * @see PrintJobsContract
 */
public class PrintJobsConnector extends CallConnector {
  public PrintJobsConnector(Context context) {
    super(context, PrintJobsContract.AUTHORITY_URI);
  }

  /**
   * Print a print job.
   *
   * @param printer a {@link Printer}, the print where the print job shall be printed.
   * @param printJob a {@link PrintJob}, the print job to print.
   *
   * @return a {@link String}, the print job ID.
   */
  public String print(Printer printer, PrintJob printJob) {
    Bundle extras = new Bundle();
    extras.setClassLoader(getClass().getClassLoader());
    extras.putParcelable(EXTRA_PRINTER, printer);
    extras.putParcelable(EXTRA_PRINTJOB, printJob);

    Bundle result = call(METHOD_PRINT, extras);
    return result != null ? result.getString(RESULT_PRINT) : null;
  }

  /**
   * Get the state of a previously printed print job.
   *
   * Only the last 100 print jobs are retained for historical purposes. If the print job for the given
   * ID has been purged from the database, null is returned.
   *
   * @param printJobId a {@link String}, the print job ID.
   *
   * @return an {@link int}, the print job state or {@link null} if the print job does not exist.
   *
   * @see PrintJobsContract#STATE_IN_QUEUE
   * @see PrintJobsContract#STATE_PRINTING
   * @see PrintJobsContract#STATE_DONE
   * @see PrintJobsContract#STATE_ERROR
   */
  public Integer getState(String printJobId) {
    Bundle extras = new Bundle();
    extras.setClassLoader(getClass().getClassLoader());
    extras.putString(EXTRA_PRINTJOB_ID, printJobId);

    Bundle result = call(METHOD_GET_STATE, extras);
    return (result != null) ? result.getInt(RESULT_GET_STATE) : null;
  }

  public List<String> getPrintJobIds(int state) {
    Bundle extras = new Bundle();
    extras.setClassLoader(getClass().getClassLoader());
    extras.putInt(EXTRA_PRINTJOB_STATE, state);

    Bundle result = call(METHOD_GET_PRINTJOB_IDS, extras);
    return (result != null) ? result.getStringArrayList(RESULT_GET_PRINTJOB_IDS) : null;
  }
}
