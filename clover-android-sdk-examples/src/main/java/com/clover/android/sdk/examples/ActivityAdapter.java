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

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityAdapter extends BaseAdapter {
  private final Context context;
  private final ActivityInfo[] activityInfos;

  public ActivityAdapter(Context context, ActivityInfo[] activityInfos) {
    this.context = context;
    this.activityInfos = activityInfos;
  }

  @Override
  public int getCount() {
    return activityInfos.length;
  }

  @Override
  public Object getItem(int i) {
    return activityInfos[i];
  }

  @Override
  public long getItemId(int i) {
    return i;
  }

  @Override
  public View getView(int i, View convertView, ViewGroup parent) {
    View view = convertView;
    if (convertView == null) {
      view = View.inflate(context, R.layout.item_activity, null);
    }

    ActivityInfo activityInfo = activityInfos[i];

    ImageView iconImage = (ImageView) view.findViewById(R.id.icon);
    int iconResource = activityInfo.getIconResource();
    if (iconResource == 0) {
      iconResource = activityInfo.applicationInfo.icon;
    }
    iconImage.setImageResource(iconResource);

    TextView descriptionText = (TextView) view.findViewById(R.id.description);
    if (activityInfo.labelRes != 0) {
      descriptionText.setText(activityInfo.labelRes);
    } else {
      descriptionText.setText(activityInfo.nonLocalizedLabel);
    }

    return view;
  }
}
