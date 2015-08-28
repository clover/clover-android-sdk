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
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v1.tender.Tender;
import com.clover.sdk.v1.tender.TenderConnector;

import java.util.List;

public class CreateCustomTenderTestActivity extends Activity {
  private static final String TAG = "CreateCustomTenderTestActivity";

  private static final int REQUEST_ACCOUNT = 1;

  private TenderConnector tenderConnector;
  private Account account;

  private TextView resultText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tenders_test);

    resultText = (TextView) findViewById(R.id.result);
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
      connect();
      createTender();
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
      tenderConnector = new TenderConnector(this, account, null);
      tenderConnector.connect();
    }
  }

  private void disconnect() {
    if (tenderConnector != null) {
      tenderConnector.disconnect();
      tenderConnector = null;
    }
  }

  private void getTenders() {
    tenderConnector.getTenders(new TenderConnector.TenderCallback<List<Tender>>() {
      @Override
      public void onServiceSuccess(List<Tender> result, ResultStatus status) {
        super.onServiceSuccess(result, status);
        String text = "Tenders:\n";
        for (Tender t : result) {
          text += "  " + t.getId() + " , " + t.getLabel() + " , " + t.getLabelKey() + " , " + t.getEnabled() + " , " + t.getOpensCashDrawer() + "\n";
        }
        resultText.setText(text);
      }

      @Override
      public void onServiceFailure(ResultStatus status) {
        super.onServiceFailure(status);
        resultText.setText(status.getStatusMessage());
      }

      @Override
      public void onServiceConnectionFailure() {
        super.onServiceConnectionFailure();
        resultText.setText("Service Connection Failure");
      }
    });
  }

  private void createTender() {
    final String tenderName = "Clover Example Tender";
    final String packageName = getPackageName();

    tenderConnector.checkAndCreateTender(tenderName, packageName, true, false, new TenderConnector.TenderCallback<Tender>() {
      @Override
      public void onServiceSuccess(Tender result, ResultStatus status) {
        super.onServiceSuccess(result, status);
        String text = "Custom Tender:\n";
        text += "  " + result.getId() + " , " + result.getLabel() + " , " + result.getLabelKey() + " , " + result.getEnabled() + " , " + result.getOpensCashDrawer() + "\n";
        resultText.setText(text);
      }

      @Override
      public void onServiceFailure(ResultStatus status) {
        super.onServiceFailure(status);
        resultText.setText(status.getStatusMessage());
      }

      @Override
      public void onServiceConnectionFailure() {
        super.onServiceConnectionFailure();
        resultText.setText("Service Connection Failure");
      }
    });
  }
}
