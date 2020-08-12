package com.clover.sdk.v1.printer.job;

import com.clover.sdk.v1.printer.Category;
import com.clover.sdk.v3.order.Order;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertTrue;


public class StaticOrderPrintJobTest {
  StaticOrderPrintJob printJob;
  ArrayList<String> itemIds = new ArrayList<>();

  @Before
  public void setup() {
    itemIds.add("item1");
    itemIds.add("item2");
    itemIds.add("item4");

    printJob = (StaticOrderPrintJob) new StaticOrderPrintJob.Builder()
        .itemIds(itemIds)
        .reprintAllowed(true)
        .markPrinted(true)
        .includePrintGroups(true)
        .build();
  }

  @Test
  public void assertProperties_itemIds() {
    ArrayList<String> list = new ArrayList<>();
    list.add("item1");
    list.add("item2");

    assertNotEquals(printJob.itemIds, list);

    list.add("item4");
    assertThat(printJob.itemIds, is(equalTo(list)));
  }

  @Test
  public void assertProperties_reprintAllowed() {
    assertTrue(printJob.reprintAllowed);

    printJob = new StaticOrderPrintJob.Builder().reprintAllowed(false).build();

    assertFalse(printJob.reprintAllowed);
  }

  @Test
  public void assertProperties_markPrinted() {
    assertTrue(printJob.markPrinted);
    printJob = new StaticOrderPrintJob.Builder().markPrinted(false).build();
    assertFalse(printJob.markPrinted);
  }

  @Test
  public void assertProperties_includePrintGroups() {
    assertEquals(printJob.flags & PrintJob.FLAG_USE_PRINT_GROUP, PrintJob.FLAG_USE_PRINT_GROUP);
    printJob = (StaticOrderPrintJob) new StaticOrderPrintJob.Builder().includePrintGroups(false).build();

    assertNotEquals(printJob.flags & PrintJob.FLAG_USE_PRINT_GROUP, PrintJob.FLAG_USE_PRINT_GROUP);
  }

  @Test
  public void assertProperties_printGroups_defaultBuilder() {
    printJob = new StaticOrderPrintJob.Builder().build();
    assertNotEquals(printJob.flags & PrintJob.FLAG_USE_PRINT_GROUP, PrintJob.FLAG_USE_PRINT_GROUP);
  }

  @Test
  public void constructorTest_deprecated() {
    ArrayList<String> list = new ArrayList<>();
    list.add("item1");
    list.add("item2");
    list.add("item4");

    printJob = new StaticOrderPrintJob(new Order(), itemIds, true, 0, true);
    assertEquals(printJob.itemIds, list);
    assertTrue(printJob.reprintAllowed);
    assertTrue(printJob.markPrinted);
    assertNotEquals(printJob.flags & PrintJob.FLAG_USE_PRINT_GROUP, PrintJob.FLAG_USE_PRINT_GROUP);
  }

  @Test
  public void getPrinterCategory() {
    assertThat(printJob.getPrinterCategory(), is(Category.ORDER));
  }

  @Test(expected = IllegalArgumentException.class)
  public void setBothExpoAndUnlabelledFlags() {
    PrintJob pj = new StaticOrderPrintJob.Builder()
        .flag(PrintJob.FLAG_UNLABELED_ITEMS)
        .flag(PrintJob.FLAG_EXPEDITOR)
        .build();
  }
}
