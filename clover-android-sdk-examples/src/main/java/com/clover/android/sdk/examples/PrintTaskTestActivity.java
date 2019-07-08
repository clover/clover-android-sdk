package com.clover.android.sdk.examples;

import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ServiceException;
import com.clover.sdk.v1.printer.Printer;
import com.clover.sdk.v1.printer.PrinterConnector;
import com.clover.sdk.v1.printer.job.ImagePrintJob;
import com.clover.sdk.v1.printer.job.PrintJob;
import com.clover.sdk.v1.printer.job.ViewPrintJob;

import android.accounts.Account;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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
  private Button viewPrintButton2;
  private Account account;
  private LinearLayout testView;
  private Bitmap icon;
  private PrinterConnector connector;
  private RecyclerView recyclerView;
  private List<Printer> resultList;
  private PrinterListAdapter adapter;
  private int selectedPosition;
  private int viewWidth;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_print_task_test);
    imagePrintButton = findViewById(R.id.button2);
    /**
     * Button which uses the original view method from ViewPrintJob
     * to print.
     * */
    viewPrintButton = findViewById(R.id.button1);
    /**
     * Button which uses the new view method from ViewPrintJob
     * to print.
     * */
    viewPrintButton2 = findViewById(R.id.button3);
    account = CloverAccount.getAccount(this);
    resultList = new ArrayList<Printer>();
    recyclerView = findViewById(R.id.recyclerView);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    PrinterListAdapter.ClickListener clickListener = new PrinterListAdapter.ClickListener() {
      @Override
      public void onItemClick(int position, View v) {
        selectedPosition = position;
        new AsyncTask<Void, Void, Void>() {
          @Override
          protected Void doInBackground(Void... voids) {
            try {
              viewWidth = connector.getPrinterTypeDetails(resultList.get(selectedPosition)).getNumDotsWidth();
              Log.v(TAG, String.valueOf(viewWidth));
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
    };

    adapter = new PrinterListAdapter(this, resultList, clickListener);
    recyclerView.setAdapter(adapter);
    connector = new PrinterConnector(this, account, null);
    getConnectedPrinters();
    testView = generateLayout(viewWidth);
    icon = BitmapFactory.decodeResource(this.getResources(), R.drawable.clover_logo);
    generateHeadLayout(testView);
    generateBodyLayout(testView);

    /**
     * Prints receipt by using the older {@link ViewPrintJob.Builder#view(View)}
     * Must call {@link #layoutAndMeasureView(View, int)} before.
     * */
    viewPrintButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        new AsyncTask<Void, Void, Void>() {
          @Override
          protected Void doInBackground(Void... voids) {
            layoutAndMeasureView(testView, viewWidth);
            ViewPrintJob.Builder builder = new ViewPrintJob.Builder().view(testView);
            ViewPrintJob printJob = builder.build();
            printJob.print(getApplicationContext(), account);
            return null;
          }
        }.execute();
      }
    });
    
    /**
     * Prints receipt by using the older {@link ViewPrintJob.Builder#view(View, int)}
     * */
    viewPrintButton2.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        new AsyncTask<Void, Void, Void>() {
          @Override
          protected Void doInBackground(Void... voids) {
            ViewPrintJob.Builder builder = new ViewPrintJob.Builder().view(testView, viewWidth);
            ViewPrintJob printJob = builder.build();
            printJob.print(getApplicationContext(), account);
            return null;
          }
        }.execute();
      }
    });

    imagePrintButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        new AsyncTask<Void, Void, Void>() {
          @Override
          protected Void doInBackground(Void... voids) {
            ImagePrintJob.Builder builder = new ImagePrintJob.Builder().bitmap(icon);
            PrintJob imagePrintJob = builder.build();
            imagePrintJob.print(getApplicationContext(), account);
            return null;
          }
        }.execute();
      }
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

  public void layoutAndMeasureView(View view, int viewWidth) {
    int measuredWidth = View.MeasureSpec.makeMeasureSpec(viewWidth, View.MeasureSpec.EXACTLY);
    int measuredHeight = View.MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.WRAP_CONTENT, View.MeasureSpec.UNSPECIFIED);
    view.measure(measuredWidth, measuredHeight);
    view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
    view.requestLayout();
  }

  @Override
  protected void onPause() {
    super.onPause();
    disconnect();
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
