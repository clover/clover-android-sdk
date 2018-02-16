package com.clover.common2.payments;

import com.clover.android.sdk.BuildConfig;
import com.clover.sdk.CloverRobolectricRunner;
import com.clover.sdk.v1.Intents;

import android.content.Intent;
import android.os.Parcel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Author: leonid
 * Date: 10/3/17.
 */
@RunWith(CloverRobolectricRunner.class)
@Config(constants = BuildConfig.class)
public class PayIntentTest {

  @Test
  public void testUseLastSwipe() {
    PayIntent payIntent = new PayIntent.Builder().build();
    assertFalse(payIntent.useLastSwipe);

    payIntent = new PayIntent.Builder().useLastSwipe(false).build();
    assertFalse(payIntent.useLastSwipe);

    payIntent = new PayIntent.Builder().useLastSwipe(true).build();
    assertTrue(payIntent.useLastSwipe);
  }

  @Test
  public void testUseLastSwipe_fromIntent() {
    Intent sourceIntent = new Intent();
    PayIntent payIntent = new PayIntent.Builder().intent(sourceIntent).build();
    assertFalse(payIntent.useLastSwipe);

    sourceIntent = new Intent();
    sourceIntent.putExtra(Intents.EXTRA_USE_LAST_SWIPE, false);
    payIntent = new PayIntent.Builder().intent(sourceIntent).build();
    assertFalse(payIntent.useLastSwipe);

    sourceIntent = new Intent();
    sourceIntent.putExtra(Intents.EXTRA_USE_LAST_SWIPE, true);
    payIntent = new PayIntent.Builder().intent(sourceIntent).build();
    assertTrue(payIntent.useLastSwipe);
  }

  @Test
  public void testUseLastSwipe_fromPayIntent() {
    PayIntent sourcePayIntent = new PayIntent.Builder().build();
    PayIntent payIntent = new PayIntent.Builder().payIntent(sourcePayIntent).build();
    assertFalse(payIntent.useLastSwipe);

    sourcePayIntent = new PayIntent.Builder().useLastSwipe(false).build();
    payIntent = new PayIntent.Builder().payIntent(sourcePayIntent).build();
    assertFalse(payIntent.useLastSwipe);

    sourcePayIntent = new PayIntent.Builder().useLastSwipe(true).build();
    payIntent = new PayIntent.Builder().payIntent(sourcePayIntent).build();
    assertTrue(payIntent.useLastSwipe);
  }

  @Test
  public void testUseLastSwipe_serialization() {
    PayIntent payIntent = new PayIntent.Builder().useLastSwipe(true).build();

    Parcel p = Parcel.obtain();
    payIntent.writeToParcel(p, 0);

    p.setDataPosition(0);
    PayIntent fromParcel = PayIntent.CREATOR.createFromParcel(p);
    assertTrue(fromParcel.useLastSwipe);
  }


}
