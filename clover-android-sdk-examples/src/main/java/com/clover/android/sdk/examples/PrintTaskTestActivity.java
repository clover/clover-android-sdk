/*
 * Copyright (C) 2020 Clover Network, Inc.
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

import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.printer.Printer;
import com.clover.sdk.v1.printer.PrinterConnector;
import com.clover.sdk.v1.printer.job.ImagePrintJob2;
import com.clover.sdk.v1.printer.job.ViewPrintJob;

import android.accounts.Account;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PrintTaskTestActivity extends Activity {

  private static final String TAG = PrintTaskTestActivity.class.getSimpleName();
  private static final String ITEM = "Item";
  private static final String COST = "$ Cost";
  private static final String MERCHANT = "Merchant";

  private Button imagePrintButton;
  private Button viewPrintButton;
  private Account account;
  private LinearLayout testView;
  private Bitmap icon;
  private PrinterConnector connector;
  private RecyclerView recyclerView;
  private List<Printer> resultList;
  private PrinterListAdapter adapter;

  private Printer selectedPrinter;
  private int viewWidth;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_print_task_test);
    viewPrintButton = findViewById(R.id.button1);
    imagePrintButton = findViewById(R.id.button2);
    account = CloverAccount.getAccount(this);
    resultList = new ArrayList<>();
    recyclerView = findViewById(R.id.recyclerView);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    PrinterListAdapter.CheckListener checkListener = new PrinterListAdapter.CheckListener() {
      @Override
      public void onItemChecked(int position, View v) {
        selectedPrinter = resultList.get(position);

        new AsyncTask<Void, Void, Integer>() {
          @Override
          protected Integer doInBackground(Void... voids) {
            try {
              int width = connector.getPrinterTypeDetails(selectedPrinter).getNumDotsWidth();
              Log.v(TAG, "Printer head width: " + width);
              return width;
            } catch (Exception e) {
              e.printStackTrace();
            }
            return 0;
          }

          @Override
          protected void onPostExecute(Integer width) {
            viewWidth = width;
          }
        }.execute();
      }

      @Override
      public void onItemUnchecked() {
        viewWidth = 0;
        selectedPrinter = null;
      }
    };

    adapter = new PrinterListAdapter(this, resultList, checkListener);
    recyclerView.setAdapter(adapter);
    connector = new PrinterConnector(this, account, null);
    getConnectedPrinters();
    testView = generateLayout(viewWidth);
    icon = BitmapFactory.decodeResource(this.getResources(), R.drawable.clover_logo);
    generateHeadLayout(testView);
    generateBodyLayout(testView);

    viewPrintButton.setOnClickListener((v) -> {
      if (selectedPrinter == null) {
        return;
      }

      Printer printer = selectedPrinter;
      int width = viewWidth;

      new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... voids) {
          ViewPrintJob printJob = new ViewPrintJob.Builder(). view(testView, width).build();
          printJob.print(getApplicationContext(), account, printer);
          return null;
        }
      }.execute();
    });

    imagePrintButton.setOnClickListener((v) -> {
      if (selectedPrinter == null) {
        return;
      }

      Printer printer = selectedPrinter;

      new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... voids) {
          ImagePrintJob2 imagePrintJob = new ImagePrintJob2.Builder(getApplicationContext()).bitmap(icon).build();
          imagePrintJob.print(getApplicationContext(), account, printer);
          return null;
        }
      }.execute();
    });
  }

/**
 * Fetch the list of all the connected printers.
 * @see PrinterConnector#getPrinter(String)
 * */
  private void getConnectedPrinters() {
    new AsyncTask<Void, Void, List<Printer>>() {
      @Override
      protected List<Printer> doInBackground(Void... params) {
        try {
          return connector.getPrinters();
        } catch (Exception e) {
          e.printStackTrace();
        }
        return null;
      }

      @Override
      protected void onPostExecute(List<Printer> printers) {
        if (printers != null && !printers.isEmpty()) {
          resultList.clear();
          resultList.addAll(printers);
          adapter.notifyDataSetChanged();
        }
      }
    }.execute();
  }

  private void generateBodyLayout(LinearLayout testView) {
    LinearLayout body = new LinearLayout(this);
    RelativeLayout row = new RelativeLayout(this);
    body.setMinimumWidth(ViewGroup.LayoutParams.MATCH_PARENT);
    row.setMinimumWidth(ViewGroup.LayoutParams.MATCH_PARENT);
    TextView item = new TextView(this);
    TextView cost = new TextView(this);
    row.addView(item);
    row.addView(cost);
    item.setText(ITEM);
    item.setTextSize(25);
    cost.setText(COST);
    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) cost.getLayoutParams();
    lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    cost.setLayoutParams(lp);
    cost.setTextSize(25);
    body.addView(row);
    testView.addView(body);
  }

  private LinearLayout generateLayout(int viewWidth) {
    LinearLayout mainLayout = new LinearLayout(this);
    mainLayout.setLayoutParams(new LinearLayout.LayoutParams(viewWidth, LinearLayout.LayoutParams.MATCH_PARENT));
    mainLayout.setOrientation(LinearLayout.VERTICAL);
    mainLayout.setBackgroundColor(Color.WHITE);
    return mainLayout;
  }

  @Override
  protected void onDestroy() {
    disconnect();
    super.onDestroy();
  }

  private void disconnect() {
    if (connector != null) {
      connector.disconnect();
      connector = null;
    }
  }

  public void generateHeadLayout(LinearLayout mainLayout) {
    ImageView image = new ImageView(this);
    image.setImageBitmap(icon);
    testView.addView(image);
    TextView merchantTv = new TextView(this);
    merchantTv.setText(MERCHANT);
    merchantTv.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
    merchantTv.setGravity(Gravity.CENTER);
    merchantTv.setTextSize(30);
    mainLayout.addView(merchantTv);
  }

}
