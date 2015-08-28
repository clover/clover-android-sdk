/**
 * Copyright (C) 2015 Clover Network, Inc.
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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ForbiddenException;
import com.clover.sdk.v1.ServiceException;
import com.clover.sdk.v1.customer.Address;
import com.clover.sdk.v1.customer.Customer;
import com.clover.sdk.v1.customer.CustomerConnector;
import com.clover.sdk.v1.customer.CustomerIntent;
import com.clover.sdk.v1.customer.EmailAddress;
import com.clover.sdk.v1.customer.PhoneNumber;

import java.util.ArrayList;
import java.util.List;

public class CustomerTestActivity extends Activity {
  private static final String TAG = "CustomerTestActivity";

  private CustomerConnector customerConnector;
  private final CustomerReceiver customerReceiver = new CustomerReceiver();

  private TextView statusText;

  private Account account;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_customer_test);

    statusText = (TextView) findViewById(R.id.status);

    account = CloverAccount.getAccount(this);
    if (account != null) {
      customerConnector = new CustomerConnector(this, account, null);
      customerConnector.connect();
    }

    Button b = (Button) findViewById(R.id.button);
    b.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        statusText.setText("");
        doExample();
      }
    });
  }

  @Override
  protected void onPause() {
    customerConnector.disconnect();
    unregisterReceiver(customerReceiver);
    super.onPause();
  }


  @Override
  protected void onResume() {
    super.onResume();
    registerReceiver(customerReceiver, new IntentFilter(CustomerIntent.ACTION_CUSTOMER_UPDATE));
  }

  public class CustomerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      Toast.makeText(context, "Broadcast Received", Toast.LENGTH_LONG).show();
      Log.v("CustomerReceiver", intent.getAction());
      if (intent.getExtras() != null) {
        for (String s : intent.getExtras().keySet()) {
          Log.v("CustomerReceiver", "    " + s + " : " + intent.getExtras().get(s));
        }
      }
    }
  }

  private void doExample() {
    new AsyncTask<Void, Void, List<String>>() {
      @Override
      protected List<String> doInBackground(Void... params) {
        try {
          ArrayList<String> output = new ArrayList<String>();

          output.add("Create customer ->");
          Customer newCustomer = customerConnector.createCustomer("Jack", "Smith", false);
          output.add(customerToOutput(newCustomer));

          output.add("\nChange customer name->");
          customerConnector.setName(newCustomer.getId(), "Julie", "Clover");
          newCustomer = customerConnector.getCustomer(newCustomer.getId());
          output.add(customerToOutput(newCustomer));

          output.add("\nChange customer marketing->");
          customerConnector.setMarketingAllowed(newCustomer.getId(), true);
          newCustomer = customerConnector.getCustomer(newCustomer.getId());
          output.add(customerToOutput(newCustomer));

          output.add("\nAdd phone number->");
          PhoneNumber newPhoneNumber = customerConnector.addPhoneNumber(newCustomer.getId(), "555-444-3333");
          newCustomer = customerConnector.getCustomer(newCustomer.getId());
          output.add(customerToOutput(newCustomer));

          output.add("\nChange phone number->");
          customerConnector.setPhoneNumber(newCustomer.getId(), newPhoneNumber.getId(), "666-555-4444");
          newCustomer = customerConnector.getCustomer(newCustomer.getId());
          output.add(customerToOutput(newCustomer));

          output.add("\nAdd email address number->");
          EmailAddress newEmailAddress = customerConnector.addEmailAddress(newCustomer.getId(), "testing@clover.com");
          newCustomer = customerConnector.getCustomer(newCustomer.getId());
          output.add(customerToOutput(newCustomer));

          output.add("\nChange email address ->");
          customerConnector.setEmailAddress(newCustomer.getId(), newEmailAddress.getId(), "demo@clover.com");
          newCustomer = customerConnector.getCustomer(newCustomer.getId());
          output.add(customerToOutput(newCustomer));

          output.add("\nAdd address number->");
          Address newAddress = customerConnector.addAddress(newCustomer.getId(), "123 Test St", "Apt 45", "Address 3", "Sunnyvale", "CA", "94086");
          newCustomer = customerConnector.getCustomer(newCustomer.getId());
          output.add(customerToOutput(newCustomer));

          output.add("\nChange address ->");
          customerConnector.setAddress(newCustomer.getId(), newAddress.getId(), "456 Main St", "", "Address 3", "Austin", "TX", "78701");
          newCustomer = customerConnector.getCustomer(newCustomer.getId());
          output.add(customerToOutput(newCustomer));

          output.add("\nDelete phone number->");
          customerConnector.deletePhoneNumber(newCustomer.getId(), newPhoneNumber.getId());
          newCustomer = customerConnector.getCustomer(newCustomer.getId());
          output.add(customerToOutput(newCustomer));

          output.add("\nDelete email address->");
          customerConnector.deleteEmailAddress(newCustomer.getId(), newEmailAddress.getId());
          newCustomer = customerConnector.getCustomer(newCustomer.getId());
          output.add(customerToOutput(newCustomer));

          output.add("\nDelete address ->");
          customerConnector.deleteAddress(newCustomer.getId(), newAddress.getId());
          newCustomer = customerConnector.getCustomer(newCustomer.getId());
          output.add(customerToOutput(newCustomer));

          return output;
        } catch (ForbiddenException e) {
          Log.e(TAG, "Auth exception", e);
        } catch (ClientException e) {
          Log.e(TAG, "Client exception", e);
        } catch (ServiceException e) {
          Log.e(TAG, "Service exception", e);
        } catch (BindingException e) {
          Log.e(TAG, "Error calling customer service", e);
        } catch (RemoteException e) {
          Log.e(TAG, "Error calling customer service", e);
        }
        return null;
      }

      @Override
      protected void onPostExecute(List<String> output) {
        if (output != null) {
          String temp = "";
          for (String s : output) {
            temp = temp + s + "\n";
            //temp += customer.getFirstName() + " " + customer.getLastName() + "\n";
          }
          statusText.setText(temp);
        } else {
          statusText.setText("");
        }
      }
    }.execute();
  }

  private String customerToOutput(Customer c) {
    StringBuilder sb = new StringBuilder();
    sb.append("  " + c.getId()).append(" : ").append(c.getFirstName()).append(" , ").append(c.getLastName()).append(" - ").append(c.getMarketingAllowed());
    if (c.getPhoneNumbers().size() > 0) {
      sb.append("\n     ").append("Phone Numbers:");
      for (PhoneNumber phoneNumber : c.getPhoneNumbers()) {
        sb.append("\n          ").append(phoneNumber.getId()).append(" : ").append(phoneNumber.getPhoneNumber());
      }
    }

    if (c.getEmailAddresses().size() > 0) {
      sb.append("\n     ").append("Email Addresses:");
      for (EmailAddress emailAddress : c.getEmailAddresses()) {
        sb.append("\n          ").append(emailAddress.getId()).append(" : ").append(emailAddress.getEmailAddress());
      }
    }

    if (c.getAddresses().size() > 0) {
      sb.append("\n     ").append("Addresses:");
      for (Address address : c.getAddresses()) {
        sb.append("\n          ").append(address.getId()).append(" : ").append(address.getAddress1()).append(" , ").append(address.getAddress2()).append(address.getAddress3()).append(" , ").append(address.getCity()).append(" , ").append(address.getState()).append(" , ").append(address.getZip());
      }
    }
    return sb.toString();
  }
}
