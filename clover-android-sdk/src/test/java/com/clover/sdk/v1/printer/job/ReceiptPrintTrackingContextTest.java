package com.clover.sdk.v1.printer.job;

import android.os.Parcel;

import com.clover.sdk.v3.payments.ReceiptTrackingDetails;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
public class ReceiptPrintTrackingContextTest {

  @Test
  public void paymentContext_survivesPrintJobParcelRoundTrip() {
    // Given
    ReceiptTrackingDetails receiptDetails = new ReceiptTrackingDetails()
        .setStampTaxAmount(200L)
        .setCustomerName("山田 太郎")
        .setProviso("お食事代として");
    ReceiptPrintTrackingContext context = new ReceiptPrintTrackingContext.Builder()
        .scope(ReceiptTrackingScope.PAYMENT)
        .orderId("order-1")
        .paymentId("payment-1")
        .receiptDetails(receiptDetails)
        .build();
    StaticPaymentPrintJob original = new StaticPaymentPrintJob.Builder()
        .paymentId("payment-1")
        .receiptPrintTrackingContext(context)
        .build();
    Parcel parcel = Parcel.obtain();

    // When
    original.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);
    StaticPaymentPrintJob restored = StaticPaymentPrintJob.CREATOR.createFromParcel(parcel);

    // Then
    assertEquals(ReceiptTrackingScope.PAYMENT, restored.receiptPrintTrackingContext.getScope());
    assertEquals("order-1", restored.receiptPrintTrackingContext.getOrderId());
    assertEquals("payment-1", restored.receiptPrintTrackingContext.getPaymentId());
    assertEquals(Long.valueOf(200L),
        restored.receiptPrintTrackingContext.getReceiptDetails().getStampTaxAmount());
    assertEquals("山田 太郎",
        restored.receiptPrintTrackingContext.getReceiptDetails().getCustomerName());
    assertEquals("お食事代として",
        restored.receiptPrintTrackingContext.getReceiptDetails().getProviso());
    parcel.recycle();
  }

  @Test
  public void orderContext_allowsNullableReceiptDetails() {
    // Given
    ReceiptPrintTrackingContext context = new ReceiptPrintTrackingContext.Builder()
        .scope(ReceiptTrackingScope.ORDER)
        .orderId("order-1")
        .build();

    // When
    StaticReceiptPrintJob printJob = new StaticReceiptPrintJob.Builder()
        .receiptPrintTrackingContext(context)
        .build();

    // Then
    assertEquals(ReceiptTrackingScope.ORDER, printJob.receiptPrintTrackingContext.getScope());
    assertEquals("order-1", printJob.receiptPrintTrackingContext.getOrderId());
    assertNull(printJob.receiptPrintTrackingContext.getPaymentId());
    assertNull(printJob.receiptPrintTrackingContext.getReceiptDetails());
  }

  @Test(expected = IllegalStateException.class)
  public void context_requiresOrderId() {
    // Given
    ReceiptPrintTrackingContext.Builder builder = new ReceiptPrintTrackingContext.Builder()
        .scope(ReceiptTrackingScope.ORDER);

    // When
    builder.build();

    // Then
    // IllegalStateException is thrown.
  }

  @Test(expected = IllegalStateException.class)
  public void paymentContext_requiresPaymentId() {
    // Given
    ReceiptPrintTrackingContext.Builder builder = new ReceiptPrintTrackingContext.Builder()
        .scope(ReceiptTrackingScope.PAYMENT)
        .orderId("order-1");

    // When
    builder.build();

    // Then
    // IllegalStateException is thrown.
  }

  @Test(expected = IllegalStateException.class)
  public void orderContext_rejectsPaymentId() {
    // Given
    ReceiptPrintTrackingContext.Builder builder = new ReceiptPrintTrackingContext.Builder()
        .scope(ReceiptTrackingScope.ORDER)
        .orderId("order-1")
        .paymentId("payment-1");

    // When
    builder.build();

    // Then
    // IllegalStateException is thrown.
  }
}
