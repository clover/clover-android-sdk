package com.clover.android.sdk.examples;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class KeypadEditText extends EditText {

  public KeypadEditText(Context context) {
    super(context);
  }

  public KeypadEditText(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public KeypadEditText(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public KeypadEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override
  public boolean isCursorVisible() {
    return true;
  }

  @Override
  public boolean isFocused() {
    return true;
  }

  @Override
  public void setText(CharSequence text, BufferType type) {
    super.setText(text, type);
    setSelection(text.length()); // Required to move cursor to end
  }
}
