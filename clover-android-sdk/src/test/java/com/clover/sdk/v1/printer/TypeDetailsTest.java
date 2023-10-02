package com.clover.sdk.v1.printer;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

import java.util.Collections;

@RunWith(RobolectricTestRunner.class)
public class TypeDetailsTest {
  @Test
  public void testNullImageUrl() {
    final TypeDetails td = new TypeDetails("A_PRINTER", "A printer", Collections.singletonList(Category.RECEIPT), 576, true, 0, null);
    final JSONObject json = td.getJSONObject();
    final TypeDetails tdFromJson = TypeDetails.JSON_CREATOR.create(json);

    assertNull(tdFromJson.getImageUrl());
  }

  @Test
  public void testNotNullImageUrl() {
    final String url = "http://pez.com";
    final TypeDetails td = new TypeDetails("A_PRINTER", "A printer", Collections.singletonList(Category.RECEIPT), 576, true, 0, url);
    final JSONObject json = td.getJSONObject();
    final TypeDetails tdFromJson = TypeDetails.JSON_CREATOR.create(json);

    assertEquals(url, tdFromJson.getImageUrl());
  }
}
