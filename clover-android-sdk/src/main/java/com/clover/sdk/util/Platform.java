/**
 * Copyright (C) 2016 Clover Network, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clover.sdk.util;

import android.os.Build;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * <b>Please discontinue using this class, it has been deprecated and will be deleted in a
 * future version of the Clover Android SDK!</b>
 * <p>
 * Code which references this class almost certainly will not work on future Clover devices or even
 * slight model variations.
 * </p><p>
 * Clover frequently adds new models for existing devices when shipping devices to new
 * regions. Please use {@link com.clover.sdk.util.Platform2} and/or relevant Android APIs to write
 * code compatible with future devices that may have different screen sizes or hardware features.
 * </p>
 */
@Deprecated
public enum Platform {

  @Deprecated
  TF300T("Clover Hub", null, Orientation.LANDSCAPE),
  @Deprecated
  C100("Clover Station", null, Orientation.LANDSCAPE, Feature.ETHERNET, Feature.CUSTOMER_ROTATION, Feature.DEFAULT_EMPLOYEE),
  @Deprecated
  C200("Clover Mobile", null, Orientation.LANDSCAPE, Feature.CUSTOMER_MODE, Feature.SECURE_PAYMENTS, Feature.DEFAULT_EMPLOYEE, Feature.BATTERY, Feature.SECURE_TOUCH),
  @Deprecated
  C201("Clover Mobile", null, Orientation.LANDSCAPE, Feature.CUSTOMER_MODE, Feature.SECURE_PAYMENTS, Feature.MOBILE_DATA, Feature.DEFAULT_EMPLOYEE, Feature.BATTERY, Feature.SECURE_TOUCH),
  @Deprecated
  C300("Clover Mini", null, Orientation.LANDSCAPE, Feature.CUSTOMER_MODE, Feature.SECURE_PAYMENTS, Feature.DEFAULT_EMPLOYEE, Feature.ETHERNET, Feature.SECURE_TOUCH),
  @Deprecated
  C301("Clover Mini", null, Orientation.LANDSCAPE, Feature.CUSTOMER_MODE, Feature.SECURE_PAYMENTS, Feature.MOBILE_DATA, Feature.DEFAULT_EMPLOYEE, Feature.ETHERNET, Feature.SECURE_TOUCH),
  @Deprecated
  C400("Clover Flex", null, Orientation.PORTRAIT, Feature.CUSTOMER_MODE, Feature.SECURE_PAYMENTS, Feature.MOBILE_DATA, Feature.DEFAULT_EMPLOYEE, Feature.BATTERY, Feature.ETHERNET, Feature.SECURE_TOUCH),
  @Deprecated
  C400U("Clover Flex", null, Orientation.PORTRAIT, Feature.CUSTOMER_MODE, Feature.SECURE_PAYMENTS, Feature.MOBILE_DATA, Feature.DEFAULT_EMPLOYEE, Feature.BATTERY, Feature.ETHERNET, Feature.SECURE_TOUCH),
  @Deprecated
  C400E("Clover Flex", null, Orientation.PORTRAIT, Feature.CUSTOMER_MODE, Feature.SECURE_PAYMENTS, Feature.MOBILE_DATA, Feature.DEFAULT_EMPLOYEE, Feature.BATTERY, Feature.ETHERNET, Feature.SECURE_TOUCH),
  @Deprecated
  C401U("Clover Flex", null, Orientation.PORTRAIT, Feature.CUSTOMER_MODE, Feature.SECURE_PAYMENTS, Feature.MOBILE_DATA, Feature.DEFAULT_EMPLOYEE, Feature.BATTERY, Feature.ETHERNET, Feature.SECURE_TOUCH),
  @Deprecated
  C401E("Clover Flex", null, Orientation.PORTRAIT, Feature.CUSTOMER_MODE, Feature.SECURE_PAYMENTS, Feature.MOBILE_DATA, Feature.DEFAULT_EMPLOYEE, Feature.BATTERY, Feature.ETHERNET, Feature.SECURE_TOUCH),
  @Deprecated
  C401L("Clover Flex", null, Orientation.PORTRAIT, Feature.CUSTOMER_MODE, Feature.SECURE_PAYMENTS, Feature.MOBILE_DATA, Feature.DEFAULT_EMPLOYEE, Feature.BATTERY, Feature.ETHERNET, Feature.SECURE_TOUCH),
  @Deprecated
  C500("Clover Station", "2018 – Gen 2", Orientation.LANDSCAPE, Feature.CUSTOMER_MODE, Feature.SECURE_PAYMENTS, Feature.DEFAULT_EMPLOYEE, Feature.ETHERNET, Feature.CUSTOMER_FACING_EXTERNAL_DISPLAY, Feature.CUSTOMER_ROTATION),
  @Deprecated
  C550("Clover Station", "2018 – Gen 2", Orientation.LANDSCAPE, Feature.CUSTOMER_MODE, Feature.SECURE_PAYMENTS, Feature.DEFAULT_EMPLOYEE, Feature.ETHERNET),
  @Deprecated
  C302("Clover Mini", "2nd generation", Orientation.LANDSCAPE, Feature.CUSTOMER_MODE, Feature.SECURE_PAYMENTS, Feature.MOBILE_DATA, Feature.DEFAULT_EMPLOYEE, Feature.ETHERNET, Feature.SECURE_TOUCH),
  @Deprecated
  C302U("Clover Mini", "2nd generation", Orientation.LANDSCAPE, Feature.CUSTOMER_MODE, Feature.SECURE_PAYMENTS, Feature.MOBILE_DATA, Feature.DEFAULT_EMPLOYEE, Feature.ETHERNET, Feature.SECURE_TOUCH),
  @Deprecated
  C302E("Clover Mini", "2nd generation", Orientation.LANDSCAPE, Feature.CUSTOMER_MODE, Feature.SECURE_PAYMENTS, Feature.MOBILE_DATA, Feature.DEFAULT_EMPLOYEE, Feature.ETHERNET, Feature.SECURE_TOUCH),
  @Deprecated
  C302L("Clover Mini", "2nd generation", Orientation.LANDSCAPE, Feature.CUSTOMER_MODE, Feature.SECURE_PAYMENTS, Feature.MOBILE_DATA, Feature.DEFAULT_EMPLOYEE, Feature.ETHERNET, Feature.SECURE_TOUCH),
  @Deprecated
  OTHER("Unknown", null, Orientation.LANDSCAPE),
  ;

  private static final String CLOVER_MANUFACTURER = "Clover";

  private static final EnumSet<Platform> FLEX = EnumSet.of(C400, C400U, C400E, C401U, C401E, C401L);
  private static final EnumSet<Platform> MINI = EnumSet.of(C300, C301, C302, C302U, C302E, C302L);
  private static final EnumSet<Platform> MINI_GEN2 = EnumSet.of(C302, C302U, C302E, C302L);
  private static final EnumSet<Platform> MOBILE = EnumSet.of(C200, C201);
  private static final EnumSet<Platform> STATION = EnumSet.of(C100);
  private static final EnumSet<Platform> STATION_2018 = EnumSet.of(C500, C550);

  /**
   * This class is deprecated please use {@link com.clover.sdk.util.Platform2.Feature} instead.
   */
  @Deprecated
  public enum Feature {
    /**
     * @deprecated Use {@link com.clover.sdk.util.Platform2.Feature#SECURE_PAYMENTS} instead.
     */
    @Deprecated
    SECURE_PAYMENTS,
    /**
     * @deprecated Use {@link com.clover.sdk.util.Platform2.Feature#CUSTOMER_MODE} instead.
     */
    @Deprecated
    CUSTOMER_MODE,
    /**
     * @deprecated Use {@link com.clover.sdk.util.Platform2.Feature#MOBILE_DATA} instead.
     */
    @Deprecated
    MOBILE_DATA,
    /**
     * @deprecated Use {@link com.clover.sdk.util.Platform2.Feature#DEFAULT_EMPLOYEE} instead.
     */
    @Deprecated
    DEFAULT_EMPLOYEE,
    /**
     * @deprecated Use {@link android.os.PowerManager} instead.
     */
    @Deprecated
    BATTERY,
    /**
     * @deprecated Use {@link com.clover.sdk.util.Platform2.Feature#ETHERNET} instead.
     */
    @Deprecated
    ETHERNET,
    /**
     * @deprecated Use {@link com.clover.sdk.util.Platform2.Feature#SECURE_TOUCH} instead.
     */
    @Deprecated
    SECURE_TOUCH,
    /**
     * @deprecated Use {@link com.clover.sdk.util.Platform2.Feature#CUSTOMER_FACING_EXTERNAL_DISPLAY}
     * instead.
     */
    @Deprecated
    CUSTOMER_FACING_EXTERNAL_DISPLAY,
    /**
     * @deprecated Use {@link com.clover.sdk.util.Platform2.Feature#CUSTOMER_ROTATION} instead.
     */
    @Deprecated
    CUSTOMER_ROTATION,
    ;
  }

  @Deprecated
  public enum SecureProcessorPlatform {
    BROADCOM,
    MAXIM,
  }

  /**
   * @see Platform2
   */
  @Deprecated
  public enum Orientation {
    LANDSCAPE,
    PORTRAIT,
  }

  /**
   * @see Platform2
   */
  @Deprecated
  public static Platform get() {
    Platform platform;
    try {
      platform = Platform.valueOf(Build.MODEL);
    } catch (IllegalArgumentException e) {
      platform = OTHER;
    }

    return platform;
  }

  /**
   * @see Platform2
   */
  @Deprecated
  public static boolean isClover() {
    return Build.MANUFACTURER.equals(CLOVER_MANUFACTURER);
  }

  /**
   * @see Platform2
   */
  @Deprecated
  public static boolean isCloverStation() {
    return isCloverStation(get());
  }

  /**
   * @see Platform2
   */
  @Deprecated
  public static boolean isCloverStation(Platform platform) {
    return STATION.contains(platform);
  }

  /**
   * @see Platform2
   */
  @Deprecated
  public static boolean isCloverMobile() {
    return isCloverMobile(get());
  }

  /**
   * @see Platform2
   */
  public static boolean isCloverMobile(Platform platform) {
    return MOBILE.contains(platform);
  }

  /**
   * @see Platform2
   */
  @Deprecated
  public static boolean isCloverMini() {
    return isCloverMini(get());
  }

  /**
   * @see Platform2
   */
  @Deprecated
  public static boolean isCloverMini(Platform platform) {
    return MINI.contains(platform);
  }

  /**
   * @see Platform2
   */
  @Deprecated
  public static boolean isCloverMiniGen2() {
    return isCloverMiniGen2(get());
  }

  /**
   * @see Platform2
   */
  @Deprecated
  public static boolean isCloverMiniGen2(Platform platform) {
    return MINI_GEN2.contains(platform);
  }

  /**
   * @see Platform2
   */
  @Deprecated
  public static boolean isCloverOne() {
    return isCloverFlex();
  }

  @Deprecated
  public static SecureProcessorPlatform getSecureProcessorPlatform() {
    return getSecureProcessorPlatform(get());
  }

  @Deprecated
  public static SecureProcessorPlatform getSecureProcessorPlatform(Platform platform) {
    if (platform == null) {
      return null;
    }
    if (platform == C200 || platform == C201 || platform == C300 || platform == C301) {
      return SecureProcessorPlatform.MAXIM;
    }
    if (supportsFeature(platform, Feature.SECURE_PAYMENTS)) {
      return SecureProcessorPlatform.BROADCOM;
    }
    return null;
  }

  /**
   * @see Platform2
   */
  @Deprecated
  public static boolean isCloverFlex() {
    return isCloverFlex(get());
  }

  /**
   * @see Platform2
   */
  @Deprecated
  public static boolean isCloverFlex(Platform platform) {
    return FLEX.contains(platform);
  }

  /**
   * @see Platform2
   */
  @Deprecated
  public static boolean isCloverStation2018() {
    return  isCloverStation2018(get());
  }
  /**
   * @see Platform2
   */
  @Deprecated
  public static boolean isCloverStation2018(Platform platform) {
    return  STATION_2018.contains(platform);
  }

  /**
   * @see Platform2
   */
  @Deprecated
  public static boolean isCloverGoldenOak() {
    return isCloverStation2018();
  }

  /**
   * @see Platform2
   */
  @Deprecated
  public static boolean is3g() {
    return get().isSupportsFeature(Feature.MOBILE_DATA);
  }

  /**
   * @see Platform2
   */
  @Deprecated
  public static boolean isSecureBoardPresent() {
    return get().isSupportsFeature(Feature.SECURE_PAYMENTS);
  }

  /**
   * @see Platform2
   */
  @Deprecated
  public static boolean isDefaultPortrait() {
    return get().getDefaultOrientation() == Orientation.PORTRAIT;
  }

  /**
   * @see Platform2
   */
  @Deprecated
  public static boolean isSupportsCustomerMode() {
    return get().isSupportsFeature(Feature.CUSTOMER_MODE);
  }

  /**
   * @see Platform2
   */
  @Deprecated
  public static boolean supportsFeature(Feature f) {
    return supportsFeature(get(), f);
  }

  /**
   * @see Platform2
   */
  @Deprecated
  public static boolean supportsFeature(Platform platform, Feature f) {
    if (platform == null) {
      return false;
    }
    return platform.isSupportsFeature(f);
  }

  /**
   * @see Platform2
   */
  @Deprecated
  public static Orientation defaultOrientation() {
    return defaultOrientation(get());
  }

  /**
   * @see Platform2
   */
  @Deprecated
  public static Orientation defaultOrientation(Platform platform) {
    if (platform == null) {
      return null;
    }
    return platform.getDefaultOrientation();
  }

  /**
   * @see Platform2
   */
  @Deprecated
  public static String productQualifier() {
    return productQualifier(get());
  }

  /**
   * @see Platform2
   */
  @Deprecated
  public static String productQualifier(Platform platform) {
    if (platform == null) {
      return null;
    }
    return platform.getProductQualifier();
  }

  /**
   * @see Platform2
   */
  @Deprecated
  public static String productName() {
    return productName(get());
  }

  /**
   * @see Platform2
   */
  @Deprecated
  public static String productName(Platform platform) {
    if (platform == null) {
      return null;
    }
    return platform.getProductName();
  }

  private final String productName;
  private final String productQualifier;
  private final Set<Feature> features;
  private final Orientation defaultOrientation;

  Platform(String productName, String productQualifier, Orientation defaultOrientation, Feature... features) {
    this.productName = productName;
    this.productQualifier = productQualifier;
    this.defaultOrientation = defaultOrientation;
    this.features = Collections.unmodifiableSet(new HashSet<Feature>(Arrays.asList(features)));
  }

  /**
   * @see Platform2
   */
  @Deprecated
  public boolean isSupportsFeature(Feature feature) {
    return features.contains(feature);
  }

  /**
   * @see Platform2
   */
  @Deprecated
  public Orientation getDefaultOrientation() {
    return defaultOrientation;
  }

  /**
   * @see Platform2
   */
  @Deprecated
  public String getProductName() {
    return productName;
  }

  /**
   * @see Platform2
   */
  @Deprecated
  public String getProductQualifier() {
    return productQualifier;
  }

}
