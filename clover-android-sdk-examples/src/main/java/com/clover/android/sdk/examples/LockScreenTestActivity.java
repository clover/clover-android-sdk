package com.clover.android.sdk.examples;

import com.clover.sdk.Lockscreen;
import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ServiceException;
import com.clover.sdk.v3.employees.Employee;
import com.clover.sdk.v3.employees.EmployeeConnector;

import android.accounts.Account;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * The activity locks the device using {@link #lockButton}.
 * {@link #unlockButton} shows a list of registered employees on the device.
 * Selected employee is saved in SharedPreference and is fetched in {@link LockScreenBootCompleteReceiver} when device
 * reboots to unlock the device.
 * If no employee is selected, {@link LockScreenBootCompleteReceiver} will not do anything.
 * */
public class LockScreenTestActivity extends Activity {
  private static final String TAG = LockScreenTestActivity.class.getSimpleName();
  private static final String EMPLOYEE_PREF = "EmployeePreference";
  private static final String EMPLOYEE_ID = "EmployeeId";
  private static final String EMPLOYEE_NAME = "EmployeeName";

  private Button lockButton;
  private TextView selectText;
  private Lockscreen lockscreen;
  private EmployeeConnector employeeConnector;
  private Account account;
  private Button unlockButton;
  private List<Employee> employees;
  private RecyclerView employeeRecyclerView;
  private LockscreenEmployeeAdapter employeeAdapter;
  private SharedPreferences.Editor editor;
  private int selectedItem = -1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_lockscreen);
    lockscreen = new Lockscreen(this);
    account = CloverAccount.getAccount(this);
    employeeConnector = new EmployeeConnector(this, account, null);
    employeeRecyclerView = new RecyclerView(this);
    employees = new ArrayList<>();
    lockButton = findViewById(R.id.lockButton);
    unlockButton = findViewById(R.id.unlockButton);
    selectText = findViewById(R.id.selectTextView);
    employeeRecyclerView = findViewById(R.id.rvEmployeeList);
    editor = getSharedPreferences(EMPLOYEE_PREF, MODE_PRIVATE).edit();

    LockscreenEmployeeAdapter.ClickListener listener = (position, v) -> {
      selectedItem = position;
      if (selectedItem == -1) {
        editor.putString(EMPLOYEE_ID, null);
        editor.putString(EMPLOYEE_NAME, null);
      } else {
        editor.putString(EMPLOYEE_ID, employees.get(position).getId());
        editor.putString(EMPLOYEE_NAME, employees.get(position).getName());
      }
      editor.apply();
    };

    employeeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    employeeAdapter = new LockscreenEmployeeAdapter(this, employees, listener);
    employeeRecyclerView.setAdapter(employeeAdapter);
    /**
     * To lock the the device.
     * @see Lockscreen#lock()
     * */
    lockButton.setOnClickListener(v -> lockscreen.lock());
    /**
     * Displays a list of registered employees on the merchant's device.
     * @see EmployeeConnector#getEmployees()
     * On next reboot device unlocks with the selected employee's profile.
     * also see, @see Lockscreen#unlock(String)
     * */
    unlockButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        new AsyncTask<Void, Void, List<Employee>>() {
          @Override
          protected void onPostExecute(List<Employee> employees) {
            super.onPostExecute(employees);
            if (employees == null) {
              return;
            }
            LockScreenTestActivity.this.employees.clear();
            LockScreenTestActivity.this.employees.addAll(employees);
            employeeAdapter.notifyDataSetChanged();
            unlockButton.setClickable(false);
            unlockButton.setBackgroundColor(Color.GRAY);
            selectText.setVisibility(View.VISIBLE);
            return;
          }

          @Override
          protected List<Employee> doInBackground(Void... voids) {
            try {
              return employeeConnector.getEmployees();
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
    });
  }

  @Override
  protected void onPause() {
    employeeConnector.disconnect();
    super.onPause();
  }

  @Override
  protected void onResume() {
    super.onResume();
    employeeConnector.connect();
  }
}
