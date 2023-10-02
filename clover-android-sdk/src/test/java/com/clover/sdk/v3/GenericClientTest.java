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

  @Test
  public void genClientTestMergeChanges() {

    // Create a source object
    ValueExtractorStyleTestObject vSource = new ValueExtractorStyleTestObject();
    vSource.setAndroidVersions(Collections.singletonList(new Reference().setId("ABC")));
    List<Reference> androidVersions = vSource.getAndroidVersions();

    // make sure it was set correctly
    Assert.assertEquals("ABC", androidVersions.get(0).getId());

    // create a destination object and set a property value
    ValueExtractorStyleTestObject vSink = new ValueExtractorStyleTestObject();
    vSink.setAndroidVersions(Collections.singletonList(new Reference().setId("DEF")));

    // read back to load value cache
    List<Reference> sinkAndroidVersions = vSink.getAndroidVersions();
    Assert.assertEquals("DEF", sinkAndroidVersions.get(0).getId());

    vSink.mergeChanges(vSource);

    // make sure changed values are there, fails if GenericClient value cache isn't invalidated in mergeChanges
    List<Reference> copiedAndroidVersions = vSink.getAndroidVersions();
    Assert.assertEquals("ABC", copiedAndroidVersions.get(0).getId());
  }

}
