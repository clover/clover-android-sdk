/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */

package com.clover.sdk.v3.payments;

@SuppressWarnings(value="unused")
@Deprecated
public class ReceiptInfo {

  private java.util.Map<java.lang.String,java.lang.String> selectedReceiptOptions = null;
  private java.lang.String receiptDeliveryType = null;
  private java.lang.String receiptDeliveryStatus = null;


  /**
   * Set the field value
   */
  public void setSelectedReceiptOptions(java.util.Map<java.lang.String,java.lang.String> selectedReceiptOptions) {
    this.selectedReceiptOptions = selectedReceiptOptions;
  }

  /**
   * Get the field value
   */
  public java.util.Map<java.lang.String,java.lang.String> getSelectedReceiptOptions() {
    return this.selectedReceiptOptions;
  }
  /**
   * Set the field value
   */
  public void setReceiptDeliveryType(java.lang.String receiptDeliveryType) {
    this.receiptDeliveryType = receiptDeliveryType;
  }

  /**
   * Get the field value
   */
  public java.lang.String getReceiptDeliveryType() {
    return this.receiptDeliveryType;
  }
  /**
   * Set the field value
   */
  public void setReceiptDeliveryStatus(java.lang.String receiptDeliveryStatus) {
    this.receiptDeliveryStatus = receiptDeliveryStatus;
  }

  /**
   * Get the field value
   */
  public java.lang.String getReceiptDeliveryStatus() {
    return this.receiptDeliveryStatus;
  }
}
