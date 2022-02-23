/*
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
import com.clover.sdk.v1.merchant.Merchant;
import com.clover.sdk.v3.order.Order;
import com.clover.sdk.v3.order.OrderCalc;
import com.clover.sdk.v3.order.OrderConnector;
import com.clover.sdk.v3.order.OrderContract;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


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
      orderTotalText.setText(currencyUtils.longToAmountString(new OrderCalc(order).getTotal(order.getLineItems())));

      TextView orderDateText = (TextView) view.findViewById(R.id.text_order_date);
      orderDateText.setText(SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT).format(order.getClientCreatedTime()));

      TextView orderPaidText = (TextView) view.findViewById(R.id.text_order_paid);
      orderPaidText.setText("Paid? " + OrderUtils.isFullyPaid(order));

      return view;
    }
  }

  private OrderConnector orderConnector;
  private ListView ordersList;
  private CurrencyUtils currencyUtils;

  private Account account;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_order_paid);

    account = CloverAccount.getAccount(this);
    orderConnector = new OrderConnector(this, account, null);

    ordersList = (ListView) findViewById(R.id.list_orders);
  }

  @Override
  protected void onResume() {
    super.onResume();

    getLoaderManager().restartLoader(ORDERS_LOADER_ID, null, this);
  }

  @Override
  protected void onDestroy() {
    if (orderConnector != null) {
      orderConnector.disconnect();
      orderConnector = null;
    }

    super.onDestroy();
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new CursorLoader(this, OrderContract.OrderSummary.contentUriWithAccount(account), null, null, null, OrderContract.OrderSummary.CREATED + " DESC LIMIT 100");
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {
    final List<String> orderIds = new ArrayList<String>();
    if (data != null && data.moveToFirst()) {
      while (!data.isAfterLast()) {
        orderIds.add(data.getString(data.getColumnIndex(OrderContract.OrderSummary.ID)));
        data.moveToNext();
      }
    }

    new AsyncTask<Void, Void, List<Order>>() {
      @Override
      protected List<Order> doInBackground(Void... params) {
        List<Order> orders = new ArrayList<>();

        if (currencyUtils == null) {
          try {
            Merchant merchant = Utils.fetchMerchantBlocking(getApplicationContext());
            currencyUtils = new CurrencyUtils(getApplicationContext(), merchant);
          } catch (IOException e) {
            e.printStackTrace();
            return orders;
          }
        }

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

        Collections.sort(orders, (lhs, rhs) -> {
            if (lhs.getClientCreatedTime() < rhs.getClientCreatedTime()) {
              return 1;
            }
            if (lhs.getClientCreatedTime() > rhs.getClientCreatedTime()) {
              return -1;
            }
            return 0;
          });

        return orders;
      }

      @Override
      protected void onPostExecute(List<Order> orders) {
        if (OrderPaidActivity.this.isFinishing() || OrderPaidActivity.this.isDestroyed()) {
          return;
        }
        ordersList.setAdapter(new OrdersAdapter(orders));
      }
    }.execute();
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
  }

}
