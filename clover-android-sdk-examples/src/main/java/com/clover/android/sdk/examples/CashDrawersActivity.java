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

import com.clover.sdk.cashdrawer.CashDrawer;
import com.clover.sdk.cashdrawer.CashDrawers;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CashDrawersActivity extends AppCompatActivity {

  private static final String TAG = CashDrawersActivity.class.getSimpleName();

  private static final Executor bgExec = Executors.newSingleThreadExecutor();

  private MyViewModel viewModel;
  private RecyclerView recyclerView;
  private LinearLayoutManager layoutManager;
  private CashDrawers cashDrawers;

  public static class MyViewModel extends AndroidViewModel {
    public MyViewModel(@NonNull Application application) {
      super(application);
    }

    final MutableLiveData<Set<CashDrawer>> cashDrawerData = new MutableLiveData<>();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_cash_drawers);

    Context appContext = getApplicationContext();
    cashDrawers = new CashDrawers(appContext);
    cashDrawers.registerDiscovery(new CustomMockCashDrawerDiscovery(appContext));

    recyclerView = findViewById(R.id.cashdrawers_rcv);
    recyclerView.setHasFixedSize(true);

    layoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(layoutManager);

    ShapeDrawable dividerDrawable = new ShapeDrawable();
    dividerDrawable.getPaint().setColor(0xFF000000);
    dividerDrawable.setIntrinsicHeight(1);
    DividerItemDecoration itemDecorator = new DividerItemDecoration(appContext, layoutManager.getOrientation());
    itemDecorator.setDrawable(dividerDrawable);
    recyclerView.addItemDecoration(itemDecorator, DividerItemDecoration.HORIZONTAL);

    viewModel = ViewModelProviders.of(this).get(MyViewModel.class);
    viewModel.cashDrawerData.observe(this, (cashDrawers -> {
      recyclerView.setAdapter(new CashDrawerViewAdapter(cashDrawers));
    }));

    Button refresh = findViewById(R.id.refresh_btn);
    refresh.setOnClickListener(this::findCashDrawers);
  }

  private void findCashDrawers(View v) {
    bgExec.execute(() -> {
      viewModel.cashDrawerData.postValue(cashDrawers.list());
    });
  }

  void popCashDrawer(CashDrawer cd) {
    bgExec.execute(() -> {
      String result;
      if (cd.pop()) {
        result = "Pop succeeded!";
      } else {
        result = "Pop failed!";
      }

      runOnUiThread(() -> Toast.makeText(this, result, Toast.LENGTH_SHORT).show());
    });
  }

  static class TextViewHolder extends RecyclerView.ViewHolder {
    final TextView textView;
    TextViewHolder(TextView v) {
      super(v);
      textView = v;
    }
  }

  static class CashDrawerComparator implements Comparator<CashDrawer> {
    @Override
    public int compare(CashDrawer o1, CashDrawer o2) {
      if (o1 == null && o2 == null) {
        return 0;
      }

      if (o1 == null) {
        return -1;
      }

      if (o2 == null) {
        return 1;
      }

      return o1.getUniqueIdentifier().compareTo(o2.getUniqueIdentifier());
    }
  }

  class CashDrawerViewAdapter extends RecyclerView.Adapter<TextViewHolder> {
    final List<CashDrawer> cashDrawers = new ArrayList<>();
    final CashDrawerComparator cashDrawerComparator = new CashDrawerComparator();

    CashDrawerViewAdapter(Collection<CashDrawer> col) {
      cashDrawers.addAll(col);
      Collections.sort(cashDrawers, cashDrawerComparator);
      Collections.reverse(cashDrawers);
    }

    @NonNull
    @Override
    public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      TextView v = (TextView) LayoutInflater.from(parent.getContext())
          .inflate(R.layout.cashdrawer_view, parent, false);
      return new TextViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TextViewHolder holder, int position) {
      CashDrawer cd = cashDrawers.get(position);
      holder.textView.setText(cd.getDisplayName() + " " + cd.getDrawerNumber());
      holder.textView.setTag(cd);
      holder.textView.setOnClickListener((view) -> popCashDrawer(cd));
    }

    @Override
    public int getItemCount() {
      return cashDrawers.size();
    }
  }

  // Test custom cash drawer
  static class CustomCashDrawer extends CashDrawer {
    CustomCashDrawer(Context context, int drawerNumber) {
      super(context, drawerNumber);
    }

    @Override
    public String getIdentifier() {
      return "com.app.CUSTOM";
    }

    @Override
    public String getDisplayName() {
      return "Custom Cash Drawer";
    }

    @Override
    public boolean pop() {
      Log.w(TAG, "You can't actually pop: " + this);
      return false;
    }
  }

  // Testing custom cash drawer discovery
  static class CustomMockCashDrawerDiscovery extends CashDrawer.Discovery<CustomCashDrawer> {
    CustomMockCashDrawerDiscovery(Context context) {
      super(context);
    }

    @Override
    public Set<CustomCashDrawer> list() {
      Set<CustomCashDrawer> result = new HashSet<>();

      // Simulate a varying number of connected cash drawers
      int count = Math.abs(new Random().nextInt(10)) + 1;

      for (int i = 0; i < count; i++) {
        result.add(new CustomCashDrawer(context, i + 1));
      }
      return result;
    }
  }

}
