package com.clover.android.sdk.examples;

import com.clover.sdk.v3.order.Order;
import com.clover.sdk.v3.order.OrderCalc;
import com.clover.sdk.v3.payments.Payment;
import com.clover.sdk.v3.payments.Refund;

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

}
