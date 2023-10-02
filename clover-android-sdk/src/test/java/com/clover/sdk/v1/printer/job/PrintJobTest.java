package com.clover.sdk.v1.printer.job;

import com.clover.sdk.v1.Intents;
import com.clover.sdk.v1.printer.Printer;
import com.clover.sdk.v1.printer.PrinterIntent;
import com.clover.sdk.v3.order.Order;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class PrintJobTest {

  private static final String SERVICE_HOST = "com.clover.engine";

  private PrintJob pj;

  @Before
  public void setup() {
    pj = new StaticOrderPrintJob.Builder()
        .itemIds(new ArrayList<>())
        .markPrinted(true)
        .order(new Order())
        .build();
  }

  @Test
  public void testPrint() {
    ArgumentCaptor<Intent> captor = ArgumentCaptor.forClass(Intent.class);
    Context context = mock(Context.class);
    Account account = mock(android.accounts.Account.class);
    Printer printer = mock(Printer.class);

    pj.print(context, account, printer);

    verify(context).startService(captor.capture());

    assertThat(captor.getValue().getAction(), Matchers.is(PrinterIntent.ACTION_PRINT_SERVICE));
    assertThat(captor.getValue().getBooleanExtra(Intents.EXTRA_SKIP_REPRINT_ROLE_CHECK, false), Matchers.is(false));
    assertThat(captor.getValue().getPackage(), Matchers.is(SERVICE_HOST));
    assertThat(captor.getValue().getParcelableExtra(Intents.EXTRA_ACCOUNT), Matchers.is(account));
    assertThat(captor.getValue().getParcelableExtra(PrinterIntent.EXTRA_PRINTER), Matchers.is(printer));
  }
}
