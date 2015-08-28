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

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.clover.android.sdk.R;

public class ReturnToMerchantDialogFragment extends DialogFragment {

  public interface Listener {
    void onDismiss();
  }

  public interface Listener2 {
    void onDismiss(int id, Parcelable tag);
  }

  private static final String EXTRA_ID = "id";
  private static final String EXTRA_TAG = "tag";
  private static final String EXTRA_TITLE = "title";
  private static final String EXTRA_INSTRUCTION = "instruction";

  private boolean isDismissed;

  public ReturnToMerchantDialogFragment() {
    super();
  }

  public static ReturnToMerchantDialogFragment newInstance(String instruction) {
    ReturnToMerchantDialogFragment f = new ReturnToMerchantDialogFragment();

    Bundle args = new Bundle();
    args.putString(EXTRA_INSTRUCTION, instruction);
    f.setArguments(args);

    return f;
  }

  public static ReturnToMerchantDialogFragment newInstance(String title, String instruction) {
    ReturnToMerchantDialogFragment f = new ReturnToMerchantDialogFragment();

    Bundle args = new Bundle();
    args.putString(EXTRA_INSTRUCTION, instruction);
    args.putString(EXTRA_TITLE, title);
    f.setArguments(args);

    return f;
  }

  public static ReturnToMerchantDialogFragment newInstance(int id, Parcelable tag, String instruction) {
    return newInstance(id, tag, null, instruction);
  }

  public static ReturnToMerchantDialogFragment newInstance(int id, Parcelable tag, String title, String instruction) {
    ReturnToMerchantDialogFragment f = new ReturnToMerchantDialogFragment();

    Bundle args = new Bundle();
    args.putInt(EXTRA_ID, id);
    args.putParcelable(EXTRA_TAG, tag);
    args.putString(EXTRA_INSTRUCTION, instruction);
    args.putString(EXTRA_TITLE, title);
    f.setArguments(args);

    return f;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo);
    setCancelable(true);
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    return new Dialog(getActivity(), getTheme()) {
      @Override
      public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!isDismissed) {
          isDismissed = true;
          dismiss();
          callOnDismiss();
        }
        return true;
      }
    };
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.return_to_merchant_dialog_fragment, container, false);

    if (getArguments() != null) {
      String title = getArguments().getString(EXTRA_TITLE);
      if (!TextUtils.isEmpty(title)) {
        ((TextView) v.findViewById(R.id.title)).setText(title);
      }

      String instruction = getArguments().getString(EXTRA_INSTRUCTION);
      if (!TextUtils.isEmpty(instruction)) {
        ((TextView) v.findViewById(R.id.instruction)).setText(instruction);
      }
    }

    v.findViewById(R.id.mainLayout).setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (!isDismissed) {
          isDismissed = true;
          dismiss();
          callOnDismiss();
        }
        return true;
      }
    });

    return v;
  }

  @Override
  public void onDismiss(DialogInterface dialog) {
    super.onDismiss(dialog);
    if (!isDismissed) {
      isDismissed = true;
      callOnDismiss();
    }
  }

  private void callOnDismiss() {
    Activity a = getActivity();
    if (a instanceof Listener) {
      ((Listener) a).onDismiss();
    }
    if (a instanceof Listener2 && getArguments().containsKey(EXTRA_ID)) {
      ((Listener2) a).onDismiss(getArguments().getInt(EXTRA_ID), getArguments().getParcelable(EXTRA_TAG));
    }
  }
}

