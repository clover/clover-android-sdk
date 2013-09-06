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
import android.widget.TextView;
import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.Intents;
import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v1.ServiceConnector;
import com.clover.sdk.v1.employee.Employee;
import com.clover.sdk.v1.employee.EmployeeConnector;
import com.clover.sdk.v1.employee.EmployeeIntent;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class EmployeeTestActivity extends Activity implements EmployeeConnector.OnActiveEmployeeChangedListener, ServiceConnector.OnServiceConnectedListener {
  public static final String EXTRA_ACCOUNT = "account";

  private static final int REQUEST_ACCOUNT = 0;
  private static final String TAG = "EmployeeTestActivity";

  private EmployeeConnector employeeConnector;

  private TextView resultEmployeesText;
  private TextView resultActiveEmployeeText;
  private TextView statusEmployeesText;
  private TextView statusActiveEmployeeText;
  private Button buttonEmployees;
  private Button buttonActiveEmployee;
  private Button loginButton;
  private Button logoutButton;
  private TextView statusLogin;
  private final BroadcastReceiver activeEmployeeChangedReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      String employeeId = intent.getStringExtra(Intents.EXTRA_EMPLOYEE_ID);
      account = EmployeeIntent.getAccount(intent);
      connect();
      employeeConnector.getEmployee(employeeId, new EmployeeConnector.EmployeeCallback<Employee>() {
        @Override
        public void onServiceSuccess(Employee result, ResultStatus status) {
          notifyActiveEmployeeChanged(result);
        }
      });
    }
  };
  private Account account;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_employee_test);

    statusEmployeesText = (TextView) findViewById(R.id.status_employees);
    statusActiveEmployeeText = (TextView) findViewById(R.id.status_active_employee);
    resultEmployeesText = (TextView) findViewById(R.id.result_employees);
    resultActiveEmployeeText = (TextView) findViewById(R.id.result_active_employee);
    buttonEmployees = (Button) findViewById(R.id.button_employees);
    buttonEmployees.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        getEmployees();
      }
    });
    buttonActiveEmployee = (Button) findViewById(R.id.button_active_employee);
    buttonActiveEmployee.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        getActiveEmployee();
      }
    });
    loginButton = (Button) findViewById(R.id.button_login);
    loginButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        login();
      }
    });
    logoutButton = (Button) findViewById(R.id.button_logout);
    logoutButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        logout();
      }
    });
    statusLogin = (TextView) findViewById(R.id.status_login);

    account = getIntent().getParcelableExtra(EXTRA_ACCOUNT);
    registerReceiver(activeEmployeeChangedReceiver, new IntentFilter(EmployeeIntent.ACTION_ACTIVE_EMPLOYEE_CHANGED));
  }

  private void login() {
    employeeConnector.login(new EmployeeConnector.EmployeeCallback<Void>() {
      @Override
      public void onServiceSuccess(Void result, ResultStatus status) {
        super.onServiceSuccess(result, status);
        updateLogin("login success", status);
      }

      @Override
      public void onServiceFailure(ResultStatus status) {
        super.onServiceFailure(status);
        updateLogin("login failed", status);
      }

      @Override
      public void onServiceConnectionFailure() {
        super.onServiceConnectionFailure();
        updateLogin("login failed", null);
      }
    });
  }

  private void logout() {
    employeeConnector.logout(new EmployeeConnector.EmployeeCallback<Void>(){
      @Override
      public void onServiceSuccess(Void result, ResultStatus status) {
        super.onServiceSuccess(result, status);
        updateLogout("logout success", status);
      }

      @Override
      public void onServiceFailure(ResultStatus status) {
        super.onServiceFailure(status);
        updateLogout("logout failed", status);
      }

      @Override
      public void onServiceConnectionFailure() {
        super.onServiceConnectionFailure();
        updateLogout("logout failed", null);
      }
    });
  }

  private void connect() {
    disconnect();
    if (account != null) {
      employeeConnector = new EmployeeConnector(this, account, this);
      employeeConnector.connect();
    }
  }

  private void disconnect() {
    if (employeeConnector != null) {
      employeeConnector.disconnect();
      employeeConnector = null;
    }
  }

  @Override
  protected void onResume() {
    super.onResume();

    if (account != null) {
      connect();
      getActiveEmployee();
      getEmployees();
    } else {
      startAccountChooser();
    }
  }

  @Override
  protected void onPause() {
    disconnect();
    super.onPause();
  }

  @Override
  protected void onDestroy() {
    unregisterReceiver(activeEmployeeChangedReceiver);
    super.onDestroy();
  }

  private void getEmployees() {
    employeeConnector.getEmployees(new EmployeeConnector.EmployeeCallback<List<Employee>>() {
      @Override
      public void onServiceSuccess(List<Employee> result, ResultStatus status) {
        super.onServiceSuccess(result, status);
        updateEmployees("get employees success", status, result);
      }

      @Override
      public void onServiceFailure(ResultStatus status) {
        super.onServiceFailure(status);
        updateEmployees("get employees failure", status, null);
      }

      @Override
      public void onServiceConnectionFailure() {
        super.onServiceConnectionFailure();
        updateEmployees("get employees failure", null, null);
      }
    });
  }

  private void getActiveEmployee() {
    employeeConnector.getEmployee(new EmployeeConnector.EmployeeCallback<Employee>() {
      @Override
      public void onServiceSuccess(Employee result, ResultStatus status) {
        super.onServiceSuccess(result, status);
        updateActiveEmployee("get active employee success", status, result);
      }

      @Override
      public void onServiceFailure(ResultStatus status) {
        super.onServiceFailure(status);
        updateActiveEmployee("get active employee failure", status, null);
      }

      @Override
      public void onServiceConnectionFailure() {
        super.onServiceConnectionFailure();
        updateActiveEmployee("get active employee failure", null, null);
      }
    });
  }

  private void updateEmployees(String status, ResultStatus resultStatus, List<Employee> result) {
    statusEmployeesText.setText("<" + status + " " + (resultStatus != null ? resultStatus : "") + ": " + DateFormat.getDateTimeInstance().format(new Date()) + ">");
    if (result == null) {
      resultEmployeesText.setText("");
    } else {
      resultEmployeesText.setText(result.toString());
    }
  }

  private void updateActiveEmployee(String status, ResultStatus resultStatus, Employee result) {
    statusActiveEmployeeText.setText("<" + status + " " + (resultStatus != null ? resultStatus : "") + ": " + DateFormat.getDateTimeInstance().format(new Date()) + ">");
    if (result == null) {
      resultActiveEmployeeText.setText("<no active employee>");
    } else {
      resultActiveEmployeeText.setText(result.toString());
    }
  }

  private void updateLogin(String status, ResultStatus resultStatus) {
    statusLogin.setText("<" + status + " " + (resultStatus != null ? resultStatus : "") + ": " + DateFormat.getDateTimeInstance().format(new Date()) + ">");
  }

  private void updateLogout(String status, ResultStatus resultStatus) {
    statusLogin.setText("<" + status + " " + (resultStatus != null ? resultStatus : "") + ": " + DateFormat.getDateTimeInstance().format(new Date()) + ">");
  }

  private void notifyActiveEmployeeChanged(Employee employee) {
    String msg;
    if (employee != null) {
      msg = String.format("Active employee changed: %s (%s)", employee.getName(), employee.getId());
    } else {
      msg = "Employee logged out";
    }
    Notification n = new Notification.Builder(this)
            .setContentTitle(msg)
            .setContentText(msg)
            .setSmallIcon(android.R.drawable.stat_sys_warning)
            .getNotification();
    NotificationManager notificationManager =
            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    n.flags |= Notification.FLAG_AUTO_CANCEL;
    notificationManager.notify(0, n);
  }

  private void startAccountChooser() {
    Intent intent = AccountManager.newChooseAccountIntent(null, null, new String[]{CloverAccount.CLOVER_ACCOUNT_TYPE}, false, null, null, null, null);
    startActivityForResult(intent, REQUEST_ACCOUNT);
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
  public void onActiveEmployeeChanged(Employee employee) {
    updateActiveEmployee("active employee changed", null, employee);
  }

  @Override
  public void onServiceConnected() {
    Log.i(TAG, "service connected");
  }

  @Override
  public void onServiceDisconnected() {
    Log.i(TAG, "service disconnected");
  }
}
