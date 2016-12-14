/**
 * Copyright (C) 2016 Clover Network, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clover.android.sdk.examples;

import android.accounts.Account;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ServiceException;
import com.clover.sdk.v3.order.Order;
import com.clover.sdk.v3.order.OrderCalc;
import com.clover.sdk.v3.order.OrderConnector;
import com.clover.sdk.v3.order.OrderContract;
import com.clover.sdk.v3.payments.Payment;
import com.clover.sdk.v3.payments.Refund;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class OrderPaidActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {
  private static final String TAG = OrderPaidActivity.class.getSimpleName();

  private static final int ORDERS_LOADER_ID = 13;

  private class OrdersAdapter extends BaseAdapter {
    private final List<Order> orders;

    private OrdersAdapter(List<Order> orders) {
      this.orders = orders;
    }

    @Override
    public int getCount() {
      return orders.size();
    }

    @Override
    public Object getItem(int position) {
      return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      View view = convertView;
      if (view == null) {
        view = LayoutInflater.from(OrderPaidActivity.this).inflate(R.layout.item_order_paid, null);
      }

      Order order = (Order) getItem(position);

      TextView orderIdText = (TextView) view.findViewById(R.id.text_order_id);
      orderIdText.setText(order.getId());

      TextView orderTotalText = (TextView) view.findViewById(R.id.text_order_total);
      orderTotalText.setText(longToAmountString(Currency.getInstance("USD"), new OrderCalc(order).getTotal(order.getLineItems())));

      TextView orderDateText = (TextView) view.findViewById(R.id.text_order_date);
      orderDateText.setText(SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT).format(order.getClientCreatedTime()));

      TextView orderPaidText = (TextView) view.findViewById(R.id.text_order_paid);
      orderPaidText.setText("Paid? " + isPaid(order));

      return view;
    }
  }

  private OrderConnector orderConnector;
  private ListView ordersList;

  private Account account;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_order_paid);

    account = CloverAccount.getAccount(this);
    ordersList = (ListView) findViewById(R.id.list_orders);
  }

  @Override
  protected void onResume() {
    super.onResume();

    orderConnector = new OrderConnector(this, account, null);
    getLoaderManager().restartLoader(ORDERS_LOADER_ID, null, this);
  }

  @Override
  protected void onPause() {
    if (orderConnector != null) {
      orderConnector.disconnect();
      orderConnector = null;
    }
    super.onPause();
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new CursorLoader(this, OrderContract.Summaries.contentUriWithAccount(account), null, null, null, OrderContract.Summaries.CREATED + " DESC LIMIT 100");
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {
    final List<String> orderIds = new ArrayList<String>();
    if (data != null && data.moveToFirst()) {
      while (!data.isAfterLast()) {
        orderIds.add(data.getString(data.getColumnIndex(OrderContract.Summaries.ID)));
        data.moveToNext();
      }
    }

    new AsyncTask<Void,Void,List<Order>>() {
      @Override
      protected List<Order> doInBackground(Void... params) {

        List<Order> orders = new ArrayList<Order>();
        for (String orderId: orderIds) {
          try {
            Order o = orderConnector.getOrder(orderId);
            if (o != null) {
              orders.add(o);
              Log.i(TAG, "Loaded order ID: " + orderId);
            }
          } catch (RemoteException | ClientException | BindingException | ServiceException e) {
            e.printStackTrace();
          }
        }

        Collections.sort(orders, new Comparator<Order>() {
          @Override
          public int compare(Order lhs, Order rhs) {
            if (lhs.getClientCreatedTime() < rhs.getClientCreatedTime()) {
              return 1;
            }
            if (lhs.getClientCreatedTime() > rhs.getClientCreatedTime()) {
              return -1;
            }
            return 0;
          }
        });

        return orders;
      }

      @Override
      protected void onPostExecute(List<Order> orders) {
        ordersList.setAdapter(new OrdersAdapter(orders));
      }
    }.execute();
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
  }

  private static boolean isPaid(Order order) {
    return (order.isNotNullLineItems() && order.getLineItems().size() > 0 && (order.isNotEmptyPayments() || order.isNotEmptyCredits()) && getAmountLeftToPay(order) <= 0);
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
    long amountRefundedWithoutTip = 0l;
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

  public static double longToDecimal(double num, Currency currency) {
    return num / Math.pow(10, currency.getDefaultFractionDigits());
  }


  // Take a long and convert it to an amount string (i.e 150 -> $1.50)
  public static String longToAmountString(Currency currency, long amt) {
    DecimalFormat decimalFormat = getCurrencyFormatInstance(currency);
    return longToAmountString(currency, amt, decimalFormat);
  }

  // Take a long and convert it to an amount string (i.e 150 -> $1.50)
  public static String longToAmountString(Currency currency, long amt, DecimalFormat decimalFormat) {
    return decimalFormat.format(longToDecimal(amt, currency));
  }

  private static ThreadLocal<DecimalFormat> DECIMAL_FORMAT = new ThreadLocal<DecimalFormat>() {
    @Override
    protected DecimalFormat initialValue() {
      return (DecimalFormat) DecimalFormat.getCurrencyInstance(Locale.getDefault());
    }
  };

  private static DecimalFormat getCurrencyFormatInstance(Currency currency) {
    DecimalFormat format = DECIMAL_FORMAT.get();
    if (format.getCurrency() != currency) {
      format.setCurrency(currency);
    }
    return format;
  }

}
