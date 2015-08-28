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
package com.clover.sdk.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import com.clover.android.sdk.R;
import com.clover.sdk.internal.util.Strings;
import com.clover.sdk.util.Platform;

/**
 * Created by lausier on 10/29/13.
 */
public class RotateToMerchantDialogFragment extends DialogFragment {

  private static final String TAG = RotateToMerchantDialogFragment.class.getSimpleName();
  private static final String EXTRA_INSTRUCTION = "instruction";

  private RotateToMerchantDialogFragment() {
    super();
  }

  public static RotateToMerchantDialogFragment newInstance(String instruction) {
    RotateToMerchantDialogFragment f = new RotateToMerchantDialogFragment();

    Bundle args = new Bundle();
    args.putString(EXTRA_INSTRUCTION, instruction);
    f.setArguments(args);

    return f;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Pick a style based on the num.
    int style = DialogFragment.STYLE_NORMAL, theme = R.style.DialogThemeNoWindow;
    setCancelable(false);
    setStyle(style, theme);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.rotate_to_merchant_dialog_fragment, container, false);

    String text = getArguments().getString(EXTRA_INSTRUCTION);
    if (!Strings.isNullOrEmpty(text)) {
      ((TextView) v.findViewById(R.id.instruction)).setText(text);
    }

    v.setTag(TAG);
    return v;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public void onStart() {
    if (Platform.isCloverMobile() || Platform.isCloverMini()) {
      getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
          WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

      super.onStart();

      int uiOptions = getDialog().getWindow().getDecorView().getSystemUiVisibility();
      getDialog().getWindow().getDecorView().setSystemUiVisibility(uiOptions
          | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
          | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

      // Set the dialog to focusable again.
      getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    } else {
      // station
      super.onStart();
      getDialog().getWindow().getDecorView().findViewById(android.R.id.content).setSystemUiVisibility(0x10000000);  // magic :)
    }
  }
}

