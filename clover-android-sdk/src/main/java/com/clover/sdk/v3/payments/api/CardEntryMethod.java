package com.clover.sdk.v3.payments.api;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum CardEntryMethod {
  /**
   * Constant referring to Magnetic Stripe Card reader
   */
  MAG_STRIPE("MAG_STRIPE"),
  /**
   * Constant referring to Chip Card reader
   */
  CHIP("CHIP"),
  /**
   * Constant referring to Contactless NFC Card reader
   */
  NFC("NFC"),
  /**
   * Constant referring to manually entering card information
   */
  MANUAL("MANUAL");

  private String value;

  CardEntryMethod(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }


  private static Set<CardEntryMethod> ALL = new HashSet<>(Arrays.asList(CardEntryMethod.CHIP, CardEntryMethod.NFC, CardEntryMethod.MAG_STRIPE, CardEntryMethod.MANUAL));
  private static Set<CardEntryMethod> CARD_READERS = new HashSet<>(Arrays.asList(CardEntryMethod.CHIP, CardEntryMethod.NFC, CardEntryMethod.MAG_STRIPE));
  private static Set<CardEntryMethod> MANUAL_ONLY = new HashSet<>(Arrays.asList(CardEntryMethod.MANUAL));

  /**
   * @return - a set of card entry options including CHIP, NFC and MAG_STRIPE
   */
  @SuppressWarnings("unused")
  public static Set<CardEntryMethod> CardReaders() {
    return CARD_READERS;
  }

  /**
   * @return - a set containing only the MANUAL card entry option
   */
  @SuppressWarnings("unused")
  public static Set<CardEntryMethod> Manual() {
    return MANUAL_ONLY;
  }

  /**
   *
   * @return - a set containing all card entry options (CHIP, NFC, MAG_STRIPE and MANUAL)
   */
  @SuppressWarnings("unused")
  public static Set<CardEntryMethod> All() {
    return ALL;
  }
}