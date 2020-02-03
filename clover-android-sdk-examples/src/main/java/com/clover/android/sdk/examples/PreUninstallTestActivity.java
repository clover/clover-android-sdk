package com.clover.android.sdk.examples;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.Nullable;
import android.widget.TextView;

public class PreUninstallTestActivity extends Activity {

  static final String ACTION_PREUNINSTALL_COUNTDOWN = "countdown";

  private TextView countdownText;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_preuninstall_test);
    countdownText = findViewById(R.id.countdown);

    if (ACTION_PREUNINSTALL_COUNTDOWN.equals(getIntent().getAction())) {
      countdown();
    }
  }

  @Override
  protected void onNewIntent(Intent intent) {
    if (ACTION_PREUNINSTALL_COUNTDOWN.equals(intent.getAction())) {
      countdown();
    }
    super.onNewIntent(intent);
  }

  private void countdown() {
    new CountDownTimer(5000, 100) {

      @Override
      public void onTick(long millisUntilFinished) {
        countdownText.setText(String.format("Uninstall in %.1f seconds", millisUntilFinished / 1000.0));
      }

      @Override
      public void onFinish() {
        countdownText.setText("Uninstall soon!");
      }
    }.start();
  }
}
