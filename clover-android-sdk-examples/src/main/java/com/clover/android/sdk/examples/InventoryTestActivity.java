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
import android.app.Activity;
import android.app.LoaderManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.util.CloverAuth;
import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ForbiddenException;
import com.clover.sdk.v1.Intents;
import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v1.ServiceCallback;
import com.clover.sdk.v1.ServiceConnector;
import com.clover.sdk.v1.ServiceException;
import com.clover.sdk.v3.inventory.Attribute;
import com.clover.sdk.v3.inventory.Category;
import com.clover.sdk.v3.inventory.Discount;
import com.clover.sdk.v3.inventory.IInventoryService;
import com.clover.sdk.v3.inventory.InventoryConnector;
import com.clover.sdk.v3.inventory.InventoryContract;
import com.clover.sdk.v3.inventory.InventoryIntent;
import com.clover.sdk.v3.inventory.Item;
import com.clover.sdk.v3.inventory.ItemGroup;
import com.clover.sdk.v3.inventory.Modifier;
import com.clover.sdk.v3.inventory.ModifierGroup;
import com.clover.sdk.v3.inventory.Option;
import com.clover.sdk.v3.inventory.OptionItem;
import com.clover.sdk.v3.inventory.PriceType;
import com.clover.sdk.v3.inventory.Tag;
import com.clover.sdk.v3.inventory.TaxRate;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class InventoryTestActivity extends Activity {
  private static final String TAG = InventoryTestActivity.class.getSimpleName();

  private enum ConnectionMethod {
    BOUND_SERVICE, SERVICE_CONNECTOR, SERVICE_CONNECTOR_USING_CALLBACKS,
    WEB_SERVICE, CONTENT_PROVIDER_AND_SERVICE_WRAPPER
  }

  // use this if connected to web service or bound service
  private IInventoryService inventoryService;

  // this applies only to bound service
  private ServiceConnection serviceConnection;

  // this applies only to service wrapper
  private InventoryConnector inventoryConnector;

  // this applies only when using content provider
  private ItemLoader itemLoader = null;

  // this is used for all connection methods
  private boolean serviceIsBound = false;

  // Change this variable if you want to use a different connection method
  private ConnectionMethod connectionMethod = ConnectionMethod.SERVICE_CONNECTOR;

  private TextView resultText;
  private TextView statusText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_inventory_test);

    resultText = (TextView) findViewById(R.id.inventoryResult);
    statusText = (TextView) findViewById(R.id.inventoryStatus);
  }

  @Override
  protected void onResume() {
    super.onResume();
    switch (connectionMethod) {
      case SERVICE_CONNECTOR:
      case SERVICE_CONNECTOR_USING_CALLBACKS:
        connectToServiceConnector();
        break;
      case BOUND_SERVICE:
        connectToInventoryService();
        break;
      case WEB_SERVICE:
        connectToInventoryWebService();
        break;
      case CONTENT_PROVIDER_AND_SERVICE_WRAPPER:
        connectToInventoryContentProviderAndServiceWrapper();
        break;
    }
  }

  private void connectToInventoryContentProviderAndServiceWrapper() {
    inventoryConnector = new InventoryConnector(this, CloverAccount.getAccount(this), new ServiceConnector.OnServiceConnectedListener() {
      @Override
      public void onServiceConnected(ServiceConnector connector) {
        serviceIsBound = true;
      }

      @Override
      public void onServiceDisconnected(ServiceConnector connector) {
        serviceIsBound = false;
      }
    });
    inventoryConnector.connect();

    if (itemLoader != null) {
      itemLoader.restart();
    } else {
      itemLoader = new ItemLoader(this);
      itemLoader.init();
    }
  }

  private void connectToServiceConnector() {
    inventoryConnector = new InventoryConnector(this, CloverAccount.getAccount(this), new ServiceConnector.OnServiceConnectedListener() {
      @Override
      public void onServiceConnected(ServiceConnector connector) {
        serviceIsBound = true;
        statusText.setText("Connected to service via service wrapper");
        if (connectionMethod == ConnectionMethod.SERVICE_CONNECTOR_USING_CALLBACKS) {
          fetchItemsFromServiceConnectorUsingCallbacks();
        } else {
          fetchObjectsFromServiceConnector();
        }
      }

      @Override
      public void onServiceDisconnected(ServiceConnector connector) {
        statusText.setText("Disconnected from service via wrapper");
        serviceIsBound = false;
      }
    });
    inventoryConnector.connect();
  }

  private void connectToInventoryWebService() {
    new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground(Void... params) {
        try {
          Account account = CloverAccount.getAccount(InventoryTestActivity.this);
          CloverAuth.AuthResult authResult = CloverAuth.authenticate(InventoryTestActivity.this, account);
          String baseUrl = authResult.authData.getString(CloverAccount.KEY_BASE_URL);

          if (authResult.authToken != null && baseUrl != null) {
            CustomHttpClient httpClient = CustomHttpClient.getHttpClient();
            String getNameUri = "/v2/merchant/name";
            String url = baseUrl + getNameUri + "?access_token=" + authResult.authToken;
            String result = httpClient.get(url);
            JSONTokener jsonTokener = new JSONTokener(result);
            JSONObject root = (JSONObject) jsonTokener.nextValue();
            String merchantId = root.getString("merchantId");
            inventoryService = new InventoryWebService(merchantId, authResult.authToken, baseUrl);
          }
        } catch (Exception e) {
          Log.e(TAG, "Error retrieving merchant info from server", e);
        }
        return null;
      }

      @Override
      protected void onPostExecute(Void aVoid) {
        if (inventoryService != null) {
          statusText.setText("using web service");
          serviceIsBound = true;
          fetchItemsFromService();
        } else {
          statusText.setText("failed to connect to web service");
        }
      }
    }.execute();
  }

  private void connectToInventoryService() {
    serviceConnection = new ServiceConnection() {
      public void onServiceConnected(ComponentName className, IBinder service) {
        inventoryService = IInventoryService.Stub.asInterface(service);
        serviceIsBound = true;
        statusText.setText("connected to AIDL service");
        fetchItemsFromService();
        createObjectsUsingService();
      }

      public void onServiceDisconnected(ComponentName className) {
        statusText.setText("disconnected from AIDL service");
        inventoryService = null;
        serviceIsBound = false;
      }
    };

    Account account = CloverAccount.getAccount(this);
    Intent intent = new Intent(InventoryIntent.ACTION_INVENTORY_SERVICE_V3);
    intent.putExtra(Intents.EXTRA_ACCOUNT, account);
    bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
  }

  // This should work for both bound AIDL service and web service
  private void fetchItemsFromService() {
    if (serviceIsBound && inventoryService != null) {
      new AsyncTask<Void, Void, Item>() {
        @Override
        protected Item doInBackground(Void... params) {
          Item item = null;
          try {
            ResultStatus getItemsStatus = new ResultStatus();
            List<Item> items = inventoryService.getItems(getItemsStatus);
            Log.i(TAG, "Received result from getItems(): " + getItemsStatus);
            if (getItemsStatus.isSuccess()) {
              for (Item i : items) {
                Log.i(TAG, "item = " + dumpItem(i));
              }
            }
            if (items != null && items.size() > 0) {
              String itemId = items.get(0).getId();
              ResultStatus getItemStatus = new ResultStatus();
              item = inventoryService.getItem(itemId, getItemStatus);
              Log.i(TAG, "Received result from getItem(): " + getItemStatus);
            }

            ResultStatus resultStatus = new ResultStatus();
            List<Category> categories = inventoryService.getCategories(resultStatus);
            if (resultStatus.isSuccess()) {
              for (Category category : categories) {
                Log.i(TAG, "category = " + dumpCategory(category));
              }
            } else {
              Log.i(TAG, "Couldn't retrieve categories: " + resultStatus);
            }
            List<TaxRate> taxRates = inventoryService.getTaxRates(resultStatus);
            if (resultStatus.isSuccess()) {
              for (TaxRate taxRate : taxRates) {
                Log.i(TAG, "tax rate = " + dumpTaxRate(taxRate));
              }
            } else {
              Log.i(TAG, "Couldn't retrieve tax rates: " + resultStatus);
            }
          } catch (RemoteException e) {
            Log.e(TAG, "Error calling inventory service", e);
          }
          return item;
        }

        @Override
        protected void onPostExecute(Item result) {
          displayItem(result);
        }
      }.execute();
    }
  }

  private void fetchItemsFromServiceConnectorUsingCallbacks() {
    if (serviceIsBound && inventoryConnector != null) {
      inventoryConnector.getItems(new ServiceCallback<List<Item>>() {

        @Override
        public void onServiceSuccess(List<Item> items, ResultStatus status) {
          if (items != null && items.size() > 0) {
            String itemId = items.get(0).getId();
            inventoryConnector.getItem(itemId, new ServiceCallback<Item>() {
              @Override
              public void onServiceSuccess(Item result, ResultStatus resultStatus) {
                displayItem(result);
              }

              @Override
              public void onServiceFailure(ResultStatus resultStatus) {
                Log.e(TAG, "Error calling getItem()");
              }

              @Override
              public void onServiceConnectionFailure() {
                Log.e(TAG, "Error calling getItem() - couldn't connect to service");
              }
            });
          }
        }

        @Override
        public void onServiceFailure(ResultStatus status) {
          Log.e(TAG, "Error calling getItems()");
        }

        @Override
        public void onServiceConnectionFailure() {
          Log.e(TAG, "Error binding to inventory service");
        }
      });

      inventoryConnector.getCategories(new ServiceCallback<List<Category>>() {
        @Override
        public void onServiceSuccess(List<Category> result, ResultStatus resultStatus) {
          for (Category category : result) {
            Log.v(TAG, "category = " + dumpCategory(category));
          }
        }

        @Override
        public void onServiceFailure(ResultStatus resultStatus) {
          Log.e(TAG, "Error calling getCategories()");
        }
      });

      inventoryConnector.getTaxRates(new ServiceCallback<List<TaxRate>>() {
        @Override
        public void onServiceSuccess(List<TaxRate> result, ResultStatus status) {
          for (TaxRate taxRate : result) {
            Log.v(TAG, "tax rate = " + dumpTaxRate(taxRate));
          }
        }

        @Override
        public void onServiceFailure(ResultStatus status) {
          Log.e(TAG, "Error calling getCategories()");
        }
      });
    }
  }

  private void fetchObjectsFromServiceConnector() {
    if (serviceIsBound && inventoryConnector != null) {
      new AsyncTask<Void, Void, Item>() {
        @Override
        protected Item doInBackground(Void... params) {
          Item item = null;
          try {
            List<String> items = inventoryConnector.getItemIds();
            if (items != null) {
              for (int i = 0; i < items.size(); i++) {
                String itemId = items.get(i);
                // just print out the first few to the console
                if (i > 10) {
                  break;
                }
                item = inventoryConnector.getItem(itemId);
                Log.v(TAG, "item = " + dumpItem(item));
              }

              // fetch tax rates, categories, modifier groups and modifiers and just print them to log for now
              List<Category> categories = inventoryConnector.getCategories();
              if (categories != null) {
                for (Category category : categories) {
                  Log.v(TAG, "category = " + dumpCategory(category));
                }
              }
              List<TaxRate> taxRates = inventoryConnector.getTaxRates();
              if (taxRates != null) {
                for (TaxRate taxRate : taxRates) {
                  Log.v(TAG, "tax rate = " + dumpTaxRate(taxRate));
                }
              }
              List<ModifierGroup> modifierGroups = inventoryConnector.getModifierGroups();
              if (modifierGroups != null) {
                for (ModifierGroup modifierGroup : modifierGroups) {
                  Log.v(TAG, "modifier group = " + dumpModifierGroup(modifierGroup));
                  List<Modifier> modifiers = inventoryConnector.getModifiers(modifierGroup.getId());
                  if (modifiers != null) {
                    for (Modifier modifier : modifiers) {
                      Log.v(TAG, "modifier = " + dumpModifier(modifier));
                    }
                  }
                }
              }
            }
          } catch (ForbiddenException e) {
            Log.e(TAG, "Auth exception", e);
          } catch (ClientException e) {
            Log.e(TAG, "Client exception", e);
          } catch (ServiceException e) {
            Log.e(TAG, "Service exception", e);
          } catch (BindingException e) {
            Log.e(TAG, "Error calling inventory service", e);
          } catch (RemoteException e) {
            Log.e(TAG, "Error calling inventory service", e);
          }
          return item;
        }

        @Override
        protected void onPostExecute(Item result) {
          displayItem(result);
        }
      }.execute();
    }
  }

  private void createObjectsUsingService() {
    if (serviceIsBound && inventoryService != null) {
      new AsyncTask<Void, Void, List<Item>>() {
        @SuppressWarnings("ConstantConditions")
        @Override
        protected List<Item> doInBackground(Void... params) {
          List<Item> items = new ArrayList<Item>();
          try {
            ResultStatus resultStatus = new ResultStatus();

            // Attempt #1
            Item newItem = new Item().setName("Test Item #1").setPrice(500L).setPriceType(PriceType.FIXED).setDefaultTaxRates(true);
            newItem = inventoryService.createItem(newItem, resultStatus);
            if (newItem != null) {
              items.add(newItem);
            }
            Log.v(TAG, "Result from createItem() #1 = " + resultStatus + ", item = " + dumpItem(newItem));

            // Attempt #2, missing data (item name) should be caught by Item() constructor
            try {
              Item newItemFailure = new Item().setPrice(500L).setPriceType(PriceType.FIXED).setDefaultTaxRates(true);
              newItemFailure = inventoryService.createItem(newItemFailure, resultStatus);
              if (newItemFailure != null) {
                items.add(newItemFailure);
              }
              Log.v(TAG, "Result from createItem() #2 = " + resultStatus + ", item = " + dumpItem(newItemFailure));
            } catch (Exception e) {
              Log.v(TAG, "Result from createItem() #2, where we were missing required field: " + e.getMessage());
            }

            // Attempt #3, invalid data (price too low) should be caught by Item() constructor
            try {
              Item newItemFailure2 = new Item().setName("Test Item 3").setPrice(-500L).setPriceType(PriceType.FIXED).setDefaultTaxRates(true);
              newItemFailure2 = inventoryService.createItem(newItemFailure2, resultStatus);
              if (newItemFailure2 != null) {
                items.add(newItemFailure2);
              }
              Log.v(TAG, "Result from createItem() #3 = " + resultStatus + ", item = " + dumpItem(newItemFailure2));
            } catch (Exception e) {
              Log.v(TAG, "Result from createItem() #3, where we set an invalid value: " + e.getMessage());
            }

            // Attempt #4, using server's constructor
            Item newItemWithId = new Item().setId("XXXXXXXXXXXXX").setName("Test Item 4").setPrice(500L).setPriceType(PriceType.FIXED).setDefaultTaxRates(true);
            newItemWithId = inventoryService.createItem(newItemWithId, resultStatus);
            if (newItemWithId != null) {
              items.add(newItemWithId);
            }
            Log.v(TAG, "Result from createItem() #4 = " + resultStatus + ", item = " + dumpItem(newItemWithId));

            // Attempt #5, maliciously manipulating data to be invalid
            String badJson = "{\"name\": \"Way Too Long Name..............................................................................................................................................\",\"price\": 600,\"priceType\": \"FIXED\",\"taxable\": true}";
            Item newItemBadJson = new Item(badJson);
            newItemBadJson = inventoryService.createItem(newItemBadJson, resultStatus);
            if (newItemBadJson != null) {
              items.add(newItemBadJson);
            }
            Log.v(TAG, "Result from createItem() #5 = " + resultStatus + ", item = " + newItemBadJson);
          } catch (Exception e) {
            Log.e(TAG, "Error calling inventory service", e);
          }
          return items;
        }

        @Override
        protected void onPostExecute(List<Item> results) {
          for (Item result : results) {
            displayItem(result);
          }
        }
      }.execute();
    }
  }

  private void displayItem(Item item) {
    if (item != null) {
      String textViewContents = "";
      CharSequence text = resultText.getText();
      if (text != null) {
        textViewContents = text.toString();
      }
      resultText.setText(textViewContents + "\nitem = " + dumpItem(item));
    }
  }

  private String dumpItem(Item item) {
    return item != null ? String.format("%s{id=%s, name=%s, price=%d}", Item.class.getSimpleName(), item.getId(), item.getName(), item.getPrice()) : null;
  }

  private String dumpTaxRate(TaxRate taxRate) {
    return String.format("%s{id=%s, name=%s, rate=%d, is default=%b}", TaxRate.class.getSimpleName(), taxRate.getId(), taxRate.getName(), taxRate.getRate(), taxRate.getIsDefault());
  }

  private String dumpModifier(Modifier modifier) {
    return String.format("%s{id=%s, name=%s}", Modifier.class.getSimpleName(), modifier.getId(), modifier.getName());
  }

  private String dumpModifierGroup(ModifierGroup modifierGroup) {
    return String.format("%s{id=%s, name=%s}", ModifierGroup.class.getSimpleName(), modifierGroup.getId(), modifierGroup.getName());
  }

  private String dumpCategory(Category category) {
    return String.format("%s{id=%s, name=%s, sort order=%d}", Category.class.getSimpleName(), category.getId(), category.getName(), category.getSortOrder());
  }

  @Override
  protected void onStop() {
    super.onStop();
  }

  @Override
  protected void onPause() {
    switch (connectionMethod) {
      case SERVICE_CONNECTOR:
      case SERVICE_CONNECTOR_USING_CALLBACKS:
        if (inventoryConnector != null) {
          inventoryConnector.disconnect();
          inventoryConnector = null;
        }
        break;
      case BOUND_SERVICE:
        if (serviceIsBound && serviceConnection != null) {
          unbindService(serviceConnection);
          serviceIsBound = false;
        }
        break;
      case WEB_SERVICE:
        break;
      case CONTENT_PROVIDER_AND_SERVICE_WRAPPER:
        // destroy cursor loader
        if (itemLoader != null) {
          itemLoader.destroy();
        }
        break;
    }

    if (serviceIsBound && serviceConnection != null) {
      unbindService(serviceConnection);
      serviceIsBound = false;
    }

    super.onPause();
  }

  private class ItemLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ITEM_LOADER_ID = 1;

    private final Activity activity;
    private final Account account;

    public ItemLoader(Activity activity) {
      this.account = CloverAccount.getAccount(activity);
      this.activity = activity;
    }

    public void init() {
      activity.getLoaderManager().initLoader(ITEM_LOADER_ID, null, this);
    }

    public void destroy() {
      activity.getLoaderManager().destroyLoader(ITEM_LOADER_ID);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
      switch (id) {
        case ITEM_LOADER_ID:
          return new CursorLoader(activity, InventoryContract.Item.contentUriWithAccount(account), null, null,
              null, InventoryContract.Item.NAME);
      }
      throw new IllegalArgumentException("Unknown loader ID");
    }

    @Override
    public final void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
      switch (loader.getId()) {
        case ITEM_LOADER_ID:
          statusText.setText("Connected to content provider");
          // We're only going to get the first result from the cursor but most usages would presumably
          // use a CursorAdapter or similar mechanism to fetch some range of data from the cursor
          if (cursor != null) {
            if (cursor.moveToFirst()) {
              try {
                Item item = getItemFromCursor(cursor);
                Log.d(TAG, "Retrieved item from cursor: " + dumpItem(item));
                // now fetch the full item with modifiers, categories, tax rates, etc. from the service
                if (serviceIsBound && inventoryConnector != null) {
                  inventoryConnector.getItem(item.getId(), new ServiceCallback<Item>() {
                    @Override
                    public void onServiceSuccess(Item result, ResultStatus status) {
                      displayItem(result);
                    }

                    @Override
                    public void onServiceFailure(ResultStatus status) {
                      Log.e(TAG, "Error calling getItem() on service wrapper");
                    }

                    @Override
                    public void onServiceConnectionFailure() {
                      Log.e(TAG, "Error binding to service wrapper");
                    }
                  });
                }
              } catch (Exception e) {
                Log.e(TAG, "Error retrieving item", e);
              }
            }
          }
          break;
      }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
      // we're not binding to an adapter or hanging on to the cursor, so no need to do anything here
    }

    public void restart() {
      activity.getLoaderManager().restartLoader(ITEM_LOADER_ID, null, this);
    }

    @SuppressWarnings("ConstantConditions")
    private Item getItemFromCursor(Cursor cursor) throws JSONException {
      int uuidIndex = cursor.getColumnIndex(InventoryContract.Item.ID);
      int nameIndex = cursor.getColumnIndex(InventoryContract.Item.NAME);
      int alternateNameIndex = cursor.getColumnIndex(InventoryContract.Item.ALTERNATE_NAME);
      int codeIndex = cursor.getColumnIndex(InventoryContract.Item.CODE);
      int unitNameIndex = cursor.getColumnIndex(InventoryContract.Item.UNIT_NAME);
      int priceIndex = cursor.getColumnIndex(InventoryContract.Item.PRICE);
      int priceTypeIndex = cursor.getColumnIndex(InventoryContract.Item.PRICE_TYPE);
      int defaultTaxRatesIndex = cursor.getColumnIndex(InventoryContract.Item.DEFAULT_TAX_RATES);

      String id = null, name = null, alternateName = null, unitName = null, code = null;
      Integer priceTypeInt, defaultTaxRatesInt;
      PriceType priceType = null;
      Long price = null;
      Boolean defaultTaxRates = null;
      Long cost = null; // not currently stored in local db

      if (uuidIndex >= 0) {
        id = cursor.getString(uuidIndex);
      }
      if (nameIndex >= 0) {
        name = cursor.getString(nameIndex);
      }
      if (alternateNameIndex >= 0) {
        alternateName = cursor.getString(alternateNameIndex);
      }
      if (codeIndex >= 0) {
        code = cursor.getString(codeIndex);
      }
      if (unitNameIndex >= 0) {
        unitName = cursor.getString(unitNameIndex);
      }
      if (priceIndex >= 0) {
        price = cursor.getLong(priceIndex);
      }
      if (priceTypeIndex >= 0) {
        priceTypeInt = cursor.getInt(priceTypeIndex);
        priceType = PriceType.values()[priceTypeInt];
      }
      if (defaultTaxRatesIndex >= 0) {
        defaultTaxRatesInt = cursor.getInt(defaultTaxRatesIndex);
        defaultTaxRates = defaultTaxRatesInt != null && defaultTaxRatesInt == 1;
      }

      return new Item().setId(id).setName(name).setAlternateName(alternateName).setCode(code).setPrice(price).setPriceType(priceType).setDefaultTaxRates(defaultTaxRates).setUnitName(unitName).setCost(cost);
    }
  }

  static class InventoryWebService implements IInventoryService {
    private static final String TAG = InventoryWebService.class.getSimpleName();

    @Override
    public List<Discount> getDiscounts(ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement getDiscounts()");
    }

    @Override
    public Discount getDiscount(String discountId, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement getDiscount()");
    }

    @Override
    public Discount createDiscount(Discount discount, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement createDiscount()");
    }

    @Override
    public void updateDiscount(Discount discount, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement updateDiscount()");
    }

    @Override
    public void deleteDiscount(String discountId, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement deleteDiscount()");
    }

    @Override
    public void addItemToCategory(String itemId, String categoryId, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement addItemToCategory()");
    }

    @Override
    public void removeItemFromCategory(String itemId, String categoryId, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement removeItemFromCategory()");
    }

    @Override
    public void moveItemInCategoryLayout(String itemId, String categoryId, int direction, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement moveItemInCategoryLayout()");
    }

    @Override
    public void assignModifierGroupToItem(String modifierGroupId, String itemId, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement assignModifierGroupToItem()");
    }

    @Override
    public void removeModifierGroupFromItem(String modifierGroupId, String itemId, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement removeModifierGroupFromItem()");
    }

    @Override
    public TaxRate createTaxRate(TaxRate taxRate, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement createTaxRate()");
    }

    @Override
    public void updateTaxRate(TaxRate taxRate, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement updateTaxRate()");
    }

    @Override
    public void deleteTaxRate(String taxRateId, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement deleteTaxRate()");
    }

    private String merchantId;
    private String accessToken;
    private String baseUrl;

    public InventoryWebService(String merchantId, String accessToken, String baseUrl) {
      this.merchantId = merchantId;
      this.accessToken = accessToken;
      this.baseUrl = baseUrl;
    }

    @Override
    public List<Item> getItems(ResultStatus resultStatus) throws RemoteException {
      String uri = "/v2/merchant/" + merchantId + "/inventory/items";
      return getArrayResults(Item.class, uri, "items", resultStatus);
    }

    @Override
    public List<Item> getItemsWithCategories(ResultStatus resultStatus) throws RemoteException {
      String uri = "/v2/merchant/" + merchantId + "/inventory/items_with_categories";
      return getArrayResults(Item.class, uri, "items", resultStatus);
    }

    @Override
    public List<String> getItemIds(ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("getItemIds() not supported through web service API");
    }

    @Override
    public Item getItem(String itemId, ResultStatus resultStatus) throws RemoteException {
      String uri = "/v2/merchant/" + merchantId + "/inventory/items/" + itemId;
      return getResult(Item.class, uri, "item", resultStatus);
    }

    @Override
    public Item getItemWithCategories(String itemId, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("getItemWithCategories() not supported through web service API");
    }

    @Override
    public Item createItem(Item item, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement createItem()");
    }

    @Override
    public void updateItem(Item item, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement updateItem()");
    }

    @Override
    public void deleteItem(String itemId, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement deleteItem()");
    }

    @Override
    public List<TaxRate> getTaxRatesForItem(String itemId, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("getTaxRatesForItem() not supported through web service API");
    }

    @Override
    public void assignTaxRatesToItem(String itemId, List<String> taxRates, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement assignTaxRatesToItem()");
    }

    @Override
    public void removeTaxRatesFromItem(String itemId, List<String> taxRates, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement removeTaxRatesFromItem()");
    }

    @Override
    public TaxRate getTaxRate(String taxRateId, ResultStatus resultStatus) throws RemoteException {
      String uri = "/v2/merchant/" + merchantId + "/tax_rates/" + taxRateId;
      return getResult(TaxRate.class, uri, "taxRate", resultStatus);
    }

    @Override
    public List<TaxRate> getTaxRates(ResultStatus resultStatus) throws RemoteException {
      String uri = "/v2/merchant/" + merchantId + "/tax_rates";
      return getArrayResults(TaxRate.class, uri, "taxRates", resultStatus);
    }

    @Override
    public List<Category> getCategories(ResultStatus resultStatus) throws RemoteException {
      String uri = "/v2/merchant/" + merchantId + "/inventory/categories";
      return getArrayResults(Category.class, uri, "categories", resultStatus);
    }

    private <T> T getResult(Class<T> dataClass, String uri, String jsonFieldName, ResultStatus resultStatus) throws RemoteException {
      CustomHttpClient httpClient = CustomHttpClient.getHttpClient();

      try {
        String url = baseUrl + uri + "?access_token=" + accessToken;
        String result = httpClient.get(url);
        if (!TextUtils.isEmpty(result)) {
          JSONTokener jsonTokener = new JSONTokener(result);
          JSONObject root = (JSONObject) jsonTokener.nextValue();
          JSONObject jsonObject = root.getJSONObject(jsonFieldName);
          if (jsonObject != null) {
            Constructor<T> constructor = dataClass.getConstructor(JSONObject.class);
            return constructor.newInstance(jsonObject);
          }
        }
      } catch (Exception e) {
        resultStatus.setStatus(ResultStatus.SERVICE_ERROR, "Error retrieving result from server");
        Log.e(TAG, "Error retrieving result from server", e);
        throw new RemoteException();
      }

      resultStatus.setStatusCode(ResultStatus.NOT_FOUND);
      return null;
    }

    private <T> List<T> getArrayResults(Class<T> dataClass, String uri, String jsonFieldName, ResultStatus resultStatus) throws RemoteException {
      CustomHttpClient httpClient = CustomHttpClient.getHttpClient();

      try {
        List<T> results = new ArrayList<T>();
        String url = baseUrl + uri + "?access_token=" + accessToken;
        String result = httpClient.get(url);
        if (!TextUtils.isEmpty(result)) {
          JSONTokener jsonTokener = new JSONTokener(result);
          JSONObject root = (JSONObject) jsonTokener.nextValue();
          JSONArray jsonArray = root.getJSONArray(jsonFieldName);
          for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Constructor<T> constructor = dataClass.getConstructor(JSONObject.class);
            T obj = constructor.newInstance(jsonObject);
            results.add(obj);
          }
        }
        resultStatus.setStatusCode(ResultStatus.OK);
        return results;
      } catch (Exception e) {
        resultStatus.setStatus(ResultStatus.SERVICE_ERROR, "Error retrieving result from server");
        Log.e(TAG, "Error retrieving result from server", e);
        throw new RemoteException();
      }
    }

    @Override
    public Category createCategory(Category category, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement createCategory()");
    }

    @Override
    public void updateCategory(Category category, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement updateCategory()");
    }

    @Override
    public void deleteCategory(String categoryId, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement deleteCategory()");
    }

    @Override
    public List<ModifierGroup> getModifierGroups(ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement getModifierGroups()");
    }

    @Override
    public ModifierGroup createModifierGroup(ModifierGroup group, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement createModifierGroup()");
    }

    @Override
    public void updateModifierGroup(ModifierGroup group, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement updateModifierGroup()");
    }

    @Override
    public void deleteModifierGroup(String groupId, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement deleteModifierGroup()");
    }

    @Override
    public List<Modifier> getModifiers(String modifierGroupId, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement getModifiers()");
    }

    @Override
    public Modifier createModifier(String modifierGroupId, Modifier modifier, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement createModifier()");
    }

    @Override
    public void updateModifier(Modifier modifier, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement updateModifier()");
    }

    @Override
    public void deleteModifier(String modifierId, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement deleteModifier()");
    }

    @Override
    public List<ModifierGroup> getModifierGroupsForItem(String itemId, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement getModifierGroupsForItem()");
    }

    @Override
    public List<Tag> getTags(ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement getTags()");
    }

    @Override
    public Tag getTag(String tagId, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement getTag()");
    }

    @Override
    public Tag createTag(Tag tag, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement createTag()");
    }

    @Override
    public void updateTag(Tag tag, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement updateTag()");
    }

    @Override
    public void deleteTag(String tagId, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement deleteTag()");
    }

    @Override
    public List<Tag> getTagsForItem(String itemId, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement getTagsForItem()");
    }

    @Override
    public void assignTagsToItem(String itemId, List<String> tags, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement assignTagsToItem()");
    }

    @Override
    public void removeTagsFromItem(String itemId, List<String> tags, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement removeTagsFromItem()");
    }

    @Override
    public void assignItemsToTag(String tagId, List<String> items, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement assignItemsToTag()");
    }

    @Override
    public void removeItemsFromTag(String tagId, List<String> items, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement removeItemsFromTag()");
    }

    @Override
    public List<Tag> getTagsForPrinter(String printerMac, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement getTagsForPrinter()");
    }

    @Override
    public void assignTagsToPrinter(String printerMac, List<String> tags, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement assignTagsToPrinter()");
    }

    @Override
    public void removeTagsFromPrinter(String printerMac, List<String> tags, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement removeTagsFromPrinter()");
    }

    @Override
    public void updateModifierSortOrder(String modifierGroupId, List<String> modifierIds, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement updateModifierSortOrder()");
    }

    @Override
    public List<Attribute> getAttributes(ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement getAttributes()");
    }

    @Override
    public Attribute getAttribute(String attributeId, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement getAttribute()");
    }

    @Override
    public Attribute createAttribute(Attribute attribute, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement createAttribute()");
    }

    @Override
    public void updateAttribute(Attribute attribute, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement updateAttribute()");
    }

    @Override
    public void deleteAttribute(String attributeId, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement deleteAttribute()");
    }

    @Override
    public List<Option> getOptions(ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement getOptions()");
    }

    @Override
    public Option getOption(String optionId, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement getOption()");
    }

    @Override
    public Option createOption(Option option, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement createOption()");
    }

    @Override
    public void updateOption(Option option, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement updateOption()");
    }

    @Override
    public void deleteOption(String optionId, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement deleteOption()");
    }

    @Override
    public List<Option> getOptionsForItem(String itemId, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement getOptionsForItem()");
    }

    @Override
    public void assignOptionsToItem(String itemId, List<String> optionIds, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement assignOptionsToItem()");
    }

    @Override
    public void removeOptionsFromItem(String itemId, List<String> optionIds, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement removeOptionsFromItem()");
    }

    @Override
    public void updateItemStock(String itemId, long stockCount, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement updateItemStock()");
    }

    @Override
    public void removeItemStock(String itemId, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement removeItemStock()");
    }

    @Override
    public ItemGroup getItemGroup(String itemGroupId, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement getItemGroup()");
    }

    @Override
    public ItemGroup createItemGroup(ItemGroup itemGroup, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement createItemGroup()");
    }

    @Override
    public void updateItemGroup(ItemGroup itemGroup, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement updateItemGroup()");
    }

    @Override
    public void deleteItemGroup(String itemGroupId, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement deleteItemGroup()");
    }

    @Override
    public void updateItemStockQuantity(String itemId, double quantity, ResultStatus resultStatus) throws RemoteException {
      throw new UnsupportedOperationException("Need to implement updateItemStockQuantity()");
    }

    @Override
    public IBinder asBinder() {
      throw new UnsupportedOperationException("Not a real Android service");
    }
  }

  static class CustomHttpClient extends DefaultHttpClient {
    private static final int CONNECT_TIMEOUT = 60000;
    private static final int READ_TIMEOUT = 60000;
    private static final int MAX_TOTAL_CONNECTIONS = 5;
    private static final int MAX_CONNECTIONS_PER_ROUTE = 3;
    private static final int SOCKET_BUFFER_SIZE = 8192;
    private static final boolean FOLLOW_REDIRECT = false;
    private static final boolean STALE_CHECKING_ENABLED = true;
    private static final String CHARSET = HTTP.UTF_8;
    private static final HttpVersion HTTP_VERSION = HttpVersion.HTTP_1_1;
    private static final String USER_AGENT = "CustomHttpClient"; // + version

    public static CustomHttpClient getHttpClient() {
      CustomHttpClient httpClient = new CustomHttpClient();
      final HttpParams params = httpClient.getParams();
      HttpProtocolParams.setUserAgent(params, USER_AGENT);
      HttpProtocolParams.setContentCharset(params, CHARSET);
      HttpProtocolParams.setVersion(params, HTTP_VERSION);
      HttpClientParams.setRedirecting(params, FOLLOW_REDIRECT);
      HttpConnectionParams.setConnectionTimeout(params, CONNECT_TIMEOUT);
      HttpConnectionParams.setSoTimeout(params, READ_TIMEOUT);
      HttpConnectionParams.setSocketBufferSize(params, SOCKET_BUFFER_SIZE);
      HttpConnectionParams.setStaleCheckingEnabled(params, STALE_CHECKING_ENABLED);
      ConnManagerParams.setTimeout(params, CONNECT_TIMEOUT);
      ConnManagerParams.setMaxTotalConnections(params, MAX_TOTAL_CONNECTIONS);
      ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRouteBean(MAX_CONNECTIONS_PER_ROUTE));

      return httpClient;
    }

    public String get(String url) throws IOException, HttpException {
      String result;
      HttpGet request = new HttpGet(url);
      HttpResponse response = execute(request);
      int statusCode = response.getStatusLine().getStatusCode();
      if (statusCode == HttpStatus.SC_OK) {
        HttpEntity entity = response.getEntity();
        if (entity != null) {
          result = EntityUtils.toString(entity);
        } else {
          throw new HttpException("Received empty body from HTTP response");
        }
      } else {
        throw new HttpException("Received non-OK status from server: " + response.getStatusLine());
      }
      return result;
    }

    @SuppressWarnings("unused")
    public String post(String url, String body) throws IOException, HttpException {
      String result;
      HttpPost request = new HttpPost(url);
      HttpEntity bodyEntity = new StringEntity(body);
      request.setEntity(bodyEntity);
      HttpResponse response = execute(request);
      int statusCode = response.getStatusLine().getStatusCode();
      if (statusCode == HttpStatus.SC_OK) {
        HttpEntity entity = response.getEntity();
        if (entity != null) {
          result = EntityUtils.toString(entity);
        } else {
          throw new HttpException("Received empty body from HTTP response");
        }
      } else {
        throw new HttpException("Received non-OK status from server: " + response.getStatusLine());
      }
      return result;
    }

  }
}