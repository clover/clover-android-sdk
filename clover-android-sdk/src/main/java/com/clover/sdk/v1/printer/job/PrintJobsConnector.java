package com.clover.sdk.v1.printer.job;

import android.accounts.Account;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.clover.sdk.v1.CallConnector;
import com.clover.sdk.v1.printer.Category;
import com.clover.sdk.v1.printer.Printer;
import com.clover.sdk.v1.printer.PrinterConnector;

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
   * Print a {@link PrintJob}. If the provided {@link Printer} is not configured
   * {@code null} is returned. If the provided {@link Printer} is null then
   * this method is equivalent to {@link #print(PrintJob)}.
   *
   * To retrieve the list of suitable printers use {@link PrinterConnector#getPrinters(Category)}
   * passing the print job's {@link PrintJob#getPrinterCategory()} as the argument.
   *
   * @param printer a {@link Printer}, where the print job shall be printed.
   * @param printJob a {@link PrintJob}, the print job to print.
   *
   * @return a {@link String}, the print job ID or {@code null} if the print job was
   * not queued.
   */
  @Nullable
  public String print(@Nullable Printer printer, @NonNull PrintJob printJob) {
    Bundle extras = new Bundle();
    extras.setClassLoader(getClass().getClassLoader());
    extras.putParcelable(EXTRA_PRINTER, printer);
    extras.putParcelable(EXTRA_PRINTJOB, printJob);

    Bundle result = call(METHOD_PRINT, extras);
    return result != null ? result.getString(RESULT_PRINT) : null;
  }

  /**
   * Print a {@link PrintJob} to any configured suitable printer. A suitable printer
   * is one where the printer's category ({@link Printer#getCategory()} matches the
   * print job's category ({@link PrintJob#getPrinterCategory()}. If no suitable printer
   * is configured the job is not printed and {@code null} is returned.
   * <p>
   * If there are multiple suitable configured printers the order of selection
   * is not defined. The selection is not stable; it may be different
   * for each invocations of this method.
   * <p>
   * If it is preferable to ensure a suitable printer is configured before printing
   * call {@link PrinterConnector#getPrinters(Category)} passing
   * {@link PrintJob#getPrinterCategory()} as the argument, and pass the
   * explicit printer argument to {@link #print(Printer, PrintJob)}
   * instead of this method.
   * <p>
   * This method is a convenience. The caller is not required to retrieve and evaluate
   * the list of configured printers first and select one, possibly
   * involving the user. Most merchants have at most one printer of each type (receipt,
   * order, label, fiscal) configured. This however is not guaranteed.
   * <p>
   * This method is equivalent to calling {@link PrintJob#print(Context, Account)}
   * with {@link PrintJob#printToAny} set to true. The actual value of
   * {@link PrintJob#printToAny} is not considered here.
   *
   * @param printJob a {@link PrintJob}, the print job to print.
   *
   * @return a {@link String}, the print job ID or {@code null} if the print job was
   * not queued.
   */
  @Nullable
  public String print(@NonNull PrintJob printJob) {
    //noinspection ConstantConditions
    return print(null, printJob);
  }

  /**
   * Get the state of a previously printed print job.
   * <p>
   * Jobs may be remove from the queue because of their age, or because of the size
   * of the queue. If the job for the provided ID has been removed from the database,
   * null is returned. It is not possible to infer success or failure from the absence
   * of a print job ID in the queue. Print job statuses should be treated as short-
   * lived. Retrieve the status immediately, and frequently after calling
   * {@link #print(PrintJob)}. For example, you might initially retrieve the status every
   * 5 seconds, backing off to every 60 seconds. If the print job is removed, assume the
   * final status is the last retrieved.
   *
   * @param printJobId a {@link String}, the print job ID.
   *
   * @return an {@link Integer}, the print job state or {@code null} if the print job does
   * not exist.
   *
   * @see PrintJobsContract#STATE_IN_QUEUE
   * @see PrintJobsContract#STATE_PRINTING
   * @see PrintJobsContract#STATE_DONE
   * @see PrintJobsContract#STATE_ERROR
   */
  @Nullable
  public Integer getState(@NonNull String printJobId) {
    Bundle extras = new Bundle();
    extras.setClassLoader(getClass().getClassLoader());
    extras.putString(EXTRA_PRINTJOB_ID, printJobId);

    Bundle result = call(METHOD_GET_STATE, extras);
    return (result != null) ? result.getInt(RESULT_GET_STATE) : null;
  }

  /**
   * Get all of the print job IDs with the given state. This is a hidden API. It is
   * subject to change without warning. Use at your own risk.
   *
   * There is very little use of this outside of test / example code. In most cases
   * callers will print, capture the return print job ID, and use
   * {@link #getState(String)} to get the state for that ID.
   *
   * @y.exclude
   */
  public List<String> getPrintJobIds(int state) {
    Bundle extras = new Bundle();
    extras.setClassLoader(getClass().getClassLoader());
    extras.putInt(EXTRA_PRINTJOB_STATE, state);

    Bundle result = call(METHOD_GET_PRINTJOB_IDS, extras);
    return (result != null) ? result.getStringArrayList(RESULT_GET_PRINTJOB_IDS) : null;
  }
}
