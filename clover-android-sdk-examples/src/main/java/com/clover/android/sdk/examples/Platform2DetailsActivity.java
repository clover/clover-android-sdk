/**
 * Copyright (C) 2016 Clover Network, Inc.
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

import com.clover.sdk.util.Platform2;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Platform2DetailsActivity extends AppCompatActivity {

  private static final String TAG = Platform2DetailsActivity.class.getSimpleName();

  private final Executor bgExec = Executors.newSingleThreadExecutor();

  public static class MyViewModel extends AndroidViewModel {
    public MyViewModel(@NonNull Application application) {
      super(application);
    }

    final MutableLiveData<String> platformOutput = new MutableLiveData<>();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_platform2details);

    MyViewModel viewModel = ViewModelProviders.of(this).get(MyViewModel.class);

    TextView tv = findViewById(R.id.platform2details);
    viewModel.platformOutput.observe(this, tv::setText);

    Context appContext = getApplicationContext();

    bgExec.execute(() -> {
      String output = "Platform2\n"
            + "isClover: " + Platform2.isClover() + "\n"
            + "defaultOrientation: " + Platform2.defaultOrientation(appContext) + "\n";

      for (Platform2.Feature feature : Platform2.Feature.values()) {
        output += checkFeature(appContext, feature) + "\n";
      }

      viewModel.platformOutput.postValue(output);
    });
  }

  private static String checkFeature(Context context, Platform2.Feature feature) {
    return feature + ": " + Platform2.supportsFeature(context, feature);
  }

}
