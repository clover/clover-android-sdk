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
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v1.ServiceConnector;
import com.clover.sdk.v3.inventory.InventoryContract;
import com.clover.sdk.v3.inventory.PriceType;
import com.clover.sdk.v1.merchant.Merchant;
import com.clover.sdk.v1.merchant.MerchantConnector;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class InventoryListActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {

  private static final int ITEM_LOADER_ID = 0;
  private static final NumberFormat sCurrencyFormat = DecimalFormat.getCurrencyInstance(Locale.US);

  private Account mAccount;
  private MerchantConnector mMerchantConnector;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mAccount = CloverAccount.getAccount(this);


    if (mAccount != null) {
      mMerchantConnector = new MerchantConnector(this, mAccount, null);
      mMerchantConnector.getMerchant(new ServiceConnector.Callback<Merchant>() {
        @Override
        public void onServiceSuccess(Merchant result, ResultStatus status) {
          // set the Currency
          sCurrencyFormat.setCurrency(result.getCurrency());

          // now start the Loader to query Inventory
          getLoaderManager().initLoader(ITEM_LOADER_ID, null, InventoryListActivity.this);
        }

        @Override
        public void onServiceFailure(ResultStatus status) {
        }

        @Override
        public void onServiceConnectionFailure() {
        }
      });
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    mMerchantConnector.disconnect();
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    switch (id) {
      case ITEM_LOADER_ID:
        return new CursorLoader(this, InventoryContract.Item.contentUriWithAccount(mAccount), null, null,
            null, InventoryContract.Item.PRICE);
    }
    throw new IllegalArgumentException("Unknown loader ID");
  }

  @Override
  public final void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    switch (loader.getId()) {
      case ITEM_LOADER_ID:
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
            this,
            android.R.layout.two_line_list_item,
            cursor,
            new String[]{InventoryContract.Item.NAME, InventoryContract.Item.PRICE},
            new int[]{android.R.id.text1, android.R.id.text2}, 0);

        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
          @Override
          public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
            if (view.getId() == android.R.id.text2) {
              TextView priceTextView = (TextView) view;
              String price = "";
              PriceType priceType = PriceType.values()[cursor.getInt(cursor.getColumnIndex(InventoryContract.Item.PRICE_TYPE))];
              String priceString = cursor.getString(cursor.getColumnIndex(InventoryContract.Item.PRICE));
              long value = Long.valueOf(priceString);
              if (priceType == PriceType.FIXED) {
                price = sCurrencyFormat.format(value / 100.0);
              } else if (priceType == PriceType.VARIABLE) {
                price = "Variable";
              } else if (priceType == PriceType.PER_UNIT) {
                price = sCurrencyFormat.format(value / 100.0) + "/" + cursor.getString(cursor.getColumnIndex(InventoryContract.Item.UNIT_NAME));
              }

              priceTextView.setText(price);
              return true;
            }
            return false;
          }
        });

        setListAdapter(adapter);
        break;
    }
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
  }
}
