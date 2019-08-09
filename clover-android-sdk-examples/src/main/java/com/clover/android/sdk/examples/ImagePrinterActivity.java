package com.clover.android.sdk.examples;

import android.accounts.Account;
import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ServiceException;
import com.clover.sdk.v1.printer.job.PrintJob;
import com.clover.sdk.v1.printer.job.StaticOrderPrintJob;
import com.clover.sdk.v1.printer.job.StaticReceiptPrintJob;
import com.clover.sdk.v3.order.LineItem;
import com.clover.sdk.v3.order.Order;
import com.clover.sdk.v3.order.OrderConnector;

import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates printing an image provided by the URI in
 * the footer of the Receipt.
 */
public class ImagePrinterActivity extends Activity {

  EditText mEditText;
  Button mPrintButton;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.image_printer_acitivity);
    mEditText = findViewById(R.id.uri_edit_box);
    mEditText.setText(PrinterImageProvider.CONTENT_URI_IMAGE.toString());
    mPrintButton = findViewById(R.id.print_btn);
    Account account = CloverAccount.getAccount(this);

    mPrintButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Order tempOrder = createAnOrder();
        int printerFlag =  PrintJob.FLAG_NONE;
        PrintJob pj = new StaticReceiptPrintJob.Builder().order(tempOrder).footerUris(Uri.parse(mEditText.getText().toString())).flag(printerFlag).build();
        pj.print(ImagePrinterActivity.this, account);
      }
    });
  }

  private Order createAnOrder() {
    Order tempOrder = new Order();
    tempOrder.setCurrency("USD");
    tempOrder.setTestMode(false);
    List<LineItem> lineItemList  = new ArrayList<>();
    LineItem item = new LineItem();
    item.setBinName("Test Order");
    item.setName("Test Item");
    item.setPrice(10000L);
    item.setPrinted(false);
    lineItemList.add(item);
    tempOrder.setLineItems(lineItemList);

    return tempOrder;
  }
}
