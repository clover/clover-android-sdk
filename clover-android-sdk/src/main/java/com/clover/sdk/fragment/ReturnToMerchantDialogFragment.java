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

  private static final String EXTRA_INSTRUCTION = "instruction";

  private Listener mListener;

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

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Pick a style based on the num.
    int style = DialogFragment.STYLE_NORMAL, theme = R.style.DialogThemeNoWindow;
    setCancelable(true);
    setStyle(style, theme);
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    return new Dialog(getActivity(), getTheme()) {
      @Override
      public boolean dispatchTouchEvent(MotionEvent ev) {
        dismiss();
        return true;
      }
    };
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.return_to_merchant_dialog_fragment, container, false);

    String text = getArguments().getString(EXTRA_INSTRUCTION);
    if (!TextUtils.isEmpty(text)) {
      ((TextView) v.findViewById(R.id.instruction)).setText(text);
    }

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

    if (mListener != null) {
      mListener.onDismiss();
    }
  }
}

