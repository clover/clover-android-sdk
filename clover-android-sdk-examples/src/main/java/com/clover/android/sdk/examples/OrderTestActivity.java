/*
 * Copyright (C) 2013 Clover Network, Inc.
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
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v1.ServiceConnector;
import com.clover.sdk.v1.ServiceException;
import com.clover.sdk.v1.order.Order;
import com.clover.sdk.v1.order.OrderConnector;
import com.clover.sdk.v1.order.OrderSummary;
import org.json.JSONException;

import java.util.List;

public class OrderTestActivity extends Activity implements ServiceConnector.OnServiceConnectedListener, OrderConnector.OnOrderUpdateListener {
  private static final String TAG = "OrderTestActivity";

  private static final int REQUEST_ACCOUNT = 1;

  private Account account;
  private String appAuthToken;
  private OrderConnector orders;

  private EditText orderIdEdit;
  private TextView statusText;
  private TextView orderJSONText;
  private Button createButton;
  private Button refreshButton;
  private Button selectButton;

  private Spinner fieldSpinner;
  private EditText fieldEdit;
  private Button fieldSetButton;

  private Order mOrder;
  private String mOrderId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_order_test);

    orderIdEdit = (EditText) findViewById(R.id.orderId);

    createButton = (Button) findViewById(R.id.create);
    createButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        createOrder();
      }
    });
    refreshButton = (Button) findViewById(R.id.refresh);
    refreshButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String orderId = orderIdEdit.getText().toString();
        if (TextUtils.isEmpty(orderId)) {
          mOrder = null;
          refreshOrder();
        } else {
          loadOrder(orderId);
        }
      }
    });
    selectButton = (Button) findViewById(R.id.select);
    selectButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        loadOrders();
      }
    });

    fieldEdit = (EditText) findViewById(R.id.fieldValue);
    fieldSetButton = (Button) findViewById(R.id.setField);
    fieldSetButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String newValue = fieldEdit.getText().toString();
        if (mOrder == null) {
          return;
        }
        boolean enabled = false;
        try {
          // "title", "note", "type", "state", "total",
          // "Group Line Items", "Tax Removed", "Test Mode", "Manual Transaction", "Customer Id", "Service Charge Id",
          switch (fieldSpinner.getSelectedItemPosition()) {
            case 0:
              orders.setTitle(mOrder.getId(), newValue, null);
              break;
            case 1:
              orders.setNote(mOrder.getId(), newValue, null);
              break;
            case 2:
              orders.setType(mOrder.getId(), newValue, null);
              break;
            case 3:
              orders.setState(mOrder.getId(), newValue, null);
              break;
            case 4:
              long total = Long.parseLong(newValue);
              orders.setTotal(mOrder.getId(), total, null);
              break;
            case 5:
              enabled = Boolean.parseBoolean(newValue);
              orders.setGroupLineItems(mOrder.getId(), enabled, null);
              break;
            case 6:
              enabled = Boolean.parseBoolean(newValue);
              orders.setTaxRemoved(mOrder.getId(), enabled, null);
              break;
            case 7:
              enabled = Boolean.parseBoolean(newValue);
              orders.setTestMode(mOrder.getId(), enabled, null);
              break;
            case 8:
              enabled = Boolean.parseBoolean(newValue);
              orders.setManualTransaction(mOrder.getId(), enabled, null);
              break;
            case 9:
              orders.setCustomer(mOrder.getId(), newValue, null);
              break;
            case 10:
              orders.addServiceCharge(mOrder.getId(), newValue, null);
              break;
          }
        } catch (RemoteException e) {
          e.printStackTrace();
        }
      }
    });

    fieldSpinner = (Spinner) findViewById(R.id.fieldSpinner);

    String[] fields = new String[]{"title", "note", "type", "state", "total",
        "Group Line Items", "Tax Removed", "Test Mode", "Manual Transaction", "Customer Id", "Service Charge Id"};
    ArrayAdapter<String> fieldsAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, fields);
    fieldSpinner.setAdapter(fieldsAdapter);

    fieldSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        refreshSpinner(i);
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {
        fieldEdit.setText("");
      }
    });

    statusText = (TextView) findViewById(R.id.status);
    orderJSONText = (TextView) findViewById(R.id.orderJSON);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_ACCOUNT && resultCode == RESULT_OK) {
      String name = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
      String type = data.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);

      account = new Account(name, type);
    }
  }

  private void startAccountChooser() {
    Intent intent = AccountManager.newChooseAccountIntent(null, null, new String[]{CloverAccount.CLOVER_ACCOUNT_TYPE}, false, null, null, null, null);
    startActivityForResult(intent, REQUEST_ACCOUNT);
  }

  @Override
  protected void onResume() {
    super.onResume();

    if (account != null) {
      orders = new OrderConnector(this, account, this);
      orders.connect();

      // for testing
      //loadOrder("3C7916CWRDDS0");

      if (TextUtils.isEmpty(appAuthToken)) {
        AccountManager.get(this).getAuthToken(account, CloverAccount.CLOVER_AUTHTOKEN_TYPE, null, this,
            new AccountManagerCallback<Bundle>() {
              public void run(AccountManagerFuture<Bundle> future) {
                try {
                  Bundle result = future.getResult();
                  Log.v(TAG, "Bundle result returned from account manager: ");
                  for (String key : result.keySet()) {
                    Object val = result.get(key);
                    Log.v(TAG, key + " => " + val);
                  }
                  appAuthToken = result.getString(AccountManager.KEY_AUTHTOKEN);
                } catch (OperationCanceledException e) {
                  Log.e(TAG, "Authentication cancelled", e);
                } catch (Exception e) {
                  Log.e(TAG, "Error retrieving authentication", e);
                }
              }
            }, null);
      }
    } else {
      startAccountChooser();
    }
  }

  @Override
  protected void onPause() {
    if (orders != null) {
      orders.disconnect();
      orders = null;
    }

    super.onPause();
  }

  @Override
  public void onServiceConnected(ServiceConnector connector) {
    Log.d(TAG, "onServiceConnected");
  }

  @Override
  public void onServiceDisconnected(ServiceConnector connector) {
    Log.d(TAG, "onServiceDisconnected");
  }

  @Override
  public void onOrderUpdated(String orderId) {
    Log.d(TAG, "onOrderUpdated " + orderId);
    if (!TextUtils.isEmpty(orderId) && mOrder != null && orderId.equals(mOrder.getId())) {
      loadOrder(orderId);
    }
  }

  ServiceConnector.Callback<Order> ordersCallback = new ServiceConnector.Callback<Order>() {
    @Override
    public void onServiceSuccess(Order result, ResultStatus status) {
      mOrder = result;
      refreshOrder();
    }

    @Override
    public void onServiceFailure(ResultStatus status) {
      mOrder = null;
      refreshOrder();
    }

    @Override
    public void onServiceConnectionFailure() {
      mOrder = null;
      refreshOrder();
    }
  };

  void createOrder() {
    if (true) {
      try {
        orders.create(ordersCallback);
      } catch (RemoteException e) {
        e.printStackTrace();
      }
    } else {
      AsyncTask<Void, Void, Order> task = new AsyncTask<Void, Void, Order>() {
        @Override
        protected Order doInBackground(Void... voids) {
          try {
            return orders.create();
          } catch (RemoteException e) {
            e.printStackTrace();
          } catch (ServiceException e) {
            e.printStackTrace();
          } catch (ClientException e) {
            e.printStackTrace();
          } catch (BindingException e) {
            e.printStackTrace();
          }
          return null;
        }

        @Override
        protected void onPostExecute(Order order) {
          mOrder = order;
          refreshOrder();
        }
      };
      task.execute();
    }
  }

  void loadOrder(String orderId) {
    if (true) {
      try {
        orders.getOrder(orderId, ordersCallback);
      } catch (RemoteException e) {
        e.printStackTrace();
      }
    } else {
      AsyncTask<String, Void, Order> task = new AsyncTask<String, Void, Order>() {
        @Override
        protected Order doInBackground(String... strings) {
          Order order = null;

          try {
            order = orders.getOrder(strings[0]);
          } catch (RemoteException e) {
            e.printStackTrace();
          } catch (ServiceException e) {
            e.printStackTrace();
          } catch (ClientException e) {
            e.printStackTrace();
          } catch (BindingException e) {
            e.printStackTrace();
          }

          return order;
        }

        @Override
        protected void onPostExecute(Order order) {
          mOrder = order;
          refreshOrder();
        }
      };
      task.execute(orderIdEdit.getText().toString());
    }
  }

  void refreshOrder() {
    if (mOrder != null) {
      mOrderId = mOrder.getId();

      orderIdEdit.setText(mOrderId);

      try {
        orderJSONText.setText(mOrder.mOrder.toString(2));
      } catch (JSONException e) {
        e.printStackTrace();
      }

    } else {
      mOrderId = null;
      orderIdEdit.setText("");
      orderJSONText.setText("");
    }

    refreshSpinner();
  }

  void refreshSpinner() {
    refreshSpinner(fieldSpinner.getSelectedItemPosition());
  }

  void refreshSpinner(int i) {
    if (mOrder != null) {
      // "title", "note", "type", "state", "total",
      // "Group Line Items", "Tax Removed", "Test Mode", "Manual Transaction", "Customer Id", "Service Charge Id",
      switch (i) {
        case 0:
          fieldEdit.setText(mOrder.getTitle());
          break;
        case 1:
          fieldEdit.setText(mOrder.getNote());
          break;
        case 2:
          fieldEdit.setText(mOrder.getType());
          break;
        case 3:
          fieldEdit.setText(mOrder.getState());
          break;
        case 4:
          fieldEdit.setText(String.valueOf(mOrder.getTotal()));
          break;
        case 5:
          fieldEdit.setText(String.valueOf(mOrder.getGroupLineItems()));
          break;
        case 6:
          fieldEdit.setText(String.valueOf(mOrder.getTaxRemoved()));
          break;
        case 7:
          fieldEdit.setText(String.valueOf(mOrder.getTestMode()));
          break;
        case 8:
          fieldEdit.setText(String.valueOf(mOrder.getManualTransaction()));
          break;
        case 9:
          fieldEdit.setText(mOrder.getCustomerId());
          break;
        case 10:
          fieldEdit.setText(mOrder.getServiceChargeId());
          break;
      }
    } else {
      fieldEdit.setText("");
    }
  }

  private void loadOrders() {
    try {
      orders.getOrders(0, 10, new ServiceConnector.Callback<List<OrderSummary>>() {
        @Override
        public void onServiceSuccess(List<OrderSummary> result, ResultStatus status) {
          showOrdersDialog(result);
        }

        @Override
        public void onServiceFailure(ResultStatus status) {
        }

        @Override
        public void onServiceConnectionFailure() {
        }
      });
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  private void showOrdersDialog(final List<OrderSummary> orders) {
    final AlertDialog.Builder builder = new AlertDialog.Builder(this);

    builder.setTitle("Select Order");
    final int size = orders.size();
    String[] names = new String[size];

    int i = 0;
    for (OrderSummary order : orders) {
      names[i++] = order.getId() + " " + order.mOrder.toString();
    }

    builder.setSingleChoiceItems(names, -1, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        // Stuff to do when the account is selected by the user
        dialog.dismiss();

        loadOrder(orders.get(which).getId());
      }
    });
    builder.show();
  }
}
