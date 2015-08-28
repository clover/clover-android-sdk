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
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v1.ServiceConnector;
import com.clover.sdk.v1.merchant.Merchant;
import com.clover.sdk.v1.merchant.MerchantAddress;
import com.clover.sdk.v1.merchant.MerchantConnector;
import com.clover.sdk.v1.merchant.MerchantIntent;

import java.text.DateFormat;
import java.util.Date;

public class MerchantTestActivity extends Activity implements MerchantConnector.OnMerchantChangedListener, ServiceConnector.OnServiceConnectedListener {
  private static final String TAG = "MerchantTestActivity";

  private static final int REQUEST_ACCOUNT = 0;

  private MerchantConnector merchantConnector;

  private TextView resultGetMerchantText;
  private TextView statusGetMerchantText;
  private Button buttonGetMerchant;
  private TextView statusSetAddressText;
  private Button buttonSetAddress;
  private Button buttonEmployees;
  private TextView statusSetPhoneText;
  private Button buttonPhone;
  private EditText address1Edit;
  private EditText address2Edit;
  private EditText address3Edit;
  private EditText cityEdit;
  private EditText stateEdit;
  private EditText zipEdit;
  private EditText countryEdit;
  private EditText phoneEdit;
  private final BroadcastReceiver merchantChangedReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      account = MerchantIntent.getAccount(intent);
      connect();
      merchantConnector.getMerchant(new MerchantConnector.MerchantCallback<Merchant>() {
        @Override
        public void onServiceSuccess(Merchant result, ResultStatus status) {
          notifyMerchantChanged(result);
        }
      });
    }
  };
  private Account account;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_merchant_test);

    resultGetMerchantText = (TextView) findViewById(R.id.result_get_merchant);
    statusGetMerchantText = (TextView) findViewById(R.id.status_get_merchant);
    buttonGetMerchant = (Button) findViewById(R.id.button_get_merchant);
    buttonGetMerchant.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startAccountChooser();
      }
    });
    statusSetAddressText = (TextView) findViewById(R.id.status_set_address);
    buttonSetAddress = (Button) findViewById(R.id.button_set_address);
    buttonSetAddress.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        setAddress();
      }
    });

    address1Edit = (EditText) findViewById(R.id.address1_edit);
    address2Edit = (EditText) findViewById(R.id.address2_edit);
    address3Edit = (EditText) findViewById(R.id.address3_edit);
    cityEdit = (EditText) findViewById(R.id.city_edit);
    stateEdit = (EditText) findViewById(R.id.state_edit);
    zipEdit = (EditText) findViewById(R.id.zip_edit);
    countryEdit = (EditText) findViewById(R.id.country_edit);
    phoneEdit = (EditText) findViewById(R.id.phone_edit);

    buttonEmployees = (Button) findViewById(R.id.button_employees);
    buttonEmployees.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(MerchantTestActivity.this, EmployeeTestActivity.class);
        intent.putExtra(EmployeeTestActivity.EXTRA_ACCOUNT, account);
        startActivity(intent);
      }
    });
    buttonPhone = (Button) findViewById(R.id.button_phone);
    buttonPhone.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        setPhoneNumber();
      }
    });
    statusSetPhoneText = (TextView) findViewById(R.id.status_set_phone);

    registerReceiver(merchantChangedReceiver, new IntentFilter(MerchantIntent.ACTION_MERCHANT_CHANGED));
  }

  private void startAccountChooser() {
    Intent intent = AccountManager.newChooseAccountIntent(null, null, new String[]{CloverAccount.CLOVER_ACCOUNT_TYPE}, false, null, null, null, null);
    startActivityForResult(intent, REQUEST_ACCOUNT);
  }

  @Override
  protected void onResume() {
    super.onResume();

    if (account != null) {
      connect();
      getMerchant();
    } else {
      startAccountChooser();
    }
  }


  @Override
  protected void onPause() {
    disconnect();
    super.onPause();
  }

  private void connect() {
    disconnect();
    if (account != null) {
      merchantConnector = new MerchantConnector(this, account, this);
      merchantConnector.setOnMerchantChangedListener(this);
      merchantConnector.connect();
    }
  }

  private void disconnect() {
    if (merchantConnector != null) {
      merchantConnector.disconnect();
      merchantConnector = null;
    }
  }

  @Override
  protected void onDestroy() {
    unregisterReceiver(merchantChangedReceiver);
    super.onDestroy();
  }

  private void getMerchant() {
    merchantConnector.getMerchant(new MerchantConnector.MerchantCallback<Merchant>() {
      @Override
      public void onServiceSuccess(Merchant result, ResultStatus status) {
        super.onServiceSuccess(result, status);

        updateMerchant("get merchant success", status, result);

        address1Edit.setText(result.getAddress().getAddress1());
        address2Edit.setText(result.getAddress().getAddress2());
        address3Edit.setText(result.getAddress().getAddress3());
        cityEdit.setText(result.getAddress().getCity());
        stateEdit.setText(result.getAddress().getState());
        zipEdit.setText(result.getAddress().getZip());
        countryEdit.setText(result.getAddress().getCountry());

        phoneEdit.setText(result.getPhoneNumber());
      }

      @Override
      public void onServiceFailure(ResultStatus status) {
        super.onServiceFailure(status);
        updateMerchant("get merchant failure", status, null);
      }

      @Override
      public void onServiceConnectionFailure() {
        super.onServiceConnectionFailure();
        updateMerchant("get merchant bind failure", null, null);
      }
    });
  }

  private void setPhoneNumber() {
    String phoneNumber = phoneEdit.getText().toString();

    merchantConnector.setPhoneNumber(phoneNumber, new MerchantConnector.MerchantCallback<Void>() {
      @Override
      public void onServiceSuccess(Void result, ResultStatus status) {
        super.onServiceSuccess(result, status);
        updateSetPhone("set phone success", status);
      }

      @Override
      public void onServiceFailure(ResultStatus status) {
        super.onServiceFailure(status);
        updateSetAddress("set phone failure", status);
      }

      @Override
      public void onServiceConnectionFailure() {
        super.onServiceConnectionFailure();
        updateSetAddress("set phone bind failure", null);
      }
    });

  }

  private void setAddress() {
    MerchantAddress address = new MerchantAddress.Builder()
        .address1(address1Edit.getText().toString())
        .address2(address2Edit.getText().toString())
        .address3(address3Edit.getText().toString())
        .city(cityEdit.getText().toString())
        .state(stateEdit.getText().toString())
        .zip(zipEdit.getText().toString())
        .country(countryEdit.getText().toString()).build();

    merchantConnector.setAddress(address, new MerchantConnector.MerchantCallback<Void>() {
      @Override
      public void onServiceSuccess(Void result, ResultStatus status) {
        super.onServiceSuccess(result, status);
        updateSetAddress("set address success", status);
      }

      @Override
      public void onServiceFailure(ResultStatus status) {
        super.onServiceFailure(status);
        updateSetAddress("set address failure", status);
      }

      @Override
      public void onServiceConnectionFailure() {
        super.onServiceConnectionFailure();
        updateSetAddress("set address bind failure", null);
      }
    });
  }

  private void updateMerchant(String status, ResultStatus resultStatus, Merchant result) {
    statusGetMerchantText.setText("<" + status + " " + (resultStatus != null ? resultStatus : "") + ": " + DateFormat.getDateTimeInstance().format(new Date()) + ">");

    if (result == null) {
      resultGetMerchantText.setText("<no merchant>");
    } else {
      resultGetMerchantText.setText(result.toString());
    }
  }

  private void updateSetAddress(String status, ResultStatus resultStatus) {
    statusSetAddressText.setText("<" + status + " " + (resultStatus != null ? resultStatus : "") + ": " + DateFormat.getDateTimeInstance().format(new Date()) + ">");
  }

  private void updateSetPhone(String status, ResultStatus resultStatus) {
    statusSetPhoneText.setText("<" + status + " " + (resultStatus != null ? resultStatus : "") + ": " + DateFormat.getDateTimeInstance().format(new Date()) + ">");
  }

  @Override
  public void onMerchantChanged(Merchant merchant) {
    updateMerchant("merchant changed", null, merchant);
  }

  private void notifyMerchantChanged(Merchant merchant) {
    Notification n = new Notification.Builder(this)
        .setContentTitle(String.format("Merchant changed: %s (%s)", merchant.getName(), merchant.getId()))
        .setContentText(String.format("Merchant changed: %s (%s)", merchant.getName(), merchant.getId()))
        .setSmallIcon(android.R.drawable.stat_sys_warning)
        .getNotification();
    NotificationManager notificationManager =
        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    n.flags |= Notification.FLAG_AUTO_CANCEL;
    notificationManager.notify(0, n);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_ACCOUNT) {
      if (resultCode == RESULT_OK) {
        String name = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String type = data.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);

        account = new Account(name, type);
      } else {
        if (account == null) {
          finish();
        }
      }
    }
  }

  @Override
  public void onServiceConnected(ServiceConnector connector) {
    Log.i(TAG, "service connected: " + connector);
  }

  @Override
  public void onServiceDisconnected(ServiceConnector connector) {
    Log.i(TAG, "service disconnected: " + connector);
  }
}
