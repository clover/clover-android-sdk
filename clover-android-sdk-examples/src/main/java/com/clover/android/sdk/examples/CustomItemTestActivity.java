package com.clover.android.sdk.examples;

import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ServiceException;
import com.clover.sdk.v3.order.LineItem;
import com.clover.sdk.v3.order.Order;
import com.clover.sdk.v3.order.OrderConnector;

import android.accounts.Account;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * This activity creates a new Order {@link #bOrder} from the device and add new custom items to the
 * order{@link #bLineItem}.
 * Custom item are of two types - Fixed price custom item and Per Unit price custom item, which are added as lineitems
 * to order {@link #addLineItem(Order)}.
 * {@link #bCompOrder} completes the order and the order id should be updated in the Orders app.
 */
public class CustomItemTestActivity extends Activity {
  private static String TAG = CustomItemTestActivity.class.getSimpleName();
  private Account account;
  private Button bOrder, bLineItem, bCompOrder;
  private List<LineItem> lineItemList;
  private Order order;
  private EditText itemName, itemPrice, itemQuantity, itemType;
  private RelativeLayout mainLayout;
  private TextView orderCreated;
  private StringBuilder result;
  private OrderConnector orderConnector;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_custom_item_test);
    bOrder = findViewById(R.id.order);
    bLineItem = findViewById(R.id.lineitem);
    bCompOrder = findViewById(R.id.bCompleteOrder);
    itemName = findViewById(R.id.edItemName);
    itemPrice = findViewById(R.id.edItemPrice);
    itemQuantity = findViewById(R.id.edItemQuantity);
    itemType = findViewById(R.id.edItemUnit);
    orderCreated = findViewById(R.id.tvCreatedOrder);
    mainLayout = findViewById(R.id.mainLayout);
    account = CloverAccount.getAccount(this);
    orderConnector = new OrderConnector(getApplicationContext(), account, null);

    bOrder.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        new AsyncTask<Void, Void, Void>() {
          @Override
          protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mainLayout.setVisibility(View.VISIBLE);
            if (result != null) {
              orderCreated.setText(result.toString());
            }
          }

          @Override
          protected Void doInBackground(Void... voids) {
            OrderConnector orderConnector = new OrderConnector(getApplicationContext(), account, null);
            try {
              order = orderConnector.createOrder(new Order());
              Log.i(TAG, "new order created with orderId" + order.getId());
              result = new StringBuilder();
              result.append(getString(R.string.order_id_custom_item));
              result.append(order.getId());
            } catch (ClientException e) {
              e.printStackTrace();
            } catch (ServiceException e) {
              e.printStackTrace();
            } catch (BindingException e) {
              e.printStackTrace();
            } catch (RemoteException e) {
              e.printStackTrace();
            }
            return null;
          }
        }.execute();
      }
    });

    bLineItem.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        addLineItem(order);
      }
    });

    bCompOrder.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        order.setLineItems(lineItemList);
        Log.i(TAG, "new order" + order.toString());
      }
    });
  }

  /**
   * adds new lineitem to the {@link #lineItemList} which contains all the items created.
   */
  private void addLineItem(Order order) {
    new AsyncTask<Void, Void, Void>() {
      @Override
      protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Toast.makeText(getApplicationContext(), "Item added to order!", Toast.LENGTH_SHORT).show();
        itemName.getText().clear();
        itemPrice.getText().clear();
        itemQuantity.getText().clear();
        itemType.getText().clear();
      }

      /**
       * LineItem might be per unit price if lineItem.setUnitQty and lineItem.setUnitPrice are set else fixed price.
       * */
      @Override
      protected Void doInBackground(Void... voids) {
        LineItem lineItem = new LineItem();
        lineItem.setCreatedTime(System.currentTimeMillis());
        lineItem.setBinName(lineItem.getBinName());
        lineItem.setUserData(lineItem.getUserData());
        lineItem.setPrice(lineItem.getPrice());
        lineItem.setAlternateName(lineItem.getAlternateName());
        lineItem.setItemCode(lineItem.getItemCode());
        lineItem.setNote(lineItem.getNote());
        if (itemName.getText() != null) {
          lineItem.setName(itemName.getText().toString());
        }
        if (!itemQuantity.getText().toString().equals("")) {
          lineItem.setUnitQty(Integer.valueOf(itemQuantity.getText().toString()) * 1000);
        }
        if (!itemPrice.getText().toString().equals("")) {
          lineItem.setPrice(Long.valueOf(itemPrice.getText().toString()) * 100);
        }
        if (!itemType.getText().toString().equals("")) {
          if (!itemPrice.getText().toString().equals("")) {
            lineItem.setUnitName(itemType.getText().toString());
          }
        }
        lineItemList = order.getLineItems();
        if (lineItemList == null) {
          lineItemList = new ArrayList<>();
        }

        try {
          LineItem result = orderConnector.addCustomLineItem(order.getId(), lineItem, false);
          lineItemList.add(result);
        } catch (RemoteException e) {
          e.printStackTrace();
        } catch (ClientException e) {
          e.printStackTrace();
        } catch (ServiceException e) {
          e.printStackTrace();
        } catch (BindingException e) {
          e.printStackTrace();
        }
        return null;
      }
    }.execute();
  }
}
