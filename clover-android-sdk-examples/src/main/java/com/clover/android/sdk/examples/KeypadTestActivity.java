package com.clover.android.sdk.examples;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.clover.sdk.v1.Intents;

import java.util.ArrayList;
import java.util.Arrays;

import static com.clover.sdk.v1.Intents.EXTRA_KEYPAD_COMPLETIONS;
import static com.clover.sdk.v1.Intents.EXTRA_KEYPAD_TYPE;
import static com.clover.sdk.v1.Intents.KEYPAD_TYPE_EMAIL;
import static com.clover.sdk.v1.Intents.KEYPAD_TYPE_NUMERIC;
import static com.clover.sdk.v1.Intents.KEYPAD_TYPE_PHONESMS;

/**
 * An example client activity for {@link Intents#ACTION_KEYPAD}.
 */
public class KeypadTestActivity extends Activity {
  private static final int REQUEST_KEYPAD = 13;

  private Button startButton;
  private TextView resultText;

  private int type = KEYPAD_TYPE_NUMERIC;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_keypad_test);

    startButton = findViewById(R.id.button_keypad_start);
    startButton.setOnClickListener(view -> {
      Intent startIntent = new Intent(Intents.ACTION_KEYPAD);
      startIntent.putExtra(EXTRA_KEYPAD_TYPE, type);

      switch (type) {
        case KEYPAD_TYPE_EMAIL:
          // Add some sample completions for email
          final ArrayList<String> completions = new ArrayList<>(Arrays.asList(getResources()
                  .getStringArray(R.array.email_completions)));
          startIntent.putExtra(EXTRA_KEYPAD_COMPLETIONS, completions);
          break;
        case KEYPAD_TYPE_NUMERIC:
        case KEYPAD_TYPE_PHONESMS:
        default:
          break;
      }

      startIntent.putExtra(Intents.EXTRA_KEYPAD_TEXT, resultText.getText().toString());
      startActivityForResult(startIntent, REQUEST_KEYPAD);
    });
    resultText = findViewById(R.id.text_keypad);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_KEYPAD) {
      if (resultCode == RESULT_OK) {
        resultText.setText(data.getStringExtra(Intents.RESULT_KEYPAD_TEXT));
      } else {
        Toast.makeText(this, "Result: CANCELED", Toast.LENGTH_SHORT).show();
      }
    }
  }

  public void onRadioButtonClicked(View view) {
    switch (view.getId()) {
      case R.id.type_numeric:
        type = KEYPAD_TYPE_NUMERIC;
        break;
      case R.id.type_email:
        type = KEYPAD_TYPE_EMAIL;
        break;
      case R.id.type_phone:
        type = KEYPAD_TYPE_PHONESMS;
        break;
      default:
        type = KEYPAD_TYPE_NUMERIC;
        break;
    }
  }
}
