package com.clover.sdk.v3;

import com.clover.sdk.ValueExtractorStyleTestObject;
import com.clover.sdk.v3.base.Reference;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Collections;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class GenericClientTest {

  @Test
  public void genClientTestValueExtractorStyleObject() {
    // Very few objects still use the old ValueExtractorEnum class, ensure it still works
    ValueExtractorStyleTestObject v = new ValueExtractorStyleTestObject();
    v.setAndroidVersions(Collections.singletonList(new Reference().setId("ABC")));
    List<Reference> androidVersions = v.getAndroidVersions();
    Assert.assertEquals("ABC", androidVersions.get(0).getId());
  }

}
