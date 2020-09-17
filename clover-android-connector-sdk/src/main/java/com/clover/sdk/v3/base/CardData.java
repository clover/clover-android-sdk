/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */

/*
 * Copyright (C) 2019 Clover Network, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.clover.sdk.v3.base;

import com.clover.sdk.GenericClient;
import com.clover.sdk.GenericParcelable;

/**
 * This is an auto-generated Clover data object.
 * <p>
 * <h3>Fields</h3>
 * <ul>
 * <li>{@link #getTrack1 track1}</li>
 * <li>{@link #getTrack2 track2}</li>
 * <li>{@link #getTrack3 track3}</li>
 * <li>{@link #getEncrypted encrypted}</li>
 * <li>{@link #getMaskedTrack1 maskedTrack1}</li>
 * <li>{@link #getMaskedTrack2 maskedTrack2}</li>
 * <li>{@link #getMaskedTrack3 maskedTrack3}</li>
 * <li>{@link #getPan pan}</li>
 * <li>{@link #getCardholderName cardholderName}</li>
 * <li>{@link #getFirstName firstName}</li>
 * <li>{@link #getLastName lastName}</li>
 * <li>{@link #getExp exp}</li>
 * <li>{@link #getLast4 last4}</li>
 * <li>{@link #getFirst6 first6}</li>
 * </ul>
 */
@SuppressWarnings("all")
public class CardData extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {

  /**
   * The track1 data from the card.
   */
  public java.lang.String getTrack1() {
    return genClient.cacheGet(CacheKey.track1);
  }

  /**
   * The track2 data from the card.
   */
  public java.lang.String getTrack2() {
    return genClient.cacheGet(CacheKey.track2);
  }

  /**
   * The track3 data from the card.
   */
  public java.lang.String getTrack3() {
    return genClient.cacheGet(CacheKey.track3);
  }

  /**
   * Indicates if the data is encrypted.
   */
  public java.lang.Boolean getEncrypted() {
    return genClient.cacheGet(CacheKey.encrypted);
  }

  /**
   * The track1 data from the card.
   */
  public java.lang.String getMaskedTrack1() {
    return genClient.cacheGet(CacheKey.maskedTrack1);
  }

  /**
   * The track2 data from the card.
   */
  public java.lang.String getMaskedTrack2() {
    return genClient.cacheGet(CacheKey.maskedTrack2);
  }

  /**
   * The track3 data from the card.
   */
  public java.lang.String getMaskedTrack3() {
    return genClient.cacheGet(CacheKey.maskedTrack3);
  }

  /**
   * The pan data from the card.
   */
  public java.lang.String getPan() {
    return genClient.cacheGet(CacheKey.pan);
  }

  /**
   * The cardholderName data from the card.
   */
  public java.lang.String getCardholderName() {
    return genClient.cacheGet(CacheKey.cardholderName);
  }

  /**
   * The firstName data from the card.
   */
  public java.lang.String getFirstName() {
    return genClient.cacheGet(CacheKey.firstName);
  }

  /**
   * The lastName data from the card.
   */
  public java.lang.String getLastName() {
    return genClient.cacheGet(CacheKey.lastName);
  }

  /**
   * The exp data from the card.
   */
  public java.lang.String getExp() {
    return genClient.cacheGet(CacheKey.exp);
  }

  /**
   * The last4 data from the card.
   */
  public java.lang.String getLast4() {
    return genClient.cacheGet(CacheKey.last4);
  }

  /**
   * The first6 data from the card.
   */
  public java.lang.String getFirst6() {
    return genClient.cacheGet(CacheKey.first6);
  }




  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    track1
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    track2
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    track3
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    encrypted
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.Boolean.class)),
    maskedTrack1
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    maskedTrack2
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    maskedTrack3
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    pan
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    cardholderName
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    firstName
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    lastName
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    exp
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    last4
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
    first6
        (com.clover.sdk.extractors.BasicExtractionStrategy.instance(java.lang.String.class)),
      ;

    private final com.clover.sdk.extractors.ExtractionStrategy extractionStrategy;

    private CacheKey(com.clover.sdk.extractors.ExtractionStrategy s) {
      extractionStrategy = s;
    }

    @Override
    public com.clover.sdk.extractors.ExtractionStrategy getExtractionStrategy() {
      return extractionStrategy;
    }
  }

  private final GenericClient<CardData> genClient;

  /**
   * Constructs a new empty instance.
   */
  public CardData() {
    genClient = new GenericClient<CardData>(this);
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  /**
   * Constructs a new empty instance.
   */
  protected CardData(boolean noInit) {
    genClient = null;
  }

  /**
   * Constructs a new instance from the given JSON String.
   */
  public CardData(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  /**
   * Construct a new instance backed by the given JSONObject, the parameter is not copied so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public CardData(org.json.JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  /**
   * Constructs a new instance that is a deep copy of the source instance. It does not copy the bundle or changelog.
   */
  public CardData(CardData src) {
    this();
    if (src.genClient.getJsonObject() != null) {
      genClient.setJsonObject(com.clover.sdk.v3.JsonHelper.deepCopy(src.genClient.getJSONObject()));
    }
  }

  /**
   * Returns the internal JSONObject backing this instance, the return value is not a copy so changes to it will be
   * reflected in this instance and vice-versa.
   */
  public org.json.JSONObject getJSONObject() {
    return genClient.getJSONObject();
  }

  @Override
  public void validate() {
  }

  /** Checks whether the 'track1' field is set and is not null */
  public boolean isNotNullTrack1() {
    return genClient.cacheValueIsNotNull(CacheKey.track1);
  }

  /** Checks whether the 'track2' field is set and is not null */
  public boolean isNotNullTrack2() {
    return genClient.cacheValueIsNotNull(CacheKey.track2);
  }

  /** Checks whether the 'track3' field is set and is not null */
  public boolean isNotNullTrack3() {
    return genClient.cacheValueIsNotNull(CacheKey.track3);
  }

  /** Checks whether the 'encrypted' field is set and is not null */
  public boolean isNotNullEncrypted() {
    return genClient.cacheValueIsNotNull(CacheKey.encrypted);
  }

  /** Checks whether the 'maskedTrack1' field is set and is not null */
  public boolean isNotNullMaskedTrack1() {
    return genClient.cacheValueIsNotNull(CacheKey.maskedTrack1);
  }

  /** Checks whether the 'maskedTrack2' field is set and is not null */
  public boolean isNotNullMaskedTrack2() {
    return genClient.cacheValueIsNotNull(CacheKey.maskedTrack2);
  }

  /** Checks whether the 'maskedTrack3' field is set and is not null */
  public boolean isNotNullMaskedTrack3() {
    return genClient.cacheValueIsNotNull(CacheKey.maskedTrack3);
  }

  /** Checks whether the 'pan' field is set and is not null */
  public boolean isNotNullPan() {
    return genClient.cacheValueIsNotNull(CacheKey.pan);
  }

  /** Checks whether the 'cardholderName' field is set and is not null */
  public boolean isNotNullCardholderName() {
    return genClient.cacheValueIsNotNull(CacheKey.cardholderName);
  }

  /** Checks whether the 'firstName' field is set and is not null */
  public boolean isNotNullFirstName() {
    return genClient.cacheValueIsNotNull(CacheKey.firstName);
  }

  /** Checks whether the 'lastName' field is set and is not null */
  public boolean isNotNullLastName() {
    return genClient.cacheValueIsNotNull(CacheKey.lastName);
  }

  /** Checks whether the 'exp' field is set and is not null */
  public boolean isNotNullExp() {
    return genClient.cacheValueIsNotNull(CacheKey.exp);
  }

  /** Checks whether the 'last4' field is set and is not null */
  public boolean isNotNullLast4() {
    return genClient.cacheValueIsNotNull(CacheKey.last4);
  }

  /** Checks whether the 'first6' field is set and is not null */
  public boolean isNotNullFirst6() {
    return genClient.cacheValueIsNotNull(CacheKey.first6);
  }



  /** Checks whether the 'track1' field has been set, however the value could be null */
  public boolean hasTrack1() {
    return genClient.cacheHasKey(CacheKey.track1);
  }

  /** Checks whether the 'track2' field has been set, however the value could be null */
  public boolean hasTrack2() {
    return genClient.cacheHasKey(CacheKey.track2);
  }

  /** Checks whether the 'track3' field has been set, however the value could be null */
  public boolean hasTrack3() {
    return genClient.cacheHasKey(CacheKey.track3);
  }

  /** Checks whether the 'encrypted' field has been set, however the value could be null */
  public boolean hasEncrypted() {
    return genClient.cacheHasKey(CacheKey.encrypted);
  }

  /** Checks whether the 'maskedTrack1' field has been set, however the value could be null */
  public boolean hasMaskedTrack1() {
    return genClient.cacheHasKey(CacheKey.maskedTrack1);
  }

  /** Checks whether the 'maskedTrack2' field has been set, however the value could be null */
  public boolean hasMaskedTrack2() {
    return genClient.cacheHasKey(CacheKey.maskedTrack2);
  }

  /** Checks whether the 'maskedTrack3' field has been set, however the value could be null */
  public boolean hasMaskedTrack3() {
    return genClient.cacheHasKey(CacheKey.maskedTrack3);
  }

  /** Checks whether the 'pan' field has been set, however the value could be null */
  public boolean hasPan() {
    return genClient.cacheHasKey(CacheKey.pan);
  }

  /** Checks whether the 'cardholderName' field has been set, however the value could be null */
  public boolean hasCardholderName() {
    return genClient.cacheHasKey(CacheKey.cardholderName);
  }

  /** Checks whether the 'firstName' field has been set, however the value could be null */
  public boolean hasFirstName() {
    return genClient.cacheHasKey(CacheKey.firstName);
  }

  /** Checks whether the 'lastName' field has been set, however the value could be null */
  public boolean hasLastName() {
    return genClient.cacheHasKey(CacheKey.lastName);
  }

  /** Checks whether the 'exp' field has been set, however the value could be null */
  public boolean hasExp() {
    return genClient.cacheHasKey(CacheKey.exp);
  }

  /** Checks whether the 'last4' field has been set, however the value could be null */
  public boolean hasLast4() {
    return genClient.cacheHasKey(CacheKey.last4);
  }

  /** Checks whether the 'first6' field has been set, however the value could be null */
  public boolean hasFirst6() {
    return genClient.cacheHasKey(CacheKey.first6);
  }


  /**
   * Sets the field 'track1'.
   */
  public CardData setTrack1(java.lang.String track1) {
    return genClient.setOther(track1, CacheKey.track1);
  }

  /**
   * Sets the field 'track2'.
   */
  public CardData setTrack2(java.lang.String track2) {
    return genClient.setOther(track2, CacheKey.track2);
  }

  /**
   * Sets the field 'track3'.
   */
  public CardData setTrack3(java.lang.String track3) {
    return genClient.setOther(track3, CacheKey.track3);
  }

  /**
   * Sets the field 'encrypted'.
   */
  public CardData setEncrypted(java.lang.Boolean encrypted) {
    return genClient.setOther(encrypted, CacheKey.encrypted);
  }

  /**
   * Sets the field 'maskedTrack1'.
   */
  public CardData setMaskedTrack1(java.lang.String maskedTrack1) {
    return genClient.setOther(maskedTrack1, CacheKey.maskedTrack1);
  }

  /**
   * Sets the field 'maskedTrack2'.
   */
  public CardData setMaskedTrack2(java.lang.String maskedTrack2) {
    return genClient.setOther(maskedTrack2, CacheKey.maskedTrack2);
  }

  /**
   * Sets the field 'maskedTrack3'.
   */
  public CardData setMaskedTrack3(java.lang.String maskedTrack3) {
    return genClient.setOther(maskedTrack3, CacheKey.maskedTrack3);
  }

  /**
   * Sets the field 'pan'.
   */
  public CardData setPan(java.lang.String pan) {
    return genClient.setOther(pan, CacheKey.pan);
  }

  /**
   * Sets the field 'cardholderName'.
   */
  public CardData setCardholderName(java.lang.String cardholderName) {
    return genClient.setOther(cardholderName, CacheKey.cardholderName);
  }

  /**
   * Sets the field 'firstName'.
   */
  public CardData setFirstName(java.lang.String firstName) {
    return genClient.setOther(firstName, CacheKey.firstName);
  }

  /**
   * Sets the field 'lastName'.
   */
  public CardData setLastName(java.lang.String lastName) {
    return genClient.setOther(lastName, CacheKey.lastName);
  }

  /**
   * Sets the field 'exp'.
   */
  public CardData setExp(java.lang.String exp) {
    return genClient.setOther(exp, CacheKey.exp);
  }

  /**
   * Sets the field 'last4'.
   */
  public CardData setLast4(java.lang.String last4) {
    return genClient.setOther(last4, CacheKey.last4);
  }

  /**
   * Sets the field 'first6'.
   */
  public CardData setFirst6(java.lang.String first6) {
    return genClient.setOther(first6, CacheKey.first6);
  }


  /** Clears the 'track1' field, the 'has' method for this field will now return false */
  public void clearTrack1() {
    genClient.clear(CacheKey.track1);
  }
  /** Clears the 'track2' field, the 'has' method for this field will now return false */
  public void clearTrack2() {
    genClient.clear(CacheKey.track2);
  }
  /** Clears the 'track3' field, the 'has' method for this field will now return false */
  public void clearTrack3() {
    genClient.clear(CacheKey.track3);
  }
  /** Clears the 'encrypted' field, the 'has' method for this field will now return false */
  public void clearEncrypted() {
    genClient.clear(CacheKey.encrypted);
  }
  /** Clears the 'maskedTrack1' field, the 'has' method for this field will now return false */
  public void clearMaskedTrack1() {
    genClient.clear(CacheKey.maskedTrack1);
  }
  /** Clears the 'maskedTrack2' field, the 'has' method for this field will now return false */
  public void clearMaskedTrack2() {
    genClient.clear(CacheKey.maskedTrack2);
  }
  /** Clears the 'maskedTrack3' field, the 'has' method for this field will now return false */
  public void clearMaskedTrack3() {
    genClient.clear(CacheKey.maskedTrack3);
  }
  /** Clears the 'pan' field, the 'has' method for this field will now return false */
  public void clearPan() {
    genClient.clear(CacheKey.pan);
  }
  /** Clears the 'cardholderName' field, the 'has' method for this field will now return false */
  public void clearCardholderName() {
    genClient.clear(CacheKey.cardholderName);
  }
  /** Clears the 'firstName' field, the 'has' method for this field will now return false */
  public void clearFirstName() {
    genClient.clear(CacheKey.firstName);
  }
  /** Clears the 'lastName' field, the 'has' method for this field will now return false */
  public void clearLastName() {
    genClient.clear(CacheKey.lastName);
  }
  /** Clears the 'exp' field, the 'has' method for this field will now return false */
  public void clearExp() {
    genClient.clear(CacheKey.exp);
  }
  /** Clears the 'last4' field, the 'has' method for this field will now return false */
  public void clearLast4() {
    genClient.clear(CacheKey.last4);
  }
  /** Clears the 'first6' field, the 'has' method for this field will now return false */
  public void clearFirst6() {
    genClient.clear(CacheKey.first6);
  }


  /**
   * Returns true if this instance has any changes.
   */
  public boolean containsChanges() {
    return genClient.containsChanges();
  }

  /**
   * Reset the log of changes made to this instance, calling copyChanges() after this would return an empty instance.
   */
  public void resetChangeLog() {
    genClient.resetChangeLog();
  }

  /**
   * Create a copy of this instance that contains only fields that were set after the constructor was called.
   */
  public CardData copyChanges() {
    CardData copy = new CardData();
    copy.mergeChanges(this);
    copy.resetChangeLog();
    return copy;
  }

  /**
   * Copy all the changed fields from the given source to this instance.
   */
  public void mergeChanges(CardData src) {
    if (src.genClient.getChangeLog() != null) {
      genClient.mergeChanges(new CardData(src).getJSONObject(), src.genClient);
    }
  }

  public static final android.os.Parcelable.Creator<CardData> CREATOR = new android.os.Parcelable.Creator<CardData>() {
    @Override
    public CardData createFromParcel(android.os.Parcel in) {
      CardData instance = new CardData(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
      instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
      instance.genClient.setChangeLog(in.readBundle());
      return instance;
    }

    @Override
    public CardData[] newArray(int size) {
      return new CardData[size];
    }
  };

  public static final com.clover.sdk.JSONifiable.Creator<CardData> JSON_CREATOR = new com.clover.sdk.JSONifiable.Creator<CardData>() {
    public Class<CardData> getCreatedClass() {
      return CardData.class;
    }

    @Override
    public CardData create(org.json.JSONObject jsonObject) {
      return new CardData(jsonObject);
    }
  };

  public interface Constraints {
    public static final boolean TRACK1_IS_REQUIRED = false;
    public static final boolean TRACK2_IS_REQUIRED = false;
    public static final boolean TRACK3_IS_REQUIRED = false;
    public static final boolean ENCRYPTED_IS_REQUIRED = false;
    public static final boolean MASKEDTRACK1_IS_REQUIRED = false;
    public static final boolean MASKEDTRACK2_IS_REQUIRED = false;
    public static final boolean MASKEDTRACK3_IS_REQUIRED = false;
    public static final boolean PAN_IS_REQUIRED = false;
    public static final boolean CARDHOLDERNAME_IS_REQUIRED = false;
    public static final boolean FIRSTNAME_IS_REQUIRED = false;
    public static final boolean LASTNAME_IS_REQUIRED = false;
    public static final boolean EXP_IS_REQUIRED = false;
    public static final boolean LAST4_IS_REQUIRED = false;
    public static final boolean FIRST6_IS_REQUIRED = false;
  }

}
