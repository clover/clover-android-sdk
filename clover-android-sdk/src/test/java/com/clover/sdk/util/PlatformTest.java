package com.clover.sdk.util;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Arrays;

import static com.clover.sdk.util.Platform.C100;
import static com.clover.sdk.util.Platform.C200;
import static com.clover.sdk.util.Platform.C201;
import static com.clover.sdk.util.Platform.C300;
import static com.clover.sdk.util.Platform.C301;
import static com.clover.sdk.util.Platform.C302;
import static com.clover.sdk.util.Platform.C302E;
import static com.clover.sdk.util.Platform.C302L;
import static com.clover.sdk.util.Platform.C302U;
import static com.clover.sdk.util.Platform.C400;
import static com.clover.sdk.util.Platform.C400E;
import static com.clover.sdk.util.Platform.C400U;
import static com.clover.sdk.util.Platform.C401E;
import static com.clover.sdk.util.Platform.C401L;
import static com.clover.sdk.util.Platform.C401U;
import static com.clover.sdk.util.Platform.C500;
import static com.clover.sdk.util.Platform.C550;
import static com.clover.sdk.util.Platform.Feature.BATTERY;
import static com.clover.sdk.util.Platform.Feature.CUSTOMER_MODE;
import static com.clover.sdk.util.Platform.Feature.DEFAULT_EMPLOYEE;
import static com.clover.sdk.util.Platform.Feature.ETHERNET;
import static com.clover.sdk.util.Platform.Feature.MOBILE_DATA;
import static com.clover.sdk.util.Platform.Feature.SECURE_PAYMENTS;
import static com.clover.sdk.util.Platform.Feature.SECURE_TOUCH;
import static com.clover.sdk.util.Platform.OTHER;
import static com.clover.sdk.util.Platform.Orientation.LANDSCAPE;
import static com.clover.sdk.util.Platform.Orientation.PORTRAIT;
import static com.clover.sdk.util.Platform.SecureProcessorPlatform.BROADCOM;
import static com.clover.sdk.util.Platform.SecureProcessorPlatform.MAXIM;
import static com.clover.sdk.util.Platform.defaultOrientation;
import static com.clover.sdk.util.Platform.getSecureProcessorPlatform;
import static com.clover.sdk.util.Platform.productName;
import static com.clover.sdk.util.Platform.productQualifier;
import static com.clover.sdk.util.Platform.supportsFeature;

@RunWith(RobolectricTestRunner.class)
public class PlatformTest {
  @Test
  public void testIsCloverStation() throws Exception {
    Assert.assertTrue(Platform.isCloverStation(C100));
    for (Platform p : Arrays.asList(C550, C200, C201, C302, C302U, C302E, C302L, null)) {
      Assert.assertFalse(Platform.isCloverStation(p));
    }
  }

  @Test
  public void testIsCloverMobile() throws Exception {
    Assert.assertTrue(Platform.isCloverMobile(Platform.C200));
    Assert.assertTrue(Platform.isCloverMobile(Platform.C201));
    for (Platform p : Arrays.asList(C550, C100, C302, C302U, C302E, C302L, null)) {
      Assert.assertFalse(Platform.isCloverMobile(p));
    }
  }

  @Test
  public void testIsCloverMini() throws Exception {
    for (Platform p : Arrays.asList(C300, C301, C302, C302U, C302E, C302L)) {
      Assert.assertTrue(Platform.isCloverMini(p));
    }
    for (Platform p : Arrays.asList(C550, C100, C400, C401E, C401E, C401E, null)) {
      Assert.assertFalse(Platform.isCloverMini(p));
    }
  }

  @Test
  public void testIsCloverMiniGen2() throws Exception {
    for (Platform p : Arrays.asList(C302, C302U, C302E, C302L)) {
      Assert.assertTrue(Platform.isCloverMiniGen2(p));
    }
    for (Platform p : Arrays.asList(C550, C100, C400, C401E, C401E, C401E, null)) {
      Assert.assertFalse(Platform.isCloverMiniGen2(p));
    }
  }

  @Test
  public void testIsCloverFlex() throws Exception {
    for (Platform p : Arrays.asList(C400, C400U, C400E, C401U, C401E, C401L)) {
      Assert.assertTrue(Platform.isCloverFlex(p));
    }
    for (Platform p : Arrays.asList(C550, C200, C201, C302, C302U, C302E, C302L, null)) {
      Assert.assertFalse(Platform.isCloverFlex(p));
    }
  }

  @Test
  public void testIsCloverStation2018() throws Exception {
    Assert.assertTrue(Platform.isCloverStation2018(C500));
    Assert.assertTrue(Platform.isCloverStation2018(Platform.C550));
    for (Platform p : Arrays.asList(C100, C200, C302, C302U, C302E, C302L, null)) {
      Assert.assertFalse(Platform.isCloverStation2018(p));
    }
  }

  @Test
  public void testGetSecureProcessorPlacform() throws Exception {
    for (Platform p : Arrays.asList(C200, C201, C300, C301)) {
      Assert.assertEquals(getSecureProcessorPlatform(p), MAXIM);
    }
    for (Platform p : Arrays.asList(C401E, C302U)) {
      Assert.assertEquals(getSecureProcessorPlatform(p), BROADCOM);
    }
    for (Platform p : Arrays.asList(C100, null)) {
      Assert.assertNull(getSecureProcessorPlatform(p));
    }
  }

  @Test
  public void testSupportsFeature() throws Exception {
    for (Platform.Feature f : Arrays.asList(CUSTOMER_MODE, SECURE_PAYMENTS, MOBILE_DATA, DEFAULT_EMPLOYEE, BATTERY, ETHERNET, SECURE_TOUCH)) {
      Assert.assertTrue(supportsFeature(C401E, f));
      Assert.assertFalse(supportsFeature(OTHER, f));
      Assert.assertFalse(supportsFeature(null, f));
    }
  }

  @Test
  public void testDefaultOrientation() throws Exception {
    Assert.assertEquals(defaultOrientation(C100), LANDSCAPE);
    Assert.assertNotSame(defaultOrientation(C100), PORTRAIT);
    Assert.assertNotSame(defaultOrientation(null), LANDSCAPE);
  }

  @Test
  public void testProductQualifier() throws Exception {
    for (Platform p : Arrays.asList(C100, C200, C201, C400, C400U, C400E, C401U, C401E, C401L, OTHER, null)) {
      Assert.assertNull(productQualifier(p));
    }
    Assert.assertEquals(productQualifier(C500), "2018 – Gen 2");
    Assert.assertEquals(productQualifier(C550), "2018 – Gen 2");
    for (Platform p : Arrays.asList(C302, C302U, C302E, C302L)) {
      Assert.assertEquals(productQualifier(p), "2nd generation");
    }
  }

  @Test
  public void testProductName() throws Exception {
    Assert.assertEquals(productName(C401U), "Clover Flex");
    Assert.assertNull(productName(null));
  }
}
