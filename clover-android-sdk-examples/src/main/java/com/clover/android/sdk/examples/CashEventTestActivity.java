package com.clover.android.sdk.examples;

import android.accounts.Account;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.customer.Address;
import com.clover.sdk.v1.customer.Customer;
import com.clover.sdk.v1.customer.EmailAddress;
import com.clover.sdk.v1.customer.PhoneNumber;
import com.clover.sdk.v3.cash.CashEvents;
import com.clover.sdk.v3.cash.Type;

import java.util.List;

public class CashEventTestActivity extends Activity {
  private static final String TAG = "CashEventTestActivity";

  private TextView statusText;

  private Account account;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cashevent_test);

    statusText = (TextView) findViewById(R.id.status);

    account = CloverAccount.getAccount(this);

    Button b = (Button) findViewById(R.id.button);
    b.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        statusText.setText("");
        doExample();
      }
    });
  }

  private void doExample() {
    new AsyncTask<Void, Void, Boolean>() {
      @Override
      protected Boolean doInBackground(Void... params) {
        CashEvents cashEvents = new CashEvents(CashEventTestActivity.this, account);
        return cashEvents.addCash(50l, "This is a test");
      }

      @Override
      protected void onPostExecute(Boolean result) {
        if (result != null && result) {
          statusText.setText("Success");
        } else {
          statusText.setText("Failure");
        }
      }
    }.execute();
  }
}
