package com.clover.android.sdk.examples;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

/**
 * Prevent user button mashing that may have undesirable results. By default the button stays
 * disabled for 2 seconds after it is pressed before it re-enables itself
 */
public class DebouncedButton extends AppCompatButton {

  private final Handler mainHandler = new Handler(Looper.getMainLooper());
  private long debounceTimeoutMillis = 2000;

  public DebouncedButton(Context context) {
    super(context);
  }

  public DebouncedButton(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public DebouncedButton(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void setDebounceTimeoutMillis(long timeoutMillis) {
    this.debounceTimeoutMillis = timeoutMillis;
  }

  private final Runnable debounce = () -> {
    setEnabled(true);
  };

  @Override
  public void setOnClickListener(@Nullable OnClickListener l) {
    mainHandler.removeCallbacks(debounce);

    if (l == null) {
      super.setOnClickListener(null);
      return;
    }

    super.setOnClickListener((view) -> {
      setEnabled(false);
      mainHandler.postDelayed(debounce, debounceTimeoutMillis);
      l.onClick(this);
    });
  }

  @Override
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    mainHandler.removeCallbacks(debounce);
  }

}
