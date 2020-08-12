package com.clover.android.sdk.examples;

import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.Intents;
import com.clover.sdk.v1.ServiceException;
import com.clover.sdk.v1.merchant.Merchant;
import com.clover.sdk.v3.order.LineItem;
import com.clover.sdk.v3.order.Order;
import com.clover.sdk.v3.order.OrderConnector;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * This test activity demonstrates use of the {@link com.clover.sdk.v1.Intents#ACTION_CLOVER_PAY}
 * intent for making a sale or a refund.
 */
public class SaleRefundTestActivity extends AppCompatActivity {

  private static final String TAG = SaleRefundTestActivity.class.getSimpleName();

  private static final int REQ_CODE_CLOVER_PAY = 101;

  private static final long AMOUNT = 10099;

  private TextView tvResult;
  private TextView tvResultTitle;
  private TextView tvAmount;

  private final Executor bgExecutor = Executors.newSingleThreadExecutor();

  private Merchant merchant;
  private CurrencyUtils currencyUtils;
  private OrderConnector orderConnector;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Context ctx = getApplicationContext();

    setContentView(R.layout.activity_salerefund_test);
    tvResult = findViewById(R.id.text_result);
    tvResultTitle = findViewById(R.id.text_result_title);
    tvAmount = findViewById(R.id.text_amount);

    bgExecutor.execute(() -> {
      try {
        merchant = Utils.fetchMerchantBlocking(ctx);
      } catch (IOException e) {
        e.printStackTrace();
        SaleRefundTestActivity.this.finish();
        return;
      }

      runOnUiThread(() -> {
        currencyUtils = new CurrencyUtils(ctx, merchant);
        orderConnector = new OrderConnector(ctx, CloverAccount.getAccount(ctx), null);

        tvAmount.setText(currencyUtils.longToAmountString(AMOUNT));

        findViewById(R.id.btn_sale).setOnClickListener((view) -> {
          bgExecutor.execute(() -> startPayment(AMOUNT));
        });

        findViewById(R.id.btn_refund).setOnClickListener((view) -> {
          bgExecutor.execute(() -> startPayment(-AMOUNT));
        });
      });
    });
  }

  private void startPayment(long amount) {
    try {
      boolean taxable = true;
      LineItem customLineItem = new LineItem();
      customLineItem.setName("Manual Transaction");
      customLineItem.setPrice(amount);

      Order order = new Order().setManualTransaction(true);
      order = orderConnector.createOrder(order);
      customLineItem = orderConnector.addCustomLineItem(order.getId(), customLineItem, taxable);
      order = orderConnector.getOrder(order.getId());

      startPayment(order);
    } catch (RemoteException | ClientException | ServiceException | BindingException e) {
      Log.w(TAG, e);
    }
  }

  private void startPayment(Order o) {
    Intent intent = new Intent(Intents.ACTION_CLOVER_PAY);
    intent.putExtra(Intents.EXTRA_ORDER_ID, o.getId());
    if (o.getTotal() < 0) {
      intent.putExtra(Intents.EXTRA_TRANSACTION_TYPE, Intents.TRANSACTION_TYPE_CREDIT);
    }
    startActivityForResult(intent, REQ_CODE_CLOVER_PAY);
  }

  private Order getOrder(String orderId) {
    try {
      return orderConnector.getOrder(orderId);
    } catch (RemoteException | ClientException | ServiceException | BindingException e) {
      Log.w(TAG, e);
    }
    return null;
  }

  private void deleteOrder(String orderId) {
    try {
      orderConnector.deleteOrder(orderId);
    } catch (RemoteException | ClientException | ServiceException | BindingException e) {
      Log.w(TAG, e);
    }
  }

  private void setResultTextSafe(String result) {
    if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
      setResultText(result);
    } else {
      runOnUiThread(() -> setResultText(result));
    }
  }

  private void setResultText(String result) {
    if (TextUtils.isEmpty(result)) {
      tvResultTitle.setText("");
    } else {
      tvResultTitle.setText("Result");
    }
    tvResult.setText(result);
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    setResultTextSafe("");

    if (requestCode != REQ_CODE_CLOVER_PAY) {
      return;
    }

    bgExecutor.execute(() -> {
      String orderId = data == null ? null : data.getStringExtra(Intents.EXTRA_ORDER_ID);
      if (TextUtils.isEmpty(orderId)) {
        setResultTextSafe(String.format("Pay failed, missing order id\n\n%s", Utils.intentToString(data)));
        return;
      }

      Order order = getOrder(orderId);
      if (order == null) {
        setResultTextSafe(String.format("Pay failed, order not found\n\n%s", Utils.intentToString(data)));
        return;
      }

      if (resultCode != RESULT_OK) {
        deleteOrder(orderId);
        setResultTextSafe(String.format("Pay failed\n\n%s", order));
        return;
      }

      if (OrderUtils.isFullyPaid(order)) {
        setResultTextSafe(String.format("Complete payment\n\n%s", order));
      } else {
        setResultTextSafe(String.format("Partial payment\n\n%s", order));
      }
    });
  }

  @Override
  protected void onDestroy() {
    if (orderConnector != null) {
      orderConnector.disconnect();
      orderConnector = null;
    }

    super.onDestroy();
  }
}
