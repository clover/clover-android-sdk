package com.clover.android.sdk.examples;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.clover.sdk.util.CloverAuth;

import java.util.concurrent.TimeUnit;

public class AppAuthTestActivity extends Activity {
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_app_auth_test);
  }

  public void authenticate(View view) {
    final boolean force = ((CheckBox) findViewById(R.id.force)).isChecked();
    try {
      CloverAuth.AuthResult result = CloverAuth.authenticate(this, force, 5L, TimeUnit.SECONDS);
      ((TextView) findViewById(R.id.auth_result)).setText(result.toString());
    } catch (Exception e) {
      ((TextView) findViewById(R.id.auth_result)).setText(e.toString());
    }
  }
}
