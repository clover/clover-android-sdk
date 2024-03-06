package com.clover.android.sdk.examples;

import com.clover.sdk.v3.inventory.OrderFee;
import com.clover.sdk.v3.order.LineItem;
import com.clover.sdk.v3.order.Order;
import com.clover.sdk.v3.order.OrderCalc;
import com.clover.sdk.v3.payments.Payment;
import com.clover.sdk.v3.payments.Refund;

import java.util.ArrayList;
import java.util.List;

public final class OrderUtils {

  private OrderUtils() {
  }

  public static boolean isFullyPaid(Order order) {
    return order.isNotNullLineItems()
           && order.getLineItems().size() > 0
           && (order.isNotEmptyPayments() || order.isNotEmptyCredits())
           && getAmountLeftToPay(order) <= 0;
  }

  public static long getAmountLeftToPay(Order order) {
    long paymentTotal = 0;
    if (order.isNotNullPayments()) {
      for (Payment p : order.getPayments()) {
        paymentTotal += p.getAmount();
      }
    }
    return new OrderCalc(order).getTotal(order.getLineItems()) - paymentTotal + amountRefundedWithoutTip(order);
  }

  public static long amountRefundedWithoutTip(Order order) {
    long amountRefundedWithoutTip = 0L;
    if (order.isNotNullRefunds()) {
      for (Refund r : order.getRefunds()) {
        if (r.getAmount() != null) {
          amountRefundedWithoutTip += r.getAmount();

          for (Payment p : order.getPayments()) {
            if (p.getId().equals(r.getPayment().getId())) {
              amountRefundedWithoutTip -= calcTipAmount(p);
            }
          }
        }
      }
    }
    return amountRefundedWithoutTip;
  }

  public static long calcTipAmount(Payment p) {
    if (p.isNotNullTipAmount()) {
      return p.getTipAmount();
    } else {
      return 0;
    }
  }

  /**
   * This method filters out service charge line items and returns the list of regular line items
   * associated with the given order
   *
   * @param order: order instance whose regular line items are to be obtained
   * @return list of regular line items without service charge line items
   */
  public static List<LineItem> getLineItemsWithoutServiceCharges(Order order) {
    return getLineItemsWithoutServiceCharges(order.getLineItems());
  }

  /**
   * This method filters out service charge line items and returns the list of regular line items
   * from a given list of lineitems
   *
   * @param lineItems: list of line items from which the service charge line items should be filtered out
   * @return list of regular line items without service charge line items
   */
  public static List<LineItem> getLineItemsWithoutServiceCharges(List<LineItem> lineItems) {
    List<LineItem> lineItemsWithoutServiceCharges = null;
    if (lineItems != null) {
      for (LineItem lineItem : lineItems) {
        if (!lineItem.getIsOrderFee()) {
          lineItemsWithoutServiceCharges.add(lineItem);
        }
      }
    }
    return lineItemsWithoutServiceCharges;
  }

  /**
   * Returns the list of service charge(s) which are applied to current order
   */
  public static List<LineItem> getServiceChargesFromOrder(Order order) {
    List<LineItem> serviceCharges = new ArrayList<LineItem>();
    if (order.isNotNullLineItems()) {
      for (LineItem lineItem : order.getLineItems()) {
        if (lineItem.isNotNullIsOrderFee() && lineItem.getIsOrderFee()) {
          serviceCharges.add(
             lineItem
          );
        }
      }
    }
    return serviceCharges;
  }

}
