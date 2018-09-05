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
 * Provides information about the device currently being used. Please keep usages of this class to
 * a minimum. Please prefer standard Android APIs such as {@link android.os.PowerManager},
 * {@link android.net.ConnectivityManager} and {@link android.content.res.Configuration} to get
 * information about the device.
 */
public enum Platform {

  TF300T("Clover Hub", null, Orientation.LANDSCAPE),
  C100("Clover Station", null, Orientation.LANDSCAPE, Feature.ETHERNET, Feature.CUSTOMER_ROTATION, Feature.DEFAULT_EMPLOYEE),
  C200("Clover Mobile", null, Orientation.LANDSCAPE, Feature.CUSTOMER_MODE, Feature.SECURE_PAYMENTS, Feature.DEFAULT_EMPLOYEE, Feature.BATTERY, Feature.SECURE_TOUCH),
  C201("Clover Mobile", null, Orientation.LANDSCAPE, Feature.CUSTOMER_MODE, Feature.SECURE_PAYMENTS, Feature.MOBILE_DATA, Feature.DEFAULT_EMPLOYEE, Feature.BATTERY, Feature.SECURE_TOUCH),
  C300("Clover Mini", null, Orientation.LANDSCAPE, Feature.CUSTOMER_MODE, Feature.SECURE_PAYMENTS, Feature.DEFAULT_EMPLOYEE, Feature.ETHERNET, Feature.SECURE_TOUCH),
  C301("Clover Mini", null, Orientation.LANDSCAPE, Feature.CUSTOMER_MODE, Feature.SECURE_PAYMENTS, Feature.MOBILE_DATA, Feature.DEFAULT_EMPLOYEE, Feature.ETHERNET, Feature.SECURE_TOUCH),
  /** C400 is deprecated, and should only be set for devices with pre-PVT roms */
  @Deprecated
  C400("Clover Flex", null, Orientation.PORTRAIT, Feature.CUSTOMER_MODE, Feature.SECURE_PAYMENTS, Feature.MOBILE_DATA, Feature.DEFAULT_EMPLOYEE, Feature.BATTERY, Feature.ETHERNET, Feature.SECURE_TOUCH),
  /** C400U is deprecated, and should only be set for devices with pre-PVT roms */
  @Deprecated
  C400U("Clover Flex", null, Orientation.PORTRAIT, Feature.CUSTOMER_MODE, Feature.SECURE_PAYMENTS, Feature.MOBILE_DATA, Feature.DEFAULT_EMPLOYEE, Feature.BATTERY, Feature.ETHERNET, Feature.SECURE_TOUCH),
  /** C400E is deprecated, and should only be set for devices with pre-PVT roms */
  @Deprecated
  C400E("Clover Flex", null, Orientation.PORTRAIT, Feature.CUSTOMER_MODE, Feature.SECURE_PAYMENTS, Feature.MOBILE_DATA, Feature.DEFAULT_EMPLOYEE, Feature.BATTERY, Feature.ETHERNET, Feature.SECURE_TOUCH),
  C401U("Clover Flex", null, Orientation.PORTRAIT, Feature.CUSTOMER_MODE, Feature.SECURE_PAYMENTS, Feature.MOBILE_DATA, Feature.DEFAULT_EMPLOYEE, Feature.BATTERY, Feature.ETHERNET, Feature.SECURE_TOUCH),
  C401E("Clover Flex", null, Orientation.PORTRAIT, Feature.CUSTOMER_MODE, Feature.SECURE_PAYMENTS, Feature.MOBILE_DATA, Feature.DEFAULT_EMPLOYEE, Feature.BATTERY, Feature.ETHERNET, Feature.SECURE_TOUCH),
  C401L("Clover Flex", null, Orientation.PORTRAIT, Feature.CUSTOMER_MODE, Feature.SECURE_PAYMENTS, Feature.MOBILE_DATA, Feature.DEFAULT_EMPLOYEE, Feature.BATTERY, Feature.ETHERNET, Feature.SECURE_TOUCH),
  C500("Clover Station", "2018 – Gen 2", Orientation.LANDSCAPE, Feature.CUSTOMER_MODE, Feature.SECURE_PAYMENTS, Feature.DEFAULT_EMPLOYEE, Feature.ETHERNET, Feature.CUSTOMER_FACING_EXTERNAL_DISPLAY, Feature.CUSTOMER_ROTATION),
  /** Currently not used */
  C550("Clover Station", "2018 – Gen 2", Orientation.LANDSCAPE, Feature.CUSTOMER_MODE, Feature.SECURE_PAYMENTS, Feature.DEFAULT_EMPLOYEE, Feature.ETHERNET),
  @Deprecated
  C302("Clover Mini", "2nd generation", Orientation.LANDSCAPE, Feature.CUSTOMER_MODE, Feature.SECURE_PAYMENTS, Feature.MOBILE_DATA, Feature.DEFAULT_EMPLOYEE, Feature.ETHERNET, Feature.SECURE_TOUCH),
  C302U("Clover Mini", "2nd generation", Orientation.LANDSCAPE, Feature.CUSTOMER_MODE, Feature.SECURE_PAYMENTS, Feature.MOBILE_DATA, Feature.DEFAULT_EMPLOYEE, Feature.ETHERNET, Feature.SECURE_TOUCH),
  C302E("Clover Mini", "2nd generation", Orientation.LANDSCAPE, Feature.CUSTOMER_MODE, Feature.SECURE_PAYMENTS, Feature.MOBILE_DATA, Feature.DEFAULT_EMPLOYEE, Feature.ETHERNET, Feature.SECURE_TOUCH),
  C302L("Clover Mini", "2nd generation", Orientation.LANDSCAPE, Feature.CUSTOMER_MODE, Feature.SECURE_PAYMENTS, Feature.MOBILE_DATA, Feature.DEFAULT_EMPLOYEE, Feature.ETHERNET, Feature.SECURE_TOUCH),
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
   * List of features that may be present on Clover devices. Non-Clover devices will return false
   * for all features.
   */
  public enum Feature {
    /**
     * Device supports the Clover Secure Payments application for taking Mag-stripe, EMV ICC and/or
     * NFC payments.
     */
    SECURE_PAYMENTS,
    /**
     * Device supports the {@link CustomerMode} class for checking, enabling and disabling
     * customer mode.
     */
    CUSTOMER_MODE,
    /**
     * Device includes cellular mobile data hardware.
     */
    MOBILE_DATA,
    /**
     * Device supports login by a "default employee" without a PIN. May or may not actually be
     * enabled, see {@link com.clover.sdk.v3.merchant.MerchantProperties} to check.
     */
    DEFAULT_EMPLOYEE,
    /**
     * Device designed for use on battery power.
     */
    BATTERY,
    /**
     * Device has an ethernet port or supports an ethernet port attachment (may or may not be
     * currently attached or connected).
     */
    ETHERNET,
    /**
     * Device has a secure touch screen.
     */
    SECURE_TOUCH,
    /**
     * Device supports customer facing external display.
     */
    CUSTOMER_FACING_EXTERNAL_DISPLAY,
    /**
     * Device supports merchant to customer rotation with landscape->portrait configuration change
     */
    CUSTOMER_ROTATION,
    ;
  }

  /**
   * List of secure processor platforms.
   */
  public enum SecureProcessorPlatform {
    BROADCOM,
    MAXIM,
  }

  /** The default orientation for this device, not the current orientation. */
  public enum Orientation {
    LANDSCAPE,
    PORTRAIT,
  }

  /**
   * Return an instance of the Platform class for this device, if not a Clover device this will
   * return {@link Platform#OTHER}.
   */
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
   * Returns true when running on Clover hardware.
   */
  public static boolean isClover() {
    return Build.MANUFACTURER.equals(CLOVER_MANUFACTURER);
  }

  /**
   * Consider using {@link #supportsFeature(Feature)} to check device capabilities, using
   * {@code android.util.DisplayMetrics} to check display size or using
   * {@code android.os.Build.VERSION.SDK_INT} to check Android API level instead for better
   * forward compatibility with new Clover hardware.
   */
  public static boolean isCloverStation() {
    return isCloverStation(get());
  }

  public static boolean isCloverStation(Platform platform) {
    return STATION.contains(platform);
  }

  /**
   * Consider using {@link #supportsFeature(Feature)} to check device capabilities, using
   * {@code android.util.DisplayMetrics} to check display size or using
   * {@code android.os.Build.VERSION.SDK_INT} to check Android API level instead for better
   * forward compatibility with new Clover hardware.
   */
  public static boolean isCloverMobile() {
    return isCloverMobile(get());
  }

  public static boolean isCloverMobile(Platform platform) {
    return MOBILE.contains(platform);
  }

  /**
   * Consider using {@link #supportsFeature(Feature)} to check device capabilities, using
   * {@code android.util.DisplayMetrics} to check display size or using
   * {@code android.os.Build.VERSION.SDK_INT} to check Android API level instead for better
   * forward compatibility with new Clover hardware.
   */
  public static boolean isCloverMini() {
    return isCloverMini(get());
  }

  public static boolean isCloverMini(Platform platform) {
    return MINI.contains(platform);
  }


  public static boolean isCloverMiniGen2() {
    return isCloverMiniGen2(get());
  }

  public static boolean isCloverMiniGen2(Platform platform) {
    return MINI_GEN2.contains(platform);
  }


  /**
   * @deprecated Use {@link #isCloverFlex()}.
   */
  @Deprecated
  public static boolean isCloverOne() {
    return isCloverFlex();
  }

  /**
   * Return the platform of the secure processor, null for devices that do not have a secure processor.
   */
  public static SecureProcessorPlatform getSecureProcessorPlatform() {
    return getSecureProcessorPlatform(get());
  }

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
   * Consider using {@link #supportsFeature(Feature)} to check device capabilities, using
   * {@code android.util.DisplayMetrics} to check display size or using
   * {@code android.os.Build.VERSION.SDK_INT} to check Android API level instead for better
   * forward compatibility with new Clover hardware.
   */
  public static boolean isCloverFlex() {
    return isCloverFlex(get());
  }

  public static boolean isCloverFlex(Platform platform) {
    return FLEX.contains(platform);
  }

  /**
   * Consider using {@link #supportsFeature(Feature)} to check device capabilities, using
   * {@code android.util.DisplayMetrics} to check display size or using
   * {@code android.os.Build.VERSION.SDK_INT} to check Android API level instead for better
   * forward compatibility with new Clover hardware.
   */
  public static boolean isCloverStation2018() {
    return  isCloverStation2018(get());
  }

  public static boolean isCloverStation2018(Platform platform) {
    return  STATION_2018.contains(platform);
  }

  /**
   * @deprecated Use {@link #isCloverStation2018()}.
   */
  @Deprecated
  public static boolean isCloverGoldenOak() {
    return isCloverStation2018();
  }

  /**
   * @deprecated Use {@link #supportsFeature(Feature)} with an argument of {@link Feature#MOBILE_DATA}.
   */
  @Deprecated
  public static boolean is3g() {
    return get().isSupportsFeature(Feature.MOBILE_DATA);
  }

  /**
   * @deprecated Use {@link #supportsFeature(Feature)} with an argument of {@link Feature#SECURE_PAYMENTS}.
   */
  @Deprecated
  public static boolean isSecureBoardPresent() {
    return get().isSupportsFeature(Feature.SECURE_PAYMENTS);
  }

  /**
   * @deprecated Use {@link #defaultOrientation()}.
   */
  @Deprecated
  public static boolean isDefaultPortrait() {
    return get().getDefaultOrientation() == Orientation.PORTRAIT;
  }

  /**
   * Indicates if the device supports getting/setting CustomerMode state
   * via the {@link CustomerMode} API.
   *
   * @return true if the device supports it, false otherwise
   * @deprecated Use {@link #supportsFeature(Feature)} with an argument of {@link Feature#CUSTOMER_MODE}.
   */
  @Deprecated
  public static boolean isSupportsCustomerMode() {
    return get().isSupportsFeature(Feature.CUSTOMER_MODE);
  }

  /**
   * Return true if the specified feature is supported on this device. Always returns
   * false for {@link Platform#OTHER}.
   */
  public static boolean supportsFeature(Feature f) {
    return supportsFeature(get(), f);
  }

  public static boolean supportsFeature(Platform platform, Feature f) {
    if (platform == null) {
      return false;
    }
    return platform.isSupportsFeature(f);
  }

  /**
   * Get the default orientation under which this device is normally used.
   *
   * @return the default orientation for Clover devices, undefined for non-Clover devices
   */
  public static Orientation defaultOrientation() {
    return defaultOrientation(get());
  }

  public static Orientation defaultOrientation(Platform platform) {
    if (platform == null) {
      return null;
    }
    return platform.getDefaultOrientation();
  }

  /**
   * Get the product details for this device if Clover, null if details aren't defined for the device type.
   */
  public static String productQualifier() {
    return productQualifier(get());
  }

  public static String productQualifier(Platform platform) {
    if (platform == null) {
      return null;
    }
    return platform.getProductQualifier();
  }

  /**
   * Get the English product name for this device if Clover, null if not a Clover device.
   */
  public static String productName() {
    return productName(get());
  }

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
   * Return true if the specified feature is supported on this Platform instance. Always returns
   * false for {@link Platform#OTHER}.
   */
  public boolean isSupportsFeature(Feature feature) {
    return features.contains(feature);
  }

  /**
   * Get the default orientation for this device.
   *
   * @return the default orientation for Clover devices, null for non-Clover devices
   */
  public Orientation getDefaultOrientation() {
    return defaultOrientation;
  }

  /**
   * Get the English product name for this device if Clover, null if not a Clover device.
   *
   * @return The Clover product name
   */
  public String getProductName() {
    return productName;
  }

  /**
   * Get the product details for this device if Clover, null if details aren't defined for the device type
   *
   * @return The Clover product details
   */
  public String getProductQualifier() {
    return productQualifier;
  }
}
