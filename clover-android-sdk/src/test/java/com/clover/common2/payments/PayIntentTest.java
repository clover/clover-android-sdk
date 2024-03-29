package com.clover.common2.payments;

import com.clover.sdk.v1.Intents;
import com.clover.sdk.v3.base.Tender;
import com.clover.sdk.v3.payments.Refund;

import android.content.Intent;
import android.os.Parcel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 * Author: leonid
 * Date: 10/3/17.
 */
@RunWith(RobolectricTestRunner.class)
public class PayIntentTest {

  @Test
  public void testUseLastSwipe() {
    PayIntent payIntent = new PayIntent.Builder().build();
    assertFalse(payIntent.useLastSwipe);

    payIntent = new PayIntent.Builder().useLastSwipe(false).build();
    assertFalse(payIntent.useLastSwipe);

    payIntent = new PayIntent.Builder().useLastSwipe(true).build();
    assertTrue(payIntent.useLastSwipe);
  }

  @Test
  public void testUseLastSwipe_fromIntent() {
    Intent sourceIntent = new Intent();
    PayIntent payIntent = new PayIntent.Builder().intent(sourceIntent).build();
    assertFalse(payIntent.useLastSwipe);

    sourceIntent = new Intent();
    sourceIntent.putExtra(Intents.EXTRA_USE_LAST_SWIPE, false);
    payIntent = new PayIntent.Builder().intent(sourceIntent).build();
    assertFalse(payIntent.useLastSwipe);

    sourceIntent = new Intent();
    sourceIntent.putExtra(Intents.EXTRA_USE_LAST_SWIPE, true);
    payIntent = new PayIntent.Builder().intent(sourceIntent).build();
    assertTrue(payIntent.useLastSwipe);
  }

  @Test
  public void testUseLastSwipe_fromPayIntent() {
    PayIntent sourcePayIntent = new PayIntent.Builder().build();
    PayIntent payIntent = new PayIntent.Builder().payIntent(sourcePayIntent).build();
    assertFalse(payIntent.useLastSwipe);

    sourcePayIntent = new PayIntent.Builder().useLastSwipe(false).build();
    payIntent = new PayIntent.Builder().payIntent(sourcePayIntent).build();
    assertFalse(payIntent.useLastSwipe);

    sourcePayIntent = new PayIntent.Builder().useLastSwipe(true).build();
    payIntent = new PayIntent.Builder().payIntent(sourcePayIntent).build();
    assertTrue(payIntent.useLastSwipe);
  }

  @Test
  public void testUseLastSwipe_serialization() {
    PayIntent payIntent = new PayIntent.Builder().useLastSwipe(true).build();

    Parcel p = Parcel.obtain();
    payIntent.writeToParcel(p, 0);

    p.setDataPosition(0);
    PayIntent fromParcel = PayIntent.CREATOR.createFromParcel(p);
    assertTrue(fromParcel.useLastSwipe);
  }

  @Test
  public void testRefund() {
    PayIntent payIntent = new PayIntent.Builder().build();
    assertNull(payIntent.refund);

    Refund refund = new Refund();

    payIntent = new PayIntent.Builder().refund(refund).build();
    assertNotNull(payIntent.refund);

    refund.setAmount(1000L);
    refund.setId("refund1");

    payIntent = new PayIntent.Builder().refund(refund).build();
    assertNotNull(payIntent.refund);
    assertEquals(1000L, (long) payIntent.refund.getAmount());
    assertEquals("refund1", payIntent.refund.getId());
  }

  @Test
  public void testRefund_fromIntent() {
    Intent sourceIntent = new Intent();
    PayIntent payIntent = new PayIntent.Builder().intent(sourceIntent).build();
    assertNull(payIntent.refund);

    Refund refund = new Refund();
    refund.setAmount(2000L);
    refund.setId("refund2");

    sourceIntent = new Intent();
    sourceIntent.putExtra(Intents.EXTRA_REFUND, refund);
    payIntent = new PayIntent.Builder().intent(sourceIntent).build();
    assertEquals(2000L, (long) payIntent.refund.getAmount());
    assertEquals("refund2", payIntent.refund.getId());

  }

  @Test
  public void testRefund_fromPayIntent() {
    PayIntent sourcePayIntent = new PayIntent.Builder().build();
    PayIntent payIntent = new PayIntent.Builder().payIntent(sourcePayIntent).build();
    assertNull(payIntent.refund);

    Refund refund = new Refund();
    refund.setAmount(3000L);
    refund.setId("refund3");

    sourcePayIntent = new PayIntent.Builder().refund(refund).build();
    payIntent = new PayIntent.Builder().payIntent(sourcePayIntent).build();
    assertEquals(3000L, (long) payIntent.refund.getAmount());
    assertEquals("refund3", payIntent.refund.getId());
  }

  @Test
  public void testRefund_serialization() {
    Refund refund = new Refund();
    refund.setAmount(4000L);
    refund.setId("refund4");
    PayIntent payIntent = new PayIntent.Builder().refund(refund).build();

    Parcel p = Parcel.obtain();
    payIntent.writeToParcel(p, 0);

    p.setDataPosition(0);
    PayIntent fromParcel = PayIntent.CREATOR.createFromParcel(p);
    assertNotNull(fromParcel.refund);
    assertEquals(4000L, (long) fromParcel.refund.getAmount());
    assertEquals("refund4", fromParcel.refund.getId());
  }

  @Test
  public void testTender() {
    PayIntent payIntent = new PayIntent.Builder().build();
    assertNull(payIntent.customerTender);

    Tender tender = new Tender();

    payIntent = new PayIntent.Builder().customerTender(tender).build();
    assertNotNull(payIntent.customerTender);

    tender.setLabel("Alipay");
    tender.setId("tender1");

    payIntent = new PayIntent.Builder().customerTender(tender).build();
    assertNotNull(payIntent.customerTender);
    assertEquals("Alipay", payIntent.customerTender.getLabel());
    assertEquals("tender1", payIntent.customerTender.getId());
  }

  @Test
  public void testTender_fromIntent() {
    Intent sourceIntent = new Intent();
    PayIntent payIntent = new PayIntent.Builder().intent(sourceIntent).build();
    assertNull(payIntent.customerTender);

    Tender tender = new Tender();
    tender.setLabel("Alipay");
    tender.setId("tender2");

    sourceIntent = new Intent();
    sourceIntent.putExtra(Intents.EXTRA_CUSTOMER_TENDER, tender);
    payIntent = new PayIntent.Builder().intent(sourceIntent).build();
    assertEquals("Alipay", payIntent.customerTender.getLabel());
    assertEquals("tender2", payIntent.customerTender.getId());

  }

  @Test
  public void testTender_fromPayIntent() {
    PayIntent sourcePayIntent = new PayIntent.Builder().build();
    PayIntent payIntent = new PayIntent.Builder().payIntent(sourcePayIntent).build();
    assertNull(payIntent.customerTender);

    Tender tender = new Tender();
    tender.setLabel("Alipay");
    tender.setId("tender3");

    sourcePayIntent = new PayIntent.Builder().customerTender(tender).build();
    payIntent = new PayIntent.Builder().payIntent(sourcePayIntent).build();
    assertEquals("Alipay", payIntent.customerTender.getLabel());
    assertEquals("tender3", payIntent.customerTender.getId());
  }

  @Test
  public void testTender_serialization() {
    Tender tender = new Tender();
    tender.setLabel("Alipay");
    tender.setId("tender4");
    PayIntent payIntent = new PayIntent.Builder().customerTender(tender).build();

    Parcel p = Parcel.obtain();
    payIntent.writeToParcel(p, 0);

    p.setDataPosition(0);
    PayIntent fromParcel = PayIntent.CREATOR.createFromParcel(p);
    assertNotNull(fromParcel.customerTender);
    assertEquals("Alipay", fromParcel.customerTender.getLabel());
    assertEquals("tender4", fromParcel.customerTender.getId());
  }

  @Test
  public void testSurcharge_fromIntent() {
    Intent sourceIntent = new Intent();
    PayIntent payIntent = new PayIntent.Builder().intent(sourceIntent).build();
    assertTrue(!payIntent.isDisableCreditSurcharge);


    sourceIntent = new Intent();
    sourceIntent.putExtra(Intents.EXTRA_DISABLE_CREDIT_SURCHARGE, true);
    payIntent = new PayIntent.Builder().intent(sourceIntent).build();
    assertTrue(payIntent.isDisableCreditSurcharge);
  }

  @Test
  public void testRefundReason() {
    PayIntent payIntent = new PayIntent.Builder().build();
    assertNull(payIntent.refundReason);

    String refundReason = "Unhappy customer";

    payIntent = new PayIntent.Builder().refundReason(refundReason).build();
    assertNotNull(payIntent.refundReason);
    assertEquals("Unhappy customer", payIntent.refundReason);
  }

  @Test
  public void testRefundReason_fromIntent() {
    Intent sourceIntent = new Intent();
    PayIntent payIntent = new PayIntent.Builder().intent(sourceIntent).build();
    assertNull(payIntent.refundReason);

    String refundReason = "Angry customer";

    sourceIntent = new Intent();
    sourceIntent.putExtra(Intents.EXTRA_REFUND_REASON, refundReason);
    payIntent = new PayIntent.Builder().intent(sourceIntent).build();
    assertNotNull(payIntent.refundReason);
    assertEquals("Angry customer", payIntent.refundReason);
  }

  @Test
  public void testRefundReason_fromPayIntent() {
    PayIntent sourcePayIntent = new PayIntent.Builder().build();
    PayIntent payIntent = new PayIntent.Builder().payIntent(sourcePayIntent).build();
    assertNull(payIntent.refundReason);

    String refundReason = "Confused customer";

    sourcePayIntent = new PayIntent.Builder().refundReason(refundReason).build();
    payIntent = new PayIntent.Builder().payIntent(sourcePayIntent).build();
    assertEquals("Confused customer", payIntent.refundReason);
  }

  @Test
  public void testRefundReason_serialization() {
    String refundReason = "Deceased customer";
    PayIntent payIntent = new PayIntent.Builder().refundReason(refundReason).build();

    Parcel p = Parcel.obtain();
    payIntent.writeToParcel(p, 0);

    p.setDataPosition(0);
    PayIntent fromParcel = PayIntent.CREATOR.createFromParcel(p);
    assertNotNull(fromParcel.refundReason);
    assertEquals("Deceased customer", fromParcel.refundReason);
  }

  @Test
  public void testThresholdMangerName() {
    PayIntent payIntent = new PayIntent.Builder().build();
    assertNull(payIntent.thresholdManagerName);

    String thresholdManagerName = "TESTNAMEMANGER";

    payIntent = new PayIntent.Builder().thresholdManagerName(thresholdManagerName).build();
    assertNotNull(payIntent.thresholdManagerName);
    assertEquals("TESTNAMEMANGER", payIntent.thresholdManagerName);
  }

  @Test
  public void testThresholdMangerName_fromIntent() {
    Intent sourceIntent = new Intent();
    PayIntent payIntent = new PayIntent.Builder().intent(sourceIntent).build();
    assertNull(payIntent.thresholdManagerName);

    String thresholdManagerName = "TESTNAMEMANGER";

    sourceIntent = new Intent();
    sourceIntent.putExtra(Intents.EXTRA_THRESHOLD_MANAGER_NAME, thresholdManagerName);
    payIntent = new PayIntent.Builder().intent(sourceIntent).build();
    assertNotNull(payIntent.thresholdManagerName);
    assertEquals("TESTNAMEMANGER", payIntent.thresholdManagerName);
  }

  @Test
  public void testThresholdMangerName_fromPayIntent() {
    PayIntent sourcePayIntent = new PayIntent.Builder().build();
    PayIntent payIntent = new PayIntent.Builder().payIntent(sourcePayIntent).build();
    assertNull(payIntent.thresholdManagerName);

    String thresholdManagerName = "TESTMANAGERNAME";

    sourcePayIntent = new PayIntent.Builder().thresholdManagerName(thresholdManagerName).build();
    payIntent = new PayIntent.Builder().payIntent(sourcePayIntent).build();
    assertEquals("TESTMANAGERNAME", payIntent.thresholdManagerName);
  }

  @Test
  public void testThresholdMangerName_serialization() {
    String thresholdManagerName = "TESTNAMEMANGER";
    PayIntent payIntent = new PayIntent.Builder().thresholdManagerName(thresholdManagerName).build();

    Parcel p = Parcel.obtain();
    payIntent.writeToParcel(p, 0);

    p.setDataPosition(0);
    PayIntent fromParcel = PayIntent.CREATOR.createFromParcel(p);
    assertNotNull(fromParcel.thresholdManagerName);
    assertEquals("TESTNAMEMANGER", fromParcel.thresholdManagerName);
  }

  @Test
  public void testThresholdMangerId() {
    PayIntent payIntent = new PayIntent.Builder().build();
    assertNull(payIntent.thresholdManagerId);

    String thresholdManagerId = "KUYUYUIY658HG";

    payIntent = new PayIntent.Builder().thresholdManagerId(thresholdManagerId).build();
    assertNotNull(payIntent.thresholdManagerId);
    assertEquals("KUYUYUIY658HG", payIntent.thresholdManagerId);
  }

  @Test
  public void testThresholdMangerId_fromIntent() {
    Intent sourceIntent = new Intent();
    PayIntent payIntent = new PayIntent.Builder().intent(sourceIntent).build();
    assertNull(payIntent.thresholdManagerId);

    String thresholdManagerId = "KUYUYUIY658HG";

    sourceIntent = new Intent();
    sourceIntent.putExtra(Intents.EXTRA_THRESHOLD_MANAGER_ID, thresholdManagerId);
    payIntent = new PayIntent.Builder().intent(sourceIntent).build();
    assertNotNull(payIntent.thresholdManagerId);
    assertEquals("KUYUYUIY658HG", payIntent.thresholdManagerId);
  }

  @Test
  public void testThresholdMangerId_fromPayIntent() {
    PayIntent sourcePayIntent = new PayIntent.Builder().build();
    PayIntent payIntent = new PayIntent.Builder().payIntent(sourcePayIntent).build();
    assertNull(payIntent.thresholdManagerId);

    String thresholdManagerId = "KUYUYUIY658HG";

    sourcePayIntent = new PayIntent.Builder().thresholdManagerId(thresholdManagerId).build();
    payIntent = new PayIntent.Builder().payIntent(sourcePayIntent).build();
    assertEquals("KUYUYUIY658HG", payIntent.thresholdManagerId);
  }

  @Test
  public void testThresholdMangerId_serialization() {
    String thresholdManagerId = "KUYUYUIY658HG";
    PayIntent payIntent = new PayIntent.Builder().thresholdManagerId(thresholdManagerId).build();

    Parcel p = Parcel.obtain();
    payIntent.writeToParcel(p, 0);

    p.setDataPosition(0);
    PayIntent fromParcel = PayIntent.CREATOR.createFromParcel(p);
    assertNotNull(fromParcel.thresholdManagerId);
    assertEquals("KUYUYUIY658HG", fromParcel.thresholdManagerId);
  }

  @Test
  public void testEBTManualCardEntryScreenFlow(){
    PayIntent payIntent = new PayIntent.Builder().build();
    assertNull(payIntent.ebtManualCardEntryScreenFlow);

    String ebtManualCardData = "{\"EBTDirectManualEntryPan\":\"5076 8000 0111 1112\",\"EBTDirectManualEntryExpiry\":\"12\\/24\"}";

    payIntent = new PayIntent.Builder().ebtManualCardEntryScreenFlow(ebtManualCardData).build();
    assertNotNull(payIntent.ebtManualCardEntryScreenFlow);
    assertEquals(ebtManualCardData, payIntent.ebtManualCardEntryScreenFlow);
  }

  @Test
  public void testEBTManualCardEntryScreenFlow_fromIntent() {
    Intent sourceIntent = new Intent();
    PayIntent payIntent = new PayIntent.Builder().intent(sourceIntent).build();
    assertNull(payIntent.ebtManualCardEntryScreenFlow);

    String ebtManualCardData = "{\"EBTDirectManualEntryPan\":\"5076 8000 0111 1112\",\"EBTDirectManualEntryExpiry\":\"12\\/24\"}";

    sourceIntent = new Intent();
    sourceIntent.putExtra(Intents.EXTRA_EBT_MANUAL_CARD_ENTRY_SCREEN_FLOW, ebtManualCardData);
    payIntent = new PayIntent.Builder().intent(sourceIntent).build();
    assertNotNull(payIntent.ebtManualCardEntryScreenFlow);
    assertEquals(ebtManualCardData, payIntent.ebtManualCardEntryScreenFlow);
  }

  @Test
  public void testEBTManualCardEntryScreenFlow_fromPayIntent() {
    PayIntent sourcePayIntent = new PayIntent.Builder().build();
    PayIntent payIntent = new PayIntent.Builder().payIntent(sourcePayIntent).build();
    assertNull(payIntent.ebtManualCardEntryScreenFlow);

    String ebtManualCardData = "{\"EBTDirectManualEntryPan\":\"5076 8000 0111 1112\",\"EBTDirectManualEntryExpiry\":\"12\\/24\"}";

    sourcePayIntent = new PayIntent.Builder().ebtManualCardEntryScreenFlow(ebtManualCardData).build();
    payIntent = new PayIntent.Builder().payIntent(sourcePayIntent).build();
    assertEquals(ebtManualCardData, payIntent.ebtManualCardEntryScreenFlow);
  }

  @Test
  public void testEBTManualCardEntryScreenFlow_serialization() {
    String ebtManualCardData = "{\"EBTDirectManualEntryPan\":\"5076 8000 0111 1112\",\"EBTDirectManualEntryExpiry\":\"12\\/24\"}";
    PayIntent payIntent = new PayIntent.Builder().ebtManualCardEntryScreenFlow(ebtManualCardData).build();

    Parcel p = Parcel.obtain();
    payIntent.writeToParcel(p, 0);

    p.setDataPosition(0);
    PayIntent fromParcel = PayIntent.CREATOR.createFromParcel(p);
    assertNotNull(fromParcel.ebtManualCardEntryScreenFlow);
    assertEquals(ebtManualCardData, fromParcel.ebtManualCardEntryScreenFlow);
  }

  @Test
  public void testPaymentType() {
    PayIntent payIntent = new PayIntent.Builder().build();
    assertNull(payIntent.paymentType);

    String paymentType = "TEST_PAYMENT_TYPE";

    payIntent = new PayIntent.Builder().paymentType(paymentType).build();
    assertNotNull(payIntent.paymentType);
    assertEquals("TEST_PAYMENT_TYPE", payIntent.paymentType);
  }

  @Test
  public void testPaymentType_fromIntent() {
    Intent sourceIntent = new Intent();
    PayIntent payIntent = new PayIntent.Builder().intent(sourceIntent).build();
    assertNull(payIntent.paymentType);

    String paymentType = "TEST_PAYMENT_TYPE";

    sourceIntent = new Intent();
    sourceIntent.putExtra(Intents.EXTRA_PAYMENT_TYPE, paymentType);
    payIntent = new PayIntent.Builder().intent(sourceIntent).build();
    assertNotNull(payIntent.paymentType);
    assertEquals("TEST_PAYMENT_TYPE", payIntent.paymentType);
  }

  @Test
  public void testPaymentType_fromPayIntent() {
    PayIntent sourcePayIntent = new PayIntent.Builder().build();
    PayIntent payIntent = new PayIntent.Builder().payIntent(sourcePayIntent).build();
    assertNull(payIntent.paymentType);

    String paymentType = "TEST_PAYMENT_TYPE";

    sourcePayIntent = new PayIntent.Builder().paymentType(paymentType).build();
    payIntent = new PayIntent.Builder().payIntent(sourcePayIntent).build();
    assertEquals("TEST_PAYMENT_TYPE", payIntent.paymentType);
  }

  @Test
  public void testPaymentType_serialization() {
    String paymentType = "TEST_PAYMENT_TYPE";
    PayIntent payIntent = new PayIntent.Builder().paymentType(paymentType).build();

    Parcel p = Parcel.obtain();
    payIntent.writeToParcel(p, 0);

    p.setDataPosition(0);
    PayIntent fromParcel = PayIntent.CREATOR.createFromParcel(p);
    assertNotNull(fromParcel.paymentType);
    assertEquals("TEST_PAYMENT_TYPE", fromParcel.paymentType);
  }

  @Test
  public void testCreateAuth() {
    PayIntent payIntent = new PayIntent.Builder().build();
    assertNull(payIntent.createAuth);

    Boolean createAuth = true;

    payIntent = new PayIntent.Builder().createAuth(createAuth).build();
    assertNotNull(payIntent.createAuth);
    assertTrue(payIntent.createAuth);
  }

  @Test
  public void testCreateAuth_fromIntent() {
    Intent sourceIntent = new Intent();
    PayIntent payIntent = new PayIntent.Builder().intent(sourceIntent).build();
    assertNull(payIntent.createAuth);

    Boolean createAuth = true;

    sourceIntent = new Intent();
    sourceIntent.putExtra(Intents.EXTRA_CREATE_AUTH, createAuth);
    payIntent = new PayIntent.Builder().intent(sourceIntent).build();
    assertNotNull(payIntent.createAuth);
    assertTrue(payIntent.createAuth);
  }

  @Test
  public void testCreateAuth_fromPayIntent() {
    PayIntent sourcePayIntent = new PayIntent.Builder().build();
    PayIntent payIntent = new PayIntent.Builder().payIntent(sourcePayIntent).build();
    assertNull(payIntent.createAuth);

    Boolean createAuth = true;

    sourcePayIntent = new PayIntent.Builder().createAuth(createAuth).build();
    payIntent = new PayIntent.Builder().payIntent(sourcePayIntent).build();
    assertTrue(payIntent.createAuth);
  }

  @Test
  public void testCreateAuth_serialization() {
    Boolean createAuth = true;
    PayIntent payIntent = new PayIntent.Builder().createAuth(createAuth).build();

    Parcel p = Parcel.obtain();
    payIntent.writeToParcel(p, 0);

    p.setDataPosition(0);
    PayIntent fromParcel = PayIntent.CREATOR.createFromParcel(p);
    assertNotNull(fromParcel.createAuth);
    assertTrue(fromParcel.createAuth);
  }
}
