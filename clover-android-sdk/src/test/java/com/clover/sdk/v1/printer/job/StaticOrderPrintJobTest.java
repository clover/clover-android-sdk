package com.clover.sdk.v1.printer.job;

import com.clover.sdk.v1.printer.Category;
import com.clover.sdk.v3.order.LineItem;
import com.clover.sdk.v3.order.Order;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertTrue;


@RunWith(RobolectricTestRunner.class)
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
  public void assertProperties_banner() {
    assertNull(printJob.banner);
    printJob = new StaticOrderPrintJob.Builder().banner("testLabel").build();
    assertEquals("testLabel", printJob.banner);
  }

  @Test
  public void assertProperties_voidedLineItems_defaultIsNull() {
    assertNull(printJob.voidedLineItems);
  }

  @Test
  public void assertProperties_voidedLineItems_setViaBuilder() {
    ArrayList<LineItem> voidedLineItems = new ArrayList<>();
    LineItem voidedLineItem = new LineItem();
    voidedLineItem.setId("voided-line-item-id");
    voidedLineItems.add(voidedLineItem);

    printJob = new StaticOrderPrintJob.Builder().voidedLineItems(voidedLineItems).build();

    assertNotNull(printJob.voidedLineItems);
    assertEquals(1, printJob.voidedLineItems.size());
    assertEquals("voided-line-item-id", printJob.voidedLineItems.get(0).getId());
  }

  @Test
  public void constructorTest_deprecated() {
    ArrayList<String> list = new ArrayList<>();
    list.add("item1");
    list.add("item2");
    list.add("item4");

    printJob = new StaticOrderPrintJob(new Order(), itemIds, true, 0, true, null);
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

  @Test
  public void builderMethodChaining_returnsCorrectType() {
    // Test that chaining methods from parent classes returns StaticOrderPrintJob.Builder
    // This verifies the fix for covariant return types in the builder hierarchy
    StaticOrderPrintJob.Builder builder = new StaticOrderPrintJob.Builder();

    // All these methods should return StaticOrderPrintJob.Builder for chaining
    StaticOrderPrintJob printJob = builder
        .order(new Order())
        .reason("Test reason")
        .flag(PrintJob.FLAG_REPRINT)
        .includePrintGroups(true)
        .itemIds(itemIds)
        .reprintAllowed(true)
        .markPrinted(true)
        .banner("Test Banner")
        .build();

    assertNotNull(printJob);
    assertTrue(printJob.reprintAllowed);
    assertTrue(printJob.markPrinted);
    assertEquals("Test Banner", printJob.banner);
    assertEquals(itemIds, printJob.itemIds);
    assertEquals(printJob.flags & PrintJob.FLAG_REPRINT, PrintJob.FLAG_REPRINT);
    assertEquals(printJob.flags & PrintJob.FLAG_USE_PRINT_GROUP, PrintJob.FLAG_USE_PRINT_GROUP);
  }
}
