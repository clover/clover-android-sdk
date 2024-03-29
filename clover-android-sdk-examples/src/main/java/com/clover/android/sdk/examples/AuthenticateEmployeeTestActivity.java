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
import android.content.Intent;
import android.os.Bundle;
import android.os.IInterface;
import android.widget.TextView;
import android.widget.Toast;


import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.Intents;
import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v1.ServiceConnector;
import com.clover.sdk.v3.employees.AccountRole;
import com.clover.sdk.v3.employees.Employee;
import com.clover.sdk.v3.employees.EmployeeConnector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class AuthenticateEmployeeTestActivity extends Activity {

  private static final int REQUEST_ANY_EMPLOYEE = 1;
  private static final int REQUEST_EMPLOYEE = 2;
  private static final int REQUEST_ROLE = 3;

  private TextView logTextView;
  private Account account;
  private Employee employee;
  private EmployeeConnector employeeConnector;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_authenticate_employee_test);

    logTextView = findViewById(R.id.log_text);

    findViewById(R.id.btn_any).setOnClickListener(v -> {
      Intent i = new Intent(Intents.ACTION_AUTHENTICATE_EMPLOYEE);
      i.putExtra(Intents.EXTRA_REASON, "Enter passcode");
      i.putParcelableArrayListExtra(Intents.EXTRA_ACCOUNT_ROLES, new ArrayList<>(Arrays.asList(
          AccountRole.ADMIN, AccountRole.MANAGER, AccountRole.EMPLOYEE)));
      startActivityForResult(i, REQUEST_ANY_EMPLOYEE);
    });

    findViewById(R.id.btn_active).setOnClickListener(v -> {
      if (employee == null) return;
      Intent i = new Intent(Intents.ACTION_AUTHENTICATE_EMPLOYEE);
      i.putExtra(Intents.EXTRA_EMPLOYEE_ID, employee.getId());
      i.putExtra(Intents.EXTRA_REASON, "Enter passcode for employee: " + employee.getName());
      startActivityForResult(i, REQUEST_EMPLOYEE);
    });

    findViewById(R.id.btn_request_admin_role).setOnClickListener(v -> {
      if (employee == null) return;
      Intent i = new Intent(Intents.ACTION_AUTHENTICATE_EMPLOYEE);
      i.putParcelableArrayListExtra(Intents.EXTRA_ACCOUNT_ROLES, new ArrayList<>(Collections.singletonList(AccountRole.ADMIN)));
      i.putExtra(Intents.EXTRA_REASON, "Enter admin passcode:");
      startActivityForResult(i, REQUEST_ROLE);
    });

  }

  @Override
  protected void onPause() {
    disconnect();
    super.onPause();
  }

  @Override
  protected void onResume() {
    super.onResume();

    // Retrieve the Clover account
    if (account == null) {
      account = CloverAccount.getAccount(this);
      if (account == null) {
        Toast.makeText(this, "No account found", Toast.LENGTH_SHORT).show();
        finish();
        return;
      }
    }

    // Create and Connect to the EmployeeConnector
    connect();

  }

  private void connect() {
    disconnect();
    if (account != null) {
      employeeConnector = new EmployeeConnector(this, account, new ServiceConnector.OnServiceConnectedListener() {
        @Override
        public void onServiceConnected(ServiceConnector<? extends IInterface> connector) {
          getEmployee();
        }

        @Override
        public void onServiceDisconnected(ServiceConnector<? extends IInterface> connector) {
        }
      });
      employeeConnector.connect();
    }
  }

  private void disconnect() {   //remember to disconnect!
    if (employeeConnector != null) {
      employeeConnector.disconnect();
      employeeConnector = null;
    }
  }

  private void getEmployee() {
    employeeConnector.getEmployee(new EmployeeConnector.EmployeeCallback<Employee>() {
      @Override
      public void onServiceSuccess(Employee result, ResultStatus status) {
        super.onServiceSuccess(result, status);
        employee = result;
      }

      @Override
      public void onServiceFailure(ResultStatus status) {
        super.onServiceFailure(status);
      }

      @Override
      public void onServiceConnectionFailure() {
        super.onServiceConnectionFailure();
      }
    });
  }
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    {
      logTextView.setText("");
      log("onActivityResult  requestCode: " + requestCode + "    resultCode: " + resultCode);
      String stringExtra;
      if (data != null && resultCode == RESULT_OK) {
        stringExtra = data.getStringExtra(Intents.EXTRA_EMPLOYEE_ID);
        if (stringExtra != null) {
          log("EXTRA_EMPLOYEE_ID: " + stringExtra);
        }
      }
    }
    super.onActivityResult(requestCode, resultCode, data);
  }
  private void log(String s) {
    if (logTextView.getText() != null && logTextView.getText().length() > 0) {
      logTextView.setText(logTextView.getText() + "\n" + s);
    } else {
      logTextView.setText("Log: \n" + s);
    }
  }
}
