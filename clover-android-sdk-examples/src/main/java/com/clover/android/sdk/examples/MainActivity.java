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
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends Activity {
  private ListView activityList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    activityList = (ListView) findViewById(R.id.activity_list);
    activityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ActivityInfo activityInfo = (ActivityInfo) adapterView.getAdapter().getItem(i);
        launch(activityInfo);
      }
    });

    try {
      PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
      ListAdapter listAdapter = new ActivityAdapter(this, filter(pi.activities));
      activityList.setAdapter(listAdapter);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
  }

  private void launch(ActivityInfo activityInfo) {
    Intent intent = new Intent("android.intent.action.MAIN");
    intent.addCategory("android.intent.category.LAUNCHER");
    intent.setComponent(new ComponentName(activityInfo.packageName, activityInfo.name));
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    startActivity(intent);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_javadoc:
        Intent docIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.clover.com/reference/android/javadoc/index.html"));
        startActivity(docIntent);
        return true;
      case R.id.action_source:
        Intent sourceIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/clover/clover-android-sdk"));
        startActivity(sourceIntent);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private ActivityInfo[] filter(ActivityInfo[] activityInfos) throws PackageManager.NameNotFoundException {
    final ActivityInfo thisActivity = getPackageManager().getActivityInfo(new ComponentName(getPackageName(), getClass().getName()), PackageManager.GET_ACTIVITIES);
    List<ActivityInfo> activityInfoList = new ArrayList<ActivityInfo>();
    for (ActivityInfo ai : activityInfos) {
      if (ai.name.equals(thisActivity.name)) {
        continue;
      }
      activityInfoList.add(ai);
    }
    Collections.sort(activityInfoList, new Comparator<ActivityInfo>() {
      @Override
      public int compare(ActivityInfo activityInfo1, ActivityInfo activityInfo2) {
        if (!activityInfo1.packageName.equals(activityInfo2.packageName)) {
          return activityInfo1.packageName.compareTo(activityInfo2.packageName);
        }
        return activityInfo1.name.compareTo(activityInfo2.name);
      }
    });

    return activityInfoList.toArray(new ActivityInfo[0]);
  }
}
