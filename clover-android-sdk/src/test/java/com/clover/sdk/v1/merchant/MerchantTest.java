package com.clover.sdk.v1.merchant;


import android.os.Bundle;
import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class MerchantTest {

  @Before
  public void setup() {
  }

  @Test
  public void testFastPay() {
    Bundle b = new Bundle();
    JSONArray modules = new JSONArray();

    b.putString("modules", modules.put("xyz").toString());  //Need to add a dummy module else it will return all modules.
    Merchant merchant = new Merchant(b, new Bundle());
    assertThat(merchant.getModules().contains(Module.FAST_PAY), is(false));

    b.putString("modules", modules.put("fast_pay").toString());
    merchant = new Merchant(b, new Bundle());
    assertThat(merchant.getModules().contains(Module.FAST_PAY), is(true));
  }
}
