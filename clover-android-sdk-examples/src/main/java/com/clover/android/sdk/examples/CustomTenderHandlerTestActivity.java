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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CustomTenderHandlerTestActivity extends Activity {

  private long amount;
  private String orderId;
  private String merchantId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tenderhandler_test);

    Intent intent = getIntent();
    if (intent.getAction().equals("clover.intent.action.PAY")) {
      amount = intent.getLongExtra("clover.intent.extra.AMOUNT", 0l);
      orderId = intent.getStringExtra("clover.intent.extra.ORDER_ID");
      merchantId = intent.getStringExtra("clover.intent.extra.MERCHANT_ID");
    }

    Button accept = (Button) findViewById(R.id.acceptButton);
    accept.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("clover.intent.extra.ORDER_ID", orderId);
        returnIntent.putExtra("clover.intent.extra.AMOUNT", amount);
        returnIntent.putExtra("clover.intent.extra.MERCHANT_ID", merchantId);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
      }
    });

    Button decline = (Button) findViewById(R.id.declineButton);
    decline.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        setResult(Activity.RESULT_CANCELED, new Intent());
        finish();
      }
    });
  }
}
