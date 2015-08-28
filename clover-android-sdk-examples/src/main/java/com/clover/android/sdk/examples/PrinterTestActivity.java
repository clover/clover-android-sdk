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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v1.ServiceConnector;
import com.clover.sdk.v1.ServiceException;
import com.clover.sdk.v1.printer.Category;
import com.clover.sdk.v1.printer.Printer;
import com.clover.sdk.v1.printer.PrinterConnector;
import com.clover.sdk.v1.printer.Type;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class PrinterTestActivity extends Activity implements ServiceConnector.OnServiceConnectedListener {
  private static final String TAG = "PrinterTestActivity";
  private static final int REQUEST_ACCOUNT = 0;
  private static final Random RANDOM = new Random(SystemClock.currentThreadTimeMillis());

  private PrinterConnector connector;
  private Account account;

  private TextView resultGetPrintersText;
  private TextView statusGetPrintersText;
  private Button buttonGetPrinters;

  private TextView resultGetPrintersCategoryText;
  private TextView statusGetPrintersCategoryText;
  private Button buttonGetPrintersCategory;
  private RadioGroup radioGroupGetPrintersCategory;

  private TextView resultIssetText;
  private TextView statusIssetText;
  private Button buttonIsset;

  private TextView resultGetPrinterIdText;
  private TextView statusGetPrinterIdText;
  private Button buttonGetPrinterId;
  private EditText editGetPrinterId;

  private TextView resultSetPrinterText;
  private TextView statusSetPrinterText;
  private Button buttonSetPrinter;

  private TextView statusRemovePrinterText;
  private EditText editRemovePrinter;
  private Button buttonRemovePrinter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_printer_test);

    resultGetPrintersText = (TextView) findViewById(R.id.result_get_printers);
    statusGetPrintersText = (TextView) findViewById(R.id.status_get_printers);
    buttonGetPrinters = (Button) findViewById(R.id.button_get_printers);
    buttonGetPrinters.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        connector.getPrinters(new PrinterConnector.PrinterCallback<List<Printer>>() {
          @Override
          public void onServiceSuccess(List<Printer> result, ResultStatus status) {
            super.onServiceSuccess(result, status);
            updatePrinters("get printers success", status, result);

            if (!result.isEmpty()) {
              editGetPrinterId.setText(result.get(RANDOM.nextInt(result.size())).getUuid());
            }
          }

          @Override
          public void onServiceFailure(ResultStatus status) {
            super.onServiceFailure(status);
            updatePrinters("get printers failure", status, null);
          }

          @Override
          public void onServiceConnectionFailure() {
            super.onServiceConnectionFailure();
            updatePrinters("get printers bind failure", null, null);
          }
        });
      }
    });

    resultGetPrintersCategoryText = (TextView) findViewById(R.id.result_get_printers_category);
    statusGetPrintersCategoryText = (TextView) findViewById(R.id.status_get_printers_category);
    buttonGetPrintersCategory = (Button) findViewById(R.id.button_get_printers_category);
    buttonGetPrintersCategory.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Category category;
        if (radioGroupGetPrintersCategory.getCheckedRadioButtonId() == R.id.button_receipt) {
          category = Category.RECEIPT;
        } else {
          category = Category.ORDER;
        }

        connector.getPrinters(category, new PrinterConnector.PrinterCallback<List<Printer>>() {
          @Override
          public void onServiceSuccess(List<Printer> result, ResultStatus status) {
            super.onServiceSuccess(result, status);
            updatePrintersCategory("get printers by category success", status, result);
          }

          @Override
          public void onServiceFailure(ResultStatus status) {
            super.onServiceFailure(status);
            updatePrintersCategory("get printers by category failure", status, null);
          }

          @Override
          public void onServiceConnectionFailure() {
            super.onServiceConnectionFailure();
            updatePrintersCategory("get printers by category bind failure", null, null);
          }
        });
      }
    });
    radioGroupGetPrintersCategory = (RadioGroup) findViewById(R.id.radiogroup_get_printers_category);
    radioGroupGetPrintersCategory.check(R.id.button_receipt);

    resultIssetText = (TextView) findViewById(R.id.result_isset);
    statusIssetText = (TextView) findViewById(R.id.status_isset);
    buttonIsset = (Button) findViewById(R.id.button_isset);
    buttonIsset.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        new AsyncTask<Void, Void, Pair<Boolean, Boolean>>() {

          @Override
          protected Pair<Boolean, Boolean> doInBackground(Void... voids) {
            boolean receiptSet = false;

            try {
              receiptSet = connector.isPrinterSet(Category.RECEIPT);
            } catch (RemoteException e) {
              e.printStackTrace();
            } catch (ServiceException e) {
              e.printStackTrace();
            } catch (BindingException e) {
              e.printStackTrace();
            } catch (ClientException e) {
              e.printStackTrace();
            }


            boolean orderSet = false;

            try {
              orderSet = connector.isPrinterSet(Category.ORDER);
            } catch (RemoteException e) {
              e.printStackTrace();
            } catch (ServiceException e) {
              e.printStackTrace();
            } catch (BindingException e) {
              e.printStackTrace();
            } catch (ClientException e) {
              e.printStackTrace();
            }

            return new Pair<Boolean, Boolean>(receiptSet, orderSet);
          }

          @Override
          protected void onPostExecute(Pair<Boolean, Boolean> result) {
            updateIsset("is set success", result);
          }
        }.execute();
      }
    });

    resultGetPrinterIdText = (TextView) findViewById(R.id.result_get_printer_id);
    statusGetPrinterIdText = (TextView) findViewById(R.id.status_get_printer_id);
    buttonGetPrinterId = (Button) findViewById(R.id.button_get_printer_id);
    buttonGetPrinterId.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        connector.getPrinter(editGetPrinterId.getText().toString(), new PrinterConnector.PrinterCallback<Printer>() {
          @Override
          public void onServiceSuccess(Printer result, ResultStatus status) {
            super.onServiceSuccess(result, status);
            updatePrinterId("get printer by id success", status, result);
          }

          @Override
          public void onServiceFailure(ResultStatus status) {
            super.onServiceFailure(status);
            updatePrinterId("get printer by id failure", status, null);
          }

          @Override
          public void onServiceConnectionFailure() {
            super.onServiceConnectionFailure();
            updatePrinterId("get printer by id bind failure", null, null);
          }
        });
      }
    });
    editGetPrinterId = (EditText) findViewById(R.id.edit_get_printer_id);

    resultSetPrinterText = (TextView) findViewById(R.id.result_set_printer);
    statusSetPrinterText = (TextView) findViewById(R.id.status_set_printer);
    buttonSetPrinter = (Button) findViewById(R.id.button_set_printer);
    buttonSetPrinter.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Printer p = new Printer.Builder().type(Type.STAR_TSP100_ETHERNET).name("Test printer").ip("127.0.0.1").mac("00:11:22:33:55:66").category(Category.RECEIPT).build();
        connector.setPrinter(p, new PrinterConnector.PrinterCallback<Printer>() {
          @Override
          public void onServiceSuccess(Printer result, ResultStatus status) {
            super.onServiceSuccess(result, status);
            updateSetPrinter("set printer success", status, result);
            editRemovePrinter.setText(result.getUuid());
            editRemovePrinter.setTag(result);
          }

          @Override
          public void onServiceFailure(ResultStatus status) {
            super.onServiceFailure(status);
            updateSetPrinter("set printer failure", status, null);
          }

          @Override
          public void onServiceConnectionFailure() {
            super.onServiceConnectionFailure();
            updateSetPrinter("set printer bind failure", null, null);
          }
        });
      }
    });

    statusRemovePrinterText = (TextView) findViewById(R.id.status_remove_printer);
    buttonRemovePrinter = (Button) findViewById(R.id.button_remove_printer);
    buttonRemovePrinter.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        final String id = editRemovePrinter.getText().toString();
        Printer p = (Printer) editRemovePrinter.getTag();
        if (p == null) {
          return;
        }

        connector.removePrinter(p, new PrinterConnector.PrinterCallback<Void>() {
          @Override
          public void onServiceSuccess(Void result, ResultStatus status) {
            super.onServiceSuccess(result, status);
            updateRemovePrinter("remove printer success", status);
          }

          @Override
          public void onServiceFailure(ResultStatus status) {
            super.onServiceFailure(status);
            updateRemovePrinter("remove printer failure", status);
          }

          @Override
          public void onServiceConnectionFailure() {
            super.onServiceConnectionFailure();
            updateRemovePrinter("remove printer bind failure", null);
          }
        });
      }
    });
    editRemovePrinter = (EditText) findViewById(R.id.edit_remove_printer);

  }

  private static String getStatusString(String status, ResultStatus resultStatus) {
    return "<" + status + " " + (resultStatus != null ? resultStatus : "") + ": " + DateFormat.getDateTimeInstance().format(new Date()) + ">";
  }

  private void updateRemovePrinter(String status, ResultStatus resultStatus) {
    statusRemovePrinterText.setText(getStatusString(status, resultStatus));
  }

  private void updateSetPrinter(String status, ResultStatus resultStatus, Printer printer) {
    statusSetPrinterText.setText(getStatusString(status, resultStatus));
    if (printer == null) {
      resultSetPrinterText.setText("");
    } else {
      resultSetPrinterText.setText(printer.toString());
    }
  }

  private void updatePrinterId(String status, ResultStatus resultStatus, Printer printer) {
    statusGetPrinterIdText.setText(getStatusString(status, resultStatus));
    if (printer == null) {
      resultGetPrinterIdText.setText("");
    } else {
      resultGetPrinterIdText.setText(printer.toString());
    }
  }

  private void updateIsset(String status, Pair<Boolean, Boolean> result) {
    statusIssetText.setText(getStatusString(status, null));
    if (result == null) {
      resultIssetText.setText("");
    } else {
      resultIssetText.setText(String.format("receipt set? %b, order set? %b", result.first, result.second));
    }
  }

  @Override
  protected void onResume() {
    super.onResume();

    if (account != null) {
      connect();
    } else {
      startAccountChooser();
    }
  }

  private void startAccountChooser() {
    Intent intent = AccountManager.newChooseAccountIntent(null, null, new String[]{CloverAccount.CLOVER_ACCOUNT_TYPE}, false, null, null, null, null);
    startActivityForResult(intent, REQUEST_ACCOUNT);
  }

  @Override
  protected void onPause() {
    disconnect();
    super.onPause();
  }

  private void connect() {
    disconnect();
    if (account != null) {
      connector = new PrinterConnector(this, account, this);
      connector.connect();
    }
  }

  private void disconnect() {
    if (connector != null) {
      connector.disconnect();
      connector = null;
    }
  }

  @Override
  public void onServiceConnected(ServiceConnector connector) {
    Log.i(TAG, "service connected: " + connector);
  }

  @Override
  public void onServiceDisconnected(ServiceConnector connector) {
    Log.i(TAG, "service disconnected: " + connector);
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

  private void updatePrinters(String status, ResultStatus resultStatus, List<Printer> result) {
    statusGetPrintersText.setText("<" + status + " " + (resultStatus != null ? resultStatus : "") + ": " + DateFormat.getDateTimeInstance().format(new Date()) + ">");
    if (result == null) {
      resultGetPrintersText.setText("");
    } else {
      resultGetPrintersText.setText(result.toString());
    }
  }

  private void updatePrintersCategory(String status, ResultStatus resultStatus, List<Printer> result) {
    statusGetPrintersCategoryText.setText("<" + status + " " + (resultStatus != null ? resultStatus : "") + ": " + DateFormat.getDateTimeInstance().format(new Date()) + ">");
    if (result == null) {
      resultGetPrintersCategoryText.setText("");
    } else {
      resultGetPrintersCategoryText.setText(result.toString());
    }
  }
}
