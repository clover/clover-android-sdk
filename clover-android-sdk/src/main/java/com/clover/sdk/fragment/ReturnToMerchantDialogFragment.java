package com.clover.sdk.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
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

  private static final String EXTRA_TITLE = "title";
  private static final String EXTRA_INSTRUCTION = "instruction";

  private Listener mListener;
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

          if (mListener != null) {
            mListener.onDismiss();
          }
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

          if (mListener != null) {
            mListener.onDismiss();
          }
        }
        return true;
      }
    });

    return v;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);

    if (activity instanceof Listener) {
      mListener = (Listener) activity;
    } else {
      mListener = null;
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();

    mListener = null;
  }

  @Override
  public void onDismiss(DialogInterface dialog) {
    super.onDismiss(dialog);

    if (mListener != null && !isDismissed) {
      mListener.onDismiss();
    }
  }
}

