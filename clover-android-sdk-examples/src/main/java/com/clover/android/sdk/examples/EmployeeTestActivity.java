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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.Intents;
import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v1.ServiceConnector;
import com.clover.sdk.v1.merchant.Merchant;
import com.clover.sdk.v1.merchant.MerchantConnector;
import com.clover.sdk.v3.employees.AccountRole;
import com.clover.sdk.v3.employees.Employee;
import com.clover.sdk.v3.employees.EmployeeConnector;
import com.clover.sdk.v3.employees.EmployeeIntent;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class EmployeeTestActivity extends Activity
    implements EmployeeConnector.OnActiveEmployeeChangedListener,
    ServiceConnector.OnServiceConnectedListener {
  public static final String EXTRA_ACCOUNT = "account";

  private static final int REQUEST_ACCOUNT = 0;
  private static final String TAG = EmployeeTestActivity.class.getSimpleName();
  private static final String TEST_EMPLOYEE_NAME = EmployeeTestActivity.class.getSimpleName();

  private EmployeeConnector employeeConnector;
  private MerchantConnector merchantConnector;

  private TextView resultEmployeesText;
  private TextView resultActiveEmployeeText;
  private TextView statusEmployeesText;
  private TextView statusActiveEmployeeText;
  private Button buttonEmployees;
  private Button buttonActiveEmployee;
  private Button loginButton;
  private Button logoutButton;
  private Button createButton;
  private Button deleteButton;
  private TextView statusLogin;
  private EditText nicknameEditText;
  private Button setNicknameButton;
  private EditText customIdEditText;
  private Button setCustomIdButton;
  private EditText pinEditText;
  private Button setPinButton;
  private Spinner roleSpinner;
  private Button setRoleButton;

  private MerchantConnector.OnMerchantChangedListener merchantListener = new MerchantConnector.OnMerchantChangedListener() {
    @Override
    public void onMerchantChanged(Merchant merchant) {
      getEmployees();
    }
  };

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
  private List<Employee> employees;

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
    createButton = (Button) findViewById(R.id.button_create);
    createButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        createTestEmployee();
      }
    });
    deleteButton = (Button) findViewById(R.id.button_delete);
    deleteButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deleteTestEmployee();
      }
    });
    nicknameEditText = (EditText) findViewById(R.id.edit_text_nickname);
    setNicknameButton = (Button) findViewById(R.id.button_set_nickname);
    setNicknameButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        setNickname();
      }
    });
    customIdEditText = (EditText) findViewById(R.id.edit_text_custom_id);
    setCustomIdButton = (Button) findViewById(R.id.button_set_custom_id);
    setCustomIdButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        setCustomId();
      }
    });
    pinEditText = (EditText) findViewById(R.id.edit_text_pin);
    setPinButton = (Button) findViewById(R.id.button_set_pin);
    setPinButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        setPin();
      }
    });
    roleSpinner = (Spinner) findViewById(R.id.spinner_role);
    setRoleButton = (Button) findViewById(R.id.button_set_role);
    setRoleButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        setRole();
      }
    });

    account = getIntent().getParcelableExtra(EXTRA_ACCOUNT);
    registerReceiver(activeEmployeeChangedReceiver, new IntentFilter(EmployeeIntent.ACTION_ACTIVE_EMPLOYEE_CHANGED));
  }

  private void createTestEmployee() {
    Employee employee = null;
    employee = new Employee();
    employee.setName(TEST_EMPLOYEE_NAME);
    employee.setNickname("Tester");
    employee.setCustomId("test123");
    employee.setEmail("employee-test-activity@example.com");
    employee.setPin("123456");
    employee.setRole(AccountRole.EMPLOYEE);
    employeeConnector.createEmployee(employee, new EmployeeConnector.EmployeeCallback<Employee>() {
      @Override
      public void onServiceSuccess(Employee result, ResultStatus status) {
        if (status.getStatusCode() / 100 != 2) {
          toast("Unable to create employee: " + status.getStatusMessage());
          return;
        }
        toast("Successfully created test employee.");
      }

      @Override
      public void onServiceFailure(ResultStatus status) {
        toast("Unable to create test employee: " + status.getStatusMessage());
      }
    });
  }

  private void deleteTestEmployee() {
    Employee e = getTestEmployee();
    if (e == null) {
      return;
    }
    employeeConnector.deleteEmployee(e.getId(), new EmployeeConnector.EmployeeCallback<Void>() {
      @Override
      public void onServiceSuccess(Void result, ResultStatus status) {
        if (status.getStatusCode() / 100 != 2) {
          toast("Unable to delete test employee: " + status.getStatusMessage());
        } else {
          toast("Successfully deleted test employee.");
        }
      }

      @Override
      public void onServiceFailure(ResultStatus status) {
        toast("Unable to delete test employee: " + status.getStatusMessage());
      }
    });
  }

  private Employee getTestEmployee() {
    if (employees == null) {
      toast("Employees have not been loaded.");
      return null;
    }
    for (Employee e : employees) {
      if (TEST_EMPLOYEE_NAME.equals(e.getName())) {
        return e;
      }
    }
    toast("Test employee has not been created.");
    return null;
  }

  public void setNickname() {
    Employee e = getTestEmployee();
    if (e == null) {
      return;
    }
    final String nickname = nicknameEditText.getText().toString().trim();
    e.setNickname(nickname);
    employeeConnector.updateEmployee(e, new EmployeeConnector.EmployeeCallback<Employee>() {
      @Override
      public void onServiceSuccess(Employee result, ResultStatus status) {
        if (status.getStatusCode() / 100 == 2) {
          toast("Successfully set the test employee nickname to '" + nickname + "'.");
        } else {
          toast("Unable to set the test employee nickname: " + status.getStatusMessage());
        }
      }

      @Override
      public void onServiceFailure(ResultStatus status) {
        toast("Unable to set the test employee nickname: " + status.getStatusMessage());
      }
    });
  }

  public void setCustomId() {
    Employee e = getTestEmployee();
    if (e == null) {
      return;
    }
    final String customId = customIdEditText.getText().toString().trim();
    e.setCustomId(customId);
    employeeConnector.updateEmployee(e, new EmployeeConnector.EmployeeCallback<Employee>() {
      @Override
      public void onServiceSuccess(Employee result, ResultStatus status) {
        if (status.getStatusCode() / 100 == 2) {
          toast("Successfully set the test employee custom id to '" + customId + "'.");
        } else {
          toast("Unable to set the test employee custom id: " + status.getStatusMessage());
        }
      }

      @Override
      public void onServiceFailure(ResultStatus status) {
        toast("Unable to set the test employee custom id: " + status.getStatusMessage());
      }
    });
  }

  public void setPin() {
    Employee e = getTestEmployee();
    if (e == null) {
      return;
    }
    final String pin = pinEditText.getText().toString().trim();
    e.setPin(pin);
    employeeConnector.updateEmployee(e, new EmployeeConnector.EmployeeCallback<Employee>() {
      @Override
      public void onServiceSuccess(Employee result, ResultStatus status) {
        if (status.getStatusCode() / 100 == 2) {
          toast("Successfully set the test employee pin to '" + pin + "'.");
        } else {
          toast("Unable to set the test employee pin: " + status.getStatusMessage());
        }
      }

      @Override
      public void onServiceFailure(ResultStatus status) {
        toast("Unable to set the test employee pin: " + status.getStatusMessage());
      }
    });
  }

  public void setRole() {
    Employee e = getTestEmployee();
    if (e == null) {
      return;
    }
    final String role = roleSpinner.getSelectedItem().toString();
    e.setRole(getRoleFromString(role));
    employeeConnector.updateEmployee(e, new EmployeeConnector.EmployeeCallback<Employee>() {
      @Override
      public void onServiceSuccess(Employee result, ResultStatus status) {
        if (status.getStatusCode() / 100 == 2) {
          toast("Successfully set the test employee role to '" + role + "'.");
        } else {
          toast("Unable to set the test employee role: " + status.getStatusMessage());
        }
      }

      @Override
      public void onServiceFailure(ResultStatus status) {
        toast("Unable to set the test employee role: " + status.getStatusMessage());
      }
    });
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
    employeeConnector.logout(new EmployeeConnector.EmployeeCallback<Void>() {
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
      merchantConnector = new MerchantConnector(this, account, this);
      merchantConnector.setOnMerchantChangedListener(merchantListener);
      merchantConnector.connect();
    }
  }

  private void disconnect() {
    if (employeeConnector != null) {
      employeeConnector.disconnect();
      employeeConnector = null;
    }
    if (merchantConnector != null) {
      merchantConnector.disconnect();
      merchantConnector = null;
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
        employees = result;
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
      String employees = "";
      for (Employee employee : result) {
        employees += employee.getName() + ", " + employee.getEmail() + "\n";
      }
      resultEmployeesText.setText(employees);
    }
  }

  private void updateActiveEmployee(String status, ResultStatus resultStatus, Employee result) {
    statusActiveEmployeeText.setText("<" + status + " " + (resultStatus != null ? resultStatus : "") + ": " + DateFormat.getDateTimeInstance().format(new Date()) + ">");
    if (result == null) {
      resultActiveEmployeeText.setText("<no active employee>");
    } else {
      resultActiveEmployeeText.setText(result.getName() + ", " + result.getEmail() + "\n");
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
  public void onServiceConnected(ServiceConnector connector) {
    Log.i(TAG, "service connected: " + connector);
  }

  @Override
  public void onServiceDisconnected(ServiceConnector connector) {
    Log.i(TAG, "service disconnected: " + connector);
  }

  private void toast(String message) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
  }

  private AccountRole getRoleFromString(String role) {
    if (role == null) throw new IllegalArgumentException("Role must not be null");
    if (role.equals("MANAGER")) {
      return AccountRole.MANAGER;
    } else if (role.equals("ADMIN")) {
      return AccountRole.ADMIN;
    } else if (role.equals("EMPLOYEE")) {
      return AccountRole.EMPLOYEE;
    }
    throw new IllegalArgumentException("Unrecognized role");
  }

}
