package com.clover.connector.sdk.v3;

/**
 * Copyright (C) 2017 Clover Network, Inc.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class CardEntryMethods {
  private static int KIOSK_CARD_ENTRY_METHODS			= 1 << 15;

  public static int CARD_ENTRY_METHOD_MAG_STRIPE = 0b0001 | 0b000100000000 | CardEntryMethods.KIOSK_CARD_ENTRY_METHODS; // 33026
  public static int CARD_ENTRY_METHOD_ICC_CONTACT = 0b0010 | 0b001000000000 | CardEntryMethods.KIOSK_CARD_ENTRY_METHODS; // 33282
  public static int CARD_ENTRY_METHOD_NFC_CONTACTLESS	= 0b0100 | 0b010000000000 | CardEntryMethods.KIOSK_CARD_ENTRY_METHODS; // 33796
  public static int CARD_ENTRY_METHOD_MANUAL = 0b1000 | 0b100000000000 | CardEntryMethods.KIOSK_CARD_ENTRY_METHODS; // 34824

  public static int DEFAULT =
  CardEntryMethods.CARD_ENTRY_METHOD_MAG_STRIPE |
  CardEntryMethods.CARD_ENTRY_METHOD_ICC_CONTACT |
  CardEntryMethods.CARD_ENTRY_METHOD_NFC_CONTACTLESS; // | CARD_ENTRY_METHOD_MANUAL;

  public static int ALL =
  CardEntryMethods.CARD_ENTRY_METHOD_MAG_STRIPE |
  CardEntryMethods.CARD_ENTRY_METHOD_ICC_CONTACT |
  CardEntryMethods.CARD_ENTRY_METHOD_NFC_CONTACTLESS |
  CardEntryMethods.CARD_ENTRY_METHOD_MANUAL;
}