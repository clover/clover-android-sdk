package com.clover.sdk.v1.printer

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PrinterTest {
  @Test
  fun testEquals() {
    val p1 = Printer.Builder()
      .uuid("QWERTYUIOPASD")
      .name("TSP100")
      .mac("USB:")
      .type(Type("TSP100"))
      .ip("1.2.3.4")
      .category(Category.RECEIPT)
      .build()

    val p2 = Printer.Builder()
      .uuid("QWERTYUIOPASD")
      .name("TSP100")
      .mac("USB:")
      .type(Type("TSP100"))
      .ip("1.2.3.4")
      .category(Category.RECEIPT)
      .build()

    Assert.assertEquals(p1, p2)
  }

  @Test
  fun testEqualsNull() {
    val p1 = Printer.Builder()
      .uuid("QWERTYUIOPASD")
      .name("TSP100")
      .mac("USB:")
      .type(Type("TSP100"))
      .ip("1.2.3.4")
      .category(Category.RECEIPT)
      .build()

    Assert.assertNotEquals(p1, null)
  }

  @Test
  fun testNotEquals() {
    val p1 = Printer.Builder()
      .uuid("QWERTYUIOPASD")
      .name("TSP100")
      .mac("USB:")
      .type(Type("TSP100"))
      .ip("1.2.3.4")
      .category(Category.RECEIPT)
      .build()

    Assert.assertNotEquals(p1, Printer.Builder().printer(p1).uuid("XXXXXXXXXXXX").build())
    Assert.assertNotEquals(p1, Printer.Builder().printer(p1).name("XXXXXXXXXXXX").build())
    Assert.assertNotEquals(p1, Printer.Builder().printer(p1).mac("XXXXXXXXXXXX").build())
    Assert.assertNotEquals(p1, Printer.Builder().printer(p1).type(Type("XXXXXXXXXXXX")).build())
    Assert.assertNotEquals(p1, Printer.Builder().printer(p1).ip("XXXXXXXXXXXX").build())
    Assert.assertNotEquals(p1, Printer.Builder().printer(p1).category(Category.ORDER).build())
  }

}